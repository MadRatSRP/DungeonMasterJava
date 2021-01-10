import java.io.IOException;
import java.io.ObjectOutputStream;

class PillarData extends WallData {
    
    public int type;
    public boolean mirror;
    public String custompic;
    
    //public PillarData(int type) { this(type,false); }
    
    public PillarData(int type, boolean mirror) {
        super();
        mapchar = 'P';
        this.type = type;
        this.mirror = mirror;
    }
    
    public void setMapCoord(int level, int x, int y) {
        mirror = ((x + y) % 2 == 0);
    }
    
    public String toString() {
        if (type == 0) return "Pillar (A)";
        else if (type == 1) return "Pillar (B)";
        else return "Pillar (Custom)";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(type);
        so.writeBoolean(mirror);
        if (type == 2) so.writeUTF(custompic);
    }
}