import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EnterName extends JDialog implements ActionListener {
    JTextField name, lastname;
    dmnew.Hero hero;
    
    public EnterName(JFrame f, dmnew.Hero h) {
        super(f, "Reincarnate", true);
        hero = h;
        Container cp = getContentPane();
        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(4, 1));
        JLabel label1 = new JLabel("Enter first name(max 8 chars):");
        JLabel label2 = new JLabel("Enter last name or title:");
        cp.setBackground(new Color(30, 30, 30));
        pan.setBackground(new Color(30, 30, 30));
        name = new JTextField(hero.name);
        lastname = new JTextField(hero.lastname);
        name.setOpaque(true);
        lastname.setOpaque(true);
        Font fnt = new Font("Times Roman", Font.BOLD, 12);
        name.setFont(fnt);
        lastname.setFont(fnt);
        name.setBackground(new Color(110, 110, 200));
        lastname.setBackground(new Color(110, 110, 200));
        JButton ok = new JButton("Ok");
        ok.addActionListener(this);
        cp.setLayout(new FlowLayout());
        pan.add(label1);
        pan.add(name);
        pan.add(label2);
        pan.add(lastname);
        cp.add(pan);
        cp.add(ok);
        setSize(220, 150);
        //setLocation(225,254);
        setLocationRelativeTo(f);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        show();
    }
    
    public void actionPerformed(ActionEvent e) {
        String nm = name.getText();
        String lnm = lastname.getText();
        if (nm.length() > 8) nm = nm.substring(0, 8);
        if (lnm.length() > 30) lnm = lnm.substring(0, 30);
        hero.name = nm;
        hero.lastname = lnm;
        dispose();
    }
}
