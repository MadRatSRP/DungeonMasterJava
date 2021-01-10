//need do lock picks #71 - success depends on ninja level and dexterity
//(need in door as well, but only if iron key)

//switch types:
//always works
//works only once
//button, key, coin (like key, but takes >1 item)
//can use up the key/coin or not

//timers:
//resets after certain time
//delay before works
//both of above

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class WallSwitch extends SidedWall2 {
    static public boolean[] ADDEDBUTTON = new boolean[11];//for now
    static public boolean[] ADDEDKEY = new boolean[23];
    static public boolean ADDEDCOIN;
    protected MapPoint xy; //point containing xy of switch
    protected MapPoint target; //point containing xy of target square
    protected int type;
    protected int keynumber;  //item # (including coins)
    protected int keysneeded; //can take >1 (used mostly for coin slots)
    protected int keysgotten = 0; //necessary for reusable coin slots
    protected int picklock = 0; //for setting lock pick difficulty (0 - can't pick)
    public int picnumber; //for variety of button,key,coinslot graphics
    private int targetlevel, targetx, targety; //level and xy of target mapsquare to change
    protected boolean isReusable;
    protected boolean consumeskey; //true if item gone when used (coins, some keys)
    public boolean stopswitch; //true if should stop going thru switches in multiple list after this one triggers
    protected boolean wasUsed;
    protected boolean switchstate;
    private int actiontype; //0 - toggle, 1 - activate, 2 - deactivate, 3 - cycle act/deact, 4 - cycle deact/act, 5 - exchange
    protected int delay; //delay time before works, 0 for none
    protected int reset; //time until resets, 0 for no reset
    protected int changecount = 0; //for delay and reset
    public boolean delaying, resetting, resetnotrigger; //set true in tryswitch if has delay/reset
    private MapObject changeto;
    private MapObject oldMapObject;
    private String soundstring;
    private int loopsound;
    private boolean abrupt, retainitems;
    protected java.awt.Image[] pushed;
    
    static final int BUTTON = 0;
    static final int KEY = 1;
    static final int COIN = 2;

        /*
        //constructs a wall switch
        //takes:
        //      xyp - mappoint containing level and xy of this switch
        //      side (direction party must be facing to see/use switch
        //      type - BUTTON,KEY,COIN (COIN can be used to have >1 key)
        //      keyn - item# of key
        //      keysn - # of keys needed (usually coins)
        //      lvl,x,y - level and xy location in DungeonMap of what is to be changed
        //      reu - is switch reusable? true if yes, false if no
        //      cons - key destroyed when used? (would usually set to true for coin type)
        //      dly - time before activates, 0 for none
        //      rset - time until resets, 0 for no reset
        //      chngto - mapobject to change to
        //      picnum - which pic to use (there are variety of keyholes and buttons)
        public WallSwitch(MapPoint xyp,int sde,int typ,int keyn,int keysn,int lvl,int x,int y,boolean reu,boolean cons,int dly,int rset,MapObject chngto,int picnum) {
                super(sde);
                xy = xyp;
                type = typ;
                keynumber = keyn;
                keysneeded = keysn;
                targetlevel = lvl;
                targetx = x;
                targety = y;
                isReusable = reu;
                consumeskey = cons;
                delay = dly;
                reset = rset;
                changeto = chngto;
                picnumber = picnum;
                mapchar = '/';
                setPics();
        }
        //constructor for switch that opens a door or triggers a proj launcher
        public WallSwitch(MapPoint xyp,int sde,int typ,int keyn,int keysn,int lvl,int x,int y,boolean reu,boolean cons,int dly,int rset,int picnum) {
                super(sde);
                xy = xyp;
                type = typ;
                keynumber = keyn;
                keysneeded = keysn;
                targetlevel = lvl;
                targetx = x;
                targety = y;
                isReusable = reu;
                consumeskey = cons;
                delay = dly;
                reset = rset;
                activates = true;
                picnumber = picnum;
                mapchar = '/';
                setPics();
        }
        */
    
    //constructor for load routine
    public WallSwitch(int sde) {
        super(sde);
        mapchar = '/';
    }
    
    //constructor for use by multwallswitch
    protected WallSwitch(MapPoint xyp, int sde, int typ, int keyn, int keysn, boolean reu, boolean cons, int dly, int rset, int picnum) {
        super(sde);
        xy = xyp;
        type = typ;
        keynumber = keyn;
        keysneeded = keysn;
        isReusable = reu;
        consumeskey = cons;
        delay = dly;
        reset = rset;
        picnumber = picnum;
        setPics();
    }
    
    public void tryWallSwitch(int x, int y) {
        if (type != BUTTON || dmnew.facing != side || changecount != 0 || delaying || resetting || (!isReusable && wasUsed))
            return;
        //old test: if (x>190 && x<256 && y>88 && y<105) {
        if ((x == -1 && y == -1) || (x > (64 + xadjust[0]) && y > (22 + yadjust[0]) && x < (64 + xadjust[0] + facingside[0].getWidth(null)) && y < (22 + yadjust[0] + facingside[0].getHeight(null)))) {
            //sound
            //try {
            //     AudioClip tempclip = Applet.newAudioClip(new URL(dmnew.soundbase,"switch.wav"));
            //     tempclip.play();
            //}
            //catch (MalformedURLException e) { e.printStackTrace(); }
            if (xy.level == dmnew.level && (x != -1 || y != -1)) dmnew.playSound("switch.wav", xy.x, xy.y);
            if (delay > 0) delaying = true;
            else if (actiontype == 0) {
                dmnew.DungeonMap[targetlevel][targetx][targety].toggle();
                switchstate = !switchstate;
            } else if (actiontype == 1) {
                dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                switchstate = !switchstate;
            } else if (actiontype == 2) {
                dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                switchstate = !switchstate;
            } else if (actiontype == 3) {
                if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                else dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                switchstate = !switchstate;
            } else if (actiontype == 4) {
                if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                else dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                switchstate = !switchstate;
            } else if (actiontype == 5) {
                if (!switchstate) {
                    if (!changeto.isPassable || changeto.mapchar == '>') {
                        if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                        else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                            int i = 0;
                            dmnew.Monster tempmon;
                            while (i < 6 && !delaying) {
                                tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                                if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                                else if (i == 3) i = 5;
                                else i++;
                            }
                        }
                    }
                    if (!delaying && !changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                        delaying = true;
                    if (!delaying) {
                        oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                        dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                        switchstate = !switchstate;
                        if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                        if (oldMapObject.hasCloud) {
                            oldMapObject.hasCloud = false;
                            changeto.hasCloud = true;
                        }
                        if (oldMapObject.numProjs > 0) {
                            changeto.numProjs = oldMapObject.numProjs;
                            oldMapObject.numProjs = 0;
                        }
                        if (oldMapObject.hasMons) {
                            changeto.hasMons = true;
                            oldMapObject.hasMons = false;
                        }
                        if (oldMapObject.hasParty) {
                            changeto.hasParty = true;
                            oldMapObject.hasParty = false;
                        }
                        if (retainitems && oldMapObject.hasItems) {
                            while (oldMapObject.mapItems.size() > 0) {
                                changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                            }
                            changeto.hasItems = true;
                            oldMapObject.hasItems = false;
                        }
                        changeto.doAction();
                    }
                } else {
                    if (!oldMapObject.isPassable || oldMapObject.mapchar == '>') {
                        if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                        else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                            int i = 0;
                            dmnew.Monster tempmon;
                            while (i < 6 && !delaying) {
                                tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                                if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                                else if (i == 3) i = 5;
                                else i++;
                            }
                        }
                    }
                    if (!delaying && !oldMapObject.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                        delaying = true;
                    if (!delaying) {
                        dmnew.DungeonMap[targetlevel][targetx][targety] = oldMapObject;
                        switchstate = !switchstate;
                        if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                        if (changeto.hasCloud) {
                            changeto.hasCloud = false;
                            oldMapObject.hasCloud = true;
                        }
                        if (changeto.numProjs > 0) {
                            oldMapObject.numProjs = changeto.numProjs;
                            changeto.numProjs = 0;
                        }
                        if (changeto.hasMons) {
                            oldMapObject.hasMons = true;
                            changeto.hasMons = false;
                        }
                        if (changeto.hasParty) {
                            oldMapObject.hasParty = true;
                            changeto.hasParty = false;
                        }
                        if (retainitems && changeto.hasItems) {
                            while (changeto.mapItems.size() > 0) {
                                oldMapObject.addItem((Item) changeto.mapItems.remove(0));
                            }
                            oldMapObject.hasItems = true;
                            changeto.hasItems = false;
                        }
                        oldMapObject.doAction();
                    }
                }
                                /*
                                //Vector tempit = map[targetx][targety].mapItems;
                                if (!switchstate) {
                                        //if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0 || (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons && !dmnew.DungeonMap[targetlevel][targetx][targety].hasImmaterialMons)) { delaying=true; }
                                        if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { delaying=true; }
                                        else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                                int i=0;
                                                dmnew.Monster tempmon;
                                                while (i<6 && !delaying) {
                                                        tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                                        if (tempmon!=null && !tempmon.isImmaterial) delaying=true;
                                                        else if (i==3) i=5;
                                                        else i++;
                                                }
                                        }
                                        if (!delaying) {
                                             oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                                             dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                                             switchstate = !switchstate;
                                             if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                                             if (oldMapObject.hasCloud) {
                                                oldMapObject.hasCloud=false;
                                                changeto.hasCloud=true;
                                             }
                                             //if (oldMapObject.hasImmaterialMons) {
                                             //   changeto.hasImmaterialMons = true;
                                             //   changeto.hasMons = true;
                                             //   oldMapObject.hasImmaterialMons = false;
                                             //   oldMapObject.hasMons = false;
                                             //}
                                        }
                                }
                                else {
                                        if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { delaying=true; }
                                        else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                                int i=0;
                                                dmnew.Monster tempmon;
                                                while (i<6 && !delaying) {
                                                        tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                                        if (tempmon!=null && !tempmon.isImmaterial) delaying=true;
                                                        else if (i==3) i=5;
                                                        else i++;
                                                }
                                        }
                                        if (!delaying) {
                                             dmnew.DungeonMap[targetlevel][targetx][targety] = oldMapObject;
                                             switchstate = !switchstate;
                                             if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                                             if (changeto.hasCloud) {
                                                changeto.hasCloud=false;
                                                oldMapObject.hasCloud=true;
                                             }
                                             //if (changeto.hasImmaterialMons) {
                                             //   oldMapObject.hasImmaterialMons = true;
                                             //   oldMapObject.hasMons = true;
                                             //   changeto.hasImmaterialMons = false;
                                             //   changeto.hasMons = false;
                                             //}
                                        }
                                }
                                //map[targetx][targety].mapItems = tempit;
                                */
            } else if (actiontype == 6) {
                if (!changeto.isPassable || changeto.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6 && !delaying) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!delaying && !changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    delaying = true;
                if (!delaying) {
                    oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                    dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                    switchstate = !switchstate;
                    if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                    if (oldMapObject.hasCloud) {
                        oldMapObject.hasCloud = false;
                        changeto.hasCloud = true;
                    }
                    if (oldMapObject.numProjs > 0) {
                        changeto.numProjs = oldMapObject.numProjs;
                        oldMapObject.numProjs = 0;
                    }
                    if (oldMapObject.hasMons) {
                        changeto.hasMons = true;
                        oldMapObject.hasMons = false;
                    }
                    if (oldMapObject.hasParty) {
                        changeto.hasParty = true;
                        oldMapObject.hasParty = false;
                    }
                    if (retainitems && oldMapObject.hasItems) {
                        while (oldMapObject.mapItems.size() > 0) {
                            changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                        }
                        changeto.hasItems = true;
                        oldMapObject.hasItems = false;
                    }
                    changeto.doAction();
                }
                                /*
                                if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { delaying=true; }
                                else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                        int i=0;
                                        dmnew.Monster tempmon;
                                        while (i<6 && !delaying) {
                                                tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                                if (tempmon!=null && !tempmon.isImmaterial) delaying=true;
                                                else if (i==3) i=5;
                                                else i++;
                                        }
                                }
                                if (!delaying) {
                                     oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                                     dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                                     switchstate = !switchstate;
                                     if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                                     if (oldMapObject.hasCloud) {
                                        oldMapObject.hasCloud=false;
                                        changeto.hasCloud=true;
                                     }
                                }
                                */
            } else if (actiontype == 7) {
                dmnew.playSound(soundstring, targetx, targety, loopsound);//when looping added
                //dmnew.playSound(soundstring,targetx,targety);
            } else if (actiontype == 8)
                dmnew.stopSounds(abrupt);//stop all looping sounds, if !abrupt let them play out first
            wasUsed = true;
            java.awt.Image tempimg;
            for (int i = 0; i < pushed.length; i++) {
                if (i < 3) {
                    tempimg = facingside[i];
                    facingside[i] = pushed[i];
                } else if (i < 6) {
                    tempimg = col1pic[i - 3];
                    col1pic[i - 3] = pushed[i];
                } else {
                    tempimg = col3pic[i - 6];
                    col3pic[i - 6] = pushed[i];
                }
                pushed[i] = tempimg;
            }
            dmnew.needredraw = true;
            if (delaying || reset > 0) {
                if (reset > 0) resetting = true;
                if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
                dmnew.mapchanging = true;
            }
        }
    }
    
    public boolean shouldClick(Item it) {
        if (dmnew.facing != side || changecount != 0 || xy.level != dmnew.level || delaying || resetting || (!isReusable && wasUsed) || (type != BUTTON && it.number != keynumber && (picklock == 0 || it.number != 71 || !dmnew.iteminhand || dmnew.inhand != it)))
            return false;
        else return true;
    }
    
    public boolean tryWallSwitch(int x, int y, Item it) {
        if (dmnew.facing != side || changecount != 0 || delaying || resetting || (!isReusable && wasUsed)) return true;
        //old test: if (x>190 && x<256 && y>88 && y<105) {
        if ((x == -1 && y == -1) || x > (64 + xadjust[0]) && y > (22 + yadjust[0]) && x < (64 + xadjust[0] + facingside[0].getWidth(null)) && y < (22 + yadjust[0] + facingside[0].getHeight(null))) {
            //if (type==BUTTON || (it.number==keynumber && (type==KEY || ++keysgotten>=keysneeded))) {
            if (type == BUTTON || (it.number == keynumber && (type == KEY || ++keysgotten >= keysneeded)) || (picklock > 0 && it.number == 71 && dmnew.iteminhand && dmnew.inhand == it)) {
                //click sound
                if (xy.level == dmnew.level && (x != -1 || y != -1)) dmnew.playSound("switch.wav", xy.x, xy.y);
                //trying to pick lock
                if (picklock > 0 && it.number == 71) {
                    int chance = dmnew.hero[dmnew.leader].nlevel * dmnew.hero[dmnew.leader].dexterity / 10;
                    if (chance < dmnew.randGen.nextInt(40) + picklock) {
                        if (type == COIN && it.number == keynumber) keysgotten--;
                        if (!MultWallSwitch2.picklockfail) {
                            dmnew.message.setMessage(dmnew.hero[dmnew.leader].name + " fails to pick the lock.", 4);
                            if (MultWallSwitch2.multipick) MultWallSwitch2.picklockfail = true;
                        }
                        return true;
                    } else if (!MultWallSwitch2.picklocksuccess) {
                        if (xy.level == dmnew.level && (x != -1 || y != -1)) dmnew.playSound("unlock.wav", xy.x, xy.y);
                        dmnew.message.setMessage(dmnew.hero[dmnew.leader].name + " picks the lock.", 4);
                        if (MultWallSwitch2.multipick) MultWallSwitch2.picklocksuccess = true;
                    }
                    dmnew.hero[dmnew.leader].gainxp('n', 1);
                    if (type == COIN && it.number != keynumber) {
                        if (++keysgotten < keysneeded) {
                            return (!consumeskey);
                        }
                    }
                }
                                /*
                                //trying lock picks
                                if (keynumber==71 && dmnew.iteminhand && dmnew.inhand==it) {
                                        int chance = dmnew.hero[dmnew.leader].nlevel*dmnew.hero[dmnew.leader].dexterity/10;
                                        if (chance<dmnew.randGen.nextInt(40)+20) {
                                                if (type==COIN) keysgotten--;
                                                dmnew.message.setMessage(dmnew.hero[dmnew.leader].name+" fails to pick the lock.",4);
                                                return true;
                                        }
                                }
                                */
                if (type == COIN) keysgotten = 0;
                
                if (delay > 0) delaying = true;
                else if (actiontype == 0) {
                    dmnew.DungeonMap[targetlevel][targetx][targety].toggle();
                    switchstate = !switchstate;
                } else if (actiontype == 1) {
                    dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                    switchstate = !switchstate;
                } else if (actiontype == 2) {
                    dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                    switchstate = !switchstate;
                } else if (actiontype == 3) {
                    if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                    else dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                    switchstate = !switchstate;
                } else if (actiontype == 4) {
                    if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                    else dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                    switchstate = !switchstate;
                } else if (actiontype == 5) {
                    if (!switchstate) {
                        if (!changeto.isPassable || changeto.mapchar == '>') {
                            if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                            else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                int i = 0;
                                dmnew.Monster tempmon;
                                while (i < 6 && !delaying) {
                                    tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                                    if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                                    else if (i == 3) i = 5;
                                    else i++;
                                }
                            }
                        }
                        if (!delaying && !changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                            delaying = true;
                        if (!delaying) {
                            oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                            dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                            switchstate = !switchstate;
                            if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                            if (oldMapObject.hasCloud) {
                                oldMapObject.hasCloud = false;
                                changeto.hasCloud = true;
                            }
                            if (oldMapObject.numProjs > 0) {
                                changeto.numProjs = oldMapObject.numProjs;
                                oldMapObject.numProjs = 0;
                            }
                            if (oldMapObject.hasMons) {
                                changeto.hasMons = true;
                                oldMapObject.hasMons = false;
                            }
                            if (oldMapObject.hasParty) {
                                changeto.hasParty = true;
                                oldMapObject.hasParty = false;
                            }
                            if (retainitems && oldMapObject.hasItems) {
                                while (oldMapObject.mapItems.size() > 0) {
                                    changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                                }
                                changeto.hasItems = true;
                                oldMapObject.hasItems = false;
                            }
                            changeto.doAction();
                        }
                    } else {
                        if (!oldMapObject.isPassable || oldMapObject.mapchar == '>') {
                            if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                            else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                int i = 0;
                                dmnew.Monster tempmon;
                                while (i < 6 && !delaying) {
                                    tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                                    if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                                    else if (i == 3) i = 5;
                                    else i++;
                                }
                            }
                        }
                        if (!delaying && !oldMapObject.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                            delaying = true;
                        if (!delaying) {
                            dmnew.DungeonMap[targetlevel][targetx][targety] = oldMapObject;
                            switchstate = !switchstate;
                            if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                            if (changeto.hasCloud) {
                                changeto.hasCloud = false;
                                oldMapObject.hasCloud = true;
                            }
                            if (changeto.numProjs > 0) {
                                oldMapObject.numProjs = changeto.numProjs;
                                changeto.numProjs = 0;
                            }
                            if (changeto.hasMons) {
                                oldMapObject.hasMons = true;
                                changeto.hasMons = false;
                            }
                            if (changeto.hasParty) {
                                oldMapObject.hasParty = true;
                                changeto.hasParty = false;
                            }
                            if (retainitems && changeto.hasItems) {
                                while (changeto.mapItems.size() > 0) {
                                    oldMapObject.addItem((Item) changeto.mapItems.remove(0));
                                }
                                oldMapObject.hasItems = true;
                                changeto.hasItems = false;
                            }
                            oldMapObject.doAction();
                        }
                    }
                                        /*
                                        //Vector tempit = map[targetx][targety].mapItems;
                                        if (!switchstate) {
                                                if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { delaying=true; }
                                                else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                                        int i=0;
                                                        dmnew.Monster tempmon;
                                                        while (i<6 && !delaying) {
                                                                tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                                                if (tempmon!=null && !tempmon.isImmaterial) delaying=true;
                                                                else if (i==3) i=5;
                                                                else i++;
                                                        }
                                                }
                                                if (!delaying) {
                                                     oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                                                     dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                                                     switchstate = !switchstate;
                                                     if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                                                     if (oldMapObject.hasCloud) {
                                                        oldMapObject.hasCloud=false;
                                                        changeto.hasCloud=true;
                                                     }
                                                     //if (oldMapObject.hasImmaterialMons) {
                                                     //   changeto.hasImmaterialMons = true;
                                                     //   changeto.hasMons = true;
                                                     //   oldMapObject.hasImmaterialMons = false;
                                                     //   oldMapObject.hasMons = false;
                                                     //}
                                                }
                                        }
                                        else {
                                                if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { delaying=true; }
                                                else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                                        int i=0;
                                                        dmnew.Monster tempmon;
                                                        while (i<6 && !delaying) {
                                                                tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                                                if (tempmon!=null && !tempmon.isImmaterial) delaying=true;
                                                                else if (i==3) i=5;
                                                                else i++;
                                                        }
                                                }
                                                if (!delaying) {
                                                     dmnew.DungeonMap[targetlevel][targetx][targety] = oldMapObject;
                                                     switchstate = !switchstate;
                                                     if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                                                     if (changeto.hasCloud) {
                                                        changeto.hasCloud=false;
                                                        oldMapObject.hasCloud=true;
                                                     }
                                                     //if (changeto.hasImmaterialMons) {
                                                     //   oldMapObject.hasImmaterialMons = true;
                                                     //   oldMapObject.hasMons = true;
                                                     //   changeto.hasImmaterialMons = false;
                                                     //   changeto.hasMons = false;
                                                     //}
                                                }
                                        }
                                        //map[targetx][targety].mapItems = tempit;
                                        */
                } else if (actiontype == 6) {
                    if (!changeto.isPassable || changeto.mapchar == '>') {
                        if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) delaying = true;
                        else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                            int i = 0;
                            dmnew.Monster tempmon;
                            while (i < 6 && !delaying) {
                                tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                                if (tempmon != null && !tempmon.isImmaterial) delaying = true;
                                else if (i == 3) i = 5;
                                else i++;
                            }
                        }
                    }
                    if (!delaying && !changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                        delaying = true;
                    if (!delaying) {
                        oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                        dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                        switchstate = !switchstate;
                        if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                        if (oldMapObject.hasCloud) {
                            oldMapObject.hasCloud = false;
                            changeto.hasCloud = true;
                        }
                        if (oldMapObject.numProjs > 0) {
                            changeto.numProjs = oldMapObject.numProjs;
                            oldMapObject.numProjs = 0;
                        }
                        if (oldMapObject.hasMons) {
                            changeto.hasMons = true;
                            oldMapObject.hasMons = false;
                        }
                        if (oldMapObject.hasParty) {
                            changeto.hasParty = true;
                            oldMapObject.hasParty = false;
                        }
                        if (retainitems && oldMapObject.hasItems) {
                            while (oldMapObject.mapItems.size() > 0) {
                                changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                            }
                            changeto.hasItems = true;
                            oldMapObject.hasItems = false;
                        }
                        changeto.doAction();
                    }
                                        /*
                                        if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { delaying=true; }
                                        else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                                int i=0;
                                                dmnew.Monster tempmon;
                                                while (i<6 && !delaying) {
                                                        tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                                        if (tempmon!=null && !tempmon.isImmaterial) delaying=true;
                                                        else if (i==3) i=5;
                                                        else i++;
                                                }
                                        }
                                        if (!delaying) {
                                             oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                                             dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                                             switchstate = !switchstate;
                                             if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                                             if (oldMapObject.hasCloud) {
                                                oldMapObject.hasCloud=false;
                                                changeto.hasCloud=true;
                                             }
                                        }
                                        */
                } else if (actiontype == 7) {
                    dmnew.playSound(soundstring, targetx, targety, loopsound);//when looping added
                    //dmnew.playSound(soundstring,targetx,targety);
                } else if (actiontype == 8)
                    dmnew.stopSounds(abrupt);//stop all looping sounds, if !abrupt let them play out first
                wasUsed = true;
                if (type == BUTTON) {
                    java.awt.Image tempimg;
                    for (int i = 0; i < pushed.length; i++) {
                        if (i < 3) {
                            tempimg = facingside[i];
                            facingside[i] = pushed[i];
                        } else if (i < 6) {
                            tempimg = col1pic[i - 3];
                            col1pic[i - 3] = pushed[i];
                        } else {
                            tempimg = col3pic[i - 6];
                            col3pic[i - 6] = pushed[i];
                        }
                        pushed[i] = tempimg;
                    }
                    dmnew.needredraw = true;
                }
                if (delaying || reset > 0) {
                    if (reset > 0) resetting = true;
                    if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
                    dmnew.mapchanging = true;
                } else dmnew.needredraw = true;
                return !consumeskey;
            } else if (it.number == keynumber && type == COIN && xy.level == dmnew.level) {
                dmnew.playSound("switch.wav", xy.x, xy.y);//click when put coin in even if doesn't activate switch yet
                return !consumeskey;
            }
            //if (consumeskey && type>BUTTON && it.number==keynumber) return false;
            return true;
        }
        return true;
    }
    
    protected void setPics() {
        super.setPics();
        if ((type == KEY && picnumber == 13) || (type == BUTTON && picnumber == 9)) {
            pushed = new java.awt.Image[1];
            facingside[0] = loadPic("eyekey1.gif");
            facingside[1] = loadPic("eyekey2.gif");
            facingside[2] = loadPic("eyekey3.gif");
            col1pic[0] = loadPic("eyekeycol11.gif");
            col3pic[0] = loadPic("eyekeycol31.gif");
            col1pic[1] = loadPic("eyekeycol12.gif");
            col3pic[1] = loadPic("eyekeycol32.gif");
            col1pic[2] = loadPic("eyekeycol13.gif");
            col3pic[2] = loadPic("eyekeycol33.gif");
            pushed[0] = facingside[0];
            //xy for facing
            xadjust[0] = 115;
            yadjust[0] = 73;
            xadjust[1] = 74;
            yadjust[1] = 47;
            xadjust[2] = 56;
            yadjust[2] = 34;
            //xy for col1
            xadjust[3] = 92;
            yadjust[3] = 72;
            xadjust[4] = 132;
            yadjust[4] = 46;
            xadjust[5] = 158;
            yadjust[5] = 34;
            //xy for col3
            xadjust[6] = 6;
            yadjust[6] = 72;
            xadjust[7] = 4;
            yadjust[7] = 46;
            xadjust[8] = 2;
            yadjust[8] = 34;
        } else if (type == BUTTON) {
            if (picnumber == 0) {
                //stone button
                pushed = new java.awt.Image[1];
                pushed[0] = loadPic("stonebutton1-p.gif");
                facingside[0] = loadPic("stonebutton1.gif");
                facingside[1] = loadPic("stonebutton2.gif");
                facingside[2] = loadPic("stonebutton3.gif");
                col1pic[0] = loadPic("stonebuttoncol11.gif");
                col3pic[0] = loadPic("stonebuttoncol31.gif");
                col1pic[1] = loadPic("stonebuttoncol12.gif");
                col3pic[1] = loadPic("stonebuttoncol32.gif");
                col1pic[2] = loadPic("stonebuttoncol13.gif");
                col3pic[2] = loadPic("stonebuttoncol33.gif");
                //xy for facing
                xadjust[0] = 127;
                yadjust[0] = 67;
                xadjust[1] = 84;
                yadjust[1] = 43;
                xadjust[2] = 60;
                yadjust[2] = 31;
                //xy for col1
                xadjust[3] = 92;
                yadjust[3] = 67;
                xadjust[4] = 133;
                yadjust[4] = 42;
                xadjust[5] = 158;
                yadjust[5] = 32;
                //xy for col3
                xadjust[6] = 9;
                yadjust[6] = 67;
                xadjust[7] = 4;
                yadjust[7] = 42;
                xadjust[8] = 3;
                yadjust[8] = 32;
            } else if (picnumber == 1) {
                //lever
                pushed = new java.awt.Image[9];
                facingside[0] = loadPic("lever-up1.gif");
                facingside[1] = loadPic("lever-up2.gif");
                facingside[2] = loadPic("lever-up3.gif");
                col1pic[0] = loadPic("lever-upcol11.gif");
                col3pic[0] = loadPic("lever-upcol31.gif");
                col1pic[1] = loadPic("lever-upcol12.gif");
                col3pic[1] = loadPic("lever-upcol32.gif");
                col1pic[2] = loadPic("lever-upcol13.gif");
                col3pic[2] = loadPic("lever-upcol33.gif");
                pushed[0] = loadPic("lever-down1.gif");
                pushed[1] = loadPic("lever-down2.gif");
                pushed[2] = loadPic("lever-down3.gif");
                pushed[3] = loadPic("lever-downcol11.gif");
                pushed[4] = loadPic("lever-downcol12.gif");
                pushed[5] = loadPic("lever-downcol13.gif");
                pushed[6] = loadPic("lever-downcol31.gif");
                pushed[7] = loadPic("lever-downcol32.gif");
                pushed[8] = loadPic("lever-downcol33.gif");
                //xy for facing
                xadjust[0] = 151;
                yadjust[0] = 61;
                xadjust[1] = 98;
                yadjust[1] = 39;
                xadjust[2] = 71;
                yadjust[2] = 28;
                //xy for col1
                xadjust[3] = 100;
                yadjust[3] = 62;
                xadjust[4] = 139;
                yadjust[4] = 43;
                xadjust[5] = 162;
                yadjust[5] = 33;
                //xy for col3
                xadjust[6] = -17;
                yadjust[6] = 62;
                xadjust[7] = -8;
                yadjust[7] = 43;
                xadjust[8] = -3;
                yadjust[8] = 33;
            } else if (picnumber == 2) {
                //small button
                pushed = new java.awt.Image[1];
                pushed[0] = loadPic("smallbutton1-p.gif");
                facingside[0] = loadPic("smallbutton1.gif");
                facingside[1] = loadPic("smallbutton2.gif");
                //facingside[2]=loadPic("smallbutton3.gif");
                facingside[2] = blankpic;
                col1pic[0] = loadPic("smallbuttoncol11.gif");
                col3pic[0] = loadPic("smallbuttoncol31.gif");
                //col1pic[1]=loadPic("smallbuttoncol12.gif");
                //col3pic[1]=loadPic("smallbuttoncol32.gif");
                //col1pic[2]=loadPic("smallbuttoncol13.gif");
                //col3pic[2]=loadPic("smallbuttoncol33.gif");
                col1pic[1] = blankpic;
                col3pic[1] = blankpic;
                col1pic[2] = blankpic;
                col3pic[2] = blankpic;
                //xy for facing
                xadjust[0] = 244;
                yadjust[0] = 187;
                xadjust[1] = 156;
                yadjust[1] = 120;
                xadjust[2] = 0;
                yadjust[2] = 0;
                //xy for col1
                xadjust[3] = 110;
                yadjust[3] = 154; //86; 172;
                xadjust[4] = 0;
                yadjust[4] = 0;
                xadjust[5] = 0;
                yadjust[5] = 0;
                //xy for col3
                xadjust[6] = 30;
                yadjust[6] = 172;
                xadjust[7] = 0;
                yadjust[7] = 0;
                xadjust[8] = 0;
                yadjust[8] = 0;
            } else if (picnumber == 6) {
                //ring
                pushed = new java.awt.Image[1];
                facingside[0] = loadPic("wallring1.gif");
                facingside[1] = loadPic("wallring2.gif");
                facingside[2] = loadPic("wallring3.gif");
                col1pic[0] = loadPic("wallringcol11.gif");
                col1pic[1] = loadPic("wallringcol12.gif");
                col1pic[2] = loadPic("wallringcol13.gif");
                col3pic[0] = loadPic("wallringcol31.gif");
                col3pic[1] = loadPic("wallringcol32.gif");
                col3pic[2] = loadPic("wallringcol33.gif");
                pushed[0] = facingside[0];
                //xy for facing
                xadjust[0] = 134;
                yadjust[0] = 82;
                xadjust[1] = 87;
                yadjust[1] = 53;
                xadjust[2] = 64;
                yadjust[2] = 38;
                //xy for col1
                xadjust[3] = 97;
                yadjust[3] = 81;
                xadjust[4] = 136;
                yadjust[4] = 51;
                xadjust[5] = 160;
                yadjust[5] = 39;
                //xy for col3
                xadjust[6] = 14;
                yadjust[6] = 81;
                xadjust[7] = 8;
                yadjust[7] = 51;
                xadjust[8] = 6;
                yadjust[8] = 39;
            }
                     /*
                     else if (picnumber==7) {
                        //hook
                        pushed = new java.awt.Image[1];
                        facingside[0] = loadPic("wallhook1.gif");
                        facingside[1] = loadPic("wallhook2.gif");
                        facingside[2] = loadPic("wallhook3.gif");
                        col1pic[0] = loadPic("wallhookcol11.gif");
                        col1pic[1] = loadPic("wallhookcol12.gif");
                        col1pic[2] = loadPic("wallhookcol13.gif");
                        col3pic[0] = loadPic("wallhookcol31.gif");
                        col3pic[1] = loadPic("wallhookcol32.gif");
                        col3pic[2] = loadPic("wallhookcol33.gif");
                        pushed[0] = facingside[0];
                        //xy for facing
                        xadjust[0]=134;  yadjust[0]=82;
                        xadjust[1]=87;   yadjust[1]=53;
                        xadjust[2]=64;   yadjust[2]=38;
                        //xy for col1
                        xadjust[3]=97;   yadjust[3]=81;
                        xadjust[4]=136;  yadjust[4]=51;
                        xadjust[5]=160;  yadjust[5]=39;
                        //xy for col3
                        xadjust[6]=13;    yadjust[6]=81;
                        xadjust[7]=7;    yadjust[7]=51;
                        xadjust[8]=5;    yadjust[8]=39;
                     }
                     */
            else if (picnumber == 7) {
                //crack button
                pushed = new java.awt.Image[1];
                pushed[0] = loadPic("wallcrackb1-p.gif");
                facingside[0] = loadPic("wallcrackb1.gif");
                facingside[1] = loadPic("wallcrack2.gif");
                facingside[2] = loadPic("wallcrack3.gif");
                col1pic[0] = loadPic("wallcrackcol11.gif");
                col1pic[1] = loadPic("wallcrackcol12.gif");
                col1pic[2] = blankpic;
                col3pic[0] = loadPic("wallcrackcol31.gif");
                col3pic[1] = loadPic("wallcrackcol32.gif");
                col3pic[2] = loadPic("wallcrackcol33.gif");
                //xy for facing
                xadjust[0] = 212;
                yadjust[0] = 131;
                xadjust[1] = 134;
                yadjust[1] = 86;
                xadjust[2] = 102;
                yadjust[2] = 62;
                //xy for col1
                xadjust[3] = 115;
                yadjust[3] = 114;
                xadjust[4] = 146;
                yadjust[4] = 72;
                //xy for col3
                xadjust[6] = 47;
                yadjust[6] = 129;
                xadjust[7] = 23;
                yadjust[7] = 83;
                xadjust[8] = 12;
                yadjust[8] = 58;
            } else if (picnumber == 8) {
                //chaos face
                pushed = new java.awt.Image[9];
                pushed[0] = loadPic("chaos1-p.gif");
                pushed[1] = loadPic("chaos2-p.gif");
                pushed[2] = loadPic("chaos3-p.gif");
                pushed[3] = loadPic("chaoscol11-p.gif");
                pushed[4] = loadPic("chaoscol12-p.gif");
                pushed[5] = loadPic("chaoscol13-p.gif");
                pushed[6] = loadPic("chaoscol31-p.gif");
                pushed[7] = loadPic("chaoscol32-p.gif");
                pushed[8] = loadPic("chaoscol33-p.gif");
                facingside[0] = loadPic("chaos1.gif");
                facingside[1] = loadPic("chaos2.gif");
                facingside[2] = loadPic("chaos3.gif");
                col1pic[0] = loadPic("chaoscol11.gif");
                col1pic[1] = loadPic("chaoscol12.gif");
                col1pic[2] = loadPic("chaoscol13.gif");
                col3pic[0] = loadPic("chaoscol31.gif");
                col3pic[1] = loadPic("chaoscol32.gif");
                col3pic[2] = loadPic("chaoscol33.gif");
                //xy for facing
                xadjust[0] = 71;
                yadjust[0] = 67;
                xadjust[1] = 45;
                yadjust[1] = 43;
                xadjust[2] = 35;
                yadjust[2] = 31;
                //xy for col1
                xadjust[3] = 72;
                yadjust[3] = 69;
                xadjust[4] = 119;
                yadjust[4] = 41;
                xadjust[5] = 150;
                yadjust[5] = 32;
                //xy for col3
                xadjust[6] = 0;
                yadjust[6] = 69;
                xadjust[7] = 0;
                yadjust[7] = 41;
                xadjust[8] = 0;
                yadjust[8] = 32;
            }
            //9 is eye, see above
            else if (picnumber == 10) {
                //demon face
                pushed = new java.awt.Image[1];
                facingside[0] = loadPic("demon1.gif");
                facingside[1] = loadPic("demon2.gif");
                facingside[2] = loadPic("demon3.gif");
                col1pic[0] = loadPic("demoncol11.gif");
                col1pic[1] = loadPic("demoncol12.gif");
                col1pic[2] = loadPic("demoncol13.gif");
                col3pic[0] = loadPic("demoncol31.gif");
                col3pic[1] = loadPic("demoncol32.gif");
                col3pic[2] = loadPic("demoncol33.gif");
                pushed[0] = facingside[0];
                //xy for facing
                xadjust[0] = 74;
                yadjust[0] = 65;
                xadjust[1] = 51;
                yadjust[1] = 46;
                xadjust[2] = 37;
                yadjust[2] = 33;
                //xy for col1
                xadjust[3] = 84;
                yadjust[3] = 68;
                xadjust[4] = 129;
                yadjust[4] = 44;
                xadjust[5] = 155;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 4;
                yadjust[6] = 68;
                xadjust[7] = 3;
                yadjust[7] = 44;
                xadjust[8] = 2;
                yadjust[8] = 35;
            } else {
                if (picnumber == 3) {
                    //green gem button
                    pushed = new java.awt.Image[3];
                    pushed[0] = loadPic("greengem1-p.gif");
                    pushed[1] = loadPic("greengem2-p.gif");
                    pushed[2] = loadPic("greengem3-p.gif");
                    facingside[0] = loadPic("greengem1.gif");
                    facingside[1] = loadPic("greengem2.gif");
                    facingside[2] = loadPic("greengem3.gif");
                    col1pic[0] = loadPic("greengemcol11.gif");
                    col3pic[0] = loadPic("greengemcol31.gif");
                    col1pic[1] = loadPic("greengemcol12.gif");
                    col3pic[1] = loadPic("greengemcol32.gif");
                    col1pic[2] = loadPic("greengemcol13.gif");
                    col3pic[2] = loadPic("greengemcol33.gif");
                } else if (picnumber == 4) {
                    //blue gem button
                    pushed = new java.awt.Image[3];
                    pushed[0] = loadPic("bluegem1-p.gif");
                    pushed[1] = loadPic("bluegem2-p.gif");
                    pushed[2] = loadPic("bluegem3-p.gif");
                    facingside[0] = loadPic("bluegem1.gif");
                    facingside[1] = loadPic("bluegem2.gif");
                    facingside[2] = loadPic("bluegem3.gif");
                    col1pic[0] = loadPic("bluegemcol11.gif");
                    col3pic[0] = loadPic("bluegemcol31.gif");
                    col1pic[1] = loadPic("bluegemcol12.gif");
                    col3pic[1] = loadPic("bluegemcol32.gif");
                    col1pic[2] = loadPic("bluegemcol13.gif");
                    col3pic[2] = loadPic("bluegemcol33.gif");
                } else {
                    //red gem button
                    pushed = new java.awt.Image[3];
                    pushed[0] = loadPic("redgem1-p.gif");
                    pushed[1] = loadPic("redgem2-p.gif");
                    pushed[2] = loadPic("redgem3-p.gif");
                    facingside[0] = loadPic("redgem1.gif");
                    facingside[1] = loadPic("redgem2.gif");
                    facingside[2] = loadPic("redgem3.gif");
                    col1pic[0] = loadPic("redgemcol11.gif");
                    col3pic[0] = loadPic("redgemcol31.gif");
                    col1pic[1] = loadPic("redgemcol12.gif");
                    col3pic[1] = loadPic("redgemcol32.gif");
                    col1pic[2] = loadPic("redgemcol13.gif");
                    col3pic[2] = loadPic("redgemcol33.gif");
                }
                //gem buttons all have same xy coords
                //xy for facing
                xadjust[0] = 145;
                yadjust[0] = 84;
                xadjust[1] = 94;
                yadjust[1] = 55;
                xadjust[2] = 69;
                yadjust[2] = 39;
                //xy for col1
                xadjust[3] = 98;
                yadjust[3] = 79;
                xadjust[4] = 136;
                yadjust[4] = 51;
                xadjust[5] = 161;
                yadjust[5] = 37;
                //xy for col3
                xadjust[6] = 15;
                yadjust[6] = 79;
                xadjust[7] = 9;
                yadjust[7] = 51;
                xadjust[8] = 6;
                yadjust[8] = 37;
            }
        } else if (type == KEY) {
            if (picnumber == 0) {
                facingside[0] = loadPic("ironkey1.gif");
                facingside[1] = loadPic("ironkey2.gif");
                facingside[2] = loadPic("ironkey3.gif");
                col1pic[0] = loadPic("ironkeycol11.gif");
                col3pic[0] = loadPic("ironkeycol31.gif");
                col1pic[1] = loadPic("ironkeycol12.gif");
                col3pic[1] = loadPic("ironkeycol32.gif");
                col1pic[2] = loadPic("ironkeycol13.gif");
                col3pic[2] = loadPic("ironkeycol33.gif");
                //xy for facing
                xadjust[0] = 137;
                yadjust[0] = 76;
                xadjust[1] = 89;
                yadjust[1] = 49;
                xadjust[2] = 66;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 92;
                yadjust[3] = 76;
                xadjust[4] = 134;
                yadjust[4] = 48;
                xadjust[5] = 160;
                yadjust[5] = 36;
                //xy for col3
                xadjust[6] = 11;
                yadjust[6] = 76;
                xadjust[7] = 6;
                yadjust[7] = 48;
                xadjust[8] = 5;
                yadjust[8] = 36;
            } else if (picnumber == 1) {
                facingside[0] = loadPic("brasskey1.gif");
                facingside[1] = loadPic("brasskey2.gif");
                facingside[2] = loadPic("brasskey3.gif");
                col1pic[0] = loadPic("brasskeycol11.gif");
                col3pic[0] = loadPic("brasskeycol31.gif");
                col1pic[1] = loadPic("brasskeycol12.gif");
                col3pic[1] = loadPic("brasskeycol32.gif");
                col1pic[2] = loadPic("brasskeycol13.gif");
                col3pic[2] = loadPic("brasskeycol33.gif");
                //xy for facing
                xadjust[0] = 137;
                yadjust[0] = 76;
                xadjust[1] = 89;
                yadjust[1] = 49;
                xadjust[2] = 66;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 92;
                yadjust[3] = 76;
                xadjust[4] = 134;
                yadjust[4] = 48;
                xadjust[5] = 160;
                yadjust[5] = 36;
                //xy for col3
                xadjust[6] = 11;
                yadjust[6] = 76;
                xadjust[7] = 6;
                yadjust[7] = 48;
                xadjust[8] = 5;
                yadjust[8] = 36;
            } else if (picnumber == 2) {
                facingside[0] = loadPic("goldkey1.gif");
                facingside[1] = loadPic("goldkey2.gif");
                facingside[2] = loadPic("goldkey3.gif");
                col1pic[0] = loadPic("goldkeycol11.gif");
                col3pic[0] = loadPic("goldkeycol31.gif");
                col1pic[1] = loadPic("goldkeycol12.gif");
                col3pic[1] = loadPic("goldkeycol32.gif");
                col1pic[2] = loadPic("goldkeycol13.gif");
                col3pic[2] = loadPic("goldkeycol33.gif");
                //xy for facing
                xadjust[0] = 141;
                yadjust[0] = 78;
                xadjust[1] = 92;
                yadjust[1] = 50;
                xadjust[2] = 67;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 96;
                yadjust[3] = 75;
                xadjust[4] = 136;
                yadjust[4] = 48;
                xadjust[5] = 160;
                yadjust[5] = 36;
                //xy for col3
                xadjust[6] = 13;
                yadjust[6] = 75;
                xadjust[7] = 6;
                yadjust[7] = 48;
                xadjust[8] = 5;
                yadjust[8] = 36;
            } else if (picnumber == 3) {
                facingside[0] = loadPic("emeraldkey1.gif");
                facingside[1] = loadPic("emeraldkey2.gif");
                facingside[2] = loadPic("emeraldkey3.gif");
                col1pic[0] = loadPic("emeraldkeycol11.gif");
                col3pic[0] = loadPic("emeraldkeycol31.gif");
                col1pic[1] = loadPic("emeraldkeycol12.gif");
                col3pic[1] = loadPic("emeraldkeycol32.gif");
                col1pic[2] = loadPic("emeraldkeycol13.gif");
                col3pic[2] = loadPic("emeraldkeycol33.gif");
                //xy for facing
                xadjust[0] = 131;
                yadjust[0] = 67;
                xadjust[1] = 84;
                yadjust[1] = 43;
                xadjust[2] = 62;
                yadjust[2] = 31;
                //xy for col1
                xadjust[3] = 93;
                yadjust[3] = 67;
                xadjust[4] = 135;
                yadjust[4] = 43;
                xadjust[5] = 159;
                yadjust[5] = 33;
                //xy for col3
                xadjust[6] = 11;
                yadjust[6] = 67;
                xadjust[7] = 5;
                yadjust[7] = 43;
                xadjust[8] = 4;
                yadjust[8] = 33;
            } else if (picnumber == 4) {
                facingside[0] = loadPic("rubykey1.gif");
                facingside[1] = loadPic("rubykey2.gif");
                facingside[2] = loadPic("rubykey3.gif");
                col1pic[0] = loadPic("rubykeycol11.gif");
                col3pic[0] = loadPic("rubykeycol31.gif");
                col1pic[1] = loadPic("rubykeycol12.gif");
                col3pic[1] = loadPic("rubykeycol32.gif");
                col1pic[2] = loadPic("rubykeycol13.gif");
                col3pic[2] = loadPic("rubykeycol33.gif");
                //xy for facing
                xadjust[0] = 123;
                yadjust[0] = 64;
                xadjust[1] = 79;
                yadjust[1] = 40;
                xadjust[2] = 57;
                yadjust[2] = 28;
                //xy for col1
                xadjust[3] = 93;
                yadjust[3] = 63;
                xadjust[4] = 135;
                yadjust[4] = 40;
                xadjust[5] = 159;
                yadjust[5] = 32;
                //xy for col3
                xadjust[6] = 11;
                yadjust[6] = 63;
                xadjust[7] = 5;
                yadjust[7] = 40;
                xadjust[8] = 5;
                yadjust[8] = 32;
            } else if (picnumber == 5) {
                facingside[0] = loadPic("ruby2key1.gif");
                facingside[1] = loadPic("ruby2key2.gif");
                facingside[2] = loadPic("ruby2key3.gif");
                col1pic[0] = loadPic("ruby2keycol11.gif");
                col3pic[0] = loadPic("ruby2keycol31.gif");
                col1pic[1] = loadPic("ruby2keycol12.gif");
                col3pic[1] = loadPic("ruby2keycol32.gif");
                col1pic[2] = loadPic("ruby2keycol13.gif");
                col3pic[2] = loadPic("ruby2keycol33.gif");
                //xy for facing
                xadjust[0] = 113;
                yadjust[0] = 54;
                xadjust[1] = 72;
                yadjust[1] = 33;
                xadjust[2] = 52;
                yadjust[2] = 23;
                //xy for col1
                xadjust[3] = 90;
                yadjust[3] = 52;
                xadjust[4] = 133;
                yadjust[4] = 32;
                xadjust[5] = 158;
                yadjust[5] = 28;
                //xy for col3
                xadjust[6] = 10;
                yadjust[6] = 52;
                xadjust[7] = 4;
                yadjust[7] = 32;
                xadjust[8] = 4;
                yadjust[8] = 28;
            } else if (picnumber == 6) {
                facingside[0] = loadPic("onyxkey1.gif");
                facingside[1] = loadPic("onyxkey2.gif");
                facingside[2] = loadPic("onyxkey3.gif");
                col1pic[0] = loadPic("onyxkeycol11.gif");
                col3pic[0] = loadPic("onyxkeycol31.gif");
                col1pic[1] = loadPic("onyxkeycol12.gif");
                col3pic[1] = loadPic("onyxkeycol32.gif");
                col1pic[2] = loadPic("onyxkeycol13.gif");
                col3pic[2] = loadPic("onyxkeycol33.gif");
                //xy for facing
                xadjust[0] = 126;
                yadjust[0] = 67;
                xadjust[1] = 81;
                yadjust[1] = 43;
                xadjust[2] = 60;
                yadjust[2] = 31;
                //xy for col1
                xadjust[3] = 90;
                yadjust[3] = 67;
                xadjust[4] = 133;
                yadjust[4] = 43;
                xadjust[5] = 158;
                yadjust[5] = 33;
                //xy for col3
                xadjust[6] = 8;
                yadjust[6] = 67;
                xadjust[7] = 3;
                yadjust[7] = 43;
                xadjust[8] = 2;
                yadjust[8] = 33;
            } else if (picnumber == 7) {
                facingside[0] = loadPic("tourqkey1.gif");
                facingside[1] = loadPic("tourqkey2.gif");
                facingside[2] = loadPic("tourqkey3.gif");
                col1pic[0] = loadPic("tourqkeycol11.gif");
                col3pic[0] = loadPic("tourqkeycol31.gif");
                col1pic[1] = loadPic("tourqkeycol12.gif");
                col3pic[1] = loadPic("tourqkeycol32.gif");
                col1pic[2] = loadPic("tourqkeycol13.gif");
                col3pic[2] = loadPic("tourqkeycol33.gif");
                //xy for facing
                xadjust[0] = 129;
                yadjust[0] = 69;
                xadjust[1] = 84;
                yadjust[1] = 45;
                xadjust[2] = 62;
                yadjust[2] = 32;
                //xy for col1
                xadjust[3] = 92;
                yadjust[3] = 66;
                xadjust[4] = 134;
                yadjust[4] = 43;
                xadjust[5] = 158;
                yadjust[5] = 33;
                //xy for col3
                xadjust[6] = 9;
                yadjust[6] = 66;
                xadjust[7] = 5;
                yadjust[7] = 43;
                xadjust[8] = 4;
                yadjust[8] = 33;
            } else if (picnumber == 8) {
                facingside[0] = loadPic("wingedkey1.gif");
                facingside[1] = loadPic("wingedkey2.gif");
                facingside[2] = loadPic("wingedkey3.gif");
                col1pic[0] = loadPic("wingedkeycol11.gif");
                col3pic[0] = loadPic("wingedkeycol31.gif");
                col1pic[1] = loadPic("wingedkeycol12.gif");
                col3pic[1] = loadPic("wingedkeycol32.gif");
                col1pic[2] = loadPic("wingedkeycol13.gif");
                col3pic[2] = loadPic("wingedkeycol33.gif");
                //xy for facing
                xadjust[0] = 129;
                yadjust[0] = 80;
                xadjust[1] = 83;
                yadjust[1] = 52;
                xadjust[2] = 62;
                yadjust[2] = 37;
                //xy for col1
                xadjust[3] = 91;
                yadjust[3] = 76;
                xadjust[4] = 133;
                yadjust[4] = 48;
                xadjust[5] = 157;
                yadjust[5] = 36;
                //xy for col3
                xadjust[6] = 7;
                yadjust[6] = 76;
                xadjust[7] = 4;
                yadjust[7] = 48;
                xadjust[8] = 3;
                yadjust[8] = 36;
            } else if (picnumber == 9) {
                facingside[0] = loadPic("masterkey1.gif");
                facingside[1] = loadPic("masterkey2.gif");
                facingside[2] = loadPic("masterkey3.gif");
                col1pic[0] = loadPic("masterkeycol11.gif");
                col3pic[0] = loadPic("masterkeycol31.gif");
                col1pic[1] = loadPic("masterkeycol12.gif");
                col3pic[1] = loadPic("masterkeycol32.gif");
                col1pic[2] = loadPic("masterkeycol13.gif");
                col3pic[2] = loadPic("masterkeycol33.gif");
                //xy for facing
                xadjust[0] = 121;
                yadjust[0] = 62;
                xadjust[1] = 78;
                yadjust[1] = 39;
                xadjust[2] = 57;
                yadjust[2] = 28;
                //xy for col1
                xadjust[3] = 87;
                yadjust[3] = 64;
                xadjust[4] = 130;
                yadjust[4] = 41;
                xadjust[5] = 156;
                yadjust[5] = 30;
                //xy for col3
                xadjust[6] = 5;
                yadjust[6] = 64;
                xadjust[7] = 2;
                yadjust[7] = 41;
                xadjust[8] = 1;
                yadjust[8] = 30;
            } else if (picnumber == 10) {
                facingside[0] = loadPic("rakey1.gif");
                facingside[1] = loadPic("rakey2.gif");
                facingside[2] = loadPic("rakey3.gif");
                col1pic[0] = loadPic("rakeycol11.gif");
                col3pic[0] = loadPic("rakeycol31.gif");
                col1pic[1] = loadPic("rakeycol12.gif");
                col3pic[1] = loadPic("rakeycol32.gif");
                col1pic[2] = loadPic("rakeycol13.gif");
                col3pic[2] = loadPic("rakeycol33.gif");
                //xy for facing
                xadjust[0] = 105;
                yadjust[0] = 43;
                xadjust[1] = 70;
                yadjust[1] = 31;
                xadjust[2] = 53;
                yadjust[2] = 24;
                //xy for col1
                xadjust[3] = 86;
                yadjust[3] = 49;
                xadjust[4] = 130;
                yadjust[4] = 34;
                xadjust[5] = 156;
                yadjust[5] = 27;
                //xy for col3
                xadjust[6] = 5;
                yadjust[6] = 49;
                xadjust[7] = 3;
                yadjust[7] = 34;
                xadjust[8] = 2;
                yadjust[8] = 27;
            } else if (picnumber == 11) {
                facingside[0] = loadPic("skeletonkey1.gif");
                facingside[1] = loadPic("skeletonkey2.gif");
                facingside[2] = loadPic("skeletonkey3.gif");
                col1pic[0] = loadPic("skeletonkeycol11.gif");
                col3pic[0] = loadPic("skeletonkeycol31.gif");
                col1pic[1] = loadPic("skeletonkeycol12.gif");
                col3pic[1] = loadPic("skeletonkeycol32.gif");
                col1pic[2] = loadPic("skeletonkeycol13.gif");
                col3pic[2] = loadPic("skeletonkeycol33.gif");
                //xy for facing
                xadjust[0] = 138;
                yadjust[0] = 70;
                xadjust[1] = 89;
                yadjust[1] = 46;
                xadjust[2] = 66;
                yadjust[2] = 33;
                //xy for col1
                xadjust[3] = 94;
                yadjust[3] = 71;
                xadjust[4] = 135;
                yadjust[4] = 46;
                xadjust[5] = 159;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 10;
                yadjust[6] = 71;
                xadjust[7] = 5;
                yadjust[7] = 46;
                xadjust[8] = 4;
                yadjust[8] = 35;
            } else if (picnumber == 12) {
                facingside[0] = loadPic("gemhole1.gif");
                facingside[1] = loadPic("gemhole2.gif");
                facingside[2] = loadPic("gemhole3.gif");
                col1pic[0] = loadPic("gemholecol11.gif");
                col3pic[0] = loadPic("gemholecol31.gif");
                col1pic[1] = loadPic("gemholecol12.gif");
                col3pic[1] = loadPic("gemholecol32.gif");
                col1pic[2] = loadPic("gemholecol13.gif");
                col3pic[2] = loadPic("gemholecol33.gif");
                //xy for facing
                xadjust[0] = 130;
                yadjust[0] = 76;
                xadjust[1] = 85;
                yadjust[1] = 49;
                xadjust[2] = 62;
                yadjust[2] = 35;
                //xy for col1
                xadjust[3] = 95;
                yadjust[3] = 75;
                xadjust[4] = 135;
                yadjust[4] = 48;
                xadjust[5] = 159;
                yadjust[5] = 36;
                //xy for col3
                xadjust[6] = 13;
                yadjust[6] = 75;
                xadjust[7] = 7;
                yadjust[7] = 48;
                xadjust[8] = 6;
                yadjust[8] = 36;
            }
                     /*
                     else if (picnumber==13) {
                        facingside[0]=loadPic("eyekey1.gif");
                        facingside[1]=loadPic("eyekey2.gif");
                        facingside[2]=loadPic("eyekey3.gif");
                        col1pic[0]=loadPic("eyekeycol11.gif");
                        col3pic[0]=loadPic("eyekeycol31.gif");
                        col1pic[1]=loadPic("eyekeycol12.gif");
                        col3pic[1]=loadPic("eyekeycol32.gif");
                        col1pic[2]=loadPic("eyekeycol13.gif");
                        col3pic[2]=loadPic("eyekeycol33.gif");
                        //xy for facing
                        xadjust[0]=115;  yadjust[0]=73;
                        xadjust[1]=74;   yadjust[1]=47;
                        xadjust[2]=56;   yadjust[2]=34;
                        //xy for col1
                        xadjust[3]=92;   yadjust[3]=72;
                        xadjust[4]=132;  yadjust[4]=46;
                        xadjust[5]=158;  yadjust[5]=34;
                        //xy for col3
                        xadjust[6]=6;    yadjust[6]=72;
                        xadjust[7]=4;    yadjust[7]=46;
                        xadjust[8]=2;    yadjust[8]=34;
                     }
                     */
            else if (picnumber == 14) {
                facingside[0] = loadPic("walldrain1.gif");
                facingside[1] = loadPic("walldrain2.gif");
                facingside[2] = loadPic("walldrain3.gif");
                col1pic[0] = loadPic("walldraincol11.gif");
                col1pic[1] = loadPic("walldraincol12.gif");
                col1pic[2] = loadPic("walldraincol13.gif");
                col3pic[0] = loadPic("walldraincol31.gif");
                col3pic[1] = loadPic("walldraincol32.gif");
                col3pic[2] = loadPic("walldraincol33.gif");
                //xy for facing
                xadjust[0] = 135;
                yadjust[0] = 199;
                xadjust[1] = 89;
                yadjust[1] = 127;
                xadjust[2] = 66;
                yadjust[2] = 92;
                //xy for col1
                xadjust[3] = 97;
                yadjust[3] = 166;
                xadjust[4] = 136;
                yadjust[4] = 111;
                xadjust[5] = 160;
                yadjust[5] = 81;
                //xy for col3
                xadjust[6] = 13;
                yadjust[6] = 166;
                xadjust[7] = 7;
                yadjust[7] = 111;
                xadjust[8] = 5;
                yadjust[8] = 81;
            } else if (picnumber == 15) {
                facingside[0] = loadPic("wallcrack1.gif");
                facingside[1] = loadPic("wallcrack2.gif");
                facingside[2] = loadPic("wallcrack3.gif");
                col1pic[0] = loadPic("wallcrackcol11.gif");
                col1pic[1] = loadPic("wallcrackcol12.gif");
                col1pic[2] = blankpic;
                col3pic[0] = loadPic("wallcrackcol31.gif");
                col3pic[1] = loadPic("wallcrackcol32.gif");
                col3pic[2] = loadPic("wallcrackcol33.gif");
                //xy for facing
                xadjust[0] = 212;
                yadjust[0] = 131;
                xadjust[1] = 134;
                yadjust[1] = 86;
                xadjust[2] = 102;
                yadjust[2] = 62;
                //xy for col1
                xadjust[3] = 115;
                yadjust[3] = 114;
                xadjust[4] = 146;
                yadjust[4] = 72;
                //xy for col3
                xadjust[6] = 47;
                yadjust[6] = 129;
                xadjust[7] = 23;
                yadjust[7] = 83;
                xadjust[8] = 12;
                yadjust[8] = 58;
            } else if (picnumber == 16) {
                facingside[0] = loadPic("blockkey1.gif");
                facingside[1] = loadPic("blockkey2.gif");
                facingside[2] = loadPic("blockkey3.gif");
                col1pic[0] = loadPic("blockkeycol11.gif");
                col1pic[1] = loadPic("blockkeycol12.gif");
                col1pic[2] = loadPic("blockkeycol13.gif");
                col3pic[0] = loadPic("blockkeycol31.gif");
                col3pic[1] = loadPic("blockkeycol32.gif");
                col3pic[2] = loadPic("blockkeycol33.gif");
                //xy for facing
                xadjust[0] = 135;
                yadjust[0] = 57;
                xadjust[1] = 88;
                yadjust[1] = 37;
                xadjust[2] = 65;
                yadjust[2] = 27;
                //xy for col1
                xadjust[3] = 90;
                yadjust[3] = 58;
                xadjust[4] = 133;
                yadjust[4] = 37;
                xadjust[5] = 157;
                yadjust[5] = 28;
                //xy for col3
                xadjust[6] = 9;
                yadjust[6] = 58;
                xadjust[7] = 5;
                yadjust[7] = 37;
                xadjust[8] = 3;
                yadjust[8] = 28;
            } else if (picnumber == 17) {
                facingside[0] = loadPic("crosskey1.gif");
                facingside[1] = loadPic("crosskey2.gif");
                facingside[2] = loadPic("crosskey3.gif");
                col1pic[0] = loadPic("crosskeycol11.gif");
                col1pic[1] = loadPic("crosskeycol12.gif");
                col1pic[2] = loadPic("crosskeycol13.gif");
                col3pic[0] = loadPic("crosskeycol31.gif");
                col3pic[1] = loadPic("crosskeycol32.gif");
                col3pic[2] = loadPic("crosskeycol33.gif");
                //xy for facing
                xadjust[0] = 135;
                yadjust[0] = 76;
                xadjust[1] = 88;
                yadjust[1] = 49;
                xadjust[2] = 65;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 90;
                yadjust[3] = 74;
                xadjust[4] = 133;
                yadjust[4] = 47;
                xadjust[5] = 159;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 9;
                yadjust[6] = 74;
                xadjust[7] = 5;
                yadjust[7] = 47;
                xadjust[8] = 2;
                yadjust[8] = 35;
            } else if (picnumber == 18) {
                facingside[0] = loadPic("doublekey1.gif");
                facingside[1] = loadPic("doublekey2.gif");
                facingside[2] = loadPic("doublekey3.gif");
                col1pic[0] = loadPic("doublekeycol11.gif");
                col1pic[1] = loadPic("doublekeycol12.gif");
                col1pic[2] = loadPic("doublekeycol13.gif");
                col3pic[0] = loadPic("doublekeycol31.gif");
                col3pic[1] = loadPic("doublekeycol32.gif");
                col3pic[2] = loadPic("doublekeycol33.gif");
                //xy for facing
                xadjust[0] = 129;
                yadjust[0] = 76;
                xadjust[1] = 84;
                yadjust[1] = 49;
                xadjust[2] = 62;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 88;
                yadjust[3] = 74;
                xadjust[4] = 132;
                yadjust[4] = 47;
                xadjust[5] = 160;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 7;
                yadjust[6] = 74;
                xadjust[7] = 4;
                yadjust[7] = 47;
                xadjust[8] = 3;
                yadjust[8] = 35;
            } else if (picnumber == 19) {
                facingside[0] = loadPic("iron2key1.gif");
                facingside[1] = loadPic("iron2key2.gif");
                facingside[2] = loadPic("iron2key3.gif");
                col1pic[0] = loadPic("iron2keycol11.gif");
                col1pic[1] = loadPic("iron2keycol12.gif");
                col1pic[2] = loadPic("iron2keycol13.gif");
                col3pic[0] = loadPic("iron2keycol31.gif");
                col3pic[1] = loadPic("iron2keycol32.gif");
                col3pic[2] = loadPic("iron2keycol33.gif");
                //xy for facing
                xadjust[0] = 135;
                yadjust[0] = 76;
                xadjust[1] = 88;
                yadjust[1] = 49;
                xadjust[2] = 65;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 90;
                yadjust[3] = 74;
                xadjust[4] = 133;
                yadjust[4] = 47;
                xadjust[5] = 161;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 9;
                yadjust[6] = 74;
                xadjust[7] = 5;
                yadjust[7] = 47;
                xadjust[8] = 4;
                yadjust[8] = 35;
            } else if (picnumber == 20) {
                facingside[0] = loadPic("solidkey1.gif");
                facingside[1] = loadPic("solidkey2.gif");
                facingside[2] = loadPic("solidkey3.gif");
                col1pic[0] = loadPic("solidkeycol11.gif");
                col1pic[1] = loadPic("solidkeycol12.gif");
                col1pic[2] = loadPic("solidkeycol13.gif");
                col3pic[0] = loadPic("solidkeycol31.gif");
                col3pic[1] = loadPic("solidkeycol32.gif");
                col3pic[2] = loadPic("solidkeycol33.gif");
                //xy for facing
                xadjust[0] = 135;
                yadjust[0] = 76;
                xadjust[1] = 87;
                yadjust[1] = 49;
                xadjust[2] = 65;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 90;
                yadjust[3] = 74;
                xadjust[4] = 133;
                yadjust[4] = 47;
                xadjust[5] = 159;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 9;
                yadjust[6] = 74;
                xadjust[7] = 5;
                yadjust[7] = 47;
                xadjust[8] = 2;
                yadjust[8] = 35;
            } else if (picnumber == 21) {
                facingside[0] = loadPic("squarekey1.gif");
                facingside[1] = loadPic("squarekey2.gif");
                facingside[2] = loadPic("squarekey3.gif");
                col1pic[0] = loadPic("squarekeycol11.gif");
                col1pic[1] = loadPic("squarekeycol12.gif");
                col1pic[2] = loadPic("squarekeycol13.gif");
                col3pic[0] = loadPic("squarekeycol31.gif");
                col3pic[1] = loadPic("squarekeycol32.gif");
                col3pic[2] = loadPic("squarekeycol33.gif");
                //xy for facing
                xadjust[0] = 139;
                yadjust[0] = 76;
                xadjust[1] = 91;
                yadjust[1] = 49;
                xadjust[2] = 67;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 92;
                yadjust[3] = 74;
                xadjust[4] = 134;
                yadjust[4] = 47;
                xadjust[5] = 160;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 11;
                yadjust[6] = 74;
                xadjust[7] = 6;
                yadjust[7] = 47;
                xadjust[8] = 5;
                yadjust[8] = 35;
            } else if (picnumber == 22) {
                facingside[0] = loadPic("topazkey1.gif");
                facingside[1] = loadPic("topazkey2.gif");
                facingside[2] = loadPic("topazkey3.gif");
                col1pic[0] = loadPic("topazkeycol11.gif");
                col1pic[1] = loadPic("topazkeycol12.gif");
                col1pic[2] = loadPic("topazkeycol13.gif");
                col3pic[0] = loadPic("topazkeycol31.gif");
                col3pic[1] = loadPic("topazkeycol32.gif");
                col3pic[2] = loadPic("topazkeycol33.gif");
                //xy for facing
                xadjust[0] = 135;
                yadjust[0] = 76;
                xadjust[1] = 88;
                yadjust[1] = 49;
                xadjust[2] = 65;
                yadjust[2] = 36;
                //xy for col1
                xadjust[3] = 90;
                yadjust[3] = 74;
                xadjust[4] = 133;
                yadjust[4] = 47;
                xadjust[5] = 159;
                yadjust[5] = 35;
                //xy for col3
                xadjust[6] = 9;
                yadjust[6] = 74;
                xadjust[7] = 5;
                yadjust[7] = 47;
                xadjust[8] = 3;
                yadjust[8] = 35;
            }
        } else {
            facingside[0] = loadPic("coinslot1.gif");
            facingside[1] = loadPic("coinslot2.gif");
            facingside[2] = loadPic("coinslot3.gif");
            col1pic[0] = loadPic("coinslotcol11.gif");
            col3pic[0] = loadPic("coinslotcol31.gif");
            col1pic[1] = loadPic("coinslotcol12.gif");
            col3pic[1] = loadPic("coinslotcol32.gif");
            col1pic[2] = loadPic("coinslotcol13.gif");
            col3pic[2] = loadPic("coinslotcol33.gif");
            //xy for facing
            xadjust[0] = 147;
            yadjust[0] = 86;
            xadjust[1] = 100;
            yadjust[1] = 56;
            xadjust[2] = 73;
            yadjust[2] = 40;
            //xy for col1
            xadjust[3] = 100;
            yadjust[3] = 78;
            xadjust[4] = 138;
            yadjust[4] = 50;
            xadjust[5] = 161;
            yadjust[5] = 39;
            //xy for col3
            xadjust[6] = 15;
            yadjust[6] = 78;
            xadjust[7] = 9;
            yadjust[7] = 50;
            xadjust[8] = 6;
            yadjust[8] = 39;
        }
        if ((type == BUTTON && !ADDEDBUTTON[picnumber]) || (type == KEY && !ADDEDKEY[picnumber]) || (type == COIN && !ADDEDCOIN)) {
            tracker.addImage(facingside[0], 0);
            tracker.addImage(facingside[1], 0);
            tracker.addImage(facingside[2], 0);
            tracker.addImage(col1pic[0], 0);
            tracker.addImage(col3pic[0], 0);
            tracker.addImage(col1pic[1], 0);
            tracker.addImage(col3pic[1], 0);
            tracker.addImage(col1pic[2], 0);
            tracker.addImage(col3pic[2], 0);
            if (type == BUTTON) {
                for (int i = 0; i < pushed.length; i++) tracker.addImage(pushed[i], 0);
                ADDEDBUTTON[picnumber] = true;
            } else if (type == KEY) ADDEDKEY[picnumber] = true;
            else ADDEDCOIN = true;
        }
    }
    
    public boolean changeState() {
        boolean tempbool = true;
        if (delaying) {
            changecount++;
            if (changecount > delay) {
                if (doSwitch()) {
                    delaying = false;
                    changecount = 0;
                    if (reset == 0) tempbool = false; //remove from mapchanging
                    else resetting = true;
                } else changecount--;
            }
        } else if (resetting) {
            changecount++;
            if (changecount > reset) {
                if (resetnotrigger || doSwitch()) {
                    resetting = false;
                    changecount = 0;
                    tempbool = false; //remove from mapchanging
                    if (resetnotrigger && type == BUTTON) {
                        java.awt.Image tempimg;
                        for (int i = 0; i < pushed.length; i++) {
                            if (i < 3) {
                                tempimg = facingside[i];
                                facingside[i] = pushed[i];
                            } else if (i < 6) {
                                tempimg = col1pic[i - 3];
                                col1pic[i - 3] = pushed[i];
                            } else {
                                tempimg = col3pic[i - 6];
                                col3pic[i - 6] = pushed[i];
                            }
                            pushed[i] = tempimg;
                        }
                        dmnew.needredraw = true;
                    }
                } else changecount--;
            }
        } else tempbool = false;
        return tempbool;
    }
    
    public void toggle() {
        if (delaying || resetting) return; //do nothing if switch state changing
        if (delay > 0 || !doSwitch()) {
            delaying = true;
            if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
            dmnew.mapchanging = true;
            if (type == BUTTON) {
                java.awt.Image tempimg;
                for (int i = 0; i < pushed.length; i++) {
                    if (i < 3) {
                        tempimg = facingside[i];
                        facingside[i] = pushed[i];
                    } else if (i < 6) {
                        tempimg = col1pic[i - 3];
                        col1pic[i - 3] = pushed[i];
                    } else {
                        tempimg = col3pic[i - 6];
                        col3pic[i - 6] = pushed[i];
                    }
                    pushed[i] = tempimg;
                }
                dmnew.needredraw = true;
            }
            return;
        } else if (reset > 0) {
            resetting = true;
            if (!dmnew.mapstochange.contains(xy)) dmnew.mapstochange.add(xy);
            dmnew.mapchanging = true;
        }
    }
    
    public void activate() {
        toggle();
    }
    //public void activate() { toggle(); }
    //public void toggle() {
    //        if (!delaying && !resetting) doSwitch();
    //}
    //public void deactivate() { toggle(); }
    
    protected boolean doSwitch() {
                /*if (actiontype==0) {
                        dmnew.DungeonMap[targetlevel][targetx][targety].toggle();
                }
                else if (actiontype==1) {
                        dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                }
                else if (actiontype==2) {
                        dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                }*/
        if (actiontype == 0) {
            dmnew.DungeonMap[targetlevel][targetx][targety].toggle();
            switchstate = !switchstate;
        } else if (actiontype == 1) {
            dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            switchstate = !switchstate;
        } else if (actiontype == 2) {
            dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            switchstate = !switchstate;
        } else if (actiontype == 3) {
            if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            else dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            switchstate = !switchstate;
        } else if (actiontype == 4) {
            if (!switchstate) dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
            else dmnew.DungeonMap[targetlevel][targetx][targety].activate();
            switchstate = !switchstate;
        }
                /*
                else if (actiontype==1) {
                        if (resetting) dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                        else dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                        switchstate = !switchstate;
                }
                else if (actiontype==2) {
                        if (resetting) dmnew.DungeonMap[targetlevel][targetx][targety].activate();
                        else dmnew.DungeonMap[targetlevel][targetx][targety].deactivate();
                        switchstate = !switchstate;
                }
                */
        else if (actiontype == 5) {
            if (!switchstate) {
                if (!changeto.isPassable || changeto.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) return false;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) return false;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    return false;
                //if (!delaying) {
                oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                switchstate = !switchstate;
                if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                if (oldMapObject.hasCloud) {
                    oldMapObject.hasCloud = false;
                    changeto.hasCloud = true;
                }
                if (oldMapObject.numProjs > 0) {
                    changeto.numProjs = oldMapObject.numProjs;
                    oldMapObject.numProjs = 0;
                }
                if (oldMapObject.hasMons) {
                    changeto.hasMons = true;
                    oldMapObject.hasMons = false;
                }
                if (oldMapObject.hasParty) {
                    changeto.hasParty = true;
                    oldMapObject.hasParty = false;
                }
                if (retainitems && oldMapObject.hasItems) {
                    while (oldMapObject.mapItems.size() > 0) {
                        changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                    }
                    changeto.hasItems = true;
                    oldMapObject.hasItems = false;
                }
                changeto.doAction();
                //}
            } else {
                if (!oldMapObject.isPassable || oldMapObject.mapchar == '>') {
                    if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) return false;
                    else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                        int i = 0;
                        dmnew.Monster tempmon;
                        while (i < 6) {
                            tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                            if (tempmon != null && !tempmon.isImmaterial) return false;
                            else if (i == 3) i = 5;
                            else i++;
                        }
                    }
                }
                if (!oldMapObject.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0)
                    return false;
                //if (!delaying) {
                dmnew.DungeonMap[targetlevel][targetx][targety] = oldMapObject;
                switchstate = !switchstate;
                if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                if (changeto.hasCloud) {
                    changeto.hasCloud = false;
                    oldMapObject.hasCloud = true;
                }
                if (changeto.numProjs > 0) {
                    oldMapObject.numProjs = changeto.numProjs;
                    changeto.numProjs = 0;
                }
                if (changeto.hasMons) {
                    oldMapObject.hasMons = true;
                    changeto.hasMons = false;
                }
                if (changeto.hasParty) {
                    oldMapObject.hasParty = true;
                    changeto.hasParty = false;
                }
                if (retainitems && changeto.hasItems) {
                    while (changeto.mapItems.size() > 0) {
                        oldMapObject.addItem((Item) changeto.mapItems.remove(0));
                    }
                    oldMapObject.hasItems = true;
                    changeto.hasItems = false;
                }
                oldMapObject.doAction();
                //}
            }
                        /*
                        //Vector tempit = dmnew.levelMap[targetx][targety].mapItems;
                        if (!switchstate) {
                                if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { return false; }
                                else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                        int i=0;
                                        dmnew.Monster tempmon;
                                        while (i<6) {
                                                tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                                if (tempmon!=null && !tempmon.isImmaterial) return false;
                                                else if (i==3) i=5;
                                                else i++;
                                        }
                                }
                                else {
                                     oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                                     dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                                     switchstate = !switchstate;
                                     if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                                     if (oldMapObject.hasCloud) {
                                        oldMapObject.hasCloud=false;
                                        changeto.hasCloud=true;
                                     }
                                     //transfer immaterial mons to new square
                                     //if (oldMapObject.hasImmaterialMons) {
                                     //   changeto.hasImmaterialMons = true;
                                     //   changeto.hasMons = true;
                                     //   oldMapObject.hasImmaterialMons = false;
                                     //   oldMapObject.hasMons = false;
                                     //}
                                }
                        }
                        else {
                                if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { return false; }
                                else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                        int i=0;
                                        dmnew.Monster tempmon;
                                        while (i<6) {
                                                tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                                if (tempmon!=null && !tempmon.isImmaterial) return false;
                                                else if (i==3) i=5;
                                                else i++;
                                        }
                                }
                                else {
                                     dmnew.DungeonMap[targetlevel][targetx][targety] = oldMapObject;
                                     switchstate = !switchstate;
                                     if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                                     if (changeto.hasCloud) {
                                        changeto.hasCloud=false;
                                        oldMapObject.hasCloud=true;
                                     }
                                     //transfer immaterial mons to new square
                                     //if (changeto.hasImmaterialMons) {
                                     //   oldMapObject.hasImmaterialMons = true;
                                     //   oldMapObject.hasMons = true;
                                     //   changeto.hasImmaterialMons = false;
                                     //   changeto.hasMons = false;
                                     //}
                                }
                        }
                        //dmnew.levelMap[targetx][targety].mapItems = tempit;
                        */
        } else if (actiontype == 6) {
            if (!changeto.isPassable || changeto.mapchar == '>') {
                if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty) return false;
                else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                    int i = 0;
                    dmnew.Monster tempmon;
                    while (i < 6) {
                        tempmon = (dmnew.Monster) dmnew.dmmons.get(targetlevel + "," + targetx + "," + targety + "," + i);
                        if (tempmon != null && !tempmon.isImmaterial) return false;
                        else if (i == 3) i = 5;
                        else i++;
                    }
                }
            }
            if (!changeto.canPassProjs && dmnew.DungeonMap[targetlevel][targetx][targety].numProjs > 0) return false;
            //if (!delaying) {
            oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
            dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
            switchstate = !switchstate;
            if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
            if (oldMapObject.hasCloud) {
                oldMapObject.hasCloud = false;
                changeto.hasCloud = true;
            }
            if (oldMapObject.numProjs > 0) {
                changeto.numProjs = oldMapObject.numProjs;
                oldMapObject.numProjs = 0;
            }
            if (oldMapObject.hasMons) {
                changeto.hasMons = true;
                oldMapObject.hasMons = false;
            }
            if (oldMapObject.hasParty) {
                changeto.hasParty = true;
                oldMapObject.hasParty = false;
            }
            if (retainitems && oldMapObject.hasItems) {
                while (oldMapObject.mapItems.size() > 0) {
                    changeto.addItem((Item) oldMapObject.mapItems.remove(0));
                }
                changeto.hasItems = true;
                oldMapObject.hasItems = false;
            }
            changeto.doAction();
            //}
                        /*
                        if (dmnew.DungeonMap[targetlevel][targetx][targety].hasParty || dmnew.DungeonMap[targetlevel][targetx][targety].numProjs>0) { return false; }
                        else if (dmnew.DungeonMap[targetlevel][targetx][targety].hasMons) {
                                int i=0;
                                dmnew.Monster tempmon;
                                while (i<6 && !delaying) {
                                        tempmon = (dmnew.Monster)dmnew.dmmons.get(targetlevel+","+targetx+","+targety+","+i);
                                        if (tempmon!=null && !tempmon.isImmaterial) return false;
                                        else if (i==3) i=5;
                                        else i++;
                                }
                        }
                        oldMapObject = dmnew.DungeonMap[targetlevel][targetx][targety];
                        dmnew.DungeonMap[targetlevel][targetx][targety] = changeto;
                        switchstate = !switchstate;
                        if (!dmnew.mapstochange.contains(target)) dmnew.mapstochange.add(target);
                        if (oldMapObject.hasCloud) {
                                oldMapObject.hasCloud=false;
                                changeto.hasCloud=true;
                        }
                        */
        } else if (actiontype == 7) {
            dmnew.playSound(soundstring, targetx, targety, loopsound);//when looping added
            //dmnew.playSound(soundstring,targetx,targety);
        } else if (actiontype == 8)
            dmnew.stopSounds(abrupt);//stop all looping sounds, if !abrupt let them play out first
        if (!delaying && type == BUTTON) {
            java.awt.Image tempimg;
            for (int i = 0; i < pushed.length; i++) {
                if (i < 3) {
                    tempimg = facingside[i];
                    facingside[i] = pushed[i];
                } else if (i < 6) {
                    tempimg = col1pic[i - 3];
                    col1pic[i - 3] = pushed[i];
                } else {
                    tempimg = col3pic[i - 6];
                    col3pic[i - 6] = pushed[i];
                }
                pushed[i] = tempimg;
            }
        }
        dmnew.needredraw = true;
        return true;
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
        target = new MapPoint(targetlevel, targetx, targety);
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
        switchstate = si.readBoolean();
        wasUsed = si.readBoolean();
        actiontype = si.readInt();
        //if (actiontype>=5 && actiontype<7 && (isReusable || !wasUsed)) {
        if (actiontype == 5 || actiontype == 6) {
            changeto = dmnew.loadMapObject(si);
            if (switchstate && actiontype == 5) oldMapObject = dmnew.loadMapObject(si);
            retainitems = si.readBoolean();
        } else if (actiontype == 7) {
            soundstring = si.readUTF();
            loopsound = si.readInt();
        } else if (actiontype == 8) abrupt = si.readBoolean();
        setPics();
        if (type == BUTTON && switchstate) {
            java.awt.Image tempimg;
            for (int i = 0; i < pushed.length; i++) {
                if (i < 3) {
                    tempimg = facingside[i];
                    facingside[i] = pushed[i];
                } else if (i < 6) {
                    tempimg = col1pic[i - 3];
                    col1pic[i - 3] = pushed[i];
                } else {
                    tempimg = col3pic[i - 6];
                    col3pic[i - 6] = pushed[i];
                }
                pushed[i] = tempimg;
            }
        }
    }
}
