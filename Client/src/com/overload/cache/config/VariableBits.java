package com.overload.cache.config;

import com.overload.cache.FileArchive;
import com.overload.io.Buffer;

public final class VariableBits {

    public static VariableBits varbits[];
    public int setting;
    public int low;
    public int high;
    private boolean aBoolean651;

    private VariableBits() {
        aBoolean651 = false;
    }

    public static void init(FileArchive streamLoader) {
        Buffer stream = new Buffer(streamLoader.readFile("varbit.dat"));
        int size = stream.readUShort();

        if (varbits == null) {
            varbits = new VariableBits[size];
        }

        for (int index = 0; index < size; index++) {

            if (varbits[index] == null) {
                varbits[index] = new VariableBits();
            }

            varbits[index].decode(stream);
            varbits[index].override(index);

            if (varbits[index].aBoolean651) {
                VariablePlayer.variables[varbits[index].setting].aBoolean713 = true;
            }

        }

        if (stream.currentPosition != stream.payload.length) {
            System.out.println("varbit load mismatch");
        }

    }

    private void decode(Buffer stream) {
        setting = stream.readUShort();
        low = stream.readUnsignedByte();
        high = stream.readUnsignedByte();
    }

    private void override(int index) {
    	switch(index) {
			case 733:
				setting = 509;
				low = 0;
				high = 3;
				break;
				
			case 4771:
				setting = 529;
				low = 0;
				high = 7;
				break;
			
			case 4772:
				setting = 529;
				low = 8;
				high = 15;
				break;
			
			case 4773:
				setting = 529;
				low = 16;
				high = 23;
				break;
			
			case 4774:
				setting = 529;
				low = 24;
				high = 31;
				break;
				
			case 4953:
				setting = 529;
				low = 0;
				high = 3;
				break;

			case 4954:
				setting = 529;
				low = 4;
				high = 7;
				break;

			case 4955:
				setting = 529;
				low = 8;
				high = 11;
				break;

			case 4956:
				setting = 529;
				low = 12;
				high = 15;
				break;

			case 4957:
				setting = 529;
				low = 16;
				high = 19;
				break;

			case 4958:
				setting = 529;
				low = 20;
				high = 23;
				break;

			case 4959:
				setting = 529;
				low = 24;
				high = 27;
				break;

			case 4960:
				setting = 529;
				low = 28;
				high = 31;
				break;
    	}
    }
    
    public int getSetting() {
        return setting;
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

}
