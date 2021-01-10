import java.io.*;
import java.util.Arrays;

public class SpecialAbility {
    public String name, sound;
    public int action, power, speed, mana, flevelneed, nlevelneed, wlevelneed, plevelneed, count;
    public char classgain;
    public Object data;
    
    public static final String[] actions = {
        "Anti-Ven", "Arc Bolt", "Armor", "Armor Party", "Backstab", "Berserker", "Blow Horn", "Bolt", "Calm", "Climb Down", "Climb Up", "Conjure",
        "Detect Illusion", "Dispell", "Drain Life", "Drain Mana", "Enhance Fist", "Enhance Weapon", "False Image", "Feeble Mind",
        "Fireball", "Freeze", "Freeze Life", "Frighten", "Good Berries", "Heal", "Invoke", "Light", "Purify", "Ruiner", "Shield", "Shield Party",
        "Sight", "Silence", "Slow", "Slowfall", "Spellshield", "Spellshield Party", "Stat Boost", "Steal", "Strip Defenses", "True Sight",
        "Ven Cloud", "Venom", "War Cry", "Weakness", "ZO",
    };
    
    public static int getActionNumber(String a) {
        //return number that "spell" a maps to
        return Arrays.binarySearch(actions, a);
    }
    
    public static String getActionName(int a) {
        //return name of "spell" that action number a maps to
        if (a < actions.length) return actions[a];
        else return null;
    }
    
    public SpecialAbility(String name, String sound, int action, char classgain, int power, int speed, int mana, int flevelneed, int nlevelneed, int wlevelneed, int plevelneed, int count, Object data) {
        this.name = name;
        this.sound = sound;
        this.action = action;
        this.classgain = classgain;
        this.power = power;
        this.speed = speed;
        this.mana = mana;
        this.flevelneed = flevelneed;
        this.nlevelneed = nlevelneed;
        this.wlevelneed = wlevelneed;
        this.plevelneed = plevelneed;
        this.count = count;
        this.data = data;
    }
    
    //copy constructor
    public SpecialAbility(SpecialAbility s) {
        name = new String(s.name);
        sound = new String(s.sound);
        action = s.action;
        classgain = s.classgain;
        power = s.power;
        speed = s.speed;
        mana = s.mana;
        flevelneed = s.flevelneed;
        nlevelneed = s.nlevelneed;
        wlevelneed = s.wlevelneed;
        plevelneed = s.plevelneed;
        count = s.count;
        if (s.data != null) {
            //deal with data here, depends on action
            if (actions[action].startsWith("Enhance") || actions[action].equals("Stat Boost")) {
                int[] olddata = (int[]) s.data;
                data = new int[olddata.length];
                for (int i = 0; i < olddata.length; i++) ((int[]) data)[i] = olddata[i];
            } else if (actions[action].equals("Conjure")) data = Item.createCopy((Item) s.data);
        }
    }
    
    public String toString() {
        return name;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof SpecialAbility) {
            SpecialAbility s = (SpecialAbility) obj;
            if (s.action == action && s.classgain == classgain && s.power == power && s.speed == speed && s.mana == mana && s.name.equals(name))
                return true;
        }
        return false;
    }
    
    public String getActionName() {
        //return name of "spell" that action maps to
        if (action < actions.length) return actions[action];
        else return ""; //will cause ability to do nothing if action # illegal
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        so.writeUTF(name);
        so.writeUTF(sound);
        so.writeInt(action);
        so.writeChar(classgain);
        so.writeInt(power);
        so.writeInt(speed);
        so.writeInt(mana);
        so.writeInt(flevelneed);
        so.writeInt(nlevelneed);
        so.writeInt(wlevelneed);
        so.writeInt(plevelneed);
        so.writeInt(count);
        if (data != null) {
            so.writeBoolean(true);
            so.writeObject(data);
        } else so.writeBoolean(false);
    }
    
    public SpecialAbility(ObjectInputStream si) throws IOException, ClassNotFoundException {
        name = si.readUTF();
        sound = si.readUTF();
        action = si.readInt();
        classgain = si.readChar();
        power = si.readInt();
        speed = si.readInt();
        mana = si.readInt();
        flevelneed = si.readInt();
        nlevelneed = si.readInt();
        wlevelneed = si.readInt();
        plevelneed = si.readInt();
        count = si.readInt();
        if (si.readBoolean()) data = si.readObject();
    }
}