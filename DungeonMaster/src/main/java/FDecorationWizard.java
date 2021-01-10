import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class FDecorationWizard extends JDialog implements ActionListener {
    private MapData data;
    private int number = 0, level, x, y;
    private JToggleButton[] decos;
    
    public FDecorationWizard(JFrame f) {
        super(f, "Floor Decoration Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(240, 240);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //decoration panel
        JPanel decopanel = new JPanel();
        JPanel decopics = new JPanel();
        decopics.setLayout(new GridLayout(4, 1));
        ButtonGroup butgrp = new ButtonGroup();
        decos = new JToggleButton[4];
        decos[0] = new JToggleButton("Puddle");
        decos[1] = new JToggleButton("Grass");
        decos[2] = new JToggleButton("Seal");
        decos[3] = new JToggleButton("Floor Grate");
        for (int i = 0; i < 4; i++) {
            decos[i].setActionCommand("" + i);
            decos[i].addActionListener(this);
            butgrp.add(decos[i]);
            decopics.add(decos[i]);
        }
        decos[0].setSelected(true);
        decopanel.add(decopics);
        decopanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Decoration"));
        
        //done/cancel
        JPanel dcpanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        dcpanel.add(done);
        dcpanel.add(cancel);
        
        cp.add("Center", decopanel);
        cp.add("South", dcpanel);
        
        dispose();
    }
    
    public void setData(MapData data, int level, int x, int y) {
        if (data.mapchar == 'F') {
            decos[((FDecorationData) data).number].doClick();
        }
        this.level = level;
        this.x = x;
        this.y = y;
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            data = new FDecorationData(number);
            ((FDecorationData) data).level = level;
            ((FDecorationData) data).xcoord = x;
            ((FDecorationData) data).ycoord = y;
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else {
            int num;
            try {
                num = Integer.parseInt(e.getActionCommand());
            } catch (NumberFormatException ex) {
                num = 0;
            }
            number = num;
        }
    }
    
    public MapData getData() {
        return data;
    }
    
}