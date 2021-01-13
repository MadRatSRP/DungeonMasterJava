import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class ItemFinder extends JFrame implements ActionListener, MouseListener {
    
    private Container cp;
    private DMEditor dmed;
    private int citemindex = -1;
    private int oldnumber = 200;
    private JPanel typepanel, centerpanel, ccenter;
    private JList itemlist, citemlist, foundlist;
    private JToggleButton[] subtypebut;
    private JScrollPane itempane, citempane, foundpane;
    private Vector foundvec = new Vector(10);
    
    private int TYPE = 0, SUBTYPE = 0;
    private int number = ItemWizard.swordnums[0];
    
    public ItemFinder(DMEditor dmed) {
        super("Find An Item");
        this.dmed = dmed;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(800, 480);
        //setLocationRelativeTo(dmed);
        cp = getContentPane();
        
        //the main list, initially showing swords
        itemlist = new JList(ItemWizard.sworditems);
        itemlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemlist.setPreferredSize(new Dimension(220, ItemWizard.sworditems.length * 19));
        itemlist.setSelectedIndex(0);
        itemlist.addMouseListener(this);
        
        //allow default and custom items
        JToggleButton predefinedbutton = new JToggleButton("Predefined");
        JToggleButton custombutton = new JToggleButton("Custom");
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
        JToggleButton wepbutton = new JToggleButton("Weapon");
        JToggleButton armbutton = new JToggleButton("Armor");
        JToggleButton otherbutton = new JToggleButton("Other");
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
        //north.setLayout(new BoxLayout(north,BoxLayout.Y_AXIS));
        north.add(bigtypepanel);
        //north.add(typepanel);
        
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
        
        //custom item panel
        ccenter = new JPanel();
        citemlist = new JList(ItemWizard.customitems);
        citemlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        citemlist.setPreferredSize(new Dimension(220, ItemWizard.customitems.size() * 19));
        citemlist.addMouseListener(this);
        citempane = new JScrollPane(citemlist);
        citempane.setPreferredSize(new Dimension(238, 155));
        ccenter.add(citempane);
        ccenter.setVisible(false);
        
        centerpanel = new JPanel();
        centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.Y_AXIS));
        centerpanel.setPreferredSize(new Dimension(500, 300));
        centerpanel.add(typepanel);
        centerpanel.add(subtypepanel);
        itempane = new JScrollPane(itemlist);
        itempane.setPreferredSize(new Dimension(238, 155));
        centerpanel.add(itempane);
        
        //east panel -> has list showing all locations of found item
        JPanel east = new JPanel();
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        foundlist = new JList();
        foundlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //foundlist.setPreferredSize(new Dimension(220,ItemWizard.sworditems.length*19));
        foundlist.setSelectedIndex(0);
        foundlist.addMouseListener(this);
        foundpane = new JScrollPane(foundlist);
        foundpane.setPreferredSize(new Dimension(238, 300));
        JPanel foundpanel = new JPanel();
        foundpanel.add(foundpane);
        foundpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Locations"));
        east.add(foundpanel);
        JButton gotobut = new JButton("Go To Item");
        gotobut.addActionListener(this);
        east.add(gotobut);
        
        JPanel west = new JPanel();
        west.add(centerpanel);
        west.add(ccenter);
        
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Find");
        JButton cancel = new JButton("Exit");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        cp.add("North", north);
        //cp.add("West",centerpanel);
        cp.add("West", west);
        cp.add("South", bottompanel);
        cp.add("East", east);
        
        number = 200;
        itemlist.ensureIndexIsVisible(itemlist.getSelectedIndex());
        JViewport viewport = itempane.getViewport();
        viewport.setViewPosition(new Point(0, itemlist.getSelectedIndex() * 19));
    }
    
    public void updateCustomItems() {
        citemlist.setListData(ItemWizard.customitems);
        citemlist.setPreferredSize(new Dimension(220, ItemWizard.customitems.size() * 19));
        if (isShowing()) repaint();
        DMEditor.NEEDSAVEITEMS = true;
    }
    
    private int searchChest(Item c) {
        Chest chest = (Chest) c;
        Item item;
        int numfound = 0;
        for (int i = 0; i < 12; i++) {
            item = chest.itemAt(i);
            if (item != null && item.number == number) numfound++;
                        /*
                        if (item!=null) {
                                if (item.number==number) numfound++;
                                else if (item.number==5) numfound+=searchChest(item);
                        }
                        */
        }
        return numfound;
    }
    
    private void searchMapData(MapData mapdata, int l, int x, int y) {
        Item item;
        int numfound = 0;
        if (mapdata.hasItems) {
            for (int i = 0; i < mapdata.mapItems.size(); i++) {
                item = (Item) mapdata.mapItems.get(i);
                if (item.number == number) numfound++;
                else if (item.number == 5) numfound += searchChest(item);
            }
            if (numfound > 0) {
                if (mapdata.mapchar == ']') foundvec.add(numfound + " found in alcove at " + l + "," + x + "," + y);
                else if (mapdata.mapchar == 'a')
                    foundvec.add(numfound + " found in vi altar at " + l + "," + x + "," + y);
                else foundvec.add(numfound + " found at " + l + "," + x + "," + y);
                numfound = 0;
            }
        } else if (mapdata.mapchar == '[') {
            //4-sided alcove
            AlcoveData ad = (AlcoveData) mapdata;
            for (int i = 0; i < ad.northside.size(); i++) {
                item = (Item) ad.northside.get(i);
                if (item.number == number) numfound++;
                else if (item.number == 5) numfound += searchChest(item);
            }
            for (int i = 0; i < ad.southside.size(); i++) {
                item = (Item) ad.southside.get(i);
                if (item.number == number) numfound++;
                else if (item.number == 5) numfound += searchChest(item);
            }
            for (int i = 0; i < ad.eastside.size(); i++) {
                item = (Item) ad.eastside.get(i);
                if (item.number == number) numfound++;
                else if (item.number == 5) numfound += searchChest(item);
            }
            for (int i = 0; i < ad.westside.size(); i++) {
                item = (Item) ad.westside.get(i);
                if (item.number == number) numfound++;
                else if (item.number == 5) numfound += searchChest(item);
            }
            if (numfound > 0) {
                foundvec.add(numfound + " found in alcove at " + l + "," + x + "," + y);
                numfound = 0;
            }
            if (ad.isSwitch) for (int i = 0; i < ad.alcoveswitchdata.switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) ad.alcoveswitchdata.switchlist.get(i);
                if (fd.actiontype == 5) {
                    if (!fd.switchstate) searchMapData(fd.changeto, l, x, y);
                    else if (fd.isReusable) searchMapData(fd.oldMapObject, l, x, y);
                }
            }
        } else if (mapdata.mapchar == 'm') {
            //mirror
            HeroData hero = ((MirrorData) mapdata).hero;
            if (hero == null) return;
            if (hero.weapon.number == number) numfound++;
            else if (hero.weapon.number == 5) numfound += searchChest(hero.weapon);
            if (hero.hand != null) {
                if (hero.hand.number == number) numfound++;
                else if (hero.hand.number == 5) numfound += searchChest(hero.hand);
            }
            if (hero.head != null && hero.head.number == number) numfound++;
            if (hero.neck != null && hero.neck.number == number) numfound++;
            if (hero.torso != null && hero.torso.number == number) numfound++;
            if (hero.legs != null && hero.legs.number == number) numfound++;
            if (hero.feet != null && hero.feet.number == number) numfound++;
            if (hero.pouch1 != null && hero.pouch1.number == number) numfound++;
            if (hero.pouch2 != null && hero.pouch2.number == number) numfound++;
            for (int i = 0; i < 6; i++) {
                if (hero.quiver[i] != null && hero.quiver[i].number == number) numfound++;
            }
            for (int i = 0; i < 16; i++) {
                if (hero.pack[i] != null) {
                    if (hero.pack[i].number == number) numfound++;
                    else if (hero.pack[i].number == 5) numfound += searchChest(hero.pack[i]);
                }
            }
            if (numfound > 0) {
                foundvec.add(numfound + " found in mirror at " + l + "," + x + "," + y);
                numfound = 0;
            }
        } else if (mapdata.mapchar == '/') {
            //wallswitch
            WallSwitchData wd = (WallSwitchData) mapdata;
            if (wd.actiontype == 5) {
                if (!wd.switchstate) searchMapData(wd.changeto, l, x, y);
                else if (wd.isReusable) searchMapData(wd.oldMapObject, l, x, y);
            }
        } else if (mapdata.mapchar == '\\') {
            //multiple wallswitch
            for (int i = 0; i < ((MultWallSwitchData) mapdata).switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) ((MultWallSwitchData) mapdata).switchlist.get(i);
                if (fd.actiontype == 5) {
                    if (!fd.switchstate) searchMapData(fd.changeto, l, x, y);
                    else if (fd.isReusable) searchMapData(fd.oldMapObject, l, x, y);
                }
            }
        } else if (mapdata.mapchar == '}') {
            //sconce
            SconceData sd = (SconceData) mapdata;
            if (number == 9 && sd.hasTorch) foundvec.add("1 found in sconce at " + l + "," + x + "," + y);
            if (sd.isSwitch) for (int i = 0; i < sd.sconceswitch.switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) sd.sconceswitch.switchlist.get(i);
                if (fd.actiontype == 5) {
                    if (!fd.switchstate) searchMapData(fd.changeto, l, x, y);
                    else if (fd.isReusable) searchMapData(fd.oldMapObject, l, x, y);
                }
            }
        } else if (mapdata.mapchar == 'l') {
            //launcher
            LauncherData ld = (LauncherData) mapdata;
            if (ld.type == 1) {
                if (ld.it.number == number) foundvec.add("Found as launcher item at " + l + "," + x + "," + y);
                else if (ld.it.number == 5) {
                    numfound += searchChest(ld.it);
                    if (numfound > 0)
                        foundvec.add("Found " + numfound + " in chest launcher item at " + l + "," + x + "," + y);
                    numfound = 0;
                }
            }
        } else if (mapdata.mapchar == '!' && number == 215) {
            //stormbringer
            if (!((StormbringerData) mapdata).wasUsed) {
                foundvec.add("1 found in stormbringer stone at " + l + "," + x + "," + y);
            }
        }
        if (mapdata.mapchar == ']' || mapdata.mapchar == 'a') {
            OneAlcoveData ad = (OneAlcoveData) mapdata;
            if (ad.isSwitch) for (int i = 0; i < ad.alcoveswitchdata.switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) ad.alcoveswitchdata.switchlist.get(i);
                if (fd.actiontype == 5) {
                    if (!fd.switchstate) searchMapData(fd.changeto, l, x, y);
                    else if (fd.isReusable) searchMapData(fd.oldMapObject, l, x, y);
                }
            }
        } else if (mapdata.mapchar == 'g') {
            //generator
            MonsterData mon = ((GeneratorData) mapdata).monster;
            for (int i = 0; i < mon.carrying.size(); i++) {
                item = (Item) mon.carrying.get(i);
                if (item.number == number) numfound++;
                else if (item.number == 5) numfound += searchChest(item);
            }
            if (numfound > 0) {
                foundvec.add(numfound + " carried by generated " + mon.name + " at " + l + "," + x + "," + y);
                numfound = 0;
            }
        } else if (mapdata.mapchar == 's') {
            //floorswitch
            FloorSwitchData fd = (FloorSwitchData) mapdata;
            if (fd.actiontype == 5) {
                if (!fd.switchstate) searchMapData(fd.changeto, l, x, y);
                else if (fd.isReusable) searchMapData(fd.oldMapObject, l, x, y);
            }
        } else if (mapdata.mapchar == 'S') {
            //multiple floorswitch
            for (int i = 0; i < ((MultFloorSwitchData) mapdata).switchlist.size(); i++) {
                FloorSwitchData fd = (FloorSwitchData) ((MultFloorSwitchData) mapdata).switchlist.get(i);
                if (fd.actiontype == 5) {
                    if (!fd.switchstate) searchMapData(fd.changeto, l, x, y);
                    else if (fd.isReusable) searchMapData(fd.oldMapObject, l, x, y);
                }
            }
        } else if (mapdata.mapchar == 'E') {
            //event square
            EventSquareData ed = (EventSquareData) mapdata;
            Action a;
            for (int i = 0; i < ed.choices.length; i++) {
                for (int j = 0; j < ed.choices[i].actions.size(); j++) {
                    a = (Action) ed.choices[i].actions.get(j);
                    if (a.actiontype == 5) {
                        if (((Item) a.action).number == number) numfound++;
                        else if (((Item) a.action).number == 5) numfound += searchChest((Item) a.action);
                    }
                }
            }
            if (numfound > 0) {
                foundvec.add(numfound + " received from event at " + l + "," + x + "," + y);
                numfound = 0;
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Find")) {
            foundvec.clear();
            //find all level,x,y locations of items (note if in alcove, in mirror, on floor, held by mon, to be fired by launcher, or created by exchange switch)
            //search map squares and mons
            //not finding -> 4-sided alcove, mirror, launcher, exchange maps in switches, generator mons
            Item item;
            MonsterData mon;
            int numfound = 0;
            MapData[][] maplevel;
            for (int l = 0; l < dmed.MAPLEVELS; l++) {
                maplevel = (MapData[][]) dmed.maplevels.get(l);
                for (int x = 0; x < dmed.MAPWIDTH; x++) {
                    for (int y = 0; y < dmed.MAPHEIGHT; y++) {
                        searchMapData(maplevel[x][y], l, x, y);
                        if (maplevel[x][y].hasMons) {
                            for (int sub = 0; sub < 6; ) {
                                mon = (MonsterData) dmed.monhash.get(l + "," + x + "," + y + "," + sub);
                                if (mon != null && mon.carrying.size() > 0) {
                                    for (int i = 0; i < mon.carrying.size(); i++) {
                                        item = (Item) mon.carrying.get(i);
                                        if (item.number == number) numfound++;
                                        else if (item.number == 5) numfound += searchChest(item);
                                    }
                                    if (numfound > 0) {
                                        foundvec.add(numfound + " carried by " + mon.name + " at " + l + "," + x + "," + y);
                                        numfound = 0;
                                    }
                                }
                                if (sub == 3) sub = 5;
                                else sub++;
                            }
                            
                        }
                    }
                }
            }
            if (foundvec.isEmpty()) foundvec.add("None Found");
            foundlist.setListData(foundvec);
        } else if (e.getActionCommand().equals("Exit")) {
            hide();
        } else if (e.getActionCommand().equals("Weapon")) {
            TYPE = 0;
            subtypebut[0].setText("Sword");
            subtypebut[1].setText("Staff");
            subtypebut[2].setText("Axe");
            subtypebut[3].setText("Blunt");
            subtypebut[4].setText("Missle");
            subtypebut[5].setText("Other");
            subtypebut[5].setActionCommand("Other Weapon");
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
            subtypebut[0].doClick();
            return;
        } else if (e.getActionCommand().equals("Go To Item")) {
            mouseClicked(new MouseEvent(foundlist, MouseEvent.MOUSE_CLICKED, 0L, MouseEvent.BUTTON2_MASK, 0, foundlist.getSelectedIndex() * 19, 2, false));
        } else if (e.getActionCommand().equals("Predefined")) {
            //typepanel.setVisible(true);
            //cp.remove(ccenter);
            //cp.add("West",centerpanel);
            ccenter.setVisible(false);
            centerpanel.setVisible(true);
            repaint();
            number = oldnumber;
        } else if (e.getActionCommand().equals("Custom")) {
            //typepanel.setVisible(false);
            //cp.remove(centerpanel);
            //cp.add("West",ccenter);
            centerpanel.setVisible(false);
            ccenter.setVisible(true);
            repaint();
            if (number < 300) oldnumber = number;
            if (citemlist.getSelectedIndex() != -1) {
                Item item = (Item) ItemWizard.customitems.get(citemlist.getSelectedIndex());
                number = item.number;
            }
        } else if (TYPE == 0) { //weapons
            if (e.getActionCommand().equals("Sword")) {
                itemlist.setListData(ItemWizard.sworditems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.sworditems.length * 19));
                SUBTYPE = 0;
                number = ItemWizard.swordnums[0];
            } else if (e.getActionCommand().equals("Staff")) {
                itemlist.setListData(ItemWizard.staffitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.staffitems.length * 19));
                SUBTYPE = 1;
                number = ItemWizard.staffnums[0];
            } else if (e.getActionCommand().equals("Axe")) {
                itemlist.setListData(ItemWizard.axeitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.axeitems.length * 19));
                SUBTYPE = 2;
                number = ItemWizard.axenums[0];
            } else if (e.getActionCommand().equals("Blunt")) {
                itemlist.setListData(ItemWizard.bluntitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.bluntitems.length * 19));
                SUBTYPE = 3;
                number = ItemWizard.bluntnums[0];
            } else if (e.getActionCommand().equals("Missle")) {
                itemlist.setListData(ItemWizard.missleitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.missleitems.length * 19));
                SUBTYPE = 4;
                number = ItemWizard.misslenums[0];
            } else {
                itemlist.setListData(ItemWizard.otherwepitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.otherwepitems.length * 19));
                SUBTYPE = 5;
                number = ItemWizard.otherwepnums[0];
            }
            itemlist.setVisible(true);
            itempane.getVerticalScrollBar().setEnabled(true);
            itemlist.setSelectedIndex(0);
            itemlist.ensureIndexIsVisible(0);
            return;
        } else if (TYPE == 1) { //armor
            if (e.getActionCommand().equals("Neck")) {
                itemlist.setListData(ItemWizard.neckitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.neckitems.length * 19));
                SUBTYPE = 0;
                number = ItemWizard.necknums[0];
            } else if (e.getActionCommand().equals("Shield")) {
                itemlist.setListData(ItemWizard.shielditems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.shielditems.length * 19));
                SUBTYPE = 1;
                number = ItemWizard.shieldnums[0];
            } else if (e.getActionCommand().equals("Head")) {
                itemlist.setListData(ItemWizard.headitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.headitems.length * 19));
                SUBTYPE = 2;
                number = ItemWizard.headnums[0];
            } else if (e.getActionCommand().equals("Torso")) {
                itemlist.setListData(ItemWizard.torsoitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.torsoitems.length * 19));
                SUBTYPE = 3;
                number = ItemWizard.torsonums[0];
            } else if (e.getActionCommand().equals("Leg")) {
                itemlist.setListData(ItemWizard.legitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.legitems.length * 19));
                SUBTYPE = 4;
                number = ItemWizard.legnums[0];
            } else {
                itemlist.setListData(ItemWizard.feetitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.feetitems.length * 19));
                SUBTYPE = 5;
                number = ItemWizard.feetnums[0];
            }
            itemlist.setVisible(true);
            itempane.getVerticalScrollBar().setEnabled(true);
            itemlist.setSelectedIndex(0);
            itemlist.ensureIndexIsVisible(0);
            return;
        } else { //other
            if (e.getActionCommand().equals("Key")) {
                itemlist.setListData(ItemWizard.keyitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.keyitems.length * 19));
                SUBTYPE = 0;
                number = ItemWizard.keynums[0];
            } else if (e.getActionCommand().equals("Treas")) {
                itemlist.setListData(ItemWizard.coingemitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.coingemitems.length * 19));
                SUBTYPE = 1;
                number = ItemWizard.coingemnums[0];
            } else if (e.getActionCommand().equals("Food")) {
                itemlist.setListData(ItemWizard.fooditems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.fooditems.length * 19));
                SUBTYPE = 2;
                number = ItemWizard.foodnums[0];
            } else if (e.getActionCommand().equals("Potion")) {
                itemlist.setListData(ItemWizard.potionitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.potionitems.length * 19));
                SUBTYPE = 3;
                number = ItemWizard.potionnums[0];
            } else if (e.getActionCommand().equals("Misc")) {
                itemlist.setListData(ItemWizard.miscitems);
                itemlist.setPreferredSize(new Dimension(220, ItemWizard.miscitems.length * 19));
                SUBTYPE = 4;
                number = ItemWizard.miscnums[0];
            } else {
                //chest
                itemlist.setVisible(false);
                itempane.getVerticalScrollBar().setEnabled(false);
                SUBTYPE = 5;
                number = 5;
                return;
            }
            itemlist.setVisible(true);
            itempane.getVerticalScrollBar().setEnabled(true);
            itemlist.setSelectedIndex(0);
            itemlist.ensureIndexIsVisible(0);
            return;
        }
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.getSource().equals(citemlist)) {
            int clickedindex = citemlist.locationToIndex(e.getPoint());
            if (clickedindex == -1 || clickedindex == citemindex) citemlist.clearSelection();
            citemindex = citemlist.getSelectedIndex();
            if (citemindex != -1) {
                Item item = (Item) ItemWizard.customitems.get(citemindex);
                number = item.number;
            } else {
                number = oldnumber;
            }
        } else if (TYPE == 0) {
            switch (SUBTYPE) {
                case 0: //sword
                    number = ItemWizard.swordnums[itemlist.getSelectedIndex()];
                    break;
                case 1: //staff
                    number = ItemWizard.staffnums[itemlist.getSelectedIndex()];
                    break;
                case 2: //axe
                    number = ItemWizard.axenums[itemlist.getSelectedIndex()];
                    break;
                case 3: //blunt
                    number = ItemWizard.bluntnums[itemlist.getSelectedIndex()];
                    break;
                case 4: //missle
                    number = ItemWizard.misslenums[itemlist.getSelectedIndex()];
                    break;
                case 5: //otherwep
                    number = ItemWizard.otherwepnums[itemlist.getSelectedIndex()];
                    break;
            }
        } else if (TYPE == 1) {
            switch (SUBTYPE) {
                case 0: //neck
                    number = ItemWizard.necknums[itemlist.getSelectedIndex()];
                    break;
                case 1: //shield
                    number = ItemWizard.shieldnums[itemlist.getSelectedIndex()];
                    break;
                case 2: //head
                    number = ItemWizard.headnums[itemlist.getSelectedIndex()];
                    break;
                case 3: //torso
                    number = ItemWizard.torsonums[itemlist.getSelectedIndex()];
                    break;
                case 4: //leg
                    number = ItemWizard.legnums[itemlist.getSelectedIndex()];
                    break;
                case 5: //foot
                    number = ItemWizard.feetnums[itemlist.getSelectedIndex()];
                    break;
            }
        } else {
            switch (SUBTYPE) {
                case 0: //key
                    number = ItemWizard.keynums[itemlist.getSelectedIndex()];
                    break;
                case 1: //coin/gem
                    number = ItemWizard.coingemnums[itemlist.getSelectedIndex()];
                    break;
                case 2: //food
                    number = ItemWizard.foodnums[itemlist.getSelectedIndex()];
                    break;
                case 3: //potion
                    number = ItemWizard.potionnums[itemlist.getSelectedIndex()];
                    break;
                case 4: //other
                    number = ItemWizard.miscnums[itemlist.getSelectedIndex()];
                    break;
                case 5: //chest - can't happen
                    break;
            }
        }
    }
    
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2 && !SwingUtilities.isRightMouseButton(e)) return;
        if (e.getSource() == foundlist) {
            //if (dmed.SQUARELOCKED) return;
            //int index = foundlist.locationToIndex(e.getPoint());
            //String itemtofind = (String)foundvec.get(index);
            String itemtofind = (String) foundvec.get(foundlist.getSelectedIndex());
            if (itemtofind.startsWith("None")) return;
            itemtofind = itemtofind.substring(itemtofind.lastIndexOf("at ") + 3);
            int l = Integer.parseInt(itemtofind.substring(0, itemtofind.indexOf(',')));
            int x = Integer.parseInt(itemtofind.substring(itemtofind.indexOf(',') + 1, itemtofind.indexOf(',', itemtofind.indexOf(',') + 1)));
            int y = Integer.parseInt(itemtofind.substring(itemtofind.lastIndexOf(',') + 1));
            if (dmed.currentlevel != l) {
                //change to proper level
                dmed.mappanel.setVisible(false);
                dmed.currentlevel = l;
                dmed.mapdata = (MapData[][]) dmed.maplevels.get(dmed.currentlevel);
                dmed.mappanel.clearTargets();
                dmed.mappanel.repaint();
                dmed.mappanel.setVisible(true);
                if (dmed.currentlevel == 0) dmed.mbutton[8].setEnabled(false);
                else dmed.mbutton[8].setEnabled(true);
            }
            //ensure square is visible
            Point itempoint;
            if (!dmed.ZOOMING) itempoint = new Point(x * 33, y * 33);
            else itempoint = new Point(x * 17, y * 17);
            itempoint.x -= dmed.mpane.getSize().width / 2;
            if (itempoint.x < 0) itempoint.x = 0;
            itempoint.y -= dmed.mpane.getSize().height / 2;
            if (itempoint.y < 0) itempoint.y = 0;
            dmed.mpane.getViewport().setViewPosition(itempoint);
            //fix mouse listener stuff and set any targets
            dmed.mapclick.x = x;
            dmed.mapclick.y = y;
            dmed.currentx = x;
            dmed.currenty = y;
            dmed.monitembox.removeAll();
            dmed.monitembox.repaint();
            boolean dopaint = dmed.mappanel.clearTargets();
            dmed.lockx = x;
            dmed.locky = y;
            dmed.setStatusBar(dmed.mapdata[x][y], x, y);
            boolean dopaint2 = dmed.mappanel.doTargets(dmed.mapdata[x][y], x, y);
            if (dopaint || dopaint2) dmed.mappanel.forcePaint();
            if (!dmed.SQUARELOCKED) {
                dmed.SQUARELOCKED = true;
                dmed.statusbar.setText(dmed.statusbar.getText() + "      (Locked)");
            }
        } else actionPerformed(new ActionEvent(null, 0, "Find"));
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}