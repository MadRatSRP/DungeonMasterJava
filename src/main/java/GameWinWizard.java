import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GameWinWizard extends JDialog implements ActionListener {
    private MapData data;
    private JTextField anim, sound;
    private FileDialog dialog;
    
    public GameWinWizard(JFrame f, MapData data) {
        super(f, "Game Win Wizard", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(365, 130);
        setLocationRelativeTo(f);
        Container cp = getContentPane();
        
        //file dialog for browsing anims/sounds
        dialog = new FileDialog(f);
        dialog.setMode(FileDialog.LOAD);
        
        JPanel animpan = new JPanel();
        anim = new JTextField("end0.gif", 15);
        animpan.add(new JLabel("Animation: "));
        animpan.add(anim);
        JButton b1 = new JButton("Browse");
        b1.addActionListener(this);
        animpan.add(b1);
        
        JPanel soundpan = new JPanel();
        sound = new JTextField("end0.wav", 15);
        soundpan.add(new JLabel("Sound: "));
        soundpan.add(sound);
        JButton b2 = new JButton("Browse");
        b2.setActionCommand("Browse2");
        b2.addActionListener(this);
        soundpan.add(b2);
        
        JPanel center = new JPanel(new GridLayout(2, 1));
        center.add(animpan);
        center.add(soundpan);
        
        JPanel bottom = new JPanel();
        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");
        done.addActionListener(this);
        cancel.addActionListener(this);
        bottom.add(done);
        bottom.add(cancel);
        
        cp.add(center, BorderLayout.CENTER);
        cp.add(bottom, BorderLayout.SOUTH);
        
        if (data.mapchar == 'W') {
            anim.setText(((GameWinData) data).endanim);
            sound.setText(((GameWinData) data).endsound);
        }
        
        //pack();
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            data = new GameWinData(anim.getText(), sound.getText());
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
        } else data = null;
        dispose();
    }
    
    public MapData getData() {
        return data;
    }
}