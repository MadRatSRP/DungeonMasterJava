import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OptionsDialog extends JDialog implements ActionListener {
    
    public static final int LOAD = 1;
    public static final int SAVE = 2;
    public static final int NEWGAME = 3;
    public static final int NEWCUST = 4;
    //public static final int QUIT=4;
    private int value = 0;
    private JToggleButton dark, trans, steps, antialias, automap, showparty;
    private JButton savegame;
    private JSlider sleeper, difficulty, brightadjust;
    private JFrame frame;
    
    public OptionsDialog(JFrame f) {
        super(f, "Options", true);
        frame = f;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container cp = getContentPane();
        
        JPanel loadsave = new JPanel();
        JButton loadgame = new JButton("Load Game");
        savegame = new JButton("Save Game");
        JButton newgame = new JButton("New Game");
        JButton customgame = new JButton("New Custom");
        loadgame.addActionListener(this);
        savegame.addActionListener(this);
        newgame.addActionListener(this);
        customgame.addActionListener(this);
        loadsave.add(loadgame);
        loadsave.add(savegame);
        loadsave.add(newgame);
        loadsave.add(customgame);
        loadsave.setBorder(BorderFactory.createRaisedBevelBorder());
        savegame.setEnabled(false);
        
        JPanel darktrans = new JPanel();
        dark = new JToggleButton("Have Darkness");
        trans = new JToggleButton("Use Transparency");
        steps = new JToggleButton("Play Footsteps");
        JPanel antmap = new JPanel();
        antialias = new JToggleButton("Text Antialiasing");
        automap = new JToggleButton("Use AutoMap");
        showparty = new JToggleButton("Show Party on Map");
        if (!dmnew.NODARK) dark.setSelected(true);
        if (!dmnew.NOTRANS) trans.setSelected(true);
        steps.setSelected(dmnew.PLAYFOOTSTEPS);
        if (dmnew.TEXTANTIALIAS) antialias.setSelected(true);
        automap.setSelected(dmnew.AUTOMAP);
        if (!dmnew.AUTOMAP) showparty.setVisible(false);
        else showparty.setSelected(dmnew.SHOWPARTYMAP);
        automap.addActionListener(this);
        darktrans.add(dark);
        darktrans.add(trans);
        darktrans.add(steps);
        antmap.add(antialias);
        antmap.add(automap);
        antmap.add(showparty);
                
                /*
                JPanel musicpan = new JPanel();
                music = new JToggleButton();
                if (dmnew.music.isplaying) {
                        music.setText("Music On");
                        music.setSelected(true);
                }
                else music.setText("Music Off");
                music.setActionCommand("Music");
                music.addActionListener(this);
                musicpan.add(music);
                */
        
        JPanel brightpan = new JPanel();
        brightpan.setLayout(new BoxLayout(brightpan, BoxLayout.Y_AXIS));
        JPanel bslidepan = new JPanel();
        brightadjust = new JSlider(0, 32, dmnew.BRIGHTADJUST);
        brightadjust.setMajorTickSpacing(1);
        brightadjust.setPaintTicks(true);
        bslidepan.add(new JLabel("None"));
        bslidepan.add(brightadjust);
        bslidepan.add(new JLabel("Brighter"));
        brightpan.add(bslidepan);
        brightpan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Brightness Boost"));
        
        JPanel sleeppan = new JPanel();
        sleeppan.setLayout(new BoxLayout(sleeppan, BoxLayout.Y_AXIS));
        JPanel slidepan = new JPanel();
        sleeper = new JSlider(10, 100, dmnew.SLEEPTIME);
        sleeper.setInverted(true);
        sleeper.setMajorTickSpacing(5);
        sleeper.setPaintTicks(true);
        slidepan.add(new JLabel("Slowest"));
        slidepan.add(sleeper);
        slidepan.add(new JLabel("Fastest"));
        sleeppan.add(slidepan);
        JButton sleepreset = new JButton("Restore Default");
        sleepreset.addActionListener(this);
        sleeppan.add(sleepreset);
        sleeppan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Game Speed"));
        
        JPanel difficultypan = new JPanel();
        difficultypan.setLayout(new BoxLayout(difficultypan, BoxLayout.Y_AXIS));
        JPanel difslidepan = new JPanel();
        difficulty = new JSlider(-2, 2, dmnew.DIFFICULTY);
        //difficulty.setInverted(true);
        difficulty.setMajorTickSpacing(1);
        difficulty.setPaintTicks(true);
        difficulty.setSnapToTicks(true);
        difslidepan.add(new JLabel("Easiest"));
        difslidepan.add(difficulty);
        difslidepan.add(new JLabel("Hardest"));
        difficultypan.add(difslidepan);
        JButton difficultyreset = new JButton("Restore Default");
        difficultyreset.setActionCommand("Restore Difficulty");
        difficultyreset.addActionListener(this);
        difficultypan.add(difficultyreset);
        difficultypan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Game Difficulty"));
        
        JPanel bottom = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        JButton quit = new JButton("Quit Game");
        done.addActionListener(this);
        cancel.addActionListener(this);
        quit.addActionListener(this);
        bottom.add(done);
        bottom.add(cancel);
        bottom.add(Box.createHorizontalStrut(180));
        bottom.add(quit);
        bottom.setBorder(BorderFactory.createRaisedBevelBorder());
        
        Box center = Box.createVerticalBox();
        center.add(loadsave);
        center.add(darktrans);
        center.add(antmap);
        //center.add(musicpan);
        center.add(brightpan);
        center.add(sleeppan);
        center.add(difficultypan);
        
        cp.add(center, BorderLayout.CENTER);
        cp.add(bottom, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(f);
        
        dispose();
    }
    
    public void setAndShow(boolean save) {
        savegame.setEnabled(save);
        value = 0;
        show();
    }
    
    public void resetOptions() {
        dark.setSelected(!dmnew.NODARK);
        trans.setSelected(!dmnew.NOTRANS);
        steps.setSelected(dmnew.PLAYFOOTSTEPS);
        antialias.setSelected(dmnew.TEXTANTIALIAS);
        automap.setSelected(dmnew.AUTOMAP);
        brightadjust.setValue(dmnew.BRIGHTADJUST);
        sleeper.setValue(dmnew.SLEEPTIME);
        difficulty.setValue(dmnew.DIFFICULTY);
        showparty.setVisible(dmnew.AUTOMAP);
        showparty.setSelected(dmnew.SHOWPARTYMAP);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Cancel")) resetOptions();
        else if (e.getActionCommand().equals("New Game")) {
            int returnval = JOptionPane.showConfirmDialog(frame, "Start New Game. Are You Sure?", "New Game?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (returnval == JOptionPane.YES_OPTION) {
                value = NEWGAME;
            } else return;
        } else if (e.getActionCommand().equals("New Custom")) {
            int returnval = JOptionPane.showConfirmDialog(frame, "Start New Custom Game. Are You Sure?", "Custom Game?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (returnval == JOptionPane.YES_OPTION) {
                value = NEWCUST;
            } else return;
        } else if (e.getActionCommand().equals("Load Game")) {
            value = LOAD;
        } else if (e.getActionCommand().equals("Save Game")) {
            value = SAVE;
        } else if (e.getActionCommand().equals("Restore Default")) {
            sleeper.setValue(45);
            return;
        } else if (e.getActionCommand().equals("Restore Difficulty")) {
            difficulty.setValue(0);
            return;
        }
                /*else if (e.getActionCommand().equals("Music")) {
                        if (dmnew.music.isplaying) {
                                music.setText("Music Off");
                                dmnew.music.stop();
                        }
                        else {
                                music.setText("Music On");
                                dmnew.music.start();
                        }
                        return;
                }*/
        else if (e.getActionCommand().equals("Use AutoMap")) {
            showparty.setVisible(automap.isSelected());
            return;
        } else if (e.getActionCommand().equals("Quit Game")) {
            int returnval = JOptionPane.showConfirmDialog(frame, "Quit Game. Are You Sure?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (returnval == JOptionPane.YES_OPTION) {
                dmnew.shutDown();
            } else return;
        } else {
            //"Done"
            if (dark.isSelected()) dmnew.NODARK = false;
            else dmnew.NODARK = true;
            if (trans.isSelected()) dmnew.NOTRANS = false;
            else dmnew.NOTRANS = true;
            dmnew.PLAYFOOTSTEPS = steps.isSelected();
            if (!dmnew.TEXTANTIALIAS && antialias.isSelected()) {
                dmnew.TEXTANTIALIAS = true;
                dmnew.herosheet.offg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            } else if (dmnew.TEXTANTIALIAS && !antialias.isSelected()) {
                dmnew.TEXTANTIALIAS = false;
                dmnew.herosheet.offg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            }
            dmnew.BRIGHTADJUST = brightadjust.getValue();
            dmnew.SLEEPTIME = sleeper.getValue();
            dmnew.DIFFICULTY = difficulty.getValue();
            dmnew.AUTOMAP = automap.isSelected();
            if (dmnew.AUTOMAP) {
                dmnew.SHOWPARTYMAP = showparty.isSelected();
                if (dmnew.dmmap == null) {
                    dmnew.dmmap = new DMMap((dmnew) dmnew.frame, dmnew.numlevels, dmnew.mapwidth, dmnew.mapheight, null);
                    //dmnew.mappane.setViewportView(dmnew.dmmap);
                    dmnew.hspacebox.add(dmnew.dmmap);
                    dmnew.hspacebox.add(Box.createHorizontalGlue());
                    dmnew.dmmap.invalidate();
                    dmnew.vspacebox.invalidate();
                    //dmnew.hspacebox.validate();
                    dmnew.mappane.validate();
                }
            }
        }
        dispose();
    }
    
    public int getValue() {
        return value;
    }
}
