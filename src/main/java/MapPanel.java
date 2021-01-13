import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

class MapPanel extends JPanel {
    
    public static final ImageIcon WallIcon = new ImageIcon("Icons" + File.separator + "wall.gif");
    public static final ImageIcon DoorIcon = new ImageIcon("Icons" + File.separator + "door.gif");
    public static final ImageIcon FakeWallIcon = new ImageIcon("Icons" + File.separator + "fakewall.gif");
    public static final ImageIcon AltarIcon = new ImageIcon("Icons" + File.separator + "altar.gif");
    public static final ImageIcon AlcoveIcon = new ImageIcon("Icons" + File.separator + "alcove.gif");
    public static final ImageIcon FountainIcon = new ImageIcon("Icons" + File.separator + "fountain.gif");
    public static final ImageIcon StairsDownIcon = new ImageIcon("Icons" + File.separator + "stairsdown.gif");
    public static final ImageIcon StairsUpIcon = new ImageIcon("Icons" + File.separator + "stairsup.gif");
    public static final ImageIcon TeleportIcon = new ImageIcon("Icons" + File.separator + "teleport.gif");
    public static final ImageIcon PitIcon = new ImageIcon("Icons" + File.separator + "pit.gif");
    public static final ImageIcon MirrorIcon = new ImageIcon("Icons" + File.separator + "mirror.gif");
    public static final ImageIcon WritingIcon = new ImageIcon("Icons" + File.separator + "writing.gif");
    public static final ImageIcon LauncherIcon = new ImageIcon("Icons" + File.separator + "launcher.gif");
    public static final ImageIcon GeneratorIcon = new ImageIcon("Icons" + File.separator + "generator.gif");
    public static final ImageIcon WallSwitchIcon = new ImageIcon("Icons" + File.separator + "wallswitch.gif");
    public static final ImageIcon MultWallSwitchIcon = new ImageIcon("Icons" + File.separator + "multwallswitch.gif");
    public static final ImageIcon FloorSwitchIcon = new ImageIcon("Icons" + File.separator + "floorswitch.gif");
    public static final ImageIcon MultFloorSwitchIcon = new ImageIcon("Icons" + File.separator + "multfloorswitch.gif");
    public static final ImageIcon SconceIcon = new ImageIcon("Icons" + File.separator + "sconce.gif");
    public static final ImageIcon DecorationIcon = new ImageIcon("Icons" + File.separator + "decoration.gif");
    public static final ImageIcon FDecorationIcon = new ImageIcon("Icons" + File.separator + "fdecoration.gif");
    public static final ImageIcon PillarIcon = new ImageIcon("Icons" + File.separator + "pillar.gif");
    public static final ImageIcon InvisibleWallIcon = new ImageIcon("Icons" + File.separator + "invisiblewall.gif");
    public static final ImageIcon EventIcon = new ImageIcon("Icons" + File.separator + "event.gif");
    public static final ImageIcon GameWinIcon = new ImageIcon("Icons" + File.separator + "gamewin.gif");
    public static final ImageIcon StormIcon = new ImageIcon("Icons" + File.separator + "stormbringer.gif");
    public static final ImageIcon GemIcon = new ImageIcon("Icons" + File.separator + "powergem.gif");
    public static final ImageIcon FulYaIcon = new ImageIcon("Icons" + File.separator + "fulya.gif");
    
    public static final ImageIcon WallIconS = new ImageIcon("Icons" + File.separator + "wall-s.gif");
    public static final ImageIcon DoorIconS = new ImageIcon("Icons" + File.separator + "door-s.gif");
    public static final ImageIcon FakeWallIconS = new ImageIcon("Icons" + File.separator + "fakewall-s.gif");
    public static final ImageIcon AltarIconS = new ImageIcon("Icons" + File.separator + "altar-s.gif");
    public static final ImageIcon AlcoveIconS = new ImageIcon("Icons" + File.separator + "alcove-s.gif");
    public static final ImageIcon FountainIconS = new ImageIcon("Icons" + File.separator + "fountain-s.gif");
    public static final ImageIcon StairsDownIconS = new ImageIcon("Icons" + File.separator + "stairsdown-s.gif");
    public static final ImageIcon StairsUpIconS = new ImageIcon("Icons" + File.separator + "stairsup-s.gif");
    public static final ImageIcon TeleportIconS = new ImageIcon("Icons" + File.separator + "teleport-s.gif");
    public static final ImageIcon PitIconS = new ImageIcon("Icons" + File.separator + "pit-s.gif");
    public static final ImageIcon MirrorIconS = new ImageIcon("Icons" + File.separator + "mirror-s.gif");
    public static final ImageIcon WritingIconS = new ImageIcon("Icons" + File.separator + "writing-s.gif");
    public static final ImageIcon LauncherIconS = new ImageIcon("Icons" + File.separator + "launcher-s.gif");
    public static final ImageIcon GeneratorIconS = new ImageIcon("Icons" + File.separator + "generator-s.gif");
    public static final ImageIcon WallSwitchIconS = new ImageIcon("Icons" + File.separator + "wallswitch-s.gif");
    public static final ImageIcon MultWallSwitchIconS = new ImageIcon("Icons" + File.separator + "multwallswitch-s.gif");
    public static final ImageIcon FloorSwitchIconS = new ImageIcon("Icons" + File.separator + "floorswitch-s.gif");
    public static final ImageIcon MultFloorSwitchIconS = new ImageIcon("Icons" + File.separator + "multfloorswitch-s.gif");
    public static final ImageIcon SconceIconS = new ImageIcon("Icons" + File.separator + "sconce-s.gif");
    public static final ImageIcon DecorationIconS = new ImageIcon("Icons" + File.separator + "decoration-s.gif");
    public static final ImageIcon FDecorationIconS = new ImageIcon("Icons" + File.separator + "fdecoration-s.gif");
    public static final ImageIcon PillarIconS = new ImageIcon("Icons" + File.separator + "pillar-s.gif");
    public static final ImageIcon InvisibleWallIconS = new ImageIcon("Icons" + File.separator + "invisiblewall-s.gif");
    public static final ImageIcon EventIconS = new ImageIcon("Icons" + File.separator + "event-s.gif");
    public static final ImageIcon GameWinIconS = new ImageIcon("Icons" + File.separator + "gamewin-s.gif");
    public static final ImageIcon StormIconS = new ImageIcon("Icons" + File.separator + "stormbringer-s.gif");
    public static final ImageIcon GemIconS = new ImageIcon("Icons" + File.separator + "powergem-s.gif");
    public static final ImageIcon FulYaIconS = new ImageIcon("Icons" + File.separator + "fulya-s.gif");
    
    private DMEditor dmed;
    private ArrayList targets;
    private boolean needupdate = true;
    private Dimension bigdim, smalldim;
    private Font bigfont, smallfont;
    private Stroke normalstroke, arrowstroke;
    private Color floorcolor;
    private Graphics2D g;//,offg;
    private BufferedImage pic;
    //private Image offimage;
    
    public MapPanel(DMEditor dmed) {
        super(false);
        this.dmed = dmed;
        targets = new ArrayList(1);
        bigdim = new Dimension(dmed.MAPWIDTH * 33, dmed.MAPHEIGHT * 33);
        smalldim = new Dimension(dmed.MAPWIDTH * 17, dmed.MAPHEIGHT * 17);
        arrowstroke = new BasicStroke(2.0f);
        floorcolor = new Color(40, 30, 30);
        setBackground(floorcolor);
        setPreferredSize(bigdim);
        setMaximumSize(bigdim);
        bigfont = dmed.dungfont.deriveFont(8.0f);
        smallfont = dmed.dungfont.deriveFont(6.0f);
        pic = new BufferedImage(50 * 33, 50 * 33, BufferedImage.TYPE_INT_BGR);
        g = pic.createGraphics();
        normalstroke = g.getStroke();
    }
    
    public void setNewSize() {
        if (dmed.MAPWIDTH != (int) bigdim.getWidth() / 33 || dmed.MAPHEIGHT != (int) bigdim.getHeight() / 33) {
            bigdim = new Dimension(dmed.MAPWIDTH * 33, dmed.MAPHEIGHT * 33);
            smalldim = new Dimension(dmed.MAPWIDTH * 17, dmed.MAPHEIGHT * 17);
            if (dmed.MAPWIDTH > pic.getWidth(null) / 33 || dmed.MAPHEIGHT > pic.getHeight(null) / 33) {
                pic.flush();
                g.dispose();
                pic = null;
                System.gc();
                pic = new BufferedImage(dmed.MAPWIDTH * 33, dmed.MAPHEIGHT * 33, BufferedImage.TYPE_INT_BGR);
                g = pic.createGraphics();
                normalstroke = g.getStroke();
            }
            setZoom();
            g.setClip(0, 0, dmed.MAPWIDTH * 33, dmed.MAPHEIGHT * 33);
        }
    }
    
    public void setZoom() {
        if (!dmed.ZOOMING) {
            setPreferredSize(bigdim);
            setMaximumSize(bigdim);
            g.setFont(bigfont);
        } else {
            g.setColor(floorcolor);
            g.fillRect(0, 0, dmed.MAPWIDTH * 33, dmed.MAPHEIGHT * 33);
            setPreferredSize(smalldim);
            setMaximumSize(smalldim);
            g.setFont(smallfont);
        }
    }
    
    public void forcePaint() {
        super.repaint();
    }
    
    public void repaint() {
        needupdate = true;
        super.repaint();
    }
    
    public void paintSquare(int x, int y, boolean drawpic) {
        if (!dmed.ZOOMING) {
            g.setColor(floorcolor);
            g.fillRect(x * 33, y * 33, 32, 32);
            Image mappic = getPic(dmed.mapdata[x][y]);
            int xpos = x * 33, ypos = y * 33;
            if (mappic != null) g.drawImage(mappic, xpos, ypos, this);
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
            if (dmed.mapdata[x][y].nomons) {
                g.setColor(Color.blue);
                g.drawRect(xpos, ypos, 31, 31);
            }
            //noghosts
            else if (dmed.mapdata[x][y].noghosts) {
                g.setColor(Color.green);
                g.drawRect(xpos, ypos, 31, 31);
            }
            //items
            g.setColor(Color.blue);
            if (dmed.mapdata[x][y].hasItems && dmed.mapdata[x][y].mapchar != ']' && dmed.mapdata[x][y].mapchar != '[' && dmed.mapdata[x][y].mapchar != 'a' && dmed.mapdata[x][y].mapchar != 'f') { //not alcoves or fountain
                if (dmed.mapdata[x][y].numitemsin[0] > 0) g.drawString("*", xpos + 4, ypos + 11);
                if (dmed.mapdata[x][y].numitemsin[1] > 0) g.drawString("*", xpos + 25, ypos + 11);
                if (dmed.mapdata[x][y].numitemsin[2] > 0) g.drawString("*", xpos + 25, ypos + 29);
                if (dmed.mapdata[x][y].numitemsin[3] > 0) g.drawString("*", xpos + 4, ypos + 29);
            } else { //alcoves
                if (dmed.mapdata[x][y].numitemsin[0] > 0) g.drawString("*", xpos + 14, ypos + 12);
                if (dmed.mapdata[x][y].numitemsin[1] > 0) g.drawString("*", xpos + 4, ypos + 20);
                if (dmed.mapdata[x][y].numitemsin[2] > 0) g.drawString("*", xpos + 14, ypos + 29);
                if (dmed.mapdata[x][y].numitemsin[3] > 0) g.drawString("*", xpos + 24, ypos + 20);
            }
            //mons
            if (dmed.mapdata[x][y].hasMons) {
                g.setColor(Color.red);
                if (dmed.mapdata[x][y].hasmonin[0]) g.drawString("*", xpos + 7, ypos + 14);
                if (dmed.mapdata[x][y].hasmonin[1]) g.drawString("*", xpos + 22, ypos + 14);
                if (dmed.mapdata[x][y].hasmonin[2]) g.drawString("*", xpos + 22, ypos + 26);
                if (dmed.mapdata[x][y].hasmonin[3]) g.drawString("*", xpos + 7, ypos + 26);
                if (dmed.mapdata[x][y].hasmonin[4]) g.drawString("*", xpos + 14, ypos + 20);
            }
            //party
            if (dmed.mapdata[x][y].hasParty) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.green);
                g.drawArc(xpos + 2, ypos + 2, 28, 28, 45, 90);
                g.setColor(Color.yellow);
                g.drawArc(xpos + 2, ypos + 2, 28, 28, 135, 90);
                g.setColor(Color.red);
                g.drawArc(xpos + 2, ypos + 2, 28, 28, 225, 90);
                g.setColor(Color.blue);
                g.drawArc(xpos + 2, ypos + 2, 28, 28, 315, 90);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
            if (drawpic) {
                //super.repaint();
                paint(getGraphics());
            }
        } else {
            g.setColor(floorcolor);
            g.fillRect(x * 17, y * 17, 16, 16);
            Image mappic = getPic(dmed.mapdata[x][y]);
            int xpos = x * 17, ypos = y * 17;
            if (mappic != null) g.drawImage(mappic, xpos, ypos, this);
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
            if (dmed.mapdata[x][y].nomons) {
                g.setColor(Color.blue);
                g.drawRect(xpos, ypos, 15, 15);
            }
            //noghosts
            else if (dmed.mapdata[x][y].noghosts) {
                g.setColor(Color.green);
                g.drawRect(xpos, ypos, 15, 15);
            }
            //items
            g.setColor(Color.blue);
            if (dmed.mapdata[x][y].hasItems && dmed.mapdata[x][y].mapchar != ']' && dmed.mapdata[x][y].mapchar != '[' && dmed.mapdata[x][y].mapchar != 'a' && dmed.mapdata[x][y].mapchar != 'f') { //not alcoves or fountain
                if (dmed.mapdata[x][y].numitemsin[0] > 0) g.drawString("*", xpos + 2, ypos + 6);
                if (dmed.mapdata[x][y].numitemsin[1] > 0) g.drawString("*", xpos + 12, ypos + 6);
                if (dmed.mapdata[x][y].numitemsin[2] > 0) g.drawString("*", xpos + 12, ypos + 16);
                if (dmed.mapdata[x][y].numitemsin[3] > 0) g.drawString("*", xpos + 2, ypos + 16);
            } else { //alcoves
                if (dmed.mapdata[x][y].numitemsin[0] > 0) g.drawString("*", xpos + 7, ypos + 6);
                if (dmed.mapdata[x][y].numitemsin[1] > 0) g.drawString("*", xpos + 2, ypos + 11);
                if (dmed.mapdata[x][y].numitemsin[2] > 0) g.drawString("*", xpos + 7, ypos + 16);
                if (dmed.mapdata[x][y].numitemsin[3] > 0) g.drawString("*", xpos + 12, ypos + 11);
            }
            //mons
            if (dmed.mapdata[x][y].hasMons) {
                g.setColor(Color.red);
                if (dmed.mapdata[x][y].hasmonin[0]) g.drawString("*", xpos + 4, ypos + 8);
                if (dmed.mapdata[x][y].hasmonin[1]) g.drawString("*", xpos + 10, ypos + 8);
                if (dmed.mapdata[x][y].hasmonin[2]) g.drawString("*", xpos + 10, ypos + 14);
                if (dmed.mapdata[x][y].hasmonin[3]) g.drawString("*", xpos + 4, ypos + 14);
                if (dmed.mapdata[x][y].hasmonin[4]) g.drawString("*", xpos + 7, ypos + 11);
            }
            //party
            if (dmed.mapdata[x][y].hasParty) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.green);
                g.drawArc(xpos + 1, ypos + 1, 14, 14, 45, 90);
                g.setColor(Color.yellow);
                g.drawArc(xpos + 1, ypos + 1, 14, 14, 135, 90);
                g.setColor(Color.red);
                g.drawArc(xpos + 1, ypos + 1, 14, 14, 225, 90);
                g.setColor(Color.blue);
                g.drawArc(xpos + 1, ypos + 1, 14, 14, 315, 90);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
            if (drawpic) {
                //super.repaint();
                paint(getGraphics());
            }
        }
    }
    
    public void paint(Graphics ong) {
        if (!needupdate) {
            ong.drawImage(pic, 0, 0, this);
            if (!targets.isEmpty()) drawTargets(ong);
            if (dmed.SQUARELOCKED) {
                int size = 32;
                if (dmed.ZOOMING) size = 16;
                ong.setColor(Color.white);
                ((Graphics2D) ong).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ong.drawOval((int) (dmed.lockx * (size + 1) - 3 * size / 8), (int) (dmed.locky * (size + 1) - 3 * size / 8), (int) (size * 1.75), (int) (size * 1.75));
                ((Graphics2D) ong).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
            return;
        } else if (!dmed.ZOOMING) {
            g.setColor(floorcolor);
            g.fillRect(0, 0, dmed.MAPWIDTH * 33, dmed.MAPHEIGHT * 33);
            g.setFont(bigfont);
            Image mappic;
            int xpos = 0, ypos = 0;
            for (int y = 0; y < dmed.MAPHEIGHT; y++) {
                for (int x = 0; x < dmed.MAPWIDTH; x++) {
                    mappic = getPic(dmed.mapdata[x][y]);
                    xpos = x * 33;
                    ypos = y * 33;
                    if (mappic != null) g.drawImage(mappic, xpos, ypos, this);
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
                    if (dmed.mapdata[x][y].nomons) {
                        g.setColor(Color.blue);
                        g.drawRect(xpos, ypos, 31, 31);
                    }
                    //noghosts
                    else if (dmed.mapdata[x][y].noghosts) {
                        g.setColor(Color.green);
                        g.drawRect(xpos, ypos, 31, 31);
                    }
                    //items
                    g.setColor(Color.blue);
                    if (dmed.mapdata[x][y].hasItems && dmed.mapdata[x][y].mapchar != ']' && dmed.mapdata[x][y].mapchar != '[' && dmed.mapdata[x][y].mapchar != 'a' && dmed.mapdata[x][y].mapchar != 'f') { //not alcoves or fountain
                        if (dmed.mapdata[x][y].numitemsin[0] > 0) g.drawString("*", xpos + 4, ypos + 11);
                        if (dmed.mapdata[x][y].numitemsin[1] > 0) g.drawString("*", xpos + 25, ypos + 11);
                        if (dmed.mapdata[x][y].numitemsin[2] > 0) g.drawString("*", xpos + 25, ypos + 29);
                        if (dmed.mapdata[x][y].numitemsin[3] > 0) g.drawString("*", xpos + 4, ypos + 29);
                    } else { //alcoves
                        if (dmed.mapdata[x][y].numitemsin[0] > 0) g.drawString("*", xpos + 14, ypos + 12);
                        if (dmed.mapdata[x][y].numitemsin[1] > 0) g.drawString("*", xpos + 4, ypos + 20);
                        if (dmed.mapdata[x][y].numitemsin[2] > 0) g.drawString("*", xpos + 14, ypos + 29);
                        if (dmed.mapdata[x][y].numitemsin[3] > 0) g.drawString("*", xpos + 24, ypos + 20);
                    }
                    //mons
                    if (dmed.mapdata[x][y].hasMons) {
                        g.setColor(Color.red);
                        if (dmed.mapdata[x][y].hasmonin[0]) g.drawString("*", xpos + 7, ypos + 14);
                        if (dmed.mapdata[x][y].hasmonin[1]) g.drawString("*", xpos + 22, ypos + 14);
                        if (dmed.mapdata[x][y].hasmonin[2]) g.drawString("*", xpos + 22, ypos + 26);
                        if (dmed.mapdata[x][y].hasmonin[3]) g.drawString("*", xpos + 7, ypos + 26);
                        if (dmed.mapdata[x][y].hasmonin[4]) g.drawString("*", xpos + 14, ypos + 20);
                    }
                    //party
                    if (dmed.mapdata[x][y].hasParty) {
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(Color.green);
                        g.drawArc(xpos + 2, ypos + 2, 28, 28, 45, 90);
                        g.setColor(Color.yellow);
                        g.drawArc(xpos + 2, ypos + 2, 28, 28, 135, 90);
                        g.setColor(Color.red);
                        g.drawArc(xpos + 2, ypos + 2, 28, 28, 225, 90);
                        g.setColor(Color.blue);
                        g.drawArc(xpos + 2, ypos + 2, 28, 28, 315, 90);
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                }
            }
        } else { //ZOOMING
            g.setColor(floorcolor);
            g.fillRect(0, 0, dmed.MAPWIDTH * 17, dmed.MAPHEIGHT * 17);
            Image mappic;
            int xpos = 0, ypos = 0;
            for (int y = 0; y < dmed.MAPHEIGHT; y++) {
                for (int x = 0; x < dmed.MAPWIDTH; x++) {
                    mappic = getPic(dmed.mapdata[x][y]);
                    xpos = x * 17;
                    ypos = y * 17;
                    if (mappic != null) g.drawImage(mappic, xpos, ypos, this);
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
                    if (dmed.mapdata[x][y].nomons) {
                        g.setColor(Color.blue);
                        g.drawRect(xpos, ypos, 15, 15);
                    }
                    //noghosts
                    else if (dmed.mapdata[x][y].noghosts) {
                        g.setColor(Color.green);
                        g.drawRect(xpos, ypos, 15, 15);
                    }
                    //items
                    g.setColor(Color.blue);
                    if (dmed.mapdata[x][y].hasItems && dmed.mapdata[x][y].mapchar != ']' && dmed.mapdata[x][y].mapchar != '[' && dmed.mapdata[x][y].mapchar != 'a' && dmed.mapdata[x][y].mapchar != 'f') { //not alcoves or fountain
                        if (dmed.mapdata[x][y].numitemsin[0] > 0) g.drawString("*", xpos + 2, ypos + 6);
                        if (dmed.mapdata[x][y].numitemsin[1] > 0) g.drawString("*", xpos + 12, ypos + 6);
                        if (dmed.mapdata[x][y].numitemsin[2] > 0) g.drawString("*", xpos + 12, ypos + 16);
                        if (dmed.mapdata[x][y].numitemsin[3] > 0) g.drawString("*", xpos + 2, ypos + 16);
                    } else { //alcoves
                        if (dmed.mapdata[x][y].numitemsin[0] > 0) g.drawString("*", xpos + 7, ypos + 6);
                        if (dmed.mapdata[x][y].numitemsin[1] > 0) g.drawString("*", xpos + 2, ypos + 11);
                        if (dmed.mapdata[x][y].numitemsin[2] > 0) g.drawString("*", xpos + 7, ypos + 16);
                        if (dmed.mapdata[x][y].numitemsin[3] > 0) g.drawString("*", xpos + 12, ypos + 11);
                    }
                    //mons
                    if (dmed.mapdata[x][y].hasMons) {
                        g.setColor(Color.red);
                        if (dmed.mapdata[x][y].hasmonin[0]) g.drawString("*", xpos + 4, ypos + 8);
                        if (dmed.mapdata[x][y].hasmonin[1]) g.drawString("*", xpos + 10, ypos + 8);
                        if (dmed.mapdata[x][y].hasmonin[2]) g.drawString("*", xpos + 10, ypos + 14);
                        if (dmed.mapdata[x][y].hasmonin[3]) g.drawString("*", xpos + 4, ypos + 14);
                        if (dmed.mapdata[x][y].hasmonin[4]) g.drawString("*", xpos + 7, ypos + 11);
                    }
                    //party
                    if (dmed.mapdata[x][y].hasParty) {
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(Color.green);
                        g.drawArc(xpos + 1, ypos + 1, 14, 14, 45, 90);
                        g.setColor(Color.yellow);
                        g.drawArc(xpos + 1, ypos + 1, 14, 14, 135, 90);
                        g.setColor(Color.red);
                        g.drawArc(xpos + 1, ypos + 1, 14, 14, 225, 90);
                        g.setColor(Color.blue);
                        g.drawArc(xpos + 1, ypos + 1, 14, 14, 315, 90);
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                }
            }
        }
        needupdate = false;
        ong.drawImage(pic, 0, 0, this);
        if (!targets.isEmpty()) drawTargets(ong);
        if (dmed.SQUARELOCKED) {
            int size = 32;
            if (dmed.ZOOMING) size = 16;
            ong.setColor(Color.white);
            ((Graphics2D) ong).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ong.drawOval((int) (dmed.lockx * (size + 1) - 3 * size / 8), (int) (dmed.locky * (size + 1) - 3 * size / 8), (int) (size * 1.75), (int) (size * 1.75));
            ((Graphics2D) ong).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }
    
    private void drawTargets(Graphics g) {
        //iterate thru targets arraylist, drawing red borders (and maybe actual arrows and up/down level indicators)
        int size = 32;
        if (dmed.ZOOMING) size = 16;
        Graphics2D gg = (Graphics2D) g;
        gg.setColor(Color.red);
        Target target;
        int numtargets = targets.size();
        for (int i = 0; i < numtargets; i++) {
            target = (Target) targets.get(i);
            if (target.targetl == dmed.currentlevel) {
                gg.drawRect(target.targetx * (size + 1), target.targety * (size + 1), size - 1, size - 1);
            }
        }
        if (!dmed.ZOOMING) gg.setStroke(arrowstroke);
        for (int i = 0; i < numtargets; i++) {
            target = (Target) targets.get(i);
            if (target.targetl == dmed.currentlevel) {
                gg.drawLine(target.originx * (size + 1) + size / 2, target.originy * (size + 1) + size / 2, target.targetx * (size + 1) + size / 2, target.targety * (size + 1) + size / 2);
            } else {
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gg.drawOval((int) (target.originx * (size + 1) - size / 4), (int) (target.originy * (size + 1) - size / 4), (int) (size * 1.5), (int) (size * 1.5));
                gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
        }
        if (!dmed.ZOOMING) gg.setStroke(normalstroke);
    }
    
    private Image getPic(MapData m) {
        if (m.mapchar == '1') {
            if (!dmed.ZOOMING) return WallIcon.getImage();
            else return WallIconS.getImage();
        } else if (m.mapchar == '0') return null;
        else if (m.mapchar == '2') {
            if (!dmed.ZOOMING) return FakeWallIcon.getImage();
            else return FakeWallIconS.getImage();
        } else if (m.mapchar == '>') {
            if (!dmed.ZOOMING) {
                if (((StairsData) m).goesUp) return StairsUpIcon.getImage();
                else return StairsDownIcon.getImage();
            } else {
                if (((StairsData) m).goesUp) return StairsUpIconS.getImage();
                else return StairsDownIconS.getImage();
            }
        } else if (m.mapchar == 'd') {
            if (!dmed.ZOOMING) return DoorIcon.getImage();
            else return DoorIconS.getImage();
        } else if (m.mapchar == 's') {
            if (!dmed.ZOOMING) return FloorSwitchIcon.getImage();
            else return FloorSwitchIconS.getImage();
        } else if (m.mapchar == '/') {
            if (!dmed.ZOOMING) return WallSwitchIcon.getImage();
            else return WallSwitchIconS.getImage();
        } else if (m.mapchar == 't') {
            if (!dmed.ZOOMING) return TeleportIcon.getImage();
            else return TeleportIconS.getImage();
        } else if (m.mapchar == ']' || m.mapchar == '[') {
            if (!dmed.ZOOMING) return AlcoveIcon.getImage();
            else return AlcoveIconS.getImage();
        } else if (m.mapchar == 'a') {
            if (!dmed.ZOOMING) return AltarIcon.getImage();
            else return AltarIconS.getImage();
        } else if (m.mapchar == 'f') {
            if (!dmed.ZOOMING) return FountainIcon.getImage();
            else return FountainIconS.getImage();
        } else if (m.mapchar == 'p') {
            if (!dmed.ZOOMING) return PitIcon.getImage();
            else return PitIconS.getImage();
        } else if (m.mapchar == 'l') {
            if (!dmed.ZOOMING) return LauncherIcon.getImage();
            else return LauncherIconS.getImage();
        } else if (m.mapchar == 'm') {
            if (!dmed.ZOOMING) return MirrorIcon.getImage();
            else return MirrorIconS.getImage();
        } else if (m.mapchar == 'g') {
            if (!dmed.ZOOMING) return GeneratorIcon.getImage();
            else return GeneratorIconS.getImage();
        } else if (m.mapchar == 'w') {
            if (!dmed.ZOOMING) return WritingIcon.getImage();
            else return WritingIconS.getImage();
        } else if (m.mapchar == 'S') {
            if (!dmed.ZOOMING) return MultFloorSwitchIcon.getImage();
            else return MultFloorSwitchIconS.getImage();
        } else if (m.mapchar == '\\') {
            if (!dmed.ZOOMING) return MultWallSwitchIcon.getImage();
            else return MultWallSwitchIconS.getImage();
        } else if (m.mapchar == '}') {
            if (!dmed.ZOOMING) return SconceIcon.getImage();
            else return SconceIconS.getImage();
        } else if (m.mapchar == 'D') {
            if (!dmed.ZOOMING) return DecorationIcon.getImage();
            else return DecorationIconS.getImage();
        } else if (m.mapchar == 'F') {
            if (!dmed.ZOOMING) return FDecorationIcon.getImage();
            else return FDecorationIconS.getImage();
        } else if (m.mapchar == 'P') {
            if (!dmed.ZOOMING) return PillarIcon.getImage();
            else return PillarIconS.getImage();
        } else if (m.mapchar == 'i') {
            if (!dmed.ZOOMING) return InvisibleWallIcon.getImage();
            else return InvisibleWallIconS.getImage();
        } else if (m.mapchar == 'E') {
            if (!dmed.ZOOMING) return EventIcon.getImage();
            else return EventIconS.getImage();
        } else if (m.mapchar == '!') {
            if (!dmed.ZOOMING) return StormIcon.getImage();
            else return StormIconS.getImage();
        } else if (m.mapchar == 'G') {
            if (!dmed.ZOOMING) return GemIcon.getImage();
            else return GemIconS.getImage();
        } else if (m.mapchar == 'y') {
            if (!dmed.ZOOMING) return FulYaIcon.getImage();
            else return FulYaIconS.getImage();
        } else if (m.mapchar == 'W') {
            if (!dmed.ZOOMING) return GameWinIcon.getImage();
            else return GameWinIconS.getImage();
        } else return null;
    }
    
    public boolean clearTargets() {
        if (targets.isEmpty()) return false;
        targets.clear();
        return true;
    }
    
    public boolean doTargets(MapData md, int x, int y) {
        int oldsize = targets.size();
        if (md.mapchar == 't') {
            targets.add(new Target(x, y, ((TeleportData) md).targetlevel, ((TeleportData) md).targetx, ((TeleportData) md).targety));
        } else if (md.mapchar == '/') {
            targets.add(new Target(x, y, ((WallSwitchData) md).targetlevel, ((WallSwitchData) md).targetx, ((WallSwitchData) md).targety));
        } else if (md.mapchar == 's') {
            targets.add(new Target(x, y, ((FloorSwitchData) md).targetlevel, ((FloorSwitchData) md).targetx, ((FloorSwitchData) md).targety));
        } else if (md.mapchar == '\\') {
            MultWallSwitchData mwd = (MultWallSwitchData) md;
            int[] target;
            for (int i = 0; i < mwd.switchlist.size(); i++) {
                target = mwd.getTarget(i);
                targets.add(new Target(x, y, target[0], target[1], target[2]));
            }
        } else if (md.mapchar == 'S') {
            MultFloorSwitchData mwd = (MultFloorSwitchData) md;
            int[] target;
            for (int i = 0; i < mwd.switchlist.size(); i++) {
                target = mwd.getTarget(i);
                targets.add(new Target(x, y, target[0], target[1], target[2]));
            }
        } else if (md.mapchar == '}') {
            SconceData d = (SconceData) md;
            if (d.isSwitch) {
                int[] target;
                for (int i = 0; i < d.sconceswitch.switchlist.size(); i++) {
                    target = d.sconceswitch.getTarget(i);
                    targets.add(new Target(x, y, target[0], target[1], target[2]));
                }
            }
        } else if (md instanceof OneAlcoveData) {
            OneAlcoveData d = (OneAlcoveData) md;
            if (d.isSwitch) {
                int[] target;
                for (int i = 0; i < d.alcoveswitchdata.switchlist.size(); i++) {
                    target = d.alcoveswitchdata.getTarget(i);
                    targets.add(new Target(x, y, target[0], target[1], target[2]));
                }
            }
        } else if (md.mapchar == '[') {
            AlcoveData d = (AlcoveData) md;
            if (d.isSwitch) {
                int[] target;
                for (int i = 0; i < d.alcoveswitchdata.switchlist.size(); i++) {
                    target = d.alcoveswitchdata.getTarget(i);
                    targets.add(new Target(x, y, target[0], target[1], target[2]));
                }
            }
        } else if (md.mapchar == 'f') {
            FountainData d = (FountainData) md;
            if (d.fountainswitch != null) {
                int[] target;
                for (int i = 0; i < d.fountainswitch.switchlist.size(); i++) {
                    target = d.fountainswitch.getTarget(i);
                    targets.add(new Target(x, y, target[0], target[1], target[2]));
                }
            }
        } else if (md.mapchar == 'E') {
            EventSquareData ed = (EventSquareData) md;
            Action a;
            for (int i = 0; i < ed.choices.length; i++) {
                for (int j = 0; j < ed.choices[i].actions.size(); j++) {
                    a = (Action) ed.choices[i].actions.get(j);
                    if (a.actiontype > 0 && a.actiontype < 4) {
                        targets.add(new Target(x, y, ((MapPoint) a.action).level, ((MapPoint) a.action).x, ((MapPoint) a.action).y));
                    }
                }
            }
            
        } else if (md.mapchar == 'm') {
            if (((MirrorData) md).target != null)
                targets.add(new Target(x, y, ((MirrorData) md).target.level, ((MirrorData) md).target.x, ((MirrorData) md).target.y));
        } else if (md.mapchar == 'y') {
            FulYaPitData fypit = (FulYaPitData) md;
            targets.add(new Target(x, y, fypit.keytarget.level, fypit.keytarget.x, fypit.keytarget.y));
            targets.add(new Target(x, y, fypit.nonkeytarget.level, fypit.nonkeytarget.x, fypit.nonkeytarget.y));
        }
        if (targets.size() != oldsize) return true;
        else return false;
    }
    
    private class Target {
        int originx, originy;
        int targetl, targetx, targety;
        
        public Target(int ox, int oy, int tl, int tx, int ty) {
            originx = ox;
            originy = oy;
            targetl = tl;
            targetx = tx;
            targety = ty;
        }
    }
}