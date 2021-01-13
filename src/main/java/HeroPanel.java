import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Vector;

class HeroPanel extends JDialog implements MouseListener {
    private JPanel hpanel;
    private HeroSheet sheet;
    private Image hsheet, poisonedpic, hurtweapon, hurthand, hurthead, hurttorso, hurtlegs, hurtfeet;
    private MediaTracker tracker;
    private JFrame frame;
    
    public static final String[] LEVELNAMES = {
        "None", "Neophyte", "Novice", "Apprentice", "Journeyman", "Craftsman", "Artisan", "Adept", "Expert",
        "LO Master", "UM Master", "ON Master", "EE Master", "PAL Master", "MON Master", "ArchMaster"};
    
    public HeroPanel(JFrame f, Font fnt) {
        super(f, "Edit Party", false);
        frame = f;
        pack();
        setSize(new Dimension(448, 423));
        setBackground(Color.black);
        setLocationRelativeTo(null);
        
        hpanel = new JPanel();
        hpanel.setBackground(Color.black);
        getContentPane().add(hpanel, "North");
        
        sheet = new HeroSheet(fnt);
        SheetClick sc = new SheetClick();
        sheet.addMouseListener(sc);
        sheet.addMouseMotionListener(sc);
        getContentPane().add(sheet, "Center");
        addKeyListener(sc);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        hsheet = tk.createImage("hsheet.gif");
        poisonedpic = tk.createImage("poisoned.gif");
        hurtweapon = tk.createImage("hurt_weapon.gif");
        hurthand = tk.createImage("hurt_hand.gif");
        hurthead = tk.createImage("hurt_head.gif");
        hurttorso = tk.createImage("hurt_torso.gif");
        hurtlegs = tk.createImage("hurt_legs.gif");
        hurtfeet = tk.createImage("hurt_feet.gif");
        tracker = new MediaTracker(this);
        tracker.addImage(hsheet, 0);
        tracker.addImage(poisonedpic, 0);
        tracker.addImage(hurtweapon, 0);
        tracker.addImage(hurthand, 0);
        tracker.addImage(hurthead, 0);
        tracker.addImage(hurttorso, 0);
        tracker.addImage(hurtlegs, 0);
        tracker.addImage(hurtfeet, 0);
        tracker.checkID(0, true);
        //try { tracker.waitForID(0); }
        //catch (InterruptedException e) {}
    }
    
    public void addHero(HeroData h) {
        hpanel.add(h);
        h.addMouseListener(this);
        //Item.ImageTracker.checkID(0,true);
        //try { Item.ImageTracker.waitForID(0); }
        //catch (InterruptedException e) {}
    }
    
    protected void removeHeroes() {
        hpanel.removeAll();
        sheet.hero = null;
    }
    
    public boolean hasHero(HeroData h) {
        if (hpanel.isAncestorOf(h)) return true;
        else return false;
    }
    
    public void setHero(HeroData h) {
        if (sheet.hero != null) sheet.hero.setBorder(null);
        h.setBorder(BorderFactory.createLineBorder(Color.yellow));
        sheet.setHero(h);
        if (isVisible()) hpanel.repaint();
    }
    
    public void mousePressed(MouseEvent e) {
        if (sheet.hero != null && e.getSource() == sheet.hero) new ChampionEdit(frame, sheet.hero);
        else setHero((HeroData) e.getSource());
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    private class HeroSheet extends JComponent {
        HeroData hero;
        BufferedImage offscreen;
        Graphics2D offg;
        boolean viewing = false;
        Font dungfont14, dungfont24;
        
        public HeroSheet(Font fnt) {
            setSize(448, 326);
            setBackground(Color.black);
            offscreen = new BufferedImage(448, 326, BufferedImage.TYPE_INT_BGR);
            offg = offscreen.createGraphics();
            offg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            dungfont14 = fnt.deriveFont(14.0f);
            dungfont24 = dungfont14.deriveFont(24.0f);
                        /*
                        try {
                                FileInputStream in = new FileInputStream("timrom.ttf");
                                dungfont14 = Font.createFont(Font.TRUETYPE_FONT,in);
                                in.close();
                                dungfont14 = dungfont.deriveFont(Font.BOLD,14);
                        } catch (Exception e) { System.out.println("Couldn't find font file. Using default."); dungfont14 = new Font("SansSerif",Font.BOLD,14); }
                        dungfont24 = dungfont14.deriveFont(24.0f);
                        */
        }
        
        public void setHero(HeroData h) {
            hero = h;
            if (isVisible()) repaint();
        }
        
        public void paint(Graphics g) {
            
            //offg.setFont(new Font("TimesRoman",Font.BOLD,14));
            offg.setFont(dungfont14);
            offg.drawImage(hsheet, 0, 0, null);
            
            offg.setColor(new Color(30, 30, 30));
            offg.drawString(hero.name + "   " + hero.lastname, 8, 20);
            offg.drawString("Health", 18, 288);
            offg.drawString(hero.health + " / " + hero.maxhealth, 128, 288);//was 290
            offg.drawString("Stamina", 18, 306);
            offg.drawString(hero.stamina + " / " + hero.maxstamina, 128, 306);//was 308
            offg.drawString("Mana", 18, 324);
            offg.drawString(hero.mana + " / " + hero.maxmana, 128, 324);
            
            offg.setColor(Color.white);
            offg.drawString(hero.name + "   " + hero.lastname, 5, 17);
            offg.drawString("Health", 15, 285);//was 252
            offg.drawString("Stamina", 15, 303);//was 270
            offg.drawString("Mana", 15, 321);//was 288
            
            if (hero.health < (hero.maxhealth / 3)) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString(hero.health + " / " + hero.maxhealth, 125, 285);
            
            if (hero.stamina < (hero.maxstamina / 3)) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString(hero.stamina + " / " + hero.maxstamina, 125, 303);
            
            if (hero.mana < (hero.maxmana / 3)) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString(hero.mana + " / " + hero.maxmana, 125, 321);
            
            offg.setColor(new Color(30, 30, 30));
            offg.drawString("Load       " + ((float) ((int) (hero.load * 10.0f + .5f))) / 10.0f + " / " + ((float) ((int) (hero.maxload * 10.0f + .5f))) / 10.0f, 263, 324);
            offg.drawString("Kg", 423, 324);
            if (hero.load > hero.maxload) offg.setColor(Color.red);
            else if (hero.load > hero.maxload * 3 / 4) offg.setColor(Color.yellow);
            else offg.setColor(Color.white);
            offg.drawString("Load       " + ((float) ((int) (hero.load * 10.0f + .5f))) / 10.0f + " / " + ((float) ((int) (hero.maxload * 10.0f + .5f))) / 10.0f, 260, 321);
            offg.drawString("Kg", 420, 321);
            
            if (hero.ispoisoned) {
                offg.setColor(new Color(0, 150, 0));
                offg.setStroke(new BasicStroke(2.0f));
                //offg.drawRect(110,24,35,35);
                offg.drawRect(106, 22, 40, 37);
                //draw poisoned pic here
                if (viewing) offg.drawImage(poisonedpic, 224, 209, null);
            }
            if (viewing && hero.silenced) {
                //offg.setFont(new Font("TimesRoman",Font.BOLD,24));
                offg.setFont(dungfont24);
                offg.setColor(new Color(30, 30, 30));
                offg.drawString("SILENCED", 264, 284);
                offg.setColor(Color.red);
                offg.drawString("SILENCED", 260, 280);
                //offg.setFont(new Font("TimesRoman",Font.BOLD,14));
                offg.setFont(dungfont14);
            }
            if (hero.strengthboost < 0 || hero.dexterityboost < 0 || hero.vitalityboost < 0 || hero.intelligenceboost < 0 || hero.wisdomboost < 0 || hero.defenseboost < 0 || hero.magicresistboost < 0) {
                offg.setColor(Color.red);
                offg.setStroke(new BasicStroke(2.0f));
                //offg.drawRect(22,24,35,35);
                offg.drawRect(18, 22, 40, 37);
            }
            
            if (!viewing) showStats();
            else {
                int len = (int) (((float) hero.food / 1000.0f) * 170.0f);
                offg.setColor(new Color(30, 30, 30));
                offg.fillRect(245, 150, len, 10);
                if (hero.food < 75) offg.setColor(Color.red);
                else if (hero.food < 100) offg.setColor(Color.yellow);
                else offg.setColor(new Color(200, 130, 10));
                offg.fillRect(240, 145, len, 10);
                
                len = (int) (((float) hero.water / 1000.0f) * 170.0f);
                offg.setColor(new Color(30, 30, 30));
                offg.fillRect(245, 197, len, 10);
                if (hero.water < 75) offg.setColor(Color.red);
                else if (hero.water < 100) offg.setColor(Color.yellow);
                else offg.setColor(Color.blue);
                offg.fillRect(240, 192, len, 10);
            }
            
            offg.setColor(new Color(63, 63, 63));
            offg.drawImage(hero.weapon.pic, 124, 106, this);
                        /*
                        if (hero.head!=null) {
                                offg.fillRect(68,52,32,32);
                                offg.drawImage(hero.head.pic,68,52,this);
                        }
                        if (hero.neck!=null) {
                                offg.fillRect(12,66,32,32);
                                offg.drawImage(hero.neck.pic,12,66,this);
                        }
                        if (hero.torso!=null) {
                                offg.fillRect(68,92,32,32);
                                offg.drawImage(hero.torso.pic,68,92,this);
                        }
                        if (hero.hand!=null) {
                                offg.fillRect(12,106,32,32);
                                offg.drawImage(hero.hand.pic,12,106,this);
                        }
                        if (hero.legs!=null) {
                                offg.fillRect(68,132,32,32);
                                offg.drawImage(hero.legs.pic,68,132,this);
                        }
                        if (hero.feet!=null) {
                                offg.fillRect(68,172,32,32);
                                offg.drawImage(hero.feet.pic,68,172,this);
                        }
                        */
            if (hero.hurtweapon) {
                if (hero.weapon.number == 6) offg.drawImage(hurtweapon, 124, 106, this);
                offg.setColor(Color.red);
                offg.setStroke(new BasicStroke(2.0f));
                offg.drawRect(123, 105, 34, 34);
                offg.setColor(new Color(63, 63, 63));
            }
            if (hero.head != null) {
                offg.fillRect(68, 52, 32, 32);
                offg.drawImage(hero.head.pic, 68, 52, this);
            }
            if (hero.hurthead) {
                if (hero.head == null) offg.drawImage(hurthead, 68, 52, this);
                offg.setColor(Color.red);
                offg.setStroke(new BasicStroke(2.0f));
                offg.drawRect(67, 51, 34, 34);
                offg.setColor(new Color(63, 63, 63));
            }
            if (hero.neck != null) {
                offg.fillRect(12, 66, 32, 32);
                offg.drawImage(hero.neck.pic, 12, 66, this);
            }
            if (hero.torso != null) {
                offg.fillRect(68, 92, 32, 32);
                offg.drawImage(hero.torso.pic, 68, 92, this);
            }
            if (hero.hurttorso) {
                if (hero.torso == null) offg.drawImage(hurttorso, 68, 92, this);
                offg.setColor(Color.red);
                offg.setStroke(new BasicStroke(2.0f));
                offg.drawRect(67, 91, 34, 34);
                offg.setColor(new Color(63, 63, 63));
            }
            if (hero.hand != null) {
                offg.fillRect(12, 106, 32, 32);
                offg.drawImage(hero.hand.pic, 12, 106, this);
            }
            if (hero.hurthand) {
                if (hero.hand == null) offg.drawImage(hurthand, 12, 106, this);
                offg.setColor(Color.red);
                offg.setStroke(new BasicStroke(2.0f));
                offg.drawRect(11, 105, 34, 34);
                offg.setColor(new Color(63, 63, 63));
            }
            if (hero.legs != null) {
                offg.fillRect(68, 132, 32, 32);
                offg.drawImage(hero.legs.pic, 68, 132, this);
            }
            if (hero.hurtlegs) {
                if (hero.legs == null) offg.drawImage(hurtlegs, 68, 132, this);
                offg.setColor(Color.red);
                offg.setStroke(new BasicStroke(2.0f));
                offg.drawRect(67, 131, 34, 34);
                offg.setColor(new Color(63, 63, 63));
            }
            if (hero.feet != null) {
                offg.fillRect(68, 172, 32, 32);
                offg.drawImage(hero.feet.pic, 68, 172, this);
            }
            if (hero.hurtfeet) {
                if (hero.feet == null) offg.drawImage(hurtfeet, 68, 172, this);
                offg.setColor(Color.red);
                offg.setStroke(new BasicStroke(2.0f));
                offg.drawRect(67, 171, 34, 34);
                offg.setColor(new Color(63, 63, 63));
            }
            
            
            if (hero.pouch1 != null) {
                offg.drawImage(hero.pouch1.pic, 12, 167, this);
            }
            if (hero.pouch2 != null) {
                offg.drawImage(hero.pouch2.pic, 12, 201, this);//was 180
            }
            for (int i = 0; i < 8; i++) {
                if (hero.pack[i] != null) {
                    offg.drawImage(hero.pack[i].pic, 166 + (34 * i), 32, this);
                }
            }
            for (int i = 0; i < 8; i++) {
                if (hero.pack[i + 8] != null) {
                    offg.drawImage(hero.pack[i + 8].pic, 166 + (34 * i), 66, this);
                }
            }
            for (int i = 0; i < 2; i++) {
                if (hero.quiver[i] != null) {
                    offg.drawImage(hero.quiver[i].pic, 124 + (34 * i), 167, this);
                }
            }
            for (int i = 0; i < 2; i++) {
                if (hero.quiver[i + 2] != null) {
                    offg.drawImage(hero.quiver[i + 2].pic, 124 + (34 * i), 201, this);
                }
            }
            for (int i = 0; i < 2; i++) {
                if (hero.quiver[i + 4] != null) {
                    offg.drawImage(hero.quiver[i + 4].pic, 124 + (34 * i), 235, this);
                }
            }
            g.drawImage(offscreen, 0, 0, null);
        }
        
        public void showStats() {
            //offg.setColor(new Color(100,100,100));
            //offg.fillRect(206,104,232,190);
            offg.setColor(new Color(60, 60, 60));
            offg.fillRect(206, 104, 232, 190);
            offg.setColor(new Color(95, 95, 95));
            offg.setStroke(new BasicStroke(2.0f));
            offg.drawRect(207, 105, 230, 190);
            //offg.setFont(new Font("TimesRoman",Font.BOLD,14));
            offg.setFont(dungfont14);
            int i = 120;
            if (hero.flevel > 0) {
                offg.setColor(new Color(30, 30, 30));
                offg.drawString(LEVELNAMES[hero.flevel] + " Fighter", 218, i + 3);
                //offg.setColor(Color.white);
                if (hero.flevelboost > 0) offg.setColor(Color.green);
                else if (hero.flevelboost < 0) offg.setColor(Color.red);
                else offg.setColor(Color.white);
                offg.drawString(LEVELNAMES[hero.flevel] + " Fighter", 215, i);
                i += 15;
            }
            if (hero.nlevel > 0) {
                offg.setColor(new Color(30, 30, 30));
                offg.drawString(LEVELNAMES[hero.nlevel] + " Ninja", 218, i + 3);
                //offg.setColor(Color.white);
                if (hero.nlevelboost > 0) offg.setColor(Color.green);
                else if (hero.nlevelboost < 0) offg.setColor(Color.red);
                else offg.setColor(Color.white);
                offg.drawString(LEVELNAMES[hero.nlevel] + " Ninja", 215, i);
                i += 15;
            }
            if (hero.wlevel > 0) {
                offg.setColor(new Color(30, 30, 30));
                offg.drawString(LEVELNAMES[hero.wlevel] + " Wizard", 218, i + 3);
                //offg.setColor(Color.white);
                if (hero.wlevelboost > 0) offg.setColor(Color.green);
                else if (hero.wlevelboost < 0) offg.setColor(Color.red);
                else offg.setColor(Color.white);
                offg.drawString(LEVELNAMES[hero.wlevel] + " Wizard", 215, i);
                i += 15;
            }
            if (hero.plevel > 0) {
                offg.setColor(new Color(30, 30, 30));
                offg.drawString(LEVELNAMES[hero.plevel] + " Priest", 218, i + 3);
                //offg.setColor(Color.white);
                if (hero.plevelboost > 0) offg.setColor(Color.green);
                else if (hero.plevelboost < 0) offg.setColor(Color.red);
                else offg.setColor(Color.white);
                offg.drawString(LEVELNAMES[hero.plevel] + " Priest", 215, i);
            }
            offg.setColor(new Color(30, 30, 30));
            offg.drawString("Strength", 218, 200);
            offg.drawString("Dexterity", 218, 214);
            offg.drawString("Vitality", 218, 228);
            offg.drawString("Intelligence", 218, 242);
            offg.drawString("Wisdom", 218, 256);
            offg.drawString("Defense", 218, 275);
            offg.drawString("Resist Magic", 218, 290);
            int xpos = 218 + offg.getFontMetrics().stringWidth("Resist Magic") + 13;
            offg.drawString("" + hero.strength, xpos, 200);
            offg.drawString("" + hero.dexterity, xpos, 214);
            offg.drawString("" + hero.vitality, xpos, 228);
            offg.drawString("" + hero.intelligence, xpos, 242);
            offg.drawString("" + hero.wisdom, xpos, 256);
            offg.drawString("" + hero.defense, xpos, 275);
            offg.drawString("" + hero.magicresist, xpos, 290);
            offg.setColor(Color.white);
            offg.drawString("Strength", 215, 197);
            offg.drawString("Dexterity", 215, 211);
            offg.drawString("Vitality", 215, 225);
            offg.drawString("Intelligence", 215, 239);
            offg.drawString("Wisdom", 215, 253);
            offg.drawString("Defense", 215, 272);
            offg.drawString("Resist Magic", 215, 287);
            xpos -= 3;
            if (hero.strengthboost > 0) offg.setColor(Color.green);
            else if (hero.strengthboost < 0) offg.setColor(Color.red);
            offg.drawString("" + hero.strength, xpos, 197);
            if (hero.dexterityboost > 0) offg.setColor(Color.green);
            else if (hero.dexterityboost < 0) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString("" + hero.dexterity, xpos, 211);
            if (hero.vitalityboost > 0) offg.setColor(Color.green);
            else if (hero.vitalityboost < 0) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString("" + hero.vitality, xpos, 225);
            if (hero.intelligenceboost > 0) offg.setColor(Color.green);
            else if (hero.intelligenceboost < 0) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString("" + hero.intelligence, xpos, 239);
            if (hero.wisdomboost > 0) offg.setColor(Color.green);
            else if (hero.wisdomboost < 0) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString("" + hero.wisdom, xpos, 253);
            if (hero.defenseboost > 0) offg.setColor(Color.green);
            else if (hero.defenseboost < 0) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString("" + hero.defense, xpos, 272);
            if (hero.magicresistboost > 0) offg.setColor(Color.green);
            else if (hero.magicresistboost < 0) offg.setColor(Color.red);
            else offg.setColor(Color.white);
            offg.drawString("" + hero.magicresist, xpos, 287);
        }
        
        
    }
    
    class SheetClick extends MouseAdapter implements MouseMotionListener, KeyListener {
        int x, y;
        
        public void mouseMoved(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }
        
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                HeroSheet herosheet = sheet;
                //weapon hand
                if (x > 124 && x < 156 && y < 136 && y > 104 && herosheet.hero.weapon != DMEditor.fistfoot) {
                    if (herosheet.hero.weapon.type == Item.WEAPON || herosheet.hero.weapon.type == Item.SHIELD) {
                        herosheet.hero.unEquipEffect(herosheet.hero.weapon);
                    }
                    herosheet.hero.load -= herosheet.hero.weapon.weight;
                    herosheet.hero.weapon = DMEditor.fistfoot;
                    herosheet.repaint();
                }
                //action hand
                else if (x > 12 && x < 44 && y < 136 && y > 104 && herosheet.hero.hand != null) {
                    if (herosheet.hero.hand.type == Item.SHIELD) {
                        herosheet.hero.unEquipEffect(herosheet.hero.hand);
                    }
                    herosheet.hero.load -= herosheet.hero.hand.weight;
                    herosheet.hero.hand = null;
                    herosheet.repaint();
                }
                //head
                else if (x > 68 && x < 100 && y < 84 && y > 52 && herosheet.hero.head != null) {
                    if (herosheet.hero.head.type == Item.HEAD) {
                        herosheet.hero.unEquipEffect(herosheet.hero.head);
                    }
                    herosheet.hero.load -= herosheet.hero.head.weight;
                    herosheet.hero.head = null;
                    herosheet.repaint();
                }
                //neck
                else if (x > 12 && x < 44 && y < 98 && y > 66 && herosheet.hero.neck != null) {
                    if (herosheet.hero.neck.type == Item.NECK) {
                        herosheet.hero.unEquipEffect(herosheet.hero.neck);
                    }
                    herosheet.hero.load -= herosheet.hero.neck.weight;
                    herosheet.hero.neck = null;
                    herosheet.repaint();
                }
                //torso
                else if (x > 68 && x < 100 && y < 124 && y > 92 && herosheet.hero.torso != null) {
                    if (herosheet.hero.torso.type == Item.TORSO) {
                        herosheet.hero.unEquipEffect(herosheet.hero.torso);
                    }
                    herosheet.hero.load -= herosheet.hero.torso.weight;
                    herosheet.hero.torso = null;
                    herosheet.repaint();
                }
                //legs
                else if (x > 68 && x < 100 && y < 164 && y > 132 && herosheet.hero.legs != null) {
                    if (herosheet.hero.legs.type == Item.LEGS) {
                        herosheet.hero.unEquipEffect(herosheet.hero.legs);
                    }
                    herosheet.hero.load -= herosheet.hero.legs.weight;
                    herosheet.hero.legs = null;
                    herosheet.repaint();
                }
                //feet
                else if (x > 68 && x < 100 && y < 204 && y > 172 && herosheet.hero.feet != null) {
                    if (herosheet.hero.feet.type == Item.FEET) {
                        herosheet.hero.unEquipEffect(herosheet.hero.feet);
                    }
                    herosheet.hero.load -= herosheet.hero.feet.weight;
                    herosheet.hero.feet = null;
                    herosheet.repaint();
                }
                //pouch1
                else if (x > 12 && x < 44 && y < 199 && y > 167 && herosheet.hero.pouch1 != null) {
                    herosheet.hero.load -= herosheet.hero.pouch1.weight;
                    herosheet.hero.pouch1 = null;
                    herosheet.repaint();
                }
                //pouch2
                else if (x > 12 && x < 44 && y < 233 && y > 201 && herosheet.hero.pouch2 != null) {
                    herosheet.hero.load -= herosheet.hero.pouch2.weight;
                    herosheet.hero.pouch2 = null;
                    herosheet.repaint();
                }
                //pack
                else if (x > 166 && x < 436 && y < 98 && y > 32) {
                    int i = 0;
                    if (y > 65) i = 8;
                    if (herosheet.hero.pack[(x - 166) / 34 + i] != null) {
                        herosheet.hero.load -= herosheet.hero.pack[(x - 166) / 34 + i].weight;
                        herosheet.hero.pack[(x - 166) / 34 + i] = null;
                        herosheet.repaint();
                    }
                }
                //quiver
                else if (x > 124 && x < 190 && y < 267 && y > 167) {
                    int i = 0;
                    if (y > 234) i = 4;
                    else if (y > 200) i = 2;
                    if (herosheet.hero.quiver[(x - 124) / 34 + i] != null) {
                        herosheet.hero.load -= herosheet.hero.quiver[(x - 124) / 34 + i].weight;
                        herosheet.hero.quiver[(x - 124) / 34 + i] = null;
                        herosheet.repaint();
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                HeroSheet herosheet = sheet;
                //weapon hand
                if (x > 124 && x < 156 && y < 136 && y > 104) {
                    herosheet.hero.hurtweapon = !herosheet.hero.hurtweapon;
                    herosheet.repaint();
                }
                //action hand
                else if (x > 12 && x < 44 && y < 136 && y > 104) {
                    herosheet.hero.hurthand = !herosheet.hero.hurthand;
                    herosheet.repaint();
                }
                //head
                else if (x > 68 && x < 100 && y < 84 && y > 52) {
                    herosheet.hero.hurthead = !herosheet.hero.hurthead;
                    herosheet.repaint();
                }
                //torso
                else if (x > 68 && x < 100 && y < 124 && y > 92) {
                    herosheet.hero.hurttorso = !herosheet.hero.hurttorso;
                    herosheet.repaint();
                }
                //legs
                else if (x > 68 && x < 100 && y < 164 && y > 132) {
                    herosheet.hero.hurtlegs = !herosheet.hero.hurtlegs;
                    herosheet.repaint();
                }
                //feet
                else if (x > 68 && x < 100 && y < 204 && y > 172) {
                    herosheet.hero.hurtfeet = !herosheet.hero.hurtfeet;
                    herosheet.repaint();
                }
            }
        }
        
        public void mousePressed(MouseEvent e) {
            HeroSheet herosheet = (HeroSheet) e.getSource();
            x = e.getX();
            y = e.getY();
            Item tempitem;
            //weapon hand
            if (x > 124 && x < 156 && y < 136 && y > 104) {
                if (herosheet.hero.weapon.number == 219 || herosheet.hero.weapon.number == 261) {
                    return;
                }
                if (herosheet.hero.weapon != DMEditor.fistfoot) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item In " + herosheet.hero.name + "'s Weapon Hand");
                    DMEditor.itemwizard.setItem(herosheet.hero.weapon);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item In " + herosheet.hero.name + "'s Weapon Hand");
                    DMEditor.itemwizard.showFromHero(Item.WEAPON);
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null) {
                    if (herosheet.hero.weapon != null) {
                        if (herosheet.hero.weapon.type == Item.WEAPON || herosheet.hero.weapon.type == Item.SHIELD)
                            herosheet.hero.unEquipEffect(herosheet.hero.weapon);
                        herosheet.hero.load -= herosheet.hero.weapon.weight;
                    }
                    herosheet.hero.weapon = tempitem;
                    if (tempitem.type == Item.WEAPON || tempitem.type == Item.SHIELD)
                        herosheet.hero.equipEffect(tempitem);
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //action hand
            else if (x > 12 && x < 44 && y < 136 && y > 104) {
                if (herosheet.hero.hand != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item In " + herosheet.hero.name + "'s Ready Hand");
                    DMEditor.itemwizard.setItem(herosheet.hero.hand);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item In " + herosheet.hero.name + "'s Ready Hand");
                    DMEditor.itemwizard.showFromHero(Item.SHIELD);
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null) {
                    if (herosheet.hero.hand != null) {
                        if (herosheet.hero.hand.type == Item.SHIELD) herosheet.hero.unEquipEffect(herosheet.hero.hand);
                        herosheet.hero.load -= herosheet.hero.hand.weight;
                    }
                    herosheet.hero.hand = tempitem;
                    if (tempitem.type == Item.SHIELD) herosheet.hero.equipEffect(tempitem);
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //head
            else if (x > 68 && x < 100 && y < 84 && y > 52) {
                if (herosheet.hero.head != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item On " + herosheet.hero.name + "'s Head");
                    DMEditor.itemwizard.setItem(herosheet.hero.head);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item On " + herosheet.hero.name + "'s Head");
                    DMEditor.itemwizard.showFromHero(Item.HEAD);
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null && tempitem.type == Item.HEAD) {
                    if (herosheet.hero.head != null) {
                        if (herosheet.hero.head.type == Item.HEAD) herosheet.hero.unEquipEffect(herosheet.hero.head);
                        herosheet.hero.load -= herosheet.hero.head.weight;
                    }
                    herosheet.hero.head = tempitem;
                    if (tempitem.type == Item.HEAD) herosheet.hero.equipEffect(tempitem);
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //neck
            else if (x > 12 && x < 44 && y < 98 && y > 66) {
                if (herosheet.hero.neck != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item Around " + herosheet.hero.name + "'s Neck");
                    DMEditor.itemwizard.setItem(herosheet.hero.neck);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item Around " + herosheet.hero.name + "'s Neck");
                    DMEditor.itemwizard.showFromHero(Item.NECK);
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null && tempitem.type == Item.NECK) {
                    if (herosheet.hero.neck != null) {
                        if (herosheet.hero.neck.type == Item.NECK) herosheet.hero.unEquipEffect(herosheet.hero.neck);
                        herosheet.hero.load -= herosheet.hero.neck.weight;
                    }
                    herosheet.hero.neck = tempitem;
                    if (tempitem.type == Item.NECK) herosheet.hero.equipEffect(tempitem);
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //torso
            else if (x > 68 && x < 100 && y < 124 && y > 92) {
                if (herosheet.hero.torso != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item On " + herosheet.hero.name + "'s Torso");
                    DMEditor.itemwizard.setItem(herosheet.hero.torso);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item On " + herosheet.hero.name + "'s Torso");
                    DMEditor.itemwizard.showFromHero(Item.TORSO);
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null && tempitem.type == Item.TORSO) {
                    if (herosheet.hero.torso != null) {
                        if (herosheet.hero.torso.type == Item.TORSO) herosheet.hero.unEquipEffect(herosheet.hero.torso);
                        herosheet.hero.load -= herosheet.hero.torso.weight;
                    }
                    herosheet.hero.torso = tempitem;
                    if (tempitem.type == Item.TORSO) herosheet.hero.equipEffect(tempitem);
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //legs
            else if (x > 68 && x < 100 && y < 164 && y > 132) {
                if (herosheet.hero.legs != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item On " + herosheet.hero.name + "'s Legs");
                    DMEditor.itemwizard.setItem(herosheet.hero.legs);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item On " + herosheet.hero.name + "'s Legs");
                    DMEditor.itemwizard.showFromHero(Item.LEGS);
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null && tempitem.type == Item.LEGS) {
                    if (herosheet.hero.legs != null) {
                        if (herosheet.hero.legs.type == Item.LEGS) herosheet.hero.unEquipEffect(herosheet.hero.legs);
                        herosheet.hero.load -= herosheet.hero.legs.weight;
                    }
                    herosheet.hero.legs = tempitem;
                    if (tempitem.type == Item.LEGS) herosheet.hero.equipEffect(tempitem);
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //feet
            else if (x > 68 && x < 100 && y < 204 && y > 172) {
                if (herosheet.hero.feet != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item On " + herosheet.hero.name + "'s Feet");
                    DMEditor.itemwizard.setItem(herosheet.hero.feet);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item On " + herosheet.hero.name + "'s Feet");
                    DMEditor.itemwizard.showFromHero(Item.FEET);
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null && tempitem.type == Item.FEET) {
                    if (herosheet.hero.feet != null) {
                        if (herosheet.hero.feet.type == Item.FEET) herosheet.hero.unEquipEffect(herosheet.hero.feet);
                        herosheet.hero.load -= herosheet.hero.feet.weight;
                    }
                    herosheet.hero.feet = tempitem;
                    if (tempitem.type == Item.FEET) herosheet.hero.equipEffect(tempitem);
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //pouch1
            else if (x > 12 && x < 44 && y < 199 && y > 167) {
                if (herosheet.hero.pouch1 != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item In " + herosheet.hero.name + "'s Pouch");
                    DMEditor.itemwizard.setItem(herosheet.hero.pouch1);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item In " + herosheet.hero.name + "'s Pouch");
                    DMEditor.itemwizard.show();
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null && tempitem.size < 2) {
                    if (herosheet.hero.pouch1 != null) {
                        herosheet.hero.load -= herosheet.hero.pouch1.weight;
                    }
                    herosheet.hero.pouch1 = tempitem;
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //pouch2
            else if (x > 12 && x < 44 && y < 233 && y > 201) {
                if (herosheet.hero.pouch2 != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item In " + herosheet.hero.name + "'s Pouch");
                    DMEditor.itemwizard.setItem(herosheet.hero.pouch2);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item In " + herosheet.hero.name + "'s Pouch");
                    DMEditor.itemwizard.show();
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null && tempitem.size < 2) {
                    if (herosheet.hero.pouch2 != null) {
                        herosheet.hero.load -= herosheet.hero.pouch2.weight;
                    }
                    herosheet.hero.pouch2 = tempitem;
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //pack
            else if (x > 166 && x < 436 && y < 98 && y > 32) {
                int i = 0;
                if (y > 65) i = 8;
                if (herosheet.hero.pack[(x - 166) / 34 + i] != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item In " + herosheet.hero.name + "'s Pack");
                    DMEditor.itemwizard.setItem(herosheet.hero.pack[(x - 166) / 34 + i]);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item In " + herosheet.hero.name + "'s Pack");
                    DMEditor.itemwizard.show();
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null) {
                    if (herosheet.hero.pack[(x - 166) / 34 + i] != null) {
                        herosheet.hero.load -= herosheet.hero.pack[(x - 166) / 34 + i].weight;
                    }
                    herosheet.hero.pack[(x - 166) / 34 + i] = tempitem;
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            }
            //quiver
            else if (x > 124 && x < 190 && y < 267 && y > 167) {
                int i = 0;
                if (y > 234) i = 4;
                else if (y > 200) i = 2;
                if (herosheet.hero.quiver[(x - 124) / 34 + i] != null) {
                    DMEditor.itemwizard.setTitle("Item Wizard - Change Item In " + herosheet.hero.name + "'s Quiver");
                    DMEditor.itemwizard.setItem(herosheet.hero.quiver[(x - 124) / 34 + i]);
                    tempitem = DMEditor.itemwizard.getItem();
                } else {
                    DMEditor.itemwizard.setTitle("Item Wizard - Put An Item In " + herosheet.hero.name + "'s Quiver");
                    DMEditor.itemwizard.showFromHero(7);
                    tempitem = DMEditor.itemwizard.getItem();
                }
                if (tempitem != null && tempitem.type == Item.WEAPON && ((tempitem.projtype > 0 && tempitem.size < 2) || (tempitem.size < 4 && (x - 124) / 34 + i == 0))) {
                    if (herosheet.hero.quiver[(x - 124) / 34 + i] != null) {
                        herosheet.hero.load -= herosheet.hero.quiver[(x - 124) / 34 + i].weight;
                    }
                    herosheet.hero.quiver[(x - 124) / 34 + i] = tempitem;
                    herosheet.hero.load += tempitem.weight;
                    herosheet.repaint();
                }
            } else if ((x > 5 && x < 250 && y < 20 && y > 4) || (x > 10 && x < 190 && y > 272) || (x > 205 && x < 438 && y < 295 && y > 103)) {
                //edit stats
                new ChampionEdit(frame, herosheet.hero);
            }
            //eye or mouth
            else if ((x > 22 && x < 57 && y < 58 && y > 24) || (x > 109 && x < 146 && y < 58 && y > 24)) {
                herosheet.viewing = true;
                herosheet.repaint();
            }
            //else if (x>420 && x<440 && y<25 && y>5) { hide(); }
        }
        
        public void mouseReleased(MouseEvent e) {
            if (sheet.viewing) {
                sheet.viewing = false;
                sheet.paint(sheet.getGraphics());
            }
        }
        
        public void keyReleased(KeyEvent e) {
        }
        
        public void keyTyped(KeyEvent e) {
        }
        
        public void mouseDragged(MouseEvent e) {
        }
        
    }
    
    class ChampionEdit extends JDialog implements ActionListener, FilenameFilter {
        HeroData hero;
        JTextField name, lastname, health, maxhealth, stamina, maxstamina, mana, maxmana, strength, nstrength, dexterity, ndexterity, vitality, nvitality, intelligence, nintelligence, wisdom, nwisdom, defense, ndefense, magicresist, nmagicresist, food, water;
        JComboBox flevelbox, nlevelbox, wlevelbox, plevelbox, flevelbox2, nlevelbox2, wlevelbox2, plevelbox2;
        //JToggleButton poisoned,silenced;
        JTextField poisoned, silenced;
        String picname, picdirectory;
        ImageIcon pic;
        JLabel portrait;
        int picnumber = 0;
        File portraitdirectory;
        File[] portraitfiles;
        //JFileChooser browser;
        FileDialog browser;
        Toolkit tk;
        //SpecialAbility[] abilities;
        Vector abilities = new Vector();
        
        public ChampionEdit(JFrame f, HeroData h) {
            super(f, "Edit Champion", true);
            setSize(610, 443);
            setLocationRelativeTo(f);
            Container cp = getContentPane();
            hero = h;
            
            //labels
            JPanel labelpanel = new JPanel();
            labelpanel.setLayout(new GridLayout(14, 1, 2, 9));
            labelpanel.add(new JLabel("Fighter Level:"));
            labelpanel.add(new JLabel("Ninja Level:"));
            labelpanel.add(new JLabel("Wizard Level:"));
            labelpanel.add(new JLabel("Priest Level:"));
            labelpanel.add(new JLabel("Health:"));
            labelpanel.add(new JLabel("Stamina:"));
            labelpanel.add(new JLabel("Mana:"));
            labelpanel.add(new JLabel("Strength:"));
            labelpanel.add(new JLabel("Dexterity:"));
            labelpanel.add(new JLabel("Vitality:"));
            labelpanel.add(new JLabel("Intelligence:"));
            labelpanel.add(new JLabel("Wisdom:"));
            labelpanel.add(new JLabel("Defense:"));
            labelpanel.add(new JLabel("Magic Resist:"));
            
            //fields
            name = new JTextField(h.name, 8);
            lastname = new JTextField(h.lastname, 25);
            food = new JTextField("" + h.food, 4);
            water = new JTextField("" + h.water, 4);
            JPanel fw = new JPanel();
            fw.add(new JLabel("Food:"));
            fw.add(food);
            fw.add(new JLabel("Water:"));
            fw.add(water);
            //poisoned = new JToggleButton("Poisoned");
            //silenced = new JToggleButton("Silenced");
            poisoned = new JTextField("0", 3);
            silenced = new JTextField("0", 3);
            //poisoned.setSelected(h.ispoisoned);
            //silenced.setSelected(h.silenced);
            if (h.ispoisoned) poisoned.setText("" + h.poison);
            if (h.silenced) silenced.setText("" + h.silencecount);
            JPanel ps = new JPanel();
            ps.add(new JLabel("Poisoned:"));
            ps.add(poisoned);
            ps.add(new JLabel("Silenced:"));
            ps.add(silenced);
            JPanel top = new JPanel();
            top.add(name);
            top.add(lastname);
            top.add(fw);
            top.add(ps);
            top.setPreferredSize(new Dimension(300, 120));
            
            flevelbox = new JComboBox(LEVELNAMES);
            flevelbox.setEditable(false);
            flevelbox.setSelectedIndex(h.flevel);
            nlevelbox = new JComboBox(LEVELNAMES);
            nlevelbox.setEditable(false);
            nlevelbox.setSelectedIndex(h.nlevel);
            wlevelbox = new JComboBox(LEVELNAMES);
            wlevelbox.setEditable(false);
            wlevelbox.setSelectedIndex(h.wlevel);
            plevelbox = new JComboBox(LEVELNAMES);
            plevelbox.setEditable(false);
            plevelbox.setSelectedIndex(h.plevel);
            flevelbox2 = new JComboBox(LEVELNAMES);
            flevelbox2.setEditable(false);
            flevelbox2.setSelectedIndex(h.flevel - h.flevelboost);
            nlevelbox2 = new JComboBox(LEVELNAMES);
            nlevelbox2.setEditable(false);
            nlevelbox2.setSelectedIndex(h.nlevel - h.nlevelboost);
            wlevelbox2 = new JComboBox(LEVELNAMES);
            wlevelbox2.setEditable(false);
            wlevelbox2.setSelectedIndex(h.wlevel - h.wlevelboost);
            plevelbox2 = new JComboBox(LEVELNAMES);
            plevelbox2.setEditable(false);
            plevelbox2.setSelectedIndex(h.plevel - h.plevelboost);
            
            health = new JTextField("" + h.health, 4);
            maxhealth = new JTextField("" + h.maxhealth, 4);
            stamina = new JTextField("" + h.stamina, 4);
            maxstamina = new JTextField("" + h.maxstamina, 4);
            mana = new JTextField("" + h.mana, 4);
            maxmana = new JTextField("" + h.maxmana, 4);
            strength = new JTextField("" + h.strength, 4);
            nstrength = new JTextField("" + (h.strength - h.strengthboost), 4);
            dexterity = new JTextField("" + h.dexterity, 4);
            ndexterity = new JTextField("" + (h.dexterity - h.dexterityboost), 4);
            vitality = new JTextField("" + h.vitality, 4);
            nvitality = new JTextField("" + (h.vitality - h.vitalityboost), 4);
            intelligence = new JTextField("" + h.intelligence, 4);
            nintelligence = new JTextField("" + (h.intelligence - h.intelligenceboost), 4);
            wisdom = new JTextField("" + h.wisdom, 4);
            nwisdom = new JTextField("" + (h.wisdom - h.wisdomboost), 4);
            defense = new JTextField("" + h.defense, 4);
            ndefense = new JTextField("" + (h.defense - h.defenseboost), 4);
            magicresist = new JTextField("" + h.magicresist, 4);
            nmagicresist = new JTextField("" + (h.magicresist - h.magicresistboost), 4);
            
            FlowLayout flay = new FlowLayout(FlowLayout.LEFT, 2, 0);
            JPanel flevelpan = new JPanel(flay);
            flevelpan.add(flevelbox);
            flevelpan.add(new JLabel("/"));
            flevelpan.add(flevelbox2);
            JPanel nlevelpan = new JPanel(flay);
            nlevelpan.add(nlevelbox);
            nlevelpan.add(new JLabel("/"));
            nlevelpan.add(nlevelbox2);
            JPanel wlevelpan = new JPanel(flay);
            wlevelpan.add(wlevelbox);
            wlevelpan.add(new JLabel("/"));
            wlevelpan.add(wlevelbox2);
            JPanel plevelpan = new JPanel(flay);
            plevelpan.add(plevelbox);
            plevelpan.add(new JLabel("/"));
            plevelpan.add(plevelbox2);
            
            JPanel healthpan = new JPanel(flay);
            healthpan.add(health);
            healthpan.add(new JLabel("/"));
            healthpan.add(maxhealth);
            JPanel staminapan = new JPanel(flay);
            staminapan.add(stamina);
            staminapan.add(new JLabel("/"));
            staminapan.add(maxstamina);
            JPanel manapan = new JPanel(flay);
            manapan.add(mana);
            manapan.add(new JLabel("/"));
            manapan.add(maxmana);
            JPanel strengthpan = new JPanel(flay);
            strengthpan.add(strength);
            strengthpan.add(new JLabel("/"));
            strengthpan.add(nstrength);
            JPanel dexteritypan = new JPanel(flay);
            dexteritypan.add(dexterity);
            dexteritypan.add(new JLabel("/"));
            dexteritypan.add(ndexterity);
            JPanel vitalitypan = new JPanel(flay);
            vitalitypan.add(vitality);
            vitalitypan.add(new JLabel("/"));
            vitalitypan.add(nvitality);
            JPanel intelligencepan = new JPanel(flay);
            intelligencepan.add(intelligence);
            intelligencepan.add(new JLabel("/"));
            intelligencepan.add(nintelligence);
            JPanel wisdompan = new JPanel(flay);
            wisdompan.add(wisdom);
            wisdompan.add(new JLabel("/"));
            wisdompan.add(nwisdom);
            JPanel defensepan = new JPanel(flay);
            defensepan.add(defense);
            defensepan.add(new JLabel("/"));
            defensepan.add(ndefense);
            JPanel magicresistpan = new JPanel(flay);
            magicresistpan.add(magicresist);
            magicresistpan.add(new JLabel("/"));
            magicresistpan.add(nmagicresist);
            
            JPanel fieldpanel = new JPanel();
            fieldpanel.setLayout(new GridLayout(14, 1, 5, 0));
            fieldpanel.add(flevelpan);
            fieldpanel.add(nlevelpan);
            fieldpanel.add(wlevelpan);
            fieldpanel.add(plevelpan);
            fieldpanel.add(healthpan);
            fieldpanel.add(staminapan);
            fieldpanel.add(manapan);
            fieldpanel.add(strengthpan);
            fieldpanel.add(dexteritypan);
            fieldpanel.add(vitalitypan);
            fieldpanel.add(intelligencepan);
            fieldpanel.add(wisdompan);
            fieldpanel.add(defensepan);
            fieldpanel.add(magicresistpan);
            
            //put left together
            JPanel left = new JPanel();
            left.add(Box.createHorizontalStrut(5));
            left.add(labelpanel);
            left.add(fieldpanel);
            left.setBorder(BorderFactory.createEtchedBorder());
            cp.add(left, "West");
            
            //portrait picker
            browser = new FileDialog(f, "Choose a Portrait", FileDialog.LOAD);
            browser.setDirectory("Heroes");
            browser.setFilenameFilter(this);
            tk = Toolkit.getDefaultToolkit();
            browser.setLocation(tk.getScreenSize().width / 2 - browser.getSize().width / 2, tk.getScreenSize().height / 2 - browser.getSize().height / 2);
            
            ///*
            //figure out where pic is
            picdirectory = "";
            File tester;
            boolean makenewpic = false;
            if (h.picname.indexOf('/') > 0) {
                picdirectory = h.picname.substring(0, h.picname.lastIndexOf('/'));
                tester = new File("Heroes/" + picdirectory);
                if (!tester.exists()) {
                    //System.out.println("picdirectory doesn't exist");
                    picdirectory = "";
                    h.picname = "balaan.gif";
                    makenewpic = true;
                }
            }
            //System.out.println(picdirectory);
            portraitdirectory = new File("Heroes/" + picdirectory);
            portraitfiles = portraitdirectory.listFiles(this);
            Arrays.sort(portraitfiles);
            tester = new File("Heroes/" + h.picname);
            //System.out.println("Heroes/"+h.picname);
            if (!picdirectory.equals("")) picdirectory += '/';
            if (!tester.exists()) {
                h.picname = picdirectory + portraitfiles[0].getName();
                makenewpic = true;
            }
            if (makenewpic) pic = new ImageIcon("Heroes/" + h.picname);
            else pic = new ImageIcon(h.pic);
            h.picname = h.picname.replace('\\', '/');
            picname = h.picname;
            portrait = new JLabel(pic);
            String shortpicname = picname;
            if (shortpicname.indexOf('/') > 0) shortpicname = shortpicname.substring(shortpicname.lastIndexOf('/') + 1);
            //System.out.println(shortpicname);
            boolean found = false;
            while (!found && picnumber < portraitfiles.length) {
                if (portraitfiles[picnumber].getName().equals(shortpicname)) found = true;
                else picnumber++;
            }
            if (!found) picnumber = 0;
            //*/
                        /*
                        portraitdirectory = new File("Heroes");
                        portraitfiles = portraitdirectory.listFiles(this);
                        Arrays.sort(portraitfiles);
                        tester = new File("Heroes"+File.separator+h.picname);
                        if (!tempfile.exists()) {
                                h.picname=portraitfiles[0].getName();
                                makenewpic = true;
                        }
                        if (makenewpic) pic = new ImageIcon("Heroes"+File.separator+h.picname);
                        else pic = new ImageIcon(h.pic);
                        picname = h.picname;
                        portrait = new JLabel(pic);
                        while (picnumber<portraitfiles.length) {
                                if (portraitfiles[picnumber].getName().equals(picname)) break;
                                else picnumber++;
                        }
                        */
            
            JButton leftarrow = new JButton(new ImageIcon("leftarrow.gif"));
            JButton rightarrow = new JButton(new ImageIcon("rightarrow.gif"));
            JButton browsebut = new JButton("Browse");
            JPanel portpan = new JPanel();
            JPanel portlabelpan = new JPanel();
            JPanel portraitpan = new JPanel();
            rightarrow.setActionCommand("<");
            rightarrow.addActionListener(this);
            leftarrow.setActionCommand(">");
            leftarrow.addActionListener(this);
            browsebut.addActionListener(this);
            portraitpan.add(portrait);
            portraitpan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Portrait"));
            portpan.add(leftarrow);
            portpan.add(rightarrow);
            portpan.add(browsebut);
            Box east = Box.createVerticalBox();
            east.add(Box.createVerticalGlue());
            east.add(top);
            east.add(Box.createVerticalStrut(20));
            east.add(portraitpan);
            east.add(portpan);
            east.add(Box.createVerticalGlue());
            cp.add(east, "Center");
            
            //copy special abilities (must make copy so cancel will work)
            if (h.abilities != null) {
                for (int j = 0; j < h.abilities.length; j++) {
                    abilities.add(new SpecialAbility(h.abilities[j]));
                }
            }
            
            //bottom
            JButton done = new JButton("Done");
            JButton cancel = new JButton("Cancel");
            JButton specials = new JButton("Special Abilities...");
            done.addActionListener(this);
            cancel.addActionListener(this);
            specials.addActionListener(this);
            JPanel bottom = new JPanel();
            bottom.add(done);
            bottom.add(cancel);
            bottom.add(Box.createHorizontalStrut(60));
            bottom.add(specials);
            cp.add(bottom, "South");
            
            show();
        }
        
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(">")) {
                picnumber--;
                if (picnumber < 0) picnumber = portraitfiles.length - 1;
                picname = picdirectory + portraitfiles[picnumber].getName();
                pic.setImage(tk.getImage("Heroes/" + picname));
                portrait.repaint();
            } else if (e.getActionCommand().equals("<")) {
                picnumber++;
                if (picnumber == portraitfiles.length) picnumber = 0;
                picname = picdirectory + portraitfiles[picnumber].getName();
                pic.setImage(tk.getImage("Heroes/" + picname));
                portrait.repaint();
            } else if (e.getActionCommand().equals("Browse")) {
                browser.show();
                String returnval = browser.getFile();
                if (returnval != null) {
                    String tempstring = browser.getDirectory();
                    int index = tempstring.indexOf("Heroes");
                    if (index >= 0) {
                        picdirectory = tempstring.substring(index + 6);
                        //System.out.println(picdirectory);
                        if (picdirectory.length() == 1) picdirectory = "";
                        else picdirectory = picdirectory.substring(1);
                        picdirectory = picdirectory.replace('\\', '/');
                        //System.out.println(picdirectory);
                        picname = picdirectory + returnval;
                        picname = picname.replace('\\', '/');
                        pic.setImage(tk.getImage("Heroes/" + picname));
                        portrait.repaint();
                        if (!browser.getDirectory().equals(portraitdirectory.getPath())) {
                            portraitdirectory = new File(browser.getDirectory());
                            portraitfiles = portraitdirectory.listFiles(this);
                            Arrays.sort(portraitfiles);
                        }
                        picnumber = 0;
                        boolean found = false;
                        String shortpicname = returnval;
                        while (!found && picnumber < portraitfiles.length) {
                            if (portraitfiles[picnumber].getName().equals(shortpicname)) found = true;
                            else picnumber++;
                        }
                        if (!found) picnumber = 0;//shouldn't ever happen
                        //System.out.println("picdirec = "+picdirectory+", picname = "+picname);
                    }
                }
            } else if (e.getActionCommand().equals("Done")) {
                int newh, newmaxh, news, newmaxs, newm, newmaxm, newstr, newnstr, newdex, newndex, newvit, newnvit, newint, newnint, newwis, newnwis, newdef, newndef, newmrst, newnmrst, newfood, newwater;
                try {
                    newh = Integer.parseInt(health.getText());
                    newmaxh = Integer.parseInt(maxhealth.getText());
                    news = Integer.parseInt(stamina.getText());
                    newmaxs = Integer.parseInt(maxstamina.getText());
                    newm = Integer.parseInt(mana.getText());
                    newmaxm = Integer.parseInt(maxmana.getText());
                    newstr = Integer.parseInt(strength.getText());
                    newnstr = Integer.parseInt(nstrength.getText());
                    newdex = Integer.parseInt(dexterity.getText());
                    newndex = Integer.parseInt(ndexterity.getText());
                    newvit = Integer.parseInt(vitality.getText());
                    newnvit = Integer.parseInt(nvitality.getText());
                    newint = Integer.parseInt(intelligence.getText());
                    newnint = Integer.parseInt(nintelligence.getText());
                    newwis = Integer.parseInt(wisdom.getText());
                    newnwis = Integer.parseInt(nwisdom.getText());
                    newdef = Integer.parseInt(defense.getText());
                    newndef = Integer.parseInt(ndefense.getText());
                    newmrst = Integer.parseInt(magicresist.getText());
                    newnmrst = Integer.parseInt(nmagicresist.getText());
                    newfood = Integer.parseInt(food.getText());
                    newwater = Integer.parseInt(water.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Number: " + ex.getMessage(), "Notice", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Entry: " + ex.getMessage(), "Notice", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (newh < 1) newh = 1;
                if (newmaxh < 1) newmaxh = 1;
                if (news < 1) news = 1;
                if (newmaxs < 10) newmaxs = 10;
                if (newm < 0) newm = 0;
                if (newmaxm < 0) newmaxm = 0;
                if (newstr < 1) newstr = 1;
                if (newnstr < 1) newnstr = 1;
                if (newdex < 1) newdex = 1;
                if (newndex < 1) newndex = 1;
                if (newvit < 1) newvit = 1;
                if (newnvit < 1) newnvit = 1;
                if (newint < 1) newint = 1;
                if (newnint < 1) newnint = 1;
                if (newwis < 1) newwis = 1;
                if (newnwis < 1) newnwis = 1;
                hero.health = newh;
                hero.maxhealth = newmaxh;
                hero.isdead = false;
                hero.stamina = news;
                hero.maxstamina = newmaxs;
                hero.mana = newm;
                hero.maxmana = newmaxm;
                hero.strength = newstr;
                hero.dexterity = newdex;
                hero.vitality = newvit;
                hero.intelligence = newint;
                hero.wisdom = newwis;
                hero.defense = newdef;
                hero.magicresist = newmrst;
                hero.strengthboost = hero.strength - newnstr;
                hero.dexterityboost = hero.dexterity - newndex;
                hero.vitalityboost = hero.vitality - newnvit;
                hero.intelligenceboost = hero.intelligence - newnint;
                hero.wisdomboost = hero.wisdom - newnwis;
                hero.defenseboost = hero.defense - newndef;
                hero.magicresistboost = hero.magicresist - newnmrst;
                hero.setMaxLoad();
                                /*
                                hero.maxload = newstr*4/5;
                                if (hero.stamina<hero.maxstamina/5) hero.maxload=hero.maxload*2/3;
                                else if (hero.stamina<hero.maxstamina/3) hero.maxload=hero.maxload*4/5;
                                */
                if (newfood < 11) newfood = 10;
                else if (newfood > 1000) newfood = 1000;
                if (newwater < 11) newwater = 10;
                else if (newwater > 1000) newwater = 1000;
                hero.food = newfood;
                hero.water = newwater;
                hero.flevel = flevelbox.getSelectedIndex();
                hero.nlevel = nlevelbox.getSelectedIndex();
                hero.wlevel = wlevelbox.getSelectedIndex();
                hero.plevel = plevelbox.getSelectedIndex();
                hero.flevelboost = hero.flevel - flevelbox2.getSelectedIndex();
                hero.nlevelboost = hero.nlevel - nlevelbox2.getSelectedIndex();
                hero.wlevelboost = hero.wlevel - wlevelbox2.getSelectedIndex();
                hero.plevelboost = hero.plevel - plevelbox2.getSelectedIndex();
                //hero.ispoisoned = poisoned.isSelected();
                //hero.silenced = silenced.isSelected();
                hero.poison = Integer.parseInt(poisoned.getText());
                hero.silencecount = Integer.parseInt(silenced.getText());
                if (hero.poison > 0) hero.ispoisoned = true;
                if (hero.silencecount > 0) hero.silenced = true;
                if (hero.poison < 0) hero.poison = 0;
                if (hero.silencecount < 0) hero.silencecount = 0;
                hero.name = name.getText();
                hero.lastname = lastname.getText();
                if (hero.name.length() > 8) hero.name = hero.name.substring(8);
                if (hero.lastname.length() > 30) hero.name = hero.lastname.substring(30);
                hero.pic = pic.getImage();
                hero.picname = picname;
                if (abilities.size() > 0) {
                    hero.abilities = new SpecialAbility[abilities.size()];
                    for (int i = 0; i < abilities.size(); i++) {
                        hero.abilities[i] = (SpecialAbility) abilities.get(i);
                    }
                } else hero.abilities = null;
                ((ImageIcon) hero.getIcon()).setImage(hero.pic);
                sheet.repaint();
                hpanel.repaint();
                dispose();
            } else if (e.getActionCommand().startsWith("Special")) {
                new HeroSpecials(frame, abilities);
            } else dispose();
        }
        
        public boolean accept(File f, String n) {
            n = n.toLowerCase();
            if (n.endsWith(".gif") || n.endsWith(".jpg") || n.endsWith(".png")) return true;
            return false;
        }
    }
    
}