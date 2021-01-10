import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SideDialog extends JDialog implements ActionListener {
    private int side;
    
    public SideDialog(JFrame f) {
        super(f, "Side Chooser", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(200, 200);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        JPanel sidepanel = new JPanel();
        sidepanel.setPreferredSize(new Dimension(160, 120));
        sidepanel.setMaximumSize(new Dimension(160, 120));
        ButtonGroup sidegrp = new ButtonGroup();
        JToggleButton[] sidebutton = new JToggleButton[4];
        sidebutton[0] = new JToggleButton("North");
        sidebutton[1] = new JToggleButton("South");
        sidebutton[2] = new JToggleButton("East");
        sidebutton[3] = new JToggleButton("West");
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
        sidebutton[1].setSelected(true);
        
        JLabel openinglabel = new JLabel("Which Side?");
        openinglabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel centerpanel = new JPanel();
        centerpanel.setLayout(new GridLayout(2, 1, 0, 0));
        centerpanel.add(openinglabel);
        centerpanel.add(sidepanel);
        
        JButton done = new JButton("Done");
        done.addActionListener(this);
        
        cp.add("Center", centerpanel);
        cp.add("South", done);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("North")) {
            side = 2;
            return;
        } else if (e.getActionCommand().equals("South")) {
            side = 0;
            return;
        } else if (e.getActionCommand().equals("East")) {
            side = 1;
            return;
        } else if (e.getActionCommand().equals("West")) {
            side = 3;
            return;
        }
        dispose();
    }
    
    public int getSide() {
        return side;
    }
}
