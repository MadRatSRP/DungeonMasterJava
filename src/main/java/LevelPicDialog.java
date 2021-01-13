import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LevelPicDialog extends JDialog implements ActionListener {
    
    private String[] leveldir;
    private JTextField[] leveldirfield;
    
    public LevelPicDialog(JFrame f, String[] leveldir) {
        super(f, "Picture Directories By Level", true);
        this.leveldir = leveldir;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container cp = getContentPane();
        
        JPanel center = new JPanel(new GridLayout(leveldir.length, 2));
        center.setPreferredSize(new Dimension(200, leveldir.length * 19));
        leveldirfield = new JTextField[leveldir.length];
        for (int l = 0; l < leveldir.length; l++) {
            leveldirfield[l] = new JTextField();
            if (leveldir[l] != null) leveldirfield[l].setText(leveldir[l]);
            JLabel levellabel = new JLabel("Level " + l);
            levellabel.setHorizontalAlignment(JLabel.CENTER);
            center.add(levellabel);
            center.add(leveldirfield[l]);
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
            for (int l = 0; l < leveldir.length; l++) {
                leveldir[l] = leveldirfield[l].getText();
                if (leveldir[l].equals("")) leveldir[l] = null;
            }
        }
        dispose();
    }
}