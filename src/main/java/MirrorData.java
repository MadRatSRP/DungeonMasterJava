import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class MirrorData extends SidedWallData {
    public boolean wasUsed = false;
    public boolean allowswap = false;
    public HeroData hero;
    public MapPoint target;
    
    public MirrorData(int sde, HeroData hero, boolean allowswap, MapPoint target) {
        super(sde);
        this.hero = hero;
        this.allowswap = allowswap;
        this.target = target;
        mapchar = 'm';
    }
    
    //for loading:
    public MirrorData(int sde) {
        super(sde);
        mapchar = 'm';
    }
    
    public String toString() {
        String temps;
        if (wasUsed) temps = "used";
        else temps = hero.name;
        if (side == 0) return "Mirror (" + temps + ") Facing South";
        else if (side == 1) return "Mirror (" + temps + ") Facing East";
        else if (side == 2) return "Mirror (" + temps + ") Facing North";
        else return "Mirror (" + temps + ") Facing West";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(wasUsed);
        if (!wasUsed) {
            hero.save(so);
            so.writeBoolean(allowswap);
            if (target != null) {
                so.writeBoolean(true);
                so.writeObject(target);
            } else so.writeBoolean(false);
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        wasUsed = si.readBoolean();
        if (!wasUsed) {
            hero = new HeroData(si.readUTF());
            hero.load(si);
            allowswap = si.readBoolean();
            if (si.readBoolean()) target = (MapPoint) si.readObject();
        }
    }
    
}
