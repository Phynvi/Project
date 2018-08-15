package com.overload;

import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.CRC32;
import javax.swing.JFrame;
import com.overload.cache.FileArchive;
import com.overload.cache.FileStore;
import com.overload.cache.FileStore.Store;
import com.overload.cache.anim.Animation;
import com.overload.cache.anim.Frame;
import com.overload.cache.anim.Graphic;
import com.overload.cache.config.VariableBits;
import com.overload.cache.config.VariablePlayer;
import com.overload.cache.def.FloorDefinition;
import com.overload.cache.def.ItemDefinition;
import com.overload.cache.def.NpcDefinition;
import com.overload.cache.def.ObjectDefinition;
import com.overload.cache.def.SpriteCache;
import com.overload.cache.graphics.AnimatedSprite;
import com.overload.cache.graphics.DropdownMenu;
import com.overload.cache.graphics.FadingScreen;
import com.overload.cache.graphics.FontArchiver;
import com.overload.cache.graphics.GameFont;
import com.overload.cache.graphics.IndexedImage;
import com.overload.cache.graphics.RSFont;
import com.overload.cache.graphics.Slider;
import com.overload.cache.graphics.Sprite;
import com.overload.cache.graphics.widget.Bank;
import com.overload.cache.graphics.widget.SettingsWidget;
import com.overload.cache.graphics.widget.SpellbookSorting;
import com.overload.cache.graphics.widget.Widget;
import com.overload.cache.graphics.widget.Bank.BankTabShow;
import com.overload.collection.Deque;
import com.overload.collection.Linkable;
import com.overload.draw.Console;
import com.overload.draw.LoadingCircle;
import com.overload.draw.ProducingGraphicsBuffer;
import com.overload.draw.Rasterizer2D;
import com.overload.draw.Rasterizer3D;
import com.overload.draw.ScriptManager;
import com.overload.draw.chatbox.ChatboxDrawing;
import com.overload.draw.chatbox.hierarchy.TeleportChatbox;
import com.overload.draw.skillorbs.SkillOrbs;
import com.overload.entity.GameObject;
import com.overload.entity.Item;
import com.overload.entity.Mob;
import com.overload.entity.Npc;
import com.overload.entity.Player;
import com.overload.entity.Renderable;
import com.overload.entity.model.IdentityKit;
import com.overload.entity.model.Model;
import com.overload.io.Buffer;
import com.overload.io.jaggrab.JagGrab;
import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;
import com.overload.io.packets.outgoing.impl.AddFriend;
import com.overload.io.packets.outgoing.impl.AddIgnore;
import com.overload.io.packets.outgoing.impl.AttackNpc;
import com.overload.io.packets.outgoing.impl.BankTabCreation;
import com.overload.io.packets.outgoing.impl.BasicPing;
import com.overload.io.packets.outgoing.impl.ChangeAppearance;
import com.overload.io.packets.outgoing.impl.ChatSettings;
import com.overload.io.packets.outgoing.impl.ChatboxDuel;
import com.overload.io.packets.outgoing.impl.ClickButton;
import com.overload.io.packets.outgoing.impl.ClickButtonAction;
import com.overload.io.packets.outgoing.impl.CloseInterface;
import com.overload.io.packets.outgoing.impl.Command;
import com.overload.io.packets.outgoing.impl.DeleteFriend;
import com.overload.io.packets.outgoing.impl.DeleteIgnore;
import com.overload.io.packets.outgoing.impl.DropItem;
import com.overload.io.packets.outgoing.impl.EnterAmount;
import com.overload.io.packets.outgoing.impl.EquipItem;
import com.overload.io.packets.outgoing.impl.ExamineItem;
import com.overload.io.packets.outgoing.impl.ExamineNpc;
import com.overload.io.packets.outgoing.impl.FinalizedRegionChange;
import com.overload.io.packets.outgoing.impl.FollowPlayer;
import com.overload.io.packets.outgoing.impl.ItemContainerOption1;
import com.overload.io.packets.outgoing.impl.ItemContainerOption2;
import com.overload.io.packets.outgoing.impl.ItemContainerOption3;
import com.overload.io.packets.outgoing.impl.ItemContainerOption4;
import com.overload.io.packets.outgoing.impl.ItemContainerOption5;
import com.overload.io.packets.outgoing.impl.ItemOnGroundItem;
import com.overload.io.packets.outgoing.impl.ItemOnItem;
import com.overload.io.packets.outgoing.impl.ItemOnNpc;
import com.overload.io.packets.outgoing.impl.ItemOnObject;
import com.overload.io.packets.outgoing.impl.ItemOnPlayer;
import com.overload.io.packets.outgoing.impl.ItemOption2;
import com.overload.io.packets.outgoing.impl.ItemOption3;
import com.overload.io.packets.outgoing.impl.MagicOnGroundItem;
import com.overload.io.packets.outgoing.impl.MagicOnItem;
import com.overload.io.packets.outgoing.impl.MagicOnNpc;
import com.overload.io.packets.outgoing.impl.MagicOnPlayer;
import com.overload.io.packets.outgoing.impl.NextDialogue;
import com.overload.io.packets.outgoing.impl.NpcOption1;
import com.overload.io.packets.outgoing.impl.NpcOption2;
import com.overload.io.packets.outgoing.impl.NpcOption3;
import com.overload.io.packets.outgoing.impl.NpcOption4;
import com.overload.io.packets.outgoing.impl.ObjectOption1;
import com.overload.io.packets.outgoing.impl.ObjectOption2;
import com.overload.io.packets.outgoing.impl.ObjectOption3;
import com.overload.io.packets.outgoing.impl.ObjectOption4;
import com.overload.io.packets.outgoing.impl.ObjectOption5;
import com.overload.io.packets.outgoing.impl.PickupItem;
import com.overload.io.packets.outgoing.impl.PlayerAttackOption;
import com.overload.io.packets.outgoing.impl.PlayerInactive;
import com.overload.io.packets.outgoing.impl.PlayerOption1;
import com.overload.io.packets.outgoing.impl.RegionChange;
import com.overload.io.packets.outgoing.impl.SecondGroundItemOption;
import com.overload.io.packets.outgoing.impl.SendSyntax;
import com.overload.io.packets.outgoing.impl.SpawnTabClick;
import com.overload.io.packets.outgoing.impl.SpecialAttack;
import com.overload.io.packets.outgoing.impl.SwitchItemSlot;
import com.overload.io.packets.outgoing.impl.TradePlayer;
import com.overload.io.packets.outgoing.impl.UpdatePlane;
import com.overload.io.packets.outgoing.impl.UseItem;
import com.overload.model.EffectTimer;
import com.overload.model.WeaponInterface;
import com.overload.model.content.Keybinding;
import com.overload.net.BufferedConnection;
import com.overload.net.IsaacCipher;
import com.overload.net.requester.Resource;
import com.overload.net.requester.ResourceProvider;
import com.overload.scene.AnimableObject;
import com.overload.scene.CollisionMap;
import com.overload.scene.Fog;
import com.overload.scene.MapRegion;
import com.overload.scene.Projectile;
import com.overload.scene.SceneGraph;
import com.overload.scene.SceneObject;
import com.overload.scene.object.GroundDecoration;
import com.overload.scene.object.SpawnedObject;
import com.overload.scene.object.WallDecoration;
import com.overload.scene.object.WallObject;
import com.overload.settings.AccountData;
import com.overload.settings.AccountManager;
import com.overload.settings.AdvancedSettings;
import com.overload.settings.impl.GameGraphics;
import com.overload.settings.impl.Misc;
import com.overload.settings.impl.UserInterface;
import com.overload.sign.Signlink;
import com.overload.sound.SoundConstants;
import com.overload.sound.SoundPlayer;
import com.overload.sound.Track;
import com.overload.util.CacheUtils;
import com.overload.util.ChatMessageCodec;
import com.overload.util.MessageCensor;
import com.overload.util.MiscUtils;
import com.overload.util.MouseDetection;
import com.overload.util.OSValidator;
import com.overload.util.PacketConstants;
import com.overload.util.SecondsTimer;
import com.overload.util.SkillConstants;
import com.overload.util.StringUtils;

public class Client extends GameApplet {

    public static final int TOTAL_ARCHIVE_CRCS = 9;
    public static final int TITLE_CRC = 1;
    public static final int CONFIG_CRC = 2;
    public static final int INTERFACE_CRC = 3;
    public static final int MEDIA_CRC = 4;
    public static final int UPDATE_CRC = 5;
    public static final int TEXTURES_CRC = 6;
    public static final int CHAT_CRC = 7;
    public static final int SOUNDS_CRC = 8;
    /**
     * Spawnable Items
     */
    public static final int[] ALLOWED_SPAWNS = { 13441, 3144, 391, 397, 385, 7946, 2436, 145, 147, 149, 2440, 157, 159, 161,
			2442, 163, 165, 167, 9739, 2444, 169, 171, 173, // potions and food
			3040, 3042, 3044, 3046, 2452, 2454, 2456, 2458, 2448, 181, 183, 185, 6685, 6687, 6689, 6691, 2450, 189, 191, 193, 3024, 3026, 3028, 3030, 2434, // potions and food
			139, 141, 143, 4417, 4419, 4421, 4423, 229, // potions and food
			1149, 3140, 4087, 4585, 1187, 11840, // dragon
			1163, 1127, 1079, 1093, 1201, 4131, // rune
			1161, 1123, 1073, 1091, 1199, 4129, // addy
			1159, 1121, 1071, 1091, 1197, 4127, // mithril
			1165, 1125, 1077, 1089, 1195, 4125, // black
			1157, 1119, 1069, 1083, 1193, 4123, // steel
			1153, 1115, 1067, 1081, 1191, 4121, // iron
			1155, 1117, 1075, 1087, 1189, 4119, // bronze
			4587, 1333, 1331, 1329, 1327, 1325, 1323, 1321, // scimitars
			21009, 1289, 1287, 1285, 1283, 1281, 1279, 1277, // swords
			1305, 1303, 1301, 1299, 1297, 1295, 1293, 1291, // longswords
			7158, 1319, 1317, 1315, 1313, 1311, 1309, 1307, // 2hs
			1347, 1345, 1343, 1341, 1339, 1335, 1337, // warhammers
			5698, 1215, 1213, 1211, 1209, 1217, 1207, 1203, 1205, // daggers
			1434, 1432, 1430, 1428, 1426, 1424, 1420, 1422, // maces
			7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, // gloves
			11126, 2550, 4151, 4153, 10887, // special weapons
			6528, 6527, 6526, 6525, 6524, 6523, 6522, // obby items
			9747, 9748, 9750, 9751, 9753, 9754, 9756, 9757, 9759, 9760, 9762, 9763, 6568, 2412, 2413, 2414, // capes
			8850, 8849, 8848, 8847, 8846, 8845, 8844, 10828, 3755, 3753, 3751, 3749, 3748, 12831, 12829, 3842,
			3844, 12608, 12610, 12612, 11235, 859, 855, 851, 847, 845, 841, 861, 857, 853, 849, 843, 841, 9185, 9183,
			9181, 9179, 9177, 9174, 11212, 892, 890, 888, 886, 884, 882, 9245, 9244, 9243, 9242, 9241, 9240, 9239, 9238,
			9237, 9236, 9305, 9144, 9143, 9142, 9141, 9140, 877, 5667, 868, 867, 866, 869, 865, 863, 864, 19484, 5653,
			830, 829, 828, 827, 826, 825, 11230, 811, 810, 809, 808, 807, 806, 10368, 10370, 10372, 10374, 10376, 10378,
			10380, 10382, 10384, 10386, 10388, 10390, 12490, 12492, 12494, 12496, 12498, 12500, 12502, 12504, 12506,
			12508, 12510, 12512, 2503, 2497, 2491, 2501, 2495, 2489, 2499, 2493, 2487, 1135, 1099, 1065, 6322, 6324,
			6326, 6328, 6330, 10954, 10956, 10958, 6131, 6133, 6135, 1169, 1133, 1097, 1131, 1167, 1129, 1095, 10499,
			4675, 1381, 1383, 1385, 1387, 1379, 4089, 4091, 4093, 4095, 4097, 4099, 4101, 4103, 4105, 4107, 4109, 4111,
			4113, 4115, 4117, 7400, 7399, 7398, 6918, 6916, 6924, 6922, 6920, 6109, 6107, 6108, 6110, 6106, 3105, 6111,
			544, 542, 1035, 1033, 579, 577, 1011, 554, 555, 556, 557, 558, 559, 561, 563, 562, 560, 565, 566, 9075,
			1704, 1731, 1725, 1727, 1729, 8013, };
    public static final int[][] PLAYER_BODY_RECOLOURS = {
            {6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193},
            {8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341,
                    16578, 35003, 25239},
            {25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621,
                    4783, 1341, 16578, 35003},
            {4626, 11146, 6439, 12, 4758, 10270},
            {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}};
    public static final int[] tabInterfaceIDs =
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public static final int[] anIntArray1204 = {9104, 10275, 7595, 3610, 7975, 8526, 918, 38802,
            24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486};
    final static int[] IDs = {1196, 1199, 1206, 1215, 1224, 1231, 1240, 1249, 1258, 1267, 1274,
            1283, 1573, 1290, 1299, 1308, 1315, 1324, 1333, 1340, 1349, 1358,
            1367, 1374, 1381, 1388, 1397, 1404, 1583, 12038, 1414, 1421, 1430,
            1437, 1446, 1453, 1460, 1469, 15878, 1602, 1613, 1624, 7456, 1478,
            1485, 1494, 1503, 1512, 1521, 1530, 1544, 1553, 1563, 1593, 1635,
            12426, 12436, 12446, 12456, 6004, 18471,
            /* Ancients */
            12940, 12988, 13036, 12902, 12862, 13046, 12964, 13012, 13054, 12920,
            12882, 13062, 12952, 13000, 13070, 12912, 12872, 13080, 12976, 13024,
            13088, 12930, 12892, 13096};
    final static int[] runeChildren = {1202, 1203, 1209, 1210, 1211, 1218, 1219, 1220, 1227, 1228,
            1234, 1235, 1236, 1243, 1244, 1245, 1252, 1253, 1254, 1261, 1262,
            1263, 1270, 1271, 1277, 1278, 1279, 1286, 1287, 1293, 1294, 1295,
            1302, 1303, 1304, 1311, 1312, 1318, 1319, 1320, 1327, 1328, 1329,
            1336, 1337, 1343, 1344, 1345, 1352, 1353, 1354, 1361, 1362, 1363,
            1370, 1371, 1377, 1378, 1384, 1385, 1391, 1392, 1393, 1400, 1401,
            1407, 1408, 1410, 1417, 1418, 1424, 1425, 1426, 1433, 1434, 1440,
            1441, 1442, 1449, 1450, 1456, 1457, 1463, 1464, 1465, 1472, 1473,
            1474, 1481, 1482, 1488, 1489, 1490, 1497, 1498, 1499, 1506, 1507,
            1508, 1515, 1516, 1517, 1524, 1525, 1526, 1533, 1534, 1535, 1547,
            1548, 1549, 1556, 1557, 1558, 1566, 1567, 1568, 1576, 1577, 1578,
            1586, 1587, 1588, 1596, 1597, 1598, 1605, 1606, 1607, 1616, 1617,
            1618, 1627, 1628, 1629, 1638, 1639, 1640, 6007, 6008, 6011, 8673,
            8674, 12041, 12042, 12429, 12430, 12431, 12439, 12440, 12441, 12449,
            12450, 12451, 12459, 12460, 15881, 15882, 15885, 18474, 18475, 18478};
    private static final long serialVersionUID = 5707517957054703648L;
    private static final int[] SKILL_EXPERIENCE;
    private static final String validUserPassChars =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    public static ScreenMode frameMode = ScreenMode.FIXED;
    public static int frameWidth = 765;
    public static int frameHeight = 503;
    public static int screenAreaWidth = 512;
    public static int screenAreaHeight = 334;
    public static int cameraZoom = 600;
    public static double brightnessState = 0.8;
    public static boolean showChatComponents = true;
    public static boolean showTabComponents = true;
    public static boolean changeChatArea = false;
    public static boolean stackSideStones = false;
    public static boolean transparentTabArea = false;
    public static Client instance;
    public int openInterfaceId;
    public static int portOffset;
    public static int anInt1089;
    public static int spellId = 0;
    public static int totalRead = 0;
    public static boolean tabAreaAltered;
    public static Player localPlayer;
    public static boolean loggedIn;
    public static int tick;
    public static int tabId;
    public boolean updateChatbox;
    public static int BIT_MASKS[];
    public static int anInt1290;
    public static String server = "";
    public static int[] fullScreenTextureArray;
    /* Console */
    public Console console = new Console();
    public static boolean shiftDown;
    public static boolean enableGridOverlay;
    static int scrollBarPosition;
    private static int anInt849;
    private static int anInt854;
    private static int anInt924;
    private static int nodeID = 10;
    private static boolean isMembers = true;
    private static boolean lowMemory = false;
    private static int anInt986;
    private static int anInt1005;
    private static int anInt1051;
    public static ProducingGraphicsBuffer loginBoxImageProducer;
    private static int anInt1117;
    private static int anInt1134;
    private static int anInt1142;
    private static int anInt1155;
    private static ProducingGraphicsBuffer tabImageProducer;
    private static ProducingGraphicsBuffer gameScreenImageProducer;
    private static ProducingGraphicsBuffer chatboxImageProducer;
    private static int anInt1175;
    private static int[] anIntArray1180;
    private static int[] anIntArray1181;
    private static int[] anIntArray1182;
    private static int anInt1188;
    private static boolean flagged;
    private static int anInt1226;
    private static int anInt1288;
    private static boolean removeShiftDropOnMenuOpen;

    public static ChatboxDrawing chatboxDrawing;
    
    private Fog fog = new Fog();
    
    static {
        SKILL_EXPERIENCE = new int[99];
        int i = 0;
        for (int j = 0; j < 99; j++) {
            int l = j + 1;
            int i1 = (int) ((double) l + 300D * Math.pow(2D, (double) l / 7D));
            i += i1;
            SKILL_EXPERIENCE[j] = i / 4;
        }
        BIT_MASKS = new int[32];
        i = 2;
        for (int k = 0; k < 32; k++) {
            BIT_MASKS[k] = i - 1;
            i += i;
        }
    }
    
    public boolean isStaff() {
    	return myPrivilege >= 2;
    }

	public boolean isDonator() {
		return myPrivilege >= 7;
	}
    
    public final String formatAmount(long amount) {
		if (amount >= 0 && amount < 100000) {
			return String.valueOf(amount);
		}
		if (amount >= 100000 && amount < 10000000) {
			return amount / 1000 + "K";
		}
		if (amount >= 10000000 && amount < 10000000000L) {
			return amount / 1000000 + "M";
		}
		if (amount >= 10000000000L && amount < 10000000000000L) {
			return amount / 1000000000 + "B";
		}
		if (amount >= 10000000000000L && amount < 10000000000000000L) {
			return amount / 1000000000000L + "T";
		}
		if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
			return amount / 1000000000000000L + "QD";
		}
		if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
			return amount / 1000000000000000000L + "QT";
		}
		return amount + "";
	}
    

	public int[] titleScreenOffsets = null;
	public int titleWidth = -1;
	public int titleHeight = -1;
	/*public void generateWorld(int x, int y) {
		terrainRegionX = x;
		terrainRegionY = y;
		aBoolean1159 = false;
		if(anInt1069 == x && anInt1070 == y && loadingStage == 2) {
			return;
		}
		anInt1069 = x;
		anInt1070 = y;
		baseX = (anInt1069 - 6) * 8;
		baseY = (anInt1070 - 6) * 8;
		aBoolean1141 = (anInt1069 / 8 == 48 || anInt1069 / 8 == 49) && anInt1070 / 8 == 48;
		if(anInt1069 / 8 == 48 && anInt1070 / 8 == 148)
			aBoolean1141 = true;
		loadingStage = 1;
		aLong824 = System.currentTimeMillis();
		int k16 = 0;
		for(int i21 = (anInt1069 - 6) / 8; i21 <= (anInt1069 + 6) / 8; i21++) {
			for(int k23 = (anInt1070 - 6) / 8; k23 <= (anInt1070 + 6) / 8; k23++)
				k16++;
		}
		aByteArrayArray1183 = new byte[k16][];
		aByteArrayArray1247 = new byte[k16][];
		anIntArray1234 = new int[k16];
		anIntArray1235 = new int[k16];
		anIntArray1236 = new int[k16];
		k16 = 0;
		for(int l23 = (anInt1069 - 6) / 8; l23 <= (anInt1069 + 6) / 8; l23++) {
			for(int j26 = (anInt1070 - 6) / 8; j26 <= (anInt1070 + 6) / 8; j26++) {
				anIntArray1234[k16] = (l23 << 8) + j26;
				if(aBoolean1141 && (j26 == 49 || j26 == 149 || j26 == 147 || l23 == 50 || l23 == 49 && j26 == 47)) {
					anIntArray1235[k16] = -1;
					anIntArray1236[k16] = -1;
					k16++;
				} else {
					int k28 = anIntArray1235[k16] = onDemandFetcher.method562(0, j26, l23);
					if(k28 != -1)
						onDemandFetcher.method558(3, k28);
					int j30 = anIntArray1236[k16] = onDemandFetcher.method562(1, j26, l23);
					if(j30 != -1)
						onDemandFetcher.method558(3, j30);
					k16++;
				}
			}
		}
		int i17 = baseX - anInt1036;
		int j21 = baseY - anInt1037;
		anInt1036 = baseX;
		anInt1037 = baseY;
		for(int j24 = 0; j24 < 16384; j24++) {
			NPC npc = npcArray[j24];
			if(npc != null) {
				for(int j29 = 0; j29 < 10; j29++) {
					npc.smallX[j29] -= i17;
					npc.smallY[j29] -= j21;
				}
				npc.x -= i17 * 128;
				npc.y -= j21 * 128;
			}
		}
		for(int i27 = 0; i27 < maxPlayers; i27++) {
			Player player = playerArray[i27];
			if(player != null) {
				for(int i31 = 0; i31 < 10; i31++) {
					player.smallX[i31] -= i17;
					player.smallY[i31] -= j21;
				}
				player.x -= i17 * 128;
				player.y -= j21 * 128;
			}
		}
		aBoolean1080 = true;
		byte byte1 = 0;
		byte byte2 = 104;
		byte byte3 = 1;
		if(i17 < 0) {
			byte1 = 103;
			byte2 = -1;
			byte3 = -1;
		}
		byte byte4 = 0;
		byte byte5 = 104;
		byte byte6 = 1;
		if(j21 < 0) {
			byte4 = 103;
			byte5 = -1;
			byte6 = -1;
		}
		for(int k33 = byte1; k33 != byte2; k33 += byte3) {
			for(int l33 = byte4; l33 != byte5; l33 += byte6) {
				int i34 = k33 + i17;
				int j34 = l33 + j21;
				for(int k34 = 0; k34 < 4; k34++)
					if(i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104)
						groundArray[k34][k33][l33] = groundArray[k34][i34][j34];
					else
						groundArray[k34][k33][l33] = null;
			}
		}
		for(Class30_Sub1 class30_sub1_1 = (Class30_Sub1)aClass19_1179.reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (Class30_Sub1)aClass19_1179.reverseGetNext()) {
			class30_sub1_1.anInt1297 -= i17;
			class30_sub1_1.anInt1298 -= j21;
			if(class30_sub1_1.anInt1297 < 0 || class30_sub1_1.anInt1298 < 0 || class30_sub1_1.anInt1297 >= 104 || class30_sub1_1.anInt1298 >= 104)
				class30_sub1_1.unlink();
		}
		if(destX != 0) {
			destX -= i17;
			destY -= j21;
		}
		aBoolean1160 = false;
	}
	
	public void resetWorld(int stage) {
		if(stage == 0) {
			anInt1062 = 0;
			anInt1278 = (int) (Math.random() * 100D) - 50;
			anInt1131 = (int) (Math.random() * 110D) - 55;
			anInt896 = (int) (Math.random() * 80D) - 40;
			minimapInt2 = (int) (Math.random() * 120D) - 60;
			minimapInt3 = (int) (Math.random() * 30D) - 20;
			minimapInt1 = (int) (Math.random() * 20D) - 10 & 0x7ff;
			anInt1021 = 0;
			loadingStage = 1;
		} else if(stage == 1) {
			aBoolean1080 = false;
		}
	}
    
	public void drawAnimatedWorldBackground(boolean display) {
		if (display) {
			DrawingArea.setAllPixelsToZero();
			int centerX = frameWidth / 2;
			int centerY = frameHeight / 2;
			if (scriptManager == null)
				loginScreenBG(true);
			int canvasCenterX = Rasterizer3D.originViewX;
			int canvasCenterY = Rasterizer3D.originViewY;
			int canvasPixels[] = Rasterizer3D.scanOffsets;
			if (titleScreenOffsets != null
					&& (titleWidth != frameWidth || titleHeight != frameHeight))
				titleScreenOffsets = null;
			if (titleScreenOffsets == null) {
				titleWidth = frameWidth;
				titleHeight = frameHeight;
				titleScreenOffsets = Rasterizer.getOffsets(titleWidth,titleHeight);
			}
			Rasterizer3D.originViewX = centerX;
			Rasterizer3D.originViewY = centerY;
			Rasterizer3D.scanOffsets = titleScreenOffsets;
			if (loadingStage == 2 && ObjectManager.anInt131 != plane)
				loadingStage = 1;
			if (!loggedIn && loadingStage == 1)
				method54();
			if (!loggedIn && loadingStage == 2 && plane != anInt985) {
				anInt985 = plane;
				method24(plane);
			}
			if (loadingStage == 2)
				try {
					scene.render(xCameraPos, yCameraPos, xCameraCurve,zCameraPos, method121(), yCameraCurve);
					double fogDistance = Math.sqrt(Math.pow(zCameraPos, 2));
					int fogStartDistance = 1330;
					int fogEndDistance = 2100;
					fog.setFogDistance((float) fogDistance);
					fog.renderFog(false, fogStartDistance, fogEndDistance, 3);
					scene.clearGameObjectCache();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			if (scriptManager != null && loadingStage == 2 && plane == anInt985 && !loggedIn)
				scriptManager.cycle();
			Rasterizer3D.originViewX = canvasCenterX;
			Rasterizer3D.originViewY = canvasCenterY;
			Rasterizer3D.scanOffsets = canvasPixels;
		}
	}*/

	public int getAmountColor(long amount) {
		int color = 0xFFFFFF;
		if (amount >= 1) {
			color = 0xFFFF00;
		}
		if (amount >= 100000) {
			color = 0xFFFFFF;
		}
		if (amount >= 10000000) {
			color = 0x00FF80;
		}
		if (amount >= 10000000000L) {
			color = 0x00FFFF;
		}
		return color;
	}

    public final int[] // Perfected (Hp, pray and run orb)
            orbX = {0, 0, 24}, orbY = {41, 85, 122}, orbTextX = {15, 16, 40}, orbTextY = {67, 111, 148},
            coloredOrbX = {27, 27, 51}, coloredOrbY = {45, 89, 126},
            currentInterface = {4016, 4012, 149}, maximumInterface = {4017, 4013, 149},
            orbIconX = {33, 30, 58}, orbIconY = {51, 92, 130};
    public final int[] currentExp;
    public final int[] currentLevels;
    public final FileStore[] indices;
    public final int[] maximumLevels;
    public final Widget aClass9_1059;
    public final int anInt1239 = 100;
    final int[] sideIconsX = {17, 49, 83, 114, 146, 180, 214, 16, 49, 82, 116, 148, 184, 217},
            sideIconsY = {9, 7, 7, 5, 2, 3, 7, 303, 306, 306, 302, 305, 303, 304, 306},
            sideIconsId = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13},
            sideIconsTab = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    private final int[] soundVolume;
    public final NumberFormat format = NumberFormat.getInstance(Locale.UK);
    private final int[] modeX = {160, 224, 288, 352, 416},
            modeNamesX = {26, 84, 146, 206, 278, 339, 408, 469},
            modeNamesY = {158, 158, 153, 153, 153, 153, 153, 158},
            channelButtonsX = {5, 69, 133, 197, 261, 325, 389, 453};
    private final String[] modeNames =
            {"All", "Game", "Public", "Private", "Clan", "Trade", "Yell", "Tools"};
    private final int[] hitmarks562 = {31, 32, 33, 34};
    private final int[] tabClickX = {38, 33, 33, 33, 33, 33, 38, 38, 33, 33, 33, 33, 33, 38},
            tabClickStart = {522, 560, 593, 625, 659, 692, 724, 522, 560, 593, 625, 659, 692,
                    724},
            tabClickY = {169, 169, 169, 169, 169, 169, 169, 466, 466, 466, 466, 466, 466,
                    466};
    private final int[] chatRights;
    private final int[] quakeMagnitudes;
    private final boolean[] quakeDirectionActive;
    private final int maxPlayers;
    private final int internalLocalPlayerIndex;
    private final long[] ignoreListAsLongs;
    private final int[] quake4PiOverPeriods;
    private final int[] chatTypes;
    private final String[] chatNames;
    private final String[] chatMessages;
    private final int[] anIntArray965 = {0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff};
    private final int[] anIntArray968;
    private final int anInt975;
    private final int[] anIntArray976;
    private final int[] anIntArray977;
    private final int[] anIntArray978;
    private final int[] anIntArray979;
    private final int[] textColourEffect;
    private final int[] anIntArray981;
    private final int[] anIntArray982;
    private final String[] aStringArray983;
    private final int[] characterDesignColours;
    private final boolean aBoolean994;
    private final int[] quakeTimes;
    private final int[] configValues;
    private final int[] minimapLeft;
    private final int[] anIntArray1057;
    private final int barFillColor;
    private final int[] myAppearance;
    private final String[] playerOptions;
    private final boolean[] playerOptionsHighPriority;
    private final int[][][] constructRegionData;
    private final int[] objectGroups =
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
    private final int[] quakeAmplitudes;
    private final int[] tracks;
	private final Sprite[] modIcons;
	private final Sprite[] modeIcons;
    private final int[] minimapLineWidth;
    private final int[] privateMessageIds;
    private final int[] trackLoops;
    private final int[] soundDelay;
    private final boolean rsAlreadyLoaded;
    public CRC32 CRC = new CRC32();
    public int[] CRCs = new int[TOTAL_ARCHIVE_CRCS];
    public Sprite[] skill_sprites = new Sprite[SkillConstants.SKILL_COUNT];
    public Sprite[] backgrounds = new Sprite[4];
    // Timers
    public java.util.List<EffectTimer> effects_list = new CopyOnWriteArrayList<EffectTimer>();
    public FileArchive mediaStreamLoader;
    public boolean exitRequested = false;
    public int dropdownInversionFlag;
    public boolean clickedQuickPrayers;
    public int rights;
    public String name;
    public String defaultText;
    public String clanname;
    public int chatTypeView;
    public int clanChatMode;
    public int autoCastId = 0;
    public String prayerBook;
    public int xCameraPos;
    public int zCameraPos;
    public int yCameraPos;
    public int localPlayerIndex;
    public String inputString;
    public int anInt897;
    public int spriteDrawX;
    public int spriteDrawY;
    public int particleDrawX;
    public int particleDrawY;
    public int particleDrawZ;
    public int settings[];
    public int anInt988;
    public int openWalkableInterface;
    public FileArchive titleArchive;
    public ResourceProvider resourceProvider;
    public int currentRegionX;
    public int currentRegionY;
    public int anInt1132;
    public int anInt1171;
    public int anInt1210;
    public int anInt1254;
    public GameFont smallText;
    public GameFont regularText;
    public GameFont boldText;
    public RSFont newSmallFont, newRegularFont, newBoldFont;
    public RSFont newFancyFont;
    public int backDialogueId;
    public int anInt1279;
    public int drawCount;
    public int fullscreenInterfaceID;
    public int anInt1044;// 377
    public int anInt1129;// 377
    public int interfaceId;// 377
    public int anInt1500;// 377
    public int anInt1501;// 377
    /* Wheel of Fortune */
    int[][] emptyClipData;
    private int[][] xp_added = new int[10][3];
    private Sprite hp;
    /**
     * Draws information about our current target
     * during combat.
     */

    private SecondsTimer combatBoxTimer = new SecondsTimer();
    private Mob currentInteract;
    // Spawn TODO remove
    private String searchSyntax = "";
    private int[] searchResults = new int[100];
    private boolean fetchSearchResults;
    private boolean searchingSpawnTab;
    private SpawnTabType spawnType = SpawnTabType.INVENTORY;
    private String enter_amount_title = "Enter amount:";
    private String enter_name_title = "Enter name:";
    private int cButtonHPos;
    private int cButtonCPos;
    private int setChannel;
    private int currentTrackTime;
    private long trackTimer;
    private String objectMaps = "", floorMaps = "";
    private int poisonType = 0;
    private int specialAttack = 0;
    private ProducingGraphicsBuffer loginScreenAccessories;

    /**
     * The player's total exp
     */
    private long totalExp;
    private int specialEnabled = 0;
    private boolean runHover, prayHover, hpHover, prayClicked,
            specialHover, expCounterHover, worldHover, autocast;
    private ProducingGraphicsBuffer leftFrame;
    private ProducingGraphicsBuffer topFrame;
    private int ignoreCount;
    private long loadingStartTime;
    private int[][] anIntArrayArray825;
    private int[] friendsNodeIDs;
    private Deque[][][] groundItems;
    private volatile boolean aBoolean831;
    private Npc[] npcs;
    private int npcCount;
    private int[] npcIndices;
    private int removedMobCount;
    private int[] removedMobs;
    private int lastOpcode;
    private int secondLastOpcode;
    private int thirdLastOpcode;
    private String clickToContinueString;
    private int privateChatMode;
    private Buffer loginBuffer;
    private boolean aBoolean848;
    private int[] anIntArray851;
    private int[] anIntArray852;
    private int[] anIntArray853;
    private int hintIconDrawType;
    public int yCameraCurve;
    public int xCameraCurve;
    private int myPrivilege;
    private int myMode;
    private Sprite mapFlag;
    private Sprite mapMarker;
    private int weight;
    private MouseDetection mouseDetection;
    private volatile boolean drawFlames;
    private String reportAbuseInput;
    private boolean menuOpen;
    private int interfaceHoverType;
    private Player[] players;
    private int playerCount;
    private int[] playerList;
    private int mobsAwaitingUpdateCount;
    private int[] mobsAwaitingUpdate;
    private Buffer[] playerSynchronizationBuffers;
    private int cameraRotation;
    private int friendsCount;
    private int friendServerStatus;
    private int[][] anIntArrayArray901;
    private byte[] aByteArray912;
    private int anInt913;
    private int crossX;
    private int crossY;
    private int crossIndex;
    private int crossType;
    public int plane;
    private boolean loadingError;
    private int[][] anIntArrayArray929;
    private Sprite aClass30_Sub2_Sub1_Sub1_931;
    private Sprite aClass30_Sub2_Sub1_Sub1_932;
    private int hintIconPlayerId;
    private int hintIconX;
    private int hintIconY;
    private int hintIconLocationArrowHeight;
    private int hintIconLocationArrowRelX;
    private int hintIconLocationArrowRelY;
    private int tickDelta;
    private SceneGraph scene;
    private Sprite[] sideIcons;
    private int menuScreenArea;
    private int menuOffsetX;
    private int menuOffsetY;
    private int menuWidth;
    private int menuHeight;
    private long aLong953;
    private boolean aBoolean954;
    private long[] friendsListAsLongs;
    private int currentSong;
    private volatile boolean drawingFlames;
    private boolean aBoolean972;
    private int anInt984;
    private int lastKnownPlane;
    private Sprite[] hitMarks;
    private int dragItemDelay;
    private int cinematicCamXViewpointLoc;
    private int cinematicCamYViewpointLoc;
    private int cinematicCamZViewpointLoc;
    private int constCinematicCamRotationSpeed;
    private int varCinematicCamRotationSpeedPromille;
    private IsaacCipher encryption;
    private Sprite multiOverlay;
    private String amountOrNameInput;
    private int daysSinceLastLogin;
    private int packetSize;
    private int opcode;
    private int timeoutCounter;
    private int ping_packet_counter;
    private int anInt1011;
    private Deque projectiles;
    private int anInt1014;
    private int anInt1015;
    private int anInt1016;
    private boolean aBoolean1017;
    private int minimapState;
    private int loadingStage;
    private Sprite scrollBar1;
    private Sprite scrollBar2;
    private int anInt1026;
    private boolean aBoolean1031;
    private Sprite[] mapFunctions;
    private int regionBaseX;
    private int regionBaseY;
    private int previousAbsoluteX;
    private int previousAbsoluteY;
    private int loginFailures;
    private int anInt1039;
    private int dialogueId;
    private boolean maleCharacter;
    private int anInt1048;
    private String aString1049;
    private int flashingSidebarId;
    private int multicombat;
    private Deque incompleteAnimables;
    private IndexedImage[] mapScenes;
    private int trackCount;
    private int friendsListAction;
    private int mouseInvInterfaceIndex;
    private int lastActiveInvInterface;
    private int anInt1071;
    private int[] minimapHintX;
    private int[] minimapHintY;
    private Sprite mapDotItem;
    private Sprite mapDotNPC;
    private Sprite mapDotPlayer;
    private Sprite mapDotFriend;
    private Sprite mapDotTeam;
    private Sprite mapDotClan;
    private int anInt1079;
    private boolean loadingMap;
    private String[] friendsList;
    private Buffer incoming;
    private int anInt1084;
    private int anInt1085;
    private int activeInterfaceType;
    private int anInt1087;
    private int anInt1088;
    private int[] firstMenuAction;
    private int[] secondMenuAction;
    private int[] menuActionTypes;
    private int[] selectedMenuActions;
    private Sprite[] headIcons;
    private Sprite[] skullIcons;
    private Sprite[] headIconsHint;
    private int x;
    private int y;
    private int height;
    private int speed;
    private int angle;
    private int systemUpdateTime;
    private ProducingGraphicsBuffer topLeft1BackgroundTile;
    private ProducingGraphicsBuffer bottomLeft1BackgroundTile;
    private ProducingGraphicsBuffer flameLeftBackground;
    private ProducingGraphicsBuffer flameRightBackground;
    private ProducingGraphicsBuffer bottomLeft0BackgroundTile;
    private ProducingGraphicsBuffer bottomRightImageProducer;
    private ProducingGraphicsBuffer loginMusicImageProducer;
    private ProducingGraphicsBuffer middleLeft1BackgroundTile;
    private ProducingGraphicsBuffer aRSImageProducer_1115;
    private int membersInt;
    private String aString1121;
    private Sprite compass;
    private ProducingGraphicsBuffer chatSettingImageProducer;
    private int cameraY;
    private int menuActionRow;
    private int spellSelected;
    private int anInt1137;
    private int spellUsableOn;
    private String spellTooltip;
    private Sprite[] minimapHint;
    private boolean inTutorialIsland;
    private int runEnergy;
    private boolean continuedDialogue;
    private Sprite[] crosses;
    private IndexedImage[] titleIndexedImages;
    private int unreadMessages;
    private boolean canMute;
    private boolean requestMapReconstruct;
    private boolean inCutScene;
    private ProducingGraphicsBuffer minimapImageProducer;
    private int daysSinceRecovChange;
    private BufferedConnection socketStream;
    private IsaacCipher cipher;
    private int privateMessageCount;
    private int minimapZoom;
    private String myUsername;
    private String myPassword;
    private boolean showClanOptions;
    private boolean genericLoadingError;
    private int reportAbuseInterfaceID;
    private Deque spawns;
    private byte[][] terrainData;
    private int anInt1184;
    private int cameraHorizontal;
    private int anInt1186;
    private int anInt1187;
    private int overlayInterfaceId;
    private int[] anIntArray1190;
    private int[] anIntArray1191;
    private Buffer outBuffer;
   // public Buffer chatBuffer;
    private int anInt1193;
    private int splitPrivateChat;
    private IndexedImage mapBack;
    private String[] menuActionText;
    private Sprite flameLeftSprite;
    private Sprite flameRightSprite;
    private int minimapRotation;
    private String promptInput;
    private int anInt1213;
    private int[][][] tileHeights;
    private long serverSeed;
    private int loginScreenCursorPos;
    private long aLong1220;
    private int hintIconNpcId;
    public int inputDialogState;
    private int nextSong;
    private boolean fadeMusic;
    private CollisionMap[] collisionMaps;
    private int[] mapCoordinates;
    private int[] terrainIndices;
    private int[] objectIndices;
    private boolean aBoolean1242;
    private int atInventoryLoopCycle;
    private int atInventoryInterface;
    private int atInventoryIndex;
    private int atInventoryInterfaceType;
    private byte[][] objectData;
    private int tradeMode;
    private int yellMode;
    private int anInt1249;
    private int onTutorialIsland;
    private int anInt1253;
    private boolean welcomeScreenRaised;
    private boolean messagePromptRaised;
    private byte[][][] tileFlags;
    private int prevSong;
    private int destinationX;
    private int destinationY;
    private Sprite minimapImage;
    private Image worldMapMarker = null;
    private int markerAngle;
    private long lastMarkerRotation;
    private int anInt1265;
    private int localX;
    private int localY;
    private GameFont gameFont;
    private int cameraX;
    private int[] bigX;
    private int[] bigY;
    private int itemSelected;
    private int anInt1283;
    private int anInt1284;
    private int anInt1285;
    private String selectedItemName;
    private int publicChatMode;
    
	public int terrainRegionX;
	public int terrainRegionY;
	public ScriptManager scriptManager;

    public Client() {
    	outBuffer = new Buffer(new byte[5000]);
    	// chatBuffer = new Buffer(new byte[5000]);
        fullscreenInterfaceID = -1;
        chatRights = new int[500];
        soundVolume = new int[50];
        chatTypeView = 0;
        clanChatMode = 0;
        cButtonHPos = -1;
        cButtonCPos = 0;
        server = Configuration.SERVER_ADDRESS;
        anIntArrayArray825 = new int[104][104];
        emptyClipData = new int[103][103];
        friendsNodeIDs = new int[200];
        groundItems = new Deque[4][104][104];
        aBoolean831 = false;
        npcs = new Npc[16384];
        npcIndices = new int[16384];
        removedMobs = new int[1000];
        loginBuffer = new Buffer(new byte[5000]);
        aBoolean848 = true;
        openInterfaceId = -1;
        currentExp = new int[SkillConstants.SKILL_COUNT];
        quakeMagnitudes = new int[5];
        backgroundimage = new Sprite[5];
        ProfilePic = new Sprite[3];
        quakeDirectionActive = new boolean[5];
        drawFlames = false;
        reportAbuseInput = "";
        localPlayerIndex = -1;
        menuOpen = false;
        inputString = "";
        maxPlayers = 2048;
        internalLocalPlayerIndex = 2047;
        players = new Player[maxPlayers];
        playerList = new int[maxPlayers];
        mobsAwaitingUpdate = new int[maxPlayers];
        playerSynchronizationBuffers = new Buffer[maxPlayers];
        anInt897 = 1;
        anIntArrayArray901 = new int[104][104];
        aByteArray912 = new byte[16384];
        currentLevels = new int[SkillConstants.SKILL_COUNT];
        ignoreListAsLongs = new long[100];
        loadingError = false;
        quake4PiOverPeriods = new int[5];
        anIntArrayArray929 = new int[104][104];
        chatTypes = new int[500];
        chatNames = new String[500];
        chatMessages = new String[500];
        sideIcons = new Sprite[15];
        aBoolean954 = true;
        friendsListAsLongs = new long[200];
        currentSong = -1;
        drawingFlames = false;
        spriteDrawX = -1;
        spriteDrawY = -1;
        anIntArray968 = new int[33];
        indices = new FileStore[5];
        settings = new int[10000];
        aBoolean972 = false;
        anInt975 = 50;
        anIntArray976 = new int[anInt975];
        anIntArray977 = new int[anInt975];
        anIntArray978 = new int[anInt975];
        anIntArray979 = new int[anInt975];
        textColourEffect = new int[anInt975];
        anIntArray981 = new int[anInt975];
        anIntArray982 = new int[anInt975];
        aStringArray983 = new String[anInt975];
        lastKnownPlane = -1;
        hitMarks = new Sprite[20];
        characterDesignColours = new int[5];
        aBoolean994 = false;
        amountOrNameInput = "";
        projectiles = new Deque();
        aBoolean1017 = false;
        openWalkableInterface = -1;
        quakeTimes = new int[5];
        aBoolean1031 = false;
        mapFunctions = new Sprite[100];
        dialogueId = -1;
        maximumLevels = new int[SkillConstants.SKILL_COUNT];
        configValues = new int[2000];
        maleCharacter = true;
        minimapLeft = new int[152];
        minimapLineWidth = new int[152];
        flashingSidebarId = -1;
        incompleteAnimables = new Deque();
        anIntArray1057 = new int[33];
        aClass9_1059 = new Widget();
        mapScenes = new IndexedImage[100];
        barFillColor = 0x4d4233;
        myAppearance = new int[7];
        minimapHintX = new int[1000];
        minimapHintY = new int[1000];
        loadingMap = false;
        friendsList = new String[200];
        incoming = new Buffer(new byte[5000]);
        firstMenuAction = new int[500];
        secondMenuAction = new int[500];
        menuActionTypes = new int[500];
        selectedMenuActions = new int[500];
        headIcons = new Sprite[20];
        skullIcons = new Sprite[20];
        headIconsHint = new Sprite[20];
        tabAreaAltered = false;
        aString1121 = "";
        playerOptions = new String[5];
        playerOptionsHighPriority = new boolean[5];
        constructRegionData = new int[4][13][13];
        anInt1132 = 2;
        minimapHint = new Sprite[1000];
        inTutorialIsland = false;
        continuedDialogue = false;
        crosses = new Sprite[8];
        loggedIn = false;
        canMute = false;
        requestMapReconstruct = false;
        inCutScene = false;
        anInt1171 = 1;
        myUsername = "";
        myPassword = "";
        genericLoadingError = false;
        reportAbuseInterfaceID = -1;
        spawns = new Deque();
        anInt1184 = 128;
        overlayInterfaceId = -1;
        menuActionText = new String[500];
        quakeAmplitudes = new int[5];
        tracks = new int[50];
        anInt1210 = 2;
        scrollBarPosition = 78;
        promptInput = "";
		modIcons = new Sprite[14];
		modeIcons = new Sprite[4];
        tabId = 3;
        updateChatbox = false;
        fadeMusic = true;
        collisionMaps = new CollisionMap[4];
        privateMessageIds = new int[100];
        trackLoops = new int[50];
        aBoolean1242 = false;
        soundDelay = new int[50];
        rsAlreadyLoaded = false;
        welcomeScreenRaised = false;
        messagePromptRaised = false;
        backDialogueId = -1;
        anInt1279 = 2;
        bigX = new int[4000];
        bigY = new int[4000];
    }
    
    public static final Rectangle MAXIMUM_SCREEN_BOUNDS = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    
    public boolean isFixed() {
    	return frameMode == ScreenMode.FIXED;
    }
  
    
    
    public void frameMode(ScreenMode mode) {
		if (frameMode.equals(mode)) {
			return;
		}


		JFrame component = ClientWindow.getFrame();
		component.dispose();

		frameMode = mode;

		if (mode.isUndecorated()) {
			component.setUndecorated(true);
			component.setLocation(0, 0);
			component.setVisible(true);
		} else {
			if (component.isUndecorated()) {
				component.setUndecorated(false);
				component.setVisible(true);
			}
		}
		component.setMinimumSize(new Dimension(765, mode == ScreenMode.FIXED ? 568 : 610));
		component.setResizable(mode.isResizable());

		Insets insets = ClientWindow.getInset();

		final int xInsetOffset = insets.left + insets.right;
		final int yInsetOffset = insets.top + insets.bottom;

		int totalClientWidth = mode.getWidth();
		int totalClientHeight = mode.getHeight();

		if (mode == ScreenMode.FULLSCREEN) {
			if (OSValidator.isMac()) {
				component.setLocation(0, insets.top);
			}
			totalClientWidth = (int) MAXIMUM_SCREEN_BOUNDS.getWidth();
			totalClientHeight = (int) MAXIMUM_SCREEN_BOUNDS.getHeight();
		} else {
			totalClientWidth += xInsetOffset;
			totalClientHeight += yInsetOffset;
		}
		
		component.setSize(totalClientWidth, totalClientHeight);
		frameWidth = totalClientWidth;
		frameHeight = totalClientHeight;

		if (mode != ScreenMode.FULLSCREEN) {
			component.setLocationRelativeTo(null);
		}
		
		screenAreaWidth = mode.getScreenAreaWidth();
		screenAreaHeight = mode.getScreenAreaHeight();
		
	    setBounds();

		component.setVisible(true);

		graphics = super.getGameComponent().getGraphics();

		 if (mode == ScreenMode.FIXED) {
             cameraZoom = 600;
             SceneGraph.viewDistance = 9;
         } else if (mode == ScreenMode.RESIZABLE) {
             cameraZoom = 850;
             SceneGraph.viewDistance = 10;
         } else if (mode == ScreenMode.FULLSCREEN) {
             cameraZoom = 600;
             SceneGraph.viewDistance = 10;
         }
	     stackSideStones = mode == ScreenMode.FIXED ? false : stackSideStones;
	     showChatComponents = mode == ScreenMode.FIXED ? true : showChatComponents;
	     showTabComponents = mode == ScreenMode.FIXED ? true : showTabComponents;

	}

    private static void setBounds() {
        Rasterizer3D.reposition(frameWidth, frameHeight);
        fullScreenTextureArray = Rasterizer3D.scanOffsets;
        Rasterizer3D.reposition(
                frameMode == ScreenMode.FIXED
                        ? (chatboxImageProducer != null
                        ? chatboxImageProducer.canvasWidth : 519)
                        : frameWidth,
                frameMode == ScreenMode.FIXED
                        ? (chatboxImageProducer != null
                        ? chatboxImageProducer.canvasHeight : 165)
                        : frameHeight);
        anIntArray1180 = Rasterizer3D.scanOffsets;
        Rasterizer3D.reposition(
                frameMode == ScreenMode.FIXED
                        ? (tabImageProducer != null ? tabImageProducer.canvasWidth
                        : 249)
                        : frameWidth,
                frameMode == ScreenMode.FIXED ? (tabImageProducer != null
                        ? tabImageProducer.canvasHeight : 335) : frameHeight);
        anIntArray1181 = Rasterizer3D.scanOffsets;
        Rasterizer3D.reposition(screenAreaWidth, screenAreaHeight);
        anIntArray1182 = Rasterizer3D.scanOffsets;
        int ai[] = new int[9];
        for (int i8 = 0; i8 < 9; i8++) {
            int k8 = 128 + i8 * 32 + 15;
            int l8 = 600 + k8 * 3;
            int i9 = Rasterizer3D.anIntArray1470[k8];
            ai[i8] = l8 * i9 >> 16;
        }
        if (frameMode == ScreenMode.RESIZABLE && (frameWidth >= 766) && (frameWidth <= 1025)
                && (frameHeight >= 504) && (frameHeight <= 850)) {
            SceneGraph.viewDistance = 9;
            cameraZoom = 575;
        } else if (frameMode == ScreenMode.FIXED) {
            cameraZoom = 600;
        } else if (frameMode == ScreenMode.RESIZABLE || frameMode == ScreenMode.FULLSCREEN) {
            SceneGraph.viewDistance = 10;
            cameraZoom = 600;
        }
        SceneGraph.setupViewport(500, 800, screenAreaWidth, screenAreaHeight, ai);
        if (loggedIn) {
            gameScreenImageProducer =
                    new ProducingGraphicsBuffer(screenAreaWidth, screenAreaHeight);
        }
    }

    private static String intToKOrMilLongName(int i) {
        String s = String.valueOf(i);
        for (int k = s.length() - 3; k > 0; k -= 3)
            s = s.substring(0, k) + "," + s.substring(k);
        if (s.length() > 8)
            s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
        else if (s.length() > 4)
            s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
        return " " + s;
    }

    public static final byte[] ReadFile(String fileName) {
        try {
            byte abyte0[];
            File file = new File(fileName);
            int i = (int) file.length();
            abyte0 = new byte[i];
            DataInputStream datainputstream = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(fileName)));
            datainputstream.readFully(abyte0, 0, i);
            datainputstream.close();
            return abyte0;
        } catch (Exception e) {
            System.out.println((new StringBuilder()).append("Read Error: ").append(fileName)
                    .toString());
            return null;
        }
    }

    public static String capitalize(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
                        s.substring(1));
            }
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                if (i + 1 < s.length()) {
                    s = String.format("%s%s%s", s.subSequence(0, i + 1),
                            Character.toUpperCase(s.charAt(i + 1)),
                            s.substring(i + 2));
                }
            }
        }
        return s;
    }

    public static AbstractMap.SimpleEntry<Integer, Integer> getNextInteger(
            ArrayList<Integer> values) {
        ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> frequencies = new ArrayList<>();
        int maxIndex = 0;
        main:
        for (int i = 0; i < values.size(); ++i) {
            int value = values.get(i);
            for (int j = 0; j < frequencies.size(); ++j) {
                if (frequencies.get(j).getKey() == value) {
                    frequencies.get(j).setValue(frequencies.get(j).getValue() + 1);
                    if (frequencies.get(maxIndex).getValue() < frequencies.get(j)
                            .getValue()) {
                        maxIndex = j;
                    }
                    continue main;
                }
            }
            frequencies.add(new AbstractMap.SimpleEntry<Integer, Integer>(value, 1));
        }
        return frequencies.get(maxIndex);
    }

    private static String intToKOrMil(int j) {
        if (j < 0x186a0)
            return String.valueOf(j);
        if (j < 0x989680)
            return j / 1000 + "K";
        else
            return j / 0xf4240 + "M";
    }

    private static void setHighMem() {
        SceneGraph.lowMem = false;
        Rasterizer3D.lowMem = false;
        lowMemory = false;
        MapRegion.lowMem = false;
        ObjectDefinition.lowMemory = false;
    }
    
    public void init() {
    	try {
			nodeID = 10;
			portOffset = 0;
			setHighMem();
			isMembers = true;
			Signlink.startpriv(InetAddress.getLocalHost());
			instance = this;
			initClientFrame(503, 765);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static void main(String args[]) {
        try {
            nodeID = 10;
            portOffset = 0;
            setHighMem();
            isMembers = true;
            Signlink.storeid = 32;
            Signlink.startpriv(InetAddress.getLocalHost());
            instance = new Client();
            instance = new ClientWindow(frameMode.getWidth(), frameMode.getHeight(), frameMode.isResizable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTab(int id) {
        tabId = id;
        tabAreaAltered = true;
    }

    private static String combatDiffColor(int i, int j) {
        int k = i - j;
        if (k < -9)
            return "@red@";
        if (k < -6)
            return "@or3@";
        if (k < -3)
            return "@or2@";
        if (k < 0)
            return "@or1@";
        if (k > 9)
            return "@gre@";
        if (k > 6)
            return "@gr3@";
        if (k > 3)
            return "@gr2@";
        if (k > 0)
            return "@gr1@";
        else
            return "@yel@";
    }

    /**
     * Gets the progress color for the xp bar
     *
     * @param percent
     * @return
     */
    public static int getProgressColor(int percent) {
        if (percent <= 15) {
            return 0x808080;
        }
        if (percent <= 45) {
            return 0x7f7f00;
        }
        if (percent <= 65) {
            return 0x999900;
        }
        if (percent <= 75) {
            return 0xb2b200;
        }
        if (percent <= 90) {
            return 0x007f00;
        }
        return 31744;
    }

    public static int getXPForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static long findSize(String path) {
        long totalSize = 0;
        ArrayList<String> directory = new ArrayList<String>();
        File file = new File(path);

        if (file.isDirectory()) {
            directory.add(file.getAbsolutePath());
            while (directory.size() > 0) {
                String folderPath = directory.get(0);
                directory.remove(0);
                File folder = new File(folderPath);
                File[] filesInFolder = folder.listFiles();
                int noOfFiles = filesInFolder.length;

                for (int i = 0; i < noOfFiles; i++) {
                    File f = filesInFolder[i];
                    if (f.isDirectory()) {
                        directory.add(f.getAbsolutePath());
                    } else {
                        totalSize += f.length();
                    }
                }
            }
        } else {
            totalSize = file.length();
        }
        return totalSize;
    }

    public boolean compareCrc(byte[] buffer, int expectedCrc) {
        CRC.reset();
        CRC.update(buffer);
        int crc = (int) CRC.getValue();
        return crc == expectedCrc;
    }

    public void savePlayerData() {
        try {
            File file = new File(Signlink.getDataDirectory() + File.separator + "/settings.dat");
            if (!file.exists()) {
                file.createNewFile();
            }
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
            if (stream != null) {
                stream.writeBoolean(true); // Accept aid
                stream.writeBoolean(settings[152] == 1); // Run
                stream.writeBoolean(true); // Chat effects
                stream.writeBoolean(true); // Split private chat
                stream.writeBoolean(true); // Mouse buttons
                stream.writeBoolean(Configuration.enableShiftClickDrop);
                stream.writeByte(Configuration.playerAttackOptionPriority);
                stream.writeByte(Configuration.npcAttackOptionPriority);
                stream.writeBoolean(animatedbackground);
                stream.writeBoolean(Configuration.escapeCloseInterface);
                stream.writeBoolean(Configuration.mergeExpDrops);
                stream.writeBoolean(Configuration.expCounterOpen);
                stream.writeDouble(brightnessState);
				for(int i = 0; i < 14; i++) {
					stream.writeByte(Keybinding.KEYBINDINGS[i]);
				}
                stream.writeBoolean(Configuration.enableMusic);
                stream.writeBoolean(Configuration.clientData);
                for(int i = 0; i < Client.instance.hiddenItems.size(); i++) {
					stream.writeUTF("ITEM:" + Client.instance.hiddenItems.get(i));
				}
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPlayerData() throws IOException {
        File file = new File(Signlink.getDataDirectory() + File.separator + "/settings.dat");
        if (!file.exists()) {
            return;
        }
        DataInputStream stream = new DataInputStream(new FileInputStream(file));
        try {
            stream.readBoolean(); // Accept aid
            settings[152] = stream.readBoolean() ? 1 : 0;
            stream.readBoolean(); // Chat effects
            stream.readBoolean(); // Split private chat
            stream.readBoolean(); // Mouse buttons
            Configuration.enableShiftClickDrop = stream.readBoolean();
            Configuration.playerAttackOptionPriority = stream.readByte();
            Configuration.npcAttackOptionPriority = stream.readByte();
            animatedbackground = stream.readBoolean();
            Configuration.escapeCloseInterface = stream.readBoolean();
            Configuration.mergeExpDrops = stream.readBoolean();
            Configuration.expCounterOpen = stream.readBoolean();
            brightnessState = stream.readDouble();
            Rasterizer3D.setBrightness(brightnessState);
			for(int i = 0; i < Keybinding.KEYBINDINGS.length; i++) {
				Keybinding.KEYBINDINGS[i] = stream.readByte();
			}
            Configuration.enableMusic = stream.readBoolean();
            Configuration.clientData = stream.readBoolean();
            String s = "";
			while ((s = stream.readUTF()).contains("ITEM:")) {
				Client.instance.hiddenItems.add(s.split(":")[1]);
			}
        } catch (IOException e) {
            System.out.println("Unable to load player data.");
            file.delete();
        } finally {
            stream.close();
        }
    }

    public void addEffectTimer(EffectTimer et) {

        // Check if exists.. If so, update delay.
        for (EffectTimer timer : effects_list) {
            if (timer.getSprite() == et.getSprite()) {
                timer.setSeconds(et.getSecondsTimer().secondsRemaining());
                return;
            }
        }

        effects_list.add(et);
    }

    public void drawEffectTimers() {
        int yDraw = frameHeight - 195;
        int xDraw = frameWidth - 330;
        for (EffectTimer timer : effects_list) {
            if (timer.getSecondsTimer().finished()) {
                effects_list.remove(timer);
                continue;
            }

            Sprite sprite = spriteCache.lookup(timer.getSprite());

            if (sprite != null) {
                sprite.drawAdvancedSprite(xDraw + 12, yDraw);
                newSmallFont.drawBasicString(calculateInMinutes(timer.getSecondsTimer().secondsRemaining()) + "", xDraw + 40, yDraw + 13, 0xFF8C00);
                yDraw -= 25;
            }
        }
    }

    private String calculateInMinutes(int paramInt) {
        int i = (int) Math.floor(paramInt / 60);
        int j = paramInt - i * 60;
        String str1 = "" + i;
        String str2 = "" + j;
        if (j < 10) {
            str2 = "0" + str2;
        }
        if (i < 10) {
            str1 = "0" + str1;
        }
        return str1 + ":" + str2;
    }

    public boolean shouldDrawCombatBox() {
        if (!UserInterface.COMBAT_OVERLAY.toggled()) {
            return false;
        }
        return currentInteract != null && !combatBoxTimer.finished();
    }

    public void drawCombatBox() {
        // Get health
        int currentHp = currentInteract.currentHealth;
        int maxHp = currentInteract.maxHealth;

        // Make sure the mob isn't dead
        if (currentHp == 0) {
            return;
        }

        // Get name
        String name = null;
        if (currentInteract instanceof Player) {
            name = ((Player) currentInteract).name;
        } else if (currentInteract instanceof Npc) {
            if (((Npc) currentInteract).desc != null) {
                name = ((Npc) currentInteract).desc.name;
            }
        }

        // Make sure the mob has a name
        if (name == null) {
            return;
        }

        // Positioning
        int height = 40;
        int width = 126;
        int xPos = 2;
        int yPos = 18;

        // Draw box
        Rasterizer2D.drawTransparentBox(xPos, yPos, width, height, 0x504a41, 180);
        Rasterizer2D.drawBoxOutline(xPos, yPos, width, height, 0x383023);

        // Draw name
        if (name != null) {
            Client.instance.newRegularFont.drawCenteredString(name, xPos + (width / 2), yPos + 14, 16777215, 0);
        }

        int percent = (int) (((double) currentHp / (double) maxHp) * (width - 9));
        if (percent > (width - 9)) {
            percent = (width - 9);
        }
        // Draw missing health
        Rasterizer2D.drawTransparentBox(xPos + 4, yPos + 19, width - 9, 16, 11740160, 190);
        // Draw existing health
        Rasterizer2D.drawTransparentBox(xPos + 4, yPos + 19, percent, 16, 31744, 190);

        // Draw health
        Client.instance.newSmallFont.drawCenteredString(currentHp + " / " + maxHp, xPos + (width / 2), yPos + 32, 16777215, 0);
    }

    public void processSpawnTab() {
        //Draw checks..
        switch (spawnType) {
            case INVENTORY:
                //Inventory ticks
                Widget.interfaceCache[31007].disabledSprite = spriteCache.lookup(332);
                Widget.interfaceCache[31009].disabledSprite = spriteCache.lookup(333); //Hover

                //Bank ticks
                Widget.interfaceCache[31011].disabledSprite = spriteCache.lookup(334);
                Widget.interfaceCache[31013].disabledSprite = spriteCache.lookup(335); //Hover
                break;
            case BANK:
                //Bank ticks
                Widget.interfaceCache[31011].disabledSprite = spriteCache.lookup(332);
                Widget.interfaceCache[31013].disabledSprite = spriteCache.lookup(333); //Hover

                //Bank ticks
                Widget.interfaceCache[31007].disabledSprite = spriteCache.lookup(334);
                Widget.interfaceCache[31009].disabledSprite = spriteCache.lookup(335); //Hover
                break;
        }

        if (fetchSearchResults) {

            //Reset search results
            for (int i = 0; i < searchResults.length; i++) {
                searchResults[i] = -1;
            }

            //Get new search results
            int totalResults = 0;
            if (searchSyntax.length() >= 2) {
                for (int itemId : ALLOWED_SPAWNS) {
                    final ItemDefinition def = ItemDefinition.lookup(itemId);

                    if (def == null || def.name == null || def.noted_item_id != -1) {
                        continue;
                    }

                    if (def.name.toLowerCase().contains(searchSyntax)) {
                        searchResults[totalResults++] = def.id;
                    }
                }
            }

            //Draw results onto interface
            //Reset text on interface..
            for (int i = 31031; i < 31731; i++) {
                Widget w = Widget.interfaceCache[i];
                w.hidden = true;
            }

            //Send new text on interface..
            int interface_ = 31031;
            final int[] results = getResultsArray();
            for (int def : results) {
                if (def == -1) {
                    continue;
                }
                Widget w = Widget.interfaceCache[interface_];
                w.hidden = false;
                String itemName = ItemDefinition.lookup(def).name;
                if (itemName.length() > 22) {
                    itemName = itemName.substring(0, 22);
                    itemName += "..";
                }
                w.defaultText = itemName;
                interface_++;
                if (interface_ == 31731) {
                    break;
                }
            }

            //Update scroll bar
            Widget.interfaceCache[31030].scrollMax = results.length * 30;

            fetchSearchResults = false;
        }

        //Draw input
        String textInput = "";
        if (searchSyntax.length() > 0) {
            textInput = StringUtils.formatText(searchSyntax);
        }

        if (tick % 25 < 10) {
            textInput += "|";
        }

        Widget.interfaceCache[31003].defaultText = textInput;
    }

    public int[] getResultsArray() {
        return searchSyntax.length() >= 2 ? searchResults :
                ALLOWED_SPAWNS;
    }

    private void addToXPCounter(int skill, int xp) {
        int font_height = 20;
        if (xp <= 0)
            return;

        int lowest_y_off = Integer.MAX_VALUE;
        for (int i = 0; i < xp_added.length; i++)
            if (xp_added[i][0] > -1)
                lowest_y_off = Math.min(lowest_y_off, xp_added[i][2]);

        if (Configuration.mergeExpDrops && lowest_y_off != Integer.MAX_VALUE && lowest_y_off <= 0) {
            for (int i = 0; i < xp_added.length; i++) {
                if (xp_added[i][2] != lowest_y_off)
                    continue;

                xp_added[i][0] |= (1 << skill);
                xp_added[i][1] += xp;
                return;
            }
        } else {
            ArrayList<Integer> list = new ArrayList<Integer>();
            int y = font_height;

            boolean go_on = true;
            while (go_on) {
                go_on = false;

                for (int i = 0; i < xp_added.length; i++) {
                    if (xp_added[i][0] == -1 || list.contains(i))
                        continue;

                    if (xp_added[i][2] < y) {
                        xp_added[i][2] = y;
                        y += font_height;
                        go_on = true;
                        list.add(i);
                    }
                }
            }

            if (lowest_y_off == Integer.MAX_VALUE || lowest_y_off >= font_height)
                lowest_y_off = 0;
            else
                lowest_y_off = 0;

            for (int i = 0; i < xp_added.length; i++)
                if (xp_added[i][0] == -1) {
                    xp_added[i][0] = (1 << skill);
                    xp_added[i][1] = xp;
                    xp_added[i][2] = lowest_y_off;
                    return;
                }
        }
    }

    public void refreshFrameSize() {
        if (frameMode == ScreenMode.RESIZABLE) {
            if (frameWidth != (appletClient() ? getGameComponent().getWidth()
                    : gameFrame.getFrameWidth())) {
                frameWidth = (appletClient() ? getGameComponent().getWidth()
                        : gameFrame.getFrameWidth());
                screenAreaWidth = frameWidth;
                setBounds();
            }
            if (frameHeight != (appletClient() ? getGameComponent().getHeight()
                    : gameFrame.getFrameHeight())) {
                frameHeight = (appletClient() ? getGameComponent().getHeight()
                        : gameFrame.getFrameHeight());
                screenAreaHeight = frameHeight;
                setBounds();
            }
        }
    }

    public boolean getMousePositions() {
        if (mouseInRegion(frameWidth - (frameWidth <= 1000 ? 240 : 420),
                frameWidth, frameHeight - (frameWidth <= 1000 ? 90 : 37), frameHeight)) {
            return false;
        }
        if (showChatComponents) {
            if (changeChatArea && frameMode != ScreenMode.FIXED) {
                if (super.mouseX > 0 && super.mouseX < 494
                        && super.mouseY > frameHeight - 175
                        && super.mouseY < frameHeight) {
                    return true;
                } else {
                    if (super.mouseX > 494 && super.mouseX < 515
                            && super.mouseY > frameHeight - 175
                            && super.mouseY < frameHeight) {
                        return false;
                    }
                }
            } else if (!changeChatArea) {
                if (super.mouseX > 0 && super.mouseX < 519
                        && super.mouseY > frameHeight - 175
                        && super.mouseY < frameHeight) {
                    return false;
                }
            }
        }
        if (mouseInRegion(frameWidth - 216, frameWidth, 0, 172)) {
            return false;
        }
        if (!stackSideStones) {
            if (super.mouseX > 0 && super.mouseY > 0 && super.mouseY < frameWidth
                    && super.mouseY < frameHeight) {
                if (super.mouseX >= frameWidth - 242 && super.mouseY >= frameHeight - 335) {
                    return false;
                }
                return true;
            }
            return false;
        }
        if (showTabComponents) {
            if (frameWidth > 1000) {
                if (super.mouseX >= frameWidth - 420 && super.mouseX <= frameWidth
                        && super.mouseY >= frameHeight - 37
                        && super.mouseY <= frameHeight
                        || super.mouseX > frameWidth - 225 && super.mouseX < frameWidth
                        && super.mouseY > frameHeight - 37 - 274
                        && super.mouseY < frameHeight) {
                    return false;
                }
            } else {
                if (super.mouseX >= frameWidth - 210 && super.mouseX <= frameWidth
                        && super.mouseY >= frameHeight - 74
                        && super.mouseY <= frameHeight
                        || super.mouseX > frameWidth - 225 && super.mouseX < frameWidth
                        && super.mouseY > frameHeight - 74 - 274
                        && super.mouseY < frameHeight) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean mouseInRegion(int x1, int x2, int y1, int y2) {
        if (super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1 && super.mouseY <= y2) {
            return true;
        }
        return false;
    }

    public boolean mouseMapPosition() {
        if (super.mouseX >= frameWidth - 21 && super.mouseX <= frameWidth && super.mouseY >= 0
                && super.mouseY <= 21) {
            return false;
        }
        return true;
    }

    private void drawLoadingMessages(int used, String s, String s1) {
        int width = regularText.getTextWidth(used == 1 ? s : s1);
        int height = s1 == null ? 25 : 38;
        Rasterizer2D.drawBox(1, 1, width + 6, height, 0);
        Rasterizer2D.drawBox(1, 1, width + 6, 1, 0xffffff);
        Rasterizer2D.drawBox(1, 1, 1, height, 0xffffff);
        Rasterizer2D.drawBox(1, height, width + 6, 1, 0xffffff);
        Rasterizer2D.drawBox(width + 6, 1, 1, height, 0xffffff);
        regularText.drawText(0xffffff, s, 18, width / 2 + 5);
        if (s1 != null) {
            regularText.drawText(0xffffff, s1, 31, width / 2 + 5);
        }
    }

    public final String formatCoins(int coins) {
        if (coins >= 0 && coins < 10000)
            return String.valueOf(coins);
        if (coins >= 10000 && coins < 10000000)
            return coins / 1000 + "K";
        if (coins >= 10000000 && coins < 999999999)
            return coins / 1000000 + "M";
        if (coins >= 999999999)
            return "*";
        else
            return "?";
    }

    private boolean menuHasAddFriend(int j) {
        if (j < 0)
            return false;
        int k = menuActionTypes[j];
        if (k >= 2000)
            k -= 2000;
        return k == 337;
    }

    public void drawChannelButtons() {
        final int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 165;
        spriteCache.lookup(49).drawSprite(0, 143 + yOffset);
        String text[] = {"On", "Friends", "Off", "Hide"};
        int textColor[] = {65280, 0xffff00, 0xff0000, 65535};
        switch (cButtonCPos) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                spriteCache.lookup(16).drawSprite(channelButtonsX[cButtonCPos], 143 + yOffset);
                break;
        }
        if (cButtonHPos == cButtonCPos) {
            switch (cButtonHPos) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    spriteCache.lookup(17).drawSprite(channelButtonsX[cButtonHPos], 143 + yOffset);
                    break;
            }
        } else {
            switch (cButtonHPos) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    spriteCache.lookup(15).drawSprite(channelButtonsX[cButtonHPos], 143 + yOffset);
                    break;
                case 7:
                    spriteCache.lookup(18).drawSprite(channelButtonsX[cButtonHPos], 143 + yOffset);
                    break;
            }
        }
        int[] modes = {publicChatMode, privateChatMode, clanChatMode, tradeMode, yellMode};
        for (int i = 0; i < modeNamesX.length; i++) {
            smallText.drawTextWithPotentialShadow(true, modeNamesX[i], 0xffffff, modeNames[i], modeNamesY[i] + yOffset);
        }
        for (int i = 0; i < modeX.length; i++) {
            smallText.method382(textColor[modes[i]], modeX[i], text[modes[i]], 164 + yOffset, true);
        }
    }

    private boolean chatStateCheck() {
        return messagePromptRaised || inputDialogState != 0 || clickToContinueString != null || backDialogueId != -1 || dialogueId != -1;
    }

    public String getPrefix(int rights) {
		return "@cr" + rights + "@";
	}

	public String getPrefixMode(int mode) {
		return "@cl" + mode + "@";
	}

	public boolean showChatboxArea;
	
    private void drawChatArea() {
        int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 165;
        if (frameMode == ScreenMode.FIXED) {
            chatboxImageProducer.initDrawingArea();
        }
        Rasterizer3D.scanOffsets = anIntArray1180;
        if (chatStateCheck()) {
            showChatComponents = true;
            spriteCache.lookup(20).drawSprite(0, yOffset);
        }
        if (showChatComponents) {
            if ((changeChatArea && frameMode != ScreenMode.FIXED) && !chatStateCheck()) {
                Rasterizer2D.drawHorizontalLine(7, 7 + yOffset, 506, 0x575757);
                Rasterizer2D.fillGradientRectangle(7, 7 + yOffset, 510, 130, 0x00000000, 0x5A000000);
            } else {
                spriteCache.lookup(20).drawSprite(0, yOffset);
            }
        }
        drawChannelButtons();
        GameFont font = regularText;
        if (messagePromptRaised) {
        	showChatboxArea = false;
        	newBoldFont.drawCenteredString(aString1121, 259, 60 + yOffset, 0, -1);
            newBoldFont.drawCenteredString(promptInput + "*", 259, 80 + yOffset, 128, -1);
        } else if (inputDialogState == 1) {
        	showChatboxArea = false;
        	newBoldFont.drawCenteredString(enter_amount_title, 259, yOffset + 60, 0, -1);
            newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128,
                    -1);
        } else if (inputDialogState == 2) {
        	showChatboxArea = false;
        	newBoldFont.drawCenteredString(enter_name_title, 259, 60 + yOffset, 0, -1);
            newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128,
                    -1);
        } else if (clickToContinueString != null) {
        	showChatboxArea = false;
        	newBoldFont.drawCenteredString(clickToContinueString, 259, 60 + yOffset, 0, -1);
            newBoldFont.drawCenteredString("Click to continue", 259, 80 + yOffset, 128, -1);
        } else if (backDialogueId != -1) {
            try {
                drawInterface(0, 20, Widget.interfaceCache[backDialogueId], 20 + yOffset);
            } catch (Exception ex) {

            }
        } else if (chatboxDrawing != null) {
			showChatboxArea = false;
			chatboxDrawing.draw(spriteCache.lookup(20).myWidth, spriteCache.lookup(20).myHeight, isFixed() ? 0 : frameHeight - 160);
        } else if (dialogueId != -1) {
            try {
                drawInterface(0, 20, Widget.interfaceCache[dialogueId], 20 + yOffset);
            } catch (Exception ex) {

            }
        } else if (showChatComponents) {
        	showChatboxArea = true;
            int j77 = -3;
            int j = 0;
            int shadow = (changeChatArea && frameMode != ScreenMode.FIXED) ? 0 : -1;
            Rasterizer2D.setDrawingArea(122 + yOffset, 8, 497, 7 + yOffset);
            for (int k = 0; k < 500; k++) {
                if (chatMessages[k] != null) {
                    int chatType = chatTypes[k];
                    int yPos = (70 - j77 * 14) + anInt1089 + 5;
                    String s1 = chatNames[k];
                    int playerRights = 0, gameMode = 0;
					if (s1 != null && s1.startsWith("@cr")) {
						int test1 = Integer.parseInt("" + s1.charAt(3));
						if (s1.charAt(4) != '@') {
							test1 = Integer.parseInt(s1.charAt(3) + ""
									+ s1.charAt(4));
							s1 = s1.substring(6);
						} else {
							s1 = s1.substring(5);
						}
						playerRights = (byte) test1;
					}
					if (s1 != null && s1.contains("@cl")) {
						int test2 = Integer.parseInt("" + s1.charAt(3));
						if (s1.charAt(4) != '@') {
							test2 = Integer.parseInt(s1.charAt(3) + ""
									+ s1.charAt(4));
							s1 = s1.substring(6);
						} else {
							s1 = s1.substring(5);
						}
						gameMode = (byte) test2;
					}
                    if (chatType == 0) {
                        if (chatTypeView == 5 || chatTypeView == 0) {
                            newRegularFont.drawBasicString(chatMessages[k], 11,
                                    yPos + yOffset, (changeChatArea && frameMode != ScreenMode.FIXED) ? 0xFFFFFF : 0, shadow);
                            j++;
                            j77++;
                        }
                    }
                    if ((chatType == 1 || chatType == 2) && (chatType == 1
                            || publicChatMode == 0
                            || publicChatMode == 1 && isFriendOrSelf(s1))) {
                        if (chatTypeView == 1 || chatTypeView == 0) {
                            int xPos = 11;

                            if (!UserInterface.DISABLE_CHAT_ICONS.toggled()) {
								if (playerRights >= 1) {
									modIcons[playerRights - 1]
											.drawAdvancedSprite(xPos - 1, yPos
													- 12 + yOffset);
									xPos += 16;
								}
								if (gameMode != 0) {
									modeIcons[gameMode - 1].drawAdvancedSprite(
											xPos + -1, yPos - 12 + yOffset);
									xPos += 15;
								}
							}

                            newRegularFont.drawBasicString(s1 + ":", xPos,
                                    yPos + yOffset, (changeChatArea && frameMode != ScreenMode.FIXED) ? 0xFFFFFF : 0, shadow);
                            xPos += font.getTextWidth(s1) + 8;
                            newRegularFont.drawBasicString(chatMessages[k], xPos,
                                    yPos + yOffset,
                                    (changeChatArea && frameMode != ScreenMode.FIXED) ? 0x7FA9FF : 255, shadow);
                            j++;
                            j77++;
                        }
                    }
                    if ((chatType == 3 || chatType == 7)
                            && (splitPrivateChat == 0 || chatTypeView == 2)
                            && (chatType == 7 || privateChatMode == 0
                            || privateChatMode == 1
                            && isFriendOrSelf(s1))) {
                        if (chatTypeView == 2 || chatTypeView == 0) {
                            int k1 = 11;
                            newRegularFont.drawBasicString("From", k1, yPos + yOffset,
                                    (changeChatArea && frameMode != ScreenMode.FIXED) ? 0 : 0xFFFFFF, shadow);
                            k1 += font.getTextWidth("From ");

                            if (!UserInterface.DISABLE_CHAT_ICONS.toggled()) {
								if (playerRights != 0) {
									modIcons[playerRights - 1]
											.drawAdvancedSprite(k1 - 1, yPos
													- 12 + yOffset);
									k1 += 12;
								}
								if (gameMode != 0) {
									modeIcons[gameMode - 1].drawAdvancedSprite(
											k1 + -1, yPos - 12 + yOffset);
									k1 += 12;
								}
							}

                            newRegularFont.drawBasicString(s1 + ":", k1,
                                    yPos + yOffset, (changeChatArea && frameMode != ScreenMode.FIXED) ? 0xFFFFFF : 0, shadow);
                            k1 += font.getTextWidth(s1) + 8;
                            newRegularFont.drawBasicString(chatMessages[k], k1,
                                    yPos + yOffset, 0x800080, shadow);
                            j++;
                            j77++;
                        }
                    }
                    if (chatType == 4 && (tradeMode == 0
                            || tradeMode == 1 && isFriendOrSelf(s1))) {
                        if (chatTypeView == 3 || chatTypeView == 0) {
                            newRegularFont.drawBasicString(s1 + " " + chatMessages[k],
                                    11, yPos + yOffset, 0x800080, shadow);
                            j++;
                            j77++;
                        }
                    }
                    if (chatType == 5 && splitPrivateChat == 0 && privateChatMode < 2) {
                        if (chatTypeView == 2 || chatTypeView == 0) {
                            newRegularFont.drawBasicString(s1 + " " + chatMessages[k],
                                    11, yPos + yOffset, 0x800080, shadow);
                            j++;
                            j77++;
                        }
                    }
                    if (chatType == 6 && (splitPrivateChat == 0 || chatTypeView == 2)
                            && privateChatMode < 2) {
                        if (chatTypeView == 2 || chatTypeView == 0) {
                            newRegularFont.drawBasicString("To " + s1 + ":", 11,
                                    yPos + yOffset, changeChatArea ? 0xFFFFFF : 0,
                                    shadow);
                            newRegularFont.drawBasicString(chatMessages[k],
                                    15 + font.getTextWidth("To :" + s1),
                                    yPos + yOffset, 0x800080, shadow);
                            j++;
                            j77++;
                        }
                    }
                    if (chatType == 8 && (tradeMode == 0
                            || tradeMode == 1 && isFriendOrSelf(s1))) {
                        if (chatTypeView == 3 || chatTypeView == 0) {
                            newRegularFont.drawBasicString(s1 + " " + chatMessages[k],
                                    11, yPos + yOffset, 0x7e3200, shadow);
                            j++;
                            j77++;
                        }
                        if (chatType == 11 && (clanChatMode == 0)) {
                            if (chatTypeView == 11) {
                                newRegularFont.drawBasicString(
                                        s1 + " " + chatMessages[k], 11,
                                        yPos + yOffset, 0x7e3200, shadow);
                                j++;
                                j77++;
                            }
                            if (chatType == 12) {
                                newRegularFont.drawBasicString(chatMessages[k] + "",
                                        11, yPos + yOffset, 0x7e3200, shadow);
                                j++;
                            }
                        }
                    }
                    if (chatType == 16) {
                        if (chatTypeView == 11 || chatTypeView == 0) {

                            newRegularFont.drawBasicString(chatMessages[k], 10,
                                    yPos + yOffset, 0x800080, shadow);

                            j++;
                            j77++;
                        }
                    }
                }
            }
            Rasterizer2D.defaultDrawingAreaSize();
            scrollBarPosition = j * 14 + 7 + 5;
            if (scrollBarPosition < 111) {
                scrollBarPosition = 111;
            }
            drawScrollbar(114, scrollBarPosition - anInt1089 - 113, 7 + yOffset, 496, scrollBarPosition, (changeChatArea && frameMode != ScreenMode.FIXED));
            String s;
            if (localPlayer != null && localPlayer.name != null) {
                s = localPlayer.name;
            } else {
                s = StringUtils.formatText(capitalize(myUsername));
            }
            Rasterizer2D.setDrawingArea(140 + yOffset, 8, 509, 120 + yOffset);
            int xOffset = 0;
            if (myPrivilege > 0 && myPrivilege < 14) {
                modIcons[myPrivilege - 1].drawAdvancedSprite(10, 122 + yOffset);
                xOffset += 14;
            }
            if (myMode > 0 && myMode < 3) {
                modeIcons[myMode - 1].drawAdvancedSprite(25, 122 + yOffset);
                xOffset += 17;
            }
            
            //draw players username
            newRegularFont.drawBasicString(s + "     : ", xOffset + 11, 133 + yOffset,
                    (changeChatArea && frameMode != ScreenMode.FIXED) ? 0xFFFFFF : 0, shadow);
            
            spriteCache.lookup(1204).drawAdvancedSprite(xOffset + font.getTextWidth(s + "  : "), 122 + yOffset);
            teleportX = xOffset + font.getTextWidth(s + "  : ");
            teleportY = (this.myHeight-42);
            xOffset += 17;
            
            //Draw players typed text
            newRegularFont.drawBasicString(inputString + "*",
                    xOffset + 14 + font.getTextWidth(s + ":"), 133 + yOffset,
                    (changeChatArea && frameMode != ScreenMode.FIXED) ? 0x7FA9FF : 255, shadow);
            Rasterizer2D.drawHorizontalLine(7, 121 + yOffset, 506, (changeChatArea && frameMode != ScreenMode.FIXED) ? 0x575757 : 0x807660);
            Rasterizer2D.defaultDrawingAreaSize();
        }
        if (menuOpen) {
            drawMenu(0, frameMode == ScreenMode.FIXED ? 338 : 0);
        }
        if (frameMode == ScreenMode.FIXED) {
            chatboxImageProducer.drawGraphics(338, super.graphics, 0);
        }
        gameScreenImageProducer.initDrawingArea();
        Rasterizer3D.scanOffsets = anIntArray1182;
    }

    private int teleportX = -1;
    private int teleportY = -1;
    
    /**
     * Initializes the client for startup
     */
    public void initialize() {
        try {
            nodeID = 10;
            portOffset = 0;
            setHighMem();
            isMembers = true;
            Signlink.storeid = 32;
            Signlink.startpriv(InetAddress.getLocalHost());
            initClientFrame(frameWidth, frameHeight);
            instance = this;
        } catch (Exception exception) {
            return;
        }
    }

    public void startRunnable(Runnable runnable, int priority) {
        if (priority > 10)
            priority = 10;
        if (Signlink.mainapp != null) {
            Signlink.startthread(runnable, priority);
        } else {
            super.startRunnable(runnable, priority);
        }
    }

    public Socket openSocket(int port) throws IOException {
        return new Socket(InetAddress.getByName(server), port);
    }

    private void processMenuClick() {
        if (activeInterfaceType != 0)
            return;
        int j = super.clickMode3;
        if (spellSelected == 1 && super.saveClickX >= 516 && super.saveClickY >= 160
                && super.saveClickX <= 765 && super.saveClickY <= 205)
            j = 0;
        if (menuOpen) {
            if (j != 1) {
                int k = super.mouseX;
                int j1 = super.mouseY;
                if (menuScreenArea == 0) {
                    k -= 4;
                    j1 -= 4;
                }
                if (menuScreenArea == 1) {
                    k -= 519;
                    j1 -= 168;
                }
                if (menuScreenArea == 2) {
                    k -= 17;
                    j1 -= 338;
                }
                if (menuScreenArea == 3) {
                    k -= 519;
                    j1 -= 0;
                }
                if (k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10
                        || j1 < menuOffsetY - 10
                        || j1 > menuOffsetY + menuHeight + 10) {
                    menuOpen = false;
                    if (menuScreenArea == 1) {
                    }
                    if (menuScreenArea == 2)
                        updateChatbox = true;
                }
            }
            if (j == 1) {
                int l = menuOffsetX;
                int k1 = menuOffsetY;
                int i2 = menuWidth;
                int k2 = super.saveClickX;
                int l2 = super.saveClickY;
                switch (menuScreenArea) {
                    case 0:
                        k2 -= 4;
                        l2 -= 4;
                        break;
                    case 1:
                        k2 -= 519;
                        l2 -= 168;
                        break;
                    case 2:
                        k2 -= 5;
                        l2 -= 338;
                        break;
                    case 3:
                        k2 -= 519;
                        l2 -= 0;
                        break;
                }
                int i3 = -1;
                for (int j3 = 0; j3 < menuActionRow; j3++) {
                    int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
                    if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3)
                        i3 = j3;
                }
                if (i3 != -1)
                    processMenuActions(i3);
                menuOpen = false;
                if (menuScreenArea == 1) {
                }
                if (menuScreenArea == 2) {
                    updateChatbox = true;
                }
            }
        } else {
            if (j == 1 && menuActionRow > 0) {
                int i1 = menuActionTypes[menuActionRow - 1];
                if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74
                        || i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447
                        || i1 == 1125) {
                    int l1 = firstMenuAction[menuActionRow - 1];
                    int j2 = secondMenuAction[menuActionRow - 1];
                    Widget class9 = Widget.interfaceCache[j2];
                    if (class9.allowSwapItems || class9.replaceItems) {
                        aBoolean1242 = false;
                        dragItemDelay = 0;
                        anInt1084 = j2;
                        anInt1085 = l1;
                        activeInterfaceType = 2;
                        anInt1087 = super.saveClickX;
                        anInt1088 = super.saveClickY;
                        if (Widget.interfaceCache[j2].parent == openInterfaceId)
                            activeInterfaceType = 1;
                        if (Widget.interfaceCache[j2].parent == backDialogueId)
                            activeInterfaceType = 3;
                        return;
                    }
                }
            }
            if (j == 1 && (anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1))
                    && menuActionRow > 2)
                j = 2;
            if (j == 1 && menuActionRow > 0)
                processMenuActions(menuActionRow - 1);
            if (j == 2 && menuActionRow > 0)
                determineMenuSize();
            processMainScreenClick();
            processTabClick();
            processChatModeClick();
            minimapHovers();
        }
    }

    private void saveMidi(boolean flag, byte abyte0[]) {
        Signlink.fadeMidi = flag ? 1 : 0;
        Signlink.saveMidi(abyte0, abyte0.length);
    }

    public void loadRegion() {
        try {
            lastKnownPlane = -1;
            incompleteAnimables.clear();
            projectiles.clear();
            Rasterizer3D.clearTextureCache();
            unlinkCaches();
            scene.initToNull();
            System.gc();
            for (int i = 0; i < 4; i++)
                collisionMaps[i].setDefault();
            for (int l = 0; l < 4; l++) {
                for (int k1 = 0; k1 < 104; k1++) {
                    for (int j2 = 0; j2 < 104; j2++)
                        tileFlags[l][k1][j2] = 0;
                }
            }

            MapRegion objectManager = new MapRegion(tileFlags, tileHeights);
            int k2 = terrainData.length;
            sendPacket(new BasicPing().create());

            if (!requestMapReconstruct) {
                for (int i3 = 0; i3 < k2; i3++) {
                    int i4 = (mapCoordinates[i3] >> 8) * 64 - regionBaseX;
                    int k5 = (mapCoordinates[i3] & 0xff) * 64 - regionBaseY;
                    byte abyte0[] = terrainData[i3];
                    if (abyte0 != null)
                        objectManager.method180(abyte0, k5, i4, (currentRegionX - 6) * 8, (currentRegionY - 6) * 8,
                                collisionMaps);
                }
                for (int j4 = 0; j4 < k2; j4++) {
                    int l5 = (mapCoordinates[j4] >> 8) * 64 - regionBaseX;
                    int k7 = (mapCoordinates[j4] & 0xff) * 64 - regionBaseY;
                    byte abyte2[] = terrainData[j4];
                    if (abyte2 == null && currentRegionY < 800)
                        objectManager.initiateVertexHeights(k7, 64, 64, l5);
                }
				/*
				 * anInt1097++; if (anInt1097 > 160) { anInt1097 = 0;
				 * //anticheat? outgoing.writeOpcode(238);
				 * outgoing.writeByte(96); }
				 */
                sendPacket(new BasicPing().create());
                for (int i6 = 0; i6 < k2; i6++) {
                    byte abyte1[] = objectData[i6];
                    if (abyte1 != null) {
                        int l8 = (mapCoordinates[i6] >> 8) * 64 - regionBaseX;
                        int k9 = (mapCoordinates[i6] & 0xff) * 64 - regionBaseY;
                        objectManager.method190(l8, collisionMaps, k9, scene, abyte1);
                    }
                }
            } else {
                for (int plane = 0; plane < 4; plane++) {
                    for (int x = 0; x < 13; x++) {
                        for (int y = 0; y < 13; y++) {
                            int chunkBits = constructRegionData[plane][x][y];
                            if (chunkBits != -1) {
                                int z = chunkBits >> 24 & 3;
                                int rotation = chunkBits >> 1 & 3;
                                int xCoord = chunkBits >> 14 & 0x3ff;
                                int yCoord = chunkBits >> 3 & 0x7ff;
                                int mapRegion = (xCoord / 8 << 8) + yCoord / 8;
                                for (int idx = 0; idx < mapCoordinates.length; idx++) {
                                    if (mapCoordinates[idx] != mapRegion || terrainData[idx] == null)
                                        continue;
                                    objectManager.loadMapChunk(z, rotation, collisionMaps, x * 8, (xCoord & 7) * 8,
                                            terrainData[idx], (yCoord & 7) * 8, plane, y * 8);
                                    break;
                                }

                            }
                        }
                    }
                }
                for (int xChunk = 0; xChunk < 13; xChunk++) {
                    for (int yChunk = 0; yChunk < 13; yChunk++) {
                        int tileBits = constructRegionData[0][xChunk][yChunk];
                        if (tileBits == -1)
                            objectManager.initiateVertexHeights(yChunk * 8, 8, 8, xChunk * 8);
                    }
                }

                sendPacket(new BasicPing().create());
                for (int chunkZ = 0; chunkZ < 4; chunkZ++) {
                    for (int chunkX = 0; chunkX < 13; chunkX++) {
                        for (int chunkY = 0; chunkY < 13; chunkY++) {
                            int tileBits = constructRegionData[chunkZ][chunkX][chunkY];
                            if (tileBits != -1) {
                                int plane = tileBits >> 24 & 3;
                                int rotation = tileBits >> 1 & 3;
                                int coordX = tileBits >> 14 & 0x3ff;
                                int coordY = tileBits >> 3 & 0x7ff;
                                int mapRegion = (coordX / 8 << 8) + coordY / 8;
                                for (int idx = 0; idx < mapCoordinates.length; idx++) {
                                    if (mapCoordinates[idx] != mapRegion || objectData[idx] == null)
                                        continue;
                                    objectManager.readObjectMap(collisionMaps, scene, plane, chunkX * 8,
                                            (coordY & 7) * 8, chunkZ, objectData[idx], (coordX & 7) * 8, rotation,
                                            chunkY * 8);
                                    break;
                                }
                            }
                        }
                    }
                }
                requestMapReconstruct = false;
            }
            sendPacket(new BasicPing().create());
            objectManager.createRegionScene(collisionMaps, scene);
            gameScreenImageProducer.initDrawingArea();
            sendPacket(new BasicPing().create());
            int k3 = MapRegion.maximumPlane;
            if (k3 > plane)
                k3 = plane;
            if (k3 < plane - 1)
                k3 = plane - 1;
            if (lowMemory)
                scene.method275(MapRegion.maximumPlane);
            else
                scene.method275(0);
            for (int i5 = 0; i5 < 104; i5++) {
                for (int i7 = 0; i7 < 104; i7++)
                    updateGroundItems(i5, i7);

            }

            anInt1051++;
            if (anInt1051 > 98) {
                anInt1051 = 0;
                // anticheat?
                // outgoing.writeOpcode(150);
            }

            clearObjectSpawnRequests();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ObjectDefinition.baseModels.clear();
        if (super.gameFrame != null) {
            sendPacket(new RegionChange().create());
        }
        if (lowMemory && Signlink.cache_dat != null) {
            int j = resourceProvider.getVersionCount(0);
            for (int i1 = 0; i1 < j; i1++) {
                int l1 = resourceProvider.getModelIndex(i1);
                if ((l1 & 0x79) == 0)
                    Model.method461(i1);
            }

        }
        System.gc();
        Rasterizer3D.initiateRequestBuffers();
        resourceProvider.clearExtras();

        int startRegionX = (currentRegionX - 6) / 8 - 1;
        int endRegionX = (currentRegionX + 6) / 8 + 1;
        int startRegionY = (currentRegionY - 6) / 8 - 1;
        int endRegionY = (currentRegionY + 6) / 8 + 1;
        for (int regionX = startRegionX; regionX <= endRegionX; regionX++) {
            for (int regionY = startRegionY; regionY <= endRegionY; regionY++) {
                if (regionX == startRegionX || regionX == endRegionX || regionY == startRegionY
                        || regionY == endRegionY) {
                    int floorMapId = resourceProvider.resolve(0, regionY, regionX);
                    if (floorMapId != -1) {
                        resourceProvider.loadExtra(floorMapId, 3);
                    }
                    int objectMapId = resourceProvider.resolve(1, regionY, regionX);
                    if (objectMapId != -1) {
                        resourceProvider.loadExtra(objectMapId, 3);
                    }
                }
            }
        }
    }

    private void unlinkCaches() {
        ObjectDefinition.baseModels.clear();
        ObjectDefinition.models.clear();
        NpcDefinition.modelCache.clear();
        ItemDefinition.models.clear();
        ItemDefinition.sprites.clear();
        Player.models.clear();
        Graphic.models.clear();
    }

    private void renderMapScene(int plane) {
        int pixels[] = minimapImage.myPixels;
        int length = pixels.length;

        for (int pixel = 0; pixel < length; pixel++) {
            pixels[pixel] = 0;
        }


        for (int y = 1; y < 103; y++) {
            int i1 = 24628 + (103 - y) * 512 * 4;
            for (int x = 1; x < 103; x++) {
                if ((tileFlags[plane][x][y] & 0x18) == 0)
                    scene.drawTileOnMinimapSprite(pixels, i1, plane, x, y);
                if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0)
                    scene.drawTileOnMinimapSprite(pixels, i1, plane + 1, x, y);
                i1 += 4;
            }

        }

        int j1 = 0xFFFFFF;
        int l1 = 0xEE0000;
        minimapImage.init();

        for (int y = 1; y < 103; y++) {
            for (int x = 1; x < 103; x++) {
                if ((tileFlags[plane][x][y] & 0x18) == 0)
                    drawMapScenes(y, j1, x, l1, plane);
                if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0)
                    drawMapScenes(y, j1, x, l1, plane + 1);
            }

        }

        gameScreenImageProducer.initDrawingArea();
        anInt1071 = 0;

        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                int id = scene.getGroundDecorationUid(plane, x, y);
                if (id != 0) {
                    id = id >> 14 & 0x7fff;

                    int function = ObjectDefinition.lookup(id).minimapFunction;
                    if (function >= 0) {
                        int viewportX = x;
                        int viewportY = y;
                        minimapHint[anInt1071] = mapFunctions[function];
                        minimapHintX[anInt1071] = viewportX;
                        minimapHintY[anInt1071] = viewportY;
                        anInt1071++;
                    }
                }
            }

        }

    }

    private void updateGroundItems(int i, int j) {
        Deque class19 = groundItems[plane][i][j];
        if (class19 == null) {
            scene.removeGroundItemTile(plane, i, j);
            return;
        }
        int k = 0xfa0a1f01;
        Object obj = null;
        for (Item item = (Item) class19.reverseGetFirst(); item != null; item =
                (Item) class19.reverseGetNext()) {
            ItemDefinition itemDef = ItemDefinition.lookup(item.ID);
            int l = itemDef.value;
            if (itemDef.stackable)
                l *= item.itemCount + 1;
            // notifyItemSpawn(item, i + baseX, j + baseY);

            if (l > k) {
                k = l;
                obj = item;
            }
        }

        class19.insertTail(((Linkable) (obj)));
        Object obj1 = null;
        Object obj2 = null;
        for (Item class30_sub2_sub4_sub2_1 = (Item) class19
                .reverseGetFirst(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 =
                     (Item) class19.reverseGetNext()) {
            if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID && obj1 == null)
                obj1 = class30_sub2_sub4_sub2_1;
            if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID
                    && class30_sub2_sub4_sub2_1.ID != ((Item) (obj1)).ID && obj2 == null)
                obj2 = class30_sub2_sub4_sub2_1;
        }

        int i1 = i + (j << 7) + 0x60000000;
        scene.addGroundItemTile(i, i1, ((Renderable) (obj1)),
                getCenterHeight(plane, j * 128 + 64, i * 128 + 64), ((Renderable) (obj2)),
                ((Renderable) (obj)), plane, j);
    }

    private boolean prioritizedNpc(Npc npc) {

        //Check if it's being interacted with
        if (localPlayer.interactingEntity != -1 &&
                localPlayer.interactingEntity < 32768) {
            if (npc.index == localPlayer.interactingEntity) {
                return true;
            }
        }

        if (npc.desc == null) {
            return false;
        }

        return npc.desc.priorityRender;
    }

    private void showPrioritizedNPCs() {
        for (int index = 0; index < npcCount; index++) {
            Npc npc = npcs[npcIndices[index]];

            if (prioritizedNpc(npc)) {
                showNpc(npc, index, npc.desc.priorityRender);
            }
        }
    }

    private void showOtherNpcs() {
        for (int index = 0; index < npcCount; index++) {
            Npc npc = npcs[npcIndices[index]];
            showNpc(npc, index, false);
        }
    }

    private boolean showNpc(Npc npc, int index, boolean priorityRender) {
        int k = 0x20000000 + (npcIndices[index] << 14);
        if (npc == null || !npc.isVisible() || npc.desc.priorityRender != priorityRender)
            return false;
        int l = npc.x >> 7;
        int i1 = npc.y >> 7;
        if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104)
            return false;
        if (npc.size == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
            if (anIntArrayArray929[l][i1] == anInt1265)
                return false;
            anIntArrayArray929[l][i1] = anInt1265;
        }
        if (!npc.desc.clickable)
            k += 0x80000000;
        scene.addAnimableA(plane, npc.orientation, getCenterHeight(plane, npc.y, npc.x), k, npc.y,
                (npc.size - 1) * 64 + 60, npc.x, npc, npc.animationStretches);
        return true;
    }

    public void drawHoverBox(int xPos, int yPos, String text) {
        String[] results = text.split("\n");
        int height = (results.length * 16) + 6;
        int width;
        width = regularText.getTextWidth(results[0]) + 6;
        for (int i = 1; i < results.length; i++)
            if (width <= regularText.getTextWidth(results[i]) + 6)
                width = regularText.getTextWidth(results[i]) + 6;
        Rasterizer2D.drawBox(xPos, yPos, width, height, 0xFFFFA0);
        Rasterizer2D.drawBoxOutline(xPos, yPos, width, height, 0);
        yPos += 14;
        for (int i = 0; i < results.length; i++) {
        	regularText.drawTextWithPotentialShadow(false, xPos + 3, 0, results[i], yPos);
            yPos += 16;
        }
    }
    
    private void unpackModIcons() {
		modIcons[0] = spriteCache.lookup(690);
		modIcons[1] = spriteCache.lookup(691);
		modIcons[2] = spriteCache.lookup(694);
		modIcons[3] = spriteCache.lookup(693);
		modIcons[4] = spriteCache.lookup(692);
		modIcons[5] = spriteCache.lookup(689);
		modIcons[6] = spriteCache.lookup(682);
		modIcons[7] = spriteCache.lookup(683);
		modIcons[8] = spriteCache.lookup(684);
		modIcons[9] = spriteCache.lookup(685);
		modIcons[10] = spriteCache.lookup(686);
		modIcons[11] = spriteCache.lookup(687);
		modIcons[12] = spriteCache.lookup(688);
	}

	private void unpackModeIcons() {
		modeIcons[0] = spriteCache.lookup(695);
		modeIcons[1] = spriteCache.lookup(696);
		modeIcons[2] = spriteCache.lookup(697);

	}

    private void buildInterfaceMenu(int i, Widget widget, int k, int l, int i1, int j1) {
        if (widget == null || widget.type != 0 || widget.children == null || widget.invisible || widget.hidden)
            return;
        if (k < i || i1 < l || k > i + widget.width || i1 > l + widget.height)
            return;
        int size = widget.children.length;
        for (int l1 = 0; l1 < size; l1++) {
            int _x = widget.childX[l1] + i;
            int _y = (widget.childY[l1] + l) - j1;
            Widget childInterface = Widget.interfaceCache[widget.children[l1]];
            if (childInterface == null || childInterface.hidden) {
                continue;
            }
            _x += childInterface.horizontalOffset;
            _y += childInterface.verticalOffset;
            if ((childInterface.hoverType >= 0 || childInterface.defaultHoverColor != 0)
                    && k >= _x && i1 >= _y && k < _x + childInterface.width
                    && i1 < _y + childInterface.height)
                if (childInterface.hoverType >= 0)
                    interfaceHoverType = childInterface.hoverType;
                else
                    interfaceHoverType = childInterface.id;
            if (childInterface.type == 8 && k >= _x && i1 >= _y
                    && k < _x + childInterface.width && i1 < _y + childInterface.height) {
                interfaceId = childInterface.id;
            }
            if (childInterface.type == Widget.TYPE_CONTAINER) {
                buildInterfaceMenu(_x, childInterface, k, _y, i1, childInterface.scrollPosition);
                if (childInterface.scrollMax > childInterface.height)
                    method65(_x + childInterface.width, childInterface.height, k, i1,
                            childInterface, _y, true, childInterface.scrollMax);
            } else {
                if (childInterface.optionType == Widget.OPTION_OK && k >= _x && i1 >= _y && k < _x + childInterface.width && i1 < _y + childInterface.height) {
                    boolean flag = false;

                    if (childInterface.contentType != 0 && (childInterface.parent == 5065 || childInterface.parent == 5715))
                        flag = buildFriendsListMenu(childInterface);

                    if (childInterface.tooltip == null || childInterface.tooltip.length() == 0) {
                        flag = true;
                    }

                    if (!flag) {
                        if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                            menuActionText[menuActionRow] = childInterface.tooltip + " @lre@" + childInterface.id;
                            menuActionTypes[menuActionRow] = 315;
                            secondMenuAction[menuActionRow] = childInterface.id;
                            menuActionRow++;
                        } else {
                            menuActionText[menuActionRow] = childInterface.tooltip;
                            menuActionTypes[menuActionRow] = 315;
                            secondMenuAction[menuActionRow] = childInterface.id;
                            menuActionRow++;
                        }
                    }
                    if (childInterface.type == Widget.TYPE_HOVER || childInterface.type == Widget.TYPE_CONFIG_HOVER || childInterface.type == Widget.TYPE_ADJUSTABLE_CONFIG
                            || childInterface.type == Widget.TYPE_BOX) {
                        childInterface.toggled = true;
                    }
                } else if (childInterface.optionType == Widget.OPTION_CLOSE && k >= _x && i1 >= _y && k < _x + childInterface.width && i1 < _y + childInterface.height) {
                    if (childInterface.type == Widget.TYPE_HOVER) {
                        childInterface.toggled = true;
                    }
                } else {
                    if (childInterface.type == Widget.TYPE_HOVER || childInterface.type == Widget.TYPE_CONFIG_HOVER || childInterface.type == Widget.TYPE_ADJUSTABLE_CONFIG
                            || childInterface.type == Widget.TYPE_BOX) {
                        childInterface.toggled = false;
                    }
                }
                if (childInterface.optionType == Widget.OPTION_USABLE && spellSelected == 0
                        && k >= _x && i1 >= _y && k < _x + childInterface.width
                        && i1 < _y + childInterface.height) {
                    String s = childInterface.selectedActionName;
                    if (s.indexOf(" ") != -1)
                        s = s.substring(0, s.indexOf(" "));
                    if (       childInterface.spellName.toLowerCase().endsWith("rush")
                            || childInterface.spellName.toLowerCase().endsWith("burst")
                            || childInterface.spellName.toLowerCase().endsWith("blitz")
                            || childInterface.spellName.toLowerCase().endsWith("barrage")
                            || childInterface.spellName.toLowerCase().endsWith("strike")
                            || childInterface.spellName.toLowerCase().endsWith("bolt")
                            || childInterface.spellName.toLowerCase().equals("crumble undead")
                            || childInterface.spellName.toLowerCase().endsWith("blast")
                            || childInterface.spellName.toLowerCase().endsWith("wave")
                            || childInterface.spellName.toLowerCase().equals("claws of guthix")
                            || childInterface.spellName.toLowerCase().equals("flames of zamorak")
                            || childInterface.spellName.toLowerCase().equals("magic dart")) {
                        menuActionText[menuActionRow] =
                                "Autocast @gre@" + childInterface.spellName;

                        menuActionTypes[menuActionRow] = 104;
                        secondMenuAction[menuActionRow] = childInterface.id;
                        menuActionRow++;
                    }
                    if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                        menuActionText[menuActionRow] =
                                s + " @gre@" + childInterface.spellName + " @lre@" + childInterface.id;
                        menuActionTypes[menuActionRow] = 626;
                        secondMenuAction[menuActionRow] = childInterface.id;
                        menuActionRow++;
                    } else {
                        menuActionText[menuActionRow] =
                                s + " @gre@" + childInterface.spellName;
                        menuActionTypes[menuActionRow] = 626;
                        secondMenuAction[menuActionRow] = childInterface.id;
                        menuActionRow++;
                    }
                }
                if (childInterface.optionType == Widget.OPTION_CLOSE && k >= _x && i1 >= _y
                        && k < _x + childInterface.width
                        && i1 < _y + childInterface.height) {
                    menuActionText[menuActionRow] = "Close";
                    menuActionTypes[menuActionRow] = 200;
                    secondMenuAction[menuActionRow] = childInterface.id;
                    menuActionRow++;
                }
                if (childInterface.optionType == Widget.OPTION_TOGGLE_SETTING && k >= _x
                        && i1 >= _y && k < _x + childInterface.width
                        && i1 < _y + childInterface.height) {
                    if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                        menuActionText[menuActionRow] = childInterface.tooltip + " @lre@(" + childInterface.id + ")";
                    } else {
                        menuActionText[menuActionRow] = childInterface.tooltip;
                    }
                    menuActionTypes[menuActionRow] = 169;
                    secondMenuAction[menuActionRow] = childInterface.id;
                    menuActionRow++;
                }

                if (childInterface.optionType == Widget.OPTION_RESET_SETTING && k >= _x
                        && i1 >= _y && k < _x + childInterface.width
                        && i1 < _y + childInterface.height) {
                    boolean flag = false;
                    if (childInterface.tooltip == null ||
                            childInterface.tooltip.length() == 0) {
                        flag = true;
                    }
                    if (!flag) {
                        if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                            menuActionText[menuActionRow] = childInterface.tooltip + " @lre@(" + childInterface.id + ")";
                        } else {
                            menuActionText[menuActionRow] = childInterface.tooltip;
                        }
                        menuActionTypes[menuActionRow] = 646;
                        secondMenuAction[menuActionRow] = childInterface.id;
                        menuActionRow++;
                    }
                }

                if (childInterface.optionType == Widget.OPTION_CONTINUE
                        && !continuedDialogue && k >= _x && i1 >= _y
                        && k < _x + childInterface.width
                        && i1 < _y + childInterface.height) {
                    menuActionText[menuActionRow] = childInterface.tooltip;
                    menuActionTypes[menuActionRow] = 679;
                    secondMenuAction[menuActionRow] = childInterface.id;
                    menuActionRow++;
                }
                if (childInterface.optionType == Widget.OPTION_DROPDOWN) {

                    boolean flag = false;
                    childInterface.hovered = false;
                    childInterface.dropdownHover = -1;

                    if (childInterface.dropdown.isOpen()) {

                        // Inverted keybinds dropdown
                        if (childInterface.type == Widget.TYPE_KEYBINDS_DROPDOWN && childInterface.inverted && k >= _x &&
                                k < _x + (childInterface.dropdown.getWidth() - 16) && i1 >= _y - childInterface.dropdown.getHeight() - 10 && i1 < _y) {

                            int yy = i1 - (_y - childInterface.dropdown.getHeight());

                            if (k > _x + (childInterface.dropdown.getWidth() / 2)) {
                                childInterface.dropdownHover = ((yy / 15) * 2) + 1;
                            } else {
                                childInterface.dropdownHover = (yy / 15) * 2;
                            }
                            flag = true;
                        } else if (!childInterface.inverted && k >= _x && k < _x + (childInterface.dropdown.getWidth() - 16) &&
                                i1 >= _y + 19 && i1 < _y + 19 + childInterface.dropdown.getHeight()) {

                            int yy = i1 - (_y + 19);

                            if (childInterface.type == Widget.TYPE_KEYBINDS_DROPDOWN && childInterface.dropdown.doesSplit()) {
                                if (k > _x + (childInterface.dropdown.getWidth() / 2)) {
                                    childInterface.dropdownHover = ((yy / 15) * 2) + 1;
                                } else {
                                    childInterface.dropdownHover = (yy / 15) * 2;
                                }
                            } else {
                                childInterface.dropdownHover = yy / 14; // Regular dropdown hover
                            }
                            flag = true;
                        }
                        if (flag) {
                            if (menuActionRow != 1) {
                                menuActionRow = 1;
                            }
                            menuActionText[menuActionRow] = "Select";
                            menuActionTypes[menuActionRow] = 770;
                            secondMenuAction[menuActionRow] = childInterface.id;
                            firstMenuAction[menuActionRow] = childInterface.dropdownHover;
                            selectedMenuActions[menuActionRow] = widget.id;
                            menuActionRow++;
                        }
                    }
                    if (k >= _x && i1 >= _y && k < _x + childInterface.dropdown.getWidth() && i1 < _y + 24 && menuActionRow == 1) {
                        childInterface.hovered = true;
                        menuActionText[menuActionRow] = childInterface.dropdown.isOpen() ? "Hide" : "Show";
                        menuActionTypes[menuActionRow] = 769;
                        secondMenuAction[menuActionRow] = childInterface.id;
                        selectedMenuActions[menuActionRow] = widget.id;
                        menuActionRow++;
                    }
                }

                if (k >= _x && i1 >= _y && k < _x + childInterface.width && i1 < _y + childInterface.height) {

                    if (childInterface.actions != null && !childInterface.invisible && !childInterface.hidden) {

                        if (!(childInterface.contentType == 206 && interfaceIsSelected(childInterface))) {
                            if ((childInterface.type == 4 && childInterface.defaultText.length() > 0) || childInterface.type == 5) {

                                boolean drawOptions = true;

                                // HARDCODE CLICKABLE TEXT HERE
                                if (childInterface.parent == 37128) { // Clan chat interface, dont show options for guests
                                    drawOptions = showClanOptions;
                                }

                                if (drawOptions) {
                                    for (int action = childInterface.actions.length
                                            - 1; action >= 0; action--) {
                                        if (childInterface.actions[action] != null) {
                                            String s = childInterface.actions[action] + (childInterface.type == 4 ? " @or1@" + childInterface.defaultText : "");

                                            if (s.contains("img")) {
                                                int prefix = s.indexOf("<img=");
                                                int suffix = s.indexOf(">");
                                                s = s.replaceAll(s.substring(prefix + 5, suffix), "");
                                                s = s.replaceAll("</img>", "");
                                                s = s.replaceAll("<img=>", "");
                                            }
                                            
                                            menuActionText[menuActionRow] = s;
                                            menuActionTypes[menuActionRow] = 647;
                                            firstMenuAction[menuActionRow] = action;
                                            secondMenuAction[menuActionRow] = childInterface.id;
                                            menuActionRow++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (childInterface.type == Widget.TYPE_INVENTORY && !childInterface.invisible && !childInterface.hidden && !(childInterface.id >= 22035 && childInterface.id <= 22042)) {
                    int k2 = 0;
                    for (int l2 = 0; l2 < childInterface.height; l2++) {
                        for (int i3 = 0; i3 < childInterface.width; i3++) {
                            int j3 = _x + i3 * (32 + childInterface.spritePaddingX);
                            int k3 = _y + l2 * (32 + childInterface.spritePaddingY);
                            if (k2 < 20) {
                                j3 += childInterface.spritesX[k2];
                                k3 += childInterface.spritesY[k2];
                            }
                            if (k >= j3 && i1 >= k3 && k < j3 + 32 && i1 < k3 + 32) {
                                mouseInvInterfaceIndex = k2;
                                lastActiveInvInterface = childInterface.id;
                                if (k2 >= childInterface.inventoryItemId.length) {
                                    continue;
                                }

                                if (childInterface.inventoryItemId[k2] > 0) {

                                    boolean hasDestroyOption = false;
                                    ItemDefinition itemDef = ItemDefinition.lookup(childInterface.inventoryItemId[k2] - 1);
                                    if (itemSelected == 1 && childInterface.hasActions) {
                                        if (childInterface.id != anInt1284 || k2 != anInt1283) {
                                            menuActionText[menuActionRow] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
                                            menuActionTypes[menuActionRow] = 870;
                                            selectedMenuActions[menuActionRow] = itemDef.id;
                                            firstMenuAction[menuActionRow] = k2;
                                            secondMenuAction[menuActionRow] = childInterface.id;
                                            menuActionRow++;
                                        }
                                    } else if (spellSelected == 1
                                            && childInterface.hasActions) {
                                        if ((spellUsableOn & 0x10) == 16) {
                                            menuActionText[menuActionRow] =
                                                    spellTooltip + " @lre@"
                                                            + itemDef.name;
                                            menuActionTypes[menuActionRow] =
                                                    543;
                                            selectedMenuActions[menuActionRow] =
                                                    itemDef.id;
                                            firstMenuAction[menuActionRow] =
                                                    k2;
                                            secondMenuAction[menuActionRow] =
                                                    childInterface.id;
                                            menuActionRow++;
                                        }
                                    } else {
                                        if (childInterface.hasActions) {
                                            for (int l3 = 4; l3 >= 3; l3--)
                                                if (itemDef.actions != null
                                                        && itemDef.actions[l3] != null) {
                                                    menuActionText[menuActionRow] =
                                                            itemDef.actions[l3]
                                                                    + " @lre@"
                                                                    + itemDef.name;
                                                    if (l3 == 3)
                                                        menuActionTypes[menuActionRow] =
                                                                493;
                                                    if (l3 == 4) {
                                                        menuActionTypes[menuActionRow] = 847;
                                                        hasDestroyOption = itemDef.actions[l3].contains("Destroy");
                                                    }
                                                    selectedMenuActions[menuActionRow] = itemDef.id;
                                                    firstMenuAction[menuActionRow] = k2;
                                                    secondMenuAction[menuActionRow] = childInterface.id;
                                                    menuActionRow++;
                                                } else if (l3 == 4) {
                                                    menuActionText[menuActionRow] = "Drop @lre@" + itemDef.name;
                                                    menuActionTypes[menuActionRow] = 847;
                                                    selectedMenuActions[menuActionRow] = itemDef.id;
                                                    firstMenuAction[menuActionRow] = k2;
                                                    secondMenuAction[menuActionRow] = childInterface.id;
                                                    menuActionRow++;
                                                }
                                        }
                                        if (childInterface.usableItems) {
                                            menuActionText[menuActionRow] = "Use @lre@" + itemDef.name;
                                            menuActionTypes[menuActionRow] = 447;
                                            selectedMenuActions[menuActionRow] = itemDef.id;
                                            firstMenuAction[menuActionRow] = k2;
                                            secondMenuAction[menuActionRow] = childInterface.id;
                                            menuActionRow++;

                                            if (Configuration.enableShiftClickDrop && !hasDestroyOption && !menuOpen && shiftDown) {
                                                menuActionsRow("Drop @lre@" + itemDef.name, 1, 847, 2);
                                                removeShiftDropOnMenuOpen = true;
                                            }
                                        }
                                        if (childInterface.hasActions && itemDef.actions != null) {
                                            for (int i4 = 2; i4 >= 0; i4--) {
                                                if (itemDef.actions[i4] != null) {
                                                    menuActionText[menuActionRow] = itemDef.actions[i4] + " @lre@" + itemDef.name;
                                                    if (i4 == 0)
                                                        menuActionTypes[menuActionRow] = 74;
                                                    if (i4 == 1)
                                                        menuActionTypes[menuActionRow] = 454;
                                                    if (i4 == 2)
                                                        menuActionTypes[menuActionRow] = 539;
                                                    selectedMenuActions[menuActionRow] = itemDef.id;
                                                    firstMenuAction[menuActionRow] = k2;
                                                    secondMenuAction[menuActionRow] = childInterface.id;
                                                    menuActionRow++;
                                                }
                                            }
                                            if (Configuration.enableShiftClickDrop && !hasDestroyOption && !menuOpen && shiftDown) {
                                                menuActionsRow("Drop @lre@" + itemDef.name, 1, 847, 2);
                                                removeShiftDropOnMenuOpen = true;
                                            }
                                        }

                                        // Menu actions, item options etc in interfaces
                                        // Hardcoding
                                        if (childInterface.actions != null) {
                                            for (int type =
                                                 4; type >= 0; type--) {
                                                if (childInterface.actions[type] != null) {
                                                    String action = childInterface.actions[type];

                                                    // HARDCODING OF MENU ACTIONS
                                                    if (openInterfaceId == 42000) {
                                                        action = action.replace("Offer", "Pricecheck");
                                                        action += " " + itemDef.id;
                                                    }

                                                    menuActionText[menuActionRow] = action + " @lre@" + itemDef.name;

                                                    if (type == 0)
                                                        menuActionTypes[menuActionRow] =
                                                                632;
                                                    if (type == 1)
                                                        menuActionTypes[menuActionRow] =
                                                                78;
                                                    if (type == 2)
                                                        menuActionTypes[menuActionRow] =
                                                                867;
                                                    if (type == 3)
                                                        menuActionTypes[menuActionRow] =
                                                                431;
                                                    if (type == 4)
                                                        menuActionTypes[menuActionRow] =
                                                                53;
                                                    selectedMenuActions[menuActionRow] =
                                                            itemDef.id;
                                                    firstMenuAction[menuActionRow] =
                                                            k2;
                                                    secondMenuAction[menuActionRow] =
                                                            childInterface.id;
                                                    menuActionRow++;
                                                }
                                            }

                                        }
                                        if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                                            menuActionText[menuActionRow] = "Examine @lre@" + itemDef.name + " @gre@(@whi@" + (childInterface.inventoryItemId[k2] - 1) + "@gre@) int: " + childInterface.id;
                                        } else {
                                            menuActionText[menuActionRow] = "Examine @lre@" + itemDef.name;
                                        }
                                        menuActionTypes[menuActionRow] = 1125;
                                        selectedMenuActions[menuActionRow] = itemDef.id;
                                        firstMenuAction[menuActionRow] = k2;
                                        secondMenuAction[menuActionRow] = childInterface.id;
                                        menuActionRow++;
                                    }

                                    if (Bank.isBankContainer(childInterface.id) && childInterface.inventoryAmounts[k2] == 0) {
                                        menuActionRow = 2;
                                        if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                                            menuActionText[0] = "Examine @lre@" + itemDef.name + " @gre@(@whi@" + (childInterface.inventoryItemId[k2] - 1) + "@gre@) int: " + childInterface.id;
                                        } else {
                                            menuActionText[0] = "Examine @lre@" + itemDef.name;
                                        }
                                        menuActionTypes[0] = 1125;
                                        selectedMenuActions[0] = itemDef.id;
                                        firstMenuAction[0] = k2;
                                        secondMenuAction[0] = childInterface.id;

                                        menuActionText[1] = "Release @lre@" + itemDef.name;
                                        menuActionTypes[1] = 633;
                                        selectedMenuActions[1] = itemDef.id;
                                        firstMenuAction[1] = k2;
                                        secondMenuAction[1] = childInterface.id;
                                    }
                                }
                            }
                            k2++;
                        }
                    }
                }
            }
        }
    }

    public void drawTransparentScrollBar(int x, int y, int height, int maxScroll, int pos) {
        spriteCache.lookup(29).drawAdvancedSprite(x, y, 120);
        spriteCache.lookup(30).drawAdvancedSprite(x, y + height - 16, 120);
        Rasterizer2D.drawTransparentVerticalLine(x, y + 16, height - 32, 0xffffff, 64);
        Rasterizer2D.drawTransparentVerticalLine(x + 15, y + 16, height - 32, 0xffffff, 64);
        int barHeight = (height - 32) * height / maxScroll;
        if (barHeight < 10) {
            barHeight = 10;
        }
        int barPos = 0;
        if (maxScroll != height) {
            barPos = (height - 32 - barHeight) * pos / (maxScroll - height);
        }
        Rasterizer2D.drawTransparentBoxOutline(x, y + 16 + barPos, 16,
                5 + y + 16 + barPos + barHeight - 5 - (y + 16 + barPos), 0xffffff, 32);
    }

    public void drawScrollbar(int height, int pos, int y, int x, int maxScroll, boolean transparent) {
        if (transparent) {
            drawTransparentScrollBar(x, y, height, maxScroll, pos);
        } else {
            scrollBar1.drawSprite(x, y);
            scrollBar2.drawSprite(x, (y + height) - 16);
            Rasterizer2D.drawBox(x, y + 16, 16, height - 32, 0x000001);
            Rasterizer2D.drawBox(x, y + 16, 15, height - 32, 0x3d3426);
            Rasterizer2D.drawBox(x, y + 16, 13, height - 32, 0x342d21);
            Rasterizer2D.drawBox(x, y + 16, 11, height - 32, 0x2e281d);
            Rasterizer2D.drawBox(x, y + 16, 10, height - 32, 0x29241b);
            Rasterizer2D.drawBox(x, y + 16, 9, height - 32, 0x252019);
            Rasterizer2D.drawBox(x, y + 16, 1, height - 32, 0x000001);
            int k1 = ((height - 32) * height) / maxScroll;
            if (k1 < 8) {
                k1 = 8;
            }
            int l1 = ((height - 32 - k1) * pos) / (maxScroll - height);
            Rasterizer2D.drawBox(x, y + 16 + l1, 16, k1, barFillColor);
            Rasterizer2D.drawVerticalLine(x, y + 16 + l1, k1, 0x000001);
            Rasterizer2D.drawVerticalLine(x + 1, y + 16 + l1, k1, 0x817051);
            Rasterizer2D.drawVerticalLine(x + 2, y + 16 + l1, k1, 0x73654a);
            Rasterizer2D.drawVerticalLine(x + 3, y + 16 + l1, k1, 0x6a5c43);
            Rasterizer2D.drawVerticalLine(x + 4, y + 16 + l1, k1, 0x6a5c43);
            Rasterizer2D.drawVerticalLine(x + 5, y + 16 + l1, k1, 0x655841);
            Rasterizer2D.drawVerticalLine(x + 6, y + 16 + l1, k1, 0x655841);
            Rasterizer2D.drawVerticalLine(x + 7, y + 16 + l1, k1, 0x61553e);
            Rasterizer2D.drawVerticalLine(x + 8, y + 16 + l1, k1, 0x61553e);
            Rasterizer2D.drawVerticalLine(x + 9, y + 16 + l1, k1, 0x5d513c);
            Rasterizer2D.drawVerticalLine(x + 10, y + 16 + l1, k1, 0x5d513c);
            Rasterizer2D.drawVerticalLine(x + 11, y + 16 + l1, k1, 0x594e3a);
            Rasterizer2D.drawVerticalLine(x + 12, y + 16 + l1, k1, 0x594e3a);
            Rasterizer2D.drawVerticalLine(x + 13, y + 16 + l1, k1, 0x514635);
            Rasterizer2D.drawVerticalLine(x + 14, y + 16 + l1, k1, 0x4b4131);
            Rasterizer2D.drawHorizontalLine(x, y + 16 + l1, 15, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 15, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 14, 0x655841);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 13, 0x6a5c43);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 11, 0x6d5f48);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 10, 0x73654a);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 7, 0x76684b);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 5, 0x7b6a4d);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 4, 0x7e6e50);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 3, 0x817051);
            Rasterizer2D.drawHorizontalLine(x, y + 17 + l1, 2, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 16, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 15, 0x564b38);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 14, 0x5d513c);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 11, 0x625640);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 10, 0x655841);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 7, 0x6a5c43);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 5, 0x6e6046);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 4, 0x716247);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 3, 0x7b6a4d);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 2, 0x817051);
            Rasterizer2D.drawHorizontalLine(x, y + 18 + l1, 1, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 16, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 15, 0x514635);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 14, 0x564b38);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 11, 0x5d513c);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 9, 0x61553e);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 7, 0x655841);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 5, 0x6a5c43);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 4, 0x6e6046);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 3, 0x73654a);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 2, 0x817051);
            Rasterizer2D.drawHorizontalLine(x, y + 19 + l1, 1, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 16, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 15, 0x4b4131);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 14, 0x544936);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 13, 0x594e3a);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 10, 0x5d513c);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 8, 0x61553e);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 6, 0x655841);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 4, 0x6a5c43);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 3, 0x73654a);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 2, 0x817051);
            Rasterizer2D.drawHorizontalLine(x, y + 20 + l1, 1, 0x000001);
            Rasterizer2D.drawVerticalLine(x + 15, y + 16 + l1, k1, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 15 + l1 + k1, 16, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 14 + l1 + k1, 15, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 14 + l1 + k1, 14, 0x3f372a);
            Rasterizer2D.drawHorizontalLine(x, y + 14 + l1 + k1, 10, 0x443c2d);
            Rasterizer2D.drawHorizontalLine(x, y + 14 + l1 + k1, 9, 0x483e2f);
            Rasterizer2D.drawHorizontalLine(x, y + 14 + l1 + k1, 7, 0x4a402f);
            Rasterizer2D.drawHorizontalLine(x, y + 14 + l1 + k1, 4, 0x4b4131);
            Rasterizer2D.drawHorizontalLine(x, y + 14 + l1 + k1, 3, 0x564b38);
            Rasterizer2D.drawHorizontalLine(x, y + 14 + l1 + k1, 2, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 16, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 15, 0x443c2d);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 11, 0x4b4131);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 9, 0x514635);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 7, 0x544936);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 6, 0x564b38);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 4, 0x594e3a);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 3, 0x625640);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 2, 0x6a5c43);
            Rasterizer2D.drawHorizontalLine(x, y + 13 + l1 + k1, 1, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 16, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 15, 0x443c2d);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 14, 0x4b4131);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 12, 0x544936);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 11, 0x564b38);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 10, 0x594e3a);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 7, 0x5d513c);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 4, 0x61553e);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 3, 0x6e6046);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 2, 0x7b6a4d);
            Rasterizer2D.drawHorizontalLine(x, y + 12 + l1 + k1, 1, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 16, 0x000001);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 15, 0x4b4131);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 14, 0x514635);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 13, 0x564b38);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 11, 0x594e3a);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 9, 0x5d513c);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 7, 0x61553e);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 5, 0x655841);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 4, 0x6a5c43);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 3, 0x73654a);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 2, 0x7b6a4d);
            Rasterizer2D.drawHorizontalLine(x, y + 11 + l1 + k1, 1, 0x000001);
        }
    }

    private void updateNPCs(Buffer stream, int i) {
        removedMobCount = 0;
        mobsAwaitingUpdateCount = 0;
        method139(stream);
        updateNPCMovement(i, stream);
        npcUpdateMask(stream);
        for (int k = 0; k < removedMobCount; k++) {
            int l = removedMobs[k];
            if (npcs[l].time != tick) {
                npcs[l].desc = null;
                npcs[l] = null;
            }
        }

        if (stream.currentPosition != i) {
            Signlink.reporterror(myUsername + " size mismatch in getnpcpos - pos:"
                    + stream.currentPosition + " psize:" + i);
            throw new RuntimeException("eek");
        }
        for (int i1 = 0; i1 < npcCount; i1++)
            if (npcs[npcIndices[i1]] == null) {
                Signlink.reporterror(myUsername + " null entry in npc list - pos:" + i1
                        + " size:" + npcCount);
                throw new RuntimeException("eek");
            }

    }

    public void processChatModeClick() {

        final int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 503;
        if (super.mouseX >= 5 && super.mouseX <= 61 && super.mouseY >= yOffset + 482
                && super.mouseY <= yOffset + 503) {
            cButtonHPos = 0;
            updateChatbox = true;
        } else if (super.mouseX >= 69 && super.mouseX <= 125 && super.mouseY >= yOffset + 482
                && super.mouseY <= yOffset + 503) {
            cButtonHPos = 1;
            updateChatbox = true;
        } else if (super.mouseX >= 133 && super.mouseX <= 189 && super.mouseY >= yOffset + 482
                && super.mouseY <= yOffset + 503) {
            cButtonHPos = 2;
            updateChatbox = true;
        } else if (super.mouseX >= 197 && super.mouseX <= 253 && super.mouseY >= yOffset + 482
                && super.mouseY <= yOffset + 503) {
            cButtonHPos = 3;
            updateChatbox = true;
        } else if (super.mouseX >= 261 && super.mouseX <= 317 && super.mouseY >= yOffset + 482
                && super.mouseY <= yOffset + 503) {
            cButtonHPos = 4;
            updateChatbox = true;
        } else if (super.mouseX >= 389 && super.mouseX <= 445 && super.mouseY >= yOffset + 482
                && super.mouseY <= yOffset + 503) {
            cButtonHPos = 6;
            updateChatbox = true;
        } else if (super.mouseX >= 453 && super.mouseX <= 509 && super.mouseY >= yOffset + 482
                && super.mouseY <= yOffset + 503) {
            cButtonHPos = 7;
            updateChatbox = true;
        } else {
            cButtonHPos = -1;
            updateChatbox = true;
        }
        if (super.clickMode3 == 1) {
            if (super.saveClickX >= 5 && super.saveClickX <= 61
                    && super.saveClickY >= yOffset + 482
                    && super.saveClickY <= yOffset + 505) {
                if (frameMode != ScreenMode.FIXED) {
                    if (setChannel != 0) {
                        cButtonCPos = 0;
                        chatTypeView = 0;
                        updateChatbox = true;
                        setChannel = 0;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    cButtonCPos = 0;
                    chatTypeView = 0;
                    updateChatbox = true;
                    setChannel = 0;
                }
            } else if (super.saveClickX >= 69 && super.saveClickX <= 125
                    && super.saveClickY >= yOffset + 482
                    && super.saveClickY <= yOffset + 505) {
                if (frameMode != ScreenMode.FIXED) {
                    if (setChannel != 1 && frameMode != ScreenMode.FIXED) {
                        cButtonCPos = 1;
                        chatTypeView = 5;
                        updateChatbox = true;
                        setChannel = 1;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    cButtonCPos = 1;
                    chatTypeView = 5;
                    updateChatbox = true;
                    setChannel = 1;
                }
            } else if (super.saveClickX >= 133 && super.saveClickX <= 189
                    && super.saveClickY >= yOffset + 482
                    && super.saveClickY <= yOffset + 505) {
                if (frameMode != ScreenMode.FIXED) {
                    if (setChannel != 2 && frameMode != ScreenMode.FIXED) {
                        cButtonCPos = 2;
                        chatTypeView = 1;
                        updateChatbox = true;
                        setChannel = 2;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    cButtonCPos = 2;
                    chatTypeView = 1;
                    updateChatbox = true;
                    setChannel = 2;
                }
            } else if (super.saveClickX >= 197 && super.saveClickX <= 253
                    && super.saveClickY >= yOffset + 482
                    && super.saveClickY <= yOffset + 505) {
                if (frameMode != ScreenMode.FIXED) {
                    if (setChannel != 3 && frameMode != ScreenMode.FIXED) {
                        cButtonCPos = 3;
                        chatTypeView = 2;
                        updateChatbox = true;
                        setChannel = 3;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    cButtonCPos = 3;
                    chatTypeView = 2;
                    updateChatbox = true;
                    setChannel = 3;
                }
            } else if (super.saveClickX >= 261 && super.saveClickX <= 317
                    && super.saveClickY >= yOffset + 482
                    && super.saveClickY <= yOffset + 505) {
                if (frameMode != ScreenMode.FIXED) {
                    if (setChannel != 4 && frameMode != ScreenMode.FIXED) {
                        cButtonCPos = 4;
                        chatTypeView = 11;
                        updateChatbox = true;
                        setChannel = 4;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    cButtonCPos = 4;
                    chatTypeView = 11;
                    updateChatbox = true;
                    setChannel = 4;
                }
            } else if (super.saveClickX >= 325 && super.saveClickX <= 381
                    && super.saveClickY >= yOffset + 482
                    && super.saveClickY <= yOffset + 505) {
                if (frameMode != ScreenMode.FIXED) {
                    if (setChannel != 5 && frameMode != ScreenMode.FIXED) {
                        cButtonCPos = 5;
                        chatTypeView = 3;
                        updateChatbox = true;
                        setChannel = 5;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    cButtonCPos = 5;
                    chatTypeView = 3;
                    updateChatbox = true;
                    setChannel = 5;
                }
            } else if (super.saveClickX >= 389 && super.saveClickX <= 445
                    && super.saveClickY >= yOffset + 482
                    && super.saveClickY <= yOffset + 505) {
                if (frameMode != ScreenMode.FIXED) {
                    if (setChannel != 6 && frameMode != ScreenMode.FIXED) {
                        cButtonCPos = 6;
                        chatTypeView = 99;
                        updateChatbox = true;
                        setChannel = 6;
                    } else {
                        showChatComponents = showChatComponents ? false : true;
                    }
                } else {
                    cButtonCPos = 6;
                    chatTypeView = 99;
                    updateChatbox = true;
                    setChannel = 6;
                }
            } else if (super.saveClickX >= 453 && super.saveClickX <= 509 // TODO
                    && super.saveClickY >= yOffset + 482
                    && super.saveClickY <= yOffset + 505) {
                // View Tools
            }
        }
    }

    public void changeMusicVolume(int newVolume) {
        boolean wasPlayingMusic = Configuration.enableMusic;

        if (newVolume <= 0) {
            Configuration.enableMusic = false;
        } else {
            if (Signlink.music != null) {
                adjustVolume(wasPlayingMusic, (100 * newVolume));
            }
            Configuration.enableMusic = true;
        }

        if (Configuration.enableMusic != wasPlayingMusic && !lowMemory) {
            if (Configuration.enableMusic) {
                nextSong = currentSong;
                fadeMusic = true;
                resourceProvider.provide(2, nextSong);
            } else {
                stopMidi();
            }
            prevSong = 0;
        }
    }

    public void updateVarp(int id) {
        int parameter = VariablePlayer.variables[id].getActionId();
        if (parameter == 0) {
            return;
        }

        int state = settings[id];

        if (parameter == 1) {

            if (state == 1) {
                Rasterizer3D.setBrightness(0.9);
                savePlayerData();
            }

            if (state == 2) {
                Rasterizer3D.setBrightness(0.8);
                savePlayerData();
            }

            if (state == 3) {
                Rasterizer3D.setBrightness(0.7);
                savePlayerData();
            }

            if (state == 4) {
                Rasterizer3D.setBrightness(0.6);
                savePlayerData();
            }

            ItemDefinition.sprites.clear();
            welcomeScreenRaised = true;
        }

        if (parameter == 3) {


            boolean previousPlayingMusic = Configuration.enableMusic;

            if (state == 0) {

                if (Signlink.music != null) {
                    adjustVolume(Configuration.enableMusic, 500);
                }

                Configuration.enableMusic = true;
            }
            if (state == 1) {

                if (Signlink.music != null) {
                    adjustVolume(Configuration.enableMusic, 300);
                }

                Configuration.enableMusic = true;
            }
            if (state == 2) {

                if (Signlink.music != null) {
                    adjustVolume(Configuration.enableMusic, 100);
                }

                Configuration.enableMusic = true;
            }
            if (state == 3) {

                if (Signlink.music != null) {
                    adjustVolume(Configuration.enableMusic, 0);
                }

                Configuration.enableMusic = true;
            }
            if (state == 4)
                Configuration.enableMusic = false;
            if (Configuration.enableMusic != previousPlayingMusic && !lowMemory) {
                if (Configuration.enableMusic) {
                    nextSong = currentSong;
                    fadeMusic = true;
                    resourceProvider.provide(2, nextSong);
                } else {
                    stopMidi();
                }
                prevSong = 0;
            }
        }

        if (parameter == 4) {
            SoundPlayer.setVolume(state);
            if (state == 0) {
                aBoolean848 = true;
                setWaveVolume(0);
            }
            if (state == 1) {
                aBoolean848 = true;
                setWaveVolume(-400);
            }
            if (state == 2) {
                aBoolean848 = true;
                setWaveVolume(-800);
            }
            if (state == 3) {
                aBoolean848 = true;
                setWaveVolume(-1200);
            }
            if (state == 4)
                aBoolean848 = false;
        }

        if (parameter == 5) {
            anInt1253 = state;
        }

        if (parameter == 6) {
            anInt1249 = state;
        }

        if (parameter == 8) {
            splitPrivateChat = state;
            updateChatbox = true;
        }

        if (parameter == 9) {
            anInt913 = state;
        }

    }

    public void updateEntities() {
        try {
            int messageLength = 0;

            for (int j = -1; j < playerCount + npcCount; j++) {
                Object obj;
                if (j == -1)
                    obj = localPlayer;
                else if (j < playerCount)
                    obj = players[playerList[j]];
                else
                    obj = npcs[npcIndices[j - playerCount]];
                if (obj == null || !((Mob) (obj)).isVisible())
                    continue;
                if (obj instanceof Npc) {
                    NpcDefinition entityDef = ((Npc) obj).desc;
                    if (Misc.NAMES_ABOVE_HEAD.toggled()) {
                        npcScreenPos(((Mob) (obj)), ((Mob) (obj)).height + 15);
                        smallText.drawText(0x0099FF, entityDef.name, spriteDrawY - 5,
                                spriteDrawX); // -15
                        // from
                        // original
                    }
                    if (entityDef.childrenIDs != null)
                        entityDef = entityDef.morph();
                    if (entityDef == null)
                        continue;
                }
                if (j < playerCount) {
                    int text_over_head_offset = 0;
                    int l = 30;
                    Player player = (Player) obj;
                    if (player.headIcon >= 0) {
                        npcScreenPos(((Mob) (obj)), ((Mob) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            if (player.skullIcon < 2) {
                                skullIcons[player.skullIcon].drawSprite(
                                        spriteDrawX - 12, spriteDrawY - l);
                                l += 25;
                                if (Misc.HP_ABOVE_HEAD.toggled() && Misc.NAMES_ABOVE_HEAD.toggled()) {
                                    text_over_head_offset -= 25;
                                } else if (Misc.NAMES_ABOVE_HEAD.toggled()) {
                                    text_over_head_offset -= 23;
                                } else if (Misc.HP_ABOVE_HEAD.toggled()) {
                                    text_over_head_offset -= 33;
                                }
                            }
                            if (player.headIcon < 13) {
                                headIcons[player.headIcon].drawSprite(
                                        spriteDrawX - 12, spriteDrawY - l - 3);
                                l += 21;
                                text_over_head_offset -= 5;
                                if (Misc.HP_ABOVE_HEAD.toggled() && Misc.NAMES_ABOVE_HEAD.toggled()) {
                                    text_over_head_offset -= 25;
                                } else if (Misc.NAMES_ABOVE_HEAD.toggled()) {
                                    text_over_head_offset -= 26;
                                } else if (Misc.HP_ABOVE_HEAD.toggled()) {
                                    text_over_head_offset -= 33;
                                }
                            }
                        }
                    }
                    if (j >= 0 && hintIconDrawType == 10
                            && hintIconPlayerId == playerList[j]) {
                        npcScreenPos(((Mob) (obj)), ((Mob) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            l += 13;
                            text_over_head_offset -= 17;
                            headIconsHint[player.hintIcon].drawSprite(
                                    spriteDrawX - 12, spriteDrawY - l);
                        }
                    }
                    if (Misc.HP_ABOVE_HEAD.toggled() && Misc.NAMES_ABOVE_HEAD.toggled()) {
                        newSmallFont.drawCenteredString(
                                (new StringBuilder())
                                        .append(((Mob) (Mob) obj).currentHealth)
                                        .append("/")
                                        .append(((Mob) (Mob) obj).maxHealth)
                                        .toString(),
                                spriteDrawX, spriteDrawY - 29 + text_over_head_offset, 0xff0000, 0);
                    } // Draws HP above head
                    else if (Misc.HP_ABOVE_HEAD.toggled() && !Misc.NAMES_ABOVE_HEAD.toggled()) {
                        newSmallFont.drawCenteredString(
                                (new StringBuilder())
                                        .append(((Mob) (Mob) obj).currentHealth)
                                        .append("/")
                                        .append(((Mob) (Mob) obj).maxHealth)
                                        .toString(),
                                spriteDrawX, spriteDrawY - 5 + text_over_head_offset, 0xff0000, 0);
                    }
                    if (Misc.NAMES_ABOVE_HEAD.toggled()) {
                        npcScreenPos(((Mob) (obj)), ((Mob) (obj)).height + 15);
                        int col = 0xff0000;
                        if (player.clanName == localPlayer.clanName)
                            col = 0x00ff00;
                        smallText.drawText(col, player.name, spriteDrawY - 15 + text_over_head_offset, spriteDrawX);
						/*if (player.clanName != "" && player.clanName != "None")
							smallText.drawText(col, "<" + player.clanName + ">",
									spriteDrawY - 5 + text_over_head_offset, spriteDrawX);*/
                    }
                } else {
                    Npc npc = ((Npc) obj);
                    if (npc.getHeadIcon() >= 0
                            && npc.getHeadIcon() < headIcons.length) {
                        npcScreenPos(((Mob) (obj)), ((Mob) (obj)).height + 15);
                        if (spriteDrawX > -1)
                            headIcons[npc.getHeadIcon()].drawSprite(
                                    spriteDrawX - 12, spriteDrawY - 30);
                    }
                    if (hintIconDrawType == 1
                            && hintIconNpcId == npcIndices[j - playerCount]
                            && tick % 20 < 10) {
                        npcScreenPos(((Mob) (obj)), ((Mob) (obj)).height + 15);
                        if (spriteDrawX > -1)
                            headIconsHint[0].drawSprite(spriteDrawX - 12,
                                    spriteDrawY - 28);
                    }
                }
                if (((Mob) (obj)).spokenText != null && (j >= playerCount
                        || publicChatMode == 0 || publicChatMode == 3
                        || publicChatMode == 1
                        && isFriendOrSelf(((Player) obj).name))) {
                    npcScreenPos(((Mob) (obj)), ((Mob) (obj)).height);
                    if (spriteDrawX > -1 && messageLength < anInt975) {
                        anIntArray979[messageLength] =
                                boldText.method384(((Mob) (obj)).spokenText) / 2;
                        anIntArray978[messageLength] = boldText.verticalSpace;
                        anIntArray976[messageLength] = spriteDrawX;
                        anIntArray977[messageLength] = spriteDrawY;
                        textColourEffect[messageLength] = ((Mob) (obj)).textColour;
                        anIntArray981[messageLength] = ((Mob) (obj)).textEffect;
                        anIntArray982[messageLength] = ((Mob) (obj)).textCycle;
                        aStringArray983[messageLength++] = ((Mob) (obj)).spokenText;
                        if (anInt1249 == 0 && ((Mob) (obj)).textEffect >= 1
                                && ((Mob) (obj)).textEffect <= 3) {
                            anIntArray978[messageLength] += 10;
                            anIntArray977[messageLength] += 5;
                        }
                        if (anInt1249 == 0 && ((Mob) (obj)).textEffect == 4)
                            anIntArray979[messageLength] = 60;
                        if (anInt1249 == 0 && ((Mob) (obj)).textEffect == 5)
                            anIntArray978[messageLength] += 5;
                    }
                }
                if (((Mob) (obj)).loopCycleStatus > tick) {
                    try {
                        npcScreenPos(((Mob) (obj)), ((Mob) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            int i1 = (((Mob) (obj)).currentHealth * 30)
                                    / ((Mob) (obj)).maxHealth;

                            if (i1 > 30) {
                                i1 = 30;
                            }
                            int hpPercent = (((Mob) (obj)).currentHealth * 56)
                                    / ((Mob) (obj)).maxHealth;

                            if (hpPercent > 56) {
                                hpPercent = 56;
                            }

                            if (!UserInterface.NEW_HP_BAR.toggled()) {
                                Rasterizer2D.drawBox(spriteDrawX - 15, spriteDrawY - 3, i1, 5,
                                        65280);
                                Rasterizer2D.drawBox((spriteDrawX - 15) + i1, spriteDrawY - 3, 30 - i1, 5,
                                        0xff0000
                                );
                            } else {
                                spriteCache.lookup(41).drawSprite(spriteDrawX - 28, spriteDrawY - 3);

                                new Sprite(hp, hpPercent, 7);
                                spriteCache.lookup(40).drawSprite(spriteDrawX - 28,
                                        spriteDrawY - 3);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (obj instanceof Npc) {
                    Npc npc = ((Npc) obj);
                    if (localPlayer.interactingEntity == -1) {

                        //Is the npc interacting with us?
                        //If we aren't interacting with others,
                        //Start combat box timer.
                        if ((npc.interactingEntity - 32768) == localPlayerIndex) {
                            currentInteract = npc;
                            combatBoxTimer.start(10);
                        }

                    } else {

                        //Are we interacting with the npc?
                        //Start combat box timer.
                        if (npc.index == localPlayer.interactingEntity) {
                            currentInteract = npc;
                            combatBoxTimer.start(10);
                        }
                    }
                } else if (obj instanceof Player) {
                    Player player = ((Player) obj);
                    if (localPlayer.interactingEntity == -1) {

                        //Is the player interacting with us?
                        //If we aren't interacting with others,
                        //Start combat box timer.
                        if ((player.interactingEntity - 32768) == localPlayerIndex) {
                            currentInteract = player;
                            combatBoxTimer.start(10);
                        }

                    } else {
                        //Are we interacting with the player?
                        //Start combat box timer.
                        if (player.index == localPlayer.interactingEntity - 32768) {
                            currentInteract = player;
                            combatBoxTimer.start(10);
                        }
                    }
                }

                //Drawing hits..
                if (!UserInterface.NEW_HITMARKS.toggled()) {
                    for (int j1 = 0; j1 < 4; j1++) {
                        if (((Mob) (obj)).hitsLoopCycle[j1] > tick) {
                            npcScreenPos(((Mob) (obj)),
                                    ((Mob) (obj)).height / 2);
                            if (spriteDrawX > -1) {
                                if (j1 == 1)
                                    spriteDrawY -= 20;
                                if (j1 == 2) {
                                    spriteDrawX -= 15;
                                    spriteDrawY -= 10;
                                }
                                if (j1 == 3) {
                                    spriteDrawX += 15;
                                    spriteDrawY -= 10;
                                }
                                hitMarks[((Mob) (obj)).hitMarkTypes[j1]]
                                        .drawSprite(spriteDrawX - 12,
                                                spriteDrawY - 12);

                                smallText.drawText(0,
                                         String.valueOf(
                                                ((Mob) (obj)).hitDamages[j1]
                                                        * 1),
                                        spriteDrawY + 4, spriteDrawX);

                                smallText.drawText(0xffffff,
                                       String.valueOf(
                                                ((Mob) (obj)).hitDamages[j1]
                                                        * 1),
                                        spriteDrawY + 3, spriteDrawX - 1);
                            }
                        }
                    }
                } else {
                    for (int j2 = 0; j2 < 4; j2++) {
                        if (((Mob) (obj)).hitsLoopCycle[j2] > tick) {
                            npcScreenPos(((Mob) (obj)),
                                    ((Mob) (obj)).height / 2);
                            if (spriteDrawX > -1) {
                                if (j2 == 0 && ((Mob) (obj)).hitDamages[j2] > 99)
                                    ((Mob) (obj)).hitMarkTypes[j2] = 3;
                                else if (j2 == 1
                                        && ((Mob) (obj)).hitDamages[j2] > 99)
                                    ((Mob) (obj)).hitMarkTypes[j2] = 3;
                                else if (j2 == 2
                                        && ((Mob) (obj)).hitDamages[j2] > 99)
                                    ((Mob) (obj)).hitMarkTypes[j2] = 3;
                                else if (j2 == 3
                                        && ((Mob) (obj)).hitDamages[j2] > 99)
                                    ((Mob) (obj)).hitMarkTypes[j2] = 3;
                                if (j2 == 1) {
                                    spriteDrawY -= 20;
                                }
                                if (j2 == 2) {
                                    spriteDrawX -=
                                            (((Mob) (obj)).hitDamages[j2] > 99
                                                    ? 30 : 20);
                                    spriteDrawY -= 10;
                                }
                                if (j2 == 3) {
                                    spriteDrawX +=
                                            (((Mob) (obj)).hitDamages[j2] > 99
                                                    ? 30 : 20);
                                    spriteDrawY -= 10;
                                }
                                if (((Mob) (obj)).hitMarkTypes[j2] == 3) {
                                    spriteDrawX -= 8;
                                }
                                spriteCache.lookup(hitmarks562[((Mob) (obj)).hitMarkTypes[j2]])
                                        .draw24BitSprite(spriteDrawX - 12,
                                                spriteDrawY - 12);
                                smallText.drawText(0xffffff,
                                        String.valueOf(
                                                ((Mob) (obj)).hitDamages[j2]),
                                        spriteDrawY + 3,
                                        (((Mob) (obj)).hitMarkTypes[j2] == 3
                                                ? spriteDrawX + 7
                                                : spriteDrawX - 1));
                            }
                        }
                    }
                }
            }
            for (int defaultText = 0; defaultText < messageLength; defaultText++) {
                int k1 = anIntArray976[defaultText];
                int l1 = anIntArray977[defaultText];
                int j2 = anIntArray979[defaultText];
                int k2 = anIntArray978[defaultText];
                boolean flag = true;
                while (flag) {
                    flag = false;
                    for (int l2 = 0; l2 < defaultText; l2++)
                        if (l1 + 2 > anIntArray977[l2] - anIntArray978[l2]
                                && l1 - k2 < anIntArray977[l2] + 2
                                && k1 - j2 < anIntArray976[l2] + anIntArray979[l2]
                                && k1 + j2 > anIntArray976[l2] - anIntArray979[l2]
                                && anIntArray977[l2] - anIntArray978[l2] < l1) {
                            l1 = anIntArray977[l2] - anIntArray978[l2];
                            flag = true;
                        }

                }
                spriteDrawX = anIntArray976[defaultText];
                spriteDrawY = anIntArray977[defaultText] = l1;
                String s = aStringArray983[defaultText];
                if (anInt1249 == 0) {
                    int i3 = 0xffff00;
                    if (textColourEffect[defaultText] < 6)
                        i3 = anIntArray965[textColourEffect[defaultText]];
                    if (textColourEffect[defaultText] == 6)
                        i3 = anInt1265 % 20 >= 10 ? 0xffff00 : 0xff0000;
                    if (textColourEffect[defaultText] == 7)
                        i3 = anInt1265 % 20 >= 10 ? 65535 : 255;
                    if (textColourEffect[defaultText] == 8)
                        i3 = anInt1265 % 20 >= 10 ? 0x80ff80 : 45056;
                    if (textColourEffect[defaultText] == 9) {
                        int j3 = 150 - anIntArray982[defaultText];
                        if (j3 < 50)
                            i3 = 0xff0000 + 1280 * j3;
                        else if (j3 < 100)
                            i3 = 0xffff00 - 0x50000 * (j3 - 50);
                        else if (j3 < 150)
                            i3 = 65280 + 5 * (j3 - 100);
                    }
                    if (textColourEffect[defaultText] == 10) {
                        int k3 = 150 - anIntArray982[defaultText];
                        if (k3 < 50)
                            i3 = 0xff0000 + 5 * k3;
                        else if (k3 < 100)
                            i3 = 0xff00ff - 0x50000 * (k3 - 50);
                        else if (k3 < 150)
                            i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
                    }
                    if (textColourEffect[defaultText] == 11) {
                        int l3 = 150 - anIntArray982[defaultText];
                        if (l3 < 50)
                            i3 = 0xffffff - 0x50005 * l3;
                        else if (l3 < 100)
                            i3 = 65280 + 0x50005 * (l3 - 50);
                        else if (l3 < 150)
                            i3 = 0xffffff - 0x50000 * (l3 - 100);
                    }
                    if (anIntArray981[defaultText] == 0) {
                        boldText.drawText(0, s, spriteDrawY + 1, spriteDrawX);
                        boldText.drawText(i3, s, spriteDrawY, spriteDrawX);
                    }
                    if (anIntArray981[defaultText] == 1) {
                        boldText.wave(0, s, spriteDrawX, anInt1265, spriteDrawY + 1);
                        boldText.wave(i3, s, spriteDrawX, anInt1265, spriteDrawY);
                    }
                    if (anIntArray981[defaultText] == 2) {
                        boldText.wave2(spriteDrawX, s, anInt1265, spriteDrawY + 1, 0);
                        boldText.wave2(spriteDrawX, s, anInt1265, spriteDrawY, i3);
                    }
                    if (anIntArray981[defaultText] == 3) {
                        boldText.shake(150 - anIntArray982[defaultText], s, anInt1265,
                                spriteDrawY + 1, spriteDrawX, 0);
                        boldText.shake(150 - anIntArray982[defaultText], s, anInt1265,
                                spriteDrawY, spriteDrawX, i3);
                    }
                    if (anIntArray981[defaultText] == 4) {
                        int i4 = boldText.method384(s);
                        int k4 = ((150 - anIntArray982[defaultText]) * (i4 + 100))
                                / 150;
                        Rasterizer2D.setDrawingArea(334, spriteDrawX - 50, spriteDrawX + 50,
                                0);
                        boldText.render(0, s, spriteDrawY + 1, (spriteDrawX + 50) - k4);
                        boldText.render(i3, s, spriteDrawY, (spriteDrawX + 50) - k4);
                        Rasterizer2D.defaultDrawingAreaSize();
                    }
                    if (anIntArray981[defaultText] == 5) {
                        int j4 = 150 - anIntArray982[defaultText];
                        int l4 = 0;
                        if (j4 < 25)
                            l4 = j4 - 25;
                        else if (j4 > 125)
                            l4 = j4 - 125;
                        Rasterizer2D.setDrawingArea(spriteDrawY + 5, 0, 512,
                                spriteDrawY - boldText.verticalSpace - 1);
                        boldText.drawText(0, s, spriteDrawY + 1 + l4, spriteDrawX);
                        boldText.drawText(i3, s, spriteDrawY + l4, spriteDrawX);
                        Rasterizer2D.defaultDrawingAreaSize();
                    }
                } else {
                    boldText.drawText(0, s, spriteDrawY + 1, spriteDrawX);
                    boldText.drawText(0xffff00, s, spriteDrawY, spriteDrawX);
                }
            }
        } catch (Exception e) {
        }
    }

    public void drawSideIcons() {
        int xOffset = frameMode == ScreenMode.FIXED ? 0 : frameWidth - 247;
        int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 336;
        if (frameMode == ScreenMode.FIXED || frameMode != ScreenMode.FIXED && !stackSideStones) {
            for (int i = 0; i < sideIconsTab.length; i++) {
                if (tabInterfaceIDs[sideIconsTab[i]] != -1) {
                    if (sideIconsId[i] != -1) {
                        Sprite sprite = sideIcons[sideIconsId[i]];
                        if (i == 13) {
                            Client.spriteCache.lookup(360).drawAdvancedSprite(sideIconsX[i] + xOffset, sideIconsY[i] + yOffset);
                        } else {
                            sprite.drawSprite(sideIconsX[i] + xOffset, sideIconsY[i] + yOffset);
                        }

                    }
                }
            }
        } else if (stackSideStones && frameWidth < 1000) {
            int[] iconId = {0, 1, 2, 3, 4, 5, 6, -1, 8, 9, 7, 11, 12, 13};
            int[] iconX = {219, 189, 156, 126, 94, 62, 30, 219, 189, 156, 124, 92, 59, 28};
            int[] iconY = {67, 69, 67, 69, 72, 72, 69, 32, 29, 29, 32, 30, 33, 31, 32};
            for (int i = 0; i < sideIconsTab.length; i++) {
                if (tabInterfaceIDs[sideIconsTab[i]] != -1) {
                    if (iconId[i] != -1) {
                        Sprite sprite = sideIcons[iconId[i]];
                        if (i == 13) {
                        	Client.spriteCache.lookup(360).drawAdvancedSprite(frameWidth - iconX[i] + 2, frameHeight - iconY[i] + 1);
                        } else {
                        	sprite.drawSprite(frameWidth - iconX[i], frameHeight - iconY[i]);
                        }
                    }
                }
            }
        } else if (stackSideStones && frameWidth >= 1000) {
            int[] iconId = {0, 1, 2, 3, 4, 5, 6, -1, 8, 9, 7, 11, 12, 13};
            int[] iconX =
                    {50, 80, 114, 143, 176, 208, 240, 242, 273, 306, 338, 370, 404, 433};
            int[] iconY = {30, 32, 30, 32, 34, 34, 32, 32, 29, 29, 32, 31, 32, 32, 32};
            for (int i = 0; i < sideIconsTab.length; i++) {
                if (tabInterfaceIDs[sideIconsTab[i]] != -1) {
                    if (iconId[i] != -1) {
                        Sprite sprite = sideIcons[iconId[i]];
                        if (i == 13) {
                        	Client.spriteCache.lookup(360).drawAdvancedSprite(frameWidth - 461 + iconX[i] + 2, frameHeight - iconY[i] + 1);
                        } else {
                        	sprite.drawSprite(frameWidth - 461 + iconX[i], frameHeight - iconY[i]);
                        }
                    }
                }
            }
        }
    }

    private void drawRedStones() {

        final int[] redStonesX =
                {6, 44, 77, 110, 143, 176, 209, 6, 44, 77, 110, 143, 176, 209},
                redStonesY = {0, 0, 0, 0, 0, 0, 0, 298, 298, 298, 298, 298, 298, 298},
                redStonesId = {35, 39, 39, 39, 39, 39, 36, 37, 39, 39, 39, 39, 39, 38};

        int xOffset = frameMode == ScreenMode.FIXED ? 0 : frameWidth - 247;
        int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 336;
        if (frameMode == ScreenMode.FIXED || frameMode != ScreenMode.FIXED && !stackSideStones) {
            if (tabInterfaceIDs[tabId] != -1 && tabId != 15) {
                spriteCache.lookup(redStonesId[tabId]).drawSprite(redStonesX[tabId] + xOffset,
                        redStonesY[tabId] + yOffset);
            }
        } else if (stackSideStones && frameWidth < 1000) {
            int[] stoneX = {226, 194, 162, 130, 99, 65, 34, 219, 195, 161, 130, 98, 65, 33};
            int[] stoneY = {73, 73, 73, 73, 73, 73, 73, -1, 37, 37, 37, 37, 37, 37, 37};
            if (tabInterfaceIDs[tabId] != -1 && tabId != 10 && showTabComponents) {
                if (tabId == 7) {
                    spriteCache.lookup(39).drawSprite(frameWidth - 130, frameHeight - 37);
                }
                spriteCache.lookup(39).drawSprite(frameWidth - stoneX[tabId],
                        frameHeight - stoneY[tabId]);
            }
        } else if (stackSideStones && frameWidth >= 1000) {
            int[] stoneX =
                    {417, 385, 353, 321, 289, 256, 224, 129, 193, 161, 130, 98, 65, 33};
            if (tabInterfaceIDs[tabId] != -1 && tabId != 10 && showTabComponents) {
                spriteCache.lookup(39).drawSprite(frameWidth - stoneX[tabId], frameHeight - 37);
            }
        }
    }

    public String getNameForTab(int tab) {
        switch (tab) {
            case 0:
                return "Combat";
            case 1:
                return "Stats";
            case 2:
                return "Quest";
            case 3:
                return "Inventory";
            case 4:
                return "Equipment";
            case 5:
                return "Prayer";
            case 6:
                return "Magic";
            case 7:
                return "Clan chat";
            case 8:
                return "Friends";
            case 9:
                return "Ignores";
            case 10:
                return "Logout";
            case 11:
                return "Settings";
            case 12:
                return "Emotes";
            case 13:
                return "Teleports";
        }
        return "";
    }

    private void drawTabArea() {
		final int xOffset = frameMode == ScreenMode.FIXED ? 0 : frameWidth - 241;
		final int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 336;
		if (frameMode == ScreenMode.FIXED) {
			tabImageProducer.initDrawingArea();
		}
		Rasterizer3D.scanOffsets = anIntArray1181;
		if (frameMode == ScreenMode.FIXED) {
			spriteCache.lookup(21).drawSprite(0, 0);
		} else if (frameMode != ScreenMode.FIXED && !stackSideStones) {
            Rasterizer2D.drawTransparentBox(frameWidth - 217, frameHeight - 304, 195, 270, 0x3E3529, transparentTabArea ? 80 : 256);
			spriteCache.lookup(47).drawSprite(xOffset, yOffset);
		} else {
			if (frameWidth >= 1000) {
				if (showTabComponents) {
                    Rasterizer2D.drawTransparentBox(frameWidth - 197, frameHeight - 304, 197, 265, 0x3E3529, transparentTabArea ? 80 : 256);
					spriteCache.lookup(50).drawSprite(frameWidth - 204, frameHeight - 311);
				}
				for (int x = frameWidth - 417, y = frameHeight - 37, index = 0; x <= frameWidth - 30 && index < 13; x += 32, index++) {
					spriteCache.lookup(46).drawSprite(x, y);
				}
			} else if (frameWidth < 1000) {
				if (showTabComponents) {
                    Rasterizer2D.drawTransparentBox(frameWidth - 197, frameHeight - 341, 195, 265, 0x3E3529, transparentTabArea ? 80 : 256);
					spriteCache.lookup(50).drawSprite(frameWidth - 204, frameHeight - 348);
				}
				for (int x = frameWidth - 226, y = frameHeight - 73, index = 0; x <= frameWidth - 32 && index < 7; x += 32, index++) {
					spriteCache.lookup(46).drawSprite(x, y);
				}
				for (int x = frameWidth - 226, y = frameHeight - 37, index = 0; x <= frameWidth - 32 && index < 7; x += 32, index++) {
					spriteCache.lookup(46).drawSprite(x, y);
				}
			}
		}
		if (overlayInterfaceId == -1) {
			drawRedStones();
			drawSideIcons();
		}
		if (showTabComponents) {
			int x = frameMode == ScreenMode.FIXED ? 31 : frameWidth - 215;
			int y = frameMode == ScreenMode.FIXED ? 37 : frameHeight - 299;
			if (stackSideStones) {
				x = frameWidth - 197;
				y = frameWidth >= 1000 ? frameHeight - 303 : frameHeight - 340;
			}
			try {
                if (overlayInterfaceId != -1) {
                    drawInterface(0, x, Widget.interfaceCache[overlayInterfaceId], y);
                } else if (tabInterfaceIDs[tabId] != -1) {
                    drawInterface(0, x, Widget.interfaceCache[tabInterfaceIDs[tabId]], y);
                }
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
		}
		if (menuOpen) {
			drawMenu(frameMode == ScreenMode.FIXED ? 516 : 0, frameMode == ScreenMode.FIXED ? 168 : 0);
		}
		if (frameMode == ScreenMode.FIXED) {
			tabImageProducer.drawGraphics(168, super.graphics, 516);
			gameScreenImageProducer.initDrawingArea();
		}
		Rasterizer3D.scanOffsets = anIntArray1182;
	}

    private void writeBackgroundTexture(int j) {
        if (!lowMemory) {
            if (Rasterizer3D.textureLastUsed[17] >= j) {
                IndexedImage background = Rasterizer3D.textures[17];
                int k = background.width * background.height - 1;
                int j1 = background.width * tickDelta * 2;
                byte raster[] = background.palettePixels;
                byte abyte3[] = aByteArray912;
                for (int i2 = 0; i2 <= k; i2++)
                    abyte3[i2] = raster[i2 - j1 & k];

                background.palettePixels = abyte3;
                aByteArray912 = raster;
                Rasterizer3D.requestTextureUpdate(17);
                anInt854++;
                if (anInt854 > 1235) {
                    anInt854 = 0;
					  /*Anticheat?
					  outgoing.writeOpcode(226);
					  outgoing.writeByte(0);
					  int l2 = outgoing.currentPosition;
					  outgoing.writeShort(58722);
					  outgoing.writeByte(240);
					  outgoing.writeShort((int) (Math.random() * 65536D));
					  outgoing.writeByte((int) (Math.random() * 256D));
					  if ((int) (Math.random() * 2D) == 0)
							outgoing.writeShort(51825);
					  outgoing.writeByte((int) (Math.random() * 256D));
					  outgoing.writeShort((int) (Math.random() * 65536D));
					  outgoing.writeShort(7130);
					  outgoing.writeShort((int) (Math.random() * 65536D));
					  outgoing.writeShort(61657);
					  outgoing.writeBytes(outgoing.currentPosition - l2);*/
                }
            }
			/* Moving textures */
            if (Rasterizer3D.textureLastUsed[24] >= j) {
                IndexedImage background_1 = Rasterizer3D.textures[24];
                int l = background_1.width * background_1.height - 1;
                int k1 = background_1.width * tickDelta * 2;
                byte abyte1[] = background_1.palettePixels;
                byte abyte4[] = aByteArray912;
                for (int j2 = 0; j2 <= l; j2++)
                    abyte4[j2] = abyte1[j2 - k1 & l];

                background_1.palettePixels = abyte4;
                aByteArray912 = abyte1;
                Rasterizer3D.requestTextureUpdate(24);
            }
            if (Rasterizer3D.textureLastUsed[34] >= j) {
                IndexedImage background_2 = Rasterizer3D.textures[34];
                int i1 = background_2.width * background_2.height - 1;
                int l1 = background_2.width * tickDelta * 2;
                byte abyte2[] = background_2.palettePixels;
                byte abyte5[] = aByteArray912;
                for (int k2 = 0; k2 <= i1; k2++)
                    abyte5[k2] = abyte2[k2 - l1 & i1];

                background_2.palettePixels = abyte5;
                aByteArray912 = abyte2;
                Rasterizer3D.requestTextureUpdate(34);
            }
            if (Rasterizer3D.textureLastUsed[40] >= j) {
                IndexedImage background_2 = Rasterizer3D.textures[40];
                int i1 = background_2.width * background_2.height - 1;
                int l1 = background_2.width * tickDelta * 2;
                byte abyte2[] = background_2.palettePixels;
                byte abyte5[] = aByteArray912;
                for (int k2 = 0; k2 <= i1; k2++)
                    abyte5[k2] = abyte2[k2 - l1 & i1];

                background_2.palettePixels = abyte5;
                aByteArray912 = abyte2;
                Rasterizer3D.requestTextureUpdate(40);
            }
            if (Rasterizer3D.textureLastUsed[59] >= j) {
                IndexedImage background_1 = Rasterizer3D.textures[59];
                int l = background_1.width * background_1.height - 1;
                int k1 = background_1.width * tickDelta * 2;
                byte abyte1[] = background_1.palettePixels;
                byte abyte4[] = aByteArray912;
                for (int j2 = 0; j2 <= l; j2++)
                    abyte4[j2] = abyte1[j2 - k1 & l];

                background_1.palettePixels = abyte4;
                aByteArray912 = abyte1;
                Rasterizer3D.requestTextureUpdate(59);
            }
        }
    }

    private void processMobChatText() {
        for (int i = -1; i < playerCount; i++) {
            int j;
            if (i == -1)
                j = internalLocalPlayerIndex;
            else
                j = playerList[i];
            Player player = players[j];
            if (player != null && player.textCycle > 0) {
                player.textCycle--;
                if (player.textCycle == 0)
                    player.spokenText = null;
            }
        }
        for (int k = 0; k < npcCount; k++) {
            int l = npcIndices[k];
            Npc npc = npcs[l];
            if (npc != null && npc.textCycle > 0) {
                npc.textCycle--;
                if (npc.textCycle == 0)
                    npc.spokenText = null;
            }
        }
    }

    private void calculateCameraPosition() {
        int i = x * 128 + 64;
        int j = y * 128 + 64;
        int k = getCenterHeight(plane, j, i) - height;
        if (xCameraPos < i) {
            xCameraPos += speed + ((i - xCameraPos) * angle) / 1000;
            if (xCameraPos > i)
                xCameraPos = i;
        }
        if (xCameraPos > i) {
            xCameraPos -= speed + ((xCameraPos - i) * angle) / 1000;
            if (xCameraPos < i)
                xCameraPos = i;
        }
        if (zCameraPos < k) {
            zCameraPos += speed + ((k - zCameraPos) * angle) / 1000;
            if (zCameraPos > k)
                zCameraPos = k;
        }
        if (zCameraPos > k) {
            zCameraPos -= speed + ((zCameraPos - k) * angle) / 1000;
            if (zCameraPos < k)
                zCameraPos = k;
        }
        if (yCameraPos < j) {
            yCameraPos += speed + ((j - yCameraPos) * angle) / 1000;
            if (yCameraPos > j)
                yCameraPos = j;
        }
        if (yCameraPos > j) {
            yCameraPos -= speed + ((yCameraPos - j) * angle) / 1000;
            if (yCameraPos < j)
                yCameraPos = j;
        }
        i = cinematicCamXViewpointLoc * 128 + 64;
        j = cinematicCamYViewpointLoc * 128 + 64;
        k = getCenterHeight(plane, j, i) - cinematicCamZViewpointLoc;
        int l = i - xCameraPos;
        int i1 = k - zCameraPos;
        int j1 = j - yCameraPos;
        int k1 = (int) Math.sqrt(l * l + j1 * j1);
        int l1 = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
        int i2 = (int) (Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
        if (l1 < 128)
            l1 = 128;
        if (l1 > 383)
            l1 = 383;
        if (yCameraCurve < l1) {
            yCameraCurve += constCinematicCamRotationSpeed + ((l1 - yCameraCurve) * varCinematicCamRotationSpeedPromille) / 1000;
            if (yCameraCurve > l1)
                yCameraCurve = l1;
        }
        if (yCameraCurve > l1) {
            yCameraCurve -= constCinematicCamRotationSpeed + ((yCameraCurve - l1) * varCinematicCamRotationSpeedPromille) / 1000;
            if (yCameraCurve < l1)
                yCameraCurve = l1;
        }
        int j2 = i2 - xCameraCurve;
        if (j2 > 1024)
            j2 -= 2048;
        if (j2 < -1024)
            j2 += 2048;
        if (j2 > 0) {
            xCameraCurve += constCinematicCamRotationSpeed + (j2 * varCinematicCamRotationSpeedPromille) / 1000;
            xCameraCurve &= 0x7ff;
        }
        if (j2 < 0) {
            xCameraCurve -= constCinematicCamRotationSpeed + (-j2 * varCinematicCamRotationSpeedPromille) / 1000;
            xCameraCurve &= 0x7ff;
        }
        int k2 = i2 - xCameraCurve;
        if (k2 > 1024)
            k2 -= 2048;
        if (k2 < -1024)
            k2 += 2048;
        if (k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0)
            xCameraCurve = i2;
    }

    public void drawMenu(int x, int y) {
        int xPos = menuOffsetX - (x - 4);
        int yPos = (-y + 4) + menuOffsetY;
        int w = menuWidth;
        int h = menuHeight + 1;
        updateChatbox = true;
        tabAreaAltered = true;
        int menuColor = 0x5d5447;
        Rasterizer2D.drawBox(xPos, yPos, w, h, menuColor);
        Rasterizer2D.drawBox(xPos + 1, yPos + 1, w - 2, 16, 0);
        Rasterizer2D.drawBoxOutline(xPos + 1, yPos + 18, w - 2, h - 19, 0);
        boldText.render(menuColor, "Choose Option", yPos + 14, xPos + 3);
        int mouseX = super.mouseX - (x);
        int mouseY = (-y) + super.mouseY;
        for (int i = 0; i < menuActionRow; i++) {
            int textY = yPos + 31 + (menuActionRow - 1 - i) * 15;
            int textColor = 0xffffff;
            if (mouseX > xPos && mouseX < xPos + w && mouseY > textY - 13 && mouseY < textY + 3) {
                textColor = 0xffff00;
            }
			String command = this.menuActionText[i];
			if(command.contains("<br>")) {
				command = command.replaceAll("<br>", " ");
			}
            boldText.drawTextWithPotentialShadow(true, xPos + 3, textColor, /*menuActionText[i]*/command, textY);
        }
    }

    private void addFriend(long nameHash) {
        if (nameHash == 0L)
            return;
        sendPacket(new AddFriend(nameHash).create());
    }

    private int getCenterHeight(int z, int y, int x) {
        int worldX = x >> 7;
        int worldY = y >> 7;
        if (worldX < 0 || worldY < 0 || worldX > 103 || worldY > 103)
            return 0;
        int plane = z;
        if (plane < 3 && (tileFlags[1][worldX][worldY] & 2) == 2)
            plane++;
        int sizeX = x & 0x7f;
        int sizeY = y & 0x7f;
        int i2 = tileHeights[plane][worldX][worldY] * (128 - sizeX)
                + tileHeights[plane][worldX + 1][worldY] * sizeX >> 7;
        int j2 = tileHeights[plane][worldX][worldY + 1] * (128 - sizeX)
                + tileHeights[plane][worldX + 1][worldY + 1] * sizeX >> 7;
        return i2 * (128 - sizeY) + j2 * sizeY >> 7;
    }
    
    public static int[] myHeadAndJaw = new int[2];

    public static int getRandom(int number, boolean greaterThan0) {
		Random random = new Random();
		int randomNr = random.nextInt(number) + (greaterThan0 ? 1 : 0);
		return randomNr;
	}
    
    private void resetLogout() {
    	setLoginState(LoginState.LOGINSCREEN);
        try {
            if (socketStream != null)
                socketStream.close();
        } catch (Exception _ex) {
        }    
        effects_list.clear();
        socketStream = null;
        loggedIn = false;
        unlinkCaches();
        scene.initToNull();
        for (int i = 0; i < 4; i++)
            collisionMaps[i].setDefault();
        Arrays.fill(chatMessages, null);
        System.gc();
        stopMidi();
        currentSong = -1;
        nextSong = -1;
        prevSong = 0;
        frameMode(ScreenMode.FIXED);
        savePlayerData();
    }

    private void changeCharacterGender() {
        aBoolean1031 = true;
        for (int j = 0; j < 7; j++) {
            myAppearance[j] = -1;
            for (int k = 0; k < IdentityKit.length; k++) {
                if (IdentityKit.kits[k].validStyle
                        || IdentityKit.kits[k].bodyPartId != j + (maleCharacter ? 0 : 7))
                    continue;
                myAppearance[j] = k;
                break;
            }
        }
    }

    private void updateNPCMovement(int i, Buffer stream) {
        while (stream.bitPosition + 21 < i * 8) {
            int k = stream.readBits(14);
            if (k == 16383)
                break;
            if (npcs[k] == null)
                npcs[k] = new Npc();
            Npc npc = npcs[k];
            npcIndices[npcCount++] = k;
            npc.time = tick;
            int l = stream.readBits(5);
            if (l > 15)
                l -= 32;
            int i1 = stream.readBits(5);
            if (i1 > 15)
                i1 -= 32;
            int j1 = stream.readBits(1);
            npc.desc = NpcDefinition.lookup(stream.readBits(Configuration.npcBits));
            int updateRequired = stream.readBits(1);
            if (updateRequired == 1)
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = k;
            npc.size = npc.desc.size;
            npc.degreesToTurn = npc.desc.degreesToTurn;
            npc.walkAnimIndex = npc.desc.walkAnim;
            npc.turn180AnimIndex = npc.desc.turn180AnimIndex;
            npc.turn90CWAnimIndex = npc.desc.turn90CWAnimIndex;
            npc.turn90CCWAnimIndex = npc.desc.turn90CCWAnimIndex;
            npc.idleAnimation = npc.desc.standAnim;
            //TODO THIS SHIT SLIDER CUNTERY
            npc.setPos(localPlayer.pathX[0] + i1, localPlayer.pathY[0] + l, j1 == 1);
            
			//Facing
			boolean updateFacing = (stream.readBits(1) == 1);
			if(updateFacing) {
				int faceX = stream.readBits(14);
				int faceY = stream.readBits(14);
				npc.faceX = faceX;
				npc.faceY = faceY;
			}
        }
        stream.disableBitAccess();
    }

    public void processGameLoop() {
        if (rsAlreadyLoaded || loadingError || genericLoadingError)
            return;
        tick++;
        if (!loggedIn && clientLoaded) {
            processLoginScreenInput();
        } else {
            mainGameProcessor();
        }
        processOnDemandQueue();
    }

    private void showPrioritizedPlayers() {
        showPlayer(localPlayer, internalLocalPlayerIndex << 14, true);//
        //Draw the player we're interacting with
        //Interacting includes combat, following, etc.
        int interact = localPlayer.interactingEntity - 32768;
        if (interact > 0) {
            Player player = players[interact];
            showPlayer(player, interact << 14, false);
        }
    }

    private void showOtherPlayers() {
        for (int l = 0; l < playerCount; l++) {
            Player player = players[playerList[l]];
            int index = playerList[l] << 14;

            //Don't draw interacting player as we've already drawn it on top
            int interact_index = (localPlayer.interactingEntity - 32768);
            if (interact_index > 0 && index == interact_index << 14) {
                continue;
            }

            if (!showPlayer(player, index, false)) {
                continue;
            }
        }
    }

    private boolean showPlayer(Player player, int i1, boolean flag) {
        if (player == null || !player.isVisible()) {
            return false;
        }
        if (localPlayer.x >> 7 == destinationX && localPlayer.y >> 7 == destinationY)
            destinationX = 0;
        player.aBoolean1699 = (lowMemory && playerCount > 50 || playerCount > 200) && !flag && player.movementAnimation == player.idleAnimation;
        int j1 = player.x >> 7;
        int k1 = player.y >> 7;
        if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104) {
            return false;
        }
        if (player.playerModel != null && tick >= player.objectModelStart && tick < player.objectModelStop) {
            player.aBoolean1699 = false;
            player.anInt1709 = getCenterHeight(plane, player.y, player.x);
            scene.addToScenePlayerAsObject(plane, player.y, player, player.orientation, player.objectAnInt1722GreaterYLoc, player.x, player.anInt1709, player.objectAnInt1719LesserXLoc, player.objectAnInt1721GreaterXLoc, i1, player.objectAnInt1720LesserYLoc);
            return false;
        }
        if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
            if (anIntArrayArray929[j1][k1] == anInt1265) {
                return false;
            }
            anIntArrayArray929[j1][k1] = anInt1265;
        }
        player.anInt1709 = getCenterHeight(plane, player.y, player.x);
        scene.addAnimableA(plane, player.orientation, player.anInt1709, i1, player.y, 60, player.x, player, player.animationStretches);
        return true;
    }

    private boolean promptUserForInput(Widget widget) {
        int contentType = widget.contentType;
        if (friendServerStatus == 2) {
            if (contentType == 201) {
                updateChatbox = true;
                inputDialogState = 0;
                //ChatboxItemSearch.reset();
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 1;
                aString1121 = "Enter name of friend to add to list";
            }
            if (contentType == 202) {
                updateChatbox = true;
                inputDialogState = 0;
                //ChatboxItemSearch.reset();
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 2;
                aString1121 = "Enter name of friend to delete from list";
            }
        }
        if (contentType == 205) {
            anInt1011 = 250;
            return true;
        }
        if (contentType == 501) {
            updateChatbox = true;
            inputDialogState = 0;
            //ChatboxItemSearch.reset();
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 4;
            aString1121 = "Enter name of player to add to list";
        }
        if (contentType == 502) {
            updateChatbox = true;
            inputDialogState = 0;
            //ChatboxItemSearch.reset();
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 5;
            aString1121 = "Enter name of player to delete from list";
        }
        if (contentType == 550) {
            updateChatbox = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 6;
            aString1121 = "Enter the name of the chat you wish to join";
        }
        if (contentType >= 300 && contentType <= 313) {
            int k = (contentType - 300) / 2;
            int j1 = contentType & 1;
            int i2 = myAppearance[k];
            if (i2 != -1) {
                do {
                    if (j1 == 0 && --i2 < 0)
                        i2 = IdentityKit.length - 1;
                    if (j1 == 1 && ++i2 >= IdentityKit.length)
                        i2 = 0;
                } while (IdentityKit.kits[i2].validStyle
                        || IdentityKit.kits[i2].bodyPartId != k + (maleCharacter ? 0 : 7));
                myAppearance[k] = i2;
                aBoolean1031 = true;
            }
        }
        if (contentType >= 314 && contentType <= 323) {
            int l = (contentType - 314) / 2;
            int k1 = contentType & 1;
            int j2 = characterDesignColours[l];
            if (k1 == 0 && --j2 < 0)
                j2 = PLAYER_BODY_RECOLOURS[l].length - 1;
            if (k1 == 1 && ++j2 >= PLAYER_BODY_RECOLOURS[l].length)
                j2 = 0;
            characterDesignColours[l] = j2;
            aBoolean1031 = true;
        }
        if (contentType == 324 && !maleCharacter) {
            maleCharacter = true;
            changeCharacterGender();
        }
        if (contentType == 325 && maleCharacter) {
            maleCharacter = false;
            changeCharacterGender();
        }
        if (contentType == 326) {
            sendPacket(new ChangeAppearance(maleCharacter, myAppearance, characterDesignColours).create());
            return true;
        }

        if (contentType == 613) {
            canMute = !canMute;
        }

        if (contentType >= 601 && contentType <= 612) {
            clearTopInterfaces();
            if (reportAbuseInput.length() > 0) {
				/* outgoing.writeOpcode(PacketConstants.REPORT_PLAYER);
                        outgoing.writeLong(StringUtils.encodeBase37(reportAbuseInput));
                        outgoing.writeByte(contentType - 601);
                        outgoing.writeByte(canMute ? 1 : 0);*/
            }
        }
        return false;
    }

    private void parsePlayerSynchronizationMask(Buffer stream) {
        for (int count = 0; count < mobsAwaitingUpdateCount; count++) {
            int index = mobsAwaitingUpdate[count];
            Player player = players[index];

            int mask = stream.readUnsignedByte();

            if ((mask & 0x40) != 0) {
                mask += stream.readUnsignedByte() << 8;
            }

            appendPlayerUpdateMask(mask, index, stream, player);
        }
    }

    private void drawMapScenes(int i, int k, int l, int i1, int j1) {
        int k1 = scene.getWallObjectUid(j1, l, i);
        if (k1 != 0) {
            int l1 = scene.getMask(j1, l, i, k1);
            int k2 = l1 >> 6 & 3;
            int i3 = l1 & 0x1f;
            int k3 = k;
            if (k1 > 0)
                k3 = i1;
            int ai[] = minimapImage.myPixels;
            int k4 = 24624 + l * 4 + (103 - i) * 512 * 4;
            int i5 = k1 >> 14 & 0x7fff;
            ObjectDefinition def = ObjectDefinition.lookup(i5);
            if (def.mapscene != -1) {
                IndexedImage background_2 = mapScenes[def.mapscene];
                if (background_2 != null) {
                    int i6 = (def.objectSizeX * 4 - background_2.width) / 2;
                    int j6 = (def.objectSizeY * 4 - background_2.height) / 2;
                    background_2.draw(48 + l * 4 + i6,
                            48 + (104 - i - def.objectSizeY) * 4 + j6);
                }
            } else {
                if (i3 == 0 || i3 == 2)
                    if (k2 == 0) {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else if (k2 == 1) {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 3) {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
                if (i3 == 3)
                    if (k2 == 0)
                        ai[k4] = k3;
                    else if (k2 == 1)
                        ai[k4 + 3] = k3;
                    else if (k2 == 2)
                        ai[k4 + 3 + 1536] = k3;
                    else if (k2 == 3)
                        ai[k4 + 1536] = k3;
                if (i3 == 2)
                    if (k2 == 3) {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else if (k2 == 0) {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else if (k2 == 1) {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
            }
        }
        k1 = scene.getGameObjectUid(j1, l, i);
        if (k1 != 0) {
            int i2 = scene.getMask(j1, l, i, k1);
            int l2 = i2 >> 6 & 3;
            int j3 = i2 & 0x1f;
            int l3 = k1 >> 14 & 0x7fff;
            ObjectDefinition class46_1 = ObjectDefinition.lookup(l3);
            if (class46_1.mapscene != -1) {
                IndexedImage background_1 = mapScenes[class46_1.mapscene];
                if (background_1 != null) {
                    int j5 = (class46_1.objectSizeX * 4 - background_1.width) / 2;
                    int k5 = (class46_1.objectSizeY * 4 - background_1.height) / 2;
                    background_1.draw(48 + l * 4 + j5,
                            48 + (104 - i - class46_1.objectSizeY) * 4 + k5);
                }
            } else if (j3 == 9) {
                int l4 = 0xeeeeee;
                if (k1 > 0)
                    l4 = 0xee0000;
                int ai1[] = minimapImage.myPixels;
                int l5 = 24624 + l * 4 + (103 - i) * 512 * 4;
                if (l2 == 0 || l2 == 2) {
                    ai1[l5 + 1536] = l4;
                    ai1[l5 + 1024 + 1] = l4;
                    ai1[l5 + 512 + 2] = l4;
                    ai1[l5 + 3] = l4;
                } else {
                    ai1[l5] = l4;
                    ai1[l5 + 512 + 1] = l4;
                    ai1[l5 + 1024 + 2] = l4;
                    ai1[l5 + 1536 + 3] = l4;
                }
            }
        }
        k1 = scene.getGroundDecorationUid(j1, l, i);
        if (k1 != 0) {
            int j2 = k1 >> 14 & 0x7fff;
            ObjectDefinition class46 = ObjectDefinition.lookup(j2);
            if (class46.mapscene != -1) {
                IndexedImage background = mapScenes[class46.mapscene];
                if (background != null) {
                    int i4 = (class46.objectSizeX * 4 - background.width) / 2;
                    int j4 = (class46.objectSizeY * 4 - background.height) / 2;
                    background.draw(48 + l * 4 + i4,
                            48 + (104 - i - class46.objectSizeY) * 4 + j4);
                }
            }
        }
    }

    private void loadTitleScreen() {

    	for (int i = 0; i < ProfilePic.length-1; i++) {
    		ProfilePic[i] = null;
    	}

        titleIndexedImages = new IndexedImage[12];
        int icon = 0;
        try {
            icon = Integer.parseInt(getParameter("fl_icon"));
        } catch (Exception ex) {

        }
        if (icon == 0) {
            for (int index = 0; index < 12; index++) {
                titleIndexedImages[index] = new IndexedImage(titleArchive, "runes", index);
            }

        } else {
            for (int index = 0; index < 12; index++) {
                titleIndexedImages[index] = new IndexedImage(titleArchive, "runes", 12 + (index & 3));
            }

        }
        flameLeftSprite = new Sprite(128, 265);
        flameRightSprite = new Sprite(128, 265);

        System.arraycopy(flameLeftBackground.canvasRaster, 0, flameLeftSprite.myPixels, 0, 33920);

        System.arraycopy(flameRightBackground.canvasRaster, 0, flameRightSprite.myPixels, 0, 33920);

        anIntArray851 = new int[256];

        for (int k1 = 0; k1 < 64; k1++)
            anIntArray851[k1] = k1 * 0x40000;

        for (int l1 = 0; l1 < 64; l1++)
            anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1;

        for (int i2 = 0; i2 < 64; i2++)
            anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;

        for (int j2 = 0; j2 < 64; j2++)
            anIntArray851[j2 + 192] = 0xffffff;

        anIntArray852 = new int[256];
        for (int k2 = 0; k2 < 64; k2++)
            anIntArray852[k2] = k2 * 1024;

        for (int l2 = 0; l2 < 64; l2++)
            anIntArray852[l2 + 64] = 65280 + 4 * l2;

        for (int i3 = 0; i3 < 64; i3++)
            anIntArray852[i3 + 128] = 65535 + 0x40000 * i3;

        for (int j3 = 0; j3 < 64; j3++)
            anIntArray852[j3 + 192] = 0xffffff;

        anIntArray853 = new int[256];
        for (int k3 = 0; k3 < 64; k3++)
            anIntArray853[k3] = k3 * 4;

        for (int l3 = 0; l3 < 64; l3++)
            anIntArray853[l3 + 64] = 255 + 0x40000 * l3;

        for (int i4 = 0; i4 < 64; i4++)
            anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4;

        for (int j4 = 0; j4 < 64; j4++)
            anIntArray853[j4 + 192] = 0xffffff;

        anIntArray1190 = new int[32768];
        anIntArray1191 = new int[32768];
        randomizeBackground(null);
        //drawLoadingText(10, "Connecting to fileserver");
        if (!aBoolean831) {
            drawFlames = true;
            aBoolean831 = true;
            startRunnable(this, 2);
        }
    }

    public boolean hover(int x1, int y1, Sprite drawnSprite) {
        return super.mouseX >= x1 && super.mouseX <= x1 + drawnSprite.myWidth && super.mouseY >= y1 && super.mouseY <= y1 + drawnSprite.myHeight;
    }

    private void loadingStages() {
        if (lowMemory && loadingStage == 2 && MapRegion.anInt131 != plane) {
            gameScreenImageProducer.initDrawingArea();
            drawLoadingMessages(1, "Loading - please wait.", null);
            gameScreenImageProducer.drawGraphics(frameMode == ScreenMode.FIXED ? 4 : 0,
                    super.graphics, frameMode == ScreenMode.FIXED ? 4 : 0);
            loadingStage = 1;
            loadingStartTime = System.currentTimeMillis();
        }
        if (loadingStage == 1) {
            int j = getMapLoadingState();
            if (j != 0 && System.currentTimeMillis() - loadingStartTime > 0x57e40L) {
                Signlink.reporterror(myUsername + " glcfb " + serverSeed + "," + j + ","
                        + lowMemory + "," + indices[0] + ","
                        + resourceProvider.remaining() + "," + plane + "," + currentRegionX
                        + "," + currentRegionY);
                loadingStartTime = System.currentTimeMillis();
            }
        }
        if (loadingStage == 2 && plane != lastKnownPlane) {
            lastKnownPlane = plane;
            renderMapScene(plane);
        }
    }

    private int getMapLoadingState() {
        if (!floorMaps.equals("") || !objectMaps.equals("")) {
            floorMaps = "";
            objectMaps = "";
        }

        for (int i = 0; i < terrainData.length; i++) {
            floorMaps += "  " + terrainIndices[i];
            objectMaps += "  " + objectIndices[i];
            if (terrainData[i] == null && terrainIndices[i] != -1)
                return -1;
            if (objectData[i] == null && objectIndices[i] != -1)
                return -2;
        }
        boolean flag = true;
        for (int j = 0; j < terrainData.length; j++) {
            byte abyte0[] = objectData[j];
            if (abyte0 != null) {
                int k = (mapCoordinates[j] >> 8) * 64 - regionBaseX;
                int l = (mapCoordinates[j] & 0xff) * 64 - regionBaseY;
                if (requestMapReconstruct) {
                    k = 10;
                    l = 10;
                }
                flag &= MapRegion.method189(k, abyte0, l);
            }
        }
        if (!flag)
            return -3;
        if (loadingMap) {
            return -4;
        } else {
            loadingStage = 2;
            MapRegion.anInt131 = plane;
            loadRegion();
            sendPacket(new FinalizedRegionChange().create());
            return 0;
        }
    }

    private void createProjectiles() {
        for (Projectile class30_sub2_sub4_sub4 = (Projectile) projectiles
                .reverseGetFirst(); class30_sub2_sub4_sub4 != null; class30_sub2_sub4_sub4 =
                     (Projectile) projectiles.reverseGetNext())
            if (class30_sub2_sub4_sub4.projectileZ != plane
                    || tick > class30_sub2_sub4_sub4.stopCycle)
                class30_sub2_sub4_sub4.unlink();
            else if (tick >= class30_sub2_sub4_sub4.startCycle) {
                if (class30_sub2_sub4_sub4.target > 0) {
                    Npc npc = npcs[class30_sub2_sub4_sub4.target - 1];
                    if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0
                            && npc.y < 13312)
                        class30_sub2_sub4_sub4.calculateIncrements(tick, npc.y,
                                getCenterHeight(class30_sub2_sub4_sub4.projectileZ, npc.y,
                                        npc.x)
                                        - class30_sub2_sub4_sub4.endHeight,
                                npc.x);
                }
                if (class30_sub2_sub4_sub4.target < 0) {
                    int j = -class30_sub2_sub4_sub4.target - 1;
                    Player player;
                    if (j == localPlayerIndex)
                        player = localPlayer;
                    else
                        player = players[j];
                    if (player != null && player.x >= 0 && player.x < 13312
                            && player.y >= 0 && player.y < 13312)
                        class30_sub2_sub4_sub4.calculateIncrements(tick, player.y,
                                getCenterHeight(class30_sub2_sub4_sub4.projectileZ, player.y,
                                        player.x)
                                        - class30_sub2_sub4_sub4.endHeight,
                                player.x);
                }
                class30_sub2_sub4_sub4.progressCycles(tickDelta);
                scene.addAnimableA(plane, class30_sub2_sub4_sub4.turnValue,
                        (int) class30_sub2_sub4_sub4.cnterHeight, -1,
                        (int) class30_sub2_sub4_sub4.yPos, 60,
                        (int) class30_sub2_sub4_sub4.xPos,
                        class30_sub2_sub4_sub4, false);
            }

    }

    public AppletContext getAppletContext() {
        if (Signlink.mainapp != null)
            return Signlink.mainapp.getAppletContext();
        else
            return super.getAppletContext();
    }

    private void drawLogo() {
        byte sprites[] = titleArchive.readFile("title.dat");
        Sprite sprite = new Sprite(sprites, this);
        flameLeftBackground.initDrawingArea();
        sprite.method346(0, 0);
        flameRightBackground.initDrawingArea();
        sprite.method346(-637, 0);
        topLeft1BackgroundTile.initDrawingArea();
        sprite.method346(-128, 0);
        bottomLeft1BackgroundTile.initDrawingArea();
        sprite.method346(-202, -371);
        loginBoxImageProducer.initDrawingArea();
        sprite.method346(-202, -171);
        loginScreenAccessories.initDrawingArea();
        sprite.method346(0, -400);
        bottomLeft0BackgroundTile.initDrawingArea();
        sprite.method346(0, -265);
        bottomRightImageProducer.initDrawingArea();
        sprite.method346(-562, -265);
        loginMusicImageProducer.initDrawingArea();
        sprite.method346(-562, -265);
        middleLeft1BackgroundTile.initDrawingArea();
        sprite.method346(-128, -171);
        aRSImageProducer_1115.initDrawingArea();
        sprite.method346(-562, -171);
        int ai[] = new int[sprite.myWidth];
        for (int j = 0; j < sprite.myHeight; j++) {
            for (int k = 0; k < sprite.myWidth; k++)
                ai[k] = sprite.myPixels[(sprite.myWidth - k - 1) + sprite.myWidth * j];

            System.arraycopy(ai, 0, sprite.myPixels, sprite.myWidth * j, sprite.myWidth);
        }
        flameLeftBackground.initDrawingArea();
        sprite.method346(382, 0);
        flameRightBackground.initDrawingArea();
        sprite.method346(-255, 0);
        topLeft1BackgroundTile.initDrawingArea();
        sprite.method346(254, 0);
        bottomLeft1BackgroundTile.initDrawingArea();
        sprite.method346(180, -371);
        loginBoxImageProducer.initDrawingArea();
        sprite.method346(180, -171);
        bottomLeft0BackgroundTile.initDrawingArea();
        sprite.method346(382, -265);
        bottomRightImageProducer.initDrawingArea();
        sprite.method346(-180, -265);
        loginMusicImageProducer.initDrawingArea();
        sprite.method346(-180, -265);
        middleLeft1BackgroundTile.initDrawingArea();
        sprite.method346(254, -171);
        aRSImageProducer_1115.initDrawingArea();
        sprite.method346(-180, -171);
        sprite = new Sprite(titleArchive, "logo", 0);
        topLeft1BackgroundTile.initDrawingArea();
        sprite.drawSprite(382 - sprite.myWidth / 2 - 128, 18);
        sprite = null;
        System.gc();
    }

    private void processOnDemandQueue() {
        do {
            Resource resource;
            do {
                resource = resourceProvider.next();
                if (resource == null)
                    return;
                if (resource.dataType == 0) {
                    Model.method460(resource.buffer, resource.ID);
                    if (backDialogueId != -1)
                        updateChatbox = true;
                }
                if (resource.dataType == 1) {
                    Frame.load(resource.ID, resource.buffer);
                }
                if (resource.dataType == 2 && resource.ID == nextSong
                        && resource.buffer != null)
                    saveMidi(fadeMusic, resource.buffer);
                if (resource.dataType == 3 && loadingStage == 1) {
                    for (int i = 0; i < terrainData.length; i++) {
                        if (terrainIndices[i] == resource.ID) {
                            terrainData[i] = resource.buffer;
                            if (resource.buffer == null)
                                terrainIndices[i] = -1;
                            break;
                        }
                        if (objectIndices[i] != resource.ID)
                            continue;
                        objectData[i] = resource.buffer;
                        if (resource.buffer == null)
                            objectIndices[i] = -1;
                        break;
                    }

                }
            } while (resource.dataType != 93
                    || !resourceProvider.landscapePresent(resource.ID));
            MapRegion.passiveRequestGameObjectModels(new Buffer(resource.buffer), resourceProvider);
        } while (true);
    }

    private void resetAnimation(int i) {
        Widget parent = Widget.interfaceCache[i];
        if (parent == null || parent.children == null) {
            return;
        }
        for (int j = 0; j < parent.children.length; j++) {
            if (parent.children[j] == -1)
                break;
            Widget child = Widget.interfaceCache[parent.children[j]];
            System.out.println("Child: " + parent.children[j]);
            if (child.type == 1) {
                resetAnimation(child.id);
            }
            child.currentFrame = 0;
            child.lastFrameTime = 0;
        }
    }

    private void drawHeadIcon() {
        if (hintIconDrawType != 2)
            return;
        calcEntityScreenPos((hintIconX - regionBaseX << 7) + hintIconLocationArrowRelX, hintIconLocationArrowHeight * 2,
                (hintIconY - regionBaseY << 7) + hintIconLocationArrowRelY);
        if (spriteDrawX > -1 && tick % 20 < 10) {
            headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
        }
    }

    private void mainGameProcessor() {

        refreshFrameSize();
        if (systemUpdateTime > 1) {
            systemUpdateTime--;
        }
        if (anInt1011 > 0) {
            anInt1011--;
        }
        for (int j = 0; j < 100; j++) { // 5 -> 100. Packets parsed per cycle
            if (!readPacket()) {
                break;
            }
        }

        if (!loggedIn) {
            return;
        }

        synchronized (mouseDetection.syncObject) {
            if (flagged) {
                if (super.clickMode3 != 0 || mouseDetection.coordsIndex >= 40) {
                    // botting
					/* outgoing.writeOpcode(PacketConstants.FLAG_ACCOUNT);
                              outgoing.writeByte(0);
                              int j2 = outgoing.currentPosition;
                              int j3 = 0;
                              for (int j4 = 0; j4 < mouseDetection.coordsIndex; j4++) {
                                    if (j2 - outgoing.currentPosition >= 240)
                                          break;
                                    j3++;
                                    int l4 = mouseDetection.coordsY[j4];
                                    if (l4 < 0)
                                          l4 = 0;
                                    else if (l4 > 502)
                                          l4 = 502;
                                    int k5 = mouseDetection.coordsX[j4];
                                    if (k5 < 0)
                                          k5 = 0;
                                    else if (k5 > 764)
                                          k5 = 764;
                                    int i6 = l4 * 765 + k5;
                                    if (mouseDetection.coordsY[j4] == -1
                                                && mouseDetection.coordsX[j4] == -1) {
                                          k5 = -1;
                                          l4 = -1;
                                          i6 = 0x7ffff;
                                    }
                                    if (k5 == anInt1237 && l4 == anInt1238) {
                                          if (duplicateClickCount < 2047)
                                                duplicateClickCount++;
                                    } else {
                                          int j6 = k5 - anInt1237;
                                          anInt1237 = k5;
                                          int k6 = l4 - anInt1238;
                                          anInt1238 = l4;
                                          if (duplicateClickCount < 8 && j6 >= -32 && j6 <= 31
                                                      && k6 >= -32 && k6 <= 31) {
                                                j6 += 32;
                                                k6 += 32;
                                                outgoing.writeShort((duplicateClickCount << 12)
                                                            + (j6 << 6) + k6);
                                                duplicateClickCount = 0;
                                          } else if (duplicateClickCount < 8) {
                                                outgoing.writeTriByte(0x800000
                                                            + (duplicateClickCount << 19) + i6);
                                                duplicateClickCount = 0;
                                          } else {
                                                outgoing.writeInt(0xc0000000
                                                            + (duplicateClickCount << 19) + i6);
                                                duplicateClickCount = 0;
                                          }
                                    }
                              }

                              outgoing.writeBytes(outgoing.currentPosition - j2);
                              if (j3 >= mouseDetection.coordsIndex) {
                                    mouseDetection.coordsIndex = 0;
                              } else {
                                    mouseDetection.coordsIndex -= j3;
                                    for (int i5 = 0; i5 < mouseDetection.coordsIndex; i5++) {
                                          mouseDetection.coordsX[i5] =
                                                      mouseDetection.coordsX[i5 + j3];
                                          mouseDetection.coordsY[i5] =
                                                      mouseDetection.coordsY[i5 + j3];
                                    }

                              }*/
                }
            } else {
                mouseDetection.coordsIndex = 0;
            }
        }
        if (super.clickMode3 != 0) {
            long l = (super.aLong29 - aLong1220) / 50L;
            if (l > 4095L)
                l = 4095L;
            aLong1220 = super.aLong29;
            int k2 = super.saveClickY;
            if (k2 < 0)
                k2 = 0;
            else if (k2 > 502)
                k2 = 502;
            int k3 = super.saveClickX;
            if (k3 < 0)
                k3 = 0;
            else if (k3 > 764)
                k3 = 764;
            if (super.clickMode3 == 2) {
			}
        }

        if (anInt1016 > 0) {
            anInt1016--;
        }

        if (super.keyArray[1] == 1 || super.keyArray[2] == 1 || super.keyArray[3] == 1
                || super.keyArray[4] == 1)
            aBoolean1017 = true;
        if (aBoolean1017 && anInt1016 <= 0) {
            anInt1016 = 20;
            aBoolean1017 = false;
			/* outgoing.writeOpcode(PacketConstants.CAMERA_MOVEMENT);
                  outgoing.writeShort(anInt1184);
                  outgoing.writeShortA(cameraHorizontal);*/
        }
        if (super.awtFocus && !aBoolean954) {
            aBoolean954 = true;
            //  sendPacket(new ClientFocused(false));
        }
        if (!super.awtFocus && aBoolean954) {
            aBoolean954 = false;
            //   sendPacket(new ClientFocused(false));
        }
        loadingStages();
        method115();
        timeoutCounter++;
        if (timeoutCounter > 750)
            dropClient();
        processPlayerMovement();
        processNpcMovement();
        processTrackUpdates();
        processMobChatText();
        tickDelta++;
        if (crossType != 0) {
            crossIndex += 20;
            if (crossIndex >= 400)
                crossType = 0;
        }
        if (atInventoryInterfaceType != 0) {
            atInventoryLoopCycle++;
            if (atInventoryLoopCycle >= 15) {
                if (atInventoryInterfaceType == 2) {
                }
                if (atInventoryInterfaceType == 3)
                    updateChatbox = true;
                atInventoryInterfaceType = 0;
            }
        }
        if (activeInterfaceType != 0) {
            dragItemDelay++;
            if (super.mouseX > anInt1087 + 5 || super.mouseX < anInt1087 - 5
                    || super.mouseY > anInt1088 + 5 || super.mouseY < anInt1088 - 5)
                aBoolean1242 = true;
            if (super.clickMode2 == 0) {
                if (activeInterfaceType == 2) {
                }
                if (activeInterfaceType == 3)
                    updateChatbox = true;
                activeInterfaceType = 0;
                if (aBoolean1242 && dragItemDelay >= 10) {
                    lastActiveInvInterface = -1;
                    processRightClick();
                    if (!createBankTab()) {
                        if (lastActiveInvInterface == anInt1084
                                && mouseInvInterfaceIndex != anInt1085) {
                            Widget childInterface = Widget.interfaceCache[anInt1084];
                            int j1 = 0;
                            if (anInt913 == 1 && childInterface.contentType == 206)
                                j1 = 1;
                            if (childInterface.inventoryItemId[mouseInvInterfaceIndex] <= 0)
                                j1 = 0;
                            if (childInterface.replaceItems) {
                                int l2 = anInt1085;
                                int l3 = mouseInvInterfaceIndex;
                                childInterface.inventoryItemId[l3] =
                                        childInterface.inventoryItemId[l2];
                                childInterface.inventoryAmounts[l3] =
                                        childInterface.inventoryAmounts[l2];
                                childInterface.inventoryItemId[l2] = -1;
                                childInterface.inventoryAmounts[l2] = 0;
                            } else if (j1 == 1) {
                                int i3 = anInt1085;
                                for (int i4 = mouseInvInterfaceIndex; i3 != i4; )
                                    if (i3 > i4) {
                                        childInterface.swapInventoryItems(i3, i3 - 1);
                                        i3--;
                                    } else if (i3 < i4) {
                                        childInterface.swapInventoryItems(i3, i3 + 1);
                                        i3++;
                                    }

                            } else {
                                childInterface.swapInventoryItems(anInt1085,
                                        mouseInvInterfaceIndex);
                            }

                            sendPacket(new SwitchItemSlot(anInt1084, j1, anInt1085, mouseInvInterfaceIndex).create());
                        }
                    }
                } else if ((anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1))
                        && menuActionRow > 2)
                    determineMenuSize();
                else if (menuActionRow > 0)
                    processMenuActions(menuActionRow - 1);
                atInventoryLoopCycle = 10;
                super.clickMode3 = 0;
            }
        }
        if (SceneGraph.clickedTileX != -1) {
            int k = SceneGraph.clickedTileX;
            int k1 = SceneGraph.clickedTileY;
            boolean flag = doWalkTo(0, 0, 0, 0, localPlayer.pathY[0], 0, 0, k1,
                    localPlayer.pathX[0], true, k);
            SceneGraph.clickedTileX = -1;
            if (flag) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 1;
                crossIndex = 0;
            }
        }
        if (super.clickMode3 == 1 && clickToContinueString != null) {
            clickToContinueString = null;
            updateChatbox = true;
            super.clickMode3 = 0;
        }
        processMenuClick();
        if (super.clickMode2 == 1 || super.clickMode3 == 1)
            anInt1213++;
        if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
            if (anInt1501 < 0 && !menuOpen) {
                anInt1501++;
                if (anInt1501 == 0) {
                    if (anInt1500 != 0) {
                        updateChatbox = true;
                    }
                    if (anInt1044 != 0) {
                    }
                }
            }
        } else if (anInt1501 > 0) {
            anInt1501--;
        }
        if (loadingStage == 2)
            checkForGameUsages();
        if (loadingStage == 2 && inCutScene)
            calculateCameraPosition();
        for (int i1 = 0; i1 < 5; i1++)
            quakeTimes[i1]++;

        manageTextInputs();

        if (super.idleTime++ > 9000) {
            anInt1011 = 250;
            super.idleTime = 0;
            sendPacket(new PlayerInactive().create());
        }

        if (ping_packet_counter++ > 65) {
            sendPacket(new BasicPing().create());
        }
        
        try {
            if (socketStream != null && outBuffer.currentPosition > 0) {
                socketStream.queueBytes(outBuffer.currentPosition, outBuffer.payload);
                outBuffer.currentPosition = 0;
            }
        } catch (IOException _ex) {
            dropClient();
            System.out.println(_ex);
        } catch (Exception exception) {
            resetLogout();
            System.out.println(exception);
        }
    }

    private void clearObjectSpawnRequests() {
        SpawnedObject spawnedObject = (SpawnedObject) spawns.reverseGetFirst();
        for (; spawnedObject != null; spawnedObject = (SpawnedObject) spawns.reverseGetNext())
            if (spawnedObject.getLongetivity == -1) {
                spawnedObject.delay = 0;
                method89(spawnedObject);
            } else {
                spawnedObject.unlink();
            }

    }

    private void setupLoginScreen() {
        if (topLeft1BackgroundTile != null)
            return;
        super.fullGameScreen = null;
        chatboxImageProducer = null;
        minimapImageProducer = null;
        tabImageProducer = null;
        gameScreenImageProducer = null;
        chatSettingImageProducer = null;
        Rasterizer2D.clear();
        flameLeftBackground = new ProducingGraphicsBuffer(128, 265);
        Rasterizer2D.clear();
        flameRightBackground = new ProducingGraphicsBuffer(128, 265);
        Rasterizer2D.clear();
        topLeft1BackgroundTile = new ProducingGraphicsBuffer(509, 171);
        Rasterizer2D.clear();
        bottomLeft1BackgroundTile = new ProducingGraphicsBuffer(360, 132);
        Rasterizer2D.clear();
        loginBoxImageProducer = new ProducingGraphicsBuffer(frameWidth, frameHeight);
        Rasterizer2D.clear();
        loginScreenAccessories = new ProducingGraphicsBuffer(300, 800);
        Rasterizer2D.clear();
        bottomLeft0BackgroundTile = new ProducingGraphicsBuffer(202, 238);
        Rasterizer2D.clear();
        bottomRightImageProducer = new ProducingGraphicsBuffer(203, 238);
        Rasterizer2D.clear();
        loginMusicImageProducer = new ProducingGraphicsBuffer(203, 238);
        Rasterizer2D.clear();
        middleLeft1BackgroundTile = new ProducingGraphicsBuffer(74, 94);
        Rasterizer2D.clear();
        aRSImageProducer_1115 = new ProducingGraphicsBuffer(75, 94);
        Rasterizer2D.clear();
        if (titleArchive != null) {
            drawLogo();
            loadTitleScreen();
        }
        welcomeScreenRaised = true;
    }
    
    public Sprite loadingBackground = null;
 
	public int percent = 0;

	public LoadingCircle loadingCircle = new LoadingCircle();

    private void method65(int i, int j, int k, int l, Widget class9, int i1, boolean flag,
                          int j1) {
        int anInt992;
        if (aBoolean972)
            anInt992 = 32;
        else
            anInt992 = 0;
        aBoolean972 = false;
        if (k >= i && k < i + 16 && l >= i1 && l < i1 + 16) {
            class9.scrollPosition -= anInt1213 * 4;
            if (flag) {
            }
        } else if (k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j) {
            class9.scrollPosition += anInt1213 * 4;
            if (flag) {
            }
        } else if (k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16
                && l < (i1 + j) - 16 && anInt1213 > 0) {
            int l1 = ((j - 32) * j) / j1;
            if (l1 < 8)
                l1 = 8;
            int i2 = l - i1 - 16 - l1 / 2;
            int j2 = j - 32 - l1;
            class9.scrollPosition = ((j1 - j) * i2) / j2;
            if (flag) {
            }
            aBoolean972 = true;
        }
    }

    private boolean clickObject(int i, int j, int k) {
        int i1 = i >> 14 & 0x7fff;
        int j1 = scene.getMask(plane, k, j, i);
        if (j1 == -1)
            return false;
        int k1 = j1 & 0x1f;
        int l1 = j1 >> 6 & 3;
        if (k1 == 10 || k1 == 11 || k1 == 22) {
            ObjectDefinition class46 = ObjectDefinition.lookup(i1);
            int i2;
            int j2;
            if (l1 == 0 || l1 == 2) {
                i2 = class46.objectSizeX;
                j2 = class46.objectSizeY;
            } else {
                i2 = class46.objectSizeY;
                j2 = class46.objectSizeX;
            }
            int k2 = class46.surroundings;
            if (l1 != 0)
                k2 = (k2 << l1 & 0xf) + (k2 >> 4 - l1);
            doWalkTo(2, 0, j2, 0, localPlayer.pathY[0], i2, k2, j, localPlayer.pathX[0],
                    false, k);
        } else {
            doWalkTo(2, l1, 0, k1 + 1, localPlayer.pathY[0], 0, 0, j, localPlayer.pathX[0],
                    false, k);
        }
        crossX = super.saveClickX;
        crossY = super.saveClickY;
        crossType = 2;
        crossIndex = 0;
        return true;
    }

    public void playSong(int id) {
        if (id != currentSong && Configuration.enableMusic && !lowMemory && prevSong == 0) {
            nextSong = id;
            fadeMusic = true;
            resourceProvider.provide(2, nextSong);
            currentSong = id;
        }
    }

    public void stopMidi() {
        if (Signlink.music != null) {
            Signlink.music.stop();
        }
        Signlink.fadeMidi = 0;
        Signlink.midi = "stop";
    }

    private void adjustVolume(boolean updateMidi, int volume) {
        Signlink.setVolume(volume);
        if (updateMidi) {
            Signlink.midi = "voladjust";
        }
    }

    private boolean saveWave(byte data[], int id) {
        return data == null || Signlink.wavesave(data, id);
    }

    private void processTrackUpdates() {
        for (int count = 0; count < trackCount; count++) {
            boolean replay = false;
            try {
                Buffer stream = Track.data(trackLoops[count], tracks[count]);
                new SoundPlayer(
                        (InputStream) new ByteArrayInputStream(stream.payload, 0,
                                stream.currentPosition),
                        soundVolume[count], soundDelay[count]);
                if (System.currentTimeMillis()
                        + (long) (stream.currentPosition / 22) > trackTimer
                        + (long) (currentTrackTime / 22)) {
                    currentTrackTime = stream.currentPosition;
                    trackTimer = System.currentTimeMillis();
                    if (saveWave(stream.payload, stream.currentPosition)) {
                    } else {
                        replay = true;
                    }
                }
            } catch (Exception exception) {
            }
            if (!replay || soundDelay[count] == -5) {
                trackCount--;
                for (int index = count; index < trackCount; index++) {
                    tracks[index] = tracks[index + 1];
                    trackLoops[index] = trackLoops[index + 1];
                    soundDelay[index] = soundDelay[index + 1];
                    soundVolume[index] = soundVolume[index + 1];
                }
                count--;
            } else {
                soundDelay[count] = -5;
            }
        }

        if (prevSong > 0) {
            prevSong -= 20;
            if (prevSong < 0)
                prevSong = 0;
            if (prevSong == 0 && Configuration.enableMusic && !lowMemory) {
                nextSong = currentSong;
                fadeMusic = true;
                resourceProvider.provide(2, nextSong);
            }
        }
    }

    public FileArchive createArchive(int file, String displayedName, String name,
                                     int expectedCRC) {
        byte buffer[] = null;

        try {
            if (indices[0] != null)
                buffer = indices[0].decompress(file);
        } catch (Exception _ex) {
        }

        //Compare crc...
        if (buffer != null) {
            if (Configuration.JAGCACHED_ENABLED) {
                if (!JagGrab.compareCrc(buffer, expectedCRC)) {
                    buffer = null;
                }
            }
        }

        if (buffer != null) {
            FileArchive streamLoader = new FileArchive(buffer);
            return streamLoader;
        }

        while (buffer == null) {
            //drawLoadingText(x, "Requesting " + displayedName);
        	System.out.println("Archive Requesting: " + displayedName);
            try (DataInputStream in = JagGrab.openJagGrabRequest(name)) {

                //Try to get the file..
                buffer = JagGrab.getBuffer(in);

                //Compare crc again...
                if (buffer != null) {
                    if (!JagGrab.compareCrc(buffer, expectedCRC)) {
                        buffer = null;
                    }
                }

                //Write file
                if (buffer != null) {
                    try {
                        if (indices[0] != null)
                            indices[0].writeFile(buffer.length, buffer, file);
                    } catch (Exception _ex) {
                        indices[0] = null;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                buffer = null;
            }

            if (buffer == null) {
                JagGrab.error("Archives");
            }
        }

        FileArchive streamLoader_1 = new FileArchive(buffer);
        return streamLoader_1;
    }

    private void dropClient() {
        if (anInt1011 > 0) {
            resetLogout();
            return;
        }
        Rasterizer2D.drawBoxOutline(2, 2, 229, 39, 0xffffff); // white box around
        Rasterizer2D.drawBox(3, 3, 227, 37, 0); // black fill
        regularText.drawText(0, "Connection lost.", 19, 120);
        regularText.drawText(0xffffff, "Connection lost.", 18, 119);
        regularText.drawText(0, "Please wait - attempting to reestablish.", 34, 117);
        regularText.drawText(0xffffff, "Please wait - attempting to reestablish.", 34, 116);
        gameScreenImageProducer.drawGraphics(frameMode == ScreenMode.FIXED ? 4 : 0,
                super.graphics, frameMode == ScreenMode.FIXED ? 4 : 0);
        minimapState = 0;
        destinationX = 0;
        BufferedConnection rsSocket = socketStream;
        loggedIn = false;
        loginFailures = 0;
        login(myUsername, myPassword, true);
        if (!loggedIn)
            resetLogout();
        try {
            rsSocket.close();
        } catch (Exception _ex) {
        }
    }

    public void setNorth() {
        cameraX = 0;
        cameraY = 0;
        cameraRotation = 0;
        cameraHorizontal = 0;
        minimapRotation = 0;
        minimapZoom = 0;
    }

    int lastNpcClicked = -1;
    
    //TODO menu actions
    private void processMenuActions(int id) {
        if (id < 0) {
            return;
        }

        searchingSpawnTab = false;

        if (inputDialogState != 0
        		&& inputDialogState != 3) {
            inputDialogState = 0;
            updateChatbox = true;
        }

        int first = firstMenuAction[id];
        int button = secondMenuAction[id];
        int action = menuActionTypes[id];
        int clicked = selectedMenuActions[id];
        if (Configuration.clientDebug) {
            System.out.println("First: " + first + " Interface: " + button + " Action: " + action + " Clicked: " + clicked);
        }

        if (action >= 2000) {
            action -= 2000;
        }

        // World map orb
        if (action == 850) {
            sendPacket(new ClickButton(156).create());
            return;
        }
        // Spec orb
        if (action == 851) {
            sendPacket(new ClickButton(155).create());
            return;
        }
        if (action == 950) { //Data
        	Configuration.clientData = !Configuration.clientData;
            return;
        }
        if (action == 960) { //Media mode
        	if(frameMode == ScreenMode.FIXED) {
        		sendMessage("@red@You have to be in Fullscreen mode to do this.", 0, "");
        		return;
        	} else {
            	Configuration.mediaMode = !Configuration.mediaMode;
        	}
            return;
        }


        // Placeholder releasing
        if (action == 633) {
            if (menuOpen) {
                action = 632;
            } else {
                determineMenuSize();
            }
        }

        // Click logout tab
        if (action == 700) {
            if (tabInterfaceIDs[10] != -1) {
                if (tabId == 10) {
                    showTabComponents = !showTabComponents;
                } else {
                    showTabComponents = true;
                }
                tabId = 10;
                tabAreaAltered = true;
            }
        }

        if (action == 769) {
            Widget d = Widget.interfaceCache[button];
            Widget p = Widget.interfaceCache[clicked];
            if (!d.dropdown.isOpen()) {
                if (p.dropdownOpen != null) {
                    p.dropdownOpen.dropdown.setOpen(false);
                }
                p.dropdownOpen = d;
            } else {
                p.dropdownOpen = null;
            }
            d.dropdown.setOpen(!d.dropdown.isOpen());
        } else if (action == 770) {
            Widget d = Widget.interfaceCache[button];
            Widget p = Widget.interfaceCache[clicked];
            if (first >= d.dropdown.getOptions().length)
                return;
            d.dropdown.setSelected(d.dropdown.getOptions()[first]);
            d.dropdown.setOpen(false);
            d.dropdown.getDrop().selectOption(first, d);
            p.dropdownOpen = null;
        }

        // reset compass to north
        if (action == 696) {
            setNorth();
        }

        // custom
        if (action == 1506 && UserInterface.TOGGLE_ORBS.toggled()) { // Select quick
            // prayers
			/*  outgoing.writeOpcode(185);
                  outgoing.writeShort(5001);*/
            sendPacket(new ClickButton(1506).create());
            return;
        }

        // custom
        if (action == 1500 && UserInterface.TOGGLE_ORBS.toggled()) { // Toggle quick
            // prayers
            sendPacket(new ClickButton(1500).create());
            return;
        }


        // button clicks
        switch (action) {
 
            case 1315:
            case 1316:
            case 1317:
            case 1318:
            case 1319:
            case 1320:
            case 1321:
            case 879:
            case 850:
            case 475:
            case 476:
            case 1050:
                // button click
                sendPacket(new ClickButton(action).create());
                break;
        }

     
        if (action == 258) {
            Configuration.expCounterOpen = !Configuration.expCounterOpen;
            return;
        } else if (action == 257) {
            Configuration.mergeExpDrops = !Configuration.mergeExpDrops;
            return;
        }

        // click autocast
        if (action == 104) {
            Widget widget = Widget.interfaceCache[button];
            sendPacket(new ClickButton(widget.id).create());
			/*spellId = widget.id;
			if (!autocast) {
				autocast = true;
				autoCastId = widget.id;
				sendPacket(new ClickButton(widget.id));
			} else if (autoCastId == widget.id) {
				autocast = false;
				autoCastId = 0;
				sendPacket(new ClickButton(widget.id));
			} else if (autoCastId != widget.id) {
				autocast = true;
				autoCastId = widget.id;
				sendPacket(new ClickButton(widget.id));
			}*/
        }

        // item on npc
        if (action == 582) {
            Npc npc = npcs[clicked];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, npc.pathY[0],
                        localPlayer.pathX[0], false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                sendPacket(new ItemOnNpc(anInt1285, clicked, anInt1283, anInt1284).create());
            }
        }

        // picking up ground item
        if (action == 234) {
            boolean flag1 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0, button,
                    localPlayer.pathX[0], false, first);
            if (!flag1)
                flag1 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, button,
                        localPlayer.pathX[0], false, first);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // pickup ground item
            sendPacket(new PickupItem(button + regionBaseY, clicked, first + regionBaseX).create());
        }

        // using item on object
        if (action == 62 && clickObject(clicked, button, first)) {
            sendPacket(new ItemOnObject(anInt1284, clicked >> 14 & 0x7fff, button + regionBaseY, anInt1283, first + regionBaseX, anInt1285).create());
        }

        // using item on ground item
        if (action == 511) {
            boolean flag2 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0, button,
                    localPlayer.pathX[0], false, first);
            if (!flag2)
                flag2 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, button,
                        localPlayer.pathX[0], false, first);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // item on ground item
            sendPacket(new ItemOnGroundItem(anInt1284, anInt1285, clicked, button + regionBaseY, anInt1283, first + regionBaseX).create());
        }

        // item option 1
        if (action == 74) {
            sendPacket(new UseItem(button, clicked, first).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Widget.interfaceCache[button].parent == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }

        // widget action
        if (action == 315) {
            Widget widget = Widget.interfaceCache[button];
            boolean flag8 = true;

			if (widget.type == Widget.TYPE_CONFIG || widget.id == 50007) { // Placeholder toggle
				widget.active = !widget.active;
			} else if (widget.type == Widget.TYPE_CONFIG_HOVER) {
				Widget.handleConfigHover(widget);
			}

			if (widget.contentType > 0)
				flag8 = promptUserForInput(widget);
			if (flag8) {
				if (AdvancedSettings.graphics_button_ids.contains(button)) {
					GameGraphics s = GameGraphics.values()[AdvancedSettings.graphics_button_ids.indexOf(button)];
					if (s.conflicting == null || s.conflicting.length == 0
							|| (s.conflicting.length > 0 && !s.toggled()))
						s.toggle();
				}
				if (AdvancedSettings.userinterface_button_ids.contains(button)) {
					UserInterface s = UserInterface.values()[AdvancedSettings.userinterface_button_ids.indexOf(button)];
					if (s.conflicting == null || s.conflicting.length == 0
							|| (s.conflicting.length > 0 && !s.toggled()))
						s.toggle();
				}
				if (AdvancedSettings.misc_button_ids.contains(button)) {
					Misc s = Misc.values()[AdvancedSettings.misc_button_ids.indexOf(button)];
					if (s.conflicting == null || s.conflicting.length == 0
							|| (s.conflicting.length > 0 && !s.toggled()))
						s.toggle();
				}
				SettingsWidget.settings(button);
				switch (button) {
				case SpellbookSorting.DEFAULT_ORDERING:
				case SpellbookSorting.COMBAT_ORDERING:
				case SpellbookSorting.TELEPORT_ORDERING:
				case SpellbookSorting.SKILL_ORDERING:
					SpellbookSorting.sort(button);
					savePlayerData();
					break;
				case 19144:
					inventoryOverlay(15106, 3213);
					resetAnimation(15106);
					updateChatbox = true;
					break;
				case 31004: // TODO remove
					searchingSpawnTab = true;
					break;
				case 31007:
					spawnType = SpawnTabType.INVENTORY;
					searchingSpawnTab = true;
					break;
				case 31011:
					spawnType = SpawnTabType.BANK;
					searchingSpawnTab = true;
					break;

				case Keybinding.RESTORE_DEFAULT:
					Keybinding.restoreDefault();
					Keybinding.updateInterface();
					sendMessage("Default keys loaded.", 0, "");
					savePlayerData();
					break;
				case Keybinding.ESCAPE_CONFIG:
					Keybinding.checkDuplicates(13, -1);
					Configuration.escapeCloseInterface = !Configuration.escapeCloseInterface;
					Widget.interfaceCache[Keybinding.ESCAPE_CONFIG].active = Configuration.escapeCloseInterface;
					savePlayerData();
					break;
					
					
					

				/** Faster spec bars toggle **/
				case 29138:
				case 29038:
				case 29063:
				case 29113:
				case 29163:
				case 29188:
				case 29213:
				case 29238:
				case 30007:
				case 48023:
				case 33033:
				case 30108:
				case 7473:
				case 7562:
				case 7487:
				case 7788:
				case 8481:
				case 7612:
				case 7587:
				case 7662:
				case 7462:
				case 7548:
				case 7687:
				case 7537:
				case 7623:
				case 12322:
				case 7637:
				case 12311:
				case 155:

					/**
					 * Just update the color of the bar before sending packet, to make it look
					 * smoother
					 **/
					WeaponInterface wepInterface = WeaponInterface.get(tabInterfaceIDs[0]);
					if (wepInterface != null && wepInterface.getSpecialMeter() > 0) {
						boolean active = Widget.interfaceCache[wepInterface.getSpecialMeter()].defaultText
								.contains("@yel@");
						if (active) {
							Widget.interfaceCache[wepInterface
									.getSpecialMeter()].defaultText = Widget.interfaceCache[wepInterface
											.getSpecialMeter()].defaultText.replaceAll("@yel@", "@bla@");
						} else {
							Widget.interfaceCache[wepInterface
									.getSpecialMeter()].defaultText = Widget.interfaceCache[wepInterface
											.getSpecialMeter()].defaultText.replaceAll("@bla@", "@yel@");
						}
					}

					sendPacket(new SpecialAttack(button).create());
					break;

				default:
					// System.out.println("BUTTON = "+button);
					sendPacket(new ClickButton(button).create());
					break;
				}
			}
        }

        // player option
        if (action == 561) {
            Player player = players[clicked];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, player.pathY[0],
                        localPlayer.pathX[0], false, player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1188 += clicked;
                if (anInt1188 >= 90) {
                    //(anti-cheat)
                    //   outgoing.writeOpcode(136);
                    anInt1188 = 0;
                }
                sendPacket(new PlayerOption1(clicked).create());
            }
        }

        // npc option 1
        if (action == 20) {
            Npc npc = npcs[clicked];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        npc.pathY[0], localPlayer.pathX[0],
                        false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // npc action 1
                sendPacket(new NpcOption1(clicked).create());
            }
        }

        // player option 2
        if (action == 779) {
            Player player = players[clicked];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        player.pathY[0], localPlayer.pathX[0],
                        false, player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // player option 2
                sendPacket(new PlayerAttackOption(clicked).create());
            }
        }

        // clicking tiles
        if (action == 519) {
            if (!menuOpen) {
                scene.clickTile(super.saveClickY - 4, super.saveClickX - 4);
            } else {
                scene.clickTile(button - 4, first - 4);
            }
        }

        // object option 5
        if (action == 1062) {
            anInt924 += regionBaseX;
            if (anInt924 >= 113) {
                // validates clicking object option 4
                // outgoing.writeOpcode(183);
                // outgoing.writeTriByte(0xe63271);
                anInt924 = 0;
            }
            clickObject(clicked, button, first);

            // object option 5
            sendPacket(new ObjectOption5(clicked >> 14 & 0x7fff, button + regionBaseY, first + regionBaseX).create());
        }

        // continue dialogue
        if (action == 679 && !continuedDialogue) {
            sendPacket(new NextDialogue(button).create());
            continuedDialogue = true;
        }

        //Pressed button
        if (action == 647) {

            //Spawn tab?
            if (button >= 31031 && button <= 31731) {
                int index = button - 31031;
                int item = getResultsArray()[index];
                if (item > 0) {
                    sendPacket(new SpawnTabClick(item, first == 1, spawnType == SpawnTabType.BANK).create());
                }
                if (first == 0) {
                    searchingSpawnTab = true;
                }
                return;
            }

            sendPacket(new ClickButtonAction(button, first).create());
        }

        // using bank all option of the bank interface
        if (action == 431) {
            sendPacket(new ItemContainerOption4(first, button, clicked).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Widget.interfaceCache[button].parent == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }


        if (action == 337 || action == 42 || action == 792 || action == 322) {
            String string = menuActionText[id];
            int indexOf = string.indexOf("@whi@");
            if (indexOf != -1) {
                long usernameHash = StringUtils.encodeBase37(string.substring(indexOf + 5).trim());
                if (action == 337) {
                    addFriend(usernameHash);
                }
                if (action == 42) {
                    addIgnore(usernameHash);
                }
                if (action == 792) {
                    removeFriend(usernameHash);
                }
                if (action == 322) {
                    removeIgnore(usernameHash);
                }
            }
        }
        // using the bank x option on the bank interface
        if (action == 53) {
            // bank x
            sendPacket(new ItemContainerOption5(first, button, clicked).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // using the second option of an item
        if (action == 539) {
            // item option 2
            sendPacket(new ItemOption3(clicked, first, button).create());

            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId) {
                atInventoryInterfaceType = 1;
            }
            if (Widget.interfaceCache[button].parent == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 484 || action == 6) {
            String string = menuActionText[id];
            int indexOf = string.indexOf("@whi@");
            if (indexOf != -1) {
                string = string.substring(indexOf + 5).trim();
                String username = StringUtils.formatText(StringUtils.decodeBase37(StringUtils.encodeBase37(string)));
                boolean flag9 = false;
                for (int count = 0; count < playerCount; count++) {
                    Player player = players[playerList[count]];
                    if (player == null || player.name == null || !player.name.equalsIgnoreCase(username)) {
                        continue;
                    }
                    doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                            player.pathY[0],
                            localPlayer.pathX[0], false,
                            player.pathX[0]);

                    // accepting trade
                    if (action == 484) {
                        //sendPacket(new ChatboxTrade(playerList[count]));
                        sendPacket(new TradePlayer(playerList[count]).create());
                    }

                    // accepting a challenge
                    if (action == 6) {
                        anInt1188 += clicked;
                        if (anInt1188 >= 90) {
                            // (anti-cheat)
                            //	outgoing.writeOpcode(136);
                            anInt1188 = 0;
                        }

                        sendPacket(new ChatboxDuel(playerList[count]).create());
                    }
                    flag9 = true;
                    break;
                }

                if (!flag9)
                    sendMessage("Unable to find " + username, 0, "");
            }
        }

        // Using an item on another item
        if (action == 870) {
            // item on item
            sendPacket(new ItemOnItem(first, anInt1283, clicked, anInt1284, anInt1285, button).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // Using the drop option of an item
        if (action == 847) {
            // Drop item
            sendPacket(new DropItem(clicked, button, first).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }
        // useable spells
        if (action == 626) {
            Widget widget = Widget.interfaceCache[button];
            spellSelected = 1;
            spellId = widget.id;
            anInt1137 = button;
            spellUsableOn = widget.spellUsableOn;
            itemSelected = 0;
            String actionName = widget.selectedActionName;
            if (actionName.indexOf(" ") != -1)
                actionName = actionName.substring(0, actionName.indexOf(" "));
            String s8 = widget.selectedActionName;
            if (s8.indexOf(" ") != -1)
                s8 = s8.substring(s8.indexOf(" ") + 1);
//            spellTooltip = actionName + " " + widget.spellName + " " + s8;
            String tooltip = widget.spellName;
            if(tooltip.contains("<br>")) {
            	tooltip = tooltip.replaceAll("<br>", " ");
			}
			spellTooltip = actionName + " <col=65280>" + widget.spellName + "</col> -> ";
            // class9_1.sprite1.drawSprite(class9_1.x, class9_1.anInt265,
            // 0xffffff);
            // class9_1.sprite1.drawSprite(200,200);
            //if (Configuration.client_debug)
            //	System.out.println(
            //		"spellId: " + spellId + " - spellSelected: " + spellSelected);
            //	System.out.println(button + " " + widget.selectedActionName + " " + anInt1137);
            if (spellUsableOn == 16) {
                tabId = 3;
                tabAreaAltered = true;
            }
            return;
        }

        // Using the bank 5 option on a bank widget
        if (action == 78) {
            // bank 5
            sendPacket(new ItemContainerOption2(button, clicked, first).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // player option 2
        if (action == 27) {
            Player player = players[clicked];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        player.pathY[0], localPlayer.pathX[0],
                        false, player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt986 += clicked;
                if (anInt986 >= 54) {
                    //(anti-cheat)
                    //	outgoing.writeOpcode(189);
                    //	outgoing.writeByte(234);
                    anInt986 = 0;
                }
                // attack player
                sendPacket(new FollowPlayer(clicked).create());
            }
        }

        // Used for lighting logs
        if (action == 213) {
            boolean flag3 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0, button,
                    localPlayer.pathX[0], false, first);
            if (!flag3)
                flag3 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, button,
                        localPlayer.pathX[0], false, first);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // light item
			/*outgoing.writeOpcode(79);
			outgoing.writeLEShort(button + regionBaseY);
			outgoing.writeShort(clicked);
			outgoing.writeShortA(first + regionBaseX);*/
        }

        // Using the unequip option on the equipment tab interface
        if (action == 632) {
            sendPacket(new ItemContainerOption1(button, first, clicked).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        if (action == 1004) {
            if (tabInterfaceIDs[10] != -1) {
                tabId = 10;
                tabAreaAltered = true;
            }
        }
        if (action == 1003) {
            clanChatMode = 2;
            updateChatbox = true;
        }
        if (action == 1002) {
            clanChatMode = 1;
            updateChatbox = true;
        }
        if (action == 1001) {
            clanChatMode = 0;
            updateChatbox = true;
        }
        if (action == 1000) {
            cButtonCPos = 4;
            chatTypeView = 11;
            updateChatbox = true;
        }

        if (action == 999) {
            cButtonCPos = 0;
            chatTypeView = 0;
            updateChatbox = true;
        }
        if (action == 998) {
            cButtonCPos = 1;
            chatTypeView = 5;
            updateChatbox = true;
        }

        // public chat "hide" option
        if (action == 997) {
            publicChatMode = 3;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // public chat "off" option
        if (action == 996) {
            publicChatMode = 2;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // public chat "friends" option
        if (action == 995) {
            publicChatMode = 1;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // public chat "on" option
        if (action == 994) {
            publicChatMode = 0;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // public chat main click
        if (action == 993) {
            cButtonCPos = 2;
            chatTypeView = 1;
            updateChatbox = true;
        }

        // private chat "off" option
        if (action == 992) {
            privateChatMode = 2;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // private chat "friends" option
        if (action == 991) {
            privateChatMode = 1;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // private chat "on" option
        if (action == 990) {
            privateChatMode = 0;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // private chat main click
        if (action == 989) {
            cButtonCPos = 3;
            chatTypeView = 2;
            updateChatbox = true;
        }

        // trade message privacy option "off" option
        if (action == 987) {
            tradeMode = 2;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // trade message privacy option "friends" option
        if (action == 986) {
            tradeMode = 1;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // trade message privacy option "on" option
        if (action == 985) {
            tradeMode = 0;
            updateChatbox = true;

            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
        }

        // trade message privacy option main click
        if (action == 984) {
            cButtonCPos = 5;
            chatTypeView = 3;
            updateChatbox = true;
        }

        // yell message privacy option "off" option
        if (action == 976) {
            yellMode = 2;
            updateChatbox = true;
            //sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode));
        }

        // yell message privacy option "on" option
        if (action == 975) {
            yellMode = 0;
            updateChatbox = true;
            //sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode));
        }

        // yell message main click
        if (action == 974) { // was 980
            cButtonCPos = 6;
            chatTypeView = 4;
            updateChatbox = true;
        }

        // Using 3rd option of an item
        if (action == 493) {
            // item option 3
            sendPacket(new ItemOption2(button, first, clicked).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // clicking some sort of tile
        if (action == 652) {
            boolean flag4 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0, button,
                    localPlayer.pathX[0], false, first);
            if (!flag4)
                flag4 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, button,
                        localPlayer.pathX[0], false, first);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            //unknown (non-anti bot)
			/*outgoing.writeOpcode(156);
			outgoing.writeShortA(first + regionBaseX);
			outgoing.writeLEShort(button + regionBaseY);
			outgoing.writeLEShortA(clicked);*/
        }

        // Using a spell on a ground item
        if (action == 94) {
            boolean flag5 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0, button,
                    localPlayer.pathX[0], false, first);
            if (!flag5)
                flag5 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, button,
                        localPlayer.pathX[0], false, first);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // magic on ground item
            sendPacket(new MagicOnGroundItem(button + regionBaseY, clicked, first + regionBaseX, anInt1137).create());
        }
        if (action == 646) {
            // button click

            switch (button) {
                case 930:
                    Client.cameraZoom = 1200;
                    break;
                case 931:
                    Client.cameraZoom = 800;
                    break;
                case 932:
                    Client.cameraZoom = 400;
                    break;
                case 933:
                    Client.cameraZoom = 200;
                    break;
                case 934:
                    Client.cameraZoom = 0;
                    break;
                case 32506:
                    Bank.bankTabShow = BankTabShow.FIRST_ITEM_IN_TAB;
                    break;
                case 32507:
                    Bank.bankTabShow = BankTabShow.DIGIT;
                    break;
                case 32508:
                    Bank.bankTabShow = BankTabShow.ROMAN_NUMERAL;
                    break;

                default:
                    sendPacket(new ClickButton(button).create());
                    break;
            }

            Widget widget = Widget.interfaceCache[button];
            if (widget.valueIndexArray != null && widget.valueIndexArray[0][0] == 5) {
                int i2 = widget.valueIndexArray[0][1];
                if (settings[i2] != widget.requiredValues[0]) {
                    settings[i2] = widget.requiredValues[0];
                    updateVarp(i2);
                }
            }
        }

        // Using the 2nd option of an npc
        if (action == 225) {
            Npc npc = npcs[clicked];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        npc.pathY[0], localPlayer.pathX[0],
                        false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1226 += clicked;
                if (anInt1226 >= 85) {
                    // (anti-cheat)
                    //outgoing.writeOpcode(230);
                    //outgoing.writeByte(239);
                    anInt1226 = 0;
                }
                // npc option 2
                sendPacket(new NpcOption2(clicked).create());
            }
        }

        // Using the 3rd option of an npc
        if (action == 965) {
            Npc npc = npcs[clicked];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, npc.pathY[0], localPlayer.pathX[0], false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1134++;
                if (anInt1134 >= 96) {
                    //(anti-cheat)
                    //outgoing.writeOpcode(152);
                    //outgoing.writeByte(88);
                    anInt1134 = 0;
                }
                // npc option 3
                sendPacket(new NpcOption3(clicked).create());
            }
        }

        // Using a spell on an npc
        if (action == 413) {
            Npc npc = npcs[clicked];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, npc.pathY[0],
                        localPlayer.pathX[0], false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // magic on npc
                sendPacket(new MagicOnNpc(clicked, anInt1137).create());
            }
        }

        // Close open interfaces
        if (action == 200) {
            clearTopInterfaces();
        }

        // Clicking "Examine" option on an npc
        if (action == 1025 || action == 1025) {
            Npc npc = npcs[clicked];
            if (npc != null) {
                NpcDefinition entityDef = npc.desc;
                if (entityDef.childrenIDs != null)
                    entityDef = entityDef.morph();
                if (entityDef != null) {
                    sendPacket(new ExamineNpc(entityDef.id).create());
                }
            }
        }

        if (action == 900) {
            clickObject(clicked, button, first);
            // object option 2
            sendPacket(new ObjectOption2(clicked >> 14 & 0x7fff, button + regionBaseY, first + regionBaseX).create());
        }

        // Using the "Attack" option on a npc
        if (action == 412) {
            Npc npc = npcs[clicked];
            if (npc != null) {
                boolean canReach = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        npc.pathY[0], localPlayer.pathX[0],
                        false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                if (canReach)
                	sendPacket(new AttackNpc(clicked).create());
            }
        }

        // Using spells on a player
        if (action == 365) {
            Player player = players[clicked];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, player.pathY[0],
                        localPlayer.pathX[0], false, player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // spells on plr
                sendPacket(new MagicOnPlayer(clicked, anInt1137).create());
            }
        }

        // Using the 3rd option of a player
        if (action == 729) {
            Player player = players[clicked];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        player.pathY[0], localPlayer.pathX[0],
                        false, player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                sendPacket(new TradePlayer(clicked).create());
            }
        }

        // Using the 4th option of a player
        if (action == 577) {
            Player player = players[clicked];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        player.pathY[0], localPlayer.pathX[0],
                        false, player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // trade request
                sendPacket(new TradePlayer(clicked).create());
            }
        }

        // Using a spell on an item
        if (action == 956 && clickObject(clicked, button, first)) {
            // magic on item
            //	sendPacket(new MagicOnItem(first + regionBaseX, anInt1137, button + regionBaseY, clicked >> 14 & 0x7fff));
        }

        // Some walking action (packet 23)
        if (action == 567) {
            boolean flag6 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0, button,
                    localPlayer.pathX[0], false, first);
            if (!flag6)
                flag6 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, button,
                        localPlayer.pathX[0], false, first);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            //anti-cheat)
			/*outgoing.writeOpcode(23);
			outgoing.writeLEShort(button + regionBaseY);
			outgoing.writeLEShort(clicked);
			outgoing.writeLEShort(first + regionBaseX);*/
        }

        // Using the bank 10 option on the bank interface
        if (action == 867) {

            if ((clicked & 3) == 0) {
                anInt1175++;
            }

            if (anInt1175 >= 59) {
                //(anti-cheat)
                //outgoing.writeOpcode(200);
                //outgoing.writeShort(25501);
                anInt1175 = 0;
            }
            // bank 10
            sendPacket(new ItemContainerOption3(button, clicked, first).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // Using a spell on an inventory item
        if (action == 543) {
            // magic on item
            sendPacket(new MagicOnItem(first, clicked, button, anInt1137).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // Report abuse button
        if (action == 606) {
            menuActionText[id].equals("View Tools");
            if (openInterfaceId == -1) {
                clearTopInterfaces();
                canMute = false;
                for (int index = 0; index < Widget.interfaceCache.length; index++) {
                    if (Widget.interfaceCache[index] == null || Widget.interfaceCache[index].contentType != 600)
                        continue;
                    reportAbuseInterfaceID = openInterfaceId = Widget.interfaceCache[index].parent;
                    break;
                }
            } else {
                sendMessage("Please close the interface you have open before using this.", 0, "");
            }
        }

        // Using an inventory item on a player
        if (action == 491) {
            Player player = players[clicked];

            if (player != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        player.pathY[0], localPlayer.pathX[0],
                        false, player.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // TODO item on player
                sendPacket(new ItemOnPlayer(anInt1284, clicked, anInt1285, anInt1283).create());
            }
        }

        // reply to private message
        if (action == 639) {
            String text = menuActionText[id];

            int indexOf = text.indexOf("@whi@");

            if (indexOf != -1) {
                long usernameHash = StringUtils.encodeBase37(text.substring(indexOf + 5).trim());
                int resultIndex = -1;
                for (int friendIndex = 0; friendIndex < friendsCount; friendIndex++) {
                    if (friendsListAsLongs[friendIndex] != usernameHash) {
                        continue;
                    }
                    resultIndex = friendIndex;
                    break;
                }

                if (resultIndex != -1 && friendsNodeIDs[resultIndex] > 0) {
                    updateChatbox = true;
                    inputDialogState = 0;
                    //ChatboxItemSearch.reset();
                    messagePromptRaised = true;
                    promptInput = "";
                    friendsListAction = 3;
                    aLong953 = friendsListAsLongs[resultIndex];
                    aString1121 = "Enter a message to send to " + friendsList[resultIndex];
                }
            }
        }

        // Using the equip option of an item in the inventory
        if (action == 454) {
            //equip item
            sendPacket(new EquipItem(clicked, first, button).create());
            atInventoryLoopCycle = 0;
            atInventoryInterface = button;
            atInventoryIndex = first;
            atInventoryInterfaceType = 2;
            if (Widget.interfaceCache[button].parent == openInterfaceId)
                atInventoryInterfaceType = 1;
            if (Widget.interfaceCache[button].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // Npc option 4
        if (action == 478) {
            Npc npc = npcs[clicked];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0,
                        npc.pathY[0], localPlayer.pathX[0],
                        false, npc.pathX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;

                if ((clicked & 3) == 0) {
                    anInt1155++;
                }

                if (anInt1155 >= 53) {
                    //TODO unknown (anti-cheat)
                    //	outgoing.writeOpcode(85);
                    //	outgoing.writeByte(66);
                    anInt1155 = 0;
                }

                // npc option 4
                sendPacket(new NpcOption4(clicked).create());
            }
        }

        // Object option 3
        if (action == 113) {
            clickObject(clicked, button, first);
            // object option 3
            sendPacket(new ObjectOption3(first + regionBaseX, button + regionBaseY, clicked >> 14 & 0x7fff).create());
        }

        // Object option 4
        if (action == 872) {
            clickObject(clicked, button, first);
            sendPacket(new ObjectOption4(first + regionBaseX, clicked >> 14 & 0x7fff, button + regionBaseY).create());
        }

        // Object option 1
        if (action == 502) {
            clickObject(clicked, button, first);
            sendPacket(new ObjectOption1(first + regionBaseX, clicked >> 14 & 0x7fff, button + regionBaseY).create());
        }


        if (action == 169) {

            sendPacket(new ClickButton(button).create());

            if (button != 19158) { // Run button, server handles config
                Widget widget = Widget.interfaceCache[button];

                if (widget.valueIndexArray != null && widget.valueIndexArray[0][0] == 5) {
                    int setting = widget.valueIndexArray[0][1];
                    settings[setting] = 1 - settings[setting];
                    updateVarp(setting);
                    //ChatboxItemSearch.reset();
                }
            }
        }

        if (action == 447) {
            itemSelected = 1;
            anInt1283 = first;
            anInt1284 = button;
            anInt1285 = clicked;
            selectedItemName = ItemDefinition.lookup(clicked).name;
            spellSelected = 0;
            return;
        }

        if (action == 1226) {
            int objectId = clicked >> 14 & 0x7fff;
            ObjectDefinition definition = ObjectDefinition.lookup(objectId);
            String message;
            if (definition.description != null)
                message = new String(definition.description);
            else
                message = "It's a " + definition.name + ".";
            sendMessage(message, 0, "");
        }

        // Click First Option Ground Item
        if (action == 244) {
            boolean flag7 = doWalkTo(2, 0, 0, 0, localPlayer.pathY[0], 0, 0, button,
                    localPlayer.pathX[0], false, first);
            if (!flag7)
                flag7 = doWalkTo(2, 0, 1, 0, localPlayer.pathY[0], 1, 0, button,
                        localPlayer.pathX[0], false, first);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            sendPacket(new SecondGroundItemOption(button + regionBaseY, clicked, first + regionBaseX).create());
			/*TODO: NO idea SOMETHING WITH GROUNDITEMS
			outgoing.writeOpcode(253);
			outgoing.writeLEShort(first + regionBaseX);
			outgoing.writeLEShortA(button + regionBaseY);
			outgoing.writeShortA(clicked);*/
        }


        if (action == 1448 || action == 1125) {
            ItemDefinition definition = ItemDefinition.lookup(clicked);
            if (definition != null) {
//            	System.out.println("Open Interface: " + openInterfaceId);
//            	System.out.println("Button Interface: " + button);
                sendPacket(new ExamineItem(clicked, button).create());
            }
        }

        itemSelected = 0;
        spellSelected = 0;

    }
    
	public static boolean hasCash(long amount) {
		for (int i = 0; i < Widget.interfaceCache[3214].inventoryItemId.length; i++) {
			if (Widget.interfaceCache[3214].inventoryItemId[i] - 1 == 995 && Widget.interfaceCache[3214].inventoryAmounts[i] >= amount)
				return true;
		}
		return false;
	}
	
    public static boolean hasItem(int itemId, long amount) {
		int hasAmount = 0;
		for (int i = 0; i < Widget.interfaceCache[3214].inventoryItemId.length; i++) {
			if (Widget.interfaceCache[3214].inventoryItemId[i] - 1 == itemId)
				hasAmount += Widget.interfaceCache[3214].inventoryAmounts[i];
		}
		return hasAmount >= amount;
	}

    public void run() {
        if (drawFlames) {
            drawFlames();
        } else {
            super.run();
        }
    }

    private void createMenu() {
        if (openInterfaceId == 15244) { // TODO: change to fullscreen?
            return;
        }
        if (itemSelected == 0 && spellSelected == 0) {
            menuActionText[menuActionRow] = shiftTeleport() ? "Teleport here" : "Walk here";
            menuActionTypes[menuActionRow] = 519;
            firstMenuAction[menuActionRow] = super.mouseX;
            secondMenuAction[menuActionRow] = super.mouseY;
            menuActionRow++;
        }

        int j = -1;
        for (int k = 0; k < Model.anInt1687; k++) {
            int l = Model.anIntArray1688[k];
            int i1 = l & 0x7f;
            int j1 = l >> 7 & 0x7f;
            int k1 = l >> 29 & 3;
            int l1 = l >> 14 & 0x7fff;
            if (l == j)
                continue;
            j = l;
            if (k1 == 2 && scene.getMask(plane, i1, j1, l) >= 0) {
                ObjectDefinition objectDef = ObjectDefinition.lookup(l1);
                if (objectDef.morphisms != null)
                    objectDef = objectDef.method580();
                if (objectDef == null)
                    continue;
                if (itemSelected == 1) {
                    menuActionText[menuActionRow] = "Use " + selectedItemName
                            + " with @cya@" + objectDef.name;
                    menuActionTypes[menuActionRow] = 62;
                    selectedMenuActions[menuActionRow] = l;
                    firstMenuAction[menuActionRow] = i1;
                    secondMenuAction[menuActionRow] = j1;
                    menuActionRow++;
                } else if (spellSelected == 1) {
                    if ((spellUsableOn & 4) == 4) {
                        menuActionText[menuActionRow] =
                                spellTooltip + " @cya@" + objectDef.name;
                        menuActionTypes[menuActionRow] = 956;
                        selectedMenuActions[menuActionRow] = l;
                        firstMenuAction[menuActionRow] = i1;
                        secondMenuAction[menuActionRow] = j1;
                        menuActionRow++;
                    }
                } else {
                    if (objectDef.interactions != null) {
                        for (int type = 4; type >= 0; type--)
                            if (objectDef.interactions[type] != null) {
                                menuActionText[menuActionRow] =
                                        objectDef.interactions[type] + " @cya@"
                                                + objectDef.name;
                                if (type == 0)
                                    menuActionTypes[menuActionRow] = 502;
                                if (type == 1)
                                    menuActionTypes[menuActionRow] = 900;
                                if (type == 2)
                                    menuActionTypes[menuActionRow] = 113;
                                if (type == 3)
                                    menuActionTypes[menuActionRow] = 872;
                                if (type == 4)
                                    menuActionTypes[menuActionRow] = 1062;
                                selectedMenuActions[menuActionRow] = l;
                                firstMenuAction[menuActionRow] = i1;
                                secondMenuAction[menuActionRow] = j1;
                                menuActionRow++;
                            }

                    }
                    if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                        menuActionText[menuActionRow] = "Examine @cya@" + objectDef.name
                                + " @gre@(@whi@" + l1 + "@gre@) (@whi@"
                                + (i1 + regionBaseX) + "," + (j1 + regionBaseY)
                                + "@gre@) (@whi@" + objectDef.varp + "," + objectDef.varbit + "@gre@)";
                        if (GameGraphics.FOG.toggled()) {
                        	//sendMessage("" + objectDef., 2, "");
                        }
                    } else {
                        menuActionText[menuActionRow] =
                                "Examine @cya@" + objectDef.name;
                    }
                    menuActionTypes[menuActionRow] = 1226;
                    selectedMenuActions[menuActionRow] = objectDef.type << 14;
                    firstMenuAction[menuActionRow] = i1;
                    secondMenuAction[menuActionRow] = j1;
                    menuActionRow++;
                }
            }
            if (k1 == 1) {
                Npc npc = npcs[l1];
                try {
                    if (npc.desc.size == 1 && (npc.x & 0x7f) == 64
                            && (npc.y & 0x7f) == 64) {
                        for (int j2 = 0; j2 < npcCount; j2++) {
                            Npc npc2 = npcs[npcIndices[j2]];
                            if (npc2 != null && npc2 != npc && npc2.desc.size == 1
                                    && npc2.x == npc.x && npc2.y == npc.y) {
                                if (npc2.showActions()) {
                                    buildAtNPCMenu(npc2.desc, npcIndices[j2], j1, i1);
                                }
                            }
                        }
                        for (int l2 = 0; l2 < playerCount; l2++) {
                            Player player = players[playerList[l2]];
                            if (player != null && player.x == npc.x
                                    && player.y == npc.y)
                                buildAtPlayerMenu(i1, playerList[l2], player,
                                        j1);
                        }
                    }
                    if (npc.showActions()) {
                        buildAtNPCMenu(npc.desc, l1, j1, i1);
                    }
                } catch (Exception e) {
                }
            }
            if (k1 == 0) {
                Player player = players[l1];
                if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
                    for (int k2 = 0; k2 < npcCount; k2++) {
                        Npc class30_sub2_sub4_sub1_sub1_2 = npcs[npcIndices[k2]];
                        if (class30_sub2_sub4_sub1_sub1_2 != null
                                && class30_sub2_sub4_sub1_sub1_2.desc.size == 1
                                && class30_sub2_sub4_sub1_sub1_2.x == player.x
                                && class30_sub2_sub4_sub1_sub1_2.y == player.y)
                            buildAtNPCMenu(class30_sub2_sub4_sub1_sub1_2.desc,
                                    npcIndices[k2], j1, i1);
                    }

                    for (int i3 = 0; i3 < playerCount; i3++) {
                        Player class30_sub2_sub4_sub1_sub2_2 =
                                players[playerList[i3]];
                        if (class30_sub2_sub4_sub1_sub2_2 != null
                                && class30_sub2_sub4_sub1_sub2_2 != player
                                && class30_sub2_sub4_sub1_sub2_2.x == player.x
                                && class30_sub2_sub4_sub1_sub2_2.y == player.y)
                            buildAtPlayerMenu(i1, playerList[i3],
                                    class30_sub2_sub4_sub1_sub2_2, j1);
                    }

                }
                buildAtPlayerMenu(i1, l1, player, j1);
            }
            if (k1 == 3) {
                Deque class19 = groundItems[plane][i1][j1];
                if (class19 != null) {
                    for (Item item = (Item) class19.getFirst(); item != null; item =
                            (Item) class19.getNext()) {
                        ItemDefinition itemDef = ItemDefinition.lookup(item.ID);
                        if (itemSelected == 1) {
                            menuActionText[menuActionRow] = "Use " + selectedItemName
                                    + " with @lre@" + itemDef.name;
                            menuActionTypes[menuActionRow] = 511;
                            selectedMenuActions[menuActionRow] = item.ID;
                            firstMenuAction[menuActionRow] = i1;
                            secondMenuAction[menuActionRow] = j1;
                            menuActionRow++;
                        } else if (spellSelected == 1) {
                            if ((spellUsableOn & 1) == 1) {
                                menuActionText[menuActionRow] =
                                        spellTooltip + " @lre@" + itemDef.name;
                                menuActionTypes[menuActionRow] = 94;
                                selectedMenuActions[menuActionRow] = item.ID;
                                firstMenuAction[menuActionRow] = i1;
                                secondMenuAction[menuActionRow] = j1;
                                menuActionRow++;
                            }
                        } else {
                            for (int j3 = 4; j3 >= 0; j3--)
                                if (itemDef.groundActions != null
                                        && itemDef.groundActions[j3] != null) {
                                    menuActionText[menuActionRow] =
                                            itemDef.groundActions[j3]
                                                    + " @lre@"
                                                    + itemDef.name;
                                    if (j3 == 0)
                                        menuActionTypes[menuActionRow] = 652;
                                    if (j3 == 1)
                                        menuActionTypes[menuActionRow] = 567;
                                    if (j3 == 2)
                                        menuActionTypes[menuActionRow] = 234;
                                    if (j3 == 3)
                                        menuActionTypes[menuActionRow] = 244;
                                    if (j3 == 4)
                                        menuActionTypes[menuActionRow] = 213;
                                    selectedMenuActions[menuActionRow] = item.ID;
                                    firstMenuAction[menuActionRow] = i1;
                                    secondMenuAction[menuActionRow] = j1;
                                    menuActionRow++;
                                } else if (j3 == 2) {
                                    menuActionText[menuActionRow] =
                                            "Take @lre@" + itemDef.name;
                                    menuActionTypes[menuActionRow] = 234;
                                    selectedMenuActions[menuActionRow] = item.ID;
                                    firstMenuAction[menuActionRow] = i1;
                                    secondMenuAction[menuActionRow] = j1;
                                    menuActionRow++;
                                }
                        }
                        if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                            menuActionText[menuActionRow] = "Examine @lre@"
                                    + itemDef.name + " @gre@ (@whi@" + item.ID
                                    + "@gre@)";
                        } else {
                            menuActionText[menuActionRow] =
                                    "Examine @lre@" + itemDef.name;
                        }
                        menuActionTypes[menuActionRow] = 1448;
                        selectedMenuActions[menuActionRow] = item.ID;
                        firstMenuAction[menuActionRow] = i1;
                        secondMenuAction[menuActionRow] = j1;
                        menuActionRow++;
                    }
                }
            }
        }
    }

    public void cleanUpForQuit() {
        exitRequested = true;
        Signlink.reporterror = false;
        try {
            if (socketStream != null) {
                socketStream.close();
            }
        } catch (Exception _ex) {
        }
        try {
			spriteCache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        socketStream = null;
        stopMidi();
        if (mouseDetection != null)
            mouseDetection.running = false;
        mouseDetection = null;
        resourceProvider.disable();
        resourceProvider = null;
        outBuffer = null;
     //   chatBuffer = null;
        loginBuffer = null;
        incoming = null;
        mapCoordinates = null;
        terrainData = null;
        objectData = null;
        terrainIndices = null;
        objectIndices = null;
        tileHeights = null;
        tileFlags = null;
        scene = null;
        collisionMaps = null;
        anIntArrayArray901 = null;
        anIntArrayArray825 = null;
        bigX = null;
        bigY = null;
        aByteArray912 = null;
        tabImageProducer = null;
        leftFrame = null;
        topFrame = null;
        minimapImageProducer = null;
        gameScreenImageProducer = null;
        chatboxImageProducer = null;
        chatSettingImageProducer = null;
		/* Null pointers for custom sprites */
        mapBack = null;
        sideIcons = null;
        compass = null;
        hitMarks = null;
        headIcons = null;
        skullIcons = null;
        headIconsHint = null;
        crosses = null;
        mapDotItem = null;
        mapDotNPC = null;
        mapDotPlayer = null;
        mapDotFriend = null;
        mapDotTeam = null;
        mapScenes = null;
        mapFunctions = null;
        anIntArrayArray929 = null;
        players = null;
        playerList = null;
        mobsAwaitingUpdate = null;
        playerSynchronizationBuffers = null;
        removedMobs = null;
        npcs = null;
        npcIndices = null;
        groundItems = null;
        spawns = null;
        projectiles = null;
        incompleteAnimables = null;
        firstMenuAction = null;
        secondMenuAction = null;
        menuActionTypes = null;
        selectedMenuActions = null;
        menuActionText = null;
        settings = null;
        minimapHintX = null;
        minimapHintY = null;
        minimapHint = null;
        minimapImage = null;
        friendsList = null;
        friendsListAsLongs = null;
        friendsNodeIDs = null;
        flameLeftBackground = null;
        flameRightBackground = null;
        topLeft1BackgroundTile = null;
        bottomLeft1BackgroundTile = null;
        loginBoxImageProducer = null;
        loginScreenAccessories = null;
        bottomLeft0BackgroundTile = null;
        bottomRightImageProducer = null;
        loginMusicImageProducer = null;
        middleLeft1BackgroundTile = null;
        aRSImageProducer_1115 = null;
        multiOverlay = null;
        nullLoader();
        ObjectDefinition.clear();
        NpcDefinition.clear();
        ItemDefinition.clear();
        FloorDefinition.underlays = null;
        FloorDefinition.overlays = null;
        IdentityKit.kits = null;
        Widget.interfaceCache = null;
        Animation.animations = null;
        Graphic.cache = null;
        Graphic.models = null;
        VariablePlayer.variables = null;
        super.fullGameScreen = null;
        Player.models = null;
        Rasterizer3D.clear();
        SceneGraph.destructor();
        Model.clear();
        Frame.clear();
        System.gc();
    }

    public Component getGameComponent() {
        if (Signlink.mainapp != null)
            return Signlink.mainapp;
        if (super.gameFrame != null)
            return super.gameFrame;
        else
            return this;
    }
    
    public void showInterface(int interfaceid) {
		//ChatboxItemSearch.reset();
    	openInterfaceId = interfaceid;
    }

    private void manageTextInputs() {
        do {
            int key = readChar(-796);
            if (key == -1)
                break;
            if (key == 96 || key == 167) {
                if (myPrivilege >= 1 && myPrivilege <= 4) {
                    Console.consoleOpen = !Console.consoleOpen;
                }
                return;
            }
            if (Console.consoleOpen) {
                if (key == 8 && console.consoleInput.length() > 0)
                    console.consoleInput = console.consoleInput.substring(0, console.consoleInput.length() - 1);
                if (key == 9) { // Tab
                    if (console.previousMessage != null) {
                        console.consoleInput += console.previousMessage;
                    } else {
                        console.printMessage("No previous command entered.", 1);
                    }
                }
                if (key >= 32 && key <= 122 && console.consoleInput.length() < 80)
                    console.consoleInput += (char) key;

                if ((key == 13 || key == 10) && console.consoleInput.length() >= 1) {
                    console.printMessage(console.consoleInput, 0);
                    console.sendCommandPacket(console.consoleInput);
                    console.consoleInput = "";
                    updateChatbox = true;
                }
                return;
            }
            if (searchingSpawnTab && tabId == 2) {
                if (key == 8 && searchSyntax.length() > 0) {
                    searchSyntax = searchSyntax.substring(0, searchSyntax.length() - 1);
                }
                if (key >= 32 && key <= 122
                        && searchSyntax.length() < 15) {
                    searchSyntax += (char) key;
                }
                fetchSearchResults = true;
                return;
            }

            //Space bar skipping dialogue
            if (!continuedDialogue && inputDialogState == 0 && backDialogueId > 0 && loggedIn && openInterfaceId == -1 && fullscreenInterfaceID == -1) {
                //System.out.println(key);

                //Simple continue action with space bar
                if (key == 32 && backDialogueId == 4893) {
                    sendPacket(new NextDialogue(4899).create());
                    continuedDialogue = true;
                }

                //3 Options
                if (backDialogueId == 2469) {
                    if (key == 49) { //Option 1
                        sendPacket(new ClickButton(2471).create());
                        continuedDialogue = true;
                    } else if (key == 50) { //Option 2
                        sendPacket(new ClickButton(2472).create());
                        continuedDialogue = true;
                    } else if (key == 51) { //Option 3
                        sendPacket(new ClickButton(2473).create());
                        continuedDialogue = true;
                    }
                }

            }

            if (openInterfaceId != -1 && openInterfaceId == reportAbuseInterfaceID) {
                if (key == 8 && reportAbuseInput.length() > 0)
                    reportAbuseInput = reportAbuseInput.substring(0,
                            reportAbuseInput.length() - 1);
                if ((key >= 97 && key <= 122 || key >= 65 && key <= 90 || key >= 48 && key <= 57
                        || key == 32) && reportAbuseInput.length() < 12)
                    reportAbuseInput += (char) key;
            } else if (messagePromptRaised) {
                if (key >= 32 && key <= 122 && promptInput.length() < 80) {
                    promptInput += (char) key;
                    updateChatbox = true;
                }
                if (key == 8 && promptInput.length() > 0) {
                    promptInput = promptInput.substring(0, promptInput.length() - 1);
                    updateChatbox = true;
                }
                if (key == 13 || key == 10) {
                    messagePromptRaised = false;
                    updateChatbox = true;
                    if (friendsListAction == 1) {
                        long l = StringUtils.encodeBase37(promptInput);
                        addFriend(l);
                    }
                    if (friendsListAction == 2 && friendsCount > 0) {
                        long l1 = StringUtils.encodeBase37(promptInput);
                        removeFriend(l1);
                    }
                    if (friendsListAction == 3 && promptInput.length() > 0) {
                        // private message
						/*	outgoing.writeOpcode(126);
						outgoing.writeByte(0);
						int k = outgoing.currentPosition;
						outgoing.writeLong(aLong953);
						ChatMessageCodec.encode(promptInput, outgoing);
						outgoing.writeBytes(outgoing.currentPosition - k);
						promptInput = ChatMessageCodec.processText(promptInput);*/

                    	promptInput = ChatMessageCodec.processText(promptInput);
                    	OutgoingPacket privateMessage = new OutgoingPacket() {
							@Override
							public PacketBuilder create() {
								PacketBuilder buf = new PacketBuilder(126);
								buf.putLong(aLong953);
								/*chatBuffer.currentPosition = 0;
								ChatMessageCodec.encode(promptInput, chatBuffer);
								for (int i = 0; i < chatBuffer.currentPosition; i++) {
									buf.putByte(chatBuffer.payload[i]);
								}*/
								buf.putString(promptInput);
								return buf;
							}
						};
						sendPacket(privateMessage.create());
						
                        sendMessage(promptInput, 6, StringUtils.formatText(
                                StringUtils.decodeBase37(aLong953)));
                        if (privateChatMode == 2) {
                            privateChatMode = 1;
                            // privacy option
                            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
                        }
                    }
                    if (friendsListAction == 4 && ignoreCount < 100) {
                        long l2 = StringUtils.encodeBase37(promptInput);
                        addIgnore(l2);
                    }
                    if (friendsListAction == 5 && ignoreCount > 0) {
                        long l3 = StringUtils.encodeBase37(promptInput);
                        removeIgnore(l3);
                    }
                    if (friendsListAction == 6) {
                        StringUtils.encodeBase37(promptInput);
                    }
                }
            } else if (inputDialogState == 1) {
                if (key >= 48 && key <= 57 && amountOrNameInput.length() < 10) {
                    amountOrNameInput += (char) key;
                    updateChatbox = true;
                }
                if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m") && !amountOrNameInput.toLowerCase().contains("b"))
                        && (key == 107 || key == 109) || key == 98) {
                    amountOrNameInput += (char) key;
                    updateChatbox = true;
                }
                if (key == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0,
                            amountOrNameInput.length() - 1);
                    updateChatbox = true;
                }
                if (key == 13 || key == 10) {
                    if (amountOrNameInput.length() > 0) {
                        int length = amountOrNameInput.length();
                        char lastChar = amountOrNameInput.charAt(length - 1);

                        if (lastChar == 'k') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000";
                        } else if (lastChar == 'm') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000000";
                        } else if (lastChar == 'b') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000000000";
                        }

                        long amount = 0;

                        try {
                            amount = Long.parseLong(amountOrNameInput);

                            // overflow concious code
                            if (amount < 0) {
                                amount = 0;
                            } else if (amount > Integer.MAX_VALUE) {
                                amount = Integer.MAX_VALUE;
                            }
                        } catch (Exception ignored) {
                        }

                        if (amount > 0) {
                            sendPacket(new EnterAmount((int) amount).create());
                        }
                    }
                    inputDialogState = 0;
                    updateChatbox = true;
                }
            } else if (inputDialogState == 2) {
                if (key >= 32 && key <= 122 && amountOrNameInput.length() < 12) {
                    amountOrNameInput += (char) key;
                    updateChatbox = true;
                }
                if (key == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0,
                            amountOrNameInput.length() - 1);
                    updateChatbox = true;
                }
                if (key == 13 || key == 10) {
                    if (amountOrNameInput.length() > 0) {
                        sendPacket(new SendSyntax(amountOrNameInput).create());
                    }
                    inputDialogState = 0;
                    updateChatbox = true;
                }
            } else if (chatboxDrawing != null) {
				chatboxDrawing.pressKey(KeyEvent.getExtendedKeyCodeForChar(key));
            } else if (backDialogueId == -1) {
                if (key >= 32 && key <= 122 && inputString.length() < 80) {
                    inputString += (char) key;
                    updateChatbox = true;
                }
                if (key == 8 && inputString.length() > 0) {
                    inputString = inputString.substring(0, inputString.length() - 1);
                    updateChatbox = true;
                }
                if (key == 9) {
                    if (openInterfaceId != -1) {
                    	clearTopInterfaces();
                    }
                    tabToReplyPm();
                }

                //Remove the ability for players to do crowns..
                if (inputString.contains("@cr")) {
                    inputString = inputString.replaceAll("@cr", "");
                } else if (inputString.contains("<img=")) {
                    inputString = inputString.replaceAll("<img=", "");
                }

                if ((key == 13 || key == 10) && inputString.length() > 0) {

                	if (inputString.startsWith("config")) {
                		String[] args = inputString.split(" ");
                		sendConfiguration(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                		return;
                	}
                	
                	if (inputString.equals("clearChat")) {
                		for (int i = 0; i < chatMessages.length; i++)
                			chatMessages[i] = null;
                        inputString = "";
                        updateChatbox = true;
                		return;
                	}
                	
                    if (inputString.startsWith("/")) {
                        inputString = "::" + inputString;
                    }

                    if (inputString.startsWith("::")) {
                        sendPacket(new Command(inputString.substring(2)).create());
                    } else {
                        String text = inputString.toLowerCase();
                        int colorCode = 0;
                        if (text.startsWith("yellow:")) {
                            colorCode = 0;
                            inputString = inputString.substring(7);
                        } else if (text.startsWith("red:")) {
                            colorCode = 1;
                            inputString = inputString.substring(4);
                        } else if (text.startsWith("green:")) {
                            colorCode = 2;
                            inputString = inputString.substring(6);
                        } else if (text.startsWith("cyan:")) {
                            colorCode = 3;
                            inputString = inputString.substring(5);
                        } else if (text.startsWith("purple:")) {
                            colorCode = 4;
                            inputString = inputString.substring(7);
                        } else if (text.startsWith("white:")) {
                            colorCode = 5;
                            inputString = inputString.substring(6);
                        } else if (text.startsWith("flash1:")) {
                            colorCode = 6;
                            inputString = inputString.substring(7);
                        } else if (text.startsWith("flash2:")) {
                            colorCode = 7;
                            inputString = inputString.substring(7);
                        } else if (text.startsWith("flash3:")) {
                            colorCode = 8;
                            inputString = inputString.substring(7);
                        } else if (text.startsWith("glow1:")) {
                            colorCode = 9;
                            inputString = inputString.substring(6);
                        } else if (text.startsWith("glow2:")) {
                            colorCode = 10;
                            inputString = inputString.substring(6);
                        } else if (text.startsWith("glow3:")) {
                            colorCode = 11;
                            inputString = inputString.substring(6);
                        }
                        text = inputString.toLowerCase();
                        int effectCode = 0;
                        if (text.startsWith("wave:")) {
                            effectCode = 1;
                            inputString = inputString.substring(5);
                        } else if (text.startsWith("wave2:")) {
                            effectCode = 2;
                            inputString = inputString.substring(6);
                        } else if (text.startsWith("shake:")) {
                            effectCode = 3;
                            inputString = inputString.substring(6);
                        } else if (text.startsWith("scroll:")) {
                            effectCode = 4;
                            inputString = inputString.substring(7);
                        } else if (text.startsWith("slide:")) {
                            effectCode = 5;
                            inputString = inputString.substring(6);
                        }
                        final int color = colorCode;
                        final int effect = effectCode;
                        inputString = ChatMessageCodec.processText(inputString);
                        OutgoingPacket chat = new OutgoingPacket() {
							@Override
							public PacketBuilder create() {
								PacketBuilder buf = new PacketBuilder(4);
						        buf.putByte(color);
						        buf.putByte(effect);			        
						        buf.putString(inputString);
						        return buf;
							}
						};			
                        sendPacket(chat.create());
                        
                        localPlayer.spokenText = inputString;
                        localPlayer.textColour = color;
                        localPlayer.textEffect = effect;
                        localPlayer.textCycle = 150;

                        
                        
                        sendMessage(localPlayer.spokenText, 2, getPrefix(myPrivilege) + getPrefixMode(myMode) + localPlayer.name);

                        if (publicChatMode == 2) {
                            publicChatMode = 3;
                            // privacy option
                            sendPacket(new ChatSettings(publicChatMode, privateChatMode, tradeMode).create());
                        }
                    }
                    inputString = "";
                    updateChatbox = true;
                }
            }
        } while (true);
    }

    private void buildPublicChat(int j) {
        int l = 0;
        for (int message = 0; message < 500; message++) {

            if (chatMessages[message] == null) {
                continue;
            }

            if (chatTypeView != 1) {
                continue;
            }

            int privacyOptionType = chatTypes[message];

            String crownName = chatNames[message];

            int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;

            if (k1 < -23) {
                break;
            }
            if (crownName != null && crownName.startsWith("@cr1@")) {
                crownName = crownName.substring(5);
            } else if (crownName != null && crownName.startsWith("@cr2@")) {
                crownName = crownName.substring(5);
            } else if (crownName != null && crownName.startsWith("@cr4@")) {
                crownName = crownName.substring(5);
            } else if (crownName != null && crownName.startsWith("@cr5@")) {
                crownName = crownName.substring(5);
            } else if (crownName != null && crownName.startsWith("@cr6@")) {
                crownName = crownName.substring(5);
            } else if (crownName != null && crownName.startsWith("@cr7@")) {
                crownName = crownName.substring(5);
            } else if (crownName != null && crownName.startsWith("@cr8@")) {
                crownName = crownName.substring(5);
            } else if (crownName != null && crownName.startsWith("@cr9@")) {
                crownName = crownName.substring(5);
            } else if (crownName != null && crownName.startsWith("@cr10@")) {
                crownName = crownName.substring(6);
            }


            if ((privacyOptionType == 1 || privacyOptionType == 2) && (privacyOptionType == 1 || publicChatMode == 0
                    || publicChatMode == 1 && isFriendOrSelf(crownName))) {
            	if(!showChatboxArea) {
            		return;
            	}
            	if (j > k1 - 14 && j <= k1 && !crownName.equals(localPlayer.name)) {
                    if (!isFriendOrSelf(name)) {
                        menuActionText[menuActionRow] = "Add ignore @whi@" + crownName;
                        menuActionTypes[menuActionRow] = 42;
                        menuActionRow++;
                        menuActionText[menuActionRow] = "Add friend @whi@" + crownName;
                        menuActionTypes[menuActionRow] = 337;
                        menuActionRow++;
                    } else {
                        menuActionText[menuActionRow] = "Message @whi@" + name;
                        menuActionTypes[menuActionRow] = 2639;
                        menuActionRow++;
                    }
                }
                l++;
            }
        }
    }
    
    private void buildFriendChat(int j) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null)
                continue;
            if (chatTypeView != 2)
                continue;
            int j1 = chatTypes[i1];
            String s = chatNames[i1];
            int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
            if (k1 < -23)
                break;
            if (s != null && s.startsWith("@cr1@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr2@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr4@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr5@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr6@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr7@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr8@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr9@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr10@")) {
                s = s.substring(6);
            }
            if ((j1 == 5 || j1 == 6) && (splitPrivateChat == 0 || chatTypeView == 2)
                    && (j1 == 6 || privateChatMode == 0
                    || privateChatMode == 1 && isFriendOrSelf(s)))
                l++;
            if ((j1 == 3 || j1 == 7) && (splitPrivateChat == 0 || chatTypeView == 2)
                    && (j1 == 7 || privateChatMode == 0
                    || privateChatMode == 1 && isFriendOrSelf(s))) {
                if (j > k1 - 14 && j <= k1) {
                	if(!showChatboxArea) {
                		return;
                	}
                    if (!isFriendOrSelf(s)) {
                        menuActionText[menuActionRow] = "Add ignore @whi@" + s;
                        menuActionTypes[menuActionRow] = 42;
                        menuActionRow++;
                        menuActionText[menuActionRow] = "Add friend @whi@" + s;
                        menuActionTypes[menuActionRow] = 337;
                        menuActionRow++;
                    } else {
                        menuActionText[menuActionRow] = "Message @whi@" + s;
                        menuActionTypes[menuActionRow] = 2639;
                        menuActionRow++;
                    }
                }
                l++;
            }
        }
    }

    private void buildDuelorTrade(int j) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null)
                continue;
            if (chatTypeView != 3 && chatTypeView != 4)
                continue;
            int j1 = chatTypes[i1];
            String s = chatNames[i1];
            int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
            if (k1 < -23)
                break;
            if (s != null && s.startsWith("@cr1@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr2@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr4@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr5@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr6@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr7@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr8@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr9@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr10@")) {
                s = s.substring(6);
            }
            if (chatTypeView == 3 && j1 == 4
                    && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
            	if(!showChatboxArea) {
            		return;
            	}
                if (j > k1 - 14 && j <= k1) {
                    menuActionText[menuActionRow] = "Accept trade @whi@" + s;
                    menuActionTypes[menuActionRow] = 484;
                    menuActionRow++;
                }
                l++;
            }
            if (chatTypeView == 4 && j1 == 8
                    && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
            	if(!showChatboxArea) {
            		return;
            	}
            	if (j > k1 - 14 && j <= k1) {
                    menuActionText[menuActionRow] = "Accept challenge @whi@" + s;
                    menuActionTypes[menuActionRow] = 6;
                    menuActionRow++;
                }
                l++;
            }
            if (j1 == 12) {
            	if(!showChatboxArea) {
            		return;
            	}
            	if (j > k1 - 14 && j <= k1) {
                    menuActionText[menuActionRow] = "Go-to @blu@" + s;
                    menuActionTypes[menuActionRow] = 915;
                    menuActionRow++;
                }
                l++;
            }
        }
    }

    private void buildChatAreaMenu(int j) {
    	if (inputDialogState == 3) {
    		return;
    	}
    	
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null)
                continue;
            int j1 = chatTypes[i1];
            int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
            String s = chatNames[i1];
            if (chatTypeView == 1) {
                buildPublicChat(j);
                break;
            }
            if (chatTypeView == 2) {
                buildFriendChat(j);
                break;
            }
            if (chatTypeView == 3 || chatTypeView == 4) {
                buildDuelorTrade(j);
                break;
            }
            if (chatTypeView == 5) {
                break;
            }
            if (s != null && s.startsWith("@cr1@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr2@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr4@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr5@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr6@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr7@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr8@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr9@")) {
                s = s.substring(5);
            } else if (s != null && s.startsWith("@cr10@")) {
                s = s.substring(6);
            }
            if (s == null) {
                continue;
            }
            if (j1 == 0)
                l++;
            if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0
                    || publicChatMode == 1 && isFriendOrSelf(s))) {
            	if(!showChatboxArea) {
            		return;
            	}
            	if (j > k1 - 14 && j <= k1 && !s.equals(localPlayer.name)) {
                    
                	if (!isFriendOrSelf(s)) {
                        menuActionText[menuActionRow] = "Add ignore @whi@" + s;
                        menuActionTypes[menuActionRow] = 42;
                        menuActionRow++;
                        menuActionText[menuActionRow] = "Add friend @whi@" + s;
                        menuActionTypes[menuActionRow] = 337;
                        menuActionRow++;
                    } else {
                        menuActionText[menuActionRow] = "Message @whi@" + s;
                        menuActionTypes[menuActionRow] = 2639;
                        menuActionRow++;
                    }
                }
                l++;
            }
            if ((j1 == 3 || j1 == 7) && splitPrivateChat == 0
                    && (j1 == 7 || privateChatMode == 0
                    || privateChatMode == 1 && isFriendOrSelf(s))) {
                if (j > k1 - 14 && j <= k1) {
                    if (!isFriendOrSelf(s)) {
                        menuActionText[menuActionRow] = "Add ignore @whi@" + s;
                        menuActionTypes[menuActionRow] = 42;
                        menuActionRow++;
                        menuActionText[menuActionRow] = "Add friend @whi@" + s;
                        menuActionTypes[menuActionRow] = 337;
                        menuActionRow++;
                    } else {
                        menuActionText[menuActionRow] = "Message @whi@" + s;
                        menuActionTypes[menuActionRow] = 2639;
                        menuActionRow++;
                    }
                }
                l++;
            }
            if (j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
                if (j > k1 - 14 && j <= k1) {
                    menuActionText[menuActionRow] = "Accept trade @whi@" + s;
                    menuActionTypes[menuActionRow] = 484;
                    menuActionRow++;
                }
                l++;
            }
            if ((j1 == 5 || j1 == 6) && splitPrivateChat == 0 && privateChatMode < 2)
                l++;
            if (j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
                if (j > k1 - 14 && j <= k1) {
                    menuActionText[menuActionRow] = "Accept challenge @whi@" + s;
                    menuActionTypes[menuActionRow] = 6;
                    menuActionRow++;
                }
                l++;
            }
        }
    }

    public int getLevelForXP(int exp) {
        int points = 0;
        int output = 0;

        if (exp > 13034430) {
            return 99;
        }

        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);

            if (output >= exp) {
                return lvl;
            }
        }

        return 0;
    }

    /**
     * interface_handle_auto_content
     */
    private void drawFriendsListOrWelcomeScreen(Widget widget) {
        int index = widget.contentType;
        if (index >= 1 && index <= 100 || index >= 701 && index <= 800) {
            if (index == 1 && friendServerStatus == 0) {
                widget.defaultText = "Loading friend list";
                widget.optionType = 0;
                return;
            }
            if (index == 1 && friendServerStatus == 1) {
                widget.defaultText = "Connecting to friendserver";
                widget.optionType = 0;
                return;
            }
            if (index == 2 && friendServerStatus != 2) {
                widget.defaultText = "Please wait...";
                widget.optionType = 0;
                return;
            }
            int k = friendsCount;
            if (friendServerStatus != 2)
                k = 0;
            if (index > 700)
                index -= 601;
            else
                index--;
            if (index >= k) {
                widget.defaultText = "";
                widget.optionType = 0;
                return;
            } else {
                widget.defaultText = friendsList[index];
                widget.optionType = 1;
                return;
            }
        }
        if (index >= 101 && index <= 200 || index >= 801 && index <= 900) {
            int l = friendsCount;
            if (friendServerStatus != 2)
                l = 0;
            if (index > 800)
                index -= 701;
            else
                index -= 101;
            if (index >= l) {
                widget.defaultText = "";
                widget.optionType = 0;
                return;
            }
            if (friendsNodeIDs[index] == 0)
                widget.defaultText = "@red@Offline";
            else if (friendsNodeIDs[index] == nodeID)
                widget.defaultText = "@gre@Online"/* + (friendsNodeIDs[j] - 9) */;
            else
                widget.defaultText = "@red@Offline"/* + (friendsNodeIDs[j] - 9) */;
            widget.optionType = 1;
            return;
        }

        if (index == 203) {
            int i1 = friendsCount;
            if (friendServerStatus != 2)
                i1 = 0;
            widget.scrollMax = i1 * 15 + 20;
            if (widget.scrollMax <= widget.height)
                widget.scrollMax = widget.height + 1;
            return;
        }
        if (index >= 401 && index <= 500) {
            if ((index -= 401) == 0 && friendServerStatus == 0) {
                widget.defaultText = "Loading ignore list";
                widget.optionType = 0;
                return;
            }
            if (index == 1 && friendServerStatus == 0) {
                widget.defaultText = "Please wait...";
                widget.optionType = 0;
                return;
            }
            int j1 = ignoreCount;
            if (friendServerStatus == 0)
                j1 = 0;
            if (index >= j1) {
                widget.defaultText = "";
                widget.optionType = 0;
                return;
            } else {
                widget.defaultText = StringUtils.formatText(
                        StringUtils.decodeBase37(ignoreListAsLongs[index]));
                widget.optionType = 1;
                return;
            }
        }
        if (index == 503) {
            widget.scrollMax = ignoreCount * 15 + 20;
            if (widget.scrollMax <= widget.height)
                widget.scrollMax = widget.height + 1;
            return;
        }
        if (index == 327) {
            widget.modelRotation1 = 150;
            widget.modelRotation2 = (int) (Math.sin((double) tick / 40D) * 256D) & 0x7ff;
            if (aBoolean1031) {
                for (int k1 = 0; k1 < 7; k1++) {
                    int l1 = myAppearance[k1];
                    if (l1 >= 0 && !IdentityKit.kits[l1].bodyLoaded())
                        return;
                }

                aBoolean1031 = false;
                Model aclass30_sub2_sub4_sub6s[] = new Model[7];
                int i2 = 0;
                for (int j2 = 0; j2 < 7; j2++) {
                    int k2 = myAppearance[j2];
                    if (k2 >= 0)
                        aclass30_sub2_sub4_sub6s[i2++] =
                                IdentityKit.kits[k2].bodyModel();
                }

                Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
                for (int l2 = 0; l2 < 5; l2++)
                    if (characterDesignColours[l2] != 0) {
                        model.recolor(PLAYER_BODY_RECOLOURS[l2][0],
                                PLAYER_BODY_RECOLOURS[l2][characterDesignColours[l2]]);
                        if (l2 == 1)
                            model.recolor(anIntArray1204[0],
                                    anIntArray1204[characterDesignColours[l2]]);
                    }

                model.skin();
                model.applyTransform(Animation.animations[localPlayer.idleAnimation].primaryFrames[0]);
                model.light(64, 850, -30, -50, -30, true);
                widget.defaultMediaType = 5;
                widget.defaultMedia = 0;
                Widget.method208(aBoolean994, model);
            }
            return;
        }
        if (index == 328) {
            Widget rsInterface = widget;
            int verticleTilt = 150;
            int animationSpeed = (int) (Math.sin((double) tick / 40D) * 256D) & 0x7ff;
            rsInterface.modelRotation1 = verticleTilt;
            rsInterface.modelRotation2 = animationSpeed;
            if (aBoolean1031) {
                Model characterDisplay = localPlayer.getAnimatedModel();
                for (int l2 = 0; l2 < 5; l2++)
                    if (characterDesignColours[l2] != 0) {
                        characterDisplay.recolor(PLAYER_BODY_RECOLOURS[l2][0],
                                PLAYER_BODY_RECOLOURS[l2][characterDesignColours[l2]]);
                        if (l2 == 1)
                            characterDisplay.recolor(anIntArray1204[0],
                                    anIntArray1204[characterDesignColours[l2]]);
                    }
                int staticFrame = localPlayer.idleAnimation;
                characterDisplay.skin();
                characterDisplay.applyTransform(Animation.animations[staticFrame].primaryFrames[0]);
                // characterDisplay.light(64, 850, -30, -50, -30, true);
                rsInterface.defaultMediaType = 5;
                rsInterface.defaultMedia = 0;
                Widget.method208(aBoolean994, characterDisplay);
            }
            return;
        }
        if (index == 329) { // Item model on Interface`
            if (widget.defaultMedia == -1) return;
            Model model = Model.getModel(widget.defaultMedia);
            Widget.method208(aBoolean994, model); // Updates model on interface
            int verticleTilt = 150;
            int animationSpeed = (int) (double) (tick / 75D * 1024D) & 2047; // Edit 75D for speed
            widget.modelRotation1 = verticleTilt;
            widget.modelRotation2 = animationSpeed;
            return;
        }
        if (index == 324) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = widget.disabledSprite;
                aClass30_Sub2_Sub1_Sub1_932 = widget.enabledSprite;
            }
            if (maleCharacter) {
                widget.disabledSprite = aClass30_Sub2_Sub1_Sub1_932;
                return;
            } else {
                widget.disabledSprite = aClass30_Sub2_Sub1_Sub1_931;
                return;
            }
        }
        if (index == 325) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = widget.disabledSprite;
                aClass30_Sub2_Sub1_Sub1_932 = widget.enabledSprite;
            }
            if (maleCharacter) {
                widget.disabledSprite = aClass30_Sub2_Sub1_Sub1_931;
                return;
            } else {
                widget.disabledSprite = aClass30_Sub2_Sub1_Sub1_932;
                return;
            }
        }
        if (index == 600) {
            widget.defaultText = reportAbuseInput;
            if (tick % 20 < 10) {
                widget.defaultText += "|";
                return;
            } else {
                widget.defaultText += " ";
                return;
            }
        }
        if (index == 613)
            if (myPrivilege >= 1) {
                if (canMute) {
                    widget.textColor = 0xff0000;
                    widget.defaultText =
                            "Moderator option: Mute player for 48 hours: <ON>";
                } else {
                    widget.textColor = 0xffffff;
                    widget.defaultText =
                            "Moderator option: Mute player for 48 hours: <OFF>";
                }
            } else {
                widget.defaultText = "";
            }
        if (index == 650 || index == 655)
            if (anInt1193 != 0) {
                String s;
                if (daysSinceLastLogin == 0)
                    s = "earlier today";
                else if (daysSinceLastLogin == 1)
                    s = "yesterday";
                else
                    s = daysSinceLastLogin + " days ago";
                widget.defaultText = "You last logged in " + s + " from: " + Signlink.dns;
            } else {
                widget.defaultText = "";
            }
        if (index == 651) {
            if (unreadMessages == 0) {
                widget.defaultText = "0 unread messages";
                widget.textColor = 0xffff00;
            }
            if (unreadMessages == 1) {
                widget.defaultText = "1 unread defaultText";
                widget.textColor = 65280;
            }
            if (unreadMessages > 1) {
                widget.defaultText = unreadMessages + " unread messages";
                widget.textColor = 65280;
            }
        }
        if (index == 652)
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1)
                    widget.defaultText =
                            "@yel@This is a non-members world: @whi@Since you are a member we";
                else
                    widget.defaultText = "";
            } else if (daysSinceRecovChange == 200) {
                widget.defaultText =
                        "You have not yet set any password recovery questions.";
            } else {
                String s1;
                if (daysSinceRecovChange == 0)
                    s1 = "Earlier today";
                else if (daysSinceRecovChange == 1)
                    s1 = "Yesterday";
                else
                    s1 = daysSinceRecovChange + " days ago";
                widget.defaultText = s1 + " you changed your recovery questions";
            }
        if (index == 653)
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1)
                    widget.defaultText =
                            "@whi@recommend you use a members world instead. You may use";
                else
                    widget.defaultText = "";
            } else if (daysSinceRecovChange == 200)
                widget.defaultText =
                        "We strongly recommend you do so now to secure your account.";
            else
                widget.defaultText =
                        "If you do not remember making this change then cancel it immediately";
        if (index == 654) {
            if (daysSinceRecovChange == 201)
                if (membersInt == 1) {
                    widget.defaultText =
                            "@whi@this world but member benefits are unavailable whilst here.";
                    return;
                } else {
                    widget.defaultText = "";
                    return;
                }
            if (daysSinceRecovChange == 200) {
                widget.defaultText =
                        "Do this from the 'account management' area on our front webpage";
                return;
            }
            widget.defaultText =
                    "Do this from the 'account management' area on our front webpage";
        }
    }

    private void drawSplitPrivateChat() {
        if (splitPrivateChat == 0) {
            return;
        }
        GameFont textDrawingArea = regularText;
        int i = 0;
        if (systemUpdateTime != 0) {
            i = 1;
        }
        for (int j = 0; j < 100; j++) {
            if (chatMessages[j] != null) {
                int k = chatTypes[j];
                String s = chatNames[j];
                byte data = 0;
                if (s != null && s.startsWith("@cr1@")) {
                    s = s.substring(5);
                    data = 1;
                } else if (s != null && s.startsWith("@cr2@")) {
                    s = s.substring(5);
                    data = 2;
                } else if (s != null && s.startsWith("@cr4@")) {
                    s = s.substring(5);
                    data = 4;
                } else if (s != null && s.startsWith("@cr5@")) {
                    s = s.substring(5);
                    data = 5;
                } else if (s != null && s.startsWith("@cr6@")) {
                    s = s.substring(5);
                    data = 6;
                } else if (s != null && s.startsWith("@cr7@")) {
                    s = s.substring(5);
                    data = 7;
                } else if (s != null && s.startsWith("@cr8@")) {
                    s = s.substring(5);
                    data = 8;
                } else if (s != null && s.startsWith("@cr9@")) {
                    s = s.substring(5);
                    data = 9;
                } else if (s != null && s.startsWith("@cr10@")) {
                    s = s.substring(6);
                    data = 10;
                }
                if ((k == 3 || k == 7) && (k == 7 || privateChatMode == 0
                        || privateChatMode == 1 && isFriendOrSelf(s))) {
                    int l = 329 - i * 13;
                    if (frameMode != ScreenMode.FIXED) {
                        l = frameHeight - 170 - i * 13;
                    }
                    int k1 = 4;
                    textDrawingArea.render(0, "From", l, k1);
                    textDrawingArea.render(65535, "From", l - 1, k1);
                    k1 += textDrawingArea.getTextWidth("From ");
                    if (data > 0 && data < 10) {
                        modIcons[data - 1].drawSprite(k1, l - 12);
                        k1 += 12;
                    }
                    textDrawingArea.render(0, s + ": " + chatMessages[j], l, k1);
                    textDrawingArea.render(65535, s + ": " + chatMessages[j], l - 1, k1);
                    if (++i >= 5) {
                        return;
                    }
                }
                if (k == 5 && privateChatMode < 2) {
                    int i1 = 329 - i * 13;
                    if (frameMode != ScreenMode.FIXED) {
                        i1 = frameHeight - 170 - i * 13;
                    }
                    textDrawingArea.render(0, chatMessages[j], i1, 4);
                    textDrawingArea.render(65535, chatMessages[j], i1 - 1, 4);
                    if (++i >= 5) {
                        return;
                    }
                }
                if (k == 6 && privateChatMode < 2) {
                    int j1 = 329 - i * 13;
                    if (frameMode != ScreenMode.FIXED) {
                        j1 = frameHeight - 170 - i * 13;
                    }
                    textDrawingArea.render(0, "To " + s + ": " + chatMessages[j], j1, 4);
                    textDrawingArea.render(65535, "To " + s + ": " + chatMessages[j],
                            j1 - 1, 4);
                    if (++i >= 5) {
                        return;
                    }
                }
            }
        }
    }

    public void sendMessage(String message, int type, String name) {

        if (type == 0 && dialogueId != -1) {
            clickToContinueString = message;
            super.clickMode3 = 0;
        }

        if (backDialogueId == -1) {
            updateChatbox = true;
        }

        for (int index = 499; index > 0; index--) {
            chatTypes[index] = chatTypes[index - 1];
            chatNames[index] = chatNames[index - 1];
            chatMessages[index] = chatMessages[index - 1];
            chatRights[index] = chatRights[index - 1];
        }

        chatTypes[0] = type;
        chatNames[0] = name;
        chatMessages[0] = message;
        chatRights[0] = rights;
    }

    private final void minimapHovers() {
        final boolean fixed = frameMode == ScreenMode.FIXED;
        final boolean specOrb = UserInterface.TOGGLE_SPEC_ORBS.toggled();

        hpHover = fixed ? mouseInRegion(520, 569, 47, 72) : mouseInRegion(frameWidth - 213, frameWidth - 164, 45, 71);

        int yOffset = specOrb ? 0 : 11;
        prayHover = fixed ? mouseInRegion(520, 569, 81 + yOffset, 105 + yOffset) : mouseInRegion(frameWidth - 213, frameWidth - 164, 78 + yOffset, 105 + yOffset);

        int xOffset = specOrb ? 0 : 13;
        yOffset = specOrb ? 0 : 15;
        runHover = fixed ? mouseInRegion(530 + xOffset, 580 + xOffset, 110 + yOffset, 138 + yOffset) :
                mouseInRegion(frameWidth - 203 + xOffset, frameWidth - 154 + xOffset, 112 + yOffset, 136 + yOffset);

        worldHover = fixed ? mouseInRegion(716, 737, 130, 152) : mouseInRegion(frameWidth - 30, frameWidth - 9, 143, 164);

        specialHover = fixed ? mouseInRegion(563, 612, 131, 163) : mouseInRegion(frameWidth - 170, frameWidth - 121, 135, 161);

        expCounterHover = fixed ? mouseInRegion(519, 536, 20, 46) : mouseInRegion(frameWidth - 216, frameWidth - 190, 22, 47);
    }

    private void processTabClick() {
        if (super.clickMode3 == 1) {
            if (frameMode == ScreenMode.FIXED
                    || frameMode != ScreenMode.FIXED && !stackSideStones) {
                int xOffset = frameMode == ScreenMode.FIXED ? 0 : frameWidth - 765;
                int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 503;
                for (int i = 0; i < tabClickX.length; i++) {
                    if (super.mouseX >= tabClickStart[i] + xOffset
                            && super.mouseX <= tabClickStart[i] + tabClickX[i]
                            + xOffset
                            && super.mouseY >= tabClickY[i] + yOffset
                            && super.mouseY < tabClickY[i] + 37 + yOffset
                            && tabInterfaceIDs[i] != -1) {
                        tabId = i;
                        tabAreaAltered = true;

                        //Spawn tab
                        if (tabId == 2) {
                            searchingSpawnTab = true;
                        } else {
                            searchingSpawnTab = false;
                        }

                        break;
                    }
                }
            } else if (stackSideStones && frameWidth < 1000) {
                if (super.saveClickX >= frameWidth - 226
                        && super.saveClickX <= frameWidth - 195
                        && super.saveClickY >= frameHeight - 72
                        && super.saveClickY < frameHeight - 40
                        && tabInterfaceIDs[0] != -1) {
                    if (tabId == 0) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 0;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 194
                        && super.saveClickX <= frameWidth - 163
                        && super.saveClickY >= frameHeight - 72
                        && super.saveClickY < frameHeight - 40
                        && tabInterfaceIDs[1] != -1) {
                    if (tabId == 1) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 1;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 162
                        && super.saveClickX <= frameWidth - 131
                        && super.saveClickY >= frameHeight - 72
                        && super.saveClickY < frameHeight - 40
                        && tabInterfaceIDs[2] != -1) {
                    if (tabId == 2) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 2;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 129
                        && super.saveClickX <= frameWidth - 98
                        && super.saveClickY >= frameHeight - 72
                        && super.saveClickY < frameHeight - 40
                        && tabInterfaceIDs[3] != -1) {
                    if (tabId == 3) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 3;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 97
                        && super.saveClickX <= frameWidth - 66
                        && super.saveClickY >= frameHeight - 72
                        && super.saveClickY < frameHeight - 40
                        && tabInterfaceIDs[4] != -1) {
                    if (tabId == 4) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 4;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 65
                        && super.saveClickX <= frameWidth - 34
                        && super.saveClickY >= frameHeight - 72
                        && super.saveClickY < frameHeight - 40
                        && tabInterfaceIDs[5] != -1) {
                    if (tabId == 5) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 5;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 33 && super.saveClickX <= frameWidth
                        && super.saveClickY >= frameHeight - 72
                        && super.saveClickY < frameHeight - 40
                        && tabInterfaceIDs[6] != -1) {
                    if (tabId == 6) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 6;
                    tabAreaAltered = true;

                }

                if (super.saveClickX >= frameWidth - 194
                        && super.saveClickX <= frameWidth - 163
                        && super.saveClickY >= frameHeight - 37
                        && super.saveClickY < frameHeight - 0
                        && tabInterfaceIDs[8] != -1) {
                    if (tabId == 8) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 8;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 162
                        && super.saveClickX <= frameWidth - 131
                        && super.saveClickY >= frameHeight - 37
                        && super.saveClickY < frameHeight - 0
                        && tabInterfaceIDs[9] != -1) {
                    if (tabId == 9) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 9;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 129
                        && super.saveClickX <= frameWidth - 98
                        && super.saveClickY >= frameHeight - 37
                        && super.saveClickY < frameHeight - 0
                        && tabInterfaceIDs[10] != -1) {
                    if (tabId == 7) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 7;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 97
                        && super.saveClickX <= frameWidth - 66
                        && super.saveClickY >= frameHeight - 37
                        && super.saveClickY < frameHeight - 0
                        && tabInterfaceIDs[11] != -1) {
                    if (tabId == 11) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 11;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 65
                        && super.saveClickX <= frameWidth - 34
                        && super.saveClickY >= frameHeight - 37
                        && super.saveClickY < frameHeight - 0
                        && tabInterfaceIDs[12] != -1) {
                    if (tabId == 12) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 12;
                    tabAreaAltered = true;

                }
                if (super.saveClickX >= frameWidth - 33 && super.saveClickX <= frameWidth
                        && super.saveClickY >= frameHeight - 37
                        && super.saveClickY < frameHeight - 0
                        && tabInterfaceIDs[13] != -1) {
                    if (tabId == 13) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    tabId = 13;
                    tabAreaAltered = true;

                }
            } else if (stackSideStones && frameWidth >= 1000) {
                if (super.mouseY >= frameHeight - 37 && super.mouseY <= frameHeight) {
                    if (super.mouseX >= frameWidth - 417
                            && super.mouseX <= frameWidth - 386) {
                        if (tabId == 0) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 0;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 385
                            && super.mouseX <= frameWidth - 354) {
                        if (tabId == 1) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 1;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 353
                            && super.mouseX <= frameWidth - 322) {
                        if (tabId == 2) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 2;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 321
                            && super.mouseX <= frameWidth - 290) {
                        if (tabId == 3) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 3;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 289
                            && super.mouseX <= frameWidth - 258) {
                        if (tabId == 4) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 4;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 257
                            && super.mouseX <= frameWidth - 226) {
                        if (tabId == 5) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 5;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 225
                            && super.mouseX <= frameWidth - 194) {
                        if (tabId == 6) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 6;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 193
                            && super.mouseX <= frameWidth - 163) {
                        if (tabId == 8) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 8;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 162
                            && super.mouseX <= frameWidth - 131) {
                        if (tabId == 9) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 9;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 130
                            && super.mouseX <= frameWidth - 99) {
                        if (tabId == 7) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 7;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 98
                            && super.mouseX <= frameWidth - 67) {
                        if (tabId == 11) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 11;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 66
                            && super.mouseX <= frameWidth - 45) {
                        if (tabId == 12) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 12;
                        tabAreaAltered = true;
                    }
                    if (super.mouseX >= frameWidth - 31 && super.mouseX <= frameWidth) {
                        if (tabId == 13) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        tabId = 13;
                        tabAreaAltered = true;
                    }
                }
            }
        }
    }

    private void setupGameplayScreen() {
        if (chatboxImageProducer != null) {
            return;
        }

        nullLoader();
        super.fullGameScreen = null;
        topLeft1BackgroundTile = null;
        bottomLeft1BackgroundTile = null;
        loginBoxImageProducer = null;
        loginScreenAccessories = null;
        flameLeftBackground = null;
        flameRightBackground = null;
        bottomLeft0BackgroundTile = null;
        bottomRightImageProducer = null;
        loginMusicImageProducer = null;
        middleLeft1BackgroundTile = null;
        aRSImageProducer_1115 = null;
        chatboxImageProducer = new ProducingGraphicsBuffer(519, 165);// chatback
        minimapImageProducer = new ProducingGraphicsBuffer(249, 168);// mapback
        Rasterizer2D.clear();
        spriteCache.lookup(19).drawSprite(0, 0);
        tabImageProducer = new ProducingGraphicsBuffer(249, 335);// inventory
        gameScreenImageProducer = new ProducingGraphicsBuffer(512, 334);// gamescreen
        Rasterizer2D.clear();
        chatSettingImageProducer = new ProducingGraphicsBuffer(249, 45);
        welcomeScreenRaised = true;
    }

    private void refreshMinimap(Sprite sprite, int j, int k) {
        int l = k * k + j * j;
        if (l > 4225 && l < 0x15f90) {
            int i1 = cameraHorizontal + minimapRotation & 0x7ff;
            int j1 = Model.SINE[i1];
            int k1 = Model.COSINE[i1];
            j1 = (j1 * 256) / (minimapZoom + 256);
            k1 = (k1 * 256) / (minimapZoom + 256);
        } else {
            markMinimap(sprite, k, j);
        }
    }

    public void rightClickChatButtons() {
        if (mouseY >= frameHeight - 22 && mouseY <= frameHeight) {
            if (super.mouseX >= 5 && super.mouseX <= 61) {
                menuActionText[1] = "Switch tab";
                menuActionTypes[1] = 999;
                menuActionRow = 2;
            } else if (super.mouseX >= 69 && super.mouseX <= 125) {
                menuActionText[1] = "Switch tab";
                menuActionTypes[1] = 998;
                menuActionRow = 2;
            } else if (super.mouseX >= 133 && super.mouseX <= 189) {
                menuActionText[1] = "@yel@Public: @whi@Hide";
                menuActionTypes[1] = 997;
                menuActionText[2] = "@yel@Public: @whi@Off";
                menuActionTypes[2] = 996;
                menuActionText[3] = "@yel@Public: @whi@Show friends";
                menuActionTypes[3] = 995;
                menuActionText[4] = "@yel@Public: @whi@Show all"; // TODO: Add show autochat
                menuActionTypes[4] = 994;
                menuActionText[5] = "Switch tab";
                menuActionTypes[5] = 993;
                menuActionRow = 6;
            } else if (super.mouseX >= 196 && super.mouseX <= 253) {
                menuActionText[1] = "@yel@Private: @whi@Off";
                menuActionTypes[1] = 992;
                menuActionText[2] = "@yel@Private: @whi@Show friends";
                menuActionTypes[2] = 991;
                menuActionText[3] = "@yel@Private: @whi@Show all";
                menuActionTypes[3] = 990;
                menuActionText[4] = "Switch tab";
                menuActionTypes[4] = 989;
                menuActionRow = 5;
            } else if (super.mouseX >= 261 && super.mouseX <= 317) {
                menuActionText[1] = "@yel@Clan: @whi@Off";
                menuActionTypes[1] = 1003;
                menuActionText[2] = "@yel@Clan: @whi@Show friends";
                menuActionTypes[2] = 1002;
                menuActionText[3] = "@yel@Clan: @whi@Show all";
                menuActionTypes[3] = 1001;
                menuActionText[4] = "Switch tab";
                menuActionTypes[4] = 1000;
                menuActionRow = 5;
            } else if (super.mouseX >= 325 && super.mouseX <= 381) {
                menuActionText[1] = "@yel@Trade: @whi@Off";
                menuActionTypes[1] = 987;
                menuActionText[2] = "@yel@Trade: @whi@Show friends";
                menuActionTypes[2] = 986;
                menuActionText[3] = "@yel@Trade: @whi@Show all";
                menuActionTypes[3] = 985;
                menuActionText[4] = "Switch tab";
                menuActionTypes[4] = 984;
                menuActionRow = 5;
            } else if (super.mouseX >= 389 && super.mouseX <= 445) {
                menuActionText[1] = "@yel@Yell: @whi@Off";
                menuActionTypes[1] = 976;
                menuActionText[2] = "@yel@Yell: @whi@On";
                menuActionTypes[2] = 975;
                menuActionText[3] = "Switch tab";
                menuActionTypes[3] = 974;
                menuActionRow = 4;
            } else if (super.mouseX >= 453 && super.mouseX <= 509) {
                menuActionText[1] = "View Tools";
                menuActionTypes[1] = 606;
                menuActionRow = 2;
            }
        }
    }

    public void processRightClick() {
        if (activeInterfaceType != 0) {
            return;
        }
        menuActionText[0] = "Cancel";
        menuActionTypes[0] = 1107;
        menuActionRow = 1;
        if (showChatComponents) {
            buildSplitPrivateChatMenu();
        }
        
        interfaceHoverType = 0;
        interfaceId = 0;
        if (super.mouseX > 0 && super.mouseY > 0 && super.mouseX < 765 && super.mouseY < 503) {
            if (fullscreenInterfaceID != -1) {
                buildInterfaceMenu(0, Widget.interfaceCache[fullscreenInterfaceID], super.mouseX, 0, super.mouseY, 0);
                return;
            }
        }
        if (frameMode == ScreenMode.FIXED) {
            if (super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516 && super.mouseY < 338) {
                if (openInterfaceId != -1) {
                    buildInterfaceMenu(4, Widget.interfaceCache[openInterfaceId], super.mouseX, 4, super.mouseY, 0);
                } else {
                    createMenu();
                }
            }
        } else if (frameMode != ScreenMode.FIXED) {
            if (getMousePositions()) {
            	int w = 512, h = 334;
				int x = (frameWidth / 2) - 256, y = (frameHeight / 2) - 167;
				int x2 = (frameWidth / 2) + 256, y2 = (frameHeight / 2) + 167;
				int count = stackSideStones ? 3 : 4;
				if (frameMode != ScreenMode.FIXED) {
					for (int i = 0; i < count; i++) {
						if (x + w > (frameWidth - 225)) {
							x = x - 30;
							x2 = x2 - 30;
							if (x < 0) {
								x = 0;
							}
						}
						if (y + h > (frameHeight - 182)) {
							y = y - 30;
							y2 = y2 - 30;
							if (y < 0) {
								y = 0;
							}
						}
					}
				}
				if (openInterfaceId != -1 && super.mouseX > x && super.mouseY > y && super.mouseX < x2 && super.mouseY < y2) {
					buildInterfaceMenu(x, Widget.interfaceCache[openInterfaceId], super.mouseX, y, super.mouseY, 0);
				} else {
					createMenu();
				}
            }
        }
        if (interfaceHoverType != anInt1026) {
            anInt1026 = interfaceHoverType;
        }
        if (interfaceId != anInt1129) {
            anInt1129 = interfaceId;
        }
        interfaceHoverType = 0;
        interfaceId = 0;
        if (!stackSideStones) {
            final int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 503;
            final int xOffset = frameMode == ScreenMode.FIXED ? 0 : frameWidth - 765;
            if (super.mouseX > 548 + xOffset && super.mouseX < 740 + xOffset
                    && super.mouseY > 207 + yOffset && super.mouseY < 468 + yOffset) {
                if (overlayInterfaceId != -1) {
                    buildInterfaceMenu(548 + xOffset,
                            Widget.interfaceCache[overlayInterfaceId], super.mouseX,
                            207 + yOffset, super.mouseY, 0);
                } else if (tabInterfaceIDs[tabId] != -1) {
                    buildInterfaceMenu(548 + xOffset,
                            Widget.interfaceCache[tabInterfaceIDs[tabId]],
                            super.mouseX, 207 + yOffset, super.mouseY, 0);
                }
            }
        } else if (stackSideStones) {
            final int yOffset = frameWidth >= 1000 ? 37 : 74;
            if (super.mouseX > frameWidth - 197 && super.mouseY > frameHeight - yOffset - 267
                    && super.mouseX < frameWidth - 7
                    && super.mouseY < frameHeight - yOffset - 7 && showTabComponents) {
                if (overlayInterfaceId != -1) {
                    buildInterfaceMenu(frameWidth - 197,
                            Widget.interfaceCache[overlayInterfaceId], super.mouseX,
                            frameHeight - yOffset - 267, super.mouseY, 0);
                } else if (tabInterfaceIDs[tabId] != -1) {
                    buildInterfaceMenu(frameWidth - 197,
                            Widget.interfaceCache[tabInterfaceIDs[tabId]],
                            super.mouseX, frameHeight - yOffset - 267, super.mouseY,
                            0);
                }
            }
        }
        if (interfaceHoverType != anInt1048) {
            tabAreaAltered = true;
            anInt1048 = interfaceHoverType;
        }
        if (interfaceId != anInt1044) {
            tabAreaAltered = true;
            anInt1044 = interfaceId;
        }
        interfaceHoverType = 0;
        interfaceId = 0;
        if (super.mouseX > 0
                && super.mouseY > (frameMode == ScreenMode.FIXED ? 338 : frameHeight - 165)
                && super.mouseX < 490
                && super.mouseY < (frameMode == ScreenMode.FIXED ? 463 : frameHeight - 40)
                && showChatComponents) {
        	if(!showChatboxArea) {
        		return;
        	}
            if (backDialogueId != -1) {


                buildInterfaceMenu(20, Widget.interfaceCache[backDialogueId], super.mouseX, (frameMode == ScreenMode.FIXED ? 358 : frameHeight - 145), super.mouseY, 0);


            } else if (super.mouseY < (frameMode == ScreenMode.FIXED ? 463 : frameHeight - 40)
                    && super.mouseX < 490) {
            	if(!showChatboxArea) {
            		return;
            	}
                buildChatAreaMenu(super.mouseY
                        - (frameMode == ScreenMode.FIXED ? 338 : frameHeight - 165));
            }
        }
        if (backDialogueId != -1 && interfaceHoverType != anInt1039) {
            updateChatbox = true;
            anInt1039 = interfaceHoverType;
        }
        if (backDialogueId != -1 && interfaceId != anInt1500) {
            updateChatbox = true;
            anInt1500 = interfaceId;
        }
        if (super.mouseX > 4 && super.mouseY > 480 && super.mouseX < 516
                && super.mouseY < frameHeight) {
            rightClickChatButtons();
        }
        processMinimapActions();
        boolean flag = false;
        while (!flag) {
            flag = true;
            for (int j = 0; j < menuActionRow - 1; j++) {
                if (menuActionTypes[j] < 1000 && menuActionTypes[j + 1] > 1000) {
                    String s = menuActionText[j];
                    menuActionText[j] = menuActionText[j + 1];
                    menuActionText[j + 1] = s;
                    int k = menuActionTypes[j];
                    menuActionTypes[j] = menuActionTypes[j + 1];
                    menuActionTypes[j + 1] = k;
                    k = firstMenuAction[j];
                    firstMenuAction[j] = firstMenuAction[j + 1];
                    firstMenuAction[j + 1] = k;
                    k = secondMenuAction[j];
                    secondMenuAction[j] = secondMenuAction[j + 1];
                    secondMenuAction[j + 1] = k;
                    k = selectedMenuActions[j];
                    selectedMenuActions[j] = selectedMenuActions[j + 1];
                    selectedMenuActions[j + 1] = k;
                    flag = false;
                }
            }
        }
    }
    
    public String loginMessage = "";

    /**
     * The login method for the 317 protocol.
     *
     * @param name         The name of the user trying to login.
     * @param password     The password of the user trying to login.
     * @param reconnecting The flag for the user indicating to attempt to reconnect.
     */
    private void login(String name, String password, boolean reconnecting) {
        Signlink.setError(name);
        try {
            if (name.length() < 3) {
                return;
            }
            if (password.length() < 3) {
                return;
            }
            if (!reconnecting) {
                drawLoginScreen(true);
            }

            socketStream = new BufferedConnection(this,
                    openSocket(Configuration.SERVER_PORT + portOffset));

            outBuffer.currentPosition = 0;
            outBuffer.writeByte(14); //REQUEST
            socketStream.queueBytes(1, outBuffer.payload);


            int response = socketStream.read();

            int copy = response;

            if (response == 0) {
                socketStream.flushInputStream(incoming.payload, 8);
                incoming.currentPosition = 0;
                serverSeed = incoming.readLong(); // aka server session key
                int seed[] = new int[4];
                seed[0] = (int) (Math.random() * 99999999D);
                seed[1] = (int) (Math.random() * 99999999D);
                seed[2] = (int) (serverSeed >> 32);
                seed[3] = (int) serverSeed;
                outBuffer.currentPosition = 0;
                outBuffer.writeByte(10);
                outBuffer.writeInt(seed[0]);
                outBuffer.writeInt(seed[1]);
                outBuffer.writeInt(seed[2]);
                outBuffer.writeInt(seed[3]);
                outBuffer.writeInt(Configuration.UID);
                outBuffer.writeString(name);
                outBuffer.writeString(password);
                outBuffer.encryptRSAContent();

                loginBuffer.currentPosition = 0;
                loginBuffer.writeByte(reconnecting ? 18 : 16);
                loginBuffer.writeByte(outBuffer.currentPosition + 2); // size of the
                // login block
                loginBuffer.writeByte(255);
                loginBuffer.writeByte(lowMemory ? 1 : 0); // low mem or not
                loginBuffer.writeBytes(outBuffer.payload, outBuffer.currentPosition, 0);
                cipher = new IsaacCipher(seed);
                for (int index = 0; index < 4; index++)
                    seed[index] += 50;

                encryption = new IsaacCipher(seed);
                socketStream.queueBytes(loginBuffer.currentPosition, loginBuffer.payload);
                response = socketStream.read();
            }

            if (response == 1) {
                try {
                    Thread.sleep(2000L);
                } catch (Exception _ex) {
                }
                login(name, password, reconnecting);
                return;
            }
            if (response == 2) {
                myPrivilege = socketStream.read();
                myMode = socketStream.read();
                //flagged = socketStream.read() == 1;
                spawnType = SpawnTabType.INVENTORY;
                searchSyntax = "";
                fetchSearchResults = true;
                totalExp = 0L;
                aLong1220 = 0L;
                mouseDetection.coordsIndex = 0;
                super.awtFocus = true;
                aBoolean954 = true;
                loggedIn = true;
                outBuffer.currentPosition = 0;
                incoming.currentPosition = 0;
                opcode = -1;
                lastOpcode = -1;
                secondLastOpcode = -1;
                thirdLastOpcode = -1;
        		//ChatboxItemSearch.reset();
                packetSize = 0;
                timeoutCounter = 0;
                systemUpdateTime = 0;
                anInt1011 = 0;
                hintIconDrawType = 0;
                menuActionRow = 0;
                menuOpen = false;
                super.idleTime = 0;
                for (int index = 0; index < 100; index++)
                    chatMessages[index] = null;
                itemSelected = 0;
                spellSelected = 0;
                loadingStage = 0;
                trackCount = 0;
                setNorth();
                minimapState = 0;
                lastKnownPlane = -1;
                destinationX = 0;
                destinationY = 0;
                playerCount = 0;
                npcCount = 0;
                for (int index = 0; index < maxPlayers; index++) {
                    players[index] = null;
                    playerSynchronizationBuffers[index] = null;
                }
                for (int index = 0; index < 16384; index++)
                    npcs[index] = null;
                localPlayer = players[internalLocalPlayerIndex] = new Player();
                projectiles.clear();
                incompleteAnimables.clear();
                clearRegionalSpawns();
                fullscreenInterfaceID = -1;
                friendServerStatus = 0;
                friendsCount = 0;
                dialogueId = -1;
                backDialogueId = -1;
                openInterfaceId = -1;
                overlayInterfaceId = -1;
                openWalkableInterface = -1;
                continuedDialogue = false;
                tabId = 3;
                inputDialogState = 0;
                menuOpen = false;
                messagePromptRaised = false;
                clickToContinueString = null;
                multicombat = 0;
                flashingSidebarId = -1;
                maleCharacter = true;
                changeCharacterGender();
                for (int index = 0; index < 5; index++)
                    characterDesignColours[index] = 0;
                for (int index = 0; index < 5; index++) {
                    playerOptions[index] = null;
                    playerOptionsHighPriority[index] = false;
                }
                anInt1175 = 0;
                anInt1134 = 0;
                anInt986 = 0;
                anInt1288 = 0;
                anInt924 = 0;
                anInt1188 = 0;
                anInt1155 = 0;
                anInt1226 = 0;
                SettingsWidget.updateSettings();
                this.stopMidi();
                setupGameplayScreen();
                
                return;
            }
            if (response == 28) {
            	loginMessage = "Username or password contains illegal characters. Try other combinations.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 30) {
            	loginMessage = "Overload has been updated! Please download the newest client!";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 3) {
            	loginMessage = "Invalid username or password.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 4) {
            	loginMessage = "Your account has been disabled. Please contact a staff-member and appeal on the forum.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 22) {
            	loginMessage = "Your computer has been banned! Please contact staff!";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 27) {
            	loginMessage = "Your host has been banned! Please contact staff!";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 5) {
            	loginMessage = "Your account is already logged in. Try again in 60 seconds...";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 6) {
            	loginMessage = "Overload has been updated!";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 7) {
            	loginMessage = "This world is full. Please use a different world.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 8) {
            	loginMessage = "Unable to connect. Login server offline.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 9) {
            	loginMessage = "Login limit exceeded. Too many connections from your address.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 10) {
            	loginMessage = "Unable to connect. Bad session id.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 11) {
            	loginMessage = "Login server rejected session. Please try again.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 12) {
            	loginMessage = "You need a members account to login to this world. Please subscribe, or use a different world.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 13) {
            	loginMessage = "Could not complete login. Please try using a different world.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 14) {
            	loginMessage = "The server is being updated. Please wait 1 minute and try again.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 80) {
            	loginMessage = "Your Hardcore Ironman is Dead";
            	setLoginState(LoginState.LOGINMESSAGES);
				return;
			}
            if (response == 15) {
                loggedIn = true;
                incoming.currentPosition = 0;
                opcode = -1;
                lastOpcode = -1;
                secondLastOpcode = -1;
                thirdLastOpcode = -1;
                packetSize = 0;
                timeoutCounter = 0;
                systemUpdateTime = 0;
                menuActionRow = 0;
                menuOpen = false;
                loadingStartTime = System.currentTimeMillis();
                return;
            }
            if (response == 16) {
            	loginMessage = "Login attempts exceeded. Please wait 1 minute and try again.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 17) {
            	loginMessage = "You are standing in a members-only area. To play on this world move to a free area first.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 20) {
            	loginMessage = "Invalid loginserver requested. Please try using a different world.";
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == 21) {
                for (int k1 = socketStream.read(); k1 >= 0; k1--) {
                    drawLoginScreen(true);
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception _ex) {
                    }
                }
                login(name, password, reconnecting);
                return;
            }
            if (response == 22) {
            	setLoginState(LoginState.LOGINMESSAGES);
                return;
            }
            if (response == -1) {
                if (copy == 0) {
                    if (loginFailures < 2) {
                        try {
                            Thread.sleep(2000L);
                        } catch (Exception _ex) {
                        }
                        loginFailures++;
                        login(name, password, reconnecting);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } catch (IOException _ex) {
        } catch (Exception e) {
            System.out.println("Error while generating uid. Skipping step.");
            e.printStackTrace();
        }
    }

    private void clearRegionalSpawns() {
        for (int plane = 0; plane < 4; plane++) {
            for (int x = 0; x < 104; x++) {
                for (int y = 0; y < 104; y++) {
                    groundItems[plane][x][y] = null;
                }
            }
        }
        if (spawns == null) {
            spawns = new Deque();
        }
        for (SpawnedObject object = (SpawnedObject) spawns
                .reverseGetFirst(); object != null; object =
                     (SpawnedObject) spawns.reverseGetNext())
            object.getLongetivity = 0;
    }

    private boolean shiftTeleport() {
        return (shiftDown && myPrivilege >= 2 && myPrivilege <= 4);
    }

    private boolean doWalkTo(int type, int j, int k, int i1, int j1, int k1, int l1, int i2, int j2,
                             boolean flag, int k2) {

        int clipData[][] = collisionMaps[plane].clipData;

        boolean shiftTeleport = shiftTeleport();
        if (shiftTeleport) {
            clipData = emptyClipData;
        }
        if (!controlDown) {
        try {
            byte byte0 = 104;
            byte byte1 = 104;
            for (int l2 = 0; l2 < byte0; l2++) {
                for (int i3 = 0; i3 < byte1; i3++) {
                    anIntArrayArray901[l2][i3] = 0;
                    anIntArrayArray825[l2][i3] = 0x5f5e0ff;
                }
            }
            int j3 = j2;
            int k3 = j1;
            anIntArrayArray901[j2][j1] = 99;
            anIntArrayArray825[j2][j1] = 0;
            int l3 = 0;
            int i4 = 0;
            bigX[l3] = j2;
            bigY[l3++] = j1;
            boolean flag1 = false;
            int j4 = bigX.length;
            while (i4 != l3) {
                j3 = bigX[i4];
                k3 = bigY[i4];
                i4 = (i4 + 1) % j4;
                if (j3 == k2 && k3 == i2) {
                    flag1 = true;
                    break;
                }
                if (i1 != 0) {
                    if ((i1 < 5 || i1 == 10)
                            && collisionMaps[plane].method219(k2, j3, k3, j, i1 - 1, i2)) {
                        flag1 = true;
                        break;
                    }
                    if (i1 < 10 && collisionMaps[plane].method220(k2, i2, k3, i1 - 1, j, j3)) {
                        flag1 = true;
                        break;
                    }
                }
                if (k1 != 0 && k != 0
                        && collisionMaps[plane].method221(i2, k2, j3, k, l1, k1, k3)) {
                    flag1 = true;
                    break;
                }
                int l4 = anIntArrayArray825[j3][k3] + 1;
                if (j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0
                        && (clipData[j3 - 1][k3] & 0x1280108) == 0) {
                    bigX[l3] = j3 - 1;
                    bigY[l3] = k3;
                    l3 = (l3 + 1) % j4;
                    anIntArrayArray901[j3 - 1][k3] = 2;
                    anIntArrayArray825[j3 - 1][k3] = l4;
                }
                if (j3 < byte0 - 1 && anIntArrayArray901[j3 + 1][k3] == 0
                        && (clipData[j3 + 1][k3] & 0x1280180) == 0) {
                    bigX[l3] = j3 + 1;
                    bigY[l3] = k3;
                    l3 = (l3 + 1) % j4;
                    anIntArrayArray901[j3 + 1][k3] = 8;
                    anIntArrayArray825[j3 + 1][k3] = l4;
                }
                if (k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0
                        && (clipData[j3][k3 - 1] & 0x1280102) == 0) {
                    bigX[l3] = j3;
                    bigY[l3] = k3 - 1;
                    l3 = (l3 + 1) % j4;
                    anIntArrayArray901[j3][k3 - 1] = 1;
                    anIntArrayArray825[j3][k3 - 1] = l4;
                }
                if (k3 < byte1 - 1 && anIntArrayArray901[j3][k3 + 1] == 0
                        && (clipData[j3][k3 + 1] & 0x1280120) == 0) {
                    bigX[l3] = j3;
                    bigY[l3] = k3 + 1;
                    l3 = (l3 + 1) % j4;
                    anIntArrayArray901[j3][k3 + 1] = 4;
                    anIntArrayArray825[j3][k3 + 1] = l4;
                }
                if (j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0
                        && (clipData[j3 - 1][k3 - 1] & 0x128010e) == 0
                        && (clipData[j3 - 1][k3] & 0x1280108) == 0
                        && (clipData[j3][k3 - 1] & 0x1280102) == 0) {
                    bigX[l3] = j3 - 1;
                    bigY[l3] = k3 - 1;
                    l3 = (l3 + 1) % j4;
                    anIntArrayArray901[j3 - 1][k3 - 1] = 3;
                    anIntArrayArray825[j3 - 1][k3 - 1] = l4;
                }
                if (j3 < byte0 - 1 && k3 > 0 && anIntArrayArray901[j3 + 1][k3 - 1] == 0
                        && (clipData[j3 + 1][k3 - 1] & 0x1280183) == 0
                        && (clipData[j3 + 1][k3] & 0x1280180) == 0
                        && (clipData[j3][k3 - 1] & 0x1280102) == 0) {
                    bigX[l3] = j3 + 1;
                    bigY[l3] = k3 - 1;
                    l3 = (l3 + 1) % j4;
                    anIntArrayArray901[j3 + 1][k3 - 1] = 9;
                    anIntArrayArray825[j3 + 1][k3 - 1] = l4;
                }
                if (j3 > 0 && k3 < byte1 - 1 && anIntArrayArray901[j3 - 1][k3 + 1] == 0
                        && (clipData[j3 - 1][k3 + 1] & 0x1280138) == 0
                        && (clipData[j3 - 1][k3] & 0x1280108) == 0
                        && (clipData[j3][k3 + 1] & 0x1280120) == 0) {
                    bigX[l3] = j3 - 1;
                    bigY[l3] = k3 + 1;
                    l3 = (l3 + 1) % j4;
                    anIntArrayArray901[j3 - 1][k3 + 1] = 6;
                    anIntArrayArray825[j3 - 1][k3 + 1] = l4;
                }
                if (j3 < byte0 - 1 && k3 < byte1 - 1 && anIntArrayArray901[j3 + 1][k3 + 1] == 0
                        && (clipData[j3 + 1][k3 + 1] & 0x12801e0) == 0
                        && (clipData[j3 + 1][k3] & 0x1280180) == 0
                        && (clipData[j3][k3 + 1] & 0x1280120) == 0) {
                    bigX[l3] = j3 + 1;
                    bigY[l3] = k3 + 1;
                    l3 = (l3 + 1) % j4;
                    anIntArrayArray901[j3 + 1][k3 + 1] = 12;
                    anIntArrayArray825[j3 + 1][k3 + 1] = l4;
                }
            }
            if (!flag1) {
                if (flag) {
                    int i5 = 100;
                    for (int k5 = 1; k5 < 2; k5++) {
                        for (int i6 = k2 - k5; i6 <= k2 + k5; i6++) {
                            for (int l6 = i2 - k5; l6 <= i2 + k5; l6++) {
                                if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104
                                        && anIntArrayArray825[i6][l6] < i5) {
                                    i5 = anIntArrayArray825[i6][l6];
                                    j3 = i6;
                                    k3 = l6;
                                    flag1 = true;
                                }
                            }
                        }
                        if (flag1)
                            break;
                    }
                }
                if (!flag1) {
                    return false;
                }
            }
            i4 = 0;
            bigX[i4] = j3;
            bigY[i4++] = k3;
            int l5;
            for (int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != j2 || k3 != j1; j5 =
                    anIntArrayArray901[j3][k3]) {
                if (j5 != l5) {
                    l5 = j5;
                    bigX[i4] = j3;
                    bigY[i4++] = k3;
                }
                if ((j5 & 2) != 0)
                    j3++;
                else if ((j5 & 8) != 0)
                    j3--;
                if ((j5 & 1) != 0)
                    k3++;
                else if ((j5 & 4) != 0)
                    k3--;
            }
            if (i4 > 0) {
                int k4 = i4;
                if (k4 > 25)
                    k4 = 25;
                i4--;
                int k6 = bigX[i4];
                int i7 = bigY[i4];
                anInt1288 += k4;
                if (anInt1288 >= 92) {
					/*Anti-cheatValidates, walking. Not used. OUTPUT_BUFFER.createFrame(36);
					OUTPUT_BUFFER.writeDWord(0);*/
                    anInt1288 = 0;
                }

                sendPacket(new UpdatePlane(plane).create());

                final int k5 = k4;
                final int i_4 = i4;
                final int keyArr = super.keyArray[5] != 1 ? 0 : 1;

                OutgoingPacket movementPacket = new OutgoingPacket() {
                    @Override
                    public PacketBuilder create() {
                    	int opcode = 164;
                    	if (type == 1) {
                    		opcode = 248;
                    	} else if (type == 2) {
                    		opcode = 98;
                    	}
                    	PacketBuilder buf = new PacketBuilder(opcode);
                    	// buf.putByte(k5 + k5 + 3);
                    	buf.putByte(shiftTeleport ? 1 : 0);
                    	buf.putSignedBigEndian(k6 + regionBaseX);
                        destinationX = bigX[0];
                        destinationY = bigY[0];
                        int i__4 = i_4;
                        for (int j7 = 1; j7 < k5; j7++) {
                            i__4--;
                            buf.putByte(bigX[i__4] - k6);
                            buf.putByte(bigY[i__4] - i7);
                        }
                        buf.putUnsignedWordBigEndian(i7 + regionBaseY);
                        buf.putByteC(keyArr);
                    	return buf;
                    }
                };
                
                lastNpcClicked = -1;
                
                sendPacket(movementPacket.create());

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        } else {
			for (int i = 0; i < togglesItemBoxesId.size(); i++) {
				int[] pos = togglesItemBoxesPos.get(i);
				String Id = togglesItemBoxesId.get(i);
				if ((super.saveClickX >= pos[0] && super.saveClickX <= pos[0]+12) && (super.saveClickY >= pos[1] && super.saveClickY <= pos[1]+12)) {
					if (hiddenItems.contains("" + Id)) {
						hiddenItems.remove("" + Id);
					} else {
						hiddenItems.add("" + Id);
					}
				}
			}
		}
        return type != 1;
    }

    private void npcUpdateMask(Buffer stream) {
        for (int j = 0; j < mobsAwaitingUpdateCount; j++) {
            int k = mobsAwaitingUpdate[j];
            Npc npc = npcs[k];
            int mask = stream.readUnsignedByte();
            if ((mask & 0x10) != 0) {
                int i1 = stream.readLEUShort();
                if (i1 == 65535)
                    i1 = -1;
                int i2 = stream.readUnsignedByte();
                if (i1 == npc.emoteAnimation && i1 != -1) {
                    int l2 = Animation.animations[i1].replayMode;
                    if (l2 == 1) {
                        npc.displayedEmoteFrames = 0;
                        npc.emoteTimeRemaining = 0;
                        npc.animationDelay = i2;
                        npc.currentAnimationLoops = 0;
                    }
                    if (l2 == 2)
                        npc.currentAnimationLoops = 0;
                } else if (i1 == -1 || npc.emoteAnimation == -1
                        || Animation.animations[i1].forcedPriority >= Animation.animations[npc.emoteAnimation].forcedPriority) {
                    npc.emoteAnimation = i1;
                    npc.displayedEmoteFrames = 0;
                    npc.emoteTimeRemaining = 0;
                    npc.animationDelay = i2;
                    npc.currentAnimationLoops = 0;
                    npc.anInt1542 = npc.remainingPath;
                }
            }
            if ((mask & 0x80) != 0) {
                npc.graphic = stream.readUShort();
                int k1 = stream.readInt();
                npc.graphicHeight = k1 >> 16;
                npc.graphicDelay = tick + (k1 & 0xffff);
                npc.currentAnimation = 0;
                npc.anInt1522 = 0;
                if (npc.graphicDelay > tick)
                    npc.currentAnimation = -1;
                if (npc.graphic == 65535)
                    npc.graphic = -1;
            }
            if ((mask & 8) != 0) {
                int damage = stream.readShort();
                int type = stream.readUnsignedByte();
                int hp = stream.readShort();
                int maxHp = stream.readShort();
                npc.updateHitData(type, damage, tick);
                npc.loopCycleStatus = tick + 300;
                npc.currentHealth = hp;
                npc.maxHealth = maxHp;
            }
            if ((mask & 0x20) != 0) {
                npc.interactingEntity = stream.readUShort();
                if (npc.interactingEntity == 65535)
                    npc.interactingEntity = -1;
            }
            if ((mask & 1) != 0) {
                npc.spokenText = stream.readString();
                npc.textCycle = 100;
            }
            if ((mask & 0x40) != 0) {
                int damage = stream.readShort();
                int type = stream.readUnsignedByte();
                int hp = stream.readShort();
                int maxHp = stream.readShort();
                npc.updateHitData(type, damage, tick);
                npc.loopCycleStatus = tick + 300;
                npc.currentHealth = hp;
                npc.maxHealth = maxHp;
            }
            if ((mask & 0x2) != 0) {
                npc.headIcon = stream.readUnsignedByte();
                boolean transform = stream.readUnsignedByte() == 1;

                if (transform) {
                    npc.desc = NpcDefinition.lookup(stream.readLEUShortA());
                    npc.size = npc.desc.size;
                    npc.degreesToTurn = npc.desc.degreesToTurn;
                    npc.walkAnimIndex = npc.desc.walkAnim;
                    npc.turn180AnimIndex = npc.desc.turn180AnimIndex;
                    npc.turn90CWAnimIndex = npc.desc.turn90CWAnimIndex;
                    npc.turn90CCWAnimIndex = npc.desc.turn90CCWAnimIndex;
                    npc.idleAnimation = npc.desc.standAnim;
                }
            }
            if ((mask & 4) != 0) {
                npc.faceX = stream.readLEUShort();
                npc.faceY = stream.readLEUShort();
            }
        }
    }

    private void buildAtNPCMenu(NpcDefinition entityDef, int i, int j, int k) {
        if (openInterfaceId == 15244) {
            return;
        }
        if (menuActionRow >= 400)
            return;
        if (entityDef.childrenIDs != null)
            entityDef = entityDef.morph();
        if (entityDef == null)
            return;
        if (!entityDef.clickable)
            return;
        String s = entityDef.name;

        if (entityDef.combatLevel != 0)
            s = s + combatDiffColor(localPlayer.combatLevel, entityDef.combatLevel)
                    + " (level-" + entityDef.combatLevel + ")";
        if (itemSelected == 1) {
            menuActionText[menuActionRow] = "Use " + selectedItemName + " with @yel@" + s;
            menuActionTypes[menuActionRow] = 582;
            selectedMenuActions[menuActionRow] = i;
            firstMenuAction[menuActionRow] = k;
            secondMenuAction[menuActionRow] = j;
            menuActionRow++;
            return;
        }
        if (spellSelected == 1) {
            if ((spellUsableOn & 2) == 2) {
                menuActionText[menuActionRow] = spellTooltip + " @yel@" + s;
                menuActionTypes[menuActionRow] = 413;
                selectedMenuActions[menuActionRow] = i;
                firstMenuAction[menuActionRow] = k;
                secondMenuAction[menuActionRow] = j;
                menuActionRow++;
            }
        } else {
            if (entityDef.actions != null) {
                for (int l = 4; l >= 0; l--)
                    if (entityDef.actions[l] != null && !entityDef.actions[l].equalsIgnoreCase("attack")) {
                        menuActionText[menuActionRow] = entityDef.actions[l] + " @yel@" + s;
                        if (l == 0)
                            menuActionTypes[menuActionRow] = 20;
                        if (l == 1)
                            menuActionTypes[menuActionRow] = 412;
                        if (l == 2)
                            menuActionTypes[menuActionRow] = 225;
                        if (l == 3)
                            menuActionTypes[menuActionRow] = 965;
                        if (l == 4)
                            menuActionTypes[menuActionRow] = 478;
                        selectedMenuActions[menuActionRow] = i;
                        firstMenuAction[menuActionRow] = k;
                        secondMenuAction[menuActionRow] = j;
                        menuActionRow++;
                    }

            }
            if (entityDef.actions != null) {
                for (int i1 = 4; i1 >= 0; i1--)
                    if (entityDef.actions[i1] != null && entityDef.actions[i1].equalsIgnoreCase("attack")) {

                        char c = '\0';
                        if (Configuration.npcAttackOptionPriority == 0) {
                            if (entityDef.combatLevel > localPlayer.combatLevel)
                                c = '\u07D0';
                        } else if (Configuration.npcAttackOptionPriority == 1) {
                            c = '\u07D0';
                        } else if (Configuration.npcAttackOptionPriority == 3) {
                            continue;
                        }


                        menuActionText[menuActionRow] = entityDef.actions[i1] + " @yel@" + s;
                        if (i1 == 0)
                            menuActionTypes[menuActionRow] = 20 + c;
                        if (i1 == 1)
                            menuActionTypes[menuActionRow] = 412 + c;
                        if (i1 == 2)
                            menuActionTypes[menuActionRow] = 225 + c;
                        if (i1 == 3)
                            menuActionTypes[menuActionRow] = 965 + c;
                        if (i1 == 4)
                            menuActionTypes[menuActionRow] = 478 + c;

                        selectedMenuActions[menuActionRow] = i;
                        firstMenuAction[menuActionRow] = k;
                        secondMenuAction[menuActionRow] = j;
                        menuActionRow++;
                    }

            }
            if ((myPrivilege >= 2 && myPrivilege <= 4)) {
                menuActionText[menuActionRow] = "Examine @yel@" + s + " @gre@(@whi@" + entityDef.interfaceType + "@gre@)";
            } else {
                menuActionText[menuActionRow] = "Examine @yel@" + s;
            }
            menuActionTypes[menuActionRow] = 1025;
            selectedMenuActions[menuActionRow] = i;
            firstMenuAction[menuActionRow] = k;
            secondMenuAction[menuActionRow] = j;
            menuActionRow++;
        }
    }

    private void buildAtPlayerMenu(int i, int j, Player player, int k) {
        if (openInterfaceId == 15244) {
            return;
        }
        if (player == localPlayer)
            return;
        if (menuActionRow >= 400)
            return;
        String s;
        if (player.skill == 0)
            s = player.name + combatDiffColor(localPlayer.combatLevel, player.combatLevel)
                    + " (level-" + player.combatLevel + ")";
        else
            s = player.name + " (skill-" + player.skill + ")";
        if (itemSelected == 1) {
            menuActionText[menuActionRow] = "Use " + selectedItemName + " with @whi@" + s;
            menuActionTypes[menuActionRow] = 491;
            selectedMenuActions[menuActionRow] = j;
            firstMenuAction[menuActionRow] = i;
            secondMenuAction[menuActionRow] = k;
            menuActionRow++;
        } else if (spellSelected == 1) {
            if ((spellUsableOn & 8) == 8) {
                menuActionText[menuActionRow] = spellTooltip + " @whi@" + s;
                menuActionTypes[menuActionRow] = 365;
                selectedMenuActions[menuActionRow] = j;
                firstMenuAction[menuActionRow] = i;
                secondMenuAction[menuActionRow] = k;
                menuActionRow++;
            }
        } else {
            for (int type = 4; type >= 0; type--) {
                if (playerOptions[type] != null) {
                    menuActionText[menuActionRow] = playerOptions[type] + " @whi@" + s;

                    char c = '\0';
                    if (playerOptions[type].equalsIgnoreCase("attack")) {

                        if (Configuration.playerAttackOptionPriority == 0) {
                            if (player.combatLevel > localPlayer.combatLevel)
                                c = '\u07D0';
                        } else if (Configuration.playerAttackOptionPriority == 1) {
                            c = '\u07D0';
                        } else if (Configuration.playerAttackOptionPriority == 3) {
                            continue;
                        }

                        if (localPlayer.team != 0 && player.team != 0)
                            if (localPlayer.team == player.team) {
                                c = '\u07D0';
                            } else {
                                c = '\0';
                            }

                    } else if (playerOptionsHighPriority[type])
                        c = '\u07D0';
                    if (type == 0) {
                        menuActionTypes[menuActionRow] = 561 + c;
                    }
                    if (type == 1) {
                        menuActionTypes[menuActionRow] = 779 + c;
                    }
                    if (type == 2) {
                        menuActionTypes[menuActionRow] = 27 + c;
                    }
                    if (type == 3) {
                        menuActionTypes[menuActionRow] = 577 + c;
                    }
                    if (type == 4) {
                        menuActionTypes[menuActionRow] = 729 + c;
                    }
                    selectedMenuActions[menuActionRow] = j;
                    firstMenuAction[menuActionRow] = i;
                    secondMenuAction[menuActionRow] = k;
                    menuActionRow++;
                }
            }
        }
        for (int row = 0; row < menuActionRow; row++) {
            if (menuActionTypes[row] == 519) {
                menuActionText[row] = (shiftTeleport() ? "Teleport here" : "Walk here") + " @whi@" + s;
                return;
            }
        }
    }

    private void method89(SpawnedObject class30_sub1) {
        int i = 0;
        int j = -1;
        int k = 0;
        int l = 0;
        if (class30_sub1.group == 0)
            i = scene.getWallObjectUid(class30_sub1.plane, class30_sub1.x, class30_sub1.y);
        if (class30_sub1.group == 1)
            i = scene.getWallDecorationUid(class30_sub1.plane, class30_sub1.x,
                    class30_sub1.y);
        if (class30_sub1.group == 2)
            i = scene.getGameObjectUid(class30_sub1.plane, class30_sub1.x,
                    class30_sub1.y);
        if (class30_sub1.group == 3)
            i = scene.getGroundDecorationUid(class30_sub1.plane, class30_sub1.x,
                    class30_sub1.y);
        if (i != 0) {
            int i1 = scene.getMask(class30_sub1.plane, class30_sub1.x, class30_sub1.y, i);
            j = i >> 14 & 0x7fff;
            k = i1 & 0x1f;
            l = i1 >> 6;
        }
        class30_sub1.getPreviousId = j;
        class30_sub1.previousType = k;
        class30_sub1.previousOrientation = l;
    }
    
    private volatile boolean clientLoaded = false;
    
    public static final SpriteCache spriteCache = new SpriteCache();
 
    public boolean writeFonts = false;
    
    public boolean fontsloaded = false;

    
    private int lastPercent = 0;
    private boolean above100 = false;

    
    public void drawLoading(String string, int percent) {
    	if (percent == 0) {
    		lastPercent = -1;
    	}
    	if (percent >= 100) {
    		percent = 100;
    		above100 = true;
    	}
    	
    	if (above100 && lastPercent > percent) {
    		lastPercent = 1;
    		percent = 0;
    		above100 = false;
    	}

    	while (lastPercent < percent) {
    		lastPercent++;
	    	if(!clientLoaded) {
	        	setupLoginScreen();		
	        	loginBoxImageProducer.initDrawingArea();
	        	
	        	try {
	        		if (loadingBackground == null) {
		        		URL url = new URL("https://eq-rsps.io/public/downloads/overload/client/1150.png");
						loadingBackground = new Sprite(url,765,503);
	        		}
	        	} catch (MalformedURLException e) {
					e.printStackTrace();
				}
	        	
	        	
	        	loadingBackground.drawAdvancedSprite(0, 0);
	    		
	    		loadingCircle.g2d = Rasterizer2D.createGraphics(false);
	
	    		loadingCircle.draw((frameWidth / 2)- (220 / 2), (frameHeight / 2)- (220 / 2), lastPercent);
	
	    		loadingCircle.g2d.dispose();
	
	    		FontArchiver.getFont1(0).drawStringCenter(string, 379, 239,0xc2baad, false, true);
	
	    		FontArchiver.getFont1(1).drawStringCenter(percent + " " + "%", 382,279, 0xf3aa63, false, true);
	    		
	    		loginBoxImageProducer.drawGraphics(0, graphics, 0);
	    	}
    	}
    }
    
    void startUp() {

		//AppdataDownloader.start(this);
		
    	if(writeFonts) {
    		FontArchiver.writeFontArchive(Signlink.findcachedir() + File.separator + "main_file_fonts.dat");
    	}
    	try {
			FontArchiver.loadfont();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
        drawLoading("Validating Cache", 0);
        if(fontsloaded) {
            CacheDownloader.start(this);
        }
        if (Signlink.cache_dat != null) {
            for (int i = 0; i < 5; i++)
                indices[i] = new FileStore(Signlink.cache_dat, Signlink.indices[i], i + 1);
        }
        try {
            drawLoading("Loading Game",5);
            titleArchive = createArchive(JagGrab.TITLE_CRC, "title screen", "title", JagGrab.CRCs[JagGrab.TITLE_CRC]);
            
            smallText = new GameFont(false, "p11_full", titleArchive);
            regularText = new GameFont(false, "p12_full", titleArchive);
            drawLoading("Loading Game",6);
            boldText = new GameFont(false, "b12_full", titleArchive);
            drawLoading("Loading Game",7);
            newSmallFont = new RSFont(false, "p11_full", titleArchive);
  
            drawLoading("Loading Game",8);
            newRegularFont = new RSFont(false, "p12_full", titleArchive);
            //drawLoadingText(11, "Loading Fonts");
            drawLoading("Loading Game",9);
            newBoldFont = new RSFont(false, "b12_full", titleArchive);
            //drawLoadingText(13, "Loading Fonts");
            drawLoading("Loading Game",10);
            newFancyFont = new RSFont(true, "q8_full", titleArchive);
            //drawLoadingText(15, "Loading Fonts");
            drawLoading("Loading Game",11);
            gameFont = new GameFont(true, "q8_full", titleArchive);
            //drawLoadingText(17, "Loading Fonts");
            drawLoading("Loading Game",12);
            
            drawLogo();
            loadTitleScreen();
            
            FileArchive configArchive = createArchive(JagGrab.CONFIG_CRC, "config", "config", JagGrab.CRCs[JagGrab.CONFIG_CRC]);
            FileArchive interfaceArchive = createArchive(JagGrab.INTERFACE_CRC, "interface", "interface", JagGrab.CRCs[JagGrab.INTERFACE_CRC]);
            FileArchive mediaArchive = createArchive(JagGrab.MEDIA_CRC, "2d graphics", "media", JagGrab.CRCs[JagGrab.MEDIA_CRC]);
            FileArchive streamLoader_6 = createArchive(JagGrab.UPDATE_CRC, "update list", "versionlist", JagGrab.CRCs[JagGrab.UPDATE_CRC]);
            this.mediaStreamLoader = mediaArchive;
            FileArchive textureArchive = createArchive(JagGrab.TEXTURES_CRC, "textures", "textures", JagGrab.CRCs[JagGrab.TEXTURES_CRC]);
            FileArchive wordencArchive = createArchive(JagGrab.CHAT_CRC, "chat system", "wordenc", JagGrab.CRCs[JagGrab.CHAT_CRC]);

            FileArchive soundArchive = createArchive(JagGrab.SOUNDS_CRC, "sound effects", "sounds", JagGrab.CRCs[JagGrab.SOUNDS_CRC]);
            
            drawLoading("Loading Game",13);
            tileFlags = new byte[4][104][104];
            drawLoading("Loading Game",14);
            tileHeights = new int[4][105][105];
            drawLoading("Loading Game",15);
            scene = new SceneGraph(tileHeights);
            drawLoading("Loading Game",16);
            for (int j = 0; j < 4; j++)
                collisionMaps[j] = new CollisionMap();
            drawLoading("Loading Game",17);
            minimapImage = new Sprite(512, 512);
            drawLoading("Loading Game",18);
            //drawLoadingText(60, "Connecting to update server");
            Frame.animationlist = new Frame[3000][0];
            resourceProvider = new ResourceProvider();
            resourceProvider.initialize(streamLoader_6, this);
            drawLoading("Loading Game",20);
            Model.method459(resourceProvider.getModelCount(), resourceProvider);
            //drawLoadingText(80, "Unpacking media");

            byte soundData[] = soundArchive.readFile("sounds.dat");
            Buffer stream = new Buffer(soundData);
            Track.unpack(stream);

            if (Configuration.repackIndexOne) {
                CacheUtils.repackCacheIndex(this, Store.MODEL);
            }

            if (Configuration.repackIndexTwo) {
                CacheUtils.repackCacheIndex(this, Store.ANIMATION);
            }

            if (Configuration.repackIndexThree) {
                CacheUtils.repackCacheIndex(this, Store.MUSIC);
            }

            if (Configuration.repackIndexFour) {
                CacheUtils.repackCacheIndex(this, Store.MAP);
            }

            if (Configuration.dumpIndexOne) {
                CacheUtils.dumpCacheIndex(this, Store.MODEL);
            }

            if (Configuration.dumpIndexTwo) {
                CacheUtils.dumpCacheIndex(this, Store.ANIMATION);
            }

            if (Configuration.dumpIndexThree) {
                CacheUtils.dumpCacheIndex(this, Store.MUSIC);
            }

            if (Configuration.dumpIndexFour) {
                CacheUtils.dumpCacheIndex(this, Store.MAP);
            }
            drawLoading("Loading Game",21);            
            try {
    			Path path = Paths.get(Signlink.findcachedir());
    			Path spriteDataPath = path.resolve("main_file_sprites.dat");
    			Path spriteMetaPath = path.resolve("main_file_sprites.idx");
    			Client.spriteCache.init(spriteDataPath.toFile(), spriteMetaPath.toFile());
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            drawLoading("Loading Game",22);
            SkillOrbs.init();
            this.hp = spriteCache.lookup(40);

            drawLoading("Loading Game",23);
            for (int imageId = 73, index = 0; index < SkillConstants.SKILL_COUNT; imageId++, index++) {
                skill_sprites[index] = spriteCache.lookup(imageId);
            }
            for (int imageId = 1150, index = 0; index < 5; imageId++, index++) {
            	backgroundimage[index] = spriteCache.lookup(imageId);
            }
            drawLoading("Loading Game",24);
            //drawLoadingText(23, "Loading Sprites");
            multiOverlay = new Sprite(mediaArchive, "overlay_multiway", 0);
            mapBack = new IndexedImage(mediaArchive, "mapback", 0);
            for (int j3 = 0; j3 <= 14; j3++)
                sideIcons[j3] = new Sprite(mediaArchive, "sideicons", j3);
            compass = new Sprite(mediaArchive, "compass", 0);
            drawLoading("Loading Game",25);
            try {
                for (int k3 = 0; k3 < 100; k3++)
                    mapScenes[k3] = new IndexedImage(mediaArchive, "mapscene", k3);
            } catch (Exception _ex) {
            }
            drawLoading("Loading Game",26);
            //drawLoadingText(26, "Loading Sprites");
            try {
                for (int l3 = 0; l3 < 100; l3++)
                    mapFunctions[l3] = new Sprite(mediaArchive, "mapfunction", l3);
            } catch (Exception _ex) {
            }
            drawLoading("Loading Game",27);
            try {
                for (int i4 = 0; i4 < 20; i4++)
                    hitMarks[i4] = new Sprite(mediaArchive, "hitmarks", i4);
            } catch (Exception _ex) {
            }
            try {
                for (int h1 = 0; h1 < 6; h1++)
                    headIconsHint[h1] = new Sprite(mediaArchive, "headicons_hint", h1);
            } catch (Exception _ex) {
            }
            drawLoading("Loading Game",28);
            try {
                for (int j4 = 0; j4 < 8; j4++)
                    headIcons[j4] = new Sprite(mediaArchive, "headicons_prayer", j4);
                for (int j45 = 0; j45 < 3; j45++)
                    skullIcons[j45] = new Sprite(mediaArchive, "headicons_pk", j45);
            } catch (Exception _ex) {
            }
            drawLoading("Loading Game",29);
            //drawLoadingText(30, "Loading Sprites");
            mapFlag = new Sprite(mediaArchive, "mapmarker", 0);
            mapMarker = new Sprite(mediaArchive, "mapmarker", 1);
            for (int k4 = 0; k4 < 8; k4++)
                crosses[k4] = new Sprite(mediaArchive, "cross", k4);
            drawLoading("Loading Game",30);
            mapDotItem = new Sprite(mediaArchive, "mapdots", 0);
            mapDotNPC = new Sprite(mediaArchive, "mapdots", 1);
            //drawLoadingText(33, "Loading Sprites");
            drawLoading("Loading Game",31);
            mapDotPlayer = new Sprite(mediaArchive, "mapdots", 2);
            mapDotFriend = new Sprite(mediaArchive, "mapdots", 3);
            mapDotTeam = new Sprite(mediaArchive, "mapdots", 4);
            mapDotClan = new Sprite(mediaArchive, "mapdots", 5);
            drawLoading("Loading Game",32);
            //drawLoadingText(36, "Loading Sprites");
            scrollBar1 = new Sprite(mediaArchive, "scrollbar", 0);
            drawLoading("Loading Game",33);
            scrollBar2 = new Sprite(mediaArchive, "scrollbar", 1);
            unpackModIcons();
            drawLoading("Loading Game",34);
            unpackModeIcons();
            //drawLoadingText(39, "Loading Sprites");
            Sprite sprite = new Sprite(mediaArchive, "screenframe", 0);
            leftFrame = new ProducingGraphicsBuffer(sprite.myWidth, sprite.myHeight);
            drawLoading("Loading Game",36);
            sprite.method346(0, 0);
            sprite = new Sprite(mediaArchive, "screenframe", 1);
            topFrame = new ProducingGraphicsBuffer(sprite.myWidth, sprite.myHeight);
            drawLoading("Loading Game",37);
            sprite.method346(0, 0);
            int i5 = (int) (Math.random() * 21D) - 10;
            drawLoading("Loading Game",38);
            int j5 = (int) (Math.random() * 21D) - 10;
            drawLoading("Loading Game",39);
            int k5 = (int) (Math.random() * 21D) - 10;
            int l5 = (int) (Math.random() * 41D) - 20;
            for (int i6 = 0; i6 < 100; i6++) {
                if (mapFunctions[i6] != null)
                    mapFunctions[i6].method344(i5 + l5, j5 + l5, k5 + l5);
                if (mapScenes[i6] != null)
                    mapScenes[i6].offsetColor(i5 + l5, j5 + l5, k5 + l5);
            }
            drawLoading("Loading Game",40);
            //drawLoadingText(42, "Loading textures");
            Rasterizer3D.loadTextures(textureArchive);
            drawLoading("Loading Game",41);
            Rasterizer3D.setBrightness(0.80000000000000004D);
            drawLoading("Loading Game",42);
            Rasterizer3D.initiateRequestBuffers();
            Animation.init(configArchive);
            drawLoading("Loading Game",48);
            ObjectDefinition.init(configArchive);
            drawLoading("Loading Game",53);
            FloorDefinition.init(configArchive);
            drawLoading("Loading Game",58);
            NpcDefinition.init(configArchive);
            
            if (Configuration.DUMP_NPC_DEFS) {
            	NpcDefinition.dump();
            }
            
            drawLoading("Loading Game",63);
            IdentityKit.init(configArchive);
            drawLoading("Loading Game",68);
            Graphic.init(configArchive);
            drawLoading("Loading Game",73);
            VariablePlayer.init(configArchive);
            VariableBits.init(configArchive);
            drawLoading("Loading Game",78);
            ItemDefinition.init(configArchive);
            ItemDefinition.isMembers = isMembers;
            drawLoading("Loading Game",83);
            
            //drawLoadingText(95, "Unpacking interfaces");
            GameFont gameFonts[] = {smallText, regularText, boldText, gameFont};
            Widget.load(interfaceArchive, gameFonts, mediaArchive, new RSFont[]{newSmallFont, newRegularFont, newBoldFont, newFancyFont});
            drawLoading("Loading Game",85);
            //drawLoadingText(100, "Preparing game engine");
            drawLoading("Loading Game",86);
            for (int j6 = 0; j6 < 33; j6++) {
                int k6 = 999;
                int i7 = 0;
                for (int k7 = 0; k7 < 34; k7++) {
                    if (mapBack.palettePixels[k7 + j6 * mapBack.width] == 0) {
                        if (k6 == 999)
                            k6 = k7;
                        continue;
                    }

                    if (k6 == 999)
                        continue;
                    i7 = k7;
                    break;
                }
                anIntArray968[j6] = k6;
                anIntArray1057[j6] = i7 - k6;
            }
            drawLoading("Loading Game",90);
            try {
    			FontArchiver.loadArchive();
    		} catch (FileNotFoundException e1) {
    			e1.printStackTrace();
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
            drawLoading("Loading Game",93);
            Widget.LoadOtherInterfaces(gameFonts);
            drawLoading("Loading Game",95);
            for (int l6 = 1; l6 < 153; l6++) {
                int j7 = 999;
                int l7 = 0;
                for (int j8 = 24; j8 < 177; j8++) {
                    if (mapBack.palettePixels[j8 + l6 * mapBack.width] == 0 && (j8 > 34 || l6 > 34)) {
                        if (j7 == 999) {
                            j7 = j8;
                        }
                        continue;
                    }
                    if (j7 == 999) {
                        continue;
                    }
                    l7 = j8;
                    break;
                }
                minimapLeft[l6 - 1] = j7 - 24;
                minimapLineWidth[l6 - 1] = l7 - j7;
            }
            drawLoading("Loading Game",96);
            setBounds();
            drawLoading("Loading Game",97);
            MessageCensor.load(wordencArchive);
            drawLoading("Loading Game",98);
            mouseDetection = new MouseDetection(this);
            drawLoading("Loading Game",99);
            startRunnable(mouseDetection, 10);
            SceneObject.clientInstance = this;
            ObjectDefinition.clientInstance = this;
            NpcDefinition.clientInstance = this;
            if (Configuration.JAGCACHED_ENABLED) {
                //new Thread(new BackgroundRequester()).start();
            }
            loadPlayerData();
			AdvancedSettings.load();
			AccountManager.loadAccount();
			
			clientLoaded = true;
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            Signlink.reporterror("loaderror " + aString1049 + " " + anInt1079);
        }
        loadingError = true;
    }

    private void updatePlayerList(Buffer stream, int packetSize) {
        while (stream.bitPosition + 10 < packetSize * 8) {
            int index = stream.readBits(11);
            if (index == 2047) {
                break;
            }
            if (players[index] == null) {
                players[index] = new Player();
                if (playerSynchronizationBuffers[index] != null) {
                    players[index].updateAppearance(playerSynchronizationBuffers[index]);
                }
            }
            playerList[playerCount++] = index;
            Player player = players[index];
            player.time = tick;

            int update = stream.readBits(1);

            if (update == 1)
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = index;

            int discardWalkingQueue = stream.readBits(1);

            int y = stream.readBits(5);

            if (y > 15) {
                y -= 32;
            }

            int x = stream.readBits(5);

            if (x > 15) {
                x -= 32;
            }

            player.setPos(localPlayer.pathX[0] + x, localPlayer.pathY[0] + y, discardWalkingQueue == 1);
        }
        stream.disableBitAccess();
    }

    public boolean inCircle(int circleX, int circleY, int clickX, int clickY, int radius) {
        return java.lang.Math.pow((circleX + radius - clickX), 2)
                + java.lang.Math.pow((circleY + radius - clickY), 2) < java.lang.Math
                .pow(radius, 2);
    }

    private long lastClick = 0L;
    
    private void processMainScreenClick() {
        if (openInterfaceId == 15244) {
            return;
        }
        if (minimapState != 0) {
            return;
        }
        if (specialHover) {
            return;
        }
        if (super.clickMode3 == 1) {
            int i = super.saveClickX - 25 - 547;
            int j = super.saveClickY - 5 - 3;
            if (frameMode != ScreenMode.FIXED) {
                i = super.saveClickX - (frameWidth - 182 + 24);
                j = super.saveClickY - 8;
            }
            if (newclickInRegion(teleportX, teleportY, spriteCache.lookup(1204))) {
            	if (System.currentTimeMillis() - lastClick < 200) {
    				TeleportChatbox.open(-1);
    				return;
    			}
            	lastClick = System.currentTimeMillis();
            }
            if (inCircle(0, 0, i, j, 76) && mouseMapPosition() && !runHover) {
                i -= 73;
                j -= 75;
                int k = cameraHorizontal + minimapRotation & 0x7ff;
                int i1 = Rasterizer3D.anIntArray1470[k];
                int j1 = Rasterizer3D.COSINE[k];
                i1 = i1 * (minimapZoom + 256) >> 8;
                j1 = j1 * (minimapZoom + 256) >> 8;
                int k1 = j * i1 + i * j1 >> 11;
                int l1 = j * j1 - i * i1 >> 11;
                int i2 = localPlayer.x + k1 >> 7;
                int j2 = localPlayer.y - l1 >> 7;
                boolean flag1 = doWalkTo(1, 0, 0, 0, localPlayer.pathY[0], 0, 0, j2,
                        localPlayer.pathX[0], true, i2);
                if (flag1) {

					/*outgoing.writeByte(i);
					outgoing.writeByte(j);
					outgoing.writeShort(cameraHorizontal);
					outgoing.writeByte(57);
					outgoing.writeByte(minimapRotation);
					outgoing.writeByte(minimapZoom);
					outgoing.writeByte(89);
					outgoing.writeShort(localPlayer.x);
					outgoing.writeShort(localPlayer.y);
					outgoing.writeByte(anInt1264);
					outgoing.writeByte(63);*/
                }
            }
            anInt1117++;
            if (anInt1117 > 1151) {
                anInt1117 = 0;
                // anti-cheat
				/*outgoing.writeOpcode(246);
				outgoing.writeByte(0);
				int bufPos = outgoing.currentPosition;

				if ((int) (Math.random() * 2D) == 0) {
					outgoing.writeByte(101);
				}

				outgoing.writeByte(197);
				outgoing.writeShort((int) (Math.random() * 65536D));
				outgoing.writeByte((int) (Math.random() * 256D));
				outgoing.writeByte(67);
				outgoing.writeShort(14214);

				if ((int) (Math.random() * 2D) == 0) {
					outgoing.writeShort(29487);
				}

				outgoing.writeShort((int) (Math.random() * 65536D));

				if ((int) (Math.random() * 2D) == 0) {
					outgoing.writeByte(220);
				}

				outgoing.writeByte(180);
				outgoing.writeBytes(outgoing.currentPosition - bufPos);*/
            }
        }
    }

    private String interfaceIntToString(int j) {
        if (j < 0x3b9ac9ff)
            return String.valueOf(format.format(j));
        else
            return "*";
    }

    private void showErrorScreen() {
        Graphics g = getGameComponent().getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, 765, 503);
        method4(1);
        if (loadingError) {
            aBoolean831 = false;
            g.setFont(new Font("Helvetica", 1, 16));
            g.setColor(Color.yellow);
            int k = 35;
            g.drawString("Sorry, an error has occured whilst loading "
                    + Configuration.CLIENT_NAME, 30, k);
            k += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, k);
            k += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, k);
            k += 30;
            g.drawString(
                    "2: Try clearing your web-browsers cache from tools->internet options",
                    30, k);
            k += 30;
            g.drawString("3: Try using a different game-world", 30, k);
            k += 30;
            g.drawString("4: Try rebooting your computer", 30, k);
            k += 30;
            g.drawString(
                    "5: Try selecting a different version of Java from the play-game menu",
                    30, k);
        }
        if (genericLoadingError) {
            aBoolean831 = false;
            g.setFont(new Font("Helvetica", 1, 20));
            g.setColor(Color.white);
            g.drawString("Error - unable to load game!", 50, 50);
            g.drawString("To play " + Configuration.CLIENT_NAME + " make sure you play from",
                    50, 100);
            g.drawString("http://www.aqp.io", 50, 150);
        }
        if (rsAlreadyLoaded) {
            aBoolean831 = false;
            g.setColor(Color.yellow);
            int l = 35;
            g.drawString("Error a copy of " + Configuration.CLIENT_NAME
                    + " already appears to be loaded", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, l);
            l += 30;
            g.drawString("2: Try rebooting your computer, and reloading", 30, l);
            l += 30;
        }
    }

    public URL getCodeBase() {
        try {
            return new URL(server + ":" + (80 + portOffset));
        } catch (Exception _ex) {
        }
        return null;
    }

    private void processNpcMovement() {
        for (int j = 0; j < npcCount; j++) {
            int k = npcIndices[j];
            Npc npc = npcs[k];
            if (npc != null)
                processMovement(npc);
        }
    }

    private void processMovement(Mob mob) {
        if (mob.x < 128 || mob.y < 128 || mob.x >= 13184 || mob.y >= 13184) {
            mob.emoteAnimation = -1;
            mob.graphic = -1;
            mob.startForceMovement = 0;
            mob.endForceMovement = 0;
            mob.x = mob.pathX[0] * 128 + mob.size * 64;
            mob.y = mob.pathY[0] * 128 + mob.size * 64;
            mob.resetPath();
        }
        if (mob == localPlayer && (mob.x < 1536 || mob.y < 1536 || mob.x >= 11776
                || mob.y >= 11776)) {
            mob.emoteAnimation = -1;
            mob.graphic = -1;
            mob.startForceMovement = 0;
            mob.endForceMovement = 0;
            mob.x = mob.pathX[0] * 128 + mob.size * 64;
            mob.y = mob.pathY[0] * 128 + mob.size * 64;
            mob.resetPath();
        }
        if (mob.startForceMovement > tick) {
            mob.nextPreForcedStep();
        } else if (mob.endForceMovement >= tick) {
            mob.nextForcedMovementStep();
        } else {
            mob.nextStep();
        }
        appendFocusDestination(mob);
        mob.updateAnimation();
    }

    private void appendFocusDestination(Mob entity) {
        if (entity.degreesToTurn == 0)
            return;
        if (entity.interactingEntity != -1 && entity.interactingEntity < 32768 && entity.interactingEntity < npcs.length) {
            Npc npc = npcs[entity.interactingEntity];
            if (npc != null) {
                int i1 = entity.x - npc.x;
                int k1 = entity.y - npc.y;
                if (i1 != 0 || k1 != 0)
                    entity.nextStepOrientation =
                            (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
            }
        }
        if (entity.interactingEntity >= 32768) {
            int j = entity.interactingEntity - 32768;
            if (j == localPlayerIndex) {
                j = internalLocalPlayerIndex;
            }
            Player player = players[j];
            if (player != null) {
                int l1 = entity.x - player.x;
                int i2 = entity.y - player.y;
                if (l1 != 0 || i2 != 0) {
                    entity.nextStepOrientation =
                            (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
                }
            }
        }
        if ((entity.faceX != 0 || entity.faceY != 0) && (entity.remainingPath == 0 || entity.anInt1503 > 0)) {
            int k = entity.x - (entity.faceX - regionBaseX - regionBaseX) * 64;
            int j1 = entity.y - (entity.faceY - regionBaseY - regionBaseY) * 64;
            if (k != 0 || j1 != 0)
                entity.nextStepOrientation =
                        (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
            entity.faceX = 0;
            entity.faceY = 0;
        }
        int l = entity.nextStepOrientation - entity.orientation & 0x7ff;
        if (l != 0) {
            if (l < entity.degreesToTurn || l > 2048 - entity.degreesToTurn)
                entity.orientation = entity.nextStepOrientation;
            else if (l > 1024)
                entity.orientation -= entity.degreesToTurn;
            else
                entity.orientation += entity.degreesToTurn;
            entity.orientation &= 0x7ff;
            if (entity.movementAnimation == entity.idleAnimation
                    && entity.orientation != entity.nextStepOrientation) {
                if (entity.standTurnAnimIndex != -1) {
                    entity.movementAnimation = entity.standTurnAnimIndex;
                    return;
                }
                entity.movementAnimation = entity.walkAnimIndex;
            }
        }
    }

    private void drawGameScreen() {
        if (fullscreenInterfaceID != -1
                && (loadingStage == 2 || super.fullGameScreen != null)) {
            if (loadingStage == 2) {
                try {
                    processWidgetAnimations(tickDelta, fullscreenInterfaceID);
                    if (openInterfaceId != -1) {
                        processWidgetAnimations(tickDelta, openInterfaceId);
                    }
                } catch (Exception ex) {

                }
                tickDelta = 0;
                resetAllImageProducers();
                super.fullGameScreen.initDrawingArea();
                Rasterizer3D.scanOffsets = fullScreenTextureArray;
                Rasterizer2D.clear();
                welcomeScreenRaised = true;
                if (openInterfaceId != -1) {
                    Widget rsInterface_1 = Widget.interfaceCache[openInterfaceId];
                    if (rsInterface_1.width == 512 && rsInterface_1.height == 334
                            && rsInterface_1.type == 0) {
                        rsInterface_1.width = 765;
                        rsInterface_1.height = 503;
                    }
                    try {
                        drawInterface(0, 0, rsInterface_1, 8);
                    } catch (Exception ex) {

                    }
                }
                Widget rsInterface = Widget.interfaceCache[fullscreenInterfaceID];
                if (rsInterface.width == 512 && rsInterface.height == 334
                        && rsInterface.type == 0) {
                    rsInterface.width = 765;
                    rsInterface.height = 503;
                }
                try {
                    drawInterface(0, 0, rsInterface, 8);
                } catch (Exception ex) {

                }
                if (!menuOpen) {
                    processRightClick();
                    drawTooltip();
                } else {
                    drawMenu(frameMode == ScreenMode.FIXED ? 4 : 0,
                            frameMode == ScreenMode.FIXED ? 4 : 0);
                }
            }
            drawCount++;
            super.fullGameScreen.drawGraphics(0, super.graphics, 0);
            return;
        } else {
            if (drawCount != 0) {
                setupGameplayScreen();
            }
        }
        if (welcomeScreenRaised) {
            welcomeScreenRaised = false;
            if (frameMode == ScreenMode.FIXED) {
                topFrame.drawGraphics(0, super.graphics, 0);
                leftFrame.drawGraphics(4, super.graphics, 0);
            }
            updateChatbox = true;
            tabAreaAltered = true;
            if (loadingStage != 2) {
                if (frameMode == ScreenMode.FIXED) {
                    gameScreenImageProducer.drawGraphics(
                            frameMode == ScreenMode.FIXED ? 4 : 0, super.graphics,
                            frameMode == ScreenMode.FIXED ? 4 : 0);
                    minimapImageProducer.drawGraphics(0, super.graphics, 516);
                }
            }
        }
        if (overlayInterfaceId != -1) {
            try {
                processWidgetAnimations(tickDelta, overlayInterfaceId);
            } catch (Exception ex) {

            }
        }
        drawTabArea();
        if (backDialogueId == -1) {
            aClass9_1059.scrollPosition = scrollBarPosition - anInt1089 - 110;
            if (super.mouseX >= 496 && super.mouseX <= 511
                    && super.mouseY > (frameMode == ScreenMode.FIXED ? 345
                    : frameHeight - 158))
                method65(494, 110, super.mouseX,
                        super.mouseY - (frameMode == ScreenMode.FIXED ? 345
                                : frameHeight - 158),
                        aClass9_1059, 0, false, scrollBarPosition);
            int i = scrollBarPosition - 110 - aClass9_1059.scrollPosition;
            if (i < 0) {
                i = 0;
            }
            if (i > scrollBarPosition - 110) {
                i = scrollBarPosition - 110;
            }
            if (anInt1089 != i) {
                anInt1089 = i;
                updateChatbox = true;
            }
        }
        
        if (backDialogueId == -1 && chatboxDrawing == null) {
        	aClass9_1059.scrollPosition = scrollBarPosition - anInt1089 - 110;
			if (super.getMouseX() > 478 && super.getMouseX() < 580 && super.getMouseY() > (frameHeight - 161)) {
				method65(494, 110, super.getMouseX() - 0, super.getMouseY() - (frameHeight - 155), aClass9_1059, 0, false, scrollBarPosition);
			}
			int i = scrollBarPosition - 110 - aClass9_1059.scrollPosition;
			if (i < 0) {
				i = 0;
			}
			if (i > scrollBarPosition - 110) {
				i = scrollBarPosition - 110;
			}
			if (anInt1089 != i) {
				anInt1089 = i;
				updateChatbox = true;
			}
		}
		
        
        if (backDialogueId != -1) {
            boolean flag2 = false;

            try {
                flag2 = processWidgetAnimations(tickDelta, backDialogueId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (flag2) {
                updateChatbox = true;
            }
        }
        if (atInventoryInterfaceType == 3)
            updateChatbox = true;
        if (activeInterfaceType == 3)
            updateChatbox = true;
        if (clickToContinueString != null)
            updateChatbox = true;
        if (menuOpen && menuScreenArea == 2)
            updateChatbox = true;
        if (updateChatbox) {
            drawChatArea();
            updateChatbox = false;
        }
        if (loadingStage == 2)
            moveCameraWithPlayer();
        if (loadingStage == 2) {
            if (frameMode == ScreenMode.FIXED) {
                drawMinimap();
                minimapImageProducer.drawGraphics(0, super.graphics, 516);
            }
        }
        if (flashingSidebarId != -1)
            tabAreaAltered = true;
        if (tabAreaAltered) {
            if (flashingSidebarId != -1 && flashingSidebarId == tabId) {
                flashingSidebarId = -1;
                // flashing sidebar
				/*outgoing.writeOpcode(120);
				outgoing.writeByte(tabId);*/
            }
            tabAreaAltered = false;
            chatSettingImageProducer.initDrawingArea();
            gameScreenImageProducer.initDrawingArea();
        }
        tickDelta = 0;
    }

    private boolean buildFriendsListMenu(Widget class9) {
        int i = class9.contentType;
        if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
            if (i >= 801)
                i -= 701;
            else if (i >= 701)
                i -= 601;
            else if (i >= 101)
                i -= 101;
            else
                i--;
            menuActionText[menuActionRow] = "Remove @whi@" + friendsList[i];
            menuActionTypes[menuActionRow] = 792;
            menuActionRow++;
            menuActionText[menuActionRow] = "Message @whi@" + friendsList[i];
            menuActionTypes[menuActionRow] = 639;
            menuActionRow++;
            return true;
        }
        if (i >= 401 && i <= 500) {
            menuActionText[menuActionRow] = "Remove @whi@" + class9.defaultText;
            menuActionTypes[menuActionRow] = 322;
            menuActionRow++;
            return true;
        } else {
            return false;
        }
    }

    private void createStationaryGraphics() {
        AnimableObject class30_sub2_sub4_sub3 =
                (AnimableObject) incompleteAnimables.reverseGetFirst();
        for (; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 =
                (AnimableObject) incompleteAnimables.reverseGetNext())
            if (class30_sub2_sub4_sub3.anInt1560 != plane
                    || class30_sub2_sub4_sub3.aBoolean1567)
                class30_sub2_sub4_sub3.unlink();
            else if (tick >= class30_sub2_sub4_sub3.anInt1564) {
                class30_sub2_sub4_sub3.method454(tickDelta);
                if (class30_sub2_sub4_sub3.aBoolean1567)
                    class30_sub2_sub4_sub3.unlink();
                else
                    scene.addAnimableA(class30_sub2_sub4_sub3.anInt1560, 0,
                            class30_sub2_sub4_sub3.anInt1563, -1,
                            class30_sub2_sub4_sub3.anInt1562, 60,
                            class30_sub2_sub4_sub3.anInt1561, class30_sub2_sub4_sub3,
                            false);
            }

    }

    public void drawBlackBox(int xPos, int yPos) {
        Rasterizer2D.drawBox(xPos - 2, yPos - 1, 1, 71, 0x726451);
        Rasterizer2D.drawBox(xPos + 174, yPos, 1, 69, 0x726451);
        Rasterizer2D.drawBox(xPos - 2, yPos - 2, 178, 1, 0x726451);
        Rasterizer2D.drawBox(xPos, yPos + 68, 174, 1, 0x726451);
        Rasterizer2D.drawBox(xPos - 1, yPos - 1, 1, 71, 0x2E2B23);
        Rasterizer2D.drawBox(xPos + 175, yPos - 1, 1, 71, 0x2E2B23);
        Rasterizer2D.drawBox(xPos, yPos - 1, 175, 1, 0x2E2B23);
        Rasterizer2D.drawBox(xPos, yPos + 69, 175, 1, 0x2E2B23);
        Rasterizer2D.drawTransparentBox(xPos, yPos, 174, 68, 0, 220);
    }

	public void drawSpellTooltip(int x, int y, int width, int height, int alpha) {
		Rasterizer2D.drawTransparentBox(	   x,     y,     width,     height,     0x000000, alpha);
		Rasterizer2D.drawTransparentBoxOutline(x + 1, y + 1, width - 2, height - 1, 0x2E2B23, 255);
		Rasterizer2D.drawTransparentBoxOutline(x,     y,     width - 1, height - 1, 0x726451, 255);
	}
	
	private int new_y = 0;
    
    private void drawInterface(int scroll_y, int x, Widget rsInterface, int y) {
        if (rsInterface == null)
            return;
        if (rsInterface.type != 0 || rsInterface.children == null)
            return;
        if (rsInterface.invisible && anInt1026 != rsInterface.id && anInt1048 != rsInterface.id
                && anInt1039 != rsInterface.id || rsInterface.hidden) {
            return;
        }
       

        int clipLeft = Rasterizer2D.leftX;
        int clipTop = Rasterizer2D.topY;
        int clipRight = Rasterizer2D.bottomX;
        int clipBottom = Rasterizer2D.bottomY;

        Rasterizer2D.setDrawingArea(y + rsInterface.height, x, x + rsInterface.width, y);
        int childCount = rsInterface.children.length;

        if (rsInterface.id == 31000) {
            processSpawnTab();
        }
        


        for (int childId = 0; childId < childCount; childId++) {
            int _x = rsInterface.childX[childId] + x;
            int _y = (rsInterface.childY[childId] + y) - scroll_y;
            Widget childInterface = Widget.interfaceCache[rsInterface.children[childId]];

            if (childInterface == null) {
                continue;
            }

            if (childInterface.hidden) {
                continue;
            }
            
            if (childInterface.id == 27656) {
            	long totalExp = 0;
        		for (int i = 0; i < 23; i++) {
        			totalExp += currentExp[i];
        		}
        		childInterface.defaultText = "Total XP: " + NumberFormat.getInstance().format(totalExp);
            }
            
            // Handle black box hovers in magic spellbook
          //  boolean drawBlackBox = false;
            if (tabId == 6) {
//            	for (SortableSpellbook spellbook : SortableSpellbook.values()) {
//            		if (tabInterfaceIDs[6] == spellbook.interfaceId) {
//            			int[][] spellData = spellbook.defaultSort;
//            			for (int[] spell : spellData) {
//            				int hoverId = Widget.interfaceCache[spell[0]].hoverType;
//            				if (childInterface.id == hoverId) {
//            					// Reposition black box hover
//            					for (int i = 0; i < rsInterface.children.length; i++) {
//            						if (rsInterface.children[i] == spell[0]) {
//            							int pos = ((rsInterface.childY[i] + y) - scroll_y);
//            							currentY = y + 165;
//            							int max = frameHeight - (frameMode == ScreenMode.FIXED ? 330 : 115);
//            							if (pos > max) {
//            								currentY = y;
//            							}
//            							break;
//            						}
//            					}
//            					break;
//            				}/* else if (childInterface.id == hoverId + 1) {
//            					drawBlackBox = true;
//            					break;
//            				}*/
//            			}                			
//        				break;
//        			}
//            	}
            }

            _x += childInterface.horizontalOffset;
            _y += childInterface.verticalOffset;
            if (childInterface.contentType > 0)
                drawFriendsListOrWelcomeScreen(childInterface);
            
            for (int r = 0; r < runeChildren.length; r++) {
                if (childInterface.id == runeChildren[r]) {
                    childInterface.modelZoom = 775;
                }
            }
            int[] ids = {
        		60001,
        		60014,
        		60027,
        		60040,
        		60053,
        		60066,
        		60079,
        		60092,
        		60105,
        		60118,
        		60131,
        		60144,
        		60157,
        		60170,
        		60183,
        		60196,
        		60209,
        		60222,
        		60235,
        		60248,
        		60261,
        		60274,
        		60287,
        		60300,
        		60313,
        		60326,
        		60339,
        		60352,
        		60365,
        		60378,
        		60391,
        		60404,
        		60417,
        		60430,
        		60443,
        		60456,
        		60469,
        		60482,
        		60495,
        		60508,
        		60521,
        		60534,
        		60547,
        		60560,
        		60573,
        		60586,
        		60599,
        		60612,
        		60625,
        		60638,
        		60651,
        		60664,
        		60677,
        		60690,
        		60703,
        		60716,
        		60729,
        		60742,
        		60755,
        		60768,
        		60781,
        		60794,
        		60807,
        		60820,
        		60833,
        		60846,
        		//END STANDARD
        		61001,
        		61014,
        		61027,
        		61040,
        		61053,
        		61066,
        		61079,
        		61092,
        		61105,
        		61118,
        		61131,
        		61144,
        		61157,
        		61170,
        		61183,
        		61196,
        		61209,
        		61222,
        		61235,
        		61248,
        		61261,
        		61274,
        		61287,
        		61300,
        		61313,
        		61326,
        		//END ANCIENT
        		62001,
        		62014,
        		62027,
        		62040,
        		62053,
        		62066,
        		62079,
        		62092,
        		62105,
        		62118,
        		62131,
        		62144,
        		62157,
        		62170,
        		62183,
        		62196,
        		62209,
        		62222,
        		62235,
        		62248,
        		62261,
        		62274,
        		62287,
        		62300,
        		62313,
        		62326,
        		62339,
        		62352,
        		62365,
        		62378,
        		62391,
        		62404,
        		62417,
        		62430,
        		62443,
        		62456,
        		62469,
        		62482,
        		62495,
        		62508,
        		62521,
        		62534,
        		62547,
        		62560,
        		62573,
        		//END LUNAR
        		63001,
        		63014,
        		63027,
        		63040,
        		63053,
        		63066,
        		63079,
        		63092,
        		63105,
        		63118,
        		63131,
        		63144,
        		63157,
        		63170,
        		63183,
        		63196,
        		63209,
        		63222,
        		63235,
        		63248,
        		63261,
        		63274,
        		63287,
        		63300,
        		63313,
        		63326,
        		63339,
        		63352,
        		63365,
        		63378,
        		63391,
        		63404,
        		63417,
        		63430,
        		63443
            };
            for (int i = 0; i < ids.length; i++) {
	            if (childInterface.id == ids[i]+1) {// && !childInterface.invisible) {
	            	int draw_y = _y;
	            	new_y++;
	            	if (new_y > 800)
						new_y = 0;
	            	if(draw_y > 50)
	            		draw_y = (250 + 37) - Widget.interfaceCache[ids[i]].height;
	            	drawSpellTooltip(35, draw_y, Widget.interfaceCache[ids[i]].width, Widget.interfaceCache[ids[i]].height, 220);	
	            }
            }
            
            if (childInterface.type == Widget.TYPE_CONTAINER) {
                if (childInterface.scrollPosition > childInterface.scrollMax - childInterface.height)
                    childInterface.scrollPosition = childInterface.scrollMax - childInterface.height;
                if (childInterface.scrollPosition < 0)
                    childInterface.scrollPosition = 0;
                drawInterface(childInterface.scrollPosition, _x, childInterface, _y);
                if (childInterface.scrollMax > childInterface.height)
                    drawScrollbar(childInterface.height, childInterface.scrollPosition, _y, _x + childInterface.width, childInterface.scrollMax, false);
            } else if (childInterface.type != 1)

                if (childInterface.type == Widget.TYPE_INVENTORY) {
                    boolean drawnBank = false;
                    int item = 0;

                    for (int row = 0; row < childInterface.height; row++) {
                        for (int column = 0; column < childInterface.width; column++) {

                            int tileX = _x + column * (32 + childInterface.spritePaddingX);
                            int tileY = _y + row * (32 + childInterface.spritePaddingY);
                            if (item < 20) {
                                tileX += childInterface.spritesX[item];
                                tileY += childInterface.spritesY[item];
                            }

                            if (item < childInterface.inventoryItemId.length && childInterface.inventoryItemId[item] > 0) {
                                int dragOffsetX = 0;
                                int dragOffsetY = 0;
                                int bindX = 0;
                                int bindY = 0;
								int itemId = (childInterface.itemId != -1 ? childInterface.inventoryItemId[item] : childInterface.inventoryItemId[item] - 1);

                                if (tileX > Rasterizer2D.leftX - 32 && tileX < Rasterizer2D.bottomX && tileY > Rasterizer2D.topY - 32 && tileY < Rasterizer2D.bottomY
                                        || activeInterfaceType != 0 && anInt1085 == item) {
                                    int outlineColour = 0;
                                    if (itemSelected == 1 && anInt1283 == item && anInt1284 == childInterface.id)
                                        outlineColour = 0xffffff;
                                    int itemOpacity = 256;
                                    if (childInterface.parent == 5382) {
                                        if (childInterface.inventoryAmounts[item] == 0) {
                                            itemOpacity = 95;
                                        }
                                    }
                                    Sprite item_icon;
                                    if (Bank.isBankContainer(childInterface.id) || (childInterface.id >= 11021 && childInterface.id <= 11045) || (childInterface.id >= 12000 && childInterface.id <= 12056))
                                    	item_icon = ItemDefinition.getSprite(itemId-1, childInterface.inventoryAmounts[item], outlineColour);
                                    else
                                    	item_icon = ItemDefinition.getSprite(itemId, childInterface.inventoryAmounts[item], outlineColour);

                                    if (item_icon != null) {
                                        if (activeInterfaceType != 0 && anInt1085 == item && anInt1084 == childInterface.id) {

                                            dragOffsetX = super.mouseX - anInt1087;
                                            dragOffsetY = super.mouseY - anInt1088;

                                            if (dragOffsetX < 5 && dragOffsetX > -5)
                                                dragOffsetX = 0;
                                            if (dragOffsetY < 5 && dragOffsetY > -5)
                                                dragOffsetY = 0;
                                            if (dragItemDelay < 10) {
                                                dragOffsetX = 0;
                                                dragOffsetY = 0;
                                            }

                                            bindX = tileX + dragOffsetX;
                                            if (bindX < Rasterizer2D.leftX) {
                                                bindX = Rasterizer2D.leftX - (dragOffsetX);
                                                if (dragOffsetX < Rasterizer2D.leftX)
                                                    bindX = Rasterizer2D.leftX;
                                            }
                                            if (bindX > Rasterizer2D.bottomX - 32) {
                                                bindX = Rasterizer2D.bottomX - 32;
                                            }

                                            bindY = tileY + dragOffsetY;
                                            if (bindY < Rasterizer2D.topY && rsInterface.scrollPosition == 0) {
                                                bindY = Rasterizer2D.topY - (dragOffsetY);
                                                if (dragOffsetY < Rasterizer2D.topY)
                                                    bindY = Rasterizer2D.topY;
                                            }
                                            if (bindY > Rasterizer2D.bottomY - 32)
                                                bindY = Rasterizer2D.bottomY - 32;

                                            if (tileY + dragOffsetY < Rasterizer2D.topY && rsInterface.scrollPosition > 0) {
                                                int notch = (tickDelta * (Rasterizer2D.topY - tileY - dragOffsetY)) / 3;
                                                if (notch > tickDelta * 10)
                                                    notch = tickDelta * 10;

                                                if (notch > rsInterface.scrollPosition)
                                                    notch = rsInterface.scrollPosition;

                                                rsInterface.scrollPosition -= notch;
                                                anInt1088 += notch;
                                                bindY = Rasterizer2D.topY;
                                            }

                                            if (tileY + dragOffsetY + 32 > Rasterizer2D.bottomY && rsInterface.scrollPosition < rsInterface.scrollMax - rsInterface.height) {
                                                int notch = (tickDelta * ((tileY + dragOffsetY + 32) - Rasterizer2D.bottomY)) / 3;
                                                if (notch > tickDelta * 10)
                                                    notch = tickDelta * 10;

                                                if (notch > rsInterface.scrollMax - rsInterface.height - rsInterface.scrollPosition)
                                                    notch = rsInterface.scrollMax - rsInterface.height - rsInterface.scrollPosition;

                                                rsInterface.scrollPosition += notch;
                                                anInt1088 -= notch;
                                            }
                                            item_icon.drawSprite1(bindX, bindY);
                                        } else if (atInventoryInterfaceType != 0 && atInventoryIndex == item && atInventoryInterface == childInterface.id) {
                                            bindX = tileX + dragOffsetX;
                                            bindY = tileY;
                                            item_icon.drawSprite1(tileX, tileY);
                                        } else {
                                            bindX = tileX + dragOffsetX;
                                            bindY = tileY;
                                            if (itemOpacity != 256) {
                                                item_icon.drawTransparentSprite(tileX, tileY, itemOpacity);
                                            } else {
                                                item_icon.drawSprite(tileX, tileY);
                                            }
                                        }
                                        if (item_icon.maxWidth == 33 || childInterface.inventoryAmounts[item] != 1) {

                                            boolean flag = true;
                                            if ((childInterface.id >= 22035 && childInterface.id <= 22043) || childInterface.id == 41042 || childInterface.id == 41107) {
                                                flag = false;
                                            }

                                            if (flag) {

                                                int k10 = childInterface.inventoryAmounts[item];

                                                if (k10 >= 1500000000 && childInterface.drawInfinity) {
                                                	spriteCache.lookup(105).drawSprite(tileX, tileY);
                                                } else if (k10 == 0) { // Placeholder text
                                                    newSmallFont.setTrans(1, 0xFFFF00, 120);
                                                    newSmallFont.drawBasicString(intToKOrMil(k10), bindX, bindY + 9);
                                                } else {
                                                    smallText.render(0, intToKOrMil(k10), bindY + 10, bindX + 1); // Shadow
                                                    if (k10 >= 1)
                                                        smallText.render(0xFFFF00, intToKOrMil(k10), bindY + 9, bindX);
                                                    if (k10 >= 100000)
                                                        smallText.render(0xFFFFFF, intToKOrMil(k10), bindY + 9, bindX);
                                                    if (k10 >= 10000000)
                                                        smallText.render(0x00FF80, intToKOrMil(k10), bindY + 9, bindX);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (childInterface.sprites != null && item < 20) {
                                Sprite image = childInterface.sprites[item];
                                if (image != null)
                                    image.drawSprite(tileX, tileY);
                            }
                            item++;
                            // Drawing tab number etc in main bank interface
                            if (childInterface.parent == 5382 && !drawnBank) {
                                for (int tabId = 0; tabId < Bank.CONTAINERS.length; tabId++) {
                                    if (childInterface.id == Bank.CONTAINERS[tabId]) {
                                        if (Bank.currentBankTab == 0) {
                                            Rasterizer2D.drawHorizontalLine((tileX - 10), (tileY - 8), 434, 0x73654a);
                                            smallText.drawText(0xefa142, "Tab " + Integer.toString(tabId + 1),
                                                    tileY - 9, tileX + 4);
                                            drawnBank = true;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                } else if (childInterface.type == Widget.TYPE_RECTANGLE) {
                    boolean hover = false;
                    if (anInt1039 == childInterface.id || anInt1048 == childInterface.id
                            || anInt1026 == childInterface.id)
                        hover = true;
                    int colour;
                    if (interfaceIsSelected(childInterface)) {
                        colour = childInterface.secondaryColor;
                        if (hover && childInterface.secondaryHoverColor != 0)
                            colour = childInterface.secondaryHoverColor;
                    } else {
                        colour = childInterface.textColor;
                        if (hover && childInterface.defaultHoverColor != 0)
                            colour = childInterface.defaultHoverColor;
                    }

                    if (childInterface.opacity == 0) {
                        if (childInterface.filled)
                            Rasterizer2D.drawBox(_x, _y, childInterface.width, childInterface.height, colour
                            );
                        else
                            Rasterizer2D.drawBoxOutline(_x, _y, childInterface.width,
                                    childInterface.height, colour);
                    } else if (childInterface.filled)
                        Rasterizer2D.drawTransparentBox(_x, _y, childInterface.width, childInterface.height, colour,
                                256 - (childInterface.opacity & 0xff));
                    else
                        Rasterizer2D.drawTransparentBoxOutline(_x, _y, childInterface.width, childInterface.height,
                                colour, 256 - (childInterface.opacity & 0xff)
                        );
                } else if (childInterface.type == Widget.TYPE_TEXT) {
                    GameFont textDrawingArea = childInterface.textDrawingAreas;
                    String text = childInterface.defaultText;
                    if (text == null) {
                        continue;
                    }

                    boolean flag1 = false;
                    if (anInt1039 == childInterface.id || anInt1048 == childInterface.id || anInt1026 == childInterface.id)
                        flag1 = true;
                    int colour;
                    if (interfaceIsSelected(childInterface)) {
                        colour = childInterface.secondaryColor;
                        if (flag1 && childInterface.secondaryHoverColor != 0)
                            colour = childInterface.secondaryHoverColor;
                        if (childInterface.secondaryText.length() > 0)
                            text = childInterface.secondaryText;
                    } else {
                        colour = childInterface.textColor;
                        if (flag1 && childInterface.defaultHoverColor != 0)
                            colour = childInterface.defaultHoverColor;
                    }
                    if (childInterface.optionType == Widget.OPTION_CONTINUE && continuedDialogue) {
                        text = "Please wait...";
                        colour = childInterface.textColor;
                    }
                    if (Rasterizer2D.width == 519) {
                        if (colour == 0xffff00)
                            colour = 255;
                        if (colour == 49152)
                            colour = 0xffffff;
                    }
                    if (frameMode != ScreenMode.FIXED) {
                        if ((backDialogueId != -1 || dialogueId != -1
                                || childInterface.defaultText
                                .contains("Click here to continue"))
                                && (rsInterface.id == backDialogueId
                                || rsInterface.id == dialogueId)) {
                            if (colour == 0xffff00) {
                                colour = 255;
                            }
                            if (colour == 49152) {
                                colour = 0xffffff;
                            }
                        }
                    }
                    if ((childInterface.parent == 1151) || (childInterface.parent == 12855)) {
                        switch (colour) {
                            case 16773120:
                                colour = 0xFE981F;
                                break;
                            case 7040819:
                                colour = 0xAF6A1A;
                                break;
                        }
                    }

                    int image = -1;
                    final String INITIAL_MESSAGE = text;
                    if (text.contains("<img=")) {
                        int prefix = text.indexOf("<img=");
                        int suffix = text.indexOf(">");
                        try {
                            image = Integer.parseInt(text.substring(prefix + 5, suffix));
                            text = text.replaceAll(text.substring(prefix + 5, suffix), "");
                            text = text.replaceAll("</img>", "");
                            text = text.replaceAll("<img=>", "");
                        } catch (NumberFormatException nfe) {
                            //System.out.println("Unable to draw player crown on interface. Unable to read rights.");
                            text = INITIAL_MESSAGE;
                        } catch (IllegalStateException ise) {
                            //System.out.println("Unable to draw player crown on interface, rights too low or high.");
                            text = INITIAL_MESSAGE;
                        }
                        if (suffix > prefix) {
                            //_x += 14;
                        }
                    }

                    for (int drawY = _y + textDrawingArea.verticalSpace; text.length() > 0; drawY +=
                            textDrawingArea.verticalSpace) {

                        if (image != -1) {

                            //CLAN CHAT LIST = 37128
                            if (childInterface.parent == 37128) {
                                spriteCache.lookup(image).drawAdvancedSprite(_x, drawY - spriteCache.lookup(image).myHeight - 1);
                                _x += spriteCache.lookup(image).myWidth + 3;
                            } else {
                                spriteCache.lookup(image).drawAdvancedSprite(_x, drawY - spriteCache.lookup(image).myHeight + 3);
                                _x += spriteCache.lookup(image).myWidth + 4;
                            }
                        }

                        if (text.indexOf("%") != -1) {
                            do {
                                int index = text.indexOf("%1");
                                if (index == -1)
                                    break;
                                if (childInterface.id < 4000 || childInterface.id > 5000
                                        && childInterface.id != 13921
                                        && childInterface.id != 13922
                                        && childInterface.id != 12171
                                        && childInterface.id != 12172) {
                                    text = text.substring(0, index)
                                            + formatCoins(executeScript(
                                            childInterface, 0))
                                            + text.substring(index + 2);

                                } else {
                                    text = text.substring(0, index) + interfaceIntToString(executeScript(childInterface, 0))
                                            + text.substring(index + 2);

                                }
                            } while (true);
                            do {
                                int index = text.indexOf("%2");
                                if (index == -1) {
                                    break;
                                }
                                text = text.substring(0, index)
                                        + interfaceIntToString(executeScript(
                                        childInterface, 1))
                                        + text.substring(index + 2);
                            } while (true);
                            do {
                                int index = text.indexOf("%3");

                                if (index == -1) {
                                    break;
                                }

                                text = text.substring(0, index)
                                        + interfaceIntToString(executeScript(
                                        childInterface, 2))
                                        + text.substring(index + 2);
                            } while (true);
                            do {
                                int index = text.indexOf("%4");

                                if (index == -1) {
                                    break;
                                }
                                text = text.substring(0, index)
                                        + interfaceIntToString(executeScript(
                                        childInterface, 3))
                                        + text.substring(index + 2);
                            } while (true);
                            do {
                                int index = text.indexOf("%5");

                                if (index == -1) {
                                    break;
                                }

                                text = text.substring(0, index)
                                        + interfaceIntToString(executeScript(
                                        childInterface, 4))
                                        + text.substring(index + 2);
                            } while (true);
                        }

                        int line = text.indexOf("\\n");
                        int line2 = text.indexOf("<br>");

                        String drawn;

                        if (line != -1 || line2 != -1) {
							drawn = text.substring(0, (line2 != -1 ? line2 : line));
							text = text.substring((line2 != -1 ? line2 + 4 : line + 2));
                        } else {
                            drawn = text;
                            text = "";
                        }
                        RSFont font = null;
                        if (textDrawingArea == smallText) {
                            font = newSmallFont;
                        } else if (textDrawingArea == regularText) {
                            font = newRegularFont;
                        } else if (textDrawingArea == boldText) {
                            font = newBoldFont;
                        } else if (textDrawingArea == gameFont) {
                            font = newFancyFont;
                        }
						if (font != null) {
							if (childInterface.centerText) {
								font.drawCenteredString(drawn, _x + childInterface.width / 2, drawY, colour,
										childInterface.textShadow ? 0 : -1);
							} else if (childInterface.rightAlignedText) {
								font.drawRightAlignedString(drawn, _x, drawY, colour,
										childInterface.textShadow ? 0 : -1);
							} else if (childInterface.rollingText) {
								font.drawRollingText(drawn, _x, drawY, colour, childInterface.textShadow ? 0 : -1);
							} else {
								font.drawBasicString(drawn, _x, drawY, colour, childInterface.textShadow ? 0 : -1);
							}
						}
                    }
                } else if (childInterface.type == Widget.TYPE_SPRITE) {

                    Sprite sprite;


                    
                    if (childInterface.spriteXOffset != 0) {
                        _x += childInterface.spriteXOffset;
                    }

                    if (childInterface.spriteYOffset != 0) {
                        _y += childInterface.spriteYOffset;
                    }

                    if (interfaceIsSelected(childInterface)) {
                        sprite = childInterface.enabledSprite;
                    } else {
                        sprite = childInterface.disabledSprite;
                    }
                    if (spellSelected == 1 && childInterface.id == spellId && spellId != 0
                            && sprite != null) {
                        sprite.drawSprite(_x, _y, 0xffffff);
                    } else {
                        if (sprite != null) {

                            boolean drawTransparent = childInterface.drawsTransparent;

                            //Check if parent draws as transparent..
                            if (!drawTransparent && childInterface.parent > 0 &&
                                    Widget.interfaceCache[childInterface.parent] != null) {
                                drawTransparent = Widget.interfaceCache[childInterface.parent].drawsTransparent;
                            }

                            if (drawTransparent) {
                                sprite.drawTransparentSprite(_x, _y, childInterface.transparency);
                            } else {
                                sprite.drawSprite(_x, _y);
                                if(childInterface.newtooltip) {
                                	sprite.drawTooltip(_x, _y, sprite, childInterface.newtooltiptext, 0xFFFFFF);
                                }
                            }
                        }
                    }
                    if (autocast && childInterface.id == autoCastId)
                        spriteCache.lookup(43).drawSprite(_x - 2, _y - 1);
                } else if (childInterface.type == Widget.TYPE_HD_SPRITE) {

                    Sprite sprite;

                    if (childInterface.spriteXOffset != 0) {
                        _x += childInterface.spriteXOffset;
                    }

                    if (childInterface.spriteYOffset != 0) {
                        _y += childInterface.spriteYOffset;
                    }

                    if (interfaceIsSelected(childInterface)) {
                        sprite = childInterface.enabledSprite;
                    } else {
                        sprite = childInterface.disabledSprite;
                    }

                    if (spellSelected == 1 && childInterface.id == spellId && spellId != 0
                            && sprite != null) {
                        sprite.drawAdvancedSprite(_x, _y, 0xffffff);
                    } else {
                        if (sprite != null) {

                            boolean drawTransparent = childInterface.drawsTransparent;

                            //Check if parent draws as transparent..
                            if (!drawTransparent && childInterface.parent > 0 &&
                                    Widget.interfaceCache[childInterface.parent] != null) {
                                drawTransparent = Widget.interfaceCache[childInterface.parent].drawsTransparent;
                            }

                            if (drawTransparent) {
                                sprite.drawTransparentSprite(_x, _y, childInterface.transparency);
                            } else {
                            	sprite.drawSprite(_x, _y);
                            }
                        }
                    }
                    if (autocast && childInterface.id == autoCastId)
                    	spriteCache.lookup(43).drawAdvancedSprite(_x - 2, _y - 1);
                } else if (childInterface.type == 543534) {

                    Sprite sprite;

                    if (childInterface.spriteXOffset != 0) {
                        _x += childInterface.spriteXOffset;
                    }

                    if (childInterface.spriteYOffset != 0) {
                        _y += childInterface.spriteYOffset;
                    }

                    if (interfaceIsSelected(childInterface)) {
                        sprite = childInterface.enabledSprite;
                    } else {
                        sprite = childInterface.disabledSprite;
                    }

                    if (spellSelected == 1 && childInterface.id == spellId && spellId != 0
                            && sprite != null) {
                        sprite.drawAdvancedSprite(_x, _y, 0xffffff);
                    } else {
                        if (sprite != null) {

                            boolean drawTransparent = childInterface.drawsTransparent;

                            //Check if parent draws as transparent..
                            if (!drawTransparent && childInterface.parent > 0 &&
                                    Widget.interfaceCache[childInterface.parent] != null) {
                                drawTransparent = Widget.interfaceCache[childInterface.parent].drawsTransparent;
                            }

                            if (drawTransparent) {
                                sprite.drawTransparentSprite(_x, _y, childInterface.transparency);
                            } else {
                            	sprite.drawHoverSprite(_x, _y,spriteCache.lookup(childInterface.hoversprite));
                            }
                        }
                    }
                    if (autocast && childInterface.id == autoCastId)
                    	spriteCache.lookup(43).drawAdvancedSprite(_x - 2, _y - 1);
                } else if (childInterface.type == Widget.TYPE_MODEL) {
                    int centreX = Rasterizer3D.originViewX;
                    int centreY = Rasterizer3D.originViewY;
                    Rasterizer3D.originViewX = _x + childInterface.width / 2;
                    Rasterizer3D.originViewY = _y + childInterface.height / 2;
                    int sine = Rasterizer3D.anIntArray1470[childInterface.modelRotation1] * childInterface.modelZoom >> 16;
                    int cosine = Rasterizer3D.COSINE[childInterface.modelRotation1] * childInterface.modelZoom >> 16;
                    boolean selected = interfaceIsSelected(childInterface);
                    int emoteAnimation;
                    if (selected)
                        emoteAnimation = childInterface.secondaryAnimationId;
                    else
                        emoteAnimation = childInterface.defaultAnimationId;
                    Model model;
                    if (emoteAnimation == -1) {
                        model = childInterface.method209(-1, -1, selected);
                    } else {
                        Animation animation = Animation.animations[emoteAnimation];
                        model = childInterface.method209(
                                animation.secondaryFrames[childInterface.currentFrame],
                                animation.primaryFrames[childInterface.currentFrame],
                                selected);
                    }
                        if (model != null)
                            model.method482(childInterface.modelRotation2, 0, childInterface.modelRotation1, 0, sine, cosine);
                    Rasterizer3D.originViewX = centreX;
                    Rasterizer3D.originViewY = centreY;
                } else if (childInterface.type == Widget.TYPE_ITEM_LIST) {
                    GameFont font = childInterface.textDrawingAreas;
                    int slot = 0;
                    for (int row = 0; row < childInterface.height; row++) {
                        for (int column = 0; column < childInterface.width; column++) {
                            if (childInterface.inventoryItemId[slot] > 0) {
                                ItemDefinition item = ItemDefinition
                                        .lookup(childInterface.inventoryItemId[slot]
                                                - 1);
                                String name = item.name;
                                if (item.stackable
                                        || childInterface.inventoryAmounts[slot] != 1)
                                    name = name + " x" + intToKOrMilLongName(
                                            childInterface.inventoryAmounts[slot]);
                                int __x = _x + column
                                        * (115 + childInterface.spritePaddingX);
                                int __y = _y + row
                                        * (12 + childInterface.spritePaddingY);
                                if (childInterface.centerText)
                                    font.method382(childInterface.textColor,
                                            __x + childInterface.width / 2,
                                            name, __y,
                                            childInterface.textShadow);
                                else
                                    font.drawTextWithPotentialShadow(
                                            childInterface.textShadow, __x,
                                            childInterface.textColor, name,
                                            __y);
                            }
                            slot++;
                        }
                    }
                } else if (childInterface.type == Widget.TYPE_OTHER
                        && (anInt1500 == childInterface.id
                        || anInt1044 == childInterface.id
                        || anInt1129 == childInterface.id)
                        && anInt1501 == 0 && !menuOpen) {

                    if (childInterface.hoverXOffset != 0) {
                        _x += childInterface.hoverXOffset;
                    }

                    if (childInterface.hoverYOffset != 0) {
                        _y += childInterface.hoverYOffset;
                    }


                    if (childInterface.regularHoverBox) {
                        drawHoverBox(_x, _y, childInterface.defaultText);

                    } else {
                        int boxWidth = 0;
                        int boxHeight = 0;
                        
						
                        String message = childInterface.defaultText;
    					if (childInterface.parent == 3917) {
    						String[] msg = message.split("\\\\n");
    						if (executeScript(childInterface, 1) >= 13_034_431) {
    							message = msg[0];
    						}
    					}
                        GameFont font = regularText;
                        for (String text = message; text.length() > 0; ) {
                            if (text.indexOf("%") != -1) {
                                do {
                                    int index = text.indexOf("%1");
                                    if (index == -1)
                                        break;
                                    text = text.substring(0, index)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 0))
                                            + text.substring(index + 2);
                                } while (true);
                                do {
                                    int index = text.indexOf("%2");
                                    if (index == -1)
                                        break;
                                    text = text.substring(0, index)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 1))
                                            + text.substring(index + 2);
                                } while (true);
                                do {
                                    int index = text.indexOf("%3");
                                    if (index == -1)
                                        break;
                                    text = text.substring(0, index)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 2))
                                            + text.substring(index + 2);
                                } while (true);
                                do {
                                    int index = text.indexOf("%4");
                                    if (index == -1)
                                        break;
                                    text = text.substring(0, index)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 3))
                                            + text.substring(index + 2);
                                } while (true);
                                do {
                                    int index = text.indexOf("%5");
                                    if (index == -1)
                                        break;
                                    text = text.substring(0, index)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 4))
                                            + text.substring(index + 2);
                                } while (true);
                                do {
    								int l7 = text.indexOf("%6");
    								if (l7 == -1)
    									break;
    								text = text.substring(0, l7)
    										+ NumberFormat.getIntegerInstance().format(executeScript(childInterface, 0) - executeScript(childInterface, 1))
    										+ text.substring(l7 + 2);
    							}
    							while (true);
                            }
                            int line1 = text.indexOf("\\n");
    						int line2 = text.indexOf("<br>");
                            String drawn;
                            if (line1 != -1 || line2 != -1) {
    							drawn = text.substring(0, (line2 != -1 ? line2 : line1));
    							text = text.substring((line2 != -1 ? line2 + 4 : line1 + 2));
                            } else {
                                drawn = text;
                                text = "";
                            }
                            int j10 = font.getTextWidth(drawn);
                            if (j10 > boxWidth) {
                                boxWidth = j10;
                            }
                            boxHeight += font.verticalSpace + 1;
                        }
                        boxWidth += 6;
                        boxHeight += 7;

                        int xPos = (_x + childInterface.width) - 5 - boxWidth;
                        int yPos = _y + childInterface.height + 5;
                        if (xPos < _x + 5) {
                            xPos = _x + 5;
                        }

                        if (xPos + boxWidth > x + rsInterface.width) {
                            xPos = (x + rsInterface.width) - boxWidth;
                        }
                        if (yPos + boxHeight > y + rsInterface.height) {
                            yPos = (_y - boxHeight);
                        }

                        String s2 = message;

                        Rasterizer2D.drawBox(xPos, yPos, boxWidth, boxHeight, 0xFFFFA0);
                        Rasterizer2D.drawBoxOutline(xPos, yPos, boxWidth, boxHeight, 0);

                        //Script hovers here

                        for (int j11 = yPos + font.verticalSpace + 2; s2.length() > 0; j11 +=
                                font.verticalSpace + 1) {// verticalSpace
                            if (s2.indexOf("%") != -1) {

                                do {
                                    int k7 = s2.indexOf("%1");
                                    if (k7 == -1)
                                        break;
                                    s2 = s2.substring(0, k7)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 0))
                                            + s2.substring(k7 + 2);
                                } while (true);

                                do {
                                    int l7 = s2.indexOf("%2");
                                    if (l7 == -1)
                                        break;
                                    s2 = s2.substring(0, l7)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 1))
                                            + s2.substring(l7 + 2);
                                } while (true);
                                do {
                                    int i8 = s2.indexOf("%3");
                                    if (i8 == -1)
                                        break;
                                    s2 = s2.substring(0, i8)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 2))
                                            + s2.substring(i8 + 2);
                                } while (true);
                                do {
                                    int j8 = s2.indexOf("%4");
                                    if (j8 == -1)
                                        break;
                                    s2 = s2.substring(0, j8)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 3))
                                            + s2.substring(j8 + 2);
                                } while (true);
                                do {
                                    int k8 = s2.indexOf("%5");
                                    if (k8 == -1)
                                        break;
                                    s2 = s2.substring(0, k8)
                                            + interfaceIntToString(executeScript(
                                            childInterface, 4))
                                            + s2.substring(k8 + 2);
                                } while (true);
    							do {
    								int k8 = s2.indexOf("%6");
    								if (k8 == -1)
    									break;
    								s2 = s2.substring(0, k8)
    										+ NumberFormat.getIntegerInstance().format(executeScript(childInterface, 0) - executeScript(childInterface, 1))
    										+ s2.substring(k8 + 2);
    							}
    							while (true);
                            }
                            int l11 = s2.indexOf("\\n");
                            String s5;
                            if (l11 != -1) {
                                s5 = s2.substring(0, l11);
                                s2 = s2.substring(l11 + 2);
                            } else {
                                s5 = s2;
                                s2 = "";
                            }
                            if (childInterface.centerText) {
                                font.method382(yPos, xPos + childInterface.width / 2, s5,
                                        j11, false);
                            } else {
                                if (s5.contains("\\r")) {
                                    String text = s5.substring(0, s5.indexOf("\\r"));
                                    String text2 = s5.substring(s5.indexOf("\\r") + 2);
                                    font.drawTextWithPotentialShadow(false, xPos + 3, 0,
                                            text, j11);
                                    int rightX = boxWidth + xPos
                                            - font.getTextWidth(text2) - 2;
                                    font.drawTextWithPotentialShadow(false, rightX, 0,
                                            text2, j11);
                                } else
                                    font.drawTextWithPotentialShadow(false, xPos + 3, 0,
                                            s5, j11);
                            }
                        }
                    }
                } else if (childInterface.type == Widget.TYPE_HOVER || childInterface.type == Widget.TYPE_CONFIG_HOVER) {
                    // Draw sprite
                    boolean flag = false;

                    if (childInterface.toggled) {
                        childInterface.disabledSprite.drawAdvancedSprite(_x, _y, childInterface.spriteOpacity);
                        flag = true;
                        childInterface.toggled = false;
                    } else {
                        childInterface.enabledSprite.drawSprite(_x, _y, childInterface.spriteOpacity);
                    }

                    // Draw text
                    if (childInterface.defaultText == null) {
                        continue;
                    }
                    if (childInterface.centerText) {
                        childInterface.rsFont.drawCenteredString(childInterface.defaultText, _x + childInterface.msgX, _y + childInterface.msgY,
                                flag ? childInterface.defaultHoverColor : childInterface.textColor, 0);
                    } else {
						if (childInterface.rsFont != null)
							childInterface.rsFont.drawBasicString(childInterface.defaultText, _x + 5,
									_y + childInterface.msgY,
									flag ? childInterface.defaultHoverColor : childInterface.textColor, 0);
					}
                } else if (childInterface.type == Widget.TYPE_CONFIG) {
                    Sprite sprite = childInterface.active ? childInterface.enabledSprite : childInterface.disabledSprite;
                    sprite.drawSprite(_x, _y);
                } else if (childInterface.type == Widget.TYPE_SLIDER) {
                    Slider slider = childInterface.slider;
                    if (slider != null) {
                        slider.draw(_x, _y);
                    }
                } else if (childInterface.type == Widget.TYPE_DROPDOWN) {

                    DropdownMenu d = childInterface.dropdown;

                    int bgColour = childInterface.dropdownColours[2];
                    int fontColour = 0xfe971e;
                    int downArrow = 397;

                    if (childInterface.hovered || d.isOpen()) {
                        downArrow = 398;
                        fontColour = 0xffb83f;
                        bgColour = childInterface.dropdownColours[3];
                    }

                    Rasterizer2D.drawPixels(20, _y, _x, childInterface.dropdownColours[0], d.getWidth());
                    Rasterizer2D.drawPixels(18, _y + 1, _x + 1, childInterface.dropdownColours[1], d.getWidth() - 2);
                    Rasterizer2D.drawPixels(16, _y + 2, _x + 2, bgColour, d.getWidth() - 4);

                    int xOffset = childInterface.centerText ? 3 : 16;
                    newSmallFont.drawCenteredString(d.getSelected(), _x + (d.getWidth() - xOffset) / 2, _y + 14, fontColour, 0);

                    if (d.isOpen()) {
                        // Up arrow
                        spriteCache.lookup(396).drawSprite(_x + d.getWidth() - 18, _y + 2);

                        Rasterizer2D.drawPixels(d.getHeight(), _y + 19, _x, childInterface.dropdownColours[0], d.getWidth());
                        Rasterizer2D.drawPixels(d.getHeight() - 2, _y + 20, _x + 1, childInterface.dropdownColours[1], d.getWidth() - 2);
                        Rasterizer2D.drawPixels(d.getHeight() - 4, _y + 21, _x + 2, childInterface.dropdownColours[3], d.getWidth() - 4);

                        int yy = 2;
                        for (int i = 0; i < d.getOptions().length; i++) {
                            if (childInterface.dropdownHover == i) {
                                if (childInterface.id == 28102) {
                                    Rasterizer2D.drawTransparentBox(_x + 2, _y + 19 + yy, d.getWidth() - 4, 13, 0xd0914d, 80);
                                } else {
                                    Rasterizer2D.drawPixels(13, _y + 19 + yy, _x + 2, childInterface.dropdownColours[4], d.getWidth() - 4);
                                }
                                newSmallFont.drawCenteredString(d.getOptions()[i], _x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xffb83f, 0);
                            } else {
                                Rasterizer2D.drawPixels(13, _y + 19 + yy, _x + 2, childInterface.dropdownColours[3], d.getWidth() - 4);
                                newSmallFont.drawCenteredString(d.getOptions()[i], _x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xfe971e, 0);
                            }
                            yy += 14;
                        }

                        drawScrollbar(d.getHeight() - 4, childInterface.scrollPosition, _y + 21, _x + d.getWidth() - 18, d.getHeight() - 5, false);
                    } else {
                        spriteCache.lookup(downArrow).drawSprite(_x + d.getWidth() - 18, _y + 2);
                    }
                   } else if (childInterface.type == Widget.TYPE_KEYBINDS_DROPDOWN) {

                    DropdownMenu d = childInterface.dropdown;

                    // If dropdown inverted, don't draw following 2 menus
                    if (dropdownInversionFlag > 0) {
                        dropdownInversionFlag--;
                        continue;
                    }

                    Rasterizer2D.drawPixels(18, _y + 1, _x + 1, 0x544834, d.getWidth() - 2);
                    Rasterizer2D.drawPixels(16, _y + 2, _x + 2, 0x2e281d, d.getWidth() - 4);
                    newRegularFont.drawBasicString(d.getSelected(), _x + 7, _y + 15, 0xff8a1f, 0);
                    spriteCache.lookup(449).drawSprite(_x + d.getWidth() - 18, _y + 2); // Arrow

                    if (d.isOpen()) {

                        Widget.interfaceCache[childInterface.id - 1].active = true; // Alter stone colour

                        int yPos = _y + 18;

                        // Dropdown inversion for lower stones
                        if (childInterface.inverted) {
                            yPos = _y - d.getHeight() - 10;
                            dropdownInversionFlag = 2;
                        }

                        Rasterizer2D.drawPixels(d.getHeight() + 12, yPos, _x + 1, 0x544834, d.getWidth() - 2);
                        Rasterizer2D.drawPixels(d.getHeight() + 10, yPos + 1, _x + 2, 0x2e281d, d.getWidth() - 4);

                        int yy = 2;
                        int xx = 0;
                        int bb = d.getWidth() / 2;

                        for (int i = 0; i < d.getOptions().length; i++) {

                            int fontColour = 0xff981f;
                            if (childInterface.dropdownHover == i) {
                                fontColour = 0xffffff;
                            }

                            if (xx == 0) {
                                newRegularFont.drawBasicString(d.getOptions()[i], _x + 5, yPos + 14 + yy, fontColour, 0x2e281d);
                                xx = 1;

                            } else {
                                newRegularFont.drawBasicString(d.getOptions()[i], _x + 5 + bb, yPos + 14 + yy, fontColour, 0x2e281d);
                                xx = 0;
                                yy += 15;
                            }
                        }
                    } else {
                        Widget.interfaceCache[childInterface.id - 1].active = false;
                    }
                } else if (childInterface.type == Widget.TYPE_ADJUSTABLE_CONFIG) {

                    int totalWidth = childInterface.width;
                    int spriteWidth = childInterface.enabledSprite.myWidth;
                    int totalHeight = childInterface.height;
                    int spriteHeight = childInterface.enabledSprite.myHeight;

                    Sprite behindSprite = childInterface.active ? childInterface.enabledAltSprite : childInterface.disabledAltSprite;

                    if (childInterface.toggled) {
                        behindSprite.drawSprite(_x, _y);
                        childInterface.enabledSprite.drawAdvancedSprite(_x + (totalWidth / 2) - spriteWidth / 2, _y + (totalHeight / 2) - spriteHeight / 2, childInterface.spriteOpacity);
                        childInterface.toggled = false;
                    } else {
                        behindSprite.drawSprite(_x, _y);
                        childInterface.enabledSprite.drawSprite(_x + (totalWidth / 2) - spriteWidth / 2, _y + (totalHeight / 2) - spriteHeight / 2);
                    }
                } else if (childInterface.type == Widget.TYPE_BOX) {
                    // Draw outline
                    Rasterizer2D.drawBox(_x - 2, _y - 2, childInterface.width + 4, childInterface.height + 4, 0x0e0e0c);
                    Rasterizer2D.drawBox(_x - 1, _y - 1, childInterface.width + 2, childInterface.height + 2, 0x474745);
                    // Draw base box
                    if (childInterface.toggled) {
                        Rasterizer2D.drawBox(_x, _y, childInterface.width, childInterface.height, childInterface.secondaryHoverColor);
                        childInterface.toggled = false;
                    } else {
                        Rasterizer2D.drawBox(_x, _y, childInterface.width, childInterface.height, childInterface.defaultHoverColor);
                    }
                } else if (childInterface.type == Widget.TYPE_MAP) {
                    Rasterizer2D.drawBox(_x + 46, _y + 52, 414, 334, 0);
                    minimapImage.drawSprite(_x, _y);

                    // Calculate player location relative to map position
                    int mapX = (_x + 52) + localPlayer.x / 32;
                    int mapY = (_y + 466) - localPlayer.y / 32;
                    Sprite markerSprite = null;
                	markerSprite = spriteCache.lookup(571);
                    
                    if (worldMapMarker == null) {
                        worldMapMarker = spriteCache.lookup(571).convertToImage();
                    }
                    Graphics2D g2d = Rasterizer2D.createGraphics(false);

                    // Pause rotation every 90 degrees
                    if (!(markerAngle % 90 == 0 && System.currentTimeMillis() - lastMarkerRotation <= 125)) {
                        markerAngle += 3;
                        lastMarkerRotation = System.currentTimeMillis();
                    }
                    
                    //System.out.println(localPlayer.orientation);
                    
                    g2d.rotate(Math.toRadians(-markerAngle), mapX, mapY);
                    g2d.drawImage(worldMapMarker, mapX - markerSprite.myWidth / 2, mapY - markerSprite.myHeight / 2, markerSprite.myWidth, markerSprite.myHeight, null);
                    g2d.dispose();
                }
        }
        if (enableGridOverlay) {
            for (int i : tabInterfaceIDs) {
                if (i == rsInterface.id) return;
            }
            drawGridOverlay();
        }
        Rasterizer2D.setDrawingArea(clipBottom, clipLeft, clipRight, clipTop);
    }

    private void randomizeBackground(IndexedImage background) {
        int j = 256;
        for (int k = 0; k < anIntArray1190.length; k++)
            anIntArray1190[k] = 0;

        for (int l = 0; l < 5000; l++) {
            int i1 = (int) (Math.random() * 128D * (double) j);
            anIntArray1190[i1] = (int) (Math.random() * 256D);
        }
        for (int j1 = 0; j1 < 20; j1++) {
            for (int k1 = 1; k1 < j - 1; k1++) {
                for (int i2 = 1; i2 < 127; i2++) {
                    int k2 = i2 + (k1 << 7);
                    anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1]
                            + anIntArray1190[k2 - 128] + anIntArray1190[k2 + 128])
                            / 4;
                }

            }
            int ai[] = anIntArray1190;
            anIntArray1190 = anIntArray1191;
            anIntArray1191 = ai;
        }
        if (background != null) {
            int l1 = 0;
            for (int j2 = 0; j2 < background.height; j2++) {
                for (int l2 = 0; l2 < background.width; l2++)
                    if (background.palettePixels[l1++] != 0) {
                        int i3 = l2 + 16 + background.drawOffsetX;
                        int j3 = j2 + 16 + background.drawOffsetY;
                        int k3 = i3 + (j3 << 7);
                        anIntArray1190[k3] = 0;
                    }
            }
        }
    }

    private void appendPlayerUpdateMask(int mask, int index, Buffer buffer, Player player) {
        if ((mask & 0x400) != 0) {

            int initialX = buffer.readUByteS();
            int initialY = buffer.readUByteS();
            int destinationX = buffer.readUByteS();
            int destinationY = buffer.readUByteS();
            int startForceMovement = buffer.readLEUShortA() + tick;
            int endForceMovement = buffer.readUShortA() + tick;
            int animation = buffer.readLEUShortA();
            int direction = buffer.readUByteS();

            player.initialX = initialX;
            player.initialY = initialY;
            player.destinationX = destinationX;
            player.destinationY = destinationY;
            player.startForceMovement = startForceMovement;
            player.endForceMovement = endForceMovement;
            player.direction = direction;

            if (animation >= 0) {
                player.emoteAnimation = animation;
                player.displayedEmoteFrames = 0;
                player.emoteTimeRemaining = 0;
                player.animationDelay = 0;
                player.currentAnimationLoops = 0;
                player.anInt1542 = player.remainingPath;
            }


            player.resetPath();
        }
        if ((mask & 0x100) != 0) {
            player.graphic = buffer.readLEUShort();
            int info = buffer.readInt();
            player.graphicHeight = info >> 16;
            player.graphicDelay = tick + (info & 0xffff);
            player.currentAnimation = 0;
            player.anInt1522 = 0;
            if (player.graphicDelay > tick)
                player.currentAnimation = -1;
            if (player.graphic == 65535)
                player.graphic = -1;

            //Load the gfx...
            try {

                if (Frame.animationlist[Graphic.cache[player.graphic].animationSequence.primaryFrames[0] >> 16].length == 0) {
                    resourceProvider.provide(1, Graphic.cache[player.graphic].animationSequence.primaryFrames[0] >> 16);
                }

            } catch (Exception e) {
                //	e.printStackTrace();
            }

        }
        if ((mask & 8) != 0) {
            int animation = buffer.readLEUShort();
            if (animation == 65535)
                animation = -1;
            int delay = buffer.readNegUByte();

            if (animation == player.emoteAnimation && animation != -1) {
                int replayMode = Animation.animations[animation].replayMode;
                if (replayMode == 1) {
                    player.displayedEmoteFrames = 0;
                    player.emoteTimeRemaining = 0;
                    player.animationDelay = delay;
                    player.currentAnimationLoops = 0;
                }
                if (replayMode == 2)
                    player.currentAnimationLoops = 0;
            } else if (animation == -1 || player.emoteAnimation == -1
                    || Animation.animations[animation].forcedPriority >= Animation.animations[player.emoteAnimation].forcedPriority) {
                player.emoteAnimation = animation;
                player.displayedEmoteFrames = 0;
                player.emoteTimeRemaining = 0;
                player.animationDelay = delay;
                player.currentAnimationLoops = 0;
                player.anInt1542 = player.remainingPath;
            }
        }
        if ((mask & 4) != 0) {
            player.spokenText = buffer.readString();
            if (player.spokenText.charAt(0) == '~') {
                player.spokenText = player.spokenText.substring(1);
                sendMessage(player.spokenText, 2, player.name);
            } else if (player == localPlayer)
                sendMessage(player.spokenText, 2, player.name);
            player.textColour = 0;
            player.textEffect = 0;
            player.textCycle = 150;
        }
        if ((mask & 0x80) != 0) {
            int textColorAndEffect = buffer.readLEUShort();
            int privilege = buffer.readUnsignedByte();
            int mode = buffer.readUnsignedByte();
            if (player.name != null && player.visible) {
                long name = StringUtils.encodeBase37(player.name);
                boolean ignored = false;
                if (privilege <= 1) {
                    for (int count = 0; count < ignoreCount; count++) {
                        if (ignoreListAsLongs[count] != name)
                            continue;
                        ignored = true;
						break;
					}

				}
				if (!ignored && onTutorialIsland == 0)
					try {

					/*	chatBuffer.currentPosition = 0;
						buffer.readReverseData(chatBuffer.payload, j3, 0);
						chatBuffer.currentPosition = 0;
						String text = ChatMessageCodec.decode(j3, chatBuffer);*/
                         
                        String text = buffer.readString();
                        // s = Censor.doCensor(s);
                        player.spokenText = text;
                        player.textColour = textColorAndEffect >> 8;
                        player.rights = privilege;
                        player.mode = mode;
                        player.textEffect = textColorAndEffect & 0xff;
                        player.textCycle = 150;

                        sendMessage(text, 1, getPrefix(privilege) + getPrefixMode(mode) + player.name);

                    } catch (Exception exception) {
                        Signlink.reporterror("cde2");
                    }
            }
            //	buffer.currentPosition = off + offset;
        }
        if ((mask & 1) != 0) {
            player.interactingEntity = buffer.readLEUShort();
            if (player.interactingEntity == 65535)
                player.interactingEntity = -1;
        }
        if ((mask & 0x10) != 0) {
            int length = buffer.readNegUByte();
            byte data[] = new byte[length];
            Buffer appearanceBuffer = new Buffer(data);
            buffer.readBytes(length, 0, data);
            playerSynchronizationBuffers[index] = appearanceBuffer;
            player.updateAppearance(appearanceBuffer);
        }
        if ((mask & 2) != 0) {
            player.faceX = buffer.readLEUShortA();
            player.faceY = buffer.readLEUShort();
        }
        if ((mask & 0x20) != 0) {
            int damage = buffer.readShort();
            int type = buffer.readUnsignedByte();
            int hp = buffer.readShort();
            int maxHp = buffer.readShort();
            player.updateHitData(type, damage, tick);
            player.loopCycleStatus = tick + 300;
            player.currentHealth = hp;
            player.maxHealth = maxHp;
        }
        if ((mask & 0x200) != 0) {
            int damage = buffer.readShort();
            int type = buffer.readUnsignedByte();
            int hp = buffer.readShort();
            int maxHp = buffer.readShort();
            player.updateHitData(type, damage, tick);
            player.loopCycleStatus = tick + 300;
            player.currentHealth = hp;
            player.maxHealth = maxHp;
        }
    }

    private void checkForGameUsages() {
        try {
            int j = localPlayer.x + cameraX;
            int k = localPlayer.y + cameraY;
            if (anInt1014 - j < -500 || anInt1014 - j > 500 || anInt1015 - k < -500
                    || anInt1015 - k > 500) {
                anInt1014 = j;
                anInt1015 = k;
            }
            // Key camera rotation speeds below
            if (anInt1014 != j)
                anInt1014 += (j - anInt1014) / 16;
            if (anInt1015 != k)
                anInt1015 += (k - anInt1015) / 16;
            if (super.keyArray[1] == 1)
                anInt1186 += (-26 - anInt1186) / 2;
            else if (super.keyArray[2] == 1)
                anInt1186 += (26 - anInt1186) / 2;
            else
                anInt1186 /= 2;
            if (super.keyArray[3] == 1)
                anInt1187 += (12 - anInt1187) / 2;
            else if (super.keyArray[4] == 1)
                anInt1187 += (-12 - anInt1187) / 2;
            else
                anInt1187 /= 2;
            cameraHorizontal = cameraHorizontal + anInt1186 / 2 & 0x7ff;
            anInt1184 += anInt1187 / 2;
            if (anInt1184 < 128)
                anInt1184 = 128;
            if (anInt1184 > 383)
                anInt1184 = 383;
            int l = anInt1014 >> 7;
            int i1 = anInt1015 >> 7;
            int j1 = getCenterHeight(plane, anInt1015, anInt1014);
            int k1 = 0;
            if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
                for (int l1 = l - 4; l1 <= l + 4; l1++) {
                    for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
                        int l2 = plane;
                        if (l2 < 3 && (tileFlags[1][l1][k2] & 2) == 2)
                            l2++;
                        int i3 = j1 - tileHeights[l2][l1][k2];
                        if (i3 > k1)
                            k1 = i3;
                    }

                }

            }
            anInt1005++;
            if (anInt1005 > 1512) {
                anInt1005 = 0;
                // Unknown (anti-cheat) or maybe cutscene-related
					/*	outgoing.writeOpcode(77);
					outgoing.writeByte(0);
					int bufPos = outgoing.currentPosition;
					outgoing.writeByte((int) (Math.random() * 256D));
					outgoing.writeByte(101);
					outgoing.writeByte(233);
					outgoing.writeShort(45092);

					if ((int) (Math.random() * 2D) == 0) {
						outgoing.writeShort(35784);
					}

					outgoing.writeByte((int) (Math.random() * 256D));
					outgoing.writeByte(64);
					outgoing.writeByte(38);
					outgoing.writeShort((int) (Math.random() * 65536D));
					outgoing.writeShort((int) (Math.random() * 65536D));
					outgoing.writeBytes(outgoing.currentPosition - bufPos);*/
            }
            int j2 = k1 * 192;
            if (j2 > 0x17f00)
                j2 = 0x17f00;
            if (j2 < 32768)
                j2 = 32768;
            if (j2 > anInt984) {
                anInt984 += (j2 - anInt984) / 24;
                return;
            }
            if (j2 < anInt984) {
                anInt984 += (j2 - anInt984) / 80;
            }
        } catch (Exception _ex) {
            Signlink.reporterror("glfc_ex " + localPlayer.x + "," + localPlayer.y + ","
                    + anInt1014 + "," + anInt1015 + "," + currentRegionX + "," + currentRegionY
                    + "," + regionBaseX + "," + regionBaseY);
            throw new RuntimeException("eek");
        }
    }

    public void processDrawing() {
        if (rsAlreadyLoaded || loadingError || genericLoadingError) {
            showErrorScreen();
            return;
        }
        if (!loggedIn && clientLoaded)
            drawLoginScreen(false);
        else
            drawGameScreen();
        anInt1213 = 0;
    }

    private boolean isFriendOrSelf(String s) {
        if (s == null)
            return false;
        for (int i = 0; i < friendsCount; i++)
            if (s.equalsIgnoreCase(friendsList[i]))
                return true;
        return s.equalsIgnoreCase(localPlayer.name);
    }

    private void setWaveVolume(int i) {
        Signlink.wavevol = i;
    }
    
    private void draw3dScreen() {
    
        if (showChatComponents) {
            drawSplitPrivateChat();
        }
        if (Configuration.expCounterOpen) {
            drawExpCounter();
        }

		if (fadingScreen != null) {
			fadingScreen.draw();
		}
		
        if (crossType == 1) {
            int offSet = frameMode == ScreenMode.FIXED ? 4 : 0;
            crosses[crossIndex / 100].drawSprite(crossX - 8 - offSet, crossY - 8 - offSet);
            anInt1142++;
            if (anInt1142 > 67) {
                anInt1142 = 0;
                //sendPacket(new ClearMinimapFlag()); //Not server-sided, flag is only handled in the client
            }
        }
        if (crossType == 2) {
            int offSet = frameMode == ScreenMode.FIXED ? 4 : 0;
            crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - offSet,
                    crossY - 8 - offSet);
        }
        if (openWalkableInterface != -1) {
            try {
                processWidgetAnimations(tickDelta, openWalkableInterface);
                Widget rsinterface = Widget.interfaceCache[openWalkableInterface];
                if (frameMode == ScreenMode.FIXED) {
                    drawInterface(0, 0, rsinterface, 0);
                } else {
                    Widget r = Widget.interfaceCache[openWalkableInterface];
                    int x = frameWidth - 215;
                    x -= r.width;
                    int min_y = Integer.MAX_VALUE;
                    for (int i = 0; i < r.children.length; i++) {
                        min_y = Math.min(min_y, r.childY[i]);
                    }
                    drawInterface(0, x, Widget.interfaceCache[openWalkableInterface], 0 - min_y + 10);
                }
            } catch (Exception ex) {
            }
        }
        if (openInterfaceId != -1) {
        	try {
	        	processWidgetAnimations(tickDelta, openInterfaceId);
				int w = 512, h = 334;
				int x = frameMode == ScreenMode.FIXED ? 0 : (frameWidth / 2) - 256;
				int y = frameMode == ScreenMode.FIXED ? 0 : (frameHeight / 2) - 167;
				int count = stackSideStones ? 3 : 4;
				if (frameMode != ScreenMode.FIXED) {
					for (int i = 0; i < count; i++) {
						if (x + w > (frameWidth - 225)) {
							x = x - 30;
							if (x < 0) {
								x = 0;
							}
						}
						if (y + h > (frameHeight - 182)) {
							y = y - 30;
							if (y < 0) {
								y = 0;
							}
						}
					}
				}
				drawInterface(0, x, Widget.interfaceCache[openInterfaceId], y);
				
				if (openInterfaceId == 5292) {
	                Bank.draw(x, y);
	            }
        	} catch(Exception e) {
        		
        	}
		}
        if (!menuOpen) {
            processRightClick();
            drawTooltip();
        } else if (menuScreenArea == 0) {
            drawMenu(frameMode == ScreenMode.FIXED ? 4 : 0,
                    frameMode == ScreenMode.FIXED ? 4 : 0);
        }

        // Multi sign
        if (multicombat == 1) {
            multiOverlay.drawSprite(frameMode == ScreenMode.FIXED ? 10 : frameWidth - frameWidth + 10,
                    frameHeight - 200);
        }

        // Effect timers
        if(UserInterface.TIMERS.toggled()) {
            drawEffectTimers();	
        }
        int x = regionBaseX + (localPlayer.x - 6 >> 7);
        int y = regionBaseY + (localPlayer.y - 6 >> 7);
        final String screenMode = frameMode == ScreenMode.FIXED ? "Fixed" : "Resizable";
        if (Configuration.clientData) {
            if(myPrivilege == 0) {
                displayFps();
            } else {
            	int textColour = 0xffff00;
                displayFps();
                regularText.render(textColour, "Client Zoom: " + cameraZoom, 90, 5);
                regularText.render(textColour, "Brightness: " + brightnessState, 105, 5);

                regularText.render(textColour, "Mouse X: " + super.mouseX + " , Mouse Y: " + super.mouseY, 30, 5);
                regularText.render(textColour, "Coords: " + x + ", " + y, 45, 5);
                regularText.render(textColour, "Client Mode: " + screenMode + "", 60, 5);
                regularText.render(textColour, "Client Resolution: " + frameWidth + "x" + frameHeight, 75, 5);

                regularText.render(textColour, "Object Maps: " + objectMaps, 130, 5);
                regularText.render(textColour, "Floor Maps: " + floorMaps, 145, 5);
            }
        	
        	
        	
        }
        if (systemUpdateTime != 0) {
            int seconds = systemUpdateTime / 50;
            int minutes = seconds / 60;
            int yOffset = frameMode == ScreenMode.FIXED ? 0 : frameHeight - 498;
            seconds %= 60;
            if (seconds < 10)
                regularText.render(0xffff00,
                        "System update in: " + minutes + ":0" + seconds, 329 + yOffset,
                        4);
            else
                regularText.render(0xffff00, "System update in: " + minutes + ":" + seconds,
                        329 + yOffset, 4);
            anInt849++;
            if (anInt849 > 75) {
                anInt849 = 0;
                //unknown (system updating)
                //outgoing.writeOpcode(148);
            }
        }
    }

    public boolean createBankTab() {
        if (openInterfaceId != 5292) {
            return false;
        }

        boolean fixed = frameMode == ScreenMode.FIXED;

        int offsetX = fixed ? 0 : (frameWidth - 765) / 2;
        int offsetY = fixed ? 0 : (frameHeight - 503) / 2;

        int[] offsets = {61, 102, 142, 182, 222, 262, 302, 342, 382, 422};
        if (anInt1084 >= 50300 && anInt1084 < 50312 && super.mouseY >= 40 + offsetY && super.mouseY <= 77 + offsetY) {
            for (int i = 0; i < offsets.length; i++) {
                if (super.mouseX < offsets[i] + offsetX) {
                    sendPacket(new BankTabCreation(anInt1084, anInt1085, i).create());
                    return true;
                }
            }
        }
        return false;
    }

    private void addIgnore(long name) {
        //try {
        if (name == 0L)
            return;
        sendPacket(new AddIgnore(name).create());
		/*	if (ignoreCount >= 100) {
				sendMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			String s = StringUtils.formatText(StringUtils.decodeBase37(name));
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == name) {
					sendMessage(s + " is already on your ignore list", 0, "");
					return;
				}
			for (int k = 0; k < friendsCount; k++)
				if (friendsListAsLongs[k] == name) {
					sendMessage("Please remove " + s + " from your friend list first", 0,
							"");
					return;
				}

			//ignoreListAsLongs[ignoreCount++] = name;
			//add ignore
			sendPacket(new AddIgnore(name));
			return;
		} catch (RuntimeException runtimeexception) {
			SignLink.reporterror(
					"45688, " + name + ", " + 4 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();*/
    }

    private void processPlayerMovement() {
        for (int index = -1; index < playerCount; index++) {

            int playerIndex;

            if (index == -1) {
                playerIndex = internalLocalPlayerIndex;
            } else {
                playerIndex = playerList[index];
            }

            Player player = players[playerIndex];

            if (player != null) {
                processMovement(player);
            }
        }

    }

    private void method115() {
        if (loadingStage == 2) {
            for (SpawnedObject spawnedObject = (SpawnedObject) spawns
                    .reverseGetFirst(); spawnedObject != null; spawnedObject =
                         (SpawnedObject) spawns.reverseGetNext()) {
                if (spawnedObject.getLongetivity > 0)
                    spawnedObject.getLongetivity--;
                if (spawnedObject.getLongetivity == 0) {
                    if (spawnedObject.getPreviousId < 0
                            || MapRegion.modelReady(spawnedObject.getPreviousId,
                            spawnedObject.previousType)) {
                        removeObject(spawnedObject.y, spawnedObject.plane,
                                spawnedObject.previousOrientation,
                                spawnedObject.previousType, spawnedObject.x,
                                spawnedObject.group, spawnedObject.getPreviousId);
                        spawnedObject.unlink();
                    }
                } else {
                    if (spawnedObject.delay > 0)
                        spawnedObject.delay--;
                    if (spawnedObject.delay == 0 && spawnedObject.x >= 1
                            && spawnedObject.y >= 1 && spawnedObject.x <= 102
                            && spawnedObject.y <= 102
                            && (spawnedObject.id < 0 || MapRegion.modelReady(
                            spawnedObject.id, spawnedObject.type))) {
                        removeObject(spawnedObject.y, spawnedObject.plane,
                                spawnedObject.orientation, spawnedObject.type,
                                spawnedObject.x, spawnedObject.group,
                                spawnedObject.id);
                        spawnedObject.delay = -1;
                        if (spawnedObject.id == spawnedObject.getPreviousId
                                && spawnedObject.getPreviousId == -1)
                            spawnedObject.unlink();
                        else if (spawnedObject.id == spawnedObject.getPreviousId
                                && spawnedObject.orientation == spawnedObject.previousOrientation
                                && spawnedObject.type == spawnedObject.previousType)
                            spawnedObject.unlink();
                    }
                }
            }

        }
    }

    private void determineMenuSize() {
        int boxLength = boldText.getTextWidth("Choose option");
        for (int row = 0; row < menuActionRow; row++) {
            int actionLength = boldText.getTextWidth(menuActionText[row]);
            if (actionLength > boxLength)
                boxLength = actionLength;
        }
        boxLength += 8;
        int offset = 15 * menuActionRow + 21;

        if (super.saveClickX > 0 && super.saveClickY > 0 && super.saveClickX < frameWidth && super.saveClickY < frameHeight) {
            int xClick = super.saveClickX - boxLength / 2;
            if (xClick + boxLength > frameWidth - 4) {
                xClick = frameWidth - 4 - boxLength;
            }
            if (xClick < 0) {
                xClick = 0;
            }
            int yClick = super.saveClickY - 0;
            if (yClick + offset > frameHeight - 6) {
                yClick = frameHeight - 6 - offset;
            }
            if (yClick < 0) {
                yClick = 0;
            }
            menuOpen = true;

            if (removeShiftDropOnMenuOpen && secondMenuAction[menuActionRow - 1] == 3214) {
                removeShiftDropOnMenuOpen = false;
                processRightClick();
            }

            menuOffsetX = xClick;
            menuOffsetY = yClick;
            menuWidth = boxLength;
            menuHeight = 15 * menuActionRow + 22;
        }
    }

    private void updateLocalPlayerMovement(Buffer stream) {
        stream.initBitAccess();

        int update = stream.readBits(1);

        if (update == 0) {
            return;
        }

        int type = stream.readBits(2);
        if (type == 0) {
            mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = internalLocalPlayerIndex;
            return;
        }
        if (type == 1) {
            int direction = stream.readBits(3);
            localPlayer.moveInDir(false, direction);
            int updateRequired = stream.readBits(1);

            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = internalLocalPlayerIndex;
            }
            return;
        }
        if (type == 2) {
            int firstDirection = stream.readBits(3);
            localPlayer.moveInDir(true, firstDirection);

            int secondDirection = stream.readBits(3);
            localPlayer.moveInDir(true, secondDirection);

            int updateRequired = stream.readBits(1);

            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = internalLocalPlayerIndex;
            }
            return;
        }
        if (type == 3) {
            plane = stream.readBits(2);

            //Fix for height changes
            if (lastKnownPlane != plane) {
                loadingStage = 1;
            }
            lastKnownPlane = plane;

            int teleport = stream.readBits(1);
            int updateRequired = stream.readBits(1);


            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = internalLocalPlayerIndex;
            }

            int y = stream.readBits(7);
            int x = stream.readBits(7);

            localPlayer.setPos(x, y, teleport == 1);
        }
    }

    private void nullLoader() {
        aBoolean831 = false;
        while (drawingFlames) {
            aBoolean831 = false;
            try {
                Thread.sleep(50L);
            } catch (Exception _ex) {
            }
        }
        titleIndexedImages = null;
        anIntArray851 = null;
        anIntArray852 = null;
        anIntArray853 = null;
        anIntArray1190 = null;
        anIntArray1191 = null;
        flameLeftSprite = null;
        flameRightSprite = null;
    }

    private boolean processWidgetAnimations(int tick, int interfaceId){
        boolean redrawRequired = false;
        Widget widget = Widget.interfaceCache[interfaceId];

        if (widget == null
                || widget.children == null) {
            return false;
        }

        for (int element : widget.children) {
            if (element == -1) {
                break;
            }

            Widget child = Widget.interfaceCache[element];

            if (child.type == Widget.TYPE_MODEL_LIST) {
                redrawRequired |= processWidgetAnimations(tick, child.id);
            }

            if (child.type == 6 && (child.defaultAnimationId != -1 || child.secondaryAnimationId != -1)) {
                boolean updated = interfaceIsSelected(child);

                int animationId = updated ? child.secondaryAnimationId : child.defaultAnimationId;

                if (animationId != -1) {
                    Animation animation = Animation.animations[animationId];
                    for (child.lastFrameTime += tick; child.lastFrameTime > animation.duration(child.currentFrame); ) {
                        child.lastFrameTime -= animation.duration(child.currentFrame) + 1;
                        child.currentFrame++;
                        if (child.currentFrame >= animation.frameCount) {
                            child.currentFrame -= animation.loopOffset;
                            if (child.currentFrame < 0
                                    || child.currentFrame >= animation.frameCount)
                                child.currentFrame = 0;
                        }
                        redrawRequired = true;
                    }

                }
            }
        }

        return redrawRequired;
    }

    private int setCameraLocation() {
        if (!Misc.TOGGLE_ROOFS.toggled())
            return plane;
        int j = 3;
        if (yCameraCurve < 310) {
            int k = xCameraPos >> 7;
            int l = yCameraPos >> 7;
            int i1 = localPlayer.x >> 7;
            int j1 = localPlayer.y >> 7;
            if ((tileFlags[plane][k][l] & 4) != 0)
                j = plane;
            int k1;
            if (i1 > k)
                k1 = i1 - k;
            else
                k1 = k - i1;
            int l1;
            if (j1 > l)
                l1 = j1 - l;
            else
                l1 = l - j1;
            if (k1 > l1) {
                int i2 = (l1 * 0x10000) / k1;
                int k2 = 32768;
                while (k != i1) {
                    if (k < i1)
                        k++;
                    else if (k > i1)
                        k--;
                    if ((tileFlags[plane][k][l] & 4) != 0)
                        j = plane;
                    k2 += i2;
                    if (k2 >= 0x10000) {
                        k2 -= 0x10000;
                        if (l < j1)
                            l++;
                        else if (l > j1)
                            l--;
                        if ((tileFlags[plane][k][l] & 4) != 0)
                            j = plane;
                    }
                }
            } else {
                int j2 = (k1 * 0x10000) / l1;
                int l2 = 32768;
                while (l != j1) {
                    if (l < j1)
                        l++;
                    else if (l > j1)
                        l--;
                    if ((tileFlags[plane][k][l] & 4) != 0)
                        j = plane;
                    l2 += j2;
                    if (l2 >= 0x10000) {
                        l2 -= 0x10000;
                        if (k < i1)
                            k++;
                        else if (k > i1)
                            k--;
                        if ((tileFlags[plane][k][l] & 4) != 0)
                            j = plane;
                    }
                }
            }
        }
        try {
	        if ((tileFlags[plane][localPlayer.x >> 7][localPlayer.y >> 7] & 4) != 0)
	            j = plane;
        } catch (ArrayIndexOutOfBoundsException e) {
        	j = plane;
        	System.out.println("Out Of Bounds: [Client::setCameraLocation()] Plane: " + plane + " LocalX: " + localPlayer.x + " LocalY: " + localPlayer.y);
        }
        return j;
    }

    private int resetCameraHeight() {
        int orientation = getCenterHeight(plane, yCameraPos, xCameraPos);
        if (orientation - zCameraPos < 800
                && (tileFlags[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0)
            return plane;
        else
            return 3;
    }

    private void removeFriend(long name) {
        if (name == 0L)
            return;
        sendPacket(new DeleteFriend(name).create());
    }

    private void removeIgnore(long name) {
        if (name == 0L)
            return;
        sendPacket(new DeleteIgnore(name).create());
    }

    public String getParameter(String s) {
        if (Signlink.mainapp != null)
            return Signlink.mainapp.getParameter(s);
        else
            return super.getParameter(s);
    }

    private int executeScript(Widget widget, int id) {
        if (widget.valueIndexArray == null || id >= widget.valueIndexArray.length)
            return -2;
        try {
            int script[] = widget.valueIndexArray[id];
            int accumulator = 0;
            int counter = 0;
            int operator = 0;
            do {
                int instruction = script[counter++];
                int value = 0;
                byte next = 0;

                if (instruction == 0) {
                    return accumulator;
                }

                if (instruction == 1) {
                    value = currentLevels[script[counter++]];
                }

                if (instruction == 2) {
                    value = maximumLevels[script[counter++]];
                }

                if (instruction == 3) {
                    value = currentExp[script[counter++]];
                }

                if (instruction == 4) {
                    Widget other = Widget.interfaceCache[script[counter++]];
                    int item = script[counter++];
                    if (item >= 0 && item < ItemDefinition.TOTAL_ITEMS
                            && (!ItemDefinition.lookup(item).is_members_only
                            || isMembers)) {
                        for (int slot = 0; slot < other.inventoryItemId.length; slot++)
                            if (other.inventoryItemId[slot] == item + 1)
                                value += other.inventoryAmounts[slot];

                    }
                }
                if (instruction == 5) {
                    value = settings[script[counter++]];
                }

                if (instruction == 6) {
                    value = SKILL_EXPERIENCE[maximumLevels[script[counter++]] - 1];
                }

                if (instruction == 7) {
                    value = (settings[script[counter++]] * 100) / 46875;
                }

                if (instruction == 8) {
                    value = localPlayer.combatLevel;
                }

                if (instruction == 9) {
                    for (int skill = 0; skill < SkillConstants.SKILL_COUNT; skill++)
                        if (SkillConstants.ENABLED_SKILLS[skill])
                            value += maximumLevels[skill];
                }

                if (instruction == 10) {
                    Widget other = Widget.interfaceCache[script[counter++]];
                    int item = script[counter++] + 1;
                    if (item >= 0 && item < ItemDefinition.TOTAL_ITEMS && isMembers) {
                        for (int stored =
                             0; stored < other.inventoryItemId.length; stored++) {
                            if (other.inventoryItemId[stored] != item)
                                continue;
                            value = 0x3b9ac9ff;
                            break;
                        }

                    }
                }

                if (instruction == 11) {
                    value = runEnergy;
                }

                if (instruction == 12) {
                    value = weight;
                }

                if (instruction == 13) {
                    int bool = settings[script[counter++]];
                    int shift = script[counter++];
                    value = (bool & 1 << shift) == 0 ? 0 : 1;
                }

                if (instruction == 14) {
                    int index = script[counter++];
                    VariableBits bits = VariableBits.varbits[index];
                    int setting = bits.getSetting();
                    int low = bits.getLow();
                    int high = bits.getHigh();
                    int mask = BIT_MASKS[high - low];
                    value = settings[setting] >> low & mask;
                }

                if (instruction == 15) {
                    next = 1;
                }

                if (instruction == 16) {
                    next = 2;
                }

                if (instruction == 17) {
                    next = 3;
                }

                if (instruction == 18) {
                    value = (localPlayer.x >> 7) + regionBaseX;
                }

                if (instruction == 19) {
                    value = (localPlayer.y >> 7) + regionBaseY;
                }

                if (instruction == 20) {
                    value = script[counter++];
                }

                if (next == 0) {

                    if (operator == 0) {
                        accumulator += value;
                    }

                    if (operator == 1) {
                        accumulator -= value;
                    }

                    if (operator == 2 && value != 0) {
                        accumulator /= value;
                    }

                    if (operator == 3) {
                        accumulator *= value;
                    }
                    operator = 0;
                } else {
                    operator = next;
                }
            } while (true);
        } catch (Exception _ex) {
            return -1;
        }
    }

    private void drawTooltip() {
        if (menuActionRow < 2 && itemSelected == 0 && spellSelected == 0)
            return;
        String s;
        if (itemSelected == 1 && menuActionRow < 2)
            s = "Use " + selectedItemName + " with...";
        else if (spellSelected == 1 && menuActionRow < 2)
            s = spellTooltip + "...";
        else
            s = menuActionText[menuActionRow - 1];
        if(s.contains("<br>"))
        	s = s.replaceAll("<br>", " ");
        if (menuActionRow > 2)
            s = s + "@whi@ / " + (menuActionRow - 2) + " more options";
        boldText.method390(4, 0xffffff, s, tick / 1000, 15);
    }

    private void markMinimap(Sprite sprite, int x, int y) {
        if (sprite == null) {
            return;
        }
        int angle = cameraHorizontal + minimapRotation & 0x7ff;
        int l = x * x + y * y;
        if (l > 6400) {
            return;
        }
        int sineAngle = Model.SINE[angle];
        int cosineAngle = Model.COSINE[angle];
        sineAngle = (sineAngle * 256) / (minimapZoom + 256);
        cosineAngle = (cosineAngle * 256) / (minimapZoom + 256);
        int spriteOffsetX = y * sineAngle + x * cosineAngle >> 16;
        int spriteOffsetY = y * cosineAngle - x * sineAngle >> 16;
        if (frameMode == ScreenMode.FIXED) {
            sprite.drawSprite(((94 + spriteOffsetX) - sprite.maxWidth / 2) + 4 + 30,
                    83 - spriteOffsetY - sprite.maxHeight / 2 - 4 + 5);
        } else {
            sprite.drawSprite(
                    ((77 + spriteOffsetX) - sprite.maxWidth / 2) + 4 + 5
                            + (frameWidth - 167),
                    85 - spriteOffsetY - sprite.maxHeight / 2);
        }
    }

    private void drawMinimap() {
        if (frameMode == ScreenMode.FIXED) {
            minimapImageProducer.initDrawingArea();
        }
        if (minimapState == 2) {
            if (frameMode == ScreenMode.FIXED) {
                spriteCache.lookup(19).drawSprite(0, 0);
            } else {
                spriteCache.lookup(44).drawSprite(frameWidth - 181, 0);
                spriteCache.lookup(45).drawSprite(frameWidth - 158, 7);
            }
            if (frameMode != ScreenMode.FIXED && stackSideStones) {
                if (super.mouseX >= frameWidth - 26 && super.mouseX <= frameWidth - 1
                        && super.mouseY >= 2 && super.mouseY <= 24 || tabId == 15) {
                    spriteCache.lookup(27).drawSprite(frameWidth - 25, 2);
                } else {
                    spriteCache.lookup(27).drawAdvancedSprite(frameWidth - 25, 2, 165);
                }
            }
            loadAllOrbs();
            compass.rotate(33, cameraHorizontal, anIntArray1057, 256, anIntArray968,
                    (frameMode == ScreenMode.FIXED ? 25 : 24), 4,
                    (frameMode == ScreenMode.FIXED ? 29 : frameWidth - 176), 33, 25);
            if (menuOpen) {
                drawMenu(frameMode == ScreenMode.FIXED ? 516 : 0, 0);
            }
            if (frameMode == ScreenMode.FIXED) {
                minimapImageProducer.initDrawingArea();
            }
            return;
        }
        int angle = cameraHorizontal + minimapRotation & 0x7ff;
        int centreX = 48 + localPlayer.x / 32;
        int centreY = 464 - localPlayer.y / 32;
        minimapImage.rotate(151, angle, minimapLineWidth, 256 + minimapZoom, minimapLeft, centreY, (frameMode == ScreenMode.FIXED ? 9 : 7),
                (frameMode == ScreenMode.FIXED ? 54 : frameWidth - 158), 146, centreX);
        for (int icon = 0; icon < anInt1071; icon++) {
            int mapX = (minimapHintX[icon] * 4 + 2) - localPlayer.x / 32;
            int mapY = (minimapHintY[icon] * 4 + 2) - localPlayer.y / 32;
            markMinimap(minimapHint[icon], mapX, mapY);
        }
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Deque class19 = groundItems[plane][x][y];
                if (class19 != null) {
                    int mapX = (x * 4 + 2) - localPlayer.x / 32;
                    int mapY = (y * 4 + 2) - localPlayer.y / 32;
                    markMinimap(mapDotItem, mapX, mapY);
                }
            }
        }
        for (int n = 0; n < npcCount; n++) {
            Npc npc = npcs[npcIndices[n]];
            if (npc != null && npc.isVisible()) {
                NpcDefinition entityDef = npc.desc;
                if (entityDef.childrenIDs != null) {
                    entityDef = entityDef.morph();
                }
                if (entityDef != null && entityDef.drawMinimapDot && entityDef.clickable) {
                    int mapX = npc.x / 32 - localPlayer.x / 32;
                    int mapY = npc.y / 32 - localPlayer.y / 32;
                    markMinimap(mapDotNPC, mapX, mapY);
                }
            }
        }
        for (int p = 0; p < playerCount; p++) {
            Player player = players[playerList[p]];
            if (player != null && player.isVisible()) {
                int mapX = player.x / 32 - localPlayer.x / 32;
                int mapY = player.y / 32 - localPlayer.y / 32;
                boolean friend = false;
                boolean clanMember = false;

                for (int i = 37144; i <= 37244; i++) {
                    if (Widget.interfaceCache[i].defaultText.toLowerCase().
                            contains(player.name.toLowerCase())) {
                        clanMember = true;
                    }
                }

                long nameHash = StringUtils.encodeBase37(player.name);
                for (int f = 0; f < friendsCount; f++) {
                    if (nameHash != friendsListAsLongs[f] || friendsNodeIDs[f] == 0) {
                        continue;
                    }
                    friend = true;
                    break;
                }
                boolean team = false;
                if (localPlayer.team != 0 && player.team != 0
                        && localPlayer.team == player.team) {
                    team = true;
                }
                if (friend) {
                    markMinimap(mapDotFriend, mapX, mapY);
                } else if (clanMember) {
                    markMinimap(mapDotClan, mapX, mapY);
                } else if (team) {
                    markMinimap(mapDotTeam, mapX, mapY);
                } else {
                    markMinimap(mapDotPlayer, mapX, mapY);
                }
            }
        }
        if (hintIconDrawType != 0 && tick % 20 < 10) {
            if (hintIconDrawType == 1 && hintIconNpcId >= 0 && hintIconNpcId < npcs.length) {
                Npc npc = npcs[hintIconNpcId];
                if (npc != null) {
                    int mapX = npc.x / 32 - localPlayer.x / 32;
                    int mapY = npc.y / 32 - localPlayer.y / 32;
                    refreshMinimap(mapMarker, mapY, mapX);
                }
            }
            if (hintIconDrawType == 2) {
                int mapX = ((hintIconX - regionBaseX) * 4 + 2) - localPlayer.x / 32;
                int mapY = ((hintIconY - regionBaseY) * 4 + 2) - localPlayer.y / 32;
                refreshMinimap(mapMarker, mapY, mapX);
            }
            if (hintIconDrawType == 10 && hintIconPlayerId >= 0
                    && hintIconPlayerId < players.length) {
                Player player = players[hintIconPlayerId];
                if (player != null) {
                    int mapX = player.x / 32 - localPlayer.x / 32;
                    int mapY = player.y / 32 - localPlayer.y / 32;
                    refreshMinimap(mapMarker, mapY, mapX);
                }
            }
        }
        if (destinationX != 0) {
            int mapX = (destinationX * 4 + 2) - localPlayer.x / 32;
            int mapY = (destinationY * 4 + 2) - localPlayer.y / 32;
            markMinimap(mapFlag, mapX, mapY);
        }
        Rasterizer2D.drawBox((frameMode == ScreenMode.FIXED ? 127 : frameWidth - 88), (frameMode == ScreenMode.FIXED ? 83 : 80), 3, 3,
                0xffffff);
        if (frameMode == ScreenMode.FIXED) {
            spriteCache.lookup(19).drawSprite(0, 0);
        } else {
            spriteCache.lookup(44).drawSprite(frameWidth - 181, 0);
        }
        compass.rotate(33, cameraHorizontal, anIntArray1057, 256, anIntArray968,
                (frameMode == ScreenMode.FIXED ? 25 : 24), 4,
                (frameMode == ScreenMode.FIXED ? 29 : frameWidth - 176), 33, 25);
        if (frameMode != ScreenMode.FIXED && stackSideStones) {
            if (super.mouseX >= frameWidth - 26 && super.mouseX <= frameWidth - 1
                    && super.mouseY >= 2 && super.mouseY <= 24 || tabId == 10) {
                spriteCache.lookup(27).drawSprite(frameWidth - 25, 2);
            } else {
                spriteCache.lookup(27).drawAdvancedSprite(frameWidth - 25, 2, 165);
            }
        }
        loadAllOrbs();
        if (menuOpen) {
            drawMenu(frameMode == ScreenMode.FIXED ? 516 : 0, 0);
        }
        if (frameMode == ScreenMode.FIXED) {
            gameScreenImageProducer.initDrawingArea();
        }
    }

    private void loadAllOrbs() {

        boolean fixed = frameMode == ScreenMode.FIXED;
        boolean specOrb = UserInterface.TOGGLE_SPEC_ORBS.toggled();
        int xOffset = fixed ? 0 : frameWidth - 217;

        if (specOrb) {
            loadSpecialOrb(xOffset);
        }

        if (!UserInterface.TOGGLE_ORBS.toggled()) {
            return;
        }

        loadHpOrb(xOffset);
        loadPrayerOrb(xOffset, specOrb ? 0 : 11);
        loadRunOrb(specOrb ? xOffset : xOffset + 13, specOrb ? 0 : 15);

		/* World map */
        spriteCache.lookup(worldHover ? 52 : 51).drawSprite(fixed ? 196 : frameWidth - 34, fixed ? 126 : 139);
		/* Xp counter */
        int offSprite = Configuration.expCounterOpen ? 53 : 22;
        int onSprite = Configuration.expCounterOpen ? 54 : 23;
        spriteCache.lookup(expCounterHover ? onSprite : offSprite).drawSprite(fixed ? 0 : frameWidth - 216, 21);
    }

    private void loadHpOrb(int xOffset) {
//        int hover = poisonType == 0 ? 8 : 7;
        Sprite bg = spriteCache.lookup(hpHover ? 8 : 7);

        int id = poisonType == 0 ? 0 : 1219;

        Sprite fg = spriteCache.lookup(id);
        bg.drawSprite(0 + xOffset, 41);
        fg.drawSprite(27 + xOffset, 45);
        int level = currentLevels[3];
        int max = maximumLevels[3];
        double percent = level / (double) max;
        spriteCache.lookup(14).myHeight = (int) (26 * (1 - percent));
        spriteCache.lookup(14).drawSprite(27 + xOffset, 45);
        if (percent <= .25) {
            spriteCache.lookup(9).drawSprite1(33 + xOffset, 52, 200 + (int) (50 * Math.sin(tick / 7.0)));
        } else {
            spriteCache.lookup(9).drawSprite(33 + xOffset, 52);
        }
        smallText.method382(getOrbTextColor((int) (percent * 100)), 15 + xOffset, "" + level, 67, true);
    }

    private void loadPrayerOrb(int xOffset, int yOffset) {
        Sprite bg = spriteCache.lookup(prayHover ? 8 : 7);
        Sprite fg = spriteCache.lookup(prayClicked ? 2 : 1);
        bg.drawSprite(0 + xOffset, 74 + yOffset);
        fg.drawSprite(27 + xOffset, 79 + yOffset);
        int level = currentLevels[5];
        int max = maximumLevels[5];
        double percent = level / (double) max;
        spriteCache.lookup(14).myHeight = (int) (26 * (1 - percent));
        spriteCache.lookup(14).drawSprite(27 + xOffset, 79 + yOffset);
        if (percent <= .25) {
            spriteCache.lookup(10).drawSprite1(30 + xOffset, 82 + yOffset, 200 + (int) (50 * Math.sin(tick / 7.0)));
        } else {
            spriteCache.lookup(10).drawSprite(30 + xOffset, 82 + yOffset);
        }
        smallText.method382(getOrbTextColor((int) (percent * 100)), 15 + xOffset, level + "", 100 + yOffset, true);
    }

    private void loadRunOrb(int xOffset, int yOffset) {
        Sprite bg = spriteCache.lookup(runHover ? 8 : 7);
        Sprite fg = spriteCache.lookup(settings[152] == 1 ? 4 : 3);
        bg.drawSprite(10 + xOffset, 107 + yOffset);
        fg.drawSprite(37 + xOffset, 111 + yOffset);
        int level = runEnergy;
        double percent = level / (double) 100;
        spriteCache.lookup(14).myHeight = (int) (26 * (1 - percent));
        spriteCache.lookup(14).drawSprite(37 + xOffset, 111 + yOffset);
        if (percent <= .25) {
            spriteCache.lookup(settings[152] == 1 ? 12 : 11).drawSprite1(43 + xOffset, 115 + yOffset, 200 + (int) (50 * Math.sin(tick / 7.0)));
        } else {
            spriteCache.lookup(settings[152] == 1 ? 12 : 11).drawSprite(43 + xOffset, 115 + yOffset);
        }
        // spriteCache.lookup(336).drawSprite(20 + xOffset, 125 + yOffset);
        smallText.method382(getOrbTextColor((int) (percent * 100)), 24 + xOffset, Integer.toString(runEnergy), 132 + yOffset, true);
    }

    private void loadSpecialOrb(int xOffset) {
        Sprite image = spriteCache.lookup(specialHover ? 8 : 7);
        Sprite fill = spriteCache.lookup(specialEnabled == 0 ? 5 : 6);
        Sprite sword = spriteCache.lookup(55);
        double percent = specialAttack / (double) 100;
        image.drawSprite(43 + xOffset, 132);
        fill.drawSprite(70 + xOffset, 135);
        spriteCache.lookup(14).myHeight = (int) (26 * (1 - percent));
        spriteCache.lookup(14).drawSprite(71 + xOffset, 135);
        sword.drawSprite(75 + xOffset, 141);
        smallText.method382(getOrbTextColor((int) (percent * 100)), 57 + xOffset, specialAttack + "", 158, true);
    }

    private void npcScreenPos(Mob entity, int i) {
        calcEntityScreenPos(entity.x, i, entity.y);
    }

    public void calcEntityScreenPos(int i, int j, int l) {

        if (i < 128 || l < 128 || i > 13056 || l > 13056) {
            spriteDrawX = -1;
            spriteDrawY = -1;
            return;
        }
        int i1 = getCenterHeight(plane, l, i) - j;
        i -= xCameraPos;
        i1 -= zCameraPos;
        l -= yCameraPos;
        int j1 = Model.SINE[yCameraCurve];
        int k1 = Model.COSINE[yCameraCurve];
        int l1 = Model.SINE[xCameraCurve];
        int i2 = Model.COSINE[xCameraCurve];
        int j2 = l * l1 + i * i2 >> 16;
        l = l * i2 - i * l1 >> 16;
        i = j2;
        j2 = i1 * k1 - l * j1 >> 16;
        l = i1 * j1 + l * k1 >> 16;
        i1 = j2;
        if (l >= 50) {
            spriteDrawX = Rasterizer3D.originViewX + (i << SceneGraph.viewDistance) / l;
            spriteDrawY = Rasterizer3D.originViewY + (i1 << SceneGraph.viewDistance) / l;
        } else {
            spriteDrawX = -1;
            spriteDrawY = -1;
        }
    }

    public void projectParticle(int i, int j, int l) {

        if (i < 128 || l < 128 || i > 13056 || l > 13056) {
            particleDrawX = -1;
            particleDrawY = -1;
            return;
        }
        int i1 = getCenterHeight(plane, l, i) - j;
        i -= xCameraPos;
        i1 -= zCameraPos;
        l -= yCameraPos;

        int j1 = Model.SINE[yCameraCurve];
        int k1 = Model.COSINE[yCameraCurve];
        int l1 = Model.SINE[xCameraCurve];
        int i2 = Model.COSINE[xCameraCurve];
        int j2 = l * l1 + i * i2 >> 16;
        l = l * i2 - i * l1 >> 16;
        i = j2;
        j2 = i1 * k1 - l * j1 >> 16;
        l = i1 * j1 + l * k1 >> 16;
        i1 = j2;
        if (l >= 50) {
            particleDrawX = Rasterizer3D.originViewX + (i << SceneGraph.viewDistance) / l;
            particleDrawY = Rasterizer3D.originViewY + (i1 << SceneGraph.viewDistance) / l;
            //particleDrawZ = Rasterizer3D.originViewY + (((i1) << SceneGraph.viewDistance) / l) * Rasterizer3D.originViewY +((i) << SceneGraph.viewDistance) / l;
            //particleDrawZ = l;
            particleDrawZ = Rasterizer3D.originViewY + ((i) << SceneGraph.viewDistance) / l; // TODO

        } else {
            particleDrawX = -1;
            particleDrawY = -1;
        }
    }

    private void buildSplitPrivateChatMenu() {
        if (splitPrivateChat == 0)
            return;
        int message = 0;
        if (systemUpdateTime != 0)
            message = 1;
        for (int index = 0; index < 100; index++)
            if (chatMessages[index] != null) {
                int type = chatTypes[index];
                String name = chatNames[index];
                if (name != null && name.startsWith("@cr1@")) {
                    name = name.substring(5);
                } else if (name != null && name.startsWith("@cr2@")) {
                    name = name.substring(5);
                } else if (name != null && name.startsWith("@cr4@")) {
                    name = name.substring(5);
                } else if (name != null && name.startsWith("@cr5@")) {
                    name = name.substring(5);
                } else if (name != null && name.startsWith("@cr6@")) {
                    name = name.substring(5);
                } else if (name != null && name.startsWith("@cr7@")) {
                    name = name.substring(5);
                } else if (name != null && name.startsWith("@cr8@")) {
                    name = name.substring(5);
                } else if (name != null && name.startsWith("@cr9@")) {
                    name = name.substring(5);
                } else if (name != null && name.startsWith("@cr10@")) {
                    name = name.substring(6);
                }
                if ((type == 3 || type == 7) && (type == 7 || privateChatMode == 0
                        || privateChatMode == 1 && isFriendOrSelf(name))) {
                    int offSet = frameMode == ScreenMode.FIXED ? 4 : 0;
                    int y = 329 - message * 13;
                    if (frameMode != ScreenMode.FIXED) {
                        y = frameHeight - 170 - message * 13;
                    }
                    if (super.mouseX > 4 && super.mouseY - offSet > y - 10
                            && super.mouseY - offSet <= y + 3) {
                        int i1 = regularText.getTextWidth(
                                "From:  " + name + chatMessages[index]) + 25;
                        if (i1 > 450)
                            i1 = 450;
                        if (super.mouseX < 4 + i1) {
                            if (!isFriendOrSelf(name)) {
                                menuActionText[menuActionRow] = "Add ignore @whi@" + name;
                                menuActionTypes[menuActionRow] = 2042;
                                menuActionRow++;
                                menuActionText[menuActionRow] = "Add friend @whi@" + name;
                                menuActionTypes[menuActionRow] = 2337;
                                menuActionRow++;
                            } else {
                                menuActionText[menuActionRow] = "Message @whi@" + name;
                                menuActionTypes[menuActionRow] = 2639;
                                menuActionRow++;
                            }
                        }
                    }
                    if (++message >= 5)
                        return;
                }
                if ((type == 5 || type == 6) && privateChatMode < 2 && ++message >= 5)
                    return;
            }

    }

    private void requestSpawnObject(int longetivity, int id, int orientation, int group, int y, int type,
                                    int plane, int x, int delay) {
        SpawnedObject object = null;
        for (SpawnedObject node = (SpawnedObject) spawns.reverseGetFirst(); node != null; node =
                (SpawnedObject) spawns.reverseGetNext()) {
            if (node.plane != plane || node.x != x || node.y != y || node.group != group)
                continue;
            object = node;
            break;
        }

        if (object == null) {
            object = new SpawnedObject();
            object.plane = plane;
            object.group = group;
            object.x = x;
            object.y = y;
            method89(object);
            spawns.insertHead(object);
        }
        object.id = id;
        object.type = type;
        object.orientation = orientation;
        object.delay = delay;
        object.getLongetivity = longetivity;
    }

    private boolean interfaceIsSelected(Widget widget) {
        if (widget.valueCompareType == null)
            return false;
        for (int i = 0; i < widget.valueCompareType.length; i++) {
            int j = executeScript(widget, i);
            int k = widget.requiredValues[i];
            if (widget.valueCompareType[i] == 2) {
                if (j >= k)
                    return false;
            } else if (widget.valueCompareType[i] == 3) {
                if (j <= k)
                    return false;
            } else if (widget.valueCompareType[i] == 4) {
                if (j == k)
                    return false;
            } else if (j != k)
                return false;
        }

        return true;
    }

    private void updateOtherPlayerMovement(Buffer stream) {
        int count = stream.readBits(8);

        if (count < playerCount) {
            for (int index = count; index < playerCount; index++) {
                removedMobs[removedMobCount++] = playerList[index];
            }
        }
        if (count > playerCount) {
            Signlink.reporterror(myUsername + " Too many players");
            throw new RuntimeException("eek");
        }
        playerCount = 0;
        for (int globalIndex = 0; globalIndex < count; globalIndex++) {
            int index = playerList[globalIndex];
            Player player = players[index];
            player.index = index;
            int updateRequired = stream.readBits(1);

            if (updateRequired == 0) {
                playerList[playerCount++] = index;
                player.time = tick;
            } else {
                int movementType = stream.readBits(2);
                if (movementType == 0) {
                    playerList[playerCount++] = index;
                    player.time = tick;
                    mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = index;
                } else if (movementType == 1) {
                    playerList[playerCount++] = index;
                    player.time = tick;

                    int direction = stream.readBits(3);

                    player.moveInDir(false, direction);

                    int update = stream.readBits(1);

                    if (update == 1) {
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = index;
                    }
                } else if (movementType == 2) {
                    playerList[playerCount++] = index;
                    player.time = tick;

                    int firstDirection = stream.readBits(3);
                    player.moveInDir(true, firstDirection);

                    int secondDirection = stream.readBits(3);
                    player.moveInDir(true, secondDirection);

                    int update = stream.readBits(1);
                    if (update == 1) {
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = index;
                    }
                } else if (movementType == 3) {
                    removedMobs[removedMobCount++] = index;
                }
            }
        }
    }

    public void drawMusicSprites() {

        int musicState = 0;
        bottomRightImageProducer.initDrawingArea();
        switch (musicState) {
            case 0:
                spriteCache.lookup(58).drawSprite(158, 196);
                break;

            case 1:
                spriteCache.lookup(59).drawSprite(158, 196);
                break;
        }
    }
    
    boolean bgAlphaMax;
	int bgAlpha = 0;
	int bgTick = 0, waitTicker = 0;

	private void handleBackground() {
		if(bgTick > 4) {
			bgTick = 0;
		}
		
		if(bgAlpha <= 0) {
			bgTick++;
			bgAlphaMax = false;
		}
		if(bgAlpha >= 650) {
			bgAlphaMax = true;
		}
		waitTicker += 4;

		if(bgAlphaMax) {
			waitTicker += 1;
			if(waitTicker >= 750) {
				waitTicker = 0;
			}
			if(waitTicker >= 600) {
				bgAlpha -= 10;
				Client.instance.backgroundimage[bgTick].drawSprite(0, 0);
			} else {
				Client.instance.backgroundimage[bgTick].drawSprite(0, 0);
			}
		} else {
			bgAlpha += 10;
			Client.instance.backgroundimage[bgTick > 4 ? 0 : bgTick].drawSprite(0, 0);
		}
		
	}
    
    public boolean animatedbackground = true;
    
    public boolean rememberMe = true;
 
	private final Sprite[] backgroundimage;
	
	public void drawTooltip(int x, int y, String[] text, String[] text1, int info, int colour) {
		if (text != null) {
			int width = 0;
			for (String element : text) {
				if (newSmallFont.getTextWidth(element) > width) {
					width = newSmallFont.getTextWidth(element) + 10;
				}
			}
			int height = (text.length * 15) + 8;
			Rasterizer2D.drawRoundedRectangle(x, y + 30, width, height + 30, 0, 150, true, true);
			for (int index = 0; index < text.length; index++) {
				newSmallFont.drawBasicString(text[index], x + 5, y + 47 + (15 * index), colour, 0);
				newSmallFont.drawBasicString(text1[index], x + 5, y + 70 + (15 * index), colour, 0);
			}
		}
	}

	public void drawTooltip(int x, int y, String[] text, int info, int colour) {
		if (text != null) {
			int width = 0;

			for (String element : text) {
				if (newRegularFont.getTextWidth(element) > width) {
					width = newRegularFont.getTextWidth(element) + 10;
				}
			}
			int height = (text.length * 15) + 8;
			Rasterizer2D.drawRoundedRectangle(x, y, width, height, 0, 150, true, false);

			for (int index = 0; index < text.length; index++) {
				newRegularFont.drawBasicString(text[index], x + 5, y + 15 + (15 * index), colour, 0);
			}
		}
	}
	
	private static boolean available() {
	    try (Socket ignored = new Socket(" h2765662.stratoserver.net", 43595)) {
	        return false;
	    } catch (IOException ignored) {
	        return true;
	    }
	}
	
	private static Sprite[] ProfilePic;
	
	public enum LoginState {
        LOGINSCREEN, SETTINGS, LOGINMESSAGES;
    }

	public static LoginState loginstate = LoginState.LOGINSCREEN;
	
	private FadingScreen fadingScreen = new FadingScreen();
    
    private void drawLoginScreen(boolean flag) {
        setupLoginScreen();
        loginBoxImageProducer.initDrawingArea();
        if (Configuration.enableMusic && !lowMemory) {
            playSong(SoundConstants.SCAPE_RUNE);
        }  
        	
        	if(animatedbackground) {
        		handleBackground();
        	} else {
        		spriteCache.lookup(1150).drawAdvancedSprite(0, 0);
        	}

        	if(loginstate == LoginState.LOGINSCREEN) {
        		spriteCache.lookup(1159).drawAdvancedSprite(215, 10); //Login Box
        		spriteCache.lookup(1164).drawHoverSprite(255, 222, spriteCache.lookup(1165)); //Username
        		spriteCache.lookup(1166).drawHoverSprite(255, 272, spriteCache.lookup(1167)); //Password
        		spriteCache.lookup(1160).drawHoverSprite(283, 350, spriteCache.lookup(1161)); //Login Button
        		if ((loginScreenCursorPos == 0) & (tick % 40 < 20)) {
        			FontArchiver.getFont(4).drawString1(myUsername + "|", 293,245,0xD3D3D3,false,false);
        		} else {
        			FontArchiver.getFont(4).drawString1(myUsername, 293,245,0xD3D3D3,false,false);
        		}
        		// password
        		String password = StringUtils.passwordAsterisks(myPassword);
        		if ((loginScreenCursorPos == 1) & (tick % 40 < 20)) {
        			FontArchiver.getFont(4).drawString1(password + "|", 293,293,0xD3D3D3,false,false);
        		} else {
            		FontArchiver.getFont(4).drawString1(password, 293,293,0xD3D3D3,false,false);
        		}
        		
        		FontArchiver.getFont(3).drawString1("Remeber Me", 283,335,0xD3D3D3,false,false);
        		
        		spriteCache.lookup(rememberMe ? 1163 : 1162).drawHoverSprite(255, 320,spriteCache.lookup(rememberMe ? 1163 : 1162)); //Remember Me
        		//spriteCache.lookup(rememberMe ? 1163 : 1162).drawTooltip(255, 320,spriteCache.lookup(rememberMe ? 1163 : 1162), String[] = {"YOOOO"}, 0xffffff);
        		
        		int CHANGE_Y = 195;
        		
        		final int centerX = Client.frameWidth / 2, centerY = Client.frameHeight / 2;
        		int accounts = AccountManager.getAccounts().size();
        		int init_take = accounts == 2 ? 80 : accounts >= 3 ? 140 : 0;
        		int x = centerX - init_take;
        		if (AccountManager.accounts != null) {
        			for (int index = 0; index < 3; index++, x += (accounts == 2 ? init_take*2 : init_take)) {
        				if (index >= accounts) {
                    		break;
        				}
        				AccountData account = AccountManager.getAccounts().get(index);
        				if (account != null) {
        					try {
        						URL url = new URL("http://overloadps.com/forum/uploads/"+account.profileUrl);
        						//System.out.println("https://overloadps.com/forum/uploads/"+account.profileUrl);
        						if (url != null) {
	        						if (ProfilePic[index] == null) {
	        							ProfilePic[index] = new AnimatedSprite(url,60,60);
	        						}
        						}
        					} catch (MalformedURLException e) {//login with your account i cant move lol
        						//hokay
        						e.printStackTrace();
        					}
        					
        					
        					
        					
        					spriteCache.lookup(1170).drawAdvancedSprite((x - (spriteCache.lookup(1170).myWidth/2)) - 3, (centerY - (spriteCache.lookup(1170).myHeight/2)) + CHANGE_Y);
        					spriteCache.lookup(1170).drawAdvancedSprite((x - (spriteCache.lookup(1170).myWidth/2)) - 3, (centerY - (spriteCache.lookup(1170).myHeight/2)) + CHANGE_Y);
        					
        				
        					
        					spriteCache.lookup(1176).drawSprite((x - (spriteCache.lookup(1176).myWidth/2)) - 1, (centerY - (spriteCache.lookup(1176).myHeight/2)) + CHANGE_Y - 5);
        					ProfilePic[index].drawAdvancedSprite((x - (ProfilePic[index].myWidth/2)) - 1, (centerY - (ProfilePic[index].myHeight/2)) + CHANGE_Y - 5);
        					spriteCache.lookup(1172).drawHoverSprite((x - (spriteCache.lookup(1172).myWidth/2)) + 33, (centerY - (spriteCache.lookup(1172).myHeight/2)) + CHANGE_Y - 37,spriteCache.lookup(1173));
        					if (clickMode3 == 1 && newclickInRegion((x - (spriteCache.lookup(1172).myWidth/2)) + 33, (centerY - (spriteCache.lookup(1172).myHeight/2)) + CHANGE_Y - 37,spriteCache.lookup(1173))) {
        						AccountManager.removeAccount(account);
        					}
        					
        					
        					
        					
        					spriteCache.lookup(1203).drawAdvancedSprite((x - (spriteCache.lookup(1203).myWidth/2)) - 35, (centerY - (spriteCache.lookup(1203).myHeight/2)) + CHANGE_Y - 38);
        					spriteCache.lookup(1203).drawAdvancedSprite((x - (spriteCache.lookup(1203).myWidth/2)) - 15, (centerY - (spriteCache.lookup(1203).myHeight/2)) + CHANGE_Y - 38);
        					
        					int divisible = 2;
        					
        					if (account.rank > 0 && account.rank < 14) {
        						if (account.mode > 0 && account.mode < 3) {
        							divisible = 1;
        						}
        		                modIcons[account.rank-1].drawAdvancedSprite(
			                		(x - modIcons[account.rank-1].myWidth/divisible) - 35,
			                		(centerY - (spriteCache.lookup(1170).myHeight/2)) + CHANGE_Y + 1
        		                );
        		            }

        		            if (account.mode > 0 && account.mode < 3) {
        		            	if (account.mode > 0 && account.mode < 3) {
        							divisible = 1;
        						} 
        		            	modeIcons[account.mode-1].drawAdvancedSprite(
      			                		(x - modeIcons[account.mode-1].myWidth/divisible) - 35,
      			                		(centerY - (spriteCache.lookup(1170).myHeight/2)) + CHANGE_Y + 1
              		             );
        		            }
        					
        		            if (ProfilePic[index] != null) {
	        					if (clickMode3 == 1 && newclickInRegion((x - (ProfilePic[index].myWidth/2)) - 1, (centerY - (ProfilePic[index].myHeight/2)) + CHANGE_Y - 5, ProfilePic[index])) {
	        						if (account.username.length() > 0 && account.password.length() > 0) {
	                                    loginFailures = 0;
	                    				if (myUsername != account.username || myPassword != account.password) {
	                    					myUsername = account.username;
	                    					myPassword = account.password;
	                    				}
	                                    login(account.username, account.password, true);
	                                    if (loggedIn) {
	                                       return;
	                                    }
	                            	}
	        					}
        		            }
        					
        					spriteCache.lookup(1171).drawAdvancedSprite((x - (spriteCache.lookup(1171).myWidth/2))-(3*(index+1))+(index*3), (centerY - (spriteCache.lookup(1170).myHeight/2)) + CHANGE_Y + 73);
        					FontArchiver.getFont(4).drawString1(
    							capitalize(account.username),
    							x - (FontArchiver.getFont(4).stringWidth(account.username)/2),
    							centerY + CHANGE_Y + 43,
    							0xFFFFFFF,
    							false,
    							false
        					);
        				}
        			}
        		}
        		
                
                
        		
        	}
    		
    		if(loginstate == LoginState.LOGINMESSAGES) {
    			Rasterizer2D.drawTransparentBox(0, 0, frameWidth, frameHeight, 0x000000, 200);
    			FontArchiver.getFont(3).drawString1(
					loginMessage,
					(frameWidth / 2) - (FontArchiver.getFont(3).stringWidth(loginMessage)/2),
					frameHeight / 2,
					0xD3D3D3,
					false,
					false
    			);
    			FontArchiver.getFont(3).drawString1(
    				"(Click Anywhere To Close)",
    				(frameWidth / 2) - (FontArchiver.getFont(3).stringWidth("(Click Anywhere To Close)")/2),
    				frameHeight / 2 + 20,
    				0x6C6C6C,
    				false,
    				false
    			);
    		}
    		
    		/****** Top Bar ******/
    		spriteCache.lookup(1178).drawHoverSprite(0, 0, spriteCache.lookup(1179));
    		spriteCache.lookup(1180).drawHoverSprite(41, 0, spriteCache.lookup(1181));
    		spriteCache.lookup(1182).drawHoverSprite(107, 0, spriteCache.lookup(1183));
    		spriteCache.lookup(1184).drawHoverSprite(216, 0, spriteCache.lookup(1185));
    		spriteCache.lookup(1186).drawHoverSprite(304, 0, spriteCache.lookup(1187));
    		spriteCache.lookup(1188).drawHoverSprite(366, 0, spriteCache.lookup(1189));
    		spriteCache.lookup(1190).drawHoverSprite(435, 0, spriteCache.lookup(1193));
    		spriteCache.lookup(1194).drawAdvancedSprite(619, 0);
    		
    		if(available()) {
    			spriteCache.lookup(1192).drawAdvancedSprite(540, 5);
    		} else {
    			spriteCache.lookup(1191).drawAdvancedSprite(540, 5);
    		}
    		
    		spriteCache.lookup(1195).drawHoverSprite(620, 4, spriteCache.lookup(1196));
    		spriteCache.lookup(1197).drawHoverSprite(655, 4, spriteCache.lookup(1198));
    		spriteCache.lookup(1199).drawHoverSprite(690, 4, spriteCache.lookup(1200));
    		spriteCache.lookup(1201).drawHoverSprite(724, 4, spriteCache.lookup(1202));
    		
    		/****** End Top Bar ******/
    	
        loginBoxImageProducer.drawGraphics(0, super.graphics, 0);
    }
    
    public static void setLoginState(LoginState screenMode) {
    	loginstate = screenMode;
    }
    
    private void processLoginScreenInput() {
    	if(loginstate == LoginState.LOGINMESSAGES) {
    		if (clickMode3 == 1 && newclickInRegion(0, 0,frameWidth, frameHeight)) {
    			setLoginState(LoginState.LOGINSCREEN);
    		}
    		return;
    	}
    	if(loginstate == LoginState.LOGINSCREEN) {
    		if (super.clickMode3 == 1) {
    			if (clickMode3 == 1 && newclickInRegion(255, 223, spriteCache.lookup(1167))) {
    				loginScreenCursorPos = 0;
    			} else if (clickMode3 == 1 && newclickInRegion(255, 273, spriteCache.lookup(1167))) {
    				loginScreenCursorPos = 1;
    			} else if (clickMode3 == 1 && newclickInRegion(256, 320,spriteCache.lookup(rememberMe ? 1162 : 1163))) {
    				rememberMe = !rememberMe;
    			} else if (clickMode3 == 1 && newclickInRegion(283, 350, spriteCache.lookup(1161))) {
    				login(myUsername, myPassword, false);
    			}
    		}
    	}
		do {
			int l1 = readChar(-796);
			if (l1 == -1)
				break;
			boolean flag1 = false;
			for (int i2 = 0; i2 < validUserPassChars.length(); i2++) {
				if (l1 != validUserPassChars.charAt(i2))
					continue;
				flag1 = true;
				break;
			}

			if (loginScreenCursorPos == 0) {
				if (l1 == 8 && myUsername.length() > 0)
					myUsername = myUsername.substring(0,
							myUsername.length() - 1);
				if (l1 == 9 || l1 == 10 || l1 == 13)
					loginScreenCursorPos = 1;
				if (flag1)
					myUsername += (char) l1;
				if (myUsername.length() > 12)
					myUsername = myUsername.substring(0, 12);
				if (myUsername.length() > 0) {
					myUsername = StringUtils.formatText(StringUtils
							.capitalize(myUsername));
				}
			} else if (loginScreenCursorPos == 1) {
				if (l1 == 8 && myPassword.length() > 0)
					myPassword = myPassword.substring(0,
							myPassword.length() - 1);
				if (l1 == 9) {
					loginScreenCursorPos = 0;
				} else if (l1 == 10 || l1 == 13) {
					login(myUsername, myPassword, false);
					return;
				}
				if (flag1)
					myPassword += (char) l1;
				if (myPassword.length() > 15)
					myPassword = myPassword.substring(0, 15);
			}
		} while (true);
		return;
	}

    private void drawFlames() {
       
    }

    public void raiseWelcomeScreen() {
        welcomeScreenRaised = true;
    }

    private void parseRegionPackets(Buffer stream, int packetType) {
        if (packetType == PacketConstants.SEND_ALTER_GROUND_ITEM_COUNT) {
            int offset = stream.readUnsignedByte();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int itemId = stream.readUShort();
            int oldItemCount = stream.readUShort();
            int newItemCount = stream.readUShort();
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 104 && yLoc < 104) {
                Deque groundItemsDeque = groundItems[plane][xLoc][yLoc];
                if (groundItemsDeque != null) {
                    for (Item groundItem = (Item) groundItemsDeque
                            .reverseGetFirst(); groundItem != null; groundItem =
                                 (Item) groundItemsDeque.reverseGetNext()) {
                        if (groundItem.ID != (itemId & 0x7fff)
                                || groundItem.itemCount != oldItemCount)
                            continue;
                        groundItem.itemCount = newItemCount;
                        break;
                    }

                    updateGroundItems(xLoc, yLoc);
                }
            }
            return;
        }
        if (packetType == 105) {
            int l = stream.readUnsignedByte();
            int k3 = localX + (l >> 4 & 7);
            int j6 = localY + (l & 7);
            int i9 = stream.readUShort();
            int l11 = stream.readUnsignedByte();
            int i14 = l11 >> 4 & 0xf;
            int i16 = l11 & 7;
            if (localPlayer.pathX[0] >= k3 - i14 && localPlayer.pathX[0] <= k3 + i14
                    && localPlayer.pathY[0] >= j6 - i14
                    && localPlayer.pathY[0] <= j6 + i14 && aBoolean848 && !lowMemory
                    && trackCount < 50) {
                tracks[trackCount] = i9;
                trackLoops[trackCount] = i16;
                soundDelay[trackCount] = Track.delays[i9];
                trackCount++;
            }
        }
        if (packetType == 215) {
            int i1 = stream.readUShortA();
            int l3 = stream.readUByteS();
            int k6 = localX + (l3 >> 4 & 7);
            int j9 = localY + (l3 & 7);
            int i12 = stream.readUShortA();
            int j14 = stream.readUShort();
            if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != localPlayerIndex) {
                Item class30_sub2_sub4_sub2_2 = new Item();
                class30_sub2_sub4_sub2_2.ID = i1;
                class30_sub2_sub4_sub2_2.itemCount = j14;
                if (groundItems[plane][k6][j9] == null)
                    groundItems[plane][k6][j9] = new Deque();
                groundItems[plane][k6][j9].insertHead(class30_sub2_sub4_sub2_2);
                updateGroundItems(k6, j9);
            }
            return;
        }
        if (packetType == PacketConstants.SEND_REMOVE_GROUND_ITEM) {
            int offset = stream.readUByteA();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int itemId = stream.readUShort();
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 104 && yLoc < 104) {
                Deque groundItemsDeque = groundItems[plane][xLoc][yLoc];
                if (groundItemsDeque != null) {
                    for (Item item =
                         (Item) groundItemsDeque.reverseGetFirst(); item != null; item =
                                 (Item) groundItemsDeque.reverseGetNext()) {
                        if (item.ID != (itemId & 0x7fff))
                            continue;
                        item.unlink();
                        break;
                    }

                    if (groundItemsDeque.reverseGetFirst() == null)
                        groundItems[plane][xLoc][yLoc] = null;
                    updateGroundItems(xLoc, yLoc);
                }
            }
            return;
        }
        if (packetType == PacketConstants.ANIMATE_OBJECT) {
            int offset = stream.readUByteS();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int objectTypeFace = stream.readUByteS();
            int objectType = objectTypeFace >> 2;
            int objectFace = objectTypeFace & 3;
            int objectGenre = objectGroups[objectType];
            int animId = stream.readUShortA();
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 103 && yLoc < 103) {
                int heightA = tileHeights[plane][xLoc][yLoc];
                int heightB = tileHeights[plane][xLoc + 1][yLoc];
                int heightC = tileHeights[plane][xLoc + 1][yLoc + 1];
                int heightD = tileHeights[plane][xLoc][yLoc + 1];
                if (objectGenre == 0) {//WallObject
                    WallObject wallObjectObject = scene.getWallObject(plane, xLoc, yLoc);
                    if (wallObjectObject != null) {
                        int objectId = wallObjectObject.uid >> 14 & 0x7fff;
                        if (objectType == 2) {
                            wallObjectObject.renderable1 = new SceneObject(objectId, 4 + objectFace, 2, heightB, heightC, heightA, heightD, animId, false);
                            wallObjectObject.renderable2 = new SceneObject(objectId, objectFace + 1 & 3, 2, heightB, heightC, heightA, heightD, animId, false);
                        } else {
                            wallObjectObject.renderable1 = new SceneObject(objectId, objectFace, objectType, heightB, heightC, heightA, heightD, animId, false);
                        }
                    }
                }
                if (objectGenre == 1) { //WallDecoration
                    WallDecoration wallDecoration = scene.getWallDecoration(xLoc, yLoc, plane);
                    if (wallDecoration != null)
                        wallDecoration.renderable = new SceneObject(wallDecoration.uid >> 14 & 0x7fff, 0, 4, heightB, heightC, heightA, heightD, animId, false);
                }
                if (objectGenre == 2) { //TiledObject
                    GameObject tiledObject = scene.getGameObject(xLoc, yLoc, plane);
                    if (objectType == 11)
                        objectType = 10;
                    if (tiledObject != null)
                        tiledObject.renderable = new SceneObject(tiledObject.uid >> 14 & 0x7fff, objectFace, objectType, heightB, heightC, heightA, heightD, animId, false);
                }
                if (objectGenre == 3) { //GroundDecoration
                    GroundDecoration groundDecoration = scene.getGroundDecoration(yLoc, xLoc, plane);
                    if (groundDecoration != null)
                        groundDecoration.renderable = new SceneObject(groundDecoration.uid >> 14 & 0x7fff, objectFace, 22, heightB, heightC, heightA, heightD, animId, false);
                }
            }
            return;
        }
        if (packetType == PacketConstants.TRANSFORM_PLAYER_TO_OBJECT) {
            int offset = stream.readUByteS();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int playerIndex = stream.readUShort();
            byte byte0GreaterXLoc = stream.readByteS();
            int startDelay = stream.readLEUShort();
            byte byte1GreaterYLoc = stream.readNegByte();
            int stopDelay = stream.readUShort();
            int objectTypeFace = stream.readUByteS();
            int objectType = objectTypeFace >> 2;
            int objectFace = objectTypeFace & 3;
            int objectGenre = objectGroups[objectType];
            byte byte2LesserXLoc = stream.readSignedByte();
            int objectId = stream.readUShort();
            byte byte3LesserYLoc = stream.readNegByte();
            Player player;
            if (playerIndex == localPlayerIndex)
                player = localPlayer;
            else
                player = players[playerIndex];
            if (player != null) {
                ObjectDefinition objectDefinition = ObjectDefinition.lookup(objectId);
                int heightA = tileHeights[plane][xLoc][yLoc];
                int heightB = tileHeights[plane][xLoc + 1][yLoc];
                int heightC = tileHeights[plane][xLoc + 1][yLoc + 1];
                int heightD = tileHeights[plane][xLoc][yLoc + 1];
                Model model = objectDefinition.modelAt(objectType, objectFace, heightA, heightB, heightC, heightD, -1);
                if (model != null) {
                    requestSpawnObject(stopDelay + 1, -1, 0, objectGenre, yLoc, 0, plane, xLoc, startDelay + 1);
                    player.objectModelStart = startDelay + tick;
                    player.objectModelStop = stopDelay + tick;
                    player.playerModel = model;
                    int playerSizeX = objectDefinition.objectSizeX;
                    int playerSizeY = objectDefinition.objectSizeY;
                    if (objectFace == 1 || objectFace == 3) {
                        playerSizeX = objectDefinition.objectSizeY;
                        playerSizeY = objectDefinition.objectSizeX;
                    }
                    player.objectXPos = xLoc * 128 + playerSizeX * 64;
                    player.objectYPos = yLoc * 128 + playerSizeY * 64;
                    player.objectCenterHeight = getCenterHeight(plane, player.objectYPos, player.objectXPos);
                    if (byte2LesserXLoc > byte0GreaterXLoc) {
                        byte tmp = byte2LesserXLoc;
                        byte2LesserXLoc = byte0GreaterXLoc;
                        byte0GreaterXLoc = tmp;
                    }
                    if (byte3LesserYLoc > byte1GreaterYLoc) {
                        byte tmp = byte3LesserYLoc;
                        byte3LesserYLoc = byte1GreaterYLoc;
                        byte1GreaterYLoc = tmp;
                    }
                    player.objectAnInt1719LesserXLoc = xLoc + byte2LesserXLoc;
                    player.objectAnInt1721GreaterXLoc = xLoc + byte0GreaterXLoc;
                    player.objectAnInt1720LesserYLoc = yLoc + byte3LesserYLoc;
                    player.objectAnInt1722GreaterYLoc = yLoc + byte1GreaterYLoc;
                }
            }
        }
        if (packetType == PacketConstants.SEND_OBJECT) {
            int offset = stream.readUByteA();
            int x = localX + (offset >> 4 & 7);
            int y = localY + (offset & 7);
            int id = stream.readLEUShort();
            int objectTypeFace = stream.readUByteS();
            int type = objectTypeFace >> 2;
            int orientation = objectTypeFace & 3;
            int group = objectGroups[type];
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                requestSpawnObject(-1, id, orientation, group, y, type, plane, x, 0);
            }
            return;
        }
        if (packetType == PacketConstants.SEND_GFX) {
            int offset = stream.readUnsignedByte();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int gfxId = stream.readUShort();
            int gfxHeight = stream.readUnsignedByte();
            int gfxDelay = stream.readUShort();
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 104 && yLoc < 104) {
                xLoc = xLoc * 128 + 64;
                yLoc = yLoc * 128 + 64;
                AnimableObject loneGfx = new AnimableObject(plane, tick,
                        gfxDelay, gfxId, getCenterHeight(plane, yLoc, xLoc) - gfxHeight, yLoc, xLoc);
                incompleteAnimables.insertHead(loneGfx);
            }
            return;
        }
        if (packetType == PacketConstants.SEND_GROUND_ITEM) {
            int itemId = stream.readLEUShortA();
            int itemCount = stream.readInt();
            int offset = stream.readUnsignedByte();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 104 && yLoc < 104) {
                Item groundItem = new Item();
                groundItem.ID = itemId;
                groundItem.itemCount = itemCount;
                if (groundItems[plane][xLoc][yLoc] == null)
                    groundItems[plane][xLoc][yLoc] = new Deque();
                groundItems[plane][xLoc][yLoc].insertHead(groundItem);
                updateGroundItems(xLoc, yLoc);
            }
            return;
        }
        if (packetType == PacketConstants.SEND_REMOVE_OBJECT) {
            int objectTypeFace = stream.readNegUByte();
            int type = objectTypeFace >> 2;
            int orientation = objectTypeFace & 3;
            int group = objectGroups[type];
            int offset = stream.readUnsignedByte();
            int x = localX + (offset >> 4 & 7);
            int y = localY + (offset & 7);
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                requestSpawnObject(-1, -1, orientation, group, y, type, plane, x, 0);
            }
            return;
        }
       
        if (packetType == PacketConstants.SEND_PROJECTILE) {
            int offset = stream.readUnsignedByte();
            int x1 = localX + (offset >> 4 & 7);
            int y1 = localY + (offset & 7);
            int x2 = x1 + stream.readSignedByte();
            int y2 = y1 + stream.readSignedByte();
            int target = stream.readShort();
            int gfxMoving = stream.readUShort();
            int startHeight = stream.readUnsignedByte() * 4;
            int endHeight = stream.readUnsignedByte() * 4;
            int startDelay = stream.readUShort();
            int speed = stream.readUShort();
            int initialSlope = stream.readUnsignedByte();
            int frontOffset = stream.readUnsignedByte();
            if (x1 >= 0 && y1 >= 0 && x1 < 104 && y1 < 104 && x2 >= 0 && y2 >= 0
                    && x2 < 104 && y2 < 104 && gfxMoving != 65535) {
                x1 = x1 * 128 + 64;
                y1 = y1 * 128 + 64;
                x2 = x2 * 128 + 64;
                y2 = y2 * 128 + 64;
                Projectile projectile = new Projectile(initialSlope, endHeight, startDelay + tick, speed + tick, frontOffset, plane, getCenterHeight(plane, y1, x1) - startHeight, y1, x1, target, gfxMoving);
                projectile.calculateIncrements(startDelay + tick, y2, getCenterHeight(plane, y2, x2) - endHeight, x2);
                projectiles.insertHead(projectile);
            }
        }
    }

    private void method139(Buffer stream) {
        stream.initBitAccess();
        int k = stream.readBits(8);
        if (k < npcCount) {
            for (int l = k; l < npcCount; l++)
                removedMobs[removedMobCount++] = npcIndices[l];

        }
        if (k > npcCount) {
            Signlink.reporterror(myUsername + " Too many npcs");
            throw new RuntimeException("eek");
        }
        npcCount = 0;
        for (int i1 = 0; i1 < k; i1++) {
            int npcIndex = npcIndices[i1];
            Npc npc = npcs[npcIndex];
            npc.index = npcIndex;
            int k1 = stream.readBits(1);
            if (k1 == 0) {
                npcIndices[npcCount++] = npcIndex;
                npc.time = tick;
            } else {
                int l1 = stream.readBits(2);
                if (l1 == 0) {
                    npcIndices[npcCount++] = npcIndex;
                    npc.time = tick;
                    mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = npcIndex;
                } else if (l1 == 1) {
                    npcIndices[npcCount++] = npcIndex;
                    npc.time = tick;
                    int i2 = stream.readBits(3);
                    npc.moveInDir(false, i2);
                    int k2 = stream.readBits(1);
                    if (k2 == 1)
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = npcIndex;
                } else if (l1 == 2) {
                    npcIndices[npcCount++] = npcIndex;
                    npc.time = tick;
                    int j2 = stream.readBits(3);
                    npc.moveInDir(true, j2);
                    int l2 = stream.readBits(3);
                    npc.moveInDir(true, l2);
                    int i3 = stream.readBits(1);
                    if (i3 == 1)
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = npcIndex;
                } else if (l1 == 3)
                    removedMobs[removedMobCount++] = npcIndex;
            }
        }

    }

    public boolean newclickInRegion(int x1, int y1, Sprite drawnSprite) {
		if (super.saveClickX >= x1 && super.saveClickX <= x1 + drawnSprite.myWidth && super.saveClickY >= y1
				&& super.saveClickY <= y1 + drawnSprite.myHeight)
			return true;
		return false;
	}

	public boolean newclickInRegion(int x1, int y1, int x2, int y2) {
		if (super.saveClickX >= x1 && super.saveClickX <= x1 + x2 && super.saveClickY >= y1
				&& super.saveClickY <= y1 + y2)
			return true;
		return false;
	}

	public boolean newmouseInRegion(int x1, int y1, Sprite drawnSprite) {
		if (super.mouseX >= x1 && super.mouseX <= x1 + drawnSprite.myWidth && super.mouseY >= y1
				&& super.mouseY <= y1 + drawnSprite.myHeight)
			return true;
		return false;
	}
	
    private void removeObject(int y, int z, int k, int l, int x, int group, int previousId) {
        if (x >= 1 && y >= 1 && x <= 102 && y <= 102) {
            if (lowMemory && z != plane)
                return;
            int key = 0;
            if (group == 0)
                key = scene.getWallObjectUid(z, x, y);
            if (group == 1)
                key = scene.getWallDecorationUid(z, x, y);
            if (group == 2)
                key = scene.getGameObjectUid(z, x, y);
            if (group == 3)
                key = scene.getGroundDecorationUid(z, x, y);
            if (key != 0) {
                int config = scene.getMask(z, x, y, key);
                int id = key >> 14 & 0x7fff;
                int objectType = config & 0x1f;
                int orientation = config >> 6;

                if (group == 0) {
                    scene.removeWallObject(x, z, y);
                    ObjectDefinition objectDef = ObjectDefinition.lookup(id);
                    if (objectDef.solid)
                        collisionMaps[z].removeObject(orientation, objectType,
                                objectDef.impenetrable, x, y);
                }
                if (group == 1)
                    scene.removeWallDecoration(y, z, x);
                if (group == 2) {
                    scene.removeTiledObject(z, x, y);
                    ObjectDefinition objectDef = ObjectDefinition.lookup(id);
                    if (x + objectDef.objectSizeX > 103 || y + objectDef.objectSizeX > 103
                            || x + objectDef.objectSizeY > 103
                            || y + objectDef.objectSizeY > 103)
                        return;
                    if (objectDef.solid)
                        collisionMaps[z].removeObject(orientation, objectDef.objectSizeX, x,
                                y, objectDef.objectSizeY, objectDef.impenetrable);
                }
                if (group == 3) {
                    scene.removeGroundDecoration(z, y, x);
                    ObjectDefinition objectDef = ObjectDefinition.lookup(id);
                    if (objectDef.solid && objectDef.isInteractive)
                        collisionMaps[z].removeFloorDecoration(y, x);
                }
            }
            if (previousId >= 0) {
                int plane = z;
                if (plane < 3 && (tileFlags[1][x][y] & 2) == 2)
                    plane++;
                MapRegion.placeObject(scene, k, y, l, plane, collisionMaps[z], tileHeights,
                        x, previousId, z);
            }
        }
    }

    private void updatePlayers(int packetSize, Buffer stream) {
        removedMobCount = 0;
        mobsAwaitingUpdateCount = 0;
        updateLocalPlayerMovement(stream);
        updateOtherPlayerMovement(stream);
        updatePlayerList(stream, packetSize);
        parsePlayerSynchronizationMask(stream);
        for (int count = 0; count < removedMobCount; count++) {
            int index = removedMobs[count];

            if (players[index].time != tick) {
                players[index] = null;
            }
        }

        if (stream.currentPosition != packetSize) {
            Signlink.reporterror("Error packet size mismatch in getplayer pos:"
                    + stream.currentPosition + " psize:" + packetSize);
            throw new RuntimeException("eek");
        }
        for (int count = 0; count < playerCount; count++) {
            if (players[playerList[count]] == null) {
                Signlink.reporterror(myUsername + " null entry in pl list - pos:" + count
                        + " size:" + playerCount);
                throw new RuntimeException("eek");
            }
        }

    }

    private void setCameraPos(int j, int k, int l, int i1, int j1, int k1) {
        int l1 = 2048 - k & 0x7ff;
        int i2 = 2048 - j1 & 0x7ff;
        int j2 = 0;
        int k2 = 0;
        int l2 = j;
        if (l1 != 0) {
            int i3 = Model.SINE[l1];
            int k3 = Model.COSINE[l1];
            int i4 = k2 * k3 - l2 * i3 >> 16;
            l2 = k2 * i3 + l2 * k3 >> 16;
            k2 = i4;
        }
        if (i2 != 0) {
            int j3 = Model.SINE[i2];
            int l3 = Model.COSINE[i2];
            int j4 = l2 * j3 + j2 * l3 >> 16;
            l2 = l2 * l3 - j2 * j3 >> 16;
            j2 = j4;
        }
        xCameraPos = l - j2;
        zCameraPos = i1 - k2;
        yCameraPos = k1 - l2;
        yCameraCurve = k;
        xCameraCurve = j1;
    }

    /**
     * This method updates default messages upon login to the desired text of the interface text.
     */
    public void updateStrings(String message, int index) {
		
    	switch (index) {
            case 1675:
                sendString(message, 17508);
                break;// Stab
            case 1676:
                sendString(message, 17509);
                break;// Slash
            case 1677:
                sendString(message, 17510);
                break;// Crush
            case 1678:
                sendString(message, 17511);
                break;// Magic
            case 1679:
                sendString(message, 17512);
                break;// Range
            case 1680:
                sendString(message, 17513);
                break;// Stab
            case 1681:
                sendString(message, 17514);
                break;// Slash
            case 1682:
                sendString(message, 17515);
                break;// Crush
            case 1683:
                sendString(message, 17516);
                break;// Magic
            case 1684:
                sendString(message, 17517);
                break;// Range
            case 1686:
                sendString(message, 17518);
                break;// Strength
            case 1687:
                sendString(message, 17519);
                break;// Prayer
        }
    }

    /**
     * Sends a string
     */
    public void sendString(String text, int index) {
        if (Widget.interfaceCache[index] == null) {
            return;
        }	
        Widget.interfaceCache[index].defaultText = text;
        if (Widget.interfaceCache[index].parent == tabInterfaceIDs[tabId]) {
        }
    }

    public void sendButtonClick(int button, int toggle, int type) {
        Widget widget = Widget.interfaceCache[button];
        switch (type) {
            case 135:
                boolean flag8 = true;

                if (widget.contentType > 0) {
                    flag8 = promptUserForInput(widget);
                }

                if (flag8) {
                    sendPacket(new ClickButton(button).create());
                }
                break;

            // case reset setting widget
            case 646:
                sendPacket(new ClickButton(button).create());

                if (widget.valueIndexArray != null && widget.valueIndexArray[0][0] == 5) {
                    if (settings[toggle] != widget.requiredValues[0]) {
                        settings[toggle] = widget.requiredValues[0];
                        updateVarp(toggle);
                    }
                }
                break;

            case 169:
                sendPacket(new ClickButton(button).create());
                if (widget.valueIndexArray != null && widget.valueIndexArray[0][0] == 5) {
                    settings[toggle] = 1 - settings[toggle];
                    updateVarp(toggle);
                }
                break;

            default:
                System.out.println("button: " + button + " - toggle: " + toggle
                        + " - type: " + type);
                break;
        }
    }

    /**
     * Sets button configurations on interfaces.
     */
    public void sendConfiguration(int id, int state) {
        configValues[id] = state;
        if (settings[id] != state) {
            settings[id] = state;
            updateVarp(id);
            if (dialogueId != -1)
                updateChatbox = true;
        }
    }

    /**
     * Clears the screen of all open interfaces.
     */
    public void clearScreen() {
        if (overlayInterfaceId != -1) {
            overlayInterfaceId = -1;
            tabAreaAltered = true;
        }
        if (backDialogueId != -1) {
            backDialogueId = -1;
            updateChatbox = true;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            updateChatbox = true;
        }
        openInterfaceId = -1;
        continuedDialogue = false;
    }

    /**
     * Displays an interface over the sidebar area.
     */
    public void inventoryOverlay(int interfaceId, int sideInterfaceId) {
        if (backDialogueId != -1) {
            backDialogueId = -1;
            updateChatbox = true;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            updateChatbox = true;
        }
        openInterfaceId = interfaceId;
        overlayInterfaceId = sideInterfaceId;
        tabAreaAltered = true;
        continuedDialogue = false;
    }
    
    public void sendPacket(PacketBuilder builder) {
        //Make sure we're logged in and that we can encrypt our packet.
        if (!loggedIn || cipher == null) {
            ping_packet_counter = 0;
            return;
        }
        
        byte[] buffer = new byte[builder.getPosition() + 2];

        // Put opcode
        buffer[0] = (byte) (builder.getOpcode() + cipher.getNextKey());

        // Put size
        buffer[1] = (byte) (builder.getPosition());

        // Copy rest of the packet data
        for (int i = 2; i < buffer.length; i++) {
        	buffer[i] = builder.getBuffer()[i - 2];
        }

        outBuffer.writeBytes(buffer);
        ping_packet_counter = 0;
    }

    private boolean readPacket() {

        if (socketStream == null) {
            return false;
        }

        try {

            int available = socketStream.available();
            if (available < 2) {
                return false;
            }

            //First we read opcode...
            if (opcode == -1) {

                socketStream.flushInputStream(incoming.payload, 1);

                opcode = incoming.payload[0] & 0xff;

                if (encryption != null) {
                    opcode = opcode - encryption.getNextKey() & 0xff;
                }

                //Now attempt to read packet size..
                socketStream.flushInputStream(incoming.payload, 2);
                packetSize = ((incoming.payload[0] & 0xff) << 8)
                        + (incoming.payload[1] & 0xff);

            }

            if (!(opcode >= 0 && opcode < 256)) {
                opcode = -1;
                return false;
            }

            incoming.currentPosition = 0;
            socketStream.flushInputStream(incoming.payload, packetSize);

            timeoutCounter = 0;
            thirdLastOpcode = secondLastOpcode;
            secondLastOpcode = lastOpcode;
            lastOpcode = opcode;

            if (opcode == PacketConstants.SET_TOTAL_EXP) {
                totalExp = incoming.readLong();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_EXP_DROP) {
                int skillId = incoming.readUnsignedByte();
                int experience = incoming.readInt();
                if (UserInterface.SKILL_ORBS.toggled()) {
                    SkillOrbs.orbs[skillId].receivedExperience();
                }
                if (Configuration.expCounterOpen) {
                	addToXPCounter(skillId, experience);
                }
                totalExp += experience;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SHOW_TELEPORT_INTERFACE) {
            	incoming.readUnsignedByte();
            	opcode = -1;
            	return true;
            }

            if (opcode == PacketConstants.UPDATE_POISON_TYPE) {
            	poisonType = incoming.readUnsignedByte();
            	opcode = -1;
            	return true;
            }

            if (opcode == PacketConstants.INTERFACE_TEXT_CLEAR) {
            	int textFrom = incoming.readInt();
            	int textTo = incoming.readInt();
            	for (int i = textFrom; i <= textTo; i++) {
            		sendString("", i);
            	}
            	opcode = -1;
            	return true;
            }

            if (opcode == PacketConstants.SET_SCROLLBAR_HEIGHT) {
                int interface_ = incoming.readInt();
                int scrollMax = incoming.readShort();
                Widget w = Widget.interfaceCache[interface_];
                if (w != null) {
                    w.scrollMax = scrollMax;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.INTERFACE_SCROLL_RESET) {
                int interface_ = incoming.readInt();
                Widget w = Widget.interfaceCache[interface_];
                if (w != null) {
                    w.scrollPosition = 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SAVE_ACCOUNT) {
                int right = incoming.readInt();
                int mode = incoming.readInt();
                String username = incoming.readString();
                String profilepic = incoming.readString();
                
                //System.out.println("Rights: " + right + "Mode: " + mode + "Username: " + username + "Password: " + myPassword +  "Profile Pic: " + profilepic );
                
    			final AccountData account = new AccountData(right,mode,username, myPassword,profilepic);
    			if (rememberMe) {
    				AccountManager.addAccount(account);
    				AccountManager.saveAccount();
    			}
    			AccountManager.getAccount(username);
                opcode = -1;
                return true;
            }
            
            if (opcode == PacketConstants.UPDATE_PLAYER_RIGHTS) {
                myPrivilege = incoming.readUnsignedByte();
                myMode = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.PLAYER_UPDATING) {
                updatePlayers(packetSize, incoming);
                loadingMap = false;
                opcode = -1;
                return true;
            }

            if (opcode == 183) {
                try {
                    specialEnabled = incoming.readNegUByte();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_CONSOLE_COMMAND) {
                String msg = incoming.readString();
                console.printMessage(msg, 1);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SHOW_CLANCHAT_OPTIONS) {
                showClanOptions = incoming.readUnsignedByte() == 1;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.OPEN_WELCOME_SCREEN) {
                daysSinceRecovChange = incoming.readNegUByte();
                unreadMessages = incoming.readUShortA();
                membersInt = incoming.readUnsignedByte();
                anInt1193 = incoming.readIMEInt();
                daysSinceLastLogin = incoming.readUShort();
                if (anInt1193 != 0 && openInterfaceId == -1) {
                    Signlink.dnslookup(StringUtils.decodeIp(anInt1193));
                    clearTopInterfaces();
                    char character = '\u028A';
                    if (daysSinceRecovChange != 201 || membersInt == 1)
                        character = '\u028F';
                    reportAbuseInput = "";
                    canMute = false;
                    for (int interfaceId =
                         0; interfaceId < Widget.interfaceCache.length; interfaceId++) {
                        if (Widget.interfaceCache[interfaceId] == null
                                || Widget.interfaceCache[interfaceId].contentType != character)
                            continue;
                        openInterfaceId = Widget.interfaceCache[interfaceId].parent;

                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 178) {
                clearRegionalSpawns();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.DELETE_GROUND_ITEM) {
                localX = incoming.readNegUByte();
                localY = incoming.readUByteS();
                for (int x = localX; x < localX + 8; x++) {
                    for (int y = localY; y < localY + 8; y++)
                        if (groundItems[plane][x][y] != null) {
                            groundItems[plane][x][y] = null;
                            updateGroundItems(x, y);
                        }
                }
                for (SpawnedObject object = (SpawnedObject) spawns
                        .reverseGetFirst(); object != null; object =
                             (SpawnedObject) spawns.reverseGetNext())
                    if (object.x >= localX && object.x < localX + 8 && object.y >= localY
                            && object.y < localY + 8 && object.plane == plane)
                        object.getLongetivity = 0;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SHOW_PLAYER_HEAD_ON_INTERFACE) {
                int playerHeadModelId = incoming.readLEUShortA();
                Widget.interfaceCache[playerHeadModelId].defaultMediaType = 3;
                if (localPlayer.npcDefinition == null)
                    Widget.interfaceCache[playerHeadModelId].defaultMedia =
                            (localPlayer.appearanceColors[0] << 25)
                                    + (localPlayer.appearanceColors[4] << 20)
                                    + (localPlayer.equipment[0] << 15)
                                    + (localPlayer.equipment[8] << 10)
                                    + (localPlayer.equipment[11] << 5)
                                    + localPlayer.equipment[1];
                else
                    Widget.interfaceCache[playerHeadModelId].defaultMedia =
                            (int) (0x12345678L + localPlayer.npcDefinition.interfaceType);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.CLAN_CHAT) {
                try {
                    name = incoming.readString();
                    defaultText = incoming.readString();
                    clanname = incoming.readString();
                    rights = incoming.readUShort();
                    sendMessage(defaultText, 16, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }
            
            if (opcode == 94) {
	            int containerId = incoming.readShort();
	            Widget container = Widget.interfaceCache[containerId];
				int containerSize = incoming.readShort();
	
				int offerItemId = incoming.readShort();
				int offerItemAmount = incoming.readUnsignedByte();
				if (offerItemAmount == 255)
					offerItemAmount = incoming.readInt();
	
				container.geItem[0] = offerItemId;
				container.geItem[1] = offerItemAmount;
	
				container.geOfferType = incoming.readSignedByte();
	
				if (container.geOfferType != -1) {
					int goalAmount = incoming.readUnsignedByte();
					if (goalAmount == 255)
						goalAmount = incoming.readInt();
					container.geItem[2] = goalAmount;
				}
	
				for (int index = 0; index < containerSize; index++) {
					int amount = incoming.readUnsignedByte();
					if (amount == 255)
						amount = incoming.readInt();
					container.inventoryItemId[index] = incoming.readLEShortA();
					container.inventoryAmounts[index] = (int) amount;
	
					amount = incoming.readUnsignedByte();
					if (amount == 255)
						amount = incoming.readInt();
					container.gePrice = amount;
				}
				for (int index = containerSize; index < container.inventoryItemId.length; index++) {
					container.inventoryItemId[index] = 0;
					container.inventoryAmounts[index] = 0;
				}
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.RESET_CAMERA) {
                inCutScene = false;
                for (int l = 0; l < 5; l++)
                    quakeDirectionActive[l] = false;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.CLEAN_ITEMS_OF_INTERFACE) {
                int id = incoming.readUShort();
                Widget widget = Widget.interfaceCache[id];
                for (int slot = 0; slot < widget.inventoryItemId.length; slot++) {
                    widget.inventoryItemId[slot] = -1;
                    widget.inventoryItemId[slot] = 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SPIN_CAMERA) {
                inCutScene = true;
                x = incoming.readUnsignedByte();
                y = incoming.readUnsignedByte();
                height = incoming.readUShort();
                speed = incoming.readUnsignedByte();
                angle = incoming.readUnsignedByte();
                if (angle >= 100) {
                    xCameraPos = x * 128 + 64;
                    yCameraPos = y * 128 + 64;
                    zCameraPos = getCenterHeight(plane, yCameraPos, xCameraPos) - height;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_SKILL) {
                int skill = incoming.readUnsignedByte();
                int level = incoming.readInt();
                int maxLevel = incoming.readInt();
                int experience = incoming.readInt();

                if (skill < currentExp.length) {
                    currentExp[skill] = experience;
                    currentLevels[skill] = level;
                    maximumLevels[skill] = maxLevel;

                    if (skill == 3 && localPlayer != null) {
                        localPlayer.currentHealth = level;
                        localPlayer.maxHealth = maxLevel;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_SIDE_TAB) {
                int id = incoming.readUShort();
                int tab = incoming.readUByteA();
                if (id == 65535)
                    id = -1;
                boolean magicSpellbookChanged = tab == 6 && (tabInterfaceIDs[6] != id);
                tabInterfaceIDs[tab] = id;
                
                // Resort the spellbook if needed.
                if (magicSpellbookChanged) {
                	SpellbookSorting.sort(SpellbookSorting.selectedOrdering);
                }
                
                tabAreaAltered = true;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.PLAY_SONG) {
                int id = incoming.readLEUShort();
                if (id == 65535)
                    id = -1;
                if (id != currentSong && Configuration.enableMusic && !lowMemory && prevSong == 0) {
                    nextSong = id;
                    fadeMusic = true;
                    resourceProvider.provide(2, nextSong);
                }
                currentSong = id;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.NEXT_OR_PREVIOUS_SONG) {
                int id = incoming.readLEUShortA();
                int delay = incoming.readUShortA();
                if (Configuration.enableMusic && !lowMemory) {
                    nextSong = id;
                    fadeMusic = false;
                    resourceProvider.provide(2, nextSong);
                    prevSong = delay;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.LOGOUT) {
                resetLogout();
                opcode = -1;
                return false;
            }

            if (opcode == PacketConstants.MOVE_COMPONENT) {
                int horizontalOffset = incoming.readShort();
                int verticalOffset = incoming.readLEShort();
                int id = incoming.readLEUShort();
                Widget widget = Widget.interfaceCache[id];
                widget.horizontalOffset = horizontalOffset;
                widget.verticalOffset = verticalOffset;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_MAP_REGION || opcode == PacketConstants.SEND_REGION_MAP_REGION) {
                clearRegionalSpawns();
                int regionX = currentRegionX;
                int regionY = currentRegionY;
                if (opcode == PacketConstants.SEND_MAP_REGION) {
                    regionX = incoming.readUShortA();
                    regionY = incoming.readUShort();
                } else if (opcode == PacketConstants.SEND_REGION_MAP_REGION) {
                    regionY = incoming.readUShortA();
                    incoming.initBitAccess();
                    for (int z = 0; z < 4; z++) {
                        for (int x = 0; x < 13; x++) {
                            for (int y = 0; y < 13; y++) {
                                int visible = incoming.readBits(1);
                                if (visible == 1) {
                                    constructRegionData[z][x][y] = incoming.readBits(26);
                                } else {
                                    constructRegionData[z][x][y] = -1;
                                }
                            }
                        }
                    }
                    incoming.disableBitAccess();
                    regionX = incoming.readUShort();
                    requestMapReconstruct = true;
                }
                if (opcode != PacketConstants.SEND_REGION_MAP_REGION
                        && currentRegionX == regionX
                        && currentRegionY == regionY && loadingStage == 2) {
                    opcode = -1;
                    return true;
                }
                currentRegionX = regionX;
                currentRegionY = regionY;
                regionBaseX = (currentRegionX - 6) * 8;
                regionBaseY = (currentRegionY - 6) * 8;
                inTutorialIsland = (currentRegionX / 8 == 48 || currentRegionX / 8 == 49) && currentRegionY / 8 == 48;
                if (currentRegionX / 8 == 48 && currentRegionY / 8 == 148)
                    inTutorialIsland = true;
                loadingStage = 1;
                loadingStartTime = System.currentTimeMillis();
                gameScreenImageProducer.initDrawingArea();
                drawLoadingMessages(1, "Loading - please wait.", null);
                gameScreenImageProducer.drawGraphics(frameMode == ScreenMode.FIXED ? 4 : 0, super.graphics,
                        frameMode == ScreenMode.FIXED ? 4 : 0);
                if (opcode == 73) {
                    int regionCount = 0;
                    for (int x = (currentRegionX - 6) / 8; x <= (currentRegionX + 6) / 8; x++) {
                        for (int y = (currentRegionY - 6) / 8; y <= (currentRegionY + 6) / 8; y++)
                            regionCount++;
                    }
                    terrainData = new byte[regionCount][];
                    objectData = new byte[regionCount][];
                    mapCoordinates = new int[regionCount];
                    terrainIndices = new int[regionCount];
                    objectIndices = new int[regionCount];
                    regionCount = 0;

                    for (int x = (currentRegionX - 6) / 8; x <= (currentRegionX + 6) / 8; x++) {
                        for (int y = (currentRegionY - 6) / 8; y <= (currentRegionY + 6) / 8; y++) {
                            mapCoordinates[regionCount] = (x << 8) + y;
                            if (inTutorialIsland
                                    && (y == 49 || y == 149 || y == 147 || x == 50 || x == 49 && y == 47)) {
                                terrainIndices[regionCount] = -1;
                                objectIndices[regionCount] = -1;
                                regionCount++;
                            } else {
                                int map = terrainIndices[regionCount] = resourceProvider.resolve(0, y, x);
                                if (map != -1) {
                                    resourceProvider.provide(3, map);
                                }

                                int landscape = objectIndices[regionCount] = resourceProvider.resolve(1, y,
                                        x);
                                if (landscape != -1) {
                                    resourceProvider.provide(3, landscape);
                                }

                                regionCount++;
                            }
                        }
                    }
                }
                if (opcode == 241) {
                    int totalLegitChunks = 0;
                    int totalChunks[] = new int[676];
                    for (int z = 0; z < 4; z++) {
                        for (int x = 0; x < 13; x++) {
                            for (int y = 0; y < 13; y++) {
                                int tileBits = constructRegionData[z][x][y];
                                if (tileBits != -1) {
                                    int xCoord = tileBits >> 14 & 0x3ff;
                                    int yCoord = tileBits >> 3 & 0x7ff;
                                    int mapRegion = (xCoord / 8 << 8) + yCoord / 8;
                                    for (int idx = 0; idx < totalLegitChunks; idx++) {
                                        if (totalChunks[idx] != mapRegion)
                                            continue;
                                        mapRegion = -1;

                                    }
                                    if (mapRegion != -1) {
                                        totalChunks[totalLegitChunks++] = mapRegion;
                                    }
                                }
                            }
                        }
                    }
                    terrainData = new byte[totalLegitChunks][];
                    objectData = new byte[totalLegitChunks][];
                    mapCoordinates = new int[totalLegitChunks];
                    terrainIndices = new int[totalLegitChunks];
                    objectIndices = new int[totalLegitChunks];
                    for (int idx = 0; idx < totalLegitChunks; idx++) {
                        int region = mapCoordinates[idx] = totalChunks[idx];
                        int l30 = region >> 8 & 0xff;
                        int l31 = region & 0xff;
                        int terrainMapId = terrainIndices[idx] = resourceProvider.resolve(0, l31, l30);
                        if (terrainMapId != -1)
                            resourceProvider.provide(3, terrainMapId);
                        int objectMapId = objectIndices[idx] = resourceProvider.resolve(1, l31, l30);
                        if (objectMapId != -1)
                            resourceProvider.provide(3, objectMapId);
                    }
                }
                int dx = regionBaseX - previousAbsoluteX;
                int dy = regionBaseY - previousAbsoluteY;
                previousAbsoluteX = regionBaseX;
                previousAbsoluteY = regionBaseY;
                for (int index = 0; index < 16384; index++) {
                    Npc npc = npcs[index];
                    if (npc != null) {
                        for (int point = 0; point < 10; point++) {
                            npc.pathX[point] -= dx;
                            npc.pathY[point] -= dy;
                        }
                        npc.x -= dx * 128;
                        npc.y -= dy * 128;
                    }
                }
                for (int index = 0; index < maxPlayers; index++) {
                    Player player = players[index];
                    if (player != null) {
                        for (int point = 0; point < 10; point++) {
                            player.pathX[point] -= dx;
                            player.pathY[point] -= dy;
                        }
                        player.x -= dx * 128;
                        player.y -= dy * 128;
                    }
                }
                loadingMap = true;
                byte startX = 0;
                byte endX = 104;
                byte stepX = 1;
                if (dx < 0) {
                    startX = 103;
                    endX = -1;
                    stepX = -1;
                }
                byte startY = 0;
                byte endY = 104;
                byte stepY = 1;

                if (dy < 0) {
                    startY = 103;
                    endY = -1;
                    stepY = -1;
                }
                for (int x = startX; x != endX; x += stepX) {
                    for (int y = startY; y != endY; y += stepY) {
                        int shiftedX = x + dx;
                        int shiftedY = y + dy;
                        for (int plane = 0; plane < 4; plane++)
                            if (shiftedX >= 0 && shiftedY >= 0 && shiftedX < 104 && shiftedY < 104) {
                                groundItems[plane][x][y] = groundItems[plane][shiftedX][shiftedY];
                            } else {
                                groundItems[plane][x][y] = null;
                            }
                    }
                }
                for (SpawnedObject object = (SpawnedObject) spawns
                        .reverseGetFirst(); object != null; object = (SpawnedObject) spawns.reverseGetNext()) {
                    object.x -= dx;
                    object.y -= dy;
                    if (object.x < 0 || object.y < 0 || object.x >= 104 || object.y >= 104)
                        object.unlink();
                }
                if (destinationX != 0) {
                    destinationX -= dx;
                    destinationY -= dy;
                }
                inCutScene = false;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_WALKABLE_INTERFACE) {
                int interfaceId = incoming.readInt();
                if (interfaceId >= 0)
                    resetAnimation(interfaceId);
                openWalkableInterface = interfaceId;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_MINIMAP_STATE) {
                minimapState = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SHOW_NPC_HEAD_ON_INTERFACE) {
                int npcId = incoming.readLEUShortA();
                int interfaceId = incoming.readLEUShortA();
                Widget.interfaceCache[interfaceId].defaultMediaType = 2;
                Widget.interfaceCache[interfaceId].defaultMedia = npcId;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SYSTEM_UPDATE) {
                systemUpdateTime = incoming.readLEUShort() * 30;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_MULTIPLE_MAP_PACKETS) {
                localY = incoming.readUnsignedByte();
                localX = incoming.readNegUByte();
                while (incoming.currentPosition < packetSize) {
                    int k3 = incoming.readUnsignedByte();
                    parseRegionPackets(incoming, k3);
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_EARTHQUAKE) {
                int quakeDirection = incoming.readUnsignedByte();
                int quakeMagnitude = incoming.readUnsignedByte();
                int quakeAmplitude = incoming.readUnsignedByte();
                int fourPiOverPeriod = incoming.readUnsignedByte();
                quakeDirectionActive[quakeDirection] = true;
                quakeMagnitudes[quakeDirection] = quakeMagnitude;
                quakeAmplitudes[quakeDirection] = quakeAmplitude;
                quake4PiOverPeriods[quakeDirection] = fourPiOverPeriod;
                quakeTimes[quakeDirection] = 0;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.PLAY_SOUND_EFFECT) {
                int soundId = incoming.readUShort();
                int type = incoming.readUnsignedByte();
                int delay = incoming.readUShort();
                int volume = incoming.readUShort();
                tracks[trackCount] = soundId;
                trackLoops[trackCount] = type;
                soundDelay[trackCount] = delay + Track.delays[soundId];
                soundVolume[trackCount] = volume;
                trackCount++;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SET_AUTOCAST_ID) {
                int auto = incoming.readUShort();
                if (auto == -1) {
                    autocast = false;
                    autoCastId = 0;
                } else {
                    autocast = true;
                    autoCastId = auto;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_PLAYER_OPTION) {
                int slot = incoming.readNegUByte();
                int lowPriority = incoming.readUByteA();
                String message = incoming.readString();
                if (slot >= 1 && slot <= 5) {
                    if (message.equalsIgnoreCase("null"))
                        message = null;
                    playerOptions[slot - 1] = message;
                    playerOptionsHighPriority[slot - 1] = lowPriority == 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.CLEAR_MINIMAP_FLAG) {
                destinationX = 0;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.ENABLE_NOCLIP) {
                for (int plane = 0; plane < 4; plane++) {
                    for (int x = 1; x < 103; x++) {
                        for (int y = 1; y < 103; y++) {
                            collisionMaps[plane].clipData[x][y] =
                                    0;
                        }
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_URL) {
                String url = incoming.readString();
                MiscUtils.launchURL(url);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_CLAN_CHAT_MESSAGE) {
                int type = incoming.readUnsignedByte();
                String name = incoming.readString();
                String message = incoming.readString();
                sendMessage(message, type, name);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_MESSAGE) {
                String message = incoming.readString();
                if (message.endsWith(":tradereq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long encodedName = StringUtils.encodeBase37(name);
                    boolean ignored = false;
                    for (int index = 0; index < ignoreCount; index++) {
                        if (ignoreListAsLongs[index] != encodedName)
                            continue;
                        ignored = true;

                    }
                    if (!ignored && onTutorialIsland == 0)
                        sendMessage("wishes to trade with you.", 4, name);
                } else if (message.endsWith("#url#")) {
                    String link = message.substring(0, message.indexOf("#"));
                    sendMessage("Join us at: ", 9, link);
                } else if (message.endsWith(":duelreq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long encodedName = StringUtils.encodeBase37(name);
                    boolean ignored = false;
                    for (int count = 0; count < ignoreCount; count++) {
                        if (ignoreListAsLongs[count] != encodedName)
                            continue;
                        ignored = true;

                    }
                    if (!ignored && onTutorialIsland == 0)
                        sendMessage("wishes to duel with you.", 8, name);
                } else if (message.endsWith(":chalreq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long encodedName = StringUtils.encodeBase37(name);
                    boolean ignored = false;
                    for (int index = 0; index < ignoreCount; index++) {
                        if (ignoreListAsLongs[index] != encodedName)
                            continue;
                        ignored = true;

                    }
                    if (!ignored && onTutorialIsland == 0) {
                        String msg = message.substring(message.indexOf(":") + 1,
                                message.length() - 9);
                        sendMessage(msg, 8, name);
                    }
                } else {
                    sendMessage(message, 0, "");
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.STOP_ALL_ANIMATIONS) {
                for (int index = 0; index < players.length; index++) {
                    if (players[index] != null)
                        players[index].emoteAnimation = -1;
                }
                for (int index = 0; index < npcs.length; index++) {
                    if (npcs[index] != null)
                        npcs[index].emoteAnimation = -1;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.ADD_FRIEND) {
                long encodedName = incoming.readLong();
                int world = incoming.readUnsignedByte();
                String name = StringUtils
                        .formatText(StringUtils.decodeBase37(encodedName));
                for (int playerIndex = 0; playerIndex < friendsCount; playerIndex++) {
                    if (encodedName != friendsListAsLongs[playerIndex])
                        continue;
                    if (friendsNodeIDs[playerIndex] != world) {
                        friendsNodeIDs[playerIndex] = world;
						/*if (world >= 2) {
							sendMessage(name + " has logged in.", 5, "");
						}
						if (world <= 1) {
							sendMessage(name + " has logged out.", 5, "");
						}*/
                    }
                    name = null;

                }
                if (name != null && friendsCount < 200) {
                    friendsListAsLongs[friendsCount] = encodedName;
                    friendsList[friendsCount] = name;
                    friendsNodeIDs[friendsCount] = world;
                    friendsCount++;
                }
                for (boolean stopSorting = false; !stopSorting; ) {
                    stopSorting = true;
                    for (int friendIndex = 0; friendIndex < friendsCount - 1; friendIndex++)
                        if (friendsNodeIDs[friendIndex] != nodeID && friendsNodeIDs[friendIndex + 1] == nodeID || friendsNodeIDs[friendIndex] == 0 && friendsNodeIDs[friendIndex + 1] != 0) {
                            int tempFriendNodeId = friendsNodeIDs[friendIndex];
                            friendsNodeIDs[friendIndex] = friendsNodeIDs[friendIndex + 1];
                            friendsNodeIDs[friendIndex + 1] = tempFriendNodeId;
                            String tempFriendName = friendsList[friendIndex];
                            friendsList[friendIndex] = friendsList[friendIndex + 1];
                            friendsList[friendIndex + 1] = tempFriendName;
                            long tempFriendLong = friendsListAsLongs[friendIndex];
                            friendsListAsLongs[friendIndex] = friendsListAsLongs[friendIndex + 1];
                            friendsListAsLongs[friendIndex + 1] = tempFriendLong;
                            stopSorting = false;
                        }
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.REMOVE_FRIEND) {
                long nameHash = incoming.readLong();

                for (int i = 0; i < friendsCount; i++) {
                    if (friendsListAsLongs[i] != nameHash) {
                        continue;
                    }

                    friendsCount--;
                    for (int n = i; n < friendsCount; n++) {
                        friendsList[n] = friendsList[n + 1];
                        friendsNodeIDs[n] = friendsNodeIDs[n + 1];
                        friendsListAsLongs[n] = friendsListAsLongs[n + 1];
                    }
                    break;
                }

                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.ADD_IGNORE) {
                long encodedName = incoming.readLong();
                if (ignoreCount < 200) {
                    ignoreListAsLongs[ignoreCount] = encodedName;
                    ignoreCount++;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.REMOVE_IGNORE) {
                long nameHash = incoming.readLong();
                for (int index = 0; index < ignoreCount; index++) {
                    if (ignoreListAsLongs[index] == nameHash) {
                        ignoreCount--;
                        System.arraycopy(ignoreListAsLongs, index + 1, ignoreListAsLongs,
                                index, ignoreCount - index);
                        break;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_TOGGLE_QUICK_PRAYERS) {
                prayClicked = incoming.readUnsignedByte() == 1;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_RUN_ENERGY) {
                runEnergy = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_TOGGLE_RUN) {
                settings[152] = incoming.readUnsignedByte();
                Widget.interfaceCache[SettingsWidget.RUN].active = settings[152] == 1;
                opcode = -1;
                return true;
            }


            if (opcode == PacketConstants.SEND_EXIT) {
                System.exit(1);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_HINT_ICON) {
                // the first byte, which indicates the type of mob
                hintIconDrawType = incoming.readUnsignedByte();
                if (hintIconDrawType == 1) //NPC Hint Arrow
                    // the world index or slot of the npc in the server (which is also the same for the client (should))
                    hintIconNpcId = incoming.readUShort();
                if (hintIconDrawType >= 2 && hintIconDrawType <= 6) { //Location Hint Arrow
                    if (hintIconDrawType == 2) { //Center
                        hintIconLocationArrowRelX = 64;
                        hintIconLocationArrowRelY = 64;
                    }
                    if (hintIconDrawType == 3) { //West side
                        hintIconLocationArrowRelX = 0;
                        hintIconLocationArrowRelY = 64;
                    }
                    if (hintIconDrawType == 4) { //East side
                        hintIconLocationArrowRelX = 128;
                        hintIconLocationArrowRelY = 64;
                    }
                    if (hintIconDrawType == 5) { //South side
                        hintIconLocationArrowRelX = 64;
                        hintIconLocationArrowRelY = 0;
                    }
                    if (hintIconDrawType == 6) { //North side
                        hintIconLocationArrowRelX = 64;
                        hintIconLocationArrowRelY = 128;
                    }
                    hintIconDrawType = 2;
                    //x offset
                    hintIconX = incoming.readUShort();

                    // y offset
                    hintIconY = incoming.readUShort();

                    // z offset
                    hintIconLocationArrowHeight = incoming.readUnsignedByte();
                }
                if (hintIconDrawType == 10) //Player Hint Arrow
                    hintIconPlayerId = incoming.readUShort();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_DUO_INTERFACE) { //Send Duo Interface: Main + Sidebar
                int mainInterfaceId = incoming.readUShortA();
                int sidebarOverlayInterfaceId = incoming.readUShort();
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    updateChatbox = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    updateChatbox = true;
                }
                //ChatboxItemSearch.reset();
                openInterfaceId = mainInterfaceId;
                overlayInterfaceId = sidebarOverlayInterfaceId;
                tabAreaAltered = true;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == 79) {
                int id = incoming.readLEUShort();
                int scrollPosition = incoming.readUShortA();
                Widget widget = Widget.interfaceCache[id];
                if (widget != null && widget.type == 0) {
                    if (scrollPosition < 0)
                        scrollPosition = 0;
                    if (scrollPosition > widget.scrollMax - widget.height)
                        scrollPosition = widget.scrollMax - widget.height;
                    widget.scrollPosition = scrollPosition;
                }
                opcode = -1;
                return true;
            }

            if (opcode == 68) {
                for (int k5 = 0; k5 < settings.length; k5++)
                    if (settings[k5] != configValues[k5]) {
                        settings[k5] = configValues[k5];
                        updateVarp(k5);
                    }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_RECEIVED_PRIVATE_MESSAGE) {
                long encodedName = incoming.readLong();
                int messageId = incoming.readInt();
                int rights = incoming.readUnsignedByte();
                boolean ignoreRequest = false;

                if (rights <= 1) {
                    for (int index = 0; index < ignoreCount; index++) {
                        if (ignoreListAsLongs[index] != encodedName)
                            continue;
                        ignoreRequest = true;

                    }
                }
                if (!ignoreRequest && onTutorialIsland == 0)
                    try {
                        privateMessageIds[privateMessageCount] = messageId;
                        privateMessageCount = (privateMessageCount + 1) % 100;
                        String message = incoming.readString();//ChatMessageCodec.decode(packetSize - 13, incoming);
                        //incoming.readString();
                       // if(l21 != 3)
                        // s9 = Censor.doCensor(s9);
                        if (rights > 0 && rights < 10) {
                            sendMessage(message, 7, "@cr" + rights + "@"
                                    + StringUtils.formatText(StringUtils
                                    .decodeBase37(encodedName)));
                        } else {
                            sendMessage(message, 3, StringUtils.formatText(
                                    StringUtils.decodeBase37(encodedName)));
                        }

                    } catch (Exception ex) {
                        Signlink.reporterror("cde1");
                    }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_REGION) {
                localY = incoming.readNegUByte();
                localX = incoming.readNegUByte();
                opcode = -1;
                return true;
            }

            if (opcode == 24) {
                flashingSidebarId = incoming.readUByteS();
                if (flashingSidebarId == tabId) {
                    if (flashingSidebarId == 3)
                        tabId = 1;
                    else
                        tabId = 3;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_ITEM_TO_INTERFACE) {
                int widget = incoming.readLEUShort();
                int scale = incoming.readUShort();
                int item = incoming.readUShort();
                if (item == 65535) {
                    Widget.interfaceCache[widget].defaultMediaType = 0;
                    opcode = -1;
                    return true;
                } else {
                    ItemDefinition definition = ItemDefinition.lookup(item);
                    Widget.interfaceCache[widget].defaultMediaType = 4;
                    Widget.interfaceCache[widget].defaultMedia = item;
                    Widget.interfaceCache[widget].modelRotation1 = definition.rotation_y;
                    Widget.interfaceCache[widget].modelRotation2 = definition.rotation_x;
                    Widget.interfaceCache[widget].modelZoom = (definition.model_zoom * 100) / scale;
                    opcode = -1;
                    return true;
                }
            }

            if (opcode == PacketConstants.SEND_INTERFACE_VISIBILITY_STATE) {
                boolean hide = incoming.readUnsignedByte() == 1;
                int id = incoming.readInt();
                Widget.interfaceCache[id].invisible = hide;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_SOLO_NON_WALKABLE_SIDEBAR_INTERFACE) {
                int id = incoming.readLEUShort();
                resetAnimation(id);
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    updateChatbox = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    updateChatbox = true;
                }
                //ChatboxItemSearch.reset();
                overlayInterfaceId = id;
                tabAreaAltered = true;
                openInterfaceId = -1;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == 137) {
                specialAttack = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SET_INTERFACE_TEXT) {
                try {

                    String text = incoming.readString();
                    int id = incoming.readInt();

                    //	updateStrings(text, id);
                    sendString(text, id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.UPDATE_CHAT_MODES) {
                publicChatMode = incoming.readUnsignedByte();
                privateChatMode = incoming.readUnsignedByte();
                tradeMode = incoming.readUnsignedByte();
                updateChatbox = true;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_PLAYER_WEIGHT) {
                weight = incoming.readShort();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_MODEL_TO_INTERFACE) {
                int id = incoming.readLEUShortA();
                int model = incoming.readUShort();
                Widget.interfaceCache[id].defaultMediaType = 1;
                Widget.interfaceCache[id].defaultMedia = model;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_CHANGE_INTERFACE_COLOUR) {
                int id = incoming.readLEUShortA();
                int color = incoming.readLEUShortA();
                int red = color >> 10 & 0x1f;
                int green = color >> 5 & 0x1f;
                int blue = color & 0x1f;
                Widget.interfaceCache[id].textColor =
                        (red << 19) + (green << 11) + (blue << 3);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_UPDATE_ITEMS) {
                try {

                    int interfaceId = incoming.readInt();
                    int itemCount = incoming.readShort();

                    Widget widget = Widget.interfaceCache[interfaceId];
                    if (widget == null || widget.inventoryItemId == null || widget.inventoryAmounts == null) {
                        opcode = -1;
                        return true;
                    }

                    for (int j22 = 0; j22 < itemCount; j22++) {
                        if (j22 == widget.inventoryItemId.length) {
                            break;
                        }
                        int amount = incoming.readInt();

                        if (amount == -1) {
                            widget.inventoryItemId[j22] = -1;
                        } else {
                            widget.inventoryItemId[j22] = incoming.readShort();
                        }

                        widget.inventoryAmounts[j22] = amount;
                    }

                    for (int slot = itemCount; slot < widget.inventoryItemId.length; slot++) {
                        widget.inventoryItemId[slot] = 0;
                        widget.inventoryAmounts[slot] = 0;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_CURRENT_BANK_TAB) {
                Bank.currentBankTab = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_EFFECT_TIMER) {
                try {

                    int timer = incoming.readShort();
                    int sprite = incoming.readShort();

                    addEffectTimer(new EffectTimer(timer, sprite));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SET_MODEL_INTERFACE_ZOOM) {
                int scale = incoming.readUShortA();
                int id = incoming.readUShort();
                int pitch = incoming.readUShort();
                int roll = incoming.readLEUShortA();
                Widget.interfaceCache[id].modelRotation1 = pitch;
                Widget.interfaceCache[id].modelRotation2 = roll;
                Widget.interfaceCache[id].modelZoom = scale;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SET_FRIENDSERVER_STATUS) {
                friendServerStatus = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.MOVE_CAMERA) { //Gradually turn camera to spatial point.
                inCutScene = true;
                cinematicCamXViewpointLoc = incoming.readUnsignedByte();
                cinematicCamYViewpointLoc = incoming.readUnsignedByte();
                cinematicCamZViewpointLoc = incoming.readUShort();
                constCinematicCamRotationSpeed = incoming.readUnsignedByte();
                varCinematicCamRotationSpeedPromille = incoming.readUnsignedByte();
                if (varCinematicCamRotationSpeedPromille >= 100) {
                    int cinCamXViewpointPos = cinematicCamXViewpointLoc * 128 + 64;
                    int cinCamYViewpointPos = cinematicCamYViewpointLoc * 128 + 64;
                    int cinCamZViewpointPos = getCenterHeight(plane, cinCamYViewpointPos, cinCamXViewpointPos) - cinematicCamZViewpointLoc;
                    int dXPos = cinCamXViewpointPos - xCameraPos;
                    int dYPos = cinCamYViewpointPos - yCameraPos;
                    int dZPos = cinCamZViewpointPos - zCameraPos;
                    int flatDistance = (int) Math.sqrt(dXPos * dXPos + dYPos * dYPos);
                    yCameraCurve = (int) (Math.atan2(dZPos, flatDistance) * 325.94900000000001D)
                            & 0x7ff;
                    xCameraCurve = (int) (Math.atan2(dXPos, dYPos) * -325.94900000000001D)
                            & 0x7ff;
                    if (yCameraCurve < 128)
                        yCameraCurve = 128;
                    if (yCameraCurve > 383)
                        yCameraCurve = 383;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_INITIALIZE_PACKET) {
                incoming.readUByteA();
                localPlayerIndex = incoming.readShort();
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.NPC_UPDATING) {
                updateNPCs(incoming, packetSize);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_ENTER_AMOUNT) {
                String title = incoming.readString();
                enter_amount_title = title;
                messagePromptRaised = false;
                inputDialogState = 1;
                amountOrNameInput = "";
                updateChatbox = true;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_ENTER_NAME) { //Send Enter Name Dialogue (still allows numbers)
                String title = incoming.readString();
                enter_name_title = title;
                messagePromptRaised = false;
                inputDialogState = 2;
                amountOrNameInput = "";
                updateChatbox = true;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_NON_WALKABLE_INTERFACE) {
                int interfaceId = incoming.readUShort();
                //resetAnimation(interfaceId);
                if (overlayInterfaceId != -1) {
                    overlayInterfaceId = -1;
                    tabAreaAltered = true;
                }
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    updateChatbox = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    updateChatbox = true;
                }
                if (interfaceId == 15244) {
                    fullscreenInterfaceID = 17511;
                    openInterfaceId = 15244;
                }
                if (interfaceId == 15244) {
					fullscreenInterfaceID = 17511;
					openInterfaceId = 15244;
				}
				if (interfaceId == 11000) {
					fullscreenInterfaceID = 11000;
					openInterfaceId = 11000;
				}
				if (interfaceId == 15000) {
					fullscreenInterfaceID = 15000;
					openInterfaceId = 15000;
				}
                openInterfaceId = interfaceId;
                continuedDialogue = false;
                opcode = -1;

                return true;
            }

            if (opcode == PacketConstants.SEND_WALKABLE_CHATBOX_INTERFACE) {
                dialogueId = incoming.readLEShortA();
                updateChatbox = true;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_CONFIG_INT) {
                int id = incoming.readLEUShort();
                int value = incoming.readMEInt();
                configValues[id] = value;
                System.out.println(id + " - " + value);
                if (settings[id] != value) {
                    settings[id] = value;

                    updateVarp(id);
                    if (dialogueId != -1)
                        updateChatbox = true;
                }
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_CONFIG_BYTE) {
                int id = incoming.readLEUShort();
                byte value = incoming.readSignedByte();

                System.out.println(id + " - " + value);

                if (id == 999) {
                    placeholdersConfigIntercept(value);
                } else if (id < configValues.length) {
                    configValues[id] = value;
                    if (settings[id] != value) {
                        settings[id] = value;
                        updateVarp(id);
                        if (dialogueId != -1)
                            updateChatbox = true;
                    }
                }
                opcode = -1;
                return true;
            }
            
            if (opcode == PacketConstants.FADE_TO_BLACK) {
            	String text = incoming.readString();
    	        byte state = incoming.readSignedByte();
    	        byte seconds = incoming.readSignedByte();
    	        System.out.print("FADE_TEXT: " + text);
    	        System.out.print("FADE_STATE: " + state);
    	        System.out.print("FADE_SECONDS: " + seconds);
    	        fadingScreen = new FadingScreen(text, state, seconds);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_MULTICOMBAT_ICON) {
                multicombat = incoming.readUnsignedByte(); //1 is active
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_ANIMATE_INTERFACE) {
                int id = incoming.readUShort();
                int animation = incoming.readShort();
                Widget widget = Widget.interfaceCache[id];
                widget.defaultAnimationId = animation;
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.CLOSE_INTERFACE) {
                if (overlayInterfaceId != -1) {
                    overlayInterfaceId = -1;
                    tabAreaAltered = true;
                }
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    updateChatbox = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    updateChatbox = true;
                }
                fullscreenInterfaceID = -1;
                openInterfaceId = -1;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }
            if (opcode == PacketConstants.UPDATE_SPECIFIC_ITEM) {

                int interfaceId = incoming.readUShort();
                Widget widget = Widget.interfaceCache[interfaceId];

                if (widget == null || widget.inventoryItemId == null) {
                    opcode = -1;
                    return true;
                }

                while (incoming.currentPosition < packetSize) {
                    int slot = incoming.readInt();//readUnsignedByte();
                    int itemInvId = incoming.readUShort();
                    int itemAmount = incoming.readUnsignedByte();//readInt();

                    if (slot >= 0 && slot < widget.inventoryItemId.length) {
                        widget.inventoryItemId[slot] = itemInvId;
                        widget.inventoryAmounts[slot] = itemAmount;
                    }
                }

                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SEND_GFX || opcode == PacketConstants.SEND_GROUND_ITEM || opcode == PacketConstants.SEND_ALTER_GROUND_ITEM_COUNT || opcode == PacketConstants.SEND_REMOVE_OBJECT || opcode == 105
                    || opcode == PacketConstants.SEND_PROJECTILE || opcode == PacketConstants.TRANSFORM_PLAYER_TO_OBJECT || opcode == PacketConstants.SEND_OBJECT || opcode == PacketConstants.SEND_REMOVE_GROUND_ITEM
                    || opcode == PacketConstants.ANIMATE_OBJECT || opcode == 215) {
                parseRegionPackets(incoming, opcode);
                opcode = -1;
                return true;
            }

            if (opcode == PacketConstants.SWITCH_TAB) {
                tabId = incoming.readNegUByte();
                tabAreaAltered = true;
                opcode = -1;
                return true;
            }
            if (opcode == PacketConstants.SEND_NONWALKABLE_CHATBOX_INTERFACE) {
                int id = incoming.readLEUShort();

                resetAnimation(id);
                if (overlayInterfaceId != -1) {
                    overlayInterfaceId = -1;
                    tabAreaAltered = true;
                }
                backDialogueId = id;
                updateChatbox = true;
                openInterfaceId = -1;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            Signlink.reporterror("T1 - " + opcode + "," + packetSize + " - "
                    + secondLastOpcode + "," + thirdLastOpcode);
            resetLogout();
        } catch (IOException _ex) {
            dropClient();
            _ex.printStackTrace();
        } catch (Exception exception) {
            String s2 = "T2 - " + opcode + "," + secondLastOpcode + "," + thirdLastOpcode
                    + " - " + packetSize + "," + (regionBaseX + localPlayer.pathX[0])
                    + "," + (regionBaseY + localPlayer.pathY[0]) + " - ";
            for (int j15 = 0; j15 < packetSize && j15 < 50; j15++)
                s2 = s2 + incoming.payload[j15] + ",";
            Signlink.reporterror(s2);
            exception.printStackTrace();
            // resetLogout();
        }
        opcode = -1;
        return true;
    }

    private void moveCameraWithPlayer() {
        anInt1265++;
        if(!Configuration.mediaMode) {
        	 showPrioritizedPlayers();
             showOtherPlayers();
             showPrioritizedNPCs();
             showOtherNpcs();
        } else {
            showPrioritizedNPCs();
            showOtherNpcs();
            newBoldFont.drawCenteredString("Press 'ESC' to Exit Media Mode", 50, 20, 0, -1);
        }

        createProjectiles();
        createStationaryGraphics();
        if (!inCutScene) {
            int i = anInt1184;
            if (anInt984 / 256 > i)
                i = anInt984 / 256;
            if (quakeDirectionActive[4] && quakeAmplitudes[4] + 128 > i)
                i = quakeAmplitudes[4] + 128;
            int k = cameraHorizontal + cameraRotation & 0x7ff;
            setCameraPos(
                    cameraZoom + i * ((SceneGraph.viewDistance == 9)
                            && (frameMode == ScreenMode.RESIZABLE) ? 2
                            : SceneGraph.viewDistance == 10 ? 5 : 3),
                    i, anInt1014, getCenterHeight(plane, localPlayer.y, localPlayer.x) - 50, k,
                    anInt1015);
        }
        int j;
        if (!inCutScene)
            j = setCameraLocation();
        else
            j = resetCameraHeight();
        int l = xCameraPos;
        int i1 = zCameraPos;
        int j1 = yCameraPos;
        int k1 = yCameraCurve;
        int l1 = xCameraCurve;
        for (int i2 = 0; i2 < 5; i2++)
            if (quakeDirectionActive[i2]) {
                int j2 = (int) ((Math.random() * (double) (quakeMagnitudes[i2] * 2 + 1)
                        - (double) quakeMagnitudes[i2]) + Math
                        .sin((double) quakeTimes[i2]
                                * ((double) quake4PiOverPeriods[i2] / 100D))
                        * (double) quakeAmplitudes[i2]);
                if (i2 == 0)
                    xCameraPos += j2;
                if (i2 == 1)
                    zCameraPos += j2;
                if (i2 == 2)
                    yCameraPos += j2;
                if (i2 == 3)
                    xCameraCurve = xCameraCurve + j2 & 0x7ff;
                if (i2 == 4) {
                    yCameraCurve += j2;
                    if (yCameraCurve < 128)
                        yCameraCurve = 128;
                    if (yCameraCurve > 383)
                        yCameraCurve = 383;
                }
            }
        int k2 = Rasterizer3D.lastTextureRetrievalCount;
        Model.aBoolean1684 = true;
        Model.anInt1687 = 0;
        Model.anInt1685 = super.mouseX - (frameMode == ScreenMode.FIXED ? 4 : 0);
        Model.anInt1686 = super.mouseY - (frameMode == ScreenMode.FIXED ? 4 : 0);
        Rasterizer2D.clear();
        scene.render(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, j, yCameraCurve);
        scene.clearGameObjectCache();
        if (GameGraphics.FOG.toggled()) {
			double fogDistance = Math.sqrt(Math.pow(zCameraPos, 2));
			int fogStartDistance = 1330;
			int fogEndDistance = 2100;
			fog.setFogDistance((float) fogDistance);
			fog.renderFog(false, fogStartDistance, fogEndDistance, 3);
		}
        if (Misc.GROUND_ITEM_NAMES.toggled()) {
            renderGroundItemNames();
        }
        updateEntities();
        drawHeadIcon();
        writeBackgroundTexture(k2);
        if(!Configuration.mediaMode) {
        	 draw3dScreen();
        } else {
           newBoldFont.drawCenteredString("Press 'ESC' to Exit Media Mode", newBoldFont.getTextWidth("Press 'ESC' to Exit Media Mode"), 20, 0xFFFFFF, -1);
        }
        console.drawConsole();
        console.drawConsoleArea();
        if (openInterfaceId == -1 && !Console.consoleOpen) {

            if (shouldDrawCombatBox()) {
                drawCombatBox();
            }
            if (UserInterface.SKILL_ORBS.toggled()) {
                SkillOrbs.process();
            }
            if (Configuration.expCounterOpen) {
                drawExpCounterDrops();
            }
        }
        if(!Configuration.mediaMode) {
        	if (frameMode != ScreenMode.FIXED) {
                drawChatArea();
                drawMinimap();
                drawTabArea();
            }
        }
        
        gameScreenImageProducer.drawGraphics(frameMode == ScreenMode.FIXED ? 4 : 0, super.graphics, frameMode == ScreenMode.FIXED ? 4 : 0);
        xCameraPos = l;
        zCameraPos = i1;
        yCameraPos = j1;
        yCameraCurve = k1;
        xCameraCurve = l1;
    }

    private void tabToReplyPm() {
        String name = null;

        for (int k = 0; k < 100; k++) {
            if (chatMessages[k] == null) {
                continue;
            }

            int l = chatTypes[k];

            if (l == 3 || l == 7) {
                name = chatNames[k];
                break;
            }
        }

        if (name == null) {
            sendMessage("@red@You haven't received any messages to which you can reply.", 0, "");
            return;
        }

        if (name != null) {
            if (name.contains("@")) {
                name = name.substring(5);
            }
        }

        long nameAsLong = MiscUtils.longForName(name.trim());

        if (nameAsLong != -1) {

            updateChatbox = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 3;
            aLong953 = nameAsLong;
            aString1121 = "Enter a message to send to " + name;
        }
    }

    private void processMinimapActions() {
        if (openInterfaceId == 15244) {
            return;
        }
        final boolean fixed = frameMode == ScreenMode.FIXED;
        if (fixed ? super.mouseX >= 542 && super.mouseX <= 579 && super.mouseY >= 2
                && super.mouseY <= 38
                : super.mouseX >= frameWidth - 180 && super.mouseX <= frameWidth - 139
                && super.mouseY >= 0 && super.mouseY <= 40) {
            menuActionText[1] = "Look North";
            menuActionTypes[1] = 696;
            menuActionRow = 2;
        }
        if (frameMode != ScreenMode.FIXED && stackSideStones) {
            if (super.mouseX >= frameWidth - 26 && super.mouseX <= frameWidth - 1 && super.mouseY >= 2 && super.mouseY <= 24) {
                menuActionText[1] = "Logout";
                menuActionTypes[1] = 700;
                menuActionRow = 2;
            }
        }
        if (worldHover && UserInterface.TOGGLE_ORBS.toggled()) {
            menuActionText[3] = "Floating @lre@World Map";
            menuActionTypes[3] = 850;
            menuActionText[2] = "Display Data";
            menuActionTypes[2] = 950;
            menuActionText[1] = "Media Mode";
            menuActionTypes[1] = 960;
            menuActionRow = 4;
        }
        if (specialHover && UserInterface.TOGGLE_SPEC_ORBS.toggled()) {
            menuActionText[1] = "Use @gre@Special Attack";
            menuActionTypes[1] = 851;
            menuActionRow = 2;
        }
        if (expCounterHover) {
            menuActionText[2] = (Configuration.expCounterOpen ? "Hide" : "Show") + " @lre@Exp counter";
            menuActionTypes[2] = 258;
            menuActionText[1] = "Toggle @lre@XP Lock";
            menuActionTypes[1] = 476;
            menuActionRow = 3;
        }
        if (prayHover && UserInterface.TOGGLE_ORBS.toggled()) {
            menuActionText[2] = (prayClicked ? "Deactivate" : "Activate") + " Quick-prayers";
            menuActionTypes[2] = 1500;
            menuActionRow = 2;
            menuActionText[1] = "Setup Quick-prayers";
            menuActionTypes[1] = 1506;
            menuActionRow = 3;
        }
        if (runHover && UserInterface.TOGGLE_ORBS.toggled()) {
            menuActionText[1] = "Toggle Run";
            menuActionTypes[1] = 1050;
            menuActionRow = 2;
        }
    }

    /**
     * Draws the exp counter
     */
    public void drawExpCounter() {

        final boolean wilderness = openWalkableInterface == 23300;
        int xPos = wilderness && frameMode != ScreenMode.FIXED ? frameWidth - 363 : frameWidth - 375;
        int yPos = wilderness ? (frameMode != ScreenMode.FIXED ? 114 : 100) : 2;

        // Draw box
        spriteCache.lookup(452).drawAdvancedSprite(xPos, yPos);
        spriteCache.lookup(453).drawAdvancedSprite(xPos + 4, yPos + 3);

        // Draw total exp
        String totalExpString = StringUtils.insertCommasToNumber("" + totalExp + "");
        newSmallFont.drawRightAlignedString(totalExpString, xPos + 115, yPos + 19, 16777215, 0);
    }

    private void drawExpCounterDrops() {

        final boolean wilderness = openWalkableInterface == 23300;

        RSFont xp_font = newSmallFont;
        int font_height = 24;
        int x = frameWidth - 261;
        int y = wilderness ? -70 : -100;

        for (int i = 0; i < xp_added.length; i++) {

            if (xp_added[i][0] > -1) {

                if (xp_added[i][2] >= 0) {

                    int transparency = 256;
                    if (xp_added[i][2] > 120) {
                        transparency = (10 - (xp_added[i][2] - 120)) * 256 / 20;
                    }

                    if (transparency > 0 && xp_added[i][1] != 0) {
                        String s = "<trans=" + transparency + ">"
                                + NumberFormat.getIntegerInstance().format(xp_added[i][1]);
                        int icons_x_off = 0;
                        for (int i2 = 0; i2 < skill_sprites.length; i2++) {
                            if ((xp_added[i][0] & (1 << i2)) == 0)
                                continue;

                            Sprite sprite = skill_sprites[i2];
                            icons_x_off += sprite.myWidth + 3;
                            sprite.drawSprite(x - xp_font.getTextWidth(s) - icons_x_off,
                                    y + 157 + (130 - xp_added[i][2]) - (font_height / 2) - (sprite.myHeight / 2),
                                    transparency);
                        }
                        xp_font.drawRightAlignedString(s, x, y + 150 + (130 - xp_added[i][2]), 0xffffff, 0);
                    }

                }
                xp_added[i][2]++;

                if (xp_added[i][2] >= (wilderness ? 60 : 240))
                    xp_added[i][0] = -1;
            }
        }
    }

    public int getOrbTextColor(int statusInt) {
        if (statusInt >= 75 && statusInt <= Integer.MAX_VALUE)
            return 0x00FF00;
        else if (statusInt >= 50 && statusInt <= 74)
            return 0xFFFF00;
        else if (statusInt >= 25 && statusInt <= 49)
            return 0xFF981F;
        else
            return 0xFF0000;
    }

    public int getOrbFill(int statusInt) {
        if (statusInt <= Integer.MAX_VALUE && statusInt >= 97)
            return 0;
        else if (statusInt <= 96 && statusInt >= 93)
            return 1;
        else if (statusInt <= 92 && statusInt >= 89)
            return 2;
        else if (statusInt <= 88 && statusInt >= 85)
            return 3;
        else if (statusInt <= 84 && statusInt >= 81)
            return 4;
        else if (statusInt <= 80 && statusInt >= 77)
            return 5;
        else if (statusInt <= 76 && statusInt >= 73)
            return 6;
        else if (statusInt <= 72 && statusInt >= 69)
            return 7;
        else if (statusInt <= 68 && statusInt >= 65)
            return 8;
        else if (statusInt <= 64 && statusInt >= 61)
            return 9;
        else if (statusInt <= 60 && statusInt >= 57)
            return 10;
        else if (statusInt <= 56 && statusInt >= 53)
            return 11;
        else if (statusInt <= 52 && statusInt >= 49)
            return 12;
        else if (statusInt <= 48 && statusInt >= 45)
            return 13;
        else if (statusInt <= 44 && statusInt >= 41)
            return 14;
        else if (statusInt <= 40 && statusInt >= 37)
            return 15;
        else if (statusInt <= 36 && statusInt >= 33)
            return 16;
        else if (statusInt <= 32 && statusInt >= 29)
            return 17;
        else if (statusInt <= 28 && statusInt >= 25)
            return 18;
        else if (statusInt <= 24 && statusInt >= 21)
            return 19;
        else if (statusInt <= 20 && statusInt >= 17)
            return 20;
        else if (statusInt <= 16 && statusInt >= 13)
            return 21;
        else if (statusInt <= 12 && statusInt >= 9)
            return 22;
        else if (statusInt <= 8 && statusInt >= 7)
            return 23;
        else if (statusInt <= 6 && statusInt >= 5)
            return 24;
        else if (statusInt <= 4 && statusInt >= 3)
            return 25;
        else if (statusInt <= 2 && statusInt >= 1)
            return 26;
        else if (statusInt <= 0)
            return 27;
        return 0;
    }

    public void clearTopInterfaces() {
        // close interface
        sendPacket(new CloseInterface().create());
        if (overlayInterfaceId != -1) {
            overlayInterfaceId = -1;
            continuedDialogue = false;
            tabAreaAltered = true;
        }
        if (backDialogueId != -1) {
            backDialogueId = -1;
            updateChatbox = true;
            continuedDialogue = false;
        }
        openInterfaceId = -1;
        fullscreenInterfaceID = -1;
    }

    public void addObject(int x, int y, int objectId, int face, int type, int height) {
        int mX = currentRegionX - 6;
        int mY = currentRegionY - 6;
        int x2 = x - mX * 8;
        int y2 = y - mY * 8;
        int i15 = 40 >> 2;
        int l17 = objectGroups[i15];
        if (y2 > 0 && y2 < 103 && x2 > 0 && x2 < 103) {
            requestSpawnObject(-1, objectId, face, l17, y2, type, height, x2, 0);

        }
    }

    public void resetAllImageProducers() {
        if (super.fullGameScreen != null) {
            return;
        }
        chatboxImageProducer = null;
        minimapImageProducer = null;
        tabImageProducer = null;
        gameScreenImageProducer = null;
        chatSettingImageProducer = null;
        topLeft1BackgroundTile = null;
        bottomLeft1BackgroundTile = null;
        loginBoxImageProducer = null;
        flameLeftBackground = null;
        flameRightBackground = null;
        bottomLeft0BackgroundTile = null;
        bottomRightImageProducer = null;
        loginMusicImageProducer = null;
        middleLeft1BackgroundTile = null;
        aRSImageProducer_1115 = null;
        super.fullGameScreen = new ProducingGraphicsBuffer(765, 503);
        welcomeScreenRaised = true;
    }

    public void mouseWheelDragged(int i, int j) {
        if (!mouseWheelDown) {
            return;
        }
        this.anInt1186 += i * 3;
        this.anInt1187 += (j << 1);
    }

    float PercentCalc(long Number1, long number2) {
        float percentage;
        percentage = (Number1 * 100 / number2);
        return percentage;
    }

    public int getMyPrivilege() {
        return myPrivilege;
    }

    public void displayFps() {
        int textColour = 0xffff00;
        int fpsColour = 0xffff00;
        if (super.fps < 15) {
            fpsColour = 0xff0000;
        }
        regularText.render(fpsColour, "Fps: " + super.fps, 12, frameMode == ScreenMode.FIXED ? 470 : frameWidth - 265);

        Runtime runtime = Runtime.getRuntime();
        int clientMemory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
        regularText.render(textColour, "Mem: " + clientMemory + "k", 27, frameMode == ScreenMode.FIXED ? 428 : frameWidth - 265);
    }
    
	public static boolean controlDown;
    
	private List<String> togglesItemBoxesId = new ArrayList<String>();
	private List<int[]> togglesItemBoxesPos = new ArrayList<int[]>();
	List<String> hiddenItems = new ArrayList<String>();

    /**
     * If toggled, render ground item names and lootbeams
     */
    private void renderGroundItemNames() {
    	togglesItemBoxesPos.clear();
		togglesItemBoxesId.clear();
		for (int x = 0; x < 104; x++) {
			for (int y = 0; y < 104; y++) {
				Deque deque = groundItems[plane][x][y];
				int count = 0;
				if (deque != null) {
					for (Item item = (Item) deque.getFirst(); item != null; item = (Item) deque.getNext()) {
						ItemDefinition itemDef = ItemDefinition.lookup(item.ID);
						int totalValue = itemDef.value * item.itemCount;
						
						int color = 0xffffff;
				
						int itemX = regionBaseX + x;
						int itemY = regionBaseY + y;
						int playerX = regionBaseX + (localPlayer.x - 6 >> 7);
						int playerY = regionBaseY + (localPlayer.y - 6 >> 7);

						calcEntityScreenPos((x * 128 + 64), 25, (y * 128 + 64));
						
						int transparencyDistance = distanceToPoint(playerX, playerY, itemX, itemY);
						int transparency = 256 - (transparencyDistance * 16);
						
						if (goodDistance(itemX, itemY, playerX, playerY, 16)) {
							String item_str = itemDef.name + " x " + NumberFormat.getInstance().format(item.itemCount) + " (" + NumberFormat.getInstance().format(totalValue) + "gp)";
							int yMod = (count * 12);
							boolean blacklist = itemOnBlacklist(itemDef.name);
							int[] tPos = new int[2];
							if (!blacklist || controlDown) {
								int w = 10;
								int h = 10;
								int xPos = newSmallFont.getTextWidth(item_str);
								
								if (blacklist && controlDown) {
									Rasterizer2D.drawBoxOutline(spriteDrawX - w - xPos/2, spriteDrawY - (yMod+10), w, h, color);
								} else if (!blacklist && controlDown) {
									Rasterizer2D.drawBox(spriteDrawX - w - xPos/2, spriteDrawY - (yMod+10), w, h, color);
								}
								
								if (controlDown) {
									tPos[0] = spriteDrawX - w - xPos/2;
									tPos[1] = spriteDrawY - (yMod+10);
									togglesItemBoxesPos.add(tPos);
									togglesItemBoxesId.add(itemDef.name);
								}
								
								newSmallFont.drawCenteredString(item_str, spriteDrawX+2, spriteDrawY - yMod, color, 0, transparency);
								count++;
							}
						}
					}
				}
			}
		}
    }
    
    public boolean itemOnBlacklist(String name) {
		return hiddenItems.contains(name);
	}
	
	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance) && (objectY - playerY <= distance && objectY - playerY >= -distance));
	}
	
	public static int distanceToPoint(int x1, int y1, int x2, int y2) {
		int x = (int) Math.pow(x1 - x2, 2.0D);
		int y = (int) Math.pow(y1 - y2, 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}

    private void menuActionsRow(String action, int index, int actionId, int row) {
        if (menuOpen)
            return;
        menuActionText[index] = action;
        menuActionTypes[index] = actionId;
        menuActionRow = row;
    }

    private void placeholdersConfigIntercept(byte value) {
        Widget.interfaceCache[50007].active = value == 1;
    }

    private void drawGridOverlay() {

        for (int i = 0; i < 516; i += 10) {
            if (i < 334) {
                Rasterizer2D.drawTransparentHorizontalLine(0, i, 516, 0x6699ff, 90);
            }
            Rasterizer2D.drawTransparentVerticalLine(i, 0, 334, 0x6699ff, 90);
        }

        int x = super.mouseX - 4 - ((super.mouseX - 4) % 10);
        int y = super.mouseY - 4 - ((super.mouseY - 4) % 10);

        Rasterizer2D.drawTransparentBoxOutline(x, y, 10, 10, 0xffff00, 255);
        newSmallFont.drawCenteredString("(" + (x + 4) + ", " + (y + 4) + ")", x + 4, y - 1, 0xffff00, 0);
    }

    private enum SpawnTabType {
        INVENTORY,
        BANK;
    }
}
