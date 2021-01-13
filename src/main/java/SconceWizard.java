import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SconceWizard extends JDialog implements ActionListener {
    private int side, level, x, y;
    private MapData data;
    private MultWallSwitchData sconceswitch;
    private JToggleButton[] sidebutton = new JToggleButton[4];
    private JToggleButton torchbut;
    private JLabel hasswitch;
    private JFrame frame;
    
    public SconceWizard(JFrame f) {
        super(f, "Sconce Wizard", true);
        frame = f;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        JPanel torchpanel = new JPanel();
        torchbut = new JToggleButton("Has a Torch");
        torchbut.setSelected(true);
        torchpanel.add(torchbut);
        
        JPanel sidepanel = new JPanel();
        sidepanel.setPreferredSize(new Dimension(160, 140));
        sidepanel.setMaximumSize(new Dimension(160, 140));
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
        sidepanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Sconce Faces:"));
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
        
        cp.add("Center", centerpanel);
        cp.add("South", bottompanel);
        cp.add("East", east);
        cp.add("North", torchpanel);
        dispose();
    }
    
    public void setData(MapData data, int level, int x, int y) {
        this.level = level;
        this.x = x;
        this.y = y;
        if (data.mapchar == '}') {
            sidebutton[(((SconceData) data).side + 2) % 4].doClick();
            torchbut.setSelected(((SconceData) data).hasTorch);
            if (((SconceData) data).isSwitch) {
                sconceswitch = ((SconceData) data).sconceswitch;
                hasswitch.setText("Has Switch");
            } else {
                sconceswitch = null;
                hasswitch.setText("No Switch");
            }
        } else {
            sconceswitch = null;
            hasswitch.setText("No Switch");
        }
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("North")) {
            side = 2;
        } else if (e.getActionCommand().equals("South")) {
            side = 0;
        } else if (e.getActionCommand().equals("East")) {
            side = 1;
        } else if (e.getActionCommand().equals("West")) {
            side = 3;
        } else if (e.getActionCommand().equals("Done")) {
            if (sconceswitch == null) data = new SconceData(side, torchbut.isSelected());
            else data = new SconceData(side, torchbut.isSelected(), sconceswitch);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("Add/Edit Switch")) {
            MultWallSwitchData tempdata = (MultWallSwitchData) (new MultWallSwitchWizard(frame, sconceswitch, level, x, y, false, -1, side)).getData();
            if (tempdata != null) {
                sconceswitch = tempdata;
                hasswitch.setText("Has Switch");
            }
        } else if (e.getActionCommand().equals("Remove Switch")) {
            if (hasswitch.getText().equals("No Switch")) return;
            sconceswitch = null;
            hasswitch.setText("No Switch");
        }
    }
    
    public MapData getData() {
        return data;
    }
}
