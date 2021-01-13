import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class SwitchFinder extends JFrame implements ActionListener, MouseListener {
    
    private Container cp;
    private DMEditor dmed;
    private JTextField targetlevel, targetx, targety;
    private JList foundlist;
    private JScrollPane foundpane;
    private Vector foundvec = new Vector(10);
    private int findlevel, findx, findy;
    
    public SwitchFinder(DMEditor dmed) {
        super("Find Switches/Teleports");
        this.dmed = dmed;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(490, 280);
        cp = getContentPane();
        
        //west panel -> has square coordinates and from map button
        JPanel west = new JPanel();
        Box tlbox = Box.createVerticalBox();
        Box tfbox = Box.createVerticalBox();
        JLabel levellabel = new JLabel(" Level ");
        JLabel xlabel = new JLabel("    X");
        JLabel ylabel = new JLabel("    Y");
        xlabel.setHorizontalAlignment(JLabel.CENTER);
        ylabel.setHorizontalAlignment(JLabel.CENTER);
        targetlevel = new JTextField("0", 3);
        targetx = new JTextField("0", 3);
        targety = new JTextField("0", 3);
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
        JButton lockbut = new JButton("Get Locked");
        lockbut.addActionListener(this);
        JPanel targetbox2 = new JPanel();
        targetbox2.setLayout(new BoxLayout(targetbox2, BoxLayout.Y_AXIS));
        Dimension dim1 = new Dimension(200, 180);
        targetbox2.setPreferredSize(dim1);
        targetbox2.setMaximumSize(dim1);
        targetbox2.add(targetbox);
        targetbox2.add(Box.createVerticalStrut(10));
        targetbox2.add(targetbut);
        targetbox2.add(lockbut);
        targetbox2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Square To Find For"));
        west.add(targetbox2);
        
        //east panel -> has list showing all locations of found item
        JPanel east = new JPanel();
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        foundlist = new JList();
        foundlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        foundlist.setSelectedIndex(0);
        foundlist.addMouseListener(this);
        foundpane = new JScrollPane(foundlist);
        Dimension dim2 = new Dimension(238, 150);
        foundpane.setPreferredSize(dim2);
        foundpane.setMaximumSize(dim2);
        JPanel foundpanel = new JPanel();
        //foundpanel.setPreferredSize(new Dimension(240,180));
        foundpanel.add(foundpane);
        foundpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Locations"));
        east.add(foundpanel);
        JButton gotobut = new JButton("Go To Square");
        gotobut.addActionListener(this);
        east.add(gotobut);
        
        //bottom panel -> find and exit buttons
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Find");
        JButton cancel = new JButton("Exit");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        cp.add("West", west);
        cp.add("South", bottompanel);
        cp.add("East", east);
    }
    
    public void showFinder(boolean locked) {
        if (locked) {
            targetlevel.setText("" + dmed.currentlevel);
            targetx.setText("" + dmed.lockx);
            targety.setText("" + dmed.locky);
            foundvec.clear();
            foundlist.setListData(foundvec);
        }
        show();
    }
    
    private boolean searchMapData(MapData mapdata, int l, int x, int y) {
        boolean found = false;
        if (mapdata.mapchar == '/') {
            //wallswitch
            WallSwitchData wd = (WallSwitchData) mapdata;
            if (wd.targetlevel == findlevel && wd.targetx == findx && wd.targety == findy) {
                foundvec.add("Wall Switch at " + l + "," + x + "," + y);
                found = true;
            } else if (wd.actiontype == 5 || wd.actiontype == 6) {
                if (searchMapData(wd.changeto, wd.targetlevel, wd.targetx, wd.targety)) {
                    foundvec.add(" -- Created by Wall Switch at " + l + "," + x + "," + y);
                    found = true;
                }
                if (wd.actiontype == 5 && wd.switchstate && searchMapData(wd.oldMapObject, wd.targetlevel, wd.targetx, wd.targety)) {
                    foundvec.add(" -- Created by Wall Switch at " + l + "," + x + "," + y);
                    found = true;
                }
            }
        } else if (mapdata.mapchar == 's') {
            //floorswitch
            FloorSwitchData fd = (FloorSwitchData) mapdata;
            if (fd.targetlevel == findlevel && fd.targetx == findx && fd.targety == findy) {
                foundvec.add("Floor Switch at " + l + "," + x + "," + y);
                found = true;
            } else if (fd.actiontype == 5 || fd.actiontype == 6) {
                if (searchMapData(fd.changeto, fd.targetlevel, fd.targetx, fd.targety)) {
                    foundvec.add(" -- Created by Floor Switch at " + l + "," + x + "," + y);
                    found = true;
                }
                if (fd.actiontype == 5 && fd.switchstate && searchMapData(fd.oldMapObject, fd.targetlevel, fd.targetx, fd.targety)) {
                    foundvec.add(" -- Created by Floor Switch at " + l + "," + x + "," + y);
                    found = true;
                }
            }
        } else if (mapdata.mapchar == 't') {
            //teleport
            TeleportData fd = (TeleportData) mapdata;
            if (fd.targetlevel == findlevel && fd.targetx == findx && fd.targety == findy) {
                foundvec.add("Teleport at " + l + "," + x + "," + y);
                found = true;
            }
        } else if (mapdata.mapchar == 'y') {
            //fulya pit
            FulYaPitData fd = (FulYaPitData) mapdata;
            if ((fd.keytarget.level == findlevel && fd.keytarget.x == findx && fd.keytarget.y == findy) || (fd.nonkeytarget.level == findlevel && fd.nonkeytarget.x == findx && fd.nonkeytarget.y == findy)) {
                foundvec.add("FulYa Pit at " + l + "," + x + "," + y);
                found = true;
            }
        } else if (mapdata.mapchar == 'E') {
            //event square
            EventSquareData fd = (EventSquareData) mapdata;
            int i = 0, j = 0;
            while (!found && i < fd.choices.length) {
                while (!found && j < fd.choices[i].actions.size()) {
                    Action a = (Action) fd.choices[i].actions.get(j);
                    if (a.actiontype > 0 && a.actiontype < 4) {
                        MapPoint target = (MapPoint) a.action;
                        if (target.level == findlevel && target.x == findx && target.y == findy) found = true;
                    }
                    j++;
                }
                i++;
            }
            if (found) foundvec.add("EventSquare at " + l + "," + x + "," + y);
        } else if (mapdata.mapchar == 'm') {
            //mirror
            if (((MirrorData) mapdata).target != null && ((MirrorData) mapdata).target.level == findlevel && ((MirrorData) mapdata).target.x == findx && ((MirrorData) mapdata).target.y == findy) {
                foundvec.add("Mirror at " + l + "," + x + "," + y);
                found = true;
            }
        } else if (mapdata.mapchar == '\\') {
            //multiple wallswitch
            for (int i = 0; i < ((MultWallSwitchData) mapdata).switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) ((MultWallSwitchData) mapdata).switchlist.get(i);
                if (searchMapData(fd, l, x, y)) found = true;
            }
        } else if (mapdata.mapchar == 'S') {
            //multiple floorswitch
            for (int i = 0; i < ((MultFloorSwitchData) mapdata).switchlist.size(); i++) {
                FloorSwitchData fd = (FloorSwitchData) ((MultFloorSwitchData) mapdata).switchlist.get(i);
                if (searchMapData(fd, l, x, y)) found = true;
            }
        } else if (mapdata.mapchar == ']' || mapdata.mapchar == 'a') {
            //one-sided alcove or altar of vi
            OneAlcoveData ad = (OneAlcoveData) mapdata;
            if (ad.isSwitch) for (int i = 0; i < ad.alcoveswitchdata.switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) ad.alcoveswitchdata.switchlist.get(i);
                if (searchMapData(fd, l, x, y)) found = true;
            }
            if (found) foundvec.add(" -- Alcove at " + l + "," + x + "," + y);
        } else if (mapdata.mapchar == '[') {
            //4-sided alcove
            AlcoveData ad = (AlcoveData) mapdata;
            if (ad.isSwitch) for (int i = 0; i < ad.alcoveswitchdata.switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) ad.alcoveswitchdata.switchlist.get(i);
                if (searchMapData(fd, l, x, y)) found = true;
            }
            if (found) foundvec.add(" -- Alcove at " + l + "," + x + "," + y);
        } else if (mapdata.mapchar == '}') {
            //sconce
            SconceData sd = (SconceData) mapdata;
            if (sd.isSwitch) for (int i = 0; i < sd.sconceswitch.switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) sd.sconceswitch.switchlist.get(i);
                if (searchMapData(fd, l, x, y)) found = true;
            }
            if (found) foundvec.add(" -- Sconce at " + l + "," + x + "," + y);
        } else if (mapdata.mapchar == 'f') {
            //fountain
            FountainData sd = (FountainData) mapdata;
            if (sd.fountainswitch != null) for (int i = 0; i < sd.fountainswitch.switchlist.size(); i++) {
                WallSwitchData fd = (WallSwitchData) sd.fountainswitch.switchlist.get(i);
                if (searchMapData(fd, l, x, y)) found = true;
            }
            if (found) foundvec.add(" -- Fountain at " + l + "," + x + "," + y);
        }
        return found;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Find")) {
            //find all level,x,y locations of switches and teleports that target selected square
            foundvec.clear();
            findlevel = Integer.parseInt(targetlevel.getText());
            findx = Integer.parseInt(targetx.getText());
            findy = Integer.parseInt(targety.getText());
            if (findlevel < 0 || findx < 0 || findy < 0 || findlevel >= dmed.MAPLEVELS || findx >= dmed.MAPWIDTH || findy >= dmed.MAPHEIGHT) {
                foundvec.add("Illegal Map Coordinate");
                foundlist.setListData(foundvec);
                return;
            }
            MapData[][] maplevel;
            for (int l = 0; l < dmed.MAPLEVELS; l++) {
                maplevel = (MapData[][]) dmed.maplevels.get(l);
                for (int x = 0; x < dmed.MAPWIDTH; x++) {
                    for (int y = 0; y < dmed.MAPHEIGHT; y++) {
                        searchMapData(maplevel[x][y], l, x, y);
                    }
                }
            }
            if (foundvec.isEmpty()) foundvec.add("None Found");
            foundlist.setListData(foundvec);
        } else if (e.getActionCommand().equals("Exit")) {
            hide();
        } else if (e.getActionCommand().equals("Go To Square")) {
            mouseClicked(new MouseEvent(foundlist, MouseEvent.MOUSE_CLICKED, 0L, MouseEvent.BUTTON2_MASK, 0, foundlist.getSelectedIndex() * 19, 2, false));
        } else if (e.getActionCommand().equals("From Map...")) {
            DMEditor.targetframe.show();
            MapPoint targ = DMEditor.targetframe.getTarget();
            if (targ != null) {
                targetlevel.setText("" + targ.level);
                targetx.setText("" + targ.x);
                targety.setText("" + targ.y);
            }
            show();
        } else if (e.getActionCommand().equals("Get Locked")) {
            if (dmed.SQUARELOCKED) {
                targetlevel.setText("" + dmed.currentlevel);
                targetx.setText("" + dmed.lockx);
                targety.setText("" + dmed.locky);
            }
        }
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2 && !SwingUtilities.isRightMouseButton(e)) return;
        String itemtofind = (String) foundvec.get(foundlist.getSelectedIndex());
        if (itemtofind.startsWith("None") || itemtofind.startsWith("Illegal")) return;
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
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}