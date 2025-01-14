package utility;

import main.Panel;

import java.util.ArrayDeque;
import java.util.Deque;

public class ActivityRecorder {
    Panel p;

    Deque<Record> done = new ArrayDeque<>();
    Deque<Record> undone = new ArrayDeque<>();
    final int maxCap = 10000;

    public ActivityRecorder(Panel p) {
        this.p = p;
    }

    public void tilePlaced(int currentId, int prevId, int pixelX, int pixelY) {
        done.addLast(new Record(currentId, prevId, pixelX, pixelY, 0));
        if(done.size()>maxCap) done.removeFirst();
        if(!undone.isEmpty()) undone.clear();
    }

    public void objPlaced(int currentId, int prevId, int pixelX, int pixelY, int scale) {
        done.addLast(new Record(currentId, prevId, pixelX, pixelY, scale));
        if(done.size()>maxCap) done.removeFirst();
        if(!undone.isEmpty()) undone.clear();
    }

    public void clear() {
        done.clear();
        undone.clear();
    }

    public void undo() {
        if(done.isEmpty()) return;
        Record current = done.removeLast();
        if(current.prevId==-1){
            for(int i=0;i<p.om.objList.size();i++){
                ObjScale tuple = p.om.objList.get(i);
                if(current.currentId==tuple.id && current.tileX==tuple.x && current.tileY==tuple.y && current.scale==tuple.tileSize){
                    p.om.objList.remove(i);
                    break;
                }
            }
        }
        else if(current.prevId==-2){
            p.om.placeObj(current.currentId, current.tileX, current.tileY, current.scale, false);
        }
        else p.map.mapNum[current.tileY][current.tileX] = current.prevId;
        undone.addLast(current);
        p.repaint();
    }

    public void redo() {
        if(undone.isEmpty()) return;
        Record current = undone.removeLast();
        if(current.prevId==-2){
            for(int i=0;i<p.om.objList.size();i++){
                ObjScale tuple = p.om.objList.get(i);
                if(current.currentId==tuple.id && current.tileX==tuple.x && current.tileY== tuple.y && current.scale==tuple.tileSize){
                    p.om.objList.remove(i);
                    break;
                }
            }
        }
        else if(current.prevId==-1){
            p.om.placeObj(current.currentId, current.tileX, current.tileY, current.scale, false);
        }
        else p.map.mapNum[current.tileY][current.tileX] = current.currentId;
        done.addLast(current);
        p.repaint();
    }
}
