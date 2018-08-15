package com.overload.net;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class BitConfig {

    private final int id;
    private final int value;

    public BitConfig(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public final int getId() {
        return id;
    }

    public final int getValue() {
        return value;
    }
}
