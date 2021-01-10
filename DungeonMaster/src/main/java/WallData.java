class WallData extends MapData {
    
    public WallData() {
        super();
        mapchar = '1';
        canPassMons = true;
        canPassImmaterial = true;
    }
    
    public String toString() {
        return "Wall";
    }
    
}