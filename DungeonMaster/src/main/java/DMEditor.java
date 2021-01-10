//?copy and paste (maybe just one square at a time)
//?make possible to add projs to map (how to draw?)

import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class DMEditor extends JFrame implements ActionListener, KeyListener, FilenameFilter {
    boolean ADDSTAIRS = true;//true if should automatically add corresponding stairs above/below new stairs
    boolean MOUSEDOWN = false, MAKEFLOOR = false;
    boolean NEEDSAVE = false;
    static boolean NEEDSAVEITEMS = false, NEEDSAVEMONS = false;
    
    static JFrame frame = null;
    static final String version = "1.04";
    Container cp;
    static int MAPLEVELS = 1;
    static int MAPWIDTH = 40;
    static int MAPHEIGHT = 40;
    File mapfile;
    FileDialog dialog;
    boolean SQUARELOCKED = false;
    int lockx, locky;
    
    boolean ZOOMING = false;
    
    HeroData[] hero = new HeroData[4];
    JMenuItem undoitem, partyedititem;
    JButton undobutton;
    int counter, darkcounter, darkfactor, magictorch, magicvision, floatcounter, dispell, slowcount, freezelife, leader, spellready, weaponready, numheroes;
    int[] heroatsub;
    static int[] leveldarkfactor = {255};
    boolean iteminhand, mirrorback, AUTOMAP;
    char[][][] automap;
    Item inhand;
    //static final Item fistfoot = new Item(6);
    static final Item fistfoot = Item.fistfoot;
    ArrayList dmprojs = new ArrayList();
    HeroPanel heropanel;
    ItemFinder itemfinder;
    SwitchFinder switchfinder;
    static Font dungfont, scrollfont;
    
    int facing = 2, partylevel = 0, partyx = 1, partyy = 1; //from savegame, or where new game will start you
    boolean create = true, nochar = true;//for maps, start types allowed
    int levelpoints = 4, hsmpoints = 25, statpoints = 50, defensepoints = 8, itempoints = 0, abilitypoints = 0, abilityauto = 0;//creation points for maps
    static Vector itemchoose = new Vector(), abilitychoose = new Vector(); //for maps, create character stuff
    int changingcount = 0; //# mapsobjects changing
    boolean mapchanging = false; //from savegame, also set true if any teleports or continuous launchers
    ArrayList mapstochange = new ArrayList(5);
    boolean cloudchanging = false;
    ArrayList cloudstochange = new ArrayList(2);
    boolean fluxchanging = false;
    Hashtable fluxcages = new Hashtable();
    ArrayList loopsounds = new ArrayList();
    
    static String[] leveldir = new String[1];
    static ArrayList maplevels = new ArrayList(5);
    ArrayList undolist = new ArrayList();
    //static ArrayList switchloading = new ArrayList();//used during load routine to set switch changeto pointers (avoids a nasty infinite save loop)
    MapData[][] mapdata = new MapData[MAPWIDTH][MAPHEIGHT];
    MapPanel mappanel;
    MapClick mapclick;
    static TargetFrame targetframe;
    static int currentlevel = 0;
    int currentx, currenty;
    char mapchangechar = '1';
    static Hashtable monhash = new Hashtable(23);
    //static HashMap monhash = new HashMap(23);
    ItemListen itemlisten = new ItemListen();
    //MapClick mapclick;
    MenuListen menulisten;
    JMenu filemenu;
    JMenuItem recent1, recent2, recent3;
    JMenu levelmenu;
    JMenuItem[] levelmenuitem;
    
    JSplitPane spane;
    JPanel buttonbox;
    static JScrollPane mpane;
    Box hspacebox;
    MyMapPanel vspacebox;
    JToggleButton[] mbutton;
    Box monitembox;
    JScrollPane mipane;
    JLabel statusbar = new JLabel("Initializing Editor...");
    JPanel loadingpanel;
    
    static MonsterWizard monsterwizard;
    static ItemWizard itemwizard, itemwizard2;
    static ItemCreator itemcreator;
    static FountainWizard fountainwizard;
    static DoorWizard doorwizard;
    static AlcoveWizard alcovewizard;
    static MirrorWizard mirrorwizard;
    static WritingWizard writingwizard;
    static TeleportWizard teleportwizard;
    static LauncherWizard launcherwizard;
    static GeneratorWizard generatorwizard;
    static PitWizard pitwizard;
    static SconceWizard sconcewizard;
    //static WallSwitchWizard wallswitchwizard;
    static DecorationWizard decorationwizard;
    static FDecorationWizard fdecorationwizard;
    static PillarWizard pillarwizard;
    static EventWizard eventwizard;
    PartyInfoDialog partyinfo;
    
    public static void main(String[] args) throws IOException {
        try {
            FileInputStream in = new FileInputStream("editorfont.ttf");
            dungfont = Font.createFont(Font.TRUETYPE_FONT, in);
            in.close();
            dungfont = dungfont.deriveFont(Font.BOLD, 12);
        } catch (FileNotFoundException e) {
            //System.out.println("editorfont.ttf not found, using default.");
            dungfont = new Font("SansSerif", Font.BOLD, 12);
        } catch (Exception ex) {
            ex.printStackTrace();
            dungfont = new Font("SansSerif", Font.BOLD, 12);
        }
        try {
            FileInputStream in = new FileInputStream("scrollfont.ttf");
            scrollfont = Font.createFont(Font.TRUETYPE_FONT, in);
            in.close();
            scrollfont = scrollfont.deriveFont(Font.BOLD, 12);
        } catch (FileNotFoundException e) {
            //System.out.println("scrollfont.ttf not found, using default.");
            scrollfont = new Font("Courier", Font.BOLD, 12);
        } catch (Exception ex) {
            ex.printStackTrace();
            scrollfont = new Font("Courier", Font.PLAIN, 12);
        }
        try {
            javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new DungTheme(dungfont));
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't set look and feel.");
        }
        frame = new DMEditor(args);
    }
    
    public DMEditor(String[] args) throws IOException {
        super("Dungeon Master Editor");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (NEEDSAVE) {
                    //pop up warning window
                    int returnval = JOptionPane.showConfirmDialog(frame, "Dungeon Modified.\nSave it before quitting?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (returnval == JOptionPane.YES_OPTION) {
                        if (mapfile == null) saveAs();
                        else save();
                        if (NEEDSAVE) return;//in case saveAs cancelled
                    } else if (returnval != JOptionPane.NO_OPTION) return;
                }
                try {
                    //write out recent file list
                    if (!recent1.getText().equals("")) {
                        PrintWriter w = new PrintWriter(new FileWriter("recent.txt"));
                        w.println(recent1.getText());
                        if (!recent2.getText().equals("")) {
                            w.println(recent2.getText());
                            if (!recent3.getText().equals("")) w.println(recent3.getText());
                        }
                        w.flush();
                        w.close();
                    }
                    //save custom items
                    if (NEEDSAVEITEMS && ItemWizard.customitems.size() > 0) {
                        FileOutputStream out = new FileOutputStream(new File("custom_items.dat"));
                        ObjectOutputStream so = new ObjectOutputStream(out);
                        so.writeInt(ItemWizard.customitems.size());
                        for (int i = 0; i < ItemWizard.customitems.size(); i++) {
                            so.writeObject(ItemWizard.customitems.get(i));
                        }
                        so.flush();
                        out.close();
                        NEEDSAVEITEMS = false;
                    }
                    //save custom mons
                    if (NEEDSAVEMONS && MonsterWizard.custommons.size() > 0) {
                        MonsterData.NOITEMS = true;
                        FileOutputStream out = new FileOutputStream(new File("custom_mons.dat"));
                        ObjectOutputStream so = new ObjectOutputStream(out);
                        so.writeInt(MonsterWizard.custommons.size());
                        for (int i = 0; i < MonsterWizard.custommons.size(); i++) {
                            ((MonsterData) MonsterWizard.custommons.get(i)).save(so);
                        }
                        so.flush();
                        out.close();
                        MonsterData.NOITEMS = false;
                        NEEDSAVEMONS = false;
                    }
                } catch (Exception ex) {
                    MonsterData.NOITEMS = false;
                }
                dispose();
                System.exit(0);
            }
        };
        addWindowListener(l);
        setIconImage(Toolkit.getDefaultToolkit().createImage("Icons" + File.separator + "dmjicon.gif"));
        cp = getContentPane();
        
        //mapclick= new MapClick();
        for (int y = 0; y < MAPHEIGHT; y++) {
            for (int x = 0; x < MAPWIDTH; x++) {
                mapdata[x][y] = new WallData();
            }
        }
        maplevels.add(mapdata);
        mapdata[1][1] = new FloorData();
        mapdata[1][1].hasParty = true;
        
        monitembox = Box.createVerticalBox();
        monitembox.setBackground(new Color(60, 60, 60));
        
        buttonbox = new JPanel();
        buttonbox.setPreferredSize(new Dimension(800, 85));
        buttonbox.setMinimumSize(new Dimension(60, 85));
        buttonbox.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        ImageIcon[] micon = new ImageIcon[29];
        
        InputStream is = getClass().getClassLoader().getResourceAsStream("Icons" + File.separator + "addmon.gif");
        byte[] resourceByteArray = IOUtils.toByteArray(is);
        //micon[0] = new ImageIcon("Icons" + File.separator + "addmon.gif");
        micon[0] = new ImageIcon(resourceByteArray);
        
        micon[1] = new ImageIcon("Icons" + File.separator + "additem.gif");
        micon[2] = MapPanel.WallIcon;
        micon[3] = MapPanel.DoorIcon;
        micon[4] = MapPanel.FakeWallIcon;
        micon[5] = MapPanel.AlcoveIcon;
        //micon[6] = MapPanel.AltarIcon;
        micon[6] = MapPanel.FountainIcon;
        micon[7] = MapPanel.StairsDownIcon;
        micon[8] = MapPanel.StairsUpIcon;//if changed from 9, must change references later on
        micon[9] = MapPanel.TeleportIcon;
        micon[10] = MapPanel.PitIcon;
        micon[11] = MapPanel.MirrorIcon;
        micon[12] = MapPanel.WritingIcon;
        micon[13] = MapPanel.LauncherIcon;
        micon[14] = MapPanel.GeneratorIcon;
        micon[15] = MapPanel.WallSwitchIcon;
        micon[16] = MapPanel.MultWallSwitchIcon;
        micon[17] = MapPanel.FloorSwitchIcon;
        micon[18] = MapPanel.MultFloorSwitchIcon;
        micon[19] = MapPanel.SconceIcon;
        micon[20] = MapPanel.DecorationIcon;
        micon[21] = MapPanel.FDecorationIcon;
        micon[22] = MapPanel.PillarIcon;
        micon[23] = MapPanel.InvisibleWallIcon;
        micon[24] = MapPanel.EventIcon;
        micon[25] = MapPanel.GameWinIcon;
        micon[26] = MapPanel.StormIcon;
        micon[27] = MapPanel.GemIcon;
        micon[28] = MapPanel.FulYaIcon;
        final String[] maction = {"M", "I", "1", "d", "2", "]", "f", "v", "^", "t", "p", "m", "w", "l", "g", "/", "\\", "s", "S", "}", "D", "F", "P", "i", "E", "W", "!", "G", "y"};
        final String[] mtip = {"Monster", "Item", "Wall <-> Floor", "Door", "Illusionary Wall <-> Floor", "Alcove/VI Altar", "Fountain", "Stairs Down",
            "Stairs Up", "Teleport", "Pit", "Mirror", "Writing", "Launcher", "Generator", "Wall Switch", "Multiple Wall Switches",
            "Floor Switch", "Multiple Floor Switches", "Sconce", "Decoration", "Floor Decoration", "Pillar", "Invisible Wall",
            "Event Square", "Game Win Square", "Stormbringer", "Power Gem", "FulYa Pit"};
        
        JButton uplevelbutton = new JButton(new ImageIcon("Icons" + File.separator + "up.gif"));
        uplevelbutton.setFocusable(false);
        uplevelbutton.setPreferredSize(new Dimension(36, 42));
        uplevelbutton.setActionCommand("Up");
        uplevelbutton.addActionListener(this);
        buttonbox.add(uplevelbutton);
        JButton downlevelbutton = new JButton(new ImageIcon("Icons" + File.separator + "down.gif"));
        downlevelbutton.setFocusable(false);
        downlevelbutton.setPreferredSize(new Dimension(36, 42));
        downlevelbutton.setActionCommand("Down");
        downlevelbutton.addActionListener(this);
        buttonbox.add(downlevelbutton);
        buttonbox.add(Box.createHorizontalStrut(10));
        
        menulisten = new MenuListen();
        
        undobutton = new JButton(new ImageIcon("Icons" + File.separator + "undo.gif"));
        undobutton.setFocusable(false);
        undobutton.setPreferredSize(new Dimension(42, 42));
        undobutton.setMinimumSize(new Dimension(42, 42));
        undobutton.setMaximumSize(new Dimension(42, 42));
        undobutton.setActionCommand("Undo");
        undobutton.addActionListener(menulisten);
        undobutton.setEnabled(false);
        buttonbox.add(undobutton);
        buttonbox.add(Box.createHorizontalStrut(10));
        
        JToggleButton zoombutton = new JToggleButton(new ImageIcon("Icons" + File.separator + "zoom.gif"));
        zoombutton.setFocusable(false);
        zoombutton.setPreferredSize(new Dimension(42, 42));
        zoombutton.setMinimumSize(new Dimension(42, 42));
        zoombutton.setMaximumSize(new Dimension(42, 42));
        zoombutton.setActionCommand("Zoom");
        zoombutton.addActionListener(this);
        buttonbox.add(zoombutton);
        buttonbox.add(Box.createHorizontalStrut(20));
        zoombutton.setSelected(true);
        
        ButtonGroup mgroup = new ButtonGroup();
        
        JToggleButton monbutton = new JToggleButton(new ImageIcon("Icons" + File.separator + "nomons.gif"));
        monbutton.setFocusable(false);
        monbutton.setPreferredSize(new Dimension(42, 42));
        monbutton.setMinimumSize(new Dimension(42, 42));
        monbutton.setMaximumSize(new Dimension(42, 42));
        monbutton.setActionCommand("3");
        monbutton.addActionListener(this);
        monbutton.setToolTipText("No Monsters");
        mgroup.add(monbutton);
        buttonbox.add(monbutton);
        
        JToggleButton ghostbutton = new JToggleButton(new ImageIcon("Icons" + File.separator + "noghosts.gif"));
        ghostbutton.setFocusable(false);
        ghostbutton.setPreferredSize(new Dimension(42, 42));
        ghostbutton.setMinimumSize(new Dimension(42, 42));
        ghostbutton.setMaximumSize(new Dimension(42, 42));
        ghostbutton.setActionCommand("4");
        ghostbutton.addActionListener(this);
        ghostbutton.setToolTipText("No Non-Material Monsters");
        mgroup.add(ghostbutton);
        buttonbox.add(ghostbutton);
        
        mbutton = new JToggleButton[29];
        for (int i = 0; i < 29; i++) {
            mbutton[i] = new JToggleButton(micon[i]);
            mbutton[i].setFocusable(false);
            mbutton[i].setPreferredSize(new Dimension(42, 42));
            mbutton[i].setMinimumSize(new Dimension(42, 42));
            mbutton[i].setMaximumSize(new Dimension(42, 42));
            mbutton[i].setActionCommand(maction[i]);
            mbutton[i].addActionListener(this);
            mbutton[i].setToolTipText(mtip[i]);
            mgroup.add(mbutton[i]);
            buttonbox.add(mbutton[i]);
            if (i == 1) buttonbox.add(Box.createHorizontalStrut(10));
        }
        mbutton[2].setSelected(true);
        mbutton[8].setEnabled(false);
        
        filemenu = new JMenu("File");
        filemenu.setMnemonic('F');
        JMenuItem newitem = new JMenuItem("New", 'N');
        JMenuItem loaditem = new JMenuItem("Load", 'L');
        JMenuItem saveitem = new JMenuItem("Save", 'S');
        JMenuItem saveasitem = new JMenuItem("Save As", 'A');
        JMenuItem exititem = new JMenuItem("Exit", 'x');
        newitem.addActionListener(menulisten);
        loaditem.addActionListener(menulisten);
        saveitem.addActionListener(menulisten);
        saveasitem.addActionListener(menulisten);
        exititem.addActionListener(menulisten);
        filemenu.add(newitem);
        filemenu.add(loaditem);
        filemenu.addSeparator();
        filemenu.add(saveitem);
        filemenu.add(saveasitem);
        filemenu.addSeparator();
        filemenu.add(exititem);
        filemenu.addSeparator();
        //recently used file
        recent1 = new JMenuItem();
        recent1.setActionCommand("recent1");
        recent1.addActionListener(menulisten);
        recent2 = new JMenuItem();
        recent2.setActionCommand("recent2");
        recent2.addActionListener(menulisten);
        recent3 = new JMenuItem();
        recent3.setActionCommand("recent3");
        recent3.addActionListener(menulisten);
        try {
            BufferedReader r = new BufferedReader(new FileReader("recent.txt"));
            String temps = r.readLine();
            if (temps != null) {
                recent1.setText(temps);
                filemenu.add(recent1);
                temps = r.readLine();
                if (temps != null) {
                    recent2.setText(temps);
                    filemenu.add(recent2);
                    temps = r.readLine();
                    if (temps != null) {
                        recent3.setText(temps);
                        filemenu.add(recent3);
                    }
                }
            }
            r.close();
        } catch (Exception e) {
        }
        
        JMenu editmenu = new JMenu("Edit");
        editmenu.setMnemonic('E');
        undoitem = new JMenuItem("Undo");
        undoitem.setEnabled(false);
        JCheckBoxMenuItem pitstairsitem = new JCheckBoxMenuItem("Auto Stairs", true);
        JMenuItem partystartitem = new JMenuItem("Set Party Info");
        partyedititem = new JMenuItem("Edit Party");
        partyedititem.setEnabled(false);
        JMenuItem leveldiritem = new JMenuItem("Set Level Pic Directories");
        JMenuItem leveldarkitem = new JMenuItem("Set Level Darkness");
        JMenuItem resizelevelitem = new JMenuItem("Resize Levels");
        JMenuItem deletelevelitem = new JMenuItem("Delete Current Level");
        JMenuItem insertlevelitem = new JMenuItem("Insert Level Before Current");
        JMenuItem itemfinderitem = new JMenuItem("Find Items...");
        JMenuItem switchfinderitem = new JMenuItem("Find Switches...");
        undoitem.addActionListener(menulisten);
        pitstairsitem.addActionListener(menulisten);
        partystartitem.addActionListener(menulisten);
        partyedititem.addActionListener(menulisten);
        leveldiritem.addActionListener(menulisten);
        leveldarkitem.addActionListener(menulisten);
        resizelevelitem.addActionListener(menulisten);
        deletelevelitem.addActionListener(menulisten);
        insertlevelitem.addActionListener(menulisten);
        itemfinderitem.addActionListener(menulisten);
        switchfinderitem.addActionListener(menulisten);
        editmenu.add(undoitem);
        editmenu.add(pitstairsitem);
        editmenu.addSeparator();
        editmenu.add(resizelevelitem);
        editmenu.add(deletelevelitem);
        editmenu.add(insertlevelitem);
        editmenu.addSeparator();
        editmenu.add(leveldiritem);
        editmenu.add(leveldarkitem);
        editmenu.addSeparator();
        editmenu.add(partystartitem);
        editmenu.add(partyedititem);
        editmenu.addSeparator();
        editmenu.add(itemfinderitem);
        editmenu.add(switchfinderitem);
        
        levelmenu = new JMenu("Levels");
        levelmenu.setMnemonic('L');
        levelmenuitem = new JMenuItem[1];
        levelmenuitem[0] = new JMenuItem("Level 0", '0');
        levelmenuitem[0].addActionListener(menulisten);
        levelmenu.add(levelmenuitem[0]);
        
        JMenu importmenu = new JMenu("Import");
        importmenu.setMnemonic('I');
        JMenuItem importitems = new JMenuItem("Custom Items...");
        JMenuItem importmons = new JMenuItem("Custom Monsters...");
        importitems.addActionListener(menulisten);
        importmons.addActionListener(menulisten);
        importmenu.add(importitems);
        importmenu.add(importmons);
        
        JMenu helpmenu = new JMenu("Help");
        helpmenu.setMnemonic('H');
        JMenuItem helpitem = new JMenuItem("Help", 'H');
        JMenuItem aboutitem = new JMenuItem("About", 'A');
        helpitem.addActionListener(menulisten);
        aboutitem.addActionListener(menulisten);
        helpmenu.add(helpitem);
        helpmenu.add(aboutitem);
        
        JMenuBar dmmenubar = new JMenuBar();
        dmmenubar.add(filemenu);
        dmmenubar.add(Box.createHorizontalStrut(50));
        dmmenubar.add(editmenu);
        dmmenubar.add(Box.createHorizontalStrut(50));
        dmmenubar.add(levelmenu);
        dmmenubar.add(Box.createHorizontalStrut(50));
        dmmenubar.add(importmenu);
        dmmenubar.add(Box.createHorizontalGlue());
        dmmenubar.add(helpmenu);
        dmmenubar.add(Box.createHorizontalStrut(100));
        this.setJMenuBar(dmmenubar);
        
        mappanel = new MapPanel(this);
        mapclick = new MapClick();
        mappanel.addMouseListener(mapclick);
        mappanel.addMouseMotionListener(mapclick);
        hspacebox = Box.createHorizontalBox();
        hspacebox.add(mappanel);
        hspacebox.add(Box.createHorizontalGlue());
        vspacebox = new MyMapPanel();
        vspacebox.add(hspacebox);
        vspacebox.add(Box.createVerticalGlue());
        mpane = new JScrollPane(vspacebox);
        
        mpane.setPreferredSize(new Dimension(695, 480));
        mipane = new JScrollPane(monitembox);
        mipane.setPreferredSize(new Dimension(105, 480));
        spane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mpane, mipane);
        statusbar.setPreferredSize(new Dimension(720, 20));
        statusbar.setHorizontalAlignment(SwingConstants.CENTER);
        statusbar.setForeground(Color.black);
        
        heropanel = new HeroPanel(this, dungfont);
        itemfinder = new ItemFinder(this);
        switchfinder = new SwitchFinder(this);
        
        dialog = new FileDialog(this);
        dialog.setDirectory("Dungeons");
        dialog.setFilenameFilter(this);
        
        try {
            File tempfile = new File("custom_items.dat");
            if (tempfile.exists()) {
                //FileInputStream in = new FileInputStream("custom_items.dat");
                FileInputStream in = new FileInputStream(tempfile);
                ObjectInputStream si = new ObjectInputStream(in);
                int numitems = si.readInt();
                for (int i = 0; i < numitems; i++) {
                    ItemWizard.customitems.add(si.readObject());
                }
                in.close();
            }
            tempfile = new File("custom_mons.dat");
            if (tempfile.exists()) {
                FileInputStream in = new FileInputStream(tempfile);
                ObjectInputStream si = new ObjectInputStream(in);
                int numitems = si.readInt();
                for (int i = 0; i < numitems; i++) {
                    MonsterWizard.custommons.add(new MonsterData(si));
                }
                in.close();
            }
        } catch (Exception e) {
            System.err.println("Error In Custom Read");
        }
        spane.setVisible(false);
        cp.add(spane);
        cp.add("South", statusbar);
        cp.add("North", buttonbox);
        pack();
        //mappanel.createOff(50,50);
        partyinfo = new PartyInfoDialog(this);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, java.util.Collections.EMPTY_SET);
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, java.util.Collections.EMPTY_SET);
        addKeyListener(this);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        show();
        
        targetframe = new TargetFrame(this);
        monsterwizard = new MonsterWizard(this);
        itemwizard = new ItemWizard(this);
        itemcreator = new ItemCreator(this);
        fountainwizard = new FountainWizard(this);
        doorwizard = new DoorWizard(this);
        alcovewizard = new AlcoveWizard(this);
        mirrorwizard = new MirrorWizard(this);
        writingwizard = new WritingWizard(this);
        teleportwizard = new TeleportWizard(this);
        launcherwizard = new LauncherWizard(this);
        generatorwizard = new GeneratorWizard(this);
        pitwizard = new PitWizard(this);
        sconcewizard = new SconceWizard(this);
        //wallswitchwizard = new WallSwitchWizard(this);
        decorationwizard = new DecorationWizard(this);
        fdecorationwizard = new FDecorationWizard(this);
        pillarwizard = new PillarWizard(this);
        eventwizard = new EventWizard(this);
        
        //load a map command line flag
        if (args.length > 0 && args[0].toLowerCase().equals("-l")) {
            if (args.length < 2) {
                if (!recent1.getText().equals("")) menulisten.actionPerformed(new ActionEvent(recent1, 0, "recent1"));
            } else {
                mapfile = new File(args[1]);
                if (mapfile.exists()) load(false);
                else {
                    mapfile = new File("Dungeons" + File.separator + args[1]);
                    if (mapfile.exists()) load(false);
                    else {
                        mapfile = new File("Saves" + File.separator + args[1]);
                        if (mapfile.exists()) load(false);
                        else mapfile = null;
                    }
                }
            }
        }
        
        spane.setVisible(true);
        validate();
        statusbar.setText("Welcome");
        //validate();
        spane.setDividerLocation(0.9);
        //spane.doLayout();
        if (Toolkit.getDefaultToolkit().getScreenSize().width < 1000 || Toolkit.getDefaultToolkit().getScreenSize().height < 740)
            JOptionPane.showMessageDialog(this, "Your screen resolution is less than 1024x768.\nSome dialogs may not fit properly!", "Warning!", JOptionPane.ERROR_MESSAGE);
        requestFocusInWindow();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Zoom")) {
            ZOOMING = !ZOOMING;
            mappanel.setZoom();
            mappanel.invalidate();
            vspacebox.invalidate();
            mpane.validate();
            spane.validate();
            mappanel.repaint();
        } else if (e.getActionCommand().equals("Up")) {
            if (currentlevel == 0 || SQUARELOCKED) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            mappanel.setVisible(false);
            currentlevel--;
            mapdata = (MapData[][]) maplevels.get(currentlevel);
            mappanel.clearTargets();
            mappanel.repaint();
            mappanel.setVisible(true);
            if (currentlevel == 0) {
                if (mbutton[8].isSelected()) mbutton[2].doClick();
                mbutton[8].setEnabled(false);
            }
        } else if (e.getActionCommand().equals("Down")) {
            if (SQUARELOCKED) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            if (currentlevel == MAPLEVELS - 1) {
                //pop up option to confirm addition of a new level
                int returnval = JOptionPane.showConfirmDialog(frame, "Add additional level to map?", "Add Level", JOptionPane.YES_NO_OPTION);
                if (returnval == JOptionPane.YES_OPTION) {
                    mappanel.setVisible(false);
                    MAPLEVELS++;
                    currentlevel++;
                    JMenuItem newlevelitem = new JMenuItem("Level " + currentlevel);
                    if (currentlevel < 10) newlevelitem.setMnemonic(Character.forDigit(currentlevel, 10));
                    newlevelitem.addActionListener(menulisten);
                    levelmenu.add(newlevelitem);
                    mapdata = new MapData[MAPWIDTH][MAPHEIGHT];
                    for (int y = 0; y < MAPHEIGHT; y++) {
                        for (int x = 0; x < MAPWIDTH; x++) {
                            mapdata[x][y] = new WallData();
                        }
                    }
                    maplevels.add(mapdata);
                    mappanel.clearTargets();
                    mappanel.repaint();
                    mappanel.setVisible(true);
                    mbutton[8].setEnabled(true);
                    return;
                } else return;
            }
            mappanel.setVisible(false);
            currentlevel++;
            mapdata = (MapData[][]) maplevels.get(currentlevel);
            mappanel.clearTargets();
            mappanel.repaint();
            mappanel.setVisible(true);
            mbutton[8].setEnabled(true);
        } else if (e.getActionCommand().equals("Monster")) {
            int sub = 5;
            String squarestring = ((JButton) e.getSource()).getText();
            if (squarestring.equals("NW")) sub = 0;
            else if (squarestring.equals("NE")) sub = 1;
            else if (squarestring.equals("SE")) sub = 2;
            else if (squarestring.equals("SW")) sub = 3;
            
            String[] options = {"Edit", "Delete", "Change Corner", "Cancel"};
            int choice = JOptionPane.showOptionDialog(frame, "What do you want to do to that monster?", "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 0) {
                MonsterData olddata = (MonsterData) monhash.get(currentlevel + "," + lockx + "," + locky + "," + sub);
                //MonsterData tempdata = (new MonsterWizard(frame,currentlevel,lockx,locky,olddata)).getData();
                monsterwizard.setMonster(olddata, currentlevel, lockx, locky);
                MonsterData tempdata = monsterwizard.getData();
                if (tempdata != null) {
                    if (tempdata.subsquare == 5 && sub != 5) {
                        //changed a non sub5 mon into a sub5 mon
                        for (int i = 0; i < 4; i++) {
                            monhash.remove(currentlevel + "," + lockx + "," + locky + "," + i);
                            mapdata[lockx][locky].hasmonin[i] = false;
                        }
                        sub = 5;
                        mapdata[lockx][locky].hasmonin[4] = true;
                    } else if (tempdata.subsquare != 5 && sub == 5) {
                        //changed a sub5 mon into a non sub5
                        monhash.remove(currentlevel + "," + lockx + "," + locky + "," + sub);
                        sub = 0;
                        mapdata[lockx][locky].hasmonin[4] = false;
                        mapdata[lockx][locky].hasmonin[0] = true;
                    } else tempdata.subsquare = sub;
                    monhash.put(currentlevel + "," + lockx + "," + locky + "," + sub, tempdata);
                    SQUARELOCKED = false;
                    setStatusBar(mapdata[lockx][locky], lockx, locky);
                    SQUARELOCKED = true;
                    statusbar.setText(statusbar.getText() + "      (Locked)");
                    mappanel.repaint();
                }
            } else if (choice == 1) {
                MonsterData olddata = (MonsterData) monhash.remove(currentlevel + "," + lockx + "," + locky + "," + sub);
                if (sub != 5) mapdata[lockx][locky].hasmonin[sub] = false;
                else mapdata[lockx][locky].hasmonin[4] = false;
                if (!mapdata[lockx][locky].hasmonin[0] && !mapdata[lockx][locky].hasmonin[1] && !mapdata[lockx][locky].hasmonin[2] && !mapdata[lockx][locky].hasmonin[3] && !mapdata[lockx][locky].hasmonin[4])
                    mapdata[lockx][locky].hasMons = false;
                SQUARELOCKED = false;
                setStatusBar(mapdata[lockx][locky], lockx, locky);
                SQUARELOCKED = true;
                statusbar.setText(statusbar.getText() + "      (Locked)");
                mappanel.repaint();
            } else if (choice == 2 && sub != 5) {
                String[] corners = {"NW", "NE", "SE", "SW"};
                int newcorner = JOptionPane.showOptionDialog(frame, "Which corner?", "Change Corner", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, corners, corners[0]);
                if (newcorner != sub) {
                    MonsterData data = (MonsterData) monhash.remove(currentlevel + "," + lockx + "," + locky + "," + sub);
                    data.subsquare = newcorner;
                    if (mapdata[lockx][locky].hasmonin[newcorner]) {
                        MonsterData olddata = (MonsterData) monhash.remove(currentlevel + "," + lockx + "," + locky + "," + newcorner);
                        olddata.subsquare = sub;
                        monhash.put(currentlevel + "," + lockx + "," + locky + "," + sub, olddata);
                    } else {
                        mapdata[lockx][locky].hasmonin[sub] = false;
                        mapdata[lockx][locky].hasmonin[newcorner] = true;
                    }
                    monhash.put(currentlevel + "," + lockx + "," + locky + "," + newcorner, data);
                    setStatusBar(mapdata[lockx][locky], lockx, locky);
                    statusbar.setText(statusbar.getText() + "      (Locked)");
                    mappanel.repaint();
                }
            }
        } else mapchangechar = (e.getActionCommand()).charAt(0);
    }
    
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
        //if (e.getKeyCode()==KeyEvent.VK_Z && e.isControlDown()) { menulisten.actionPerformed(new ActionEvent(undoitem,0,"Undo")); }
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            char mapchar = mapdata[currentx][currenty].mapchar;
            switch (mapchar) {
                case '0':
                case '1':
                    mbutton[2].doClick();
                    break;
                case 'd':
                    mbutton[3].doClick();
                    break;
                case '2':
                    mbutton[4].doClick();
                    break;
                case ']':
                case '[':
                case 'a':
                    mbutton[5].doClick();
                    break;
                case 'f':
                    mbutton[6].doClick();
                    break;
                case '>':
                    if (!((StairsData) mapdata[currentx][currenty]).goesUp) mbutton[7].doClick();
                    else mbutton[8].doClick();
                    break;
                case 't':
                    mbutton[9].doClick();
                    break;
                case 'p':
                    mbutton[10].doClick();
                    break;
                case 'm':
                    mbutton[11].doClick();
                    break;
                case 'w':
                    mbutton[12].doClick();
                    break;
                case 'l':
                    mbutton[13].doClick();
                    break;
                case 'g':
                    mbutton[14].doClick();
                    break;
                case '/':
                    mbutton[15].doClick();
                    break;
                case '\\':
                    mbutton[16].doClick();
                    break;
                case 's':
                    mbutton[17].doClick();
                    break;
                case 'S':
                    mbutton[18].doClick();
                    break;
                case '}':
                    mbutton[19].doClick();
                    break;
                case 'D':
                    mbutton[20].doClick();
                    break;
                case 'F':
                    mbutton[21].doClick();
                    break;
                case 'P':
                    mbutton[22].doClick();
                    break;
                case 'i':
                    mbutton[23].doClick();
                    break;
                case 'E':
                    mbutton[24].doClick();
                    break;
                case 'W':
                    mbutton[25].doClick();
                    break;
                case '!':
                    mbutton[26].doClick();
                    break;
                case 'G':
                    mbutton[27].doClick();
                    break;
                case 'y':
                    mbutton[28].doClick();
                    break;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            SQUARELOCKED = !SQUARELOCKED;
            if (SQUARELOCKED) statusbar.setText(statusbar.getText() + "      (Locked)");
            else {
                if (lockx != currentx || locky != currenty) {
                    boolean dopaint = mappanel.clearTargets();
                    //monitembox.removeAll(); monitembox.repaint();
                    lockx = currentx;
                    locky = currenty;
                    setStatusBar(mapdata[currentx][currenty], currentx, currenty);
                    boolean dopaint2 = mappanel.doTargets(mapdata[currentx][currenty], currentx, currenty);
                    if (dopaint || dopaint2) mappanel.forcePaint();
                } else
                    statusbar.setText(statusbar.getText().substring(0, statusbar.getText().indexOf("(Locked)")).trim());
            }
            mappanel.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            if (partylevel == currentlevel) {
                mapdata[partyx][partyy].hasParty = false;
                mappanel.paintSquare(partyx, partyy, true);
            } else {
                MapData[][] tempdata = (MapData[][]) maplevels.get(partylevel);
                tempdata[partyx][partyy].hasParty = false;
            }
            partylevel = currentlevel;
            if (SQUARELOCKED) {
                partyx = lockx;
                partyy = locky;
                mapdata[lockx][locky].hasParty = true;
            } else {
                partyx = currentx;
                partyy = currenty;
                mapdata[currentx][currenty].hasParty = true;
            }
            mappanel.paintSquare(partyx, partyy, true);
            //mappanel.repaint();
            NEEDSAVE = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            NEEDSAVE = true;
            if ((!SQUARELOCKED || (SQUARELOCKED && lockx == currentx && locky == currenty)) && (mapdata[currentx][currenty].hasItems || (mapdata[currentx][currenty].mapchar == '[' && (mapdata[currentx][currenty].numitemsin[0] > 0 || mapdata[currentx][currenty].numitemsin[1] > 0 || mapdata[currentx][currenty].numitemsin[2] > 0 || mapdata[currentx][currenty].numitemsin[3] > 0)))) {
                if (mapdata[currentx][currenty].mapchar != '[')
                    mapdata[currentx][currenty].removeItem(mapdata[currentx][currenty].mapItems.size() - 1);
                else {
                    AlcoveData data = (AlcoveData) mapdata[currentx][currenty];
                    if (data.westside.size() > 0) data.removeItem(data.westside.size() - 1, 1);
                    else if (data.eastside.size() > 0) data.removeItem(data.eastside.size() - 1, 3);
                    else if (data.southside.size() > 0) data.removeItem(data.southside.size() - 1, 2);
                    else data.removeItem(data.northside.size() - 1, 0);
                }
                mappanel.paintSquare(currentx, currenty, true);
                //mappanel.repaint();
                setStatusBar(mapdata[currentx][currenty], currentx, currenty);
            } else if ((!SQUARELOCKED || (SQUARELOCKED && lockx == currentx && locky == currenty)) && mapdata[currentx][currenty].hasMons) {
                MonsterData olddata;
                boolean found = false;
                int sub;
                for (sub = 5; sub >= 0 && !found; ) {
                    olddata = (MonsterData) monhash.remove(currentlevel + "," + currentx + "," + currenty + "," + sub);
                    if (olddata != null) found = true;
                    else if (sub == 5) sub = 3;
                    else sub--;
                }
                if (sub != 5) mapdata[currentx][currenty].hasmonin[sub] = false;
                else mapdata[currentx][currenty].hasmonin[4] = false;
                if (!mapdata[currentx][currenty].hasmonin[0] && !mapdata[currentx][currenty].hasmonin[1] && !mapdata[currentx][currenty].hasmonin[2] && !mapdata[currentx][currenty].hasmonin[3] && !mapdata[currentx][currenty].hasmonin[4])
                    mapdata[currentx][currenty].hasMons = false;
                mappanel.paintSquare(currentx, currenty, true);
                //mappanel.repaint();
                setStatusBar(mapdata[currentx][currenty], currentx, currenty);
            } else if ((!SQUARELOCKED || (SQUARELOCKED && lockx == currentx && locky == currenty)) && mapdata[currentx][currenty].mapchar != 0) {
                //turn to floor
                boolean dopaint = mappanel.clearTargets();
                //monitembox.removeAll(); monitembox.repaint();
                boolean oldmons = mapdata[currentx][currenty].hasMons;
                boolean hadparty = mapdata[currentx][currenty].hasParty;
                boolean hadcloud = mapdata[currentx][currenty].hasCloud;
                int oldprojs = mapdata[currentx][currenty].numProjs;
                boolean waschanging = false;
                if (mapdata[currentx][currenty].mapchar == 't' || (mapdata[currentx][currenty].mapchar == 'l' && ((LauncherData) mapdata[currentx][currenty]).shootrate > 0) || (mapdata[currentx][currenty].mapchar == 'g' && (((GeneratorData) mapdata[currentx][currenty]).genrate > 0 || ((GeneratorData) mapdata[currentx][currenty]).delaying)) || (mapdata[currentx][currenty].mapchar == 'p' && ((PitData) mapdata[currentx][currenty]).isContinuous) || mapdata[currentx][currenty].mapchar == 'y')
                    waschanging = true;
                boolean oldnomons = mapdata[currentx][currenty].canPassMons;
                boolean oldnoghosts = mapdata[currentx][currenty].canPassImmaterial;
                undolist.add(mapdata[currentx][currenty]);
                undolist.add(new MapPoint(currentlevel, currentx, currenty));
                undoitem.setEnabled(true);
                undobutton.setEnabled(true);
                mapdata[currentx][currenty] = new FloorData();
                mapdata[currentx][currenty].hasParty = hadparty;
                mapdata[currentx][currenty].hasMons = oldmons;
                mapdata[currentx][currenty].numProjs = oldprojs;//preserve since changing to floor
                mapdata[currentx][currenty].hasCloud = hadcloud;//preserve clouds -> will be destroyed in game if on a wall or something (as will fluxcages, but they have no map flag)
                mapdata[currentx][currenty].canPassMons = oldnomons;
                mapdata[currentx][currenty].canPassImmaterial = oldnoghosts;
                boolean dopaint2 = mappanel.doTargets(mapdata[currentx][currenty], currentx, currenty);
                mappanel.paintSquare(currentx, currenty, true);
                if (dopaint || dopaint2) mappanel.forcePaint();
                //mappanel.repaint();
                setStatusBar(mapdata[currentx][currenty], currentx, currenty);
                if (waschanging) {
                    MapPoint temppoint;
                    boolean found = false;
                    int index = 0;
                    while (!found) {
                        temppoint = (MapPoint) mapstochange.get(index);
                        if (temppoint.level == currentlevel && temppoint.x == currentx && temppoint.y == currenty)
                            found = true;
                        else index++;
                    }
                    mapstochange.remove(index);
                    changingcount--;
                    if (changingcount == 0) mapchanging = false;
                }
                //statusbar.setText(mapdata[currentx][currenty]+" at "+currentlevel+","+currentx+","+currenty);
            }
        } else if (e.getKeyChar() == 'i' || e.getKeyChar() == 'I') {
            mbutton[1].doClick();
        } else if (e.getKeyChar() == 'm' || e.getKeyChar() == 'M') {
            mbutton[0].doClick();
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
            //save map
            if (mapfile == null) {
                saveAs();
                repaint();
            } else save();
        } else if (e.getKeyCode() == KeyEvent.VK_F7) {
            //load a map
            if (NEEDSAVE) {
                //pop up warning window
                int returnval = JOptionPane.showConfirmDialog(frame, "Dungeon Modified.\nSave it before loading?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (returnval == JOptionPane.YES_OPTION) {
                    if (mapfile == null) saveAs();
                    else save();
                    if (NEEDSAVE) return;//in case saveAs cancelled
                } else if (returnval != JOptionPane.NO_OPTION) return;
            }
            load(true);
            repaint();
        }
        //else if (e.getKeyChar()=='z' || e.getKeyChar()=='Z') {
        //        menulisten.actionPerformed(new ActionEvent(undoitem,0,"Undo"));
        //}
        else if (e.getKeyChar() == 'f' || e.getKeyChar() == 'F')
            System.out.println("" + Runtime.getRuntime().freeMemory());
    }
    
    public void newdungeon(boolean newmapfile) {
        SQUARELOCKED = false;
        if (mbutton[8].isSelected()) mbutton[2].doClick();
        mbutton[8].setEnabled(false);
        currentlevel = 0;
        if (heropanel.isVisible()) heropanel.dispose();
        heropanel = new HeroPanel(this, dungfont);
        partyedititem.setEnabled(false);
        //Item.ImageTracker = new MediaTracker(heropanel);
        if (newmapfile) {
            maplevels.clear();
            counter = 0;
            darkcounter = 0;
            darkfactor = 0;
            magictorch = 0;
            magicvision = 0;
            floatcounter = 0;
            dispell = 0;
            slowcount = 0;
            freezelife = 0;
            partylevel = 0;
            partyx = 1;
            partyy = 1;
            facing = 2;
            leader = 0;
            partyinfo.updateInfo();
            //heroatsub[0] = -1;
            //heroatsub[1] = -1;
            //heroatsub[2] = -1;
            //heroatsub[3] = -1;
            heroatsub = new int[4];
            iteminhand = false;
            spellready = 0;
            weaponready = 0;
            mirrorback = false;
            numheroes = 0;
            MAPLEVELS = 1;
            mapdata = new MapData[MAPWIDTH][MAPHEIGHT];
            for (int y = 0; y < MAPHEIGHT; y++) {
                for (int x = 0; x < MAPWIDTH; x++) {
                    mapdata[x][y] = new WallData();
                }
            }
            mapdata[1][1] = new FloorData();
            mapdata[1][1].hasParty = true;
            maplevels.add(mapdata);
            mapfile = null;
            AUTOMAP = false;
            leveldir = new String[1];
            if (leveldarkfactor.length > 1) leveldarkfactor = new int[1];
            leveldarkfactor[0] = 255;
            mappanel.repaint();
        }
        monitembox.removeAll();
        monhash.clear();
        dmprojs.clear();
        mapchanging = false;
        mapstochange.clear();
        cloudchanging = false;
        fluxchanging = false;
        cloudstochange.clear();
        fluxcages.clear();
        loopsounds.clear();
        undolist.clear();
        undoitem.setEnabled(false);
        undobutton.setEnabled(false);
    }
    
    public void saveAs() {
        //if (numheroes>0) dialog.setDirectory("Saves");
        //else dialog.setDirectory("Dungeons");
        dialog.setTitle("Save Dungeon");
        dialog.setMode(FileDialog.SAVE);
        dialog.show();
        String returnVal = dialog.getFile();
        if (returnVal != null) {
            mapfile = new File(dialog.getDirectory() + returnVal);
            String newrecent = mapfile.getPath();
            if (!recent1.getText().equals(newrecent)) {
                if (recent2.getText().equals(newrecent)) {
                    recent2.setText(recent1.getText());
                } else {
                    recent3.setText(recent2.getText());
                    recent2.setText(recent1.getText());
                }
                recent1.setText(newrecent);
                if (!filemenu.isMenuComponent(recent1)) filemenu.add(recent1);
                if (!filemenu.isMenuComponent(recent2) && !recent2.getText().equals("")) filemenu.add(recent2);
                if (!filemenu.isMenuComponent(recent3) && !recent3.getText().equals("")) filemenu.add(recent3);
            }
            save();
        }
    }
    
    public void save() {
        try {
            if (mapfile.exists()) {
                String mapname = mapfile.getPath();
                File oldbak = new File(mapname + ".bak");
                if (oldbak.exists()) oldbak.delete();
                //mapfile.renameTo(new File(mapname+".bak"));
                mapfile.renameTo(oldbak);
                mapfile = new File(mapname);
            }
            FileOutputStream out = new FileOutputStream(mapfile);
            ObjectOutputStream so = new ObjectOutputStream(out);
            
            //map start info
            so.writeUTF(version);
            so.writeBoolean(create);
            so.writeBoolean(nochar);
            if (create) {
                so.writeInt(levelpoints);
                so.writeInt(hsmpoints);
                so.writeInt(statpoints);
                so.writeInt(defensepoints);
                so.writeInt(itemchoose.size());
                if (itemchoose.size() > 0) {
                    for (int i = 0; i < itemchoose.size(); i++) {
                        so.writeObject(itemchoose.get(i));
                    }
                    so.writeInt(itempoints);
                }
                so.writeInt(abilitychoose.size());
                if (abilitychoose.size() > 0) {
                    so.writeInt(abilityauto);
                    for (int i = 0; i < abilitychoose.size(); i++) {
                        ((SpecialAbility) abilitychoose.get(i)).save(so);
                    }
                    so.writeInt(abilitypoints);
                }
            }
            
            //global stuff
            so.writeInt(counter);
            if (leveldarkfactor.length != MAPLEVELS) {
                int[] ld = new int[MAPLEVELS];
                for (int l = 0; l < MAPLEVELS; l++) {
                    if (l < leveldarkfactor.length) ld[l] = leveldarkfactor[l];
                    else ld[l] = 15;
                }
                leveldarkfactor = ld;
            }
            so.writeObject(leveldarkfactor);
            so.writeInt(darkcounter);
            so.writeInt(darkfactor);
            so.writeInt(magictorch);
            so.writeInt(magicvision);
            so.writeInt(floatcounter);
            so.writeInt(dispell);
            so.writeInt(slowcount);
            so.writeInt(freezelife);
            so.writeBoolean(mapchanging);
            so.writeBoolean(cloudchanging);
            so.writeBoolean(fluxchanging);
            
            so.writeInt(partylevel);
            so.writeInt(partyx);
            so.writeInt(partyy);
            
            so.writeInt(facing);
            so.writeInt(leader);
            so.writeObject(heroatsub);
            so.writeBoolean(iteminhand);
            if (iteminhand) so.writeObject(inhand);
            so.writeInt(spellready);
            so.writeInt(weaponready);
            so.writeBoolean(mirrorback);
            
            //monsters
            MonsterData tempmon;
            so.writeInt(monhash.size());
            for (Enumeration e = monhash.elements(); e.hasMoreElements(); ) {
                tempmon = (MonsterData) e.nextElement();
                so.writeBoolean(tempmon.isdying);
                tempmon.save(so);
            }
            
            //projectiles
            //need savegame test, also way to add/remove projs from map...
            so.writeInt(dmprojs.size());
            ProjectileData tempproj;
            for (Iterator i = dmprojs.iterator(); i.hasNext(); ) {
                tempproj = (ProjectileData) i.next();
                so.writeBoolean(tempproj.isending);
                //write true if proj is made of an item, else false
                if (tempproj.it != null) {
                    so.writeBoolean(true);
                    so.writeObject(tempproj.it);
                } else {
                    so.writeBoolean(false);
                    so.writeObject(tempproj.sp);
                }
                so.writeInt(tempproj.level);
                so.writeInt(tempproj.x);
                so.writeInt(tempproj.y);
                so.writeInt(tempproj.dist);
                so.writeInt(tempproj.direction);
                so.writeInt(tempproj.subsquare);
                if (tempproj.sp != null) {
                    so.writeInt(tempproj.powdrain);
                    so.writeInt(tempproj.powcount);
                }
                so.writeBoolean(tempproj.justthrown);
                so.writeBoolean(tempproj.notelnext);
            }
            tempproj = null;
            //System.out.println("projs saved");
            
            //heroes
            so.writeInt(numheroes);
            for (int i = 0; i < numheroes; i++) {
                hero[i].save(so);
            }
            //System.out.println("heroes saved\n");
            
            //mapObjects
            //System.out.print("saving map");
            so.writeInt(MAPLEVELS);
            so.writeInt(MAPWIDTH);
            so.writeInt(MAPHEIGHT);
            for (int l = 0; l < MAPLEVELS; l++) {
                mapdata = (MapData[][]) maplevels.get(l);
                if (l == partylevel) mapdata[partyx][partyy].hasParty = true;
                for (int x = 0; x < MAPWIDTH; x++) {
                    for (int y = 0; y < MAPHEIGHT; y++) {
                        mapdata[x][y].save(so);
                        //if (mapdata[x][y].mapchar=='t' || (mapdata[x][y].mapchar=='l' && ((LauncherData)mapdata[x][y]).shootrate>0) || (mapdata[x][y].mapchar=='g' && ((GeneratorData)mapdata[x][y]).genrate>0) || (mapdata[x][y].mapchar=='p' && ((PitData)mapdata[x][y]).isContinuous) || mapdata[x][y].mapchar=='y') mapstochange.add(new MapPoint(l,x,y));
                    }
                }
            }
            mapdata = (MapData[][]) maplevels.get(currentlevel);
            
            //mapchanging = true;
            if (mapchanging) {
                so.writeInt(mapstochange.size());
                for (Iterator i = mapstochange.iterator(); i.hasNext(); ) {
                    so.writeObject(i.next());
                }
            }
            if (cloudchanging) {
                PoisonCloudData tempcloud;
                so.writeInt(cloudstochange.size());
                for (Iterator i = cloudstochange.iterator(); i.hasNext(); ) {
                    tempcloud = (PoisonCloudData) i.next();
                    tempcloud.save(so);
                }
            }
            if (fluxchanging) {
                FluxCageData tempcage;
                so.writeInt(fluxcages.size());
                for (Enumeration e = fluxcages.elements(); e.hasMoreElements(); ) {
                    tempcage = (FluxCageData) e.nextElement();
                    tempcage.save(so);
                }
            }
            
            //save ambient sound data
            so.writeInt(loopsounds.size());
            for (int i = 0; i < loopsounds.size(); i++) {
                LoopSound sound = (LoopSound) loopsounds.get(i);
                so.writeUTF(sound.clipfile);
                so.writeInt(sound.x);
                so.writeInt(sound.y);
                so.writeInt(sound.loop);
                so.writeInt(sound.count);
            }
            
            //automap
            so.writeBoolean(AUTOMAP);
            if (AUTOMAP) so.writeObject(automap);
            
            //save map picture directory modifier
            if (leveldir.length != MAPLEVELS) {
                String[] ld = new String[MAPLEVELS];
                for (int l = 0; l < MAPLEVELS && l < leveldir.length; l++) {
                    ld[l] = leveldir[l];
                }
                leveldir = ld;
            }
            for (int l = 0; l < MAPLEVELS; l++) {
                if (leveldir[l] != null) so.writeUTF(leveldir[l]);
                else so.writeUTF("");
            }
            
            so.flush();
            out.close();
            if (!SQUARELOCKED) statusbar.setText("Dungeon Saved.");
            NEEDSAVE = false;
            //save custom items
            if (NEEDSAVEITEMS && ItemWizard.customitems.size() > 0) {
                out = new FileOutputStream(new File("custom_items.dat"));
                so = new ObjectOutputStream(out);
                so.writeInt(ItemWizard.customitems.size());
                for (int i = 0; i < ItemWizard.customitems.size(); i++) {
                    so.writeObject(ItemWizard.customitems.get(i));
                }
                so.flush();
                out.close();
                NEEDSAVEITEMS = false;
            }
            //save custom mons
            if (NEEDSAVEMONS && MonsterWizard.custommons.size() > 0) {
                MonsterData.NOITEMS = true;
                out = new FileOutputStream(new File("custom_mons.dat"));
                so = new ObjectOutputStream(out);
                so.writeInt(MonsterWizard.custommons.size());
                for (int i = 0; i < MonsterWizard.custommons.size(); i++) {
                    ((MonsterData) MonsterWizard.custommons.get(i)).save(so);
                }
                so.flush();
                out.close();
                MonsterData.NOITEMS = false;
                NEEDSAVEMONS = false;
            }
            undolist.clear();
            undoitem.setEnabled(false);
            undobutton.setEnabled(false);
        } catch (Exception e) {
            if (!SQUARELOCKED) statusbar.setText("Unable to save dungeon!");
            System.out.println("Unable to save dungeon!");
            //pop up a dialog too
            JOptionPane.showMessageDialog(frame, "Unable to save dungeon!", "Error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            MonsterData.NOITEMS = false;
            mapfile.delete();
        }
    }
    
    public void load(boolean choose) {
        if (choose) {
            dialog.setTitle("Load Dungeon");
            dialog.setMode(FileDialog.LOAD);
            dialog.show();
            String returnVal = dialog.getFile();
            if (returnVal != null) {
                mapfile = new File(dialog.getDirectory() + returnVal);
            } else return;
        }
        newdungeon(false);
        
        try {
            //cp.remove(spane);
            //loadingpanel.setSize(mpane.getSize());
            //loadingpanel.setPreferredSize(mpane.getSize());
            //cp.add("Center",loadingpanel);
            //cp.doLayout();
            //paint(getGraphics());
            spane.setVisible(false);
            statusbar.setText("Loading Map");
            paint(getGraphics());
            FileInputStream in = new FileInputStream(mapfile);
            ObjectInputStream si = new ObjectInputStream(in);
            
            //map start info
            String ver = si.readUTF();
            if (!ver.equals(version)) {
                in.close();
                statusbar.setText("Unable to load dungeon!");
                System.out.println("Incorrect Map Version: Found " + ver + ", need " + version);
                JOptionPane.showMessageDialog(frame, "Incorrect Map Version: Found " + ver + ", need " + version, "Error!", JOptionPane.ERROR_MESSAGE);
                spane.setVisible(true);
                return;
            }
            create = si.readBoolean();
            nochar = si.readBoolean();
            if (create) {
                levelpoints = si.readInt();
                hsmpoints = si.readInt();
                statpoints = si.readInt();
                defensepoints = si.readInt();
                itemchoose.clear();
                abilitychoose.clear();
                int num = si.readInt();
                if (num > 0) {
                    for (int i = 0; i < num; i++) {
                        itemchoose.add(si.readObject());
                    }
                    itempoints = si.readInt();
                } else itempoints = 0;
                num = si.readInt();
                if (num > 0) {
                    abilityauto = si.readInt();
                    for (int i = 0; i < num; i++) {
                        abilitychoose.add(new SpecialAbility(si));
                    }
                    abilitypoints = si.readInt();
                } else abilitypoints = 0;
            }
            
            //global stuff
            counter = si.readInt();
            leveldarkfactor = (int[]) si.readObject();
            darkcounter = si.readInt();
            darkfactor = si.readInt();
            magictorch = si.readInt();
            magicvision = si.readInt();
            floatcounter = si.readInt();
            dispell = si.readInt();
            slowcount = si.readInt();
            freezelife = si.readInt();
            mapchanging = si.readBoolean();
            cloudchanging = si.readBoolean();
            fluxchanging = si.readBoolean();
            
            partylevel = si.readInt();
            partyx = si.readInt();
            partyy = si.readInt();
            facing = si.readInt();
            leader = si.readInt();
            heroatsub = (int[]) si.readObject();
            iteminhand = si.readBoolean();
            if (iteminhand) inhand = (Item) si.readObject();
            spellready = si.readInt();
            weaponready = si.readInt();
            mirrorback = si.readBoolean();
            
            //monsters
            int nummons = si.readInt();
            boolean isdying;
            MonsterData tempmon;
            for (int i = 0; i < nummons; i++) {
                isdying = si.readBoolean(); //isdying = false;
                tempmon = new MonsterData(si);
                tempmon.isdying = isdying;
                monhash.put(tempmon.level + "," + tempmon.x + "," + tempmon.y + "," + tempmon.subsquare, tempmon);
            }
            //System.out.println("mons loaded, "+monhash.size()+" total");
            statusbar.setText("Loading Map.");
            paint(getGraphics());
            
            //projectiles
            int numprojs = si.readInt();
            boolean type, isending;
            ProjectileData tempproj;
            for (int i = 0; i < numprojs; i++) {
                isending = si.readBoolean();
                type = si.readBoolean();
                if (type)
                    tempproj = new ProjectileData((Item) si.readObject(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readBoolean(), si.readBoolean());
                else
                    tempproj = new ProjectileData((Spell) si.readObject(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readBoolean(), si.readBoolean());
                tempproj.isending = isending;
                dmprojs.add(tempproj);
            }
            //System.out.println("projs loaded\n");
            statusbar.setText("Loading Map..");
            paint(getGraphics());
            
            //heroes
            numheroes = si.readInt();
            if (numheroes > 0) {
                for (int i = 0; i < numheroes; i++) {
                    hero[i] = new HeroData(si.readUTF());
                    hero[i].heronumber = i;
                    hero[i].load(si);
                    if (i == leader) hero[i].isleader = true;
                    heropanel.addHero(hero[i]);
                }
                partyedititem.setEnabled(true);
                heropanel.setHero(hero[0]);
            }
            partyinfo.updateInfo();
            //System.out.println("heroes loaded\n");
            statusbar.setText("Loading Map...");
            paint(getGraphics());
            
            //try {
            //java.io.OutputStream out = new java.io.FileOutputStream("edload.log");
            //System.setErr(new java.io.PrintStream(out,true));
            //} catch (Exception e) {}
            
            currentlevel = partylevel;
            //mapObjects
            int oldmaplevels = MAPLEVELS, oldmapwidth = MAPWIDTH, oldmapheight = MAPHEIGHT;
            MAPLEVELS = si.readInt();
            MAPWIDTH = si.readInt();
            MAPHEIGHT = si.readInt();
            statusbar.setText("Loading Map....");
            paint(getGraphics());
            levelmenu.removeAll();
            levelmenuitem = new JMenuItem[MAPLEVELS];
            ArrayList oldmaps = maplevels;
            maplevels = new ArrayList(MAPLEVELS);
            MapData[][] oldmapdata;
            MapData oldmap;
            for (int l = 0; l < MAPLEVELS; l++) {
                if (l < oldmaplevels && oldmaps.size() > l) oldmapdata = (MapData[][]) oldmaps.get(l);
                else oldmapdata = null;
                mapdata = new MapData[MAPWIDTH][MAPHEIGHT];
                for (int x = 0; x < MAPWIDTH; x++) {
                    for (int y = 0; y < MAPHEIGHT; y++) {
                        if (oldmapdata != null && x < oldmapwidth && y < oldmapheight) oldmap = oldmapdata[x][y];
                        else oldmap = null;
                        mapdata[x][y] = loadMapData(si, l, x, y, oldmap);
                    }
                }
                maplevels.add(mapdata);
                if (l % (MAPLEVELS / 4 + 1) == 0) {
                    statusbar.setText(statusbar.getText() + ".");
                    paint(getGraphics());
                }
                levelmenuitem[l] = new JMenuItem("Level " + l, Character.forDigit(l, 10));
                levelmenuitem[l].addActionListener(menulisten);
                levelmenu.add(levelmenuitem[l]);
            }
            mapdata = (MapData[][]) maplevels.get(partylevel);
            if (partylevel > 0) mbutton[8].setEnabled(true);
            else {
                if (mbutton[8].isSelected()) mbutton[2].doClick();
                mbutton[8].setEnabled(false);
            }
                /*
                //set any necessary switch changeto pointers
                while (switchloading.size()>0) {
                        oldmap = (MapData)switchloading.remove(0);
                        if (oldmap.mapchar=='s') ((FloorSwitchData)oldmap).setChangeTo((MapData[][])maplevels.get(((FloorSwitchData)oldmap).targetlevel));
                        else ((WallSwitchData)oldmap).setChangeTo((MapData[][])maplevels.get(((WallSwitchData)oldmap).targetlevel));
                        //if (oldmap.mapchar=='s') ((FloorSwitch)oldmap).changeto = DungeonMap[((FloorSwitch)oldmap).targetlevel][((FloorSwitch)oldmap).targetx][((FloorSwitch)oldmap).targety];
                        //else ((WallSwitch)oldmap).changeto = DungeonMap[((WallSwitch)oldmap).targetlevel][((WallSwitch)oldmap).targetx][((WallSwitch)oldmap).targety];
                }
                */
            if (mapchanging) {
                changingcount = si.readInt();
                for (int i = 0; i < changingcount; i++) {
                    mapstochange.add(si.readObject());
                }
            }
            if (cloudchanging) {
                PoisonCloudData tempcloud;
                int numclouds = si.readInt();
                for (int i = 0; i < numclouds; i++) {
                    tempcloud = new PoisonCloudData(si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt());
                    cloudstochange.add(tempcloud);
                }
            }
            if (fluxchanging) {
                FluxCageData tempcage;
                int numcages = si.readInt();
                for (int i = 0; i < numcages; i++) {
                    tempcage = new FluxCageData(si.readInt(), si.readInt(), si.readInt(), si.readInt());
                    fluxcages.put(tempcage.level + "," + tempcage.x + "," + tempcage.y, tempcage);
                }
            }
            
            //load ambient sound data
            int numsounds = si.readInt();
            for (int i = 0; i < numsounds; i++) {
                String clipfile = si.readUTF();
                loopsounds.add(new LoopSound(null, clipfile, si.readInt(), si.readInt(), si.readInt(), si.readInt()));
            }
            
            //automap
            if (si.readBoolean()) {
                AUTOMAP = true;
                automap = (char[][][]) si.readObject();
            } else AUTOMAP = false;
            
            //load map picture directory modifier
            leveldir = new String[MAPLEVELS];
            for (int l = 0; l < MAPLEVELS; l++) {
                leveldir[l] = si.readUTF();
                if (leveldir[l].equals("")) leveldir[l] = null;
            }
            
            in.close();
            System.gc();
            mappanel.setNewSize();
            mappanel.invalidate();
            mpane.invalidate();
            spane.invalidate();
            mpane.validate();
            spane.validate();
            mappanel.repaint();
            spane.setVisible(true);
            statusbar.setText("Dungeon Loaded.");
            //Item.ImageTracker.checkID(0,true);
            NEEDSAVE = false;
            //update recent list
            String newrecent = mapfile.getPath();
            if (!recent1.getText().equals(newrecent)) {
                if (recent2.getText().equals(newrecent)) {
                    recent2.setText(recent1.getText());
                } else {
                    recent3.setText(recent2.getText());
                    recent2.setText(recent1.getText());
                }
                recent1.setText(newrecent);
                if (!filemenu.isMenuComponent(recent1)) filemenu.add(recent1);
                if (!filemenu.isMenuComponent(recent2) && !recent2.getText().equals("")) filemenu.add(recent2);
                if (!filemenu.isMenuComponent(recent3) && !recent3.getText().equals("")) filemenu.add(recent3);
            }
        } catch (Exception e) {
            statusbar.setText("Unable to load dungeon!");
            System.out.println("Unable to load dungeon!");
            //pop up a dialog too
            JOptionPane.showMessageDialog(frame, "Unable to load dungeon!", "Error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            spane.setVisible(true);
        }
    }
    
    static public MapData loadMapData(ObjectInputStream si, int lvl, int x, int y) throws IOException, ClassNotFoundException {
        return loadMapData(si, lvl, x, y, null);
    }
    
    static public MapData loadMapData(ObjectInputStream si, int lvl, int x, int y, MapData oldmap) throws IOException, ClassNotFoundException {
        char mapchar;
        MapData m = null;
        boolean canHoldItems, isPassable, canPassProjs, canPassMons, canPassImmaterial, drawItems, drawFurtherItems, hasParty, hasMons, hasItems;
        int numProjs;
        ArrayList mapItems = null;
        mapchar = si.readChar();
        canHoldItems = si.readBoolean();
        isPassable = si.readBoolean();
        canPassProjs = si.readBoolean();
        canPassMons = si.readBoolean();
        canPassImmaterial = si.readBoolean();
        drawItems = si.readBoolean();
        drawFurtherItems = si.readBoolean();
        numProjs = si.readInt();
        hasParty = si.readBoolean();
        hasMons = si.readBoolean();
        hasItems = si.readBoolean();
        if (hasItems) mapItems = (ArrayList) si.readObject();
        switch (mapchar) {
            case '1': //wall
                if (oldmap != null && oldmap.mapchar == '1') m = oldmap;
                else m = new WallData();
                break;
            case '0': //floor
                if (oldmap != null && oldmap.mapchar == '0') m = oldmap;
                else m = new FloorData();
                break;
            case 'd': //door
                m = new DoorData((MapPoint) si.readObject(), si.readInt(), si.readInt(), si.readInt(), si.readBoolean(), si.readBoolean(), si.readBoolean(), si.readInt(), si.readBoolean(), si.readInt(), si.readInt(), si.readBoolean(), si.readInt());
                //((DoorData)m).changecount = si.readInt();
                //((DoorData)m).isclosing = si.readBoolean();
                //if ( ((DoorData)m).isBreakable && !((DoorData)m).isBroken ) ((DoorData)m).breakpoints = si.readInt();
                m.load(si);
                break;
            case 's': //floorswitch
                m = new FloorSwitchData();
                m.load(si);//for everything
                break;
            case '/': //wallswitch
                m = new WallSwitchData(si.readInt());
                m.load(si);//for everything but side
                break;
            case 't': //teleport
                //m = new TeleportData(si.readInt(),si.readInt(),si.readInt(),si.readInt(),si.readInt(),si.readInt(),si.readInt(),si.readInt(),si.readInt(),si.readInt(),si.readInt(),si.readBoolean(),si.readBoolean(),si.readInt(),si.readInt(),si.readBoolean(),si.readBoolean(),si.readBoolean(),si.readBoolean(),si.readBoolean(),si.readInt(),si.readInt(),si.readBoolean(),si.readBoolean(),si.readInt(),si.readBoolean(),si.readInt(),si.readBoolean());
                m = new TeleportData();
                m.load(si);
                break;
            case ']': //onealcove
                m = new OneAlcoveData(si.readInt());
                m.load(si);//for floorswitch stuff
                break;
            case '[': //alcove
                m = new AlcoveData();
                m.load(si);//for vectors and floorswitch stuff
                break;
            case 'a': //altar
                m = new AltarData(si.readInt());
                m.load(si);//for floorswitch stuff
                break;
            case '2': //fakewall
                if (oldmap != null && oldmap.mapchar == '2') m = oldmap;
                else m = new FakeWallData();
                break;
            case 'f': //fountain
                m = new FountainData(si.readInt());
                m.load(si);
                break;
            case 'p': //pit
                m = new PitData(si.readInt(), si.readInt(), si.readInt(), si.readBoolean(), si.readBoolean(), si.readBoolean(), si.readBoolean(), si.readBoolean(), si.readBoolean(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readBoolean(), si.readInt(), si.readInt(), si.readBoolean(), si.readInt(), si.readBoolean(), si.readInt(), si.readBoolean(), si.readInt());
                break;
            case '>': //stairs
                int side = si.readInt();
                boolean goesUp = si.readBoolean();
                if (oldmap != null && oldmap.mapchar == '>' && ((StairsData) oldmap).goesUp == goesUp) {
                    m = oldmap;
                    ((StairsData) m).side = side;
                } else m = new StairsData(side, goesUp);
                //m = new StairsData(si.readInt(),si.readBoolean());
                break;
            case 'l': //launcher
                side = si.readInt();
                m = new LauncherData(si.readInt(), si.readInt(), si.readInt(), side, si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readBoolean(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readBoolean());
                m.load(si);
                break;
            case 'm': //mirror
                m = new MirrorData(si.readInt());
                m.load(si);//for hero and wasused
                break;
            case 'g': //generator
                m = new GeneratorData(si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readBoolean(), si.readInt(), si.readInt(), si.readInt(), si.readInt(), si.readBoolean(), new MonsterData(si));
                //si.readBoolean();//delaying
                ((GeneratorData) m).delaying = si.readBoolean();
                break;
            case 'w': //writing
                side = si.readInt();
                String[] message = (String[]) si.readObject();
                if (oldmap != null && oldmap.mapchar == 'w') {
                    m = oldmap;
                    ((WritingData) m).message = message;
                } else m = new WritingData(side, message);
                //m = new WritingData(si.readInt(),(String[])si.readObject());
                break;
            case 'W': //gamewinsquare (note: has same mapchar as writing2 -> but one of writings will go away)
                m = new GameWinData(si.readUTF(), si.readUTF());
                break;
            case 'S': //multfloorswitch
                m = new MultFloorSwitchData();
                m.load(si);//for everything
                break;
            case '\\': //multwallswitch
                m = new MultWallSwitchData(si.readInt());
                m.load(si);//for everything except side
                break;
            case '}': //sconce
                m = new SconceData(si.readInt());
                m.load(si);//for torch and switch stuff
                break;
            case '!': //stormbringer
                m = new StormbringerData(si.readBoolean());
                break;
            case 'G': //power gem
                m = new PowerGemData(si.readBoolean());
                break;
            case 'D': //decoration
                side = si.readInt();
                int number = si.readInt();
                if (oldmap != null && oldmap.mapchar == 'D') {
                    m = oldmap;
                    ((DecorationData) m).side = side;
                    ((DecorationData) m).number = number;
                } else m = new DecorationData(side, number);
                //m = new DecorationData(si.readInt(),si.readInt());
                break;
            case 'F': //floor decoration
                number = si.readInt();
                if (oldmap != null && oldmap.mapchar == 'F') {
                    m = oldmap;
                    ((FDecorationData) m).number = number;
                } else m = new FDecorationData(number);
                if (number == 3) {
                    ((FDecorationData) m).level = si.readInt();
                    ((FDecorationData) m).xcoord = si.readInt();
                    ((FDecorationData) m).ycoord = si.readInt();
                }
                //m = new FDecorationData(si.readInt());
                break;
            case 'P': //pillar
                int type = si.readInt();
                boolean mirror = si.readBoolean();
                if (oldmap != null && oldmap.mapchar == 'P') {
                    m = oldmap;
                    ((PillarData) m).type = type;
                    ((PillarData) m).mirror = mirror;
                } else m = new PillarData(type, mirror);
                if (type == 2) ((PillarData) m).custompic = si.readUTF();
                //m = new PillarData(si.readInt(),si.readBoolean());
                break;
            case 'i': //invisible wall
                if (oldmap != null && oldmap.mapchar == 'i') m = oldmap;
                else m = new InvisibleWallData();
                break;
            case 'E': //event square
                m = new EventSquareData();
                m.load(si);
                break;
            case 'y': //fulya pit
                m = new FulYaPitData((MapPoint) si.readObject(), si.readInt(), (MapPoint) si.readObject(), (MapPoint) si.readObject());
                break;
            //case 'c': //customsided
            //        //m = new CustomSided(si.readInt(),si.readUTF(),(int[])si.readObject(),(int[])si.readObject());
            //        break;
        }
        m.canHoldItems = canHoldItems;
        m.isPassable = isPassable;
        m.canPassProjs = canPassProjs;
        m.canPassMons = canPassMons;
        m.canPassImmaterial = canPassImmaterial;
        m.drawItems = drawItems;
        m.drawFurtherItems = drawFurtherItems;
        m.numProjs = numProjs;
        m.hasParty = hasParty;
        m.hasMons = hasMons;
        m.hasItems = hasItems;
        m.mapItems = mapItems;
        m.numitemsin[0] = 0;
        m.numitemsin[1] = 0;
        m.numitemsin[2] = 0;
        m.numitemsin[3] = 0;
        m.hasmonin[0] = false;
        m.hasmonin[1] = false;
        m.hasmonin[2] = false;
        m.hasmonin[3] = false;
        if (hasItems && lvl >= 0) {
            //System.out.println(mapchar+" at "+lvl+","+x+","+y);
            if (mapchar == ']' || mapchar == 'a' || mapchar == 'f') {
                //set hasItems[] and numitemsin[] by (side+2)%4
                int side = (((SidedWallData) m).side + 2) % 4;
                m.numitemsin[side] = mapItems.size();
            } else {
                //set hasItems[] and numitemsin[] by subsquare
                int ss;
                Item tempitem;
                for (Iterator i = mapItems.iterator(); i.hasNext(); ) {
                    tempitem = (Item) i.next();
                    m.numitemsin[tempitem.subsquare]++;
                }
            }
        } else if (mapchar == '[') {
            //set hasItems[] and numitemsin[] by each side (northside,westside,...)
            if (!((AlcoveData) m).northside.isEmpty()) {
                m.numitemsin[0] = ((AlcoveData) m).northside.size();
            }
            if (!((AlcoveData) m).westside.isEmpty()) {
                m.numitemsin[1] = ((AlcoveData) m).westside.size();
            }
            if (!((AlcoveData) m).southside.isEmpty()) {
                m.numitemsin[2] = ((AlcoveData) m).southside.size();
            }
            if (!((AlcoveData) m).eastside.isEmpty()) {
                m.numitemsin[3] = ((AlcoveData) m).eastside.size();
            }
        }
        if (hasMons) {
            for (int sub = 0; sub < 6; ) {
                if (monhash.get(lvl + "," + x + "," + y + "," + sub) != null) {
                    if (sub < 4) m.hasmonin[sub] = true;
                    else m.hasmonin[4] = true;
                }
                if (sub == 3) sub = 5;
                else sub++;
            }
        }
        if (!canPassMons && (!(m instanceof WallData) || m.mapchar == '2' || m.mapchar == '>')) {
            m.nomons = true;
        } else m.nomons = false;
        if (!canPassImmaterial && m.mapchar != '!' && m.mapchar != 'G') m.noghosts = true;
        else m.noghosts = false;
        return m;
    }
    
    public void setStatusBar(MapData md, int x, int y) {
        statusbar.setText(md + " at " + currentlevel + "," + x + "," + y);
        if (md.nomons) statusbar.setText(statusbar.getText() + "  (No Mons)");
        else if (md.noghosts) statusbar.setText(statusbar.getText() + "  (No Ghosts)");
        if (md.mapchar == 't') {
            statusbar.setText(statusbar.getText() + "       Targets " + ((TeleportData) md).targetlevel + "," + ((TeleportData) md).targetx + "," + ((TeleportData) md).targety);
        } else if (md.mapchar == '/') {
            statusbar.setText(statusbar.getText() + "       Targets " + ((WallSwitchData) md).targetlevel + "," + ((WallSwitchData) md).targetx + "," + ((WallSwitchData) md).targety);
        } else if (md.mapchar == 's') {
            statusbar.setText(statusbar.getText() + "       Targets " + ((FloorSwitchData) md).targetlevel + "," + ((FloorSwitchData) md).targetx + "," + ((FloorSwitchData) md).targety);
        } else if (md.mapchar == '\\') {
            MultWallSwitchData mwd = (MultWallSwitchData) md;
            int[] target;
            int firstlevel = 0, firstx = 0, firsty = 0;
            boolean multtargs = false;
            int i = 0;
            while (!multtargs && i < mwd.switchlist.size()) {
                target = mwd.getTarget(i);
                if (i == 0) {
                    firstlevel = target[0];
                    firstx = target[1];
                    firsty = target[2];
                } else if (!multtargs && (firstlevel != target[0] || firstx != target[1] || firsty != target[2]))
                    multtargs = true;
                i++;
            }
            if (multtargs) statusbar.setText(statusbar.getText() + "       (Multiple Targets)");
            else {
                target = mwd.getTarget(0);
                statusbar.setText(statusbar.getText() + "       Targets " + target[0] + "," + target[1] + "," + target[2]);
            }
        } else if (md.mapchar == 'S') {
            MultFloorSwitchData mwd = (MultFloorSwitchData) md;
            int[] target;
            int firstlevel = 0, firstx = 0, firsty = 0;
            boolean multtargs = false;
            int i = 0;
            while (!multtargs && i < mwd.switchlist.size()) {
                target = mwd.getTarget(i);
                if (i == 0) {
                    firstlevel = target[0];
                    firstx = target[1];
                    firsty = target[2];
                } else if (!multtargs && (firstlevel != target[0] || firstx != target[1] || firsty != target[2]))
                    multtargs = true;
                i++;
            }
            if (multtargs) statusbar.setText(statusbar.getText() + "       (Multiple Targets)");
            else {
                target = mwd.getTarget(0);
                statusbar.setText(statusbar.getText() + "       Targets " + target[0] + "," + target[1] + "," + target[2]);
            }
        } else if (md.mapchar == '}') {
            //if isswitch, outline the target if on screen
            SconceData d = (SconceData) md;
            if (d.isSwitch) {
                int[] target;
                int firstlevel = 0, firstx = 0, firsty = 0;
                boolean multtargs = false;
                int i = 0;
                while (!multtargs && i < d.sconceswitch.switchlist.size()) {
                    target = d.sconceswitch.getTarget(i);
                    if (i == 0) {
                        firstlevel = target[0];
                        firstx = target[1];
                        firsty = target[2];
                    } else if (!multtargs && (firstlevel != target[0] || firstx != target[1] || firsty != target[2]))
                        multtargs = true;
                    i++;
                }
                if (multtargs) statusbar.setText(statusbar.getText() + "       (Multiple Targets)");
                else {
                    target = d.sconceswitch.getTarget(0);
                    statusbar.setText(statusbar.getText() + "       Targets " + target[0] + "," + target[1] + "," + target[2]);
                }
            }
        } else if (md instanceof OneAlcoveData) {
            OneAlcoveData d = (OneAlcoveData) md;
            if (d.isSwitch) {
                int[] target;
                int firstlevel = 0, firstx = 0, firsty = 0;
                boolean multtargs = false;
                int i = 0;
                while (!multtargs && i < d.alcoveswitchdata.switchlist.size()) {
                    target = d.alcoveswitchdata.getTarget(i);
                    if (i == 0) {
                        firstlevel = target[0];
                        firstx = target[1];
                        firsty = target[2];
                    } else if (!multtargs && (firstlevel != target[0] || firstx != target[1] || firsty != target[2]))
                        multtargs = true;
                    i++;
                }
                if (multtargs) statusbar.setText(statusbar.getText() + "       (Multiple Targets)");
                else {
                    target = d.alcoveswitchdata.getTarget(0);
                    statusbar.setText(statusbar.getText() + "       Targets " + target[0] + "," + target[1] + "," + target[2]);
                }
            }
        } else if (md.mapchar == '[') {
            AlcoveData d = (AlcoveData) md;
            if (d.isSwitch) {
                int[] target;
                int firstlevel = 0, firstx = 0, firsty = 0;
                boolean multtargs = false;
                int i = 0;
                while (!multtargs && i < d.alcoveswitchdata.switchlist.size()) {
                    target = d.alcoveswitchdata.getTarget(i);
                    if (i == 0) {
                        firstlevel = target[0];
                        firstx = target[1];
                        firsty = target[2];
                    } else if (!multtargs && (firstlevel != target[0] || firstx != target[1] || firsty != target[2]))
                        multtargs = true;
                    i++;
                }
                if (multtargs) statusbar.setText(statusbar.getText() + "       (Multiple Targets)");
                else {
                    target = d.alcoveswitchdata.getTarget(0);
                    statusbar.setText(statusbar.getText() + "       Targets " + target[0] + "," + target[1] + "," + target[2]);
                }
            }
        } else if (md.mapchar == 'f') {
            FountainData d = (FountainData) md;
            if (d.fountainswitch != null) {
                int[] target;
                int firstlevel = 0, firstx = 0, firsty = 0;
                boolean multtargs = false;
                int i = 0;
                while (!multtargs && i < d.fountainswitch.switchlist.size()) {
                    target = d.fountainswitch.getTarget(i);
                    if (i == 0) {
                        firstlevel = target[0];
                        firstx = target[1];
                        firsty = target[2];
                    } else if (!multtargs && (firstlevel != target[0] || firstx != target[1] || firsty != target[2]))
                        multtargs = true;
                    i++;
                }
                if (multtargs) statusbar.setText(statusbar.getText() + "       (Multiple Targets)");
                else {
                    target = d.fountainswitch.getTarget(0);
                    statusbar.setText(statusbar.getText() + "       Targets " + target[0] + "," + target[1] + "," + target[2]);
                }
            }
        } else if (md.mapchar == 'm') {
            if (((MirrorData) md).target != null) {
                statusbar.setText(statusbar.getText() + "       Targets " + ((MirrorData) md).target.level + "," + ((MirrorData) md).target.x + "," + ((MirrorData) md).target.y);
            }
        } else if (md.mapchar == 'y') {
            FulYaPitData fypit = (FulYaPitData) md;
            statusbar.setText(statusbar.getText() + "       If Key Targets " + fypit.keytarget.level + "," + fypit.keytarget.x + "," + fypit.keytarget.y + "       If Not Key Targets " + fypit.nonkeytarget.level + "," + fypit.nonkeytarget.x + "," + fypit.nonkeytarget.y);
        }
        if (SQUARELOCKED) statusbar.setText(statusbar.getText() + "      (Locked)");
        monitembox.removeAll();
        if (md.hasMons || md.hasItems || md.mapchar == 'm' || md.mapchar == 'g' || md.mapchar == 'E' || (md instanceof AlcoveData && ((AlcoveData) md).holdingItems())) {
            monitembox.add(Box.createVerticalGlue());
            if (md.mapchar == 'm') {
                HeroData hero = ((MirrorData) md).hero;
                if (hero != null) {
                    monitembox.add(new JLabel(new ImageIcon(hero.pic)));
                    if (hero.weapon != fistfoot) monitembox.add(new JLabel(new ImageIcon(hero.weapon.pic)));
                    if (hero.hand != null) monitembox.add(new JLabel(new ImageIcon(hero.hand.pic)));
                    if (hero.head != null) monitembox.add(new JLabel(new ImageIcon(hero.head.pic)));
                    if (hero.neck != null) monitembox.add(new JLabel(new ImageIcon(hero.neck.pic)));
                    if (hero.torso != null) monitembox.add(new JLabel(new ImageIcon(hero.torso.pic)));
                    if (hero.legs != null) monitembox.add(new JLabel(new ImageIcon(hero.legs.pic)));
                    if (hero.feet != null) monitembox.add(new JLabel(new ImageIcon(hero.feet.pic)));
                    for (int i = 0; i < 6; i++) {
                        if (hero.quiver[i] != null) monitembox.add(new JLabel(new ImageIcon(hero.quiver[i].pic)));
                    }
                    if (hero.pouch1 != null) monitembox.add(new JLabel(new ImageIcon(hero.pouch1.pic)));
                    if (hero.pouch2 != null) monitembox.add(new JLabel(new ImageIcon(hero.pouch2.pic)));
                    for (int i = 0; i < 16; i++) {
                        if (hero.pack[i] != null) monitembox.add(new JLabel(new ImageIcon(hero.pack[i].pic)));
                    }
                }
            } else if (md.mapchar == 'g') {
                MonsterData monster = ((GeneratorData) md).monster;
                JLabel genmonlab = new JLabel(monster.pic);
                genmonlab.setForeground(Color.black);
                int numtogen = ((GeneratorData) md).numtogen;
                if (numtogen == 5) genmonlab.setText("R");
                else genmonlab.setText("" + numtogen);
                monitembox.add(genmonlab);
                                /*
                                JLabel monitemlab;
                                for (int i=0;i<monster.carrying.size();i++) {
                                        Item tempitem = ((Item)monster.carrying.get(i));
                                        monitemlab = new JLabel(new ImageIcon(tempitem.pic));
                                        monitembox.add(monitemlab);
                                        if (tempitem.number==5) {
                                                Item tempitem2;
                                                for (int j=0;j<12;j++) {
                                                        tempitem2 = ((Chest)tempitem).itemAt(j);
                                                        if (tempitem2!=null) {
                                                                JLabel chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                                                                monitembox.add(chestitemlab);
                                                        }
                                                }
                                        }
                                }
                                */
                addMonsterItems(monster);
            } else if (md.mapchar == 'E') {
                //event squares
                EventSquareData ed = (EventSquareData) md;
                Action a;
                JLabel chestitemlab;
                for (int i = 0; i < ed.choices.length; i++) {
                    for (int j = 0; j < ed.choices[i].actions.size(); j++) {
                        a = (Action) ed.choices[i].actions.get(j);
                        if (a.actiontype == 4) monitembox.add(new JLabel(new ImageIcon(((HeroData) a.action).pic)));
                        else if (a.actiontype == 5) {
                            monitembox.add(new JLabel(new ImageIcon(((Item) a.action).pic)));
                            if (((Item) a.action).number == 5) {
                                Item tempitem2;
                                for (int k = 0; k < 12; k++) {
                                    tempitem2 = ((Chest) ((Item) a.action)).itemAt(k);
                                    if (tempitem2 != null) {
                                        chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                                        monitembox.add(chestitemlab);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (md.hasMons) {
                MonsterData data;
                JButton monbutton;
                //JLabel monitemlab;
                Dimension mondim = new Dimension(45, 45);
                for (int sub = 0; sub < 6; ) {
                    data = (MonsterData) monhash.get(currentlevel + "," + x + "," + y + "," + sub);
                    if (data != null) {
                        monbutton = new JButton(data.pic);
                        monbutton.setFocusable(false);
                        if (data.subsquare == 0) monbutton.setText("NW");
                        else if (data.subsquare == 1) monbutton.setText("NE");
                        else if (data.subsquare == 2) monbutton.setText("SE");
                        else if (data.subsquare == 3) monbutton.setText("SW");
                        monbutton.setPreferredSize(mondim);
                        monbutton.setMargin(new Insets(0, 5, 0, 5));
                        monbutton.setActionCommand("Monster");
                        monbutton.addActionListener((DMEditor) frame);
                        monitembox.add(monbutton);
                                                /*
                                                for (int i=0;i<data.carrying.size();i++) {
                                                        Item tempitem = ((Item)data.carrying.get(i));
                                                        monitemlab = new JLabel(new ImageIcon(tempitem.pic));
                                                        monitembox.add(monitemlab);
                                                        if (tempitem.number==5) {
                                                                Item tempitem2;
                                                                for (int j=0;j<12;j++) {
                                                                        tempitem2 = ((Chest)tempitem).itemAt(j);
                                                                        if (tempitem2!=null) {
                                                                                JLabel chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                                                                                monitembox.add(chestitemlab);
                                                                        }
                                                                }
                                                        }
                                                }
                                                */
                        addMonsterItems(data);
                    }
                    if (sub == 3) sub = 5;
                    else sub++;
                }
            }
            if (md instanceof AlcoveData && ((AlcoveData) md).holdingItems()) {
                JButton itembutton;
                JLabel chestitemlab;
                Dimension itemdim = new Dimension(45, 45);
                Item tempitem;
                int index = 0;
                for (Iterator iter = ((AlcoveData) md).northside.iterator(); iter.hasNext(); index++) {
                    tempitem = (Item) iter.next();
                    itembutton = new JButton("N", new ImageIcon(tempitem.pic));
                    itembutton.setFocusable(false);
                    itembutton.setPreferredSize(itemdim);
                    itembutton.setMargin(new Insets(0, 5, 0, 5));
                    itembutton.setActionCommand("" + index);
                    itembutton.addActionListener(itemlisten);
                    monitembox.add(itembutton);
                    if (tempitem.number == 5) {
                        Item tempitem2;
                        for (int i = 0; i < 12; i++) {
                            tempitem2 = ((Chest) tempitem).itemAt(i);
                            if (tempitem2 != null) {
                                chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                                monitembox.add(chestitemlab);
                            }
                        }
                    }
                }
                index = 0;
                for (Iterator iter = ((AlcoveData) md).southside.iterator(); iter.hasNext(); index++) {
                    tempitem = (Item) iter.next();
                    itembutton = new JButton("S", new ImageIcon(tempitem.pic));
                    itembutton.setFocusable(false);
                    itembutton.setPreferredSize(itemdim);
                    itembutton.setMargin(new Insets(0, 5, 0, 5));
                    itembutton.setActionCommand("" + index);
                    itembutton.addActionListener(itemlisten);
                    monitembox.add(itembutton);
                    if (tempitem.number == 5) {
                        Item tempitem2;
                        for (int i = 0; i < 12; i++) {
                            tempitem2 = ((Chest) tempitem).itemAt(i);
                            if (tempitem2 != null) {
                                chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                                monitembox.add(chestitemlab);
                            }
                        }
                    }
                }
                index = 0;
                for (Iterator iter = ((AlcoveData) md).eastside.iterator(); iter.hasNext(); index++) {
                    tempitem = (Item) iter.next();
                    itembutton = new JButton("E", new ImageIcon(tempitem.pic));
                    itembutton.setFocusable(false);
                    itembutton.setPreferredSize(itemdim);
                    itembutton.setMargin(new Insets(0, 5, 0, 5));
                    itembutton.setActionCommand("" + index);
                    itembutton.addActionListener(itemlisten);
                    monitembox.add(itembutton);
                    if (tempitem.number == 5) {
                        Item tempitem2;
                        for (int i = 0; i < 12; i++) {
                            tempitem2 = ((Chest) tempitem).itemAt(i);
                            if (tempitem2 != null) {
                                chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                                monitembox.add(chestitemlab);
                            }
                        }
                    }
                }
                index = 0;
                for (Iterator iter = ((AlcoveData) md).westside.iterator(); iter.hasNext(); index++) {
                    tempitem = (Item) iter.next();
                    itembutton = new JButton("W", new ImageIcon(tempitem.pic));
                    itembutton.setFocusable(false);
                    itembutton.setPreferredSize(itemdim);
                    itembutton.setMargin(new Insets(0, 5, 0, 5));
                    itembutton.setActionCommand("" + index);
                    itembutton.addActionListener(itemlisten);
                    monitembox.add(itembutton);
                    if (tempitem.number == 5) {
                        Item tempitem2;
                        for (int i = 0; i < 12; i++) {
                            tempitem2 = ((Chest) tempitem).itemAt(i);
                            if (tempitem2 != null) {
                                chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                                monitembox.add(chestitemlab);
                            }
                        }
                    }
                }
            } else if (md.hasItems) {
                boolean alcove = false;
                if (md instanceof OneAlcoveData || md.mapchar == 'f') alcove = true;
                JButton itembutton;
                JLabel chestitemlab;
                Dimension itemdim = new Dimension(45, 45);
                Item tempitem;
                int numitems = md.mapItems.size();
                for (int index = 0; index < numitems; index++) {
                    tempitem = (Item) md.mapItems.get(index);
                    itembutton = new JButton(new ImageIcon(tempitem.pic));
                    itembutton.setFocusable(false);
                    if (!alcove) {
                        if (tempitem.subsquare == 0) itembutton.setText("NW");
                        else if (tempitem.subsquare == 1) itembutton.setText("NE");
                        else if (tempitem.subsquare == 2) itembutton.setText("SE");
                        else itembutton.setText("SW");
                    }
                    itembutton.setPreferredSize(itemdim);
                    itembutton.setMargin(new Insets(0, 5, 0, 5));
                    itembutton.setActionCommand("" + index);
                    itembutton.addActionListener(itemlisten);
                    monitembox.add(itembutton);
                    if (tempitem.number == 5) {
                        Item tempitem2;
                        for (int i = 0; i < 12; i++) {
                            tempitem2 = ((Chest) tempitem).itemAt(i);
                            if (tempitem2 != null) {
                                chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                                monitembox.add(chestitemlab);
                            }
                        }
                    }
                }
            }
            monitembox.add(Box.createVerticalGlue());
        }
        monitembox.validate();
        monitembox.repaint();
        mipane.validate();
        mipane.repaint();
    }
    
    //add labels for all items equipped/carried by mon
    private void addMonsterItems(MonsterData monster) {
        JLabel monitemlab;
        boolean done = false;
        ArrayList monlist;
        if (monster.equipped != null) monlist = monster.equipped;
        else monlist = monster.carrying;
        while (!done) {
            for (int i = 0; i < monlist.size(); i++) {
                Item tempitem = ((Item) monlist.get(i));
                monitemlab = new JLabel(new ImageIcon(tempitem.pic));
                monitembox.add(monitemlab);
                if (tempitem.number == 5) {
                    Item tempitem2;
                    for (int j = 0; j < 12; j++) {
                        tempitem2 = ((Chest) tempitem).itemAt(j);
                        if (tempitem2 != null) {
                            JLabel chestitemlab = new JLabel(new ImageIcon(tempitem2.pic));
                            monitembox.add(chestitemlab);
                        }
                    }
                }
            }
            if (monlist == monster.carrying) done = true;
            else monlist = monster.carrying;
        }
    }
    
    //public static TargetFrame getTargetFrame() {
    //return new TargetFrame(frame,MAPLEVELS,MAPWIDTH,MAPHEIGHT,currentlevel,maplevels);
    //        return targetframe;
    //}
    
    public boolean accept(File dir, String name) {
        name = name.toLowerCase();
        if (name.endsWith(".dat") || name.endsWith(".data") || name.endsWith(".sav")) return true;
        return false;
    }
    
    class MapClick extends MouseAdapter implements MouseMotionListener {
        public int x = -1, y = -1;
        
        public void mouseMoved(MouseEvent e) {
            int newx, newy;
            if (!ZOOMING) {
                newx = e.getX() / 33;
                newy = e.getY() / 33;
            } else {
                newx = e.getX() / 17;
                newy = e.getY() / 17;
            }
            if (newx >= MAPWIDTH || newy >= MAPHEIGHT || (newx == x && newy == y)) return;
            x = newx;
            y = newy;
            currentx = x;
            currenty = y;
            if (SQUARELOCKED) return;
            boolean dopaint = doExit();
            lockx = x;
            locky = y;
            boolean dopaint2 = doEnter(mapdata[x][y], x, y);
            if (dopaint || dopaint2) mappanel.forcePaint();
        }
        
        public void mouseDragged(MouseEvent e) {
            int newx, newy;
            if (!ZOOMING) {
                newx = e.getX() / 33;
                newy = e.getY() / 33;
            } else {
                newx = e.getX() / 17;
                newy = e.getY() / 17;
            }
            if (newx >= MAPWIDTH || newy >= MAPHEIGHT || (newx == x && newy == y)) return;
            x = newx;
            y = newy;
            currentx = x;
            currenty = y;
            if (SQUARELOCKED) return;
            boolean dopaint = doExit();
            lockx = x;
            locky = y;
            boolean dopaint2 = doEnter(mapdata[x][y], x, y);
            if (dopaint || dopaint2) mappanel.forcePaint();
            mousePressed(new MouseEvent(mappanel, MouseEvent.MOUSE_PRESSED, 0, 0, e.getX(), e.getY(), 0, false));
        }
        
        private boolean doExit() {
            monitembox.removeAll();
            monitembox.repaint();
            return mappanel.clearTargets();
            //boolean hadtargs = mappanel.clearTargets();
            //return hadtargs;
        }
        
        private boolean doEnter(MapData m, int x, int y) {
            setStatusBar(m, x, y);
            return mappanel.doTargets(m, x, y);
            //boolean hastargs = mappanel.doTargets(m,x,y);
            //return hastargs;
        }
        
        public void mouseExited(MouseEvent e) {
            if (!SQUARELOCKED) {
                x = -1;
                y = -1;
                mappanel.clearTargets();
                mappanel.forcePaint();
            }
        }
        
        public void mouseEntered(MouseEvent e) {
            mouseMoved(e);
        }
        
        public void mouseReleased(MouseEvent e) {
            MOUSEDOWN = false;
        }
        
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                SQUARELOCKED = !SQUARELOCKED;
                if (SQUARELOCKED) statusbar.setText(statusbar.getText() + "      (Locked)");
                else {
                    if (lockx != currentx || locky != currenty) {
                        boolean dopaint = doExit();
                        lockx = currentx;
                        locky = currenty;
                        boolean dopaint2 = doEnter(mapdata[currentx][currenty], currentx, currenty);
                        if (dopaint || dopaint2) mappanel.forcePaint();
                        //else if (dopaint) mappanel.paint(mappanel.getGraphics());
                    } else
                        statusbar.setText(statusbar.getText().substring(0, statusbar.getText().indexOf("(Locked)")).trim());
                }
                mappanel.repaint();
                return;
            }
            int clickx, clicky;
            if (!ZOOMING) {
                clickx = e.getX() / 33;
                clicky = e.getY() / 33;
            } else {
                clickx = e.getX() / 17;
                clicky = e.getY() / 17;
            }
            if (SQUARELOCKED && (clickx != lockx || clicky != locky)) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            MapData clicked = mapdata[clickx][clicky];
            //if (e.getModifiers()==MouseEvent.BUTTON3_MASK || e.getModifiers()==10) keyPressed(new KeyEvent(frame,KeyEvent.KEY_PRESSED,0,0,KeyEvent.VK_CONTROL));
            if (SwingUtilities.isMiddleMouseButton(e) || e.isControlDown()) {
                //System.out.println("edit click");
                keyPressed(new KeyEvent(frame, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED));
            }
            if (mapchangechar == 'I') {
                if (clicked.mapchar == '1' || clicked.mapchar == 'm' || clicked.mapchar == 'w' || clicked.mapchar == 'l' || clicked.mapchar == '!' || clicked.mapchar == 'G' || clicked.mapchar == '}' || clicked.mapchar == 'D' || clicked.mapchar == '/' || clicked.mapchar == '\\' || clicked.mapchar == 'y' || clicked.mapchar == 'P')
                    return; //can't put items in walls,mirrors,writing,etc.
                //pop up item wizard
                //Item tempitem = (new ItemWizard(frame)).getItem();
                itemwizard.show();
                Item tempitem = itemwizard.getItem();
                if (tempitem != null) {
                    if (clicked.mapchar == '[') {
                        int side;
                        int ch = 32;
                        if (ZOOMING) ch = 16;
                        if (e.getY() % (ch + 1) < ch / 4) side = 0;
                        else if (e.getY() % (ch + 1) > 3 * ch / 4) side = 2;
                        else if (e.getX() % (ch + 1) < ch / 2) side = 1;
                        else side = 3;
                        ((AlcoveData) clicked).addItem(tempitem, side);
                    } else {
                        int ch = 32;
                        if (ZOOMING) ch = 16;
                        if (e.getX() % (ch + 1) < ch / 2 && e.getY() % (ch + 1) < ch / 2) tempitem.subsquare = 0;
                        else if (e.getX() % (ch + 1) < ch / 2) tempitem.subsquare = 3;
                        else if (e.getY() % (ch + 1) < ch / 2) tempitem.subsquare = 1;
                        else tempitem.subsquare = 2;
                        clicked.addItem(tempitem);
                    }
                    //mappanel.repaint();
                    mappanel.paintSquare(clickx, clicky, true);
                    setStatusBar(clicked, clickx, clicky);
                } else return;
                //setStatusBar(clicked,clickx,clicky);
                NEEDSAVE = true;
                return;
            } else if (mapchangechar == 'M') {
                if (clicked.mapchar == '!' || clicked.mapchar == 'G') return;
                int subsquare = 5;
                if (!clicked.hasmonin[4]) {
                    int ch = 32;
                    if (ZOOMING) ch = 16;
                    if (e.getX() % (ch + 1) < ch / 2 && e.getY() % (ch + 1) < ch / 2) subsquare = 0;
                    else if (e.getX() % (ch + 1) < ch / 2) subsquare = 3;
                    else if (e.getY() % (ch + 1) < ch / 2) subsquare = 1;
                    else subsquare = 2;
                }
                //pop up monster wizard
                MonsterData tempdata, olddata = null;
                //if (subsquare!=5 && clicked.hasmonin[subsquare]) tempdata = (new MonsterWizard(frame,currentlevel,clickx,clicky,(MonsterData)monhash.get(currentlevel+","+clickx+","+clicky+","+subsquare) )).getData();
                //else if (clicked.hasmonin[4]) tempdata = (new MonsterWizard(frame,currentlevel,clickx,clicky,(MonsterData)monhash.get(currentlevel+","+clickx+","+clicky+","+5) )).getData();
                //else tempdata = (new MonsterWizard(frame,currentlevel,clickx,clicky)).getData();
                if (subsquare != 5 && clicked.hasmonin[subsquare])
                    olddata = (MonsterData) monhash.get(currentlevel + "," + clickx + "," + clicky + "," + subsquare);
                else if (clicked.hasmonin[4])
                    olddata = (MonsterData) monhash.get(currentlevel + "," + clickx + "," + clicky + "," + 5);
                monsterwizard.setMonster(olddata, currentlevel, clickx, clicky);
                tempdata = monsterwizard.getData();
                
                if (tempdata == null) return;
                else if (tempdata.subsquare == 5) {
                    //delete mons in corners (can't fit with one in center)
                    for (int i = 0; i < 4; i++) {
                        if (clicked.hasmonin[i]) {
                            clicked.hasmonin[i] = false;
                            monhash.remove(currentlevel + "," + clickx + "," + clicky + "," + i);
                        }
                    }
                    subsquare = 5;
                } else if (subsquare != 5) tempdata.subsquare = subsquare;
                else {
                    //in case changed a sub 5 mon into a smaller mon
                    clicked.hasmonin[4] = false;
                    monhash.remove(currentlevel + "," + clickx + "," + clicky + "," + 5);
                    tempdata.subsquare = 0;
                    subsquare = 0;
                }
                monhash.put(currentlevel + "," + clickx + "," + clicky + "," + subsquare, tempdata);
                clicked.hasMons = true;
                if (subsquare != 5) clicked.hasmonin[subsquare] = true;
                else clicked.hasmonin[4] = true;
                //mappanel.repaint();
                mappanel.paintSquare(clickx, clicky, true);
                setStatusBar(clicked, clickx, clicky);
                NEEDSAVE = true;
                return;
            } else if (mapchangechar == '3') { //no mons
                if (clicked.mapchar == '!' || clicked.mapchar == 'G') return;
                if (clicked instanceof WallData && clicked.mapchar != '2' && clicked.mapchar != '>')
                    return; //can't modify walls to pass mons
                if (MOUSEDOWN && MAKEFLOOR && !clicked.canPassMons) return;
                else if (MOUSEDOWN && !MAKEFLOOR && clicked.canPassMons) return;
                clicked.canPassMons = !clicked.canPassMons;
                if (!clicked.canPassMons) {
                    clicked.nomons = true;
                    MAKEFLOOR = true;
                } else {
                    clicked.nomons = false;
                    MAKEFLOOR = false;
                }
                //mappanel.repaint();
                mappanel.paintSquare(clickx, clicky, true);
                setStatusBar(clicked, clickx, clicky);
                MOUSEDOWN = true;
                NEEDSAVE = true;
                return;
            } else if (mapchangechar == '4') { //no ghosts
                if (clicked.mapchar == '!' || clicked.mapchar == 'G') return;
                if (MOUSEDOWN && MAKEFLOOR && !clicked.canPassImmaterial) return;
                else if (MOUSEDOWN && !MAKEFLOOR && clicked.canPassImmaterial) return;
                clicked.canPassImmaterial = !clicked.canPassImmaterial;
                if (!clicked.canPassImmaterial) {
                    clicked.noghosts = true;
                    MAKEFLOOR = true;
                } else {
                    clicked.noghosts = false;
                    MAKEFLOOR = false;
                }
                //mappanel.repaint();
                mappanel.paintSquare(clickx, clicky, true);
                setStatusBar(clicked, clickx, clicky);
                MOUSEDOWN = true;
                NEEDSAVE = true;
                return;
            } else if (SQUARELOCKED) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            boolean oldmons = clicked.hasMons;
            boolean[] oldhasmonin = new boolean[4];
            if (oldmons) {
                oldhasmonin[0] = clicked.hasmonin[0];
                oldhasmonin[1] = clicked.hasmonin[1];
                oldhasmonin[2] = clicked.hasmonin[2];
                oldhasmonin[3] = clicked.hasmonin[3];
            }
            boolean haditems = clicked.hasItems;
            int[] oldnumitemsin = new int[4];
            ArrayList oldmapitems = null;
            if (haditems) {
                oldmapitems = clicked.mapItems;
                if (clicked.mapchar == ']' || clicked.mapchar == 'a' || clicked.mapchar == 'f') {
                    for (int i = 0; i < oldmapitems.size(); i++) {
                        ((Item) oldmapitems.get(i)).subsquare = (((SidedWallData) clicked).side + 2) % 4;
                    }
                }
                oldnumitemsin[0] = clicked.numitemsin[0];
                oldnumitemsin[1] = clicked.numitemsin[1];
                oldnumitemsin[2] = clicked.numitemsin[2];
                oldnumitemsin[3] = clicked.numitemsin[3];
            } else if (clicked.mapchar == '[' && (clicked.numitemsin[0] > 0 || clicked.numitemsin[1] > 0 || clicked.numitemsin[2] > 0 || clicked.numitemsin[3] > 0)) {
                haditems = true;
                oldnumitemsin[0] = clicked.numitemsin[0];
                oldnumitemsin[1] = clicked.numitemsin[1];
                oldnumitemsin[2] = clicked.numitemsin[2];
                oldnumitemsin[3] = clicked.numitemsin[3];
                oldmapitems = new ArrayList(4);
                AlcoveData data = (AlcoveData) clicked;
                Item tempitem;
                int index;
                boolean done = false;
                while (!done) {
                    index = -1;
                    if (data.northside.size() > 0) index = 0;
                    else if (data.southside.size() > 0) index = 2;
                    else if (data.eastside.size() > 0) index = 3;
                    else if (data.westside.size() > 0) index = 1;
                    if (index != -1) {
                        tempitem = data.removeItem(0, index);
                        tempitem.subsquare = index;
                        oldmapitems.add(tempitem);
                    } else done = true;
                }
            }
            boolean hadparty = clicked.hasParty;
            int oldprojs = clicked.numProjs;
            boolean hadcloud = clicked.hasCloud;
            boolean oldnomons = clicked.nomons;
            boolean oldnoghosts = clicked.noghosts;
            boolean waschanging = false;
            boolean nowchanging = false;
            if (clicked.mapchar == 't' || (clicked.mapchar == 'l' && ((LauncherData) clicked).shootrate > 0) || (clicked.mapchar == 'g' && (((GeneratorData) clicked).genrate > 0 || ((GeneratorData) clicked).delaying)) || (clicked.mapchar == 'p' && ((PitData) clicked).isContinuous) || clicked.mapchar == 'y')
                waschanging = true;
            int oldside, tempside;
            MapData olddata = clicked;
            MapData tempdata = null;
            MapData[][] newdata;
            switch (mapchangechar) {
                case '1'://wall selected
                    if ((MOUSEDOWN && MAKEFLOOR) || (!MOUSEDOWN && clicked.mapchar == '1')) {
                        clicked = new FloorData();
                        MAKEFLOOR = true;
                    } else {
                        clicked = new WallData();
                        MAKEFLOOR = false;
                    }
                    MOUSEDOWN = true;
                    break;
                case 'd'://door
                    //tempdata = (new DoorWizard(frame,clicked,currentlevel,clickx,clicky)).getData();
                    doorwizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = doorwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case '2'://fakewall
                    if ((MOUSEDOWN && MAKEFLOOR) || (!MOUSEDOWN && clicked.mapchar == '2')) {
                        clicked = new FloorData();
                        MAKEFLOOR = true;
                    } else {
                        clicked = new FakeWallData();
                        MAKEFLOOR = false;
                    }
                    MOUSEDOWN = true;
                    break;
                case ']'://alcove
                case 'a'://altar
                    //pop up wizard dialog
                    //tempdata = (new AlcoveWizard(frame,clicked,currentlevel,clickx,clicky)).getData();
                    alcovewizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = alcovewizard.getData();
                    if (tempdata != null) clicked = tempdata;
                    break;
                case 'f'://fountain
                    //pop up "what side is it facing" dialog
                    //if (clicked.mapchar=='f') oldside = (((FountainData)clicked).side+2)%4;
                    //else oldside = 2;
                    //tempside = (new MoverDialog(frame,"Choose Fountain Side",oldside,false)).getNewLocation();
                    //if (tempside>=0) clicked = new FountainData((tempside+2)%4);
                    //else break;
                    //tempdata = clicked;//correct order of assignment, don't worry
                    fountainwizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = fountainwizard.getData();
                    if (tempdata != null) clicked = tempdata;
                    break;
                case 'm'://mirror
                    //tempdata = (new MirrorWizard(frame,clicked)).getData();
                    mirrorwizard.setData(clicked);
                    tempdata = mirrorwizard.getData();
                    if (tempdata != null) clicked = tempdata;
                    break;
                case 'w'://writing
                    //tempdata = (new WritingWizard(frame,clicked)).getData();
                    writingwizard.setData(clicked);
                    tempdata = writingwizard.getData();
                    if (tempdata != null) clicked = tempdata;
                    break;
                case 't'://teleport
                    //pop up teleport wizard
                    //tempdata = (new TeleportWizard(frame,clicked,currentlevel,clickx,clicky)).getData();
                    teleportwizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = teleportwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    nowchanging = true;
                    break;
                case 'l'://launcher
                    //pop up launcher wizard
                    //tempdata = (new LauncherWizard(frame,clicked,currentlevel,clickx,clicky)).getData();
                    launcherwizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = launcherwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    if (((LauncherData) tempdata).shootrate > 0) nowchanging = true;
                    break;
                case 'g'://generator
                    //pop up generator wizard
                    //tempdata = (new GeneratorWizard(frame,clicked,currentlevel,clickx,clicky)).getData();
                    generatorwizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = generatorwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    if (((GeneratorData) tempdata).genrate > 0) nowchanging = true;
                    break;
                case 'v'://stairs down
                    //add a new level if necessary, and put a stairs going up on it
                    newdata = null;
                    if (currentlevel == MAPLEVELS - 1) {
                        //pop up option to confirm addition of a new level
                        int returnval = JOptionPane.showConfirmDialog(frame, "This action requires an additional level be added.\nIs this ok?", "Add Level", JOptionPane.YES_NO_OPTION);
                        if (returnval == JOptionPane.YES_OPTION) {
                            MAPLEVELS++;
                            newdata = new MapData[MAPWIDTH][MAPHEIGHT];
                            for (int y = 0; y < MAPHEIGHT; y++) {
                                for (int x = 0; x < MAPWIDTH; x++) {
                                    newdata[x][y] = new WallData();
                                }
                            }
                            maplevels.add(newdata);
                            JMenuItem newlevelitem = new JMenuItem("Level " + (currentlevel + 1));
                            if ((currentlevel + 1) < 10)
                                newlevelitem.setMnemonic(Character.forDigit((currentlevel + 1), 10));
                            newlevelitem.addActionListener(menulisten);
                            levelmenu.add(newlevelitem);
                        } else break;
                        NEEDSAVE = true;
                    }
                    if (clicked.mapchar == '>') oldside = (((StairsData) clicked).side + 2) % 4;
                    else oldside = 2;
                    tempside = (new MoverDialog(frame, "Choose Stairs Direction", oldside, false)).getNewLocation();
                    if (tempside >= 0) clicked = new StairsData((tempside + 2) % 4, false);
                    else break;
                    if (ADDSTAIRS) {
                        newdata = (MapData[][]) maplevels.get(currentlevel + 1);
                        boolean stairsmons = false;
                        boolean stairsparty = newdata[clickx][clicky].hasParty;
                        boolean stairscloud = newdata[clickx][clicky].hasCloud;
                        boolean[] stairshadmonin = new boolean[4];
                        if (newdata[clickx][clicky].hasMons) {
                            stairsmons = true;
                            stairshadmonin[0] = newdata[clickx][clicky].hasmonin[0];
                            stairshadmonin[1] = newdata[clickx][clicky].hasmonin[1];
                            stairshadmonin[2] = newdata[clickx][clicky].hasmonin[2];
                            stairshadmonin[3] = newdata[clickx][clicky].hasmonin[3];
                        }
                        int stairsprojs = newdata[clickx][clicky].numProjs;
                        newdata[clickx][clicky] = new StairsData((((StairsData) clicked).side + 2) % 4, true);
                        if (stairsmons) {
                            newdata[clickx][clicky].hasMons = true;
                            newdata[clickx][clicky].hasmonin[0] = stairshadmonin[0];
                            newdata[clickx][clicky].hasmonin[1] = stairshadmonin[1];
                            newdata[clickx][clicky].hasmonin[2] = stairshadmonin[2];
                            newdata[clickx][clicky].hasmonin[3] = stairshadmonin[3];
                        }
                        if (stairsprojs > 0) {
                            ProjectileData tempproj;
                            int i = 0;
                            while (stairsprojs > 0) {
                                tempproj = (ProjectileData) dmprojs.get(i);
                                if (tempproj.level == currentlevel + 1 && tempproj.x == clickx && tempproj.y == clicky) {
                                    stairsprojs--;
                                    dmprojs.remove(i);
                                } else i++;
                            }
                        }
                        newdata[clickx][clicky].hasParty = stairsparty;
                        newdata[clickx][clicky].hasCloud = stairscloud;
                    }
                    tempdata = clicked;
                    break;
                case '^'://stairs up
                    if (clicked.mapchar == '>') oldside = (((StairsData) clicked).side + 2) % 4;
                    else oldside = 2;
                    tempside = (new MoverDialog(frame, "Choose Stairs Direction", oldside, false)).getNewLocation();
                    if (tempside >= 0) clicked = new StairsData((tempside + 2) % 4, true);
                    else break;
                    //put a stairs going down on level above
                    if (ADDSTAIRS) {
                                                /*
                                                newdata = (MapData[][])maplevels.get(currentlevel-1);
                                                boolean oldhadmons = false;
                                                if (newdata[clickx][clicky].hasMons) {
                                                        oldhadmons = true;
                                                }
                                                newdata[clickx][clicky] = new StairsData((((StairsData)clicked).side+2)%4,false);
                                                if (oldhadmons) {
                                                        newdata[clickx][clicky].hasMons = true;
                                                }
                                                */
                        newdata = (MapData[][]) maplevels.get(currentlevel - 1);
                        boolean stairsmons = false;
                        boolean stairsparty = newdata[clickx][clicky].hasParty;
                        boolean stairscloud = newdata[clickx][clicky].hasCloud;
                        boolean[] stairshadmonin = new boolean[4];
                        if (newdata[clickx][clicky].hasMons) {
                            stairsmons = true;
                            stairshadmonin[0] = newdata[clickx][clicky].hasmonin[0];
                            stairshadmonin[1] = newdata[clickx][clicky].hasmonin[1];
                            stairshadmonin[2] = newdata[clickx][clicky].hasmonin[2];
                            stairshadmonin[3] = newdata[clickx][clicky].hasmonin[3];
                        }
                        int stairsprojs = newdata[clickx][clicky].numProjs;
                        newdata[clickx][clicky] = new StairsData((((StairsData) clicked).side + 2) % 4, false);
                        if (stairsmons) {
                            newdata[clickx][clicky].hasMons = true;
                            newdata[clickx][clicky].hasmonin[0] = stairshadmonin[0];
                            newdata[clickx][clicky].hasmonin[1] = stairshadmonin[1];
                            newdata[clickx][clicky].hasmonin[2] = stairshadmonin[2];
                            newdata[clickx][clicky].hasmonin[3] = stairshadmonin[3];
                        }
                        if (stairsprojs > 0) {
                            ProjectileData tempproj;
                            int i = 0;
                            while (stairsprojs > 0) {
                                tempproj = (ProjectileData) dmprojs.get(i);
                                if (tempproj.level == currentlevel - 1 && tempproj.x == clickx && tempproj.y == clicky) {
                                    stairsprojs--;
                                    dmprojs.remove(i);
                                } else i++;
                            }
                        }
                        newdata[clickx][clicky].hasParty = stairsparty;
                        newdata[clickx][clicky].hasCloud = stairscloud;
                    }
                    tempdata = clicked;
                    break;
                case 'p'://pit
                    //add a new level if necessary
                    if (currentlevel == MAPLEVELS - 1) {
                        //pop up option to confirm addition of a new level
                        int returnval = JOptionPane.showConfirmDialog(frame, "This action requires an additional level be added.\nIs this ok?", "Add Level", JOptionPane.YES_NO_OPTION);
                        if (returnval == JOptionPane.YES_OPTION) {
                            MAPLEVELS++;
                            newdata = new MapData[MAPWIDTH][MAPHEIGHT];
                            for (int y = 0; y < MAPHEIGHT; y++) {
                                for (int x = 0; x < MAPWIDTH; x++) {
                                    newdata[x][y] = new WallData();
                                }
                            }
                            newdata[clickx][clicky] = new FloorData();
                            maplevels.add(newdata);
                            JMenuItem newlevelitem = new JMenuItem("Level " + (currentlevel + 1));
                            if ((currentlevel + 1) < 10)
                                newlevelitem.setMnemonic(Character.forDigit((currentlevel + 1), 10));
                            newlevelitem.addActionListener(menulisten);
                            levelmenu.add(newlevelitem);
                        } else break;
                        NEEDSAVE = true;
                    }
                    //tempdata = (new PitWizard(frame,clicked,currentlevel,clickx,clicky)).getData();
                    pitwizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = pitwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    if (((PitData) clicked).isContinuous) nowchanging = true;
                    break;
                case '/'://wallswitch
                    //pop up wallswitch wizard
                    tempdata = (new WallSwitchWizard(frame, clicked, currentlevel, clickx, clicky)).getData();
                    //wallswitchwizard.setData(clicked,currentlevel,clickx,clicky);
                    //tempdata = wallswitchwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case 's'://floorswitch
                    //pop up floorswitch wizard
                    tempdata = (new FloorSwitchWizard(frame, clicked, currentlevel, clickx, clicky)).getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case '\\'://multwallswitch
                    //pop up multwallswitch wizard
                    tempdata = (new MultWallSwitchWizard(frame, clicked, currentlevel, clickx, clicky)).getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case 'S'://multfloorswitch
                    //pop up multfloorswitch wizard
                    tempdata = (new MultFloorSwitchWizard(frame, clicked, currentlevel, clickx, clicky)).getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case '}'://sconce
                    //pop up sconce wizard
                    //tempdata = (new SconceWizard(frame,clicked,currentlevel,clickx,clicky)).getData();
                    sconcewizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = sconcewizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case 'D'://decoration
                    //pop up deco wizard
                    //tempdata = (new DecorationWizard(frame,clicked)).getData();
                    decorationwizard.setData(clicked);
                    tempdata = decorationwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case 'F'://floor decoration
                    //pop up floor deco wizard
                    //tempdata = (new FDecorationWizard(frame,clicked)).getData();
                    fdecorationwizard.setData(clicked, currentlevel, clickx, clicky);
                    tempdata = fdecorationwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case 'P'://pillar
                    //pop up pillar wizard
                    //tempdata = (new PillarWizard(frame,clicked,((clickx+clicky)%2==0))).getData();
                    pillarwizard.setData(clicked, (clickx + clicky) % 2 == 0);
                    tempdata = pillarwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case 'i'://invisible wall
                    if ((MOUSEDOWN && MAKEFLOOR) || (!MOUSEDOWN && clicked.mapchar == 'i')) {
                        clicked = new FloorData();
                        MAKEFLOOR = true;
                    } else {
                        clicked = new InvisibleWallData();
                        MAKEFLOOR = false;
                    }
                    MOUSEDOWN = true;
                    break;
                case 'E'://event
                    eventwizard.setData(clicked);
                    tempdata = eventwizard.getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case 'W'://gamewin
                    tempdata = (new GameWinWizard(frame, clicked)).getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    break;
                case '!'://stormbringer
                    clicked = new StormbringerData(false);
                    break;
                case 'G'://power gem
                    clicked = new PowerGemData(false);
                    break;
                case 'y'://fulya pit
                    //pop up fulya wizard
                    tempdata = (new FulYaWizard(frame, clicked, currentlevel, clickx, clicky)).getData();
                    if (tempdata == null) break;
                    clicked = tempdata;
                    nowchanging = true;
                    break;
            }
            if (tempdata == null && mapchangechar != '1' && mapchangechar != '2' && mapchangechar != 'i' && mapchangechar != '!' && mapchangechar != 'G') {
                setStatusBar(clicked, clickx, clicky);
                //if (clicked.mapchar=='t' || clicked.mapchar=='/' || clicked.mapchar=='s' || clicked.mapchar=='\\' || clicked.mapchar=='S'|| clicked.mapchar=='[' || clicked.mapchar==']' || clicked.mapchar=='a' || clicked.mapchar=='}' || clicked.mapchar=='y') doExit(); //turn off target highlight (for tels & switches)
                return;
            }
            if (undolist.size() > 40) {
                undolist.remove(0);
                undolist.remove(0);
            } //undo only stores 20 changes (2 objects for each)
            //stormbringer and power gem can't have mons or items
            if (oldmons && (clicked.mapchar == '!' || clicked.mapchar == 'G')) {
                for (int sub = 0; sub < 6; ) {
                    monhash.remove(currentlevel + "," + clicked + "," + sub);
                    if (sub == 3) sub = 5;
                    else sub++;
                }
                oldmons = false;
                olddata.hasMons = false;
                olddata.hasmonin[0] = false;
                olddata.hasmonin[1] = false;
                olddata.hasmonin[2] = false;
                olddata.hasmonin[3] = false;
                olddata.hasmonin[4] = false;
                clicked.hasmonin[0] = false;
                clicked.hasmonin[1] = false;
                clicked.hasmonin[2] = false;
                clicked.hasmonin[3] = false;
                clicked.hasmonin[4] = false;
            } else if (haditems && clicked.mapchar != '1' && clicked.mapchar != 'm' && clicked.mapchar != 'w' && clicked.mapchar != 'l' && clicked.mapchar != '}' && clicked.mapchar != 'D' && clicked.mapchar != '/' && clicked.mapchar != '\\' && clicked.mapchar != 'y' && clicked.mapchar != 'P') {
                Item tempitem;
                if (clicked.mapchar == '[') {
                    for (int i = 0; i < oldmapitems.size(); i++) {
                        tempitem = (Item) oldmapitems.get(i);
                        ((AlcoveData) clicked).addItem(tempitem, tempitem.subsquare);
                    }
                } else if (clicked.mapchar == ']' || clicked.mapchar == 'a' || clicked.mapchar == 'f') {
                    clicked.hasItems = true;
                    //clicked.mapItems = oldmapitems;
                    clicked.mapItems = new ArrayList(oldmapitems.size());
                    for (int i = 0; i < oldmapitems.size(); i++) {
                        tempitem = (Item) oldmapitems.get(i);
                        tempitem.subsquare = (((SidedWallData) clicked).side + 2) % 4;
                        clicked.mapItems.add(tempitem);
                        clicked.numitemsin[tempitem.subsquare]++;
                    }
                } else {
                    clicked.hasItems = true;
                    //clicked.mapItems = oldmapitems;
                    clicked.mapItems = new ArrayList(oldmapitems.size());
                    for (int i = 0; i < oldmapitems.size(); i++) clicked.mapItems.add(oldmapitems.get(i));
                    clicked.numitemsin[0] = oldnumitemsin[0];
                    clicked.numitemsin[1] = oldnumitemsin[1];
                    clicked.numitemsin[2] = oldnumitemsin[2];
                    clicked.numitemsin[3] = oldnumitemsin[3];
                }
            }
            if (oldprojs > 0) {
                ProjectileData tempproj;
                int i = 0;
                while (oldprojs > 0) {
                    tempproj = (ProjectileData) dmprojs.get(i);
                    if (tempproj.level == currentlevel && tempproj.x == clickx && tempproj.y == clicky) {
                        oldprojs--;
                        dmprojs.remove(i);
                    } else i++;
                }
            }
            clicked.hasCloud = hadcloud;//preserve clouds -> will be destroyed in game if on a wall or something (as will fluxcages, but they have no map flag)
            clicked.hasParty = hadparty;
            clicked.hasMons = oldmons;
            if (oldmons) {
                clicked.hasmonin[0] = oldhasmonin[0];
                clicked.hasmonin[1] = oldhasmonin[1];
                clicked.hasmonin[2] = oldhasmonin[2];
                clicked.hasmonin[3] = oldhasmonin[3];
            }
            if (oldnomons) {
                if ((!(clicked instanceof WallData) || clicked.mapchar == '2' || clicked.mapchar == '>')) {
                    clicked.canPassMons = false;
                    clicked.nomons = true;
                }
            }
            if (oldnoghosts) {
                clicked.canPassImmaterial = false;
                clicked.noghosts = true;
            }
            //clicked.numitemsin[0]=0;
            //clicked.numitemsin[1]=0;
            //clicked.numitemsin[2]=0;
            //clicked.numitemsin[3]=0;
            boolean needforce = doExit();//clear any targets/items it had
            doEnter(clicked, clickx, clicky);
            if (waschanging && !nowchanging) {
                MapPoint temppoint;
                boolean found = false;
                int index = 0;
                while (!found) {
                    temppoint = (MapPoint) mapstochange.get(index);
                    if (temppoint.level == currentlevel && temppoint.x == clickx && temppoint.y == clicky) found = true;
                    else index++;
                }
                mapstochange.remove(index);
                changingcount--;
                if (changingcount == 0) mapchanging = false;
            } else if (!waschanging && nowchanging) {
                mapstochange.add(new MapPoint(currentlevel, clickx, clicky));
                changingcount++;
                mapchanging = true;
            }
            mapdata[clickx][clicky] = clicked;
            if (!needforce) mappanel.paintSquare(clickx, clicky, true);
            else mappanel.repaint();
            undolist.add(olddata);
            undolist.add(new MapPoint(currentlevel, clickx, clicky));
            undoitem.setEnabled(true);
            undobutton.setEnabled(true);
            NEEDSAVE = true;
        }
        
    }
    
    class ItemListen implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            if (mapdata[lockx][locky].mapchar == '[') {
                String[] options = {"Edit", "Delete", "Change Side", "Cancel"};
                int choice = JOptionPane.showOptionDialog(frame, "What do you want to do to that item?", "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 3 || choice == JOptionPane.CLOSED_OPTION) return;
                AlcoveData data = (AlcoveData) mapdata[lockx][locky];
                int side;
                String sidestring = ((JButton) e.getSource()).getText();
                if (sidestring.equals("N")) side = 0;
                else if (sidestring.equals("W")) side = 1;
                else if (sidestring.equals("S")) side = 2;
                else side = 3;
                if (choice == 0) {
                    if (side == 0) editItem(data.northside, Integer.parseInt(e.getActionCommand()));
                    else if (side == 1) editItem(data.westside, Integer.parseInt(e.getActionCommand()));
                    else if (side == 2) editItem(data.southside, Integer.parseInt(e.getActionCommand()));
                    else editItem(data.eastside, Integer.parseInt(e.getActionCommand()));
                } else if (choice == 1) {
                    deleteItem(Integer.parseInt(e.getActionCommand()), side);
                } else {
                    int newside = (new MoverDialog(frame, side, false)).getNewLocation();
                    if (newside == side || newside == -1) return;
                    NEEDSAVE = true;
                    data.changeItemSide(Integer.parseInt(e.getActionCommand()), side, newside);
                    mappanel.repaint();
                    setStatusBar(mapdata[lockx][locky], lockx, locky);
                }
            } else if (mapdata[lockx][locky].mapchar == ']' || mapdata[lockx][locky].mapchar == 'a') {
                String[] options = {"Edit", "Delete", "Cancel"};
                int choice = JOptionPane.showOptionDialog(frame, "What do you want to do to that item?", "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 0) {
                    editItem(mapdata[lockx][locky].mapItems, Integer.parseInt(e.getActionCommand()));
                } else if (choice == 1) {
                    deleteItem(Integer.parseInt(e.getActionCommand()), 0);
                }
            } else {
                String[] options = {"Edit", "Delete", "Change Corner", "Cancel"};
                int choice = JOptionPane.showOptionDialog(frame, "What do you want to do to that item?", "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 0) {
                    editItem(mapdata[lockx][locky].mapItems, Integer.parseInt(e.getActionCommand()));
                } else if (choice == 1) {
                    deleteItem(Integer.parseInt(e.getActionCommand()), 0);
                } else if (choice == 2) {
                    
                    Item tempitem = (Item) mapdata[lockx][locky].mapItems.get(Integer.parseInt(e.getActionCommand()));
                    
                    int newcorner = (new MoverDialog(frame, tempitem.subsquare, true)).getNewLocation();
                    if (newcorner == tempitem.subsquare || newcorner == -1) return;
                    NEEDSAVE = true;
                    mapdata[lockx][locky].numitemsin[tempitem.subsquare]--;
                    mapdata[lockx][locky].numitemsin[newcorner]++;
                    tempitem.subsquare = newcorner;
                    mappanel.repaint();
                    if (newcorner == 0) ((JButton) e.getSource()).setText("NW");
                    else if (newcorner == 1) ((JButton) e.getSource()).setText("NE");
                    else if (newcorner == 2) ((JButton) e.getSource()).setText("SE");
                    else ((JButton) e.getSource()).setText("SW");
                }
            }
        }
        
        public void editItem(ArrayList itemvec, int index) {
            Item olditem = (Item) itemvec.get(index);
            //Item tempitem = (new ItemWizard(frame,olditem)).getItem();
            itemwizard.setTitle("Item Wizard - Edit Map Item");
            itemwizard.setItem(olditem);
            Item tempitem = itemwizard.getItem();
            if (tempitem != null) {
                int oldsubsquare = ((Item) itemvec.remove(index)).subsquare;
                tempitem.subsquare = oldsubsquare;
                itemvec.add(index, tempitem);
                setStatusBar(mapdata[lockx][locky], lockx, locky);
                NEEDSAVE = true;
            }
        }
        
        public void deleteItem(int index, int side) {
            if (mapdata[lockx][locky] instanceof AlcoveData) {
                ((AlcoveData) mapdata[lockx][locky]).removeItem(index, side);
            } else mapdata[lockx][locky].removeItem(index);
            mappanel.repaint();
            setStatusBar(mapdata[lockx][locky], lockx, locky);
            NEEDSAVE = true;
        }
        
    }
    
    class MenuListen implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("New")) {
                if (NEEDSAVE) {
                    //pop up warning window
                    int returnval = JOptionPane.showConfirmDialog(frame, "Dungeon Modified.\nSave it before creating new?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (returnval == JOptionPane.YES_OPTION) {
                        if (mapfile == null) saveAs();
                        else save();
                        if (NEEDSAVE) return;//in case saveAs cancelled
                    } else if (returnval != JOptionPane.NO_OPTION) return;
                }
                //pop up a thing asking for map dimensions
                Object[] dims = new Object[6];
                dims[0] = new JLabel("Width:");
                dims[1] = new JTextField("" + MAPWIDTH, 3);
                dims[2] = new JLabel("Height:");
                dims[3] = new JTextField("" + MAPHEIGHT, 3);
                dims[4] = "Ok";
                dims[5] = "Cancel";
                int returnval = JOptionPane.showOptionDialog(frame, "Enter New Map Dimensions.\nThese will affect every level of the dungeon.", "New Dungeon", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, dims, dims[1]);
                if (returnval == JOptionPane.CLOSED_OPTION || returnval == 5) return;
                mappanel.setVisible(false);
                MAPWIDTH = Integer.parseInt(((JTextField) dims[1]).getText());
                MAPHEIGHT = Integer.parseInt(((JTextField) dims[3]).getText());
                mappanel.setNewSize();
                newdungeon(true);
                mappanel.setVisible(true);
            } else if (e.getActionCommand().equals("Save")) {
                if (mapfile == null) {
                    saveAs();
                    repaint();
                } else save();
            } else if (e.getActionCommand().equals("Save As")) {
                saveAs();
                repaint();
            } else if (e.getActionCommand().equals("Load")) {
                if (NEEDSAVE) {
                    //pop up warning window
                    int returnval = JOptionPane.showConfirmDialog(frame, "Dungeon Modified.\nSave it before loading?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (returnval == JOptionPane.YES_OPTION) {
                        if (mapfile == null) saveAs();
                        else save();
                        if (NEEDSAVE) return;//in case saveAs cancelled
                    } else if (returnval != JOptionPane.NO_OPTION) return;
                }
                load(true);
                repaint();
            } else if (e.getActionCommand().equals("Exit")) {
                if (NEEDSAVE) {
                    //pop up warning window
                    int returnval = JOptionPane.showConfirmDialog(frame, "Dungeon Modified.\nSave it before quitting?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (returnval == JOptionPane.YES_OPTION) {
                        if (mapfile == null) saveAs();
                        else save();
                        if (NEEDSAVE) return;//in case saveAs cancelled
                    } else if (returnval != JOptionPane.NO_OPTION) return;
                }
                try {
                    //write out recent file list
                    if (!recent1.getText().equals("")) {
                        PrintWriter w = new PrintWriter(new FileWriter("recent.txt"));
                        w.println(recent1.getText());
                        if (!recent2.getText().equals("")) {
                            w.println(recent2.getText());
                            if (!recent3.getText().equals("")) w.println(recent3.getText());
                        }
                        w.flush();
                        w.close();
                    }
                    //save custom items
                    if (NEEDSAVEITEMS && ItemWizard.customitems.size() > 0) {
                        FileOutputStream out = new FileOutputStream(new File("custom_items.dat"));
                        ObjectOutputStream so = new ObjectOutputStream(out);
                        so.writeInt(ItemWizard.customitems.size());
                        for (int i = 0; i < ItemWizard.customitems.size(); i++) {
                            so.writeObject(ItemWizard.customitems.get(i));
                        }
                        so.flush();
                        out.close();
                        NEEDSAVEITEMS = false;
                    }
                    //save custom mons
                    if (NEEDSAVEMONS && MonsterWizard.custommons.size() > 0) {
                        MonsterData.NOITEMS = true;
                        FileOutputStream out = new FileOutputStream(new File("custom_mons.dat"));
                        ObjectOutputStream so = new ObjectOutputStream(out);
                        so.writeInt(MonsterWizard.custommons.size());
                        for (int i = 0; i < MonsterWizard.custommons.size(); i++) {
                            ((MonsterData) MonsterWizard.custommons.get(i)).save(so);
                        }
                        so.flush();
                        out.close();
                        MonsterData.NOITEMS = false;
                        NEEDSAVEMONS = false;
                    }
                } catch (Exception ex) {
                    MonsterData.NOITEMS = false;
                }
                dispose();
                System.exit(0);
            } else if (e.getActionCommand().equals("Set Party Info")) {
                //open dialog allowing change
                //PartyInfoDialog pid = new PartyInfoDialog((DMEditor)frame,partyedititem.isEnabled());
                //pid.show();
                int oldpartylevel = partylevel, oldpartyx = partyx, oldpartyy = partyy;
                partyinfo.show();
                if (oldpartylevel != partylevel || oldpartyx != partyx || oldpartyy != partyy) {
                    if (oldpartylevel == currentlevel) mappanel.paintSquare(oldpartyx, oldpartyy, true);
                    if (partylevel == currentlevel) mappanel.paintSquare(partyx, partyy, true);
                }
            } else if (e.getActionCommand().equals("Edit Party")) {
                heropanel.show();
                NEEDSAVE = true;
            } else if (e.getActionCommand().equals("Undo")) {
                if (undolist.size() > 0) {
                    if (SQUARELOCKED) {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }
                    MapPoint p = (MapPoint) undolist.remove(undolist.size() - 1);
                    MapData m = (MapData) undolist.remove(undolist.size() - 1);
                    m.numProjs = 0; //projs are permanently destroyed
                    MapData[][] tempdata;
                    if (currentlevel != p.level) {
                        tempdata = (MapData[][]) maplevels.get(p.level);
                    } else tempdata = mapdata;
                    //could put redo stuff here:
                    //redolist.add(tempdata[p.x][p.y]); redolist.add(p); redolist.setEnabled(true);
                    m.hasMons = tempdata[p.x][p.y].hasMons;
                    m.hasmonin[0] = tempdata[p.x][p.y].hasmonin[0];
                    m.hasmonin[1] = tempdata[p.x][p.y].hasmonin[1];
                    m.hasmonin[2] = tempdata[p.x][p.y].hasmonin[2];
                    m.hasmonin[3] = tempdata[p.x][p.y].hasmonin[3];
                    m.hasmonin[4] = tempdata[p.x][p.y].hasmonin[4];
                    boolean waschanging = false;
                    if (tempdata[p.x][p.y].mapchar == 't' || (tempdata[p.x][p.y].mapchar == 'l' && ((LauncherData) tempdata[p.x][p.y]).shootrate > 0) || (tempdata[p.x][p.y].mapchar == 'g' && (((GeneratorData) tempdata[p.x][p.y]).genrate > 0 || ((GeneratorData) tempdata[p.x][p.y]).delaying)) || (tempdata[p.x][p.y].mapchar == 'p' && ((PitData) tempdata[p.x][p.y]).isContinuous) || tempdata[p.x][p.y].mapchar == 'y')
                        waschanging = true;
                    tempdata[p.x][p.y] = m;
                    boolean nowchanging = false;
                    if (tempdata[p.x][p.y].mapchar == 't' || (tempdata[p.x][p.y].mapchar == 'l' && ((LauncherData) tempdata[p.x][p.y]).shootrate > 0) || (tempdata[p.x][p.y].mapchar == 'g' && (((GeneratorData) tempdata[p.x][p.y]).genrate > 0 || ((GeneratorData) tempdata[p.x][p.y]).delaying)) || (tempdata[p.x][p.y].mapchar == 'p' && ((PitData) tempdata[p.x][p.y]).isContinuous) || tempdata[p.x][p.y].mapchar == 'y')
                        nowchanging = true;
                    if (waschanging && !nowchanging) {
                        MapPoint temppoint;
                        boolean found = false;
                        int index = 0;
                        while (!found) {
                            temppoint = (MapPoint) mapstochange.get(index);
                            if (temppoint.level == p.level && temppoint.x == p.x && temppoint.y == p.y) found = true;
                            else index++;
                        }
                        mapstochange.remove(index);
                        changingcount--;
                        if (changingcount == 0) mapchanging = false;
                    } else if (!waschanging && nowchanging) {
                        mapstochange.add(new MapPoint(p.level, p.x, p.y));
                        changingcount++;
                        mapchanging = true;
                    }
                    if (undolist.size() == 0) {
                        undoitem.setEnabled(false);
                        undobutton.setEnabled(false);
                    }
                    mappanel.paintSquare(p.x, p.y, true);
                }
            } else if (e.getActionCommand().equals("recent1")) {
                if (NEEDSAVE) {
                    //pop up warning window
                    int returnval = JOptionPane.showConfirmDialog(frame, "Dungeon Modified.\nSave it before loading?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (returnval == JOptionPane.YES_OPTION) {
                        if (mapfile == null) saveAs();
                        else save();
                        if (NEEDSAVE) return;//in case saveAs cancelled
                    } else if (returnval != JOptionPane.NO_OPTION) return;
                }
                mapfile = new File(recent1.getText());
                dialog.setDirectory(mapfile.getParent());
                load(false);
            } else if (e.getActionCommand().equals("recent2")) {
                if (NEEDSAVE) {
                    //pop up warning window
                    int returnval = JOptionPane.showConfirmDialog(frame, "Dungeon Modified.\nSave it before loading?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (returnval == JOptionPane.YES_OPTION) {
                        if (mapfile == null) saveAs();
                        else save();
                        if (NEEDSAVE) return;//in case saveAs cancelled
                    } else if (returnval != JOptionPane.NO_OPTION) return;
                }
                mapfile = new File(recent2.getText());
                dialog.setDirectory(mapfile.getParent());
                load(false);
            } else if (e.getActionCommand().equals("recent3")) {
                if (NEEDSAVE) {
                    //pop up warning window
                    int returnval = JOptionPane.showConfirmDialog(frame, "Dungeon Modified.\nSave it before loading?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (returnval == JOptionPane.YES_OPTION) {
                        if (mapfile == null) saveAs();
                        else save();
                        if (NEEDSAVE) return;//in case saveAs cancelled
                    } else if (returnval != JOptionPane.NO_OPTION) return;
                }
                mapfile = new File(recent3.getText());
                dialog.setDirectory(mapfile.getParent());
                load(false);
            } else if (e.getActionCommand().equals("Find Items...")) {
                //itemfinder.updateCustomItems();
                itemfinder.show();
            } else if (e.getActionCommand().equals("Find Switches...")) {
                switchfinder.showFinder(SQUARELOCKED);
            } else if (e.getActionCommand().startsWith("Level")) {
                if (SQUARELOCKED) return;
                int newlevel = Integer.parseInt(e.getActionCommand().substring(e.getActionCommand().indexOf(' ') + 1));
                if (currentlevel == newlevel) return;
                mappanel.setVisible(false);
                currentlevel = newlevel;
                mapdata = (MapData[][]) maplevels.get(currentlevel);
                mappanel.repaint();
                mappanel.setVisible(true);
                if (currentlevel == 0) {
                    if (mbutton[8].isSelected()) mbutton[2].doClick();
                    mbutton[8].setEnabled(false);
                } else mbutton[8].setEnabled(true);
            } else if (e.getActionCommand().equals("Custom Items...")) {
                String olddir = dialog.getDirectory();
                dialog.setDirectory(".");
                dialog.setTitle("Import Items");
                dialog.setMode(FileDialog.LOAD);
                dialog.show();
                String returnVal = dialog.getFile();
                if (returnVal != null) {
                    try {
                        FileInputStream in = new FileInputStream(dialog.getDirectory() + returnVal);
                        ObjectInputStream si = new ObjectInputStream(in);
                        int numitems = si.readInt();
                        int numadjust = ItemWizard.customitems.size();
                        Item it;
                        for (int i = 0; i < numitems; i++) {
                            it = (Item) si.readObject();
                            it.number += numadjust;
                            if (ItemWizard.customitems.contains(it)) {
                                int duplicate = 0;
                                String basename = new String(it.name);
                                do {
                                    duplicate++;
                                    it.name = basename + duplicate;
                                }
                                while (ItemWizard.customitems.contains(it));
                            }
                            ItemWizard.customitems.add(it);
                        }
                        in.close();
                        itemwizard.updateCustomItems();
                        itemfinder.updateCustomItems();
                        JOptionPane.showMessageDialog(frame, "Items successfully imported.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        System.err.println("Error In Custom Item Import");
                        JOptionPane.showMessageDialog(frame, "Unable to import!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                dialog.setDirectory(olddir);
            } else if (e.getActionCommand().equals("Custom Monsters...")) {
                String olddir = dialog.getDirectory();
                dialog.setDirectory(".");
                dialog.setTitle("Import Monsters");
                dialog.setMode(FileDialog.LOAD);
                dialog.show();
                String returnVal = dialog.getFile();
                if (returnVal != null) {
                    try {
                        FileInputStream in = new FileInputStream(dialog.getDirectory() + returnVal);
                        ObjectInputStream si = new ObjectInputStream(in);
                        int nummons = si.readInt();
                        int numadjust = MonsterWizard.custommons.size();
                        MonsterData mon;
                        for (int i = 0; i < nummons; i++) {
                            mon = new MonsterData(si);
                            mon.number += numadjust;
                            if (MonsterWizard.custommons.contains(mon)) {
                                int duplicate = 0;
                                String basename = new String(mon.name);
                                do {
                                    duplicate++;
                                    mon.name = basename + duplicate;
                                }
                                while (MonsterWizard.custommons.contains(mon));
                            }
                            MonsterWizard.custommons.add(mon);
                        }
                        in.close();
                        monsterwizard.updateCustomMons();
                        JOptionPane.showMessageDialog(frame, "Monsters successfully imported.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        System.err.println("Error In Custom Monster Import");
                        JOptionPane.showMessageDialog(frame, "Unable to import!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                dialog.setDirectory(olddir);
            } else if (e.getActionCommand().equals("Auto Stairs")) {
                ADDSTAIRS = !ADDSTAIRS;
            } else if (e.getActionCommand().equals("Help")) {
                JOptionPane.showMessageDialog(frame, "Please see help.html", "Help", JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getActionCommand().equals("About")) {
                //open the about dialog box
                String[] message = {"Java Dungeon Master Editor", "by alandale", "\nCopyright 2000,2001\n"};
                JOptionPane.showMessageDialog(frame, message, "About", JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getActionCommand().equals("Set Level Pic Directories")) {
                if (leveldir.length != MAPLEVELS) {
                    String[] ld = new String[MAPLEVELS];
                    for (int l = 0; l < MAPLEVELS && l < leveldir.length; l++) {
                        ld[l] = leveldir[l];
                    }
                    leveldir = ld;
                }
                new LevelPicDialog(frame, leveldir);
            } else if (e.getActionCommand().equals("Set Level Darkness")) {
                if (leveldarkfactor.length != MAPLEVELS) {
                    int[] ld = new int[MAPLEVELS];
                    for (int l = 0; l < MAPLEVELS; l++) {
                        if (l < leveldarkfactor.length) ld[l] = leveldarkfactor[l];
                        else ld[l] = 15;
                    }
                    leveldarkfactor = ld;
                }
                new LevelDarkDialog(frame, leveldarkfactor);
            } else if (e.getActionCommand().equals("Resize Levels")) {
                if (SQUARELOCKED) return;
                //pop up a thing asking for map dimensions
                Object[] dims = new Object[6];
                dims[0] = new JLabel("Width:");
                dims[1] = new JTextField("" + MAPWIDTH, 3);
                dims[2] = new JLabel("Height:");
                dims[3] = new JTextField("" + MAPHEIGHT, 3);
                dims[4] = "Ok";
                dims[5] = "Cancel";
                int returnval = JOptionPane.showOptionDialog(frame, "Enter New Map Dimensions.\nThese will affect every level of the dungeon.", "New Dimensions", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, dims, dims[1]);
                if (returnval == JOptionPane.CLOSED_OPTION || returnval == 5) return;
                int newwidth = 1, newheight = 1;
                try {
                    newwidth = Integer.parseInt(((JTextField) dims[1]).getText());
                    newheight = Integer.parseInt(((JTextField) dims[3]).getText());
                } catch (NumberFormatException ex) {
                    return;
                }
                if (newwidth == MAPWIDTH && newheight == MAPHEIGHT || newwidth < 1 || newheight < 1) return;
                if (newwidth < MAPWIDTH || newheight < MAPHEIGHT) {
                    undolist.clear();
                    undoitem.setEnabled(false);
                    undobutton.setEnabled(false);
                }
                //resize panel and update every level
                mappanel.setVisible(false);
                
                MapData[][] newmapdata;
                for (int l = 0; l < MAPLEVELS; l++) {
                    mapdata = (MapData[][]) maplevels.get(l);
                    if (l == 0 && (newwidth < partyx || newheight < partyy)) {
                        partylevel = 0;
                        partyx = 1;
                        partyy = 1;
                        mapdata[1][1].hasParty = true;
                    }
                    newmapdata = new MapData[newwidth][newheight];
                    for (int y = 0; y < newheight; y++) {
                        for (int x = 0; x < newwidth; x++) {
                            if (y < MAPHEIGHT && x < MAPWIDTH) newmapdata[x][y] = mapdata[x][y];
                            else newmapdata[x][y] = new WallData();
                        }
                    }
                    maplevels.set(l, newmapdata);
                }
                MAPWIDTH = newwidth;
                MAPHEIGHT = newheight;
                mapdata = (MapData[][]) maplevels.get(currentlevel);
                mappanel.setNewSize();
                mappanel.repaint();
                mappanel.setVisible(true);
            } else if (e.getActionCommand().equals("Delete Current Level")) {
                if (SQUARELOCKED || MAPLEVELS == 1) return;
                //open confirmation dialog
                int returnval = JOptionPane.showConfirmDialog(frame, "Current level will be deleted.\nTargets and stairs could become invalid.\nIs this correct?", "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (returnval == JOptionPane.YES_OPTION) {
                    mappanel.setVisible(false);
                    //fix targets and mons
                    MapData[][] tempdata = null;
                    MonsterData tempmon;
                    for (int l = currentlevel; l < MAPLEVELS; l++) {
                        tempdata = (MapData[][]) maplevels.get(l);
                        for (int y = 0; y < MAPHEIGHT; y++) {
                            for (int x = 0; x < MAPWIDTH; x++) {
                                if (tempdata[x][y].hasMons) {
                                    for (int sub = 0; sub < 6; sub++) {
                                        tempmon = (MonsterData) monhash.remove(l + "," + x + "," + y + "," + sub);
                                        if (tempmon != null && l != currentlevel) {
                                            tempmon.level--;
                                            monhash.put(tempmon.level + "," + x + "," + y + "," + sub, tempmon);
                                        }
                                        if (sub == 3) sub++;
                                    }
                                }
                                if (l > currentlevel) tempdata[x][y].changeLevel(-1, currentlevel);
                            }
                        }
                    }
                    maplevels.remove(currentlevel);
                    if (currentlevel == MAPLEVELS - 1) currentlevel--;
                    MAPLEVELS--;
                    mapdata = (MapData[][]) maplevels.get(currentlevel);
                    if (partylevel > currentlevel) {
                        partylevel--;
                        tempdata = (MapData[][]) maplevels.get(partylevel);
                        tempdata[partyx][partyy].hasParty = true;
                    } else if (partylevel == currentlevel) mapdata[partyx][partyy].hasParty = true;
                    levelmenu.removeAll();
                    levelmenuitem = new JMenuItem[MAPLEVELS];
                    for (int l = 0; l < MAPLEVELS; l++) {
                        levelmenuitem[l] = new JMenuItem("Level " + l, Character.forDigit(l, 10));
                        levelmenuitem[l].addActionListener(menulisten);
                        levelmenu.add(levelmenuitem[l]);
                    }
                    mappanel.repaint();
                    mappanel.setVisible(true);
                    if (currentlevel == 0) {
                        if (mbutton[8].isSelected()) mbutton[2].doClick();
                        mbutton[8].setEnabled(false);
                    }
                    undolist.clear();
                    undoitem.setEnabled(false);
                    undobutton.setEnabled(false);
                }
            } else if (e.getActionCommand().equals("Insert Level Before Current")) {
                if (SQUARELOCKED) return;
                //open confirmation dialog
                int returnval = JOptionPane.showConfirmDialog(frame, "Targets and stairs could become invalid.\nReally insert level?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (returnval == JOptionPane.YES_OPTION) {
                    mappanel.setVisible(false);
                    MAPLEVELS++;
                    if (partylevel >= currentlevel) partylevel++;
                    mapdata = new MapData[MAPWIDTH][MAPHEIGHT];
                    for (int y = 0; y < MAPHEIGHT; y++) {
                        for (int x = 0; x < MAPWIDTH; x++) {
                            mapdata[x][y] = new WallData();
                        }
                    }
                    maplevels.add(currentlevel, mapdata);
                    mappanel.repaint();
                    mappanel.setVisible(true);
                    //fix targets and mons
                    MapData[][] tempdata = null;
                    MonsterData tempmon;
                    for (int l = currentlevel + 1; l < MAPLEVELS; l++) {
                        tempdata = (MapData[][]) maplevels.get(l);
                        for (int y = 0; y < MAPHEIGHT; y++) {
                            for (int x = 0; x < MAPWIDTH; x++) {
                                if (tempdata[x][y].hasMons) {
                                    for (int sub = 0; sub < 6; sub++) {
                                        tempmon = (MonsterData) monhash.remove((l - 1) + "," + x + "," + y + "," + sub);
                                        if (tempmon != null) {
                                            tempmon.level++;
                                            monhash.put(tempmon.level + "," + x + "," + y + "," + sub, tempmon);
                                        }
                                        if (sub == 3) sub++;
                                    }
                                }
                                tempdata[x][y].changeLevel(1, currentlevel);
                            }
                        }
                    }
                    JMenuItem newlevelitem = new JMenuItem("Level " + (MAPLEVELS - 1));
                    if ((MAPLEVELS - 1) < 10) newlevelitem.setMnemonic(Character.forDigit((MAPLEVELS - 1), 10));
                    newlevelitem.addActionListener(menulisten);
                    levelmenu.add(newlevelitem);
                    undolist.clear();
                    undoitem.setEnabled(false);
                    undobutton.setEnabled(false);
                }
            }
        }
        
    }
}
