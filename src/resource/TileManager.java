package resource;

import main.Panel;
import utility.UtilFunc;

import java.awt.*;

public class TileManager extends Manager {

    public TileManager(Panel p, String resType) {
        super(p, resType);
    }

    @Override
    public void draw(Graphics2D g2) {
        int tileSize = p.map.tileSize;
        int topX = p.map.userX, topY = p.map.userY;
        int bottomX = topX+editorWidth, bottomY = topY+editorHeight;
        int pixelTopX = Math.max(0, topX/tileSize), pixelTopY = Math.max(0, topY/tileSize);
        int pixelBottomX = Math.min(p.map.col, bottomX/tileSize+1), pixelBottomY = Math.min(p.map.row, bottomY/tileSize+1);
        for(int i=pixelTopY;i<pixelBottomY;i++){
            for(int j=pixelTopX;j<pixelBottomX;j++){
                if(p.map.mapNum[i][j]==0 || p.map.mapNum[i][j]>imgCount) continue;
                g2.drawImage(p.tm.img.get(p.map.mapNum[i][j]-1),
                          j*tileSize-topX,
                          i*tileSize-topY,
                             tileSize,
                             tileSize, null
                            );
            }
        }
        if(p.map.onTile){
            if(p.resp.currentTile>0){
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.drawImage(p.tm.img.get(p.resp.currentTile -1),
                          p.map.tileX*tileSize-topX,
                          p.map.tileY*tileSize-topY,
                             tileSize,
                             tileSize, null
                             );
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
            UtilFunc.drawBorder(g2, p.map.tileX*tileSize-topX, p.map.tileY*tileSize-topY, tileSize, tileSize, 2, 15, Color.white);
        }
    }
}
