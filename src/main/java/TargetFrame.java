import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

class TargetFrame extends JDialog implements ActionListener, MouseListener, MouseMotionListener, WindowListener {
    
    private int oldlevel, currentlevel, currentx = 0, currenty = 0;
    private boolean didchoose = false;
    private boolean oldzoom;
    private JToggleButton zoombutton;
    private JLabel statusbar = new JLabel();
    private MapPanel mappanel;
    private Box hspacebox;
    private JScrollPane mpane;
    private DMEditor dmed;
    
    public TargetFrame(DMEditor dmed) {
        super(dmed, "Targets:", true);
        this.dmed = dmed;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(this);
        setSize(800, 600);
        setLocationRelativeTo(dmed);
        Container cp = getContentPane();
        
        //try {
        //java.io.OutputStream out = new java.io.FileOutputStream("tframe.log");
        //System.setErr(new java.io.PrintStream(out,true));
        //} catch (Exception e) {}
        
        //map panel
        //mappanel = new MapPanel(dmed);
        mappanel = dmed.mappanel;
        hspacebox = Box.createHorizontalBox();
        //hspacebox.add(mappanel);
        hspacebox.add(Box.createHorizontalGlue());
        MyMapPanel vspacebox = new MyMapPanel();
        vspacebox.add(hspacebox);
        vspacebox.add(Box.createVerticalGlue());
        mpane = new JScrollPane(vspacebox);
        mpane.setPreferredSize(new Dimension(640, 400));
        
        //zoom, up and down level buttons
        JPanel buttonpan = new JPanel();
        zoombutton = new JToggleButton(new ImageIcon("Icons" + File.separator + "zoom.gif"));
        zoombutton.setPreferredSize(new Dimension(42, 42));
        zoombutton.setMinimumSize(new Dimension(42, 42));
        zoombutton.setMaximumSize(new Dimension(42, 42));
        zoombutton.setActionCommand("Zoom");
        zoombutton.addActionListener(this);
        zoombutton.setSelected(!dmed.ZOOMING);
        buttonpan.add(zoombutton);
        JButton uplevelbutton = new JButton(new ImageIcon("Icons" + File.separator + "up.gif"));
        uplevelbutton.setPreferredSize(new Dimension(36, 42));
        uplevelbutton.setActionCommand("Up");
        uplevelbutton.addActionListener(this);
        buttonpan.add(uplevelbutton);
        JButton downlevelbutton = new JButton(new ImageIcon("Icons" + File.separator + "down.gif"));
        downlevelbutton.setPreferredSize(new Dimension(36, 42));
        downlevelbutton.setActionCommand("Down");
        downlevelbutton.addActionListener(this);
        buttonpan.add(downlevelbutton);
        
        statusbar.setPreferredSize(new Dimension(640, 20));
        statusbar.setHorizontalAlignment(SwingConstants.CENTER);
        statusbar.setForeground(Color.black);
        cp.add("North", buttonpan);
        cp.add("Center", mpane);
        cp.add("South", statusbar);
        
        pack();
        //mappanel.createOff();
    }
    
    public void show() {
        mappanel.removeMouseListener(dmed.mapclick);
        mappanel.removeMouseMotionListener(dmed.mapclick);
        hspacebox.add(mappanel);
        mappanel.addMouseListener(this);
        mappanel.addMouseMotionListener(this);
        oldlevel = dmed.currentlevel;
        oldzoom = dmed.ZOOMING;
        zoombutton.setSelected(!oldzoom);
        currentlevel = dmed.currentlevel;
        super.show();
    }
    
    public MapPoint getTarget() {
                /*
                mappanel.removeMouseListener(this);
                mappanel.removeMouseMotionListener(this);
                mappanel.addMouseListener(dmed.mapclick);
                mappanel.addMouseMotionListener(dmed.mapclick);
                dmed.mapdata = (MapData[][])dmed.maplevels.get(oldlevel);
                dmed.ZOOMING = oldzoom;
                dmed.currentlevel = oldlevel;
                mappanel.setZoom();
                mappanel.repaint();
                dmed.hspacebox.add(mappanel);
                */
        if (didchoose) {
            didchoose = false;
            return new MapPoint(currentlevel, currentx, currenty);
        } else return null;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Up")) {
            if (dmed.currentlevel == 0) return;
            mappanel.setVisible(false);
            dmed.currentlevel--;
            currentlevel--;
            dmed.mapdata = (MapData[][]) dmed.maplevels.get(dmed.currentlevel);
            mappanel.clearTargets();
            mappanel.repaint();
            mappanel.setVisible(true);
        } else if (e.getActionCommand().equals("Down")) {
            if (dmed.currentlevel == dmed.MAPLEVELS - 1) return;
            mappanel.setVisible(false);
            dmed.currentlevel++;
            currentlevel++;
            dmed.mapdata = (MapData[][]) dmed.maplevels.get(dmed.currentlevel);
            mappanel.clearTargets();
            mappanel.repaint();
            mappanel.setVisible(true);
        } else {
            //zoom
            dmed.ZOOMING = !dmed.ZOOMING;
            mappanel.setZoom();
            mappanel.invalidate();
            //vspacebox.invalidate();
            mpane.validate();
            //spane.validate();
            mappanel.repaint();
        }
    }
    
    //mouse entered a square
    public void mouseMoved(MouseEvent e) {
        int newx, newy;
        if (!dmed.ZOOMING) {
            newx = e.getX() / 33;
            newy = e.getY() / 33;
        } else {
            newx = e.getX() / 17;
            newy = e.getY() / 17;
        }
        if (newx == currentx && newy == currenty || newx >= dmed.MAPWIDTH || newy >= dmed.MAPHEIGHT) return;
        currentx = newx;
        currenty = newy;
        boolean dopaint = mappanel.clearTargets();//doExit();
        boolean dopaint2 = mappanel.doTargets(dmed.mapdata[currentx][currenty], currentx, currenty);//doEnter(dmed.mapdata[currentx][currenty],currentx,currenty);
        if (dopaint || dopaint2) mappanel.forcePaint();
        statusbar.setText(dmed.mapdata[currentx][currenty] + " at " + currentlevel + "," + currentx + "," + currenty);
    }
    
    //clicked on a square - that square will be the target
    public void mousePressed(MouseEvent e) {
        didchoose = true;
        dispose();
    }
    
    public void mouseExited(MouseEvent e) {
        currentx = -1;
        currenty = -1;
        mappanel.clearTargets();
        mappanel.forcePaint();
    }
    
    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }
    
    public void windowClosing(WindowEvent e) {
    }
    
    public void windowClosed(WindowEvent e) {
        mappanel.removeMouseListener(this);
        mappanel.removeMouseMotionListener(this);
        mappanel.addMouseListener(dmed.mapclick);
        mappanel.addMouseMotionListener(dmed.mapclick);
        dmed.mapdata = (MapData[][]) dmed.maplevels.get(oldlevel);
        dmed.ZOOMING = oldzoom;
        dmed.currentlevel = oldlevel;
        dmed.mapdata = (MapData[][]) dmed.maplevels.get(dmed.currentlevel);
        dmed.hspacebox.add(mappanel);
        mappanel.setZoom();
        mappanel.repaint();
    }
    
    //necessary for interface completeness
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseDragged(MouseEvent e) {
    }
    
    public void windowOpened(WindowEvent e) {
    }
    
    public void windowIconified(WindowEvent e) {
    }
    
    public void windowDeiconified(WindowEvent e) {
    }
    
    public void windowActivated(WindowEvent e) {
    }
    
    public void windowDeactivated(WindowEvent e) {
    }
}
