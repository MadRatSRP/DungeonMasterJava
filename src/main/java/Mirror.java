import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Mirror extends SidedWall2 {
    static public boolean ADDEDPICS;
    public dmnew.Hero hero;
    public boolean wasUsed = false;
    public boolean allowswap = false;
    public MapPoint target;
    
    public Mirror(int sde, dmnew.Hero h) {
        super(sde);
        hero = h;
        mapchar = 'm';
        setPics();
    }
    
    //constructor for loading only:
    public Mirror(int sde) {
        super(sde);
        mapchar = 'm';
    }
    
    protected void setPics() {
        super.setPics();
        facingside[0] = loadPic("mirror1.gif");
        facingside[1] = loadPic("mirror2.gif");
        facingside[2] = loadPic("mirror3.gif");
        col1pic[0] = loadPic("mirrorcol11.gif");
        col1pic[1] = loadPic("mirrorcol12.gif");
        col1pic[2] = loadPic("mirrorcol13.gif");
        col3pic[0] = loadPic("mirrorcol31.gif");
        col3pic[1] = loadPic("mirrorcol32.gif");
        col3pic[2] = loadPic("mirrorcol33.gif");
        if (!wasUsed) tracker.addImage(hero.pic, 0);
        if (!ADDEDPICS) {
            tracker.addImage(facingside[0], 0);
            tracker.addImage(facingside[1], 0);
            tracker.addImage(facingside[2], 0);
            tracker.addImage(col1pic[0], 0);
            tracker.addImage(col1pic[1], 0);
            tracker.addImage(col1pic[2], 0);
            tracker.addImage(col3pic[0], 0);
            tracker.addImage(col3pic[1], 0);
            tracker.addImage(col3pic[2], 0);
            ADDEDPICS = true;
        }
        //xy for facing
        xadjust[0] = 109;
        yadjust[0] = 55;
        xadjust[1] = 76;
        yadjust[1] = 36;
        xadjust[2] = 57;
        yadjust[2] = 26;
        //xy for col1
        xadjust[3] = 80;
        yadjust[3] = 58;
        xadjust[4] = 127;
        yadjust[4] = 38;
        xadjust[5] = 154;
        yadjust[5] = 29;
        //xy for col3
        xadjust[6] = 4;
        yadjust[6] = 58;
        xadjust[7] = 1;
        yadjust[7] = 38;
        xadjust[8] = 0;
        yadjust[8] = 29;
        
    }
    
    public void tryWallSwitch(int x, int y) {
        if (wasUsed || dmnew.facing != side) return;
        if (x > 177 && x < 265 && y > 80 && y < 161) {
            int yadjust = dmnew.partyy, xadjust = dmnew.partyx;
            if (dmnew.facing == dmnew.NORTH) yadjust--;
            else if (dmnew.facing == dmnew.WEST) xadjust--;
            else if (dmnew.facing == dmnew.SOUTH) yadjust++;
            else xadjust++;
            
            dmnew.herolookx = xadjust;
            dmnew.herolooky = yadjust;
            dmnew.mirrorhero = hero;
            dmnew.herolook = true;
            dmnew.allowswap = allowswap;
            
            //code in dmnew to get hero
            //disables hclick,sets sheet=true
            //sets wasused to true
        }
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        tryWallSwitch(x, y);
        return true;
    }
    
    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        super.drawPic(row, col, xc, yc, g, obs);
        if (wasUsed || row != 1 || col != 2 || dmnew.facing != side) return;
        if (!dmnew.NOTRANS) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f));
            g2.drawImage(hero.pic, xc + 125, yc + 70, obs);
            g2.dispose();
        } else g.drawImage(hero.pic, xc + 125, yc + 70, obs);
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeBoolean(wasUsed);
        if (!wasUsed) {
                        /*
                        so.writeUTF(hero.picname);
                        so.writeInt(hero.subsquare);
                        so.writeInt(hero.number);
                        so.writeUTF(hero.name);
                        so.writeUTF(hero.lastname);
                        so.writeInt(hero.maxhealth);
                        so.writeInt(hero.health);
                        so.writeInt(hero.maxstamina);
                        so.writeInt(hero.stamina);
                        so.writeInt(hero.maxmana);
                        so.writeInt(hero.mana);
                        //so.writeFloat(hero.maxload);
                        so.writeFloat(hero.load);
                        so.writeInt(hero.food);
                        so.writeInt(hero.water);
                        so.writeInt(hero.strength);
                        so.writeInt(hero.vitality);
                        so.writeInt(hero.dexterity);
                        so.writeInt(hero.intelligence);
                        so.writeInt(hero.wisdom);
                        so.writeInt(hero.defense);
                        so.writeInt(hero.magicresist);
                        so.writeInt(hero.strengthboost);
                        so.writeInt(hero.vitalityboost);
                        so.writeInt(hero.dexterityboost);
                        so.writeInt(hero.intelligenceboost);
                        so.writeInt(hero.wisdomboost);
                        so.writeInt(hero.defenseboost);
                        so.writeInt(hero.magicresistboost);
                        so.writeInt(hero.flevel);
                        so.writeInt(hero.nlevel);
                        so.writeInt(hero.plevel);
                        so.writeInt(hero.wlevel);
                        so.writeInt(hero.flevelboost);
                        so.writeInt(hero.nlevelboost);
                        so.writeInt(hero.plevelboost);
                        so.writeInt(hero.wlevelboost);
                        so.writeInt(hero.fxp);
                        so.writeInt(hero.nxp);
                        so.writeInt(hero.pxp);
                        so.writeInt(hero.wxp);
                        so.writeBoolean(hero.isdead);
                        so.writeBoolean(hero.wepready);
                        so.writeBoolean(hero.ispoisoned);
                        if (hero.ispoisoned) {
                                so.writeInt(hero.poison);
                                so.writeInt(hero.poisoncounter);
                        }
                        so.writeBoolean(hero.silenced);
                        if (hero.silenced) so.writeInt(hero.silencecount);
                        so.writeBoolean(hero.hurtweapon);
                        so.writeBoolean(hero.hurthand);
                        so.writeBoolean(hero.hurthead);
                        so.writeBoolean(hero.hurttorso);
                        so.writeBoolean(hero.hurtlegs);
                        so.writeBoolean(hero.hurtfeet);
                        so.writeInt(hero.timecounter);
                        so.writeInt(hero.walkcounter);
                        so.writeInt(hero.spellcount);
                        so.writeInt(hero.weaponcount);
                        so.writeInt(hero.kuswordcount);
                        so.writeInt(hero.rosbowcount);
                        so.writeUTF(hero.currentspell);
                        if (hero.abilities!=null) {
                                so.writeInt(hero.abilities.length);
                                for (int j=0;j<hero.abilities.length;j++) {
                                        hero.abilities[j].save(so);
                                }
                        }
                        else so.writeInt(0);
                        if (hero.weapon==dmnew.fistfoot) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.weapon);
                        }
                        if (hero.hand==null) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.hand);
                        }
                        if (hero.head==null) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.head);
                        }
                        if (hero.torso==null) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.torso);
                        }
                        if (hero.legs==null) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.legs);
                        }
                        if (hero.feet==null) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.feet);
                        }
                        if (hero.neck==null) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.neck);
                        }
                        if (hero.pouch1==null) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.pouch1);
                        }
                        if (hero.pouch2==null) so.writeBoolean(false);
                        else {
                                so.writeBoolean(true);
                                so.writeObject(hero.pouch2);
                        }
                        so.writeObject(hero.quiver);
                        so.writeObject(hero.pack);
                        */
            hero.save(so);
            so.writeBoolean(allowswap);
            if (target != null) {
                so.writeBoolean(true);
                so.writeObject(target);
            } else so.writeBoolean(false);
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        wasUsed = si.readBoolean();
        if (!wasUsed) {
                        /*
                        hero.subsquare = si.readInt();
                        hero.number = si.readInt();
                        hero.name = si.readUTF();
                        hero.lastname = si.readUTF();
                        hero.maxhealth = si.readInt();
                        hero.health = si.readInt();
                        hero.maxstamina = si.readInt();
                        hero.stamina = si.readInt();
                        hero.maxmana = si.readInt();
                        hero.mana = si.readInt();
                        //hero.maxload = si.readFloat();
                        hero.load = si.readFloat();
                        hero.food = si.readInt();
                        hero.water = si.readInt();
                        hero.strength = si.readInt();
                        hero.vitality = si.readInt();
                        hero.dexterity = si.readInt();
                        hero.intelligence = si.readInt();
                        hero.wisdom = si.readInt();
                        hero.defense = si.readInt();
                        hero.magicresist = si.readInt();
                        hero.strengthboost = si.readInt();
                        hero.vitalityboost = si.readInt();
                        hero.dexterityboost = si.readInt();
                        hero.intelligenceboost = si.readInt();
                        hero.wisdomboost = si.readInt();
                        hero.defenseboost = si.readInt();
                        hero.magicresistboost = si.readInt();
                        hero.flevel = si.readInt();
                        hero.nlevel = si.readInt();
                        hero.plevel = si.readInt();
                        hero.wlevel = si.readInt();
                        hero.flevelboost = si.readInt();
                        hero.nlevelboost = si.readInt();
                        hero.plevelboost = si.readInt();
                        hero.wlevelboost = si.readInt();
                        hero.fxp = si.readInt();
                        hero.nxp = si.readInt();
                        hero.pxp = si.readInt();
                        hero.wxp = si.readInt();
                        hero.isdead = si.readBoolean();
                        //hero.splready = si.readBoolean();
                        hero.wepready = si.readBoolean();
                        hero.ispoisoned = si.readBoolean();
                        if (hero.ispoisoned) {
                                hero.poison = si.readInt();
                                hero.poisoncounter = si.readInt();
                        }
                        hero.silenced = si.readBoolean();
                        if (hero.silenced) hero.silencecount = si.readInt();
                        hero.hurtweapon = si.readBoolean();
                        hero.hurthand = si.readBoolean();
                        hero.hurthead = si.readBoolean();
                        hero.hurttorso = si.readBoolean();
                        hero.hurtlegs = si.readBoolean();
                        hero.hurtfeet = si.readBoolean();
                        hero.timecounter = si.readInt();
                        hero.walkcounter = si.readInt();
                        hero.spellcount = si.readInt();
                        hero.weaponcount = si.readInt();
                        hero.kuswordcount = si.readInt();
                        hero.rosbowcount = si.readInt();
                        hero.currentspell = si.readUTF();
                        int numabils = si.readInt();
                        if (numabils>0) {
                                hero.abilities = new SpecialAbility[numabils];
                                for (int j=0;j<numabils;j++) {
                                        hero.abilities[j] = new SpecialAbility(si);
                                }
                        }
                        if (si.readBoolean()) {
                                hero.weapon = (Item)si.readObject();
                                if (hero.weapon.number==9) ((Torch)hero.weapon).setPic();
                        }
                        else hero.weapon=dmnew.fistfoot;
                        if (si.readBoolean()) {
                                hero.hand = (Item)si.readObject();
                                if (hero.hand.number==9) ((Torch)hero.hand).setPic();
                        }
                        if (si.readBoolean()) hero.head = (Item)si.readObject();
                        if (si.readBoolean()) hero.torso = (Item)si.readObject();
                        if (si.readBoolean()) hero.legs = (Item)si.readObject();
                        if (si.readBoolean()) hero.feet = (Item)si.readObject();
                        if (si.readBoolean()) {
                                hero.neck = (Item)si.readObject();
                        }
                        if (si.readBoolean()) hero.pouch1 = (Item)si.readObject();
                        if (si.readBoolean()) hero.pouch2 = (Item)si.readObject();
                        hero.quiver = (Item[])si.readObject();
                        hero.pack = (Item[])si.readObject();
                        hero.setMaxLoad();
                        */
            hero = ((dmnew) dmnew.frame).new Hero(si.readUTF());
            hero.load(si);
            hero.doCompass();
            allowswap = si.readBoolean();
            if (si.readBoolean()) target = (MapPoint) si.readObject();
        }
        setPics();
    }
    
}
