import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class DecorationWizard extends JDialog implements ActionListener {
    private MapData data;
    private int side = 0, number = 0;
    private JToggleButton[] sidebutton, decos;
        
        /*
        public DecorationWizard(JFrame f, MapData data) {
                super(f,"Decoration Wizard",true);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setSize(320,346);
                setLocationRelativeTo(f);
                Container cp = getContentPane();

                //decoration panel
                JPanel decopanel = new JPanel();
                JPanel decopics = new JPanel();
                decopics.setLayout(new GridLayout(9,1));
                ButtonGroup butgrp = new ButtonGroup();
                JToggleButton[] decos = new JToggleButton[9];
                decos[0] = new JToggleButton("Ring");
                decos[1] = new JToggleButton("Hook");
                decos[2] = new JToggleButton("Slime");
                decos[3] = new JToggleButton("Grate");
                decos[4] = new JToggleButton("Drain");
                decos[5] = new JToggleButton("Crack");
                decos[6] = new JToggleButton("Scratches");
                decos[7] = new JToggleButton("Chaos");
                decos[8] = new JToggleButton("Chaos w/Glow");
                for (int i=0;i<9;i++) {
                        decos[i].setActionCommand(""+i);
                        decos[i].addActionListener(this);
                        butgrp.add(decos[i]);
                        decopics.add(decos[i]);
                }
                decos[0].setSelected(true);
                decopanel.add(decopics);
                decopanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),"Decoration"));

                //side chooser
                JPanel sidepanel = new JPanel();
                sidepanel.setLayout(new GridLayout(2,2));
                sidepanel.setPreferredSize(new Dimension(140,50));
                //sidepanel.setMaximumSize(new Dimension(140,50));
                ButtonGroup sidegrp = new ButtonGroup();
                JToggleButton[] sidebutton = new JToggleButton[4];
                sidebutton[0] = new JToggleButton("North");
                sidebutton[1] = new JToggleButton("West");
                sidebutton[2] = new JToggleButton("South");
                sidebutton[3] = new JToggleButton("East");
                sidebutton[0].setMargin(new Insets(0,4,0,4));
                sidebutton[1].setMargin(new Insets(0,4,0,4));
                sidebutton[2].setMargin(new Insets(0,4,0,4));
                sidebutton[3].setMargin(new Insets(0,4,0,4));
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
                sidebutton[2].setSelected(true);
                sidepanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Decoration Faces"));
                Box east = Box.createVerticalBox();
                east.add(Box.createVerticalGlue());
                east.add(sidepanel);
                east.add(Box.createVerticalGlue());
                                
                //done/cancel
                JPanel dcpanel = new JPanel();
                JButton done = new JButton("Done");
                JButton cancel = new JButton("Cancel");
                done.addActionListener(this);
                cancel.addActionListener(this);
                dcpanel.add(done);
                dcpanel.add(cancel);
                
                if (data.mapchar=='D') {
                        sidebutton[(((DecorationData)data).side+2)%4].doClick();
                        decos[((DecorationData)data).number].doClick();
                }
                
                cp.add("Center",decopanel);
                cp.add("East",east);
                //cp.add("East",sidepanel);
                cp.add("South",dcpanel);
                show();
        }
        */
    
    public DecorationWizard(JFrame f) {
        super(f, "Decoration Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(320, 375);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //decoration panel
        JPanel decopanel = new JPanel();
        JPanel decopics = new JPanel();
        decopics.setLayout(new GridLayout(10, 1));
        ButtonGroup butgrp = new ButtonGroup();
        decos = new JToggleButton[10];
        decos[0] = new JToggleButton("Ring");
        decos[1] = new JToggleButton("Hook");
        decos[2] = new JToggleButton("Slime");
        decos[3] = new JToggleButton("Grate");
        decos[4] = new JToggleButton("Drain");
        decos[5] = new JToggleButton("Crack");
        decos[6] = new JToggleButton("Scratches");
        decos[7] = new JToggleButton("Chaos");
        decos[8] = new JToggleButton("Chaos w/Glow");
        decos[9] = new JToggleButton("Demon Face");
        for (int i = 0; i < 10; i++) {
            decos[i].setActionCommand("" + i);
            decos[i].addActionListener(this);
            butgrp.add(decos[i]);
            decopics.add(decos[i]);
        }
        decos[0].setSelected(true);
        decopanel.add(decopics);
        decopanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Decoration"));
        
        //side chooser
        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new GridLayout(2, 2));
        sidepanel.setPreferredSize(new Dimension(140, 50));
        //sidepanel.setMaximumSize(new Dimension(140,50));
        ButtonGroup sidegrp = new ButtonGroup();
        sidebutton = new JToggleButton[4];
        sidebutton[0] = new JToggleButton("North");
        sidebutton[1] = new JToggleButton("West");
        sidebutton[2] = new JToggleButton("South");
        sidebutton[3] = new JToggleButton("East");
        sidebutton[0].setMargin(new Insets(0, 4, 0, 4));
        sidebutton[1].setMargin(new Insets(0, 4, 0, 4));
        sidebutton[2].setMargin(new Insets(0, 4, 0, 4));
        sidebutton[3].setMargin(new Insets(0, 4, 0, 4));
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
        sidebutton[2].setSelected(true);
        sidepanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Decoration Faces"));
        Box east = Box.createVerticalBox();
        east.add(Box.createVerticalGlue());
        east.add(sidepanel);
        east.add(Box.createVerticalGlue());
        
        //done/cancel
        JPanel dcpanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        dcpanel.add(done);
        dcpanel.add(cancel);
        
        cp.add("Center", decopanel);
        cp.add("East", east);
        cp.add("South", dcpanel);
        
        dispose();
    }
    
    public void setData(MapData data) {
        if (data.mapchar == 'D') {
            sidebutton[(((DecorationData) data).side + 2) % 4].doClick();
            decos[((DecorationData) data).number].doClick();
        }
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            data = new DecorationData(side, number);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("North")) {
            side = 2;
        } else if (e.getActionCommand().equals("South")) {
            side = 0;
        } else if (e.getActionCommand().equals("East")) {
            side = 1;
        } else if (e.getActionCommand().equals("West")) {
            side = 3;
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