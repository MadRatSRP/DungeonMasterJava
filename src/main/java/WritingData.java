import java.io.IOException;
import java.io.ObjectOutputStream;

class WritingData extends SidedWallData {
    public String[] message;
    
    public WritingData(int sde, String[] mes) {
        super(sde);
        mapchar = 'w';
        message = mes;
    }
    
    public String toString() {
        if (side == 0) return "Writing (" + message[0] + "...) Facing South";
        else if (side == 1) return "Writing (" + message[0] + "...) Facing East";
        else if (side == 2) return "Writing (" + message[0] + "...) Facing North";
        else return "Writing (" + message[0] + "...) Facing West";
    }
    
    public void save(ObjectOutputStream so) throws IOException {
        super.save(so);
        so.writeObject(message);
    }
}