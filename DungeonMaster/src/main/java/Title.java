import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

class Title extends JPanel implements ActionListener, MouseListener, MouseMotionListener, Runnable {
    dmnew dm;
    static JLabel loading;
    JLabel pic;
    ImageIcon icon;
    Image presents, titlepic, entrance, titlenoglow, titleglow;
    Image introa, introb, introc, introc2, introd, credits;
    BufferedImage entranceimg;
    Intro intro;
    Graphics2D introg, entranceg, entranceg2;
    Thread introthread = null;
    int timecount = 0, oldover = -1;
    javax.swing.Timer t;
    private boolean create, nochar, endintro;
    private int levelpoints = -1, hsmpoints = -1, statpoints = -1, defensepoints = -1, itempoints, abilitypoints, abilityauto;
    private Item[] itemchoose;
    private SpecialAbility[] abilitychoose;
    
    public Title(JFrame f) {
        dm = (dmnew) f;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0, 0, 64));
        Dimension sixforty = new Dimension(640, 480);
        setPreferredSize(sixforty);
        setMaximumSize(sixforty);
        
        intro = new Intro();
        intro.setSize(640, 480);
        intro.setPreferredSize(sixforty);
        intro.setMaximumSize(sixforty);
        intro.addMouseListener(this);
        introa = dm.tk.getImage("Endings" + File.separator + "introa.gif");
        introb = dm.tk.getImage("Endings" + File.separator + "introb.gif");
        introc = dm.tk.getImage("Endings" + File.separator + "introc.gif");
        introc2 = dm.tk.getImage("Endings" + File.separator + "introc2.gif");
        introd = dm.tk.getImage("Endings" + File.separator + "introd.gif");
        credits = dm.tk.getImage("Endings" + File.separator + "credits.gif");
        
        //dm.chooser.setDirectory("Dungeons");
        //dm.chooser.setTitle("Load a Custom Dungeon Map");
        //dm.chooser.setMode(FileDialog.LOAD);
        
        icon = new ImageIcon("alandale.gif");
        pic = new JLabel(icon);
        pic.setBackground(new Color(0, 0, 64));
        add("Center", pic);
        loading = new JLabel();
        loading.setHorizontalAlignment(JLabel.CENTER);
        loading.setFont(dm.dungfont14.deriveFont(16.0f));
        loading.setForeground(Color.white);
        loading.setText("Loading Game...");
        loading.setPreferredSize(sixforty);
        
        MediaTracker tracker = new MediaTracker(this);
        presents = dm.tk.getImage("presents.gif");
        titlepic = dm.tk.getImage("title.gif");
        entrance = dm.tk.getImage("entrance.jpg");
        titlenoglow = dm.tk.getImage("Endings" + File.separator + "titlenoglow.png");
        titleglow = dm.tk.getImage("Endings" + File.separator + "titleglow.png");
        tracker.addImage(presents, 0);
        tracker.addImage(titlepic, 0);
        tracker.addImage(entrance, 0);
        tracker.addImage(titlenoglow, 0);
        tracker.addImage(titleglow, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
        }
        
        tracker.addImage(introa, 1);
        tracker.addImage(introb, 1);
        tracker.addImage(introc, 1);
        tracker.addImage(introc2, 1);
        tracker.addImage(introd, 1);
        tracker.addImage(credits, 1);
        try {
            tracker.waitForID(1);
        } catch (InterruptedException e) {
        }
        tracker = null;
        
        entranceimg = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        entranceg = entranceimg.createGraphics();
        //entranceg.setFont(dm.scrollfont.deriveFont(24.0f));
        //entranceg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawEntrance(-1);
        
        dm.creditslabel.setIcon(new ImageIcon(credits));
        
        t = new javax.swing.Timer(4000, this);
        pic.addMouseListener(this);
        t.start();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == null) {
            //timer stuff
            timecount++;
            switch (timecount) {
                case 1:
                    icon.setImage(presents);
                    repaint();
                    break;
                case 2:
                    icon.setImage(titlepic);
                    repaint();
                    break;
                case 3:
                    t.stop();
                    icon.setImage(entranceimg);
                    repaint();
                    pic.addMouseMotionListener(this);
                    break;
            }
        }
    }
    
    private void startDMJ(File gamefile) {
        removeAll();
        add("Center", loading);
        validate();
        dm.validate();
        dm.paint(dm.getGraphics());
        if (gamefile != null) dm.loadMap(gamefile);
        else dm.loadGame(true);
        dm.start();
        dm.addKeyListener(dm.dmove);
        dm.setContentPane(dm.imagePane);
        dm.validate();
    }
    
    private class Intro extends JComponent {
        BufferedImage offscreen;
        Graphics2D offg;
        
        public Intro() {
            offscreen = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
            offg = offscreen.createGraphics();
            offg.setFont(dm.scrollfont.deriveFont(20.0f));
            offg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            setBackground(Color.black);
            setForeground(Color.black);
        }
        
        public void paint(Graphics g) {
            g.drawImage(offscreen, 0, 0, null);
        }
    }
    
    public void run() {
        try {
            introthread.sleep(200);
            //display first pic
            introg.drawImage(introa, 0, 40, null);
            introthread.sleep(1000);
            int count = 0;
            while (!endintro) {
                switch (count) {
                    case 0:
                        //display text1
                        drawText("CENTURIES HAVE PASSED SINCE CHAOS WAS LAST DEFEATED.", 3000);
                        introg.drawImage(introa, 0, 40, null);
                        intro.offg.drawImage(introa, 0, 40, null);
                        introthread.sleep(200);
                        break;
                    case 1:
                        drawText("THE WORLD HAS MOVED ON, WITH PERIODS OF PEACE AND PROSPERITY MIXED WITH INEVITABLE WAR AND REPRESSION.", 6000);
                        break;
                    case 2:
                        //scroll first pic right
                        for (int x = -5; x >= -640; x -= 5) {
                            introg.drawImage(introa, x, 40, null);
                            intro.offg.drawImage(introa, x, 40, null);
                            introthread.sleep(40);
                        }
                        introthread.sleep(1000);
                        break;
                    case 3:
                        //display text2
                        drawText("THE BALANCE OF THE UNIVERSE HAS BEEN MAINTAINED.", 6000);
                        break;
                    case 4:
                        //display second pic
                        introg.drawImage(introb, 0, 0, null);
                        intro.offg.drawImage(introb, 0, 0, null);
                        introthread.sleep(1000);
                        break;
                    case 5:
                        //display text3
                        drawText("BUT ALL IS THREATENED NOW WITH THE RISING OF AN OLD EVIL.", 5000);
                        break;
                    case 6:
                        //scroll second pic down
                        for (int y = -5; y >= -480; y -= 5) {
                            introg.drawImage(introb, 0, y, null);
                            intro.offg.drawImage(introb, 0, y, null);
                            introthread.sleep(40);
                        }
                        introthread.sleep(1000);
                        break;
                    case 7:
                        //display text4
                        drawText("A DEMON.", 2000);
                        introg.drawImage(introb, 0, -480, null);
                        intro.offg.drawImage(introb, 0, -480, null);
                        introthread.sleep(200);
                        break;
                    case 8:
                        drawText("ONE OF MANY ONCE GATHERED UNDER CHAOS, HE HAS GROWN STRONG OVER THE YEARS.", 4000);
                        introg.drawImage(introb, 0, -480, null);
                        intro.offg.drawImage(introb, 0, -480, null);
                        introthread.sleep(200);
                        break;
                    case 9:
                        drawText("HE HATES THE WORLD AND ALL WHO STRIVE FOR BALANCE.", 5000);
                        break;
                    case 10:
                        //scroll second pic back up
                        for (int y = -475; y <= 0; y += 5) {
                            introg.drawImage(introb, 0, y, null);
                            intro.offg.drawImage(introb, 0, y, null);
                            introthread.sleep(20);
                        }
                        introthread.sleep(1000);
                        break;
                    case 11:
                        //display text5
                        drawText("HE SEEKS THE RETURN OF HIS MASTER.", 4000);
                        introg.drawImage(introb, 0, 0, null);
                        intro.offg.drawImage(introb, 0, 0, null);
                        introthread.sleep(200);
                        break;
                    case 12:
                        drawText("ALREADY HE IS CLOSE.", 4000);
                        break;
                    case 13:
                        //display third pic
                        introg.drawImage(introc, 0, 0, null);
                        intro.offg.drawImage(introc, 0, 0, null);
                        introthread.sleep(1000);
                        break;
                    case 14:
                        //display text6
                        drawText("HE CONTROLS THE POWER GEM.", 300, 3000);
                        introg.drawImage(introc, 0, 0, null);
                        intro.offg.drawImage(introc, 0, 0, null);
                        introthread.sleep(200);
                        break;
                    case 15:
                        drawText("USING ITS MAGICK, HE HAS TAKEN THE GREY LORD PRISONER.", 300, 5000);
                        introg.drawImage(introc2, 0, 0, null);
                        intro.offg.drawImage(introc2, 0, 0, null);
                        introthread.sleep(200);
                        break;
                    case 16:
                        drawText("THROUGH TORTURE AND EXPERIMENTATION, HE IS ATTEMPTING TO LEARN MORE OF ITS SECRETS.", 300, 6000);
                        introg.drawImage(introc2, 0, 0, null);
                        intro.offg.drawImage(introc2, 0, 0, null);
                        introthread.sleep(200);
                        break;
                    case 17:
                        drawText("IT IS ONLY A MATTER OF TIME BEFORE HE DISCOVERS HOW TO SEPARATE CHAOS AND ORDER ONCE AGAIN.", 300, 6000);
                        break;
                    case 18:
                        //display last pic
                        introg.setColor(Color.black);
                        introg.fillRect(0, 0, 640, 480);
                        introg.drawImage(introd, 0, 40, null);
                        intro.offg.clearRect(0, 0, 640, 480);
                        intro.offg.drawImage(introc, 118, 65, null);
                        introthread.sleep(1000);
                        break;
                    case 19:
                        //scroll last pic right
                        for (int x = -5; x >= -640; x -= 5) {
                            introg.drawImage(introd, x, 40, null);
                            intro.offg.drawImage(introd, x, 40, null);
                            introthread.sleep(40);
                        }
                        introthread.sleep(1000);
                        break;
                    default:
                        //display text8
                        drawText("THE WORLD IS ON THE VERGE OF DAMNATION.", 7000);
                        endintro = true;
                        break;
                }
                count++;
            }
            //restore entrance pic
            remove(intro);
            add("Center", pic);
            validate();
            dm.validate();
            doLayout();
            repaint();
        } catch (InterruptedException e) {
            remove(intro);
            add("Center", pic);
            validate();
            dm.validate();
            doLayout();
            repaint();
        }
    }
    
    private void drawText(String text, int time) {
        drawText(text, 240, time);
    }
    
    private void drawText(String text, int y, int time) {
        try {
            int numrows, lastindex, nextindex, xpos, ypos;
            String[] rows;
            numrows = text.length() / 35;
            if (text.length() % 35 > 0) numrows++;
            rows = new String[numrows];
            lastindex = 0;
            int i = 0;
            while (lastindex < text.length()) {
                nextindex = text.indexOf(' ', lastindex + 35);
                if (nextindex == -1) nextindex = text.length();
                rows[i] = text.substring(lastindex, nextindex);
                lastindex = nextindex + 1;
                i++;
            }
            numrows = i;
            introg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            introg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            intro.offg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            intro.offg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            for (int j = 0; j < 6; j++) {
                for (i = 0; i < numrows; i++) {
                    xpos = 320 - introg.getFontMetrics().stringWidth(rows[i]) / 2;
                    ypos = y + (i - numrows / 2) * 20;
                    introg.setColor(new Color(0, 0, 0, 25 * j));
                    introg.drawString(rows[i], xpos + 2, ypos + 2);
                    introg.setColor(new Color(255, 206, 16, 25 * j));
                    introg.drawString(rows[i], xpos, ypos);
                }
                introthread.sleep(15);
            }
            for (int j = 0; j < 6; j++) {
                for (i = 0; i < numrows; i++) {
                    xpos = 320 - intro.offg.getFontMetrics().stringWidth(rows[i]) / 2;
                    ypos = y + (i - numrows / 2) * 20;
                    intro.offg.setColor(new Color(0, 0, 0, 25 * j));
                    intro.offg.drawString(rows[i], xpos + 2, ypos + 2);
                    intro.offg.setColor(new Color(255, 206, 16, 25 * j));
                    intro.offg.drawString(rows[i], xpos, ypos);
                }
            }
            introthread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
    
    private boolean setMapStart(File gamefile) {
        try {
            
            FileInputStream in = new FileInputStream(gamefile);
            ObjectInputStream si = new ObjectInputStream(in);
            
            String version = si.readUTF();
            if (!version.equals(dm.version)) {
                in.close();
                System.out.println("Incorrect Map Version: Found " + version + ", need " + dm.version);
                JOptionPane.showMessageDialog(dm, "Incorrect Map Version: Found " + version + ", need " + dm.version, "Error!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            create = si.readBoolean();
            nochar = si.readBoolean();
            if (create) {
                levelpoints = si.readInt();
                hsmpoints = si.readInt();
                statpoints = si.readInt();
                defensepoints = si.readInt();
                int num = si.readInt();
                if (num > 0) {
                    itemchoose = new Item[num];
                    for (int i = 0; i < num; i++) {
                        itemchoose[i] = (Item) si.readObject();
                    }
                    itempoints = si.readInt();
                } else {
                    itemchoose = null;
                    itempoints = 0;
                }
                num = si.readInt();
                if (num > 0) {
                    abilityauto = si.readInt();
                    abilitychoose = new SpecialAbility[num];
                    for (int i = 0; i < num; i++) {
                        abilitychoose[i] = new SpecialAbility(si);
                    }
                    abilitypoints = si.readInt();
                } else {
                    abilitychoose = null;
                    abilitypoints = 0;
                }
            }
            
            in.close();
        } catch (Exception e) {
            System.out.println("Unable to load from map.");
            e.printStackTrace(System.out);
            //pop up a dialog too
            JOptionPane.showMessageDialog(dm, "Unable to load map!", "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.getSource().equals(pic) && t.isRunning()) {
            t.stop();
            icon.setImage(entranceimg);
            repaint();
            pic.addMouseMotionListener(this);
        } else if (introthread != null && introthread.isAlive()) {
            endintro = true;
            introthread.interrupt();
        } else if (icon.getImage() != credits) {
            int x = e.getX(), y = e.getY();
            if (y < 45 && y > 20) {
                if (x > 40 && x < 100) {
                    //enter
                    File gamefile = new File("Dungeons" + File.separator + "dungeon.dat");
                    setMapStart(gamefile);
                    if (create) {
                        CreateCharacter createit = new CreateCharacter(dm, gamefile, create, nochar, levelpoints, hsmpoints, statpoints, defensepoints, itemchoose, itempoints, abilitychoose, abilityauto, abilitypoints);
                        dm.setContentPane(createit);
                        dm.validate();
                    } else startDMJ(gamefile);
                } else if (x > 120 && x < 200) {
                    //resume
                    if (!dm.setGameFile(true)) return;
                    startDMJ(null);
                } else if (x > 215 && x < 300) {
                    //custom
                    File gamefile;
                                        /*
                                        dm.chooser.setDirectory("Dungeons");
                                        dm.chooser.show();
                                        String returnval = dm.chooser.getFile();
                                        if (returnval!=null) {
                                                gamefile = new File(dm.chooser.getDirectory()+returnval);
                                        }
                                        else return;
                                        */
                    dm.chooser.setCurrentDirectory(new File("Dungeons"));
                    dm.chooser.setDialogTitle("Load a Custom Dungeon Map");
                    int returnVal = dm.chooser.showOpenDialog(dm);
                    if (returnVal == JFileChooser.APPROVE_OPTION) gamefile = dm.chooser.getSelectedFile();
                    else return;
                    setMapStart(gamefile);
                    if (create) {
                        //CreateCharacter createit = new CreateCharacter(dm,gamefile,create,nochar,levelpoints,hsmpoints,statpoints,defensepoints);
                        CreateCharacter createit = new CreateCharacter(dm, gamefile, create, nochar, levelpoints, hsmpoints, statpoints, defensepoints, itemchoose, itempoints, abilitychoose, abilityauto, abilitypoints);
                        dm.setContentPane(createit);
                        dm.validate();
                    } else startDMJ(gamefile);
                } else if (x > 325 && x < 450) {
                    //display intro animation
                    if (introthread == null || !introthread.isAlive()) {
                        introthread = new Thread(this);
                        introthread.setPriority(Thread.MAX_PRIORITY);
                        intro.offg.clearRect(0, 0, 640, 480);
                        intro.offg.drawImage(introa, 0, 40, null);
                        remove(pic);
                        add("Center", intro);
                        introg = (Graphics2D) intro.getGraphics();
                        introg.setFont(dm.scrollfont.deriveFont(20.0f));//was 18
                        introg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        introg.clearRect(0, 0, 640, 480);
                        endintro = false;
                        introthread.start();
                    }
                } else if (x > 465 && x < 520) {
                    //exit
                    System.exit(0);
                }
            } else if (x > 420 && x < 520 && y > 420 && y < 460) {
                icon.setImage(credits);
                repaint();
            }
        } else {
            icon.setImage(entranceimg);
            repaint();
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        if (icon.getImage() != entranceimg) return;
        int x = e.getX(), y = e.getY();
        int newover = -1;
        if (y < 45 && y > 20) {
            if (x > 465 && x < 520) newover = 4;
            else if (x > 325 && x < 450) newover = 3;
            else if (x > 215 && x < 300) newover = 2;
            else if (x > 120 && x < 200) newover = 1;
            else if (x > 40 && x < 100) newover = 0;
        }
        //434,409 //title scroll pic coords
        if (newover != oldover) {
            drawEntrance(newover);
            oldover = newover;
            icon.setImage(entranceimg);
            repaint();
        }
    }
    
    private void drawEntrance(int over) {
        entranceg.drawImage(entrance, 0, 0, this);
                /*
                entranceg.setColor(Color.black);
                entranceg.drawString("Enter",43,43);
                entranceg.drawString("Resume",123,43);
                entranceg.drawString("Custom",223,43);
                entranceg.drawString("View Intro",333,43);
                entranceg.drawString("Exit",473,43);
                entranceg.setColor(Color.yellow);
                entranceg.drawString("Enter",40,40);
                entranceg.drawString("Resume",120,40);
                entranceg.drawString("Custom",220,40);
                entranceg.drawString("View Intro",330,40);
                entranceg.drawString("Exit",470,40);
                */
        
        if (over < 0) {
            entranceg.drawImage(titlenoglow, 30, 13, this);
            return;
        }
        if (over == 0) {
            entranceg.setClip(110, 13, 600, 40);
            entranceg.drawImage(titlenoglow, 30, 13, this);
            entranceg.setClip(30, 13, 80, 40);
        } else if (over == 1) {
            entranceg.setClip(30, 13, 80, 40);
            entranceg.drawImage(titlenoglow, 30, 13, this);
            entranceg.setClip(210, 13, 600, 40);
            entranceg.drawImage(titlenoglow, 30, 13, this);
            entranceg.setClip(110, 13, 100, 40);
        } else if (over == 2) {
            entranceg.setClip(30, 13, 180, 40);
            entranceg.drawImage(titlenoglow, 30, 13, this);
            entranceg.setClip(314, 13, 600, 40);
            entranceg.drawImage(titlenoglow, 30, 13, this);
            entranceg.setClip(210, 13, 104, 40);
        } else if (over == 3) {
            entranceg.setClip(30, 13, 284, 40);
            entranceg.drawImage(titlenoglow, 30, 13, this);
            entranceg.setClip(459, 13, 600, 40);
            entranceg.drawImage(titlenoglow, 30, 13, this);
            entranceg.setClip(314, 13, 145, 40);
        } else if (over == 4) {
            entranceg.setClip(30, 13, 429, 40);
            entranceg.drawImage(titlenoglow, 30, 13, this);
            entranceg.setClip(459, 13, 70, 40);
        }
        entranceg.drawImage(titleglow, 30, 13, this);
        entranceg.setClip(null);
    }
    
    public void mouseDragged(MouseEvent e) {
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
