import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class DoorWizard extends JDialog implements ActionListener, MouseListener {
    private MapData data;
    private int level, x, y, side = 0, pictype, opentype = 1, keynumber = 31;
    private boolean isOpen, isBroken;
    private JScrollPane keypanel, tpane1;
    private JPanel keypan2, custompicpan;
    private JList keylist;
    private JToggleButton buttonbut, keybut, nonebut, isopenbut, isBreakable, isbrokenbut, resetcount, consumeskey;
    private JToggleButton woodbut, gratebut, metalbut, fancywoodbut, fancymetalbut, blackbut, woodwbut, blackwbut, redbut, glassbut, ironcrossbut, ironfacebut, custombut, transparent;
    private JToggleButton[] sidebutton;
    private JTextField maxcount, count, breakpoints, picklock, custompic;
    
    public DoorWizard(JFrame f) {
        super(f, "Door Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 520);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        ButtonGroup picgrp = new ButtonGroup();
        woodbut = new JToggleButton("Wood");
        gratebut = new JToggleButton("Grate");
        metalbut = new JToggleButton("Metal");
        fancywoodbut = new JToggleButton("Fancy Wood");
        fancymetalbut = new JToggleButton("Fancy Metal");
        blackbut = new JToggleButton("Black");
        woodwbut = new JToggleButton("Wood w/Window");
        blackwbut = new JToggleButton("Black w/Window");
        redbut = new JToggleButton("Red");
        glassbut = new JToggleButton("Ven Glass");
        ironcrossbut = new JToggleButton("Iron Cross");
        ironfacebut = new JToggleButton("Iron Face");
        custombut = new JToggleButton("Custom...");
        woodbut.addActionListener(this);
        gratebut.addActionListener(this);
        metalbut.addActionListener(this);
        fancywoodbut.addActionListener(this);
        fancymetalbut.addActionListener(this);
        blackbut.addActionListener(this);
        woodwbut.addActionListener(this);
        blackwbut.addActionListener(this);
        redbut.addActionListener(this);
        glassbut.addActionListener(this);
        ironcrossbut.addActionListener(this);
        ironfacebut.addActionListener(this);
        custombut.addActionListener(this);
        picgrp.add(woodbut);
        picgrp.add(gratebut);
        picgrp.add(metalbut);
        picgrp.add(fancywoodbut);
        picgrp.add(fancymetalbut);
        picgrp.add(blackbut);
        picgrp.add(woodwbut);
        picgrp.add(blackwbut);
        picgrp.add(redbut);
        picgrp.add(glassbut);
        picgrp.add(ironcrossbut);
        picgrp.add(ironfacebut);
        picgrp.add(custombut);
        woodbut.setSelected(true);
        
        ButtonGroup opengrp = new ButtonGroup();
        buttonbut = new JToggleButton("Button");
        keybut = new JToggleButton("Key");
        nonebut = new JToggleButton("Other");
        nonebut.addActionListener(this);
        buttonbut.addActionListener(this);
        keybut.addActionListener(this);
        opengrp.add(nonebut);
        opengrp.add(buttonbut);
        opengrp.add(keybut);
        buttonbut.setSelected(true);
        
        JPanel typepanel = new JPanel();
        JPanel tp1 = new JPanel();
        tp1.setLayout(new GridLayout(13, 1, 6, 2));
        //tp1.add(new JLabel("Type:"));
        tp1.add(woodbut);
        tp1.add(gratebut);
        tp1.add(metalbut);
        tp1.add(fancywoodbut);
        tp1.add(fancymetalbut);
        tp1.add(blackbut);
        tp1.add(woodwbut);
        tp1.add(blackwbut);
        tp1.add(redbut);
        tp1.add(glassbut);
        tp1.add(ironcrossbut);
        tp1.add(ironfacebut);
        tp1.add(custombut);
        JPanel tp2 = new JPanel();
        tp2.setLayout(new GridLayout(4, 1, 6, 2));
        tp2.add(new JLabel("Opens By:"));
        tp2.add(buttonbut);
        tp2.add(keybut);
        tp2.add(nonebut);
        tpane1 = new JScrollPane(tp1);
        tpane1.setPreferredSize(new Dimension(200, 260));
        typepanel.add(tpane1);
        typepanel.add(tp2);
        typepanel.setBorder(BorderFactory.createRaisedBevelBorder());
        
        custompicpan = new JPanel();
        custompicpan.add(new JLabel("Pics Name:"));
        custompic = new JTextField(10);
        custompicpan.add(custompic);
        transparent = new JToggleButton("See Items Behind");
        custompicpan.add(transparent);
        custompicpan.setVisible(false);
        
        isopenbut = new JToggleButton("Is Open");
        isBreakable = new JToggleButton("Is Breakable");
        isbrokenbut = new JToggleButton("Is Broken");
        isopenbut.addActionListener(this);
        isBreakable.addActionListener(this);
        isbrokenbut.addActionListener(this);
        breakpoints = new JTextField("80", 4);
        breakpoints.setEnabled(false);
        
        keylist = new JList(ItemWizard.keyitems);
        keylist.addMouseListener(this);
        keylist.setSelectedIndex(0);
        keypanel = new JScrollPane(keylist);
        keypanel.setPreferredSize(new Dimension(80, 60));
        keypanel.setVisible(false);
        keypan2 = new JPanel();
        consumeskey = new JToggleButton("Key Disappears");
        keypan2.add(consumeskey);
        picklock = new JTextField("0", 4);
        keypan2.add(new JLabel("Pick Lock:"));
        keypan2.add(picklock);
        keypan2.setVisible(false);
        
        //switch counter
        JLabel mcountlabel = new JLabel("Total Switch count:");
        maxcount = new JTextField("1", 3);
        JLabel countlabel = new JLabel("Remaining Switch count:");
        count = new JTextField("1", 3);
        resetcount = new JToggleButton("Count Resets");
        //resetcount.setSelected(true);
        JPanel countpanel = new JPanel(new GridLayout(5, 1));
        countpanel.add(mcountlabel);
        countpanel.add(maxcount);
        countpanel.add(countlabel);
        countpanel.add(count);
        countpanel.add(resetcount);
        
        //side chooser
        Box east = Box.createVerticalBox();
        JPanel eastpanel = new JPanel();
        eastpanel.setPreferredSize(new Dimension(140, 70));
        JPanel sidepanel = new JPanel();
        sidepanel.setLayout(new GridLayout(2, 1));
        //sidepanel.setPreferredSize(new Dimension(70,50));
        //sidepanel.setMaximumSize(new Dimension(70,50));
        ButtonGroup sidegrp = new ButtonGroup();
        sidebutton = new JToggleButton[2];
        sidebutton[0] = new JToggleButton("North/South");
        sidebutton[1] = new JToggleButton("East/West");
        sidebutton[0].addActionListener(this);
        sidebutton[1].addActionListener(this);
        sidegrp.add(sidebutton[0]);
        sidegrp.add(sidebutton[1]);
        sidepanel.add(sidebutton[0]);
        sidepanel.add(sidebutton[1]);
        sidebutton[0].setSelected(true);
        JPanel sidelabelpanel = new JPanel();
        JLabel sidelabel = new JLabel("Door Faces:");
        sidelabel.setHorizontalAlignment(JLabel.CENTER);
        sidelabelpanel.add(sidelabel);
        eastpanel.add(sidelabelpanel);
        eastpanel.add(sidepanel);
        east.add(Box.createVerticalGlue());
        east.add(eastpanel);
        east.add(countpanel);
        east.add(Box.createVerticalGlue());
        
        Box centerbox = Box.createVerticalBox();
        centerbox.add(Box.createGlue());
        centerbox.add(typepanel);
        //centerbox.add(Box.createHorizontalGlue());
        //centerbox.add(isopenbut);
        //centerbox.add(Box.createHorizontalGlue());
        JPanel openpanel = new JPanel();
        openpanel.add(isopenbut);
        openpanel.add(isBreakable);
        openpanel.add(breakpoints);
        openpanel.add(isbrokenbut);
        centerbox.add(custompicpan);
        centerbox.add(openpanel);
        centerbox.add(keypanel);
        //centerbox.add(consumeskey);
        //centerbox.add(picklockpan);
        centerbox.add(keypan2);
        centerbox.add(Box.createGlue());
        JPanel centerpanel = new JPanel();
        centerpanel.add(centerbox);
        //centerpanel.add(isopenbut);
        
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
        if (data.mapchar == 'd') {
            DoorData dd = (DoorData) data;
            sidebutton[dd.side].doClick();
            if (dd.pictype == 0) woodbut.doClick();
            else if (dd.pictype == 1) gratebut.doClick();
            else if (dd.pictype == 2) metalbut.doClick();
            else if (dd.pictype == 3) fancywoodbut.doClick();
            else if (dd.pictype == 4) fancymetalbut.doClick();
            else if (dd.pictype == 5) blackbut.doClick();
            else if (dd.pictype == 6) woodwbut.doClick();
            else if (dd.pictype == 7) blackwbut.doClick();
            else if (dd.pictype == 8) redbut.doClick();
            else if (dd.pictype == 9) glassbut.doClick();
            else if (dd.pictype == 10) ironcrossbut.doClick();
            else if (dd.pictype == 11) ironfacebut.doClick();
            else if (dd.pictype == 12) {
                custombut.doClick();
                custompic.setText(dd.custompic);
                transparent.setSelected(dd.transparent);
            }
            JViewport viewport = tpane1.getViewport();
            viewport.setViewPosition(new Point(0, dd.pictype * 27));
            if (dd.isBroken != isbrokenbut.isSelected()) isbrokenbut.doClick();
            if (dd.isOpen != isopenbut.isSelected()) isopenbut.doClick();
            if (dd.isBreakable != isBreakable.isSelected()) isBreakable.doClick();
            if (!dd.isBroken && dd.isBreakable) {
                breakpoints.setText("" + dd.breakpoints);
                breakpoints.setEnabled(true);
            } else breakpoints.setEnabled(false);
            if (dd.opentype == 0) {
                if (!nonebut.isSelected()) nonebut.doClick();
            } else if (dd.opentype == 1) {
                if (!buttonbut.isSelected()) buttonbut.doClick();
            } else {
                if (!keybut.isSelected()) keybut.doClick();
                keylist.setSelectedValue(ItemWizard.keyitems[(dd.keynumber - 31)], true);
                this.keynumber = dd.keynumber;
                consumeskey.setSelected(dd.consumeskey);
                picklock.setText("" + dd.picklock);
            }
            maxcount.setText("" + (dd.maxcount + 1));
            count.setText("" + (dd.count + 1));
            resetcount.setSelected(dd.resetcount);
        }
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Wood")) {
            pictype = 0;
            if (!isBroken) {
                isBreakable.setEnabled(true);
                isbrokenbut.setEnabled(true);
                breakpoints.setText("80");
            }
        } else if (e.getActionCommand().equals("Grate")) {
            pictype = 1;
            if (!isBroken) {
                isBreakable.setEnabled(true);
                isbrokenbut.setEnabled(true);
                breakpoints.setText("280");
            }
        } else if (e.getActionCommand().equals("Metal")) {
            pictype = 2;
        } else if (e.getActionCommand().equals("Fancy Wood")) {
            pictype = 3;
        } else if (e.getActionCommand().equals("Fancy Metal")) {
            pictype = 4;
        } else if (e.getActionCommand().equals("Black")) {
            pictype = 5;
            if (!isBroken) {
                isBreakable.setEnabled(true);
                isbrokenbut.setEnabled(true);
                breakpoints.setText("160");
            }
        } else if (e.getActionCommand().equals("Wood w/Window")) {
            pictype = 6;
        } else if (e.getActionCommand().equals("Black w/Window")) {
            pictype = 7;
        } else if (e.getActionCommand().equals("Red")) {
            pictype = 8;
        } else if (e.getActionCommand().equals("Ven Glass")) {
            pictype = 9;
        } else if (e.getActionCommand().equals("Iron Cross")) {
            pictype = 10;
        } else if (e.getActionCommand().equals("Iron Face")) {
            pictype = 11;
        } else if (e.getActionCommand().equals("Custom...")) {
            pictype = 12;
            custompicpan.setVisible(true);
        } else if (e.getActionCommand().equals("Other")) {
            opentype = 0;
            keypanel.setVisible(false);
            //consumeskey.setVisible(false);
            keypan2.setVisible(false);
        } else if (e.getActionCommand().equals("Button")) {
            opentype = 1;
            keypanel.setVisible(false);
            //consumeskey.setVisible(false);
            keypan2.setVisible(false);
        } else if (e.getActionCommand().equals("Key")) {
            opentype = 2;
            keypanel.setVisible(true);
            //consumeskey.setVisible(true);
            keypan2.setVisible(true);
            validate();
        } else if (e.getActionCommand().equals("Is Open")) {
            isOpen = !isOpen;
        } else if (e.getActionCommand().equals("Is Breakable")) {
            if (isBreakable.isSelected()) breakpoints.setEnabled(true);
            else breakpoints.setEnabled(false);
        } else if (e.getActionCommand().equals("Is Broken")) {
            isBroken = !isBroken;
            if (isBroken) {
                isOpen = true;
                isopenbut.setSelected(true);
                isopenbut.setEnabled(false);
                isBreakable.setSelected(true);
                isBreakable.setEnabled(false);
                breakpoints.setEnabled(false);
            } else {
                isopenbut.setEnabled(true);
                isBreakable.setEnabled(true);
                breakpoints.setEnabled(true);
            }
        } else if (e.getActionCommand().equals("North/South")) {
            side = 0;
        } else if (e.getActionCommand().equals("East/West")) {
            side = 1;
        } else if (e.getActionCommand().equals("Done")) {
            if (opentype != 2) keynumber = -1;
            data = new DoorData(new MapPoint(level, x, y), side, pictype, opentype, isOpen, isBreakable.isSelected(), isBroken, keynumber, consumeskey.isSelected(), Integer.parseInt(maxcount.getText()) - 1, Integer.parseInt(count.getText()) - 1, resetcount.isSelected(), Integer.parseInt(picklock.getText()));
            try {
                if (isBreakable.isSelected() && !isBroken)
                    ((DoorData) data).breakpoints = Integer.parseInt(breakpoints.getText());
            } catch (NumberFormatException ex) {
                ((DoorData) data).breakpoints = 80;
            }
            if (pictype == 12) {
                ((DoorData) data).custompic = custompic.getText();
                ((DoorData) data).transparent = transparent.isSelected();
            }
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            data = null;
            dispose();
        }
        if (pictype > 1 && pictype != 5) {
            isBreakable.setSelected(false);
            isBreakable.setEnabled(false);
            isbrokenbut.setSelected(false);
            isbrokenbut.setEnabled(false);
            breakpoints.setText("");
            breakpoints.setEnabled(false);
            isBroken = false;
            isopenbut.setEnabled(true);
        }
        if (pictype != 12) custompicpan.setVisible(false);
    }
    
    public void mousePressed(MouseEvent e) {
        keynumber = ItemWizard.keynums[keylist.getSelectedIndex()];
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public MapData getData() {
        return data;
    }
    
}
