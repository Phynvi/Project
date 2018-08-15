package com.overload.game.model;

/**
 * Represents a rectangular boundary.
 *
 * @author Professor Oak
 */
public class Boundary {

    public static final Boundary FIGHT_CAVE = new Boundary(2365, 5052, 2437, 5123, true);
    public static final Boundary ZULRAH = new Boundary(2248, 3059, 2283, 3084, true);
	
    private final int x;
    private final int x2;
    private final int y;
    private final int y2;
    private final int z;
    private final int z2;

    public Boundary(int x, int x2, int y, int y2) {
        this.x = x;
        this.x2 = x2;
        this.y = y;
        this.y2 = y2;
        this.z = -1;
        this.z2 = -1;
    }
    
    public Boundary(int x, int y, int x2, int y2, boolean n) {
        this.x = x;
        this.x2 = x2;
        this.y = y;
        this.y2 = y2;
        this.z = -1;
        this.z2 = -1;
    }
    
    public Boundary(int x, int y, int x2, int y2, int z) {
        this.x = x;
        this.x2 = x2;
        this.y = y;
        this.y2 = y2;
        this.z = z;
        this.z2 = z;
    }
    
    public Boundary(int x, int y, int x2, int y2, int z, int z2) {
        this.x = x;
        this.x2 = x2;
        this.y = y;
        this.y2 = y2;
        this.z = z;
        this.z2 = z2;
    }

    public Boundary(int x, int y) {
        this.x = x;
        this.x2 = x;
        this.y = y;
        this.y2 = y;
        this.z = -1;
        this.z2 = -1;
	}

	public int getX() {
        return x;
    }

    public int getX2() {
        return x2;
    }

    public int getY() {
        return y;
    }

    public int getY2() {
        return y2;
    }

    public int getZ() {
        return z;
    }

    public int getZ2() {
        return z2;
    }

    public boolean inside(Position p) {
    	boolean ignoreZ = z == -1 || z2 == -1;
        if (p.getX() >= x && p.getX() <= x2 && p.getY() >= y && p.getY() <= y2) {
        	if (ignoreZ)
        		return true;
        	else {
        		if (p.getZ() >= z && p.getZ() <= z2)
        			return true;
        	}
        }
        return false;
    }
}
