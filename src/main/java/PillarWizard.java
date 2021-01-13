import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

class PillarWizard extends JDialog implements ActionListener {
    private MapData data;
    private int number = 0;
    private JToggleButton[] decos;
    private boolean mirror;
    private JPanel custompicpan;
    private JTextField custompic;
    private ImageIcon cpic = null;
    private String oldpicname = "";
    
    public PillarWizard(JFrame f) {
        super(f, "Pillar Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(405, 275);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //decoration panel
        JPanel decopanel = new JPanel();
        JPanel decopics = new JPanel();
        decopics.setLayout(new GridLayout(1, 3));
        ButtonGroup butgrp = new ButtonGroup();
        decos = new JToggleButton[3];
        decos[0] = new JToggleButton(new ImageIcon("Maps" + File.separator + "pillara3.gif"));
        decos[1] = new JToggleButton(new ImageIcon("Maps" + File.separator + "pillarb3.gif"));
        decos[2] = new JToggleButton("Custom Picture");
        decos[2].setHorizontalTextPosition(JToggleButton.CENTER);
        for (int i = 0; i < 3; i++) {
            decos[i].setActionCommand("" + i);
            decos[i].addActionListener(this);
            butgrp.add(decos[i]);
            decopics.add(decos[i]);
        }
        decos[0].setSelected(true);
        decopanel.add(decopics);
        decopanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Appearance"));
        
        //custom pic panel
        custompicpan = new JPanel();
        custompic = new JTextField(10);
        custompic.addActionListener(this);
        custompicpan.add(new JLabel("Pics Name:"));
        custompicpan.add(custompic);
        custompicpan.setVisible(false);
        
        //done/cancel
        JPanel dcpanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        dcpanel.add(done);
        dcpanel.add(cancel);
        
        cp.add("North", decopanel);
        cp.add("Center", custompicpan);
        cp.add("South", dcpanel);
        dispose();
    }
    
    public void setData(MapData data, boolean mirror) {
        if (data.mapchar == 'P') {
            decos[((PillarData) data).type].doClick();
            if (number == 2) {
                if (!((PillarData) data).custompic.equals(custompic.getText()))
                    custompic.setText(((PillarData) data).custompic);
                if (!custompic.getText().equals(oldpicname)) setCustomIcon(custompic.getText());
            }
        } else custompic.setText(oldpicname);
        this.mirror = mirror;
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            data = new PillarData(number, mirror);
            if (number == 2) ((PillarData) data).custompic = custompic.getText();
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getSource() == custompic) setCustomIcon(custompic.getText());
        else {
            int num;
            try {
                num = Integer.parseInt(e.getActionCommand());
            } catch (NumberFormatException ex) {
                num = 0;
            }
            number = num;
            if (number == 2) custompicpan.setVisible(true);
            else custompicpan.setVisible(false);
        }
    }
    
    private void setCustomIcon(String picname) {
        String ftype = ".gif";
        File testfile = new File("Maps" + File.separator + picname + "3.gif");
        if (!testfile.exists()) ftype = ".png";
        cpic = new ImageIcon("Maps" + File.separator + picname + "3" + ftype);
        decos[2].setIcon(cpic);
        oldpicname = picname;
        //repaint();
    }
    
    public MapData getData() {
        return data;
    }
    
}