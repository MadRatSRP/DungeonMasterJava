import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class FountainWizard extends JDialog implements ActionListener {
    private int side, level, x, y;
    private MapData data;
    private MultWallSwitchData fountainswitch = null;
    private JToggleButton[] sidebutton = new JToggleButton[4];
    private JToggleButton cantakeitems;
    private JComboBox canputitems;
    private JLabel hasswitch;
    private JFrame frame;
    
    public FountainWizard(JFrame f) {
        super(f, "Fountain Wizard", true);
        frame = f;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //take and put items
        JPanel itempanel = new JPanel();
        cantakeitems = new JToggleButton("Can Take Out Items");
        cantakeitems.setSelected(true);
        String[] itemsizes = {"None", "0", "1", "2", "3", "4"};
        canputitems = new JComboBox(itemsizes);
        JPanel putitemspan = new JPanel();
        putitemspan.add(new JLabel("Maximum size of items that can be put in:"));
        putitemspan.add(canputitems);
        itempanel.add(cantakeitems);
        itempanel.add(putitemspan);
        
        //side facing
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
        sidepanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Fountain Faces:"));
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
        cp.add("North", itempanel);
        
        dispose();
    }
    
    public void setData(MapData data, int level, int x, int y) {
        this.level = level;
        this.x = x;
        this.y = y;
        if (data.mapchar == 'f') {
            FountainData fd = (FountainData) data;
            sidebutton[(fd.side + 2) % 4].doClick();
            if (fd.fountainswitch != null) {
                fountainswitch = fd.fountainswitch;
                hasswitch.setText("Has Switch");
            } else {
                fountainswitch = null;
                hasswitch.setText("No Switch");
            }
            cantakeitems.setSelected(fd.cantakeitems);
            canputitems.setSelectedIndex(fd.canputitems + 1);
        } else {
            fountainswitch = null;
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
            data = new FountainData(side, cantakeitems.isSelected(), canputitems.getSelectedIndex() - 1, fountainswitch);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("Add/Edit Switch")) {
            MultWallSwitchData tempdata = (MultWallSwitchData) (new MultWallSwitchWizard(frame, fountainswitch, level, x, y, false, -1, side)).getData();
            if (tempdata != null) {
                fountainswitch = tempdata;
                hasswitch.setText("Has Switch");
            }
        } else if (e.getActionCommand().equals("Remove Switch")) {
            if (hasswitch.getText().equals("No Switch")) return;
            fountainswitch = null;
            hasswitch.setText("No Switch");
        }
    }
    
    public MapData getData() {
        return data;
    }
}
