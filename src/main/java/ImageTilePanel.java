import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageTilePanel extends JPanel {
    
    public static final int BOTTOM = 0;
    public static final int CENTER = 1;
    public static final int TOP = 2;
    
    protected Image tilepic;
    protected MediaTracker tracker;
    protected boolean tiled = true;
    protected boolean scaled = false;
    protected int valign = TOP;
    
    public ImageTilePanel() {
        //this("bluerock.gif");
        super();
        setOpaque(false);
    }
    
    public ImageTilePanel(String picname) {
        super();
        setOpaque(false);
        File testfile = new File(picname);
        if (testfile.exists()) {
            tilepic = Toolkit.getDefaultToolkit().getImage(picname);
            tracker = new MediaTracker(this);
            tracker.addImage(tilepic, 0);
            try {
                tracker.waitForID(0);
            } catch (InterruptedException e) {
            }
            tracker.removeImage(tilepic);
        }
    }
    
    public ImageTilePanel(Image pic) {
        super();
        setOpaque(false);
        tilepic = pic;
    }
    
    public void setImage(String picname) {
        File testfile = new File(picname);
        if (testfile.exists()) {
            tilepic = Toolkit.getDefaultToolkit().getImage(picname);
            tracker = new MediaTracker(this);
            tracker.addImage(tilepic, 0);
            try {
                tracker.waitForID(0);
            } catch (InterruptedException e) {
            }
            tracker.removeImage(tilepic);
            repaint();
        }
    }
    
    public void setImage(Image newpic) {
        tilepic = newpic;
        repaint();
    }
    
    public Image getImage() {
        return tilepic;
    }
    
    public void setTiled(boolean t) {
        tiled = t;
    }
    
    public boolean getTiled() {
        return tiled;
    }
    
    public void setScaled(boolean s) {
        scaled = s;
    }
    
    public boolean getScaled() {
        return scaled;
    }
    
    public void setVerticalAlignment(int align) {
        valign = align;
    }
    
    public int getVerticalAlignment() {
        return valign;
    }
    
    public void paint(Graphics g) {
        if (tilepic != null) {
            //draw the picture first
            int picwidth = tilepic.getWidth(this);
            int picheight = tilepic.getHeight(this);
            int panwidth = this.getSize().width;
            int panheight = this.getSize().height;
            //tile image if set (this is default)
            if (tiled) {
                int numrows = panheight / picheight;
                if (panheight % picheight > 0) numrows++;
                int numcols = panwidth / picwidth;
                if (panwidth % picwidth > 0) numcols++;
                for (int i = 0; i < numrows; i++) {
                    for (int j = 0; j < numcols; j++) {
                        g.drawImage(tilepic, j * picwidth, i * picheight, this);
                    }
                }
            }
            //else scale it to fit panel if set
            else if (scaled) {
                g.drawImage(tilepic, 0, 0, panwidth, panheight, 0, 0, picwidth, picheight, this);
            }
            //else center it horizontally with specified vertical alignment
            else if (valign == TOP) g.drawImage(tilepic, panwidth / 2 - picwidth / 2, 0, this);
            else if (valign == CENTER)
                g.drawImage(tilepic, panwidth / 2 - picwidth / 2, panheight / 2 - picheight / 2, this);
            else g.drawImage(tilepic, panwidth / 2 - picwidth / 2, panheight - picheight, this);
        }
        //else { g.setColor(Color.black); g.fillRect(0,0,this.getSize().width,this.getSize().height); }
        //now draw everything else
        super.paint(g);
    }
}