import java.io.IOException;
import java.io.ObjectOutputStream;

class GameWinData extends FloorData {
    
    public String endanim, endsound;//gif ending animation and music to play
    
    //construct a game winner with specified endnum
    public GameWinData(String ea, String es) {
        super();
        mapchar = 'W';
        endanim = ea;
        endsound = es;
    }
    
    public String toString() {
        return "GameWin (" + endanim + ", " + endsound + ")";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeUTF(endanim);
        so.writeUTF(endsound);
    }
    
}
