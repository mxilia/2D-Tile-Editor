package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseClickRegister implements MouseListener {
    Panel p;
    int clickX, clickY;

    public MouseClickRegister(Panel p) {
        this.p = p;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(p.map.currentFileDirectory.isEmpty()) return;
        int x = e.getX();
        int y = e.getY();
        if(e.getButton() == MouseEvent.BUTTON3){
            if(x>p.map.x && y>p.map.y && x<p.map.x+p.map.width && y<y+p.map.height){
                p.motionRegister.pressedX = x;
                p.motionRegister.pressedY = y;
                p.motionRegister.rightPressed = true;
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON1){
            clickX = x;
            clickY = y;
            p.motionRegister.leftPressed = true;
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        p.motionRegister.leftPressed = false;
        p.motionRegister.rightPressed = false;
        if(x>p.resp.resSelect.x && y>p.resp.resSelect.y+p.resp.objSign.height
            && x<p.resp.resSelect.x+p.resp.resSelect.width && y<p.resp.resSelect.y+p.resp.objSign.height+p.resp.height){
            if(p.resp.isObject){
                if(p.resp.resSelect.searching) p.resp.currentObj = p.resp.resSelect.searchedItem;
                else p.resp.currentObj = Math.min(p.om.imgCount-1, (p.resp.objUserY+(y-p.resp.resSelect.y-25))/p.resp.resSelect.itemLength);
            }
            else {
                if(p.resp.resSelect.searching) p.resp.currentTile = p.resp.resSelect.searchedItem+1;
                else p.resp.currentTile = Math.min(p.tm.imgCount, (p.resp.tileUserY +(y-p.resp.resSelect.y-25))/p.resp.resSelect.itemLength+1);
            }
            p.eraserOn = false;
        }
        else if(x>p.resp.tileSign.x && y>p.resp.tileSign.y && x<p.resp.tileSign.x+p.resp.tileSign.width && y<p.resp.tileSign.y+p.resp.tileSign.height){
            if(p.resp.isObject){
                p.resp.resSelect.searching = false;
                p.resp.resSelect.searchedItem = -1;
                p.resp.searchBar.button.requestFocusInWindow();
            }
            p.resp.isObject = false;
        }
        else if(x>p.resp.objSign.x && y>p.resp.objSign.y && x<p.resp.objSign.x+p.resp.objSign.width && y<p.resp.objSign.y+p.resp.objSign.height){
            if(!p.resp.isObject){
                p.resp.resSelect.searching = false;
                p.resp.resSelect.searchedItem = -1;
                p.resp.searchBar.button.requestFocusInWindow();
            }
            p.resp.isObject = true;
        }
        p.repaint();
        if(p.map.currentFileDirectory.isEmpty()) return;
        p.motionRegister.rightPressed = false;
        if(e.getButton() != MouseEvent.BUTTON1) return;
        if(x>p.map.x && y>p.map.y && x<p.map.x+p.map.width && y<y+p.map.height){
            if(Math.abs(clickX-x)>=p.map.tileSize || Math.abs(clickY-y)>=p.map.tileSize) return;
            if(p.resp.isObject){
                if(!p.map.onObject) return;
                if(p.resp.currentObj>=0) p.om.placeObj(p.resp.currentObj, p.map.objX, p.map.objY, p.map.tileSize);
                else if(p.resp.currentObj == -1 && p.om.deleteObj(p.map.objX, p.map.objY)) p.repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
