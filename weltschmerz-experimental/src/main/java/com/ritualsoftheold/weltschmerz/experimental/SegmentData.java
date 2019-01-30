package com.ritualsoftheold.weltschmerz.experimental;

public class SegmentData {
    public int x0, x1;
    public int y0, y1;
    public int area;
    public int coll_count = 0;
    public SegmentData( int x0, int x1, int y0, int y1,
                        int area){
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.area = area;
    }
}
