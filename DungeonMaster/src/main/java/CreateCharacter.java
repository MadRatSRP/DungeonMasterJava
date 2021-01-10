import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;//File;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

class CreateCharacter extends JPanel implements ActionListener, FilenameFilter {
    
    dmnew dm;
    public static int MAXLEVELPOINTS = 5;
    public static int MAXHSMPOINTS = 20;
    public static int MAXSTATPOINTS = 75;
    public static int MAXDEFENSEPOINTS = 8;
    private int levelpoints = MAXLEVELPOINTS;
    private int hsmpoints = MAXHSMPOINTS;
    private int statpoints = MAXSTATPOINTS;
    private int defensepoints = MAXDEFENSEPOINTS;//for defense and magicresist
    
    private boolean dmstarted, finishnewgame;
    private JPanel createpanel;
    private JTextField name, lastname;
    private JLabel[] stat;
    private JLabel levelpointslabel = new JLabel("" + MAXLEVELPOINTS);
    private JLabel hsmpointslabel = new JLabel("" + MAXHSMPOINTS);
    private JLabel statpointslabel = new JLabel("" + MAXSTATPOINTS);
    private JLabel defensepointslabel = new JLabel("" + MAXDEFENSEPOINTS);
    private ImageIcon pic;
    private JLabel portrait;
    private String picname, picdirectory;
    private int picnumber = 0;
    private File portraitdirectory;
    private File[] portraitfiles;
    //private JFileChooser browser;
    private FileDialog browser;
    private File gamefile;
    public static final String[] statstring =
        {"Fighter Level", "Ninja Level", "Wizard Level", "Priest Level",
            "Health", "Stamina", "Mana",
            "Strength", "Dexterity", "Vitality", "Intelligence", "Wisdom",
            "Defense", "Magic Resistance"};
    public int[] statnum =
        {0, 0, 0, 0, 10, 10, 0, 30, 30, 30, 30, 30, 0, 0};
    private Random randGen = new Random();
    private int[] levelboost = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    
    private Item[] itemchoose;
    private SpecialAbility[] abilitychoose;
    private int itempoints, abilitypoints, abilityauto;
    private JPanel itabpan;
    private JToggleButton[] itembutton, abilitybutton;
    private JLabel itpointlabel, abpointlabel;
    
    public CreateCharacter(JFrame f, File gfile, boolean create, boolean nochar, int lp, int hsmp, int sp, int dp, Item[] ic, int ip, SpecialAbility[] ac, int aa, int ap) {
        this(f, gfile, create, nochar, lp, hsmp, sp, dp, ic, ip, ac, aa, ap, false, false);
    }
    
    public CreateCharacter(JFrame f, File gfile, boolean create, boolean nochar, int lp, int hsmp, int sp, int dp, Item[] ic, int ip, SpecialAbility[] ac, int aa, int ap, boolean started) {
        this(f, gfile, create, nochar, lp, hsmp, sp, dp, ic, ip, ac, aa, ap, started, false);
    }
    
    public CreateCharacter(JFrame f, File gfile, boolean create, boolean nochar, int lp, int hsmp, int sp, int dp, Item[] ic, int ip, SpecialAbility[] ac, int aa, int ap, boolean started, boolean finishnewgame) {
        //super("Dungeon Master Java - New Game");
        dm = (dmnew) f;
        
        gamefile = gfile;
        dmstarted = started;
        this.finishnewgame = finishnewgame;
        
        setBackground(new Color(0, 0, 64));
        
        createpanel = new JPanel(new BorderLayout(10, 10));
        createpanel.setBackground(new Color(0, 0, 64));
        JLabel namelabel = new JLabel("First Name");
        JLabel lastnamelabel = new JLabel("Last Name / Title");
        Color labelcolor = new Color(80, 80, 80);
        namelabel.setForeground(labelcolor);
        lastnamelabel.setForeground(labelcolor);
        name = new JTextField(8);
        lastname = new JTextField(30);
        Font fnt = dm.dungfont;
        name.setFont(fnt);
        lastname.setFont(fnt);
        name.setBackground(new Color(110, 110, 200));
        lastname.setBackground(new Color(110, 110, 200));
        //name.addFocusListener(this);
        //lastname.addFocusListener(this);
        Box top = Box.createVerticalBox();
        JPanel top1 = new JPanel();
        top1.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel top2 = new JPanel();
        top1.setBackground(new Color(0, 0, 64));
        top2.setBackground(new Color(0, 0, 64));
        top1.add(Box.createHorizontalStrut(35));
        top1.add(namelabel);
        top1.add(Box.createHorizontalStrut(60));
        top1.add(lastnamelabel);
        top2.add(name);
        top2.add(lastname);
        top.add(top1);
        top.add(top2);
        
        JLabel[] statname = new JLabel[14];
        stat = new JLabel[14];
        JButton[] fastuparrow = new JButton[8];
        JButton[] uparrow = new JButton[14];
        JButton[] fastdownarrow = new JButton[8];
        JButton[] downarrow = new JButton[10];
        Box statnamebox = Box.createVerticalBox();
        JPanel statbox = new JPanel();
        statbox.setLayout(new BoxLayout(statbox, BoxLayout.Y_AXIS));
        statbox.setBackground(new Color(0, 0, 64));
        Box fastuparrowbox = Box.createVerticalBox();
        Box uparrowbox = Box.createVerticalBox();
        Box fastdownarrowbox = Box.createVerticalBox();
        Box downarrowbox = Box.createVerticalBox();
        ImageIcon fastuparw = new ImageIcon("fastuparrow.gif");
        ImageIcon uparw = new ImageIcon("uparrow.gif");
        ImageIcon fastdownarw = new ImageIcon("fastdownarrow.gif");
        ImageIcon downarw = new ImageIcon("downarrow.gif");
        statnamebox.add(Box.createVerticalStrut(6));
        statbox.add(Box.createVerticalStrut(6));
        fastuparrowbox.add(Box.createVerticalStrut(34));
        fastdownarrowbox.add(Box.createVerticalStrut(34));
        downarrowbox.add(Box.createVerticalStrut(73));
        Dimension statnamedim = new Dimension(110, 17);
        Dimension statdim = new Dimension(35, 17);
        for (int i = 0; i < 14; i++) {
            statname[i] = new JLabel(statstring[i]);
            statname[i].setPreferredSize(statnamedim);
            statname[i].setMinimumSize(statnamedim);
            statname[i].setMaximumSize(statnamedim);
            statname[i].setForeground(labelcolor);
            statnamebox.add(statname[i]);
            stat[i] = new JLabel("" + statnum[i]);
            stat[i].setPreferredSize(statdim);
            stat[i].setMinimumSize(statdim);
            stat[i].setMaximumSize(statdim);
            stat[i].setForeground(Color.white);
            statbox.add(stat[i]);
            uparrow[i] = new JButton(uparw);
            uparrow[i].setPreferredSize(new Dimension(18, 16));
            uparrow[i].setMaximumSize(new Dimension(18, 16));
            uparrow[i].setActionCommand("up" + i);
            uparrow[i].addActionListener(this);
            uparrowbox.add(Box.createVerticalStrut(1));
            uparrowbox.add(uparrow[i]);
            if (i > 3) {
                downarrow[i - 4] = new JButton(downarw);
                downarrow[i - 4].setPreferredSize(new Dimension(18, 16));
                downarrow[i - 4].setMaximumSize(new Dimension(18, 16));
                downarrow[i - 4].setActionCommand("down" + (i - 4));
                downarrow[i - 4].addActionListener(this);
                downarrowbox.add(Box.createVerticalStrut(1));
                downarrowbox.add(downarrow[i - 4]);
            }
            if (i > 3 && i < 12) {
                fastuparrow[i - 4] = new JButton(fastuparw);
                fastuparrow[i - 4].setPreferredSize(new Dimension(18, 16));
                fastuparrow[i - 4].setMaximumSize(new Dimension(18, 16));
                fastuparrow[i - 4].setActionCommand("fup" + i);
                fastuparrow[i - 4].addActionListener(this);
                fastdownarrow[i - 4] = new JButton(fastdownarw);
                fastdownarrow[i - 4].setPreferredSize(new Dimension(18, 16));
                fastdownarrow[i - 4].setMaximumSize(new Dimension(18, 16));
                fastdownarrow[i - 4].setActionCommand("fdown" + i);
                fastdownarrow[i - 4].addActionListener(this);
                fastuparrowbox.add(Box.createVerticalStrut(1));
                fastuparrowbox.add(fastuparrow[i - 4]);
                fastdownarrowbox.add(Box.createVerticalStrut(1));
                fastdownarrowbox.add(fastdownarrow[i - 4]);
                if (i == 6) {
                    fastuparrowbox.add(Box.createVerticalStrut(5));
                    fastdownarrowbox.add(Box.createVerticalStrut(5));
                }
            }
            if (i == 3 || i == 6 || i == 11) {
                statnamebox.add(Box.createVerticalStrut(5));
                statbox.add(Box.createVerticalStrut(5));
                uparrowbox.add(Box.createVerticalStrut(5));
                if (i != 3) downarrowbox.add(Box.createVerticalStrut(5));
            }
        }
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setBackground(new Color(0, 0, 64));
        left.add(Box.createHorizontalStrut(10));
        left.add(statnamebox);
        left.add(Box.createHorizontalStrut(20));
        left.add(statbox);
        left.add(Box.createHorizontalStrut(20));
        left.add(fastuparrowbox);
        left.add(Box.createHorizontalStrut(2));
        left.add(uparrowbox);
        left.add(Box.createHorizontalStrut(2));
        left.add(downarrowbox);
        left.add(Box.createHorizontalStrut(2));
        left.add(fastdownarrowbox);
        
        browser = new FileDialog(f, "Choose a Portrait", FileDialog.LOAD);
        browser.setDirectory("Heroes");
        browser.setFilenameFilter(this);
        browser.setLocation(dm.tk.getScreenSize().width / 2 - browser.getSize().width / 2, dm.tk.getScreenSize().height / 2 - browser.getSize().height / 2);
        picdirectory = "";
        portraitdirectory = new File("Heroes");
        portraitfiles = portraitdirectory.listFiles(this);
        Arrays.sort(portraitfiles);
        picname = portraitfiles[0].getName();
        pic = new ImageIcon("Heroes" + File.separator + picname);
        JLabel portlabel = new JLabel("Portrait");
        portlabel.setForeground(labelcolor);
        portrait = new JLabel(pic);
        ImageIcon leftarw = new ImageIcon("leftarrow.gif");
        ImageIcon rightarw = new ImageIcon("rightarrow.gif");
        JButton leftarrow = new JButton(leftarw);
        JButton rightarrow = new JButton(rightarw);
        JButton browsebut = new JButton("Browse");
        JPanel portpan = new JPanel();
        JPanel portlabelpan = new JPanel();
        JPanel portraitpan = new JPanel();
        portpan.setBackground(new Color(0, 0, 64));
        portlabelpan.setBackground(new Color(0, 0, 64));
        portraitpan.setBackground(new Color(0, 0, 64));
        rightarrow.setActionCommand("<");
        rightarrow.addActionListener(this);
        leftarrow.setActionCommand(">");
        leftarrow.addActionListener(this);
        browsebut.addActionListener(this);
        portlabelpan.add(portlabel);
        portraitpan.add(portrait);
        portpan.setPreferredSize(new Dimension(150, 100));
        portpan.setMaximumSize(new Dimension(150, 100));
        portpan.add(leftarrow);
        portpan.add(rightarrow);
        portpan.add(browsebut);
        
        JPanel pointbox1 = new JPanel();
        pointbox1.setBackground(new Color(110, 110, 200));
        pointbox1.setLayout(new BoxLayout(pointbox1, BoxLayout.Y_AXIS));
        JPanel pointbox2 = new JPanel();
        pointbox2.setBackground(new Color(110, 110, 200));
        pointbox2.setLayout(new BoxLayout(pointbox2, BoxLayout.Y_AXIS));
        pointbox1.add(new JLabel("Level Points:"));
        pointbox2.add(levelpointslabel);
        pointbox1.add(new JLabel("H/S/M Points:"));
        pointbox2.add(hsmpointslabel);
        pointbox1.add(new JLabel("Stat Points:"));
        pointbox2.add(statpointslabel);
        pointbox1.add(new JLabel("Defense Points:"));
        pointbox2.add(defensepointslabel);
        JPanel pointpanel = new JPanel();
        pointpanel.setBackground(new Color(110, 110, 200));
        pointpanel.add(pointbox1);
        pointpanel.add(pointbox2);
        pointpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.gray, Color.black), "Points Remaining"));
        
        JButton itabbut = new JButton("Items and Abilities...");
        itabbut.setMargin(new Insets(2, 4, 0, 4));
        itabbut.addActionListener(this);
        JPanel itabbutpan = new JPanel();
        itabbutpan.setOpaque(false);
        itabbutpan.add(itabbut);
        
        JPanel itabtop = new JPanel();
        itabtop.setOpaque(false);
        itemchoose = ic;
        abilitychoose = ac;
        itempoints = ip;
        abilitypoints = ap;
        abilityauto = aa;
        if (itemchoose == null && abilitychoose == null) itabbut.setEnabled(false);
        Dimension butsize = new Dimension(187, 20);
        if (itemchoose != null) {
            JPanel itempanel = new JPanel();
            itempanel.setLayout(new BoxLayout(itempanel, BoxLayout.Y_AXIS));
            itempanel.setOpaque(false);
            itembutton = new JToggleButton[itemchoose.length];
            for (int i = 0; i < itemchoose.length; i++) {
                itembutton[i] = new JToggleButton(itemchoose[i].name);
                itembutton[i].setPreferredSize(butsize);
                itembutton[i].setMaximumSize(butsize);
                itembutton[i].setMinimumSize(butsize);
                itembutton[i].setActionCommand("i" + i);
                itembutton[i].addActionListener(this);
                itempanel.add(itembutton[i]);
            }
            JScrollPane itempane = new JScrollPane(itempanel);
            itempane.setPreferredSize(new Dimension(200, 200));
            itempane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Starting Items"));
            //itempane.setOpaque(false);
            itabtop.add(itempane);
        }
        if (abilitychoose != null) {
            JPanel abilitypanel = new JPanel();
            abilitypanel.setLayout(new BoxLayout(abilitypanel, BoxLayout.Y_AXIS));
            abilitypanel.setOpaque(false);
            abilitybutton = new JToggleButton[abilitychoose.length];
            for (int i = 0; (i + abilityauto) < abilitychoose.length; i++) {
                abilitybutton[i] = new JToggleButton(abilitychoose[i + abilityauto].name);
                abilitybutton[i].setPreferredSize(butsize);
                abilitybutton[i].setMaximumSize(butsize);
                abilitybutton[i].setMinimumSize(butsize);
                abilitybutton[i].setActionCommand("a" + i);
                abilitybutton[i].addActionListener(this);
                abilitypanel.add(abilitybutton[i]);
            }
            JScrollPane abilitypane = new JScrollPane(abilitypanel);
            abilitypane.setPreferredSize(new Dimension(200, 200));
            abilitypane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Starting Abilities"));
            //abilitypane.setOpaque(false);
            itabtop.add(abilitypane);
        }
        JPanel itabpointpan = new JPanel();
        itpointlabel = new JLabel("Items Left: " + itempoints);
        abpointlabel = new JLabel("Abilities Left: " + abilitypoints);
        itpointlabel.setForeground(labelcolor);
        abpointlabel.setForeground(labelcolor);
        if (itemchoose != null) itabpointpan.add(itpointlabel);
        if (abilitychoose != null) itabpointpan.add(abpointlabel);
        itabpointpan.setOpaque(false);
        JPanel itabbot = new JPanel();
        JButton backbut = new JButton("Back");
        backbut.addActionListener(this);
        itabbot.add(backbut);
        itabbot.setOpaque(false);
        //put it together
        itabpan = new JPanel(new BorderLayout());
        itabpan.add("North", itabtop);
        itabpan.add("Center", itabpointpan);
        itabpan.add("South", itabbot);
        itabpan.setPreferredSize(new Dimension(500, 400));
        itabpan.setOpaque(false);
        
        JPanel right = new JPanel();
        right.setBackground(new Color(0, 0, 64));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setPreferredSize(new Dimension(150, 300));
        right.add(Box.createVerticalGlue());
        right.add(portlabelpan);
        right.add(portraitpan);
        right.add(portpan);
        right.add(Box.createVerticalGlue());
        right.add(itabbutpan);
        right.add(Box.createVerticalStrut(10));
        right.add(pointpanel);
        
        JButton done = new JButton("Done");
        JButton donereinc = new JButton("Done - Reincarnate");
        JButton resetlevels = new JButton("Reset Levels");
        JButton resetall = new JButton("Reset All");
        done.addActionListener(this);
        donereinc.addActionListener(this);
        resetlevels.addActionListener(this);
        resetall.addActionListener(this);
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(0, 0, 64));
        bottom.add(done);
        bottom.add(donereinc);
        bottom.add(resetlevels);
        bottom.add(resetall);
        
        createpanel.add("North", top);
        createpanel.add("South", bottom);
        createpanel.add("West", left);
        createpanel.add("East", right);
        
        JButton createbutton = new JButton("Create Character");
        JButton nocharbutton = new JButton("No Starting Character");
        createbutton.addActionListener(this);
        nocharbutton.addActionListener(this);
        if (!create && !nochar) create = true;
        createbutton.setEnabled(create);
        nocharbutton.setEnabled(nochar);
        if (create) {
            MAXLEVELPOINTS = lp;
            MAXHSMPOINTS = hsmp;
            MAXSTATPOINTS = sp;
            MAXDEFENSEPOINTS = dp;
            levelpointslabel.setText("" + MAXLEVELPOINTS);
            hsmpointslabel.setText("" + MAXHSMPOINTS);
            statpointslabel.setText("" + MAXSTATPOINTS);
            defensepointslabel.setText("" + MAXDEFENSEPOINTS);
            levelpoints = MAXLEVELPOINTS;
            hsmpoints = MAXHSMPOINTS;
            statpoints = MAXSTATPOINTS;
            defensepoints = MAXDEFENSEPOINTS;//for defense and magicresist
        }
        JPanel centerbutpan = new JPanel();
        centerbutpan.setBackground(new Color(0, 0, 64));
        centerbutpan.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 150));
        centerbutpan.add(createbutton);
        centerbutpan.add(nocharbutton);
        
        add("Center", centerbutpan);
        
        setSize(500, 400);
        
        if (!nochar) createbutton.doClick();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().startsWith("Done")) {
            //add a name check, pop up a warning if necessary
            if (name.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "You must give your character a name.", "Notice", JOptionPane.ERROR_MESSAGE);
                name.requestFocus();
                return;
            } else if (name.getText().length() > 8) {
                JOptionPane.showMessageDialog(this, "Your character's name will be shortened to 8 characters.", "Notice", JOptionPane.ERROR_MESSAGE);
                name.setText(name.getText().substring(0, 8));
                name.requestFocus();
                return;
            } else if (lastname.getText().length() > 30) {
                JOptionPane.showMessageDialog(this, "Your character's last name will be shortened to 30 characters.", "Notice", JOptionPane.ERROR_MESSAGE);
                lastname.setText(lastname.getText().substring(0, 30));
                lastname.requestFocus();
                return;
            }
            //points used check - don't enforce total usage anymore
            if (levelpoints > 0 || hsmpoints > 0 || statpoints > 0 || defensepoints > 0 || itempoints > 0 || abilitypoints > 0) {
                int returnval = JOptionPane.showConfirmDialog(this, "You have points remaining. Create Anyway?", "Notice", JOptionPane.YES_NO_OPTION);
                if (returnval != JOptionPane.YES_OPTION) return;
            }
            
            //create the new hero
            dm.hero[0] = dm.new Hero(picname, name.getText(), lastname.getText(), Integer.parseInt(stat[0].getText()), Integer.parseInt(stat[1].getText()), Integer.parseInt(stat[2].getText()),
                Integer.parseInt(stat[3].getText()), Integer.parseInt(stat[4].getText()), Integer.parseInt(stat[5].getText()), Integer.parseInt(stat[6].getText()),
                Integer.parseInt(stat[7].getText()), Integer.parseInt(stat[8].getText()), Integer.parseInt(stat[9].getText()), Integer.parseInt(stat[10].getText()),
                Integer.parseInt(stat[11].getText()), Integer.parseInt(stat[12].getText()), Integer.parseInt(stat[13].getText()));
            //add any abilities
            if (abilitychoose != null) {
                ArrayList abils = new ArrayList();
                for (int i = 0; i < abilitychoose.length; i++) {
                    if (i < abilityauto) abils.add(abilitychoose[i]);
                    else if (abilitybutton[i - abilityauto].isSelected()) abils.add(abilitychoose[i]);
                }
                if (abils.size() > 0) {
                    dm.hero[0].abilities = new SpecialAbility[abils.size()];
                    for (int i = 0; i < abils.size(); i++) dm.hero[0].abilities[i] = (SpecialAbility) abils.get(i);
                }
            }
            //add any items
            if (itemchoose != null) {
                ArrayList items = new ArrayList();
                for (int i = 0; i < itemchoose.length; i++) {
                    if (itembutton[i].isSelected()) items.add(itemchoose[i]);
                }
                if (items.size() > 0) {
                    for (int i = 0; i < items.size() && i < 16; i++) dm.hero[0].pack[i] = (Item) items.get(i);
                }
            }
            if (e.getActionCommand().endsWith("Reincarnate")) {
                dm.hero[0].flevel = 0;
                dm.hero[0].nlevel = 0;
                dm.hero[0].wlevel = 0;
                dm.hero[0].plevel = 0;
            }
            //start dm
            removeAll();
            add("Center", Title.loading);
            validate();
            update(getGraphics());
            dm.loadMap(gamefile);
            if (!dmstarted) {
                dm.startNew();//adds hero to hpanel, makes spellsheet and weaponsheet, etc.
                dm.start();
            } else if (!finishnewgame) {
                synchronized (dm.CREATELOCK) {
                    dm.CREATEFLAG = true;
                    dm.CREATELOCK.notify();
                }
            }
            dm.addKeyListener(dm.dmove);
            dm.setContentPane(dm.imagePane);
            dm.showCenter(dm.dview);
            if (finishnewgame) dm.finishNewGame();
        } else if (e.getActionCommand().equals("Reset Levels")) {
            for (int i = 0; i < 12; i++) {
                stat[i].setText("" + (Integer.parseInt(stat[i].getText()) - levelboost[i]));
                levelboost[i] = 0;
            }
            levelpoints = MAXLEVELPOINTS;
            levelpointslabel.setText("" + levelpoints);
        } else if (e.getActionCommand().equals("Reset All")) {
            for (int i = 0; i < 14; i++) {
                stat[i].setText("" + statnum[i]);
                if (i < 12) levelboost[i] = 0;
            }
            levelpoints = MAXLEVELPOINTS;
            hsmpoints = MAXHSMPOINTS;
            statpoints = MAXSTATPOINTS;
            defensepoints = MAXDEFENSEPOINTS;
            levelpointslabel.setText("" + levelpoints);
            hsmpointslabel.setText("" + hsmpoints);
            statpointslabel.setText("" + statpoints);
            defensepointslabel.setText("" + defensepoints);
        } else if (e.getActionCommand().startsWith("up")) {
            int index = Integer.parseInt(e.getActionCommand().substring(2));
            int currentstat = Integer.parseInt(stat[index].getText());
            if (index < 4) {
                //levels
                if (levelpoints == 0 || levelboost[index] > 15) return;
                levelpoints--;
                levelpointslabel.setText("" + levelpoints);
                levelboost[index]++;
                stat[index].setText("" + (currentstat + 1));
                int statchange;
                switch (index) {
                    case 0: //fighter
                        statchange = randGen.nextInt(3) + 2;
                        levelboost[7] += statchange;
                        stat[7].setText("" + (Integer.parseInt(stat[7].getText()) + statchange)); //strength
                        statchange = randGen.nextInt(2) + 1;
                        levelboost[8] += statchange;
                        stat[8].setText("" + (Integer.parseInt(stat[8].getText()) + statchange)); //dexterity
                        statchange = randGen.nextInt(3) + 1;
                        levelboost[9] += statchange;
                        stat[9].setText("" + (Integer.parseInt(stat[9].getText()) + statchange)); //vitality
                        statchange = randGen.nextInt() % 5 + Integer.parseInt(stat[9].getText()) / 4;
                        if (statchange < 1) statchange = 1;
                        levelboost[4] += statchange;
                        stat[4].setText("" + (Integer.parseInt(stat[4].getText()) + statchange)); //health
                        statchange = randGen.nextInt() % 5 + Integer.parseInt(stat[9].getText()) / 4;
                        if (statchange < 1) statchange = 1;
                        levelboost[5] += statchange;
                        stat[5].setText("" + (Integer.parseInt(stat[5].getText()) + statchange)); //stamina
                        break;
                    case 1: //ninja
                        statchange = randGen.nextInt(3) + 1;
                        levelboost[7] += statchange;
                        stat[7].setText("" + (Integer.parseInt(stat[7].getText()) + statchange)); //strength
                        statchange = randGen.nextInt(3) + 2;
                        levelboost[8] += statchange;
                        stat[8].setText("" + (Integer.parseInt(stat[8].getText()) + statchange)); //dexterity
                        statchange = randGen.nextInt(3) + 1;
                        levelboost[9] += statchange;
                        stat[9].setText("" + (Integer.parseInt(stat[9].getText()) + statchange)); //vitality
                        statchange = randGen.nextInt() % 5 + Integer.parseInt(stat[9].getText()) / 5;
                        if (statchange < 1) statchange = 1;
                        levelboost[4] += statchange;
                        stat[4].setText("" + (Integer.parseInt(stat[4].getText()) + statchange)); //health
                        statchange = randGen.nextInt() % 5 + Integer.parseInt(stat[9].getText()) / 5;
                        if (statchange < 1) statchange = 1;
                        levelboost[5] += statchange;
                        stat[5].setText("" + (Integer.parseInt(stat[5].getText()) + statchange)); //stamina
                        break;
                    case 2: //wizard
                        statchange = randGen.nextInt(3) + 2;
                        levelboost[10] += statchange;
                        stat[10].setText("" + (Integer.parseInt(stat[10].getText()) + statchange)); //intelligence
                        statchange = randGen.nextInt() % 5 + Integer.parseInt(stat[9].getText()) / 8;
                        if (statchange < 1) statchange = 1;
                        levelboost[4] += statchange;
                        stat[4].setText("" + (Integer.parseInt(stat[4].getText()) + statchange)); //health
                        statchange = randGen.nextInt() % 5 + Integer.parseInt(stat[9].getText()) / 8;
                        if (statchange < 1) statchange = 1;
                        levelboost[5] += statchange;
                        stat[5].setText("" + (Integer.parseInt(stat[5].getText()) + statchange)); //stamina
                        //statchange = randGen.nextInt()%5+Integer.parseInt(stat[10].getText())/4;
                        statchange = 9 - levelboost[index];
                        if (statchange < 4) statchange = 4;
                        statchange = randGen.nextInt(5) + Integer.parseInt(stat[10].getText()) / statchange;
                        levelboost[6] += statchange;
                        stat[6].setText("" + (Integer.parseInt(stat[6].getText()) + statchange)); //mana
                        break;
                    case 3: //priest
                        statchange = randGen.nextInt(3) + 2;
                        levelboost[11] += statchange;
                        stat[11].setText("" + (Integer.parseInt(stat[11].getText()) + statchange)); //wisdom
                        statchange = randGen.nextInt() % 5 + Integer.parseInt(stat[9].getText()) / 8;
                        if (statchange < 1) statchange = 1;
                        levelboost[4] += statchange;
                        stat[4].setText("" + (Integer.parseInt(stat[4].getText()) + statchange)); //health
                        statchange = randGen.nextInt() % 5 + Integer.parseInt(stat[9].getText()) / 8;
                        if (statchange < 1) statchange = 1;
                        levelboost[5] += statchange;
                        stat[5].setText("" + (Integer.parseInt(stat[5].getText()) + statchange)); //stamina
                        //statchange = randGen.nextInt()%5+Integer.parseInt(stat[11].getText())/4;
                        //statchange = randGen.nextInt(3)+3+Integer.parseInt(stat[11].getText())/10*((levelboost[index]-1)*5/6);
                        statchange = 9 - levelboost[index];
                        if (statchange < 4) statchange = 4;
                        statchange = randGen.nextInt(5) + Integer.parseInt(stat[11].getText()) / statchange;
                        levelboost[6] += statchange;
                        stat[6].setText("" + (Integer.parseInt(stat[6].getText()) + statchange)); //mana
                        break;
                }
            } else if (index < 7) {
                if (hsmpoints > 0) {
                    //health,stamina,mana
                    hsmpoints--;
                    stat[index].setText("" + (currentstat + 1));
                    hsmpointslabel.setText("" + hsmpoints);
                }
            } else if (index < 12) {
                if (statpoints > 0) {
                    //stats
                    statpoints--;
                    stat[index].setText("" + (currentstat + 1));
                    statpointslabel.setText("" + statpoints);
                }
            } else if (defensepoints > 0) {
                //defense/magicresist
                defensepoints--;
                stat[index].setText("" + (currentstat + 1));
                defensepointslabel.setText("" + defensepoints);
            }
        } else if (e.getActionCommand().startsWith("down")) {
            int index = Integer.parseInt(e.getActionCommand().substring(4)) + 4;
            int currentstat = Integer.parseInt(stat[index].getText());
            if (index < 7) {
                //health,stamina,mana
                if (hsmpoints == MAXHSMPOINTS || currentstat == statnum[index] + levelboost[index]) return;
                hsmpoints++;
                hsmpointslabel.setText("" + hsmpoints);
            } else if (index < 12) {
                //stats
                if (statpoints == MAXSTATPOINTS || currentstat == statnum[index] + levelboost[index]) return;
                statpoints++;
                statpointslabel.setText("" + statpoints);
            } else {
                //defense/magicresist
                if (defensepoints == MAXDEFENSEPOINTS || currentstat == statnum[index]) return;
                defensepoints++;
                defensepointslabel.setText("" + defensepoints);
            }
            stat[index].setText("" + (currentstat - 1));
        } else if (e.getActionCommand().startsWith("fup")) {
            int index = Integer.parseInt(e.getActionCommand().substring(3));
            int currentstat = Integer.parseInt(stat[index].getText());
            if (index < 7) {
                if (hsmpoints > 4) {
                    stat[index].setText("" + (currentstat + 5));
                    hsmpoints -= 5;
                    hsmpointslabel.setText("" + hsmpoints);
                } else {
                    stat[index].setText("" + (currentstat + hsmpoints));
                    hsmpoints = 0;
                    hsmpointslabel.setText("" + hsmpoints);
                }
            } else {
                if (statpoints > 4) {
                    stat[index].setText("" + (currentstat + 5));
                    statpoints -= 5;
                    statpointslabel.setText("" + statpoints);
                } else {
                    stat[index].setText("" + (currentstat + statpoints));
                    statpoints = 0;
                    statpointslabel.setText("" + statpoints);
                }
            }
        } else if (e.getActionCommand().startsWith("fdown")) {
            int index = Integer.parseInt(e.getActionCommand().substring(5));
            int currentstat = Integer.parseInt(stat[index].getText());
            if (index < 7) {
                if (hsmpoints + 5 <= MAXHSMPOINTS) {
                    if ((currentstat - statnum[index] - levelboost[index]) > 4) {
                        hsmpoints += 5;
                        stat[index].setText("" + (currentstat - 5));
                        hsmpointslabel.setText("" + hsmpoints);
                    } else {
                        hsmpoints += (currentstat - statnum[index] - levelboost[index]);
                        stat[index].setText("" + (statnum[index] + levelboost[index]));
                        hsmpointslabel.setText("" + hsmpoints);
                    }
                } else {
                    if ((currentstat - statnum[index] - levelboost[index]) >= (MAXHSMPOINTS - hsmpoints)) {
                        stat[index].setText("" + (currentstat - (MAXHSMPOINTS - hsmpoints)));
                        hsmpoints = MAXHSMPOINTS;
                        hsmpointslabel.setText("" + hsmpoints);
                    } else {
                        hsmpoints += (currentstat - statnum[index] - levelboost[index]);
                        stat[index].setText("" + (statnum[index] + levelboost[index]));
                        hsmpointslabel.setText("" + hsmpoints);
                    }
                }
            } else {
                if (statpoints + 5 <= MAXSTATPOINTS) {
                    if ((currentstat - statnum[index] - levelboost[index]) > 4) {
                        statpoints += 5;
                        stat[index].setText("" + (currentstat - 5));
                        statpointslabel.setText("" + statpoints);
                    } else {
                        statpoints += (currentstat - statnum[index] - levelboost[index]);
                        stat[index].setText("" + (statnum[index] + levelboost[index]));
                        statpointslabel.setText("" + statpoints);
                    }
                } else {
                    if ((currentstat - statnum[index] - levelboost[index]) >= (MAXSTATPOINTS - statpoints)) {
                        stat[index].setText("" + (currentstat - (MAXSTATPOINTS - statpoints)));
                        statpoints = MAXSTATPOINTS;
                        statpointslabel.setText("" + statpoints);
                    } else {
                        statpoints += (currentstat - statnum[index] - levelboost[index]);
                        stat[index].setText("" + (statnum[index] + levelboost[index]));
                        statpointslabel.setText("" + statpoints);
                    }
                }
            }
        } else if (e.getActionCommand().equals(">")) {
            picnumber--;
            if (picnumber < 0) picnumber = portraitfiles.length - 1;
            picname = portraitfiles[picnumber].getName();
            if (!picdirectory.equals("")) picname = picdirectory + picname;
            pic.setImage(dm.tk.getImage("Heroes" + File.separator + picname));
            portrait.repaint();
        } else if (e.getActionCommand().equals("<")) {
            picnumber++;
            if (picnumber == portraitfiles.length) picnumber = 0;
            picname = portraitfiles[picnumber].getName();
            if (!picdirectory.equals("")) picname = picdirectory + picname;
            pic.setImage(dm.tk.getImage("Heroes" + File.separator + picname));
            portrait.repaint();
        } else if (e.getActionCommand().equals("Browse")) {
            browser.show();
            String returnval = browser.getFile();
            if (returnval != null) {
                String tempstring = browser.getDirectory();
                int index = tempstring.indexOf("Heroes");
                if (index >= 0) {
                    picdirectory = tempstring.substring(index + 6);
                    if (picdirectory.length() == 1) picdirectory = "";
                    else picdirectory = picdirectory.substring(1);
                    picdirectory = picdirectory.replace('\\', '/');
                    picname = picdirectory + returnval;
                    picname = picname.replace('\\', '/');
                    pic.setImage(dm.tk.getImage("Heroes/" + picname));
                    portrait.repaint();
                    if (!browser.getDirectory().equals(portraitdirectory.getPath())) {
                        portraitdirectory = new File(browser.getDirectory());
                        portraitfiles = portraitdirectory.listFiles(this);
                        Arrays.sort(portraitfiles);
                    }
                    picnumber = 0;
                    boolean found = false;
                    String shortpicname = returnval;
                    while (!found && picnumber < portraitfiles.length) {
                        if (portraitfiles[picnumber].getName().equals(shortpicname)) found = true;
                        else picnumber++;
                    }
                    if (!found) picnumber = 0;//shouldn't ever happen
                }
            }
        } else if (e.getActionCommand().endsWith("...")) {
            remove(createpanel);
            add("Center", itabpan);
            validate();
            repaint();
        } else if (e.getActionCommand().equals("Back")) {
            remove(itabpan);
            add("Center", createpanel);
            validate();
            repaint();
        } else if (e.getActionCommand().equals("Create Character")) {
            removeAll();
            add("North", Box.createRigidArea(new Dimension(640, 65)));
            add("Center", createpanel);
            validate();
            update(getGraphics());
        } else if (e.getActionCommand().equals("No Starting Character")) {
            //start dm
            removeAll();
            add("Center", Title.loading);
            validate();
            update(getGraphics());
            //dm.doLayout();
            //dm.paint(dm.getGraphics());
            dm.loadMap(gamefile);
            if (!dmstarted) {
                dm.start();
            } else if (!finishnewgame) {
                synchronized (dm.CREATELOCK) {
                    dm.CREATEFLAG = true;
                    dm.CREATELOCK.notify();
                }
            }
            dm.addKeyListener(dm.dmove);
            dm.setContentPane(dm.imagePane);
            dm.showCenter(dm.dview);
            if (finishnewgame) dm.finishNewGame();
        } else if (e.getActionCommand().startsWith("i")) {
            int i = Integer.parseInt(e.getActionCommand().substring(1));
            if (!itembutton[i].isSelected()) itempoints++;
            else if (itempoints == 0) {
                itembutton[i].setSelected(false);
                return;
            } else itempoints--;
            itpointlabel.setText("Items Left: " + itempoints);
        } else if (e.getActionCommand().startsWith("a")) {
            int i = Integer.parseInt(e.getActionCommand().substring(1));
            if (!abilitybutton[i].isSelected()) abilitypoints++;
            else if (abilitypoints == 0) {
                abilitybutton[i].setSelected(false);
                return;
            } else abilitypoints--;
            abpointlabel.setText("Abilities Left: " + abilitypoints);
        }
    }
        
        /*
        public void setSpecials() {                        
                if (picname.endsWith("vaag.gif")) {
                        statnum[6]+=8; stat[6].setText(""+(Integer.parseInt(stat[6].getText())+8)); //mana
                        statnum[10]+=5; stat[10].setText(""+(Integer.parseInt(stat[10].getText())+5)); //intelligence
                        statnum[13]+=5; stat[13].setText(""+(Integer.parseInt(stat[13].getText())+5)); //magic resistance
                        //could set rate of food/water consumption here too
                }
        }
        */
    
    public boolean accept(File f, String n) {
        n = n.toLowerCase();
        if (n.endsWith(".gif") || n.endsWith(".jpg") || n.endsWith(".png")) return true;
        return false;
    }
}
