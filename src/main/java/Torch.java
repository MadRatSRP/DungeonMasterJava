import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Torch extends Item {
    
    public int lightboost = 225;
    transient private static Image[] torchpics;
    
    public Torch() {
        super();
        name = "Torch";
        number = 9;
        type = WEAPON;
        weight = 1.1f;
        size = 3;
        throwpow = 2;
        functions = 1;
        function = new String[1][2];
        function[0][0] = "Swing";
        function[0][1] = "f";
        power = new int[1];
        power[0] = 2;
        speed = new int[1];
        speed[0] = 8;
        level = new int[1];
        level[0] = 0;
        picstring = "torch1.gif";
        dpicstring = "dtorch.gif";
        setPics();
    }
    
    public void burnDown() {
        lightboost -= 20;
        if (lightboost < 0) lightboost = 0;
        setPic();
    }
    
    public void putOut() {
        pic = torchpics[6];
        picstring = "torch7.gif";
    }
    
    public void setPic() {
        //set pic
        if (lightboost == 0) {
            pic = torchpics[6];
            picstring = "torch7.gif";
        } else if (lightboost > 0 && lightboost < 37) {
            pic = torchpics[5];
            picstring = "torch6.gif";
        } else if (lightboost >= 37 && lightboost < 74) {
            pic = torchpics[4];
            picstring = "torch5.gif";
        } else if (lightboost >= 74 && lightboost < 111) {
            pic = torchpics[3];
            picstring = "torch4.gif";
        } else if (lightboost >= 111 && lightboost < 148) {
            pic = torchpics[2];
            picstring = "torch3.gif";
        } else if (lightboost >= 148 && lightboost < 185) {
            pic = torchpics[1];
            picstring = "torch2.gif";
        } else {
            pic = torchpics[0];
            picstring = "torch1.gif";
        }
    }
    
    public void setPics() {
        if (torchpics == null) {
            torchpics = new Image[7];
            torchpics[0] = (Image) pics.get("torch1.gif");
            if (torchpics[0] == null) {
                torchpics[0] = tk.getImage("Items" + File.separator + "torch1.gif");
                ImageTracker.addImage(torchpics[0], 0);
            }
            torchpics[1] = (Image) pics.get("torch2.gif");
            if (torchpics[1] == null) {
                torchpics[1] = tk.getImage("Items" + File.separator + "torch2.gif");
                ImageTracker.addImage(torchpics[1], 0);
            }
            torchpics[2] = (Image) pics.get("torch3.gif");
            if (torchpics[2] == null) {
                torchpics[2] = tk.getImage("Items" + File.separator + "torch3.gif");
                ImageTracker.addImage(torchpics[2], 0);
            }
            torchpics[3] = (Image) pics.get("torch4.gif");
            if (torchpics[3] == null) {
                torchpics[3] = tk.getImage("Items" + File.separator + "torch4.gif");
                ImageTracker.addImage(torchpics[3], 0);
            }
            torchpics[4] = (Image) pics.get("torch5.gif");
            if (torchpics[4] == null) {
                torchpics[4] = tk.getImage("Items" + File.separator + "torch5.gif");
                ImageTracker.addImage(torchpics[4], 0);
            }
            torchpics[5] = (Image) pics.get("torch6.gif");
            if (torchpics[5] == null) {
                torchpics[5] = tk.getImage("Items" + File.separator + "torch6.gif");
                ImageTracker.addImage(torchpics[5], 0);
            }
            torchpics[6] = (Image) pics.get("torch7.gif");
            if (torchpics[6] == null) {
                torchpics[6] = tk.getImage("Items" + File.separator + "torch7.gif");
                ImageTracker.addImage(torchpics[6], 0);
            }
        }
        pic = torchpics[6];
        dpic = (Image) pics.get(dpicstring);
        if (dpic == null) {
            dpic = tk.createImage("Items" + File.separator + dpicstring);
            pics.put(dpicstring, dpic);
            ImageTracker.addImage(dpic, 0);
        }
    }
    
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeInt(lightboost);
    }
    
    private void readObject(ObjectInputStream s) throws IOException {
        lightboost = s.readInt();
        setPics();
    }
    
}