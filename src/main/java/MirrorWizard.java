import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MirrorWizard extends HeroPanel implements ActionListener {
    
    int side = 0;
    MapData data;
    HeroData hero;
    JToggleButton[] sidebutton;
    JToggleButton allowswap, hastarget;
    JPanel targetbox2;
    JTextField targetlevel, targetx, targety;
    
    public MirrorWizard(JFrame f) {
        super(f, ((DMEditor) f).dungfont);
        setTitle("Mirror Wizard");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container cp = getContentPane();
        
        //side picker
        Box sidebox = Box.createVerticalBox();
        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new GridLayout(2, 2));
        sidepanel.setPreferredSize(new Dimension(150, 50));
        sidepanel.setMaximumSize(new Dimension(150, 50));
        ButtonGroup sidegrp = new ButtonGroup();
        sidebutton = new JToggleButton[4];
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
        JPanel swappan = new JPanel();
        allowswap = new JToggleButton("Allow Swapping");
        swappan.add(allowswap);
        JPanel sidelabelpan = new JPanel();
        sidelabelpan.add(new JLabel("Mirror Faces:"));
        sidebox.add(sidelabelpan);
        sidebox.add(sidepanel);
        sidebox.add(swappan);
        sidebutton[2].setSelected(true);
        JPanel east = new JPanel();
        east.add(sidebox);
        
        //target for activation
        hastarget = new JToggleButton("Activate On Use");
        hastarget.addActionListener(this);
        JPanel hastargetpan = new JPanel();
        hastargetpan.add(hastarget);
        Box tlbox = Box.createVerticalBox();
        Box tfbox = Box.createVerticalBox();
        JLabel levellabel = new JLabel(" Level ");
        JLabel xlabel = new JLabel("    X");
        JLabel ylabel = new JLabel("    Y");
        xlabel.setHorizontalAlignment(JLabel.CENTER);
        ylabel.setHorizontalAlignment(JLabel.CENTER);
        targetlevel = new JTextField("0", 3);
        targetx = new JTextField("0", 3);
        targety = new JTextField("0", 3);
        tlbox.add(levellabel);
        tlbox.add(Box.createVerticalStrut(14));
        tlbox.add(xlabel);
        tlbox.add(Box.createVerticalStrut(15));
        tlbox.add(ylabel);
        tfbox.add(targetlevel);
        tfbox.add(Box.createVerticalStrut(10));
        tfbox.add(targetx);
        tfbox.add(Box.createVerticalStrut(10));
        tfbox.add(targety);
        Box targetbox = Box.createHorizontalBox();
        targetbox.add(tlbox);
        targetbox.add(Box.createHorizontalStrut(5));
        targetbox.add(tfbox);
        JButton targetbut = new JButton("From Map...");
        targetbut.addActionListener(this);
        targetbox2 = new JPanel();
        targetbox2.setLayout(new BoxLayout(targetbox2, BoxLayout.Y_AXIS));
        //Dimension dim1 = new Dimension(150,150);
        //targetbox2.setPreferredSize(dim1);
        //targetbox2.setMaximumSize(dim1);
        targetbox2.add(targetbox);
        targetbox2.add(Box.createVerticalStrut(10));
        targetbox2.add(targetbut);
        targetbox2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Activation Target"));
        targetbox2.setVisible(false);
        
        //east panel
        JPanel eastpanel = new JPanel();
        eastpanel.setLayout(new BoxLayout(eastpanel, BoxLayout.Y_AXIS));
        eastpanel.setPreferredSize(new Dimension(150, 230));
        eastpanel.setMaximumSize(new Dimension(150, 230));
        eastpanel.add(east);
        eastpanel.add(hastargetpan);
        eastpanel.add(targetbox2);
        eastpanel.add(Box.createVerticalGlue());
        
        //bottom panel
        JPanel bottompanel = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        cp.add(eastpanel, "East");
        cp.add(bottompanel, "South");
        
        setModal(true);
        setSize(616, 465);
        
        dispose();
    }
    
    public void setData(MapData data) {
        if (hero != null) {
            hero.removeMouseListener(this);
            hero = null;
            removeHeroes();
        }
        if (data.mapchar == 'm') {
            if (((MirrorData) data).hero != null) hero = new HeroData(((MirrorData) data).hero);
            else hero = new HeroData();
            sidebutton[(((MirrorData) data).side + 2) % 4].doClick();
            allowswap.setSelected(((MirrorData) data).allowswap);
            if (((MirrorData) data).target != null) {
                targetlevel.setText("" + ((MirrorData) data).target.level);
                targetx.setText("" + ((MirrorData) data).target.x);
                targety.setText("" + ((MirrorData) data).target.y);
                hastarget.setSelected(true);
                targetbox2.setVisible(true);
            } else {
                hastarget.setSelected(false);
                targetbox2.setVisible(false);
            }
        } else hero = new HeroData();
        addHero(hero);
        setHero(hero);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            MapPoint target = null;
            if (targetbox2.isVisible()) {
                target = new MapPoint(Integer.parseInt(targetlevel.getText()), Integer.parseInt(targetx.getText()), Integer.parseInt(targety.getText()));
            }
            data = new MirrorData(side, hero, allowswap.isSelected(), target);
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
        } else if (e.getActionCommand().startsWith("Activate")) {
            targetbox2.setVisible(!targetbox2.isVisible());
        } else if (e.getActionCommand().equals("From Map...")) {
            DMEditor.targetframe.show();
            MapPoint targ = DMEditor.targetframe.getTarget();
            if (targ != null) {
                targetlevel.setText("" + targ.level);
                targetx.setText("" + targ.x);
                targety.setText("" + targ.y);
            }
        }
    }
    
    public MapData getData() {
        return data;
    }
    
}        