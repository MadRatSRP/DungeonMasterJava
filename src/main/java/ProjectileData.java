class ProjectileData {
    public Item it;
    public Spell sp;
    public int level, x, y, dist, direction, subsquare, powdrain, powcount;
    public boolean justthrown, notelnext, isending;
    
    public ProjectileData(Item i, int lvl, int x, int y, int d, int dr, int subs, boolean jt, boolean ntn) {
        it = i;
        this.level = lvl;
        this.x = x;
        this.y = y;
        dist = d;
        direction = dr;
        subsquare = subs;
        justthrown = jt;
        notelnext = ntn;
    }
    
    public ProjectileData(Spell s, int lvl, int x, int y, int d, int dr, int subs, int pd, int pc, boolean jt, boolean ntn) {
        sp = s;
        this.level = lvl;
        this.x = x;
        this.y = y;
        dist = d;
        direction = dr;
        subsquare = subs;
        powdrain = pd;
        powcount = pc;
        justthrown = jt;
        notelnext = ntn;
    }
}