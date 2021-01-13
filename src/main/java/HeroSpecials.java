import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

class HeroSpecials extends JDialog implements ActionListener, MouseListener {
    private Vector abilities, abils;
    private JList abilitylist;
    private int listindex = -1;
    private AbilityPanel abilitypanel;
    
    public HeroSpecials(JFrame f, Vector abils) {
        super(f, "Hero Specials", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(615, 575);
        setLocationRelativeTo(null);
        Container cp = getContentPane();
        
        //list of current abilities
        this.abils = abils;//actual vector
        abilities = new Vector(); //"working" vector so cancel works
        for (int i = 0; i < abils.size(); i++) {
            abilities.add(new SpecialAbility((SpecialAbility) abils.get(i)));//make new copy of ability so cancel works
        }
        abilitylist = new JList(abilities);
        abilitylist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        abilitylist.setVisibleRowCount(6);
        abilitylist.addMouseListener(this);
        JScrollPane abilitypane = new JScrollPane(abilitylist);
        abilitypane.setPreferredSize(new Dimension(200, 120));
        JPanel knownabilitypan = new JPanel();
        knownabilitypan.add(abilitypane);
        knownabilitypan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Current Abilities"));
        
        //add/replace and delete buttons
        JButton addreplacebut = new JButton("< Add (Replace Selected With) Ability Above ^");
        addreplacebut.addActionListener(this);
        JButton removeabilitybut = new JButton("< Remove Selected Ability");
        removeabilitybut.addActionListener(this);
        Box addremovepan = Box.createVerticalBox();
        //JPanel addremovepan = new JPanel();
        addremovepan.add(addreplacebut);
        addremovepan.add(removeabilitybut);
        knownabilitypan.add(addremovepan);
        
        //ability creation panel
        abilitypanel = new AbilityPanel(f);
        abilitypanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Create/Edit Ability"));
        abilitypanel.setPreferredSize(new Dimension(600, 280));
        if (abilities.size() > 0 && !abilitypanel.hasLastAbility()) {
            abilitylist.setSelectedIndex(0);
            listindex = 0;
            abilitypanel.setAbility((SpecialAbility) abilities.get(0));
        }
        
        //center panel -> holds ability list and creation panel
        JPanel apanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        //apanel.setPreferredSize(new Dimension(540,180));
        apanel.add(abilitypanel);
        //apanel.add(addreplacebut);
        apanel.add(knownabilitypan);
        apanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Abilities"));
        
        //bottom panel -> done and cancel buttons
        JPanel bottom = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottom.add(done);
        bottom.add(cancel);
        
        cp.add(apanel, BorderLayout.CENTER);
        cp.add(bottom, BorderLayout.SOUTH);
        
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            //modify actual abilities vector
            abils.clear();
            for (int i = 0; i < abilities.size(); i++) {
                abils.add(abilities.get(i));
            }
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) dispose();
            //else if (e.getActionCommand().startsWith("v")) {
        else if (e.getActionCommand().startsWith("< A")) {
            SpecialAbility newspecial = abilitypanel.createSpecial();
            if (newspecial == null) return;
            if (listindex != -1) {
                //replace
                abilities.set(listindex, newspecial);
            } else {
                //add
                abilities.add(newspecial);
            }
            abilitylist.setListData(abilities);
            listindex = -1;
        } else if (e.getActionCommand().startsWith("< R")) {
            //delete
            if (listindex != -1) {
                abilities.remove(listindex);
                abilitylist.setListData(abilities);
            }
        }
    }
    
    public void mousePressed(MouseEvent e) {
        int clickedindex = abilitylist.locationToIndex(e.getPoint());
        if (clickedindex == -1 || clickedindex == listindex) abilitylist.clearSelection();
        listindex = abilitylist.getSelectedIndex();
        //set gui stuff based on selected ability (if anything selected)
        if (listindex != -1) abilitypanel.setAbility((SpecialAbility) abilities.get(listindex));
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}