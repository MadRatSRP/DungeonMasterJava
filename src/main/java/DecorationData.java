import java.io.IOException;
import java.io.ObjectOutputStream;

class DecorationData extends SidedWallData {
    public int number; //id of decoration
    
    public DecorationData(int sde, int num) {
        super(sde);
        number = num;
        mapchar = 'D';
    }
    
    public String toString() {
        String s;
        if (number == 0) s = "Ring";
        else if (number == 1) s = "Hook";
        else if (number == 2) s = "Slime";
        else if (number == 3) s = "Grate";
        else if (number == 4) s = "Drain";
        else if (number == 5) s = "Crack";
        else if (number == 6) s = "Scratches";
        else if (number == 9) s = "Demon";
        else s = "Chaos";
        if (side == 0) s += " Facing South";
        else if (side == 1) s += " Facing East";
        else if (side == 2) s += " Facing North";
        else s += " Facing West";
        return s;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(number);
    }
}