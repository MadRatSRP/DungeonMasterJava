import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

class MonsterWizard extends JDialog implements ActionListener, MouseListener { //,ListSelectionListener {
    
    static final String[] monsternames = {"Mummy", "Screamer", "Giggler", "Rock Pile", "Slime", "Wing Eye", "Ghost", "Muncher", "Skeleton", "Worm", "Fire Elemental", "Water Elemental", "Goblin", "Giant Rat", "Ant Man", "Beholder", "Couatyl", "Fader", "Tentacle Beast", "Scorpion", "Demon", "Deth Knight", "Spider", "Stone Golem", "Sorcerer", "Dragon", "Lord Chaos", "Demon Lord"};
    //"Mummy","Screamer","Giggler","Rock Pile","Slime","Wing Eye","Ghost","Muncher","Skeleton","Worm","Fire Elemental",
    //"Water Elemental","Goblin","Giant Rat","Ant Man","Beholder","Couatyl","Fader","Tentacle Beast","Scorpion","Demon",
    //"Deth Knight","Spider","Stone Golem","Sorcerer","Dragon","Lord Chaos","Demon Lord"
    static final int[] defaulthealth = {100, 120, 60, 120, 110, 150, 150, 40, 300, 250, 540, 440, 200, 320, 450, 180, 380, 240, 300, 400, 500, 650, 600, 1040, 640, 2700, 10000, 8000, 1};
    static final int[] defaultmana = {0, 0, 0, 0, 0, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 500, 0, 1500, 500, 0, 1000, 0, 500, 0, 5000, 0, 10000, 10000, 0};
    //static final int[] defaultpower =  { 12 ,12 ,1  ,20 ,15 ,10 ,27 ,6 ,35 ,44 ,60 ,40 ,50 ,60 ,60 ,24  ,65 ,35  ,55  ,70  ,80  ,90 ,90  ,100 ,70   ,180 ,140  ,200  ,1  };
    static final int[] defaultpower = {12, 12, 1, 20, 15, 10, 27, 6, 40, 40, 70, 50, 35, 45, 70, 24, 65, 35, 55, 70, 85, 140, 100, 150, 70, 210, 150, 200, 1};
    static final int[] defaultdefense = {0, 0, 10, 65, 40, 10, 20, 50, 20, 15, 30, 25, 30, 30, 40, 10, 20, 30, 40, 45, 50, 75, 35, 95, 20, 50, 50, 50, 0};
    static final int[] defaultmresist = {0, 0, 10, 30, 0, 30, 5, 0, 10, 10, 20, 20, 15, 20, 30, 30, 20, 30, 20, 10, 40, 90, 20, 90, 90, 40, 50, 50, 0};
    static final int[] defaultspeed = {55, 50, 140, 50, 60, 75, 70, 95, 70, 65, 75, 70, 75, 70, 70, 80, 75, 70, 70, 70, 75, 75, 75, 70, 80, 80, 100, 100, 50};
    static final int[] defaultpoison = {0, 0, 0, 4, 2, 0, 0, 8, 0, 2, 0, 0, 0, 0, 0, 0, 8, 0, 3, 8, 0, 0, 12, 0, 0, 0, 0, 0, 0};
    static final int[] defaultfresist = {9, 8, 10, 5, 8, 2, 0, 10, 3, 4, 0, 0, 5, 2, 4, 3, 5, 0, 5, 4, 1, 1, 2, 3, 1, 1, 0, 0, 5};
    static final int[] defaultcpower = {0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 5, 4, 0, 4, 0, 3, 0, 5, 5, 5, 5, 0};
    static final int[] defaultmpower = {0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 70, 70, 0, 90, 0, 60, 0, 140, 150, 150, 150, 0};
    static final int[] defaultai = {1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 4, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1};
    //static final int[] defaultmspeed = { 4  ,10 ,0  ,15 ,10 ,4  ,7  ,3 ,5  ,8  ,11 ,10 ,5  ,6  ,5  ,5   ,5  ,7   ,8   ,6   ,7   ,7  ,6   ,14  ,5    ,7   ,6    ,6    ,6  };
    static final int[] defaultmspeed = {4, 10, 1, 15, 10, 4, 6, 2, 5, 8, 11, 10, 5, 6, 5, 5, 5, 7, 8, 6, 6, 6, 6, 12, 5, 6, 6, 5, 6};
    static final int[] defaultaspeed = {-1, 2, 0, 10, 0, 0, 2, 1, 1, 1, 4, 4, 1, 1, 1, 1, 1, 2, 1, 1, 2, 3, 2, 3, 1, 1, 1, 1, 0};
    static final int[] defaultpickup = {2, 2, 4, 2, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 4, 4, 0};
    static final String[] pickupstr = {"Never Pick Up", "25% Pick Up", "50% Pick Up", "75% Pick Up", "Always Pick Up"};
    static final String[] stealstr = {"Never Steal", "25% Steal", "50% Steal", "75% Steal", "Always Steal"};
    
    //static final int[] defaultpower ={ 12,12,1  ,25 ,15 ,10 ,35 ,6 ,45 ,74 ,80 ,40 ,70 ,100,120,24  ,95 ,35  ,100 ,140 ,140 ,160,140,190,100  ,200 ,150   };
    //static final int[] defaultspeed ={ 35,30,80 ,30 ,35 ,45 ,40 ,75,55 ,40 ,45 ,35 ,65 ,45 ,60 ,50  ,75 ,50  ,50  ,55  ,60  ,50 ,55 ,40 ,65   ,50   };
    int carryingindex = -1, cmonsterindex = -1;
    
    MonsterData data = null;
    JList monsterlist, cmonsterlist;
    int level, x, y;
    int number = 0;
    int ai = 1;
    int facing = 0;
    boolean HITANDRUN;
    boolean isImmaterial;
    JTextField health, mana, power, defense, magicresist, speed, poison, fearresist, movespeed, attackspeed, name, picstring, soundstring, footstep;
    JComboBox facebox, aibox, pickup, steal;
    JToggleButton hitandrunbut, immaterialbut, sub5, isflyingbut, canusestairsbut, ignoremonsbut, canteleport, poisonimmune;
    JScrollPane monpane, cmonsterpane;
    JPanel ccenter;
    static Vector custommons = new Vector();
    
    public boolean gamewin = false;
    //public String endanim,endmusic,endsound;
    public String endanim, endsound;
    public int hurtitem, needitem, needhandneck;
    
    JToggleButton hasmagic, hasheal, hasdrain, useammo;
    JToggleButton predefinedbutton, custombutton;
    Vector spells = new Vector(3);
    Vector spellnames = new Vector(3);
    JList knownspells;
    JComboBox castpower, spellbox;
    JButton addspellbut, removespellbut;
    JTextField manapower;
    //Box spellpanel;
    JPanel spellpanel;
    //String[] monspells = { "Fireball","Lightning","Poison Bolt","Ven Cloud","Arc Bolt","Weakness","Feeble Mind","Slow","Strip Defenses","Silence","Door Open" };
    String[] spellnumbers = {"44", "335", "51", "31", "642", "461", "363", "362", "664", "523", "6"};
    
    //JLabel ammolabel;
    //int ammonumber = -1;
    ArrayList carrying = new ArrayList(1);
    ArrayList equipped = new ArrayList(3);
    Vector carryingnames = new Vector(3);
    JList carryinglist;
    JButton additembut, removeitembut, equipitembut;
    static final Random randGen = new Random();
    static final String[] powers = {"LO", "UM", "ON", "EE", "PAL", "MON"};
    JFrame frame;
    
    public MonsterWizard(JFrame f) {
        super(f, "Monster Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(815, 675);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        frame = f;
        
        //the list of monsters to choose from
        monsterlist = new JList(monsternames);
        monsterlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        monsterlist.setCellRenderer(new MonCellRenderer());
        //monsterlist.addListSelectionListener(this);
        //monsterlist.setPreferredSize(new Dimension(220,600));
        monsterlist.setSelectedIndex(0);
        monsterlist.addMouseListener(this);
        monpane = new JScrollPane(monsterlist);
        monpane.setPreferredSize(new Dimension(200, 120));
        
        //list of custom monsters, name, canusestairs, isflying
        cmonsterlist = new JList(custommons);
        cmonsterlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cmonsterlist.setCellRenderer(new MonCellRenderer());
        //cmonsterlist.addListSelectionListener(this);
        cmonsterlist.setPreferredSize(new Dimension(182, custommons.size() * 34));
        cmonsterlist.addMouseListener(this);
        cmonsterpane = new JScrollPane(cmonsterlist);
        cmonsterpane.setPreferredSize(new Dimension(200, 120));
        JButton addupdatebut = new JButton("Add/Update Mon");
        JButton deletebut = new JButton("Delete Mon");
        addupdatebut.addActionListener(this);
        deletebut.addActionListener(this);
        JPanel adddeletemonpan = new JPanel();
        adddeletemonpan.add(addupdatebut);
        adddeletemonpan.add(deletebut);
        JPanel cmonpanel = new JPanel();
        cmonpanel.setLayout(new BoxLayout(cmonpanel, BoxLayout.Y_AXIS));
        cmonpanel.add(cmonsterpane);
        cmonpanel.add(adddeletemonpan);
        //cmonpanel.add(deletebut);
        name = new JTextField("Monster Name", 8);
        picstring = new JTextField("mummy", 10);
        soundstring = new JTextField("mummy.wav", 10);
        footstep = new JTextField("step1.wav", 10);
        canusestairsbut = new JToggleButton("Use Stairs");
        canusestairsbut.setSelected(true);
        isflyingbut = new JToggleButton("Flying");
        ignoremonsbut = new JToggleButton("Better AI");
        canteleport = new JToggleButton("Teleports");
        JPanel cmonpanel2 = new JPanel(new GridLayout(8, 2));
        cmonpanel2.setPreferredSize(new Dimension(200, 150));
        cmonpanel2.add(new JLabel("Name:"));
        cmonpanel2.add(name);
        cmonpanel2.add(new JLabel("Pics Name:"));
        cmonpanel2.add(picstring);
        cmonpanel2.add(new JLabel("Sound:"));
        cmonpanel2.add(soundstring);
        cmonpanel2.add(new JLabel("Footstep:"));
        cmonpanel2.add(footstep);
        cmonpanel2.add(canusestairsbut);
        cmonpanel2.add(isflyingbut);
        cmonpanel2.add(ignoremonsbut);
        cmonpanel2.add(canteleport);
        ccenter = new JPanel();
        ccenter.add(cmonpanel);
        ccenter.add(cmonpanel2);
        ccenter.setVisible(false);
        
        //north panel -> predefined or custom
        predefinedbutton = new JToggleButton("Predefined");
        custombutton = new JToggleButton("Custom");
        predefinedbutton.addActionListener(this);
        custombutton.addActionListener(this);
        ButtonGroup typegrp = new ButtonGroup();
        typegrp.add(predefinedbutton);
        typegrp.add(custombutton);
        JPanel north = new JPanel();
        north.add(predefinedbutton);
        north.add(custombutton);
        predefinedbutton.setSelected(true);
        
        //health,mana,power,defense,magicresist,speed,fear,poison
        health = new JTextField("" + defaulthealth[0], 6);
        mana = new JTextField("" + defaultmana[0], 6);
        power = new JTextField("" + defaultpower[0], 6);
        defense = new JTextField("" + defaultdefense[0], 6);
        magicresist = new JTextField("" + defaultmresist[0], 6);
        speed = new JTextField("" + defaultspeed[0], 6);
        fearresist = new JTextField("" + defaultfresist[0], 2);
        poison = new JTextField("0", 2);
        movespeed = new JTextField("" + defaultmspeed[0], 3);
        attackspeed = new JTextField("" + defaultaspeed[0], 3);
        JPanel box1 = new JPanel(new GridLayout(5, 2));
        JPanel box2 = new JPanel(new GridLayout(5, 2));
        box1.add(new JLabel("Health:"));
        box1.add(health);
        box1.add(new JLabel("Mana:"));
        box1.add(mana);
        box1.add(new JLabel("Power:"));
        box1.add(power);
        box1.add(new JLabel("Defense:"));
        box1.add(defense);
        box1.add(new JLabel("Magic Resist:"));
        box1.add(magicresist);
        box2.add(new JLabel("Dexterity:"));
        box2.add(speed);
        box2.add(new JLabel("Fear Resist:"));
        box2.add(fearresist);
        box2.add(new JLabel("Poison Power:"));
        box2.add(poison);
        box2.add(new JLabel("Move Speed:"));
        box2.add(movespeed);
        box2.add(new JLabel("Attack Mod:"));
        box2.add(attackspeed);
        JPanel statpan = new JPanel();
        statpan.add(box1);
        statpan.add(box2);
        
        //facing modifier
        String[] faces = {"Facing North", "Facing West", "Facing South", "Facing East"};
        facebox = new JComboBox(faces);
        facebox.setEditable(false);
        facebox.addActionListener(this);
        
        //ai modifier
        //RANDOM = 0, GOTOWARDS = 1, STAYBACK = 2, RUN = 3, GUARD = 4, FRIENDLY = 5
        String[] aitypes = {"Aggressively Attack", "Attack From Distance", "Always Run Away", "Unmoving", "Friendly", "Friendly Unmoving"};
        aibox = new JComboBox(aitypes);
        aibox.setEditable(false);
        aibox.addActionListener(this);
        
        //hitandrun
        hitandrunbut = new JToggleButton("Flees after an Attack");
        hitandrunbut.setActionCommand("HITANDRUN");
        hitandrunbut.addActionListener(this);
        
        //poison immunity
        poisonimmune = new JToggleButton("Poison Immunity");
        
        //immaterial
        immaterialbut = new JToggleButton("Non-Material");
        immaterialbut.addActionListener(this);
        
        //has magic
        hasmagic = new JToggleButton("Has Magic");
        hasmagic.setSelected(false);
        hasmagic.addActionListener(this);
        
        //use ammo
        useammo = new JToggleButton("Throw Weapons");
        useammo.setSelected(false);
        
        //use subsquare 5 (take full square)
        sub5 = new JToggleButton("Fills Square");
        sub5.setSelected(false);
        
        //pick up items from the dungeon floor
        pickup = new JComboBox(pickupstr);
        pickup.setSelectedIndex(2);
        pickup.setEditable(false);
        
        //steal
        steal = new JComboBox(stealstr);
        steal.setSelectedIndex(0);
        steal.setEditable(false);
        
        JPanel fahipanel = new JPanel();
        fahipanel.add(facebox);
        fahipanel.add(aibox);
        fahipanel.add(hitandrunbut);
        fahipanel.add(sub5);
        fahipanel.add(immaterialbut);
        JPanel fahipanel2 = new JPanel();
        fahipanel2.add(hasmagic);
        fahipanel2.add(poisonimmune);
        fahipanel2.add(useammo);
        fahipanel2.add(pickup);
        fahipanel2.add(steal);
        
        //spells known
        spellpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        spellpanel.setPreferredSize(new Dimension(540, 180));
        hasheal = new JToggleButton("Has Heal Magic");
        hasheal.setSelected(false);
        hasdrain = new JToggleButton("Has Drain Magic");
        hasdrain.setSelected(false);
        String[] powers = {"LO", "UM", "ON", "EE", "PAL", "MON"};
        castpower = new JComboBox(powers);
        castpower.setEditable(false);
        castpower.setSelectedIndex(0);
        manapower = new JTextField("50", 3);
        knownspells = new JList();
        knownspells.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        knownspells.setVisibleRowCount(4);
        String[] monspells = {"Fireball", "Lightning", "Poison Bolt", "Ven Cloud", "Arc Bolt", "Weakness", "Feeble Mind", "Slow", "Strip Defenses", "Silence", "Door Open"};
        spellbox = new JComboBox(monspells);
        spellbox.setEditable(false);
        addspellbut = new JButton("^ Add Above Spell");
        removespellbut = new JButton("< Remove Selected Spell");
        addspellbut.addActionListener(this);
        removespellbut.addActionListener(this);
        Box spellbutbox = Box.createVerticalBox();
        spellbutbox.add(spellbox);
        spellbutbox.add(addspellbut);
        spellbutbox.add(removespellbut);
        JScrollPane spellpane = new JScrollPane(knownspells);
        spellpane.setPreferredSize(new Dimension(200, 80));
        JPanel knownspellpan = new JPanel();
        knownspellpan.add(spellpane);
        knownspellpan.add(spellbutbox);
        knownspellpan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Known Projectile Type"));
        JPanel span = new JPanel();
        span.add(hasheal);
        span.add(hasdrain);
        span.add(new JLabel("Casting Power:"));
        span.add(castpower);
        span.add(new JLabel("Intelligence:"));
        span.add(manapower);
        spellpanel.add(span);
        spellpanel.add(knownspellpan);
        spellpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Spells"));
        spellpanel.setVisible(false);
        
        //carrying
        carryinglist = new JList();
        carryinglist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //carryinglist.setPreferredSize(new Dimension(220,110));
        carryinglist.setVisibleRowCount(6);
        carryinglist.addMouseListener(this);
        additembut = new JButton("Add/Edit Item");
        equipitembut = new JButton("Equip/UnEquip Item");
        removeitembut = new JButton("Remove Item");
        additembut.setActionCommand("Add Item");
        additembut.addActionListener(this);
        equipitembut.addActionListener(this);
        removeitembut.addActionListener(this);
        Box carryingbutbox = Box.createVerticalBox();
        carryingbutbox.add(additembut);
        carryingbutbox.add(equipitembut);
        carryingbutbox.add(removeitembut);
        JPanel carryingpanel = new JPanel();
        JScrollPane carrypane = new JScrollPane(carryinglist);
        carrypane.setPreferredSize(new Dimension(200, 100));
        carryingpanel.add(carrypane);
        carryingpanel.add(carryingbutbox);
        carryingpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Carrying"));
        
        JPanel centerpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        centerpanel.setPreferredSize(new Dimension(500, 450));
        centerpanel.add(monpane);
        centerpanel.add(ccenter);
        centerpanel.add(statpan);
        centerpanel.add(fahipanel);
        centerpanel.add(fahipanel2);
        centerpanel.add(spellpanel);
        centerpanel.add(carryingpanel);
        
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        JButton specials = new JButton("Specials...");
        done.addActionListener(this);
        cancel.addActionListener(this);
        specials.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        bottompanel.add(Box.createHorizontalStrut(40));
        bottompanel.add(specials);
        
        cp.add("North", north);
        cp.add("Center", centerpanel);
        cp.add("South", bottompanel);
        
        dispose();
    }
        
        /*
        public MonsterWizard(JFrame f,int level,int x,int y) { this(f,level,x,y,null); }
        public MonsterWizard(JFrame f,int level,int x,int y,MonsterData olddata) {
                super(f,"Monster Wizard",true);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                //setSize(600,580);
                setSize(770,650);
                setLocationRelativeTo(f);
                Container cp = getContentPane();
                frame = f;
                
                this.level = level;
                this.x = x;
                this.y = y;
                
                //the list of monsters to choose from
                monsterlist = new JList(monsternames);
                monsterlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                monsterlist.setCellRenderer(new MonCellRenderer());
                //monsterlist.addListSelectionListener(this);
                //monsterlist.setPreferredSize(new Dimension(220,600));
                monsterlist.setSelectedIndex(0);
                monsterlist.addMouseListener(this);
                monpane = new JScrollPane(monsterlist);
                monpane.setPreferredSize(new Dimension(200,120));
                
                //list of custom monsters, name, canusestairs, isflying
                cmonsterlist = new JList(custommons);
                cmonsterlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                //cmonsterlist.addListSelectionListener(this);
                cmonsterlist.setPreferredSize(new Dimension(182,custommons.size()*19));
                cmonsterlist.addMouseListener(this);
                cmonsterpane = new JScrollPane(cmonsterlist);
                cmonsterpane.setPreferredSize(new Dimension(200,120));
                JButton deletebut = new JButton("Delete Mon");
                deletebut.addActionListener(this);
                JPanel cmonpanel = new JPanel();
                cmonpanel.setLayout(new BoxLayout(cmonpanel,BoxLayout.Y_AXIS));
                cmonpanel.add(cmonsterpane);
                cmonpanel.add(deletebut);
                name = new JTextField("Monster Name",8);
                picstring = new JTextField("mummy",10);
                soundstring = new JTextField("mummy.wav",10);
                canusestairsbut = new JToggleButton("Use Stairs");
                canusestairsbut.setSelected(true);
                isflyingbut = new JToggleButton("Flying");
                ignoremonsbut = new JToggleButton("Improved AI");
                JPanel cmonpanel1 = new JPanel();
                cmonpanel1.add(new JLabel("Pics Name:"));
                cmonpanel1.add(picstring);
                JPanel cmonpanel0 = new JPanel();
                cmonpanel0.add(new JLabel("Sound Name:"));
                cmonpanel0.add(soundstring);
                //JPanel cmonpanel2 = new JPanel();
                //cmonpanel2.setLayout(new BoxLayout(cmonpanel2,BoxLayout.Y_AXIS));
                JPanel cmonpanel2 = new JPanel(new GridLayout(6,1));
                cmonpanel2.setPreferredSize(new Dimension(200,140));
                cmonpanel2.add(name);
                cmonpanel2.add(cmonpanel1);
                cmonpanel2.add(cmonpanel0);
                cmonpanel2.add(canusestairsbut);
                cmonpanel2.add(isflyingbut);
                cmonpanel2.add(ignoremonsbut);
                ccenter = new JPanel();
                ccenter.add(cmonpanel);
                ccenter.add(cmonpanel2);
                ccenter.setVisible(false);
                
                //north panel -> predefined or custom
                JToggleButton predefinedbutton = new JToggleButton("Predefined");
                JToggleButton custombutton = new JToggleButton("Custom");
                predefinedbutton.addActionListener(this);
                custombutton.addActionListener(this);
                ButtonGroup typegrp = new ButtonGroup();
                typegrp.add(predefinedbutton);
                typegrp.add(custombutton);
                JPanel north = new JPanel();
                north.add(predefinedbutton);
                north.add(custombutton);
                predefinedbutton.setSelected(true);
                
                //health,mana,power,defense,magicresist,speed,fear,poison
                health = new JTextField(""+defaulthealth[0],6);
                mana = new JTextField(""+defaultmana[0],6);
                power = new JTextField(""+defaultpower[0],6);
                defense = new JTextField(""+defaultdefense[0],6);
                magicresist = new JTextField(""+defaultmresist[0],6);
                speed = new JTextField(""+defaultspeed[0],6);
                fearresist = new JTextField(""+defaultfresist[0],2);
                poison = new JTextField("0",2);
                movespeed = new JTextField(""+defaultmspeed[0],3);
                attackspeed = new JTextField(""+defaultaspeed[0],3);
                JPanel box1 = new JPanel(new GridLayout(5,2));
                JPanel box2 = new JPanel(new GridLayout(5,2));
                box1.add(new JLabel("Health:"));
                box1.add(health);
                box1.add(new JLabel("Mana:"));
                box1.add(mana);
                box1.add(new JLabel("Power:"));
                box1.add(power);
                box1.add(new JLabel("Defense:"));
                box1.add(defense);
                box1.add(new JLabel("Magic Resist:"));
                box1.add(magicresist);
                box2.add(new JLabel("Dexterity:"));
                box2.add(speed);
                box2.add(new JLabel("Fear Resist:"));
                box2.add(fearresist);
                box2.add(new JLabel("Poison Power:"));
                box2.add(poison);
                box2.add(new JLabel("Move Speed:"));
                box2.add(movespeed);
                box2.add(new JLabel("Attack Mod:"));
                box2.add(attackspeed);
                JPanel statpan = new JPanel();
                statpan.add(box1);
                statpan.add(box2);
                
                //facing modifier
                String[] faces = { "Facing North","Facing West","Facing South","Facing East" };
                facebox = new JComboBox(faces);
                facebox.setEditable(false);
                facebox.addActionListener(this);
                
                //ai modifier
                //RANDOM = 0, GOTOWARDS = 1, STAYBACK = 2, RUN = 3, GUARD = 4
                String[] aitypes = { "Aggressively Attack","Attack From Distance","Always Run Away","Unmoving" };
                aibox = new JComboBox(aitypes);
                aibox.setEditable(false);
                aibox.addActionListener(this);
                
                //hitandrun
                hitandrunbut = new JToggleButton("Flees after an Attack");
                hitandrunbut.setActionCommand("HITANDRUN");
                hitandrunbut.addActionListener(this);
                
                //immaterial
                immaterialbut = new JToggleButton("Non-Material");
                immaterialbut.addActionListener(this);
                
                //has magic
                hasmagic = new JToggleButton("Has Magic");
                hasmagic.setSelected(false);
                hasmagic.addActionListener(this);

                //use ammo
                useammo = new JToggleButton("Throw Weapons");
                useammo.setSelected(false);
                
                //use subsquare 5 (take full square)
                sub5 = new JToggleButton("Fills Square");
                sub5.setSelected(false);
                
                //pick up items from the dungeon floor
                pickup = new JComboBox(pickupstr);
                pickup.setSelectedIndex(2);
                pickup.setEditable(false);
                
                //steal
                //steal = new JComboBox(stealstr);
                //steal.setSelectedIndex(0);
                //steal.setEditable(false);

                JPanel fahipanel = new JPanel();
                fahipanel.setLayout(new GridLayout(1,4,2,2));
                fahipanel.add(facebox);
                fahipanel.add(aibox);
                fahipanel.add(hitandrunbut);
                fahipanel.add(sub5);
                JPanel fahipanel2 = new JPanel();
                fahipanel2.setLayout(new GridLayout(1,5,2,2));
                fahipanel2.add(immaterialbut);
                fahipanel2.add(hasmagic);
                fahipanel2.add(useammo);
                fahipanel2.add(pickup);
                //fahipanel2.add(steal);
                
                //spells known
                spellpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
                spellpanel.setPreferredSize(new Dimension(540,180));
                hasheal = new JToggleButton("Has Heal Magic");
                hasheal.setSelected(false);
                hasdrain = new JToggleButton("Has Drain Magic");
                hasdrain.setSelected(false);
                String[] powers = { "LO","UM","ON","EE","PAL","MON" };
                castpower = new JComboBox(powers);
                castpower.setEditable(false);
                castpower.setSelectedIndex(0);
                manapower = new JTextField("50",3);
                knownspells = new JList();
                knownspells.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                knownspells.setVisibleRowCount(4);
                String[] monspells = { "Fireball","Lightning","Poison Bolt","Ven Cloud","Arc Bolt","Weakness","Feeble Mind","Slow","Strip Defenses","Silence","Door Open" };
                spellbox = new JComboBox(monspells);
                spellbox.setEditable(false);
                addspellbut = new JButton("^ Add Above Spell");
                removespellbut = new JButton("< Remove Selected Spell");
                addspellbut.addActionListener(this);
                removespellbut.addActionListener(this);
                Box spellbutbox = Box.createVerticalBox();
                spellbutbox.add(spellbox);
                spellbutbox.add(addspellbut);
                spellbutbox.add(removespellbut);
                JScrollPane spellpane = new JScrollPane(knownspells);
                spellpane.setPreferredSize(new Dimension(200,80));
                JPanel knownspellpan = new JPanel();
                knownspellpan.add(spellpane);
                knownspellpan.add(spellbutbox);
                knownspellpan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Known Projectile Type"));
                JPanel span = new JPanel();
                span.add(hasheal);
                span.add(hasdrain);
                span.add(new JLabel("Casting Power:"));
                span.add(castpower);
                span.add(new JLabel("Intelligence:"));
                span.add(manapower);
                spellpanel.add(span);
                spellpanel.add(knownspellpan);
                spellpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),"Spells"));
                spellpanel.setVisible(false);
                
                //carrying
                carryinglist = new JList();
                carryinglist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                //carryinglist.setPreferredSize(new Dimension(220,110));
                carryinglist.setVisibleRowCount(6);
                carryinglist.addMouseListener(this);
                additembut = new JButton("Add/Edit Item");
                removeitembut = new JButton("Remove Item");
                additembut.setActionCommand("Add Item");
                additembut.addActionListener(this);
                removeitembut.addActionListener(this);
                Box carryingbutbox = Box.createVerticalBox();
                carryingbutbox.add(additembut);
                carryingbutbox.add(removeitembut);
                JPanel carryingpanel = new JPanel();
                JScrollPane carrypane = new JScrollPane(carryinglist);
                carrypane.setPreferredSize(new Dimension(200,100));
                carryingpanel.add(carrypane);
                carryingpanel.add(carryingbutbox);
                carryingpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),"Carrying"));

                JPanel centerpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,2));
                centerpanel.setPreferredSize(new Dimension(500,450));
                centerpanel.add(monpane);
                centerpanel.add(ccenter);
                centerpanel.add(statpan);
                centerpanel.add(fahipanel);
                centerpanel.add(fahipanel2);
                centerpanel.add(spellpanel);
                centerpanel.add(carryingpanel);
                
                JPanel bottompanel = new JPanel();
                JButton done = new JButton("Done");
                JButton cancel = new JButton("Cancel");
                JButton specials = new JButton("Specials...");
                done.addActionListener(this);
                cancel.addActionListener(this);
                specials.addActionListener(this);
                bottompanel.add(done);
                bottompanel.add(cancel);
                bottompanel.add(Box.createHorizontalStrut(40));
                bottompanel.add(specials);
                
                cp.add("North",north);
                cp.add("Center",centerpanel);
                cp.add("South",bottompanel);
                
                data = olddata;
                if (data!=null) {
                        number = data.number;
                        if (number>27) {
                                custombutton.doClick();
                                cmonsterlist.setSelectedValue(data,true);
                                cmonsterlist.ensureIndexIsVisible(cmonsterlist.getSelectedIndex());
                                cmonsterpane.getViewport().setViewPosition(new Point(0,cmonsterlist.getSelectedIndex()*19));
                                cmonsterindex = cmonsterlist.getSelectedIndex();
                                name.setText(data.name);
                                picstring.setText(data.picstring);
                                soundstring.setText(data.soundstring);
                                canusestairsbut.setSelected(data.canusestairs);
                                isflyingbut.setSelected(data.isflying);
                        }
                        else {
                                monsterlist.setSelectedValue(monsternames[data.number],true);
                                monsterlist.ensureIndexIsVisible(monsterlist.getSelectedIndex());
                                monpane.getViewport().setViewPosition(new Point(0,monsterlist.getSelectedIndex()*34));
                        }
                        ai = data.defaultai;
                        aibox.setSelectedIndex(ai-1);
                        facing = data.facing;
                        facebox.setSelectedIndex(facing);
                        pickup.setSelectedIndex(data.pickup);
                        //steal.setSelectedIndex(data.steal);
                        if (data.HITANDRUN) hitandrunbut.doClick();
                        if (data.name.equals("Giggler")) immaterialbut.setEnabled(false);
                        else if (data.isImmaterial) immaterialbut.doClick();
                        if (number==9 || number==10 || number==13 || number==16 || number==19 || number==22 || number==25 || number==26 || number==27) {
                                sub5.setSelected(true);
                                sub5.setEnabled(false);
                        }
                        else if (data.subsquare==5) sub5.setSelected(true);
                        health.setText(""+data.maxhealth);
                        mana.setText(""+data.maxmana);
                        power.setText(""+data.power);
                        defense.setText(""+data.defense);
                        magicresist.setText(""+data.magicresist);
                        speed.setText(""+data.speed);
                        movespeed.setText(""+data.movespeed);
                        attackspeed.setText(""+data.attackspeed);
                        poison.setText(""+data.poison);
                        fearresist.setText(""+data.fearresist);
                        useammo.setSelected(data.useammo);
                        if (data.hasmagic) {
                                hasmagic.doClick();
                                hasheal.setSelected(data.hasheal);
                                hasdrain.setSelected(data.hasdrain);
                                manapower.setText(""+data.manapower);
                                castpower.setSelectedIndex(data.castpower-1);
                                for (int i=0;i<data.knownspells.length;i++) {
                                        spells.add(data.knownspells[i]);
                                        if (data.knownspells[i].equals("44")) spellnames.add("Fireball");
                                        else if (data.knownspells[i].equals("335")) spellnames.add("Lightning");
                                        else if (data.knownspells[i].equals("51")) spellnames.add("Poison Bolt");
                                        else if (data.knownspells[i].equals("31")) spellnames.add("Ven Cloud");
                                        else if (data.knownspells[i].equals("642")) spellnames.add("Arc Bolt");
                                        else if (data.knownspells[i].equals("461")) spellnames.add("Weakness");
                                        else if (data.knownspells[i].equals("363")) spellnames.add("Feeble Mind");
                                        else if (data.knownspells[i].equals("362")) spellnames.add("Slow");
                                        else if (data.knownspells[i].equals("664")) spellnames.add("Strip Defenses");
                                        else if (data.knownspells[i].equals("523")) spellnames.add("Silence");
                                        else if (data.knownspells[i].equals("6")) spellnames.add("Door Open");
                                }
                                knownspells.setListData(spellnames);
                        }
                        //carrying = data.carrying;
                        Item tempitem;
                        for (int i=0;i<data.carrying.size();i++) {
                                tempitem = (Item)data.carrying.get(i);
                                carrying.add(tempitem);
                                if (tempitem.ispotion) carryingnames.add(tempitem.name+" ("+powers[tempitem.potioncastpow-1]+")");
                                else if (tempitem instanceof Waterskin) {
                                        if (((Waterskin)tempitem).drinks==0) carryingnames.add("Waterskin (Empty)");
                                        else if (((Waterskin)tempitem).drinks==1) carryingnames.add("Waterskin (Almost Empty)"); 
                                        else if (((Waterskin)tempitem).drinks==2) carryingnames.add("Waterskin (Almost Full)"); 
                                        else carryingnames.add("Waterskin (Full)");  
                                }
                                else carryingnames.add(tempitem.name);
                        }
                        carryinglist.setListData(carryingnames);
                        gamewin = data.gamewin;
                        //if (gamewin) { endanim=data.endanim; endmusic=data.endmusic; endsound=data.endsound; }
                        if (gamewin) { endanim=data.endanim; endsound=data.endsound; }
                        hurtitem = data.hurtitem; needitem = data.needitem; needhandneck = data.needhandneck;
                }
                else if (LASTNUMBER<28) {
                        monsterlist.setSelectedValue(monsternames[LASTNUMBER],true);
                        mousePressed(new MouseEvent(monsterlist,MouseEvent.MOUSE_PRESSED,0,0,0,LASTNUMBER*34+1,1,false));
                        monpane.getViewport().setViewPosition(new Point(0,monsterlist.getSelectedIndex()*34));
                }
                else {
                        custombutton.doClick();
                        cmonsterlist.setSelectedValue((MonsterData)custommons.get(LASTNUMBER-28),true);
                        cmonsterlist.ensureIndexIsVisible(cmonsterlist.getSelectedIndex());
                        cmonsterpane.getViewport().setViewPosition(new Point(0,cmonsterlist.getSelectedIndex()*19));
                        mousePressed(new MouseEvent(cmonsterlist,MouseEvent.MOUSE_PRESSED,0,0,0,(LASTNUMBER-28)*19+1,1,false));
                }
                //monpane.revalidate();
                show();
        }
        */
    
    public void setMonster(MonsterData olddata, int level, int x, int y) {
        data = olddata;
        this.level = level;
        this.x = x;
        this.y = y;
        if (olddata != null) {
            number = data.number;
            if (number > 27) {
                cmonsterlist.clearSelection();
                cmonsterindex = -1;
                cmonsterlist.setSelectedValue(data, true);
                if (!ccenter.isVisible()) {
                    predefinedbutton.setSelected(false);
                    custombutton.setSelected(true);
                    monpane.setVisible(false);
                    ccenter.setVisible(true);
                }
                cmonsterindex = cmonsterlist.getSelectedIndex();
                if (cmonsterindex >= 0) {
                    cmonsterlist.ensureIndexIsVisible(cmonsterindex);
                    cmonsterpane.getViewport().setViewPosition(new Point(0, cmonsterindex * 34));
                }
                name.setText(data.name);
                picstring.setText(data.picstring);
                soundstring.setText(data.soundstring);
                footstep.setText(data.footstep);
                canusestairsbut.setSelected(data.canusestairs);
                isflyingbut.setSelected(data.isflying);
                canteleport.setSelected(data.canteleport);
                //sub5.setEnabled(true);
                //sub5.setSelected(data.subsquare==5);
            } else {
                monsterlist.setSelectedValue(monsternames[data.number], true);
                if (ccenter.isVisible()) {
                    ccenter.setVisible(false);
                    monpane.setVisible(true);
                    custombutton.setSelected(false);
                    predefinedbutton.setSelected(true);
                }
                monsterlist.ensureIndexIsVisible(monsterlist.getSelectedIndex());
                monpane.getViewport().setViewPosition(new Point(0, monsterlist.getSelectedIndex() * 34));
            }
            ai = data.defaultai;
            aibox.setSelectedIndex(ai - 1);
            facing = data.facing;
            facebox.setSelectedIndex(facing);
            pickup.setSelectedIndex(data.pickup);
            steal.setSelectedIndex(data.steal);
            poisonimmune.setSelected(data.poisonimmune);
            if (data.HITANDRUN != HITANDRUN) hitandrunbut.doClick();
            if (data.isImmaterial != immaterialbut.isSelected()) immaterialbut.doClick();
            if (number == 9 || number == 10 || number == 13 || number == 16 || number == 19 || number == 22 || number == 25 || number == 26 || number == 27) {
                sub5.setSelected(true);
                sub5.setEnabled(false);
            } else {
                sub5.setEnabled(true);
                sub5.setSelected(data.subsquare == 5);
                //if (data.subsquare==5) sub5.setSelected(true);
                //else sub5.setSelected(false);
            }
            health.setText("" + data.maxhealth);
            mana.setText("" + data.maxmana);
            power.setText("" + data.power);
            defense.setText("" + data.defense);
            magicresist.setText("" + data.magicresist);
            speed.setText("" + data.speed);
            movespeed.setText("" + data.movespeed);
            attackspeed.setText("" + data.attackspeed);
            poison.setText("" + data.poison);
            fearresist.setText("" + data.fearresist);
            useammo.setSelected(data.useammo);
            if (data.hasmagic != hasmagic.isSelected()) hasmagic.doClick();
            if (data.hasmagic) {
                hasheal.setSelected(data.hasheal);
                hasdrain.setSelected(data.hasdrain);
                manapower.setText("" + data.manapower);
                castpower.setSelectedIndex(data.castpower - 1);
                spells.clear();
                spellnames.clear();
                for (int i = 0; i < data.knownspells.length; i++) {
                    spells.add(data.knownspells[i]);
                    if (data.knownspells[i].equals("44")) spellnames.add("Fireball");
                    else if (data.knownspells[i].equals("335")) spellnames.add("Lightning");
                    else if (data.knownspells[i].equals("51")) spellnames.add("Poison Bolt");
                    else if (data.knownspells[i].equals("31")) spellnames.add("Ven Cloud");
                    else if (data.knownspells[i].equals("642")) spellnames.add("Arc Bolt");
                    else if (data.knownspells[i].equals("461")) spellnames.add("Weakness");
                    else if (data.knownspells[i].equals("363")) spellnames.add("Feeble Mind");
                    else if (data.knownspells[i].equals("362")) spellnames.add("Slow");
                    else if (data.knownspells[i].equals("664")) spellnames.add("Strip Defenses");
                    else if (data.knownspells[i].equals("523")) spellnames.add("Silence");
                    else if (data.knownspells[i].equals("6")) spellnames.add("Door Open");
                }
                knownspells.setListData(spellnames);
            }
            carrying = new ArrayList(1);
            equipped = new ArrayList(3);
            carryingnames.clear();
            Item tempitem;
            if (data.equipped != null) for (int i = 0; i < data.equipped.size(); i++) {
                tempitem = (Item) data.equipped.get(i);
                equipped.add(Item.createCopy(tempitem));
                if (tempitem.ispotion)
                    carryingnames.add(tempitem.name + " (" + powers[tempitem.potioncastpow - 1] + ") (E)");
                else if (tempitem instanceof Waterskin) {
                    if (((Waterskin) tempitem).drinks == 0) carryingnames.add("Waterskin (Empty) (E)");
                    else if (((Waterskin) tempitem).drinks == 1) carryingnames.add("Waterskin (Almost Empty) (E)");
                    else if (((Waterskin) tempitem).drinks == 2) carryingnames.add("Waterskin (Almost Full) (E)");
                    else carryingnames.add("Waterskin (Full) (E)");
                } else carryingnames.add(tempitem.name + " (E)");
            }
            for (int i = 0; i < data.carrying.size(); i++) {
                tempitem = (Item) data.carrying.get(i);
                carrying.add(Item.createCopy(tempitem));
                if (tempitem.ispotion)
                    carryingnames.add(tempitem.name + " (" + powers[tempitem.potioncastpow - 1] + ")");
                else if (tempitem instanceof Waterskin) {
                    if (((Waterskin) tempitem).drinks == 0) carryingnames.add("Waterskin (Empty)");
                    else if (((Waterskin) tempitem).drinks == 1) carryingnames.add("Waterskin (Almost Empty)");
                    else if (((Waterskin) tempitem).drinks == 2) carryingnames.add("Waterskin (Almost Full)");
                    else carryingnames.add("Waterskin (Full)");
                } else carryingnames.add(tempitem.name);
            }
            carryinglist.setListData(carryingnames);
            gamewin = data.gamewin;
            if (gamewin) {
                endanim = data.endanim;
                endsound = data.endsound;
            }
            hurtitem = data.hurtitem;
            needitem = data.needitem;
            needhandneck = data.needhandneck;
        } else {
            if (cmonsterindex != -1) {
                cmonsterlist.setSelectedIndex(cmonsterindex);
                cmonsterpane.getViewport().setViewPosition(new Point(0, cmonsterindex * 34));
            }
            ArrayList carrynew = new ArrayList(3);
            for (int i = 0; i < carrying.size(); i++) {
                carrynew.add(Item.createCopy((Item) carrying.get(i)));
            }
            carrying = carrynew;
            ArrayList equippednew = new ArrayList(3);
            for (int i = 0; i < equipped.size(); i++) {
                equippednew.add(Item.createCopy((Item) equipped.get(i)));
            }
            equipped = equippednew;
                        /*
                        if (LASTNUMBER<28) {
                                monsterlist.setSelectedValue(monsternames[LASTNUMBER],true);
                                mousePressed(new MouseEvent(monsterlist,MouseEvent.MOUSE_PRESSED,0,0,0,LASTNUMBER*34+1,1,false));
                                monpane.getViewport().setViewPosition(new Point(0,monsterlist.getSelectedIndex()*34));
                        }
                        else {
                                custombutton.doClick();
                                cmonsterlist.setSelectedValue((MonsterData)custommons.get(LASTNUMBER-28),true);
                                cmonsterlist.ensureIndexIsVisible(cmonsterlist.getSelectedIndex());
                                cmonsterpane.getViewport().setViewPosition(new Point(0,cmonsterlist.getSelectedIndex()*19));
                                mousePressed(new MouseEvent(cmonsterlist,MouseEvent.MOUSE_PRESSED,0,0,0,(LASTNUMBER-28)*19+1,1,false));
                        }
                        */
        }
        show();
    }
    
    public void updateCustomMons() {
        cmonsterlist.setListData(custommons);
        cmonsterlist.setPreferredSize(new Dimension(182, custommons.size() * 34));
        DMEditor.NEEDSAVEMONS = true;
        DMEditor.eventwizard.updateMons();
    }
    
    public MonsterData getData() {
        return data;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            //put test here for custom mons
            if (ccenter.isVisible() && !name.getText().equals("")) {
                data = new MonsterData(28, level, x, y, Integer.parseInt(health.getText()), Integer.parseInt(mana.getText()), facing, ai, HITANDRUN, isImmaterial, Integer.parseInt(power.getText()), Integer.parseInt(defense.getText()), Integer.parseInt(magicresist.getText()), Integer.parseInt(speed.getText()), Integer.parseInt(poison.getText()), Integer.parseInt(fearresist.getText()), carrying, equipped, gamewin, endanim, endsound, hurtitem, needitem, needhandneck, sub5.isSelected(), pickup.getSelectedIndex(), steal.getSelectedIndex(), name.getText(), picstring.getText(), soundstring.getText(), footstep.getText(), canusestairsbut.isSelected(), isflyingbut.isSelected(), !ignoremonsbut.isSelected(), canteleport.isSelected(), poisonimmune.isSelected());
                int index = cmonsterlist.getSelectedIndex();
                if (index == -1) index = custommons.indexOf(data);
                if (index == -1) index = custommons.size();
                number = 28 + index;
                data.number = number;
                //add custom mon if don't have it already
                if (index == custommons.size()) {
                    custommons.add(data);
                    cmonsterlist.setPreferredSize(new Dimension(182, custommons.size() * 34));
                    cmonsterlist.setListData(custommons);
                    cmonsterlist.setSelectedIndex(index);
                    cmonsterindex = index;
                    cmonsterlist.ensureIndexIsVisible(cmonsterindex);
                    cmonsterpane.getViewport().setViewPosition(new Point(0, cmonsterindex * 34));
                    DMEditor.NEEDSAVEMONS = true;
                }
                                /*
                                if (index<custommons.size()) custommons.set(index,data);
                                else {
                                        custommons.add(data);
                                        cmonsterlist.setPreferredSize(new Dimension(182,custommons.size()*34));
                                }
                                cmonsterlist.setListData(custommons);
                                DMEditor.NEEDSAVEMONS = true;
                                */
            } else
                data = new MonsterData(number, level, x, y, Integer.parseInt(health.getText()), Integer.parseInt(mana.getText()), facing, ai, HITANDRUN, isImmaterial, Integer.parseInt(power.getText()), Integer.parseInt(defense.getText()), Integer.parseInt(magicresist.getText()), Integer.parseInt(speed.getText()), Integer.parseInt(poison.getText()), Integer.parseInt(fearresist.getText()), carrying, equipped, gamewin, endanim, endsound, hurtitem, needitem, needhandneck, sub5.isSelected(), pickup.getSelectedIndex(), steal.getSelectedIndex(), poisonimmune.isSelected());
            finishCreation();
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("Specials...")) {
            new MonSpecials(this);
        } else if (e.getActionCommand().equals("Has Magic")) {
            spellpanel.setVisible(!spellpanel.isVisible());
            spellpanel.invalidate();
            getContentPane().validate();
        } else if (e.getActionCommand().equals("^ Add Above Spell")) {
            //if (spellnames.contains(spellbox.getSelectedItem())) return;
            spellnames.add(spellbox.getSelectedItem());
            spells.add(spellnumbers[spellbox.getSelectedIndex()]);
            knownspells.setListData(spellnames);
        } else if (e.getActionCommand().equals("< Remove Selected Spell")) {
            int index = knownspells.getSelectedIndex();
            if (index == -1) return;
            spellnames.remove(index);
            spells.remove(index);
            knownspells.setListData(spellnames);
        } else if (e.getActionCommand().equals("Add Item")) {
            int index = carryinglist.getSelectedIndex();
            if (index == -1) {
                //Item tempitem = (new ItemWizard(frame,"Item Wizard - Give this Monster an Item")).getItem();
                DMEditor.itemwizard.setTitle("Item Wizard - Give This Monster An Item");
                DMEditor.itemwizard.show();
                Item tempitem = DMEditor.itemwizard.getItem();
                if (tempitem == null) {
                    carryinglist.clearSelection();
                    return;
                }
                carrying.add(tempitem);
                if (tempitem.ispotion)
                    carryingnames.add(tempitem.name + " (" + powers[tempitem.potioncastpow - 1] + ")");
                else if (tempitem instanceof Waterskin) {
                    if (((Waterskin) tempitem).drinks == 0) carryingnames.add("Waterskin (Empty)");
                    else if (((Waterskin) tempitem).drinks == 1) carryingnames.add("Waterskin (Almost Empty)");
                    else if (((Waterskin) tempitem).drinks == 2) carryingnames.add("Waterskin (Almost Full)");
                    else carryingnames.add("Waterskin (Full)");
                } else carryingnames.add(tempitem.name);
                carryinglist.setListData(carryingnames);
            } else {
                //Item tempitem = (new ItemWizard(frame,"Item Wizard - Give this Monster an Item",((Item)carrying.get(index)))).getItem();
                DMEditor.itemwizard.setTitle("Item Wizard - Change An Item This Monster Is Carrying");
                                /*
                                ArrayList whichlist = carrying;
                                int actualindex = 0;
                                if (((String)carryingnames.get(index)).endsWith("(E)")) whichlist = equipped;
                                for (int i=0;i<index;i++) {
                                        String cname = (String)carryingnames.get(i);
                                        if (cname.endsWith("(E)")) { 
                                                if (whichlist==equipped) actualindex++;
                                        }
                                        else if (whichlist==carrying) actualindex++;
                                }
                                */
                ArrayList whichlist = getActualList(index);
                int actualindex = getActualIndex(whichlist, index);
                DMEditor.itemwizard.setItem((Item) whichlist.get(actualindex));
                Item tempitem = DMEditor.itemwizard.getItem();
                if (tempitem == null) {
                    carryinglist.clearSelection();
                    carryingindex = -1;
                    return;
                }
                whichlist.set(actualindex, tempitem);
                String adds;
                if (tempitem.ispotion) adds = tempitem.name + " (" + powers[tempitem.potioncastpow - 1] + ")";
                else if (tempitem instanceof Waterskin) {
                    if (((Waterskin) tempitem).drinks == 0) adds = "Waterskin (Empty)";
                    else if (((Waterskin) tempitem).drinks == 1) adds = "Waterskin (Almost Empty)";
                    else if (((Waterskin) tempitem).drinks == 2) adds = "Waterskin (Almost Full)";
                    else adds = "Waterskin (Full)";
                } else adds = tempitem.name;
                if (whichlist == equipped) adds += " (E)";
                carryingnames.set(index, adds);
                carryinglist.setListData(carryingnames);
                carryingindex = -1;
            }
        } else if (e.getActionCommand().equals("Equip/UnEquip Item")) {
            if (carryingnames.size() == 0) return;
            int index = carryinglist.getSelectedIndex();
            if (index == -1) return;
            ArrayList whichlist = getActualList(index);
            int actualindex = getActualIndex(whichlist, index);
            Item it = (Item) whichlist.remove(actualindex);
            carryingnames.remove(index);
            if (whichlist == carrying) {
                equipped.add(it);
                carryingnames.add(it.name + " (E)");
            } else {
                carrying.add(it);
                carryingnames.add(it.name);
            }
            carryinglist.setListData(carryingnames);
            carryingindex = -1;
        } else if (e.getActionCommand().equals("Remove Item")) {
            if (carryingnames.size() == 0) return;
            int index = carryinglist.getSelectedIndex();
            if (index == -1) return;
            ArrayList whichlist = getActualList(index);
            int actualindex = getActualIndex(whichlist, index);
            whichlist.remove(actualindex);
            carryingnames.remove(index);
            carryinglist.setListData(carryingnames);
            carryingindex = -1;
        } else if (e.getActionCommand().equals("HITANDRUN")) {
            HITANDRUN = !HITANDRUN;
        } else if (e.getActionCommand().equals("Non-Material")) {
            isImmaterial = !isImmaterial;
            if (isImmaterial) {
                carrying.clear();
                carryingnames.clear();
                carryinglist.setListData(carryingnames);
                additembut.setEnabled(false);
                equipitembut.setEnabled(false);
                removeitembut.setEnabled(false);
                pickup.setSelectedIndex(0);
                pickup.setEnabled(false);
                steal.setSelectedIndex(0);
                steal.setEnabled(false);
                useammo.setSelected(false);
                useammo.setEnabled(false);
                poisonimmune.setSelected(true);
                poisonimmune.setEnabled(false);
            } else {
                additembut.setEnabled(true);
                equipitembut.setEnabled(true);
                removeitembut.setEnabled(true);
                pickup.setEnabled(true);
                steal.setEnabled(true);
                useammo.setEnabled(true);
                poisonimmune.setEnabled(true);
            }
        } else if (e.getSource().equals(facebox)) {
            facing = facebox.getSelectedIndex();
        } else if (e.getSource().equals(aibox)) {
            ai = aibox.getSelectedIndex() + 1;
        } else if (e.getActionCommand().equals("Predefined")) {
            ccenter.setVisible(false);
            monpane.setVisible(true);
            number = monsterlist.getSelectedIndex();
            mousePressed(new MouseEvent(monsterlist, MouseEvent.MOUSE_PRESSED, 0, 0, 0, number * 34 + 1, 1, false));
            //if (number==9 || number==10 || number==13 || number==16 || number==19 || number==22 || number==25 || number==26 || number==27) {
            //        sub5.setSelected(true);
            //        sub5.setEnabled(false);
            //}
            //if (number==2) immaterialbut.setEnabled(false);
        } else if (e.getActionCommand().equals("Custom")) {
            monpane.setVisible(false);
            ccenter.setVisible(true);
            cmonsterlist.clearSelection();
            cmonsterindex = -1;
            name.setText("");
            sub5.setSelected(false);
            sub5.setEnabled(true);
            if (immaterialbut.isSelected()) immaterialbut.doClick();
            carrying.clear();
            equipped.clear();
            carryingnames.clear();
            carryinglist.setListData(carryingnames);
        } else if (e.getActionCommand().equals("Add/Update Mon")) {
            if (name.getText().equals("")) return;
            data = new MonsterData(28, level, x, y, Integer.parseInt(health.getText()), Integer.parseInt(mana.getText()), facing, ai, HITANDRUN, isImmaterial, Integer.parseInt(power.getText()), Integer.parseInt(defense.getText()), Integer.parseInt(magicresist.getText()), Integer.parseInt(speed.getText()), Integer.parseInt(poison.getText()), Integer.parseInt(fearresist.getText()), carrying, equipped, gamewin, endanim, endsound, hurtitem, needitem, needhandneck, sub5.isSelected(), pickup.getSelectedIndex(), steal.getSelectedIndex(), name.getText(), picstring.getText(), soundstring.getText(), footstep.getText(), canusestairsbut.isSelected(), isflyingbut.isSelected(), !ignoremonsbut.isSelected(), canteleport.isSelected(), poisonimmune.isSelected());
            int index = cmonsterlist.getSelectedIndex();
            if (index == -1) index = custommons.indexOf(data);
            if (index == -1) index = custommons.size();
            number = 28 + index;
            data.number = number;
            finishCreation();
            if (index < custommons.size()) custommons.set(index, data);
            else {
                custommons.add(data);
                cmonsterlist.setPreferredSize(new Dimension(182, custommons.size() * 34));
            }
            cmonsterlist.setListData(custommons);
            cmonsterlist.setSelectedIndex(index);
            cmonsterindex = index;
            DMEditor.NEEDSAVEMONS = true;
        } else if (e.getActionCommand().equals("Delete Mon")) {
            int index = cmonsterlist.getSelectedIndex();
            if (index == -1) return;
            custommons.remove(index);
            for (int i = index; i < custommons.size(); i++) {
                //subtract one from numbers of mons after deleted one -> could cause number sharing!
                ((MonsterData) custommons.get(i)).number--;
            }
            cmonsterlist.setListData(custommons);
            cmonsterlist.setPreferredSize(new Dimension(182, custommons.size() * 34));
            cmonsterindex = -1;
            DMEditor.NEEDSAVEMONS = true;
        }
    }
    
    //determines which list is selected
    private ArrayList getActualList(int index) {
        ArrayList whichlist = carrying;
        if (((String) carryingnames.get(index)).endsWith("(E)")) whichlist = equipped;
        return whichlist;
    }
    
    //determines index in whichlist of selected index
    private int getActualIndex(ArrayList whichlist, int index) {
        int actualindex = 0;
        for (int i = 0; i < index; i++) {
            String cname = (String) carryingnames.get(i);
            if (cname.endsWith("(E)")) {
                if (whichlist == equipped) actualindex++;
            } else if (whichlist == carrying) actualindex++;
        }
        return actualindex;
    }
    
    private void finishCreation() {
        data.timecounter = randGen.nextInt(100);
        data.movecounter = randGen.nextInt(10);
        data.movespeed = Integer.parseInt(movespeed.getText());
        data.attackspeed = Integer.parseInt(attackspeed.getText());
        if (data.movespeed <= 0) data.movespeed = 1;
        if (data.attackspeed >= data.movespeed) data.attackspeed = data.movespeed - 1;
        if (hasmagic.isSelected()) {
            data.hasmagic = true;
            data.hasheal = hasheal.isSelected();
            data.hasdrain = hasdrain.isSelected();
            data.castpower = castpower.getSelectedIndex() + 1;
            data.manapower = Integer.parseInt(manapower.getText());
            data.numspells = spells.size();
            //int minproj = 55;//666 drain
            int minproj = 24;//666 drain
            if (data.numspells > 0) {
                data.knownspells = new String[data.numspells];
                minproj = 21;//664 strip defenses
                for (int i = 0; i < data.numspells; i++) {
                    data.knownspells[i] = (String) spells.get(i);
                    if (minproj > 17 && (data.knownspells[i].equals("363") || data.knownspells[i].equals("335") || data.knownspells[i].equals("642") || data.knownspells[i].equals("461")))
                        minproj = 17;
                        //else if (minproj>17 && data.knownspells[i].equals("335")) minproj=17;
                        //else if (minproj>17 && data.knownspells[i].equals("642")) minproj=17;
                        //else if (minproj>17 && data.knownspells[i].equals("461")) minproj=17;
                    else if (minproj > 16 && data.knownspells[i].equals("362")) minproj = 16;
                    else if (minproj > 15 && data.knownspells[i].equals("523")) minproj = 15;
                    else if (minproj > 13 && data.knownspells[i].equals("44")) minproj = 13;
                    else if (minproj > 11 && data.knownspells[i].equals("51")) minproj = 11;
                    else if (minproj > 9 && data.knownspells[i].equals("31")) minproj = 9;
                    else if (minproj > 8 && data.knownspells[i].equals("6")) minproj = 8;
                }
                if (number == 25 && spells.contains("44")) minproj = 0;
            }
            data.minproj = minproj;
        }
        if (useammo.isSelected()) {
            data.useammo = true;
            data.ammo = 0;
            for (int i = 0; i < carrying.size(); i++) {
                Item tempitem = (Item) carrying.get(i);
                if (tempitem.number > 220 && (tempitem.hasthrowpic || tempitem.number == 266)) data.ammo++;
            }
        }
        carrying = new ArrayList(1);
        equipped = new ArrayList(3);
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.getSource().equals(carryinglist)) {
            int clickedindex = carryinglist.locationToIndex(e.getPoint());
            if (clickedindex == -1 || clickedindex == carryingindex) carryinglist.clearSelection();
            carryingindex = carryinglist.getSelectedIndex();
        } else if (e.getSource().equals(cmonsterlist)) {
            int clickedindex = cmonsterlist.locationToIndex(e.getPoint());
            if (clickedindex == -1 || clickedindex == cmonsterindex) cmonsterlist.clearSelection();
            cmonsterindex = cmonsterlist.getSelectedIndex();
            if (cmonsterindex != -1) {
                //set field stuff kind of like below
                data = (MonsterData) custommons.get(cmonsterindex);
                name.setText(data.name);
                picstring.setText(data.picstring);
                soundstring.setText(data.soundstring);
                footstep.setText(data.footstep);
                health.setText("" + data.health);
                mana.setText("" + data.mana);
                power.setText("" + data.power);
                defense.setText("" + data.defense);
                magicresist.setText("" + data.magicresist);
                speed.setText("" + data.speed);
                fearresist.setText("" + data.fearresist);
                poison.setText("" + data.poison);
                movespeed.setText("" + data.movespeed);
                attackspeed.setText("" + data.attackspeed);
                ai = data.defaultai;
                aibox.setSelectedIndex(ai - 1);
                carrying.clear();
                equipped.clear();
                carryingnames.clear();
                if (data.equipped != null) {
                    Item it;
                    for (int i = 0; i < data.equipped.size(); i++) {
                        it = Item.createCopy((Item) data.equipped.get(i));
                        equipped.add(it);
                        carryingnames.add(it.name + " (E)");
                    }
                }
                carryinglist.setListData(carryingnames);
                spells.clear();
                spellnames.clear();
                if (data.hasmagic) {
                    if (!hasmagic.isSelected()) hasmagic.doClick();
                    hasheal.setSelected(data.hasheal);
                    hasdrain.setSelected(data.hasdrain);
                    castpower.setSelectedIndex(data.castpower - 1);
                    manapower.setText("" + data.manapower);
                    for (int i = 0; i < data.knownspells.length; i++) {
                        spells.add(data.knownspells[i]);
                        if (data.knownspells[i].equals("44")) spellnames.add("Fireball");
                        else if (data.knownspells[i].equals("335")) spellnames.add("Lightning");
                        else if (data.knownspells[i].equals("51")) spellnames.add("Poison Bolt");
                        else if (data.knownspells[i].equals("31")) spellnames.add("Ven Cloud");
                        else if (data.knownspells[i].equals("642")) spellnames.add("Arc Bolt");
                        else if (data.knownspells[i].equals("461")) spellnames.add("Weakness");
                        else if (data.knownspells[i].equals("363")) spellnames.add("Feeble Mind");
                        else if (data.knownspells[i].equals("362")) spellnames.add("Slow");
                        else if (data.knownspells[i].equals("664")) spellnames.add("Strip Defenses");
                        else if (data.knownspells[i].equals("523")) spellnames.add("Silence");
                        else if (data.knownspells[i].equals("6")) spellnames.add("Door Open");
                    }
                    knownspells.setListData(spellnames);
                } else if (hasmagic.isSelected()) hasmagic.doClick();
                useammo.setSelected(data.useammo);
                if (data.isImmaterial != immaterialbut.isSelected()) immaterialbut.doClick();
                HITANDRUN = data.HITANDRUN;
                hitandrunbut.setSelected(HITANDRUN);
                canusestairsbut.setSelected(data.canusestairs);
                ignoremonsbut.setSelected(!data.ignoremons);
                isflyingbut.setSelected(data.isflying);
                canteleport.setSelected(data.canteleport);
                sub5.setSelected(data.subsquare == 5);
                pickup.setSelectedIndex(data.pickup);
                steal.setSelectedIndex(data.steal);
                poisonimmune.setSelected(data.poisonimmune);
            }
        } else {
            number = monsterlist.getSelectedIndex();
            //set default stuff
            health.setText("" + defaulthealth[number]);
            mana.setText("" + defaultmana[number]);
            power.setText("" + defaultpower[number]);
            defense.setText("" + defaultdefense[number]);
            magicresist.setText("" + defaultmresist[number]);
            speed.setText("" + defaultspeed[number]);
            fearresist.setText("" + defaultfresist[number]);
            poison.setText("" + defaultpoison[number]);
            movespeed.setText("" + defaultmspeed[number]);
            attackspeed.setText("" + defaultaspeed[number]);
            aibox.setSelectedIndex(defaultai[number] - 1);
            castpower.setSelectedIndex(defaultcpower[number]);
            manapower.setText("" + defaultmpower[number]);
            carrying.clear();
            equipped.clear();
            carryingnames.clear();
            spells.clear();
            spellnames.clear();
            if (hasmagic.isSelected()) hasmagic.doClick();
            hasheal.setSelected(false);
            hasdrain.setSelected(false);
            useammo.setSelected(false);
            pickup.setSelectedIndex(defaultpickup[number]);
            steal.setSelectedIndex(0);
            poisonimmune.setSelected(false);
            if (HITANDRUN) hitandrunbut.doClick();
            if (isImmaterial) immaterialbut.doClick();//sets isImmaterial to false, restores carrying buttons
            if (number == 9 || number == 10 || number == 13 || number == 16 || number == 19 || number == 22 || number == 25 || number == 26 || number == 27) {
                sub5.setSelected(true);
                sub5.setEnabled(false);
            } else {
                sub5.setSelected(false);
                sub5.setEnabled(true);
            }
            int numitems;
            //set carrying, known spells, hitandrun
            switch (number) {
                case 1: //screamer
                    //add screamer slices
                    numitems = randGen.nextInt(3) + 2;
                    for (int i = 0; i < numitems; i++) {
                        equipped.add(new Item(65)); //screamer slice
                    }
                    break;
                case 2: //giggler
                    hitandrunbut.doClick();
                    carryinglist.setListData(carryingnames);
                    steal.setSelectedIndex(4);
                    return;
                case 3: //rock pile
                    //add rocks
                    numitems = randGen.nextInt(3) + 2;
                    for (int i = 0; i < numitems; i++) {
                        equipped.add(new Item(266));//rock
                    }
                    equipped.add(new Item(76));//boulder
                    if (randGen.nextBoolean()) {
                        equipped.add(new Item(76));//boulder
                    }
                    break;
                case 4: //slime
                    poisonimmune.setSelected(true);
                    break;
                case 5: //wing eye
                    spells.add("335");
                    spells.add("664");
                    spells.add("335");
                    spellnames.add("Lightning");
                    spellnames.add("Strip Defenses");
                    spellnames.add("Lightning");
                    knownspells.setListData(spellnames);
                    hasmagic.doClick();
                    break;
                case 8: //skeleton
                    //add sword/shield
                    equipped.add(new Item(200));//falchion
                    equipped.add(new Item(104));//wooden shield
                    poisonimmune.setSelected(true);
                    break;
                case 9: //worm
                    //add worm rounds
                    numitems = randGen.nextInt(4) + 1;
                    for (int i = 0; i < numitems; i++) {
                        equipped.add(new Item(61)); //worm round
                    }
                    break;
                case 12://goblin
                    numitems = randGen.nextInt(5) + 4;
                    for (int i = 0; i < numitems; i++) {
                        carrying.add(new Item(267)); //dagger
                    }
                    useammo.setSelected(true);
                    break;
                case 13://rat
                    //add drumsticks
                    equipped.add(new Item(67));//drumstick, maybe 2
                    if (randGen.nextBoolean()) {
                        equipped.add(new Item(67));
                    }
                    break;
                case 14://ant man
                    //add club
                    equipped.add(new Item(226));//club
                    break;
                case 15://beholder
                    spells.add("6");
                    spells.add("44");
                    spells.add("335");
                    spells.add("642");
                    spells.add("363");
                    spells.add("362");
                    spellnames.add("Door Open");
                    spellnames.add("Fireball");
                    spellnames.add("Lightning");
                    spellnames.add("Arc Bolt");
                    spellnames.add("Feeble Mind");
                    spellnames.add("Slow");
                    knownspells.setListData(spellnames);
                    hasheal.setSelected(true);
                    hasdrain.setSelected(true);
                    hasmagic.doClick();
                    break;
                case 17://fader
                    immaterialbut.doClick();
                    spells.add("44");
                    spells.add("523");
                    spells.add("664");
                    spells.add("44");
                    spellnames.add("Fireball");
                    spellnames.add("Silence");
                    spellnames.add("Strip Defenses");
                    spellnames.add("Fireball");
                    knownspells.setListData(spellnames);
                    hasmagic.doClick();
                    break;
                case 18://tentacle beast
                    spells.add("461");
                    spellnames.add("Weakness");
                    poisonimmune.setSelected(true);
                case 22://spider
                    spells.add("51");
                    spells.add("31");
                    spellnames.add("Poison Bolt");
                    spellnames.add("Ven Cloud");
                    knownspells.setListData(spellnames);
                    hasmagic.doClick();
                    break;
                case 20://demon
                    spells.add("44");
                    spells.add("461");
                    spells.add("44");
                    spells.add("44");
                    spellnames.add("Fireball");
                    spellnames.add("Weakness");
                    spellnames.add("Fireball");
                    spellnames.add("Fireball");
                    knownspells.setListData(spellnames);
                    hasmagic.doClick();
                    break;
                case 21://knight
                    //2 cursed swords
                    Item sword = new Item(201);
                    sword.cursed = 100;
                    sword.haseffect = true;
                    sword.effect = new String[2];
                    sword.effect[0] = "strength,-5";
                    sword.effect[1] = "vitality,-5";
                    equipped.add(sword);
                    equipped.add(Item.createCopy(sword));
                    //cursed horned helmet - not created yet
                    //equipped.add(helmet);
                    //cursed torso plate
                    Item torsoplate = new Item(143);
                    torsoplate.cursed = 100;
                    torsoplate.haseffect = true;
                    torsoplate.effect = new String[2];
                    torsoplate.effect[0] = "strength,-5";
                    torsoplate.effect[1] = "vitality,-5";
                    equipped.add(torsoplate);
                    //cursed leg plate
                    Item legplate = new Item(163);
                    legplate.cursed = 100;
                    legplate.haseffect = true;
                    legplate.effect = new String[2];
                    legplate.effect[0] = "strength,-5";
                    legplate.effect[1] = "vitality,-5";
                    equipped.add(legplate);
                    //cursed foot plate
                    Item footplate = new Item(181);
                    footplate.cursed = 100;
                    footplate.haseffect = true;
                    footplate.effect = new String[2];
                    footplate.effect[0] = "strength,-5";
                    footplate.effect[1] = "vitality,-5";
                    equipped.add(footplate);
                    //deth knights immune to poison
                    poisonimmune.setSelected(true);
                    break;
                case 23://golem
                    //add stone club and rocks
                    equipped.add(new Item(227));//stone club
                    numitems = randGen.nextInt(3) + 2;
                    for (int i = 0; i < numitems; i++) {
                        equipped.add(new Item(266));//rock
                    }
                    equipped.add(new Item(76));//boulder
                    if (randGen.nextBoolean()) {
                        equipped.add(new Item(76));//boulder
                    }
                    poisonimmune.setSelected(true);
                    break;
                case 24://sorcerer
                    spells.add("642");
                    spells.add("363");
                    spellnames.add("Arc Bolt");
                    spellnames.add("Feeble Mind");
                case 26://chaos
                    spells.add("44");
                    spells.add("335");
                    spells.add("51");
                    spells.add("31");
                    spells.add("664");
                    spells.add("523");
                    spells.add("461");
                    spells.add("362");
                    spellnames.add("Fireball");
                    spellnames.add("Lightning");
                    spellnames.add("Poison Bolt");
                    spellnames.add("Ven Cloud");
                    spellnames.add("Strip Defenses");
                    spellnames.add("Silence");
                    spellnames.add("Weakness");
                    spellnames.add("Slow");
                    hasheal.setSelected(true);
                    hasdrain.setSelected(true);
                    knownspells.setListData(spellnames);
                    hasmagic.doClick();
                    poisonimmune.setSelected(true);
                    break;
                case 25://dragon
                    //add steak
                    numitems = randGen.nextInt(4) + 4;
                    for (int i = 0; i < numitems; i++) {
                        equipped.add(new Item(69)); //dragon steak
                    }
                    spells.add("44");
                    spellnames.add("Fireball");
                    knownspells.setListData(spellnames);
                    hasmagic.doClick();
                    break;
                case 27://demon lord
                    spells.add("44");
                    spells.add("664");
                    spells.add("44");
                    spells.add("44");
                    spellnames.add("Fireball");
                    spellnames.add("Strip Defenses");
                    spellnames.add("Fireball");
                    spellnames.add("Fireball");
                    hasdrain.setSelected(true);
                    knownspells.setListData(spellnames);
                    hasmagic.doClick();
                    break;
                case 6: //ghost
                case 10://fire el
                case 11://water el
                    immaterialbut.doClick();
                    break;
            }
            for (int i = 0; i < equipped.size(); i++) carryingnames.add(((Item) equipped.get(i)).name + " (E)");
            for (int i = 0; i < carrying.size(); i++) carryingnames.add(((Item) carrying.get(i)).name);
            carryinglist.setListData(carryingnames);
        }
    }
        
        /*
        public void valueChanged(ListSelectionEvent e) {
                if (e.getSource()==monsterlist) {
                        System.out.println(e);
                        //mousePressed(new MouseEvent(monsterlist,MouseEvent.MOUSE_PRESSED,0,0,0,e.getFirstIndex()*34,1,false));
                }
                else {
                        System.out.println(e);
                        //mousePressed(new MouseEvent(cmonsterlist,MouseEvent.MOUSE_PRESSED,0,0,0,e.getFirstIndex()*19,1,false));
                }
        }
        */
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
}