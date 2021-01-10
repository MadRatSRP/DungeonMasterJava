import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Vector;

public class EventSquareData extends FloorData {
    public String[] eventtext; //original text
    public Color textcolor;
    public Choice[] choices;
    public int eventface, picalign, textalign;//party facing requirement; and alignments: 0 = bottom, 1 = center, else top
    public String picname, soundstring;
    public boolean blackback, loopsound;
    
    //wizard constructor
    public EventSquareData(int eventface, String picname, int picalign, boolean blackback, Color textcolor, int textalign, String[] eventtext, String soundstring, boolean loopsound, Choice[] choices) {
        super();
        this.eventface = eventface;
        this.picname = picname;
        this.picalign = picalign;
        this.blackback = blackback;
        this.textcolor = textcolor;
        this.textalign = textalign;
        this.eventtext = eventtext;
        this.soundstring = soundstring;
        this.loopsound = loopsound;
        this.choices = choices;
        if (picname.equals("")) this.picname = null;
        if (soundstring.equals("")) {
            this.soundstring = null;
            this.loopsound = false;
        }
        mapchar = 'E';
    }
    
    //for loading
    public EventSquareData() {
        super();
        mapchar = 'E';
    }
    
    public String toString() {
        return "Event Square";
    }
    
    public void changeLevel(int amt, int lvlnum) {
        Action a;
        for (int i = 0; i < choices.length; i++) {
            for (int j = 0; j < choices[i].actions.size(); j++) {
                a = (Action) choices[i].actions.get(j);
                if (a.actiontype > 0 && a.actiontype < 4) {
                    MapPoint target = (MapPoint) a.action;
                    if (target.level >= lvlnum) target = new MapPoint(target.level + amt, target.x, target.y);
                    if (target.level < 0) target = new MapPoint(0, target.x, target.y);
                    a.action = target;
                }
            }
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(choices.length);
        if (choices.length > 0) {
            so.writeInt(eventface);
            if (picname != null) {
                so.writeBoolean(true);
                so.writeUTF(picname);
                so.writeInt(picalign);
            } else so.writeBoolean(false);
            so.writeBoolean(blackback);
            so.writeInt(textcolor.getRed());
            so.writeInt(textcolor.getGreen());
            so.writeInt(textcolor.getBlue());
            so.writeInt(textalign);
            so.writeObject(eventtext);
            if (soundstring != null) {
                so.writeBoolean(true);
                so.writeUTF(soundstring);
                so.writeBoolean(loopsound);
            } else so.writeBoolean(false);
            for (int i = 0; i < choices.length; i++) {
                choices[i].save(so);
            }
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        int numchoices = si.readInt();
        if (numchoices > 0) {
            eventface = si.readInt();
            if (si.readBoolean()) {
                picname = si.readUTF();
                picalign = si.readInt();
            }
            blackback = si.readBoolean();
            textcolor = new Color(si.readInt(), si.readInt(), si.readInt());
            textalign = si.readInt();
            eventtext = (String[]) si.readObject();
            if (si.readBoolean()) {
                soundstring = si.readUTF();
                loopsound = si.readBoolean();
            }
            choices = new Choice[numchoices];
            for (int i = 0; i < numchoices; i++) {
                choices[i] = new Choice(si.readUTF());
                choices[i].load(si);
            }
        }
    }
    
}
