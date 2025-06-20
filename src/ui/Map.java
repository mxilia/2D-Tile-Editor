package ui;

import main.Panel;

import java.awt.*;

public class Map extends UserInterface {
    final public int maxCol = 500, maxRow = 500;
    public int userX = 0, userY = 0, userScale = 1;
    public int tileX, tileY, objX, objY;
    public int mapWidth, mapHeight, tileSize;
    public int col, row;
    public int[][] mapNum;
    public String currentFileDirectory = "";

    public boolean gridOn = true;
    public boolean onTile = false;
    public boolean onObject = false;

    public Map(Panel p) {
        super(p);
        setDefaultValue();
    }

    void setDefaultValue() {
        x = 0;
        y = 0;
        width = p.width-151;
        height = p.height-y;
        col=row=0;
        mapNum = new int[500][500];
    }

    public void changeMap(int row, int col) {
        this.row = row;
        this.col = col;
        userScale = 1;
        userX = 0;
        userY = 0;
        updateScale(0);
    }

    public void updateObjPos(int mouseX, int mouseY) {
        objX = (mouseX+userX)/(p.om.incrementValue*userScale)*p.om.incrementValue*userScale;
        objY = (mouseY+userY)/(p.om.incrementValue*userScale)*p.om.incrementValue*userScale;
    }

    public void updateScale(int z) {
        userScale+=z;
        userScale = Math.max(1, userScale);
        tileSize = p.baseTileLength*userScale;
        mapWidth = col*tileSize;
        mapHeight = row*tileSize;
    }

    void drawGrid(Graphics2D g2) {
        int tileSize = p.map.tileSize;
        int topX = p.map.userX, topY = p.map.userY;
        int bottomX = topX+p.tm.editorWidth, bottomY = topY+p.tm.editorHeight;
        int pixelTopX = Math.max(0, topX/tileSize), pixelTopY = Math.max(0, topY/tileSize);
        int pixelBottomX = Math.min(p.map.col, bottomX/tileSize+1), pixelBottomY = Math.min(p.map.row, bottomY/tileSize+1);
        g2.setColor(Color.white);
        if(p.map.gridOn){
            for(int i=pixelTopY;i<=pixelBottomY;i++){
                for(int j=pixelTopX;j<=pixelBottomX;j++){
                    if(i<pixelBottomY) g2.fillRect(j*tileSize-topX, i*tileSize-topY, 1, tileSize);
                    if(j<pixelBottomX) g2.fillRect(j*tileSize-topX, i*tileSize-topY, tileSize, 1);
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.gray);
        g2.fillRect(x, y, width, height);
        if(!currentFileDirectory.isEmpty()){
            p.tm.draw(g2);
            p.om.draw(g2);
            drawGrid(g2);
        }
    }
}
