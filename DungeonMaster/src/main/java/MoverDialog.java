import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MoverDialog extends JDialog implements ActionListener {
    
    private int newlocation = -1;
    private JToggleButton[] location = new JToggleButton[4];
    
    public MoverDialog(JFrame f, int loc, boolean docorners) {
        this(f, "Change Location", loc, docorners);
    }
    
    public MoverDialog(JFrame f, String title, int loc, boolean docorners) {
        super(f, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container cp = getContentPane();
        setResizable(false);
        
        JPanel buttonpan;
        if (docorners) {
            setSize(180, 160);
            buttonpan = new JPanel(new GridLayout(2, 2, 10, 10));
            location[0] = new JToggleButton("NW");
            location[1] = new JToggleButton("NE");
            location[2] = new JToggleButton("SE");
            location[3] = new JToggleButton("SW");
            buttonpan.add(location[0]);
            buttonpan.add(location[1]);
            buttonpan.add(location[3]);
            buttonpan.add(location[2]);
            //buttonpan.setBorder(BorderFactory.createRaisedBevelBorder());
        } else {
            setSize(200, 210);
            buttonpan = new JPanel(new GridLayout(3, 1, 10, 5));
            location[0] = new JToggleButton("N");
            location[1] = new JToggleButton("W");
            location[2] = new JToggleButton("S");
            location[3] = new JToggleButton("E");
            location[0].setPreferredSize(new Dimension(60, 36));
            location[1].setPreferredSize(new Dimension(60, 36));
            location[2].setPreferredSize(new Dimension(60, 36));
            location[3].setPreferredSize(new Dimension(60, 36));
            JPanel toppan = new JPanel();
            toppan.add(location[0]);
            JPanel midpan = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            midpan.add(location[1]);
            midpan.add(location[3]);
            JPanel botpan = new JPanel();
            botpan.add(location[2]);
            buttonpan.add(toppan);
            buttonpan.add(midpan);
            buttonpan.add(botpan);
            //buttonpan.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        ButtonGroup grp = new ButtonGroup();
        grp.add(location[0]);
        grp.add(location[1]);
        grp.add(location[2]);
        grp.add(location[3]);
        location[loc].setSelected(true);
        
        JPanel bottom = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottom.add(done);
        bottom.add(cancel);
        bottom.setBorder(BorderFactory.createRaisedBevelBorder());
        
        cp.add(buttonpan, BorderLayout.CENTER);
        cp.add(bottom, BorderLayout.SOUTH);
        //pack();
        setLocationRelativeTo(f);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            if (location[0].isSelected()) newlocation = 0;
            else if (location[1].isSelected()) newlocation = 1;
            else if (location[2].isSelected()) newlocation = 2;
            else newlocation = 3;
        }
        dispose();
    }
    
    public int getNewLocation() {
        return newlocation;
    }
}
