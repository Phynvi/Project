package com.overload.scene;

import com.overload.Client;
import com.overload.cache.anim.Animation;
import com.overload.cache.config.VariableBits;
import com.overload.cache.def.ObjectDefinition;
import com.overload.entity.Renderable;
import com.overload.entity.model.Model;

public final class SceneObject extends Renderable {

    public static Client clientInstance;
    private final int[] morphisms;
    private final int varbit;
    private final int varp;
    private final int anInt1603;
    private final int anInt1604;
    private final int anInt1605;
    private final int anInt1606;
    private final int objectId;
    private final int anInt1611;
    private final int anInt1612;
    private int animLength;
    private Animation objectAnim;
    private int tick;

    public SceneObject(int i, int j, int k, int l, int i1, int j1, int k1, int l1, boolean loop) {
        objectId = i;
        anInt1611 = k;
        anInt1612 = j;
        anInt1603 = j1;
        anInt1604 = l;
        anInt1605 = i1;
        anInt1606 = k1;
        if (l1 != -1) {
            objectAnim = Animation.animations[l1];
            animLength = 0;
            tick = Client.tick;
            if (loop && objectAnim.loopOffset != -1) {
                animLength = (int) (Math.random() * (double) objectAnim.frameCount);
                tick -= (int) (Math.random() * (double) objectAnim.duration(animLength));
            }
        }
        ObjectDefinition objectDef = ObjectDefinition.lookup(objectId);
        varbit = objectDef.varbit;
        varp = objectDef.varp;
        morphisms = objectDef.morphisms;
    }

    private ObjectDefinition method457() {
        int i = -1;
        if (varbit != -1) {
            try {
                VariableBits varBit = VariableBits.varbits[varbit];
                int k = varBit.getSetting();
                int l = varBit.getLow();
                int i1 = varBit.getHigh();
                int j1 = Client.BIT_MASKS[i1 - l];
                i = clientInstance.settings[k] >> l & j1;
            } catch (Exception ex) {
            }
        } else if (varp != -1 && varp < clientInstance.settings.length) {
            i = clientInstance.settings[varp];
        }
        int var;
        if (i >= 0 && i < morphisms.length) {
            var = morphisms[i];
        } else 
            var = morphisms[morphisms.length - 1];

        return var != -1 ? ObjectDefinition.lookup(var) : null;

    }

    public Model getRotatedModel() {
        int j = -1;
        if (objectAnim != null) {
            int k = Client.tick - tick;
            if (k > 100 && objectAnim.loopOffset > 0) {
                k = 100;
            }
            while (k > objectAnim.duration(animLength)) {
                k -= objectAnim.duration(animLength);
                animLength++;
                if (animLength < objectAnim.frameCount)
                    continue;
                animLength -= objectAnim.loopOffset;
                if (animLength >= 0 && animLength < objectAnim.frameCount)
                    continue;
                objectAnim = null;
                break;
            }
            tick = Client.tick - k;
            if (objectAnim != null) {
                j = objectAnim.primaryFrames[animLength];
            }
        }
        ObjectDefinition class46;
        if (morphisms != null)
            class46 = method457();
        else
            class46 = ObjectDefinition.lookup(objectId);
        if (class46 == null) {
            return null;
        } else {
            return class46.modelAt(anInt1611, anInt1612, anInt1603, anInt1604, anInt1605, anInt1606, j);
        }
    }
}