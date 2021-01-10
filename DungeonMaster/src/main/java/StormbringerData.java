import java.io.*;

class StormbringerData extends WallData {
    public boolean wasUsed;
    
    public StormbringerData(boolean wasUsed) {
        super();
        this.wasUsed = wasUsed;
        mapchar = '!';
    }
    
    public String toString() {
        if (!wasUsed) return "Stormbringer";
        else return "Stormbringer (Sword Gone)";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(wasUsed);
    }
}