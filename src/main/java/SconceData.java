import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class SconceData extends SidedWallData {
    
    public Torch torch;
    public boolean hasTorch;
    public boolean isSwitch = false;
    public MultWallSwitchData sconceswitch;
    
    public SconceData(int sde, boolean hasTorch) {
        super(sde);
        mapchar = '}';
        this.hasTorch = hasTorch;
        if (hasTorch) torch = new Torch();
    }
    
    public SconceData(int sde, boolean hasTorch, MultWallSwitchData ss) {
        super(sde);
        mapchar = '}';
        this.hasTorch = hasTorch;
        if (hasTorch) torch = new Torch();
        sconceswitch = ss;
        isSwitch = true;
        sconceswitch.setFacing(sde);
    }
    
    //for loading
    public SconceData(int sde) {
        super(sde);
        mapchar = '}';
    }
    
    public void changeLevel(int amt, int lvlnum) {
        if (isSwitch) sconceswitch.changeLevel(amt, lvlnum);
    }
    
    public void setMapCoord(int level, int x, int y) {
        if (isSwitch) sconceswitch.setMapCoord(level, x, y);
    }
    
    public String toString() {
        String s;
        if (side == 0) s = "Sconce Facing South";
        else if (side == 1) s = "Sconce Facing East";
        else if (side == 2) s = "Sconce Facing North";
        else s = "Sconce Facing West";
        if (hasTorch) s += " - Has Torch";
        if (isSwitch) s += " (Switch)";
        return s;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(hasTorch);
        if (hasTorch) so.writeObject(torch);
        so.writeBoolean(isSwitch);
        if (isSwitch) sconceswitch.save(so);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        hasTorch = si.readBoolean();
        if (hasTorch) torch = (Torch) si.readObject();
        isSwitch = si.readBoolean();
        if (isSwitch) sconceswitch = (MultWallSwitchData) DMEditor.loadMapData(si, -1, 0, 0);
    }
}