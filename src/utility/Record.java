package utility;

public class Record {
    int currentId;
    int prevId;
    int tileX;
    int tileY;
    int scale;

    public Record(int currentId, int prevId, int tileX, int tileY, int scale) {
        this.currentId = currentId;
        this.prevId = prevId;
        this.tileX = tileX;
        this.tileY = tileY;
        this.scale = scale;
    }

    public void print() {
        System.out.println(currentId + " " + prevId + " " + tileX + " " + tileY);
    }
}
