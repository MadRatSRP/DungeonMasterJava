import java.io.*;

class FDecorationData extends FloorData {
    public int number, level, xcoord, ycoord; //id of decoration, level,x,y for floor grates
    
    public FDecorationData(int num) {
        super();
        number = num;
        mapchar = 'F';
    }
    
    public void changeLevel(int amt, int lvlnum) {
        level += amt;
    }
    
    public void setMapCoord(int level, int x, int y) {
        this.level = level;
        xcoord = x;
        ycoord = y;
    }
    
    public String toString() {
        if (number == 0) return "Puddle";
        else if (number == 1) return "Grass";
        else if (number == 2) return "Seal";
        else return "Floor Grate";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(number);
        if (number == 3) {
            so.writeInt(level);
            so.writeInt(xcoord);
            so.writeInt(ycoord);
        }
    }
}