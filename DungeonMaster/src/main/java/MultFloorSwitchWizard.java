import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.util.ArrayList;

class MultFloorSwitchWizard extends JDialog implements ActionListener, MouseListener {
    private MapData data;
    private int level, x, y, switchindex = -1;
    private JList switchlist;
    private ArrayList switches = new ArrayList(3);
    private Vector switchnames = new Vector(3);
    private JToggleButton isVisible;//,makesSound;
    private JFrame frame;
    
    public MultFloorSwitchWizard(JFrame f, MapData data, int level, int x, int y) {
        super(f, "Multiple Floor Switches Wizard", true);
        frame = f;
        this.level = level;
        this.x = x;
        this.y = y;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 250);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //switch panel
        JPanel switchpanel = new JPanel();
        switchlist = new JList();
        switchlist.setVisibleRowCount(6);
        switchlist.addMouseListener(this);
        Box switchbox = Box.createVerticalBox();
        JScrollPane switchpane = new JScrollPane(switchlist);
        switchpane.setPreferredSize(new Dimension(240, 120));
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
        
        //east panel
        Box east = Box.createVerticalBox();
        JPanel setpan = new JPanel();
        //setpan.setLayout(new GridLayout(3,1));
        setpan.setPreferredSize(new Dimension(120, 50));
        isVisible = new JToggleButton("Visible");
        //makesSound = new JToggleButton("Makes Sound");
        setpan.add(isVisible);
        //setpan.add(makesSound);
        isVisible.setSelected(true);
        //makesSound.setSelected(true);
        east.add(Box.createVerticalGlue());
        east.add(setpan);
        east.add(Box.createVerticalGlue());
        
        //done/cancel
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        if (data.mapchar == 'S') {
            MultFloorSwitchData ws = (MultFloorSwitchData) data;
            FloorSwitchData sdata;
            for (int i = 0; i < ws.switchlist.size(); i++) {
                sdata = (FloorSwitchData) ws.switchlist.get(i);
                switches.add(sdata);
                switchnames.add("Targets: " + sdata.targetlevel + "," + sdata.targetx + "," + sdata.targety);
            }
            switchlist.setListData(switchnames);
            if (!ws.haspic) isVisible.setSelected(false);
            //if (ws.playsound) makesSound.setSelected(true);
        } else if (data.mapchar == 's') {
            switches.add(data);
            switchnames.add("Targets: " + ((FloorSwitchData) data).targetlevel + "," + ((FloorSwitchData) data).targetx + "," + ((FloorSwitchData) data).targety);
            switchlist.setListData(switchnames);
        }
        
        cp.add("Center", centerpanel);
        cp.add("East", east);
        cp.add("South", bottompanel);
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
            if (switches.size() < 2) return;
            //data = new MultFloorSwitchData(new MapPoint(level,x,y),isVisible.isSelected(),makesSound.isSelected(),switches);
            data = new MultFloorSwitchData(new MapPoint(level, x, y), isVisible.isSelected(), switches);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("Add/Edit Switch")) {
            FloorSwitchData temps;
            int index = switchlist.getSelectedIndex();
            if (index == -1) {
                temps = (FloorSwitchData) (new FloorSwitchWizard(frame, null, level, x, y)).getData();
                if (temps == null) return;
                switches.add(temps);
                switchnames.add("Targets: " + temps.targetlevel + "," + temps.targetx + "," + temps.targety);
                switchlist.setListData(switchnames);
            } else {
                temps = (FloorSwitchData) (new FloorSwitchWizard(frame, (FloorSwitchData) switches.get(index), level, x, y)).getData();
                if (temps == null) {
                    switchlist.clearSelection();
                    switchindex = -1;
                    return;
                }
                switches.set(index, temps);
                switchnames.set(index, "Targets: " + temps.targetlevel + "," + temps.targetx + "," + temps.targety);
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