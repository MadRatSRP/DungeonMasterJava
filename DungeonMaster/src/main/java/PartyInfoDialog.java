import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

public class PartyInfoDialog extends JDialog implements ActionListener, MouseListener {
    
    private DMEditor ed;
    private JTextField level, partyx, partyy, levelpoints, hsmpoints, statpoints, defensepoints, itempoints, abilitypoints;//,nocharlevel,nocharx,nochary;
    private JComboBox facing;
    private JToggleButton create, nochar;
    private JPanel createpan, center;//,nocharpan;
    private JList itemlist;
    private Vector itemchoose, abilitychoose, abilityauto;
    private int itemindex = -1;
    
    public PartyInfoDialog(DMEditor ed) {
        super((Frame) ed, "Set Party Information", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container cp = getContentPane();
        this.ed = ed;
        
        //location and facing
        JPanel top = new JPanel();
        JPanel toptop = new JPanel();
        JPanel topbottom = new JPanel();
        
        JPanel locationpan = new JPanel();
        Box labelbox = Box.createVerticalBox();
        Box fieldbox = Box.createVerticalBox();
        JLabel levellabel = new JLabel("Level:");
        level = new JTextField("" + ed.partylevel, 3);
        JLabel partyxlabel = new JLabel("X-Coord:");
        partyx = new JTextField("" + ed.partyx, 3);
        JLabel partyylabel = new JLabel("Y-Coord:");
        partyy = new JTextField("" + ed.partyy, 3);
        labelbox.add(levellabel);
        labelbox.add(Box.createVerticalStrut(5));
        labelbox.add(partyxlabel);
        labelbox.add(Box.createVerticalStrut(5));
        labelbox.add(partyylabel);
        fieldbox.add(level);
        fieldbox.add(partyx);
        fieldbox.add(partyy);
        locationpan.add(labelbox);
        locationpan.add(Box.createHorizontalStrut(5));
        locationpan.add(fieldbox);
        
        String[] direcs = {"Facing North", "Facing West", "Facing South", "Facing East"};
        facing = new JComboBox(direcs);
        facing.setSelectedIndex(ed.facing);
        
        JButton frommap = new JButton("From Map...");
        frommap.addActionListener(this);
        
        toptop.add(locationpan);
        toptop.add(Box.createHorizontalStrut(10));
        toptop.add(facing);
        topbottom.add(frommap);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(toptop);
        top.add(topbottom);
        top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Location and Facing"));
        
        //select start style
        center = new JPanel();
        
        //create and nochar toggles
        JPanel topcenter = new JPanel();
        create = new JToggleButton("Allow Create Character");
        nochar = new JToggleButton("Allow Start Without Character");
        create.addActionListener(this);
        nochar.addActionListener(this);
        create.setSelected(ed.create);
        nochar.setSelected(ed.nochar);
        topcenter.add(create);
        topcenter.add(nochar);
        //creation stat points
        Box lbox = Box.createVerticalBox();
        Box fbox = Box.createVerticalBox();
        JLabel lplabel = new JLabel("Level Points:");
        levelpoints = new JTextField("" + ed.levelpoints, 3);
        JLabel hsmplabel = new JLabel("Health/Stamina/Mana Points:");
        hsmpoints = new JTextField("" + ed.hsmpoints, 3);
        JLabel splabel = new JLabel("Stat Points:");
        statpoints = new JTextField("" + ed.statpoints, 3);
        JLabel dplabel = new JLabel("Defense Points:");
        defensepoints = new JTextField("" + ed.defensepoints, 3);
        JLabel iplabel = new JLabel("Items Allowed:");
        itempoints = new JTextField("" + ed.itempoints, 3);
        JLabel aplabel = new JLabel("Abilities Allowed:");
        abilitypoints = new JTextField("" + ed.abilitypoints, 3);
        lbox.add(lplabel);
        lbox.add(Box.createVerticalStrut(5));
        lbox.add(hsmplabel);
        lbox.add(Box.createVerticalStrut(5));
        lbox.add(splabel);
        lbox.add(Box.createVerticalStrut(5));
        lbox.add(dplabel);
        lbox.add(Box.createVerticalStrut(5));
        lbox.add(iplabel);
        lbox.add(Box.createVerticalStrut(5));
        lbox.add(aplabel);
        fbox.add(levelpoints);
        fbox.add(hsmpoints);
        fbox.add(statpoints);
        fbox.add(defensepoints);
        fbox.add(itempoints);
        fbox.add(abilitypoints);
        //creation item choices
        JPanel itempanel = new JPanel();
        itempanel.setLayout(new BoxLayout(itempanel, BoxLayout.Y_AXIS));
        itemchoose = new Vector();
        itemlist = new JList(itemchoose);
        itemlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemlist.addMouseListener(this);
        JScrollPane itempane = new JScrollPane(itemlist);
        itempane.setPreferredSize(new Dimension(200, 120));
        JPanel itembutpan = new JPanel();
        JButton additembut = new JButton("Add/Edit Item");
        JButton removeitembut = new JButton("Remove Item");
        additembut.addActionListener(this);
        removeitembut.addActionListener(this);
        itembutpan.add(additembut);
        itembutpan.add(removeitembut);
        itempanel.add(itempane);
        itempanel.add(itembutpan);
        //creation ability choices
        //JPanel abilitypanel = new JPanel();
        Box abilitypanel = Box.createVerticalBox();
        abilitychoose = new Vector();
        JButton abilitybut = new JButton("Choosable Abilities...");
        abilitybut.addActionListener(this);
        abilitypanel.add(abilitybut);
        abilityauto = new Vector();
        JButton abilitybut2 = new JButton("Automatic Abilities...");
        abilitybut2.addActionListener(this);
        abilitypanel.add(abilitybut2);
        //put creation panel together
        createpan = new JPanel();
        createpan.add(lbox);
        createpan.add(Box.createHorizontalStrut(5));
        createpan.add(fbox);
        createpan.add(itempanel);
        createpan.add(abilitypanel);
        createpan.setVisible(ed.create);
        
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(topcenter);
        center.add(createpan);
        center.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Starting Character"));
        
        //done/cancel
        JPanel bottom = new JPanel();
        JButton donebutton = new JButton("Done");
        JButton cancelbutton = new JButton("Cancel");
        donebutton.addActionListener(this);
        cancelbutton.addActionListener(this);
        bottom.add(donebutton);
        bottom.add(cancelbutton);
        
        cp.add("South", bottom);
        cp.add("North", top);
        cp.add("Center", center);
        
        setSize(640, 480);
        setLocationRelativeTo((Frame) ed);
        
        dispose();
    }
    
    public void show() {
        level.setText("" + ed.partylevel);
        partyx.setText("" + ed.partyx);
        partyy.setText("" + ed.partyy);
        super.show();
    }
    
    
    public void updateInfo() {
        facing.setSelectedIndex(ed.facing);
        levelpoints.setText("" + ed.levelpoints);
        hsmpoints.setText("" + ed.hsmpoints);
        statpoints.setText("" + ed.statpoints);
        defensepoints.setText("" + ed.defensepoints);
        itempoints.setText("" + ed.itempoints);
        abilitypoints.setText("" + ed.abilitypoints);
        itemchoose.clear();
        for (int i = 0; i < DMEditor.itemchoose.size(); i++) {
            itemchoose.add(Item.createCopy((Item) DMEditor.itemchoose.get(i)));
        }
        itemlist.setListData(itemchoose);
        abilitychoose.clear();
        abilityauto.clear();
        for (int i = 0; i < ed.abilitychoose.size(); i++) {
            if (i < ed.abilityauto)
                abilityauto.add(new SpecialAbility((SpecialAbility) ed.abilitychoose.get(i))); //ed.abilityauto is just an int
            else abilitychoose.add(new SpecialAbility((SpecialAbility) ed.abilitychoose.get(i)));
        }
        nochar.setSelected(ed.nochar);
        if (ed.create != create.isSelected()) create.doClick();
        if (ed.partyedititem.isEnabled()) {
            center.setVisible(false);
        } else center.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            int newlevel, newx, newy, newlp = 0, newhsmp = 0, newsp = 0, newdp = 0, newip = 0, newap = 0;
            try {
                newlevel = Integer.parseInt(level.getText());
                newx = Integer.parseInt(partyx.getText());
                newy = Integer.parseInt(partyy.getText());
                if (create != null && create.isSelected()) {
                    newlp = Integer.parseInt(levelpoints.getText());
                    newhsmp = Integer.parseInt(hsmpoints.getText());
                    newsp = Integer.parseInt(statpoints.getText());
                    newdp = Integer.parseInt(defensepoints.getText());
                    newip = Integer.parseInt(itempoints.getText());
                    newap = Integer.parseInt(abilitypoints.getText());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ed, "Invalid Number: " + ex.getMessage(), "Notice", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newlevel < 0 || newx < 0 || newy < 0 || newlevel > ed.MAPLEVELS - 1 || newx > ed.MAPWIDTH - 1 || newy > ed.MAPHEIGHT - 1 || newlp < 0 || newhsmp < 0 || newsp < 0 || newdp < 0 || newip < 0 || newap < 0) {
                JOptionPane.showMessageDialog(ed, "A number is negative or outside the map", "Notice", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (newip > 16) {
                JOptionPane.showMessageDialog(ed, "You may not have more than 16 starting items", "Notice", JOptionPane.ERROR_MESSAGE);
                itempoints.setText("16");
                return;
            }
            MapData[][] tempdata = (MapData[][]) ed.maplevels.get(ed.partylevel);
            tempdata[ed.partyx][ed.partyy].hasParty = false;
            ed.partylevel = newlevel;
            ed.partyx = newx;
            ed.partyy = newy;
            ed.facing = facing.getSelectedIndex();
            tempdata = (MapData[][]) ed.maplevels.get(ed.partylevel);
            tempdata[ed.partyx][ed.partyy].hasParty = true;
            if (center.isVisible()) {
                ed.create = create.isSelected();
                ed.nochar = nochar.isSelected();
                if (ed.create) {
                    ed.levelpoints = newlp;
                    ed.hsmpoints = newhsmp;
                    ed.statpoints = newsp;
                    ed.defensepoints = newdp;
                    ed.itemchoose.clear();
                    for (int i = 0; i < itemchoose.size(); i++) {
                        ed.itemchoose.add(itemchoose.get(i));
                    }
                    if (newip > itemchoose.size()) newip = itemchoose.size();
                    ed.itempoints = newip;
                    ed.abilitychoose.clear();
                    for (int i = 0; i < abilityauto.size(); i++) {
                        ed.abilitychoose.add(abilityauto.get(i));
                    }
                    ed.abilityauto = abilityauto.size();//just an int index
                    for (int i = 0; i < abilitychoose.size(); i++) {
                        ed.abilitychoose.add(abilitychoose.get(i));
                    }
                    if (newap > abilitychoose.size()) newap = abilitychoose.size();
                    ed.abilitypoints = newap;
                }
                //if (!ed.create && !ed.nochar) ed.create = true;
                if (!ed.create && !ed.nochar) ed.nochar = true;
            }
            ed.NEEDSAVE = true;
        } else if (e.getActionCommand().equals("Allow Create Character")) {
            if (!create.isSelected() && !nochar.isSelected()) {
                create.setSelected(true);
                return;
            }
            createpan.setVisible(!createpan.isVisible());
            return;
        } else if (e.getActionCommand().equals("Allow Start Without Character")) {
            if (!create.isSelected() && !nochar.isSelected()) {
                nochar.setSelected(true);
                return;
            }
            return;
        } else if (e.getActionCommand().equals("From Map...")) {
            //MapPoint targ = (DMEditor.getTargetFrame()).getTarget();
            DMEditor.targetframe.show();
            MapPoint targ = DMEditor.targetframe.getTarget();
            if (targ != null) {
                level.setText("" + targ.level);
                partyx.setText("" + targ.x);
                partyy.setText("" + targ.y);
            }
            return;
        } else if (e.getActionCommand().equals("Choosable Abilities...")) {
            new HeroSpecials(ed, abilitychoose);
            return;
        } else if (e.getActionCommand().equals("Automatic Abilities...")) {
            new HeroSpecials(ed, abilityauto);
            return;
        } else if (e.getActionCommand().equals("Add/Edit Item")) {
            if (itemindex == -1) {
                DMEditor.itemwizard.setTitle("Item Wizard");
                DMEditor.itemwizard.show();
                Item tempitem = DMEditor.itemwizard.getItem();
                if (tempitem == null) return;
                itemchoose.add(tempitem);
            } else {
                DMEditor.itemwizard.setTitle("Item Wizard - Change Item");
                DMEditor.itemwizard.setItem((Item) itemchoose.get(itemindex));
                Item tempitem = DMEditor.itemwizard.getItem();
                if (tempitem == null) {
                    itemlist.clearSelection();
                    itemindex = -1;
                    return;
                }
                itemchoose.set(itemindex, tempitem);
            }
            itemlist.setListData(itemchoose);
            itemindex = -1;
            return;
        } else if (e.getActionCommand().equals("Remove Item")) {
            if (itemchoose.size() == 0 || itemindex == -1) return;
            itemchoose.remove(itemindex);
            itemlist.setListData(itemchoose);
            itemindex = -1;
            return;
        } else updateInfo();
        dispose();
    }
    
    public void mousePressed(MouseEvent e) {
        int clickedindex = itemlist.locationToIndex(e.getPoint());
        if (clickedindex == -1 || clickedindex == itemindex) itemlist.clearSelection();
        itemindex = itemlist.getSelectedIndex();
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