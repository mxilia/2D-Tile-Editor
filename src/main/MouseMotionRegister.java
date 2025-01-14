package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MouseMotionRegister extends MouseMotionAdapter {
    Panel p;
    public int pressedX, pressedY;
    public boolean pressed;

    public MouseMotionRegister(Panel p) {
        this.p = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(p.map.currentFileDirectory.isEmpty()) return;
        if(x+p.map.userX>=0 && y+p.map.userY>=0 && x+p.map.userX<p.map.mapWidth && y+p.map.userY<p.map.mapHeight && !p.resp.isObject){
            p.map.tileX = (x+p.map.userX)/p.map.tileSize;
            p.map.tileY = (y+p.map.userY)/p.map.tileSize;
            p.map.onTile = true;
        }
        else p.map.onTile = false;
        p.map.onObject = p.resp.isObject;
        p.map.objX = x+p.map.userX;
        p.map.objY = y+p.map.userY;
        p.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!pressed) return;
        int x = e.getX();
        int y = e.getY();
        if(x>p.map.x && y>p.map.y && x<p.map.x+p.map.width && y<y+p.map.height){
            p.map.userX-=x-pressedX;
            p.map.userY-=y-pressedY;
            pressedX = x;
            pressedY = y;
            p.repaint();
        }
        else pressed = false;
    }
}
