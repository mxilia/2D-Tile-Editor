package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MouseMotionRegister extends MouseMotionAdapter {
    Panel p;
    public int pressedX, pressedY;
    public boolean leftPressed, rightPressed;

    public MouseMotionRegister(Panel p) {
        this.p = p;
    }

    public void moveMouse(int x, int y) {
        if(x+p.map.userX>=0 && y+p.map.userY>=0 && x+p.map.userX<p.map.mapWidth && y+p.map.userY<p.map.mapHeight && !p.resp.isObject){
            p.map.tileX = (x+p.map.userX)/p.map.tileSize;
            p.map.tileY = (y+p.map.userY)/p.map.tileSize;
            p.map.onTile = true;
        }
        else p.map.onTile = false;
        p.map.onObject = p.resp.isObject;
        p.map.updateObjPos(x, y);
        p.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(p.map.currentFileDirectory.isEmpty()) return;
        moveMouse(x, y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(p.map.currentFileDirectory.isEmpty()) return;
        moveMouse(x, y);
        if(x>p.map.x && y>p.map.y && x<p.map.x+p.map.width && y<y+p.map.height && rightPressed){
            p.map.userX-=x-pressedX;
            p.map.userY-=y-pressedY;
            pressedX = x;
            pressedY = y;
        }
        if(x>p.map.x && y>p.map.y && x<p.map.x+p.map.width && y<y+p.map.height && leftPressed){
           if(!p.resp.isObject){
                if(!p.map.onTile) return;
                if(p.resp.currentTile != p.map.mapNum[p.map.tileY][p.map.tileX]){
                    p.actRecord.tilePlaced(p.resp.currentTile, p.map.mapNum[p.map.tileY][p.map.tileX], p.map.tileX, p.map.tileY);
                }
                p.map.mapNum[p.map.tileY][p.map.tileX] = p.resp.currentTile;
            }
        }
        p.repaint();
    }
}
