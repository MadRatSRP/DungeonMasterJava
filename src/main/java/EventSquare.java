import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;

public class EventSquare extends Floor implements ActionListener {
    private String[] eventtext;
    private Color textcolor;
    private int textalign, picalign;
    private Choice[] choices;
    private String picname, soundstring;
    private ImageIcon pic;
    private JLabel piclabel;
    private EventPanel eventpanel;
    private JPanel textpanel, riddlepanel;
    private JTextField riddle;
    private int choiceindex, riddlej, riddleactions, actionlimit;
    private boolean blackback, loopsound, shouldend;
    private String riddleanswer;
    private dmnew dm;
    public int eventface, movieloop;
    
    public EventSquare(dmnew dm) {
        super();
        this.dm = dm;
        mapchar = 'E';
    }
    
    public void tryTeleport() {
        if (eventface != 0 && dmnew.facing != eventface - 1) return;
        dm.nomovement = true;
        if (dm.numheroes > 0) {
            for (int i = 0; i < dm.numheroes; i++) {
                dm.hero[i].removeMouseListener(dm.hclick);
            }
            dm.spellsheet.setVisible(false);
            dm.weaponsheet.setVisible(false);
        }
        dm.arrowsheet.setVisible(false);
        if (!blackback) {
            Graphics dg = dm.dview.getGraphics();
            dm.dview.paint(dg);
            dg.dispose();
        }
        if (soundstring != null) {
            dm.playSound(soundstring, -1, -1, loopsound ? -1 : 0);
        }
        //autotrigger
        for (int i = 0; i < choices.length; i++) {
            //System.out.println(choices[i]);
            if (choices[i].autotrigger && choices[i].visible && choices[i].shouldshow) {
                //perform choice
                doChoice(i);
            }
        }
        if (dm.nomovement) {
            dm.eventpanel.add(eventpanel);
            dm.showCenter(dm.eventpanel);
            updateVisibility();
            dm.repaint();
        }
    }
    
    public void doAction() {
        if (hasParty) {
            tryTeleport();
        }
    }
    
    private void updateVisibility() {
        //set which choice buttons visible here (for needitem and needskill)
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].visible) {
                choices[i].shouldshow = true;
                if (choices[i].needitem != null) {
                    choices[i].shouldshow = false;
                    //search for item
                    int j = 0;
                    if (dm.iteminhand && dm.inhand.number == choices[i].needitem.number) choices[i].shouldshow = true;
                    while (!choices[i].shouldshow && j < dm.numheroes) {
                        if (dm.hero[j].weapon.number == choices[i].needitem.number) choices[i].shouldshow = true;
                        else if (dm.hero[j].hand != null && dm.hero[j].hand.number == choices[i].needitem.number)
                            choices[i].shouldshow = true;
                        else j++;
                    }
                }
                if (choices[i].shouldshow && choices[i].needskill != null) {
                    choices[i].shouldshow = false;
                    //test for skill
                    int skillnum = choices[i].needskill[0];
                    int skillval = choices[i].needskill[1];
                    int j = 0;
                    while (!choices[i].shouldshow && j < dm.numheroes) {
                        if (dm.hero[j].isdead) j++;
                        else if (skillnum == 0 && dm.hero[j].flevel >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 1 && dm.hero[j].nlevel >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 2 && dm.hero[j].wlevel >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 3 && dm.hero[j].plevel >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 4 && dm.hero[j].strength >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 5 && dm.hero[j].dexterity >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 6 && dm.hero[j].vitality >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 7 && dm.hero[j].intelligence >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 8 && dm.hero[j].wisdom >= skillval) choices[i].shouldshow = true;
                        else if (skillnum == 9 && ((dm.hero[j].flevel + dm.hero[j].nlevel + dm.hero[j].wlevel + dm.hero[j].plevel) / 4) >= skillval)
                            choices[i].shouldshow = true;
                        else if (skillnum == 10 && ((dm.hero[j].strength + dm.hero[j].dexterity + dm.hero[j].vitality + dm.hero[j].intelligence + dm.hero[j].wisdom) / 5) >= skillval)
                            choices[i].shouldshow = true;
                        else j++;
                    }
                }
                if (choices[i].shouldshow && choices[i].needmons >= 0) {
                    //test for monsters alive/dead
                    Enumeration locEnum = dm.dmmons.elements();
                    dmnew.Monster tempmon;
                    boolean found = false;
                    while (choices[i].shouldshow && !found && locEnum.hasMoreElements()) {
                        tempmon = (dmnew.Monster) locEnum.nextElement();
                        if (tempmon.number == choices[i].needmons && tempmon.level == choices[i].needmonslvl && !tempmon.isdying) {
                            if (choices[i].needdead) choices[i].shouldshow = false;
                            else found = true;
                        }
                    }
                    //if need live ones but didn't find any, set invisible
                    if (!choices[i].needdead && !found) choices[i].shouldshow = false;
                }
            } else choices[i].shouldshow = false;
        }
        eventpanel.updateChoices();
    }
    
    public void doChoice(int i) {
        choiceindex = i;
        //destroy item if supposed to
        if (choices[choiceindex].needitem != null && choices[choiceindex].takeitem) destroyItem(choiceindex);
        actionlimit = -1;
        shouldend = false;
        doActions(0);
        //System.out.println("did normal call: riddlej="+riddlej+", riddleactions="+riddleactions+", actionlimit="+actionlimit+", shouldend="+shouldend);
    }
    
    public void actionPerformed(ActionEvent e) {
        eventpanel.remove(riddlepanel);
        if (!riddle.getText().toLowerCase().equals(riddleanswer.toLowerCase())) {
            riddlej += riddleactions;
            //System.out.println("got it wrong");
        } else {
            actionlimit = riddleactions;
            //System.out.println("got it right");
        }
        doActions(riddlej);
    }
    
    private void doActions(int j) {
        //System.out.println("doActions called, choiceindex = "+choiceindex);
        //do actions
        Action a;
        riddlej = j;
        int i = choiceindex;
        while (!shouldend && j < choices[i].actions.size()) {
            a = (Action) choices[i].actions.get(j);
            if (actionlimit > 0) actionlimit--;
            //check action type
            if (a.actiontype == 0) {
                shouldend = true;
                //System.out.println("should end");
            } else if (a.actiontype > 0 && a.actiontype < 4) {
                //toggle,activate,or deactivate a target
                MapPoint target = (MapPoint) a.action;
                if (a.actiontype == 1) {
                    dmnew.DungeonMap[target.level][target.x][target.y].toggle();
                } else if (a.actiontype == 2) {
                    dmnew.DungeonMap[target.level][target.x][target.y].activate();
                } else { //if (a.actiontype==3) {
                    dmnew.DungeonMap[target.level][target.x][target.y].deactivate();
                }
            } else if (a.actiontype == 4) {
                //new champion joins (trade places if necessary and if player says ok)
                dmnew.Hero hero = dm.new Hero((HeroData) a.action);
                int chosen = dm.numheroes;
                hero.heronumber = chosen;
                boolean swapping = false;
                if (chosen > 3) {
                    swapping = true;
                    chosen = dm.leader;
                    //allow choose whom to swap with or cancel
                    String[] types = {dm.hero[0].name, dm.hero[1].name, dm.hero[2].name, dm.hero[3].name};
                    Object[] cs = new Object[3];
                    cs[0] = new JComboBox(types);
                    cs[1] = "Ok";
                    cs[2] = "Cancel";
                    ((JComboBox) cs[0]).setSelectedIndex(chosen);
                    int returnval = JOptionPane.showOptionDialog(dm, "Swap With Whom?", "Trade Champions", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, cs, cs[1]);
                    if (returnval == 2 || returnval == JOptionPane.CLOSED_OPTION) return;
                    chosen = ((JComboBox) cs[0]).getSelectedIndex();
                    hero.subsquare = dm.hero[chosen].subsquare;
                    hero.heronumber = chosen;
                } else {
                    //find free subsquare
                    while (dm.heroatsub[hero.subsquare] != -1) {
                        hero.subsquare = (hero.subsquare + 1) % 4;
                    }
                }
                //swapping
                if (swapping) {
                    //check for illumlet on old champ
                    if (dm.hero[chosen].neck != null && dm.hero[chosen].neck.number == 89)
                        dm.numillumlets--;//dm.leveldark-=60;
                    dmnew.Hero temphero = dm.hero[chosen];
                    dm.hero[chosen] = hero;
                    dm.heroatsub[hero.subsquare] = chosen;
                    hero = temphero;
                    dm.hero[chosen].isleader = hero.isleader;
                    dm.hpanel.removeAll();
                    for (int k = 0; k < dm.numheroes; k++) {
                        dm.hpanel.add(dm.hero[k]);
                    }
                    a.action = new HeroData(hero);
                    //a.reusable = -1; //make swap reusable?
                } else {
                    dm.hero[chosen] = hero;
                    dm.heroatsub[hero.subsquare] = chosen;
                    dm.numheroes++;
                    dm.hpanel.add(dm.hero[chosen]);
                    if (dm.numheroes == 1) {
                        if (dm.spellsheet != null) {
                            //dm.weaponsheet.weaponButton[0].setEnabled(true);
                            if (dm.weaponsheet.showingspecials) dm.weaponsheet.toggleSpecials(0);
                            //dm.weaponsheet.weaponButton[0].doClick();
                        } else {
                            dm.spellsheet = dm.new SpellSheet();
                            dm.weaponsheet = dm.new WeaponSheet();
                            dm.spellsheet.setVisible(false);
                            dm.weaponsheet.setVisible(false);
                        }
                        dm.hero[chosen].isleader = true;
                        dm.leader = chosen;
                        dm.eastpanel.removeAll();
                        dm.eastpanel.add(dm.ecpanel);
                        dm.eastpanel.add(Box.createVerticalStrut(10));
                        dm.eastpanel.add(dm.spellsheet);
                        dm.eastpanel.add(Box.createVerticalStrut(20));
                        dm.eastpanel.add(dm.weaponsheet);
                        dm.eastpanel.add(Box.createVerticalStrut(10));
                        dm.eastpanel.add(dm.arrowsheet);
                        dm.herosheet.removeMouseListener(dm.sheetclick);
                        dm.herosheet.addMouseListener(dm.sheetclick);
                    }
                    a.reusable = 0; //enforce non-reusable champion join (can only be reusable if swapping)
                }
                dm.formation.addNewHero();
                //check for illumlet on new champ
                if (dm.hero[chosen].neck != null && dm.hero[chosen].neck.number == 89)
                    dm.numillumlets++;//dm.leveldark+=60;
                //dm.weaponsheet.update();
                dm.spellsheet.repaint();
                dm.weaponsheet.repaint();
                dm.hupdate();
                dm.updateDark();//in case has any torches in hand
                dm.hpanel.validate();
                dm.message.setMessage(dm.hero[chosen].name + " joins the party.", chosen);
            } else if (a.actiontype == 5) {
                //give item to party (copy item in case reusable)
                if (!dm.iteminhand && dm.numheroes > 0) {
                    //put it in cursor hand if possible
                    dm.inhand = Item.createCopy((Item) a.action);
                    dm.hero[dm.leader].load += dm.inhand.weight;
                    dm.iteminhand = true;
                    dm.changeCursor();
                } else {
                    //else put it on the ground in front and to left of party
                    ((Item) a.action).subsquare = (-dm.facing + 4) % 4;
                    addItem(Item.createCopy((Item) a.action));
                }
            } else if (a.actiontype == 6) {
                //heal party
                int health = ((MapPoint) a.action).level;
                int stamina = ((MapPoint) a.action).x;
                int mana = ((MapPoint) a.action).y;
                for (int k = 0; k < dm.numheroes; k++) {
                    if (!dm.hero[k].isdead) {
                        dm.hero[k].heal(dm.hero[k].maxhealth * health);
                        dm.hero[k].vitalize(dm.hero[k].maxstamina * stamina);
                        int managain = dm.hero[k].maxmana * mana;
                        if (dm.hero[k].mana + managain > dm.hero[k].maxmana)
                            managain = dm.hero[k].maxmana - dm.hero[k].mana;
                        dm.hero[k].energize(managain);
                        dm.hero[k].repaint();
                    }
                }
            } else if (a.actiontype == 7) {
                //change text
                eventtext = (String[]) a.action;
            } else if (a.actiontype == 8) {
                //change picture
                picname = (String) a.action;
                pic = new ImageIcon("Events" + File.separator + picname);
                eventpanel.setImage(pic.getImage());
            } else if (a.actiontype == 9) {
                //play sound
                String sm = (String) a.action;
                if (sm.toLowerCase().endsWith("wav")) dm.playSound(sm, -1, -1); //no looping possible
            } else if (a.actiontype == 10 || a.actiontype == 11) {
                //set choice visible/invisible
                int[] targets = (int[]) a.action;
                for (int k = 0; k < targets.length; k++) {
                    if (choices[targets[k]].actions.size() > 0) { //only affects choices that still have actions
                        //System.out.println("set visible = "+(a.actiontype==10)+", target = "+targets[k]+" which is "+choices[targets[k]]);
                        choices[targets[k]].visible = (a.actiontype == 10); //10 is set visible, 11 is set invisible
                    }
                }
            } else if (a.actiontype == 12) {
                //learn special ability
                if (dm.numheroes > 0) {
                    int chosen = dm.leader;
                    boolean nolearn = true;
                    SpecialAbility special = (SpecialAbility) a.action;
                    while (nolearn) {
                        if (dm.numheroes > 1) {
                            //allow choose who will learn
                            String[] types = new String[dm.numheroes];
                            for (int k = 0; k < dm.numheroes; k++) types[k] = dm.hero[k].name;
                            Object[] cs = new Object[3];
                            cs[0] = new JComboBox(types);
                            cs[1] = "Ok";
                            cs[2] = "Pass";
                            ((JComboBox) cs[0]).setSelectedIndex(chosen);
                            int returnval = JOptionPane.showOptionDialog(dm, "Who Will Learn?", "Gain Special Ability", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, cs, cs[1]);
                            if (returnval == 2) break;
                            chosen = ((JComboBox) cs[0]).getSelectedIndex();
                        }
                        if (dm.hero[chosen].abilities == null) {
                            //System.out.println("was null, now set to "+(SpecialAbility)a.action);
                            dm.hero[chosen].abilities = new SpecialAbility[1];
                            dm.hero[chosen].abilities[0] = new SpecialAbility(special);
                            nolearn = false;
                        } else {
                            //System.out.println("was not null, added to end");
                            SpecialAbility[] newabilities = new SpecialAbility[dm.hero[chosen].abilities.length + 1];
                            int k = 0;
                            nolearn = false;
                            while (!nolearn && k < newabilities.length - 1) {
                                newabilities[k] = dm.hero[chosen].abilities[k];
                                if (newabilities[k].equals(special)) nolearn = true;
                                else k++;
                            }
                            if (!nolearn) {
                                newabilities[newabilities.length - 1] = new SpecialAbility(special);
                                dm.hero[chosen].abilities = newabilities;
                            } else dm.message.setMessage(dm.hero[chosen].name + " already has that ability.", 4);
                        }
                    }
                    if (!nolearn) {
                        dm.message.setMessage(dm.hero[chosen].name + " gains ability \"" + special.name + "\".", dm.hero[chosen].heronumber);
                        if (special.flevelneed <= dm.hero[chosen].flevel && special.nlevelneed <= dm.hero[chosen].nlevel && special.wlevelneed <= dm.hero[chosen].wlevel && special.plevelneed <= dm.hero[chosen].plevel) {
                        } else
                            dm.message.setMessage("(" + dm.hero[chosen].name + " needs more experience before it can be used.)", 4);
                    } else if (a.reusable > 0) a.reusable++; //passed, so don't decrement reusable
                }
            } else if (a.actiontype == 14) {
                //change text color
                textcolor = (Color) a.action;
            } else if (a.actiontype == 15) {
                //change text alignment
                textalign = ((Integer) a.action).intValue();
            } else if (a.actiontype == 16) {
                //change pic alignment
                picalign = ((Integer) a.action).intValue();
                eventpanel.setVerticalAlignment(picalign);
            } else if (a.actiontype == 17) {
                //gain experience
                for (int k = 0; k < dm.numheroes; k++) {
                    if (!dm.hero[k].isdead) {
                        dm.hero[k].gainxp('f', ((int[]) a.action)[0]);
                        dm.hero[k].gainxp('n', ((int[]) a.action)[1]);
                        dm.hero[k].gainxp('w', ((int[]) a.action)[2]);
                        dm.hero[k].gainxp('p', ((int[]) a.action)[3]);
                    }
                }
            }
            if (a.reusable == 0) choices[i].actions.remove(j);
            else {
                j++;
                riddlej++;
                if (a.reusable > 0) a.reusable--;
            }
            //put here because getting wrong ends action iteration, reusable is done first
            if (a.actiontype == 13) {
                //riddle
                riddleanswer = (String) a.action;
                String num = riddleanswer.substring(riddleanswer.lastIndexOf('~') + 1);
                riddleanswer = riddleanswer.substring(0, riddleanswer.lastIndexOf('~'));
                riddleactions = Integer.parseInt(num);
                eventpanel.add(riddlepanel);
                eventpanel.validate();
                eventpanel.repaint();
                return;
            }
            //end action iteration, but not event, put here so reusable is done first
            if (a.actiontype == 18 || actionlimit == 0) break;
        }
        //if no more actions, set invisible
        if (choices[i].actions.size() == 0) {
            choices[i].visible = false;
        }
        updateVisibility();
        //return unless end event action was done
        if (!shouldend) {
            eventpanel.repaint();
            return;
        }
        //dismiss event panel
        if (dm.sheet) dm.showCenter(dm.herosheet);
        else dm.showCenter(dm.dview);
        dm.eventpanel.remove(eventpanel);
        if (dm.numheroes > 0) {
            dm.spellsheet.setVisible(true);
            dm.weaponsheet.setVisible(true);
        }
        dm.arrowsheet.setVisible(true);
        for (j = 0; j < dm.numheroes; j++) {
            if (!dm.hero[j].isdead) dm.hero[j].addMouseListener(dm.hclick);
        }
        if (soundstring != null && loopsound) {
            LoopSound sound;
            boolean found = false;
            int k = 0;
            while (!found) {
                sound = (LoopSound) dm.loopsounds.get(k);
                if (sound.clipfile.equals(soundstring)) {
                    dm.loopsounds.remove(k);
                    sound.clip.stop();
                    sound.clip.close();
                    found = true;
                } else k++;
            }
        }
        dm.nomovement = false;
        dm.requestFocusInWindow();
    }
    
    private void destroyItem(int i) {
        boolean found = false;
        //search for item
        if (dm.iteminhand && dm.inhand.number == choices[i].needitem.number) {
            dm.iteminhand = false;
            dm.hero[dm.leader].load -= dm.inhand.weight;
            dm.changeCursor();
            found = true;
        }
        int j = 0;
        while (!found && j < dm.numheroes) {
            if (dm.hero[j].weapon.number == choices[i].needitem.number) {
                if (dm.hero[j].weapon.type == Item.WEAPON || dm.hero[j].weapon.type == Item.SHIELD || dm.hero[j].weapon.number == 4)
                    dm.hero[j].weapon.unEquipEffect(dm.hero[j]);
                dm.hero[j].load -= dm.hero[j].weapon.weight;
                dm.hero[j].weapon = dm.fistfoot;
                dm.hero[j].repaint();
                dm.weaponsheet.repaint();
                found = true;
            } else if (dm.hero[j].hand != null && dm.hero[j].hand.number == choices[i].needitem.number) {
                if (dm.hero[j].hand.type == Item.SHIELD) dm.hero[j].hand.unEquipEffect(dm.hero[j]);
                dm.hero[j].load -= dm.hero[j].hand.weight;
                dm.hero[j].hand = null;
                dm.hero[j].repaint();
                found = true;
            } else j++;
        }
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(choices.length);
        if (choices.length > 0) {
            so.writeInt(eventface);
            if (picname != null) {
                so.writeBoolean(true);
                so.writeUTF(picname);
                so.writeInt(picalign);
            } else so.writeBoolean(false);
            so.writeBoolean(blackback);
            so.writeInt(textcolor.getRed());
            so.writeInt(textcolor.getGreen());
            so.writeInt(textcolor.getBlue());
            so.writeInt(textalign);
            so.writeObject(eventtext);
            if (soundstring != null) {
                so.writeBoolean(true);
                so.writeUTF(soundstring);
                so.writeBoolean(loopsound);
            } else so.writeBoolean(false);
            for (int i = 0; i < choices.length; i++) {
                choices[i].save(so);
            }
        }
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        int numchoices = si.readInt();
        if (numchoices > 0) {
            eventpanel = new EventPanel();
            eventface = si.readInt();
            if (si.readBoolean()) {
                picname = si.readUTF();
                picalign = si.readInt();
                pic = new ImageIcon("Events" + File.separator + picname);
                eventpanel.setImage(pic.getImage());
                eventpanel.setVerticalAlignment(picalign);
            }
            blackback = si.readBoolean();
            textcolor = new Color(si.readInt(), si.readInt(), si.readInt());
            textalign = si.readInt();
            eventtext = (String[]) si.readObject();
            if (si.readBoolean()) {
                soundstring = si.readUTF();
                loopsound = si.readBoolean();
            }
            choices = new Choice[numchoices];
            for (int i = 0; i < numchoices; i++) {
                choices[i] = new Choice(si.readUTF());
                choices[i].load(si);
            }
            riddle = new JTextField(20);
            riddle.addActionListener(this);
            JButton riddleokbut = new JButton("Ok");
            riddleokbut.addActionListener(this);
            riddlepanel = new JPanel();
            riddlepanel.add(riddle);
            riddlepanel.add(riddleokbut);
            riddlepanel.setPreferredSize(new Dimension(448, 34));
            riddlepanel.setMaximumSize(new Dimension(448, 34));
            riddlepanel.setOpaque(false);
        }
    }
    
    protected class EventPanel extends ImageTilePanel implements MouseListener {
        private static final int SPACELEN = 8;
        private int rows = 0;
        private int[][] choiceindex = new int[10][20];
        private int[] rowlength = new int[10];
        private int[] cols = new int[10];
        
        public EventPanel() {
            super();
            setSize(448, 326);
            setPreferredSize(new Dimension(448, 326));
            setMaximumSize(new Dimension(448, 326));
            setMinimumSize(new Dimension(448, 326));
            setTiled(false);
            addMouseListener(this);
        }
        
        public void updateChoices() {
            //System.out.println("update choices called");
            Graphics2D g = (Graphics2D) getGraphics();
            g.setFont(dmnew.scrollfont);
            if (dm.TEXTANTIALIAS)
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            rows = 0;
            cols[0] = 0;
            rowlength[0] = 0;
            int choicelength = 0;
            for (int i = 0; i < choices.length; i++) {
                if (choices[i].shouldshow) {
                    int len = g.getFontMetrics().stringWidth(choices[i].choicename);
                    if (choicelength != 0) choicelength += SPACELEN;
                    choicelength += len;
                    if (choicelength > 448) {
                        rowlength[rows] = choicelength - len - SPACELEN;
                        rows++;
                        choicelength = len;
                        cols[rows] = 0;
                    }
                    choiceindex[rows][cols[rows]] = i;
                    cols[rows]++;
                }
            }
            rowlength[rows] = choicelength;
            
            g.dispose();
        }
        
        public void paint(Graphics g) {
            //System.out.println("paint called");
            if (!blackback) g.drawImage(dm.dview.offscreen, 0, 0, this);
            super.paint(g);
            
            g.setFont(dmnew.scrollfont);
            if (dm.TEXTANTIALIAS)
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            if (!riddlepanel.isShowing()) {
                //draw visible choices
                int ypos = 322 - rows * 22;
                for (int i = 0; i <= rows; i++) {
                    int xpos = 224 - rowlength[i] / 2;
                    for (int j = 0; j < cols[i]; j++) {
                        int len = g.getFontMetrics().stringWidth(choices[choiceindex[i][j]].choicename);
                        g.setColor(new Color(80, 80, 80));
                        g.fill3DRect(xpos - 2, ypos - 16, len + 4, 20, true);
                        g.setColor(Color.yellow);
                        g.drawString(choices[choiceindex[i][j]].choicename, xpos, ypos);
                        xpos += len + SPACELEN;
                    }
                    ypos += 22;
                }
            }
            
            if (eventtext != null) {
                //draw current event text
                g.setColor(textcolor);
                int ypos;
                if (textalign == 0) ypos = getSize().height - rows * 22 - eventtext.length * 20; //bottom
                else if (textalign == 1) ypos = (getSize().height - rows * 22) / 2 - eventtext.length * 10; //center
                else ypos = 20; //top
                for (int i = 0; i < eventtext.length; i++) {
                    g.drawString(eventtext[i], getSize().width / 2 - g.getFontMetrics().stringWidth(eventtext[i]) / 2, ypos + i * 20);
                }
            }
        }
        
        public void mousePressed(MouseEvent e) {
            int x = e.getX(), y = e.getY();
            //System.out.println("click at "+x+","+y);
            y = rows - (326 - y) / 22;
            if (y < 0) return;
            
            Graphics2D g = (Graphics2D) getGraphics();
            g.setFont(dmnew.scrollfont);
            if (dm.TEXTANTIALIAS)
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int xpos = 224 - rowlength[y] / 2;
            if (x < xpos) return;
            boolean found = false;
            for (int j = 0; !found && j < cols[y]; j++) {
                int len = g.getFontMetrics().stringWidth(choices[choiceindex[y][j]].choicename);
                xpos += len + SPACELEN / 2;
                if (x < xpos) {
                    x = j;
                    found = true;
                } else xpos += SPACELEN / 2;
            }
            g.dispose();
            if (found) {
                //System.out.println("x,y = "+x+","+y);
                //System.out.println(choices[choiceindex[y][x]].choicename);
                doChoice(choiceindex[y][x]);
            }
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
    
}
