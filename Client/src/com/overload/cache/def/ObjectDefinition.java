package com.overload.cache.def;

import com.overload.Client;
import com.overload.cache.FileArchive;
import com.overload.cache.anim.Frame;
import com.overload.cache.config.VariableBits;
import com.overload.collection.ReferenceCache;
import com.overload.entity.model.Model;
import com.overload.io.Buffer;
import com.overload.net.requester.ResourceProvider;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class ObjectDefinition {

    public static final Model[] aModelArray741s = new Model[4];
    public static boolean lowMemory;
    public static Buffer stream;
    public static int[] streamIndices;
    public static Client clientInstance;
    public static int cacheIndex;
    public static ReferenceCache models = new ReferenceCache(30);
    public static ObjectDefinition[] cache;
    public static ReferenceCache baseModels = new ReferenceCache(500);
    public static int TOTAL_OBJECTS;
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
    public int type;
    public boolean impenetrable;
    public int mapscene;
    public int morphisms[];
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
    private short[] originalModelTexture;
    private short[] modifiedModelTexture;

    public ObjectDefinition() {
        type = -1;
    }

    public static void dumpNames() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("./Cache/object_names.txt"));
        for (int i = 0; i < TOTAL_OBJECTS; i++) {
            ObjectDefinition def = lookup(i);
            String name = def == null ? "null" : def.name;
            writer.write("ID: " + i + ", name: " + name + "");
            writer.newLine();
        }
        writer.close();
    }

    public static ObjectDefinition lookup(int id) {
        if (id > streamIndices.length)
            id = streamIndices.length - 1;
        for (int index = 0; index < 20; index++)
            if (cache[index].type == id)
                return cache[index];

        cacheIndex = (cacheIndex + 1) % 20;
        ObjectDefinition objectDef = cache[cacheIndex];
        stream.currentPosition = streamIndices[id];
        objectDef.type = id;
        objectDef.reset();
        objectDef.readValues(stream);
        if (objectDef.type > 14500) {
        	if (objectDef.delayShading) {
        		objectDef.delayShading = false;
        	}
        }
        switch (id) {
        	//Webs
        	case 733:
        		objectDef.varbit = 729;
        		break;
        	//Farming Patches
            case 7577:
            case 7578:
            case 7579:
            case 7580:
            case 7771:
            case 7807:
            case 7963:
            case 7964:
            case 7965:
            case 8173:
            case 8174:
            case 8175:
            case 8176:
            case 8337:
            case 8338:
            case 8384:
            case 8385:
            case 8386:
            case 8387:
            case 8388:
            case 8389:
            case 8390:
            case 8391:
            case 8550:
            case 8552:
            case 8554:
            case 8556:
            case 14837:
            case 18816:
            case 19146:
            case 19147:
            case 21950:
            case 26579:
            case 27113:
            case 27116:
            	objectDef.varbit = 4771;
            	break;
            case 7962:
            case 8382:
            case 8383:
            case 8551:
            case 8553:
            case 8555:
            case 8557:
            case 9372:
            case 14838:
            case 27114:
            	objectDef.varbit = 4772;
            	break;
            case 7847:
            case 7848:
            case 7849:
            case 7850:
            case 27111:
            	objectDef.varbit = 4773;
            	break;
            case 8150:
            case 8151:
            case 8152:
            case 8153:
            case 27115:
            	objectDef.varbit = 4774;
            	break;
            case 7836:
            case 7837:
            case 7838:
            case 7839:
            case 27112:
            	objectDef.varbit = 4775;
            	break;
			case 11810:
			case 12605:
            	objectDef.varbit = 4953;
            	break;
			case 11811:
			case 12606:
            	objectDef.varbit = 4954;
            	break;
			case 11812:
			case 12607:
            	objectDef.varbit = 4955;
            	break;
			case 11813:
			case 12608:
            	objectDef.varbit = 4956;
            	break;
			case 11814:
			case 13422:
            	objectDef.varbit = 4957;
            	break;
			case 11815:
			case 13423:
            	objectDef.varbit = 4958;
            	break;
			case 11816:
			case 13424:
            	objectDef.varbit = 4959;
            	break;
			case 11817:
			case 13425:
            	objectDef.varbit = 4960;
            	break;
        }


        return objectDef;
    }

    public static void clear() {
        baseModels = null;
        models = null;
        streamIndices = null;
        cache = null;
        stream = null;
    }

    public static void init(FileArchive streamLoader) throws IOException {
//        stream = new Buffer(FileOperations.readFile(Signlink.findcachedir() + "loc.dat"));
//        Buffer stream = new Buffer(FileOperations.readFile(Signlink.findcachedir() + "loc.idx"));
        stream = new Buffer(streamLoader.readFile("loc.dat"));
		Buffer stream = new Buffer(streamLoader.readFile("loc.idx"));
        TOTAL_OBJECTS = stream.readUShort();
        streamIndices = new int[TOTAL_OBJECTS];
        int offset = 2;
        for (int index = 0; index < TOTAL_OBJECTS; index++) {
            streamIndices[index] = offset;
            offset += stream.readUShort();
        }
        cache = new ObjectDefinition[20];
        for (int index = 0; index < 20; index++)
            cache[index] = new ObjectDefinition();

        System.err.println("[Overload] Loaded: " + TOTAL_OBJECTS + " objects");
    }

    public void reset() {
        modelIds = null;
        modelTypes = null;
        name = null;
        description = null;
        modifiedModelColors = null;
        originalModelColors = null;
		modifiedModelTexture = null;
		originalModelTexture = null;
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
        morphisms = null;
    }

    public void loadModels(ResourceProvider archive) {
        if (modelIds == null)
            return;
        for (int index = 0; index < modelIds.length; index++)
            archive.loadExtra(modelIds[index] & 0xffff, 0);
    }

    public boolean method577(int i) {
        if (modelTypes == null) {
            if (modelIds == null)
                return true;
            if (i != 10)
                return true;
            boolean flag1 = true;
            for (int k = 0; k < modelIds.length; k++)
                flag1 &= Model.isCached(modelIds[k] & 0xffff);

            return flag1;
        }
        for (int j = 0; j < modelTypes.length; j++)
            if (modelTypes[j] == i)
                return Model.isCached(modelIds[j] & 0xffff);

        return true;
    }

    public Model modelAt(int type, int orientation, int aY, int bY, int cY, int dY, int frameId) {
        Model model = model(type, frameId, orientation);
        if (model == null)
            return null;
        if (contouredGround || delayShading)
            model = new Model(contouredGround, delayShading, model);
        if (contouredGround) {
            int y = (aY + bY + cY + dY) / 4;
            for (int vertex = 0; vertex < model.numVertices; vertex++) {
                int x = model.vertexX[vertex];
                int z = model.vertexZ[vertex];
                int l2 = aY + ((bY - aY) * (x + 64)) / 128;
                int i3 = dY + ((cY - dY) * (x + 64)) / 128;
                int j3 = l2 + ((i3 - l2) * (z + 64)) / 128;
                model.vertexY[vertex] += j3 - y;
            }

            model.computeSphericalBounds();
        }
        return model;
    }

    public boolean method579() {
        if (modelIds == null)
            return true;
        boolean flag1 = true;
        for (int i = 0; i < modelIds.length; i++)
            flag1 &= Model.isCached(modelIds[i] & 0xffff);
        return flag1;
    }
    
    public ObjectDefinition method580() {
        int i = -1;
        if (varbit != -1) {
            VariableBits varBit = VariableBits.varbits[varbit];
            int j = varBit.getSetting();
            int k = varBit.getLow();
            int l = varBit.getHigh();
            int i1 = Client.BIT_MASKS[l - k];
            i = clientInstance.settings[j] >> k & i1;
        } else if (varp != -1)
            i = clientInstance.settings[varp];

        int var;

        if (i >= 0 && i < morphisms.length) {
            var = morphisms[i];
        } else 
            var = morphisms[morphisms.length - 1];

        return var != -1 ? lookup(var) : null;
    }

    public Model model(int j, int k, int l) {
        Model model = null;
        long l1;
        if (modelTypes == null) {
            if (j != 10)
                return null;
            l1 = (long) ((type << 6) + l) + ((long) (k + 1) << 32);
            Model model_1 = (Model) models.get(l1);
            if (model_1 != null) {
                return model_1;
            }
            if (modelIds == null)
                return null;
            boolean flag1 = inverted ^ (l > 3);
            int k1 = modelIds.length;
            for (int i2 = 0; i2 < k1; i2++) {
                int l2 = modelIds[i2];
                if (flag1)
                    l2 += 0x10000;
                model = (Model) baseModels.get(l2);
                if (model == null) {
                    model = Model.getModel(l2 & 0xffff);
                    if (model == null)
                        return null;
                    if (flag1)
                        model.method477();
                    baseModels.put(model, l2);
                }
                if (k1 > 1)
                    aModelArray741s[i2] = model;
            }

            if (k1 > 1)
                model = new Model(k1, aModelArray741s);
        } else {
            int i1 = -1;
            for (int j1 = 0; j1 < modelTypes.length; j1++) {
                if (modelTypes[j1] != j)
                    continue;
                i1 = j1;
                break;
            }

            if (i1 == -1)
                return null;
            l1 = (long) ((type << 8) + (i1 << 3) + l) + ((long) (k + 1) << 32);
            Model model_2 = (Model) models.get(l1);
            if (model_2 != null) {
                return model_2;
            }
            if (modelIds == null) {
                return null;
            }
            int j2 = modelIds[i1];
            boolean flag3 = inverted ^ (l > 3);
            if (flag3)
                j2 += 0x10000;
            model = (Model) baseModels.get(j2);
            if (model == null) {
                model = Model.getModel(j2 & 0xffff);
                if (model == null)
                    return null;
                if (flag3)
                    model.method477();
                baseModels.put(model, j2);
            }
        }
        boolean flag;
        flag = scaleX != 128 || scaleY != 128 || scaleZ != 128;
        boolean flag2;
        flag2 = translateX != 0 || translateY != 0 || translateZ != 0;
    	Model model_3 = new Model(modifiedModelColors == null,
				Frame.noAnimationInProgress(k), l == 0 && k == -1 && !flag
				&& !flag2, model);
        if (k != -1) {
            model_3.skin();
            model_3.applyTransform(k);
            model_3.faceGroups = null;
            model_3.vertexGroups = null;
        }
        while (l-- > 0)
            model_3.rotate90Degrees();
        if (modifiedModelColors != null) {
            for (int k2 = 0; k2 < modifiedModelColors.length; k2++)
                model_3.recolor(modifiedModelColors[k2],
                        originalModelColors[k2]);

        }
        
        if (flag)
            model_3.scale(scaleX, scaleZ, scaleY);
        if (flag2)
            model_3.translate(translateX, translateY, translateZ);
        model_3.light(85 + ambientLighting, 768 + lightDiffusion, -50, -10, -50, !delayShading);
        if (supportItems == 1)
            model_3.itemDropHeight = model_3.modelBaseY;
        models.put(model_3, l1);
        return model_3;
    }

    public void readValues(Buffer stream) {
		int flag = -1;
		do {
			int type = stream.readUnsignedByte();
			if (type == 0)
				break;
			if (type == 1) {
				int len = stream.readUnsignedByte();
				if (len > 0) {
					if (modelIds == null || lowMemory) {
						modelTypes = new int[len];
						modelIds = new int[len];
						for (int k1 = 0; k1 < len; k1++) {
							modelIds[k1] = stream.readUShort();
							modelTypes[k1] = stream.readUnsignedByte();
						}
					} else {
						stream.currentPosition += len * 3;
					}
				}
			} else if (type == 2)
				name = stream.readString();
			else if (type == 3)
				description = stream.readString();
			else if (type == 5) {
				int len = stream.readUnsignedByte();
				if (len > 0) {
					if (modelIds == null || lowMemory) {
						modelTypes = null;
						modelIds = new int[len];
						for (int l1 = 0; l1 < len; l1++) {
							modelIds[l1] = stream.readUShort();
						}
					} else {
						stream.currentPosition += len * 2;
					}
				}
			} else if (type == 14)
				objectSizeX = stream.readUnsignedByte();
			else if (type == 15)
				objectSizeY = stream.readUnsignedByte();
			else if (type == 17)
				solid = false;
			else if (type == 18)
				impenetrable = false;
			else if (type == 19)
				isInteractive = (stream.readUnsignedByte() == 1);
			else if (type == 21)
				contouredGround = true;
			else if (type == 22)
				delayShading = true;
			else if (type == 23)
				occludes = true;
			else if (type == 24) {
				animation = stream.readUShort();
				if (animation == 65535)
					animation = -1;
			} else if (type == 28)
				decorDisplacement = stream.readUnsignedByte();
			else if (type == 29)
				ambientLighting = stream.readSignedByte();
			else if (type == 39)
				lightDiffusion = stream.readSignedByte();
			else if (type >= 30 && type < 39) {
				if (interactions == null)
					interactions = new String[5];
				interactions[type - 30] = stream.readString();
				if (interactions[type - 30].equalsIgnoreCase("hidden"))
					interactions[type - 30] = null;
			} else if (type == 40) {
				int i1 = stream.readUnsignedByte();
				modifiedModelColors = new int[i1];
				originalModelColors = new int[i1];
				for (int i2 = 0; i2 < i1; i2++) {
					modifiedModelColors[i2] = stream.readUShort();
					originalModelColors[i2] = stream.readUShort();
				}
			} else if (type == 41) {
				int j2 = stream.readUnsignedByte();
				modifiedModelTexture = new short[j2];
				originalModelTexture = new short[j2];
				for (int k = 0; k < j2; k++) {
					modifiedModelTexture[k] = (short) stream.readUShort();
					originalModelTexture[k] = (short) stream.readUShort();
				}

			} else if (type == 60)
				minimapFunction = stream.readUShort();
			else if (type == 62)
				inverted = true;
			else if (type == 64)
				castsShadow = false;
			else if (type == 65)
				scaleX = stream.readUShort();
			else if (type == 66)
				scaleY = stream.readUShort();
			else if (type == 67)
				scaleZ = stream.readUShort();
			else if (type == 68)
				mapscene = stream.readUShort();
			else if (type == 69)
				surroundings = stream.readUnsignedByte();
			else if (type == 70)
				translateX = stream.readShort();
			else if (type == 71)
				translateY = stream.readShort();
			else if (type == 72)
				translateZ = stream.readShort();
			else if (type == 73)
				obstructsGround = true;
			else if (type == 74)
				hollow = true;
			else if (type == 75)
				supportItems = stream.readUnsignedByte();
			else if (type == 77 || type == 92) {
				varp = stream.readUShort();
				if (varp == 65535)
					varp = -1;
				varbit = stream.readUShort();
				if (varbit == 65535)
					varbit = -1;

				int var3 = -1;
				if(type == 92){
					var3 = stream.readUShort();
					if(var3 == 65535)
						var3 = -1;
				}
				
				int j1 = stream.readUnsignedByte();
				morphisms = new int[j1 + 2];
				for (int j2 = 0; j2 <= j1; j2++) {
					morphisms[j2] = stream.readUShort();
					if (morphisms[j2] == 65535)
						morphisms[j2] = -1;
				}
				morphisms[j1 + 1] = var3;
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

}