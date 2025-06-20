package resource;

import main.Panel;
import utility.ObjScale;
import utility.UtilFunc;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;

public class ObjectManager extends Manager {
    public ArrayList<ObjScale> objList = new ArrayList<>();
    public int incrementValue = 1;
    int invalidId = -1;

    public ObjectManager(Panel p, String resType) {
        super(p, resType);
        readSettings();
    }

    public void readSettings() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(p.resDirectory+p.settingsName));
            String currentLine;
            currentLine = reader.readLine();
            try {
                setIncrement(Integer.parseInt(currentLine));
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setIncrement(int val) {
        incrementValue = val;
    }

    public void placeObj(int id, int x, int y, int scale) {
        objList.add(new ObjScale(id, x, y, img.get(id).getHeight(), scale));
        p.actRecord.objPlaced(id, -1, x, y, scale);
        objList.sort(Comparator.comparingInt(ObjScale::compare));
    }

    public void placeObj(int id, int x, int y, int scale, boolean noAdd) {
        objList.add(new ObjScale(id, x, y, img.get(id).getHeight(), scale));
        objList.sort(Comparator.comparingInt(ObjScale::compare));
    }

    public boolean deleteObj(int mouseX, int mouseY) {
        for(int i=objList.size()-1;i>=0;i--){
            ObjScale tuple = objList.get(i);
            int x = tuple.x/tuple.tileSize*p.map.tileSize+(tuple.x%tuple.tileSize)*p.map.tileSize/tuple.tileSize-p.map.userX;
            int y = tuple.y/tuple.tileSize*p.map.tileSize+(tuple.y%tuple.tileSize)*p.map.tileSize/tuple.tileSize-p.map.userY;
            int width = img.get(tuple.id).getWidth()*p.map.userScale;
            int height = img.get(tuple.id).getHeight()*p.map.userScale;
            if(mouseX-p.map.userX>x && mouseY-p.map.userY>y && mouseX-p.map.userX<x+width && mouseY-p.map.userY<y+height){
                objList.remove(i);
                p.actRecord.objPlaced(tuple.id, -2, tuple.x, tuple.y, tuple.tileSize);
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Graphics2D g2) {
        int topX = p.map.userX, topY = p.map.userY;
        int bottomX = topX+editorWidth, bottomY = topY+editorHeight;
        for(int i=0;i<objList.size();i++){
            ObjScale tuple = objList.get(i);
            if(tuple.id>=imgCount){
                invalidId = i;
                continue;
            }
            int x = tuple.x/tuple.tileSize*p.map.tileSize+(tuple.x%tuple.tileSize)*p.map.tileSize/tuple.tileSize;
            int y = tuple.y/tuple.tileSize*p.map.tileSize+(tuple.y%tuple.tileSize)*p.map.tileSize/tuple.tileSize;
            int width = p.om.img.get(tuple.id).getWidth()*p.map.userScale;
            int height = p.om.img.get(tuple.id).getHeight()*p.map.userScale;
            if((x+width>=topX && y+height>=topY) || (x+width>=topX && y<=bottomY) || (x>=bottomX && y+height>=topY) || (x>=bottomX && y>=bottomY)){
                g2.drawImage(p.om.img.get(tuple.id), x-topX, y-topY, width, height, null);
            }
        }
        if(invalidId != -1){
            objList.remove(invalidId);
            invalidId = -1;
        }
        if(p.map.onObject){
            if(p.resp.currentObj>=0){
                int x = p.map.objX-topX;
                int y = p.map.objY-topY;
                int width = p.om.img.get(p.resp.currentObj).getWidth()*p.map.userScale;
                int height = p.om.img.get(p.resp.currentObj).getHeight()*p.map.userScale;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.drawImage(p.om.img.get(p.resp.currentObj), x, y, width, height, null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                UtilFunc.drawBorder(g2, x, y, width, height, 1, 15, Color.white);
            }
        }
    }
}
