import java.io.*;

class WallSwitchData extends SidedWallData {
    
    public MapPoint xy; //point containing xy of switch
    public int type;
    public int keynumber;  //item # (including coins)
    public int keysneeded; //can take >1 (used mostly for coin slots)
    public int keysgotten = 0; //how many put in so far
    public int picklock; //how hard to pick the lock (0 if can't)
    public int picnumber; //for variety of button,key,coinslot graphics
    public int targetlevel, targetx, targety; //level and xy of target mapsquare to change
    public boolean isReusable;
    public boolean consumeskey; //true if item gone when used (coins, some keys)
    public boolean stopswitch; //true if should stop going thru switches in multiple list after this one triggers
    public boolean wasUsed;
    public boolean switchstate;
    public int actiontype; //0 - toggle, 1 - activate, 2 - deactivate, 3 - cycle act/deact, 4 - cycle deact/act, 5 - exchange
    public int delay; //delay time before works, 0 for none
    public int reset; //time until resets, 0 for no reset
    public int changecount = 0; //for delay and reset
    public boolean delaying, resetting, resetnotrigger; //set true in tryswitch if has delay/reset
    public MapData changeto;
    public MapData oldMapObject;
    public String soundstring;
    public int loopsound;
    public boolean abrupt, retainitems;
    
    //static final int BUTTON = 0;
    //static final int KEY = 1;
    //static final int COIN = 2;
    
    //constructs a wall switch
    //takes:
    //      xyp - mappoint containing level and xy of this switch
    //      side (direction party must be facing to see/use switch
    //      type - BUTTON,KEY,COIN (COIN can be used to have >1 key)
    //      keyn - item# of key
    //      keysn - # of keys needed (usually coins)
    //      pklk - picklock difficulty
    //      lvl,x,y - level and xy location in DungeonMap of what is to be changed
    //      reu - is switch reusable? true if yes, false if no
    //      cons - key destroyed when used? (would usually set to true for coin type)
    //      dly - time before activates, 0 for none
    //      rset - time until resets, 0 for no reset
    //      rnt - true if doesn't trigger switch when reset happens (so reset just makes switch inactive for a time)
    //      act - 0 - toggle, 1 - activate, 2 - deactivate, 3 - exchange
    //      chngto - mapobject to change to (null if above true)
    //      picnum - which pic to use (there are variety of keyholes and buttons)
    //      retainitems - for swap/setto types, if true old items transferred to new square
    public WallSwitchData(MapPoint xyp, int sde, int typ, int keyn, int keysn, int pklk, int lvl, int x, int y, boolean reu, boolean cons, boolean stop, int dly, int rset, boolean rnt, int act, MapData chngto, int picnum, boolean rt) {
        super(sde);
        xy = xyp;
        type = typ;
        keynumber = keyn;
        keysneeded = keysn;
        picklock = pklk;
        targetlevel = lvl;
        targetx = x;
        targety = y;
        isReusable = reu;
        consumeskey = cons;
        stopswitch = stop;
        delay = dly;
        reset = rset;
        resetnotrigger = rnt;
        actiontype = act;
        changeto = chngto;
        picnumber = picnum;
        retainitems = rt;
        mapchar = '/';
    }
    
    //constructor for load routine
    public WallSwitchData(int sde) {
        super(sde);
        mapchar = '/';
    }
    
    public void changeLevel(int amt, int lvlnum) {
        //if (amt>0) {
        if (targetlevel >= lvlnum) targetlevel += amt;
        //}
        //else {
        //if (targetlevel>=lvlnum) targetlevel+=amt;
        if (targetlevel < 0) targetlevel = 0;
        //}
        xy = new MapPoint(xy.level + amt, xy.x, xy.y);
    }
    
    public void setMapCoord(int level, int x, int y) {
        xy = new MapPoint(level, x, y);
    }
    
    public String toString() {
                /*
                StringBuffer s = new StringBuffer("WallSwitch");
                if (type==0) s.append(" (Button)");
                else if (type==1 || type==2) {
                        Item tempitem;
                        //potion
                        if (keynumber>9 && keynumber<31) {
                                tempitem = new Item(keynumber,1,1);
                        }
                        //chest
                        else if (keynumber==5) {
                                tempitem = new Chest();
                        }
                        //scroll
                        else if (keynumber==4) {
                                String[] mess = new String[5];
                                tempitem = new Item(mess);
                        }
                        //torch
                        else if (keynumber==9) {
                                tempitem = new Torch();
                        }
                        //waterskin
                        else if (keynumber==73) {
                                tempitem = new Waterskin();
                        }
                        //compass
                        else if (keynumber==8) {
                                tempitem = new Compass();
                        }
                        //everything else
                        else if (keynumber<300) tempitem = new Item(keynumber);
                        else tempitem = null;
                        if (tempitem!=null) {
                                if (type==1) s.append(" (Key - "+tempitem.name+")");
                                else s.append(" (Coin - "+tempitem.name+")");
                        }
                        else if (type==1) s.append(" (Key - Custom Item #"+(keynumber-299)+")");
                        else s.append(" (Coin - Custom Item #"+(keynumber-299)+")");
                }
                if (side==0) s.append(" Facing South");
                else if (side==1) s.append(" Facing East");
                else if (side==2) s.append(" Facing North");
                else s.append(" Facing West");
                return s.toString();
                */
                /*
                String s = "WallSwitch";
                if (type==0) s=s+" (Button)";
                else {
                        Item tempitem;
                        //potion
                        if (keynumber>9 && keynumber<31) {
                                tempitem = new Item(keynumber,1,1);
                        }
                        //chest
                        else if (keynumber==5) {
                                tempitem = new Chest();
                        }
                        //scroll
                        else if (keynumber==4) {
                                String[] mess = new String[5];
                                tempitem = new Item(mess);
                        }
                        //torch
                        else if (keynumber==9) {
                                tempitem = new Torch();
                        }
                        //waterskin
                        else if (keynumber==73) {
                                tempitem = new Waterskin();
                        }
                        //compass
                        else if (keynumber==8) {
                                tempitem = new Compass();
                        }
                        //everything else
                        else if (keynumber<300) tempitem = new Item(keynumber);
                        else tempitem = null;
                        if (tempitem!=null) {
                                if (type==1) s=s+" (Key - "+tempitem.name+")";
                                else s=s+" (Coin - "+tempitem.name+")";
                        }
                        else if (type==1) s=s+" (Key - Custom Item #"+(keynumber-299)+")";
                        else s=s+" (Coin - Custom Item #"+(keynumber-299)+")";
                }
                if (side==0) s=s+" Facing South";
                else if (side==1) s=s+" Facing East";
                else if (side==2) s=s+" Facing North";
                else s=s+" Facing West";
                return s;
                */
                /*
                String s = "WallSwitch";
                if (type==0) s=s+" (Button)";
                else if (type==1) {
                        if (keynumber>30 && keynumber<47) s+=" (Key - "+ItemWizard.keyitems[keynumber-31]+")";
                        else if (keynumber>50 && keynumber<60) s+=" (Key - "+ItemWizard.coingemitems[keynumber-51]+")";
                        else if (keynumber>281 && keynumber<286) s+=" (Key - "+ItemWizard.miscitems[keynumber-261]+")";
                        else s=s+" (Key)";
                }
                else if (keynumber>50 && keynumber<60) s=s+" (Coin - "+ItemWizard.coingemitems[keynumber-51]+")";
                else s=s+" (Coin)";
                if (side==0) s=s+" Facing South";
                else if (side==1) s=s+" Facing East";
                else if (side==2) s=s+" Facing North";
                else s=s+" Facing West";
                return s;
                */
        String s = "WallSwitch";
        if (type == 0) s = s + " (Button)";
        else {
            Item tempitem = null;
            String keyname = null;
            //potion
            if (keynumber > 9 && keynumber < 31) {
                tempitem = new Item(keynumber, 1, 1);
                keyname = tempitem.name;
            }
            //chest
            else if (keynumber == 5) {
                //tempitem = new Chest();
                keyname = "Chest";
            }
            //scroll
            else if (keynumber == 4) {
                //String[] mess = new String[5];
                //tempitem = new Item(mess);
                keyname = "Scroll";
            }
            //torch
            else if (keynumber == 9) {
                //tempitem = new Torch();
                keyname = "Torch";
            }
            //waterskin
            else if (keynumber == 73) {
                //tempitem = new Waterskin();
                keyname = "Waterskin";
            }
            //compass
            else if (keynumber == 8) {
                //tempitem = new Compass();
                keyname = "Compass";
            }
            //everything else
            else if (keynumber < 300) {
                tempitem = new Item(keynumber);
                keyname = tempitem.name;
            }
            if (keyname != null) {
                if (type == 1) s = s + " (Key - " + keyname + ")";
                else s = s + " (Coin - " + keyname + ")";
            } else if (type == 1) s = s + " (Key - Custom Item #" + (keynumber - 299) + ")";
            else s = s + " (Coin - Custom Item #" + (keynumber - 299) + ")";
        }
        if (side == 0) s = s + " Facing South";
        else if (side == 1) s = s + " Facing East";
        else if (side == 2) s = s + " Facing North";
        else s = s + " Facing West";
        return s;
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(xy);
        so.writeInt(type);
        so.writeInt(keynumber);
        so.writeInt(keysneeded);
        so.writeInt(keysgotten);
        so.writeInt(picklock);
        so.writeInt(targetlevel);
        so.writeInt(targetx);
        so.writeInt(targety);
        so.writeBoolean(isReusable);
        so.writeBoolean(consumeskey);
        so.writeBoolean(stopswitch);
        so.writeInt(delay);
        so.writeInt(reset);
        so.writeInt(picnumber);
        so.writeInt(changecount);
        so.writeBoolean(delaying);
        so.writeBoolean(resetting);
        so.writeBoolean(resetnotrigger);
        so.writeBoolean(switchstate);
        so.writeBoolean(wasUsed);
        so.writeInt(actiontype);
        //if (actiontype>=5 && actiontype<7 && (isReusable || !wasUsed)) {
        if (actiontype == 5 || actiontype == 6) {
            changeto.save(so);
            if (switchstate && actiontype == 5) oldMapObject.save(so);
            so.writeBoolean(retainitems);
        } else if (actiontype == 7) {
            so.writeUTF(soundstring);
            so.writeInt(loopsound);
        } else if (actiontype == 8) so.writeBoolean(abrupt);
    }
    
    public void load(ObjectInputStream si) throws IOException, java.lang.ClassNotFoundException {
        xy = (MapPoint) si.readObject();
        type = si.readInt();
        keynumber = si.readInt();
        keysneeded = si.readInt();
        keysgotten = si.readInt();
        picklock = si.readInt();
        targetlevel = si.readInt();
        targetx = si.readInt();
        targety = si.readInt();
        isReusable = si.readBoolean();
        consumeskey = si.readBoolean();
        stopswitch = si.readBoolean();
        delay = si.readInt();
        reset = si.readInt();
        picnumber = si.readInt();
        changecount = si.readInt();
        delaying = si.readBoolean();
        resetting = si.readBoolean();
        resetnotrigger = si.readBoolean();
        //resetnotrigger = false;
        switchstate = si.readBoolean();
        wasUsed = si.readBoolean();
        actiontype = si.readInt();
        //if (actiontype>=5 && actiontype<7 && (isReusable || !wasUsed)) {
        if (actiontype == 5 || actiontype == 6) {
            changeto = DMEditor.loadMapData(si, -1, 0, 0);
            if (switchstate && actiontype == 5) oldMapObject = DMEditor.loadMapData(si, -1, 0, 0);
            retainitems = si.readBoolean();
        } else if (actiontype == 7) {
            soundstring = si.readUTF();
            loopsound = si.readInt();
        } else if (actiontype == 8) abrupt = si.readBoolean();
    }
    
}