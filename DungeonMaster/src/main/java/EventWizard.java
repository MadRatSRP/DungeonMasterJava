import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

class EventWizard extends JDialog implements ActionListener, MouseListener {
    private MapData data;
    private Box centerpanel;
    private JTextField[] line = new JTextField[6];
    private JTextField[] newline = new JTextField[6];
    private JTextField picname, newpicname, playsound, choice, statfield, targetlevel, targetx, targety, health, stamina, mana, riddle, riddlenum, soundstring, reusable, fxp, nxp, wxp, pxp, needmonslvl;
    private FileDialog dialog;
    private Color textcolor, textcolor2;
    private JComboBox eventface, picalign, textalign, newalign, needmons;
    private JLabel textcolorlabel, textcolorlabel2, needitemlabel, receiveitemlabel;
    private JList choicelist, actionlist, visiblelist;
    private Vector choicevec, actionvec;
    private int choiceindex = -1, actionindex = -1;
    private JPanel makechoicepanel, actionpanel, newpicpanel, soundpanel, esoundpanel, targetbox2, healingbox, newtextlines, receiveitempanel, needskillpanel, visiblepanel, riddlepanel, changecolorpanel, newalignpanel, reusablepan, xppanel, needmonspanel;
    private AbilityPanel abilitypanel;
    private JComboBox actionbox, needskillbox, levelbox;
    private JToggleButton visiblebut, needitembut, takeitembut, loopsound, needdead, autotrigger, blackback;
    private HeroPanel heropanel;
    private HeroData hero;
    private Item itemneeded, itemreceived;
    private JButton done;
    private JFrame frame;
    
    
    public EventWizard(JFrame f) {
        super(f, "Event Square Wizard", true);
        frame = f;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 590);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //picture panel
        JPanel picpanel = new JPanel();
        picname = new JTextField(10);
        picpanel.add(new JLabel("Filename (blank for none):"));
        picpanel.add(picname);
        JButton browsebut = new JButton("Browse");
        browsebut.setActionCommand("Browse1");
        browsebut.addActionListener(this);
        picpanel.add(browsebut);
        //file dialog for browsing images
        dialog = new FileDialog(f, "Choose A Picture", FileDialog.LOAD);
        dialog.setDirectory("Events");
        //alignment
        String[] alignments = {"Bottom", "Center", "Top"};
        picalign = new JComboBox(alignments);
        picpanel.add(picalign);
        //background
        blackback = new JToggleButton("Black Background");
        blackback.setSelected(true);
        picpanel.add(blackback);
        picpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Initial Picture"));
        
        //message lines
        JPanel messagepanel = new JPanel();
        Box linebox = Box.createVerticalBox();
        for (int i = 0; i < 6; i++) {
            line[i] = new JTextField(24);
            line[i].setFont(DMEditor.scrollfont);
            linebox.add(line[i]);
        }
        messagepanel.add(linebox);
        messagepanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Initial Message"));
        
        //choices
        JPanel choicepanel = new JPanel();
        choicevec = new Vector();
        //choicevec.add(Choice.makeLeave());
        choicelist = new JList(choicevec);
        choicelist.addMouseListener(this);
        JScrollPane choicepane = new JScrollPane(choicelist);
        choicepane.setPreferredSize(new Dimension(250, 120));
        JButton addeditbutton = new JButton("Add/Edit Choice");
        JButton deletebutton = new JButton("Delete Choice");
        addeditbutton.addActionListener(this);
        deletebutton.addActionListener(this);
        JButton moveupbutton = new JButton("Move Choice Up");
        JButton movedownbutton = new JButton("Move Choice Down");
        moveupbutton.addActionListener(this);
        movedownbutton.addActionListener(this);
        Box editpanel = Box.createVerticalBox();
        editpanel.add(addeditbutton);
        editpanel.add(deletebutton);
        editpanel.add(moveupbutton);
        editpanel.add(movedownbutton);
        choicepanel.add(choicepane);
        choicepanel.add(editpanel);
        choicepanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Choices"));
        
        //event facing requirement
        JPanel eventfacepanel = new JPanel();
        final String[] facings = {"Any", "North", "West", "South", "East"};
        eventface = new JComboBox(facings);
        eventface.setSelectedIndex(0);
        eventfacepanel.add(eventface);
        eventfacepanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Direction Party Must Face To Trigger"));
        
        //text color
        JPanel textcolorpanel = new JPanel();
        textcolor = Color.white;
        textcolorlabel = new JLabel("Text Color");
        textcolorlabel.setForeground(textcolor);
        JButton textcolorbutton = new JButton("Set Text Color");
        textcolorbutton.addActionListener(this);
        textcolorpanel.add(textcolorlabel);
        textcolorpanel.add(textcolorbutton);
        //text alignment
        textalign = new JComboBox(alignments);
        textcolorpanel.add(textalign);
        
        //event sound
        esoundpanel = new JPanel();
        esoundpanel.add(new JLabel("Filename (blank for none):"));
        soundstring = new JTextField(10);
        esoundpanel.add(soundstring);
        JButton browsebut4 = new JButton("Browse");
        browsebut4.setActionCommand("Browse4");
        browsebut4.addActionListener(this);
        esoundpanel.add(browsebut4);
        loopsound = new JToggleButton("Loop");
        esoundpanel.add(loopsound);
        esoundpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Initial Sound"));
        
        //choice name and button visibility
        JPanel choicenamepanel = new JPanel();
        choice = new JTextField("Leave", 15);
        choicenamepanel.add(new JLabel("Choice Name:"));
        choicenamepanel.add(choice);
        visiblebut = new JToggleButton("Choice Visible");
        visiblebut.setSelected(true);
        choicenamepanel.add(visiblebut);
        autotrigger = new JToggleButton("Auto-Trigger");
        choicenamepanel.add(autotrigger);
        //action list panel
        JPanel actionlistpanel = new JPanel();
        actionlist = new JList();
        actionlist.addMouseListener(this);
        JScrollPane actionpane = new JScrollPane(actionlist);
        actionpane.setPreferredSize(new Dimension(250, 120));
        JButton addactionbutton = new JButton("Add/Edit Action");
        JButton deleteactionbutton = new JButton("Delete Action");
        JButton moveupaction = new JButton("Move Action Up");
        JButton movedownaction = new JButton("Move Action Down");
        addactionbutton.addActionListener(this);
        deleteactionbutton.addActionListener(this);
        moveupaction.addActionListener(this);
        movedownaction.addActionListener(this);
        Box editpanel2 = Box.createVerticalBox();
        editpanel2.add(addactionbutton);
        editpanel2.add(deleteactionbutton);
        editpanel2.add(moveupaction);
        editpanel2.add(movedownaction);
        actionlistpanel.add(actionpane);
        actionlistpanel.add(editpanel2);
        actionlistpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Actions"));
        //item needed
        JPanel needitempanel = new JPanel();
        needitemlabel = new JLabel();
        needitembut = new JToggleButton("Item Required");
        needitembut.addActionListener(this);
        takeitembut = new JToggleButton("Item Taken");
        takeitembut.setSelected(true);
        takeitembut.setVisible(false);
        needitempanel.add(needitemlabel);
        needitempanel.add(needitembut);
        needitempanel.add(takeitembut);
        needitempanel.setBorder(BorderFactory.createEtchedBorder());
        //skill required
        needskillpanel = new JPanel();
        final String[] skillnames = {"None", "Fighter", "Ninja", "Wizard", "Priest", "Strength", "Dexterity", "Vitality", "Intelligence", "Wisdom", "Average Level", "Average Stat"};
        needskillbox = new JComboBox(skillnames);
        needskillbox.addActionListener(this);
        final String[] levelnames = {"Neophyte", "Novice", "Apprentice", "Journeyman", "Craftsman", "Artisan", "Adept", "Expert", "LO Master", "UM Master", "ON Master", "EE Master", "PAL Master", "MON Master", "ArchMaster"};
        levelbox = new JComboBox(levelnames);
        levelbox.setVisible(false);
        statfield = new JTextField("50", 3);
        statfield.setVisible(false);
        needskillpanel.add(needskillbox);
        needskillpanel.add(levelbox);
        needskillpanel.add(statfield);
        needskillpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Skill Required"));
        //need all monsters #=x on level #=y alive/dead
        needmonspanel = new JPanel();
        needmons = new JComboBox(MonsterWizard.monsternames);
        needmons.insertItemAt("None", 0);
        for (int i = 0; i < MonsterWizard.custommons.size(); i++) {
            needmons.addItem(MonsterWizard.custommons.get(i));
        }
        needmons.setSelectedIndex(0);
        needmonslvl = new JTextField("0", 3);
        needdead = new JToggleButton("Need Them Dead");
        needdead.setSelected(true);
        needmonspanel.add(new JLabel("Monster:"));
        needmonspanel.add(needmons);
        needmonspanel.add(new JLabel("Dungeon Level:"));
        needmonspanel.add(needmonslvl);
        needmonspanel.add(needdead);
        needmonspanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Requires Monsters Dead or Alive"));
        //assemble makechoice panel
        makechoicepanel = new JPanel();
        makechoicepanel.setLayout(new BoxLayout(makechoicepanel, BoxLayout.Y_AXIS));
        makechoicepanel.add(choicenamepanel);
        makechoicepanel.add(actionlistpanel);
        makechoicepanel.add(needitempanel);
        makechoicepanel.add(needskillpanel);
        makechoicepanel.add(needmonspanel);
        makechoicepanel.setVisible(false);
        //action selection
        actionbox = new JComboBox(Action.actionnames);
        actionbox.addActionListener(this);
        //action reusable?
        reusablepan = new JPanel();
        reusable = new JTextField("-1", 3);
        reusablepan.add(new JLabel("Number of Reuses (-1 is Infinite)"));
        reusablepan.add(reusable);
        //target panel for actions
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
        Dimension dim1 = new Dimension(200, 150);
        targetbox2.setPreferredSize(dim1);
        targetbox2.setMaximumSize(dim1);
        targetbox2.add(targetbox);
        targetbox2.add(Box.createVerticalStrut(10));
        targetbox2.add(targetbut);
        targetbox2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Target"));
        targetbox2.setVisible(false);
        //healing panel
        Box hlbox = Box.createVerticalBox();
        Box hfbox = Box.createVerticalBox();
        JLabel healthlabel = new JLabel("Health");
        JLabel staminalabel = new JLabel("Stamina");
        JLabel manalabel = new JLabel("Mana");
        healthlabel.setHorizontalAlignment(JLabel.CENTER);
        staminalabel.setHorizontalAlignment(JLabel.CENTER);
        manalabel.setHorizontalAlignment(JLabel.CENTER);
        health = new JTextField("50", 4);
        stamina = new JTextField("0", 4);
        mana = new JTextField("0", 4);
        hlbox.add(healthlabel);
        hlbox.add(Box.createVerticalStrut(14));
        hlbox.add(staminalabel);
        hlbox.add(Box.createVerticalStrut(15));
        hlbox.add(manalabel);
        hfbox.add(health);
        hfbox.add(Box.createVerticalStrut(10));
        hfbox.add(stamina);
        hfbox.add(Box.createVerticalStrut(10));
        hfbox.add(mana);
        healingbox = new JPanel();
        healingbox.setLayout(new BoxLayout(healingbox, BoxLayout.X_AXIS));
        Dimension dim2 = new Dimension(200, 110);
        healingbox.setPreferredSize(dim2);
        healingbox.setMaximumSize(dim2);
        healingbox.add(hlbox);
        healingbox.add(Box.createHorizontalStrut(5));
        healingbox.add(hfbox);
        healingbox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Healing"));
        healingbox.setVisible(false);
        //hero stuff
        heropanel = new HeroPanel(f, DMEditor.dungfont);
        heropanel.setModal(true);
        //item received
        receiveitempanel = new JPanel();
        receiveitemlabel = new JLabel("none");
        JButton receiveitembut = new JButton("Change Item");
        receiveitembut.addActionListener(this);
        receiveitempanel.add(receiveitemlabel);
        receiveitempanel.add(receiveitembut);
        receiveitempanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Item Received"));
        receiveitempanel.setVisible(false);
        //new text
        newtextlines = new JPanel();
        Box linebox2 = Box.createVerticalBox();
        for (int i = 0; i < 6; i++) {
            newline[i] = new JTextField(24);
            newline[i].setFont(DMEditor.scrollfont);
            linebox2.add(newline[i]);
        }
        newtextlines.add(linebox2);
        newtextlines.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "New Text"));
        newtextlines.setVisible(false);
        //new pic
        newpicpanel = new JPanel();
        newpicname = new JTextField(10);
        newpicpanel.add(newpicname);
        JButton browsebut2 = new JButton("Browse");
        browsebut2.setActionCommand("Browse2");
        browsebut2.addActionListener(this);
        newpicpanel.add(browsebut2);
        newpicpanel.setVisible(false);
        //play sound
        soundpanel = new JPanel();
        playsound = new JTextField(10);
        soundpanel.add(playsound);
        JButton browsebut3 = new JButton("Browse");
        browsebut3.setActionCommand("Browse3");
        browsebut3.addActionListener(this);
        soundpanel.add(browsebut3);
        soundpanel.setVisible(false);
        //visibility of choices
        visiblepanel = new JPanel();
        visiblelist = new JList(choicevec);
        JScrollPane vispane = new JScrollPane(visiblelist);
        vispane.setPreferredSize(new Dimension(250, 160));
        visiblepanel.add(vispane);
        visiblepanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Choices To Set Visibile/Invisible"));
        visiblepanel.setVisible(false);
        //special ability
        abilitypanel = new AbilityPanel(frame);
        abilitypanel.setVisible(false);
        //riddle
        riddle = new JTextField("Answer Goes Here", 20);
        riddlenum = new JTextField("1", 3);
        riddlepanel = new JPanel();
        riddlepanel.add(riddle);
        riddlepanel.add(new JLabel("Number Of Actions If Correct:"));
        riddlepanel.add(riddlenum);
        riddlepanel.setVisible(false);
        //change text color
        textcolorlabel2 = new JLabel("New Text Color");
        textcolor2 = Color.black;
        textcolorlabel2.setForeground(textcolor2);
        JButton textcolorbutton2 = new JButton("Change Color");
        textcolorbutton2.addActionListener(this);
        changecolorpanel = new JPanel();
        changecolorpanel.add(textcolorlabel2);
        changecolorpanel.add(textcolorbutton2);
        changecolorpanel.setVisible(false);
        //change text/pic alignment
        newalign = new JComboBox(alignments);
        newalignpanel = new JPanel();
        newalignpanel.add(newalign);
        newalignpanel.setVisible(false);
        //gain experience
        xppanel = new JPanel();
        fxp = new JTextField("0", 4);
        nxp = new JTextField("0", 4);
        wxp = new JTextField("0", 4);
        pxp = new JTextField("0", 4);
        xppanel.add(new JLabel("Fighter:"));
        xppanel.add(fxp);
        xppanel.add(new JLabel("Ninja:"));
        xppanel.add(nxp);
        xppanel.add(new JLabel("Wizard:"));
        xppanel.add(wxp);
        xppanel.add(new JLabel("Priest:"));
        xppanel.add(pxp);
        xppanel.setVisible(false);
        
        //assemble action panel
        actionpanel = new JPanel();
        actionpanel.setLayout(new BoxLayout(actionpanel, BoxLayout.Y_AXIS));
        JPanel actiontypepanel = new JPanel();
        actiontypepanel.add(new JLabel("Action Type:"));
        actiontypepanel.add(actionbox);
        actiontypepanel.add(reusablepan);
        actionpanel.add(actiontypepanel);
        actionpanel.add(targetbox2);
        actionpanel.add(healingbox);
        actionpanel.add(receiveitempanel);
        actionpanel.add(newtextlines);
        actionpanel.add(newpicpanel);
        actionpanel.add(soundpanel);
        actionpanel.add(visiblepanel);
        actionpanel.add(abilitypanel);
        actionpanel.add(riddlepanel);
        actionpanel.add(changecolorpanel);
        actionpanel.add(newalignpanel);
        actionpanel.add(xppanel);
        actionpanel.setVisible(false);
        
        //center panel
        centerpanel = Box.createVerticalBox();
        centerpanel.add(eventfacepanel);
        centerpanel.add(picpanel);
        centerpanel.add(esoundpanel);
        centerpanel.add(messagepanel);
        centerpanel.add(choicepanel);
        centerpanel.add(textcolorpanel);
        //centerpanel.add(Box.createVerticalGlue());
        
        //bottom panel
        JPanel bottompanel = new JPanel();
        done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.setActionCommand("Done");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottompanel.add(done);
        bottompanel.add(cancel);
        
        //main center panel
        JPanel maincenterpanel = new JPanel();
        maincenterpanel.setLayout(new BoxLayout(maincenterpanel, BoxLayout.Y_AXIS));
        maincenterpanel.add(centerpanel);
        maincenterpanel.add(makechoicepanel);
        maincenterpanel.add(actionpanel);
        maincenterpanel.add(Box.createVerticalGlue());
        
        cp.add(maincenterpanel);
        cp.add("South", bottompanel);
                
                /*
                try {
                java.io.OutputStream out = new java.io.FileOutputStream("eventwiz.log");
                System.setErr(new java.io.PrintStream(out,true));
                } catch (Exception e) {}
                */
        
        dispose();
    }
    
    public void setData(MapData data) {
        if (data.mapchar == 'E') {
            EventSquareData ed = (EventSquareData) data;
            eventface.setSelectedIndex(ed.eventface);
            if (ed.picname != null) picname.setText(ed.picname);
            else picname.setText("");
            picalign.setSelectedIndex(ed.picalign);
            blackback.setSelected(ed.blackback);
            for (int i = 0; i < 6; i++) {
                if (i < ed.eventtext.length) line[i].setText(ed.eventtext[i]);
                else line[i].setText("");
            }
            textcolor = ed.textcolor;
            textcolorlabel.setForeground(textcolor);
            textalign.setSelectedIndex(ed.textalign);
            if (ed.soundstring != null) {
                soundstring.setText(ed.soundstring);
                loopsound.setSelected(ed.loopsound);
            } else {
                soundstring.setText("");
                loopsound.setSelected(false);
            }
            heropanel.removeHeroes();
            choicevec.clear();
            Choice c;
            for (int i = 0; i < ed.choices.length; i++) {
                c = new Choice(ed.choices[i].choicename);
                c.visible = ed.choices[i].visible;
                c.autotrigger = ed.choices[i].autotrigger;
                c.needitem = ed.choices[i].needitem;
                c.takeitem = ed.choices[i].takeitem;
                c.needskill = ed.choices[i].needskill;
                c.needmons = ed.choices[i].needmons;
                c.needmonslvl = ed.choices[i].needmonslvl;
                c.needdead = ed.choices[i].needdead;
                c.actions = new Vector();
                for (int j = 0; j < ed.choices[i].actions.size(); j++) {
                    c.actions.add(ed.choices[i].actions.get(j));
                }
                choicevec.add(c);
            }
        } else {
            choicevec.clear();
            choicevec.add(Choice.makeLeave());
        }
        choicelist.setListData(choicevec);
        choiceindex = -1;
        show();
    }
    
    public void updateMons() {
        //update monster list -> called when custommons changes
        while (needmons.getItemCount() > 29) {
            needmons.removeItemAt(29);
        }
        for (int i = 0; i < MonsterWizard.custommons.size(); i++) {
            needmons.addItem(MonsterWizard.custommons.get(i));
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            if (actionpanel.isVisible()) {
                actionpanel.setVisible(false);
                makechoicepanel.setVisible(true);
                if (actionindex == -1) {
                    actionvec.add(makeAction());
                } else {
                    actionvec.set(actionindex, makeAction());
                }
                actionlist.setListData(actionvec);
                actionindex = -1;
                done.setText("Done Creating Choice");
            } else if (makechoicepanel.isVisible()) {
                makechoicepanel.setVisible(false);
                centerpanel.setVisible(true);
                if (choiceindex == -1) {
                    choicevec.add(makeChoice());
                } else {
                    choicevec.set(choiceindex, makeChoice());
                }
                choicelist.setListData(choicevec);
                choiceindex = -1;
                done.setText("Done");
            } else if (choicevec.size() > 0) {
                String[] eventtext;
                if (line[5].getText().equals("")) {
                    if (line[4].getText().equals("")) {
                        if (line[3].getText().equals("")) {
                            if (line[2].getText().equals("")) {
                                if (line[1].getText().equals("")) {
                                    if (line[0].getText().equals("")) {
                                        eventtext = null;
                                    } else eventtext = new String[1];
                                } else eventtext = new String[2];
                            } else eventtext = new String[3];
                        } else eventtext = new String[4];
                    } else eventtext = new String[5];
                } else eventtext = new String[6];
                if (eventtext != null) for (int i = 0; i < eventtext.length; i++) {
                    eventtext[i] = line[i].getText().trim();
                }
                Choice[] choices = new Choice[choicevec.size()];
                for (int i = 0; i < choicevec.size(); i++) {
                    choices[i] = (Choice) choicevec.get(i);
                }
                data = new EventSquareData(eventface.getSelectedIndex(), picname.getText(), picalign.getSelectedIndex(), blackback.isSelected(), textcolor, textalign.getSelectedIndex(), eventtext, soundstring.getText(), loopsound.isSelected(), choices);
                dispose();
            } else
                JOptionPane.showMessageDialog(frame, "Events must have at least one choice!", "Notice", JOptionPane.ERROR_MESSAGE);
        } else if (e.getActionCommand().equals("Cancel")) {
            if (actionpanel.isVisible()) {
                actionpanel.setVisible(false);
                makechoicepanel.setVisible(true);
                done.setText("Done Creating Choice");
            } else if (makechoicepanel.isVisible()) {
                makechoicepanel.setVisible(false);
                centerpanel.setVisible(true);
                done.setText("Done");
            } else {
                data = null;
                dispose();
            }
        } else if (e.getActionCommand().equals("Set Text Color")) {
            Color tempcolor = JColorChooser.showDialog(this, "Set Text Color", textcolor);
            if (tempcolor != null) {
                textcolor = tempcolor;
                textcolorlabel.setForeground(textcolor);
                textcolorlabel.repaint();
            }
        } else if (e.getActionCommand().equals("Change Color")) {
            Color tempcolor = JColorChooser.showDialog(this, "Set Text Color", textcolor2);
            if (tempcolor != null) {
                textcolor2 = tempcolor;
                textcolorlabel2.setForeground(textcolor2);
                textcolorlabel2.repaint();
            }
        } else if (e.getActionCommand().equals("Add/Edit Choice")) {
            centerpanel.setVisible(false);
            //update choice stuff
            Choice c;
            if (choiceindex == -1) {
                c = new Choice("Choice " + choicevec.size());
                c.actions = new Vector();
            } else c = (Choice) choicevec.get(choiceindex);
            choice.setText(c.choicename);
            visiblebut.setSelected(c.visible);
            autotrigger.setSelected(c.autotrigger);
            //need item (and takeitem)
            needitembut.setSelected(c.needitem != null);
            if (needitembut.isSelected()) {
                takeitembut.setSelected(c.takeitem);
                takeitembut.setVisible(true);
            } else takeitembut.setVisible(false);
            if (needitembut.isSelected()) needitemlabel.setText(c.needitem.name);
            else needitemlabel.setText("");
            //need skill
            if (c.needskill == null) needskillbox.setSelectedIndex(0);
            else {
                needskillbox.setSelectedIndex(c.needskill[0] + 1);
                if (levelbox.isVisible()) levelbox.setSelectedIndex(c.needskill[1] - 1);
                else statfield.setText("" + c.needskill[1]);
            }
            //need monsters
            //needmons.setText(""+c.needmons);
            needmons.setSelectedIndex(c.needmons + 1);
            needmonslvl.setText("" + c.needmonslvl);
            needdead.setSelected(c.needdead);
            //action list
            actionvec = new Vector();
            for (int i = 0; i < c.actions.size(); i++) {
                actionvec.add(c.actions.get(i));
            }
            actionlist.setListData(actionvec);
            actionindex = -1;
            makechoicepanel.setVisible(true);
            done.setText("Done Creating Choice");
        } else if (e.getActionCommand().equals("Add/Edit Action")) {
            makechoicepanel.setVisible(false);
            Action a;
            if (actionindex == -1) a = new Action(0, null, -1);
            else a = (Action) actionvec.get(actionindex);
            //champion join
            if (a.actiontype == 4) {
                hero = new HeroData((HeroData) a.action);
                heropanel.removeHeroes();
            }
            //receive item
            else if (a.actiontype == 5) {
                itemreceived = (Item) a.action;
                receiveitemlabel.setText(itemreceived.name);
            }
            reusable.setText("" + a.reusable);
            actionbox.setSelectedIndex(a.actiontype);
            //toggle,activate,deactivate
            if (a.actiontype > 0 && a.actiontype < 4) {
                int tl = ((MapPoint) a.action).level;
                int tx = ((MapPoint) a.action).x;
                int ty = ((MapPoint) a.action).y;
                targetlevel.setText("" + tl);
                targetx.setText("" + tx);
                targety.setText("" + ty);
            }
            //healing
            else if (a.actiontype == 6) {
                int tl = ((MapPoint) a.action).level;
                int tx = ((MapPoint) a.action).x;
                int ty = ((MapPoint) a.action).y;
                health.setText("" + tl);
                stamina.setText("" + tx);
                mana.setText("" + ty);
            }
            //new text
            else if (a.actiontype == 7) {
                //for (int i=0;i<6;i++) {
                //        if (i<((String[])a.action).length) newline[i].setText(((String[])a.action)[i]);
                //        else newline[i].setText("");
                //}
                for (int i = 0; i < ((String[]) a.action).length; i++) newline[i].setText(((String[]) a.action)[i]);
            }
            //new pic
            else if (a.actiontype == 8) newpicname.setText((String) a.action);
                //play sound
            else if (a.actiontype == 9) playsound.setText((String) a.action);
                //choice visible/invisible
            else if (a.actiontype == 10 || a.actiontype == 11) {
                //visiblelist.setListData(choicevec);
                visiblelist.setSelectedIndices((int[]) a.action); //need to verify indices are valid?
            }
            //learn ability
            else if (a.actiontype == 12) abilitypanel.setAbility((SpecialAbility) a.action);
                //riddle
            else if (a.actiontype == 13) {
                String answer = (String) a.action;
                String num = answer.substring(answer.lastIndexOf('~') + 1);
                answer = answer.substring(0, answer.lastIndexOf('~'));
                riddle.setText(answer);
                riddlenum.setText(num);
            }
            //change text color
            else if (a.actiontype == 14) {
                textcolor2 = (Color) a.action;
                textcolorlabel2.setForeground(textcolor2);
            }
            //change text/pic align
            else if (a.actiontype == 15 || a.actiontype == 16) {
                newalign.setSelectedIndex(((Integer) a.action).intValue());
            }
            //receive experience
            else if (a.actiontype == 17) {
                fxp.setText("" + ((int[]) a.action)[0]);
                nxp.setText("" + ((int[]) a.action)[1]);
                wxp.setText("" + ((int[]) a.action)[2]);
                pxp.setText("" + ((int[]) a.action)[3]);
            }
            actionpanel.setVisible(true);
            done.setText("Done Creating Action");
        } else if (e.getActionCommand().equals("Delete Choice")) {
            if (choiceindex != -1) {
                choicevec.remove(choiceindex);
                choicelist.setListData(choicevec);
                choiceindex = -1;
            }
        } else if (e.getActionCommand().equals("Delete Action")) {
            if (actionindex != -1) {
                actionvec.remove(actionindex);
                actionlist.setListData(actionvec);
                actionindex = -1;
            }
        } else if (e.getActionCommand().equals("Move Choice Up")) {
            if (choiceindex > 0) {
                Choice c = (Choice) choicevec.remove(choiceindex);
                choicevec.add(choiceindex - 1, c);
                choicelist.setListData(choicevec);
                choiceindex = -1;
            }
        } else if (e.getActionCommand().equals("Move Choice Down")) {
            if (choiceindex != -1 && choicevec.size() > 1 && choiceindex < choicevec.size() - 1) {
                Choice c = (Choice) choicevec.remove(choiceindex);
                choicevec.add(choiceindex + 1, c);
                choicelist.setListData(choicevec);
                choiceindex = -1;
            }
        } else if (e.getActionCommand().equals("Move Action Up")) {
            if (actionindex > 0) {
                Action a = (Action) actionvec.remove(actionindex);
                actionvec.add(actionindex - 1, a);
                actionlist.setListData(actionvec);
                actionindex = -1;
            }
        } else if (e.getActionCommand().equals("Move Action Down")) {
            if (actionindex != -1 && actionvec.size() > 1 && actionindex < actionvec.size() - 1) {
                Action a = (Action) actionvec.remove(actionindex);
                actionvec.add(actionindex + 1, a);
                actionlist.setListData(actionvec);
                actionindex = -1;
            }
        } else if (e.getActionCommand().startsWith("Browse")) {
            char c = e.getActionCommand().charAt(6);
            if (c == '3' || c == '4') dialog.setDirectory("Sounds");
            else dialog.setDirectory("Events");
            dialog.show();
            String newpic = dialog.getFile();
            if (newpic != null) {
                if (c == '2') newpicname.setText(newpic);
                else if (c == '3') playsound.setText(newpic);
                else if (c == '4') soundstring.setText(newpic);
                else picname.setText(newpic);
            }
        } else if (e.getActionCommand().equals("From Map...")) {
            DMEditor.targetframe.show();
            MapPoint targ = DMEditor.targetframe.getTarget();
            if (targ != null) {
                targetlevel.setText("" + targ.level);
                targetx.setText("" + targ.x);
                targety.setText("" + targ.y);
            }
        } else if (e.getActionCommand().equals("Item Required")) {
            if (needitembut.isSelected()) {
                DMEditor.itemwizard.show();
                Item tempitem = DMEditor.itemwizard.getItem();
                itemneeded = tempitem;
                if (itemneeded != null) {
                    needitemlabel.setText(itemneeded.name);
                    takeitembut.setVisible(true);
                } else {
                    needitemlabel.setText("");
                    takeitembut.setVisible(false);
                    needitembut.setSelected(false);
                }
            } else {
                itemneeded = null;
                needitemlabel.setText("");
                takeitembut.setVisible(false);
            }
        } else if (e.getActionCommand().equals("Change Item")) {
            DMEditor.itemwizard.setItem(itemreceived);
            Item tempitem = DMEditor.itemwizard.getItem();
            if (tempitem != null) itemreceived = tempitem;
            if (itemreceived == null) itemreceived = new Item(200);
            receiveitemlabel.setText(itemreceived.name);
        } else if (e.getSource() == actionbox) {
            int actiontype = actionbox.getSelectedIndex();
            receiveitempanel.setVisible(false);
            targetbox2.setVisible(false);
            healingbox.setVisible(false);
            newtextlines.setVisible(false);
            newpicpanel.setVisible(false);
            soundpanel.setVisible(false);
            visiblepanel.setVisible(false);
            abilitypanel.setVisible(false);
            riddlepanel.setVisible(false);
            changecolorpanel.setVisible(false);
            newalignpanel.setVisible(false);
            xppanel.setVisible(false);
            if (actiontype > 0) {
                if (actiontype > 0 && actiontype < 4) targetbox2.setVisible(true);
                else if (actiontype == 4) {
                    //champ join
                    if (hero == null) hero = new HeroData();
                    if (!heropanel.hasHero(hero)) {
                        heropanel.addHero(hero);
                        heropanel.setHero(hero);
                    }
                    heropanel.show();
                } else if (actiontype == 5) {
                    //select item
                    if (itemreceived != null) DMEditor.itemwizard.setItem(itemreceived);
                    else DMEditor.itemwizard.show();
                    Item tempitem = DMEditor.itemwizard.getItem();
                    if (tempitem != null) itemreceived = tempitem;
                    if (itemreceived == null) itemreceived = new Item(200);
                    receiveitemlabel.setText(itemreceived.name);
                    receiveitempanel.setVisible(true);
                } else if (actiontype == 6) healingbox.setVisible(true);
                else if (actiontype == 7) {
                    newtextlines.setVisible(true);
                    for (int i = 0; i < 6; i++) newline[i].setText("");
                } else if (actiontype == 8) newpicpanel.setVisible(true);
                else if (actiontype == 9) soundpanel.setVisible(true);
                else if (actiontype == 10 || actiontype == 11) {
                    visiblelist.setListData(choicevec);
                    visiblepanel.setVisible(true);
                } else if (actiontype == 12) abilitypanel.setVisible(true);
                else if (actiontype == 13) riddlepanel.setVisible(true);
                else if (actiontype == 14) changecolorpanel.setVisible(true);
                else if (actiontype == 15 || actiontype == 16) newalignpanel.setVisible(true);
                else if (actiontype == 17) xppanel.setVisible(true);
            }
        } else if (e.getSource() == needskillbox) {
            if (needskillbox.getSelectedIndex() == 0) {
                statfield.setVisible(false);
                levelbox.setVisible(false);
            } else if (needskillbox.getSelectedIndex() < 5 || needskillbox.getSelectedIndex() == 10) {
                statfield.setVisible(false);
                levelbox.setVisible(true);
            } else {
                levelbox.setVisible(false);
                statfield.setVisible(true);
            }
            needskillpanel.validate();
        }
    }
    
    public MapData getData() {
        return data;
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.getSource().equals(choicelist)) {
            int clickedindex = choicelist.locationToIndex(e.getPoint());
            if (clickedindex == -1 || clickedindex == choiceindex) choicelist.clearSelection();
            choiceindex = choicelist.getSelectedIndex();
        } else if (e.getSource().equals(actionlist)) {
            int clickedindex = actionlist.locationToIndex(e.getPoint());
            if (clickedindex == -1 || clickedindex == actionindex) actionlist.clearSelection();
            actionindex = actionlist.getSelectedIndex();
        }
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    private Choice makeChoice() {
        Choice c = new Choice(choice.getText());
        c.actions = actionvec;
        if (needitembut.isSelected()) {
            c.needitem = itemneeded;
            c.takeitem = takeitembut.isSelected();
        }
        if (needskillbox.getSelectedIndex() != 0) {
            c.needskill = new int[2];
            c.needskill[0] = needskillbox.getSelectedIndex() - 1;
            if (c.needskill[0] < 4 || c.needskill[0] == 9) c.needskill[1] = levelbox.getSelectedIndex() + 1;
            else c.needskill[1] = Integer.parseInt(statfield.getText());
        }
        c.needmons = needmons.getSelectedIndex() - 1;
        if (c.needmons >= 0) {
            c.needmonslvl = Integer.parseInt(needmonslvl.getText());
            c.needdead = needdead.isSelected();
        }
        c.visible = visiblebut.isSelected();
        c.autotrigger = autotrigger.isSelected();
        return c;
    }
    
    private Action makeAction() {
        Action a = new Action();
        a.actiontype = actionbox.getSelectedIndex();
        if (a.actiontype > 0 && a.actiontype < 4) {
            //target
            a.action = new MapPoint(Integer.parseInt(targetlevel.getText()), Integer.parseInt(targetx.getText()), Integer.parseInt(targety.getText()));
        } else if (a.actiontype == 4) {
            a.action = hero;
            heropanel.removeHeroes();
            hero = null;
        } else if (a.actiontype == 5) {
            a.action = Item.createCopy(itemreceived);
        } else if (a.actiontype == 6) {
            //healing
            a.action = new MapPoint(Integer.parseInt(health.getText()), Integer.parseInt(stamina.getText()), Integer.parseInt(mana.getText()));
        } else if (a.actiontype == 7) {
            //newtext
            if (newline[5].getText().equals("")) {
                if (newline[4].getText().equals("")) {
                    if (newline[3].getText().equals("")) {
                        if (newline[2].getText().equals("")) {
                            if (newline[1].getText().equals("")) {
                                if (newline[0].getText().equals("")) {
                                    a.action = null;
                                } else a.action = new String[1];
                            } else a.action = new String[2];
                        } else a.action = new String[3];
                    } else a.action = new String[4];
                } else a.action = new String[5];
            } else a.action = new String[6];
            for (int i = 0; i < ((String[]) a.action).length; i++) {
                ((String[]) a.action)[i] = newline[i].getText().trim();
            }
        } else if (a.actiontype == 8) a.action = newpicname.getText();
        else if (a.actiontype == 9) a.action = playsound.getText();
        else if (a.actiontype == 10 || a.actiontype == 11) {
            a.action = visiblelist.getSelectedIndices();
        } else if (a.actiontype == 12) a.action = abilitypanel.createSpecial();
        else if (a.actiontype == 13) a.action = riddle.getText() + "~" + riddlenum.getText();
        else if (a.actiontype == 14) a.action = new Color(textcolor2.getRGB());
        else if (a.actiontype == 15 || a.actiontype == 16) a.action = new Integer(newalign.getSelectedIndex());
        else if (a.actiontype == 17) {
            a.action = new int[4];
            ((int[]) a.action)[0] = Integer.parseInt(fxp.getText());
            ((int[]) a.action)[1] = Integer.parseInt(nxp.getText());
            ((int[]) a.action)[2] = Integer.parseInt(wxp.getText());
            ((int[]) a.action)[3] = Integer.parseInt(pxp.getText());
        }
        a.reusable = Integer.parseInt(reusable.getText());
        return a;
    }
    
}