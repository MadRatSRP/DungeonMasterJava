import java.awt.Component;
import javax.swing.*;

class MonCellRenderer
    extends DefaultListCellRenderer
    implements ListCellRenderer<Object> {
    
    private JLabel listlabel;
    //public MonCellRenderer() { super(); }
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        //String s = value.toString();
        //setText(s);
        listlabel = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof String) listlabel.setIcon(MonsterData.MonsterIcon[index]);
        else listlabel.setIcon(((MonsterData) value).pic);
        return listlabel;
    }
}

