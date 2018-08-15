package com.overload.game.model;

import com.overload.game.entity.impl.player.Player;
import com.overload.util.Misc;


/**
 * Represents a single world tile position.
 *
 * @author relex lawl
 */

public class Position {

    /**
     * The x coordinate of the position.
     */
    private int x;
    /**
     * The y coordinate of the position.
     */
    private int y;
    /**
     * The height level of the position.
     */
    private int z;

    /**
     * The Position constructor.
     *
     * @param x The x-type coordinate of the position.
     * @param y The y-type coordinate of the position.
     * @param z The height of the position.
     */
    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * The Position constructor.
     *
     * @param x The x-type coordinate of the position.
     * @param y The y-type coordinate of the position.
     */
    public Position(int x, int y) {
        this(x, y, 0);
    }

    /**
     * Gets the x coordinate of this position.
     *
     * @return The associated x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of this position.
     *
     * @return The Position instance.
     */
    public Position setX(int x) {
        this.x = x;
        return this;
    }

    /**
     * Gets the y coordinate of this position.
     *
     * @return The associated y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate of this position.
     *
     * @return The Position instance.
     */
    public Position setY(int y) {
        this.y = y;
        return this;
    }

    /**
     * Gets the height level of this position.
     *
     * @return The associated height level.
     */
    public int getZ() {
        return z;
    }

    /**
     * Sets the height level of this position.
     *
     * @return The Position instance.
     */
    public Position setZ(int z) {
        this.z = z;
        return this;
    }

    /**
     * Sets the player's associated Position values.
     *
     * @param x The new x coordinate.
     * @param y The new y coordinate.
     * @param z The new height level.
     */
    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setAs(Position other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    /**
     * Creates a new location based on this location.
     *
     * @param diffX X difference.
     * @param diffY Y difference.
     * @param diffZ Z difference.
     * @return The new location.
     */
    public Position transform(int diffX, int diffY, int diffZ) {
        return Position.create(x + diffX, y + diffY);
    }

    /**
     * Creates a new location based on this location.
     *
     * @param vector The vector to transform by.
     * @return The new location.
     */
    public Position transform(final Position vector) {
        return Position.create(x + (int) getX(), y + (int) getY());
    }

    public double distance(Position other) {
        if (z != other.z) {
            return Double.MAX_VALUE - 1;
        }
        return distanceFormula(x, y, other.x, other.y);
    }


    public double distance(int x, int y, int z) {
        if (this.z != z) {
            return Double.MAX_VALUE - 1;
        }
        return distanceFormula(this.x, this.y, x, y);
    }

    public static double distanceFormula(int x, int y, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
    }


    /**
     * Gets the local x coordinate relative to a specific region.
     *
     * @param position The region the coordinate will be relative to.
     * @return The local x coordinate.
     */
    public int getLocalX(Position position) {
        return x - 8 * position.getRegionX();
    }

    /**
     * Gets the local y coordinate relative to a specific region.
     *
     * @param position The region the coordinate will be relative to.
     * @return The local y coordinate.
     */
    public int getLocalY(Position position) {
        return y - 8 * position.getRegionY();
    }

    /**
     * Gets the local x coordinate relative to a specific region.
     *
     * @return The local x coordinate.
     */
    public int getLocalX() {
        return x - 8 * getRegionX();
    }

    /**
     * Gets the local y coordinate relative to a specific region.
     *
     * @return The local y coordinate.
     */
    public int getLocalY() {
        return y - 8 * getRegionY();
    }

    /**
     * Gets the region x coordinate.
     *
     * @return The region x coordinate.
     */
    public int getRegionX() {
        return (x >> 3) - 6;
    }

    /**
     * Gets the region y coordinate.
     *
     * @return The region y coordinate.
     */
    public int getRegionY() {
        return (y >> 3) - 6;
    }

    /**
     * Adds steps/coordinates to this position.
     */
    public Position add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Adds steps/coordinates to this position.
     */
    public Position add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Position addX(int x) {
        this.x += x;
        return this;
    }

    public Position addY(int y) {
        this.y += y;
        return this;
    }

    public Position addZ(int z) {
        this.z += z;
        return this;
    }
    
    public Position take(int x, int y, int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Position takeX(int x) {
        this.x -= x;
        return this;
    }

    public Position takeY(int y) {
        this.y -= y;
        return this;
    }

    public Position takeZ(int z) {
        this.z -= z;
        return this;
    }

    public static Position create(int x, int y) {
        return new Position(x, y, 0);
    }

    /**
     * Checks if this location is within range of another.
     *
     * @param other The other location.
     * @return <code>true</code> if the location is in range,
     * <code>false</code> if not.
     */
    public boolean isWithinDistance(Position other) {
        if (z != other.z)
            return false;
        int deltaX = other.x - x, deltaY = other.y - y;
        return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
    }


    public static boolean goodDistance(int objectX, int objectY, int playerX,
                                       int playerY, int distance) {
        if (playerX == objectX && playerY == objectY)
            return true;
        for (int i = 0; i <= distance; i++) {
            for (int j = 0; j <= distance; j++) {
                if ((objectX + i) == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if ((objectX - i) == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if (objectX == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean goodDistance(Position pos1, Position pos2, int distanceReq) {
        if(pos1.getZ() != pos2.getZ())
            return false;
        return goodDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), distanceReq);
    }

    /**
     * Checks if the position is within distance of another.
     *
     * @param other    The other position.
     * @param distance The distance.
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean isWithinDistance(Position other, int distance) {
        if (z != other.getZ())
            return false;
        int deltaX = Math.abs(x - other.x);
        int deltaY = Math.abs(y - other.y);
        return deltaX <= distance && deltaY <= distance;
    }

    /**
     * Checks if this location is within interaction range of another.
     *
     * @param other The other location.
     * @return <code>true</code> if the location is in range,
     * <code>false</code> if not.
     */
    public boolean isWithinInteractionDistance(Position other) {
        if (z != other.z) {
            return false;
        }
        int deltaX = other.x - x, deltaY = other.y - y;
        return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
    }

    /**
     * Gets the distance between this position and another position. Only X and
     * Y are considered (i.e. 2 dimensions).
     *
     * @param other The other position.
     * @return The distance.
     */
    public int getDistance(Position other) {
        int deltaX = x - other.x;
        int deltaY = y - other.y;
        return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }

    /**
     * Checks if {@code position} has the same values as this position.
     *
     * @param position The position to check.
     * @return The values of {@code position} are the same as this position's.
     */
    public boolean sameAs(Position position) {
        return position.x == x && position.y == y && position.z == z;
    }

    public double distanceToPoint(int pointX, int pointY) {
        return Math.sqrt(Math.pow(x - pointX, 2)
                + Math.pow(y - pointY, 2));
    }
    public boolean inPosition(Position southWest, Position northEast, boolean inclusive) {
        return !inclusive ? this.x > southWest.getX() && this.x < northEast.getX() && this.y > southWest.getY() && this.y < northEast.getY() : this.x >= southWest.getX() && this.x <= northEast.getX() && this.y >= southWest.getY() && this.y <= northEast.getY();
    }

    public static boolean inGnomeCourse(Player entity) {
        return entity.getPosition().inPosition(new Position(2469, 3414), new Position(2490, 3440), true) && entity.getPosition().getZ() == 0;
    }

    public static boolean inBarbarianCourse(Player entity) {
        return entity.getPosition().inPosition(new Position(2530, 3543), new Position(2553, 3556), true) && entity.getPosition().getZ() == 0;
    }

    public boolean withinDistance(Position loc, int distance) {
        return Math.abs(x - loc.x) <= distance && Math.abs(y - loc.y) <= distance;
    }

    public static boolean inWildernessCourse(Player entity) {
        return entity.getPosition().inPosition(new Position(2992, 3931), new Position(3007, 3961), true) && entity.getPosition().getZ() == 0;
    }

    @Override
    public Position clone() {
        return new Position(x, y, z);
    }

    @Override
    public String toString() {
        return "Position values: [x, y, z] - [" + x + ", " + y + ", " + z + "].";
    }

    @Override
    public int hashCode() {
        return z << 30 | x << 15 | y;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Position)) {
            return false;
        }
        Position position = (Position) other;
        return position.x == x && position.y == y && position.z == z;
    }

    public boolean isViewableFrom(Position other) {
        if (this.getZ() != other.getZ())
            return false;
        Position p = Misc.delta(this, other);
        return p.x <= 14 && p.x >= -15 && p.y <= 14 && p.y >= -15;
    }
    
    public Position getDelta(Position position) {
		return new Position(x - position.x, y - position.y);
	}
}