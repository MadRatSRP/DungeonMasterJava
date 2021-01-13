import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.util.ArrayList;

class WallSwitchWizard extends JDialog implements ActionListener, MouseListener {
    private MapData data, changeto;
    private int level, x, y, side = 0, type = 0, picnum = 0, changetype = 0;
    private int actiontype = 0;
    private Item key, coin;
    private JList itemlist;
    private int itemindex = -1;
    private ArrayList mapitems = new ArrayList(4);
    private Vector itemnames = new Vector(4);
    private JPanel typepanel, buttonpanel, buttonpics, keypanel, coinpanel, newsquarepan, soundpan, itempanel, picklockpan, bottom, stoppanel;
    private JLabel keylabel, coinlabel, newsquare;
    private JTextField coincount, picklock, reset, delay, targetlevel, targetx, targety, soundstring;
    private JToggleButton togbut, actbut, deactbut, actdeactbut, deactactbut, swapbut, settobut, soundbut, nosoundbut, abrupt, buttonbut, keybut, coinbut, isReusable, consumebutton1, consumebutton2, consumebutton3, additembut, nomonsbut, noghostsbut, stopswitch, retainitems, resetnotrigger;
    private JComboBox loopsound;
    private FileDialog dialog;
    private JFrame frame;
    public static final String[] corners = {"NW", "NE", "SE", "SW"};
    public static final String[] sides = {"N", "W", "S", "E"};
    private boolean multiple;
    
    public WallSwitchWizard(JFrame f, MapData data, int level, int x, int y) {
        this(f, data, level, x, y, false, -1, 0, 0);
    }
    
    public WallSwitchWizard(JFrame f, MapData data, int level, int x, int y, boolean multiple, int alcove, int side, int type) {
        super(f, "Wall Switch Wizard", true);
        frame = f;
        this.level = level;
        this.x = x;
        this.y = y;
        this.multiple = multiple;
        this.side = side;
        this.type = type;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 532);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //type panel
        typepanel = new JPanel();
        ButtonGroup typegrp = new ButtonGroup();
        buttonbut = new JToggleButton("Button");
        keybut = new JToggleButton("Key");
        coinbut = new JToggleButton("Coin");
        buttonbut.addActionListener(this);
        keybut.addActionListener(this);
        coinbut.addActionListener(this);
        typegrp.add(buttonbut);
        typegrp.add(keybut);
        typegrp.add(coinbut);
        typepanel.add(buttonbut);
        typepanel.add(keybut);
        typepanel.add(coinbut);
        buttonbut.setSelected(true);
        
        //button panel
        buttonpanel = new JPanel();
        buttonpics = new JPanel();
        buttonpics.setLayout(new GridLayout(11, 1));
        ButtonGroup butgrp = new ButtonGroup();
        JToggleButton[] buttons = new JToggleButton[11];
        buttons[0] = new JToggleButton("Stone Button");
        buttons[1] = new JToggleButton("Lever");
        buttons[2] = new JToggleButton("Small Button");
        buttons[3] = new JToggleButton("Green Gem");
        buttons[4] = new JToggleButton("Blue Gem");
        buttons[5] = new JToggleButton("Red Gem");
        buttons[6] = new JToggleButton("Wall Ring");
        buttons[7] = new JToggleButton("Wall Crack");
        buttons[8] = new JToggleButton("Chaos Face");
        buttons[9] = new JToggleButton("Eye");
        buttons[10] = new JToggleButton("Demon Face");
        for (int i = 0; i < 11; i++) {
            buttons[i].setActionCommand("" + i);
            buttons[i].addActionListener(this);
            butgrp.add(buttons[i]);
            buttonpics.add(buttons[i]);
        }
        buttons[0].setSelected(true);
        JScrollPane buttonpane = new JScrollPane(buttonpics);
        buttonpane.setPreferredSize(new Dimension(200, 260));
        buttonpanel.add(buttonpane);
        consumebutton3 = new JToggleButton("Item Disappears");
        //if (alcove) buttonpanel.add(consumebutton3);
        
        //key panel
        keypanel = new JPanel();
        keypanel.setLayout(new BoxLayout(keypanel, BoxLayout.Y_AXIS));
        key = new Item(31);
        JPanel keypan = new JPanel();
        keylabel = new JLabel("Iron Key");
        JButton keybutton = new JButton("Change");
        keybutton.addActionListener(this);
        keypan.add(keylabel);
        keypan.add(keybutton);
        JPanel keypan2 = new JPanel();
        consumebutton1 = new JToggleButton("Key Disappears");
        keypan2.add(consumebutton1);
        consumebutton1.setSelected(true);
        JPanel keypics = new JPanel();
        keypics.setLayout(new GridLayout(23, 1));
        ButtonGroup keygrp = new ButtonGroup();
        JToggleButton[] keys = new JToggleButton[23];
        keys[0] = new JToggleButton("Iron Keyhole");
        keys[1] = new JToggleButton("Brass Keyhole");
        keys[2] = new JToggleButton("Gold Keyhole");
        keys[3] = new JToggleButton("Emerald Keyhole");
        keys[4] = new JToggleButton("Ruby Keyhole");
        keys[5] = new JToggleButton("Ruby (Alt) Keyhole");
        keys[6] = new JToggleButton("Onyx Keyhole");
        keys[7] = new JToggleButton("Tourquoise Keyhole");
        keys[8] = new JToggleButton("Winged Keyhole");
        keys[9] = new JToggleButton("Master Keyhole");
        keys[10] = new JToggleButton("Ra Keyhole");
        keys[11] = new JToggleButton("Skeleton Keyhole");
        keys[12] = new JToggleButton("Gem Hole");
        keys[13] = new JToggleButton("Eye");
        keys[14] = new JToggleButton("Wall Drain");
        keys[15] = new JToggleButton("Wall Crack");
        keys[16] = new JToggleButton("Block Keyhole");
        keys[17] = new JToggleButton("Cross Keyhole");
        keys[18] = new JToggleButton("Double Keyhole");
        keys[19] = new JToggleButton("Iron (Alt) Keyhole");
        keys[20] = new JToggleButton("Solid Keyhole");
        keys[21] = new JToggleButton("Square Keyhole");
        keys[22] = new JToggleButton("Topaz Keyhole");
        for (int i = 0; i < 23; i++) {
            keys[i].setActionCommand("" + i);
            keys[i].addActionListener(this);
            keygrp.add(keys[i]);
            keypics.add(keys[i]);
        }
        keys[0].setSelected(true);
        JScrollPane keypane = new JScrollPane(keypics);
        //keypane.setPreferredSize(new Dimension(200,165));
        keypane.setPreferredSize(new Dimension(200, 191));
        keypanel.add(keypan);
        keypanel.add(keypan2);
        keypanel.add(keypane);
        keypanel.setVisible(false);
        
        //picklock panel
        picklockpan = new JPanel();
        picklock = new JTextField("0", 4);
        picklockpan.add(new JLabel("Pick Lock:"));
        picklockpan.add(picklock);
        picklockpan.setVisible(false);
        
        //coin panel
        coinpanel = new JPanel();
        coinpanel.setLayout(new BoxLayout(coinpanel, BoxLayout.Y_AXIS));
        coin = new Item(51);
        JPanel cpan1 = new JPanel();
        coinlabel = new JLabel("Gold Coin");
        JButton coinbutton = new JButton("Change");
        coinbutton.addActionListener(this);
        cpan1.add(coinlabel);
        cpan1.add(coinbutton);
        JPanel cpan2 = new JPanel();
        coincount = new JTextField("1", 3);
        cpan2.add(new JLabel("Coins Needed:"));
        cpan2.add(coincount);
        coinpanel.add(cpan1);
        coinpanel.add(cpan2);
        consumebutton2 = new JToggleButton("Coin Disappears");
        coinpanel.add(consumebutton2);
        consumebutton2.setSelected(true);
        coinpanel.setVisible(false);
        
        if (alcove >= 0) {
            buttonpanel.add(consumebutton3);
            consumebutton1.setSelected(false);
            consumebutton2.setSelected(false);
        }
        
        //switch type (activates or swaps)
        JPanel stypechoose1 = new JPanel();
        JPanel stypechoose2 = new JPanel();
        ButtonGroup stypegrp = new ButtonGroup();
        togbut = new JToggleButton("Toggles");
        actbut = new JToggleButton("Activates");
        deactbut = new JToggleButton("Deactivates");
        actdeactbut = new JToggleButton("Act/Deact");
        deactactbut = new JToggleButton("Deact/Act");
        swapbut = new JToggleButton("Exchanges");
        settobut = new JToggleButton("Set To");
        soundbut = new JToggleButton("Play Sound");
        nosoundbut = new JToggleButton("Stop Sounds");
        togbut.addActionListener(this);
        actbut.addActionListener(this);
        deactbut.addActionListener(this);
        actdeactbut.addActionListener(this);
        deactactbut.addActionListener(this);
        swapbut.addActionListener(this);
        settobut.addActionListener(this);
        soundbut.addActionListener(this);
        nosoundbut.addActionListener(this);
        stypegrp.add(togbut);
        stypegrp.add(actbut);
        stypegrp.add(deactbut);
        stypegrp.add(actdeactbut);
        stypegrp.add(deactactbut);
        stypegrp.add(swapbut);
        stypegrp.add(settobut);
        stypegrp.add(soundbut);
        stypegrp.add(nosoundbut);
        stypechoose1.add(togbut);
        stypechoose1.add(actbut);
        stypechoose1.add(deactbut);
        stypechoose1.add(actdeactbut);
        stypechoose2.add(deactactbut);
        stypechoose2.add(swapbut);
        stypechoose2.add(settobut);
        stypechoose2.add(soundbut);
        stypechoose2.add(nosoundbut);
        togbut.setSelected(true);
        newsquarepan = new JPanel();
        changeto = new FloorData();
        newsquare = new JLabel("Floor");
        newsquare.setPreferredSize(new Dimension(180, 15));
        //Font fnt = new Font("Times Roman",Font.BOLD,9);
        JButton squarebut = new JButton("Change");
        Font fnt = squarebut.getFont().deriveFont(9.0f);
        nomonsbut = new JToggleButton("No Mons");
        noghostsbut = new JToggleButton("No Ghosts");
        retainitems = new JToggleButton("Retain Items");
        additembut = new JToggleButton("Items...");
        squarebut.setFont(fnt);
        nomonsbut.setFont(fnt);
        noghostsbut.setFont(fnt);
        retainitems.setFont(fnt);
        additembut.setFont(fnt);
        squarebut.setActionCommand("Change Square");
        squarebut.addActionListener(this);
        squarebut.setMargin(new Insets(0, 5, 0, 5));
        nomonsbut.addActionListener(this);
        nomonsbut.setMargin(new Insets(0, 5, 0, 5));
        noghostsbut.addActionListener(this);
        noghostsbut.setMargin(new Insets(0, 5, 0, 5));
        retainitems.setMargin(new Insets(0, 5, 0, 5));
        additembut.addActionListener(this);
        additembut.setMargin(new Insets(0, 5, 0, 5));
        newsquarepan.add(new JLabel("Replace With: "));
        newsquarepan.add(newsquare);
        newsquarepan.add(squarebut);
        newsquarepan.add(nomonsbut);
        newsquarepan.add(noghostsbut);
        newsquarepan.add(retainitems);
        newsquarepan.add(additembut);
        newsquarepan.setVisible(false);
        soundpan = new JPanel();
        soundstring = new JTextField("switch.wav", 20);
        String[] looparray = {"No Loop", "Loop - Delay", "Loop - No Delay"};
        loopsound = new JComboBox(looparray);
        soundpan.add(new JLabel("Sound To Play:"));
        soundpan.add(soundstring);
        JButton browsebut = new JButton("Browse");
        browsebut.addActionListener(this);
        soundpan.add(browsebut);
        //soundpan.add(new JLabel("Looping:"));
        soundpan.add(loopsound);
        soundpan.setVisible(false);
        //file dialog for browsing sounds
        dialog = new FileDialog(f, "Choose A Sound", FileDialog.LOAD);
        dialog.setDirectory("Sounds");
        abrupt = new JToggleButton("Abrupt Cut-Off");
        abrupt.setVisible(false);
        
        //item panel
        itempanel = new JPanel();
        itemlist = new JList();
        itemlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemlist.setVisibleRowCount(6);
        itemlist.addMouseListener(this);
        Box itembox = Box.createVerticalBox();
        JScrollPane itempane = new JScrollPane(itemlist);
        itempane.setPreferredSize(new Dimension(180, 120));
        itembox.add(Box.createVerticalStrut(20));
        itembox.add(new JLabel("New Square's Items:"));
        itembox.add(itempane);
        Box itembutbox = Box.createVerticalBox();
        JButton addbut = new JButton("Add/Edit Item");
        JButton removebut = new JButton("Remove Item");
        JButton sidecornbut = new JButton("Change Side/Corner");
        addbut.setFont(fnt);
        removebut.setFont(fnt);
        sidecornbut.setFont(fnt);
        addbut.setMargin(new Insets(0, 5, 0, 5));
        removebut.setMargin(new Insets(0, 5, 0, 5));
        sidecornbut.setMargin(new Insets(0, 5, 0, 5));
        addbut.addActionListener(this);
        removebut.addActionListener(this);
        sidecornbut.addActionListener(this);
        itembutbox.add(Box.createVerticalStrut(20));
        itembutbox.add(addbut);
        itembutbox.add(removebut);
        itembutbox.add(sidecornbut);
        itempanel.add(itembox);
        itempanel.add(itembutbox);
        itempanel.setVisible(false);
        
        //stopswitch panel
        JPanel stoppanel = new JPanel();
        stopswitch = new JToggleButton("Stops Multiple Switch Iteration");
        stoppanel.add(stopswitch);
        if (!multiple) stoppanel.setVisible(false);
        
        //center panel
        JPanel centerpanel = new JPanel();
        centerpanel.add(buttonpanel);
        centerpanel.add(keypanel);
        centerpanel.add(coinpanel);
        centerpanel.add(itempanel);
        centerpanel.add(picklockpan);
        centerpanel.add(stoppanel);
        
        //side chooser
        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new GridLayout(2, 2));
        sidepanel.setPreferredSize(new Dimension(140, 50));
        //sidepanel.setMaximumSize(new Dimension(140,50));
        ButtonGroup sidegrp = new ButtonGroup();
        JToggleButton[] sidebutton = new JToggleButton[4];
        sidebutton[0] = new JToggleButton("North");
        sidebutton[1] = new JToggleButton("West");
        sidebutton[2] = new JToggleButton("South");
        sidebutton[3] = new JToggleButton("East");
        sidebutton[0].addActionListener(this);
        sidebutton[1].addActionListener(this);
        sidebutton[2].addActionListener(this);
        sidebutton[3].addActionListener(this);
        sidegrp.add(sidebutton[0]);
        sidegrp.add(sidebutton[1]);
        sidegrp.add(sidebutton[2]);
        sidegrp.add(sidebutton[3]);
        sidepanel.add(sidebutton[0]);
        sidepanel.add(sidebutton[1]);
        sidepanel.add(sidebutton[2]);
        sidepanel.add(sidebutton[3]);
        sidebutton[(side + 2) % 4].setSelected(true);
        JPanel sidelabelpanel = new JPanel();
        JLabel sidelabel = new JLabel("Switch Faces:");
        sidelabel.setHorizontalAlignment(JLabel.CENTER);
        sidelabelpanel.add(sidelabel);
        
        //target
        Box tlbox = Box.createVerticalBox();
        Box tfbox = Box.createVerticalBox();
        JLabel levellabel = new JLabel(" Level ");
        JLabel xlabel = new JLabel("    X");
        JLabel ylabel = new JLabel("    Y");
        xlabel.setHorizontalAlignment(JLabel.CENTER);
        ylabel.setHorizontalAlignment(JLabel.CENTER);
        targetlevel = new JTextField("" + level, 3);
        targetx = new JTextField("" + x, 3);
        targety = new JTextField("" + y, 3);
        tlbox.add(levellabel);
        tlbox.add(Box.createVerticalStrut(14));
        tlbox.add(xlabel);
        tlbox.add(Box.createVerticalStrut(15));
        tlbox.add(ylabel);
        tfbox.add(targetlevel);
        tfbox.add(Box.createVerticalStrut(10));
        tfbox.add(targetx);
        tfbox.add(Box.createVerticalStrut(10));
        tfbox.add(targety);
        Box targetbox = Box.createHorizontalBox();
        targetbox.add(tlbox);
        targetbox.add(Box.createHorizontalStrut(5));
        targetbox.add(tfbox);
        JButton targetbut = new JButton("From Map...");
        targetbut.addActionListener(this);
        JPanel targetbox2 = new JPanel();
        targetbox2.setPreferredSize(new Dimension(150, 150));
        targetbox2.setMaximumSize(new Dimension(150, 150));
        targetbox2.setLayout(new BoxLayout(targetbox2, BoxLayout.Y_AXIS));
        targetbox2.add(targetbox);
        targetbox2.add(Box.createVerticalStrut(10));
        targetbox2.add(targetbut);
        targetbox2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Target"));
        
        //east panel
        JPanel reusepan = new JPanel();
        isReusable = new JToggleButton("Reusable");
        reusepan.add(isReusable);
        isReusable.setSelected(true);
        Box east = Box.createVerticalBox();
        //east.add(Box.createVerticalGlue());
        //east.add(Box.createVerticalStrut(20));
        east.add(sidelabelpanel);
        east.add(sidepanel);
        east.add(Box.createVerticalStrut(10));
        //east.add(targetbox);
        east.add(targetbox2);
        east.add(Box.createVerticalStrut(10));
        east.add(reusepan);
        east.add(Box.createVerticalStrut(40));
        east.add(Box.createVerticalGlue());
        
        //bottom panel
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(stypechoose1);
        bottom.add(stypechoose2);
        bottom.add(newsquarepan);
        bottom.add(soundpan);
        bottom.add(abrupt);
        JPanel resetdelay = new JPanel();
        reset = new JTextField("0", 4);
        delay = new JTextField("0", 4);
        resetnotrigger = new JToggleButton("No Trigger on Reset");
        resetdelay.add(new JLabel("Reset Count (0 for none):"));
        resetdelay.add(reset);
        resetdelay.add(new JLabel("Delay Count (0 for none):"));
        resetdelay.add(delay);
        resetdelay.add(resetnotrigger);
        bottom.add(resetdelay);
        
        //done/cancel
        JPanel dcpanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        dcpanel.add(done);
        dcpanel.add(cancel);
        bottom.add(dcpanel);
        
        if (data != null && data.mapchar == '/') {
            WallSwitchData ws = (WallSwitchData) data;
            consumebutton1.setSelected(ws.consumeskey);
            consumebutton2.setSelected(ws.consumeskey);
            stopswitch.setSelected(ws.stopswitch);
            if (ws.type == 0) {
                int selectednum = ws.picnumber;
                buttons[ws.picnumber].doClick();
                JViewport viewport = buttonpane.getViewport();
                viewport.setViewPosition(new Point(0, selectednum * 27));
            } else if (ws.type == 1) {
                //key = new Item(ws.keynumber);
                //potion
                if (ws.keynumber > 9 && ws.keynumber < 31) {
                    key = new Item(ws.keynumber, 1, 1);
                }
                //chest
                else if (ws.keynumber == 5) {
                    key = new Chest();
                }
                //scroll
                else if (ws.keynumber == 4) {
                    String[] mess = new String[5];
                    key = new Item(mess);
                }
                //torch
                else if (ws.keynumber == 9) {
                    key = new Torch();
                }
                //waterskin
                else if (ws.keynumber == 73) {
                    key = new Waterskin();
                }
                //compass
                else if (ws.keynumber == 8) {
                    key = new Compass();
                }
                //everything else
                else if (ws.keynumber < 300) key = new Item(ws.keynumber);
                else key = null;
                if (key != null) keylabel.setText(key.name);
                else keylabel.setText("Custom Item #" + (ws.keynumber - 299));
                keybut.doClick();
                keys[ws.picnumber].doClick();
                JViewport viewport = keypane.getViewport();
                viewport.setViewPosition(new Point(0, ws.picnumber * 27));
            } else {
                //coin = new Item(ws.keynumber);
                //potion
                if (ws.keynumber > 9 && ws.keynumber < 31) {
                    coin = new Item(ws.keynumber, 1, 1);
                }
                //chest
                else if (ws.keynumber == 5) {
                    coin = new Chest();
                }
                //scroll
                else if (ws.keynumber == 4) {
                    String[] mess = new String[5];
                    coin = new Item(mess);
                }
                //torch
                else if (ws.keynumber == 9) {
                    coin = new Torch();
                }
                //waterskin
                else if (ws.keynumber == 73) {
                    coin = new Waterskin();
                }
                //compass
                else if (ws.keynumber == 8) {
                    coin = new Compass();
                }
                //everything else
                else coin = new Item(ws.keynumber);
                coinlabel.setText(coin.name);
                coincount.setText("" + ws.keysneeded);
                coinbut.doClick();
            }
            picklock.setText("" + ws.picklock);
            sidebutton[(ws.side + 2) % 4].doClick();
            targetlevel.setText("" + ws.targetlevel);
            targetx.setText("" + ws.targetx);
            targety.setText("" + ws.targety);
            if (!ws.isReusable) isReusable.setSelected(false);
            if (ws.actiontype == 1) actbut.doClick();
            else if (ws.actiontype == 2) deactbut.doClick();
            else if (ws.actiontype == 3) actdeactbut.doClick();
            else if (ws.actiontype == 4) deactactbut.doClick();
            else if (ws.actiontype == 7) {
                soundbut.doClick();
                soundstring.setText(ws.soundstring);
                if (ws.loopsound == 0) loopsound.setSelectedIndex(0);
                if (ws.loopsound < 0) loopsound.setSelectedIndex(2);
                else loopsound.setSelectedIndex(1);
            } else if (ws.actiontype == 8) {
                nosoundbut.doClick();
                abrupt.setSelected(ws.abrupt);
            } else if (ws.actiontype >= 5) {
                changeto = ws.changeto;
                switch (changeto.mapchar) {
                    case '0':
                        changetype = 0;
                        break;
                    case '1':
                        changetype = 1;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case '2':
                        changetype = 2;
                        break;
                    case 'w':
                        changetype = 3;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case '[':
                    case 'a':
                    case ']':
                        changetype = 4;
                        nomonsbut.setEnabled(false);
                        break;
                    case 'm':
                        changetype = 5;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case 'f':
                        changetype = 6;
                        nomonsbut.setEnabled(false);
                        break;
                    case '}':
                        changetype = 7;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case 'P':
                        changetype = 8;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case 'd':
                        changetype = 9;
                        break;
                    case 't':
                        changetype = 10;
                        break;
                    case 'p':
                        changetype = 11;
                        break;
                    case 'l':
                        changetype = 12;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case 'g':
                        changetype = 13;
                        break;
                    case 's':
                        changetype = 14;
                        break;
                    case '/':
                        changetype = 15;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case 'S':
                        changetype = 16;
                        break;
                    case '\\':
                        changetype = 17;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case 'D':
                        changetype = 18;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case 'F':
                        changetype = 19;
                        break;
                    case 'i':
                        changetype = 20;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        break;
                    case '>':
                        if (((StairsData) changeto).goesUp) changetype = 21;
                        else changetype = 22;
                        additembut.setEnabled(false);
                        break;
                    case '!':
                        changetype = 23;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        noghostsbut.setEnabled(false);
                        break;
                    case 'G':
                        changetype = 24;
                        additembut.setEnabled(false);
                        nomonsbut.setEnabled(false);
                        noghostsbut.setEnabled(false);
                        break;
                    case 'y':
                        changetype = 25;
                        additembut.setEnabled(false);
                        break;
                    case 'W':
                        changetype = 26;
                        break;
                    case 'E':
                        changetype = 27;
                        break;
                }
                newsquare.setText(changeto.toString());
                retainitems.setSelected(ws.retainitems);
                retainitems.setEnabled(changeto.canHoldItems);
                if (ws.actiontype == 5) swapbut.doClick();
                else settobut.doClick();
                if (!changeto.canPassMons && nomonsbut.isEnabled()) {
                    nomonsbut.setSelected(true);
                    noghostsbut.setSelected(true);
                    noghostsbut.setEnabled(false);
                } else if (!changeto.canPassImmaterial && noghostsbut.isEnabled()) noghostsbut.setSelected(true);
                if (ws.changeto.hasItems && (ws.changeto.mapchar == ']' || ws.changeto.mapchar == 'a' || ws.changeto.mapchar == 'f')) {
                    String sidestring = " (" + sides[(((SidedWallData) ws.changeto).side + 2) % 4] + ")";
                    Item tempitem;
                    for (int i = 0; i < ws.changeto.mapItems.size(); i++) {
                        tempitem = (Item) ws.changeto.mapItems.get(i);
                        mapitems.add(tempitem);
                        itemnames.add(tempitem.name + sidestring);
                    }
                    itemlist.setListData(itemnames);
                } else if (ws.changeto.mapchar == '[') {
                    AlcoveData adata = (AlcoveData) ws.changeto;
                    for (int i = 0; i < adata.northside.size(); i++) {
                        mapitems.add(adata.northside.get(i));
                        itemnames.add(((Item) adata.northside.get(i)).name + " (N)");
                    }
                    for (int i = 0; i < adata.westside.size(); i++) {
                        mapitems.add(adata.westside.get(i));
                        itemnames.add(((Item) adata.westside.get(i)).name + " (W)");
                    }
                    for (int i = 0; i < adata.southside.size(); i++) {
                        mapitems.add(adata.southside.get(i));
                        itemnames.add(((Item) adata.southside.get(i)).name + " (S)");
                    }
                    for (int i = 0; i < adata.eastside.size(); i++) {
                        mapitems.add(adata.eastside.get(i));
                        itemnames.add(((Item) adata.eastside.get(i)).name + " (E)");
                    }
                    itemlist.setListData(itemnames);
                } else if (ws.changeto.hasItems) {
                    Item tempitem;
                    for (int i = 0; i < ws.changeto.mapItems.size(); i++) {
                        tempitem = (Item) ws.changeto.mapItems.get(i);
                        mapitems.add(tempitem);
                        itemnames.add(tempitem.name + " (" + corners[tempitem.subsquare] + ")");
                    }
                    itemlist.setListData(itemnames);
                }
            }
            delay.setText("" + ws.delay);
            reset.setText("" + ws.reset);
            resetnotrigger.setSelected(ws.resetnotrigger);
        }
        
        if (multiple) {
            buttonpane.setVisible(false);
            keypane.setVisible(false);
            if (type == 1) keybut.doClick();
            else if (type == 2) coinbut.doClick();
            if (alcove <= 0) {
                sidelabelpanel.setVisible(false);
                sidepanel.setVisible(false);
            }
        }
        
        cp.add("North", typepanel);
        cp.add("Center", centerpanel);
        cp.add("East", east);
        cp.add("South", bottom);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            int keynum = -1, keysneeded = 1;
            boolean consume = false;
            if (type == 1) {
                keynum = key.number;
                consume = consumebutton1.isSelected();
            } else if (type == 2) {
                keynum = coin.number;
                keysneeded = Integer.parseInt(coincount.getText());
                consume = consumebutton2.isSelected();
            } else if (consumebutton3.isVisible()) consume = consumebutton3.isSelected();
            if (changeto.hasItems) changeto.mapItems.clear();
            if (actiontype >= 5 && actiontype < 7) {
                if (mapitems.size() > 0) {
                    Item tempitem;
                    if (changeto.mapchar == '[') {
                        AlcoveData tempdata = (AlcoveData) changeto;
                        tempdata.northside.clear();
                        tempdata.westside.clear();
                        tempdata.southside.clear();
                        tempdata.eastside.clear();
                        for (int i = 0; i < mapitems.size(); i++) {
                            tempitem = (Item) mapitems.get(i);
                            tempdata.addItem(tempitem, tempitem.subsquare);
                        }
                    } else {
                        for (int i = 0; i < mapitems.size(); i++) {
                            tempitem = (Item) mapitems.get(i);
                            changeto.addItem(tempitem);
                        }
                    }
                }
                changeto.setMapCoord(Integer.parseInt(targetlevel.getText()), Integer.parseInt(targetx.getText()), Integer.parseInt(targety.getText()));
            } else changeto = null;
            data = new WallSwitchData(new MapPoint(level, x, y), side, type, keynum, keysneeded, Integer.parseInt(picklock.getText()), Integer.parseInt(targetlevel.getText()), Integer.parseInt(targetx.getText()), Integer.parseInt(targety.getText()), isReusable.isSelected(), consume, stopswitch.isSelected(), Integer.parseInt(delay.getText()), Integer.parseInt(reset.getText()), resetnotrigger.isSelected(), actiontype, changeto, picnum, retainitems.isSelected());
            if (actiontype == 7) {
                ((WallSwitchData) data).soundstring = soundstring.getText();
                if (loopsound.getSelectedIndex() == 1) ((WallSwitchData) data).loopsound = 1;
                else if (loopsound.getSelectedIndex() == 2) ((WallSwitchData) data).loopsound = -1;
            } else if (actiontype == 8) ((WallSwitchData) data).abrupt = abrupt.isSelected();
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("Button")) {
            type = 0;
            keypanel.setVisible(false);
            coinpanel.setVisible(false);
            picklockpan.setVisible(false);
            buttonpanel.setVisible(true);
        } else if (e.getActionCommand().equals("Key")) {
            type = 1;
            buttonpanel.setVisible(false);
            coinpanel.setVisible(false);
            keypanel.setVisible(true);
            picklockpan.setVisible(true);
        } else if (e.getActionCommand().equals("Coin")) {
            type = 2;
            buttonpanel.setVisible(false);
            keypanel.setVisible(false);
            coinpanel.setVisible(true);
            picklockpan.setVisible(true);
        } else if (e.getActionCommand().equals("From Map...")) {
            //MapPoint targ = (DMEditor.getTargetFrame()).getTarget();
            DMEditor.targetframe.show();
            MapPoint targ = DMEditor.targetframe.getTarget();
            if (targ != null) {
                targetlevel.setText("" + targ.level);
                targetx.setText("" + targ.x);
                targety.setText("" + targ.y);
            }
        } else if (e.getActionCommand().equals("Change")) {
            Item tempitem;
            if (type == 1) {
                //tempitem = (new ItemWizard(frame,"Set Switch Key",key)).getItem();
                DMEditor.itemwizard.setTitle("Item Wizard - Set Switch Key");
                DMEditor.itemwizard.setItem(key);
                tempitem = DMEditor.itemwizard.getItem();
                if (tempitem != null) {
                    key = tempitem;
                    keylabel.setText(key.name);
                }
            } else {
                //tempitem = (new ItemWizard(frame,"Set Switch Coin",coin)).getItem();
                DMEditor.itemwizard.setTitle("Item Wizard - Set Switch Coin");
                DMEditor.itemwizard.setItem(coin);
                tempitem = DMEditor.itemwizard.getItem();
                if (tempitem != null) {
                    coin = tempitem;
                    coinlabel.setText(coin.name);
                }
            }
        } else if (e.getActionCommand().equals("Toggles")) {
            newsquarepan.setVisible(false);
            soundpan.setVisible(false);
            abrupt.setVisible(false);
            actiontype = 0;
            if (itempanel.isVisible()) {
                additembut.setSelected(false);
                itempanel.setVisible(false);
                buttonbut.setEnabled(true);
                keybut.setEnabled(true);
                coinbut.setEnabled(true);
                if (type == 0) buttonpanel.setVisible(true);
                else if (type == 1) keypanel.setVisible(true);
                else coinpanel.setVisible(true);
            }
        } else if (e.getActionCommand().equals("Activates")) {
            newsquarepan.setVisible(false);
            soundpan.setVisible(false);
            abrupt.setVisible(false);
            actiontype = 1;
            if (itempanel.isVisible()) {
                additembut.setSelected(false);
                itempanel.setVisible(false);
                buttonbut.setEnabled(true);
                keybut.setEnabled(true);
                coinbut.setEnabled(true);
                if (type == 0) buttonpanel.setVisible(true);
                else if (type == 1) keypanel.setVisible(true);
                else coinpanel.setVisible(true);
            }
        } else if (e.getActionCommand().equals("Deactivates")) {
            newsquarepan.setVisible(false);
            soundpan.setVisible(false);
            abrupt.setVisible(false);
            actiontype = 2;
            if (itempanel.isVisible()) {
                additembut.setSelected(false);
                itempanel.setVisible(false);
                buttonbut.setEnabled(true);
                keybut.setEnabled(true);
                coinbut.setEnabled(true);
                if (type == 0) buttonpanel.setVisible(true);
                else if (type == 1) keypanel.setVisible(true);
                else coinpanel.setVisible(true);
            }
        } else if (e.getActionCommand().equals("Act/Deact")) {
            newsquarepan.setVisible(false);
            soundpan.setVisible(false);
            abrupt.setVisible(false);
            actiontype = 3;
            if (itempanel.isVisible()) {
                additembut.setSelected(false);
                itempanel.setVisible(false);
                buttonbut.setEnabled(true);
                keybut.setEnabled(true);
                coinbut.setEnabled(true);
                if (type == 0) buttonpanel.setVisible(true);
                else if (type == 1) keypanel.setVisible(true);
                else coinpanel.setVisible(true);
            }
        } else if (e.getActionCommand().equals("Deact/Act")) {
            newsquarepan.setVisible(false);
            soundpan.setVisible(false);
            abrupt.setVisible(false);
            actiontype = 4;
            if (itempanel.isVisible()) {
                additembut.setSelected(false);
                itempanel.setVisible(false);
                buttonbut.setEnabled(true);
                keybut.setEnabled(true);
                coinbut.setEnabled(true);
                if (type == 0) buttonpanel.setVisible(true);
                else if (type == 1) keypanel.setVisible(true);
                else coinpanel.setVisible(true);
            }
        } else if (e.getActionCommand().equals("Exchanges")) {
            soundpan.setVisible(false);
            abrupt.setVisible(false);
            newsquarepan.setVisible(true);
            actiontype = 5;
        } else if (e.getActionCommand().equals("Set To")) {
            soundpan.setVisible(false);
            abrupt.setVisible(false);
            newsquarepan.setVisible(true);
            actiontype = 6;
        } else if (e.getActionCommand().equals("Play Sound")) {
            newsquarepan.setVisible(false);
            abrupt.setVisible(false);
            soundpan.setVisible(true);
            actiontype = 7;
        } else if (e.getActionCommand().equals("Stop Sounds")) {
            newsquarepan.setVisible(false);
            soundpan.setVisible(false);
            abrupt.setVisible(true);
            actiontype = 8;
        } else if (e.getActionCommand().equals("Change Square")) {
            String[] types = {"Floor", "Wall", "False Wall", "Writing", "Alcove", "Mirror", "Fountain", "Sconce", "Pillar", "Door", "Teleport", "Pit", "Launcher", "Generator", "Floor Switch", "Wall Switch", "Multiple Floor Switch", "Multiple Wall Switch", "Wall Decoration", "Floor Decoration", "Invisible Wall", "Stairs Up", "Stairs Down", "Stormbringer", "Power Gem", "FulYa Pit", "Win Square", "Event Square"};
            Object[] cs = new Object[3];
            cs[0] = new JComboBox(types);
            cs[1] = "Ok";
            cs[2] = "Cancel";
            ((JComboBox) cs[0]).setSelectedIndex(changetype);
            int returnval = JOptionPane.showOptionDialog(frame, "What Kind of Map Object?", "Specify Exchanged Object", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, cs, cs[1]);
            if (returnval == 2 || returnval == JOptionPane.CLOSED_OPTION) return;
            MapData tempdata = null;
            changetype = ((JComboBox) cs[0]).getSelectedIndex();
            boolean oldaddenab = additembut.isEnabled(), oldmonsenab = nomonsbut.isEnabled(), oldmonssel = nomonsbut.isSelected(), oldghostenab = noghostsbut.isEnabled(), oldghostsel = noghostsbut.isSelected();
            additembut.setEnabled(true);
            nomonsbut.setEnabled(true);
            nomonsbut.setSelected(false);
            noghostsbut.setEnabled(true);
            noghostsbut.setSelected(false);
            int oldside, tempside;
            switch (changetype) {
                case 0: //floor
                    changeto = new FloorData();
                    tempdata = changeto;
                    break;
                case 1: //wall
                    changeto = new WallData();
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    tempdata = changeto;
                    break;
                case 2: //false wall
                    changeto = new FakeWallData();
                    tempdata = changeto;
                    break;
                case 3: //writing
                    //tempdata = (new WritingWizard(frame,changeto)).getData();
                    DMEditor.writingwizard.setData(changeto);
                    tempdata = DMEditor.writingwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    break;
                case 4: //alcove
                    //tempdata = (new AlcoveWizard(frame,changeto,level,x,y)).getData();
                    if (!DMEditor.alcovewizard.isVisible()) {
                        DMEditor.alcovewizard.setData(changeto, level, x, y);
                        tempdata = DMEditor.alcovewizard.getData();
                    } else {
                        AlcoveWizard aw = new AlcoveWizard(frame);
                        aw.setData(changeto, level, x, y);
                        tempdata = aw.getData();
                    }
                    if (tempdata == null) break;
                    changeto = tempdata;
                    nomonsbut.setEnabled(false);
                    break;
                case 5: //mirror
                    //tempdata = (new MirrorWizard(frame,changeto)).getData();
                    DMEditor.mirrorwizard.setData(changeto);
                    tempdata = DMEditor.mirrorwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    break;
                case 6: //fountain
                    if (!DMEditor.fountainwizard.isVisible()) {
                        DMEditor.fountainwizard.setData(changeto, level, x, y);
                        tempdata = DMEditor.fountainwizard.getData();
                    } else {
                        FountainWizard fw = new FountainWizard(frame);
                        fw.setData(changeto, level, x, y);
                        tempdata = fw.getData();
                    }
                    if (tempdata == null) break;
                    changeto = tempdata;
                    nomonsbut.setEnabled(false);
                    break;
                case 7: //sconce
                    //tempdata = (new SconceWizard(frame,changeto,level,x,y)).getData();
                    if (!DMEditor.sconcewizard.isVisible()) {
                        DMEditor.sconcewizard.setData(changeto, level, x, y);
                        tempdata = DMEditor.sconcewizard.getData();
                    } else {
                        SconceWizard fw = new SconceWizard(frame);
                        fw.setData(changeto, level, x, y);
                        tempdata = fw.getData();
                    }
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    break;
                case 8: //pillar
                    //tempdata = (new PillarWizard(frame,changeto,((x+y)%2==0))).getData();
                    DMEditor.pillarwizard.setData(changeto, ((x + y) % 2 == 0));
                    tempdata = DMEditor.pillarwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    break;
                case 9: //door
                    //tempdata = (new DoorWizard(frame,changeto,level,x,y)).getData();
                    DMEditor.doorwizard.setData(changeto, level, x, y);
                    tempdata = DMEditor.doorwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
                case 10: //teleport
                    //tempdata = (new TeleportWizard(frame,changeto,level,x,y)).getData();
                    DMEditor.teleportwizard.setData(changeto, level, x, y);
                    tempdata = DMEditor.teleportwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
                case 11: //pit
                    //tempdata = (new PitWizard(frame,changeto,level,x,y)).getData();
                    DMEditor.pitwizard.setData(changeto, level, x, y);
                    tempdata = DMEditor.pitwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
                case 12: //launcher
                    //tempdata = (new LauncherWizard(frame,changeto,level,x,y)).getData();
                    DMEditor.launcherwizard.setData(changeto, level, x, y);
                    tempdata = DMEditor.launcherwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    break;
                case 13: //generator
                    //tempdata = (new GeneratorWizard(frame,changeto,level,x,y)).getData();
                    DMEditor.generatorwizard.setData(changeto, level, x, y);
                    tempdata = DMEditor.generatorwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
                case 14: //floor switch
                    tempdata = (new FloorSwitchWizard(frame, changeto, Integer.parseInt(targetlevel.getText()), Integer.parseInt(targetx.getText()), Integer.parseInt(targety.getText()))).getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
                case 15: //wall switch
                    tempdata = (new WallSwitchWizard(frame, changeto, Integer.parseInt(targetlevel.getText()), Integer.parseInt(targetx.getText()), Integer.parseInt(targety.getText()))).getData();
                    //WallSwitchWizard wsw = new WallSwitchWizard(frame);
                    //wsw.setData(changeto,level,x,y);
                    //tempdata = wsw.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    break;
                case 16: //multiple floor switch
                    tempdata = (new MultFloorSwitchWizard(frame, changeto, Integer.parseInt(targetlevel.getText()), Integer.parseInt(targetx.getText()), Integer.parseInt(targety.getText()))).getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
                case 17: //multiple wall switch
                    tempdata = (new MultWallSwitchWizard(frame, changeto, Integer.parseInt(targetlevel.getText()), Integer.parseInt(targetx.getText()), Integer.parseInt(targety.getText()))).getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    break;
                case 18: //wall dec
                    //tempdata = (new DecorationWizard(frame,changeto)).getData();
                    DMEditor.decorationwizard.setData(changeto);
                    tempdata = DMEditor.decorationwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    break;
                case 19: //floor dec
                    //tempdata = (new FDecorationWizard(frame,changeto)).getData();
                    DMEditor.fdecorationwizard.setData(changeto, level, x, y);
                    tempdata = DMEditor.fdecorationwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
                case 20: //invisible wall
                    changeto = new InvisibleWallData();
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    tempdata = changeto;
                    break;
                case 21: //stairs up
                    if (changeto.mapchar == '>') oldside = (((StairsData) changeto).side + 2) % 4;
                    else oldside = 2;
                    tempside = (new MoverDialog(frame, "Choose Stairs Direction", oldside, false)).getNewLocation();
                    if (tempside >= 0) changeto = new StairsData((tempside + 2) % 4, true);
                    else break;
                    additembut.setEnabled(false);
                    tempdata = changeto;
                    break;
                case 22: //stairs down
                    if (changeto.mapchar == '>') oldside = (((StairsData) changeto).side + 2) % 4;
                    else oldside = 2;
                    tempside = (new MoverDialog(frame, "Choose Stairs Direction", oldside, false)).getNewLocation();
                    if (tempside >= 0) changeto = new StairsData((tempside + 2) % 4, false);
                    else break;
                    additembut.setEnabled(false);
                    tempdata = changeto;
                    break;
                case 23: //stormbringer
                    changeto = new StormbringerData(false);
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    noghostsbut.setEnabled(false);
                    tempdata = changeto;
                    break;
                case 24: //power gem
                    changeto = new PowerGemData(false);
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    nomonsbut.setEnabled(false);
                    noghostsbut.setEnabled(false);
                    tempdata = changeto;
                    break;
                case 25: //fulya pit
                    tempdata = (new FulYaWizard(frame, changeto, level, x, y)).getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    if (additembut.isSelected()) additembut.doClick();
                    additembut.setEnabled(false);
                    break;
                case 26: //gamewin
                    //String ending = JOptionPane.showInputDialog("Enter Ending Animation");
                    //if (ending=="") return;
                    //changeto = new GameWinData(ending);
                    tempdata = (new GameWinWizard(frame, changeto)).getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
                case 27: //event square
                    DMEditor.eventwizard.setData(changeto);
                    tempdata = DMEditor.eventwizard.getData();
                    if (tempdata == null) break;
                    changeto = tempdata;
                    break;
            }
            if (tempdata == null) {
                additembut.setEnabled(oldaddenab);
                nomonsbut.setEnabled(oldmonsenab);
                nomonsbut.setSelected(oldmonssel);
                noghostsbut.setEnabled(oldghostenab);
                noghostsbut.setSelected(oldghostsel);
                return;
            }
            newsquare.setText(changeto.toString());
            mapitems.clear();
            itemnames.clear();
            itemlist.setListData(itemnames);
            retainitems.setEnabled(changeto.canHoldItems);
            if (!changeto.canHoldItems) retainitems.setSelected(false);
        } else if (e.getActionCommand().equals("Browse")) {
            dialog.show();
            String newpic = dialog.getFile();
            if (newpic != null) {
                soundstring.setText(newpic);
            }
        } else if (e.getActionCommand().equals("No Mons")) {
            changeto.canPassMons = !changeto.canPassMons;
            if (!changeto.canPassMons) {
                noghostsbut.setSelected(true);
                noghostsbut.setEnabled(false);
            } else noghostsbut.setEnabled(true);
        } else if (e.getActionCommand().equals("No Ghosts")) {
            changeto.canPassImmaterial = !changeto.canPassImmaterial;
        } else if (e.getActionCommand().equals("Items...")) {
            if (!itempanel.isVisible()) {
                buttonbut.setEnabled(false);
                keybut.setEnabled(false);
                coinbut.setEnabled(false);
                if (type == 0) buttonpanel.setVisible(false);
                else if (type == 1) keypanel.setVisible(false);
                else coinpanel.setVisible(false);
                itempanel.setVisible(true);
                return;
            }
            itempanel.setVisible(false);
            buttonbut.setEnabled(true);
            keybut.setEnabled(true);
            coinbut.setEnabled(true);
            if (type == 0) buttonpanel.setVisible(true);
            else if (type == 1) keypanel.setVisible(true);
            else coinpanel.setVisible(true);
        } else if (e.getActionCommand().equals("Add/Edit Item")) {
            Item tempitem;
            int index = itemlist.getSelectedIndex();
            if (index == -1) {
                //tempitem = (new ItemWizard(frame)).getItem();
                DMEditor.itemwizard.setTitle("Item Wizard - Add An Item To The New Square");
                DMEditor.itemwizard.show();
                tempitem = DMEditor.itemwizard.getItem();
                if (tempitem == null) return;
                tempitem.subsquare = 0;
                mapitems.add(tempitem);
                if (changeto.mapchar == '[') itemnames.add(tempitem.name + " (N)");
                else if (changeto.mapchar == 'a' || changeto.mapchar == ']' || changeto.mapchar == 'f') {
                    tempitem.subsquare = (((SidedWallData) changeto).side + 2) % 4;
                    itemnames.add(tempitem.name + " (" + sides[tempitem.subsquare] + ")");
                } else itemnames.add(tempitem.name + " (NW)");
                itemlist.setListData(itemnames);
            } else {
                //tempitem = (new ItemWizard(frame,(Item)mapitems.get(index))).getItem();
                DMEditor.itemwizard.setTitle("Item Wizard - Change An Item On The New Square");
                DMEditor.itemwizard.setItem((Item) mapitems.get(index));
                tempitem = DMEditor.itemwizard.getItem();
                if (tempitem == null) {
                    itemlist.clearSelection();
                    itemindex = -1;
                    return;
                }
                tempitem.subsquare = ((Item) mapitems.get(index)).subsquare;
                mapitems.set(index, tempitem);
                if (changeto.mapchar == '[')
                    itemnames.setElementAt(tempitem.name + " (" + sides[tempitem.subsquare] + ")", index);
                else if (changeto.mapchar == 'a' || changeto.mapchar == ']' || changeto.mapchar == 'f') {
                    tempitem.subsquare = (((SidedWallData) changeto).side + 2) % 4;
                    itemnames.setElementAt(tempitem.name + " (" + sides[tempitem.subsquare] + ")", index);
                } else itemnames.setElementAt(tempitem.name + " (" + corners[tempitem.subsquare] + ")", index);
                itemlist.setListData(itemnames);
                itemindex = -1;
            }
        } else if (e.getActionCommand().equals("Remove Item")) {
            if (itemnames.size() == 0 || itemlist.isSelectionEmpty()) return;
            int index = itemlist.getSelectedIndex();
            mapitems.remove(index);
            itemnames.remove(index);
            itemlist.setListData(itemnames);
            itemindex = -1;
        } else if (e.getActionCommand().equals("Change Side/Corner")) {
            if (itemnames.size() == 0 || itemlist.isSelectionEmpty()) return;
            //if one-sided alcove or altar, return
            if (changeto.mapchar == 'a' || changeto.mapchar == ']') {
                itemlist.clearSelection();
                itemindex = -1;
                return;
            }
            int index = itemlist.getSelectedIndex();
            if (changeto.mapchar == '[') {
                Item tempitem = (Item) mapitems.get(index);
                int newside = JOptionPane.showOptionDialog(frame, "Which side?", "Change Side", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, sides, sides[tempitem.subsquare]);
                tempitem.subsquare = newside;
                itemnames.setElementAt(tempitem.name + " (" + sides[newside] + ")", index);
                itemlist.setListData(itemnames);
                itemindex = -1;
            } else {
                Item tempitem = (Item) mapitems.get(index);
                int newcorner = JOptionPane.showOptionDialog(frame, "Which corner?", "Change Corner", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, corners, corners[tempitem.subsquare]);
                tempitem.subsquare = newcorner;
                itemnames.setElementAt(tempitem.name + " (" + corners[newcorner] + ")", index);
                itemlist.setListData(itemnames);
                itemindex = -1;
            }
        } else if (e.getActionCommand().equals("North")) {
            side = 2;
        } else if (e.getActionCommand().equals("South")) {
            side = 0;
        } else if (e.getActionCommand().equals("East")) {
            side = 1;
        } else if (e.getActionCommand().equals("West")) {
            side = 3;
        } else {
            int num;
            try {
                num = Integer.parseInt(e.getActionCommand());
            } catch (NumberFormatException ex) {
                num = 0;
            }
            picnum = num;
        }
    }
    
    public void mousePressed(MouseEvent e) {
        int clickedindex = itemlist.locationToIndex(e.getPoint());
        if (clickedindex == -1 || clickedindex == itemindex) itemlist.clearSelection();
        itemindex = itemlist.getSelectedIndex();
    }
    
    public MapData getData() {
        return data;
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
}