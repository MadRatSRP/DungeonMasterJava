import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Waterskin extends Item {
    int drinks;             //# drinks remaining
    transient Image altpic; //empty (or full) pic
    static final long serialVersionUID = -1356018247955633413L;
    
    public Waterskin() {
        this(3);
    }
    
    public Waterskin(int drinks) {
        super();
        this.drinks = drinks;
        number = 73;
        type = OTHER;
        name = "Waterskin";
        weight = 0.9f;//.9 is full, changed by fountain and drinking (.2 for each drink)
        size = 1;
        foodvalue = 300;
        picstring = "waterskin.gif";
        dpicstring = "dwaterskin.gif";
        setPics();
    }
    
    private void setPics() {
        pic = (Image) pics.get(picstring);
        dpic = (Image) pics.get(dpicstring);
        altpic = (Image) pics.get("waterskin_empty.gif");
        if (pic == null) {
            pic = tk.createImage("Items" + File.separator + picstring);
            pics.put(picstring, pic);
            ImageTracker.addImage(pic, 0);
        }
        if (dpic == null) {
            dpic = tk.createImage("Items" + File.separator + dpicstring);
            pics.put(dpicstring, dpic);
            ImageTracker.addImage(dpic, 0);
        }
        if (altpic == null) {
            altpic = tk.createImage("Items" + File.separator + "waterskin_empty.gif");
            pics.put("waterskin_empty.gif", altpic);
            ImageTracker.addImage(altpic, 0);
        }
        if (drinks == 0) swapPics();
    }
    
    //called by fountain
    public void swapPics() {
        Image temppic = pic;
        pic = altpic;
        altpic = temppic;
    }
    
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeInt(drinks);
    }
    
    private void readObject(ObjectInputStream s) throws IOException {
        drinks = s.readInt();
        setPics();
    }
}
