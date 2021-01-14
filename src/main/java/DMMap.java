import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import static util.Utils.getImageIconFromFile;

class DMMap extends JPanel implements MouseListener {
    public static final String pathToImageIcons = "Icons" + File.separator;
    
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
    public static final ImageIcon FloorSwitchIconS = getImageIconFromFile(pathToImageIcons + "floorswitch-s.gif");
    public static final ImageIcon SconceIconS = getImageIconFromFile(pathToImageIcons + "sconce-s.gif");
    public static final ImageIcon DecorationIconS = getImageIconFromFile(pathToImageIcons + "decoration-s.gif");
    public static final ImageIcon FDecorationIconS = getImageIconFromFile(pathToImageIcons + "fdecoration-s.gif");
    public static final ImageIcon PillarIconS = getImageIconFromFile(pathToImageIcons + "pillar-s.gif");
    public static final ImageIcon InvisibleWallIconS = getImageIconFromFile(pathToImageIcons + "invisiblewall-s.gif");
    public static final ImageIcon EventIconS = getImageIconFromFile(pathToImageIcons + "event-s.gif");
    public static final ImageIcon StormIconS = getImageIconFromFile(pathToImageIcons + "stormbringer-s.gif");
    public static final ImageIcon GemIconS = getImageIconFromFile(pathToImageIcons + "powergem-s.gif");
    public static final ImageIcon FulYaIconS = getImageIconFromFile(pathToImageIcons + "fulya-s.gif");
    
    private dmnew dm;
    private boolean needupdate = true;
    private int levels, width, height;
    public int currentlevel;
    public char[][][] map;
    private Dimension smalldim;
    private Font smallfont;
    private Color floorcolor, greycolor;
    private Graphics2D g;
    private BufferedImage pic;
    
    public DMMap(dmnew dm, int levels, int width, int height, char[][][] map) {
        super(false);
        this.dm = dm;
        floorcolor = new Color(40, 30, 30);
        //greycolor = new Color(60,60,60);
        greycolor = getBackground();
        smalldim = new Dimension(width * 17, height * 17);
        smallfont = dm.dungfont.deriveFont(6.0f);
        //setBackground(greycolor);
        //setPreferredSize(smalldim);
        //setMaximumSize(smalldim);
        //setPreferredSize(new Dimension(dm.getSize().width-10,dm.getSize().height-10));
        //setMaximumSize(dm.getSize());
        //setMinimumSize(dm.getSize());
        pic = new BufferedImage(width * 17, height * 17, BufferedImage.TYPE_INT_BGR);
        g = pic.createGraphics();
        //updateSize();
        setMap(levels, width, height, map);
        addMouseListener(this);
    }
    
    public void updateSize() {
        int w = dm.getSize().width - 30;
        if (w < width * 17) w = width * 17;
        int h = dm.getSize().height - 30;
        if (h < height * 17) h = height * 17;
        setPreferredSize(new Dimension(w, h));
        setMaximumSize(new Dimension(w, h));
        setMinimumSize(new Dimension(w, h));
    }
    
    public void setMap(int levels, int width, int height, char[][][] map) {
        this.levels = levels;
        if (map != null) this.map = map;
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            smalldim = new Dimension(width * 17, height * 17);
            if (width > pic.getWidth(null) / 17 || height > pic.getHeight(null) / 17) {
                pic.flush();
                g.dispose();
                pic = null;
                pic = new BufferedImage(width * 17, height * 17, BufferedImage.TYPE_INT_BGR);
                g = pic.createGraphics();
                g.setFont(smallfont);
            }
            //g.setColor(floorcolor);
            //g.fillRect(0,0,width*17,height*17);
            
            //setPreferredSize(smalldim);
            //setMaximumSize(smalldim);
            //g.setClip(0,0,width*17,height*17);
        }
        currentlevel = dm.level;
        if (map == null) makeMap();
        needupdate = true;
        updateSize();
        repaint();
    }
    
    private void makeMap() {
        if (map == null || map.length != levels || map[0].length != width || map[0][0].length != height)
            map = new char[levels][width][height];
        for (int l = 0; l < levels; l++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    map[l][x][y] = '?';
                }
            }
        }
    }
    
    public void doMap() {
        int level = dm.level, partyx = dm.partyx, partyy = dm.partyy;
        char mapchar = '0';
        //current square
        if (dm.DungeonMap[level][partyx][partyy].mapchar == 'd') mapchar = 'd';
        else if (dm.DungeonMap[level][partyx][partyy].mapchar == 't' && (map[level][partyx][partyy] == 't' || (((Teleport) dm.DungeonMap[level][partyx][partyy]).isVisible && ((Teleport) dm.DungeonMap[level][partyx][partyy]).isOn)))
            mapchar = 't';
        else if (dm.DungeonMap[level][partyx][partyy].mapchar == '>') {
            if (((Stairs) dm.DungeonMap[level][partyx][partyy]).goesUp) mapchar = '<';
            else mapchar = '>';
        } else if (dm.DungeonMap[level][partyx][partyy].mapchar == '2') mapchar = '2';
        else if (dm.DungeonMap[level][partyx][partyy].mapchar == 'F') mapchar = 'F';
        else if (dm.DungeonMap[level][partyx][partyy].mapchar == 'g' && ((Generator) dm.DungeonMap[level][partyx][partyy]).monster.number == 10)
            mapchar = 'g';
        else if (dm.DungeonMap[level][partyx][partyy].mapchar == 's' && (map[level][partyx][partyy] == 's' || ((FloorSwitch) dm.DungeonMap[level][partyx][partyy]).haspic))
            mapchar = 's';
        else if (dm.DungeonMap[level][partyx][partyy].mapchar == 'S' && (map[level][partyx][partyy] == 's' || ((MultFloorSwitch2) dm.DungeonMap[level][partyx][partyy]).haspic))
            mapchar = 's';
        else if (dm.DungeonMap[level][partyx][partyy].mapchar == 'p') {
            if (map[level][partyx][partyy] == 'p' || ((Pit) dm.DungeonMap[level][partyx][partyy]).isOpen || ((Pit) dm.DungeonMap[level][partyx][partyy]).isIllusionary)
                mapchar = 'p';
        } else if (dm.DungeonMap[level][partyx][partyy].mapchar == 'E') mapchar = 'E';
        update(level, partyx, partyy, mapchar);
        
        //square 1 in front
        int xadjust = partyx, yadjust = partyy;
        if (dm.facing == dmnew.NORTH) yadjust--;
        else if (dm.facing == dmnew.WEST) xadjust--;
        else if (dm.facing == dmnew.SOUTH) yadjust++;
        else xadjust++;
        checkMap(level, xadjust, yadjust, dm.facing);
        
        //square to left
        xadjust = partyx;
        yadjust = partyy;
        if (dm.facing == dmnew.NORTH) xadjust--;
        else if (dm.facing == dmnew.WEST) yadjust++;
        else if (dm.facing == dmnew.SOUTH) xadjust++;
        else yadjust--;
        checkMap(level, xadjust, yadjust, (dm.facing + 1) % 4);
        
        //square to right
        xadjust = partyx;
        yadjust = partyy;
        if (dm.facing == dmnew.NORTH) xadjust++;
        else if (dm.facing == dmnew.WEST) yadjust--;
        else if (dm.facing == dmnew.SOUTH) xadjust--;
        else yadjust++;
        checkMap(level, xadjust, yadjust, (dm.facing + 3) % 4);
        
        if (dm.SHOWPARTYMAP) needupdate = true;
    }
    
    private void checkMap(int level, int xadjust, int yadjust, int face) {
        if (xadjust < 0 || yadjust < 0 || xadjust >= width || yadjust >= width) return;
        char mapchar = '0';
        if (dm.DungeonMap[level][xadjust][yadjust] instanceof Wall) {
            mapchar = '1';
            if (dm.DungeonMap[level][xadjust][yadjust] instanceof SidedWall) {
                if (dm.DungeonMap[level][xadjust][yadjust].mapchar == map[level][xadjust][yadjust])
                    mapchar = map[level][xadjust][yadjust];
                else if (((SidedWall) dm.DungeonMap[level][xadjust][yadjust]).side == face) {
                    if (dm.DungeonMap[level][xadjust][yadjust].mapchar == '>') {
                        if (((Stairs) dm.DungeonMap[level][xadjust][yadjust]).goesUp) mapchar = '<';
                        else mapchar = '>';
                    } else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'w') mapchar = 'w';
                    else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'f') mapchar = 'f';
                    else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == ']') mapchar = ']';
                    else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'a') mapchar = 'a';
                }
            } else if (dm.DungeonMap[level][xadjust][yadjust] instanceof SidedWall2) {
                if (dm.DungeonMap[level][xadjust][yadjust].mapchar == map[level][xadjust][yadjust])
                    mapchar = map[level][xadjust][yadjust];
                else if (((SidedWall2) dm.DungeonMap[level][xadjust][yadjust]).side == face) {
                    if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'D') mapchar = 'D';
                    else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == '/') {
                        int picnum = ((WallSwitch) dm.DungeonMap[level][xadjust][yadjust]).picnumber;
                        if (picnum != 2 && picnum != 6 && picnum != 7 && picnum != 8 && picnum != 14 && picnum != 15)
                            mapchar = '/';
                        else if (map[level][xadjust][yadjust] == '/') mapchar = '/';
                        else if (picnum != 2) mapchar = 'D';
                    } else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == '\\') {
                        int picnum = ((MultWallSwitch2) dm.DungeonMap[level][xadjust][yadjust]).picnumber;
                        if (picnum != 2 && picnum != 6 && picnum != 7 && picnum != 8 && picnum != 14 && picnum != 15)
                            mapchar = '/';
                        else if (map[level][xadjust][yadjust] == '/') mapchar = '/';
                        else if (picnum != 2) mapchar = 'D';
                    } else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == '}') mapchar = '}';
                    else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'm') mapchar = 'm';
                }
            } else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == '2' && map[level][xadjust][yadjust] == '2')
                mapchar = '2';
            else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == '[') mapchar = ']';
            else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'P') mapchar = 'P';
            else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'i') {
                if (map[level][xadjust][yadjust] == 'i') mapchar = 'i';
                else mapchar = '0';
            }
        } else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'd') mapchar = 'd';
        else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 't' && (map[level][xadjust][yadjust] == 't' || (((Teleport) dm.DungeonMap[level][xadjust][yadjust]).isVisible && ((Teleport) dm.DungeonMap[level][xadjust][yadjust]).isOn)))
            mapchar = 't';
        else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'F') mapchar = 'F';
        else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'g' && ((Generator) dm.DungeonMap[level][xadjust][yadjust]).monster.number == 10)
            mapchar = 'g';
        else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 's' && (map[level][xadjust][yadjust] == 's' || ((FloorSwitch) dm.DungeonMap[level][xadjust][yadjust]).haspic))
            mapchar = 's';
        else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'S' && (map[level][xadjust][yadjust] == 's' || ((MultFloorSwitch2) dm.DungeonMap[level][xadjust][yadjust]).haspic))
            mapchar = 's';
        else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'p') {
            if (map[level][xadjust][yadjust] == 'p' || ((Pit) dm.DungeonMap[level][xadjust][yadjust]).isOpen || ((Pit) dm.DungeonMap[level][xadjust][yadjust]).isIllusionary)
                mapchar = 'p';
        } else if (dm.DungeonMap[level][xadjust][yadjust].mapchar == 'E' && map[level][xadjust][yadjust] == 'E')
            mapchar = 'E';
        update(level, xadjust, yadjust, mapchar);
    }
    
    public void update(int level, int x, int y, char newmap) {
        if (map[level][x][y] == newmap) return;
        map[level][x][y] = newmap;
        needupdate = true;
    }
    
    public void paint(Graphics ong) {
        super.paint(ong);
        if (!needupdate) {
            //ong.drawImage(pic,0,0,this);
            ong.drawImage(pic, getSize().width / 2 - width * 17 / 2, getSize().height / 2 - height * 17 / 2, this);
            return;
        }
        currentlevel = dm.level; //will eventually have up/down arrows
        g.setColor(floorcolor);
        g.fillRect(0, 0, width * 17, height * 17);
        //g.drawImage(dm.scrollpic,0,0,width*17,height*17,this);
        Image mappic;
        int xpos = 0, ypos = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                xpos = x * 17;
                ypos = y * 17;
                mappic = null;
                if (map[currentlevel][x][y] == '?') {
                    g.setColor(greycolor);
                    g.fillRect(xpos, ypos, 17, 17);
                } else mappic = getPic(currentlevel, x, y);
                if (mappic != null) g.drawImage(mappic, xpos, ypos, this);
                                /*
                                //items
                                g.setColor(Color.blue);
                                if (dm.DungeonMap[currentlevel][x][y].hasItems && dm.DungeonMap[currentlevel][x][y].mapchar!=']' && dm.DungeonMap[currentlevel][x][y].mapchar!='[' && dm.DungeonMap[currentlevel][x][y].mapchar!='a' && dmn.DungeonMap[currentlevel][x][y].mapchar!='f') { //not alcoves or fountain
                                        if (dmed.mapdata[x][y].numitemsin[0]>0) g.drawString("*",xpos+2,ypos+6);
                                        if (dmed.mapdata[x][y].numitemsin[1]>0) g.drawString("*",xpos+12,ypos+6);
                                        if (dmed.mapdata[x][y].numitemsin[2]>0) g.drawString("*",xpos+12,ypos+16);
                                        if (dmed.mapdata[x][y].numitemsin[3]>0) g.drawString("*",xpos+2,ypos+16);
                                }
                                else { //alcoves
                                        if (dmed.mapdata[x][y].numitemsin[0]>0) g.drawString("*",xpos+7,ypos+6);
                                        if (dmed.mapdata[x][y].numitemsin[1]>0) g.drawString("*",xpos+2,ypos+11);
                                        if (dmed.mapdata[x][y].numitemsin[2]>0) g.drawString("*",xpos+7,ypos+16);
                                        if (dmed.mapdata[x][y].numitemsin[3]>0) g.drawString("*",xpos+12,ypos+11);
                                }
                                //mons
                                if (dmed.mapdata[x][y].hasMons) {
                                        g.setColor(Color.red);
                                        if (dmed.mapdata[x][y].hasmonin[0]) g.drawString("*",xpos+4,ypos+8);
                                        if (dmed.mapdata[x][y].hasmonin[1]) g.drawString("*",xpos+10,ypos+8);
                                        if (dmed.mapdata[x][y].hasmonin[2]) g.drawString("*",xpos+10,ypos+14);
                                        if (dmed.mapdata[x][y].hasmonin[3]) g.drawString("*",xpos+4,ypos+14);
                                        if (dmed.mapdata[x][y].hasmonin[4]) g.drawString("*",xpos+7,ypos+11);
                                }
                                */
                //party
                if (dm.SHOWPARTYMAP && dm.level == currentlevel && dm.partyx == x && dm.partyy == y) {
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
        needupdate = false;
        //ong.drawImage(pic,0,0,this);
        ong.drawImage(pic, getSize().width / 2 - width * 17 / 2, getSize().height / 2 - height * 17 / 2, this);
    }
    
    private Image getPic(int level, int x, int y) {
        if (map[level][x][y] == '1') return WallIconS.getImage();
        else if (map[level][x][y] == '0') return null;
        else if (map[level][x][y] == '2') return FakeWallIconS.getImage();
        else if (map[level][x][y] == '>') return StairsDownIconS.getImage();
        else if (map[level][x][y] == '<') return StairsUpIconS.getImage();
        else if (map[level][x][y] == 'd') return DoorIconS.getImage();
        else if (map[level][x][y] == 's') return FloorSwitchIconS.getImage();
        else if (map[level][x][y] == '/') return WallSwitchIconS.getImage();
        else if (map[level][x][y] == 't') return TeleportIconS.getImage();
        else if (map[level][x][y] == ']') return AlcoveIconS.getImage();
        else if (map[level][x][y] == 'a') return AltarIconS.getImage();
        else if (map[level][x][y] == 'f') return FountainIconS.getImage();
        else if (map[level][x][y] == 'p') return PitIconS.getImage();
        else if (map[level][x][y] == 'l') return LauncherIconS.getImage();
        else if (map[level][x][y] == 'm') return MirrorIconS.getImage();
        else if (map[level][x][y] == 'g') return GeneratorIconS.getImage();
        else if (map[level][x][y] == 'w') return WritingIconS.getImage();
        else if (map[level][x][y] == '}') return SconceIconS.getImage();
        else if (map[level][x][y] == 'D') return DecorationIconS.getImage();
        else if (map[level][x][y] == 'F') return FDecorationIconS.getImage();
        else if (map[level][x][y] == 'P') return PillarIconS.getImage();
        else if (map[level][x][y] == 'i') return InvisibleWallIconS.getImage();
        else if (map[level][x][y] == 'E') return EventIconS.getImage();
        else if (map[level][x][y] == '!') return StormIconS.getImage();
        else if (map[level][x][y] == 'G') return GemIconS.getImage();
        else if (map[level][x][y] == 'y') return FulYaIconS.getImage();
        else return null;
    }
    
    public void mousePressed(MouseEvent e) {
        dm.mappane.setVisible(false);
        dm.toppanel.setVisible(true);
        dm.centerpanel.setVisible(true);
                /*
                dm.ecpanel.setVisible(true);
                if (dm.spellsheet!=null) {
                        dm.spellsheet.setVisible(true);
                        dm.weaponsheet.setVisible(true);
                }
                dm.arrowsheet.setVisible(true);
                dm.message.setVisible(true);
                dm.hpanel.setVisible(true);
                dm.formation.setVisible(true);
                dm.toppanel.setVisible(true);
                dm.maincenterpan.setVisible(true);
                dm.eastpanel.setVisible(true);
                */
        if (dm.sheet && SwingUtilities.isRightMouseButton(e)) {
            dm.showCenter(dm.dview);
            dm.sheet = false;
        }
        dm.validate();
                /*
                if (dm.sheet && SwingUtilities.isRightMouseButton(e)) {
                        dm.showCenter(dm.dview);
                        dm.sheet = false;
                }
                dm.setContentPane(dm.imagePane);
                dm.validate();
                */
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}