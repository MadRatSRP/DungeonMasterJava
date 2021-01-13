class AltarData extends OneAlcoveData {
    
    public AltarData(int sde) {
        super(sde);
        mapchar = 'a';
    }
    
    public AltarData(int sde, MultWallSwitchData fs) {
        super(sde, fs);
        mapchar = 'a';
    }
    
    public String toString() {
        String s;
        if (side == 0) s = "Altar Facing South";
        else if (side == 1) s = "Altar Facing East";
        else if (side == 2) s = "Altar Facing North";
        else s = "Altar Facing West";
        if (super.isSwitch) s += " (Switch)";
        return s;
    }
}