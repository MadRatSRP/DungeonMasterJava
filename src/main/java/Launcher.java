import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Launcher extends SidedWall2 {
    static public boolean ADDEDPICS;
    private int type, spnumber, power, style, castpower;
    private Item it;
    private int level, xcoord, ycoord;
    public int ammocount = -1;//neg # = unlimited ammo
    public int ammocount2;
    private boolean noprojend;
    private static dmnew dmapp;
    private int shootrate; //0 means not continuous
    public int shootcounter = 0;
    public boolean isShooting;
    
    //lvl,xc,yc - level,xcoord,ycoord of this launcher
    //dm - dmnew app
    //s - dir must face to see(hole on opp side)
    //t - type: 0-spell,else item
    //p - power: spellpower or shotpow
    //cp - cast power (like intelligence) for spells and bombs
    //sty - style: 0-both holes,1-left hole,2-right hole,3-random hole
    //npe - if true, projs don't fade away
    //sr - shootrate
    //am - ammocount (-1 for unlimited)
    //am2 - # ammo for continuous to reset to when run out and shut off (0 for no reset)
    //sc - counter value for continuous launchers
    //issh - is it shooting now?
    public Launcher(int lvl, int xc, int yc, dmnew dm, int s, int t, int p, int cp, int sty, boolean npe, int sr, int am, int am2, int sc, boolean issh) {
        super(s);
        dmapp = dm;
        level = lvl;
        xcoord = xc;
        ycoord = yc;
        mapchar = 'l';
        type = t;
        power = p;
        castpower = cp;
        style = sty;
        noprojend = npe;
        shootrate = sr;
        ammocount = am;
        ammocount2 = am2;
        shootcounter = sc;
        isShooting = issh;
        setPics();
    }
    
    protected void setPics() {
        super.setPics();
        facingside[0] = loadPic("launcher1.gif");
        facingside[1] = loadPic("launcher2.gif");
        facingside[2] = loadPic("launcher3.gif");
        col1pic[0] = loadPic("launchercol11.gif");
        col1pic[1] = loadPic("launchercol12.gif");
        col1pic[2] = loadPic("launchercol13.gif");
        col3pic[0] = loadPic("launchercol31.gif");
        col3pic[1] = loadPic("launchercol32.gif");
        col3pic[2] = loadPic("launchercol33.gif");
        if (!ADDEDPICS) {
            tracker.addImage(facingside[0], 0);
            tracker.addImage(facingside[1], 0);
            tracker.addImage(facingside[2], 0);
            tracker.addImage(col1pic[0], 0);
            tracker.addImage(col1pic[1], 0);
            tracker.addImage(col1pic[2], 0);
            tracker.addImage(col3pic[0], 0);
            tracker.addImage(col3pic[1], 0);
            tracker.addImage(col3pic[2], 0);
            ADDEDPICS = true;
        }
        //xy for facing
        xadjust[0] = 58;
        yadjust[0] = 86;
        xadjust[1] = 40;
        yadjust[1] = 55;
        xadjust[2] = 34;
        yadjust[2] = 41;
        //xy for col1
        xadjust[3] = 79;
        yadjust[3] = 82;
        xadjust[4] = 127;
        yadjust[4] = 52;
        xadjust[5] = 154;
        yadjust[5] = 38;
        //xy for col3
        xadjust[6] = 2;
        yadjust[6] = 82;
        xadjust[7] = 2;
        yadjust[7] = 52;
        xadjust[8] = 1;
        yadjust[8] = 38;
    }
    
    //called by switches:
    public void toggle() {
        if (shootrate > 0) {
            if (isShooting) {
                //if (dmapp.mapstochange.size()==1) dmapp.mapchanging=false;
                isShooting = false;
                shootcounter = 0;
            } else { //activate
                //dmapp.mapchanging = true;
                isShooting = true;
                shoot();
            }
        } else shoot();
    }
    
    public void activate() {
        if (shootrate == 0 || !isShooting) toggle();
    }
    
    public void deactivate() {
        if (isShooting) toggle();
    }
    
    
    public void shoot() {
        if (ammocount == 0) return; //out of ammo
        int dir, subsq1, subsq2;
        if (side < 2) dir = side + 2;
        else dir = side - 2;
        if (dir == 0) {
            subsq1 = 0;
            subsq2 = 1;
        } else if (dir == 1) {
            subsq1 = 3;
            subsq2 = 0;
        } else if (dir == 2) {
            subsq1 = 2;
            subsq2 = 3;
        } else {
            subsq1 = 1;
            subsq2 = 2;
        }
        subsq1 = (subsq1 + dmnew.facing) % 4;
        subsq2 = (subsq2 + dmnew.facing) % 4;
        //determine which hole(s) should fire
        boolean shootleft = false, shootright = false;
        if (style == 0) {
            shootleft = true;
            shootright = true;
        } else if (style == 1) shootleft = true;
        else if (style == 2) shootright = true;
        else if (dmnew.randGen.nextBoolean()) shootleft = true;
        else shootright = true;
        dmnew.Projectile p;
        if (type == 0) {
            try {
                Spell spell = new Spell("" + (power + 1) + "" + spnumber);
                //spell.power = dmnew.randGen.nextInt()%10+spell.gain*power+spell.power;
                if (spell.number != 461 && spell.number != 363 && spell.number != 362 && spell.number != 664 && spell.number != 523) {
                    for (int j = spell.gain - 1; j >= 0; j--) {
                        spell.powers[j] += dmnew.randGen.nextInt() % 10 + j * castpower / 8;
                        if (spell.powers[j] < 1) spell.powers[j] = dmnew.randGen.nextInt(4) + 1;
                    }
                    spell.power = spell.powers[spell.gain - 1];
                }
                if (shootleft) {
                    if (noprojend) p = dmapp.new Projectile(spell, level, xcoord, ycoord, 100, dir, subsq1);
                    else p = dmapp.new Projectile(spell, level, xcoord, ycoord, spell.dist * 4 / 3, dir, subsq1);
                } else {
                    if (noprojend) p = dmapp.new Projectile(spell, level, xcoord, ycoord, 100, dir, subsq2);
                    else p = dmapp.new Projectile(spell, level, xcoord, ycoord, spell.dist * 4 / 3, dir, subsq2);
                }
                if (ammocount > 0) ammocount--;
                if (ammocount != 0 && shootright && shootleft) {
                    spell = new Spell("" + (power + 1) + "" + spnumber);
                    //spell.power = dmnew.randGen.nextInt()%10+spell.gain*power+spell.power;
                    if (spell.number != 461 && spell.number != 363 && spell.number != 362 && spell.number != 664 && spell.number != 523) {
                        for (int j = spell.gain - 1; j >= 0; j--) {
                            spell.powers[j] += dmnew.randGen.nextInt() % 10 + j * castpower / 8;
                            if (spell.powers[j] < 1) spell.powers[j] = dmnew.randGen.nextInt(4) + 1;
                        }
                        spell.power = spell.powers[spell.gain - 1];
                    }
                    if (noprojend) p = dmapp.new Projectile(spell, level, xcoord, ycoord, 100, dir, subsq2);
                    else p = dmapp.new Projectile(spell, level, xcoord, ycoord, spell.dist * 4 / 3, dir, subsq2);
                    if (ammocount > 0) ammocount--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Item tempitem = Item.createCopy(it);
            tempitem.shotpow = power;
            boolean spelldone = false;
            if (tempitem.isbomb) {
                Spell tempspell;
                try {
                    tempspell = new Spell(tempitem.bombnum);
                    //tempspell.power = tempitem.potionpow + dmnew.randGen.nextInt()%10;
                    for (int j = tempspell.gain - 1; j >= 0; j--) {
                        tempspell.powers[j] += dmnew.randGen.nextInt() % 10 + j * castpower / 8;
                        if (tempspell.powers[j] < 1) tempspell.powers[j] = dmnew.randGen.nextInt(4) + 1;
                    }
                    tempspell.power = tempspell.powers[tempspell.gain - 1];
                    if (shootleft) {
                        if (noprojend) p = dmapp.new Projectile(tempspell, level, xcoord, ycoord, 100, dir, subsq1);
                        else p = dmapp.new Projectile(tempspell, level, xcoord, ycoord, power * 3, dir, subsq1);
                    } else {
                        if (noprojend) p = dmapp.new Projectile(tempspell, level, xcoord, ycoord, 100, dir, subsq2);
                        else p = dmapp.new Projectile(tempspell, level, xcoord, ycoord, power * 3, dir, subsq2);
                    }
                    spelldone = true;
                } catch (Exception e) {
                }
            }
            if (!spelldone && shootleft) {
                if (noprojend) p = dmapp.new Projectile(tempitem, level, xcoord, ycoord, 100, dir, subsq1);
                else p = dmapp.new Projectile(tempitem, level, xcoord, ycoord, power * 3, dir, subsq1);
            } else if (!spelldone) {
                if (noprojend) p = dmapp.new Projectile(tempitem, level, xcoord, ycoord, 100, dir, subsq2);
                else p = dmapp.new Projectile(tempitem, level, xcoord, ycoord, power * 3, dir, subsq2);
            }
            if (ammocount > 0) ammocount--;
            if (ammocount != 0 && shootright && shootleft) {
                spelldone = false;
                if (tempitem.isbomb) {
                    Spell tempspell;
                    try {
                        tempspell = new Spell(tempitem.bombnum);
                        //tempspell.power = tempitem.potionpow + dmnew.randGen.nextInt()%10;
                        for (int j = tempspell.gain - 1; j >= 0; j--) {
                            tempspell.powers[j] += dmnew.randGen.nextInt() % 10 + j * castpower / 8;
                            if (tempspell.powers[j] < 1) tempspell.powers[j] = dmnew.randGen.nextInt(4) + 1;
                        }
                        tempspell.power = tempspell.powers[tempspell.gain - 1];
                        if (noprojend) p = dmapp.new Projectile(tempspell, level, xcoord, ycoord, 100, dir, subsq2);
                        else p = dmapp.new Projectile(tempspell, level, xcoord, ycoord, power * 3, dir, subsq2);
                        spelldone = true;
                    } catch (Exception e) {
                    }
                }
                if (!spelldone) {
                    tempitem = Item.createCopy(it);
                    tempitem.shotpow = power;
                    if (noprojend) p = dmapp.new Projectile(tempitem, level, xcoord, ycoord, 100, dir, subsq2);
                    else p = dmapp.new Projectile(tempitem, level, xcoord, ycoord, power * 3, dir, subsq2);
                }
                if (ammocount > 0) ammocount--;
            }
        }
        if (ammocount == 0 && ammocount2 > 0) {
            ammocount = ammocount2;
            isShooting = false;
        }
        if (level == dmnew.level) {
            int xdist = xcoord - dmapp.partyx;
            if (xdist < 0) xdist *= -1;
            int ydist = ycoord - dmapp.partyy;
            if (ydist < 0) ydist *= -1;
            if (xdist < 5 && ydist < 5) dmapp.needredraw = true;
        }
    }
    
    public boolean changeState() {
        if (isShooting) {
            shootcounter++;
            if (shootcounter > shootrate) {
                shoot();
                shootcounter = 0;
            }
        }
        return true;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(level);
        so.writeInt(xcoord);
        so.writeInt(ycoord);
        so.writeInt(type);
        so.writeInt(power);
        so.writeInt(castpower);
        so.writeInt(style);
        so.writeBoolean(noprojend);
        so.writeInt(shootrate);
        so.writeInt(ammocount);
        so.writeInt(ammocount2);
        so.writeInt(shootcounter);
        so.writeBoolean(isShooting);
        if (type == 0) so.writeInt(spnumber);
        else so.writeObject(it);
    }
    
    public void load(ObjectInputStream si) throws IOException, ClassNotFoundException {
        if (type == 0) spnumber = si.readInt();
        else it = (Item) si.readObject();
    }
    
}
