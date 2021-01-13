import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

class ItemCreator extends JDialog implements ActionListener, FilenameFilter {
    private Item item;
    private int TYPE, ARMORTYPE = Item.HEAD;
    private static final String[] SIZES = {"0", "1", "2", "3", "4"};//see dung.txt or Item.java
    private static final String[] POISONS = {"None", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final String[] FOODS = {"None", "150", "200", "250", "300", "350", "400", "450", "500", "550", "600", "700", "800", "900", "1000"};
    public static final String[] FUNCTIONS = {
        "None", "Punch", "Kick", "Stab", "Jab", "Swing", "Bash", "Slash", "Chop", "Melee", "Stun", "Thrust", "Crush", "Cleave", "Berzerk", "Disrupt", "Parry", "Throw", "Shoot",
        
        "Anti-Ven", "Arc Bolt", "Armor", "Armor Party", "Backstab", "Berserker", "Blow Horn", "Bolt", "Calm", "Climb Down", "Climb Up",
        "Detect Illusion", "Dispell", "Drain Life", "Drain Mana", "False Image", "Feeble Mind", "Fireball",
        "Freeze", "Freeze Life", "Frighten", "Good Berries", "Heal", "Invoke", "Light", "Purify", "Ruiner", "Shield", "Shield Party",
        "Sight", "Silence", "Slow", "Slowfall", "Spellshield", "Spellshield Party", "Steal", "Strip Defenses", "True Sight",
        "Ven Cloud", "Venom", "War Cry", "Weakness", "ZO",
        
    };
    
    public static final String[] CLASSES = {"Fighter", "Ninja", "Wizard", "Priest", "None"};
    public static final String[] LEVELS = {"None", "Neophyte", "Novice", "Apprentice", "Journeyman", "Crafstman", "Artisan", "Adept", "Expert", "LO Master", "UM Master", "ON Master", "EE Master", "PAL Master", "MON Master", "ArchMaster"};
    private static final String[] AMMO = {"None", "Arrows", "Rocks", "Darts", "Stars", "Daggers", "Fits Quiver"};
    private static final int[] defaultclass = {0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 3, 2, 3, 3, 1, 0, 3, 2, 3, 1, 1, 1, 2, 2, 2, 1, 2, 2, 3, 3, 3, 3, 3, 2, 2, 3, 2, 3, 3, 3, 3, 2, 3, 3, 3, 1, 2, 3, 2, 2, 3, 2, 2};
    private static final int[] defaultlvlneed = {0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 4, 6, 5, 7, 8, 4, 1, 0, 0, 1, 1, 1, 1, 4, 4, 0, 1, 1, 0, 7, 9, 1, 4, 8, 9, 1, 1, 0, 1, 0, 4, 4, 0, 0, 8, 6, 1, 2, 0, 4, 1, 1, 2, 3, 4, 1, 9, 1, 1, 0, 1, 1};
    private static final int[] defaultpower = {0, 1, 2, 6, 6, 6, 7, 10, 10, 16, 14, 16, 19, 18, 25, 20, 4, 0, 10, 15, 3, 10, 10, 0, 3, 6, 3, 4, 0, 0, 0, 3, 50, 20, 1, 3, 3, 125, 100, 3, 20, 40, 0, 100, 0, 3, 15, 10, 25, 3, 3, 20, 15, 10, 20, 3, 20, 3, 3, 2, 3, 3};
    private static final int[] defaultspeed = {0, 3, 7, 8, 7, 10, 12, 9, 16, 18, 16, 18, 20, 19, 25, 20, 14, 10, 15, 20, 20, 20, 20, 5, 5, 15, 20, 20, 10, 20, 25, 20, 20, 25, 20, 20, 20, 14, 20, 20, 20, 25, 18, 10, 20, 25, 20, 20, 15, 20, 20, 20, 20, 20, 20, 20, 30, 20, 20, 12, 20, 20};
    private static final int[] defaultcharges = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, -1, 20, -1, 20, 20, -1, -1, -1, 20, 20, 20, 20, 20, 20, 1, 1, 20, 20, 20, -1, 1, 20, 10, 20, 20, 20, 20, 20, 20, 20, 20, -1, 20, 10, 20, 20, -1, 20, 20};
    private Container cp;
    private JPanel equippan, weaponpan, armorpan, miscpan, picturespan, picpan, dpicpan, epicpan, upicpan, throwpicpan0, throwpicpan1, throwpicpan2, throwpicpan3, cursepan;
    private JTextField name, defense, magicresist, health, stamina, mana, strength, dexterity, vitality, intelligence, wisdom, fighter, ninja, wizard, priest;
    private JTextField picstring, dpicstring, epicstring, upicstring, throwpicstring;
    private ImageIcon pic, dpic, epic, upic, throwpic0, throwpic1, throwpic2, throwpic3;
    private JScrollPane picscrollpan;
    private JTextField weight, throwpower, cursed;
    private JTextField[] power, speed, charges;
    private JComboBox size, poison, projtype, food, foodpoison;
    private JComboBox[] functions, classes, lvlneed;
    private JToggleButton weapon, armor, misc, hitsImmaterial;
    private JToggleButton[] armortype = new JToggleButton[6];
    private String oldname = null; //used for predefined item editing -> if name changed becomes a custom item
    private FileDialog dialog;
    private Toolkit tk;
    private JFrame frame;
    
    public ItemCreator(JFrame f) {
        super(f, "Custom Item Creation", true);
        frame = f;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(953, 570);//was 903,570
        setLocationRelativeTo(null);
        cp = getContentPane();
        
        //name panel
        JPanel namepan = new JPanel();
        name = new JTextField("New Item", 20);
        namepan.add(new JLabel("Name:"));
        namepan.add(name);
        
        //item type panel -> weapon,armor,misc
        JPanel typepan = new JPanel();
        weapon = new JToggleButton("Weapon");
        armor = new JToggleButton("Armor");
        misc = new JToggleButton("Misc");
        weapon.addActionListener(this);
        armor.addActionListener(this);
        misc.addActionListener(this);
        ButtonGroup typegrp = new ButtonGroup();
        typegrp.add(weapon);
        typegrp.add(armor);
        typegrp.add(misc);
        weapon.setSelected(true);
        typepan.add(weapon);
        typepan.add(armor);
        typepan.add(misc);
        
        //north panel -> name and type
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.add(namepan);
        north.add(typepan);
        north.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Item Name And Type"));
        
        //weapon panel
        weaponpan = new JPanel();
        weaponpan.setLayout(new BoxLayout(weaponpan, BoxLayout.Y_AXIS));
        JPanel functionpan = new JPanel();
        functions = new JComboBox[3];
        functions[0] = new JComboBox(FUNCTIONS);
        functions[1] = new JComboBox(FUNCTIONS);
        functions[2] = new JComboBox(FUNCTIONS);
        functions[0].setEditable(true);
        functions[0].setSelectedIndex(0);
        functions[0].setActionCommand("f0");
        functions[0].addActionListener(this);
        functions[1].setEditable(true);
        functions[1].setSelectedIndex(0);
        functions[1].setActionCommand("f1");
        functions[1].addActionListener(this);
        functions[2].setEditable(true);
        functions[2].setSelectedIndex(0);
        functions[2].setActionCommand("f2");
        functions[2].addActionListener(this);
        classes = new JComboBox[3];
        classes[0] = new JComboBox(CLASSES);
        classes[1] = new JComboBox(CLASSES);
        classes[2] = new JComboBox(CLASSES);
        classes[0].setEditable(false);
        classes[0].setSelectedIndex(0);
        classes[1].setEditable(false);
        classes[1].setSelectedIndex(0);
        classes[2].setEditable(false);
        classes[2].setSelectedIndex(0);
        lvlneed = new JComboBox[3];
        lvlneed[0] = new JComboBox(LEVELS);
        lvlneed[1] = new JComboBox(LEVELS);
        lvlneed[2] = new JComboBox(LEVELS);
        lvlneed[0].setEditable(false);
        lvlneed[0].setSelectedIndex(0);
        lvlneed[1].setEditable(false);
        lvlneed[1].setSelectedIndex(0);
        lvlneed[2].setEditable(false);
        lvlneed[2].setSelectedIndex(0);
        power = new JTextField[3];
        power[0] = new JTextField("0", 4);
        power[1] = new JTextField("0", 4);
        power[2] = new JTextField("0", 4);
        speed = new JTextField[3];
        speed[0] = new JTextField("0", 4);
        speed[1] = new JTextField("0", 4);
        speed[2] = new JTextField("0", 4);
        charges = new JTextField[3];
        charges[0] = new JTextField("0", 4);
        charges[1] = new JTextField("0", 4);
        charges[2] = new JTextField("0", 4);
        JPanel fpan0 = new JPanel();
        JPanel fpan1 = new JPanel();
        JPanel fpan2 = new JPanel();
        fpan0.setLayout(new BoxLayout(fpan0, BoxLayout.Y_AXIS));
        fpan1.setLayout(new BoxLayout(fpan1, BoxLayout.Y_AXIS));
        fpan2.setLayout(new BoxLayout(fpan2, BoxLayout.Y_AXIS));
        Dimension fdim = new Dimension(135, 170);//was 115,170
        fpan0.setPreferredSize(fdim);
        fpan1.setPreferredSize(fdim);
        fpan2.setPreferredSize(fdim);
        fpan0.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Function 1"));
        fpan1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Function 2"));
        fpan2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Function 3"));
        fpan0.add(functions[0]);
        fpan0.add(classes[0]);
        fpan0.add(lvlneed[0]);
        //Font smallfont = new Font("TimesRoman",Font.PLAIN,11);
        JPanel ppan0 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel plab0 = new JLabel("Power:");
        //plab0.setFont(smallfont);
        ppan0.add(plab0);
        ppan0.add(power[0]);
        JPanel span0 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel slab0 = new JLabel("Speed:");
        //slab0.setFont(smallfont);
        span0.add(slab0);
        span0.add(speed[0]);
        JPanel cpan0 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel clab0 = new JLabel("Charges:");
        //clab0.setFont(smallfont);
        cpan0.add(clab0);
        cpan0.add(charges[0]);
        fpan0.add(ppan0);
        fpan0.add(span0);
        fpan0.add(cpan0);
        fpan1.add(functions[1]);
        fpan1.add(classes[1]);
        fpan1.add(lvlneed[1]);
        JPanel ppan1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel plab1 = new JLabel("Power:");
        //plab1.setFont(smallfont);
        ppan1.add(plab1);
        ppan1.add(power[1]);
        JPanel span1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel slab1 = new JLabel("Speed:");
        //slab1.setFont(smallfont);
        span1.add(slab1);
        span1.add(speed[1]);
        JPanel cpan1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel clab1 = new JLabel("Charges:");
        //clab1.setFont(smallfont);
        cpan1.add(clab1);
        cpan1.add(charges[1]);
        fpan1.add(ppan1);
        fpan1.add(span1);
        fpan1.add(cpan1);
        fpan2.add(functions[2]);
        fpan2.add(classes[2]);
        fpan2.add(lvlneed[2]);
        JPanel ppan2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel plab2 = new JLabel("Power:");
        //plab2.setFont(smallfont);
        ppan2.add(plab2);
        ppan2.add(power[2]);
        JPanel span2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel slab2 = new JLabel("Speed:");
        //slab2.setFont(smallfont);
        span2.add(slab2);
        span2.add(speed[2]);
        JPanel cpan2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel clab2 = new JLabel("Charges:");
        //clab2.setFont(smallfont);
        cpan2.add(clab2);
        cpan2.add(charges[2]);
        fpan2.add(ppan2);
        fpan2.add(span2);
        fpan2.add(cpan2);
        functionpan.add(fpan0);
        functionpan.add(fpan1);
        functionpan.add(fpan2);
        poison = new JComboBox(POISONS);
        poison.setSelectedIndex(0);
        poison.setEditable(true);
        hitsImmaterial = new JToggleButton("Hits Non-Material");
        projtype = new JComboBox(AMMO);
        projtype.setEditable(false);
        projtype.setSelectedIndex(0);
        JPanel wpan0 = new JPanel();
        wpan0.add(hitsImmaterial);
        JPanel wpan1 = new JPanel();
        wpan1.add(new JLabel("Poison Value:"));
        wpan1.add(poison);
        JPanel wpan2 = new JPanel();
        wpan2.add(new JLabel("Ammo Type:"));
        wpan2.add(projtype);
        weaponpan.add(functionpan);
        weaponpan.add(wpan0);
        weaponpan.add(wpan1);
        weaponpan.add(wpan2);
        
        //armor panel
        armorpan = new JPanel(new GridLayout(2, 3, 5, 5));
        armortype[0] = new JToggleButton("Head");
        armortype[1] = new JToggleButton("Neck");
        armortype[2] = new JToggleButton("Torso");
        armortype[3] = new JToggleButton("Leg");
        armortype[4] = new JToggleButton("Foot");
        armortype[5] = new JToggleButton("Shield");
        armortype[0].addActionListener(this);
        armortype[1].addActionListener(this);
        armortype[2].addActionListener(this);
        armortype[3].addActionListener(this);
        armortype[4].addActionListener(this);
        armortype[5].addActionListener(this);
        ButtonGroup armortypegrp = new ButtonGroup();
        armortypegrp.add(armortype[0]);
        armortypegrp.add(armortype[1]);
        armortypegrp.add(armortype[2]);
        armortypegrp.add(armortype[3]);
        armortypegrp.add(armortype[4]);
        armortypegrp.add(armortype[5]);
        armortype[0].setSelected(true);
        armorpan.add(armortype[0]);
        armorpan.add(armortype[1]);
        armorpan.add(armortype[2]);
        armorpan.add(armortype[3]);
        armorpan.add(armortype[4]);
        armorpan.add(armortype[5]);
        armorpan.setVisible(false);
        
        //misc panel
        miscpan = new JPanel();
        miscpan.setLayout(new BoxLayout(miscpan, BoxLayout.Y_AXIS));
        food = new JComboBox(FOODS);
        food.setSelectedIndex(0);
        food.setEditable(true);
        JPanel foodpan1 = new JPanel();
        foodpan1.add(new JLabel("Food Value:"));
        foodpan1.add(food);
        miscpan.add(foodpan1);
        foodpoison = new JComboBox(POISONS);
        foodpoison.setSelectedIndex(0);
        foodpoison.setEditable(true);
        JPanel foodpan2 = new JPanel();
        foodpan2.add(new JLabel("Poison Value:"));
        foodpan2.add(foodpoison);
        miscpan.add(foodpan2);
        miscpan.setVisible(false);
        
        //statistics panel -> weight, size, throwpower, along with weapon,armor,misc panels
        JPanel statpan = new JPanel();
        JPanel basestatpan = new JPanel();
        JPanel bslabels = new JPanel(new GridLayout(3, 1, 2, 9));
        bslabels.add(new JLabel("Weight:"));
        bslabels.add(new JLabel("Size:"));
        bslabels.add(new JLabel("Throw Power:"));
        weight = new JTextField("0.1", 4);
        size = new JComboBox(SIZES);
        size.setEditable(false);
        size.setSelectedIndex(0);
        throwpower = new JTextField("1", 4);
        JPanel bsfields = new JPanel(new GridLayout(3, 1, 2, 2));
        bsfields.add(weight);
        bsfields.add(size);
        bsfields.add(throwpower);
        basestatpan.add(bslabels);
        basestatpan.add(Box.createHorizontalStrut(5));
        basestatpan.add(bsfields);
        statpan.add(miscpan);
        statpan.add(weaponpan);
        statpan.add(armorpan);
        statpan.add(basestatpan);
        cursepan = new JPanel();
        cursepan.add(new JLabel("Curse Strength:"));
        cursed = new JTextField("0", 3);
        cursepan.add(cursed);
        statpan.add(cursepan);
        
        //center panel -> holds stat panel, weapon, armor, and misc panels
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(statpan);
        center.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Item Attributes"));
        
        //equip effects panel (shared between weapon and armor panels)
        equippan = new JPanel();
        defense = new JTextField("0", 4);
        magicresist = new JTextField("0", 4);
        health = new JTextField("0", 4);
        stamina = new JTextField("0", 4);
        mana = new JTextField("0", 4);
        strength = new JTextField("0", 4);
        dexterity = new JTextField("0", 4);
        vitality = new JTextField("0", 4);
        intelligence = new JTextField("0", 4);
        wisdom = new JTextField("0", 4);
        fighter = new JTextField("0", 4);
        ninja = new JTextField("0", 4);
        wizard = new JTextField("0", 4);
        priest = new JTextField("0", 4);
        JPanel labelpanel = new JPanel();
        labelpanel.setLayout(new GridLayout(14, 2, 4, 2));
        labelpanel.add(new JLabel("Defense:"));
        labelpanel.add(defense);
        labelpanel.add(new JLabel("Magic Resist:"));
        labelpanel.add(magicresist);
        labelpanel.add(new JLabel("Health:"));
        labelpanel.add(health);
        labelpanel.add(new JLabel("Stamina:"));
        labelpanel.add(stamina);
        labelpanel.add(new JLabel("Mana:"));
        labelpanel.add(mana);
        labelpanel.add(new JLabel("Strength:"));
        labelpanel.add(strength);
        labelpanel.add(new JLabel("Dexterity:"));
        labelpanel.add(dexterity);
        labelpanel.add(new JLabel("Vitality:"));
        labelpanel.add(vitality);
        labelpanel.add(new JLabel("Intelligence:"));
        labelpanel.add(intelligence);
        labelpanel.add(new JLabel("Wisdom:"));
        labelpanel.add(wisdom);
        labelpanel.add(new JLabel("Fighter Level:"));
        labelpanel.add(fighter);
        labelpanel.add(new JLabel("Ninja Level:"));
        labelpanel.add(ninja);
        labelpanel.add(new JLabel("Wizard Level:"));
        labelpanel.add(wizard);
        labelpanel.add(new JLabel("Priest Level:"));
        labelpanel.add(priest);
        equippan.add(labelpanel);
        equippan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Equip Effects"));
        
        //west panel -> equip effects, etc.?
        JPanel west = new JPanel();
        west.add(equippan);
        
        //pictures panel -> pic, dpic, epic, upic, throwpics (with textfields for filenames)
        picturespan = new JPanel(new GridLayout(8, 1, 5, 5));
        
        picpan = new JPanel();
        picpan.setLayout(new BoxLayout(picpan, BoxLayout.Y_AXIS));
        picstring = new JTextField("rock.gif", 15);
        picstring.setActionCommand("Refresh Pics");
        picstring.addActionListener(this);
        pic = new ImageIcon("Items" + File.separator + picstring.getText());
        JButton picbut = new JButton("Browse");
        picbut.setActionCommand("pic");
        picbut.addActionListener(this);
        JPanel ppan = new JPanel();
        ppan.add(picstring);
        ppan.add(picbut);
        picpan.add(new JLabel(pic));
        picpan.add(ppan);
        picpan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Inventory View"));
        
        dpicpan = new JPanel();
        dpicpan.setLayout(new BoxLayout(dpicpan, BoxLayout.Y_AXIS));
        dpicstring = new JTextField("drock.gif", 15);
        dpicstring.setActionCommand("Refresh Pics");
        dpicstring.addActionListener(this);
        dpic = new ImageIcon("Items" + File.separator + dpicstring.getText());
        JButton dpicbut = new JButton("Browse");
        dpicbut.setActionCommand("dpic");
        dpicbut.addActionListener(this);
        JPanel dpan = new JPanel();
        dpan.add(dpicstring);
        dpan.add(dpicbut);
        dpicpan.add(new JLabel(dpic));
        dpicpan.add(dpan);
        dpicpan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Dungeon Floor View"));
        
        epicpan = new JPanel();
        epicpan.setLayout(new BoxLayout(epicpan, BoxLayout.Y_AXIS));
        epicstring = new JTextField(15);
        epicstring.setActionCommand("Refresh Pics");
        epicstring.addActionListener(this);
        epic = new ImageIcon();
        JButton epicbut = new JButton("Browse");
        epicbut.setActionCommand("epic");
        epicbut.addActionListener(this);
        JPanel epan = new JPanel();
        epan.add(epicstring);
        epan.add(epicbut);
        JLabel epiclabel = new JLabel(epic);
        epiclabel.setBackground(Color.black);
        epiclabel.setForeground(Color.black);
        epiclabel.setPreferredSize(new Dimension(32, 32));
        epicpan.add(epiclabel);
        epicpan.add(epan);
        epicpan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Equipped Icon"));
        
        upicpan = new JPanel();
        upicpan.setLayout(new BoxLayout(upicpan, BoxLayout.Y_AXIS));
        upicstring = new JTextField(15);
        upicstring.setActionCommand("Refresh Pics");
        upicstring.addActionListener(this);
        upic = new ImageIcon();
        JButton upicbut = new JButton("Browse");
        upicbut.setActionCommand("upic");
        upicbut.addActionListener(this);
        JPanel upan = new JPanel();
        upan.add(upicstring);
        upan.add(upicbut);
        JLabel upiclabel = new JLabel(upic);
        upiclabel.setBackground(Color.black);
        upiclabel.setForeground(Color.black);
        upiclabel.setPreferredSize(new Dimension(32, 32));
        upicpan.add(upiclabel);
        upicpan.add(upan);
        upicpan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Out-Of-Charges Icon"));
        
        throwpicpan0 = new JPanel();
        throwpicpan1 = new JPanel();
        throwpicpan2 = new JPanel();
        throwpicpan3 = new JPanel();
        throwpicpan0.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Flying Away View"));
        throwpicpan1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Flying Toward View"));
        throwpicpan2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Flying Left View"));
        throwpicpan3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Flying Right View"));
        throwpicstring = new JTextField(15);
        throwpicstring.setActionCommand("Refresh Pics");
        throwpicstring.addActionListener(this);
        throwpic0 = new ImageIcon("Items" + File.separator + dpicstring.getText());
        throwpic1 = new ImageIcon("Items" + File.separator + dpicstring.getText());
        throwpic2 = new ImageIcon("Items" + File.separator + dpicstring.getText());
        throwpic3 = new ImageIcon("Items" + File.separator + dpicstring.getText());
        //throwpic0 = new ImageIcon("Items"+File.separator+throwpicstring.getText()+"-away.gif");
        //throwpic1 = new ImageIcon("Items"+File.separator+throwpicstring.getText()+"-toward.gif");
        //throwpic2 = new ImageIcon("Items"+File.separator+throwpicstring.getText()+"-left.gif");
        //throwpic3 = new ImageIcon("Items"+File.separator+throwpicstring.getText()+"-right.gif");
        
        JLabel tlabel0 = new JLabel(throwpic0);
        tlabel0.setBackground(Color.black);
        tlabel0.setForeground(Color.black);
        throwpicpan0.add(tlabel0);
        
        JLabel tlabel1 = new JLabel(throwpic1);
        tlabel1.setBackground(Color.black);
        tlabel1.setForeground(Color.black);
        throwpicpan1.add(tlabel1);
        
        JLabel tlabel2 = new JLabel(throwpic2);
        tlabel2.setBackground(Color.black);
        tlabel2.setForeground(Color.black);
        throwpicpan2.add(tlabel2);
        
        JLabel tlabel3 = new JLabel(throwpic3);
        tlabel3.setBackground(Color.black);
        tlabel3.setForeground(Color.black);
        throwpicpan3.add(tlabel3);
        
        picturespan.add(picpan);
        picturespan.add(dpicpan);
        picturespan.add(epicpan);
        picturespan.add(upicpan);
        picturespan.add(throwpicpan0);
        picturespan.add(throwpicpan1);
        picturespan.add(throwpicpan2);
        picturespan.add(throwpicpan3);
        picscrollpan = new JScrollPane(picturespan);
        picscrollpan.setPreferredSize(new Dimension(320, 400));
        
        JPanel updatepan = new JPanel();
        JButton updatebut = new JButton("Refresh Pics");
        updatebut.addActionListener(this);
        updatepan.add(updatebut);
        
        JPanel throwpicpan = new JPanel();
        throwpicpan.add(new JLabel("Flying View Name:"));
        throwpicpan.add(throwpicstring);
        
        //file dialog for browsing images
        dialog = new FileDialog(f, "Choose A Picture", FileDialog.LOAD);
        dialog.setDirectory("Items");
        dialog.setFilenameFilter(this);
        tk = Toolkit.getDefaultToolkit();
        dialog.setLocation(tk.getScreenSize().width / 2 - dialog.getSize().width / 2, tk.getScreenSize().height / 2 - dialog.getSize().height / 2);
        
        //east panel -> pics, etc.?
        JPanel east = new JPanel();
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.add(updatepan);
        east.add(picscrollpan);
        east.add(throwpicpan);
        east.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Item Pictures"));
        
        //bottom panel
        JPanel bottom = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottom.add(done);
        bottom.add(cancel);
        
        cp.add(north, BorderLayout.NORTH);
        cp.add(west, BorderLayout.WEST);
        cp.add(east, BorderLayout.EAST);
        cp.add(center, BorderLayout.CENTER);
        cp.add(bottom, BorderLayout.SOUTH);
    }
    
    public void setItem(Item it) {
        name.setText(it.name);
        if (it.number < 300) oldname = new String(it.name);
        else oldname = null;
        weight.setText("" + it.weight);
        size.setSelectedIndex(it.size);
        throwpower.setText("" + it.throwpow);
        cursed.setText("" + it.cursed);
        picstring.setText(it.picstring);
        dpicstring.setText(it.dpicstring);
        epicstring.setText(it.equipstring);
        upicstring.setText(it.usedupstring);
        throwpicstring.setText(it.throwpicstring);
        if (it.type == Item.WEAPON) {
            weapon.doClick();
            hitsImmaterial.setSelected(it.hitsImmaterial);
            if (it.poisonous > 0) poison.setSelectedItem("" + it.poisonous);
            else poison.setSelectedItem("None");
            projtype.setSelectedIndex(it.projtype);
            for (int i = 0; i < it.functions; i++) {
                functions[i].setSelectedItem(it.function[i][0]);
                String classstr = it.function[i][1];
                if (classstr.equals("f")) classstr = "Fighter";
                else if (classstr.equals("n")) classstr = "Ninja";
                else if (classstr.equals("w")) classstr = "Wizard";
                else if (classstr.equals("p")) classstr = "Priest";
                else classstr = "None";
                classes[i].setSelectedItem(classstr);
                lvlneed[i].setSelectedIndex(it.level[i]);
                power[i].setText("" + it.power[i]);
                speed[i].setText("" + it.speed[i]);
                charges[i].setText("" + it.charges[i]);
            }
            for (int i = it.functions; i < 3; i++) {
                functions[i].setSelectedItem("None");
                classes[i].setSelectedItem("Fighter");
                lvlneed[i].setSelectedIndex(0);
                power[i].setText("0");
                speed[i].setText("0");
                charges[i].setText("0");
            }
        } else if (it.type == Item.FOOD) {
            food.setSelectedItem("" + it.foodvalue);
            if (it.poisonous > 0) foodpoison.setSelectedItem("" + it.poisonous);
            else foodpoison.setSelectedItem("None");
            misc.doClick();
        } else if (it.type == Item.OTHER) {
            food.setSelectedItem("None");
            foodpoison.setSelectedItem("None");
            misc.doClick();
        } else {
            if (it.type == Item.HEAD) armortype[0].doClick();
            else if (it.type == Item.NECK) armortype[1].doClick();
            else if (it.type == Item.TORSO) armortype[2].doClick();
            else if (it.type == Item.LEGS) armortype[3].doClick();
            else if (it.type == Item.FEET) armortype[4].doClick();
            else if (it.type == Item.SHIELD) armortype[5].doClick();
            armor.doClick();
        }
        String e = "0";
        mana.setText(e);
        health.setText(e);
        stamina.setText(e);
        strength.setText(e);
        vitality.setText(e);
        dexterity.setText(e);
        intelligence.setText(e);
        wisdom.setText(e);
        fighter.setText(e);
        ninja.setText(e);
        wizard.setText(e);
        priest.setText(e);
        if (it.type != Item.OTHER) {
            //equip effects
            defense.setText("" + it.defense);
            magicresist.setText("" + it.magicresist);
            if (it.haseffect) {
                String whataffected;
                //String e;
                for (int i = it.effect.length; i > 0; i--) {
                    whataffected = it.effect[i - 1].substring(0, it.effect[i - 1].indexOf(','));
                    e = it.effect[i - 1].substring(it.effect[i - 1].indexOf(',') + 1);
                    whataffected.toLowerCase();
                    if (whataffected.equals("mana")) mana.setText(e);
                    else if (whataffected.equals("health")) health.setText(e);
                    else if (whataffected.equals("stamina")) stamina.setText(e);
                    else if (whataffected.equals("strength")) strength.setText(e);
                    else if (whataffected.equals("vitality")) vitality.setText(e);
                    else if (whataffected.equals("dexterity")) dexterity.setText(e);
                    else if (whataffected.equals("intelligence")) intelligence.setText(e);
                    else if (whataffected.equals("wisdom")) wisdom.setText(e);
                    else if (whataffected.equals("flevel")) fighter.setText(e);
                    else if (whataffected.equals("nlevel")) ninja.setText(e);
                    else if (whataffected.equals("wlevel")) wizard.setText(e);
                    else if (whataffected.equals("plevel")) priest.setText(e);
                }
            }
        }
        refreshPics();
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            try {
                item = new Item();
                item.name = name.getText();
                item.weight = (float) Double.parseDouble(weight.getText());
                item.size = size.getSelectedIndex();
                item.throwpow = Integer.parseInt(throwpower.getText());
                item.picstring = picstring.getText();
                item.pic = pic.getImage();//tk.getImage("Items"+File.separator+item.picstring);
                item.dpicstring = dpicstring.getText();
                if (!throwpicstring.getText().equals("")) {
                    item.hasthrowpic = true;
                    item.throwpicstring = throwpicstring.getText();
                    item.throwpic = new Image[4];
                }
                if (TYPE == 0) {
                    //weapon
                    item.type = Item.WEAPON;
                    item.cursed = Integer.parseInt(cursed.getText());
                    item.hitsImmaterial = hitsImmaterial.isSelected();
                    if (poison.getSelectedIndex() == 0) item.poisonous = 0;
                    else item.poisonous = Integer.parseInt((String) poison.getSelectedItem());
                    item.projtype = projtype.getSelectedIndex();
                    if (functions[0].getSelectedIndex() == 0 || (functions[1].getSelectedIndex() == 0 && functions[2].getSelectedIndex() > 0)) {
                        //display error message...
                        //System.err.println("Bad Function Declaration");
                        JOptionPane.showMessageDialog(frame, "Weapon Functions Invalid. Must Have {1}, {1,2}, or {1,2,3}.", "Notice", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (functions[1].getSelectedIndex() == 0) item.functions = 1;
                    else if (functions[2].getSelectedIndex() == 0) item.functions = 2;
                    else item.functions = 3;
                    item.function = new String[item.functions][2];
                    item.level = new int[item.functions];
                    item.power = new int[item.functions];
                    item.speed = new int[item.functions];
                    for (int i = 0; i < item.functions; i++) {
                        item.function[i][0] = (String) functions[i].getSelectedItem();
                        if (classes[i].getSelectedIndex() != 4)
                            item.function[i][1] = ((String) classes[i].getSelectedItem()).toLowerCase().substring(0, 1);
                        else item.function[i][1] = "-";
                        item.level[i] = lvlneed[i].getSelectedIndex();
                        item.power[i] = Integer.parseInt(power[i].getText());
                        item.speed[i] = Integer.parseInt(speed[i].getText());
                        item.charges[i] = Integer.parseInt(charges[i].getText());
                        //System.out.println(item.function[i][0]+","+item.function[i][1]+","+item.level[i]+","+item.power[i]+","+item.speed[i]+","+item.charges[i]);
                    }
                    item.equipstring = epicstring.getText();
                    item.epic = epic.getImage();//tk.getImage("Items"+File.separator+item.equipstring);
                    item.usedupstring = upicstring.getText();
                } else if (TYPE == 1) {
                    //armor
                    item.type = ARMORTYPE;
                    item.cursed = Integer.parseInt(cursed.getText());
                    item.equipstring = epicstring.getText();
                    item.epic = epic.getImage();//tk.getImage("Items"+File.separator+item.equipstring);
                } else {
                    //misc
                    if (food.getSelectedIndex() != 0) {
                        item.foodvalue = Integer.parseInt((String) food.getSelectedItem());
                        item.type = Item.FOOD;
                        if (foodpoison.getSelectedIndex() == 0) item.poisonous = 0;
                        else item.poisonous = Integer.parseInt((String) foodpoison.getSelectedItem());
                    } else item.type = Item.OTHER;
                }
                //if (TYPE<2) {
                if (item.type != Item.OTHER) {
                    //equip effects
                    item.defense = Integer.parseInt(defense.getText());
                    item.magicresist = Integer.parseInt(magicresist.getText());
                    String[] effect = new String[14];
                    int numeffects = 0;
                    if (!health.getText().equals("0")) {
                        effect[numeffects] = "health," + health.getText();
                        numeffects++;
                        Integer.parseInt(health.getText());
                    }
                    if (!stamina.getText().equals("0")) {
                        effect[numeffects] = "stamina," + stamina.getText();
                        numeffects++;
                        Integer.parseInt(stamina.getText());
                    }
                    if (!mana.getText().equals("0")) {
                        effect[numeffects] = "mana," + mana.getText();
                        numeffects++;
                        Integer.parseInt(mana.getText());
                    }
                    if (!strength.getText().equals("0")) {
                        effect[numeffects] = "strength," + strength.getText();
                        numeffects++;
                        Integer.parseInt(strength.getText());
                    }
                    if (!dexterity.getText().equals("0")) {
                        effect[numeffects] = "dexterity," + dexterity.getText();
                        numeffects++;
                        Integer.parseInt(dexterity.getText());
                    }
                    if (!vitality.getText().equals("0")) {
                        effect[numeffects] = "vitality," + vitality.getText();
                        numeffects++;
                        Integer.parseInt(vitality.getText());
                    }
                    if (!intelligence.getText().equals("0")) {
                        effect[numeffects] = "intelligence," + intelligence.getText();
                        numeffects++;
                        Integer.parseInt(intelligence.getText());
                    }
                    if (!wisdom.getText().equals("0")) {
                        effect[numeffects] = "wisdom," + wisdom.getText();
                        numeffects++;
                        Integer.parseInt(wisdom.getText());
                    }
                    if (!fighter.getText().equals("0")) {
                        effect[numeffects] = "flevel," + fighter.getText();
                        numeffects++;
                        Integer.parseInt(fighter.getText());
                    }
                    if (!ninja.getText().equals("0")) {
                        effect[numeffects] = "nlevel," + ninja.getText();
                        numeffects++;
                        Integer.parseInt(ninja.getText());
                    }
                    if (!wizard.getText().equals("0")) {
                        effect[numeffects] = "wlevel," + wizard.getText();
                        numeffects++;
                        Integer.parseInt(wizard.getText());
                    }
                    if (!priest.getText().equals("0")) {
                        effect[numeffects] = "plevel," + priest.getText();
                        numeffects++;
                        Integer.parseInt(priest.getText());
                    }
                    if (numeffects > 0) {
                        item.haseffect = true;
                        item.effect = new String[numeffects];
                        for (int i = 0; i < numeffects; i++) {
                            item.effect[i] = effect[i];
                            //System.out.println(item.effect[i]);
                        }
                    }
                }
                if (oldname != null && !item.name.equals(oldname)) {
                    //name changed, so add item to custom list
                    ItemWizard.customitems.add(item);
                    item.number = 299 + ItemWizard.customitems.size();
                }
                dispose();
            } catch (NumberFormatException ex) {
                //deal with bad numbers...
                //System.err.println(ex);
                //JOptionPane.showMessageDialog(null, "Fields Must Contain Valid Numbers.", "Notice", JOptionPane.ERROR_MESSAGE);
                JOptionPane.showMessageDialog(frame, "Invalid Number: " + ex.getMessage(), "Notice", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            item = null;
            dispose();
        } else if (e.getActionCommand().equals("Weapon")) {
            TYPE = 0;
            armorpan.setVisible(false);
            miscpan.setVisible(false);
            //equippan.setVisible(true);
            weaponpan.setVisible(true);
            epicpan.setVisible(true);
            upicpan.setVisible(true);
            cursepan.setVisible(true);
            equippan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Equip Effects"));
        } else if (e.getActionCommand().equals("Armor")) {
            TYPE = 1;
            weaponpan.setVisible(false);
            miscpan.setVisible(false);
            //equippan.setVisible(true);
            armorpan.setVisible(true);
            upicpan.setVisible(false);
            epicpan.setVisible(true);
            cursepan.setVisible(true);
            equippan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Equip Effects"));
        } else if (e.getActionCommand().equals("Misc")) {
            TYPE = 2;
            weaponpan.setVisible(false);
            armorpan.setVisible(false);
            //equippan.setVisible(false);
            miscpan.setVisible(true);
            epicpan.setVisible(false);
            upicpan.setVisible(false);
            cursepan.setVisible(false);
            equippan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Consume Effects"));
        } else if (e.getActionCommand().equals("Head")) {
            ARMORTYPE = Item.HEAD;
        } else if (e.getActionCommand().equals("Neck")) {
            ARMORTYPE = Item.NECK;
        } else if (e.getActionCommand().equals("Torso")) {
            ARMORTYPE = Item.TORSO;
        } else if (e.getActionCommand().equals("Leg")) {
            ARMORTYPE = Item.LEGS;
        } else if (e.getActionCommand().equals("Foot")) {
            ARMORTYPE = Item.FEET;
        } else if (e.getActionCommand().equals("Shield")) {
            ARMORTYPE = Item.SHIELD;
        } else if (e.getActionCommand().equals("f0")) {
            int i = functions[0].getSelectedIndex();
            if (i == -1) return;
            classes[0].setSelectedIndex(defaultclass[i]);
            lvlneed[0].setSelectedIndex(defaultlvlneed[i]);
            power[0].setText("" + defaultpower[i]);
            speed[0].setText("" + defaultspeed[i]);
            charges[0].setText("" + defaultcharges[i]);
        } else if (e.getActionCommand().equals("f1")) {
            int i = functions[1].getSelectedIndex();
            if (i == -1) return;
            classes[1].setSelectedIndex(defaultclass[i]);
            lvlneed[1].setSelectedIndex(defaultlvlneed[i]);
            power[1].setText("" + defaultpower[i]);
            speed[1].setText("" + defaultspeed[i]);
            charges[1].setText("" + defaultcharges[i]);
        } else if (e.getActionCommand().equals("f2")) {
            int i = functions[2].getSelectedIndex();
            if (i == -1) return;
            classes[2].setSelectedIndex(defaultclass[i]);
            lvlneed[2].setSelectedIndex(defaultlvlneed[i]);
            power[2].setText("" + defaultpower[i]);
            speed[2].setText("" + defaultspeed[i]);
            charges[2].setText("" + defaultcharges[i]);
        } else if (e.getActionCommand().equals("pic")) {
            dialog.show();
            String newpic = dialog.getFile();
            if (newpic != null) {
                picstring.setText(newpic);
                refreshPics();
            }
        } else if (e.getActionCommand().equals("dpic")) {
            dialog.show();
            String newpic = dialog.getFile();
            if (newpic != null) {
                dpicstring.setText(newpic);
                refreshPics();
            }
        } else if (e.getActionCommand().equals("epic")) {
            dialog.show();
            String newpic = dialog.getFile();
            if (newpic != null) {
                epicstring.setText(newpic);
                refreshPics();
            }
        } else if (e.getActionCommand().equals("upic")) {
            dialog.show();
            String newpic = dialog.getFile();
            if (newpic != null) {
                upicstring.setText(newpic);
                refreshPics();
            }
        } else if (e.getActionCommand().equals("Refresh Pics")) {
            refreshPics();
        }
    }
    
    private void refreshPics() {
        pic.setImage(tk.getImage("Items" + File.separator + picstring.getText()));
        dpic.setImage(tk.getImage("Items" + File.separator + dpicstring.getText()));
        if (TYPE != 2) epic.setImage(tk.getImage("Items" + File.separator + epicstring.getText()));
        if (TYPE == 0) upic.setImage(tk.getImage("Items" + File.separator + upicstring.getText()));
        if (throwpicstring.getText().equals("")) {
            throwpic0.setImage(tk.getImage("Items" + File.separator + dpicstring.getText()));
            throwpic1.setImage(tk.getImage("Items" + File.separator + dpicstring.getText()));
            throwpic2.setImage(tk.getImage("Items" + File.separator + dpicstring.getText()));
            throwpic3.setImage(tk.getImage("Items" + File.separator + dpicstring.getText()));
        } else {
            throwpic0.setImage(tk.getImage("Items" + File.separator + throwpicstring.getText() + "-away.gif"));
            throwpic1.setImage(tk.getImage("Items" + File.separator + throwpicstring.getText() + "-toward.gif"));
            throwpic2.setImage(tk.getImage("Items" + File.separator + throwpicstring.getText() + "-left.gif"));
            throwpic3.setImage(tk.getImage("Items" + File.separator + throwpicstring.getText() + "-right.gif"));
        }
        picscrollpan.doLayout();
        picturespan.doLayout();
        picpan.doLayout();
        dpicpan.doLayout();
        epicpan.doLayout();
        upicpan.doLayout();
        throwpicpan0.doLayout();
        throwpicpan1.doLayout();
        throwpicpan2.doLayout();
        throwpicpan3.doLayout();
                /*picpan.repaint();
                dpicpan.repaint();
                epicpan.repaint();
                upicpan.repaint();
                throwpicpan0.repaint();
                throwpicpan1.repaint();
                throwpicpan2.repaint();
                throwpicpan3.repaint();
                picturespan.repaint();
                picscrollpan.repaint();
                */
        repaint();
        cp.doLayout();
        cp.repaint();
        cp.validate();
    }
    
    public Item getItem() {
        return item;
    }
    
    public boolean accept(File dir, String name) {
        if (dir.getName().equals("Items")) {
            name = name.toLowerCase();
            if (name.endsWith(".gif") || name.endsWith(".jpg") || name.endsWith(".png")) return true;
        }
        return false;
    }
}