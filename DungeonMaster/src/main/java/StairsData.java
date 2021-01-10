import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class StairsData extends SidedWallData {
    public boolean goesUp;
    
    public StairsData(int sde, boolean dir) {
        super(sde);
        mapchar = '>';
        isPassable = true;
        canPassMons = true;
        canPassImmaterial = true;
        canHoldItems = true;
        drawItems = true;
        drawFurtherItems = true;
        goesUp = dir;
    }
    
    public String toString() {
        String s;
        if (goesUp) s = "Stairs Going Up, ";
        else s = "Stairs Going Down, ";
        if (side == 0) s += "Facing South";
        else if (side == 1) s += "Facing East";
        else if (side == 2) s += "Facing North";
        else s += "Facing West";
        return s;
    }
    
    public void addItem(Item i) {
        if (mapItems == null) mapItems = new ArrayList();
        if (side == 0) {
            if (i.subsquare == 0) i.subsquare = 3;
            else if (i.subsquare == 1) i.subsquare = 2;
        } else if (side == 2) {
            if (i.subsquare == 2) i.subsquare = 1;
            else if (i.subsquare == 3) i.subsquare = 0;
        } else if (side == 1) {
            if (i.subsquare == 0) i.subsquare = 1;
            else if (i.subsquare == 3) i.subsquare = 2;
        } else {
            if (i.subsquare == 1) i.subsquare = 0;
            else if (i.subsquare == 2) i.subsquare = 3;
        }
        mapItems.add(i);
        hasItems = true;
        numitemsin[i.subsquare]++;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(goesUp);
    }
    
}