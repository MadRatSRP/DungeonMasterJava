import javax.sound.sampled.Clip;

public class LoopSound {
    public Clip clip;
    public String clipfile;
    public int x, y, loop, count;
    
    public LoopSound(Clip clip, String clipfile, int x, int y, int loop, int count) {
        this.clip = clip;
        this.clipfile = clipfile;
        this.x = x;
        this.y = y;
        this.loop = loop;
        this.count = count;
    }
        
        /*
        public boolean equals(Object obj) {
                if (obj instanceof LoopSound && ((LoopSound)obj).clipfile.equals(clipfile)) return true;
                else return false;
        }
        */
}