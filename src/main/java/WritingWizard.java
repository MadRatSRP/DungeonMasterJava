import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class WritingWizard extends JDialog implements ActionListener {
    private int side;
    private MapData data;
    private JToggleButton[] sidebutton = new JToggleButton[4];
    private JTextField[] line = new JTextField[6];
    private JFrame frame;

        /*
        public WritingWizard(JFrame f,MapData data) {
                super(f,"Writing Wizard",true);
                frame = f;
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setSize(400,320);
                setLocationRelativeTo(f);
                Container cp = getContentPane();
                
                //side buttons
                JPanel sidepanel = new JPanel();
                sidepanel.setLayout(new GridLayout(2,2));
                sidepanel.setPreferredSize(new Dimension(140,50));
                sidepanel.setMaximumSize(new Dimension(140,50));
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
                sidebutton[2].setSelected(true);
                JPanel writlabelpanel = new JPanel();
                JLabel writlabel = new JLabel("Writing faces:");
                writlabel.setHorizontalAlignment(JLabel.CENTER);
                writlabelpanel.add(writlabel);
                
                //message lines
                JPanel messagepanel = new JPanel();
                Box linebox = Box.createVerticalBox();
                Box linelabelbox = Box.createVerticalBox();
                JLabel[] linelabel = new JLabel[6];
                Font linefont = new Font("Courier",Font.PLAIN,12);
                for (int i=0;i<6;i++) {
                        line[i] = new JTextField(24);
                        line[i].setFont(linefont);
                        linelabel[i] = new JLabel("Line "+(i+1));
                        linelabel[i].setHorizontalAlignment(JLabel.CENTER);
                        linelabelbox.add(linelabel[i]);
                        linelabelbox.add(Box.createVerticalStrut(5));
                        linebox.add(line[i]);
                }
                messagepanel.add(linelabelbox);
                messagepanel.add(linebox);
                
                //center panel
                Box centerpanel = Box.createVerticalBox();
                centerpanel.add(messagepanel);
                centerpanel.add(writlabelpanel);
                centerpanel.add(sidepanel);
                centerpanel.add(Box.createVerticalGlue());
                
                //bottom panel
                JPanel bottompanel = new JPanel();
                JButton done = new JButton("Done");
                JButton cancel = new JButton("Cancel");
                done.addActionListener(this);
                cancel.addActionListener(this);
                bottompanel.add(done);
                bottompanel.add(cancel);

                if (data.mapchar=='w') {
                        for (int i=0;i<((WritingData)data).message.length;i++)
                                line[i].setText(((WritingData)data).message[i]);
                        sidebutton[(((WritingData)data).side+2)%4].doClick();
                }
                
                cp.add("Center",centerpanel);
                cp.add("South",bottompanel);
                show();
        }
        */
    
    public WritingWizard(JFrame f) {
        super(f, "Writing Wizard", true);
        frame = f;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //side buttons
        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new GridLayout(2, 2));
        sidepanel.setPreferredSize(new Dimension(140, 50));
        sidepanel.setMaximumSize(new Dimension(140, 50));
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
        sidebutton[2].setSelected(true);
        JPanel writlabelpanel = new JPanel();
        JLabel writlabel = new JLabel("Writing faces:");
        writlabel.setHorizontalAlignment(JLabel.CENTER);
        writlabelpanel.add(writlabel);
        
        //message lines
        JPanel messagepanel = new JPanel();
        Box linebox = Box.createVerticalBox();
        Box linelabelbox = Box.createVerticalBox();
        JLabel[] linelabel = new JLabel[6];
        Font linefont = new Font("Courier", Font.PLAIN, 12);
                /*
                Font linefont = null;
                try {   
                        FileInputStream in = new FileInputStream("scrollfont.ttf");
                        linefont = Font.createFont(Font.TRUETYPE_FONT,in);
                        in.close();
                        linefont = linefont.deriveFont(Font.BOLD,12);
                } catch (Exception e) { 
                        e.printStackTrace();
                        linefont = new Font("Serif",Font.PLAIN,12);
                }
                */
        for (int i = 0; i < 6; i++) {
            line[i] = new JTextField(24);
            line[i].setFont(linefont);
            linelabel[i] = new JLabel("Line " + (i + 1));
            linelabel[i].setHorizontalAlignment(JLabel.CENTER);
            linelabelbox.add(linelabel[i]);
            linelabelbox.add(Box.createVerticalStrut(5));
            linebox.add(line[i]);
        }
        messagepanel.add(linelabelbox);
        messagepanel.add(linebox);
        
        //center panel
        Box centerpanel = Box.createVerticalBox();
        centerpanel.add(messagepanel);
        centerpanel.add(writlabelpanel);
        centerpanel.add(sidepanel);
        centerpanel.add(Box.createVerticalGlue());
        
        //bottom panel
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        cp.add("Center", centerpanel);
        cp.add("South", bottompanel);
        
        dispose();
    }
    
    public void setData(MapData data) {
        if (data.mapchar == 'w') {
            for (int i = 0; i < ((WritingData) data).message.length; i++)
                line[i].setText(((WritingData) data).message[i]);
            sidebutton[(((WritingData) data).side + 2) % 4].doClick();
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
            String[] message;
            if (line[5].getText().equals("")) {
                if (line[4].getText().equals("")) {
                    if (line[3].getText().equals("")) {
                        if (line[2].getText().equals("")) {
                            if (line[1].getText().equals("")) {
                                if (line[0].getText().equals("")) {
                                    JOptionPane.showMessageDialog(frame, "No Text!", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                } else message = new String[1];
                            } else message = new String[2];
                        } else message = new String[3];
                    } else message = new String[4];
                } else message = new String[5];
            } else message = new String[6];
            for (int i = 0; i < message.length; i++) {
                message[i] = line[i].getText().trim();
                message[i] = message[i].toLowerCase();
                if (message[i].length() > 23) message[i] = message[i].substring(0, 23);
            }
            data = new WritingData(side, message);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        }
    }
    
    public MapData getData() {
        return data;
    }
}