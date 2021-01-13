import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class LauncherWizard extends JDialog implements ActionListener {
    private int level, x, y, side, type;
    private MapData data;
    private Item item;
    
    private JToggleButton spellbutton, itembutton;
    private JPanel spellpanel, itempanel, cppanel;
    
    private JToggleButton[] sidebutton = new JToggleButton[4];
    
    private JComboBox spell, spellpower, style;
    private JLabel itemname;
    private JTextField shotpower, ammocount, ammocount2, shootrate, shootcount, castpower;
    private JToggleButton noprojend, isshooting;
    private JFrame frame;
    
    private static final String[] spells = {"Fireball", "Lightning", "Poison Bolt", "Ven Cloud", "Dispell Immaterial", "Arc Bolt", "Weakness", "Feeble Mind", "Slow", "Strip Defenses", "Silence", "Door Open"};
    //{ "Fireball","Lightning","Poison Bolt","Ven Cloud","Dispell Immaterial","Open Door" };
        
        
        /*
        public LauncherWizard(JFrame f, MapData data, int level, int x, int y) {
                super(f,"Launcher Wizard",true);
                frame = f;
                this.level = level;
                this.x = x;
                this.y = y;
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setSize(490,450);
                setLocationRelativeTo(f);
                Container cp = getContentPane();
        
                //side buttons
                JPanel sidepanel = new JPanel();
                sidepanel.setLayout(new GridLayout(2,2));
                sidepanel.setPreferredSize(new Dimension(140,50));
                sidepanel.setMaximumSize(new Dimension(140,50));
                ButtonGroup sidegrp = new ButtonGroup();
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
                sidebutton[2].setSelected(true);
                JPanel labelpanel = new JPanel();
                JLabel label = new JLabel("Launcher faces:");
                label.setHorizontalAlignment(JLabel.CENTER);
                labelpanel.add(label);
                
                //type buttons
                JPanel typepanel = new JPanel();
                typepanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                spellbutton = new JToggleButton("Spell");
                itembutton = new JToggleButton("Item");
                spellbutton.addActionListener(this);
                itembutton.addActionListener(this);
                ButtonGroup typegrp = new ButtonGroup();
                typegrp.add(spellbutton);
                typegrp.add(itembutton);
                typepanel.add(Box.createHorizontalStrut(40));
                typepanel.add(spellbutton);
                typepanel.add(itembutton);
                spellbutton.setSelected(true);
                
                //style list
                String[] styles = { "Both Holes","Left Hole","Right Hole","Random Hole" };
                style = new JComboBox(styles);
                style.setEditable(false);
                typepanel.add(Box.createHorizontalStrut(10));
                typepanel.add(style);
                
                //spell panel
                spellpanel = new JPanel();
                spellpanel.setPreferredSize(new Dimension(200,30));
                spell = new JComboBox(spells);
                spell.setEditable(false);
                spellpower = new JComboBox(ItemWizard.powers);
                spellpower.setEditable(false);
                spellpower.setSelectedIndex(0);
                spellpanel.add(spell);
                spellpanel.add(spellpower);
                
                //item panel
                item = new Item(267);
                itempanel = new JPanel();
                itempanel.setPreferredSize(new Dimension(200,30));
                JPanel upitempanel = new JPanel();
                itemname = new JLabel("Dagger");
                itemname.setPreferredSize(new Dimension(100,20));
                itemname.setHorizontalAlignment(JLabel.CENTER);
                JButton changebutton = new JButton("Change");
                changebutton.setFont(new Font("Times Roman",Font.BOLD,9));
                changebutton.setMargin(new Insets(0,5,0,5));
                changebutton.addActionListener(this);
                upitempanel.add(itemname);
                upitempanel.add(changebutton);
                shotpower = new JTextField("4",3);
                //itempanel.add(itemname);
                //itempanel.add(changebutton);
                itempanel.add(upitempanel);
                itempanel.add(new JLabel("Shot Power:"));
                itempanel.add(shotpower);
                itempanel.setVisible(false);

                //cast power
                castpower = new JTextField("60",4);
                cppanel = new JPanel();
                cppanel.add(new JLabel("Cast Power:"));
                cppanel.add(castpower);
                
                //bottom center panel
                JPanel bcpanel = new JPanel();
                Box bcbox = Box.createVerticalBox();
                bcbox.add(new JLabel("Current Number Of Shots (-1 For Infinite):"));
                ammocount = new JTextField("-1",4);
                JPanel bcpan1 = new JPanel();
                bcpan1.add(ammocount);
                bcbox.add(bcpan1);
                bcbox.add(new JLabel("Number Of Shots To Reset To (0 For No Reset):"));
                ammocount2 = new JTextField("0",4);
                JPanel bcpan5 = new JPanel();
                bcpan5.add(ammocount2);
                bcbox.add(bcpan5);
                bcbox.add(new JLabel("Shot Rate (0 If Not Continuous):"));
                shootrate = new JTextField("0",4);
                JPanel bcpan2 = new JPanel();
                bcpan2.add(shootrate);
                bcbox.add(bcpan2);
                bcbox.add(new JLabel("Current Shot Counter:"));
                shootcount = new JTextField("0",4);
                JPanel bcpan4 = new JPanel();
                bcpan4.add(shootcount);
                bcbox.add(bcpan4);
                noprojend = new JToggleButton("Projectiles Unending");
                //noprojend.addActionListener(this);
                JPanel bcpan6 = new JPanel();
                bcpan6.add(noprojend);
                bcbox.add(bcpan6); 
                isshooting = new JToggleButton("Is Shooting");
                //isshooting.addActionListener(this);
                JPanel bcpan3 = new JPanel();
                bcpan3.add(isshooting);
                bcbox.add(bcpan3); 
                bcpanel.add(bcbox);
                
                //center panel
                Box centerpanel = Box.createVerticalBox();
                centerpanel.add(spellpanel);
                centerpanel.add(itempanel);
                centerpanel.add(cppanel);
                centerpanel.add(bcpanel);
                
                //east panel
                Box east = Box.createVerticalBox();
                east.add(Box.createVerticalGlue());
                east.add(Box.createVerticalStrut(100));
                east.add(labelpanel);
                east.add(sidepanel);
                east.add(Box.createVerticalStrut(70));
                east.add(Box.createVerticalGlue());
                
                //bottom panel
                JPanel bottompanel = new JPanel();
                JButton done = new JButton("Done");
                JButton cancel = new JButton("Cancel");
                done.addActionListener(this);
                cancel.addActionListener(this);
                bottompanel.add(done);
                bottompanel.add(cancel);
                
                if (data.mapchar=='l') {
                        LauncherData ldata = (LauncherData)data;
                        if (ldata.type==1) {
                                //item = new Item(ldata.number);
                                item = ldata.it;
                                itemname.setText(item.name);
                                shotpower.setText(""+ldata.power);
                                itembutton.doClick();
                                if (item.isbomb) {
                                        castpower.setText(""+ldata.castpower);
                                        cppanel.setVisible(true);
                                }
                        }
                        else {
                                switch (ldata.spnumber) {
                                        case 44:
                                                spell.setSelectedIndex(0);
                                                break;
                                        case 335:
                                                spell.setSelectedIndex(1);
                                                break;
                                        case 51:
                                                spell.setSelectedIndex(2);
                                                break;
                                        case 31:
                                                spell.setSelectedIndex(3);
                                                break;
                                        case 52:
                                                spell.setSelectedIndex(4);
                                                break;
                                        case 642:
                                                spell.setSelectedIndex(5);
                                                break;
                                        case 461:
                                                spell.setSelectedIndex(6);
                                                break;
                                        case 363:
                                                spell.setSelectedIndex(7);
                                                break;
                                        case 362:
                                                spell.setSelectedIndex(8);
                                                break;
                                        case 664:
                                                spell.setSelectedIndex(9);
                                                break;
                                        case 523:
                                                spell.setSelectedIndex(10);
                                                break;
                                        case 6:
                                                spell.setSelectedIndex(11);
                                                break;
                                }
                                spellpower.setSelectedIndex(ldata.power);
                                castpower.setText(""+ldata.castpower);
                        }
                        if (ldata.shootrate>0) shootrate.setText(""+ldata.shootrate);
                        if (ldata.shootcounter>0) shootcount.setText(""+ldata.shootcounter);
                        if (ldata.ammocount>-1) ammocount.setText(""+ldata.ammocount);
                        if (ldata.ammocount2>0) ammocount2.setText(""+ldata.ammocount2);
                        style.setSelectedIndex(ldata.style);
                        isshooting.setSelected(ldata.isShooting);
                        noprojend.setSelected(ldata.noprojend);
                        sidebutton[(ldata.side+2)%4].doClick();
                }
                
                cp.add("North",typepanel);
                cp.add("Center",centerpanel);
                cp.add("South",bottompanel);
                cp.add("East",east);
                show();
        }
        */
    
    public LauncherWizard(JFrame f) {
        super(f, "Launcher Wizard", true);
        frame = f;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(490, 450);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //side buttons
        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new GridLayout(2, 2));
        sidepanel.setPreferredSize(new Dimension(140, 50));
        sidepanel.setMaximumSize(new Dimension(140, 50));
        ButtonGroup sidegrp = new ButtonGroup();
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
        sidebutton[2].setSelected(true);
        JPanel labelpanel = new JPanel();
        JLabel label = new JLabel("Launcher faces:");
        label.setHorizontalAlignment(JLabel.CENTER);
        labelpanel.add(label);
        
        //type buttons
        JPanel typepanel = new JPanel();
        typepanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        spellbutton = new JToggleButton("Spell");
        itembutton = new JToggleButton("Item");
        spellbutton.addActionListener(this);
        itembutton.addActionListener(this);
        ButtonGroup typegrp = new ButtonGroup();
        typegrp.add(spellbutton);
        typegrp.add(itembutton);
        typepanel.add(Box.createHorizontalStrut(40));
        typepanel.add(spellbutton);
        typepanel.add(itembutton);
        spellbutton.setSelected(true);
        
        //style list
        String[] styles = {"Both Holes", "Left Hole", "Right Hole", "Random Hole"};
        style = new JComboBox(styles);
        style.setEditable(false);
        typepanel.add(Box.createHorizontalStrut(10));
        typepanel.add(style);
        
        //spell panel
        spellpanel = new JPanel();
        spellpanel.setPreferredSize(new Dimension(200, 30));
        spell = new JComboBox(spells);
        spell.setEditable(false);
        spellpower = new JComboBox(ItemWizard.powers);
        spellpower.setEditable(false);
        spellpower.setSelectedIndex(0);
        spellpanel.add(spell);
        spellpanel.add(spellpower);
        
        //item panel
        item = new Item(267);
        itempanel = new JPanel();
        itempanel.setPreferredSize(new Dimension(200, 30));
        JPanel upitempanel = new JPanel();
        itemname = new JLabel("Dagger");
        itemname.setPreferredSize(new Dimension(100, 20));
        itemname.setHorizontalAlignment(JLabel.CENTER);
        JButton changebutton = new JButton("Change");
        changebutton.setFont(new Font("Times Roman", Font.BOLD, 9));
        changebutton.setMargin(new Insets(0, 5, 0, 5));
        changebutton.addActionListener(this);
        upitempanel.add(itemname);
        upitempanel.add(changebutton);
        shotpower = new JTextField("4", 3);
        itempanel.add(upitempanel);
        itempanel.add(new JLabel("Shot Power:"));
        itempanel.add(shotpower);
        itempanel.setVisible(false);
        
        //cast power
        castpower = new JTextField("60", 4);
        cppanel = new JPanel();
        cppanel.add(new JLabel("Cast Power:"));
        cppanel.add(castpower);
        
        //bottom center panel
        JPanel bcpanel = new JPanel();
        Box bcbox = Box.createVerticalBox();
        bcbox.add(new JLabel("Current Number Of Shots (-1 For Infinite):"));
        ammocount = new JTextField("-1", 4);
        JPanel bcpan1 = new JPanel();
        bcpan1.add(ammocount);
        bcbox.add(bcpan1);
        bcbox.add(new JLabel("Number Of Shots To Reset To (0 For No Reset):"));
        ammocount2 = new JTextField("0", 4);
        JPanel bcpan5 = new JPanel();
        bcpan5.add(ammocount2);
        bcbox.add(bcpan5);
        bcbox.add(new JLabel("Shot Rate (0 If Not Continuous):"));
        shootrate = new JTextField("0", 4);
        JPanel bcpan2 = new JPanel();
        bcpan2.add(shootrate);
        bcbox.add(bcpan2);
        bcbox.add(new JLabel("Current Shot Counter:"));
        shootcount = new JTextField("0", 4);
        JPanel bcpan4 = new JPanel();
        bcpan4.add(shootcount);
        bcbox.add(bcpan4);
        noprojend = new JToggleButton("Projectiles Unending");
        //noprojend.addActionListener(this);
        JPanel bcpan6 = new JPanel();
        bcpan6.add(noprojend);
        bcbox.add(bcpan6);
        isshooting = new JToggleButton("Is Shooting");
        //isshooting.addActionListener(this);
        JPanel bcpan3 = new JPanel();
        bcpan3.add(isshooting);
        bcbox.add(bcpan3);
        bcpanel.add(bcbox);
        
        //center panel
        Box centerpanel = Box.createVerticalBox();
        centerpanel.add(spellpanel);
        centerpanel.add(itempanel);
        centerpanel.add(cppanel);
        centerpanel.add(bcpanel);
        
        //east panel
        Box east = Box.createVerticalBox();
        east.add(Box.createVerticalGlue());
        east.add(Box.createVerticalStrut(100));
        east.add(labelpanel);
        east.add(sidepanel);
        east.add(Box.createVerticalStrut(70));
        east.add(Box.createVerticalGlue());
        
        //bottom panel
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        cp.add("North", typepanel);
        cp.add("Center", centerpanel);
        cp.add("South", bottompanel);
        cp.add("East", east);
        
        dispose();
    }
    
    public void setData(MapData data, int level, int x, int y) {
        this.level = level;
        this.x = x;
        this.y = y;
        if (data.mapchar == 'l') {
            LauncherData ldata = (LauncherData) data;
            if (ldata.type == 1) {
                item = ldata.it;
                itemname.setText(item.name);
                shotpower.setText("" + ldata.power);
                itembutton.doClick();
                if (item.isbomb) {
                    castpower.setText("" + ldata.castpower);
                    cppanel.setVisible(true);
                } else cppanel.setVisible(false);
            } else {
                item = Item.createCopy(item);
                switch (ldata.spnumber) {
                    case 44:
                        spell.setSelectedIndex(0);
                        break;
                    case 335:
                        spell.setSelectedIndex(1);
                        break;
                    case 51:
                        spell.setSelectedIndex(2);
                        break;
                    case 31:
                        spell.setSelectedIndex(3);
                        break;
                    case 52:
                        spell.setSelectedIndex(4);
                        break;
                    case 642:
                        spell.setSelectedIndex(5);
                        break;
                    case 461:
                        spell.setSelectedIndex(6);
                        break;
                    case 363:
                        spell.setSelectedIndex(7);
                        break;
                    case 362:
                        spell.setSelectedIndex(8);
                        break;
                    case 664:
                        spell.setSelectedIndex(9);
                        break;
                    case 523:
                        spell.setSelectedIndex(10);
                        break;
                    case 6:
                        spell.setSelectedIndex(11);
                        break;
                }
                spellpower.setSelectedIndex(ldata.power);
                castpower.setText("" + ldata.castpower);
                spellbutton.doClick();
            }
            shootrate.setText("" + ldata.shootrate);
            shootcount.setText("" + ldata.shootcounter);
            ammocount.setText("" + ldata.ammocount);
            ammocount2.setText("" + ldata.ammocount2);
            style.setSelectedIndex(ldata.style);
            isshooting.setSelected(ldata.isShooting);
            noprojend.setSelected(ldata.noprojend);
            sidebutton[(ldata.side + 2) % 4].doClick();
        } else item = Item.createCopy(item);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Spell")) {
            type = 0;
            itempanel.setVisible(false);
            spellpanel.setVisible(true);
            cppanel.setVisible(true);
        } else if (e.getActionCommand().equals("Item")) {
            type = 1;
            spellpanel.setVisible(false);
            itempanel.setVisible(true);
            if (item.isbomb) cppanel.setVisible(true);
            else cppanel.setVisible(false);
        } else if (e.getActionCommand().equals("Change")) {
            //Item tempitem = (new ItemWizard(frame,"Change Launcher Item",item)).getItem();
            DMEditor.itemwizard.setTitle("Item Wizard - Change Launcher Item");
            DMEditor.itemwizard.setItem(item);
            Item tempitem = DMEditor.itemwizard.getItem();
            if (tempitem != null) {
                item = tempitem;
                itemname.setText(item.name);
                if (item.isbomb) cppanel.setVisible(true);
                else cppanel.setVisible(false);
            }
        } else if (e.getActionCommand().equals("North")) {
            side = 2;
        } else if (e.getActionCommand().equals("South")) {
            side = 0;
        } else if (e.getActionCommand().equals("East")) {
            side = 1;
        } else if (e.getActionCommand().equals("West")) {
            side = 3;
        } else if (e.getActionCommand().equals("Done")) {
            int number = 0, power = 0;
            if (type == 0) { //spell
                power = spellpower.getSelectedIndex();
                switch (spell.getSelectedIndex()) {
                    case 0: //fireball
                        number = 44;
                        break;
                    case 1: //lightning
                        number = 335;
                        break;
                    case 2: //poison bolt
                        number = 51;
                        break;
                    case 3: //ven cloud
                        number = 31;
                        break;
                    case 4: //dispell immaterial
                        number = 52;
                        break;
                    case 5: //arc bolt
                        number = 642;
                        break;
                    case 6: //weakness
                        number = 461;
                        break;
                    case 7: //feeble mind
                        number = 363;
                        break;
                    case 8: //slow
                        number = 362;
                        break;
                    case 9: //strip defenses
                        number = 664;
                        break;
                    case 10://silence
                        number = 523;
                        break;
                    case 11://door open
                        number = 6;
                        break;
                }
            } else {
                power = Integer.parseInt(shotpower.getText());
                //number = item.number;
                //if (number<10 && number!=7) number=267; //prevent some, but could still be unrealistic (torso plates for example)
            }
            //data = new LauncherData(level,x,y,side,type,number,power,style.getSelectedIndex(),noprojend.isSelected(),Integer.parseInt(shootrate.getText()),Integer.parseInt(ammocount.getText()),Integer.parseInt(ammocount2.getText()),Integer.parseInt(shootcount.getText()),isshooting.isSelected());
            int cp = 0;
            if (castpower.isVisible()) cp = Integer.parseInt(castpower.getText());
            data = new LauncherData(level, x, y, side, type, power, cp, style.getSelectedIndex(), noprojend.isSelected(), Integer.parseInt(shootrate.getText()), Integer.parseInt(ammocount.getText()), Integer.parseInt(ammocount2.getText()), Integer.parseInt(shootcount.getText()), isshooting.isSelected());
            if (type == 0) ((LauncherData) data).spnumber = number;
            else ((LauncherData) data).it = item;
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        }
    }
    
    public MapData getData() {
        return data;
    }
}