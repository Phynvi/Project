package com.overload.net.login;


import com.overload.game.model.PlayerModes;
import com.overload.game.model.rights.Right;


/**
 * The packet that contains information about a players login attempt.
 *
 * @author Vult-R
 */
public final class LoginResponsePacket {

    /**
     * The login response that was indicated.
     */
    private final int response;

    /**
     * The rights of the player logging in.
     */

    private final Right rights;
    
    /**
     * The mode of the player logging in.
     */
    private final PlayerModes mode;


    /**
     * Creates a new {@link LoginResponsePacket}.
     *
     * @param response The response that was indicated.
     * @param rights   The rights of the player logging in.
     * @param flagged  The flag that indicates a player was flagged.
     */
    public LoginResponsePacket(int response, Right rights, PlayerModes mode) {
        this.response = response;
        this.rights = rights;
        this.mode = mode;
    }

    public LoginResponsePacket(int response) {
        this.response = response;
        this.rights = Right.PLAYER;
        this.mode = PlayerModes.NORMAL;
    }

    public int getResponse() {
        return response;
    }

    public Right getRights() {
        return rights;
    }
    
    public PlayerModes getModes() {
        return mode;
    }
}

