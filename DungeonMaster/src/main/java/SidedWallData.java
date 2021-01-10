import java.io.IOException;
import java.io.ObjectOutputStream;

class SidedWallData extends WallData {
    protected int side; //dir party must face to see special directly
    
    public SidedWallData(int sde) {
        super();
        side = sde;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(side);
    }
}