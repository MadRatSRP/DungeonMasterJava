import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static util.Utils.getImageIconFromFile;

class MapPanel extends JPanel {
    public static final String pathToImageIcons = "Icons" + File.separator;
    
    public static final ImageIcon WallIcon = getImageIconFromFile(pathToImageIcons + "wall.gif");
    public static final ImageIcon DoorIcon = getImageIconFromFile(pathToImageIcons + "door.gif");
    public static final ImageIcon FakeWallIcon = getImageIconFromFile(pathToImageIcons + "fakewall.gif");
    public static final ImageIcon AltarIcon = getImageIconFromFile(pathToImageIcons + "altar.gif");
    public static final ImageIcon AlcoveIcon = getImageIconFromFile(pathToImageIcons + "alcove.gif");
    public static final ImageIcon FountainIcon = getImageIconFromFile(pathToImageIcons + "fountain.gif");
    public static final ImageIcon StairsDownIcon = getImageIconFromFile(pathToImageIcons + "stairsdown.gif");
    public static final ImageIcon StairsUpIcon = getImageIconFromFile(pathToImageIcons + "stairsup.gif");
    public static final ImageIcon TeleportIcon = getImageIconFromFile(pathToImageIcons + "teleport.gif");
    public static final ImageIcon PitIcon = getImageIconFromFile(pathToImageIcons + "pit.gif");
    public static final ImageIcon MirrorIcon = getImageIconFromFile(pathToImageIcons + "mirror.gif");
    public static final ImageIcon WritingIcon = getImageIconFromFile(pathToImageIcons + "writing.gif");
    public static final ImageIcon LauncherIcon = getImageIconFromFile(pathToImageIcons + "launcher.gif");
    public static final ImageIcon GeneratorIcon = getImageIconFromFile(pathToImageIcons + "generator.gif");
    public static final ImageIcon WallSwitchIcon = getImageIconFromFile(pathToImageIcons + "wallswitch.gif");
    public static final ImageIcon MultWallSwitchIcon = getImageIconFromFile(pathToImageIcons + "multwallswitch.gif");
    public static final ImageIcon FloorSwitchIcon = getImageIconFromFile(pathToImageIcons + "floorswitch.gif");
    public static final ImageIcon MultFloorSwitchIcon = getImageIconFromFile(pathToImageIcons + "multfloorswitch.gif");
    public static final ImageIcon SconceIcon = getImageIconFromFile(pathToImageIcons + "sconce.gif");
    public static final ImageIcon DecorationIcon = getImageIconFromFile(pathToImageIcons + "decoration.gif");
    public static final ImageIcon FDecorationIcon = getImageIconFromFile(pathToImageIcons + "fdecoration.gif");
    public static final ImageIcon PillarIcon = getImageIconFromFile(pathToImageIcons + "pillar.gif");
    public static final ImageIcon InvisibleWallIcon = getImageIconFromFile(pathToImageIcons + "invisiblewall.gif");
    public static final ImageIcon EventIcon = getImageIconFromFile(pathToImageIcons + "event.gif");
    public static final ImageIcon GameWinIcon = getImageIconFromFile(pathToImageIcons + "gamewin.gif");
    public static final ImageIcon StormIcon = getImageIconFromFile(pathToImageIcons + "stormbringer.gif");
    public static final ImageIcon GemIcon = getImageIconFromFile(pathToImageIcons + "powergem.gif");
    public static final ImageIcon FulYaIcon = getImageIconFromFile(pathToImageIcons + "fulya.gif");
    
    public static final ImageIcon WallIconS = getImageIconFromFile(pathToImageIcons + "wall-s.gif");
    public static final ImageIcon DoorIconS = getImageIconFromFile(pathToImageIcons + "door-s.gif");
    public static final ImageIcon FakeWallIconS = getImageIconFromFile(pathToImageIcons + "fakewall-s.gif");
    public static final ImageIcon AltarIconS = getImageIconFromFile(pathToImageIcons + "altar-s.gif");
    public static final ImageIcon AlcoveIconS = getImageIconFromFile(pathToImageIcons + "alcove-s.gif");
    public static final ImageIcon FountainIconS = getImageIconFromFile(pathToImageIcons + "fountain-s.gif");
    public static final ImageIcon StairsDownIconS = getImageIconFromFile(pathToImageIcons + "stairsdown-s.gif");
    public static final ImageIcon StairsUpIconS = getImageIconFromFile(pathToImageIcons + "stairsup-s.gif");
    public static final ImageIcon TeleportIconS = getImageIconFromFile(pathToImageIcons + "teleport-s.gif");
    public static final ImageIcon PitIconS = getImageIconFromFile(pathToImageIcons + "pit-s.gif");
    public static final ImageIcon MirrorIconS = getImageIconFromFile(pathToImageIcons + "mirror-s.gif");
    public static final ImageIcon WritingIconS = getImageIconFromFile(pathToImageIcons + "writing-s.gif");
    public static final ImageIcon LauncherIconS = getImageIconFromFile(pathToImageIcons + "launcher-s.gif");
    public static final ImageIcon GeneratorIconS = getImageIconFromFile(pathToImageIcons + "generator-s.gif");
    public static final ImageIcon WallSwitchIconS = getImageIconFromFile(pathToImageIcons + "wallswitch-s.gif");
    public static final ImageIcon MultWallSwitchIconS = getImageIconFromFile(pathToImageIcons + "multwallswitch-s.gif");
    public static final ImageIcon FloorSwitchIconS = getImageIconFromFile(pathToImageIcons + "floorswitch-s.gif");
    public static final ImageIcon MultFloorSwitchIconS = getImageIconFromFile(pathToImageIcons + "multfloorswitch-s.gif");
    public static final ImageIcon SconceIconS = getImageIconFromFile(pathToImageIcons + "sconce-s.gif");
    public static final ImageIcon DecorationIconS = getImageIconFromFile(pathToImageIcons + "decoration-s.gif");
    public static final ImageIcon FDecorationIconS = getImageIconFromFile(pathToImageIcons + "fdecoration-s.gif");
    public static final ImageIcon PillarIconS = getImageIconFromFile(pathToImageIcons + "pillar-s.gif");
    public static final ImageIcon InvisibleWallIconS = getImageIconFromFile(pathToImageIcons + "invisiblewall-s.gif");
    public static final ImageIcon EventIconS = getImageIconFromFile(pathToImageIcons + "event-s.gif");
    public static final ImageIcon GameWinIconS = getImageIconFromFile(pathToImageIcons + "gamewin-s.gif");
    public static final ImageIcon StormIconS = getImageIconFromFile(pathToImageIcons + "stormbringer-s.gif");
    public static final ImageIcon GemIconS = getImageIconFromFile(pathToImageIcons + "powergem-s.gif");
    public static final ImageIcon FulYaIconS = getImageIconFromFile(pathToImageIcons + "fulya-s.gif");
    
    private final DMEditor dmEditor;
    private ArrayList targets;
    private boolean isUpdatedNeeded = true;
    private Dimension bigDimension, smallDimension;
    private final Font bigFont;
    private final Font smallFont;
    private Stroke normalStroke;
    private final Stroke arrowStroke;
    private final Color floorColor;
    private Graphics2D graphics2D;//,offg;
    private BufferedImage pic;
    //private Image offimage;
    
    public MapPanel(DMEditor dmEditor) {
        super(false);
        this.dmEditor = dmEditor;
        
        targets = new ArrayList(1);
        bigDimension = new Dimension(DMEditor.MAPWIDTH * 33, DMEditor.MAPHEIGHT * 33);
        smallDimension = new Dimension(DMEditor.MAPWIDTH * 17, DMEditor.MAPHEIGHT * 17);
        arrowStroke = new BasicStroke(2.0f);
        floorColor = new Color(40, 30, 30);
        setBackground(floorColor);
        setPreferredSize(bigDimension);
        setMaximumSize(bigDimension);
        bigFont = DMEditor.dungfont.deriveFont(8.0f);
        smallFont = DMEditor.dungfont.deriveFont(6.0f);
        pic = new BufferedImage(50 * 33, 50 * 33, BufferedImage.TYPE_INT_BGR);
        graphics2D = pic.createGraphics();
        normalStroke = graphics2D.getStroke();
    }
    
    public void setNewSize() {
        if (DMEditor.MAPWIDTH != (int) bigDimension.getWidth() / 33 || DMEditor.MAPHEIGHT != (int) bigDimension.getHeight() / 33) {
            bigDimension = new Dimension(DMEditor.MAPWIDTH * 33, DMEditor.MAPHEIGHT * 33);
            smallDimension = new Dimension(DMEditor.MAPWIDTH * 17, DMEditor.MAPHEIGHT * 17);
            if (DMEditor.MAPWIDTH > pic.getWidth(null) / 33 || DMEditor.MAPHEIGHT > pic.getHeight(null) / 33) {
                pic.flush();
                graphics2D.dispose();
                pic = null;
                System.gc();
                pic = new BufferedImage(DMEditor.MAPWIDTH * 33, DMEditor.MAPHEIGHT * 33, BufferedImage.TYPE_INT_BGR);
                graphics2D = pic.createGraphics();
                normalStroke = graphics2D.getStroke();
            }
            setZoom();
            graphics2D.setClip(0, 0, DMEditor.MAPWIDTH * 33, DMEditor.MAPHEIGHT * 33);
        }
    }
    
    public void setZoom() {
        if (!dmEditor.ZOOMING) {
            setPreferredSize(bigDimension);
            setMaximumSize(bigDimension);
            graphics2D.setFont(bigFont);
        } else {
            graphics2D.setColor(floorColor);
            graphics2D.fillRect(0, 0, DMEditor.MAPWIDTH * 33, DMEditor.MAPHEIGHT * 33);
            setPreferredSize(smallDimension);
            setMaximumSize(smallDimension);
            graphics2D.setFont(smallFont);
        }
    }
    
    public void forcePaint() {
        super.repaint();
    }
    
    public void repaint() {
        isUpdatedNeeded = true;
        super.repaint();
    }
    
    public void paintSquare(int x, int y, boolean drawpic) {
        if (!dmEditor.ZOOMING) {
            graphics2D.setColor(floorColor);
            graphics2D.fillRect(x * 33, y * 33, 32, 32);
            Image mappic = getPic(dmEditor.mapdata[x][y]);
            int xpos = x * 33, ypos = y * 33;
            if (mappic != null) graphics2D.drawImage(mappic, xpos, ypos, this);
            /*
               if (mappic!=null) {
                    java.awt.geom.AffineTransform t = null;
                    int side = 0;
                    if (dmed.mapdata[x][y].mapchar=='d') side = ((DoorData)dmed.mapdata[x][y]).side;
                    else if (dmed.mapdata[x][y] instanceof SidedWallData) side = ((SidedWallData)dmed.mapdata[x][y]).side;
                    if (side!=0) {
                            t = g.getTransform();
                            g.setTransform(java.awt.geom.AffineTransform.getRotateInstance(-Math.PI/2.0*side,xpos+16,ypos+16));
                    }
                    g.drawImage(mappic,xpos,ypos,this);
                    if (t!=null) g.setTransform(t);
               }
            */
            //nomons
            if (dmEditor.mapdata[x][y].nomons) {
                graphics2D.setColor(Color.blue);
                graphics2D.drawRect(xpos, ypos, 31, 31);
            }
            //noghosts
            else if (dmEditor.mapdata[x][y].noghosts) {
                graphics2D.setColor(Color.green);
                graphics2D.drawRect(xpos, ypos, 31, 31);
            }
            //items
            graphics2D.setColor(Color.blue);
            if (dmEditor.mapdata[x][y].hasItems && dmEditor.mapdata[x][y].mapchar != ']'
                && dmEditor.mapdata[x][y].mapchar != '[' && dmEditor.mapdata[x][y].mapchar != 'a'
                && dmEditor.mapdata[x][y].mapchar != 'f') { //not alcoves or fountain
                if (dmEditor.mapdata[x][y].numitemsin[0] > 0) graphics2D.drawString("*", xpos + 4, ypos + 11);
                if (dmEditor.mapdata[x][y].numitemsin[1] > 0) graphics2D.drawString("*", xpos + 25, ypos + 11);
                if (dmEditor.mapdata[x][y].numitemsin[2] > 0) graphics2D.drawString("*", xpos + 25, ypos + 29);
                if (dmEditor.mapdata[x][y].numitemsin[3] > 0) graphics2D.drawString("*", xpos + 4, ypos + 29);
            } else { //alcoves
                if (dmEditor.mapdata[x][y].numitemsin[0] > 0) graphics2D.drawString("*", xpos + 14, ypos + 12);
                if (dmEditor.mapdata[x][y].numitemsin[1] > 0) graphics2D.drawString("*", xpos + 4, ypos + 20);
                if (dmEditor.mapdata[x][y].numitemsin[2] > 0) graphics2D.drawString("*", xpos + 14, ypos + 29);
                if (dmEditor.mapdata[x][y].numitemsin[3] > 0) graphics2D.drawString("*", xpos + 24, ypos + 20);
            }
            //mons
            if (dmEditor.mapdata[x][y].hasMons) {
                graphics2D.setColor(Color.red);
                if (dmEditor.mapdata[x][y].hasmonin[0]) graphics2D.drawString("*", xpos + 7, ypos + 14);
                if (dmEditor.mapdata[x][y].hasmonin[1]) graphics2D.drawString("*", xpos + 22, ypos + 14);
                if (dmEditor.mapdata[x][y].hasmonin[2]) graphics2D.drawString("*", xpos + 22, ypos + 26);
                if (dmEditor.mapdata[x][y].hasmonin[3]) graphics2D.drawString("*", xpos + 7, ypos + 26);
                if (dmEditor.mapdata[x][y].hasmonin[4]) graphics2D.drawString("*", xpos + 14, ypos + 20);
            }
            //party
            if (dmEditor.mapdata[x][y].hasParty) {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(Color.green);
                graphics2D.drawArc(xpos + 2, ypos + 2, 28, 28, 45, 90);
                graphics2D.setColor(Color.yellow);
                graphics2D.drawArc(xpos + 2, ypos + 2, 28, 28, 135, 90);
                graphics2D.setColor(Color.red);
                graphics2D.drawArc(xpos + 2, ypos + 2, 28, 28, 225, 90);
                graphics2D.setColor(Color.blue);
                graphics2D.drawArc(xpos + 2, ypos + 2, 28, 28, 315, 90);
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
            if (drawpic) {
                //super.repaint();
                paint(getGraphics());
            }
        } else {
            graphics2D.setColor(floorColor);
            graphics2D.fillRect(x * 17, y * 17, 16, 16);
            Image mappic = getPic(dmEditor.mapdata[x][y]);
            int xpos = x * 17, ypos = y * 17;
            if (mappic != null) graphics2D.drawImage(mappic, xpos, ypos, this);
            /*
               if (mappic!=null) {
                    java.awt.geom.AffineTransform t = null;
                    int side = 0;
                    if (dmed.mapdata[x][y].mapchar=='d') side = ((DoorData)dmed.mapdata[x][y]).side;
                    else if (dmed.mapdata[x][y] instanceof SidedWallData) side = ((SidedWallData)dmed.mapdata[x][y]).side;
                    if (side!=0) {
                            t = g.getTransform();
                            g.setTransform(java.awt.geom.AffineTransform.getRotateInstance(-Math.PI/2.0*side,xpos+8,ypos+8));
                    }
                    g.drawImage(mappic,xpos,ypos,this);
                    if (t!=null) g.setTransform(t);
               }
            */
            //nomons
            if (dmEditor.mapdata[x][y].nomons) {
                graphics2D.setColor(Color.blue);
                graphics2D.drawRect(xpos, ypos, 15, 15);
            }
            //noghosts
            else if (dmEditor.mapdata[x][y].noghosts) {
                graphics2D.setColor(Color.green);
                graphics2D.drawRect(xpos, ypos, 15, 15);
            }
            //items
            graphics2D.setColor(Color.blue);
            if (dmEditor.mapdata[x][y].hasItems && dmEditor.mapdata[x][y].mapchar != ']'
                && dmEditor.mapdata[x][y].mapchar != '[' && dmEditor.mapdata[x][y].mapchar != 'a'
                && dmEditor.mapdata[x][y].mapchar != 'f') { //not alcoves or fountain
                if (dmEditor.mapdata[x][y].numitemsin[0] > 0) graphics2D.drawString("*", xpos + 2, ypos + 6);
                if (dmEditor.mapdata[x][y].numitemsin[1] > 0) graphics2D.drawString("*", xpos + 12, ypos + 6);
                if (dmEditor.mapdata[x][y].numitemsin[2] > 0) graphics2D.drawString("*", xpos + 12, ypos + 16);
                if (dmEditor.mapdata[x][y].numitemsin[3] > 0) graphics2D.drawString("*", xpos + 2, ypos + 16);
            } else { //alcoves
                if (dmEditor.mapdata[x][y].numitemsin[0] > 0) graphics2D.drawString("*", xpos + 7, ypos + 6);
                if (dmEditor.mapdata[x][y].numitemsin[1] > 0) graphics2D.drawString("*", xpos + 2, ypos + 11);
                if (dmEditor.mapdata[x][y].numitemsin[2] > 0) graphics2D.drawString("*", xpos + 7, ypos + 16);
                if (dmEditor.mapdata[x][y].numitemsin[3] > 0) graphics2D.drawString("*", xpos + 12, ypos + 11);
            }
            //mons
            if (dmEditor.mapdata[x][y].hasMons) {
                graphics2D.setColor(Color.red);
                if (dmEditor.mapdata[x][y].hasmonin[0]) graphics2D.drawString("*", xpos + 4, ypos + 8);
                if (dmEditor.mapdata[x][y].hasmonin[1]) graphics2D.drawString("*", xpos + 10, ypos + 8);
                if (dmEditor.mapdata[x][y].hasmonin[2]) graphics2D.drawString("*", xpos + 10, ypos + 14);
                if (dmEditor.mapdata[x][y].hasmonin[3]) graphics2D.drawString("*", xpos + 4, ypos + 14);
                if (dmEditor.mapdata[x][y].hasmonin[4]) graphics2D.drawString("*", xpos + 7, ypos + 11);
            }
            //party
            if (dmEditor.mapdata[x][y].hasParty) {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(Color.green);
                graphics2D.drawArc(xpos + 1, ypos + 1, 14, 14, 45, 90);
                graphics2D.setColor(Color.yellow);
                graphics2D.drawArc(xpos + 1, ypos + 1, 14, 14, 135, 90);
                graphics2D.setColor(Color.red);
                graphics2D.drawArc(xpos + 1, ypos + 1, 14, 14, 225, 90);
                graphics2D.setColor(Color.blue);
                graphics2D.drawArc(xpos + 1, ypos + 1, 14, 14, 315, 90);
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
            if (drawpic) {
                //super.repaint();
                paint(getGraphics());
            }
        }
    }
    
    public void paint(Graphics ong) {
        if (!isUpdatedNeeded) {
            ong.drawImage(pic, 0, 0, this);
            if (!targets.isEmpty()) drawTargets(ong);
            if (dmEditor.SQUARELOCKED) {
                int size = 32;
                if (dmEditor.ZOOMING) size = 16;
                ong.setColor(Color.white);
                ((Graphics2D) ong).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ong.drawOval((int) (dmEditor.lockx * (size + 1) - 3 * size / 8), (int) (dmEditor.locky * (size + 1) - 3 * size / 8), (int) (size * 1.75), (int) (size * 1.75));
                ((Graphics2D) ong).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
            return;
        } else if (!dmEditor.ZOOMING) {
            graphics2D.setColor(floorColor);
            graphics2D.fillRect(0, 0, DMEditor.MAPWIDTH * 33, DMEditor.MAPHEIGHT * 33);
            graphics2D.setFont(bigFont);
            Image mappic;
            int xpos = 0, ypos = 0;
            for (int y = 0; y < DMEditor.MAPHEIGHT; y++) {
                for (int x = 0; x < DMEditor.MAPWIDTH; x++) {
                    mappic = getPic(dmEditor.mapdata[x][y]);
                    xpos = x * 33;
                    ypos = y * 33;
                    if (mappic != null) graphics2D.drawImage(mappic, xpos, ypos, this);
                    /*
                       if (mappic!=null) {
                            java.awt.geom.AffineTransform t = null;
                            int side = 0;
                            if (dmed.mapdata[x][y].mapchar=='d') side = ((DoorData)dmed.mapdata[x][y]).side;
                            else if (dmed.mapdata[x][y] instanceof SidedWallData) side = ((SidedWallData)dmed.mapdata[x][y]).side;
                            if (side!=0) {
                                    t = g.getTransform();
                                    g.setTransform(java.awt.geom.AffineTransform.getRotateInstance(-Math.PI/2.0*side,xpos+16,ypos+16));
                            }
                            g.drawImage(mappic,xpos,ypos,this);
                            if (t!=null) g.setTransform(t);
                       }
                    */
                    //nomons
                    if (dmEditor.mapdata[x][y].nomons) {
                        graphics2D.setColor(Color.blue);
                        graphics2D.drawRect(xpos, ypos, 31, 31);
                    }
                    //noghosts
                    else if (dmEditor.mapdata[x][y].noghosts) {
                        graphics2D.setColor(Color.green);
                        graphics2D.drawRect(xpos, ypos, 31, 31);
                    }
                    //items
                    graphics2D.setColor(Color.blue);
                    if (dmEditor.mapdata[x][y].hasItems && dmEditor.mapdata[x][y].mapchar != ']'
                        && dmEditor.mapdata[x][y].mapchar != '[' && dmEditor.mapdata[x][y].mapchar != 'a'
                        && dmEditor.mapdata[x][y].mapchar != 'f') { //not alcoves or fountain
                        if (dmEditor.mapdata[x][y].numitemsin[0] > 0) graphics2D.drawString("*", xpos + 4, ypos + 11);
                        if (dmEditor.mapdata[x][y].numitemsin[1] > 0) graphics2D.drawString("*", xpos + 25, ypos + 11);
                        if (dmEditor.mapdata[x][y].numitemsin[2] > 0) graphics2D.drawString("*", xpos + 25, ypos + 29);
                        if (dmEditor.mapdata[x][y].numitemsin[3] > 0) graphics2D.drawString("*", xpos + 4, ypos + 29);
                    } else { //alcoves
                        if (dmEditor.mapdata[x][y].numitemsin[0] > 0) graphics2D.drawString("*", xpos + 14, ypos + 12);
                        if (dmEditor.mapdata[x][y].numitemsin[1] > 0) graphics2D.drawString("*", xpos + 4, ypos + 20);
                        if (dmEditor.mapdata[x][y].numitemsin[2] > 0) graphics2D.drawString("*", xpos + 14, ypos + 29);
                        if (dmEditor.mapdata[x][y].numitemsin[3] > 0) graphics2D.drawString("*", xpos + 24, ypos + 20);
                    }
                    //mons
                    if (dmEditor.mapdata[x][y].hasMons) {
                        graphics2D.setColor(Color.red);
                        if (dmEditor.mapdata[x][y].hasmonin[0]) graphics2D.drawString("*", xpos + 7, ypos + 14);
                        if (dmEditor.mapdata[x][y].hasmonin[1]) graphics2D.drawString("*", xpos + 22, ypos + 14);
                        if (dmEditor.mapdata[x][y].hasmonin[2]) graphics2D.drawString("*", xpos + 22, ypos + 26);
                        if (dmEditor.mapdata[x][y].hasmonin[3]) graphics2D.drawString("*", xpos + 7, ypos + 26);
                        if (dmEditor.mapdata[x][y].hasmonin[4]) graphics2D.drawString("*", xpos + 14, ypos + 20);
                    }
                    //party
                    if (dmEditor.mapdata[x][y].hasParty) {
                        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        graphics2D.setColor(Color.green);
                        graphics2D.drawArc(xpos + 2, ypos + 2, 28, 28, 45, 90);
                        graphics2D.setColor(Color.yellow);
                        graphics2D.drawArc(xpos + 2, ypos + 2, 28, 28, 135, 90);
                        graphics2D.setColor(Color.red);
                        graphics2D.drawArc(xpos + 2, ypos + 2, 28, 28, 225, 90);
                        graphics2D.setColor(Color.blue);
                        graphics2D.drawArc(xpos + 2, ypos + 2, 28, 28, 315, 90);
                        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                }
            }
        } else { //ZOOMING
            graphics2D.setColor(floorColor);
            graphics2D.fillRect(0, 0, DMEditor.MAPWIDTH * 17, DMEditor.MAPHEIGHT * 17);
            Image mappic;
            int xpos = 0, ypos = 0;
            for (int y = 0; y < DMEditor.MAPHEIGHT; y++) {
                for (int x = 0; x < DMEditor.MAPWIDTH; x++) {
                    mappic = getPic(dmEditor.mapdata[x][y]);
                    xpos = x * 17;
                    ypos = y * 17;
                    if (mappic != null) graphics2D.drawImage(mappic, xpos, ypos, this);
                   /*
                       if (mappic!=null) {
                            java.awt.geom.AffineTransform t = null;
                            int side = 0;
                            if (dmed.mapdata[x][y].mapchar=='d') side = ((DoorData)dmed.mapdata[x][y]).side;
                            else if (dmed.mapdata[x][y] instanceof SidedWallData) side = ((SidedWallData)dmed.mapdata[x][y]).side;
                            if (side!=0) {
                                    t = g.getTransform();
                                    g.setTransform(java.awt.geom.AffineTransform.getRotateInstance(-Math.PI/2.0*side,xpos+8,ypos+8));
                            }
                            g.drawImage(mappic,xpos,ypos,this);
                            if (t!=null) g.setTransform(t);
                       }
                   */
                    //nomons
                    if (dmEditor.mapdata[x][y].nomons) {
                        graphics2D.setColor(Color.blue);
                        graphics2D.drawRect(xpos, ypos, 15, 15);
                    }
                    //noghosts
                    else if (dmEditor.mapdata[x][y].noghosts) {
                        graphics2D.setColor(Color.green);
                        graphics2D.drawRect(xpos, ypos, 15, 15);
                    }
                    //items
                    graphics2D.setColor(Color.blue);
                    if (dmEditor.mapdata[x][y].hasItems && dmEditor.mapdata[x][y].mapchar != ']'
                        && dmEditor.mapdata[x][y].mapchar != '[' && dmEditor.mapdata[x][y].mapchar != 'a'
                        && dmEditor.mapdata[x][y].mapchar != 'f') { //not alcoves or fountain
                        if (dmEditor.mapdata[x][y].numitemsin[0] > 0) graphics2D.drawString("*", xpos + 2, ypos + 6);
                        if (dmEditor.mapdata[x][y].numitemsin[1] > 0) graphics2D.drawString("*", xpos + 12, ypos + 6);
                        if (dmEditor.mapdata[x][y].numitemsin[2] > 0) graphics2D.drawString("*", xpos + 12, ypos + 16);
                        if (dmEditor.mapdata[x][y].numitemsin[3] > 0) graphics2D.drawString("*", xpos + 2, ypos + 16);
                    } else { //alcoves
                        if (dmEditor.mapdata[x][y].numitemsin[0] > 0) graphics2D.drawString("*", xpos + 7, ypos + 6);
                        if (dmEditor.mapdata[x][y].numitemsin[1] > 0) graphics2D.drawString("*", xpos + 2, ypos + 11);
                        if (dmEditor.mapdata[x][y].numitemsin[2] > 0) graphics2D.drawString("*", xpos + 7, ypos + 16);
                        if (dmEditor.mapdata[x][y].numitemsin[3] > 0) graphics2D.drawString("*", xpos + 12, ypos + 11);
                    }
                    //mons
                    if (dmEditor.mapdata[x][y].hasMons) {
                        graphics2D.setColor(Color.red);
                        if (dmEditor.mapdata[x][y].hasmonin[0]) graphics2D.drawString("*", xpos + 4, ypos + 8);
                        if (dmEditor.mapdata[x][y].hasmonin[1]) graphics2D.drawString("*", xpos + 10, ypos + 8);
                        if (dmEditor.mapdata[x][y].hasmonin[2]) graphics2D.drawString("*", xpos + 10, ypos + 14);
                        if (dmEditor.mapdata[x][y].hasmonin[3]) graphics2D.drawString("*", xpos + 4, ypos + 14);
                        if (dmEditor.mapdata[x][y].hasmonin[4]) graphics2D.drawString("*", xpos + 7, ypos + 11);
                    }
                    //party
                    if (dmEditor.mapdata[x][y].hasParty) {
                        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        graphics2D.setColor(Color.green);
                        graphics2D.drawArc(xpos + 1, ypos + 1, 14, 14, 45, 90);
                        graphics2D.setColor(Color.yellow);
                        graphics2D.drawArc(xpos + 1, ypos + 1, 14, 14, 135, 90);
                        graphics2D.setColor(Color.red);
                        graphics2D.drawArc(xpos + 1, ypos + 1, 14, 14, 225, 90);
                        graphics2D.setColor(Color.blue);
                        graphics2D.drawArc(xpos + 1, ypos + 1, 14, 14, 315, 90);
                        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                }
            }
        }
        isUpdatedNeeded = false;
        ong.drawImage(pic, 0, 0, this);
        if (!targets.isEmpty()) drawTargets(ong);
        if (dmEditor.SQUARELOCKED) {
            int size = 32;
            if (dmEditor.ZOOMING) size = 16;
            ong.setColor(Color.white);
            ((Graphics2D) ong).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ong.drawOval((int) (dmEditor.lockx * (size + 1) - 3 * size / 8), (int) (dmEditor.locky * (size + 1) - 3 * size / 8), (int) (size * 1.75), (int) (size * 1.75));
            ((Graphics2D) ong).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }
    
    private void drawTargets(Graphics graphics) {
        //iterate thru targets arraylist, drawing red borders (and maybe actual arrows and up/down level indicators)
        int size = 32;
        if (dmEditor.ZOOMING) size = 16;
        Graphics2D gg = (Graphics2D) graphics;
        gg.setColor(Color.red);
        Target target;
        int numtargets = targets.size();
        for (Object object : targets) {
            target = (Target) object;
            if (target.targetl == DMEditor.currentlevel) {
                gg.drawRect(target.targetx * (size + 1), target.targety * (size + 1), size - 1, size - 1);
            }
        }
        if (!dmEditor.ZOOMING) gg.setStroke(arrowStroke);
        for (Object object : targets) {
            target = (Target) object;
            if (target.targetl == DMEditor.currentlevel) {
                gg.drawLine(target.originX * (size + 1) + size / 2, target.originY * (size + 1) + size / 2,
                    target.targetx * (size + 1) + size / 2, target.targety * (size + 1) + size / 2);
            } else {
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gg.drawOval((int) (target.originX * (size + 1) - size / 4), (int) (target.originY * (size + 1) - size / 4),
                    (int) (size * 1.5), (int) (size * 1.5));
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
        }
        if (!dmEditor.ZOOMING) gg.setStroke(normalStroke);
    }
    
    private Image getPic(MapData mapData) {
        if (mapData.mapchar == '1') {
            if (!dmEditor.ZOOMING) return WallIcon.getImage();
            else return WallIconS.getImage();
        } else if (mapData.mapchar == '0') return null;
        else if (mapData.mapchar == '2') {
            if (!dmEditor.ZOOMING) return FakeWallIcon.getImage();
            else return FakeWallIconS.getImage();
        } else if (mapData.mapchar == '>') {
            if (!dmEditor.ZOOMING) {
                if (((StairsData) mapData).goesUp) return StairsUpIcon.getImage();
                else return StairsDownIcon.getImage();
            } else {
                if (((StairsData) mapData).goesUp) return StairsUpIconS.getImage();
                else return StairsDownIconS.getImage();
            }
        } else if (mapData.mapchar == 'd') {
            if (!dmEditor.ZOOMING) return DoorIcon.getImage();
            else return DoorIconS.getImage();
        } else if (mapData.mapchar == 's') {
            if (!dmEditor.ZOOMING) return FloorSwitchIcon.getImage();
            else return FloorSwitchIconS.getImage();
        } else if (mapData.mapchar == '/') {
            if (!dmEditor.ZOOMING) return WallSwitchIcon.getImage();
            else return WallSwitchIconS.getImage();
        } else if (mapData.mapchar == 't') {
            if (!dmEditor.ZOOMING) return TeleportIcon.getImage();
            else return TeleportIconS.getImage();
        } else if (mapData.mapchar == ']' || mapData.mapchar == '[') {
            if (!dmEditor.ZOOMING) return AlcoveIcon.getImage();
            else return AlcoveIconS.getImage();
        } else if (mapData.mapchar == 'a') {
            if (!dmEditor.ZOOMING) return AltarIcon.getImage();
            else return AltarIconS.getImage();
        } else if (mapData.mapchar == 'f') {
            if (!dmEditor.ZOOMING) return FountainIcon.getImage();
            else return FountainIconS.getImage();
        } else if (mapData.mapchar == 'p') {
            if (!dmEditor.ZOOMING) return PitIcon.getImage();
            else return PitIconS.getImage();
        } else if (mapData.mapchar == 'l') {
            if (!dmEditor.ZOOMING) return LauncherIcon.getImage();
            else return LauncherIconS.getImage();
        } else if (mapData.mapchar == 'm') {
            if (!dmEditor.ZOOMING) return MirrorIcon.getImage();
            else return MirrorIconS.getImage();
        } else if (mapData.mapchar == 'g') {
            if (!dmEditor.ZOOMING) return GeneratorIcon.getImage();
            else return GeneratorIconS.getImage();
        } else if (mapData.mapchar == 'w') {
            if (!dmEditor.ZOOMING) return WritingIcon.getImage();
            else return WritingIconS.getImage();
        } else if (mapData.mapchar == 'S') {
            if (!dmEditor.ZOOMING) return MultFloorSwitchIcon.getImage();
            else return MultFloorSwitchIconS.getImage();
        } else if (mapData.mapchar == '\\') {
            if (!dmEditor.ZOOMING) return MultWallSwitchIcon.getImage();
            else return MultWallSwitchIconS.getImage();
        } else if (mapData.mapchar == '}') {
            if (!dmEditor.ZOOMING) return SconceIcon.getImage();
            else return SconceIconS.getImage();
        } else if (mapData.mapchar == 'D') {
            if (!dmEditor.ZOOMING) return DecorationIcon.getImage();
            else return DecorationIconS.getImage();
        } else if (mapData.mapchar == 'F') {
            if (!dmEditor.ZOOMING) return FDecorationIcon.getImage();
            else return FDecorationIconS.getImage();
        } else if (mapData.mapchar == 'P') {
            if (!dmEditor.ZOOMING) return PillarIcon.getImage();
            else return PillarIconS.getImage();
        } else if (mapData.mapchar == 'i') {
            if (!dmEditor.ZOOMING) return InvisibleWallIcon.getImage();
            else return InvisibleWallIconS.getImage();
        } else if (mapData.mapchar == 'E') {
            if (!dmEditor.ZOOMING) return EventIcon.getImage();
            else return EventIconS.getImage();
        } else if (mapData.mapchar == '!') {
            if (!dmEditor.ZOOMING) return StormIcon.getImage();
            else return StormIconS.getImage();
        } else if (mapData.mapchar == 'G') {
            if (!dmEditor.ZOOMING) return GemIcon.getImage();
            else return GemIconS.getImage();
        } else if (mapData.mapchar == 'y') {
            if (!dmEditor.ZOOMING) return FulYaIcon.getImage();
            else return FulYaIconS.getImage();
        } else if (mapData.mapchar == 'W') {
            if (!dmEditor.ZOOMING) return GameWinIcon.getImage();
            else return GameWinIconS.getImage();
        } else return null;
    }
    
    public boolean clearTargets() {
        if (targets.isEmpty()) return false;
        targets.clear();
        return true;
    }
    
    public boolean doTargets(MapData mapData, int x, int y) {
        int oldsize = targets.size();
        if (mapData.mapchar == 't') {
            targets.add(new Target(x, y, ((TeleportData) mapData).targetlevel, ((TeleportData) mapData).targetx, ((TeleportData) mapData).targety));
        } else if (mapData.mapchar == '/') {
            targets.add(new Target(x, y, ((WallSwitchData) mapData).targetlevel, ((WallSwitchData) mapData).targetx, ((WallSwitchData) mapData).targety));
        } else if (mapData.mapchar == 's') {
            targets.add(new Target(x, y, ((FloorSwitchData) mapData).targetlevel, ((FloorSwitchData) mapData).targetx, ((FloorSwitchData) mapData).targety));
        } else if (mapData.mapchar == '\\') {
            MultWallSwitchData multWallSwitchData = (MultWallSwitchData) mapData;
            int[] target;
            for (int i = 0; i < multWallSwitchData.switchlist.size(); i++) {
                target = multWallSwitchData.getTarget(i);
                targets.add(new Target(x, y, target[0], target[1], target[2]));
            }
        } else if (mapData.mapchar == 'S') {
            MultFloorSwitchData mwd = (MultFloorSwitchData) mapData;
            int[] target;
            for (int i = 0; i < mwd.switchlist.size(); i++) {
                target = mwd.getTarget(i);
                targets.add(new Target(x, y, target[0], target[1], target[2]));
            }
        } else if (mapData.mapchar == '}') {
            SconceData d = (SconceData) mapData;
            if (d.isSwitch) {
                int[] target;
                for (int i = 0; i < d.sconceswitch.switchlist.size(); i++) {
                    target = d.sconceswitch.getTarget(i);
                    targets.add(new Target(x, y, target[0], target[1], target[2]));
                }
            }
        } else if (mapData instanceof OneAlcoveData) {
            OneAlcoveData d = (OneAlcoveData) mapData;
            if (d.isSwitch) {
                int[] target;
                for (int i = 0; i < d.alcoveswitchdata.switchlist.size(); i++) {
                    target = d.alcoveswitchdata.getTarget(i);
                    targets.add(new Target(x, y, target[0], target[1], target[2]));
                }
            }
        } else if (mapData.mapchar == '[') {
            AlcoveData d = (AlcoveData) mapData;
            if (d.isSwitch) {
                int[] target;
                for (int i = 0; i < d.alcoveswitchdata.switchlist.size(); i++) {
                    target = d.alcoveswitchdata.getTarget(i);
                    targets.add(new Target(x, y, target[0], target[1], target[2]));
                }
            }
        } else if (mapData.mapchar == 'f') {
            FountainData d = (FountainData) mapData;
            if (d.fountainswitch != null) {
                int[] target;
                for (int i = 0; i < d.fountainswitch.switchlist.size(); i++) {
                    target = d.fountainswitch.getTarget(i);
                    targets.add(new Target(x, y, target[0], target[1], target[2]));
                }
            }
        } else if (mapData.mapchar == 'E') {
            EventSquareData ed = (EventSquareData) mapData;
            Action a;
            for (int i = 0; i < ed.choices.length; i++) {
                for (int j = 0; j < ed.choices[i].actions.size(); j++) {
                    a = (Action) ed.choices[i].actions.get(j);
                    if (a.actiontype > 0 && a.actiontype < 4) {
                        targets.add(new Target(x, y, ((MapPoint) a.action).level, ((MapPoint) a.action).x, ((MapPoint) a.action).y));
                    }
                }
            }
            
        } else if (mapData.mapchar == 'm') {
            if (((MirrorData) mapData).target != null)
                targets.add(new Target(x, y, ((MirrorData) mapData).target.level, ((MirrorData) mapData).target.x, ((MirrorData) mapData).target.y));
        } else if (mapData.mapchar == 'y') {
            FulYaPitData fypit = (FulYaPitData) mapData;
            targets.add(new Target(x, y, fypit.keytarget.level, fypit.keytarget.x, fypit.keytarget.y));
            targets.add(new Target(x, y, fypit.nonkeytarget.level, fypit.nonkeytarget.x, fypit.nonkeytarget.y));
        }
        
        return targets.size() != oldsize;
    }
    
    private static class Target {
        int originX, originY;
        int targetl, targetx, targety;
        
        public Target(int ox, int oy, int tl, int tx, int ty) {
            originX = ox;
            originY = oy;
            targetl = tl;
            targetx = tx;
            targety = ty;
        }
    }
}