package com.overload.net.login;

import java.util.Date;
import java.sql.Timestamp;
import java.time.Instant;

import com.jcabi.jdbc.JdbcSession;
import com.overload.Server;
import com.overload.game.GameConstants;
import com.overload.game.World;
import com.overload.game.entity.impl.player.Player;
import com.overload.game.entity.impl.player.PlayerLoading;
import com.overload.game.model.rights.Right;
import com.overload.net.codec.LoginResponse;
import com.overload.net.security.BCrypt;
import com.overload.util.Misc;
import com.overload.util.PlayerPunishment;

public class LoginEvaluated {

	public static LoginResponse authenticatedForumUser(Player player,boolean isEmail) {
		final String username = player.getUsername();
		try {
			final LoginResponse response = new JdbcSession(GameConstants.fs.getConnection())
					.sql(isEmail ? 
					"SELECT email,member_id, member_group_id, mgroup_others, members_pass_hash, name, temp_ban FROM core_members WHERE UPPER(email) = ?" : 
					"SELECT email,member_id, member_group_id, mgroup_others, members_pass_hash, temp_ban FROM core_members WHERE UPPER(name) = ?")
					.set(username.toUpperCase())
					.select((rset, stmt) -> {
						if (rset.next()) {
							final String fullemail = rset.getString(1);
							final int memberId = rset.getInt(2);
							final int right = rset.getInt(3);
							final String passwordHash = rset.getString(5);
							final String forumUsername = isEmail ? rset.getString(6) : username;
							final long unixTime = rset.getLong(isEmail ? 7 : 6);
							if (isBanned(unixTime)) {
								return LoginResponse.ACCOUNT_DISABLED;
							}
							if (passwordHash.isEmpty()) {
								return LoginResponse.INVALID_CREDENTIALS;
							} else if (BCrypt.checkpw(player.getPassword(),passwordHash)) {
								player.setMemberId(memberId);
								player.setUsername(forumUsername);
								player.setPassword(passwordHash);
								player.setEmail(fullemail);
								
								String playerRights = Right.forForumId(right).toString().replace(" ", "_");
								player.getRights().setPrimary(Right.valueOf(playerRights));
								
								player.getPacketSender().sendRights();

								return LoginResponse.NORMAL;
							} else {
								return LoginResponse.INVALID_CREDENTIALS;
							}
						}
						return LoginResponse.FORUM_REGISTRATION;
					});
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return LoginResponse.LOGIN_SERVER_OFFLINE;
	}

	private static boolean isBanned(long unixTime) {
		if (unixTime == 0) {
			return false;
		} else if (unixTime == -1) {
			return true;
		}
		final Date date = Date.from(Instant.ofEpochSecond(unixTime));
		final Date currentDate = Date.from(Instant.now());
		return date.after(currentDate);
	}

	public static final int evaluate(Player player, LoginDetailsMessage msg) {
		final String username = player.getUsername();
		final boolean isEmail = username.contains("@");

		if (World.getPlayers().isFull()) {
			return LoginResponse.WORLD_FULL.getOpcode();
		}

		if (Server.isUpdating()) {
			return LoginResponse.GAME_UPDATED.getOpcode();
		}

		if (player.getUsername().startsWith(" ") || player.getUsername().endsWith(" ") || !Misc.isValidName(player.getUsername())) {
			return LoginResponse.INVALID_CREDENTIALS_COMBINATION.getOpcode();
		}

		if (World.getPlayerByName(player.getUsername()).isPresent()) {
			return LoginResponse.ACCOUNT_ONLINE.getOpcode();
		}

		if (PlayerPunishment.IPBanned(msg.getHost())) {
			return LoginResponse.INVALID_EMAIL.getOpcode();
		}

		if (player.VPSEnvironment()) {
			final int response = authenticatedForumUser(player, isEmail).getOpcode();
			if (response != LoginResponse.NORMAL.getOpcode()) {
				return response;
			}
		}
		
		// Attempt to load the character file..
        int playerLoadingResponse = PlayerLoading.getResult(player);

        // New player?
        if (playerLoadingResponse == LoginResponse.NO_RESPONSE.getOpcode()) {
            player.setNewPlayer(true);
            player.setCreationDate(new Timestamp(new Date().getTime()));
            playerLoadingResponse = LoginResponse.NORMAL.getOpcode();
        }

		return playerLoadingResponse;
	}

}
