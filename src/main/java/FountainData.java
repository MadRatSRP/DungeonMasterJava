import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class FountainData extends SidedWallData {
    public MultWallSwitchData fountainswitch;
    public boolean cantakeitems = true;//if true, items are retrieved when clicked
    public int canputitems = -1;//max size of items that can be put in, -1 for none
    
    //for load
    public FountainData(int s) {
        super(s);
        mapchar = 'f';
    }
    
    //from wizard
    public FountainData(int s, boolean t, int p, MultWallSwitchData fs) {
        super(s);
        mapchar = 'f';
        cantakeitems = t;
        canputitems = p;
        fountainswitch = fs;
        if (fountainswitch != null) fountainswitch.setFacing(s);
    }
    
    public void addItem(Item i) {
        if (i == null) return;
        if (mapItems == null) mapItems = new ArrayList();
        i.subsquare = (side + 2) % 4;
        mapItems.add(i);
        hasItems = true;
        numitemsin[(side + 2) % 4]++;
    }
    
    public void removeItem(int index) {
        if (mapItems.size() == 1) hasItems = false;
        numitemsin[(side + 2) % 4]--;
        mapItems.remove(index);
    }
    
    public void changeLevel(int amt, int lvlnum) {
        if (fountainswitch != null) fountainswitch.changeLevel(amt, lvlnum);
    }
    
    public void setMapCoord(int level, int x, int y) {
        if (fountainswitch != null) fountainswitch.setMapCoord(level, x, y);
    }
    
    public String toString() {
        String s;
        if (side == 0) s = "Fountain Facing South";
        else if (side == 1) s = "Fountain Facing East";
        else if (side == 2) s = "Fountain Facing North";
        else s = "Fountain Facing West";
        if (fountainswitch != null) s += " (Switch)";
        return s;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(cantakeitems);
        so.writeInt(canputitems);
        if (fountainswitch != null) {
            so.writeBoolean(true);
            fountainswitch.save(so);
        } else so.writeBoolean(false);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        cantakeitems = si.readBoolean();
        canputitems = si.readInt();
        if (si.readBoolean()) fountainswitch = (MultWallSwitchData) DMEditor.loadMapData(si, -1, 0, 0);
    }
}