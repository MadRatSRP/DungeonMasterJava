class FakeWallData extends WallData {
    
    public FakeWallData() {
        super();
        mapchar = '2';
        isPassable = true;
        canPassProjs = true;
        canHoldItems = true;
        canPassMons = true;
        canPassImmaterial = true;
    }
    
    public String toString() {
        return "False Wall";
    }
}
