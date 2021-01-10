import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.util.ArrayList;

class MultWallSwitchWizard extends JDialog implements ActionListener, MouseListener {
    private MapData data;
    private int level, x, y, side, pictype = 0, picnum = 0, switchindex = -1;
    private int alcove;
    private JList switchlist;
    private ArrayList switches = new ArrayList(3);
    private Vector switchnames = new Vector(3);
    private JPanel typepanel, buttonpanel, keypanel, switchpanel;
    private JToggleButton buttonbut, keybut, coinbut;
    private JFrame frame;
    
    public MultWallSwitchWizard(JFrame f, MapData data, int level, int x, int y) {
        this(f, data, level, x, y, true, -1, 0);
    }
    
    public MultWallSwitchWizard(JFrame f, MapData data, int level, int x, int y, boolean pic, int alcove, int side) {
        super(f, "Multiple Wall Switches Wizard", true);
        frame = f;
        this.level = level;
        this.x = x;
        this.y = y;
        this.alcove = alcove;
        this.side = side;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 480);//520
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //pictype label
        JPanel piclabelpan = new JPanel();
        piclabelpan.setPreferredSize(new Dimension(200, 20));
        JLabel piclabel = new JLabel("Picture Used:");
        piclabelpan.add(piclabel);
        
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
        //buttonpanel.setPreferredSize(new Dimension(200,300));
        JPanel buttonpics = new JPanel();
        
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
        buttonpane.setPreferredSize(new Dimension(200, 165));
        buttonpanel.add(buttonpane);
        
        //buttonpanel.add(buttonpics);
        
        //key panel
        keypanel = new JPanel();
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
        keypanel.add(keypane);
        keypanel.setVisible(false);
        
        
        //switch panel
        switchpanel = new JPanel();
        switchlist = new JList();
        switchlist.setVisibleRowCount(6);
        switchlist.addMouseListener(this);
        Box switchbox = Box.createVerticalBox();
        JScrollPane switchpane = new JScrollPane(switchlist);
        switchpane.setPreferredSize(new Dimension(380, 120));//240
        switchbox.add(Box.createVerticalStrut(20));
        switchbox.add(new JLabel("Switches:"));
        switchbox.add(switchpane);
        Box switchbutbox = Box.createVerticalBox();
        JButton addbut = new JButton("Add/Edit Switch");
        JButton removebut = new JButton("Remove Switch");
        JButton moveupbut = new JButton("Move Switch Up");
        JButton movedownbut = new JButton("Move Switch Down");
        Font fnt = addbut.getFont().deriveFont(9.0f);
        addbut.setFont(fnt);
        removebut.setFont(fnt);
        moveupbut.setFont(fnt);
        movedownbut.setFont(fnt);
        addbut.setMargin(new Insets(0, 5, 0, 5));
        removebut.setMargin(new Insets(0, 5, 0, 5));
        moveupbut.setMargin(new Insets(0, 5, 0, 5));
        movedownbut.setMargin(new Insets(0, 5, 0, 5));
        addbut.addActionListener(this);
        removebut.addActionListener(this);
        moveupbut.addActionListener(this);
        movedownbut.addActionListener(this);
        switchbutbox.add(Box.createVerticalStrut(20));
        switchbutbox.add(addbut);
        switchbutbox.add(removebut);
        switchbutbox.add(Box.createVerticalStrut(5));
        switchbutbox.add(moveupbut);
        switchbutbox.add(movedownbut);
        switchpanel.add(switchbox);
        switchpanel.add(switchbutbox);
        
        //center panel
        JPanel centerpanel = new JPanel();
        centerpanel.add(switchpanel);
        centerpanel.add(piclabelpan);
        centerpanel.add(typepanel);
        centerpanel.add(buttonpanel);
        centerpanel.add(keypanel);
        
        //side chooser
        Box east = Box.createVerticalBox();
        JPanel eastpanel = new JPanel();
        eastpanel.setPreferredSize(new Dimension(140, 70));
        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new GridLayout(2, 2));
        sidepanel.setPreferredSize(new Dimension(140, 50));
        sidepanel.setMaximumSize(new Dimension(140, 50));
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
        JLabel sidelabel = new JLabel("Faces:");
        sidelabel.setHorizontalAlignment(JLabel.CENTER);
        sidelabelpanel.add(sidelabel);
        eastpanel.add(sidelabelpanel);
        eastpanel.add(sidepanel);
        east.add(Box.createVerticalGlue());
        east.add(eastpanel);
        east.add(Box.createVerticalGlue());
        
        //done/cancel
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        if (data != null && data.mapchar == '\\') {
            MultWallSwitchData ws = (MultWallSwitchData) data;
            if (ws.pictype == 0) {
                buttons[ws.picnumber].doClick();
                JViewport viewport = buttonpane.getViewport();
                viewport.setViewPosition(new Point(0, ws.picnumber * 27));
            } else if (ws.pictype == 1) {
                keybut.doClick();
                keys[ws.picnumber].doClick();
                JViewport viewport = keypane.getViewport();
                viewport.setViewPosition(new Point(0, ws.picnumber * 27));
            } else coinbut.doClick();
            sidebutton[(ws.side + 2) % 4].doClick();
            WallSwitchData sdata;
            for (int i = 0; i < ws.switchlist.size(); i++) {
                sdata = (WallSwitchData) ws.switchlist.get(i);
                switches.add(sdata);
                //switchnames.add("Targets: "+sdata.targetlevel+","+sdata.targetx+","+sdata.targety);
                switchnames.add(sdata.toString() + " Targets: " + sdata.targetlevel + "," + sdata.targetx + "," + sdata.targety);
            }
            switchlist.setListData(switchnames);
        } else if (data != null && data.mapchar == '/') {
            switches.add(data);
            switchnames.add(data.toString() + " Targets: " + ((WallSwitchData) data).targetlevel + "," + ((WallSwitchData) data).targetx + "," + ((WallSwitchData) data).targety);
            switchlist.setListData(switchnames);
        }
        
        cp.add("Center", centerpanel);
        cp.add("East", east);
        cp.add("South", bottompanel);
        if (!pic) {
            east.setVisible(false);
            piclabelpan.setVisible(false);
            typepanel.setVisible(false);
            buttonpanel.setVisible(false);
            keypanel.setVisible(false);
        }
        show();
    }
    
    public void mousePressed(MouseEvent e) {
        int clickedindex = switchlist.locationToIndex(e.getPoint());
        if (clickedindex == -1 || clickedindex == switchindex) switchlist.clearSelection();
        switchindex = switchlist.getSelectedIndex();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            //make sure switches has >1 switch, go thru and make sure all have correct side
            if (switches.size() < 1) return;
                        /*//set sides to match picture
                        WallSwitchData tempdata;
                        for (int i=0;i<switches.size();i++) {
                                tempdata = (WallSwitchData)switches.get(i);
                                tempdata.side=side;
                        }
                        */
            data = new MultWallSwitchData(new MapPoint(level, x, y), side, pictype, picnum, switches);
            if (alcove <= 0) ((MultWallSwitchData) data).setFacing(side);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("Button")) {
            pictype = 0;
            keypanel.setVisible(false);
            buttonpanel.setVisible(true);
        } else if (e.getActionCommand().equals("Key")) {
            pictype = 1;
            buttonpanel.setVisible(false);
            keypanel.setVisible(true);
        } else if (e.getActionCommand().equals("Coin")) {
            pictype = 2;
            buttonpanel.setVisible(false);
            keypanel.setVisible(false);
        } else if (e.getActionCommand().equals("Add/Edit Switch")) {
            WallSwitchData temps;
            int index = switchlist.getSelectedIndex();
            if (index == -1) {
                temps = (WallSwitchData) (new WallSwitchWizard(frame, null, level, x, y, true, alcove, side, pictype)).getData();
                if (temps == null) return;
                switches.add(temps);
                //switchnames.add("Targets: "+temps.targetlevel+","+temps.targetx+","+temps.targety);
                switchnames.add(temps.toString() + " Targets: " + temps.targetlevel + "," + temps.targetx + "," + temps.targety);
                switchlist.setListData(switchnames);
            } else {
                temps = (WallSwitchData) (new WallSwitchWizard(frame, (WallSwitchData) switches.get(index), level, x, y, true, alcove, side, pictype)).getData();
                if (temps == null) {
                    switchlist.clearSelection();
                    switchindex = -1;
                    return;
                }
                switches.set(index, temps);
                //switchnames.set(index,"Targets: "+temps.targetlevel+","+temps.targetx+","+temps.targety);
                switchnames.set(index, temps.toString() + " Targets: " + temps.targetlevel + "," + temps.targetx + "," + temps.targety);
                switchlist.setListData(switchnames);
                switchindex = -1;
            }
        } else if (e.getActionCommand().equals("Remove Switch")) {
            if (switchnames.size() == 0 || switchlist.isSelectionEmpty()) return;
            int index = switchlist.getSelectedIndex();
            switches.remove(index);
            switchnames.removeElementAt(index);
            switchlist.setListData(switchnames);
            switchindex = -1;
        } else if (e.getActionCommand().endsWith("Up")) {
            if (switchindex > 0) {
                Object c = switches.remove(switchindex);
                Object d = switchnames.remove(switchindex);
                switches.add(switchindex - 1, c);
                switchnames.add(switchindex - 1, d);
                switchlist.setListData(switchnames);
                switchindex = -1;
            }
        } else if (e.getActionCommand().endsWith("Down")) {
            if (switchindex != -1 && switches.size() > 1 && switchindex < switches.size() - 1) {
                Object c = switches.remove(switchindex);
                Object d = switchnames.remove(switchindex);
                switches.add(switchindex + 1, c);
                switchnames.add(switchindex + 1, d);
                switchlist.setListData(switchnames);
                switchindex = -1;
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