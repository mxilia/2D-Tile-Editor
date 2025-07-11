package main;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelRegister implements MouseWheelListener {
    Panel p;

    public MouseWheelRegister(Panel p) {
        this.p = p;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();  // Number of notches the wheel has moved
        int x = e.getX();
        int y = e.getY();
        // Zoom in map
        if(x>p.map.x && y>p.map.y && x<p.map.x+p.map.width && y<y+p.map.height){
            if(p.map.currentFileDirectory.isEmpty()) return;
            if(notches < 0) p.map.updateScale(1);
            else p.map.updateScale(-1);
            p.map.updateObjPos(x, y);
            p.repaint();
        }
        // Scrolling Tab
        if(x>p.resp.resSelect.x && y>p.resp.resSelect.y+p.resp.objSign.height
           && x<p.resp.resSelect.x+p.resp.resSelect.width && y<p.resp.resSelect.y+p.resp.objSign.height+p.resp.height){
            if(p.resp.isObject){
                if(notches < 0) p.resp.objUserY-=45;
                else p.resp.objUserY+=45;
                p.resp.objUserY = Math.max(0, p.resp.objUserY);
                p.resp.objUserY = Math.min(Math.max(0,p.om.imgCount-p.resp.resSelect.numDisplay)*p.resp.resSelect.itemLength, p.resp.objUserY);
            }
            else {
                if(notches < 0) p.resp.tileUserY-=45;
                else p.resp.tileUserY+=45;
                p.resp.tileUserY = Math.max(0, p.resp.tileUserY);
                p.resp.tileUserY = Math.min(Math.max(0,p.tm.imgCount-p.resp.resSelect.numDisplay)*p.resp.resSelect.itemLength, p.resp.tileUserY);
            }
            p.repaint();
        }
    }
}
