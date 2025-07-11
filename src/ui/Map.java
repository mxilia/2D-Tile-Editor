package ui;

import main.Panel;
import utility.ObjScale;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Comparator;

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
        readMaps();
    }

    void readMaps() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(p.resDirectory+p.dataDirectory+p.mapsName));
            String currentLine;
            currentLine = reader.readLine();
            if(currentLine!=null) openMap(currentLine);
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        if(userScale+z>=1){
            userScale+=z;
            tileSize = p.baseTileLength*userScale;
            mapWidth = col*tileSize;
            mapHeight = row*tileSize;
        }
    }

    public void updateMaps() {
        try {
            FileWriter fileWriter = new FileWriter(p.resDirectory+p.dataDirectory+p.mapsName);
            fileWriter.write(currentFileDirectory);
            fileWriter.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(p.window, "Maps File Not Found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openMap(String dir) {
        boolean okFile = true;
        int row = 0, col = 0;
        currentFileDirectory = dir;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(currentFileDirectory));
            String current_line;
            current_line = reader.readLine();
            String[] numbers = current_line.split(" ");
            int fileRow = Integer.parseInt(numbers[0]);
            int fileCol = Integer.parseInt(numbers[1]);
            while((current_line = reader.readLine()) != null){
                if(current_line.equals("Obj:")) break;
                numbers = current_line.split(" ");
                col = 0;
                for(String number : numbers){
                    mapNum[row][col] = Integer.parseInt(number);
                    col++;
                }
                if(col != fileCol) {
                    okFile = false;
                    break;
                }
                row++;
            }
            if(row != fileRow) okFile = false;
            if(okFile) p.om.objList.clear();
            String[] objInfo;
            while((current_line = reader.readLine()) != null && okFile){
                objInfo = current_line.split(" ");
                if(objInfo.length!=4) continue;
                p.om.objList.add(new ObjScale(Integer.parseInt(objInfo[0]),
                                Integer.parseInt(objInfo[1]),
                                Integer.parseInt(objInfo[2]),
                                p.om.getImg(Integer.parseInt(objInfo[0])).getHeight(),
                                Integer.parseInt(objInfo[3])
                        )
                );
            }
            p.om.objList.sort(Comparator.comparing(ObjScale::compare));
            reader.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(p.window, "File Not Found");
            okFile = false;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(p.window, "An Error Occurred");
            okFile = false;
        }
        if(okFile){
            changeMap(row, col);
            updateMaps();
            p.actRecord.clear();
            p.repaint();
        }
        else {
            JOptionPane.showMessageDialog(p.window, "Invalid Map");
            mapNum = new int[maxRow][maxCol];
            currentFileDirectory = "";
            p.om.objList.clear();
        }
    }

    public void saveMap() {
        if(currentFileDirectory.isEmpty()) return;
        try {
            FileWriter fileWriter = new FileWriter(currentFileDirectory);
            fileWriter.write(row + " " + col + "\n");
            for(int i=0;i<row;i++){
                for(int j=0;j<col;j++){
                    fileWriter.write(String.valueOf(mapNum[i][j]));
                    if(j!=col-1) fileWriter.write(" ");
                }
                fileWriter.write("\n");
            }
            fileWriter.write("Obj:");
            for(int i=0;i<p.om.objList.size();i++){
                fileWriter.write("\n");
                ObjScale tuple = p.om.objList.get(i);
                fileWriter.write(tuple.id + " " + tuple.x + " " + tuple.y + " " + tuple.tileSize);
            }
            fileWriter.close();
            JOptionPane.showMessageDialog(p.window, "Saved");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(p.window, "An Error Occurred");
        }
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
