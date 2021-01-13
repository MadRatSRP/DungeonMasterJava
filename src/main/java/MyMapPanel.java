import javax.swing.*;
import java.awt.*;

public class MyMapPanel extends JPanel implements Scrollable {
    
    private int maxUnitIncrement = 1;
    
    public MyMapPanel() {
        this(33);
    }
    
    public MyMapPanel(int m) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        maxUnitIncrement = m;
    }
    
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
    
    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation,
                                          int direction) {
        //Get the current position.
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL)
            currentPosition = visibleRect.x;
        else
            currentPosition = visibleRect.y;
        
        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition -
                (currentPosition / maxUnitIncrement) *
                    maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1) *
                maxUnitIncrement - currentPosition;
        }
    }
    
    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        if (orientation == SwingConstants.HORIZONTAL)
            return visibleRect.width - maxUnitIncrement;
        else
            return visibleRect.height - maxUnitIncrement;
    }
    
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
    
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    public void setMaxUnitIncrement(int pixels) {
        maxUnitIncrement = pixels;
    }
}


