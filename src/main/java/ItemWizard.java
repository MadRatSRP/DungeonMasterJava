import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.Vector;

class ItemWizard extends JDialog implements ActionListener, MouseListener {
    
    //weapons:
    static final String[] sworditems = {"Falchion", "Sword", "Sabre", "Samurai Sword", "Dragon Tounge", "Rapier", "Delta", "Diamond Edge", "Triashka", "The Inquisitor", "Dragon Fang", "Darkwing Cutter", "Vorpal Blade", "Ven Blade", "Bolt Blade", "Fury", "Ra Blade", "Sar Sword", "Stormbringer"};
    static final int[] swordnums = {200, 201, 202, 203, 216, 204, 205, 206, 217, 207, 208, 218, 209, 210, 211, 212, 213, 214, 215};
    
    static final String[] axeitems = {"Axe", "Hardcleave", "Executioner"};
    static final int[] axenums = {221, 222, 223};
    
    static final String[] bluntitems = {"Club", "Stone Club", "Mace", "Mace of Order", "Morningstar"};
    static final int[] bluntnums = {226, 227, 228, 229, 230};
    
    static final String[] staffitems = {"Wand", "Teowand", "Staff", "Staff of Claws", "Yew Staff", "Staff of Manar", "Staff of Irra", "Snake Staff", "Cross of Neta", "Dragon Spit", "Sceptre of Lyf", "The Conduit", "Serpent Staff", "Deth Staff", "Staff of Decay", "The Firestaff", "The Firestaff+"};
    static final int[] staffnums = {236, 237, 238, 239, 240, 241, 242, 243, 250, 244, 245, 246, 247, 252, 251, 248, 249};
    
    static final String[] missleitems = {"Sling", "Bow", "Claw Bow", "Crossbow", "Speedbow", "Rock", "Dagger", "Poison Dart", "Throwing Star", "Arrow", "Slayer Arrow"};
    static final int[] misslenums = {256, 257, 258, 259, 260, 266, 267, 268, 269, 270, 271};
    
    static final String[] otherwepitems = {"Blue Box", "Blue Box (All)", "Green Box", "Green Box (All)", "Eye of Time", "Horn of Fear", "Storm Ring", "Flamitt"};
    static final int[] otherwepnums = {276, 286, 277, 287, 278, 279, 280, 281};
    
    //armor:
    static final String[] neckitems = {"Cape", "Cloak of Night", "Choker", "Illumlet", "Moonstone", "Jewel Symal", "Ekkhard Cross", "Gem of Ages", "Pendant Feral", "Symbol of Ra", "Symbol of Sar", "The Hellion"};
    static final int[] necknums = {86, 87, 88, 89, 90, 91, 92, 93, 94, 96, 97, 95};
    
    static final String[] shielditems = {"Hide Shield", "Buckler", "Small Shield", "Wooden Shield", "Large Shield", "Lyte Shield", "Sar Shield", "Dragon Shield", "Ra Shield"};
    static final int[] shieldnums = {101, 102, 103, 104, 105, 106, 107, 108, 109};
    
    static final String[] headitems = {"Berserker Helm", "Basinet", "Helmet", "Casque 'n Coif", "Armet", "Dexhelm", "Lyte Helm", "Sar Helm", "Dragon Helm", "Ra Helm", "Calista", "Crown of Nerra", "Sar Circlet", "Ra Circlet", "Executioner's Hood", "Elven Circlet"};
    static final int[] headnums = {118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 116, 117, 128, 129, 130, 131};
    
    static final String[] torsoitems = {"Halter", "Robe", "Fine Robe", "Ghi", "Leather Jerkin", "Elven Dublet", "Mail Aketon", "Torso Plate", "Mithril Aketon", "Lyte Plate", "Sar Plate", "Dragon Plate", "Ra Plate", "Sar Robe", "Ra Robe"};
    static final int[] torsonums = {136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150};
    
    static final String[] legitems = {"Barbarian Hide", "Robe", "Fine Robe", "Ghi Trousers", "Leather Pants", "Elven Huke", "Leg Mail", "Leg Plate", "Mithril Leg", "Lyte Poleyn", "Sar Poleyn", "Dragon Poleyn", "Ra Poleyn", "Powertowers", "Sar Robe", "Ra Robe"};
    static final int[] legnums = {156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171};
    
    static final String[] feetitems = {"Sandals", "Leather Boots", "Black Boots", "Elven Boots", "Mail Hosen", "Foot Plate", "Mithril Hosen", "Lyte Greave", "Sar Greave", "Dragon Greave", "Ra Greave", "Boots of Speed"};
    static final int[] feetnums = {176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187};
    
    //other:
    static final String[] keyitems = {"Iron Key", "Key of B", "Square Key", "Solid Key", "Cross Key", "Skeleton Key", "Onyx Key", "Tourquoise Key", "Gold Key", "Master Key", "Ra Key", "Winged Key", "Emerald Key", "Ruby Key", "Sapphire Key", "Topaz Key"};
    static final int[] keynums = {31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46};
    
    static final String[] coingemitems = {"Gold Coin", "Silver Coin", "Copper Coin", "Gor Coin", "Blue Gem", "Green Gem", "Orange Gem", "Sar Crystal", "Ra Crystal"};
    static final int[] coingemnums = {51, 52, 53, 54, 55, 56, 57, 58, 59};
    
    static final String[] fooditems = {"Worm Round", "Apple", "Corn", "Bread", "Screamer Slice", "Cheese", "Drumstick", "Shank", "Dragon Steak", "Good Berries"};
    static final int[] foodnums = {61, 62, 63, 64, 65, 66, 67, 68, 69, 70};
    
    static final String[] potionitems = {"Health Potion", "Stamina Potion", "Mana Potion", "Strength Potion", "Dexterity Potion", "Vitality Potion", "Intelligence Potion", "Wisdom Potion", "Shield Potion", "Resistance Potion", "Anti-Venom", "Anti-Silence", "Remove Curse", "Ful Bomb", "Ven Bomb", "Bolt Bomb"};
    static final int[] potionnums = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 25, 21, 22, 23};
    
    static final String[] miscitems = {"Flask", "Water Flask", "Waterskin (Full)", "Waterskin (Almost Full)", "Waterskin (Almost Empty)", "Waterskin (Empty)", "Torch", "Scroll", "Rope", "Grapple", "Compass", "Lock Picks", "Ashes", "Bones", "Boulder", "Magnifier", "Mirror of Dawn", "Corbamite", "Corbum Ore", "Zo Kath Ra", "Stick", "Fire Rune", "Water Rune", "Earth Rune", "Wind Rune", "Rabbit's Foot"};
    static final int[] miscnums = {7, 72, 731, 732, 733, 734, 9, 4, 81, 85, 8, 71, 74, 75, 76, 77, 78, 79, 82, 80, 83, 282, 283, 284, 285, 84};
    
    //static final int chestnum = 5; //why was this here?
    
    static Vector customitems = new Vector(); //customitems list to be saved in a file, loaded when dmeditor starts, saved when it quits
    
    //static Item LASTITEM = new Item(200);
    int chestindex = -1, citemindex = -1;
    int oldnumber = 200;
    boolean nochest;
    
    JToggleButton predefinedbutton, custombutton, wepbutton, armbutton, otherbutton;
    JPanel typepanel, centerpanel, ccenter;
    
    JPanel chestpanel;
    JList chestlist;
    int chestcount = 0;
    Vector chestcontents = new Vector(6);
    Vector contentnames = new Vector(6);
    JScrollPane itempane, citempane;
    
    JPanel scrollpanel;
    JTextField firstline, secondline, thirdline, fourthline, fifthline;
    
    JComboBox potionbox;
    static final String[] powers = {"LO", "UM", "ON", "EE", "PAL", "MON"};
    static Random randGen = new Random();
    
    int TYPE = 0, SUBTYPE = 0;
    private int number = swordnums[0];
    private Item item = new Item(200);
    JList itemlist, citemlist;
    JToggleButton[] subtypebut;
    JButton editprebut;
    Container cp;
    JFrame frame;
    
    public ItemWizard(JFrame f) {
        this(f, false);
    }
    
    public ItemWizard(JFrame f, boolean nochest) {
        super(f, "Item Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 490);
        setLocationRelativeTo(f);
        cp = getContentPane();
        frame = f;
        this.nochest = nochest;
        
        //the main list, initially showing swords
        itemlist = new JList(sworditems);
        itemlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemlist.setPreferredSize(new Dimension(220, sworditems.length * 19));
        //itemlist.setPreferredSize(new Dimension(220,420));
        itemlist.setSelectedIndex(0);
        itemlist.addMouseListener(this);
        
        //allow default and custom items
        predefinedbutton = new JToggleButton("Predefined");
        custombutton = new JToggleButton("Custom");
        predefinedbutton.addActionListener(this);
        custombutton.addActionListener(this);
        ButtonGroup bigtypegrp = new ButtonGroup();
        bigtypegrp.add(predefinedbutton);
        bigtypegrp.add(custombutton);
        JPanel bigtypepanel = new JPanel();
        bigtypepanel.add(predefinedbutton);
        bigtypepanel.add(custombutton);
        predefinedbutton.setSelected(true);
        
        //control what subtypes are:
        wepbutton = new JToggleButton("Weapon");
        armbutton = new JToggleButton("Armor");
        otherbutton = new JToggleButton("Other");
        wepbutton.addActionListener(this);
        armbutton.addActionListener(this);
        otherbutton.addActionListener(this);
        ButtonGroup typegrp = new ButtonGroup();
        typegrp.add(wepbutton);
        typegrp.add(armbutton);
        typegrp.add(otherbutton);
        wepbutton.setSelected(true);
        typepanel = new JPanel();
        typepanel.setLayout(new GridLayout(1, 3, 2, 2));
        typepanel.add(wepbutton);
        typepanel.add(armbutton);
        typepanel.add(otherbutton);
        
        //put north panel together
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.add(bigtypepanel);
        north.add(typepanel);
        
        //control what items shown in list:
        subtypebut = new JToggleButton[6];
        subtypebut[0] = new JToggleButton("Sword");
        subtypebut[1] = new JToggleButton("Staff");
        subtypebut[2] = new JToggleButton("Axe");
        subtypebut[3] = new JToggleButton("Blunt");
        subtypebut[4] = new JToggleButton("Missle");
        subtypebut[5] = new JToggleButton("Other");
        subtypebut[5].setActionCommand("Other Weapon");
        ButtonGroup subtypegrp = new ButtonGroup();
        JPanel subtypepanel = new JPanel();
        subtypepanel.setLayout(new GridLayout(2, 3, 2, 2));
        for (int i = 0; i < 6; i++) {
            subtypebut[i].setPreferredSize(new Dimension(80, 30));
            subtypegrp.add(subtypebut[i]);
            subtypebut[i].addActionListener(this);
            subtypepanel.add(subtypebut[i]);
        }
        subtypebut[0].setSelected(true);
        
        //chest stuff
        chestpanel = new JPanel();
        if (!nochest) {
            chestlist = new JList();
            chestlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            //chestlist.setPreferredSize(new Dimension(220,228));
            chestlist.setVisibleRowCount(6);
            chestlist.addMouseListener(this);
            JButton additembut = new JButton("Add/Edit Item");
            JButton removeitembut = new JButton("Remove Item");
            additembut.setActionCommand("Add Item");
            additembut.addActionListener(this);
            removeitembut.addActionListener(this);
            Box chestbutbox = Box.createVerticalBox();
            chestbutbox.add(additembut);
            chestbutbox.add(removeitembut);
            JScrollPane chestpane = new JScrollPane(chestlist);
            chestpane.setPreferredSize(new Dimension(238, 118));
            chestpanel.add(chestpane);
            chestpanel.add(chestbutbox);
            chestpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Contents"));
            chestpanel.setVisible(false);
        }
        
        //scroll stuff
        scrollpanel = new JPanel();
        scrollpanel.setLayout(new BoxLayout(scrollpanel, BoxLayout.Y_AXIS));
        firstline = new JTextField(20);
        secondline = new JTextField(20);
        thirdline = new JTextField(20);
        fourthline = new JTextField(20);
        fifthline = new JTextField(20);
        //Font scrollfont = new Font("Courier",Font.PLAIN,12);
        Font scrollfont = ((DMEditor) frame).scrollfont;
                /*
                try {   
                        FileInputStream in = new FileInputStream("scrollfont.ttf");
                        scrollfont = Font.createFont(Font.TRUETYPE_FONT,in);
                        in.close();
                        scrollfont = scrollfont.deriveFont(Font.BOLD,12);
                } catch (Exception e) { 
                        e.printStackTrace();
                        scrollfont = new Font("Courier",Font.PLAIN,12);
                }
                */
        firstline.setFont(scrollfont);
        secondline.setFont(scrollfont);
        thirdline.setFont(scrollfont);
        fourthline.setFont(scrollfont);
        fifthline.setFont(scrollfont);
        //firstline.setColumns(20);
        //secondline.setColumns(20);
        //thirdline.setColumns(20);
        //fourthline.setColumns(20);
        //fifthline.setColumns(20);
        scrollpanel.add(firstline);
        scrollpanel.add(secondline);
        scrollpanel.add(thirdline);
        scrollpanel.add(fourthline);
        scrollpanel.add(fifthline);
        scrollpanel.setVisible(false);
        
        //potion stuff
        potionbox = new JComboBox(powers);
        potionbox.setEditable(false);
        potionbox.setVisible(false);
        
        //edit predefined item
        JPanel editprepan = new JPanel();
        editprebut = new JButton("Edit Predefined");
        editprebut.addActionListener(this);
        editprepan.add(editprebut);
        
        //custom item panel
        ccenter = new JPanel();
        citemlist = new JList(customitems);
        citemlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        citemlist.setPreferredSize(new Dimension(220, customitems.size() * 19));
        citemlist.addMouseListener(this);
        citempane = new JScrollPane(citemlist);
        citempane.setPreferredSize(new Dimension(238, 155));
        ccenter.add(citempane);
        JButton addeditbut = new JButton("Add/Edit Item");
        JButton deletebut = new JButton("Delete Item");
        addeditbut.addActionListener(this);
        deletebut.addActionListener(this);
        JPanel cbutpan = new JPanel();
        cbutpan.setLayout(new BoxLayout(cbutpan, BoxLayout.Y_AXIS));
        cbutpan.add(addeditbut);
        cbutpan.add(deletebut);
        ccenter.add(cbutpan);
        
        //put add/edit and delete buttons below list (add/edit displays this panel)
        // - edit only changes future uses, not existing ones
        // - delete does not remove or affect existing ones
        //numbers are 300+ in order of creation
        // - make sure to note in help files that adding after deleting will reuse a number, possibly making 2 items share a number and both work as a key (could be useful in a weird way)
        
        centerpanel = new JPanel();
        centerpanel.setPreferredSize(new Dimension(500, 300));
        centerpanel.add(subtypepanel);
        //centerpanel.add(new JScrollPane(itemlist));
        itempane = new JScrollPane(itemlist);
        itempane.setPreferredSize(new Dimension(238, 155));
        centerpanel.add(itempane);
        if (!nochest) centerpanel.add(chestpanel);
        centerpanel.add(scrollpanel);
        centerpanel.add(potionbox);
        centerpanel.add(editprepan);
        
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        //try {
        //java.io.OutputStream out = new java.io.FileOutputStream("itemwiz.log");
        //System.setErr(new java.io.PrintStream(out,true));
        //} catch (Exception e) {}
        
        cp.add("North", north);
        cp.add("Center", centerpanel);
        cp.add("South", bottompanel);
        
        dispose();
    }
    
    public void setItem(Item it) {
        item = it;
        number = item.number;
        if (number < 300 && !typepanel.isVisible()) {
            typepanel.setVisible(true);
            cp.remove(ccenter);
            cp.add("Center", centerpanel);
            custombutton.setSelected(false);
            predefinedbutton.setSelected(true);
            //repaint();
        }
        if (item.number == 4) {
            //scroll
            firstline.setText(item.scroll[0]);
            secondline.setText(item.scroll[1]);
            thirdline.setText(item.scroll[2]);
            fourthline.setText(item.scroll[3]);
            fifthline.setText(item.scroll[4]);
            otherbutton.doClick();
            subtypebut[4].doClick();
            itemlist.setSelectedValue(item.name, true);
            scrollpanel.setVisible(true);
        } else if (item.number == 5) {
            //chest
            chestcontents.clear();
            contentnames.clear();
            chestcount = 0;
            Item tempitem;
            for (int i = 0; i < 12; i++) {
                tempitem = ((Chest) item).itemAt(i);
                if (tempitem != null) {
                    tempitem = Item.createCopy(tempitem);
                    chestcontents.add(tempitem);
                    if (tempitem.ispotion)
                        contentnames.add(tempitem.name + " (" + powers[tempitem.potioncastpow - 1] + ")");
                    else if (tempitem instanceof Waterskin) {
                        if (((Waterskin) tempitem).drinks == 0) contentnames.add("Waterskin (Empty)");
                        else if (((Waterskin) tempitem).drinks == 1) contentnames.add("Waterskin (Almost Empty)");
                        else if (((Waterskin) tempitem).drinks == 2) contentnames.add("Waterskin (Almost Full)");
                        else contentnames.add("Waterskin (Full)");
                    } else contentnames.add(tempitem.name);
                    chestcount++;
                }
            }
            chestlist.setListData(contentnames);
            otherbutton.doClick();
            subtypebut[5].doClick();
        } else if (number < 31 && number > 9) {
            //potions
            potionbox.setSelectedIndex(item.potioncastpow - 1);
            otherbutton.doClick();
            subtypebut[3].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 51 && number > 30) {
            //keys
            otherbutton.doClick();
            subtypebut[0].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 61 && number > 50) {
            //treas
            otherbutton.doClick();
            subtypebut[1].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 71 && number > 60) {
            //food
            otherbutton.doClick();
            subtypebut[2].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number == 73 || (number < 10 && number > 6) || (number < 86 && number > 70) || (number < 286 && number > 281)) {
            //misc
            otherbutton.doClick();
            subtypebut[4].doClick();
            if (item.number == 73) {
                if (((Waterskin) item).drinks == 0) {
                    number = 731;
                    itemlist.setSelectedValue("Waterskin (Empty)", true);
                } else if (((Waterskin) item).drinks == 1) {
                    number = 732;
                    itemlist.setSelectedValue("Waterskin (Almost Empty)", true);
                } else if (((Waterskin) item).drinks == 2) {
                    number = 733;
                    itemlist.setSelectedValue("Waterskin (Almost Full)", true);
                } else {
                    number = 734;
                    itemlist.setSelectedValue("Waterskin (Full)", true);
                }
            } else itemlist.setSelectedValue(item.name, true);
        } else if (number < 101 && number > 85) {
            //neck
            armbutton.doClick();
            subtypebut[0].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 116 && number > 100) {
            //shield
            armbutton.doClick();
            subtypebut[1].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 136 && number > 115) {
            //head
            armbutton.doClick();
            subtypebut[2].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 156 && number > 135) {
            //torso
            armbutton.doClick();
            subtypebut[3].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 176 && number > 155) {
            //leg
            armbutton.doClick();
            subtypebut[4].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 196 && number > 175) {
            //foot
            armbutton.doClick();
            subtypebut[5].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 221 && number > 199) {
            //sword
            wepbutton.doClick();
            subtypebut[0].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 226 && number > 220) {
            //axe
            wepbutton.doClick();
            subtypebut[2].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 236 && number > 225) {
            //blunt
            wepbutton.doClick();
            subtypebut[3].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 256 && number > 235) {
            //staff
            wepbutton.doClick();
            subtypebut[1].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 276 && number > 255) {
            //missle
            wepbutton.doClick();
            subtypebut[4].doClick();
            itemlist.setSelectedValue(item.name, true);
        } else if (number < 300 && number > 275) {
            //other weapons
            wepbutton.doClick();
            subtypebut[5].doClick();
            if (item.number == 286) itemlist.setSelectedValue("Blue Box (All)", true);
            else if (item.number == 287) itemlist.setSelectedValue("Green Box (All)", true);
            else itemlist.setSelectedValue(item.name, true);
        } else if (item.number > 299) {
            //custom items
            //custombutton.doClick();
            if (typepanel.isVisible()) {
                typepanel.setVisible(false);
                cp.remove(centerpanel);
                cp.add("Center", ccenter);
                predefinedbutton.setSelected(false);
                custombutton.setSelected(true);
                //repaint();
            }
            if (citemlist.getSelectedIndex() != -1) {
                //System.out.println("citemlist selected index is "+citemlist.getSelectedIndex());
                item = (Item) customitems.get(citemlist.getSelectedIndex());
                number = item.number;
                //System.out.println("custom clicked -> list had selection "+citemlist.getSelectedIndex()+", number = "+number);
            }
            citemlist.setSelectedValue(item, true);
            if (citemlist.getSelectedIndex() == -1) {
                //item not found in list so add it
                Item newitem = Item.createCopy(item);
                customitems.add(newitem);
                citemlist.setListData(customitems);
                citemlist.setPreferredSize(new Dimension(220, customitems.size() * 19));
                citemlist.setSelectedValue(newitem, true);
                ((DMEditor) frame).itemfinder.updateCustomItems();
            }
            citemlist.ensureIndexIsVisible(citemlist.getSelectedIndex());
            JViewport cviewport = citempane.getViewport();
            cviewport.setViewPosition(new Point(0, citemlist.getSelectedIndex() * 19));
            citemindex = citemlist.getSelectedIndex();
        }
        number = item.number;//needed since subtypebut clicks reset number to 0
        if (number == 4 || number == 5 || number == 8 || number == 9 || number == 73 || (number > 9 && number < 31))
            editprebut.setVisible(false);
        itemlist.ensureIndexIsVisible(itemlist.getSelectedIndex());
        JViewport viewport = itempane.getViewport();
        viewport.setViewPosition(new Point(0, itemlist.getSelectedIndex() * 19));
        //try {
        //java.io.OutputStream out = new java.io.FileOutputStream("itemwiz.log");
        //System.setErr(new java.io.PrintStream(out,true));
        //} catch (Exception e) {}
        super.show();
    }
    
    public void show() {
        if (item != null) item = Item.createCopy(item);
        super.show();
    }
    
    public void showFromHero(int type) {
        if (typepanel.isVisible() && (item == null || (item.type != type && type != 7) || (type == 7 && item.projtype == 0))) {
            switch (type) {
                case Item.WEAPON:
                    wepbutton.doClick();
                    subtypebut[0].doClick();
                    break;
                case Item.SHIELD:
                    armbutton.doClick();
                    subtypebut[1].doClick();
                    break;
                case Item.HEAD:
                    armbutton.doClick();
                    subtypebut[2].doClick();
                    break;
                case Item.NECK:
                    armbutton.doClick();
                    subtypebut[0].doClick();
                    break;
                case Item.TORSO:
                    armbutton.doClick();
                    subtypebut[3].doClick();
                    break;
                case Item.LEGS:
                    armbutton.doClick();
                    subtypebut[4].doClick();
                    break;
                case Item.FEET:
                    armbutton.doClick();
                    subtypebut[5].doClick();
                    break;
                case 7:
                    //quiver
                    wepbutton.doClick();
                    subtypebut[4].doClick();
                    number = misslenums[9];//arrow
                    itemlist.setSelectedIndex(9);
                    itemlist.ensureIndexIsVisible(9);
                    break;
            }
        } else if (item != null) item = Item.createCopy(item);
        super.show();
    }
    
    public void updateCustomItems() {
        citemlist.setListData(customitems);
        citemlist.setPreferredSize(new Dimension(220, customitems.size() * 19));
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            //create item if haven't already
            if (number < 300 || (number > 730 && number < 735)) {
                if (item == null || item.number != number || item.number == 4 || item.number == 5 || (item.number > 9 && item.number < 31))
                    makeItem();
            } else item = Item.createCopy(item);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            item = null;
            dispose();
        } else if (e.getActionCommand().equals("Weapon")) {
            TYPE = 0;
            subtypebut[0].setText("Sword");
            subtypebut[1].setText("Staff");
            subtypebut[2].setText("Axe");
            subtypebut[3].setText("Blunt");
            subtypebut[4].setText("Missle");
            subtypebut[5].setText("Other");
            subtypebut[5].setActionCommand("Other Weapon");
            subtypebut[5].setEnabled(true);
            subtypebut[0].doClick();
            return;
        } else if (e.getActionCommand().equals("Armor")) {
            TYPE = 1;
            subtypebut[0].setText("Neck");
            subtypebut[1].setText("Shield");
            subtypebut[2].setText("Head");
            subtypebut[3].setText("Torso");
            subtypebut[4].setText("Leg");
            subtypebut[5].setText("Foot");
            subtypebut[5].setActionCommand("Foot");
            subtypebut[5].setEnabled(true);
            subtypebut[0].doClick();
            return;
        } else if (e.getActionCommand().equals("Other")) {
            TYPE = 2;
            subtypebut[0].setText("Key");
            subtypebut[1].setText("Treas");
            subtypebut[2].setText("Food");
            subtypebut[3].setText("Potion");
            subtypebut[4].setText("Misc");
            subtypebut[5].setText("Chest");
            subtypebut[5].setActionCommand("Chest");
            if (nochest) subtypebut[5].setEnabled(false);
            subtypebut[0].doClick();
            return;
        } else if (e.getActionCommand().equals("Add Item")) {
            int index = chestlist.getSelectedIndex();
            if (index == -1) {
                if (chestcount > 11) return;
                if (DMEditor.itemwizard2 == null) DMEditor.itemwizard2 = new ItemWizard(frame, true);
                DMEditor.itemwizard2.setTitle("Item Wizard - Add An Item To The Chest");
                DMEditor.itemwizard2.show();
                Item tempitem = DMEditor.itemwizard2.getItem();
                if (tempitem == null || tempitem.number == 5) return; //don't allow chest inside chest
                chestcontents.add(tempitem);
                if (tempitem.ispotion)
                    contentnames.add(tempitem.name + " (" + powers[tempitem.potioncastpow - 1] + ")");
                else if (tempitem instanceof Waterskin) {
                    if (((Waterskin) tempitem).drinks == 0) contentnames.add("Waterskin (Empty)");
                    else if (((Waterskin) tempitem).drinks == 1) contentnames.add("Waterskin (Almost Empty)");
                    else if (((Waterskin) tempitem).drinks == 2) contentnames.add("Waterskin (Almost Full)");
                    else contentnames.add("Waterskin (Full)");
                } else contentnames.add(tempitem.name);
                chestlist.setListData(contentnames);
                chestcount++;
                return;
            } else {
                if (DMEditor.itemwizard2 == null) DMEditor.itemwizard2 = new ItemWizard(frame, true);
                //DMEditor.itemwizard2.setTitle("Item Wizard - Change Chest Item \""+((Item)chestcontents.get(index)).name+"\"");
                DMEditor.itemwizard2.setTitle("Item Wizard - Change An Item In The Chest");
                DMEditor.itemwizard2.setItem((Item) chestcontents.elementAt(index));
                Item tempitem = DMEditor.itemwizard2.getItem();
                if (tempitem == null || tempitem.number == 5 || tempitem == ((Item) chestcontents.elementAt(index))) {
                    //chestlist.setListData(contentnames);
                    chestlist.clearSelection();
                    chestindex = -1;
                    return;
                }
                chestcontents.removeElementAt(index);
                chestcontents.insertElementAt(tempitem, index);
                contentnames.removeElementAt(index);
                if (tempitem.ispotion)
                    contentnames.insertElementAt(tempitem.name + " (" + powers[tempitem.potioncastpow - 1] + ")", index);
                else if (tempitem instanceof Waterskin) {
                    if (((Waterskin) tempitem).drinks == 0) contentnames.insertElementAt("Waterskin (Empty)", index);
                    else if (((Waterskin) tempitem).drinks == 1)
                        contentnames.insertElementAt("Waterskin (Almost Empty)", index);
                    else if (((Waterskin) tempitem).drinks == 2)
                        contentnames.insertElementAt("Waterskin (Almost Full)", index);
                    else contentnames.insertElementAt("Waterskin (Full)", index);
                } else contentnames.insertElementAt(tempitem.name, index);
                chestlist.setListData(contentnames);
                chestindex = -1;
                return;
            }
        } else if (e.getActionCommand().equals("Remove Item")) {
            if (chestcount < 1) return;
            int index = chestlist.getSelectedIndex();
            if (index == -1) return;
            chestcontents.removeElementAt(index);
            contentnames.removeElementAt(index);
            chestlist.setListData(contentnames);
            chestindex = -1;
            chestcount--;
            return;
        } else if (e.getActionCommand().equals("Predefined")) {
            typepanel.setVisible(true);
            cp.remove(ccenter);
            cp.add("Center", centerpanel);
            repaint();
            number = oldnumber;
        } else if (e.getActionCommand().equals("Custom")) {
            typepanel.setVisible(false);
            cp.remove(centerpanel);
            cp.add("Center", ccenter);
            repaint();
            if (number < 300) oldnumber = number;
            if (citemlist.getSelectedIndex() != -1) {
                item = (Item) customitems.get(citemlist.getSelectedIndex());
                number = item.number;
                //System.out.println("custom clicked -> list had selection "+citemlist.getSelectedIndex()+", number = "+number);
            }
        } else if (e.getActionCommand().equals("Edit Predefined")) {
            if (item == null || item.number != number) makeItem();
            //Item tempitem = (new ItemCreator(frame,item)).getItem();
            DMEditor.itemcreator.setTitle("Item Creator - Edit Predefined Item");
            DMEditor.itemcreator.setItem(item);
            Item tempitem = DMEditor.itemcreator.getItem();
            if (tempitem != null) {
                item = tempitem;
                if (item.number < 300) item.number = number;
                else {
                    //item name was changed, became custom -> switch to custom view
                    citemlist.setListData(customitems);
                    citemlist.setPreferredSize(new Dimension(220, customitems.size() * 19));
                    custombutton.doClick();
                    number = item.number;
                    citemlist.setSelectedIndex(number - 300);
                    citemindex = number - 300;
                    ((DMEditor) frame).itemfinder.updateCustomItems();
                    citemlist.ensureIndexIsVisible(citemindex);
                    JViewport cviewport = citempane.getViewport();
                    cviewport.setViewPosition(new Point(0, citemindex * 19));
                }
            }
        } else if (e.getActionCommand().equals("Add/Edit Item")) {
            int index = citemlist.getSelectedIndex();
            Item tempitem;
            if (index == -1) {
                //tempitem = (new ItemCreator(frame,null)).getItem();
                DMEditor.itemcreator.setTitle("Item Creator");
                DMEditor.itemcreator.show();
                tempitem = DMEditor.itemcreator.getItem();
                if (tempitem != null) {
                    customitems.add(tempitem);
                    citemlist.setListData(customitems);
                    citemlist.setPreferredSize(new Dimension(220, customitems.size() * 19));
                    item = tempitem;
                    item.number = 299 + customitems.size();
                    number = item.number;
                    citemlist.setSelectedIndex(number - 300);
                    citemindex = number - 300;
                    ((DMEditor) frame).itemfinder.updateCustomItems();
                } else {
                    citemlist.clearSelection();
                    citemindex = -1;
                    number = oldnumber;
                }
            } else {
                //tempitem = (new ItemCreator(frame,(Item)customitems.get(index))).getItem();
                DMEditor.itemcreator.setTitle("Item Creator");
                DMEditor.itemcreator.setItem((Item) customitems.get(index));
                tempitem = DMEditor.itemcreator.getItem();
                if (tempitem != null) {
                    customitems.set(index, tempitem);
                    citemlist.setListData(customitems);
                    citemlist.setPreferredSize(new Dimension(220, customitems.size() * 19));
                    item = tempitem;
                    item.number = 300 + index;
                    number = item.number;
                    citemlist.setSelectedIndex(index);
                    citemindex = index;
                    //System.out.println("num = "+item.number+", index = "+index);
                    ((DMEditor) frame).itemfinder.updateCustomItems();
                } else {
                    citemlist.clearSelection();
                    citemindex = -1;
                    number = oldnumber;
                }
            }
        } else if (e.getActionCommand().equals("Delete Item")) {
            int index = citemlist.getSelectedIndex();
            if (index == -1) return;
            customitems.remove(index);
            for (int i = index; i < customitems.size(); i++) {
                //subtract one from numbers of items after deleted one -> could cause number sharing!
                ((Item) customitems.get(i)).number--;
            }
            citemlist.setListData(customitems);
            citemlist.setPreferredSize(new Dimension(220, customitems.size() * 19));
            citemindex = -1;
            item = null;
            number = oldnumber;
            ((DMEditor) frame).itemfinder.updateCustomItems();
        } else if (TYPE == 0) { //weapons
            if (e.getActionCommand().equals("Sword")) {
                itemlist.setListData(sworditems);
                itemlist.setPreferredSize(new Dimension(220, sworditems.length * 19));
                SUBTYPE = 0;
                number = swordnums[0];
            } else if (e.getActionCommand().equals("Staff")) {
                itemlist.setListData(staffitems);
                itemlist.setPreferredSize(new Dimension(220, staffitems.length * 19));
                SUBTYPE = 1;
                number = staffnums[0];
            } else if (e.getActionCommand().equals("Axe")) {
                itemlist.setListData(axeitems);
                itemlist.setPreferredSize(new Dimension(220, axeitems.length * 19));
                SUBTYPE = 2;
                number = axenums[0];
            } else if (e.getActionCommand().equals("Blunt")) {
                itemlist.setListData(bluntitems);
                itemlist.setPreferredSize(new Dimension(220, bluntitems.length * 19));
                SUBTYPE = 3;
                number = bluntnums[0];
            } else if (e.getActionCommand().equals("Missle")) {
                itemlist.setListData(missleitems);
                itemlist.setPreferredSize(new Dimension(220, missleitems.length * 19));
                SUBTYPE = 4;
                number = misslenums[0];
            } else {
                itemlist.setListData(otherwepitems);
                itemlist.setPreferredSize(new Dimension(220, otherwepitems.length * 19));
                SUBTYPE = 5;
                number = otherwepnums[0];
            }
            potionbox.setVisible(false);
            scrollpanel.setVisible(false);
            chestpanel.setVisible(false);
            itemlist.setVisible(true);
            editprebut.setVisible(true);
            itempane.getVerticalScrollBar().setEnabled(true);
            itemlist.setSelectedIndex(0);
            itemlist.ensureIndexIsVisible(0);
            return;
        } else if (TYPE == 1) { //armor
            if (e.getActionCommand().equals("Neck")) {
                itemlist.setListData(neckitems);
                itemlist.setPreferredSize(new Dimension(220, neckitems.length * 19));
                SUBTYPE = 0;
                number = necknums[0];
            } else if (e.getActionCommand().equals("Shield")) {
                itemlist.setListData(shielditems);
                itemlist.setPreferredSize(new Dimension(220, shielditems.length * 19));
                SUBTYPE = 1;
                number = shieldnums[0];
            } else if (e.getActionCommand().equals("Head")) {
                itemlist.setListData(headitems);
                itemlist.setPreferredSize(new Dimension(220, headitems.length * 19));
                SUBTYPE = 2;
                number = headnums[0];
            } else if (e.getActionCommand().equals("Torso")) {
                itemlist.setListData(torsoitems);
                itemlist.setPreferredSize(new Dimension(220, torsoitems.length * 19));
                SUBTYPE = 3;
                number = torsonums[0];
            } else if (e.getActionCommand().equals("Leg")) {
                itemlist.setListData(legitems);
                itemlist.setPreferredSize(new Dimension(220, legitems.length * 19));
                SUBTYPE = 4;
                number = legnums[0];
            } else {
                itemlist.setListData(feetitems);
                itemlist.setPreferredSize(new Dimension(220, feetitems.length * 19));
                SUBTYPE = 5;
                number = feetnums[0];
            }
            potionbox.setVisible(false);
            scrollpanel.setVisible(false);
            chestpanel.setVisible(false);
            itemlist.setVisible(true);
            editprebut.setVisible(true);
            itempane.getVerticalScrollBar().setEnabled(true);
            itemlist.setSelectedIndex(0);
            itemlist.ensureIndexIsVisible(0);
            return;
        } else { //other
            if (e.getActionCommand().equals("Key")) {
                itemlist.setListData(keyitems);
                itemlist.setPreferredSize(new Dimension(220, keyitems.length * 19));
                SUBTYPE = 0;
                number = keynums[0];
            } else if (e.getActionCommand().equals("Treas")) {
                itemlist.setListData(coingemitems);
                itemlist.setPreferredSize(new Dimension(220, coingemitems.length * 19));
                SUBTYPE = 1;
                number = coingemnums[0];
            } else if (e.getActionCommand().equals("Food")) {
                itemlist.setListData(fooditems);
                itemlist.setPreferredSize(new Dimension(220, fooditems.length * 19));
                SUBTYPE = 2;
                number = foodnums[0];
            } else if (e.getActionCommand().equals("Potion")) {
                itemlist.setListData(potionitems);
                itemlist.setPreferredSize(new Dimension(220, potionitems.length * 19));
                SUBTYPE = 3;
                number = potionnums[0];
                scrollpanel.setVisible(false);
                chestpanel.setVisible(false);
                editprebut.setVisible(false);
                itemlist.setVisible(true);
                itempane.getVerticalScrollBar().setEnabled(true);
                potionbox.setVisible(true);
                itemlist.setSelectedIndex(0);
                itemlist.ensureIndexIsVisible(0);
                return;
            } else if (e.getActionCommand().equals("Misc")) {
                itemlist.setListData(miscitems);
                itemlist.setPreferredSize(new Dimension(220, miscitems.length * 19));
                SUBTYPE = 4;
                number = miscnums[0];
            } else {
                //chest
                itemlist.setVisible(false);
                itempane.getVerticalScrollBar().setEnabled(false);
                SUBTYPE = 5;
                number = 5;
                potionbox.setVisible(false);
                scrollpanel.setVisible(false);
                editprebut.setVisible(false);
                chestpanel.setVisible(true);
                return;
            }
            potionbox.setVisible(false);
            scrollpanel.setVisible(false);
            chestpanel.setVisible(false);
            editprebut.setVisible(true);
            itemlist.setVisible(true);
            itempane.getVerticalScrollBar().setEnabled(true);
            itemlist.setSelectedIndex(0);
            itemlist.ensureIndexIsVisible(0);
            return;
        }
    }
    
    private void makeItem() {
        //potion
        if (number > 9 && number < 31) {
            int potionpower;
            if (number < 21 || number > 23) {
                potionpower = (potionbox.getSelectedIndex() + 1) * 2;
                if (potionpower == 10) potionpower = 12;
                else if (potionpower == 12) potionpower = 16;
                if (number == 10 || number == 11 || number == 12) potionpower *= 5;
                item = new Item(number, potionpower, potionbox.getSelectedIndex() + 1);
            } else if (number == 21) { //ful bomb
                potionpower = potionbox.getSelectedIndex();
                if (potionpower == 0) potionpower = 40;
                else if (potionpower == 1) potionpower = 70;
                else if (potionpower == 2) potionpower = 150;
                else if (potionpower == 3) potionpower = 200;
                else if (potionpower == 4) potionpower = 250;
                else potionpower = 300;
                potionpower += randGen.nextInt() % 20;
                item = new Item(number, potionpower, potionbox.getSelectedIndex() + 1);
            } else if (number == 22) { //ven bomb
                potionpower = potionbox.getSelectedIndex();
                if (potionpower == 0) potionpower = 1;
                else if (potionpower == 1) potionpower = 3;
                else if (potionpower == 2) potionpower = 5;
                else if (potionpower == 3) potionpower = 7;
                else if (potionpower == 4) potionpower = 9;
                else potionpower = 12;
                item = new Item(number, potionpower, potionbox.getSelectedIndex() + 1);
            } else if (number == 23) { //bolt bomb
                potionpower = potionbox.getSelectedIndex();
                if (potionpower == 0) potionpower = 50;
                else if (potionpower == 1) potionpower = 100;
                else if (potionpower == 2) potionpower = 175;
                else if (potionpower == 3) potionpower = 250;
                else if (potionpower == 4) potionpower = 300;
                else potionpower = 350;
                potionpower += randGen.nextInt() % 20;
                item = new Item(number, potionpower, potionbox.getSelectedIndex() + 1);
            }
        }
        //chest
        else if (number == 5) {
            item = new Chest();
            for (int i = 0; i < chestcount; i++) {
                ((Chest) item).putItem(i, Item.createCopy((Item) chestcontents.elementAt(i)));
            }
        }
        //scroll
        else if (number == 4) {
            String[] mess = new String[5];
            mess[0] = firstline.getText();
            mess[1] = secondline.getText();
            mess[2] = thirdline.getText();
            mess[3] = fourthline.getText();
            mess[4] = fifthline.getText();
            //Graphics sg = firstline.getGraphics();
            //sg.setFont(sg.getFont().deriveFont(16.0f));
            for (int i = 0; i < 5; i++) {
                //while (sg.getFontMetrics().stringWidth(mess[i])>100) {
                //        mess[i] = mess[i].substring(0,mess[i].length());
                //}
                if (mess[i].length() > 20) mess[i] = mess[i].substring(0, 20);
            }
            item = new Item(mess);
        }
        //torch
        else if (number == 9) {
            item = new Torch();
        }
        //waterskin
        else if (number > 730) {
            if (number == 731) item = new Waterskin();
            else if (number == 732) item = new Waterskin(2);
            else if (number == 733) item = new Waterskin(1);
            else item = new Waterskin(0);
        }
        //compass
        else if (number == 8) {
            item = new Compass();
        }
        //everything else (except custom which will have set item already)
        else if (number < 300) item = new Item(number);
    }
    
    public Item getItem() {
        setTitle("Item Wizard");
        return item;
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.getSource().equals(chestlist)) {
            int clickedindex = chestlist.locationToIndex(e.getPoint());
            if (clickedindex == -1 || clickedindex == chestindex) chestlist.clearSelection();
            chestindex = chestlist.getSelectedIndex();
            return;
        } else if (e.getSource().equals(citemlist)) {
            int clickedindex = citemlist.locationToIndex(e.getPoint());
            if (clickedindex == -1 || clickedindex == citemindex) citemlist.clearSelection();
            citemindex = citemlist.getSelectedIndex();
            if (citemindex != -1) {
                item = (Item) customitems.get(citemindex);
                number = item.number;
                return;
            } else {
                item = null;
                number = oldnumber;
            }
        } else if (TYPE == 0) {
            switch (SUBTYPE) {
                case 0: //sword
                    number = swordnums[itemlist.getSelectedIndex()];
                    break;
                case 1: //staff
                    number = staffnums[itemlist.getSelectedIndex()];
                    break;
                case 2: //axe
                    number = axenums[itemlist.getSelectedIndex()];
                    break;
                case 3: //blunt
                    number = bluntnums[itemlist.getSelectedIndex()];
                    break;
                case 4: //missle
                    number = misslenums[itemlist.getSelectedIndex()];
                    break;
                case 5: //otherwep
                    number = otherwepnums[itemlist.getSelectedIndex()];
                    break;
            }
        } else if (TYPE == 1) {
            switch (SUBTYPE) {
                case 0: //neck
                    number = necknums[itemlist.getSelectedIndex()];
                    break;
                case 1: //shield
                    number = shieldnums[itemlist.getSelectedIndex()];
                    break;
                case 2: //head
                    number = headnums[itemlist.getSelectedIndex()];
                    break;
                case 3: //torso
                    number = torsonums[itemlist.getSelectedIndex()];
                    break;
                case 4: //leg
                    number = legnums[itemlist.getSelectedIndex()];
                    break;
                case 5: //foot
                    number = feetnums[itemlist.getSelectedIndex()];
                    break;
            }
        } else {
            switch (SUBTYPE) {
                case 0: //key
                    number = keynums[itemlist.getSelectedIndex()];
                    break;
                case 1: //coin/gem
                    number = coingemnums[itemlist.getSelectedIndex()];
                    break;
                case 2: //food
                    number = foodnums[itemlist.getSelectedIndex()];
                    break;
                case 3: //potion
                    number = potionnums[itemlist.getSelectedIndex()];
                    break;
                case 4: //other
                    number = miscnums[itemlist.getSelectedIndex()];
                    if (number == 4) {
                        editprebut.setVisible(false);
                        scrollpanel.setVisible(true);
                        return;
                    } else if (number == 8 || number == 9 || number > 730) editprebut.setVisible(false);
                    break;
                case 5: //chest - can't happen
                    break;
            }
            scrollpanel.setVisible(false);
            if (SUBTYPE != 3 && number != 8 && number != 9 && number < 731) editprebut.setVisible(true);
        }
        item = null;
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
