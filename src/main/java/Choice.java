import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class Choice {
    public Vector actions;
    public Item needitem;
    public boolean takeitem, visible = true, shouldshow, autotrigger, needdead;
    public int[] needskill;
    public String choicename;
    public int needmons = -1, needmonslvl;
    //public boolean allowswap; //affects all champion join actions
    
    public Choice(String choicename) {
        this.choicename = choicename;
    }
    
    //create a "Leave" choice
    public static Choice makeLeave() {
        Choice c = new Choice("Leave");
        c.actions = new Vector();
        c.actions.add(new Action(0, null, -1));
        return c;
    }
    
    public String toString() {
        return choicename + "  < " + actions.size() + " Action(s) >";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        //write choice stuff
        so.writeUTF(choicename);
        so.writeBoolean(visible);
        so.writeBoolean(autotrigger);
        if (needitem != null) {
            so.writeBoolean(true);
            so.writeObject(needitem);
            so.writeBoolean(takeitem);
        } else so.writeBoolean(false);
        if (needskill != null) {
            so.writeBoolean(true);
            so.writeObject(needskill);
        } else so.writeBoolean(false);
        so.writeInt(needmons);
        if (needmons >= 0) {
            so.writeInt(needmonslvl);
            so.writeBoolean(needdead);
        }
        //write choice's action stuff
        so.writeInt(actions.size());
        Action a;
        for (int i = 0; i < actions.size(); i++) {
            a = ((Action) actions.get(i));
            so.writeInt(a.actiontype);
            if (a.actiontype == 4) ((HeroData) a.action).save(so);
            else if (a.actiontype == 12) ((SpecialAbility) a.action).save(so);
            else so.writeObject(a.action);
            so.writeInt(a.reusable);
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        //read choice stuff
        visible = si.readBoolean();
        autotrigger = si.readBoolean();
        if (si.readBoolean()) {
            needitem = (Item) si.readObject();
            takeitem = si.readBoolean();
        }
        if (si.readBoolean()) {
            needskill = (int[]) si.readObject();
        }
        needmons = si.readInt();
        if (needmons >= 0) {
            needmonslvl = si.readInt();
            needdead = si.readBoolean();
        }
        //read choice's action stuff
        actions = new Vector();
        int numactions = si.readInt();
        Action a;
        for (int i = 0; i < numactions; i++) {
            a = new Action();
            a.actiontype = si.readInt();
            if (a.actiontype == 4) {
                a.action = new HeroData(si.readUTF());
                ((HeroData) a.action).load(si);
            } else if (a.actiontype == 12) a.action = new SpecialAbility(si);
            else a.action = si.readObject();
            a.reusable = si.readInt();
            //if (si.readBoolean()) a.reusable = -1; //update
            actions.add(a);
        }
    }
}
