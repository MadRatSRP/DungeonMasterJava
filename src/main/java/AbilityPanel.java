import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


class AbilityPanel extends JPanel implements ActionListener {
    private JComboBox actionbox, classgain, castpower;
    private JTextField name, sound, power, speed, mana, strength, dexterity, vitality, intelligence, wisdom, flevel, nlevel, wlevel, plevel;
    private JComboBox[] lvlneed;
    private JToggleButton immbut, diamondbut, stunbut;
    private JComboBox poison;
    private JPanel conjurepanel, statpanel, weappanel;
    private JLabel powerlabel, conjurelabel;
    private Item conjureitem;
    private FileDialog dialog;
    private JFrame frame;
    private static ArrayList usesymbol = new ArrayList(13);
    private static ArrayList usenone = new ArrayList(8);
    
    private static SpecialAbility lastability = null;

        /*
        public static final String[] actions = {
                
                "Anti-Ven","Arc Bolt","Armor","Armor Party","Backstab","Berserker","Blow Horn","Bolt","Calm","Climb Down","Climb Up","Conjure",
                "Detect Illusion","Disrupt","Dispell","Drain Life","Drain Mana","Enhance Fist","Enhance Weapon","False Image","Feeble Mind",
                "Fireball","Freeze","Freeze Life","Frighten","Good Berries","Heal","Invoke","Light","Purify","Ruiner","Shield","Shield Party",
                "Sight","Silence","Slow","Slowfall","Spellshield","Spellshield Party","Stat Boost","Steal","Strip Defenses","True Sight",
                "Ven Cloud","Venom","Weakness","ZO",
                
                
                spell symbol: "Arc Bolt","Bolt","Dispell","Feeble Mind","Fireball","Ruiner","Silence","Slow","Strip Defenses","Ven Cloud","Venom","Weakness","ZO",
                
                
                raw power:    "Anti-Ven"(1-15, posionpow removed), "Blow Horn","Calm","Frighten","War Cry",(# steps mon will run), "Drain Life"(health to drain before magicresist), "Drain Mana"(mana to drain before magicresist), "Freeze","Freeze Life"(time to freeze see blue/green boxes),
                              "Heal"(%maxhealth to heal), "Light"(amount of boost see staff), "Shield","Shield Party"(points to add to defense), "Spellshield","Spellshield Party"(points to add to magicresist), "Armor Party","Armor"(points to add to both defense and mresist),
                              "Sight"(time to add see staff and spell), "Slowfall"(points to add see spell 10-100), "True Sight"(points to add see spell 10-100), "Weapon Enhance","Fist Enhance"(number of attacks), "Steal"(%chance to succeed before ninja boost),
                              "Good Berries"(berry heal stamina restore % of max before priest bonus), "False Image"(# of images), "Berserker"(duration - decay same rate as statboosts)
                
                no power:     "Climb Down","Climb Up","Detect Illusion","Invoke","Stat Boost","Conjure","Purify","Backstab",
                
                
        };
        */
    
    public AbilityPanel(JFrame f) {
        
        if (usesymbol.size() == 0) {
            usesymbol.add("Arc Bolt");
            usesymbol.add("Bolt");
            usesymbol.add("Dispell");
            usesymbol.add("Feeble Mind");
            usesymbol.add("Fireball");
            usesymbol.add("Ruiner");
            usesymbol.add("Silence");
            usesymbol.add("Slow");
            usesymbol.add("Strip Defenses");
            usesymbol.add("Ven Cloud");
            usesymbol.add("Venom");
            usesymbol.add("Weakness");
            usesymbol.add("ZO");
            
            usenone.add("Climb Down");
            usenone.add("Climb Up");
            usenone.add("Detect Illusion");
            usenone.add("Invoke");
            usenone.add("Stat Boost");
            usenone.add("Conjure");
            usenone.add("Purify");
            usenone.add("Backstab");
        }
        frame = f;
        //file dialog for browsing sounds
        dialog = new FileDialog(f, "Choose A Sound", FileDialog.LOAD);
        dialog.setDirectory("Sounds");
        
        //create/edit controls
        Box cepanel = Box.createVerticalBox();
        name = new JTextField("Breathe Fire", 20);
        sound = new JTextField("roar.wav", 10);
        JButton browsebut = new JButton("Browse");
        browsebut.addActionListener(this);
        actionbox = new JComboBox(SpecialAbility.actions);
        actionbox.setEditable(false);
        actionbox.setSelectedItem("Fireball");
        actionbox.addActionListener(this);
        classgain = new JComboBox(ItemCreator.CLASSES);
        classgain.setSelectedIndex(2);
        castpower = new JComboBox(ItemWizard.powers);
        castpower.setEditable(false);
        castpower.setSelectedIndex(0);
        power = new JTextField("10", 4);
        power.setVisible(false);
        speed = new JTextField("30", 4);
        mana = new JTextField("0", 4);
        lvlneed = new JComboBox[4];
        lvlneed[0] = new JComboBox(ItemCreator.LEVELS);
        lvlneed[1] = new JComboBox(ItemCreator.LEVELS);
        lvlneed[2] = new JComboBox(ItemCreator.LEVELS);
        lvlneed[3] = new JComboBox(ItemCreator.LEVELS);
        lvlneed[0].setEditable(false);
        lvlneed[0].setSelectedIndex(0);
        lvlneed[1].setEditable(false);
        lvlneed[1].setSelectedIndex(0);
        lvlneed[2].setEditable(false);
        lvlneed[2].setSelectedIndex(9);
        lvlneed[3].setEditable(false);
        lvlneed[3].setSelectedIndex(0);
        JPanel nameaction = new JPanel();
        nameaction.add(name);
        nameaction.add(actionbox);
        nameaction.add(sound);
        nameaction.add(browsebut);
        cepanel.add(nameaction);
        JPanel cpsm = new JPanel();
        cpsm.add(new JLabel("Class:"));
        cpsm.add(classgain);
        powerlabel = new JLabel("Power:");
        cpsm.add(powerlabel);
        cpsm.add(castpower);
        cpsm.add(power);
        cpsm.add(new JLabel("Delay Before Next Use:"));
        cpsm.add(speed);
        cpsm.add(new JLabel("Mana Consumed:"));
        cpsm.add(mana);
        cepanel.add(cpsm);
        JPanel levelpan = new JPanel(new GridLayout(4, 2));
        Dimension dim1 = new Dimension(335, 110);
        levelpan.setPreferredSize(dim1);
        levelpan.setMaximumSize(dim1);
        levelpan.add(new JLabel("Fighter Level Needed:"));
        levelpan.add(lvlneed[0]);
        levelpan.add(new JLabel("Ninja Level Needed:"));
        levelpan.add(lvlneed[1]);
        levelpan.add(new JLabel("Wizard Level Needed:"));
        levelpan.add(lvlneed[2]);
        levelpan.add(new JLabel("Priest Level Needed:"));
        levelpan.add(lvlneed[3]);
        cepanel.add(levelpan);
        //conjure item panel
        conjurepanel = new JPanel();
        conjureitem = new Item(200);
        conjurelabel = new JLabel(conjureitem.name);
        JButton conjurebutton = new JButton("Change Item");
        conjurebutton.addActionListener(this);
        conjurepanel.add(conjurelabel);
        conjurepanel.add(conjurebutton);
        conjurepanel.setVisible(false);
        //stat boost panel
        statpanel = new JPanel();
        strength = new JTextField("0", 3);
        dexterity = new JTextField("0", 3);
        vitality = new JTextField("0", 3);
        intelligence = new JTextField("0", 3);
        wisdom = new JTextField("0", 3);
        flevel = new JTextField("0", 3);
        nlevel = new JTextField("0", 3);
        wlevel = new JTextField("0", 3);
        plevel = new JTextField("0", 3);
        JPanel statpanel1 = new JPanel();
        JPanel statpanel2 = new JPanel();
        statpanel1.add(new JLabel("Strength:"));
        statpanel1.add(strength);
        statpanel1.add(new JLabel("Dexterity:"));
        statpanel1.add(dexterity);
        statpanel1.add(new JLabel("Vitality:"));
        statpanel1.add(vitality);
        statpanel1.add(new JLabel("Intelligence:"));
        statpanel1.add(intelligence);
        statpanel1.add(new JLabel("Wisdom:"));
        statpanel1.add(wisdom);
        statpanel2.add(new JLabel("Fighter Level:"));
        statpanel2.add(flevel);
        statpanel2.add(new JLabel("Ninja Level:"));
        statpanel2.add(nlevel);
        statpanel2.add(new JLabel("Wizard Level:"));
        statpanel2.add(wlevel);
        statpanel2.add(new JLabel("Priest Level:"));
        statpanel2.add(plevel);
        statpanel.setLayout(new BoxLayout(statpanel, BoxLayout.Y_AXIS));
        statpanel.add(statpanel1);
        statpanel.add(statpanel2);
        statpanel.setVisible(false);
        //weapon/fist panel
        weappanel = new JPanel();
        immbut = new JToggleButton("Hit Non-Material");
        diamondbut = new JToggleButton("Diamond Edge Style");
        stunbut = new JToggleButton("Stunning Blow");
        String[] poisonvals = {"None", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        poison = new JComboBox(poisonvals);
        poison.setSelectedIndex(0);
        weappanel.add(immbut);
        weappanel.add(diamondbut);
        weappanel.add(stunbut);
        weappanel.add(new JLabel("Poison:"));
        weappanel.add(poison);
        weappanel.setVisible(false);
        //put it together
        Box abilitybutbox = Box.createVerticalBox();
        abilitybutbox.add(cepanel);
        abilitybutbox.add(conjurepanel);
        abilitybutbox.add(statpanel);
        abilitybutbox.add(weappanel);
        
        add(abilitybutbox);
        if (lastability != null) setAbility(lastability);
    }
    
    public SpecialAbility createSpecial() {
        //create a new special ability based on current gui configuration
        if (name.getText().equals("")) {
            //pop up error dialog...
            JOptionPane.showMessageDialog(frame, "You must give the ability a name.", "Notice", JOptionPane.ERROR_MESSAGE);
            name.requestFocus();
            return null;
        }
        int p, s, m;
        int[] boost = new int[9];
        Object data = null;
        try {
            if (power.isVisible()) p = Integer.parseInt(power.getText());
            else p = castpower.getSelectedIndex() + 1;
            s = Integer.parseInt(speed.getText());
            m = Integer.parseInt(mana.getText());
            if (SpecialAbility.actions[actionbox.getSelectedIndex()].equals("Stat Boost")) {
                boost[0] = Integer.parseInt(strength.getText());
                boost[1] = Integer.parseInt(dexterity.getText());
                boost[2] = Integer.parseInt(vitality.getText());
                boost[3] = Integer.parseInt(intelligence.getText());
                boost[4] = Integer.parseInt(wisdom.getText());
                boost[5] = Integer.parseInt(flevel.getText());
                boost[6] = Integer.parseInt(nlevel.getText());
                boost[7] = Integer.parseInt(wlevel.getText());
                boost[8] = Integer.parseInt(plevel.getText());
                data = boost;
            }
        } catch (NumberFormatException ex) {
            //pop up error dialog...
            JOptionPane.showMessageDialog(frame, "Invalid Number: " + ex.getMessage(), "Notice", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        char classchar;
        if (classgain.getSelectedIndex() == 0) classchar = 'f';
        else if (classgain.getSelectedIndex() == 1) classchar = 'n';
        else if (classgain.getSelectedIndex() == 2) classchar = 'w';
        else if (classgain.getSelectedIndex() == 3) classchar = 'p';
        else classchar = '-';
        if (SpecialAbility.actions[actionbox.getSelectedIndex()].equals("Conjure")) data = conjureitem;
        else if (SpecialAbility.actions[actionbox.getSelectedIndex()].startsWith("Enhance")) {
            data = new int[4];
            if (immbut.isSelected()) ((int[]) data)[0] = 1;
            if (diamondbut.isSelected()) ((int[]) data)[1] = 1;
            if (stunbut.isSelected()) ((int[]) data)[2] = 1;
            ((int[]) data)[3] = poison.getSelectedIndex();
        }
        lastability = new SpecialAbility(name.getText(), sound.getText(), actionbox.getSelectedIndex(), classchar, p, s, m, lvlneed[0].getSelectedIndex(), lvlneed[1].getSelectedIndex(), lvlneed[2].getSelectedIndex(), lvlneed[3].getSelectedIndex(), 0, data);
        //return new SpecialAbility(lastability);
        return lastability;
    }
    
    public void setAbility(SpecialAbility s) {
        name.setText(s.name);
        sound.setText(s.sound);
        actionbox.setSelectedIndex(s.action);
        if (s.getActionName().equals("Conjure")) {
            conjureitem = Item.createCopy((Item) s.data);
            conjurelabel.setText(conjureitem.name);
        } else if (s.getActionName().equals("Stat Boost")) {
            int[] boost = (int[]) s.data;
            strength.setText("" + boost[0]);
            dexterity.setText("" + boost[1]);
            vitality.setText("" + boost[2]);
            intelligence.setText("" + boost[3]);
            wisdom.setText("" + boost[4]);
            flevel.setText("" + boost[5]);
            nlevel.setText("" + boost[6]);
            wlevel.setText("" + boost[7]);
            plevel.setText("" + boost[8]);
        } else if (s.getActionName().startsWith("Enhance")) {
            int[] boost = (int[]) s.data;
            immbut.setSelected(boost[0] != 0);
            diamondbut.setSelected(boost[1] != 0);
            stunbut.setSelected(boost[2] != 0);
            poison.setSelectedIndex(boost[3]);
        }
        int cindex;
        if (s.classgain == 'f') cindex = 0;
        else if (s.classgain == 'n') cindex = 1;
        else if (s.classgain == 'w') cindex = 2;
        else if (s.classgain == 'p') cindex = 3;
        else cindex = 4;
        classgain.setSelectedIndex(cindex);
        if (power.isVisible()) power.setText("" + s.power);
        else if (castpower.isVisible()) castpower.setSelectedIndex(s.power - 1);
        speed.setText("" + s.speed);
        mana.setText("" + s.mana);
        lvlneed[0].setSelectedIndex(s.flevelneed);
        lvlneed[1].setSelectedIndex(s.nlevelneed);
        lvlneed[2].setSelectedIndex(s.wlevelneed);
        lvlneed[3].setSelectedIndex(s.plevelneed);
        lastability = s;
    }
    
    public boolean hasLastAbility() {
        if (lastability == null) return false;
        return true;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == actionbox) {
            int i = actionbox.getSelectedIndex();
            conjurepanel.setVisible(false);
            statpanel.setVisible(false);
            weappanel.setVisible(false);
            //does not use power:
            if (usenone.contains(SpecialAbility.actions[i])) {
                powerlabel.setVisible(false);
                power.setVisible(false);
                castpower.setVisible(false);
                if (SpecialAbility.actions[i].equals("Conjure")) conjurepanel.setVisible(true);
                else if (SpecialAbility.actions[i].equals("Stat Boost")) statpanel.setVisible(true);
            }
            //uses spell symbol:
            else if (usesymbol.contains(SpecialAbility.actions[i])) {
                power.setVisible(false);
                powerlabel.setVisible(true);
                castpower.setVisible(true);
            }
            //uses raw power value from textfield: (Good ranges will need to be given for each in docs!!! May want to change some of them to use symbol power instead...)
            else {
                castpower.setVisible(false);
                powerlabel.setVisible(true);
                power.setVisible(true);
                if (SpecialAbility.actions[i].startsWith("Enhance")) weappanel.setVisible(true);
            }
            //maybe set default stuff here...
            validate();
        } else if (e.getActionCommand().startsWith("Browse")) {
            dialog.show();
            String s = dialog.getFile();
            if (s != null) {
                sound.setText(s);
            }
        } else if (e.getActionCommand().equals("Change Item")) {
            //change conjured item
            DMEditor.itemwizard.setItem(conjureitem);
            Item tempitem = DMEditor.itemwizard.getItem();
            if (tempitem != null) conjureitem = tempitem;
            if (conjureitem == null) conjureitem = new Item(200);
            conjurelabel.setText(conjureitem.name);
        }
    }
}