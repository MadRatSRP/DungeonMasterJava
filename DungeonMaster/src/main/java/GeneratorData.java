import java.io.IOException;
import java.io.ObjectOutputStream;

class GeneratorData extends FloorData {
    public MonsterData monster;
    public boolean monsterhnr, monsterisim;
    public int genrate, gencounter, gencounter2;
    public int numtogen, maxgen;
    public int level, xcoord, ycoord;
    public int count, maxcount;
    public boolean resetcount;
    public boolean isContinuous = false, isActive = true, delaying = false;
    
    //lvl,x,y - level and xy coords of this generator
    //genr - generation rate (how fast a continuous generator spits them out, 0 if not continuos)
    //genc - gencounter
    public GeneratorData(int lvl, int x, int y, int numgen, int mgen, int genr, boolean isac, int genc, int genc2, int mcnt, int cnt, boolean rcnt, MonsterData mon) {
        super();
        mapchar = 'g';
        isPassable = true;
        canPassProjs = true;
        canHoldItems = true;
        drawItems = true;
        drawFurtherItems = true;
        level = lvl;
        xcoord = x;
        ycoord = y;
        numtogen = numgen;
        maxgen = mgen;
        genrate = genr;
        if (genrate > 0) isContinuous = true;
        isActive = isac;
        gencounter = genc;
        gencounter2 = genc2;
        monster = mon;
        maxcount = mcnt;
        count = cnt;
        resetcount = rcnt;
    }
    
    public void changeLevel(int amt, int lvlnum) {
        level += amt;
        monster.level += amt;
    }
    
    public void setMapCoord(int level, int x, int y) {
        this.level = level;
        xcoord = x;
        ycoord = y;
        monster.level = level;
        monster.x = x;
        monster.y = y;
    }
    
    public String toString() {
        return "Monster Generator (" + monster.name + ")";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeInt(level);
        so.writeInt(xcoord);
        so.writeInt(ycoord);
        so.writeInt(numtogen);
        so.writeInt(maxgen);
        so.writeInt(genrate);
        so.writeBoolean(isActive);
        so.writeInt(gencounter);
        so.writeInt(gencounter2);
        so.writeInt(maxcount);
        so.writeInt(count);
        so.writeBoolean(resetcount);
        monster.save(so);
        //so.writeBoolean(false);//delaying
        so.writeBoolean(delaying);
    }
}