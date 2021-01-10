import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MonSpecials extends JDialog implements ActionListener {
    private MonsterWizard wizard;
    //private JTextField anim,music,sound;
    private JTextField anim, sound;
    private JToggleButton gamewin, handbut;
    //private JPanel animpan,musicpan,soundpan,needpan,needlabpan;
    private JPanel animpan, soundpan, needpan, needlabpan;
    private JLabel hurtlab, needlab;
    private Item hurtitem, needitem;
    private FileDialog dialog;
    
    public MonSpecials(MonsterWizard wizard) {
        super(wizard.frame, "Monster Specials", true);
        this.wizard = wizard;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(360, 370);
        setLocationRelativeTo(null);
        Container cp = getContentPane();
        
        //file dialog for browsing anims/sounds
        dialog = new FileDialog(wizard.frame);
        dialog.setMode(FileDialog.LOAD);
        
        //win game on death
        animpan = new JPanel();
        anim = new JTextField("end0.gif", 15);
        animpan.add(new JLabel("Animation: "));
        animpan.add(anim);
        JButton b1 = new JButton("Browse");
        b1.addActionListener(this);
        animpan.add(b1);
        animpan.setVisible(false);
        //musicpan = new JPanel();
        //music = new JTextField("end0.mid",15);
        //musicpan.add(new JLabel("Music: "));
        //musicpan.add(music);
        //musicpan.setVisible(false);
        soundpan = new JPanel();
        sound = new JTextField("end0.wav", 15);
        soundpan.add(new JLabel("Sound: "));
        soundpan.add(sound);
        JButton b2 = new JButton("Browse");
        b2.setActionCommand("Browse2");
        b2.addActionListener(this);
        soundpan.add(b2);
        soundpan.setVisible(false);
        Box winpan = Box.createVerticalBox();
        gamewin = new JToggleButton("Win Game on Death");
        gamewin.addActionListener(this);
        winpan.add(gamewin);
        winpan.add(animpan);
        //winpan.add(musicpan);
        winpan.add(soundpan);
        JPanel gamewinpan = new JPanel();
        gamewinpan.add(winpan);
        
        //only hurt by item #x
        JToggleButton hurtby = new JToggleButton("Only Certain Item Can Damage");
        hurtby.addActionListener(this);
        hurtlab = new JLabel();
        hurtlab.setVisible(false);
        JPanel hurtpan = new JPanel();
        hurtpan.add(hurtby);
        hurtpan.add(hurtlab);
        
        //need item #x in hand or around neck
        JToggleButton needbut = new JToggleButton("Need Certain Item Before Can Damage");
        needbut.addActionListener(this);
        JPanel needbutpan = new JPanel();
        needbutpan.add(needbut);
        ButtonGroup handneck = new ButtonGroup();
        handbut = new JToggleButton("Need In Hand");
        JToggleButton neckbut = new JToggleButton("Need Around Neck");
        handbut.addActionListener(this);
        neckbut.addActionListener(this);
        handneck.add(handbut);
        handneck.add(neckbut);
        handbut.setSelected(true);
        needlab = new JLabel();
        needlabpan = new JPanel();
        needlabpan.add(needlab);
        needlabpan.setVisible(false);
        needpan = new JPanel();
        needpan.add(handbut);
        needpan.add(neckbut);
        needpan.setVisible(false);
        Box needitembox = Box.createVerticalBox();
        needitembox.add(needbutpan);
        needitembox.add(needlabpan);
        needitembox.add(needpan);
        JPanel needitempan = new JPanel();
        needitempan.add(needitembox);
        
        //activate mappoint on death?
        
        //main center panel
        JPanel center = new JPanel();
        gamewinpan.setBorder(BorderFactory.createLoweredBevelBorder());
        hurtpan.setBorder(BorderFactory.createLoweredBevelBorder());
        needitempan.setBorder(BorderFactory.createLoweredBevelBorder());
        center.add(gamewinpan);
        center.add(hurtpan);
        center.add(needitempan);
        
        //bottom panel
        JPanel bottom = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottom.add(done);
        bottom.add(cancel);
        
        cp.add(center, BorderLayout.CENTER);
        cp.add(bottom, BorderLayout.SOUTH);
        
        //set stuff based on wizard's existing settings
        if (wizard.gamewin) {
            gamewin.doClick();
            anim.setText(wizard.endanim);
            //music.setText(wizard.endmusic);
            sound.setText(wizard.endsound);
        }
        if (wizard.hurtitem != 0) {
            hurtby.setSelected(true);
            //hurtitem = new Item(wizard.hurtitem);
            //hurtlab.setText(hurtitem.name);
            //hurtlab.setVisible(true);
            //potion
            if (wizard.hurtitem > 9 && wizard.hurtitem < 31) {
                hurtitem = new Item(wizard.hurtitem, 1, 1);
            }
            //chest
            else if (wizard.hurtitem == 5) {
                hurtitem = new Chest();
            }
            //scroll
            else if (wizard.hurtitem == 4) {
                String[] mess = new String[5];
                hurtitem = new Item(mess);
            }
            //torch
            else if (wizard.hurtitem == 9) {
                hurtitem = new Torch();
            }
            //waterskin
            else if (wizard.hurtitem == 73) {
                hurtitem = new Waterskin();
            }
            //compass
            else if (wizard.hurtitem == 8) {
                hurtitem = new Compass();
            }
            //everything else
            else if (wizard.hurtitem < 300) hurtitem = new Item(wizard.hurtitem);
            else hurtitem = null;
            if (hurtitem != null) hurtlab.setText(hurtitem.name);
            else hurtlab.setText(hurtitem.name + " (Custom Item #" + (wizard.hurtitem - 299) + ")");
            hurtlab.setVisible(true);
        }
        if (wizard.needitem != 0) {
            needbut.setSelected(true);
            //needitem = new Item(wizard.needitem);
            //needlab.setText(needitem.name);
            //potion
            if (wizard.needitem > 9 && wizard.needitem < 31) {
                needitem = new Item(wizard.needitem, 1, 1);
            }
            //chest
            else if (wizard.needitem == 5) {
                needitem = new Chest();
            }
            //scroll
            else if (wizard.needitem == 4) {
                String[] mess = new String[5];
                needitem = new Item(mess);
            }
            //torch
            else if (wizard.needitem == 9) {
                needitem = new Torch();
            }
            //waterskin
            else if (wizard.needitem == 73) {
                needitem = new Waterskin();
            }
            //compass
            else if (wizard.needitem == 8) {
                needitem = new Compass();
            }
            //everything else
            else if (wizard.needitem < 300) needitem = new Item(wizard.needitem);
            else needitem = null;
            if (needitem != null) needlab.setText(needitem.name);
            else needlab.setText(needitem.name + " (Custom Item #" + (wizard.needitem - 299) + ")");
            needlabpan.setVisible(true);
            needpan.setVisible(true);
            if (wizard.needhandneck != 0) neckbut.doClick();
        }
        
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Win Game on Death")) {
            animpan.setVisible(!animpan.isVisible());
            //musicpan.setVisible(!musicpan.isVisible());
            soundpan.setVisible(!soundpan.isVisible());
        } else if (e.getActionCommand().equals("Only Certain Item Can Damage")) {
            if (hurtlab.isVisible()) {
                hurtitem = null;
                hurtlab.setVisible(false);
            } else {
                //pop up item wizard
                //hurtitem = (new ItemWizard(wizard.frame,"Item Wizard - Select Item That Damages This Monster")).getItem();
                if (hurtitem != null) DMEditor.itemwizard.setItem(hurtitem);
                else DMEditor.itemwizard.show();
                hurtitem = DMEditor.itemwizard.getItem();
                if (hurtitem == null) {
                    ((JToggleButton) e.getSource()).setSelected(false);
                    return;
                }
                hurtlab.setText(hurtitem.name);
                hurtlab.setVisible(true);
            }
        } else if (e.getActionCommand().equals("Need Certain Item Before Can Damage")) {
            if (needpan.isVisible()) {
                needitem = null;
                needlabpan.setVisible(false);
                needpan.setVisible(false);
            } else {
                //pop up item wizard
                //needitem = (new ItemWizard(wizard.frame,"Item Wizard - Select Item Needed")).getItem();
                if (needitem != null) DMEditor.itemwizard.setItem(needitem);
                else DMEditor.itemwizard.show();
                needitem = DMEditor.itemwizard.getItem();
                if (needitem == null) {
                    ((JToggleButton) e.getSource()).setSelected(false);
                    return;
                }
                needlab.setText(needitem.name);
                needlabpan.setVisible(true);
                needpan.setVisible(true);
            }
        } else if (e.getActionCommand().startsWith("Browse")) {
            if (e.getActionCommand().endsWith("2")) {
                dialog.setDirectory("Sounds");
                dialog.setTitle("Choose a Sound");
            } else {
                dialog.setDirectory("Endings");
                dialog.setTitle("Choose an Animation");
            }
            dialog.show();
            String newpic = dialog.getFile();
            if (newpic != null) {
                if (e.getActionCommand().endsWith("2")) sound.setText(newpic);
                else anim.setText(newpic);
            }
            return;
        } else if (e.getActionCommand().equals("Done")) {
            //win game on death
            if (gamewin.isSelected()) {
                wizard.gamewin = true;
                wizard.endanim = anim.getText();
                //wizard.endmusic = music.getText();
                wizard.endsound = sound.getText();
            } else {
                wizard.gamewin = false;
                wizard.endanim = null;
                //wizard.endmusic = null;
                wizard.endsound = null;
            }
            //only hurt by item #x
            if (hurtlab.isVisible()) {
                wizard.hurtitem = hurtitem.number;
            } else wizard.hurtitem = 0;
            //need item #x in hand / around neck
            if (needpan.isVisible()) {
                wizard.needitem = needitem.number;
                if (handbut.isSelected()) wizard.needhandneck = 0;
                else wizard.needhandneck = 1;
            } else {
                wizard.needitem = 0;
                wizard.needhandneck = 0;
            }
            //activate mappoint on death?
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) dispose();
    }
}