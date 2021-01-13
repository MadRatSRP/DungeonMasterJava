import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Random;

class Item implements Serializable {
    int type;
    int size = 0; //<0 - fits all,passes grates>, <1 - fits all,no grates>, <2 - no pouch,only sheath,passes grates>, <3 - no pouch,only sheath,no grates>, <4 - no pouch,no quiver,no grates>
    float weight = 0.0f;
    int number;
    String name;
    Image pic;
    Image dpic;
    Image epic = null;      //if has special equipped pic (jewel symal turns red, illumlet glows, etc.)
    Image upic = null;      //if has special out of charges pic (fury w/o fire, etc.)
    Image temppic;          //for holding old pic when equipped
    Image[] throwpic;
    String picstring = "";       //for serialization
    String dpicstring = "";      //for serialization
    String equipstring = "";     //for serialization
    String usedupstring = "";    //for serialization
    String throwpicstring = "";  //for serialization
    int throwpow = 1;       //power when thrown/shot
    int shotpow = 0;        //added power from bow,sling,whatever (is equal to the power of the bow,sling,whatever)
    int projtype = 0;       //arrow, rock, dart types must match to bows, slings, blowpipe
    String[][] function;    //function & class that incs f,n,w,p,0(none)
    int power[];
    int speed[];            //greater is slower
    int level[];            //level needed to use item ability
    int defense = 0;        //0 if no effect
    int magicresist = 0;    //"
    int foodvalue;          //used for both food & water, how much to restore
    int functions = 0;      //# functions a weapon has
    int charges[] = {0, 0, 0};//# charges a magic function has
    int potionpow;          //? total power of potion
    int potioncastpow;      //power level of potion (LO,MON,etc)
    String bombnum;
    boolean hasthrowpic = false;
    boolean ispotion = false;
    boolean isbomb = false; //?needs work?
    boolean hitsImmaterial = false;
    int cursed = 0; //power of curse (0 for not cursed), remove curse decreases this, item can't be removed until cursed==0 (not implemented yet)
    boolean cursefound; //when true and still cursed, item is outlined in red
    int poisonous = 0; //power of poison (0 for not poisonous)
    String[] scroll;
    boolean[] bound;
    static HashMap pics = new HashMap(350);
    static final long serialVersionUID = -5698111842003644130L;
    
    boolean haseffect = false;    //for raising a stat or level
    String[] effect;              //effect & how much, sep by comma - is array since can have more than 1
    //effects: "health" -> maxhealth, "mana" -> maxmana, "stamina" -> maxstamina //did have: "load" -> maxload
    //"<some attribute name>" -> that attrib (str,vit,dex,int,wis)
    //"flevel nlevel wlevel plevel" -> that level (if below mon master already - won't raise to arch)
    int fleveladded = 0, nleveladded = 0, wleveladded = 0, pleveladded = 0; //how much added to each
    int stradded, dexadded, vitadded, intadded, wisadded, hadded, sadded, madded; //how much added to each
    
    //when on ground:
    int subsquare = 0; //what corner of floor square (index)
    int xoffset, yoffset;
    
    static final int WEAPON = 0;
    static final int SHIELD = 1;
    static final int HEAD = 2;
    static final int TORSO = 3;
    static final int NECK = 4;
    static final int LEGS = 5;
    static final int FEET = 6;
    static final int FOOD = 7;
    static final int OTHER = 8;
    
    static final Random randGen = new Random();
    static final Toolkit tk = Toolkit.getDefaultToolkit();
    static MediaTracker ImageTracker = new MediaTracker(new JPanel());
    
    static boolean PATCHING = false; //unfortunately necessary for the patch program
    //static boolean PATCHING2 = true; //temporary, just to update my current maps
    
    static final Item fistfoot = new Item(6);
    
    public Item() { //for chests and custom items
        xoffset = randGen.nextInt() % 5;
        yoffset = randGen.nextInt() % 5;
    }
    
    public Item(int num) {
        number = num;
        xoffset = randGen.nextInt() % 5;
        yoffset = randGen.nextInt() % 5;
        subsquare = randGen.nextInt(4);
        switch (number) {
            //0-3 for bones (correspond to hero numbers)
            //4 for scrolls
            //5 for chests
            case 6: //special item fist/foot
                type = WEAPON;
                name = "Fist/Foot";
                picstring = "fistfoot.gif";
                dpicstring = "";
                weight = 0.0f;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Punch";
                function[0][1] = "n";
                function[1][0] = "Kick";
                function[1][1] = "n";
                //function[2][0]="Detect Illusion";
                function[2][0] = "War Cry";
                function[2][1] = "p";
                charges[2] = -1;
                power = new int[3];
                power[0] = 1;
                power[1] = 3;
                //power[2]=0;
                power[2] = 2;
                speed = new int[3];
                speed[0] = 3;
                speed[1] = 7;
                //speed[2]=25;
                speed[2] = 12;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                //level[2]=9;
                level[2] = 0;
                break;
            case 7: //special item flask
                type = OTHER;
                name = "Flask";
                picstring = "flask.gif";
                dpicstring = "dflask.gif";
                weight = 0.1f;
                size = 1;
                break;
                        /*
                        case 8: //special item compass
                                type = OTHER;
                                name = "Compass";
                                picstring = "compass-n.gif";
                                dpicstring = "dcompass.gif";
                                weight = 0.1f;
                                break;
                        */
            //9 is torch
            //10-30 potions (not all used currently)
                        
                        /*
                        
                        //stuff i will probably get rid of:
                        case :
                                type = LEGS;
                                name = "Blue Pants";
                                picstring = "blue_pants.gif";
                                dpicstring = "dblue_clothes.gif";
                                weight = 0.6f;
                                size = 2;
                                defense = 2;
                                magicresist = 1;
                                break;
                        case :
                                type = TORSO;
                                name = "Kirtle";
                                picstring = "kirtle.gif";
                                dpicstring = "dblue_clothes.gif";
                                weight = 0.4f;
                                size = 2;
                                defense = 1;
                                magicresist = 1;
                                break;
                        case :
                                type = LEGS;
                                name = "Gunna";
                                picstring = "gunna.gif";
                                dpicstring = "dblue_clothes.gif";
                                weight = 0.5f;
                                size = 2;
                                defense = 1;
                                magicresist = 1;
                                break;
                        case :
                                type = TORSO;
                                name = "Tunic";
                                picstring = "tunic.gif";
                                dpicstring = "dblue_clothes.gif";
                                weight = 0.5f;
                                size = 2;
                                defense = 2;
                                magicresist = 1;
                                break;
                        case :
                                type = TORSO;
                                name = "Silk Shirt";
                                picstring = "silk_shirt.gif";
                                dpicstring = "dwhite_clothes.gif";
                                weight = 0.2f;
                                size = 2;
                                defense = 1;
                                magicresist = 1;
                                break;
                        case :
                                type = LEGS;
                                name = "Tabbard";
                                picstring = "tabbard.gif";
                                dpicstring = "dwhite_clothes.gif";
                                weight = 0.4f;
                                size = 2;
                                defense = 1;
                                magicresist = 1;
                                break;
                        case :
                                type = TORSO;
                                name = "Flamebain";
                                picstring = "flamebain.gif";
                                dpicstring = "dmail.gif";
                                weight = 5.7f;
                                size = 2;
                                defense = 5;
                                magicresist = 15;
                                break;
                                
                                
                        */
            
            //---------------------
            //       MISC
            //---------------------
            
            //---------------------
            //------KEYS-----------
            //---------------------
            //31-50
            case 31:
                type = OTHER;
                name = "Iron Key";
                picstring = "iron_key.gif";
                dpicstring = "dkey_iron.gif";
                weight = 0.1f;
                break;
            case 32:
                type = OTHER;
                name = "Key of B";
                picstring = "key_b.gif";
                dpicstring = "dkey_iron.gif";
                weight = 0.1f;
                break;
            case 33:
                type = OTHER;
                name = "Square Key";
                picstring = "square_key.gif";
                dpicstring = "dkey_iron.gif";
                weight = 0.1f;
                break;
            case 34:
                type = OTHER;
                name = "Solid Key";
                picstring = "solid_key.gif";
                dpicstring = "dkey_iron.gif";
                weight = 0.2f;
                break;
            case 35:
                type = OTHER;
                name = "Cross Key";
                picstring = "cross_key.gif";
                dpicstring = "dkey_iron.gif";
                weight = 0.1f;
                break;
            case 36:
                type = OTHER;
                name = "Skeleton Key";
                picstring = "skeleton_key.gif";
                dpicstring = "dkey_iron.gif";
                weight = 0.1f;
                break;
            case 37:
                type = OTHER;
                name = "Onyx Key";
                picstring = "onyx_key.gif";
                dpicstring = "dkey_iron.gif";
                weight = 0.1f;
                break;
            case 38:
                type = OTHER;
                name = "Tourquoise Key";
                picstring = "tourquoise_key.gif";
                dpicstring = "dkey_iron.gif";
                weight = 0.1f;
                break;
            case 39:
                type = OTHER;
                name = "Gold Key";
                picstring = "gold_key.gif";
                dpicstring = "dkey_gold.gif";
                weight = 0.1f;
                break;
            case 40:
                type = OTHER;
                name = "Master Key";
                picstring = "master_key.gif";
                dpicstring = "dkey_gold.gif";
                weight = 0.1f;
                break;
            case 41:
                type = OTHER;
                name = "Ra Key";
                picstring = "ra_key.gif";
                dpicstring = "dkey_gold.gif";
                weight = 0.1f;
                break;
            case 42:
                type = OTHER;
                name = "Winged Key";
                picstring = "winged_key.gif";
                dpicstring = "dkey_gold.gif";
                weight = 0.1f;
                break;
            case 43:
                type = OTHER;
                name = "Emerald Key";
                picstring = "emerald_key.gif";
                dpicstring = "dkey_gold.gif";
                weight = 0.1f;
                break;
            case 44:
                type = OTHER;
                name = "Ruby Key";
                picstring = "ruby_key.gif";
                dpicstring = "dkey_gold.gif";
                weight = 0.1f;
                break;
            case 45:
                type = OTHER;
                name = "Sapphire Key";
                picstring = "sapphire_key.gif";
                dpicstring = "dkey_gold.gif";
                weight = 0.1f;
                break;
            case 46:
                type = OTHER;
                name = "Topaz Key";
                picstring = "topaz_key.gif";
                dpicstring = "dkey_gold.gif";
                weight = 0.1f;
                break;
            
            //---------------------
            //------COINS----------
            //---------------------
            //51-54
            case 51:
                type = OTHER;
                name = "Gold Coin";
                picstring = "coin_gold.gif";
                dpicstring = "dcoin_gold.gif";
                weight = 0.1f;
                break;
            case 52:
                type = OTHER;
                name = "Silver Coin";
                picstring = "coin_silver.gif";
                dpicstring = "dcoin_silver.gif";
                weight = 0.1f;
                break;
            case 53:
                type = OTHER;
                name = "Copper Coin";
                picstring = "coin_copper.gif";
                dpicstring = "dcoin_gold.gif";
                weight = 0.1f;
                break;
            case 54:
                type = OTHER;
                name = "Gor Coin";
                picstring = "coin_gor.gif";
                dpicstring = "dcoin_gold.gif";
                weight = 0.1f;
                break;
            
            //---------------------
            //------GEMS-----------
            //---------------------
            //55-60
            case 55:
                type = OTHER;
                name = "Blue Gem";
                picstring = "gem_blue.gif";
                dpicstring = "dgem_blue.gif";
                weight = 0.2f;
                break;
            case 56:
                type = OTHER;
                name = "Green Gem";
                picstring = "gem_green.gif";
                dpicstring = "dgem_green.gif";
                weight = 0.2f;
                break;
            case 57:
                type = OTHER;
                name = "Orange Gem";
                picstring = "gem_orange.gif";
                dpicstring = "dgem_orange.gif";
                weight = 0.3f;
                break;
            case 58:
                type = SHIELD;
                name = "Sar Crystal";
                picstring = "sar_crystal.gif";
                dpicstring = "dgem_blue.gif";
                weight = 0.5f;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,10";
                break;
            case 59:
                type = SHIELD;
                name = "Ra Crystal";
                picstring = "ra_crystal.gif";
                dpicstring = "dgem_orange.gif";
                weight = 0.5f;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,10";
                break;
            //---------------------
            //------FOOD-----------
            //---------------------
            //61-70
            case 61:
                type = FOOD;
                name = "Worm Round";
                picstring = "worm_round.gif";
                dpicstring = "dworm_round.gif";
                weight = 1.1f;
                size = 3;
                foodvalue = 150;
                break;
            case 62:
                type = FOOD;
                name = "Apple";
                picstring = "apple.gif";
                dpicstring = "dapple.gif";
                weight = 0.4f;
                foodvalue = 175;
                break;
            case 63:
                type = FOOD;
                name = "Corn";
                picstring = "corn.gif";
                dpicstring = "dcorn.gif";
                weight = 0.4f;
                size = 1;
                foodvalue = 200;
                break;
            case 64:
                type = FOOD;
                name = "Bread";
                picstring = "bread.gif";
                dpicstring = "dbread.gif";
                weight = 0.3f;
                size = 1;
                foodvalue = 250;
                break;
            case 65:
                type = FOOD;
                name = "Screamer Slice";
                picstring = "screamer_slice.gif";
                dpicstring = "dscreamer_slice.gif";
                weight = 0.5f;
                size = 3;
                foodvalue = 275;
                break;
            case 66:
                type = FOOD;
                name = "Cheese";
                picstring = "cheese.gif";
                dpicstring = "dcheese.gif";
                weight = 0.8f;
                size = 1;
                foodvalue = 300;
                break;
            case 67:
                type = FOOD;
                name = "Drumstick";
                picstring = "drumstick.gif";
                dpicstring = "ddrumstick.gif";
                weight = 0.4f;
                foodvalue = 450;
                break;
            case 68:
                type = FOOD;
                name = "Shank";
                picstring = "shank.gif";
                dpicstring = "dshank.gif";
                weight = 0.4f;
                size = 1;
                foodvalue = 550;
                break;
            case 69:
                type = FOOD;
                name = "Dragon Steak";
                picstring = "dragon_steak.gif";
                dpicstring = "dsteak.gif";
                weight = 0.6f;
                size = 1;
                foodvalue = 850;
                break;
            case 70:
                type = FOOD;
                name = "Good Berries";
                picstring = "good_berries.gif";
                dpicstring = "dberries.gif";
                weight = 0.1f;
                foodvalue = 80;
                haseffect = true;
                effect = new String[2];
                effect[0] = "health,20";
                effect[1] = "stamina,20";
                break;
            //---------------------
            //------OTHER----------
            //---------------------
            //71-85
            case 71:
                type = OTHER;
                name = "Lock Picks"; //success depends on dexterity - not implemented yet, may need make this special # so can easily test in wallswitch
                picstring = "lock_picks.gif";
                dpicstring = "dlock_picks.gif";
                weight = 0.1f;
                break;
            case 72: //water flask -> its # in Fountain
                type = OTHER;
                name = "Water Flask";
                picstring = "water_flask.gif";
                dpicstring = "dwater_flask.gif";
                weight = 0.3f;
                size = 1;
                foodvalue = 200;
                break;
                        /*is now sep class
                        case 73:
                                type = OTHER;
                                name = "Waterskin";
                                picstring = "waterskin.gif";
                                dpicstring = "dwaterskin.gif";
                                weight = 0.9f;//.9 is full, changed by fountain and drinking (.2 for each drink)
                                size = 2;
                                foodvalue=300;
                                break;
                        */
            case 74:
                type = OTHER;
                name = "Ashes";
                picstring = "ashes.gif";
                dpicstring = "dashes.gif";
                weight = 0.4f;
                size = 1;
                break;
            case 75:  //number used in dmnew and altar
                type = OTHER;
                name = "Bones";
                picstring = "bones.gif";
                dpicstring = "dbones.gif";
                weight = 0.8f;
                size = 3;
                break;
            case 76:
                type = OTHER;
                name = "Boulder";
                picstring = "boulder.gif";
                dpicstring = "dboulder.gif";
                weight = 8.1f;
                size = 3;
                throwpow = 5;
                break;
            case 77:
                type = OTHER;
                name = "Magnifier";
                picstring = "magnifier.gif";
                dpicstring = "dmagnifier.gif";
                weight = 0.2f;
                break;
            case 78:
                type = OTHER;
                name = "Mirror of Dawn";
                picstring = "mirror_dawn.gif";
                dpicstring = "dmirror.gif";
                weight = 0.3f;
                size = 1;
                break;
            case 79:
                type = OTHER;
                name = "Corbamite";
                picstring = "corbamite.gif";
                dpicstring = "dcorbamite.gif";
                weight = 0.0f;
                break;
            case 80:  //# used in dmnew
                type = OTHER;
                name = "Zo Kath Ra";
                picstring = "zo_kath_ra.gif";
                dpicstring = "zo_kath_ra.gif";
                weight = 0.0f;
                break;
            case 81:
                type = WEAPON;
                name = "Rope";
                picstring = "rope.gif";
                dpicstring = "drope.gif";
                weight = 1.0f;
                size = 3;
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Climb Down";
                function[0][1] = "n";
                power = new int[1];
                power[0] = 0;
                speed = new int[1];
                speed[0] = 6;
                level = new int[1];
                level[0] = 0;
                break;
            case 82:
                type = OTHER;
                name = "Corbum Ore";
                picstring = "corbum.gif";
                dpicstring = "corbum.gif";
                weight = 0.0f;
                break;
            case 83:
                type = WEAPON;
                name = "Stick";
                picstring = "stick.gif";
                dpicstring = "dstick.gif";
                weight = 0.2f;
                size = 3;
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                power = new int[1];
                power[0] = 2;
                speed = new int[1];
                speed[0] = 8;
                level = new int[1];
                level[0] = 0;
                bound = new boolean[4];
                break;
            case 84:
                type = OTHER; //maybe SHIELD?
                name = "Rabbit's Foot";
                picstring = "rabbit_foot.gif";
                dpicstring = "drabbit_foot.gif";
                weight = 0.1f;
                break;
            case 85:
                type = WEAPON;
                name = "Grapple";
                picstring = "grapple.gif";
                dpicstring = "drope.gif";
                weight = 1.5f;
                size = 3;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Climb Down";
                function[0][1] = "n";
                function[1][0] = "Climb Up";
                function[1][1] = "n";
                power = new int[2];
                power[0] = 0;
                power[1] = 0;
                speed = new int[2];
                speed[0] = 6;
                speed[1] = 6;
                level = new int[2];
                level[0] = 0;
                level[1] = 7;
                break;
            //---------------------
            //       ARMOR
            //---------------------
            //86-199
            //---------------------
            //------NECK-----------
            //---------------------
            //86-100
            case 86:
                type = NECK;
                name = "Cape";
                picstring = "cape.gif";
                dpicstring = "dbrown_clothes.gif";
                weight = 0.3f;
                size = 1;
                defense = 1;
                magicresist = 1;
                break;
            case 87:
                type = NECK;
                name = "Cloak of Night";
                picstring = "cloak_night.gif";
                dpicstring = "dbrown_clothes.gif";
                weight = 0.4f;
                size = 1;
                defense = 3;
                magicresist = 5;
                haseffect = true;
                effect = new String[1];
                effect[0] = "dexterity,8";
                break;
            case 88:
                type = NECK;
                name = "Choker";
                picstring = "choker.gif";
                dpicstring = "dchoker.gif";
                weight = 0.1f;
                size = 1;
                defense = 1;
                magicresist = 0;
                break;
            case 89:
                type = NECK;
                name = "Illumlet";
                picstring = "illumlet.gif";
                dpicstring = "dnecklace.gif";
                equipstring = "illumlet-on.gif";
                weight = 0.2f;
                size = 1;
                defense = 0;
                magicresist = 0;
                break;
            case 90:
                type = NECK;
                name = "Moonstone";
                picstring = "moonstone.gif";
                dpicstring = "dmoonstone.gif";
                weight = 0.2f;
                size = 1;
                defense = 0;
                magicresist = 1;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,10";
                break;
            case 91:
                type = NECK;
                name = "Jewel Symal";
                picstring = "jewel_symal.gif";
                dpicstring = "dnecklace.gif";
                equipstring = "jewel_symal-on.gif";
                weight = 0.2f;
                size = 1;
                defense = 0;
                magicresist = 10;
                break;
            case 92:
                type = NECK;
                name = "Ekkhard Cross";
                picstring = "ekkhard_cross.gif";
                dpicstring = "dnecklace.gif";
                weight = 0.3f;
                size = 1;
                defense = 0;
                magicresist = 2;
                haseffect = true;
                effect = new String[1];
                effect[0] = "wisdom,4";
                break;
            case 93:
                type = NECK;
                name = "Gem of Ages";
                picstring = "gem_ages.gif";
                dpicstring = "dnecklace.gif";
                weight = 0.2f;
                size = 1;
                defense = 0;
                magicresist = 2;
                haseffect = true;
                effect = new String[1];
                effect[0] = "intelligence,4";
                break;
            case 94:
                type = NECK;
                name = "Pendant Feral";
                picstring = "pendant_feral.gif";
                dpicstring = "dpendant.gif";
                weight = 0.2f;
                size = 1;
                defense = 0;
                magicresist = 5;
                haseffect = true;
                effect = new String[1];
                effect[0] = "wlevel,1";
                break;
            case 95:
                type = NECK;
                name = "The Hellion"; //maybe make do something different, something special?
                picstring = "hellion.gif";
                dpicstring = "dpendant.gif";
                weight = 0.2f;
                size = 1;
                defense = 0;
                magicresist = 2;
                haseffect = true;
                effect = new String[5];
                effect[0] = "strength,3";
                effect[1] = "vitality,3";
                effect[2] = "dexterity,3";
                effect[3] = "intelligence,3";
                effect[4] = "wisdom,3";
                break;
            case 96:
                type = NECK;
                name = "Symbol of Ra";
                picstring = "ra_necklace.gif";
                dpicstring = "dnecklace.gif";
                weight = 0.2f;
                size = 1;
                defense = 0;
                magicresist = 4;
                haseffect = true;
                effect = new String[3];
                effect[0] = "vitality,4";
                effect[1] = "intelligence,4";
                effect[2] = "wisdom,4";
                break;
            case 97:
                type = NECK;
                name = "Symbol of Sar";
                picstring = "sar_necklace.gif";
                dpicstring = "dpendant.gif";
                weight = 0.2f;
                size = 1;
                defense = 2;
                magicresist = 2;
                haseffect = true;
                effect = new String[3];
                effect[0] = "strength,4";
                effect[1] = "intelligence,4";
                effect[2] = "wisdom,4";
                break;
                        /*
                        //will inc wizard xp like ekkhard cross
                        case 98:
                                type = NECK;
                                name = "Mana Shard";
                                picstring = "mana_shard.gif";
                                dpicstring = "dnecklace.gif";
                                weight = 0.3f;
                                size = 1;
                                defense = 0;
                                magicresist = 2;
                                haseffect = true;
                                effect = new String[1];
                                effect[0] = "intelligence,4";
                                break;
                        */
            //---------------------
            //------SHIELD---------
            //---------------------
            //101-115
            case 101:
                type = SHIELD;
                name = "Hide Shield";
                picstring = "hide_shield.gif";
                dpicstring = "dsmall_shield.gif";
                weight = 1.0f;
                size = 3;
                defense = 2;
                magicresist = 2;
                break;
            case 102:
                type = SHIELD;
                name = "Buckler";
                picstring = "buckler.gif";
                dpicstring = "dsmall_shield.gif";
                weight = 1.1f;
                size = 3;
                defense = 3;
                magicresist = 3;
                break;
            case 103:
                type = SHIELD;
                name = "Small Shield";
                picstring = "small_shield.gif";
                dpicstring = "dsmall_shield.gif";
                weight = 2.1f;
                size = 3;
                defense = 5;
                magicresist = 5;
                break;
            case 104:
                type = SHIELD;
                name = "Wooden Shield";
                picstring = "wooden_shield.gif";
                dpicstring = "dlarge_shield.gif";
                weight = 1.4f;
                size = 3;
                defense = 6;
                magicresist = 4;
                break;
            case 105:
                type = SHIELD;
                name = "Large Shield";
                picstring = "large_shield.gif";
                dpicstring = "dlarge_shield.gif";
                weight = 3.4f;
                size = 3;
                defense = 7;
                magicresist = 8;
                break;
            case 106:
                type = SHIELD;
                name = "Lyte Shield";
                picstring = "lyte_shield.gif";
                dpicstring = "dlarge_shield.gif";
                weight = 3.0f;
                size = 3;
                defense = 8;
                magicresist = 9;
                break;
            case 107:
                type = SHIELD;
                name = "Sar Shield";
                picstring = "sar_shield.gif";
                dpicstring = "dlarge_shield.gif";
                weight = 5.0f;
                size = 3;
                defense = 10;
                magicresist = 10;
                break;
            case 108:
                type = SHIELD;
                name = "Dragon Shield";
                picstring = "dragon_shield.gif";
                dpicstring = "dlarge_shield.gif";
                weight = 4.0f;
                size = 3;
                defense = 12;
                magicresist = 11;
                break;
            case 109:
                type = SHIELD;
                name = "Ra Shield";
                picstring = "ra_shield.gif";
                dpicstring = "dlarge_shield.gif";
                weight = 3.4f;
                size = 3;
                defense = 10;
                magicresist = 12;
                break;
            
            
            //---------------------
            //------HEAD-----------
            //---------------------
            //116-135
            case 116:
                type = HEAD;
                name = "Calista";
                picstring = "calista.gif";
                dpicstring = "dcrown.gif";
                weight = 0.4f;
                size = 1;
                defense = 1;
                magicresist = 8;
                break;
            case 117:
                type = HEAD;
                name = "Crown of Nerra";
                picstring = "crown_nerra.gif";
                dpicstring = "dcrown.gif";
                weight = 0.6f;
                size = 3;
                defense = 1;
                magicresist = 4;
                haseffect = true;
                effect = new String[1];
                effect[0] = "wisdom,10";
                break;
            case 118:
                type = HEAD;
                name = "Berserker Helm";
                picstring = "berserker_helm.gif";
                dpicstring = "dsmall_helm.gif";
                weight = 1.1f;
                size = 3;
                defense = 2;
                magicresist = 2;
                break;
            case 119:
                type = HEAD;
                name = "Basinet";
                picstring = "basinet.gif";
                dpicstring = "dsmall_helm.gif";
                weight = 1.5f;
                size = 3;
                defense = 3;
                magicresist = 3;
                break;
            case 120:
                type = HEAD;
                name = "Helmet";
                picstring = "helmet.gif";
                dpicstring = "dsmall_helm.gif";
                weight = 1.4f;
                size = 3;
                defense = 4;
                magicresist = 4;
                break;
            case 121:
                type = HEAD;
                name = "Casque 'n Coif";
                picstring = "casque_n_coif.gif";
                dpicstring = "dsmall_helm.gif";
                weight = 1.6f;
                size = 3;
                defense = 5;
                magicresist = 5;
                break;
            case 122:
                type = HEAD;
                name = "Armet";
                picstring = "armet.gif";
                dpicstring = "dhelm.gif";
                weight = 1.9f;
                size = 3;
                defense = 6;
                magicresist = 6;
                break;
            case 123:
                type = HEAD;
                name = "Dexhelm";
                picstring = "dexhelm.gif";
                dpicstring = "dhelm.gif";
                weight = 1.4f;
                size = 3;
                defense = 6;
                magicresist = 6;
                haseffect = true;
                effect = new String[1];
                effect[0] = "dexterity,10"; //adds 10 to dexterity
                break;
            case 124:
                type = HEAD;
                name = "Lyte Helm";
                picstring = "lyte_helm.gif";
                dpicstring = "dhelm.gif";
                weight = 1.7f;
                size = 3;
                defense = 7;
                magicresist = 7;
                break;
            case 125:
                type = HEAD;
                name = "Sar Helm";
                picstring = "sar_helm.gif";
                dpicstring = "dhelm.gif";
                weight = 3.5f;
                size = 3;
                defense = 8;
                magicresist = 8;
                break;
            case 126:
                type = HEAD;
                name = "Dragon Helm";
                picstring = "dragon_helm.gif";
                dpicstring = "dhelm.gif";
                weight = 3.5f;
                size = 3;
                defense = 10;
                magicresist = 9;
                break;
            case 127:
                type = HEAD;
                name = "Ra Helm";
                picstring = "ra_helm.gif";
                dpicstring = "dhelm.gif";
                weight = 2.5f;
                size = 3;
                defense = 9;
                magicresist = 10;
                break;
            case 128:
                type = HEAD;
                name = "Sar Circlet";
                picstring = "sar_circlet.gif";
                dpicstring = "dcrown.gif";
                weight = 0.5f;
                size = 1;
                defense = 1;
                magicresist = 5;
                haseffect = true;
                effect = new String[2];
                effect[0] = "intelligence,8";
                effect[1] = "mana,15";
                break;
            case 129:
                type = HEAD;
                name = "Ra Circlet";
                picstring = "ra_circlet.gif";
                dpicstring = "dcrown.gif";
                weight = 0.3f;
                size = 1;
                defense = 1;
                magicresist = 5;
                haseffect = true;
                effect = new String[2];
                effect[0] = "intelligence,8";
                effect[1] = "mana,15";
                break;
            case 130:
                type = HEAD;
                name = "Executioner's Hood";
                picstring = "executioner_hood.gif";
                dpicstring = "dblack_clothes.gif";
                weight = 0.5f;
                size = 3;
                defense = 4;
                magicresist = 10;
                haseffect = true;
                effect = new String[2];
                effect[0] = "intelligence,10";
                effect[1] = "mana,20";
                break;
            case 131:
                type = HEAD;
                name = "Elven Circlet";
                picstring = "elven_circlet.gif";
                dpicstring = "dcrown.gif";
                equipstring = "elven_circlet-on.gif";
                weight = 0.1f;
                size = 1;
                defense = 4;
                magicresist = 10;
                haseffect = true;
                effect = new String[3];
                effect[0] = "wisdom,10";
                effect[1] = "intelligence,10";
                effect[2] = "mana,20";
                break;
            //---------------------
            //------TORSO----------
            //---------------------
            //136-155
            case 136:
                type = TORSO;
                name = "Halter"; //keep this? maybe make a magical one instead?
                picstring = "halter.gif";
                dpicstring = "dhalter.gif";
                weight = 0.2f;
                size = 3;
                defense = 1;
                magicresist = 1;
                break;
            case 137:
                type = TORSO;
                name = "Robe";
                picstring = "robe_top.gif";
                dpicstring = "dwhite_clothes.gif";
                weight = 0.4f;
                size = 3;
                defense = 1;
                magicresist = 3;
                break;
            case 138:
                type = TORSO;
                name = "Fine Robe";
                picstring = "fine_robe_top.gif";
                dpicstring = "dwhite_clothes.gif";
                weight = 0.3f;
                size = 3;
                defense = 2;
                magicresist = 4;
                break;
            case 139:
                type = TORSO;
                name = "Ghi";
                picstring = "ghi.gif";
                dpicstring = "dwhite_clothes.gif";
                weight = 0.5f;
                size = 3;
                defense = 4;
                magicresist = 2;
                haseffect = true;
                effect = new String[1];
                effect[0] = "dexterity,4";
                break;
            case 140:
                type = TORSO;
                name = "Leather Jerkin";
                picstring = "leather_jerkin.gif";
                dpicstring = "dbrown_clothes.gif";
                weight = 0.6f;
                size = 3;
                defense = 3;
                magicresist = 3;
                break;
            case 141:
                type = TORSO;
                name = "Elven Dublet";
                picstring = "elven_dublet.gif";
                dpicstring = "delven_clothes.gif";
                weight = 0.3f;
                size = 3;
                defense = 6;
                magicresist = 6;
                break;
            case 142:
                type = TORSO;
                name = "Mail Aketon";
                picstring = "mail_aketon.gif";
                dpicstring = "dmail.gif";
                weight = 6.5f;
                size = 3;
                defense = 8;
                magicresist = 8;
                break;
            case 143:
                type = TORSO;
                name = "Torso Plate";
                picstring = "torso_plate.gif";
                dpicstring = "dtorso_plate.gif";
                weight = 12.0f;
                size = 4;
                defense = 10;
                magicresist = 10;
                break;
            case 144:
                type = TORSO;
                name = "Mithril Aketon";
                picstring = "mithril_aketon.gif";
                dpicstring = "dmail.gif";
                weight = 4.2f;
                size = 3;
                defense = 12;
                magicresist = 12;
                break;
            case 145:
                type = TORSO;
                name = "Lyte Plate";
                picstring = "lyte_torso.gif";
                dpicstring = "dtorso_plate.gif";
                weight = 10.8f;
                size = 4;
                defense = 14;
                magicresist = 14;
                break;
            case 146:
                type = TORSO;
                name = "Sar Plate";
                picstring = "sar_torso.gif";
                dpicstring = "dtorso_plate.gif";
                weight = 14.1f;
                size = 4;
                defense = 16;
                magicresist = 16;
                break;
            case 147:
                type = TORSO;
                name = "Dragon Plate";
                picstring = "dragon_torso.gif";
                dpicstring = "dtorso_plate.gif";
                weight = 14.1f;
                size = 4;
                defense = 20;
                magicresist = 17;
                break;
            case 148:
                type = TORSO;
                name = "Ra Plate";
                picstring = "ra_torso.gif";
                dpicstring = "dtorso_plate.gif";
                weight = 12.1f;
                size = 4;
                defense = 17;
                magicresist = 20;
                break;
            case 149:
                type = TORSO;
                name = "Sar Robe";
                picstring = "sar_robe_top.gif";
                dpicstring = "dblack_clothes.gif";
                weight = 0.6f;
                size = 3;
                defense = 5;
                magicresist = 12;
                haseffect = true;
                effect = new String[3];
                effect[0] = "intelligence,5";
                effect[1] = "wisdom,5";
                effect[2] = "mana,10";
                break;
            case 150:
                type = TORSO;
                name = "Ra Robe";
                picstring = "ra_robe_top.gif";
                dpicstring = "dwhite_clothes.gif";
                weight = 0.6f;
                size = 3;
                defense = 5;
                magicresist = 12;
                haseffect = true;
                effect = new String[3];
                effect[0] = "intelligence,5";
                effect[1] = "wisdom,5";
                effect[2] = "mana,10";
                break;
            
            //---------------------
            //------LEGS-----------
            //---------------------
            //156-175
            case 156:
                type = LEGS;
                name = "Barbarian Hide";
                picstring = "barbarian_hide.gif";
                dpicstring = "dbrown_clothes.gif";
                weight = 0.3f;
                size = 3;
                defense = 1;
                magicresist = 0;
                break;
            case 157:
                type = LEGS;
                name = "Robe";
                picstring = "robe_bottom.gif";
                dpicstring = "dwhite_clothes.gif";
                weight = 0.4f;
                size = 3;
                defense = 1;
                magicresist = 1;
                break;
            case 158:
                type = LEGS;
                name = "Fine Robe";
                picstring = "fine_robe_bottom.gif";
                dpicstring = "dwhite_clothes.gif";
                weight = 0.3f;
                size = 3;
                defense = 1;
                magicresist = 2;
                break;
            case 159:
                type = LEGS;
                name = "Ghi Trousers";
                picstring = "ghi_trousers.gif";
                dpicstring = "dwhite_clothes.gif";
                weight = 0.5f;
                size = 3;
                defense = 2;
                magicresist = 1;
                haseffect = true;
                effect = new String[1];
                effect[0] = "dexterity,2";
                break;
            case 160:
                type = LEGS;
                name = "Leather Pants";
                picstring = "leather_pants.gif";
                dpicstring = "dbrown_clothes.gif";
                weight = 0.8f;
                size = 3;
                defense = 2;
                magicresist = 2;
                break;
            case 161:
                type = LEGS;
                name = "Elven Huke";
                picstring = "elven_huke.gif";
                dpicstring = "delven_clothes.gif";
                weight = 0.3f;
                size = 3;
                defense = 3;
                magicresist = 3;
                break;
            case 162:
                type = LEGS;
                name = "Leg Mail";
                picstring = "mail_leg.gif";
                dpicstring = "dmail.gif";
                weight = 5.3f;
                size = 3;
                defense = 4;
                magicresist = 4;
                break;
            case 163:
                type = LEGS;
                name = "Leg Plate";
                picstring = "leg_plate.gif";
                dpicstring = "dleg_plate.gif";
                weight = 8.0f;
                size = 3;
                defense = 5;
                magicresist = 5;
                break;
            case 164:
                type = LEGS;
                name = "Mithril Leg";
                picstring = "mithril_leg.gif";
                dpicstring = "dmail.gif";
                weight = 3.1f;
                size = 3;
                defense = 6;
                magicresist = 6;
                break;
            case 165:
                type = LEGS;
                name = "Lyte Poleyn";
                picstring = "lyte_poleyn.gif";
                dpicstring = "dleg_plate.gif";
                weight = 7.2f;
                size = 3;
                defense = 7;
                magicresist = 7;
                break;
            case 166:
                type = LEGS;
                name = "Sar Poleyn";
                picstring = "sar_poleyn.gif";
                dpicstring = "dleg_plate.gif";
                weight = 9.0f;
                size = 3;
                defense = 7;
                magicresist = 7;
                break;
            case 167:
                type = LEGS;
                name = "Dragon Poleyn";
                picstring = "dragon_poleyn.gif";
                dpicstring = "dleg_plate.gif";
                weight = 9.0f;
                size = 3;
                defense = 8;
                magicresist = 8;
                break;
            case 168:
                type = LEGS;
                name = "Ra Poleyn";
                picstring = "ra_poleyn.gif";
                dpicstring = "dleg_plate.gif";
                weight = 8.0f;
                size = 3;
                defense = 8;
                magicresist = 8;
                break;
            case 169:
                type = LEGS;
                name = "Powertowers";
                picstring = "powertowers.gif";
                dpicstring = "dleg_plate.gif";
                weight = 8.2f;
                size = 3;
                defense = 6;
                magicresist = 6;
                haseffect = true;
                effect = new String[1];
                effect[0] = "strength,10"; //adds 10 to strength
                break;
            case 170:
                type = LEGS;
                name = "Sar Robe";
                picstring = "sar_robe_bottom.gif";
                dpicstring = "dblack_clothes.gif";
                weight = 0.6f;
                size = 3;
                defense = 2;
                magicresist = 4;
                haseffect = true;
                effect = new String[3];
                effect[0] = "intelligence,2";
                effect[1] = "wisdom,2";
                effect[2] = "mana,5";
                break;
            case 171:
                type = LEGS;
                name = "Ra Robe";
                picstring = "ra_robe_bottom.gif";
                dpicstring = "dwhite_clothes.gif";
                weight = 0.6f;
                size = 3;
                defense = 2;
                magicresist = 4;
                haseffect = true;
                effect = new String[3];
                effect[0] = "intelligence,2";
                effect[1] = "wisdom,2";
                effect[2] = "mana,5";
                break;
            
            //---------------------
            //------FEET-----------
            //---------------------
            //176-195
            case 176:
                type = FEET;
                name = "Sandals";
                picstring = "sandals.gif";
                dpicstring = "dsandals.gif";
                weight = 0.6f;
                size = 3;
                defense = 1;
                magicresist = 0;
                break;
            case 177:
                type = FEET;
                name = "Leather Boots";
                picstring = "leather_boots.gif";
                dpicstring = "dleather_boots.gif";
                weight = 1.4f;
                size = 3;
                defense = 1;
                magicresist = 1;
                break;
            case 178:
                type = FEET;
                name = "Black Boots";
                picstring = "black_boots.gif";
                dpicstring = "dblack_boots.gif";
                weight = 1.6f;
                size = 3;
                defense = 2;
                magicresist = 1;
                break;
            case 179:
                type = FEET;
                name = "Elven Boots";
                picstring = "elven_boots.gif";
                dpicstring = "delven_boots.gif";
                weight = 0.4f;
                size = 3;
                defense = 2;
                magicresist = 3;
                haseffect = true;
                effect = new String[1];
                //effect[0] = "load,6";
                effect[0] = "strength,6";
                break;
            case 180:
                type = FEET;
                name = "Mail Hosen";
                picstring = "mail_hosen.gif";
                dpicstring = "dmail.gif";
                weight = 1.6f;
                size = 3;
                defense = 3;
                magicresist = 3;
                break;
            case 181:
                type = FEET;
                name = "Foot Plate";
                picstring = "foot_plate.gif";
                dpicstring = "dgreave.gif";
                weight = 2.8f;
                size = 3;
                defense = 4;
                magicresist = 3;
                break;
            case 182:
                type = FEET;
                name = "Mithril Hosen";
                picstring = "mithril_hosen.gif";
                dpicstring = "dmail.gif";
                weight = 0.9f;
                size = 3;
                defense = 5;
                magicresist = 5;
                break;
            case 183:
                type = FEET;
                name = "Lyte Greave";
                picstring = "lyte_greave.gif";
                dpicstring = "dgreave.gif";
                weight = 2.4f;
                size = 3;
                defense = 6;
                magicresist = 6;
                break;
            case 184:
                type = FEET;
                name = "Sar Greave";
                picstring = "sar_greave.gif";
                dpicstring = "dgreave.gif";
                weight = 3.1f;
                size = 3;
                defense = 7;
                magicresist = 7;
                break;
            case 185:
                type = FEET;
                name = "Dragon Greave";
                picstring = "dragon_greave.gif";
                dpicstring = "dgreave.gif";
                weight = 3.1f;
                size = 3;
                defense = 8;
                magicresist = 7;
                break;
            case 186:
                type = FEET;
                name = "Ra Greave";
                picstring = "ra_greave.gif";
                dpicstring = "dgreave.gif";
                weight = 2.8f;
                size = 3;
                defense = 7;
                magicresist = 8;
                break;
            case 187://# in dmnew
                type = FEET;
                name = "Boots of Speed";
                picstring = "boots_speed.gif";
                dpicstring = "dboots_speed.gif";
                weight = 0.3f;
                size = 3;
                defense = 3;
                magicresist = 4;
                haseffect = true;
                effect = new String[1];
                effect[0] = "dexterity,4";
                break;
            //196-199 free
            //---------------------
            //      WEAPONS
            //---------------------
            //200-299 except 282-285 are runes
            //---------------------
            //------SWORD----------
            //---------------------
            //200-220
            case 200:
                type = WEAPON;
                name = "Falchion";
                picstring = "falchion.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.3f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Parry";
                function[1][1] = "n";
                function[2][0] = "Chop";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 6;
                power[1] = 4;
                power[2] = 10;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 16;
                speed[2] = 16;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 3;
                break;
            case 201:
                type = WEAPON;
                name = "Sword";
                picstring = "sword.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.2f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[3][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Parry";
                function[1][1] = "n";
                function[2][0] = "Chop";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 6;
                power[1] = 4;
                power[2] = 10;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 15;
                speed[2] = 15;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 3;
                break;
            case 202:
                type = WEAPON;
                name = "Sabre";
                picstring = "sabre.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.5f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Slash";
                function[0][1] = "n";
                function[1][0] = "Parry";
                function[1][1] = "n";
                function[2][0] = "Melee";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 8;
                power[1] = 4;
                power[2] = 13;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 14;
                speed[2] = 18;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 4;
                break;
            case 203:
                type = WEAPON;
                name = "Samurai Sword";
                picstring = "samurai_sword.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.6f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Slash";
                function[0][1] = "n";
                function[1][0] = "Parry";
                function[1][1] = "n";
                function[2][0] = "Melee";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 10;
                power[1] = 6;
                power[2] = 14;
                speed = new int[3];
                speed[0] = 10;
                speed[1] = 15;
                speed[2] = 17;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 4;
                break;
            case 204:
                type = WEAPON;
                name = "Rapier";
                picstring = "rapier.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 2.6f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Jab";
                function[0][1] = "f";
                function[1][0] = "Parry";
                function[1][1] = "n";
                function[2][0] = "Thrust";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 5;
                power[1] = 4;
                power[2] = 15;
                speed = new int[3];
                speed[0] = 6;
                speed[1] = 14;
                speed[2] = 18;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 5;
                break;
            case 205:
                type = WEAPON;
                name = "Delta";
                picstring = "delta.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.3f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Chop";
                function[0][1] = "f";
                function[1][0] = "Melee";
                function[1][1] = "f";
                function[2][0] = "Thrust";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 10;
                power[1] = 14;
                power[2] = 16;
                speed = new int[3];
                speed[0] = 15;
                speed[1] = 17;
                speed[2] = 19;
                level = new int[3];
                level[0] = 3;
                level[1] = 4;
                level[2] = 6;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,3";
                break;
            case 206:
                type = WEAPON;
                name = "Diamond Edge";
                picstring = "diamond_edge.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.7f;
                size = 2;
                throwpow = 15;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Stab";
                function[0][1] = "f";
                function[1][0] = "Chop";
                function[1][1] = "f";
                function[2][0] = "Cleave";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 8;
                power[1] = 11;
                power[2] = 17;
                speed = new int[3];
                speed[0] = 7;
                speed[1] = 14;
                speed[2] = 18;
                level = new int[3];
                level[0] = 0;
                level[1] = 4;
                level[2] = 7;
                break;
            case 207:
                type = WEAPON;
                name = "The Inquisitor";
                picstring = "inquisitor.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.9f;
                size = 2;
                throwpow = 20;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Thrust";
                function[1][1] = "f";
                function[2][0] = "Berzerk";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 8;
                power[1] = 15;
                power[2] = 21;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 16;
                speed[2] = 24;
                level = new int[3];
                level[0] = 0;
                level[1] = 5;
                level[2] = 8;
                haseffect = true;
                effect = new String[2];
                effect[0] = "mana,10";
                effect[1] = "health,25";
                break;
            case 208:
                type = WEAPON;
                name = "Dragon Fang";
                picstring = "dragon_fang.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.4f;
                size = 2;
                throwpow = 25;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Cleave";
                function[1][1] = "f";
                function[2][0] = "Berzerk";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 8;
                power[1] = 17;
                power[2] = 20;
                speed = new int[3];
                speed[0] = 14;
                speed[1] = 18;
                speed[2] = 25;
                level = new int[3];
                level[0] = 0;
                level[1] = 6;
                level[2] = 9;
                poisonous = 6;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,3";
                break;
            case 209:
                type = WEAPON;
                name = "Vorpal Blade";
                picstring = "vorpal_blade.gif";
                dpicstring = "dvorpal_blade.gif";
                throwpicstring = "vorpal_blade";
                weight = 3.9f;
                size = 2;
                throwpow = 15;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Jab";
                function[0][1] = "f";
                function[1][0] = "Cleave";
                function[1][1] = "f";
                function[2][0] = "Disrupt";//change so has no effect on material
                function[2][1] = "w";
                power = new int[3];
                power[0] = 6;
                power[1] = 17;
                power[2] = 20;
                speed = new int[3];
                speed[0] = 7;
                speed[1] = 18;
                speed[2] = 24;
                level = new int[3];
                level[0] = 0;
                level[1] = 5;
                level[2] = 6;
                hitsImmaterial = true;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,6";
                break;
            case 210:
                type = WEAPON;
                name = "Ven Blade";
                picstring = "ven_blade.gif";
                dpicstring = "dsword.gif";
                equipstring = "ven_blade-anim.gif";
                usedupstring = "ven_blade-dead.gif";
                throwpicstring = "sword";
                weight = 3.9f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Jab";
                function[0][1] = "f";
                function[1][0] = "Thrust";
                function[1][1] = "f";
                function[2][0] = "Venom";
                function[2][1] = "w";
                charges[2] = 20;
                power = new int[3];
                power[0] = 6;
                power[1] = 15;
                power[2] = 3;
                speed = new int[3];
                speed[0] = 8;
                speed[1] = 18;
                speed[2] = 24;
                level = new int[3];
                level[0] = 0;
                level[1] = 5;
                level[2] = 1;
                poisonous = 6;
                break;
            case 211:
                type = WEAPON;
                name = "Bolt Blade";
                picstring = "bolt_blade.gif";
                dpicstring = "dsword.gif";
                equipstring = "bolt_blade-anim.gif";
                usedupstring = "bolt_blade-dead.gif";
                throwpicstring = "sword";
                weight = 3.0f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Jab";
                function[0][1] = "f";
                function[1][0] = "Chop";
                function[1][1] = "f";
                function[2][0] = "Bolt";
                function[2][1] = "w";
                charges[2] = 20;
                power = new int[3];
                power[0] = 6;
                power[1] = 10;
                power[2] = 4;
                speed = new int[3];
                speed[0] = 8;
                speed[1] = 14;
                speed[2] = 22;
                level = new int[3];
                level[0] = 0;
                level[1] = 3;
                level[2] = 1;
                break;
            case 212:
                type = WEAPON;
                name = "Fury";
                picstring = "fury.gif";
                dpicstring = "dsword.gif";
                equipstring = "fury-anim.gif";
                usedupstring = "fury-dead.gif";
                throwpicstring = "sword";
                weight = 4.7f;
                size = 2;
                throwpow = 10;
                functions = 3;
                function = new String[3][2];
                function[0][0] = "Chop";
                function[0][1] = "f";
                function[1][0] = "Melee";
                function[1][1] = "f";
                function[2][0] = "Fireball";
                function[2][1] = "w";
                charges[2] = 20;
                power = new int[3];
                power[0] = 10;
                power[1] = 14;
                power[2] = 5;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 17;
                speed[2] = 24;
                level = new int[3];
                level[0] = 3;
                level[1] = 4;
                level[2] = 1;
                break;
            case 213:
                type = WEAPON;
                name = "Ra Blade";
                picstring = "ra_blade.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.7f;
                size = 2;
                throwpow = 12;
                functions = 3;
                function = new String[3][2];
                function[0][0] = "Thrust";
                function[0][1] = "f";
                function[1][0] = "Bolt";
                function[1][1] = "w";
                function[2][0] = "Fireball";
                function[2][1] = "w";
                charges[1] = 20;
                charges[2] = 20;
                power = new int[3];
                power[0] = 16;
                power[1] = 4;
                power[2] = 4;
                speed = new int[3];
                speed[0] = 16;
                speed[1] = 22;
                speed[2] = 22;
                level = new int[3];
                level[0] = 4;
                level[1] = 1;
                level[2] = 1;
                break;
            case 214:
                type = WEAPON;
                name = "Sar Sword";
                picstring = "sar_sword.gif";
                dpicstring = "dsar_sword.gif";
                throwpicstring = "sar_sword";
                weight = 5.1f;
                size = 2;
                throwpow = 20;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Thrust";
                function[1][1] = "f";
                function[2][0] = "Berzerk";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 9;
                power[1] = 17;
                power[2] = 21;
                speed = new int[3];
                speed[0] = 15;
                speed[1] = 21;
                speed[2] = 26;
                level = new int[3];
                level[0] = 2;
                level[1] = 5;
                level[2] = 9;
                haseffect = true;
                effect = new String[1];
                effect[0] = "strength,5";
                defense = 5;
                break;
                        /*
                        case 215:
                                //very powerful, but occasionally (very rarely, but will happen) kills other party members whenever is in weapon hand (check after each kill as well as in timepass)
                                //--displays message saying "Stormbringer feeds"
                                //--can't ressurrect, since soul is consumed (instead of party bones dropped, regular item bones instead)
                                //restores health and stamina if kills
                                //stamina decreases more quickly if in weapon hand
                                type = WEAPON;
                                name = "Stormbringer";
                                picstring = "stormbringer.gif";
                                dpicstring = "dstormbringer.gif";
                                equipstring = "stormbringer-anim.gif";
                                throwpicstring = "stormbringer";
                                weight = 5.9f;
                                size = 2;
                                throwpow = 30;
                                functions=3;
                                function = new String[functions][2];
                                function[0][0]="Thrust";
                                function[0][1]="f";
                                function[1][0]="Cleave";
                                function[1][1]="f";
                                function[2][0]="Berzerk";
                                function[2][1]="f";
                                power = new int[3];
                                power[0]=18;
                                power[1]=21;
                                power[2]=25;
                                speed = new int[3];
                                speed[0]=14;
                                speed[1]=19;
                                speed[2]=24;
                                level = new int[3];
                                level[0]=0;
                                level[1]=0;
                                level[2]=0;
                                hitsImmaterial = true;
                                haseffect = true;
                                effect = new String[7];
                                effect[0] = "health,50";
                                effect[1] = "stamina,50";
                                effect[2] = "mana,50";
                                effect[3] = "strength,10";
                                effect[4] = "intelligence,10";
                                effect[5] = "flevel,1";
                                effect[6] = "wlevel,1";
                                defense = 12;
                                magicresist = 12;
                                //System.out.println("made stormbringer");
                                break;
                        */
            case 215:
                type = WEAPON;
                name = "Stormbringer";
                picstring = "stormbringer.gif";
                dpicstring = "dstormbringer.gif";
                throwpicstring = "stormbringer";
                weight = 5.9f;
                size = 2;
                throwpow = 30;
                functions = 3;
                function = new String[3][2];
                function[0][0] = "Thrust";
                function[0][1] = "f";
                function[1][0] = "Cleave";
                function[1][1] = "f";
                function[2][0] = "Berzerk";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 18;
                power[1] = 21;
                power[2] = 25;
                speed = new int[3];
                speed[0] = 14;
                speed[1] = 19;
                speed[2] = 24;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                level[2] = 0;
                hitsImmaterial = true;
                haseffect = true;
                effect = new String[7];
                effect[0] = "health,50";
                effect[1] = "stamina,50";
                effect[2] = "mana,50";
                effect[3] = "strength,10";
                effect[4] = "intelligence,10";
                effect[5] = "flevel,1";
                effect[6] = "wlevel,1";
                defense = 12;
                magicresist = 12;
                break;
            case 216:
                type = WEAPON;
                name = "Dragon Tounge";
                picstring = "dragon_tounge.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.3f;
                size = 2;
                throwpow = 12;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Parry";
                function[1][1] = "n";
                function[2][0] = "Melee";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 7;
                power[1] = 7;
                power[2] = 14;
                speed = new int[3];
                speed[0] = 9;
                speed[1] = 14;
                speed[2] = 16;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 4;
                haseffect = true;
                effect = new String[1];
                effect[0] = "vitality,2";
                break;
            case 217:
                type = WEAPON;
                name = "Triashka";
                picstring = "triashka.gif";
                dpicstring = "dsword.gif";
                throwpicstring = "sword";
                weight = 3.3f;
                size = 2;
                throwpow = 12;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Chop";
                function[0][1] = "f";
                function[1][0] = "Melee";
                function[1][1] = "f";
                function[2][0] = "Thrust";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 10;
                power[1] = 14;
                power[2] = 16;
                speed = new int[3];
                speed[0] = 13;
                speed[1] = 15;
                speed[2] = 17;
                level = new int[3];
                level[0] = 3;
                level[1] = 4;
                level[2] = 5;
                haseffect = true;
                effect = new String[3];
                effect[0] = "health,5";
                effect[1] = "stamina,6";
                effect[2] = "mana,3";
                break;
            case 218:
                type = WEAPON;
                name = "Darkwing Cutter";
                picstring = "darkwing_cutter.gif";
                dpicstring = "ddarkwing_cutter.gif";
                throwpicstring = "darkwing_cutter";
                weight = 3.3f;
                size = 2;
                throwpow = 12;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Chop";
                function[0][1] = "f";
                function[1][0] = "Melee";
                function[1][1] = "f";
                function[2][0] = "Disrupt";
                function[2][1] = "w";
                power = new int[3];
                power[0] = 10;
                power[1] = 14;
                power[2] = 18;
                speed = new int[3];
                speed[0] = 7;
                speed[1] = 15;
                speed[2] = 20;
                level = new int[3];
                level[0] = 3;
                level[1] = 4;
                level[2] = 7;
                hitsImmaterial = true;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,4";
                break;
            case 219:
                type = WEAPON;
                name = "KU Sword";
                picstring = "ku_sword.gif";
                dpicstring = "ku_sword.gif";
                weight = 0.0f;
                weight = 3.2f;
                size = 2;
                functions = 1;
                function = new String[1][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                power = new int[1];
                power[0] = 6;
                speed = new int[1];
                speed[0] = 12;
                level = new int[1];
                level[0] = 0;
                break;
            //---------------------
            //------AXE------------
            //---------------------
            //221-225 - need this range for throwpics in load
            case 221:
                type = WEAPON;
                name = "Axe";
                picstring = "axe.gif";
                dpicstring = "daxe.gif";
                throwpicstring = "axe";
                weight = 4.3f;
                size = 3;
                throwpow = 15;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Chop";
                function[1][1] = "f";
                function[2][0] = "Melee";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 8;
                power[1] = 12;
                power[2] = 14;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 14;
                speed[2] = 16;
                level = new int[3];
                level[0] = 0;
                level[1] = 3;
                level[2] = 5;
                break;
            case 222:
                type = WEAPON;
                name = "Hardcleave";
                picstring = "hardcleave.gif";
                dpicstring = "daxe.gif";
                throwpicstring = "axe";
                weight = 5.7f;
                size = 3;
                throwpow = 20;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Chop";
                function[0][1] = "f";
                function[1][0] = "Melee";
                function[1][1] = "f";
                function[2][0] = "Cleave";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 12;
                power[1] = 14;
                power[2] = 18;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 16;
                speed[2] = 20;
                level = new int[3];
                level[0] = 3;
                level[1] = 4;
                level[2] = 6;
                break;
            case 223:
                type = WEAPON;
                name = "Executioner";
                picstring = "executioner.gif";
                dpicstring = "daxe.gif";
                throwpicstring = "axe";
                weight = 6.5f;
                size = 3;
                throwpow = 25;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Chop";
                function[0][1] = "f";
                function[1][0] = "Cleave";
                function[1][1] = "f";
                function[2][0] = "Berzerk";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 14;
                power[1] = 17;
                power[2] = 21;
                speed = new int[3];
                speed[0] = 16;
                speed[1] = 20;
                speed[2] = 24;
                level = new int[3];
                level[0] = 3;
                level[1] = 5;
                level[2] = 8;
                break;
            
            //---------------------
            //------BLUNT----------
            //---------------------
            //226-235
            case 226:
                type = WEAPON;
                name = "Club";
                picstring = "club.gif";
                dpicstring = "dclub.gif";
                throwpicstring = "club";
                weight = 3.6f;
                size = 3;
                throwpow = 10;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Bash";
                function[0][1] = "f";
                function[1][0] = "Throw";
                function[1][1] = "n";
                power = new int[2];
                power[0] = 7;
                power[1] = 4;
                speed = new int[2];
                speed[0] = 12;
                speed[1] = 14;
                level = new int[2];
                level[0] = 0;
                level[1] = 0;
                break;
            case 227:
                type = WEAPON;
                name = "Stone Club";
                picstring = "stone_club.gif";
                dpicstring = "dstone_club.gif";
                throwpicstring = "stone_club";
                weight = 11.0f;
                size = 3;
                throwpow = 20;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Bash";
                function[0][1] = "f";
                function[1][0] = "Throw";
                function[1][1] = "n";
                power = new int[2];
                power[0] = 18;
                power[1] = 20;
                speed = new int[2];
                speed[0] = 20;
                speed[1] = 26;
                level = new int[2];
                level[0] = 0;
                level[1] = 0;
                break;
            case 228:
                type = WEAPON;
                name = "Mace";
                picstring = "mace.gif";
                dpicstring = "dmace.gif";
                weight = 3.1f;
                size = 3;
                throwpow = 12;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Bash";
                function[1][1] = "f";
                function[2][0] = "Stun";//freeze briefly
                function[2][1] = "f";
                power = new int[3];
                power[0] = 6;
                power[1] = 8;
                power[2] = 12;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 14;
                speed[2] = 16;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                level[2] = 4;
                break;
            case 229:
                type = WEAPON;
                name = "Mace of Order";
                picstring = "mace_order.gif";
                dpicstring = "dmace.gif";
                weight = 4.1f;
                size = 3;
                throwpow = 15;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Bash";
                function[1][1] = "f";
                function[2][0] = "Stun";//freeze briefly
                function[2][1] = "f";
                power = new int[3];
                power[0] = 8;
                power[1] = 10;
                power[2] = 14;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 14;
                speed[2] = 16;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                level[2] = 5;
                haseffect = true;
                effect = new String[1];
                effect[0] = "strength,5";
                break;
            case 230:
                type = WEAPON;
                name = "Morningstar";
                picstring = "morningstar.gif";
                dpicstring = "dmorningstar.gif";
                weight = 5.0f;
                size = 4;
                throwpow = 15;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Stun";//freeze briefly
                function[1][1] = "f";
                function[2][0] = "Crush";
                function[2][1] = "f";
                power = new int[3];
                power[0] = 12;
                power[1] = 15;
                power[2] = 19;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 18;
                speed[2] = 20;
                level = new int[3];
                level[0] = 0;
                level[1] = 4;
                level[2] = 7;
                break;
            
            //---------------------
            //------STAFF----------
            //---------------------
            //236-255
            case 236:
                type = WEAPON;
                name = "Wand";
                picstring = "wand.gif";
                dpicstring = "dwand.gif";
                weight = 0.1f;
                functions = 3;
                function = new String[3][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Shield Party";
                function[1][1] = "p";
                function[2][0] = "Heal";
                function[2][1] = "p";
                charges[1] = 12;
                charges[2] = 12;
                power = new int[3];
                power[0] = 1;
                power[1] = 5;
                power[2] = 20;
                speed = new int[3];
                speed[0] = 7;
                speed[1] = 16;
                speed[2] = 22;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                level[2] = 2;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,3"; //adds 3 to maxmana
                break;
            case 237:
                type = WEAPON;
                name = "Teowand";
                picstring = "teowand.gif";
                dpicstring = "dwand.gif";
                weight = 0.2f;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Calm";
                function[0][1] = "p";
                function[1][0] = "Shield Party";
                function[1][1] = "p";
                function[2][0] = "SpellShield Party";
                function[2][1] = "p";
                charges[0] = 12;
                charges[1] = 24;
                charges[2] = 24;
                power = new int[3];
                power[0] = 2;//will run for 2 steps only, if successfully frightened
                power[1] = 8;
                power[2] = 8;
                speed = new int[3];
                speed[0] = 9;
                speed[1] = 18;
                speed[2] = 18;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                level[2] = 2;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,6";
                break;
            case 238:
                type = WEAPON;
                name = "Staff";
                picstring = "staff.gif";
                dpicstring = "dstaff.gif";
                weight = 2.6f;
                size = 3;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Light";
                function[1][1] = "w";
                charges[1] = 80;
                power = new int[2];
                power[0] = 2;
                power[1] = 100;//changes alpha by 100
                speed = new int[2];
                speed[0] = 8;
                speed[1] = 16;
                level = new int[2];
                level[0] = 0;
                level[1] = 0;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,4";
                break;
            case 239:
                type = WEAPON;
                name = "Staff of Claws";
                picstring = "staff_claws.gif";
                dpicstring = "dstaff.gif";
                weight = 0.9f;
                size = 3;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Slash";
                function[0][1] = "n";
                function[1][0] = "Frighten";
                function[1][1] = "p";
                charges[1] = 12;
                power = new int[2];
                power[0] = 8;
                power[1] = 2;
                speed = new int[2];
                speed[0] = 8;
                speed[1] = 12;
                level = new int[2];
                level[0] = 0;
                level[1] = 0;
                poisonous = 3;
                haseffect = true;
                effect = new String[2];
                effect[0] = "dexterity,4";
                effect[1] = "mana,6";
                break;
            case 240:
                type = WEAPON;
                name = "Yew Staff";
                picstring = "yew_staff.gif";
                dpicstring = "dstaff.gif";
                weight = 3.5f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Parry";
                function[0][1] = "n";
                function[1][0] = "Light";
                function[1][1] = "w";
                function[2][0] = "Dispell";
                function[2][1] = "w";
                charges[1] = 24;
                charges[2] = 10;
                power = new int[3];
                power[0] = 2;
                power[1] = 150;
                power[2] = 4;
                speed = new int[3];
                speed[0] = 14;
                speed[1] = 20;
                speed[2] = 20;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                level[2] = 2;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,8";
                break;
            case 241:
                type = WEAPON;
                name = "Staff of Manar";
                picstring = "staff_manar.gif";
                dpicstring = "dstaff.gif";
                weight = 2.9f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Dispell";
                function[1][1] = "w";
                function[2][0] = "SpellShield Party";
                function[2][1] = "p";
                charges[1] = 18;
                charges[2] = 14;
                power = new int[3];
                power[0] = 3;
                power[1] = 4;
                power[2] = 12;
                speed = new int[3];
                speed[0] = 10;
                speed[1] = 20;
                speed[2] = 20;
                level = new int[3];
                level[0] = 0;
                level[1] = 2;
                level[2] = 2;
                haseffect = true;
                effect = new String[2];
                effect[0] = "mana,15";
                effect[1] = "intelligence,5";
                break;
            case 242:
                type = WEAPON;
                name = "Staff of Irra";
                picstring = "staff_irra.gif";
                dpicstring = "dstaff.gif";
                weight = 2.9f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Shield Party";
                function[1][1] = "p";
                function[2][0] = "SpellShield Party";
                function[2][1] = "p";
                charges[1] = 12;
                charges[2] = 12;
                power = new int[3];
                power[0] = 3;
                power[1] = 10;
                power[2] = 14;
                speed = new int[3];
                speed[0] = 10;
                speed[1] = 20;
                speed[2] = 20;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 2;
                haseffect = true;
                effect = new String[2];
                effect[0] = "mana,15";
                effect[1] = "wisdom,5";
                break;
            case 243:
                type = WEAPON;
                name = "Snake Staff";
                picstring = "snake_staff.gif";
                dpicstring = "dstaff.gif";
                weight = 2.1f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Heal";
                function[1][1] = "p";
                function[2][0] = "Anti-Ven";
                function[2][1] = "p";
                defense = 5;
                magicresist = 5;
                charges[1] = 12;
                charges[2] = 20;
                power = new int[3];
                power[0] = 4;
                power[1] = 30;
                power[2] = 13;//will heal most poison
                speed = new int[3];
                speed[0] = 9;
                speed[1] = 20;
                speed[2] = 20;
                level = new int[3];
                level[0] = 0;
                level[1] = 2;
                level[2] = 4;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,12";
                break;
            case 244:
                type = WEAPON;
                name = "Dragon Spit";
                picstring = "dragon_spit.gif";
                dpicstring = "ddragon_spit.gif";
                equipstring = "dragon_spit-on.gif";
                weight = 0.8f;
                size = 3;
                functions = 1;//intentionally 1 instead of 2
                function = new String[2][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Fireball";
                function[1][1] = "w";
                charges[1] = 0;//starts empty
                power = new int[2];
                power[0] = 12;
                power[1] = 6;
                speed = new int[2];
                speed[0] = 6;
                speed[1] = 6;
                level = new int[2];
                level[0] = 0;
                level[1] = 0;
                //haseffect = true;
                //effect = new String[1];
                //effect[0] = "mana,7";
                magicresist = 8;
                break;
            case 245:
                type = WEAPON;
                name = "Sceptre of Lyf";
                picstring = "sceptre_lyf.gif";
                dpicstring = "dsceptre.gif";
                weight = 1.8f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Shield Party";
                function[0][1] = "p";
                function[1][0] = "SpellShield Party";
                function[1][1] = "p";
                function[2][0] = "Heal";
                function[2][1] = "p";
                charges[0] = 20;
                charges[1] = 20;
                charges[2] = 20;
                power = new int[3];
                power[0] = 16;
                power[1] = 16;
                power[2] = 90;//heal 90%
                speed = new int[3];
                speed[0] = 16;
                speed[1] = 16;
                speed[2] = 16;
                level = new int[3];
                level[0] = 0;
                level[1] = 2;
                level[2] = 2;
                haseffect = true;
                effect = new String[3];
                effect[0] = "mana,15";
                effect[1] = "health,20";
                effect[2] = "stamina,20";
                break;
            case 246:
                type = WEAPON;
                name = "The Conduit";
                picstring = "conduit.gif";
                dpicstring = "dconduit.gif";
                weight = 3.3f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Bolt";
                function[1][1] = "w";
                function[2][0] = "Sight";
                function[2][1] = "p";
                charges[1] = 25;
                charges[2] = 30;
                power = new int[3];
                power[0] = 12;
                power[1] = 5;
                power[2] = 30;//adds 30 to magicvision counter
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 16;
                speed[2] = 16;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 0;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,20";
                break;
            case 247:
                type = WEAPON;
                name = "Serpent Staff";
                picstring = "serpent_staff.gif";
                dpicstring = "dserpent_staff.gif";
                weight = 3.3f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Venom";
                function[1][1] = "w";
                function[2][0] = "Ven Cloud";
                function[2][1] = "w";
                charges[1] = 25;
                charges[2] = 25;
                power = new int[3];
                power[0] = 8;
                power[1] = 5;
                power[2] = 5;
                speed = new int[3];
                speed[0] = 12;
                speed[1] = 16;
                speed[2] = 16;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 4;
                poisonous = 8;
                haseffect = true;
                effect = new String[1];
                effect[0] = "mana,20";
                break;
            case 248:
                type = WEAPON;
                name = "The Firestaff";
                picstring = "firestaff.gif";
                dpicstring = "dfirestaff.gif";
                weight = 2.4f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Parry";
                function[1][1] = "n";
                function[2][0] = "SpellShield Party";
                function[2][1] = "p";
                charges[2] = -1;
                power = new int[3];
                power[0] = 6;
                power[1] = 5;
                power[2] = 20;
                speed = new int[3];
                speed[0] = 8;
                speed[1] = 14;
                speed[2] = 18;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                level[2] = 0;
                haseffect = true;
                effect = new String[4];
                effect[0] = "flevel,1";
                effect[1] = "nlevel,1";
                effect[2] = "plevel,1";
                effect[3] = "wlevel,1";
                break;
            case 249:
                type = WEAPON;
                name = "The Firestaff+";
                picstring = "firestaff_gem.gif";
                dpicstring = "dfirestaff_gem.gif";
                weight = 3.6f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Invoke";
                function[0][1] = "w";
                function[1][0] = "Fuse";
                function[1][1] = "w";
                function[2][0] = "Fluxcage";
                function[2][1] = "p";
                charges[0] = -1;
                charges[1] = -1;
                charges[2] = -1;
                power = new int[3];
                power[0] = 0;//n/a
                power[1] = 0;//n/a
                power[2] = 0;//n/a
                speed = new int[3];
                speed[0] = 18;
                speed[1] = 4;
                speed[2] = 5;
                level = new int[3];
                level[0] = 0;
                level[1] = 0;
                level[2] = 0;
                haseffect = true;
                effect = new String[4];
                effect[0] = "flevel,2";
                effect[1] = "nlevel,2";
                effect[2] = "plevel,2";
                effect[3] = "wlevel,2";
                break;
            case 250:
                type = WEAPON;
                name = "Cross of Neta";
                picstring = "cross_neta.gif";
                dpicstring = "dstaff.gif";
                weight = 2.1f;
                size = 3;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Heal";
                function[1][1] = "p";
                function[2][0] = "Calm";
                function[2][1] = "p";
                charges[1] = 12;
                charges[2] = 24;
                power = new int[3];
                power[0] = 4;
                power[1] = 30;
                power[2] = 6;
                speed = new int[3];
                speed[0] = 9;
                speed[1] = 20;
                speed[2] = 20;
                level = new int[3];
                level[0] = 0;
                level[1] = 2;
                level[2] = 2;
                haseffect = true;
                effect = new String[2];
                effect[0] = "mana,15";
                effect[1] = "wisdom,8";
                break;
            case 251:
                type = WEAPON;
                name = "Staff of Decay";
                picstring = "staff_decay.gif";
                dpicstring = "dstaff.gif";
                weight = 2.6f;
                size = 3;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Ruiner";
                function[1][1] = "w";
                charges[1] = 30;
                power = new int[2];
                power[0] = 3;
                power[1] = 6;
                speed = new int[2];
                speed[0] = 9;
                speed[1] = 25;
                level = new int[2];
                level[0] = 4;
                level[1] = 10;
                haseffect = true;
                effect = new String[4];
                effect[0] = "vitality,-10";
                effect[1] = "stamina,-10";
                effect[2] = "health,-10";
                effect[3] = "mana,30";
                break;
            case 252:
                type = WEAPON;
                name = "Deth Staff";
                picstring = "deth_staff.gif";
                dpicstring = "dstaff.gif";
                weight = 2.6f;
                size = 3;
                hitsImmaterial = true;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Swing";
                function[0][1] = "f";
                function[1][0] = "Disrupt";
                function[1][1] = "w";
                function[2][0] = "Dispell";
                function[2][1] = "w";
                charges[2] = 30;
                power = new int[3];
                power[0] = 8;
                power[1] = 18;
                power[2] = 5;
                speed = new int[3];
                speed[0] = 9;
                speed[1] = 20;
                speed[2] = 20;
                level = new int[3];
                level[0] = 0;
                level[1] = 6;
                level[2] = 1;
                haseffect = true;
                effect = new String[2];
                effect[0] = "intelligence,5";
                effect[1] = "mana,15";
                break;
            //---------------------
            //------BOW------------
            //---------------------
            //256-265
            case 256:
                type = WEAPON;
                name = "Sling";
                picstring = "sling.gif";
                dpicstring = "dstaff.gif";
                weight = 1.9f;
                size = 3;
                projtype = 2; //rock type
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Shoot";
                function[0][1] = "n";
                power = new int[1];
                power[0] = 9;
                speed = new int[1];
                speed[0] = 12;
                level = new int[1];
                level[0] = 0;
                break;
            case 257:
                type = WEAPON;
                name = "Bow";
                picstring = "bow.gif";
                dpicstring = "dbow.gif";
                weight = 1.5f;
                size = 4;
                projtype = 1; //arrow type
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Shoot";
                function[0][1] = "n";
                //function[1][0]="Swing";
                //function[1][1]="f";
                power = new int[1];
                power[0] = 12;
                //power[1]=3;
                speed = new int[1];
                speed[0] = 16;
                //speed[1]=8;
                level = new int[1];
                level[0] = 0;
                //level[1]=0;
                break;
            case 258:
                type = WEAPON;
                name = "Claw Bow";
                picstring = "claw_bow.gif";
                dpicstring = "dclaw_bow.gif";
                weight = 2.0f;
                size = 4;
                projtype = 1; //arrow type
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Shoot";
                function[0][1] = "n";
                //function[1][0]="Swing";
                //function[1][1]="f";
                power = new int[1];
                power[0] = 17;
                //power[1]=8;
                speed = new int[1];
                speed[0] = 16;
                //speed[1]=9;
                level = new int[1];
                level[0] = 4;
                //level[1]=0;
                break;
            case 259:
                type = WEAPON;
                name = "Crossbow";
                picstring = "crossbow.gif";
                dpicstring = "dcrossbow.gif";
                weight = 2.8f;
                size = 4;
                projtype = 1; //arrow type
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Shoot";
                function[0][1] = "n";
                power = new int[1];
                power[0] = 14;
                speed = new int[1];
                speed[0] = 20;
                level = new int[1];
                level[0] = 0;
                break;
            case 260:
                type = WEAPON;
                name = "Speedbow";
                picstring = "speedbow.gif";
                dpicstring = "dcrossbow.gif";
                weight = 3.0f;
                size = 4;
                projtype = 1; //arrow type
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Shoot";
                function[0][1] = "n";
                power = new int[1];
                power[0] = 16;
                speed = new int[1];
                speed[0] = 12;
                level = new int[1];
                level[0] = 0;
                break;
            case 261:
                type = WEAPON;
                name = "ROS Bow";
                picstring = "ros_bow.gif";
                dpicstring = "ros_bow.gif";
                weight = 0.0f;
                size = 4;
                projtype = 1; //arrow type
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Magic Shot";
                function[0][1] = "n";
                power = new int[1];
                power[0] = 0;
                speed = new int[1];
                speed[0] = 14;
                level = new int[1];
                level[0] = 0;
                break;
            //---------------------
            //------THROWN---------
            //---------------------
            //266-275
            default:
            case 266:
                type = WEAPON;
                name = "Rock";
                picstring = "rock.gif";
                dpicstring = "drock.gif";
                weight = 1.0f;
                projtype = 2; //rock type
                throwpow = 2;
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Throw";
                function[0][1] = "n";
                power = new int[1];
                power[0] = 2;
                speed = new int[1];
                speed[0] = 8;
                level = new int[1];
                level[0] = 0;
                break;
            case 267:
                type = WEAPON;
                name = "Dagger";
                picstring = "dagger.gif";
                dpicstring = "ddagger.gif";
                throwpicstring = "dagger";
                weight = 0.5f;
                projtype = 5; //dagger type
                throwpow = 15;
                functions = 3;
                function = new String[functions][2];
                function[0][0] = "Slash";
                function[0][1] = "n";
                function[1][0] = "Stab";
                function[1][1] = "f";
                function[2][0] = "Throw";
                function[2][1] = "n";
                power = new int[3];
                power[0] = 5;
                power[1] = 6;
                power[2] = 3;
                speed = new int[3];
                speed[0] = 9;
                speed[1] = 10;
                speed[2] = 10;
                level = new int[3];
                level[0] = 0;
                level[1] = 1;
                level[2] = 0;
                break;
            case 268:
                type = WEAPON;
                name = "Poison Dart";
                picstring = "dart.gif";
                dpicstring = "ddart.gif";
                throwpicstring = "dart";
                weight = 0.3f;
                projtype = 3; //dart type
                throwpow = 12;
                poisonous = 3; //slightly poisonous
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Throw";
                function[0][1] = "n";
                function[1][0] = "Stab";
                function[1][1] = "f";
                power = new int[2];
                power[0] = 2;
                power[1] = 3;
                speed = new int[2];
                speed[0] = 8;
                speed[1] = 8;
                level = new int[2];
                level[0] = 0;
                level[1] = 1;
                break;
            case 269:
                type = WEAPON;
                name = "Throwing Star";
                picstring = "throwing_star.gif";
                dpicstring = "dthrowing_star.gif";
                throwpicstring = "throwing_star";
                weight = 0.1f;
                projtype = 4; //star type -> may not be necessary, unless have a star launching item...
                throwpow = 20;
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Throw";
                function[0][1] = "n";
                power = new int[1];
                power[0] = 6;
                speed = new int[1];
                speed[0] = 6;
                level = new int[1];
                level[0] = 0;
                break;
            case 270:
                type = WEAPON;
                name = "Arrow";
                picstring = "arrow.gif";
                dpicstring = "darrow.gif";
                throwpicstring = "arrow";
                weight = 0.2f;
                throwpow = 12;
                projtype = 1; //arrow type
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Stab";
                function[0][1] = "f";
                function[1][0] = "Throw";
                function[1][1] = "n";
                power = new int[2];
                power[0] = 2;
                power[1] = 2;
                speed = new int[2];
                speed[0] = 6;
                speed[1] = 12;
                level = new int[2];
                level[0] = 0;
                level[1] = 0;
                break;
            case 271:
                type = WEAPON;
                name = "Slayer Arrow";
                picstring = "slayer.gif";
                dpicstring = "dslayer.gif";
                throwpicstring = "slayer";
                weight = 0.2f;
                throwpow = 17;
                projtype = 1; //arrow type
                poisonous = 7;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Stab";
                function[0][1] = "f";
                function[1][0] = "Throw";
                function[1][1] = "n";
                power = new int[2];
                power[0] = 2;
                power[1] = 3;
                speed = new int[2];
                speed[0] = 6;
                speed[1] = 12;
                level = new int[2];
                level[0] = 1;
                level[1] = 0;
                break;
            
            //---------------------
            //------OTHER----------
            //---------------------
            //276+
            case 276:
                type = WEAPON;
                name = "Blue Box";
                picstring = "magic_box_blue.gif";
                dpicstring = "dbox_blue.gif";
                weight = 0.6f;
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Freeze";
                function[0][1] = "p";
                charges[0] = 1;
                power = new int[1];
                power[0] = 125;
                speed = new int[1];
                speed[0] = 14;
                level = new int[1];
                level[0] = 0;
                break;
            case 277:
                type = WEAPON;
                name = "Green Box";
                picstring = "magic_box_green.gif";
                dpicstring = "dbox_green.gif";
                weight = 0.9f;
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Freeze";
                function[0][1] = "p";
                charges[0] = 1;
                power = new int[1];
                power[0] = 225;
                speed = new int[1];
                speed[0] = 18;
                level = new int[1];
                level[0] = 0;
                break;
            case 278:
                type = WEAPON;
                name = "Eye of Time";
                picstring = "eye_time.gif";
                dpicstring = "dring.gif";
                usedupstring = "eye_time-dead.gif";
                weight = 0.1f;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Punch";
                function[0][1] = "f";
                function[1][0] = "Freeze Life";
                function[1][1] = "p";
                charges[1] = 10;
                power = new int[2];
                power[0] = 1;
                power[1] = 175;
                speed = new int[2];
                speed[0] = 5;
                speed[1] = 24;
                level = new int[2];
                level[0] = 0;
                level[1] = 0;
                break;
            case 279:
                type = WEAPON;
                name = "Horn of Fear";
                picstring = "horn_fear.gif";
                dpicstring = "dhorn.gif";
                weight = 0.8f;
                size = 1;
                functions = 1;
                function = new String[1][2];
                function[0][0] = "Blow Horn";
                function[0][1] = "p";
                charges[0] = -1;
                power = new int[1];
                power[0] = 6;//will run for 6 steps, if successfully frightened
                speed = new int[1];
                speed[0] = 12;
                level = new int[1];
                level[0] = 0;
                break;
            case 280:
                type = WEAPON;
                name = "Storm Ring";
                picstring = "storm_ring.gif";
                dpicstring = "dring.gif";
                usedupstring = "storm_ring-dead.gif";
                weight = 0.1f;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Punch";
                function[0][1] = "f";
                function[1][0] = "Bolt";
                function[1][1] = "w";
                charges[1] = 12;
                power = new int[2];
                power[0] = 1;
                power[1] = 5;
                speed = new int[2];
                speed[0] = 5;
                speed[1] = 20;
                level = new int[2];
                level[0] = 0;
                level[1] = 0;
                break;
            case 281:
                type = WEAPON;
                name = "Flamitt";
                picstring = "flamitt.gif";
                dpicstring = "dflamitt.gif";
                equipstring = "flamitt-on.gif";
                usedupstring = "flamitt-dead.gif";
                weight = 1.2f;
                size = 1;
                functions = 2;
                function = new String[functions][2];
                function[0][0] = "Punch";
                function[0][1] = "f";
                function[1][0] = "Fireball";
                function[1][1] = "w";
                charges[1] = 12;
                power = new int[2];
                power[0] = 3;
                power[1] = 5;
                speed = new int[2];
                speed[0] = 5;
                speed[1] = 16;
                level = new int[2];
                level[0] = 0;
                level[1] = 1;
                break;
            case 286:
                type = WEAPON;
                name = "Blue Box";
                picstring = "magic_box_blue.gif";
                dpicstring = "dbox_blue.gif";
                weight = 0.6f;
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Freeze Life";
                function[0][1] = "p";
                charges[0] = 1;
                power = new int[1];
                power[0] = 100;
                speed = new int[1];
                speed[0] = 14;
                level = new int[1];
                level[0] = 0;
                break;
            case 287:
                type = WEAPON;
                name = "Green Box";
                picstring = "magic_box_green.gif";
                dpicstring = "dbox_green.gif";
                weight = 0.9f;
                functions = 1;
                function = new String[functions][2];
                function[0][0] = "Freeze Life";
                function[0][1] = "p";
                charges[0] = 1;
                power = new int[1];
                power[0] = 200;
                speed = new int[1];
                speed[0] = 18;
                level = new int[1];
                level[0] = 0;
                break;
            //---------------------
            //------RUNES----------
            //---------------------
            case 282:
                type = SHIELD;
                name = "Fire Rune";
                picstring = "fire_rune.gif";
                dpicstring = "dfire_rune.gif";
                weight = 0.1f;
                haseffect = true;
                effect = new String[1];
                effect[0] = "strength,4";
                break;
            case 283:
                type = SHIELD;
                name = "Water Rune";
                picstring = "water_rune.gif";
                dpicstring = "dwater_rune.gif";
                weight = 0.1f;
                haseffect = true;
                effect = new String[1];
                effect[0] = "vitality,4";
                break;
            case 284:
                type = SHIELD;
                name = "Earth Rune";
                picstring = "earth_rune.gif";
                dpicstring = "dearth_rune.gif";
                weight = 0.1f;
                defense = 4;
                break;
            case 285:
                type = SHIELD;
                name = "Wind Rune";
                picstring = "wind_rune.gif";
                dpicstring = "dwind_rune.gif";
                weight = 0.1f;
                haseffect = true;
                effect = new String[1];
                effect[0] = "dexterity,4";
                break;
            //300+ for custom items
        }
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
        if (!equipstring.equals("")) {
            epic = (Image) pics.get(equipstring);
            if (epic == null) {
                epic = tk.createImage("Items" + File.separator + equipstring);
                pics.put(equipstring, epic);
                ImageTracker.addImage(epic, 0);
            }
        }
        if (!usedupstring.equals("")) {
            upic = (Image) pics.get(usedupstring);
            if (upic == null) {
                upic = tk.createImage("Items" + File.separator + usedupstring);
                pics.put(usedupstring, upic);
                ImageTracker.addImage(upic, 0);
            }
        }
        if (!throwpicstring.equals("")) {
            hasthrowpic = true;
            throwpic = new Image[4];
            throwpic[0] = (Image) pics.get(throwpicstring + "-away.gif");
            if (throwpic[0] == null) {
                throwpic[0] = tk.createImage("Items" + File.separator + throwpicstring + "-away.gif");
                ImageTracker.addImage(throwpic[0], 0);
            }
            throwpic[1] = (Image) pics.get(throwpicstring + "-toward.gif");
            if (throwpic[1] == null) {
                throwpic[1] = tk.createImage("Items" + File.separator + throwpicstring + "-toward.gif");
                ImageTracker.addImage(throwpic[1], 0);
            }
            throwpic[2] = (Image) pics.get(throwpicstring + "-left.gif");
            if (throwpic[2] == null) {
                throwpic[2] = tk.createImage("Items" + File.separator + throwpicstring + "-left.gif");
                ImageTracker.addImage(throwpic[2], 0);
            }
            throwpic[3] = (Image) pics.get(throwpicstring + "-right.gif");
            if (throwpic[3] == null) {
                throwpic[3] = tk.createImage("Items" + File.separator + throwpicstring + "-right.gif");
                ImageTracker.addImage(throwpic[3], 0);
            }
            
        }
    }
    
    public Item(int num, int potpow, int cpow) {
        number = num;
        potionpow = potpow; //redundant for bombs
        potioncastpow = cpow;
        ispotion = true; //redundant for bombs
        type = WEAPON;
        //type = OTHER;
        throwpow = 1; //redundant for bombs
        weight = 0.3f;
        size = 1;
        xoffset = randGen.nextInt() % 5;
        yoffset = randGen.nextInt() % 5;
        
        switch (number) {
            case 10:
                name = "Health Potion";
                picstring = "potion_health.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 11:
                name = "Stamina Potion";
                picstring = "potion_stamina.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 12:
                name = "Mana Potion";
                picstring = "potion_mana.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 13:
                name = "Strength Potion";
                picstring = "potion_str.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 14:
                name = "Dexterity Potion";
                picstring = "potion_dex.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 15:
                name = "Vitality Potion";
                picstring = "potion_vit.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 16:
                name = "Intelligence Potion";
                picstring = "potion_int.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 17:
                name = "Wisdom Potion";
                picstring = "potion_wis.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 18:
                name = "Shield Potion";
                picstring = "potion_shield.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 19:
                name = "Resistance Potion";
                picstring = "potion_resist.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 20:
                name = "Anti-Venom";//anti-ven
                picstring = "potion_ven.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 21:
                name = "Ful Bomb";
                picstring = "ful_bomb.gif";
                dpicstring = "dful_bomb.gif";
                isbomb = true;
                bombnum = potioncastpow + "46";
                break;
            case 22:
                name = "Ven Bomb";
                picstring = "ven_bomb.gif";
                dpicstring = "dven_bomb.gif";
                isbomb = true;
                bombnum = potioncastpow + "61";
                break;
            case 23:
                name = "Bolt Bomb";
                picstring = "bolt_bomb.gif";
                dpicstring = "dbolt_bomb.gif";
                isbomb = true;
                bombnum = potioncastpow + "365";
                break;
            case 24:
                name = "Anti-Silence";
                picstring = "potion_silence.gif";
                dpicstring = "dwater_flask.gif";
                break;
            case 25:
                name = "Remove Curse";
                picstring = "potion_curse.gif";
                dpicstring = "dwater_flask.gif";
                break;
        }
        functions = 1;
        function = new String[functions][2];
        speed = new int[functions];
        level = new int[functions];
        level[0] = 0;
        power = new int[functions];
        power[0] = 0;
        if (!isbomb) {
            function[0][0] = "Drink";
            function[0][1] = "-";
            speed[0] = 5;
        } else {
            function[0][0] = "Throw";
            function[0][1] = "n";
            speed[0] = 12;
        }
        
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
    
    //scrolls
    public Item(String[] mes) {
        number = 4;
        xoffset = randGen.nextInt() % 5;
        yoffset = randGen.nextInt() % 5;
        type = OTHER;
        name = "Scroll";
        size = 1;
        weight = 0.5f;
        throwpow = 1;
        scroll = mes;
        picstring = "scroll.gif";
        dpicstring = "dscroll.gif";
        equipstring = "scroll-open.gif";
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
        epic = (Image) pics.get(equipstring);
        if (epic == null) {
            epic = tk.createImage("Items" + File.separator + equipstring);
            pics.put(equipstring, epic);
            ImageTracker.addImage(epic, 0);
        }
    }
    
    //bones
    public Item(String nam, int num) {
        number = num;
        xoffset = randGen.nextInt() % 5;
        yoffset = randGen.nextInt() % 5;
        type = OTHER;
        name = nam + " Bones";
        weight = 5.0f;
        size = 4;
        throwpow = 1;
        picstring = "bones.gif";
        dpicstring = "dbones.gif";
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
    
    public static void doFlaskBones() {
        Image tempimg;
        if (!pics.containsKey("flask.gif")) {
            tempimg = tk.createImage("Items" + File.separator + "flask.gif");
            pics.put("flask.gif", tempimg);
            ImageTracker.addImage(tempimg, 0);
            if (!pics.containsKey("dflask.gif")) {
                tempimg = tk.createImage("Items" + File.separator + "dflask.gif");
                pics.put("dflask.gif", tempimg);
                ImageTracker.addImage(tempimg, 0);
            }
        }
        if (!pics.containsKey("dbones.gif")) {
            tempimg = tk.createImage("Items" + File.separator + "dbones.gif");
            pics.put("dbones.gif", tempimg);
            ImageTracker.addImage(tempimg, 0);
        }
        if (!pics.containsKey("potion_health.gif")) {
            tempimg = tk.createImage("Items" + File.separator + "potion_health.gif");
            pics.put("potion_health.gif", tempimg);
            ImageTracker.addImage(tempimg, 0);
        }
        if (!pics.containsKey("potion_stamina.gif")) {
            tempimg = tk.createImage("Items" + File.separator + "potion_stamina.gif");
            pics.put("potion_stamina.gif", tempimg);
            ImageTracker.addImage(tempimg, 0);
        }
        if (!pics.containsKey("dwater_flask.gif")) {
            tempimg = tk.createImage("Items" + File.separator + "dwater_flask.gif");
            pics.put("dwater_flask.gif", tempimg);
            ImageTracker.addImage(tempimg, 0);
        }
        if (!pics.containsKey("fistfoot.gif")) {
            tempimg = tk.createImage("Items" + File.separator + "fistfoot.gif");
            pics.put("fistfoot.gif", tempimg);
            ImageTracker.addImage(tempimg, 0);
        }
    }
    
    public String toString() {
        return name;
    }
    
    public boolean equals(Object ob) {
        //if (ob instanceof Item && number==((Item)ob).number) return true;
        if (ob instanceof Item && name.equals(((Item) ob).name) && type == ((Item) ob).type) return true;
        else return false;
    }
    
    public static Item createCopy(Item it) {
        Item tempitem;
        if (it.number == 7 || (it.number > 30 && it.number != 73 && it.number < 300)) tempitem = new Item(it.number);
        else if (it.number > 9 && it.number < 31) {
            //potions
            tempitem = new Item(it.number, it.potionpow, it.potioncastpow);
        } else if (it.number == 4) {
            //scroll
            String[] mess = new String[5];
            mess[0] = new String(it.scroll[0]);
            mess[1] = new String(it.scroll[1]);
            mess[2] = new String(it.scroll[2]);
            mess[3] = new String(it.scroll[3]);
            mess[4] = new String(it.scroll[4]);
            tempitem = new Item(mess);
        } else if (it.number == 8) {
            //compass
            tempitem = new Compass();
        } else if (it.number == 9) {
            //torch
            tempitem = new Torch();
            ((Torch) tempitem).lightboost = ((Torch) it).lightboost;
        } else if (it.number == 73) {
            //waterskin
            tempitem = new Waterskin(((Waterskin) it).drinks);
        } else if (it.number == 5) {
            //chest
            tempitem = new Chest();
            for (int i = 0; i < 12; i++) {
                if (((Chest) it).itemAt(i) != null) ((Chest) tempitem).putItem(i, createCopy(((Chest) it).itemAt(i)));
            }
        } else {
            //custom
            tempitem = new Item();
            tempitem.number = it.number;
        }
        tempitem.name = new String(it.name);
        tempitem.type = it.type;
        tempitem.weight = it.weight;
        tempitem.size = it.size;
        tempitem.throwpow = it.throwpow;
        tempitem.cursed = it.cursed;
        tempitem.subsquare = it.subsquare;
        tempitem.picstring = new String(it.picstring);
        //tempitem.pic = tk.createImage("Items"+File.separator+it.picstring);
        tempitem.pic = it.pic;
        tempitem.dpicstring = new String(it.dpicstring);
        //tempitem.dpic = tk.createImage("Items"+File.separator+it.dpicstring);
        tempitem.dpic = it.dpic;
        if (!it.equipstring.equals("")) {
            tempitem.equipstring = new String(it.equipstring);
            //tempitem.epic = tk.createImage("Items"+File.separator+it.equipstring);
            tempitem.epic = it.epic;
            if (it.temppic != null) tempitem.temppic = it.temppic;
        } else {
            tempitem.equipstring = "";
            tempitem.epic = null;
        }
        if (!it.usedupstring.equals("")) {
            tempitem.usedupstring = new String(it.usedupstring);
            //tempitem.upic = tk.createImage("Items"+File.separator+it.usedupstring);
            tempitem.upic = it.upic;
        } else {
            tempitem.usedupstring = "";
            tempitem.upic = null;
        }
        tempitem.hasthrowpic = it.hasthrowpic;
        if (tempitem.hasthrowpic) {
            tempitem.throwpicstring = new String(it.throwpicstring);
            tempitem.throwpic = new Image[4];
            //tempitem.throwpic[0] = tk.createImage("Items"+File.separator+it.throwpicstring+"-away.gif");
            //tempitem.throwpic[1] = tk.createImage("Items"+File.separator+it.throwpicstring+"-toward.gif");
            //tempitem.throwpic[2] = tk.createImage("Items"+File.separator+it.throwpicstring+"-left.gif");
            //tempitem.throwpic[3] = tk.createImage("Items"+File.separator+it.throwpicstring+"-right.gif");
            tempitem.throwpic[0] = it.throwpic[0];
            tempitem.throwpic[1] = it.throwpic[1];
            tempitem.throwpic[2] = it.throwpic[2];
            tempitem.throwpic[3] = it.throwpic[3];
        }
        tempitem.defense = it.defense;
        tempitem.magicresist = it.magicresist;
        tempitem.poisonous = it.poisonous;
        tempitem.haseffect = it.haseffect;
        if (tempitem.haseffect) {
            int numeffects = it.effect.length;
            tempitem.effect = new String[numeffects];
            for (int i = 0; i < numeffects; i++) {
                tempitem.effect[i] = new String(it.effect[i]);
            }
        }
        if (tempitem.type == FOOD) tempitem.foodvalue = it.foodvalue;
        else if (tempitem.type == WEAPON) {
            tempitem.hitsImmaterial = it.hitsImmaterial;
            tempitem.projtype = it.projtype;
            tempitem.functions = it.functions;
            tempitem.function = new String[tempitem.functions][2];
            tempitem.level = new int[tempitem.functions];
            tempitem.power = new int[tempitem.functions];
            tempitem.speed = new int[tempitem.functions];
            for (int i = 0; i < tempitem.functions; i++) {
                tempitem.function[i][0] = new String(it.function[i][0]);
                tempitem.function[i][1] = new String(it.function[i][1]);
                tempitem.level[i] = it.level[i];
                tempitem.power[i] = it.power[i];
                tempitem.speed[i] = it.speed[i];
                tempitem.charges[i] = it.charges[i];
            }
        }
        return tempitem;
    }
    
    public void equipEffect(dmnew.Hero h) {
        if (epic != null) {
            temppic = pic;
            pic = epic;
        }
        h.defense += defense;
        h.magicresist += magicresist;
        if (!haseffect) return;
        String whataffected;
        int e = 0;
        for (int i = effect.length; i > 0; i--) {
            whataffected = effect[i - 1].substring(0, effect[i - 1].indexOf(','));
            e = Integer.parseInt(effect[i - 1].substring(effect[i - 1].indexOf(',') + 1));
            whataffected.toLowerCase();
            if (whataffected.equals("mana")) {
                if (h.maxmana + e >= 0) {
                    h.maxmana += e;
                    madded = e;
                } else {
                    madded = 0 - h.maxmana;
                    h.maxmana = 0;
                }
                if (e < 0 && h.mana > h.maxmana) {
                    if (h.mana + e >= 0) h.mana += e;
                    else h.mana = 0;
                }
            } else if (whataffected.equals("health")) {
                if (h.maxhealth + e > 0) {
                    h.maxhealth += e;
                    hadded = e;
                } else {
                    hadded = 1 - h.maxhealth;
                    h.maxhealth = 1;
                }
                if (h.health > h.maxhealth) h.health = h.maxhealth;
                                /*
                                if (e<0) {
                                        if (h.health+e>0) h.health+=e;
                                        else h.health=1;
                                }
                                */
            } else if (whataffected.equals("stamina")) {
                if (h.maxstamina + e > 9) {
                    h.maxstamina += e;
                    sadded = e;
                } else {
                    sadded = 10 - h.maxstamina;
                    h.maxstamina = 10;
                }
                if (h.stamina > h.maxstamina) h.stamina = h.maxstamina;
                                /*
                                if (e<0) {
                                        if (h.stamina+e>0) h.stamina+=e;
                                        else h.stamina=1;
                                }
                                */
            } else if (whataffected.equals("strength")) {
                h.strength -= h.strengthboost;
                if (h.strength + e > 0) {
                    h.strength += e;
                    stradded = e;
                } else {
                    stradded = 1 - h.strength;
                    h.strength = 1;
                }
                if (h.strength + h.strengthboost <= 0) h.strengthboost = 1 - h.strength;
                h.strength += h.strengthboost;
            } else if (whataffected.equals("vitality")) {
                h.vitality -= h.vitalityboost;
                if (h.vitality + e > 0) {
                    h.vitality += e;
                    vitadded = e;
                } else {
                    vitadded = 1 - h.vitality;
                    h.vitality = 1;
                }
                if (h.vitality + h.vitalityboost <= 0) h.vitalityboost = 1 - h.vitality;
                h.vitality += h.vitalityboost;
            } else if (whataffected.equals("dexterity")) {
                h.dexterity -= h.dexterityboost;
                if (h.dexterity + e > 0) {
                    h.dexterity += e;
                    dexadded = e;
                } else {
                    dexadded = 1 - h.dexterity;
                    h.dexterity = 1;
                }
                if (h.dexterity + h.dexterityboost <= 0) h.dexterityboost = 1 - h.dexterity;
                h.dexterity += h.dexterityboost;
            } else if (whataffected.equals("intelligence")) {
                h.intelligence -= h.intelligenceboost;
                if (h.intelligence + e > 0) {
                    h.intelligence += e;
                    intadded = e;
                } else {
                    intadded = 1 - h.intelligence;
                    h.intelligence = 1;
                }
                if (h.intelligence + h.intelligenceboost <= 0) h.intelligenceboost = 1 - h.intelligence;
                h.intelligence += h.intelligenceboost;
            } else if (whataffected.equals("wisdom")) {
                h.wisdom -= h.wisdomboost;
                if (h.wisdom + e > 0) {
                    h.wisdom += e;
                    wisadded = e;
                } else {
                    wisadded = 1 - h.wisdom;
                    h.wisdom = 1;
                }
                if (h.wisdom + h.wisdomboost <= 0) h.wisdomboost = 1 - h.wisdom;
                h.wisdom += h.wisdomboost;
            } else if (whataffected.equals("flevel")) {
                if (h.flevel + e >= 0 && h.flevel + e < 15) {
                    h.flevel += e;
                    fleveladded = e;
                } else if (h.flevel + e < 0) {
                    fleveladded = 0 - h.flevel;
                    h.flevel = 0;
                } else {
                    fleveladded = 15 - h.flevel;
                    h.flevel = 15;
                }
            } else if (whataffected.equals("nlevel")) {
                if (h.nlevel + e >= 0 && h.nlevel + e < 15) {
                    h.nlevel += e;
                    nleveladded = e;
                } else if (h.nlevel + e < 0) {
                    nleveladded = 0 - h.nlevel;
                    h.nlevel = 0;
                } else {
                    nleveladded = 15 - h.nlevel;
                    h.nlevel = 15;
                }
            } else if (whataffected.equals("wlevel")) {
                if (h.wlevel + e >= 0 && h.wlevel + e < 15) {
                    h.wlevel += e;
                    wleveladded = e;
                } else if (h.wlevel + e < 0) {
                    wleveladded = 0 - h.wlevel;
                    h.wlevel = 0;
                } else {
                    wleveladded = 15 - h.wlevel;
                    h.wlevel = 15;
                }
            } else if (whataffected.equals("plevel")) {
                if (h.plevel + e >= 0 && h.plevel + e < 15) {
                    h.plevel += e;
                    pleveladded = e;
                } else if (h.plevel + e < 0) {
                    pleveladded = 0 - h.plevel;
                    h.plevel = 0;
                } else {
                    pleveladded = 15 - h.plevel;
                    h.plevel = 15;
                }
            }
        }
        h.setMaxLoad();
                /*
                h.maxload=h.strength*4/5;
                //if (h.stamina<h.maxstamina/5) h.maxload*=3/4;
                if (h.stamina<h.maxstamina/5) h.maxload=h.maxload*1/2;
                else if (h.stamina<h.maxstamina/4) h.maxload=h.maxload*2/3;
                else if (h.stamina<h.maxstamina/3) h.maxload=h.maxload*3/4;
                */
    }
    
    public void unEquipEffect(dmnew.Hero h) {
        if (epic != null) {
            pic = temppic;
        }
        h.defense -= defense;
        h.magicresist -= magicresist;
        if (!haseffect) return;
        String whataffected;
        //int e = 0;
        for (int i = effect.length; i > 0; i--) {
            whataffected = effect[i - 1].substring(0, effect[i - 1].indexOf(','));
            //e = Integer.parseInt(effect[i-1].substring(effect[i-1].indexOf(',')+1));
            whataffected.toLowerCase();
            if (whataffected.equals("mana")) {
                //h.maxmana-=e;
                //if (h.mana>h.maxmana) h.mana=h.maxmana;
                h.maxmana -= madded;
            } else if (whataffected.equals("health")) {
                //h.maxhealth-=e;
                h.maxhealth -= hadded;
                if (h.health > h.maxhealth) h.health = h.maxhealth;
            } else if (whataffected.equals("stamina")) {
                //h.maxstamina-=e;
                h.maxstamina -= sadded;
                if (h.stamina > h.maxstamina) h.stamina = h.maxstamina;
            } else if (whataffected.equals("strength")) {
                //h.strength-=e;
                h.strength -= h.strengthboost;
                h.strength -= stradded;
                if (h.strength + h.strengthboost <= 0) h.strengthboost = 1 - h.strength;
                h.strength += h.strengthboost;
            } else if (whataffected.equals("vitality")) {
                //h.vitality-=e;
                h.vitality -= h.vitalityboost;
                h.vitality -= vitadded;
                if (h.vitality + h.vitalityboost <= 0) h.vitalityboost = 1 - h.vitality;
                h.vitality += h.vitalityboost;
            } else if (whataffected.equals("dexterity")) {
                //h.dexterity-=e;
                h.dexterity -= h.dexterityboost;
                h.dexterity -= dexadded;
                if (h.dexterity + h.dexterityboost <= 0) h.dexterityboost = 1 - h.dexterity;
                h.dexterity += h.dexterityboost;
            } else if (whataffected.equals("intelligence")) {
                //h.intelligence-=e;
                h.intelligence -= h.intelligenceboost;
                h.intelligence -= intadded;
                if (h.intelligence + h.intelligenceboost <= 0) h.intelligenceboost = 1 - h.intelligence;
                h.intelligence += h.intelligenceboost;
            } else if (whataffected.equals("wisdom")) {
                //h.wisdom-=e;
                h.wisdom -= h.wisdomboost;
                h.wisdom -= wisadded;
                if (h.wisdom + h.wisdomboost <= 0) h.wisdomboost = 1 - h.wisdom;
                h.wisdom += h.wisdomboost;
            }
            //else if (whataffected.equals("load")) h.maxload-=e;
            else if (whataffected.equals("flevel")) {
                h.flevel -= fleveladded;
            } else if (whataffected.equals("nlevel")) {
                h.nlevel -= nleveladded;
            } else if (whataffected.equals("wlevel")) {
                h.wlevel -= wleveladded;
            } else if (whataffected.equals("plevel")) {
                h.plevel -= pleveladded;
            }
        }
        h.setMaxLoad();
                /*
                h.maxload=h.strength*4/5;
                //if (h.stamina<h.maxstamina/5) h.maxload*=3/4;
                if (h.stamina<h.maxstamina/5) h.maxload=h.maxload*1/2;
                else if (h.stamina<h.maxstamina/4) h.maxload=h.maxload*2/3;
                else if (h.stamina<h.maxstamina/3) h.maxload=h.maxload*3/4;
                */
    }
    
    public void foodEffect(dmnew.Hero h) {
        //put defense/magicresist/poison here
        if (defense != 0) {
            if (defense > 0 && defense > h.defenseboost) {
                h.defense -= h.defenseboost;
                h.defenseboost = defense;
                h.defense += defense;
            } else if (defense < 0) {
                h.defenseboost -= defense;
                h.defense -= defense;
            }
        }
        if (magicresist != 0) {
            if (magicresist > 0 && magicresist > h.magicresistboost) {
                h.magicresist -= h.magicresistboost;
                h.magicresistboost = magicresist;
                h.magicresist += magicresist;
            } else if (magicresist < 0) {
                h.magicresistboost -= magicresist;
                h.magicresist -= magicresist;
            }
        }
        if (poisonous != 0) {
            h.poison += poisonous;
            if (h.poison <= 0) {
                h.poison = 0;
                h.ispoisoned = false;
            } else h.ispoisoned = true;
        }
        if (!haseffect) return;
        String whataffected;
        int e = 0;
        for (int i = effect.length; i > 0; i--) {
            whataffected = effect[i - 1].substring(0, effect[i - 1].indexOf(','));
            e = Integer.parseInt(effect[i - 1].substring(effect[i - 1].indexOf(',') + 1));
            whataffected.toLowerCase();
            if (whataffected.equals("mana")) h.energize(h.maxmana * e / 100);
            else if (whataffected.equals("health")) h.heal(h.maxhealth * e / 100);
            else if (whataffected.equals("stamina")) h.vitalize(h.maxstamina * e / 100);
            else if (whataffected.equals("strength")) {
                if (h.strength < 151) {
                    h.strengthboost += e;
                    h.strength += e;
                }
            } else if (whataffected.equals("vitality")) {
                if (h.vitality < 151) {
                    h.vitalityboost += e;
                    h.vitality += e;
                }
            } else if (whataffected.equals("dexterity")) {
                if (h.dexterity < 151) {
                    h.dexterityboost += e;
                    h.dexterity += e;
                }
            } else if (whataffected.equals("intelligence")) {
                if (h.intelligence < 151) {
                    h.intelligenceboost += e;
                    h.intelligence += e;
                }
            } else if (whataffected.equals("wisdom")) {
                if (h.wisdom < 151) {
                    h.wisdomboost += e;
                    h.wisdom += e;
                }
            } else if (whataffected.equals("flevel")) {
                if (h.flevel + e < 15) {
                    h.flevel += e;
                    h.flevelboost += e;
                } else {
                    h.flevelboost = 15 - h.flevel;
                    h.flevel = 15;
                }
                h.levelboostchange = 3;
            } else if (whataffected.equals("nlevel")) {
                if (h.nlevel + e < 15) {
                    h.nlevel += e;
                    h.nlevelboost += e;
                } else {
                    h.nlevelboost = 15 - h.nlevel;
                    h.nlevel = 15;
                }
                h.levelboostchange = 3;
            } else if (whataffected.equals("wlevel")) {
                if (h.wlevel + e < 15) {
                    h.wlevel += e;
                    h.wlevelboost += e;
                } else {
                    h.wlevelboost = 15 - h.wlevel;
                    h.wlevel = 15;
                }
                h.levelboostchange = 3;
            } else if (whataffected.equals("plevel")) {
                if (h.plevel + e < 15) {
                    h.plevel += e;
                    h.plevelboost += e;
                } else {
                    h.plevelboost = 15 - h.plevel;
                    h.plevel = 15;
                }
                h.levelboostchange = 3;
            }
        }
        h.setMaxLoad();
    }
    
    private void writeObject(ObjectOutputStream s) throws IOException {
        //update items
                /*
                if (type==WEAPON) {
                        Item tempitem;
                        if (number==9) tempitem = new Torch();
                        else tempitem = new Item(number);
                        for (int i=0;i<functions;i++) power[i]=tempitem.power[i];
                        //for (int i=0;i<effect.length;i++) {
                        //        effect[i] = new String(tempitem.effect[i]);
                        //}
                }
                */
        //end update
        try {
            s.writeInt(type);
            s.writeInt(size);
            s.writeFloat(weight);
            s.writeInt(number);
            s.writeUTF(name);
            s.writeUTF(picstring);
            s.writeUTF(dpicstring);
            s.writeUTF(equipstring);
            s.writeUTF(usedupstring);
            if (epic != null && pic == epic) s.writeInt(1);
            else if (upic != null && pic == upic && epic == upic) s.writeInt(2);
            else s.writeInt(0);
            s.writeInt(throwpow);
            s.writeInt(shotpow);
            if (type == WEAPON) {
                s.writeInt(functions);
                s.writeObject(function);
                s.writeObject(power);
                s.writeObject(speed);
                s.writeObject(level);
                s.writeObject(charges);
                s.writeBoolean(hitsImmaterial);
                //s.writeInt(poisonous);
                s.writeInt(projtype);
            }
            if (type != OTHER) {
                s.writeInt(defense);
                s.writeInt(magicresist);
                if (type != FOOD) {
                    s.writeInt(cursed);
                    s.writeBoolean(cursefound);
                }
            }
            if (type == FOOD || number == 72 || number == 73) s.writeInt(foodvalue);
            if (type == WEAPON || type == FOOD) s.writeInt(poisonous);
            s.writeBoolean(ispotion);
            if (ispotion) {
                s.writeInt(potionpow);
                s.writeInt(potioncastpow);
                s.writeBoolean(isbomb);
                if (isbomb) s.writeUTF(bombnum);
            }
            s.writeBoolean(hasthrowpic);
            if (hasthrowpic) s.writeUTF(throwpicstring);
            if (number == 4) s.writeObject(scroll);
            else if (number == 83) s.writeObject(bound);
            s.writeBoolean(haseffect);
            if (haseffect) {
                s.writeObject(effect);
                s.writeInt(fleveladded);
                s.writeInt(nleveladded);
                s.writeInt(wleveladded);
                s.writeInt(pleveladded);
                s.writeInt(stradded);
                s.writeInt(dexadded);
                s.writeInt(vitadded);
                s.writeInt(intadded);
                s.writeInt(wisadded);
                s.writeInt(hadded);
                s.writeInt(sadded);
                s.writeInt(madded);
            }
            s.writeInt(subsquare);
        } catch (Exception e) {
            System.out.println("Error in Item write - " + name);
            e.printStackTrace();
        }
    }
    
    private void readObject(ObjectInputStream s) throws IOException {
        try {
            type = s.readInt();
            size = s.readInt();
            weight = s.readFloat();
            number = s.readInt();
            name = s.readUTF();
            picstring = s.readUTF();
            dpicstring = s.readUTF();
            equipstring = s.readUTF();
            usedupstring = s.readUTF();
            int status = s.readInt();
            throwpow = s.readInt();
            shotpow = s.readInt();
            if (type == WEAPON) {
                functions = s.readInt();
                function = (String[][]) s.readObject();
                power = (int[]) s.readObject();
                speed = (int[]) s.readObject();
                level = (int[]) s.readObject();
                charges = (int[]) s.readObject();
                hitsImmaterial = s.readBoolean();
                if (PATCHING) {
                    poisonous = s.readInt();
                    //fix shield/spellshield function
                    for (int i = 0; i < functions; i++) {
                        if (function[i][0].equals("Shield") || function[i][0].equals("SpellShield"))
                            function[i][0] += " Party";
                    }
                }
                projtype = s.readInt();
            }
            if (type != OTHER) {
                if (type != FOOD || !PATCHING) {
                    defense = s.readInt();
                    magicresist = s.readInt();
                }
            }
            if (!PATCHING && type != OTHER && type != FOOD) {
                cursed = s.readInt();
                ///*
                //cursefound goes here, PATCHING2 is temp update flag
                cursefound = s.readBoolean(); //final version
                //if (!PATCHING2) cursefound = s.readBoolean();
                //*/
            }
            if (type == FOOD || number == 72 || number == 73) foodvalue = s.readInt();
            if (!PATCHING && (type == WEAPON || type == FOOD)) poisonous = s.readInt();
            ispotion = s.readBoolean();
            if (ispotion) {
                potionpow = s.readInt();
                potioncastpow = s.readInt();
                isbomb = s.readBoolean();
                if (isbomb) bombnum = s.readUTF();
            }
            
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
            if (!equipstring.equals("")) {
                epic = (Image) pics.get(equipstring);
                if (epic == null) {
                    epic = tk.createImage("Items" + File.separator + equipstring);
                    pics.put(equipstring, epic);
                    ImageTracker.addImage(epic, 0);
                }
            }
            if (!usedupstring.equals("")) {
                upic = (Image) pics.get(usedupstring);
                if (upic == null) {
                    upic = tk.createImage("Items" + File.separator + usedupstring);
                    pics.put(usedupstring, upic);
                    ImageTracker.addImage(upic, 0);
                }
            }
            hasthrowpic = s.readBoolean();
            if (hasthrowpic) {
                throwpicstring = s.readUTF();
                throwpic = new Image[4];
                throwpic[0] = (Image) pics.get(throwpicstring + "-away.gif");
                if (throwpic[0] == null) {
                    throwpic[0] = tk.createImage("Items" + File.separator + throwpicstring + "-away.gif");
                    ImageTracker.addImage(throwpic[0], 0);
                }
                throwpic[1] = (Image) pics.get(throwpicstring + "-toward.gif");
                if (throwpic[1] == null) {
                    throwpic[1] = tk.createImage("Items" + File.separator + throwpicstring + "-toward.gif");
                    ImageTracker.addImage(throwpic[1], 0);
                }
                throwpic[2] = (Image) pics.get(throwpicstring + "-left.gif");
                if (throwpic[2] == null) {
                    throwpic[2] = tk.createImage("Items" + File.separator + throwpicstring + "-left.gif");
                    ImageTracker.addImage(throwpic[2], 0);
                }
                throwpic[3] = (Image) pics.get(throwpicstring + "-right.gif");
                if (throwpic[3] == null) {
                    throwpic[3] = tk.createImage("Items" + File.separator + throwpicstring + "-right.gif");
                    ImageTracker.addImage(throwpic[3], 0);
                }
            } else throwpicstring = "";
            if (number == 4) scroll = (String[]) s.readObject();
            else if (number == 83) bound = (boolean[]) s.readObject();
            haseffect = s.readBoolean();
            if (haseffect) {
                effect = (String[]) s.readObject();
                fleveladded = s.readInt();
                nleveladded = s.readInt();
                wleveladded = s.readInt();
                pleveladded = s.readInt();
                ///*
                if (!PATCHING) { //for final version
                    //if (!PATCHING2) { //for temp update of my current maps
                    stradded = s.readInt();
                    dexadded = s.readInt();
                    vitadded = s.readInt();
                    intadded = s.readInt();
                    wisadded = s.readInt();
                    hadded = s.readInt();
                    sadded = s.readInt();
                    madded = s.readInt();
                }
                //*/
            }
            subsquare = s.readInt();
            
            if (number != 215) {
                xoffset = randGen.nextInt() % 5;
                yoffset = randGen.nextInt() % 5;
            } else {
                xoffset = 0;
                yoffset = 0;
            }
            //if (number>maxitemnum) maxitemnum = number;
            if (status == 1) {
                temppic = pic;
                pic = epic;
            } else if (status == 2) {
                temppic = upic;
                epic = upic;
                pic = upic;
            }
        } catch (Exception e) {
            System.out.println("Error in Item read - " + name);
            e.printStackTrace();
        }
    }
    
    
}
