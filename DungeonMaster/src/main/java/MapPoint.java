class MapPoint extends Object implements java.io.Serializable {
    
    public final int level, x, y;
    
    public MapPoint(int level, int x, int y) {
        this.level = level;
        this.x = x;
        this.y = y;
    }
    
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MapPoint)) return false;
        if (((MapPoint) obj).level != level || ((MapPoint) obj).x != x || ((MapPoint) obj).y != y) return false;
        return true;
    }
}