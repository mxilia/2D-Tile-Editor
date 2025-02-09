package resource;

import main.Panel;
import utility.Pair;
import utility.UtilFunc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

public abstract class Manager {
    Panel p;
    String resType;
    public String imgDirectory;

    public ArrayList<BufferedImage> img;
    public ArrayList<String> displayName;
    public HashMap<String, Integer> map = new HashMap<>();
    public int imgCount;
    final public int editorWidth, editorHeight;

    public Manager(Panel p, String resType) {
        this.p = p;
        this.resType = resType;
        editorWidth = p.width-151;
        editorHeight = p.height;
        if(resType.equals("objects")) imgDirectory = p.resDirectory+p.objectDirectory;
        else imgDirectory = p.resDirectory+p.tileDirectory;
        loadImg();
    }

    public BufferedImage getImg(int id) {
        return img.get(Math.min(imgCount-1, id));
    }

    public void addImgRT(BufferedImage newImg, String str) {
        this.img.add(newImg);
        this.displayName.add(UtilFunc.filterStr(str, false));
        this.map.put(UtilFunc.filterStr(str, true), this.imgCount);
        this.imgCount++;
    }

    public void loadImg() {
        img = new ArrayList<>();
        displayName = new ArrayList<>();
        File tileFolder = new File(imgDirectory);
        File[] files = tileFolder.listFiles();
        if(files != null){
            Arrays.sort(files, (file1, file2) -> {
                try {
                    FileTime creationTime1 = Files.readAttributes(file1.toPath(), BasicFileAttributes.class).creationTime();
                    FileTime creationTime2 = Files.readAttributes(file2.toPath(), BasicFileAttributes.class).creationTime();
                    return creationTime1.compareTo(creationTime2);
                } catch(IOException e) {
                    System.err.println("Error reading attributes for file: " + file1.getName());
                    return 0;
                }
            });
            imgCount = files.length;
            for(int i=0;i<imgCount;i++){
                try {
                    img.add(ImageIO.read(files[i]));
                    displayName.add(UtilFunc.filterStr(files[i].getName(), false));
                    map.put(UtilFunc.filterStr(files[i].getName(), true), i);
                } catch (IOException e) {
                    System.out.println("Error: " + i);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void draw(Graphics2D g2){};

}