import java.io.IOException;
import java.io.ObjectOutputStream;

class FluxCageData {
    public int level, x, y, counter;
    
    public FluxCageData(int level, int x, int y, int counter) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.counter = counter;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        so.writeInt(level);
        so.writeInt(x);
        so.writeInt(y);
        so.writeInt(counter);
    }
}