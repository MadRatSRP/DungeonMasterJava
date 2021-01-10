import java.awt.*;
import java.io.File;

class Chest extends Item {
    
    public Item[] contents;
    
    public Chest() {
        super();
        name = "Chest";
        number = 5;
        type = OTHER;
        weight = 4.0f;
        size = 4;
        throwpow = 2;
        picstring = "chest.gif";
        dpicstring = "dchest.gif";
        contents = new Item[12];
        pic = (Image) pics.get(picstring);
        if (pic == null) {
            pic = tk.createImage("Items" + File.separator + picstring);
            pics.put(picstring, pic);
            ImageTracker.addImage(pic, 0);
        }
        dpic = (Image) pics.get(dpicstring);
        if (dpic == null) {
            dpic = tk.createImage("Items" + File.separator + dpicstring);
            pics.put(dpicstring, dpic);
            ImageTracker.addImage(dpic, 0);
        }
    }
    
    public Item getItem(int index) {
        if (contents[index] != null) {
            Item tempitem = contents[index];
            contents[index] = null;
            setWeight();
            return tempitem;
        } else return null;
    }
    
    public void putItem(int index, Item it) {
        contents[index] = it;
        setWeight();
    }
    
    public Item itemAt(int index) {
        if (index >= contents.length) return null;
        if (index >= 0 && index < 12 && contents[index] != null) return contents[index];
        else return null;
    }
    
    private void setWeight() {
        weight = 4.0f;
        for (int i = 0; i < 12; i++) {
            if (contents[i] != null) weight += contents[i].weight;
        }
    }
    
}