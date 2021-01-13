class InvisibleWallData extends WallData {
    
    public InvisibleWallData() {
        super();
        mapchar = 'i';
        canPassMons = true;
        canPassImmaterial = true;
    }
    
    public String toString() {
        return "Invisible Wall";
    }
    
}