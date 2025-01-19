package utility;

public class ObjScale {
    public int id;
    public int x;
    public int y;
    public int height;
    public int tileSize;

    public ObjScale(int first, int second, int third, int forth, int fifth) {
        this.id = first;
        this.x = second;
        this.y = third;
        this.height = forth;
        this.tileSize = fifth;
    }

    public int compare() {
        return (int)Math.floor((double)(y)/(double)tileSize*10.0)+height;
    }
}