package com.overload.game.definition;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.overload.game.GameConstants;
import com.overload.game.collision.Buffer;
import com.overload.util.FileUtil;
import com.overload.util.ObjectIdentifiers;

public final class ObjectDefinition extends ObjectIdentifiers {

    public static boolean lowMemory;
    public static int[] streamIndices;
    public static int cacheIndex;
    public static ObjectDefinition[] cache;
    private static Buffer stream;
    private static int totalObjects;
    public boolean obstructsGround;
    public byte ambientLighting;
    public int translateX;
    public String name;
    public int scaleZ;
    public byte lightDiffusion;
    public int objectSizeX;
    public int translateY;
    public int minimapFunction;
    public int[] originalModelColors;
    public int scaleX;
    public int varp;
    public boolean inverted;
    public int id;
    public boolean impenetrable;
    public int mapscene;
    public int childrenIDs[];
    public int supportItems;
    public int objectSizeY;
    public boolean contouredGround;
    public boolean occludes;
    public boolean hollow;
    public boolean solid;
    public int surroundings;
    public boolean delayShading;
    public int scaleY;
    public int[] modelIds;
    public int varbit;
    public int decorDisplacement;
    public int[] modelTypes;
    public String description;
    public boolean isInteractive;
    public boolean castsShadow;
    public int animation;
    public int translateZ;
    public int[] modifiedModelColors;
    public String interactions[];
    private short[] originalTexture;
    private short[] modifiedTexture;

    public ObjectDefinition() {
        id = -1;
    }

    public static void dumpNames() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("./Cache/object_names.txt"));
        for (int i = 0; i < totalObjects; i++) {
            ObjectDefinition def = forId(i);
            String name = def == null ? "null" : def.name;
            writer.write("ID: " + i + ", name: " + name + "");
            writer.newLine();
        }
        writer.close();
    }

    public static ObjectDefinition forId(int id) {
        if (id > streamIndices.length)
            id = streamIndices.length - 1;
        for (int index = 0; index < 20; index++)
            if (cache[index].id == id)
                return cache[index];

        cacheIndex = (cacheIndex + 1) % 20;
        ObjectDefinition objectDef = cache[cacheIndex];
        stream.offset = streamIndices[id];
        objectDef.id = id;
        objectDef.reset();
        objectDef.readValues(stream);
        if (objectDef.id > 14500) {
        	if (objectDef.delayShading) {
        		objectDef.delayShading = false;
        	}
        }
        return objectDef;
    }

    public static void init() {
        try {
            byte[] dat = FileUtil.readFile(GameConstants.CLIPPING_DIRECTORY + "loc.dat");
            byte[] idx = FileUtil.readFile(GameConstants.CLIPPING_DIRECTORY + "loc.idx");

            stream = new Buffer(dat);
            Buffer idxBuffer525 = new Buffer(idx);

            int totalObjects525 = idxBuffer525.readUnsignedWord();
            streamIndices = new int[totalObjects525];
            int i = 2;
            for (int j = 0; j < totalObjects525; j++) {
                streamIndices[j] = i;
                i += idxBuffer525.readUnsignedWord();
            }

            cache = new ObjectDefinition[20];
            for (int k = 0; k < 20; k++) {
                cache[k] = new ObjectDefinition();
            }

            //		+ totalObjects667 + " cache object definitions #667 in " + (System.currentTimeMillis() - startup) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        modelIds = null;
        modelTypes = null;
        name = null;
        description = null;
        modifiedModelColors = null;
        originalModelColors = null;
        objectSizeX = 1;
        objectSizeY = 1;
        solid = true;
        impenetrable = true;
        isInteractive = false;
        contouredGround = false;
        delayShading = false;
        occludes = false;
        animation = -1;
        decorDisplacement = 16;
        ambientLighting = 0;
        lightDiffusion = 0;
        interactions = null;
        minimapFunction = -1;
        mapscene = -1;
        inverted = false;
        castsShadow = true;
        scaleX = 128;
        scaleY = 128;
        scaleZ = 128;
        surroundings = 0;
        translateX = 0;
        translateY = 0;
        translateZ = 0;
        obstructsGround = false;
        hollow = false;
        supportItems = -1;
        varbit = -1;
        varp = -1;
        childrenIDs = null;
    }

    public void readValues(Buffer stream) {
        int flag = -1;
        do {
            int type = stream.getUByte();
            if (type == 0)
                break;
            if (type == 1) {
                int len = stream.getUByte();
                if (len > 0) {
                    if (modelIds == null || lowMemory) {
                        modelTypes = new int[len];
                        modelIds = new int[len];
                        for (int k1 = 0; k1 < len; k1++) {
                            modelIds[k1] = stream.getUShort();
                            modelTypes[k1] = stream.getUByte();
                        }
                    } else {
                        stream.offset += len * 3;
                    }
                }
            } else if (type == 2)
                name = stream.getString();
            else if (type == 3)
                description = stream.getString();
            else if (type == 5) {
                int len = stream.getUByte();
                if (len > 0) {
                    if (modelIds == null || lowMemory) {
                        modelTypes = null;
                        modelIds = new int[len];
                        for (int l1 = 0; l1 < len; l1++)
                            modelIds[l1] = stream.getUShort();
                    } else {
                        stream.offset += len * 2;
                    }
                }
            } else if (type == 14)
                objectSizeX = stream.getUByte();
            else if (type == 15)
                objectSizeY = stream.getUByte();
            else if (type == 17)
                solid = false;
            else if (type == 18)
                impenetrable = false;
            else if (type == 19)
                isInteractive = (stream.getUByte() == 1);
            else if (type == 21)
                contouredGround = true;
            else if (type == 22)
                delayShading = false;
            else if (type == 23)
                occludes = true;
            else if (type == 24) {
                animation = stream.getUShort();
                if (animation == 65535)
                    animation = -1;
            } else if (type == 28)
                decorDisplacement = stream.getUByte();
            else if (type == 29)
                ambientLighting = stream.getByte();
            else if (type == 39)
                lightDiffusion = stream.getByte();
            else if (type >= 30 && type < 39) {
                if (interactions == null)
                    interactions = new String[5];
                interactions[type - 30] = stream.getString();
                if (interactions[type - 30].equalsIgnoreCase("hidden"))
                    interactions[type - 30] = null;
            } else if (type == 40) {
                int i1 = stream.getUByte();
                modifiedModelColors = new int[i1];
                originalModelColors = new int[i1];
                for (int i2 = 0; i2 < i1; i2++) {
                    modifiedModelColors[i2] = stream.getUShort();
                    originalModelColors[i2] = stream.getUShort();
                }
            } else if (type == 41) {
                int j2 = stream.getUByte();
                modifiedTexture = new short[j2];
                originalTexture = new short[j2];
                for (int k = 0; k < j2; k++) {
                    modifiedTexture[k] = (short) stream.getUShort();
                    originalTexture[k] = (short) stream.getUShort();
                }


            } else if (type == 60)
                minimapFunction = stream.getUShort();
            else if (type == 62)
                inverted = true;
            else if (type == 64)
                castsShadow = false;
            else if (type == 65)
                scaleX = stream.getUShort();
            else if (type == 66)
                scaleY = stream.getUShort();
            else if (type == 67)
                scaleZ = stream.getUShort();
            else if (type == 68)
                mapscene = stream.getUShort();
            else if (type == 69)
                surroundings = stream.getUByte();
            else if (type == 70)
                translateX = stream.getShort();
            else if (type == 71)
                translateY = stream.getShort();
            else if (type == 72)
                translateZ = stream.getShort();
            else if (type == 73)
                obstructsGround = true;
            else if (type == 74)
                hollow = true;
            else if (type == 75)
                supportItems = stream.getUByte();
            else if (type == 77) {
                varp = stream.getUShort();
                if (varp == 65535)
                    varp = -1;
                varbit = stream.getUShort();
                if (varbit == 65535)
                    varbit = -1;
                
                //OLD SHIT
//                int j1 = stream.getUByte();
//                childrenIDs = new int[j1 + 1];
//                for (int j2 = 0; j2 <= j1; j2++) {
//                    childrenIDs[j2] = stream.getUShort();
//                    if (childrenIDs[j2] == 65535)
//                        childrenIDs[j2] = -1;
//                }
                
                //NEW SHIT
                {
	                int var3 = -1;
					if(type == 92){
						var3 = stream.getUShort();
						if(var3 == 65535)
							var3 = -1;
					}
					
					int j1 = stream.getUByte();
					childrenIDs = new int[j1 + 2];
					for (int j2 = 0; j2 <= j1; j2++) {
						childrenIDs[j2] = stream.getUShort();
						if (childrenIDs[j2] == 65535)
							childrenIDs[j2] = -1;
					}
					childrenIDs[j1 + 1] = var3;
                }
            }
        } while (true);
        
        if (flag == -1 && name != "null" && name != null) {
            isInteractive = modelIds != null
                    && (modelTypes == null || modelTypes[0] == 10);
            if (interactions != null)
                isInteractive = true;
        }
        if (hollow) {
            solid = false;
            impenetrable = false;
        }
        if (supportItems == -1)
            supportItems = solid ? 1 : 0;
    }

    public String getName() {
        return name;
    }

    public int getSizeX() {
        return objectSizeX;
    }

    public int getSizeY() {
        return objectSizeY;
    }

    public boolean hasActions() {
        return isInteractive;
    }
    
    public int getSize() {
    	switch (id) {
	    	case BARROWS_STAIRCASE_AHRIM:
	    	case BARROWS_STAIRCASE_DHAROK:
	    	case BARROWS_STAIRCASE_GUTHAN:
	    	case BARROWS_STAIRCASE_KARIL:
	    	case BARROWS_STAIRCASE_VERAC:
	    		return 2;
	    	case BARROWS_STAIRCASE_TORAG:
	    		return 3;
    	}
    	    	
    	return (getSizeX() + getSizeY()) - 1;
    }
}