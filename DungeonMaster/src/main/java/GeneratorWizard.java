import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

class GeneratorWizard extends JDialog implements ActionListener {
    private MapData data;
    private int level, x, y;
    private MonsterData mon;
    private JTextField genrate, maxgen, maxcount, count;
    private JToggleButton isActive, resetcount;
    private JLabel monsterlabel;
    private JComboBox numtogen;
    private JFrame frame;
        
        /*
        public GeneratorWizard(JFrame f,MapData data,int level,int x,int y) {
                super(f,"Generator Wizard",true);
                frame = f;
                this.level = level;
                this.x = x;
                this.y = y;
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setSize(525,230);
                setLocationRelativeTo(f);
                Container cp = getContentPane();
                
                //monster panel
                JPanel monpanel = new JPanel();
                //mon = new MonsterData(10,level,x,y,440,0,0,4,false,true,60,30,30,75,0,0,new ArrayList(),false,null,null,0,0,0,true,0,0);
                mon = new MonsterData(10,level,x,y,440,0,0,4,false,true,60,30,30,75,0,0,new ArrayList(),false,null,null,0,0,0,true,0);
                monsterlabel = new JLabel(mon.name,MonsterData.MonsterIcon[mon.number],JLabel.RIGHT);
                JButton editbutton = new JButton("Edit");
                editbutton.addActionListener(this);
                String[] nummons = { "1","2","3","4","Random" };
                numtogen = new JComboBox(nummons);
                numtogen.setEditable(false);
                numtogen.setVisible(false);
                monpanel.add(numtogen);
                monpanel.add(monsterlabel);
                monpanel.add(editbutton);
                
                //lower center panel
                //JPanel lowercenter = new JPanel();
                Box lowercenter = Box.createVerticalBox();
                JPanel pan1 = new JPanel();
                pan1.add(new JLabel("Generation Rate (0 If Not Continuous):"));
                genrate = new JTextField("0",4);
                pan1.add(genrate);
                JPanel pan2 = new JPanel();
                pan2.add(new JLabel("# Generations Before Deactivation (0 For Infinite):"));
                maxgen = new JTextField("0",4);
                pan2.add(maxgen);
                JPanel pan3 = new JPanel();
                isActive = new JToggleButton("Is Active");
                pan3.add(isActive);
                lowercenter.add(pan1);
                lowercenter.add(pan2);
                lowercenter.add(pan3);

                //center panel
                JPanel centerpanel = new JPanel();
                centerpanel.add(monpanel);
                centerpanel.add(lowercenter);
                
                //east panel
                JLabel mcountlabel = new JLabel("Total Switch count:");
                maxcount = new JTextField("0",3);
                JLabel countlabel = new JLabel("Remaining Switch count:");
                count = new JTextField("0",3);
                resetcount = new JToggleButton("Count Resets");
                JPanel countpanel = new JPanel(new GridLayout(5,1));
                countpanel.add(mcountlabel);
                countpanel.add(maxcount);
                countpanel.add(countlabel);
                countpanel.add(count);
                countpanel.add(resetcount);
                JPanel east = new JPanel();
                east.add(Box.createGlue());
                east.add(countpanel);
                east.add(Box.createGlue());

                //bottom panel
                JPanel bottompanel = new JPanel();
                JButton done = new JButton("Done");
                JButton cancel = new JButton("Cancel");
                done.addActionListener(this);
                cancel.addActionListener(this);
                bottompanel.add(done);
                bottompanel.add(cancel);
                
                if (data.mapchar=='g') {
                        GeneratorData gdata = (GeneratorData)data;
                        mon = gdata.monster;
                        monsterlabel.setText(mon.name);
                        //monsterlabel.setIcon(MonsterData.MonsterIcon[mon.number]);
                        monsterlabel.setIcon(mon.pic);
                        if (mon.subsquare!=5) {
                                numtogen.setSelectedIndex(gdata.numtogen-1);
                                numtogen.setVisible(true);
                        }
                        if (gdata.genrate>0) genrate.setText(""+gdata.genrate);
                        if (gdata.maxgen>0) maxgen.setText(""+gdata.maxgen);
                        if (gdata.isActive) isActive.setSelected(true);
                        maxcount.setText(""+gdata.maxcount);
                        count.setText(""+gdata.count);
                        if (gdata.resetcount) resetcount.setSelected(true);
                }
                
                cp.add("Center",centerpanel);
                cp.add("South",bottompanel);
                cp.add("East",east);
                show();
        }
        */
    
    public GeneratorWizard(JFrame f) {
        super(f, "Generator Wizard", true);
        frame = f;
        this.level = level;
        this.x = x;
        this.y = y;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(525, 230);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //monster panel
        JPanel monpanel = new JPanel();
        //mon = new MonsterData(10,level,x,y,440,0,0,4,false,true,60,30,30,75,0,0,new ArrayList(),false,null,null,0,0,0,true,0,0);
        mon = new MonsterData(10, level, x, y, MonsterWizard.defaulthealth[10], 0, 0, 4, false, true, MonsterWizard.defaultpower[10], MonsterWizard.defaultdefense[10], MonsterWizard.defaultmresist[10], MonsterWizard.defaultspeed[10], 0, 0, new ArrayList(), null, false, null, null, 0, 0, 0, true, 0, 0, false);
        mon.movespeed = 11;
        mon.attackspeed = 4;
        //mon.timecounter = MonsterWizard.randGen.nextInt(10);
        //mon.movecounter = MonsterWizard.randGen.nextInt(10);
        monsterlabel = new JLabel(mon.name, MonsterData.MonsterIcon[mon.number], JLabel.RIGHT);
        JButton editbutton = new JButton("Edit");
        editbutton.addActionListener(this);
        String[] nummons = {"1", "2", "3", "4", "Random"};
        numtogen = new JComboBox(nummons);
        numtogen.setEditable(false);
        numtogen.setVisible(false);
        monpanel.add(numtogen);
        monpanel.add(monsterlabel);
        monpanel.add(editbutton);
        
        //lower center panel
        //JPanel lowercenter = new JPanel();
        Box lowercenter = Box.createVerticalBox();
        JPanel pan1 = new JPanel();
        pan1.add(new JLabel("Generation Rate (0 If Not Continuous):"));
        genrate = new JTextField("0", 4);
        pan1.add(genrate);
        JPanel pan2 = new JPanel();
        pan2.add(new JLabel("# Generations Before Deactivation (0 For Infinite):"));
        maxgen = new JTextField("0", 4);
        pan2.add(maxgen);
        JPanel pan3 = new JPanel();
        isActive = new JToggleButton("Is Active");
        pan3.add(isActive);
        lowercenter.add(pan1);
        lowercenter.add(pan2);
        lowercenter.add(pan3);
        
        //center panel
        JPanel centerpanel = new JPanel();
        centerpanel.add(monpanel);
        centerpanel.add(lowercenter);
        
        //east panel
        JLabel mcountlabel = new JLabel("Total Switch count:");
        maxcount = new JTextField("1", 3);
        JLabel countlabel = new JLabel("Remaining Switch count:");
        count = new JTextField("1", 3);
        resetcount = new JToggleButton("Count Resets");
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
        cp.add("East", east);
        
        dispose();
    }
    
    public void setData(MapData data, int level, int x, int y) {
        this.level = level;
        this.x = x;
        this.y = y;
        if (data.mapchar == 'g') {
            GeneratorData gdata = (GeneratorData) data;
            mon = new MonsterData(gdata.monster);
            monsterlabel.setText(mon.name);
            //monsterlabel.setIcon(MonsterData.MonsterIcon[mon.number]);
            monsterlabel.setIcon(mon.pic);
            if (mon.subsquare != 5) {
                numtogen.setSelectedIndex(gdata.numtogen - 1);
                numtogen.setVisible(true);
            } else {
                numtogen.setSelectedIndex(0);
                numtogen.setVisible(false);
            }
            genrate.setText("" + gdata.genrate);
            maxgen.setText("" + gdata.maxgen);
            isActive.setSelected(gdata.isActive);
            maxcount.setText("" + (gdata.maxcount + 1));
            count.setText("" + (gdata.count + 1));
            resetcount.setSelected(gdata.resetcount);
        } else mon = new MonsterData(mon);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            if (Integer.parseInt(genrate.getText()) == 0) isActive.setSelected(false);
            data = new GeneratorData(level, x, y, numtogen.getSelectedIndex() + 1, Integer.parseInt(maxgen.getText()), Integer.parseInt(genrate.getText()), isActive.isSelected(), 0, 0, Integer.parseInt(maxcount.getText()) - 1, Integer.parseInt(count.getText()) - 1, resetcount.isSelected(), mon);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        } else if (e.getActionCommand().equals("Edit")) {
            //MonsterData tempdata = (new MonsterWizard(frame,level,x,y,mon)).getData();
            DMEditor.monsterwizard.setMonster(mon, level, x, y);
            MonsterData tempdata = DMEditor.monsterwizard.getData();
            if (tempdata != null) {
                mon = tempdata;
                monsterlabel.setText(mon.name);
                //monsterlabel.setIcon(MonsterData.MonsterIcon[mon.number]);
                monsterlabel.setIcon(mon.pic);
                if (mon.subsquare != 5) {
                    //numtogen.setSelectedIndex(0);
                    numtogen.setVisible(true);
                } else {
                    numtogen.setSelectedIndex(0);
                    numtogen.setVisible(false);
                }
            }
        }
    }
    
    public MapData getData() {
        return data;
    }
}