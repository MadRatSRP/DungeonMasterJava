import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class MonsterData {
    public String name;
    public String[] knownspells;
    public int number, level, x, y, subsquare, health, maxhealth, mana, maxmana, facing, defaultai, currentai, hurtitem, needitem, needhandneck;
    public int power, defense, magicresist, speed, movespeed, attackspeed, poison, fearresist, castpower, manapower, numspells, minproj, ammo, pickup, steal;
    public int timecounter, movecounter, randomcounter, runcounter, poisonpow, poisoncounter, silencecount;
    public int powerboost, defenseboost, magicresistboost, speedboost, manapowerboost, movespeedboost, attackspeedboost;
    public boolean isImmaterial, hasmagic = false, hasheal = false, hasdrain = false, wasfrightened, wasstuck, hurt, ispoisoned, silenced, useammo = false, isflying, canusestairs, ignoremons, canteleport, poisonimmune;
    public boolean HITANDRUN;
    public boolean gamewin, isdying = false;
    //public String endanim,endmusic,endsound,picstring,soundstring;
    public String endanim, endsound, picstring, soundstring, footstep;
    public ArrayList carrying, equipped;
    public ImageIcon pic;
    public static boolean NOITEMS = false;
    //static Toolkit tk = Toolkit.getDefaultToolkit();
    
    static final ImageIcon[] MonsterIcon = {
        new ImageIcon("Icons" + File.separator + "mummy.gif"),
        new ImageIcon("Icons" + File.separator + "screamer.gif"),
        new ImageIcon("Icons" + File.separator + "giggler.gif"),
        new ImageIcon("Icons" + File.separator + "rockpile.gif"),
        new ImageIcon("Icons" + File.separator + "slime.gif"),
        new ImageIcon("Icons" + File.separator + "wingeye.gif"),
        new ImageIcon("Icons" + File.separator + "ghost.gif"),
        new ImageIcon("Icons" + File.separator + "muncher.gif"),
        new ImageIcon("Icons" + File.separator + "skeleton.gif"),
        new ImageIcon("Icons" + File.separator + "worm.gif"),
        new ImageIcon("Icons" + File.separator + "fireel.gif"),
        new ImageIcon("Icons" + File.separator + "waterel.gif"),
        new ImageIcon("Icons" + File.separator + "goblin.gif"),
        new ImageIcon("Icons" + File.separator + "rat.gif"),
        new ImageIcon("Icons" + File.separator + "antman.gif"),
        new ImageIcon("Icons" + File.separator + "beholder.gif"),
        new ImageIcon("Icons" + File.separator + "couatyl.gif"),
        new ImageIcon("Icons" + File.separator + "fader.gif"),
        new ImageIcon("Icons" + File.separator + "tentacle.gif"),
        new ImageIcon("Icons" + File.separator + "scorpion.gif"),
        new ImageIcon("Icons" + File.separator + "demon.gif"),
        new ImageIcon("Icons" + File.separator + "knight.gif"),
        new ImageIcon("Icons" + File.separator + "spider.gif"),
        new ImageIcon("Icons" + File.separator + "golem.gif"),
        new ImageIcon("Icons" + File.separator + "sorcerer.gif"),
        new ImageIcon("Icons" + File.separator + "dragon.gif"),
        new ImageIcon("Icons" + File.separator + "chaos.gif"),
        new ImageIcon("Icons" + File.separator + "demonlord.gif"),
        new ImageIcon("Icons" + File.separator + "question.gif"),
    };
    
    //static final int RANDOM = 0, GOTOWARDS = 1, STAYBACK = 2, RUN = 3, GUARD = 4; //AI states
    //static final String[] monsternames = { "Mummy","Screamer","Giggler","Rock Pile","Slime","Wing Eye",
    //"Ghost","Muncher","Skeleton","Worm","Fire Elemental","Water Elemental","Goblin","Giant Rat","Ant Man","Beholder",
    //"Couatyl","Fader","Tentacle Beast","Scorpion","Demon","Deth Knight","Spider","Stone Golem","Sorcerer","Dragon","Lord Chaos","Demon Lord" };
    public MonsterData() {
    }
    
    public MonsterData(ObjectInputStream si) throws IOException, ClassNotFoundException {
        //isdying = si.readBoolean();
        number = si.readInt();
        x = si.readInt();
        y = si.readInt();
        level = si.readInt();
        if (number > 27) {
            //System.out.println("number = "+number+", x = "+x+", y = "+y+", level = "+level);
            name = si.readUTF();
            picstring = si.readUTF();
            soundstring = si.readUTF();
            footstep = si.readUTF();
            //footstep = "step1.wav";
            canusestairs = si.readBoolean();
            isflying = si.readBoolean();
            ignoremons = si.readBoolean();
            canteleport = si.readBoolean();
            File picfile = new File("Icons" + File.separator + picstring + ".gif");
            if (picfile.exists()) pic = new ImageIcon(picfile.getPath());
            else pic = MonsterIcon[28];
        } else {
            pic = MonsterIcon[number];
            name = MonsterWizard.monsternames[number];
        }
        subsquare = si.readInt();
        health = si.readInt();
        maxhealth = si.readInt();
        mana = si.readInt();
        maxmana = si.readInt();
        facing = si.readInt();
        currentai = si.readInt();
        defaultai = si.readInt();
        HITANDRUN = si.readBoolean();
        isImmaterial = si.readBoolean();
        
        wasfrightened = si.readBoolean();
        hurt = si.readBoolean();
        wasstuck = si.readBoolean();
        ispoisoned = si.readBoolean();
        if (ispoisoned) {
            poisonpow = si.readInt();
            poisoncounter = si.readInt();
        }
        timecounter = si.readInt();
        movecounter = si.readInt();
        randomcounter = si.readInt();
        runcounter = si.readInt();
        
        carrying = (ArrayList) si.readObject();
        if (si.readBoolean()) equipped = (ArrayList) si.readObject();
        gamewin = si.readBoolean();
        if (gamewin) {
            endanim = si.readUTF();
            endsound = si.readUTF();
        }
        hurtitem = si.readInt();
        needitem = si.readInt();
        needhandneck = si.readInt();
        
        power = si.readInt();
        defense = si.readInt();
        magicresist = si.readInt();
        speed = si.readInt();
        movespeed = si.readInt();
        attackspeed = si.readInt();
        poison = si.readInt();
        fearresist = si.readInt();
        hasmagic = si.readBoolean();
        if (hasmagic) {
            castpower = si.readInt();
            manapower = si.readInt();
            numspells = si.readInt();
            if (numspells > 0) knownspells = (String[]) si.readObject();
            minproj = si.readInt();
            hasheal = si.readBoolean();
            hasdrain = si.readBoolean();
        }
        //si.readInt();
        useammo = si.readBoolean();
        if (useammo) ammo = si.readInt();
        pickup = si.readInt();
        steal = si.readInt();
        poisonimmune = si.readBoolean();
        //poisonimmune = false;
        
        powerboost = si.readInt();
        defenseboost = si.readInt();
        magicresistboost = si.readInt();
        speedboost = si.readInt();
        manapowerboost = si.readInt();
        movespeedboost = si.readInt();
        attackspeedboost = si.readInt();
        silenced = si.readBoolean();
        if (silenced) silencecount = si.readInt();
                
                /*make deth knight equipment cursed
                if (number==21 && equipped!=null) {
                        Item it;
                        for (int i=0;i<equipped.size();i++) {
                                it = (Item)equipped.get(i);
                                it.cursed = 100;
                                //it.haseffect = true;
                                //it.effect = new String[2];
                                //it.effect[0] = "strength,-5";
                                //it.effect[1] = "vitality,-5";
                        }
                }
                */
                /*
                //--------------//--------------
                //update dungeon
                //--------------//--------------
                //move certain items to equipped list
                if (number==1 || number==3 || number==8 || number==9 || number==13 || number==14 || number==21 || number==23 || number==25) {
                        Item it;
                        for (int i=0;i<carrying.size();) {
                                it = (Item)carrying.get(i);
                                if ( (number==1 && it.number==65) || (number==3 && (it.number==266 || it.number==76)) || (number==8 && (it.number==200 || it.number==104)) || (number==9 && it.number==61) || (number==13 && it.number==67) || (number==14 && it.number==226) || (number==21 && (it.number==201 || it.number==143 || it.number==163 || it.number==181)) || (number==23 && (it.number==227 || it.number==266 || it.number==76)) || (number==25 && it.number==69) ) {
                                        carrying.remove(i);
                                        if (equipped==null) equipped = new ArrayList();
                                        equipped.add(it);
                                }
                                else i++;
                        }
                }
                //set poison immunity
                if (number==4 || number==8 || number==18 || number==21 || number==23 || number==26) poisonimmune = true;
                //fix speeds for deth knight and golem
                if (number==21) movespeed--;
                else if (number==23) {
                        movespeed-=2;
                        attackspeed--;
                }
                //--------------//--------------
                //end update
                //--------------//--------------
                */
    }
        /*
        //for loading
        public MonsterData(int number, int x, int y, int level) {
                this.number = number;
                this.x = x;
                this.y = y;
                this.level = level;
                pic = MonsterIcon[number];
                name = MonsterWizard.monsternames[number];
        }
        public MonsterData(int number,int x,int y,int level,String name,String picstring,boolean canusestairs,boolean isflying,boolean ignoremons) {
                this.number = number;
                this.x = x;
                this.y = y;
                this.level = level;
                this.name = name;
                this.picstring = picstring;
                this.canusestairs = canusestairs;
                this.isflying = isflying;
                this.ignoremons = ignoremons;
                File picfile = new File("Icons"+File.separator+picstring+".gif");
                if (picfile.exists()) pic = new ImageIcon(picfile.getPath());
                else pic = MonsterIcon[28];
        }
        */
    
    public MonsterData(int number, int level, int x, int y, int health, int mana, int facing, int ai, boolean HITANDRUN, boolean isImmaterial, int power, int defense, int magicresist, int speed, int poison, int fearresist, ArrayList carrying, ArrayList equipped, boolean gamewin, String endanim, String endsound, int hurtitem, int needitem, int needhandneck, boolean sub5, int pickup, int steal, boolean poisonimmune) {
        this(number, level, x, y, health, mana, facing, ai, HITANDRUN, isImmaterial, power, defense, magicresist, speed, poison, fearresist, carrying, equipped, gamewin, endanim, endsound, hurtitem, needitem, needhandneck, sub5, pickup, steal, MonsterWizard.monsternames[number], "", null, null, false, false, false, false, poisonimmune);
        //this(number,level,x,y,health,mana,facing,ai,HITANDRUN,isImmaterial,power,defense,magicresist,speed,poison,fearresist,carrying,gamewin,endanim,endsound,hurtitem,needitem,needhandneck,sub5,pickup,MonsterWizard.monsternames[number],"",null,false,false,false);
    }
    
    public MonsterData(int number, int level, int x, int y, int health, int mana, int facing, int ai, boolean HITANDRUN, boolean isImmaterial, int power, int defense, int magicresist, int speed, int poison, int fearresist, ArrayList carrying, ArrayList equipped, boolean gamewin, String endanim, String endsound, int hurtitem, int needitem, int needhandneck, boolean sub5, int pickup, int steal, String name, String picstring, String soundstring, String footstep, boolean canusestairs, boolean isflying, boolean ignoremons, boolean canteleport, boolean poisonimmune) {
        //public MonsterData(int number,int level,int x,int y,int health,int mana,int facing,int ai,boolean HITANDRUN,boolean isImmaterial,int power,int defense,int magicresist,int speed,int poison,int fearresist, ArrayList carrying, boolean gamewin, String endanim, String endsound, int hurtitem, int needitem, int needhandneck, boolean sub5, int pickup, String name, String picstring, String soundstring, boolean canusestairs, boolean isflying, boolean ignoremons) {
        this.number = number;
        this.level = level;
        this.x = x;
        this.y = y;
        this.health = health;
        this.mana = mana;
        this.facing = facing;
        this.defaultai = ai;
        this.HITANDRUN = HITANDRUN;
        this.isImmaterial = isImmaterial;
        this.power = power;
        this.defense = defense;
        this.magicresist = magicresist;
        this.speed = speed;
        this.poison = poison;
        this.fearresist = fearresist;
        this.carrying = carrying;
        if (equipped != null && equipped.size() != 0) this.equipped = equipped;
        this.gamewin = gamewin;
        this.endanim = endanim;
        this.endsound = endsound;
        this.hurtitem = hurtitem;
        this.needitem = needitem;
        this.needhandneck = needhandneck;
        this.name = name;
        if (number > 27) {
            this.picstring = picstring;
            this.soundstring = soundstring;
            this.footstep = footstep;
            this.canusestairs = canusestairs;
            this.isflying = isflying;
            this.ignoremons = ignoremons;
            this.canteleport = canteleport;
            File picfile = new File("Icons" + File.separator + picstring + ".gif");
            if (picfile.exists()) pic = new ImageIcon(picfile.getPath());
            else pic = MonsterIcon[28];
        } else pic = MonsterIcon[number];
        maxhealth = health;
        maxmana = mana;
        if (ai >= 4) currentai = ai;
        else currentai = 0;
        //worm,fire-el,rat,couatyl,scorpion,spider,dragon,chaos,highdemon all take up whole square (currently no way to do 2 take up a square)
        //if (number==9 || number==10 || number==13 || number==16 || number==19 || number==22 || number==25 || number==26 || number==27) subsquare=5;
        if (sub5) subsquare = 5;
        this.pickup = pickup;
        this.steal = steal;
        this.poisonimmune = poisonimmune;
    }
    
    //copy constructor
    public MonsterData(MonsterData m) {
        number = m.number;
        level = m.level;
        x = m.x;
        y = m.y;
        health = m.health;
        mana = m.mana;
        facing = m.facing;
        defaultai = m.defaultai;
        currentai = m.currentai;
        HITANDRUN = m.HITANDRUN;
        isImmaterial = m.isImmaterial;
        power = m.power;
        defense = m.defense;
        magicresist = m.magicresist;
        speed = m.speed;
        poison = m.poison;
        fearresist = m.fearresist;
        //carrying = m.carrying;
        gamewin = m.gamewin;
        if (gamewin) {
            endanim = new String(m.endanim);
            endsound = new String(m.endsound);
        }
        hurtitem = m.hurtitem;
        needitem = m.needitem;
        needhandneck = m.needhandneck;
        name = new String(m.name);
        if (number > 27) {
            picstring = new String(m.picstring);
            soundstring = new String(m.soundstring);
            footstep = new String(m.footstep);
            canusestairs = m.canusestairs;
            isflying = m.isflying;
            ignoremons = m.ignoremons;
            canteleport = m.canteleport;
        }
        pic = m.pic;
        maxhealth = m.maxhealth;
        maxmana = m.maxmana;
        subsquare = m.subsquare;
        pickup = m.pickup;
        
        wasfrightened = m.wasfrightened;
        hurt = m.hurt;
        wasstuck = m.wasstuck;
        ispoisoned = m.ispoisoned;
        poisonpow = m.poisonpow;
        poisoncounter = m.poisoncounter;
        timecounter = m.timecounter;
        movecounter = m.movecounter;
        randomcounter = m.randomcounter;
        runcounter = m.runcounter;
        
        movespeed = m.movespeed;
        attackspeed = m.attackspeed;
        carrying = new ArrayList();
        if (m.carrying != null && m.carrying.size() > 0) {
            for (int i = 0; i < m.carrying.size(); i++) {
                carrying.add(Item.createCopy((Item) m.carrying.get(i)));
            }
        }
        if (m.equipped != null && m.equipped.size() > 0) {
            equipped = new ArrayList();
            for (int i = 0; i < m.equipped.size(); i++) {
                equipped.add(Item.createCopy((Item) m.equipped.get(i)));
            }
        }
        hasmagic = m.hasmagic;
        if (hasmagic) {
            castpower = m.castpower;
            manapower = m.manapower;
            numspells = m.numspells;
            if (numspells > 0) {
                knownspells = new String[m.knownspells.length];
                for (int i = 0; i < knownspells.length; i++) {
                    knownspells[i] = new String(m.knownspells[i]);
                }
            }
            minproj = m.minproj;
            hasheal = m.hasheal;
            hasdrain = m.hasdrain;
        }
        useammo = m.useammo;
        ammo = m.ammo;
        steal = m.steal;
        poisonimmune = m.poisonimmune;
        
        powerboost = m.powerboost;
        defenseboost = m.defenseboost;
        magicresistboost = m.magicresistboost;
        speedboost = m.speedboost;
        manapowerboost = m.manapowerboost;
        movespeedboost = m.movespeedboost;
        attackspeedboost = m.attackspeedboost;
        silenced = m.silenced;
        silencecount = m.silencecount;
    }
    
    public String toString() {
        return name;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof MonsterData && name.equals(((MonsterData) obj).name)) return true;
        else return false;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        //update map
        //if (number==21) { maxhealth = MonsterWizard.defaulthealth[21]; health = maxhealth; }
        //if (fearresist!=MonsterWizard.defaultfresist[number]) fearresist = MonsterWizard.defaultfresist[number];
        //if (number==10) { speed=MonsterWizard.defaultspeed[10]; movespeed = MonsterWizard.defaultmspeed[10]; attackspeed = MonsterWizard.defaultaspeed[10]; }
        //end update
        //so.writeBoolean(isdying);
        so.writeInt(number);
        so.writeInt(x);
        so.writeInt(y);
        so.writeInt(level);
        if (number > 27) {
            so.writeUTF(name);
            so.writeUTF(picstring);
            so.writeUTF(soundstring);
            so.writeUTF(footstep);
            so.writeBoolean(canusestairs);
            so.writeBoolean(isflying);
            so.writeBoolean(ignoremons);
            so.writeBoolean(canteleport);
        }
        so.writeInt(subsquare);
        so.writeInt(health);
        so.writeInt(maxhealth);
        so.writeInt(mana);
        so.writeInt(maxmana);
        so.writeInt(facing);
        so.writeInt(currentai);
        so.writeInt(defaultai);
        so.writeBoolean(HITANDRUN);
        so.writeBoolean(isImmaterial);
        so.writeBoolean(wasfrightened);
        so.writeBoolean(hurt);
        so.writeBoolean(wasstuck);
        so.writeBoolean(ispoisoned);
        if (ispoisoned) {
            so.writeInt(poisonpow);
            so.writeInt(poisoncounter);
        }
        so.writeInt(timecounter);
        so.writeInt(movecounter);
        so.writeInt(randomcounter);
        so.writeInt(runcounter);
        
        if (!NOITEMS) so.writeObject(carrying); //noitems prevents saving of carrying -> for custom_mons.dat file
        else so.writeObject(new ArrayList());
        if (equipped != null) {
            so.writeBoolean(true);
            so.writeObject(equipped);
        } else so.writeBoolean(false);
        so.writeBoolean(gamewin);
        //if (gamewin) { so.writeUTF(endanim); so.writeUTF(endmusic); so.writeUTF(endsound);}
        if (gamewin) {
            so.writeUTF(endanim);
            so.writeUTF(endsound);
        }
        so.writeInt(hurtitem);
        so.writeInt(needitem);
        so.writeInt(needhandneck);
        
        so.writeInt(power);
        so.writeInt(defense);
        so.writeInt(magicresist);
        so.writeInt(speed);
        so.writeInt(movespeed);
        so.writeInt(attackspeed);
        so.writeInt(poison);
        so.writeInt(fearresist);
        so.writeBoolean(hasmagic);
        if (hasmagic) {
            so.writeInt(castpower);
            so.writeInt(manapower);
            so.writeInt(numspells);
            if (numspells > 0) so.writeObject(knownspells);
            so.writeInt(minproj);
            so.writeBoolean(hasheal);
            so.writeBoolean(hasdrain);
        }
        //so.writeInt(ammonumber);
        so.writeBoolean(useammo);
        if (useammo) so.writeInt(ammo);
        so.writeInt(pickup);
        so.writeInt(steal);
        so.writeBoolean(poisonimmune);
        
        so.writeInt(powerboost);
        so.writeInt(defenseboost);
        so.writeInt(magicresistboost);
        so.writeInt(speedboost);
        so.writeInt(manapowerboost);
        so.writeInt(movespeedboost);
        so.writeInt(attackspeedboost);
        so.writeBoolean(silenced);
        if (silenced) so.writeInt(silencecount);
    }
        /*public void load(ObjectInputStream si) throws IOException,ClassNotFoundException {
                //x = si.readInt();
                //y = si.readInt();
                //level = si.readInt();
                subsquare = si.readInt();
                health = si.readInt();
                maxhealth = si.readInt();
                mana = si.readInt();
                maxmana = si.readInt();
                facing = si.readInt();
                currentai = si.readInt();
                defaultai = si.readInt();
                HITANDRUN = si.readBoolean();
                isImmaterial = si.readBoolean();
                
                wasfrightened = si.readBoolean();
                hurt = si.readBoolean();
                wasstuck = si.readBoolean();
                ispoisoned = si.readBoolean();
                if (ispoisoned) {
                        poisonpow = si.readInt();
                        poisoncounter = si.readInt();
                }
                timecounter = si.readInt();
                movecounter = si.readInt();
                randomcounter = si.readInt();
                runcounter = si.readInt();
                
                carrying = (ArrayList)si.readObject();
                gamewin = si.readBoolean();
                if (gamewin) { endanim = si.readUTF(); endmusic = si.readUTF(); }
                hurtitem = si.readInt();
                needitem = si.readInt();
                needhandneck = si.readInt();
                
                power = si.readInt();
                defense = si.readInt();
                magicresist = si.readInt();
                speed = si.readInt();
                movespeed = si.readInt();
                attackspeed = si.readInt();
                poison = si.readInt();
                fearresist = si.readInt();
                hasmagic = si.readBoolean();
                if (hasmagic) {
                        castpower = si.readInt();
                        manapower = si.readInt();
                        numspells = si.readInt();
                        if (numspells>0) knownspells = (String[])si.readObject();
                        minproj = si.readInt();
                        hasheal = si.readBoolean();
                        hasdrain = si.readBoolean();
                }
                //si.readInt();
                useammo = si.readBoolean();
                if (useammo) ammo = si.readInt();
                
                powerboost = si.readInt();
                defenseboost = si.readInt();
                magicresistboost = si.readInt();
                speedboost = si.readInt();
                manapowerboost = si.readInt();
                movespeedboost = si.readInt();
                attackspeedboost = si.readInt();
                silenced = si.readBoolean();
        }*/
    
}