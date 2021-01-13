import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LevelDarkDialog extends JDialog implements ActionListener {
    
    private int[] leveldark;
    private JTextField[] leveldarkfield;
    
    public LevelDarkDialog(JFrame f, int[] leveldark) {
        super(f, "Darkness By Level", true);
        this.leveldark = leveldark;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container cp = getContentPane();
        
        JPanel center = new JPanel(new GridLayout(leveldark.length, 2));
        center.setPreferredSize(new Dimension(200, leveldark.length * 19));
        leveldarkfield = new JTextField[leveldark.length];
        for (int l = 0; l < leveldark.length; l++) {
            leveldarkfield[l] = new JTextField();
            leveldarkfield[l].setText("" + leveldark[l]);
            JLabel levellabel = new JLabel("Level " + l);
            levellabel.setHorizontalAlignment(JLabel.CENTER);
            center.add(levellabel);
            center.add(leveldarkfield[l]);
        }
        JScrollPane centerpane = new JScrollPane(center);
        
        JPanel bottom = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottom.add(done);
        bottom.add(cancel);
        bottom.setBorder(BorderFactory.createRaisedBevelBorder());
        
        cp.add(centerpane, BorderLayout.CENTER);
        cp.add(bottom, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(f);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            for (int l = 0; l < leveldark.length; l++) {
                leveldark[l] = Integer.parseInt(leveldarkfield[l].getText());
                if (leveldark[l] < 0) leveldark[l] = 0;
                else if (leveldark[l] > 255) leveldark[l] = 255;
            }
        }
        dispose();
    }
}