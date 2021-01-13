import javax.swing.*;
import java.awt.*;
import java.io.*;

class Spell implements Serializable {
    int number;
    int type;//0 potion, 1 proj, 2 special
    int power;
    int dist;
    int gain;
    int potionnum = -1;
    int powers[] = new int[6];
    char clsgain = 'w';
    boolean ismultiple = false;
    boolean hitsImmaterial = false;
    Image pic;
    Image endpic;
    Image endpic0;
    String picstring;       //for serialization
    String endpicstring;    //for serialization
    //String endsound = "zap.wav"; //sound when hits something
    static final Toolkit tk = Toolkit.getDefaultToolkit();
    static final MediaTracker ImageTracker = new MediaTracker(new JPanel());
    static Image spellpic, spellendpic, spellendpic0, firepic, fireendpic, fireendpic0, boltpic, boltendpic, boltendpic0, poisonpic, poisonendpic, poisonendpic0,
        arcboltpic, arcboltendpic, arcboltendpic0, weakpic, weakendpic, weakendpic0, silencepic, silenceendpic, silenceendpic0, magicarrowpic, magicarrowendpic, magicarrowendpic0,
        fuseendpic, fuseendpic0, fulbombpic, venbombpic, boltbombpic, blankpic;
    static final long serialVersionUID = -8076252191649996993L;
    
    //?instead exception, return a boolean?
    
    public Spell(String n) throws Exception {
        power = Integer.parseInt(n.substring(0, 1)) - 1;
        number = Integer.parseInt(n.substring(1, n.length()));
        gain = power + 1;
        dist = gain * 5 + 4;//was gain*5+10
        
        switch (number) {
            //stamina potion
            case 1: //ya
                //health potion
            case 2: //vi
                type = 0;
                clsgain = 'p';
                potionnum = 12 - number;
                powers[0] = 6;
                powers[1] = 11;
                powers[2] = 16;
                powers[3] = 20;
                powers[4] = 26;
                powers[5] = 36;
                break;
            //magic-resist potion
            case 653: //zo bro dain
                type = 0;
                clsgain = 'p';
                potionnum = 19;
                powers[0] = 4;
                powers[1] = 6;
                powers[2] = 8;
                powers[3] = 12;
                powers[4] = 16;
                powers[5] = 20;
                break;
            //defense potion
            case 15: //ya bro
                type = 0;
                clsgain = 'p';
                potionnum = 18;
                powers[0] = 4;
                powers[1] = 6;
                powers[2] = 8;
                powers[3] = 12;
                powers[4] = 16;
                powers[5] = 20;
                break;
            //wisdom potion
            case 154: //ya bro neta
                type = 0;
                clsgain = 'p';
                potionnum = 17;
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //dexterity potion
            case 352: //oh bro ros
                type = 0;
                clsgain = 'p';
                potionnum = 14;
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //intelligence potion
            case 353: //oh bro dain
                type = 0;
                clsgain = 'p';
                potionnum = 16;
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //vitality potion
            case 354: //oh bro neta
                type = 0;
                clsgain = 'p';
                potionnum = 15;
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //strength potion
            case 451: //ful bro ku
                type = 0;
                clsgain = 'p';
                potionnum = 13;
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //weakness
            case 461: //ful gor ku
                type = 1;
                picstring = "weak";
                endpicstring = "weak";
                pic = weakpic;
                endpic = weakendpic;
                endpic0 = weakendpic0;
                powers[0] = 6;
                powers[1] = 12;
                powers[2] = 18;
                powers[3] = 24;
                powers[4] = 32;
                powers[5] = 40;
                break;
            //feeble mind
            case 363: //oh gor dain
                type = 1;
                picstring = "weak";
                endpicstring = "weak";
                pic = weakpic;
                endpic = weakendpic;
                endpic0 = weakendpic0;
                powers[0] = 6;
                powers[1] = 12;
                powers[2] = 18;
                powers[3] = 24;
                powers[4] = 32;
                powers[5] = 40;
                break;
            //slow
            case 362: //oh gor ros
                type = 1;
                picstring = "weak";
                endpicstring = "weak";
                pic = weakpic;
                endpic = weakendpic;
                endpic0 = weakendpic0;
                ismultiple = true;
                powers[0] = 6;
                powers[1] = 12;
                powers[2] = 18;
                powers[3] = 24;
                powers[4] = 32;
                powers[5] = 40;
                break;
            //strip defenses
            case 664: //zo gor neta
                type = 1;
                picstring = "weak";
                endpicstring = "weak";
                pic = weakpic;
                endpic = weakendpic;
                endpic0 = weakendpic0;
                //ismultiple = true;
                powers[0] = 7;
                powers[1] = 12;
                powers[2] = 18;
                powers[3] = 25;
                powers[4] = 32;
                powers[5] = 40;
                break;
            //mana potion
            case 655: //zo bro ra
                type = 0;
                clsgain = 'p';
                potionnum = 12;
                powers[0] = 6;
                powers[1] = 8;
                powers[2] = 12;
                powers[3] = 16;
                powers[4] = 20;
                powers[5] = 26;
                break;
            //antidote
            case 25: //vi bro
                type = 0;
                clsgain = 'p';
                potionnum = 20;
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //nothing but wind bind
            case 3: //oh
                type = 2;
                return;
            //magic torch
            case 4: //ful
                type = 2;
                powers[0] = 24;
                powers[1] = 36;
                powers[2] = 48;
                powers[3] = 60;
                powers[4] = 72;
                powers[5] = 84;
                break;
            //open/close door
            case 6: //zo
                type = 1;
                picstring = "spell";
                endpicstring = "spell";
                pic = spellpic;
                endpic = spellendpic;
                endpic0 = spellendpic0;
                powers[0] = 0;
                powers[1] = 0;
                powers[2] = 0;
                powers[3] = 0;
                powers[4] = 0;
                powers[5] = 0;
                break;
            //fireball
            case 44: //ful ir
                type = 1;
                picstring = "fire";
                endpicstring = "fire";
                pic = firepic;
                endpic = fireendpic;
                endpic0 = fireendpic0;
                //endsound = "fball.wav";
                powers[0] = 35;
                powers[1] = 50;
                powers[2] = 100;
                powers[3] = 150;
                powers[4] = 200;
                powers[5] = 250;
                ismultiple = true;
                break;
            //ful bomb
            case 46: //ful gor
                type = 0;
                potionnum = 21;
                picstring = "ful_bomb";
                endpicstring = "fire";
                pic = fulbombpic;
                endpic = fireendpic;
                endpic0 = fireendpic0;
                //endsound = "fball.wav";
                                /*
                                powers[0]=40;
                                powers[1]=70;
                                powers[2]=150;
                                powers[3]=200;
                                powers[4]=250;
                                powers[5]=300;
                                */
                powers[0] = 40;
                powers[1] = 60;
                powers[2] = 120;
                powers[3] = 175;
                powers[4] = 225;
                powers[5] = 275;
                ismultiple = true;
                break;
            //dispell immaterial
            case 52: //des ew
                type = 1;
                hitsImmaterial = true;
                picstring = "spell";
                endpicstring = "spell";
                pic = spellpic;
                endpic = spellendpic;
                endpic0 = spellendpic0;
                powers[0] = 20;
                powers[1] = 40;
                powers[2] = 60;
                powers[3] = 80;
                powers[4] = 100;
                powers[5] = 120;
                break;
            //magic vision
            case 325: //oh ew ra
                type = 2;
                clsgain = 'p';
                powers[0] = 5;
                powers[1] = 11;
                powers[2] = 18;
                powers[3] = 25;
                powers[4] = 35;
                powers[5] = 50;
                break;
            //true sight (dispell illusion)
            case 322: //oh ew ros
                type = 2;
                clsgain = 'p';
                powers[0] = 5;
                powers[1] = 11;
                powers[2] = 18;
                powers[3] = 25;
                powers[4] = 35;
                powers[5] = 50;
                break;
            //shield party
            case 14: //ya ir
                type = 2;
                clsgain = 'p';
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //shield party from magic
            case 643: //zo ir dain
                type = 2;
                clsgain = 'p';
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //poison cloud
            case 31: //oh ven
                type = 1;
                picstring = "poison";
                endpicstring = "blank";
                pic = poisonpic;
                endpic = blankpic;
                endpic0 = blankpic;
                //these #'s will likely change (need to know how long lasts, and how much it hurts)
                powers[0] = 1;
                powers[1] = 3;
                powers[2] = 5;
                powers[3] = 7;
                powers[4] = 9;
                powers[5] = 12;
                break;
            //poison missle
            case 51: //des ven
                //causes poison if hit by
                type = 1;
                picstring = "poison";
                endpicstring = "poison";
                pic = poisonpic;
                endpic = poisonendpic;
                endpic0 = poisonendpic0;
                powers[0] = 12;
                powers[1] = 24;
                powers[2] = 36;
                powers[3] = 48;
                powers[4] = 60;
                powers[5] = 72;
                break;
            //ven bomb
            case 61: //zo ven
                type = 0;
                potionnum = 22;
                picstring = "ven_bomb";
                endpicstring = "blank";
                pic = venbombpic;
                endpic = blankpic;
                endpic0 = blankpic;
                powers[0] = 1;
                powers[1] = 3;
                powers[2] = 5;
                powers[3] = 7;
                powers[4] = 9;
                powers[5] = 12;
                break;
            //lightning
            case 335: //oh kath ra
                type = 1;
                picstring = "bolt";
                endpicstring = "bolt";
                pic = boltpic;
                endpic = boltendpic;
                endpic0 = boltendpic0;
                //endsound = "fball.wav";
                                /*
                                powers[0]=40;
                                powers[1]=70;
                                powers[2]=150;
                                powers[3]=200;
                                powers[4]=250;
                                powers[5]=300;
                                */
                powers[0] = 40;
                powers[1] = 60;
                powers[2] = 120;
                powers[3] = 175;
                powers[4] = 225;
                powers[5] = 275;
                break;
            //float (slowfall)
            case 344: //oh ir neta
                type = 2;
                clsgain = 'p';
                //powers are duration -> message.setMessage("Float wears off",4);
                powers[0] = 10;
                powers[1] = 20;
                powers[2] = 30;
                powers[3] = 40;
                powers[4] = 50;
                powers[5] = 60;
                break;
            //magic sword -> need magic sword item and test like stormbringer so can't remove
            case 121: //ya ew ku
                type = 2;
                //gain determines power of sword, powers are duration of sword
                powers[0] = 50;
                powers[1] = 75;
                powers[2] = 100;
                powers[3] = 125;
                powers[4] = 150;
                powers[5] = 175;
                                /*
                                powers[0]=1;
                                powers[1]=2;
                                powers[2]=3;
                                powers[3]=4;
                                powers[4]=5;
                                powers[5]=6;
                                */
                break;
            //magic bow -> need magic bow item and test like stormbringer so can't remove
            case 122: //ya ew ros
                type = 2;
                //gain determines power of bow, powers are duration of bow
                powers[0] = 50;
                powers[1] = 75;
                powers[2] = 100;
                powers[3] = 125;
                powers[4] = 150;
                powers[5] = 175;
                                /*
                                powers[0]=1;
                                powers[1]=2;
                                powers[2]=3;
                                powers[3]=4;
                                powers[4]=5;
                                powers[5]=6;
                                */
                break;
            //silence
            case 523: //des ew dain
                type = 1;
                clsgain = 'p';
                //powers are duration
                //success depends on gain of spell, level of caster, and resistance of mon (need new var for this like fearresist)
                picstring = "silence";
                endpicstring = "silence";
                pic = silencepic;
                endpic = silenceendpic;
                endpic0 = silenceendpic0;
                powers[0] = 50;
                powers[1] = 75;
                powers[2] = 100;
                powers[3] = 125;
                powers[4] = 160;
                powers[5] = 200;
                ismultiple = true;
                break;
            //anti-silence potion
            case 654: //zo bro neta
                type = 0;
                clsgain = 'p';
                potionnum = 24;
                powers[0] = 25;
                powers[1] = 50;
                powers[2] = 75;
                powers[3] = 100;
                powers[4] = 125;
                powers[5] = 160;
                break;
                        /*
                        //invisibility
                        case 326: //oh ew sar
                                type = 2;
                                clsgain = 'p';
                                //powers are duration
                                powers[0]=10;
                                powers[1]=20;
                                powers[2]=30;
                                powers[3]=40;
                                powers[4]=50;
                                powers[5]=60;
                                break;
                        */
            //remove curse potion
            case 553: //des bro dain
                type = 0;
                clsgain = 'p';
                potionnum = 25;
                powers[0] = 2;
                powers[1] = 4;
                powers[2] = 6;
                powers[3] = 8;
                powers[4] = 12;
                powers[5] = 16;
                break;
            //detect curse
            case 364: //oh gor neta
                type = 2;
                clsgain = 'p';
                //powers are duration
                powers[0] = 10;
                powers[1] = 20;
                powers[2] = 30;
                powers[3] = 40;
                powers[4] = 50;
                powers[5] = 60;
                break;
            //arc bolt
            case 642: //zo ir ros
                type = 1;
                picstring = "arcbolt";
                endpicstring = "arcbolt";
                pic = arcboltpic;
                endpic = arcboltendpic;
                endpic0 = arcboltendpic0;
                powers[0] = 20;
                powers[1] = 40;
                powers[2] = 60;
                powers[3] = 80;
                powers[4] = 100;
                powers[5] = 120;
                break;
            //bolt bomb
            case 365: //oh gor ra
                type = 0;
                potionnum = 23;
                picstring = "bolt_bomb";
                endpicstring = "bolt";
                pic = boltbombpic;
                endpic = boltendpic;
                endpic0 = boltendpic0;
                                /*
                                powers[0]=50;
                                powers[1]=100;
                                powers[2]=175;
                                powers[3]=250;
                                powers[4]=300;
                                powers[5]=350;
                                */
                powers[0] = 50;
                powers[1] = 80;
                powers[2] = 160;
                powers[3] = 200;
                powers[4] = 250;
                powers[5] = 325;
                break;
                        /*
                        //aura of ra
                        case 665: //zo gor ra  -> if attack all around you
                        case 655: //zo bro ra  -> if perfect shield/reflector? against lightning (and/or fireballs?)
                                type = 2;
                                picstring+="ra.gif";
                                pic = tk.getImage(picstring);
                                powers[0]=15;
                                powers[1]=30;
                                powers[2]=45;
                                powers[3]=60;
                                powers[4]=75;
                                powers[5]=90;
                                break;
                        */
            //drain life
            case 666: //zo gor sar
                type = 2;
                //picstring+="drain.gif";
                //pic = tk.getImage(picstring);
                //endpic0 = tk.getImage("Spells"+File.separator+"drain0.gif");
                powers[0] = 15;
                powers[1] = 30;
                powers[2] = 45;
                powers[3] = 60;
                powers[4] = 75;
                powers[5] = 90;
                break;
                        /*
                        //drain mana
                        case 566: //zo gor dain
                                type = 2;
                                //picstring+="drain.gif";
                                //pic = tk.getImage(picstring);
                                //endpic0 = tk.getImage("Spells"+File.separator+"drain0.gif");
                                powers[0]=15;
                                powers[1]=30;
                                powers[2]=45;
                                powers[3]=60;
                                powers[4]=75;
                                powers[5]=90;
                                break;
                        */
            //ZoKathRa plasma
            case 635: //zo kath ra
                type = 2;
                powers[0] = 0;
                powers[1] = 1;
                powers[2] = 2;
                powers[3] = 3;
                powers[4] = 4;
                powers[5] = 5;
                break;
            default: //nonsense
                throw (new Exception());
        }
        power = powers[power];
    }
    
    //fuse
    public Spell() {
        power = 0;
        number = 0;
        gain = 6;
        dist = 1;
        type = 1;
        hitsImmaterial = true;
        picstring = "blank";
        endpicstring = "fuse";
        pic = blankpic;
        endpic = fuseendpic;
        endpic0 = fuseendpic0;
    }
    
    //magic arrow
    public Spell(int gain) {
        this.gain = gain;
        number = 7;
        type = 1;//set 0 if don't want scaling
        picstring = "magic_arrow";
        endpicstring = "magic_arrow";
        pic = magicarrowpic;
        endpic = magicarrowendpic;
        endpic0 = magicarrowendpic0;
        powers[0] = 20;
        powers[1] = 30;
        powers[2] = 40;
        powers[3] = 50;
        powers[4] = 65;
        powers[5] = 80;
        power = powers[gain - 1];
    }
    
    public static void doPics() {
        spellpic = tk.createImage("Spells" + File.separator + "spell.gif");
        spellendpic = tk.createImage("Spells" + File.separator + "spellend.gif");
        spellendpic0 = tk.createImage("Spells" + File.separator + "spellend0.gif");
        firepic = tk.createImage("Spells" + File.separator + "fire.png");
        fireendpic = tk.createImage("Spells" + File.separator + "fireend.gif");
        fireendpic0 = tk.createImage("Spells" + File.separator + "fireend0.gif");
        boltpic = tk.createImage("Spells" + File.separator + "bolt.gif");
        boltendpic = tk.createImage("Spells" + File.separator + "boltend.gif");
        boltendpic0 = tk.createImage("Spells" + File.separator + "boltend0.gif");
        poisonpic = tk.createImage("Spells" + File.separator + "poison.gif");
        poisonendpic = tk.createImage("Spells" + File.separator + "poisonend.gif");
        poisonendpic0 = tk.createImage("Spells" + File.separator + "poisonend0.gif");
        arcboltpic = tk.createImage("Spells" + File.separator + "arcbolt.gif");
        arcboltendpic = tk.createImage("Spells" + File.separator + "arcboltend.gif");
        arcboltendpic0 = tk.createImage("Spells" + File.separator + "arcboltend0.gif");
        weakpic = tk.createImage("Spells" + File.separator + "weak.gif");
        weakendpic = tk.createImage("Spells" + File.separator + "weakend.gif");
        weakendpic0 = tk.createImage("Spells" + File.separator + "weakend0.gif");
        silencepic = tk.createImage("Spells" + File.separator + "silence.gif");
        silenceendpic = tk.createImage("Spells" + File.separator + "silenceend.gif");
        silenceendpic0 = tk.createImage("Spells" + File.separator + "silenceend0.gif");
        magicarrowpic = tk.createImage("Spells" + File.separator + "magicarrow.gif");
        magicarrowendpic = tk.createImage("Spells" + File.separator + "magicarrowend.gif");
        magicarrowendpic0 = tk.createImage("Spells" + File.separator + "magicarrowend0.gif");
        fuseendpic = tk.createImage("Spells" + File.separator + "fuse.gif");
        fuseendpic0 = tk.createImage("Spells" + File.separator + "fuse0.gif");
        fulbombpic = tk.createImage("Items" + File.separator + "dful_bomb.gif");
        venbombpic = tk.createImage("Items" + File.separator + "dven_bomb.gif");
        boltbombpic = tk.createImage("Items" + File.separator + "dbolt_bomb.gif");
        blankpic = tk.createImage("blank.gif");
        
        Item.pics.put("dful_bomb.gif", fulbombpic);
        Item.pics.put("dven_bomb.gif", venbombpic);
        Item.pics.put("dbolt_bomb.gif", boltbombpic);
        
        ImageTracker.addImage(firepic, 0);
        ImageTracker.addImage(fireendpic, 0);
        ImageTracker.addImage(fireendpic0, 0);
        ImageTracker.addImage(spellpic, 0);
        ImageTracker.addImage(spellendpic, 0);
        ImageTracker.addImage(spellendpic0, 0);
        ImageTracker.addImage(boltpic, 0);
        ImageTracker.addImage(boltendpic, 0);
        ImageTracker.addImage(boltendpic0, 0);
        ImageTracker.addImage(poisonpic, 0);
        ImageTracker.addImage(poisonendpic, 0);
        ImageTracker.addImage(poisonendpic0, 0);
        ImageTracker.addImage(arcboltpic, 0);
        ImageTracker.addImage(arcboltendpic, 0);
        ImageTracker.addImage(arcboltendpic0, 0);
        ImageTracker.addImage(weakpic, 0);
        ImageTracker.addImage(weakendpic, 0);
        ImageTracker.addImage(weakendpic0, 0);
        ImageTracker.addImage(silencepic, 0);
        ImageTracker.addImage(silenceendpic, 0);
        ImageTracker.addImage(silenceendpic0, 0);
        ImageTracker.addImage(magicarrowpic, 0);
        ImageTracker.addImage(magicarrowendpic, 0);
        ImageTracker.addImage(magicarrowendpic0, 0);
        ImageTracker.addImage(fuseendpic, 0);
        ImageTracker.addImage(fuseendpic0, 0);
        ImageTracker.addImage(fulbombpic, 0);
        ImageTracker.addImage(venbombpic, 0);
        ImageTracker.addImage(boltbombpic, 0);
        try {
            ImageTracker.waitForID(0, 10000);
        } catch (InterruptedException e) {
        }
    }
    
    private void writeObject(ObjectOutputStream s) throws IOException {
        try {
            s.writeInt(number);
            s.writeInt(type);
            s.writeInt(power);
            s.writeInt(dist);
            s.writeInt(gain);
            s.writeInt(potionnum);
            s.writeObject(powers);
            s.writeChar(clsgain);
            s.writeBoolean(ismultiple);
            s.writeBoolean(hitsImmaterial);
            s.writeUTF(picstring);
            s.writeUTF(endpicstring);
        } catch (Exception e) {
            System.out.println("Error in Spell write - " + number);
            e.printStackTrace();
        }
    }
    
    private void readObject(ObjectInputStream s) throws IOException {
        try {
            number = s.readInt();
            type = s.readInt();
            power = s.readInt();
            dist = s.readInt();
            gain = s.readInt();
            potionnum = s.readInt();
            powers = (int[]) s.readObject();
            clsgain = s.readChar();
            ismultiple = s.readBoolean();
            hitsImmaterial = s.readBoolean();
            picstring = s.readUTF();
            endpicstring = s.readUTF();
            //endsound = s.readUTF();
            if (picstring.indexOf("fire") >= 0) {
                pic = firepic;
                endpic = fireendpic;
                endpic0 = fireendpic0;
            } else if (picstring.indexOf("spell") >= 0) {
                pic = spellpic;
                endpic = spellendpic;
                endpic0 = spellendpic0;
            } else if (picstring.indexOf("arc") >= 0) {
                pic = arcboltpic;
                endpic = arcboltendpic;
                endpic0 = arcboltendpic0;
            } else if (picstring.indexOf("bolt_bomb") >= 0) {
                pic = boltbombpic;
                endpic = boltendpic;
                endpic0 = boltendpic0;
            } else if (picstring.indexOf("bolt") >= 0) {
                pic = boltpic;
                endpic = boltendpic;
                endpic0 = boltendpic0;
            } else if (picstring.indexOf("poison") >= 0) {
                pic = poisonpic;
                endpic = poisonendpic;
                endpic0 = poisonendpic0;
            } else if (picstring.indexOf("weak") >= 0) {
                pic = weakpic;
                endpic = weakendpic;
                endpic0 = weakendpic0;
            } else if (picstring.indexOf("magic") >= 0) {
                pic = magicarrowpic;
                endpic = magicarrowendpic;
                endpic0 = magicarrowendpic0;
            } else if (picstring.indexOf("silence") >= 0) {
                pic = silencepic;
                endpic = silenceendpic;
                endpic0 = silenceendpic0;
            } else if (picstring.indexOf("ful") >= 0) {
                pic = fulbombpic;
                endpic = fireendpic;
                endpic0 = fireendpic0;
            } else if (picstring.indexOf("ven") >= 0) {
                pic = venbombpic;
                endpic = blankpic;
                endpic0 = blankpic;
            }
            if (endpicstring.indexOf("fuse") >= 0) {
                pic = blankpic;
                endpic = fuseendpic;
                endpic0 = fuseendpic0;
            } else if (endpicstring.indexOf("blank") >= 0) {
                endpic = blankpic;
                endpic0 = blankpic;
            }
        } catch (Exception e) {
            System.out.println("Error in Spell read - " + number);
            e.printStackTrace();
        }
    }
    
}

