import java.io.IOException;
import java.io.ObjectOutputStream;

class PowerGemData extends WallData {
    public boolean wasUsed = false;
    
    public PowerGemData(boolean wasUsed) {
        super();
        this.wasUsed = wasUsed;
        mapchar = 'G';
    }
    
    public String toString() {
        if (!wasUsed) return "PowerGem";
        else return "PowerGem (Gone)";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(wasUsed);
    }
}