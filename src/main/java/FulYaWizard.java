import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class FulYaWizard extends JDialog implements ActionListener {
    private MapData data;
    private int level, x, y;
    private Item key;
    private JPanel typepanel, buttonpanel, keypanel, coinpanel, newsquarepan, itempanel;
    private JLabel keylabel;
    private JTextField keytargetlevel, keytargetx, keytargety, nonkeytargetlevel, nonkeytargetx, nonkeytargety;
    private JFrame frame;
    
    public FulYaWizard(JFrame f, MapData data, int level, int x, int y) {
        super(f, "FulYa Pit Wizard", true);
        frame = f;
        this.level = level;
        this.x = x;
        this.y = y;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 320);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //key panel
        key = new Item(82);
        JPanel keypan = new JPanel();
        keylabel = new JLabel("Corbum Ore");
        keylabel.setPreferredSize(new Dimension(150, 20));
        JButton keybutton = new JButton("Change");
        keybutton.addActionListener(this);
        keypan.add(keylabel);
        keypan.add(keybutton);
        keypan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Key"));
        
        //key target
        Box tlbox = Box.createVerticalBox();
        Box tfbox = Box.createVerticalBox();
        JLabel levellabel = new JLabel(" Level ");
        JLabel xlabel = new JLabel("    X");
        JLabel ylabel = new JLabel("    Y");
        xlabel.setHorizontalAlignment(JLabel.CENTER);
        ylabel.setHorizontalAlignment(JLabel.CENTER);
        keytargetlevel = new JTextField("" + level, 3);
        keytargetx = new JTextField("" + x, 3);
        keytargety = new JTextField("" + y, 3);
        tlbox.add(levellabel);
        tlbox.add(Box.createVerticalStrut(14));
        tlbox.add(xlabel);
        tlbox.add(Box.createVerticalStrut(15));
        tlbox.add(ylabel);
        tfbox.add(keytargetlevel);
        tfbox.add(Box.createVerticalStrut(10));
        tfbox.add(keytargetx);
        tfbox.add(Box.createVerticalStrut(10));
        tfbox.add(keytargety);
        Box targetbox = Box.createHorizontalBox();
        targetbox.add(tlbox);
        targetbox.add(Box.createHorizontalStrut(5));
        targetbox.add(tfbox);
        JButton targetbut = new JButton("From Map...");
        targetbut.addActionListener(this);
        JPanel targetbox2 = new JPanel();
        targetbox2.setLayout(new BoxLayout(targetbox2, BoxLayout.Y_AXIS));
        targetbox2.add(targetbox);
        targetbox2.add(Box.createVerticalStrut(10));
        targetbox2.add(targetbut);
        targetbox2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Target If Key"));
        
        //non-key target
        Box tlbox2 = Box.createVerticalBox();
        Box tfbox2 = Box.createVerticalBox();
        JLabel levellabel2 = new JLabel(" Level ");
        JLabel xlabel2 = new JLabel("    X");
        JLabel ylabel2 = new JLabel("    Y");
        xlabel2.setHorizontalAlignment(JLabel.CENTER);
        ylabel2.setHorizontalAlignment(JLabel.CENTER);
        nonkeytargetlevel = new JTextField("" + level, 3);
        nonkeytargetx = new JTextField("" + x, 3);
        nonkeytargety = new JTextField("" + y, 3);
        tlbox2.add(levellabel2);
        tlbox2.add(Box.createVerticalStrut(14));
        tlbox2.add(xlabel2);
        tlbox2.add(Box.createVerticalStrut(15));
        tlbox2.add(ylabel2);
        tfbox2.add(nonkeytargetlevel);
        tfbox2.add(Box.createVerticalStrut(10));
        tfbox2.add(nonkeytargetx);
        tfbox2.add(Box.createVerticalStrut(10));
        tfbox2.add(nonkeytargety);
        Box targetbox3 = Box.createHorizontalBox();
        targetbox3.add(tlbox2);
        targetbox3.add(Box.createHorizontalStrut(5));
        targetbox3.add(tfbox2);
        JButton targetbut2 = new JButton("From Map...");
        targetbut2.setActionCommand("From Map...2");
        targetbut2.addActionListener(this);
        JPanel targetbox4 = new JPanel();
        targetbox4.setLayout(new BoxLayout(targetbox4, BoxLayout.Y_AXIS));
        targetbox4.add(targetbox3);
        targetbox4.add(Box.createVerticalStrut(10));
        targetbox4.add(targetbut2);
        targetbox4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Target If Not Key"));
        JPanel targets = new JPanel();
        targets.add(targetbox2);
        targets.add(targetbox4);
        
        //done/cancel
        JPanel bottom = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottom.add(done);
        bottom.add(cancel);
        
        //center
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(keypan);
        center.add(targets);
        
        //check if editing existing fulya pit
        if (data != null && data.mapchar == 'y') {
            FulYaPitData fdata = (FulYaPitData) data;
            //key = new Item(fdata.keynumber);
            //keylabel.setText(key.name);
            //potion
            if (fdata.keynumber > 9 && fdata.keynumber < 31) {
                key = new Item(fdata.keynumber, 1, 1);
            }
            //chest
            else if (fdata.keynumber == 5) {
                key = new Chest();
            }
            //scroll
            else if (fdata.keynumber == 4) {
                String[] mess = new String[5];
                key = new Item(mess);
            }
            //torch
            else if (fdata.keynumber == 9) {
                key = new Torch();
            }
            //waterskin
            else if (fdata.keynumber == 73) {
                key = new Waterskin();
            }
            //compass
            else if (fdata.keynumber == 8) {
                key = new Compass();
            }
            //everything else
            else if (fdata.keynumber < 300) key = new Item(fdata.keynumber);
            else key = null;
            if (key != null) keylabel.setText(key.name);
            else keylabel.setText("Custom Item #" + (fdata.keynumber - 299));
            keytargetlevel.setText("" + fdata.keytarget.level);
            keytargetx.setText("" + fdata.keytarget.x);
            keytargety.setText("" + fdata.keytarget.y);
            nonkeytargetlevel.setText("" + fdata.nonkeytarget.level);
            nonkeytargetx.setText("" + fdata.nonkeytarget.x);
            nonkeytargety.setText("" + fdata.nonkeytarget.y);
        }
        
        cp.add("Center", center);
        cp.add("South", bottom);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            data = new FulYaPitData(new MapPoint(level, x, y), key.number, new MapPoint(Integer.parseInt(keytargetlevel.getText()), Integer.parseInt(keytargetx.getText()), Integer.parseInt(keytargety.getText())), new MapPoint(Integer.parseInt(nonkeytargetlevel.getText()), Integer.parseInt(nonkeytargetx.getText()), Integer.parseInt(nonkeytargety.getText())));
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("From Map...")) {
            //MapPoint targ = (DMEditor.getTargetFrame()).getTarget();
            DMEditor.targetframe.show();
            MapPoint targ = DMEditor.targetframe.getTarget();
            if (targ != null) {
                keytargetlevel.setText("" + targ.level);
                keytargetx.setText("" + targ.x);
                keytargety.setText("" + targ.y);
            }
        } else if (e.getActionCommand().equals("From Map...2")) {
            //MapPoint targ = (DMEditor.getTargetFrame()).getTarget();
            DMEditor.targetframe.show();
            MapPoint targ = DMEditor.targetframe.getTarget();
            if (targ != null) {
                nonkeytargetlevel.setText("" + targ.level);
                nonkeytargetx.setText("" + targ.x);
                nonkeytargety.setText("" + targ.y);
            }
        } else if (e.getActionCommand().equals("Change")) {
            //Item tempitem = (new ItemWizard(frame,"Set FulYa Pit Key",key)).getItem();
            DMEditor.itemwizard.setTitle("Item Wizard - Set Key Item For Ful-Ya Pit");
            DMEditor.itemwizard.setItem(key);
            Item tempitem = DMEditor.itemwizard.getItem();
            if (tempitem != null) {
                key = tempitem;
                keylabel.setText(key.name);
            }
        }
    }
    
    public MapData getData() {
        return data;
    }
}