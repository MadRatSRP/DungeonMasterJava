import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TeleportWizard extends JDialog implements ActionListener {
    private int type, level, x, y;
    private MapData data;
    private boolean reusable = true, visible = true, makessound = true;
    //note: newface = 0,1,2,3 means face that direction, 4 is turn right 90, 5 is turn left 90, 6 is turn 180, 7 is turn random
    private int newface = -1;//-1 no change
    
    JComboBox facelist;
    JTextField levelfield, xfield, yfield, blinkonfield, blinkofffield, blinkcounter, mcountfield, countfield, delay, reset;
    JToggleButton resetcount, isActive, isOn, switchVisible, visbutton, reusebutton, sndbutton, swapplaces;
    JToggleButton[] typebutton;
    
    public TeleportWizard(JFrame f) {
        super(f, "Teleport Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 440);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        JPanel typepanel = new JPanel();
        typepanel.setLayout(new GridLayout(4, 1, 10, 2));
        typebutton = new JToggleButton[4];
        typebutton[0] = new JToggleButton("Party");
        typebutton[1] = new JToggleButton("Monsters");
        typebutton[2] = new JToggleButton("Items");
        typebutton[3] = new JToggleButton("Projectiles");
        typebutton[0].setActionCommand("1");
        typebutton[1].setActionCommand("2");
        typebutton[2].setActionCommand("4");
        typebutton[3].setActionCommand("8");
        typebutton[0].addActionListener(this);
        typebutton[1].addActionListener(this);
        typebutton[2].addActionListener(this);
        typebutton[3].addActionListener(this);
        typepanel.add(typebutton[0]);
        typepanel.add(typebutton[1]);
        typepanel.add(typebutton[2]);
        typepanel.add(typebutton[3]);
        typebutton[0].setSelected(true);
        typebutton[1].setSelected(true);
        typebutton[2].setSelected(true);
        typebutton[3].setSelected(true);
        type = 15;
        
        JPanel featurepanel = new JPanel();
        JPanel featurepanel1 = new JPanel(new GridLayout(7, 1, 10, 2));
        JPanel featurepanel2 = new JPanel(new GridLayout(11, 1, 10, 2));
        visbutton = new JToggleButton("Visible");
        visbutton.addActionListener(this);
        visbutton.setSelected(true);
        sndbutton = new JToggleButton("Makes Sound");
        sndbutton.addActionListener(this);
        sndbutton.setSelected(true);
        isActive = new JToggleButton("Is Blinking");
        isActive.setSelected(false);
        isOn = new JToggleButton("Is On");
        isOn.setSelected(true);
        reusebutton = new JToggleButton("Reusable");
        reusebutton.addActionListener(this);
        reusebutton.setSelected(true);
        String[] facestring = {"No Spin", "Right 90", "Left 90", "180", "North", "South", "East", "West", "Random"};
        facelist = new JComboBox(facestring);
        facelist.setEditable(false);
        facelist.addActionListener(this);
        swapplaces = new JToggleButton("Swaps Mons and Party");
        swapplaces.setSelected(true);
        JLabel blinkonlabel = new JLabel("Blinkrate (On):");
        blinkonfield = new JTextField("0", 3);
        JLabel blinkofflabel = new JLabel("Blinkrate (Off):");
        blinkofffield = new JTextField("0", 3);
        JLabel blinkclabel = new JLabel("Blinkcounter:");
        blinkcounter = new JTextField("0", 3);
        JLabel mcountlabel = new JLabel("Total Switch count:");
        mcountfield = new JTextField("1", 3);
        JLabel countlabel = new JLabel("Remaining Switch count:");
        countfield = new JTextField("1", 3);
        resetcount = new JToggleButton("Count Resets");
        //resetcount.setSelected(true);
        featurepanel1.add(visbutton);
        featurepanel1.add(sndbutton);
        featurepanel1.add(swapplaces);
        featurepanel1.add(isActive);
        featurepanel1.add(isOn);
        featurepanel1.add(reusebutton);
        featurepanel1.add(facelist);
        featurepanel2.add(blinkonlabel);
        featurepanel2.add(blinkonfield);
        featurepanel2.add(blinkofflabel);
        featurepanel2.add(blinkofffield);
        featurepanel2.add(blinkclabel);
        featurepanel2.add(blinkcounter);
        featurepanel2.add(mcountlabel);
        featurepanel2.add(mcountfield);
        featurepanel2.add(countlabel);
        featurepanel2.add(countfield);
        featurepanel2.add(resetcount);
        featurepanel.add(featurepanel1);
        featurepanel.add(Box.createRigidArea(new Dimension(10, 100)));
        featurepanel.add(featurepanel2);
        
        Box tlbox = Box.createVerticalBox();
        Box tfbox = Box.createVerticalBox();
        JLabel levellabel = new JLabel(" Level ");
        JLabel xlabel = new JLabel("    X");
        JLabel ylabel = new JLabel("    Y");
        xlabel.setHorizontalAlignment(JLabel.CENTER);
        ylabel.setHorizontalAlignment(JLabel.CENTER);
        levelfield = new JTextField("" + level, 3);
        xfield = new JTextField("" + x, 3);
        yfield = new JTextField("" + y, 3);
        tlbox.add(levellabel);
        tlbox.add(Box.createVerticalStrut(14));
        tlbox.add(xlabel);
        tlbox.add(Box.createVerticalStrut(15));
        tlbox.add(ylabel);
        tfbox.add(levelfield);
        tfbox.add(Box.createVerticalStrut(10));
        tfbox.add(xfield);
        tfbox.add(Box.createVerticalStrut(10));
        tfbox.add(yfield);
        Box targetbox = Box.createHorizontalBox();
        targetbox.add(tlbox);
        targetbox.add(Box.createHorizontalStrut(5));
        targetbox.add(tfbox);
        JButton targetbut = new JButton("From Map...");
        targetbut.addActionListener(this);
        //Box targetbox2 = Box.createVerticalBox();
        JPanel targetbox2 = new JPanel();
        targetbox2.setLayout(new BoxLayout(targetbox2, BoxLayout.Y_AXIS));
        targetbox2.add(targetbox);
        targetbox2.add(Box.createVerticalStrut(10));
        targetbox2.add(targetbut);
        targetbox2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Target"));
        
        JPanel resetdelay = new JPanel();
        resetdelay.setLayout(new BoxLayout(resetdelay, BoxLayout.Y_AXIS));
        switchVisible = new JToggleButton("Switch Visible");
        reset = new JTextField("0", 4);
        delay = new JTextField("0", 4);
        resetdelay.add(switchVisible);
        resetdelay.add(new JLabel("Reset Count (0 for none):"));
        resetdelay.add(reset);
        resetdelay.add(new JLabel("Delay Count (0 for none):"));
        resetdelay.add(delay);
        resetdelay.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Switched Type"));
        
        Box rightbox = Box.createVerticalBox();
        rightbox.add(targetbox2);
        rightbox.add(Box.createVerticalStrut(10));
        rightbox.add(resetdelay);
        
        JPanel centerpanel = new JPanel();
        centerpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        //centerpanel.add(Box.createHorizontalGlue());
        centerpanel.add(typepanel);
        centerpanel.add(featurepanel);
        //centerpanel.add(resetdelay);
        //centerpanel.add(targetbox);
        //centerpanel.add(targetbox2);
        centerpanel.add(rightbox);
        //centerpanel.add(Box.createHorizontalGlue());
        
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        cp.add("Center", centerpanel);
        cp.add("South", bottompanel);
    }
    
    public void setData(MapData data, int level, int x, int y) {
        this.level = level;
        this.x = x;
        this.y = y;
        if (data.mapchar == 't') {
            TeleportData tdata = (TeleportData) data;
            type = tdata.type;
            typebutton[0].setSelected((type & 1) != 0);
            typebutton[1].setSelected((type & 2) != 0);
            typebutton[2].setSelected((type & 4) != 0);
            typebutton[3].setSelected((type & 8) != 0);
            if (!tdata.isVisible) {
                visbutton.setSelected(false);
                visible = false;
            } else {
                visbutton.setSelected(true);
                visible = true;
            }
            if (!tdata.playsound) {
                sndbutton.setSelected(false);
                makessound = false;
            } else {
                sndbutton.setSelected(true);
                makessound = true;
            }
            if (!tdata.isReusable) {
                reusebutton.setSelected(false);
                reusable = false;
            } else {
                reusebutton.setSelected(true);
                reusable = true;
            }
            swapplaces.setSelected(tdata.swapplaces);
            switchVisible.setSelected(tdata.switchVisible);
            delay.setText("" + tdata.delay);
            reset.setText("" + tdata.reset);
            isActive.setSelected(tdata.isActive);
            isOn.setSelected(tdata.isOn);
            blinkonfield.setText("" + tdata.blinkrateon);
            blinkofffield.setText("" + tdata.blinkrateoff);
            blinkcounter.setText("" + tdata.blinkcounter);
            mcountfield.setText("" + (tdata.maxcount + 1));
            countfield.setText("" + (tdata.count + 1));
            resetcount.setSelected(tdata.resetcount);
            levelfield.setText("" + tdata.targetlevel);
            xfield.setText("" + tdata.targetx);
            yfield.setText("" + tdata.targety);
            newface = tdata.newface;
            if (newface < 0) facelist.setSelectedIndex(0);
            else if (newface == 4) facelist.setSelectedIndex(1);
            else if (newface == 5) facelist.setSelectedIndex(2);
            else if (newface == 6) facelist.setSelectedIndex(3);
            else if (newface == 0) facelist.setSelectedIndex(4);
            else if (newface == 2) facelist.setSelectedIndex(5);
            else if (newface == 3) facelist.setSelectedIndex(6);
            else if (newface == 1) facelist.setSelectedIndex(7);
            else facelist.setSelectedIndex(8);
        } else {
            levelfield.setText("" + level);
            xfield.setText("" + x);
            yfield.setText("" + y);
        }
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Visible")) {
            visible = !visible;
        } else if (e.getActionCommand().equals("Makes Sound")) {
            makessound = !makessound;
        } else if (e.getActionCommand().equals("Reusable")) {
            reusable = !reusable;
        } else if (e.getSource().equals(facelist)) {
            String s = (String) facelist.getSelectedItem();
            if (s.equals("No Spin")) newface = -1;
            else if (s.equals("Right 90")) newface = 4;
            else if (s.equals("Left 90")) newface = 5;
            else if (s.equals("180")) newface = 6;
            else if (s.equals("Random")) newface = 7;
            else if (s.equals("North")) newface = 0;
            else if (s.equals("South")) newface = 2;
            else if (s.equals("East")) newface = 3;
            else newface = 1;
        } else if (e.getActionCommand().equals("From Map...")) {
            DMEditor.targetframe.show();
            MapPoint targ = DMEditor.targetframe.getTarget();
            if (targ != null) {
                levelfield.setText("" + targ.level);
                xfield.setText("" + targ.x);
                yfield.setText("" + targ.y);
            }
        } else if (e.getActionCommand().equals("Done")) {
            int targetlevel = Integer.parseInt(levelfield.getText());
            int targetx = Integer.parseInt(xfield.getText());
            int targety = Integer.parseInt(yfield.getText());
            int blinkrateon = Integer.parseInt(blinkonfield.getText());
            int blinkrateoff = Integer.parseInt(blinkofffield.getText());
            int d = Integer.parseInt(delay.getText());
            int r = Integer.parseInt(reset.getText());
            boolean isSwitched = false;
            if (d > 0 || r > 0) isSwitched = true;
            else switchVisible.setSelected(false);
            data = new TeleportData(level, x, y, type, targetlevel, targetx, targety, newface, blinkrateon, blinkrateoff, Integer.parseInt(blinkcounter.getText()), isSwitched, switchVisible.isSelected(), d, r, reusable, visible, isActive.isSelected(), isOn.isSelected(), makessound, swapplaces.isSelected(), Integer.parseInt(mcountfield.getText()) - 1, Integer.parseInt(countfield.getText()) - 1, resetcount.isSelected(), false, 0, false, 0, false);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else {
            int t = Integer.parseInt(e.getActionCommand());
            if (((JToggleButton) e.getSource()).isSelected()) type += t;
            else type -= t;
        }
    }
    
    public MapData getData() {
        return data;
    }
}
