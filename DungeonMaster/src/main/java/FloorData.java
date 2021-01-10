class FloorData extends MapData {
    
    public FloorData() {
        super();
        mapchar = '0';
        isPassable = true;
        canPassProjs = true;
        canHoldItems = true;
        drawItems = true;
        drawFurtherItems = true;
    }
    
    public String toString() {
        return "Floor";
    }
    
}