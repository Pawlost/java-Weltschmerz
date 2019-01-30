package com.ritualsoftheold.weltschmerz.experimental;

public interface PlateInterface {
    int addCollision(int wx, int wy);
    void addCrustByCollision(int x, int y, float z, int t);
    void addCrustBySubduction(int x, int y, float z, int t, double dx, double dy);
    float aggregateCrust(Plate p, int wx, int wy);
    void applyFriction(float deforming_mass);
    void collide(Plate p, int wx, int wy, float coll_mass);
    void erode(float lower_bound);
    int getContinentArea(int wx, int wy);
    float getCrust(int x, int y);
    int getCrustTimestamp(int x, int y);
    float[] getMap();
    void move();
    void resetSegments();
    void selectCollisionSegment(int coll_x, int coll_y);
    void setCrust(int x, int y, float z, int t);
    float getMomentum();
    int getHeight();
    float getLeft();
    float getTop();
    float getVelocity();
    float getVelX();
    float getVelY();
    int getWidth();
    boolean isEmpty();
    int createSegment(int wx, int wy);
    int getMapIndex(int x, int y);
}
