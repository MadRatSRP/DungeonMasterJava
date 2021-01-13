import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class MapData {
    
    public int[] numitemsin = new int[4];
    public boolean[] hasmonin = new boolean[5];
    public boolean nomons = false;
    public boolean noghosts = false;
    
    public char mapchar;
    public int numProjs = 0;
    public ArrayList mapItems;
    
    public boolean canHoldItems;            //true if can set items down on
    public boolean isPassable;              //true if can walk through
    public boolean hasParty;                //true if party standing on this square
    public boolean canPassProjs;            //true if can fire projectiles through
    public boolean canPassMons = true;      //true if mons could step on (if !ispassable, only affects immaterial)
    public boolean canPassImmaterial = true;//true if immaterial mon can be on
    public boolean hasMons;                 //true if has a mon standing on
    //public boolean hasImmaterialMons;       //true if has immaterial mon in
    public boolean hasCloud;                //true if has poison cloud in
    public boolean hasItems;                //true if has any items on
    public boolean drawItems;               //true if should draw any, false if embedded in wall or something
    public boolean drawFurtherItems;        //true if should draw 0,1 subsquares (true for floor, false for closed door, etc)
    
    public MapData() {
        //mapItems = new ArrayList(4);
    }
        
        /*public void addMon(int subsquare) {
                if (subsquare==5) subsquare=4;
                hasmonin[subsquare]=true;
        }
        public void removeMon(int subsquare) {
                if (subsquare==5) subsquare=4;
                hasmonin[subsquare]=false;
        }*/
    
    public void addItem(Item i) {
        if (i == null) return;
        if (mapItems == null) mapItems = new ArrayList();
        mapItems.add(i);
        hasItems = true;
        numitemsin[i.subsquare]++;
    }
    
    public void removeItem(int index) {
        if (mapItems.size() == 1) hasItems = false;
        numitemsin[((Item) mapItems.get(index)).subsquare]--;
        mapItems.remove(index);
    }
    
    public void changeLevel(int amt, int lvlnum) {
    }
    
    public void setMapCoord(int level, int x, int y) {
    }
    
    public String toString() {
        return "" + mapchar;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        so.writeChar(mapchar);
        so.writeBoolean(canHoldItems);
        so.writeBoolean(isPassable);
        so.writeBoolean(canPassProjs);
        so.writeBoolean(canPassMons);
        so.writeBoolean(canPassImmaterial);
        so.writeBoolean(drawItems);
        so.writeBoolean(drawFurtherItems);
        so.writeInt(numProjs);
        so.writeBoolean(hasParty);
        so.writeBoolean(hasMons);
        //so.writeBoolean(hasImmaterialMons);
        so.writeBoolean(hasItems);
        if (hasItems) so.writeObject(mapItems);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
    }
}