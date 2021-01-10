import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PitWizard extends JDialog implements ActionListener {
    private MapData data;
    private int level, x, y;
    private boolean isOpen = true, isConcealed = false, isIllusionary = false, isContinuous = false, isActive = false;
    JToggleButton monswork, resetcount, openbutton, concealbutton, illusbutton, activebutton;
    JTextField blinkfieldo, blinkfieldc, blinkcounter, delay, reset, maxcount, count;
    JPanel resetdelay;
    
    public PitWizard(JFrame f) {
        super(f, "Pit Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 325);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        openbutton = new JToggleButton("Is Open");
        openbutton.addActionListener(this);
        openbutton.setSelected(true);
        concealbutton = new JToggleButton("Concealed");
        concealbutton.addActionListener(this);
        illusbutton = new JToggleButton("Has Illusion");
        illusbutton.addActionListener(this);
        activebutton = new JToggleButton("Is Blinking");
        activebutton.addActionListener(this);
        JLabel blinklabelo = new JLabel("Blinkrate (Open):");
        blinkfieldo = new JTextField("0", 3);
        JLabel blinklabelc = new JLabel("Blinkrate (Closed):");
        blinkfieldc = new JTextField("0", 3);
        JLabel blinkclabel = new JLabel("Blinkcounter:");
        blinkcounter = new JTextField("0", 3);
        JPanel blinkpanel = new JPanel();
        blinkpanel.add(Box.createHorizontalGlue());
        blinkpanel.add(blinklabelo);
        blinkpanel.add(blinkfieldo);
        blinkpanel.add(blinklabelc);
        blinkpanel.add(blinkfieldc);
        blinkpanel.add(blinkclabel);
        blinkpanel.add(blinkcounter);
        blinkpanel.add(Box.createHorizontalGlue());
        blinkpanel.add(activebutton);
        
        JPanel center = new JPanel();
        //center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.setPreferredSize(new Dimension(200, 90));
        resetdelay = new JPanel();
        resetdelay.setLayout(new BoxLayout(resetdelay, BoxLayout.Y_AXIS));
        reset = new JTextField("0", 4);
        delay = new JTextField("0", 4);
        monswork = new JToggleButton("Monsters Also Trigger");
        resetdelay.add(monswork);
        resetdelay.add(new JLabel("Reset Count (0 for none):"));
        resetdelay.add(reset);
        resetdelay.add(new JLabel("Delay Count (0 for none):"));
        resetdelay.add(delay);
        resetdelay.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Supplies For Quick Type"));
        center.add(resetdelay);
        
        JLabel mcountlabel = new JLabel("Total Switch count:");
        maxcount = new JTextField("1", 3);
        JLabel countlabel = new JLabel("Remaining Switch count:");
        count = new JTextField("1", 3);
        resetcount = new JToggleButton("Count Resets");
        //resetcount.setSelected(true);
        JPanel countpanel = new JPanel(new GridLayout(5, 1));
        countpanel.add(mcountlabel);
        countpanel.add(maxcount);
        countpanel.add(countlabel);
        countpanel.add(count);
        countpanel.add(resetcount);
        JPanel east = new JPanel();
        east.add(Box.createGlue());
        east.add(countpanel);
        east.add(Box.createGlue());
        
        JPanel centerpanel = new JPanel();
        centerpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        centerpanel.add(Box.createHorizontalGlue());
        centerpanel.add(openbutton);
        centerpanel.add(concealbutton);
        centerpanel.add(illusbutton);
        centerpanel.add(Box.createHorizontalGlue());
        centerpanel.add(blinkpanel);
        centerpanel.add(Box.createHorizontalGlue());
        centerpanel.setPreferredSize(new Dimension(300, 120));
        
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        cp.add("North", centerpanel);
        cp.add("Center", center);
        cp.add("East", east);
        cp.add("South", bottompanel);
        
        dispose();
    }
    
    public void setData(MapData data, int level, int x, int y) {
        this.level = level;
        this.x = x;
        this.y = y;
        if (data.mapchar == 'p') {
            PitData pdata = (PitData) data;
            if (!pdata.isOpen) {
                openbutton.setSelected(false);
                isOpen = false;
            } else {
                openbutton.setSelected(true);
                isOpen = true;
            }
            if (pdata.isConcealed) {
                concealbutton.setSelected(true);
                isConcealed = true;
            } else {
                concealbutton.setSelected(false);
                isConcealed = false;
            }
            if (pdata.isIllusionary) {
                illusbutton.setSelected(true);
                isIllusionary = true;
            } else {
                illusbutton.setSelected(false);
                isIllusionary = false;
            }
            if (pdata.isActive) {
                activebutton.setSelected(true);
                isActive = true;
            } else {
                activebutton.setSelected(false);
                isActive = false;
            }
            blinkfieldo.setText("" + pdata.blinkrateo);
            blinkfieldc.setText("" + pdata.blinkratec);
            blinkcounter.setText("" + pdata.blinkcounter);
            monswork.setSelected(pdata.monswork);
            delay.setText("" + pdata.delay);
            reset.setText("" + pdata.reset);
            maxcount.setText("" + (pdata.maxcount + 1));
            count.setText("" + (pdata.count + 1));
            resetcount.setSelected(pdata.resetcount);
        }
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Is Open")) {
            isOpen = !isOpen;
            return;
        } else if (e.getActionCommand().equals("Concealed")) {
            isConcealed = !isConcealed;
            return;
        } else if (e.getActionCommand().equals("Has Illusion")) {
            isIllusionary = !isIllusionary;
            return;
        } else if (e.getActionCommand().equals("Is Blinking")) {
            isActive = !isActive;
            return;
        } else if (e.getActionCommand().equals("Done")) {
            int blinkrateo = Integer.parseInt(blinkfieldo.getText());
            int blinkratec = Integer.parseInt(blinkfieldc.getText());
            if (blinkrateo <= 0) blinkrateo = 0;
            if (blinkratec <= 0) blinkratec = 0;
            if (blinkrateo == 0 && blinkratec == 0) {
                isContinuous = false;
                isActive = false;
            } else isContinuous = true;
            int d = Integer.parseInt(delay.getText());
            int r = Integer.parseInt(reset.getText());
            boolean isSupplies = false;
            if (d > 0 || r > 0) isSupplies = true;
            data = new PitData(level, x, y, isOpen, isConcealed, isIllusionary, isSupplies, isContinuous, isActive, blinkrateo, blinkratec, d, r, monswork.isSelected(), Integer.parseInt(maxcount.getText()) - 1, Integer.parseInt(count.getText()) - 1, resetcount.isSelected(), Integer.parseInt(blinkcounter.getText()), false, 0, false, 0);
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
        }
        dispose();
    }
    
    public MapData getData() {
        return data;
    }
}
