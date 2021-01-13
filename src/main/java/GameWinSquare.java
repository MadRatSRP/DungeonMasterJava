//win the game if you step on it

import java.io.IOException;
import java.io.ObjectOutputStream;

class GameWinSquare extends Floor {
    
    private String endanim, endsound;//gif ending animation and sound to play
    
    //construct a game winner - default of 0 for endnum
    //public GameWinSquare() { this("end0.gif","end0.wav"); }
    
    //construct a game winner with specified endnum
    public GameWinSquare(String ea, String es) {
        super();
        mapchar = 'W';
        endanim = ea;
        endsound = es;
    }
    
    //party steps on
    public void tryFloorSwitch(int onoff) {
        if (onoff != PARTYSTEPPINGON) return;
        dmnew.endanim = endanim;
        dmnew.endsound = endsound;
        dmnew.nomovement = true;
        dmnew.gameover = true;
    }
    
    public void doAction() {
        tryFloorSwitch(PARTYSTEPPINGON);
    }
    
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeUTF(endanim);
        so.writeUTF(endsound);
    }
    
}
