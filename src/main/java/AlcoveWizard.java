import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AlcoveWizard extends JDialog implements ActionListener {
    private int side, level, x, y;
    private MapData data;
    private MultWallSwitchData alcoveswitch;
    private JToggleButton onealc, vialtar, manyalc;
    private JToggleButton[] sidebutton = new JToggleButton[4];
    private JLabel hasswitch;
    private JFrame frame;
    public static final String[] sides = {"N", "W", "S", "E"};
    
    public AlcoveWizard(JFrame f) {
        super(f, "Alcove Wizard", true);
        frame = f;
        this.level = level;
        this.x = x;
        this.y = y;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        JPanel typepanel = new JPanel();
        typepanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup onemanygrp = new ButtonGroup();
        onealc = new JToggleButton("1-Sided");
        vialtar = new JToggleButton("VI Altar");
        manyalc = new JToggleButton("4-Sided");
        onealc.addActionListener(this);
        vialtar.addActionListener(this);
        manyalc.addActionListener(this);
        onemanygrp.add(onealc);
        onemanygrp.add(vialtar);
        onemanygrp.add(manyalc);
        typepanel.add(onealc);
        typepanel.add(vialtar);
        typepanel.add(manyalc);
        onealc.setSelected(true);
        
        JPanel sidepanel = new JPanel();
        sidepanel.setPreferredSize(new Dimension(160, 140));
        sidepanel.setMaximumSize(new Dimension(160, 140));
        JLabel openinglabel = new JLabel("Opening is to the:");
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
        sidepanel.add(openinglabel);
        sidepanel.add(sidebutton[0]);
        sidepanel.add(sidebutton[1]);
        sidepanel.add(sidebutton[2]);
        sidepanel.add(sidebutton[3]);
        sidebutton[2].setSelected(true);
        
        //switch panel
        JPanel east = new JPanel();
        JPanel switchpanel = new JPanel();
        switchpanel.setLayout(new GridLayout(3, 1));
        switchpanel.setPreferredSize(new Dimension(140, 70));
        hasswitch = new JLabel("No Switch");
        JButton addswitch = new JButton("Add/Edit Switch");
        JButton removeswitch = new JButton("Remove Switch");
        addswitch.addActionListener(this);
        removeswitch.addActionListener(this);
        switchpanel.add(hasswitch);
        switchpanel.add(addswitch);
        switchpanel.add(removeswitch);
        east.add(switchpanel);
        
        JPanel centerpanel = new JPanel();
        centerpanel.add(sidepanel);
        
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
        if (data.mapchar == '[') {
            manyalc.doClick();
            if (((AlcoveData) data).isSwitch) {
                alcoveswitch = ((AlcoveData) data).alcoveswitchdata;
                hasswitch.setText("Has Switch");
            } else {
                alcoveswitch = null;
                hasswitch.setText("No Switch");
            }
        } else if (data instanceof OneAlcoveData) {
            if (data.mapchar == ']') onealc.doClick();
            else vialtar.doClick();
            sidebutton[(((OneAlcoveData) data).side + 2) % 4].doClick();
            if (((OneAlcoveData) data).isSwitch) {
                alcoveswitch = ((OneAlcoveData) data).alcoveswitchdata;
                hasswitch.setText("Has Switch");
            } else {
                alcoveswitch = null;
                hasswitch.setText("No Switch");
            }
        } else {
            alcoveswitch = null;
            hasswitch.setText("No Switch");
        }
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("4-Sided")) {
            //disable side chooser
            sidebutton[0].setEnabled(false);
            sidebutton[1].setEnabled(false);
            sidebutton[2].setEnabled(false);
            sidebutton[3].setEnabled(false);
        } else if (e.getActionCommand().equals("1-Sided") || e.getActionCommand().equals("VI Altar")) {
            //enable side chooser
            sidebutton[0].setEnabled(true);
            sidebutton[1].setEnabled(true);
            sidebutton[2].setEnabled(true);
            sidebutton[3].setEnabled(true);
        } else if (e.getActionCommand().equals("North")) {
            side = 2;
        } else if (e.getActionCommand().equals("South")) {
            side = 0;
        } else if (e.getActionCommand().equals("East")) {
            side = 1;
        } else if (e.getActionCommand().equals("West")) {
            side = 3;
        } else if (e.getActionCommand().equals("Done")) {
            if (onealc.isSelected()) {
                if (alcoveswitch == null) data = new OneAlcoveData(side);
                else data = new OneAlcoveData(side, alcoveswitch);
            } else if (vialtar.isSelected()) {
                if (alcoveswitch == null) data = new AltarData(side);
                else data = new AltarData(side, alcoveswitch);
            } else {
                if (alcoveswitch == null) data = new AlcoveData();
                else data = new AlcoveData(alcoveswitch);
            }
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("Add/Edit Switch")) {
            int alctype = 0;
            if (manyalc.isSelected()) alctype = 1;
            MultWallSwitchData tempdata = (MultWallSwitchData) (new MultWallSwitchWizard(frame, alcoveswitch, level, x, y, false, alctype, side)).getData();
            if (tempdata != null) {
                alcoveswitch = tempdata;
                hasswitch.setText("Has Switch");
            }
        } else if (e.getActionCommand().equals("Remove Switch")) {
            if (hasswitch.getText().equals("No Switch")) return;
            alcoveswitch = null;
            hasswitch.setText("No Switch");
        }
    }
    
    public MapData getData() {
        return data;
    }
}
