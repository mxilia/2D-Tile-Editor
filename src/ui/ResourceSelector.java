package ui;

import main.Panel;
import utility.UtilFunc;

import java.awt.*;

public class ResourceSelector extends UserInterface {
    public int itemLength = 100;
    public int numDisplay = 5;

    public boolean searching = false;

    public int searchedItem = -1;
    public ResourceSelector(Panel p, int x, int y, int width, int height) {
        super(p);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void searchItem(String str) {
        if(p.resp.isObject){
            if(p.om.img.isEmpty()) return;
            int l=0, r=p.om.imgCount-1;
            while(l<r){
                int mid = (l+r)>>1;
                if(p.om.fileName.get(mid).getFirst().compareTo(str)>=0) r = mid;
                else l = mid+1;
            }
            if(p.om.fileName.get(l).getFirst().compareTo(str)==0) searchedItem = p.om.fileName.get(l).getSecond();
            else searchedItem = -1;
        }
        else {
            if(p.tm.img.isEmpty()) return;
            int l=0, r=p.tm.imgCount-1;
            while(l<r){
                int mid = (l+r)>>1;
                if(p.tm.fileName.get(mid).getFirst().compareTo(str)>=0) r = mid;
                else l = mid+1;
            }
            if(p.tm.fileName.get(l).getFirst().compareTo(str)==0) searchedItem = p.tm.fileName.get(l).getSecond();
            else searchedItem = -1;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.darkGray);
        g2.fillRect(x, y, width, height);
        if(p.resp.isObject){
            if(p.om.img.isEmpty()) return;
            if(!searching){
                int start = Math.max(0, p.resp.objUserY/itemLength-1);
                int end = Math.min(p.om.imgCount-1, p.resp.objUserY/itemLength+numDisplay);
                for(int i=start;i<=end;i++){
                    g2.drawImage(p.om.img.get(i), x+32, y+i*itemLength+37-p.resp.objUserY, itemLength-35, itemLength-35, null);
                    UtilFunc.drawBorder(g2, x+15, y+i*itemLength+29-p.resp.objUserY, width-29, itemLength-10, 1, 15, Color.white);
                    g2.drawString(p.om.displayName.get(i), x+32, y+i*itemLength+115-p.resp.objUserY);
                    if(p.resp.currentObj == i) UtilFunc.drawHighlight(g2, x+6, y+i*itemLength+22-p.resp.objUserY, width-11, itemLength+4, Color.CYAN, Color.white, 0.1f);
                }
            }
            else {
                if(searchedItem == -1) return;
                g2.drawImage(p.om.img.get(searchedItem), x+32, y+37, itemLength-35, itemLength-35, null);
                UtilFunc.drawBorder(g2, x+15, y+29, width-29, itemLength-10, 1, 15, Color.white);
                g2.drawString(p.om.displayName.get(searchedItem), x+32, y+115);
                if(p.resp.currentObj == searchedItem) UtilFunc.drawHighlight(g2, x+6, y+22, width-11, itemLength+4, Color.CYAN, Color.white, 0.1f);
            }
        }
        else {
            if(p.tm.img.isEmpty()) return;
            if(!searching){
                int start = Math.max(0, p.resp.tileUserY /itemLength-1);
                int end = Math.min(p.tm.imgCount-1, p.resp.tileUserY /itemLength+numDisplay);
                for(int i=start;i<=end;i++){
                    g2.drawImage(p.tm.img.get(i), x+32, y+i*itemLength+37-p.resp.tileUserY, itemLength-35, itemLength-35, null);
                    UtilFunc.drawBorder(g2, x+15, y+i*itemLength+29-p.resp.tileUserY, width-29, itemLength-10, 1, 15, Color.white);
                    g2.drawString(p.tm.displayName.get(i), x+32, y+i*itemLength+115-p.resp.tileUserY);
                    if(p.resp.currentTile == i+1) UtilFunc.drawHighlight(g2, x+6, y+i*itemLength+22-p.resp.tileUserY, width-11, itemLength+4, Color.CYAN, Color.white, 0.1f);
                }
            }
            else {
                if(searchedItem == -1) return;
                g2.drawImage(p.tm.img.get(searchedItem), x+32, y+37, itemLength-35, itemLength-35, null);
                UtilFunc.drawBorder(g2, x+15, y+29, width-29, itemLength-10, 1, 15, Color.white);
                g2.drawString(p.tm.displayName.get(searchedItem), x+32, y+115);
                if(p.resp.currentTile == searchedItem+1) UtilFunc.drawHighlight(g2, x+6, y+22, width-11, itemLength+4, Color.CYAN, Color.white, 0.1f);
            }
        }
    }
}
