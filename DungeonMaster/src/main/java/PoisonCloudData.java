import java.io.ObjectOutputStream;
import java.io.IOException;

class PoisonCloudData {
    public int level, x, y, stage, stagecounter;
    
    public PoisonCloudData(int level, int x, int y, int stage, int stagecounter) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.stage = stage;
        this.stagecounter = stagecounter;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        so.writeInt(level);
        so.writeInt(x);
        so.writeInt(y);
        so.writeInt(stage);
        so.writeInt(stagecounter);
    }
}