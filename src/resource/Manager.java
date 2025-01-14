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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public abstract class Manager {
    Panel p;
    String resType;
    public String imgDirectory;

    public ArrayList<BufferedImage> img;
    public ArrayList<String> displayName;
    public ArrayList<Pair<String, Integer>> fileName;
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

    public void addImgRT(BufferedImage newImg, String str) {
        this.imgCount++;
        this.img.add(newImg);
        this.displayName.add(UtilFunc.filterStr(str));
        this.fileName.add(new Pair<>(str, this.imgCount));
        this.fileName.sort(Comparator.comparing(Pair::getFirst));
    }

    public void loadImg() {
        img = new ArrayList<>();
        displayName = new ArrayList<>();
        fileName = new ArrayList<>();
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
                    displayName.add(UtilFunc.filterStr(files[i].getName()));
                    fileName.add(new Pair<>(files[i].getName().substring(0, files[i].getName().length()-4), i));
                } catch (IOException e) {
                    System.out.println("Error: " + i);
                    throw new RuntimeException(e);
                }
            }
        }
        fileName.sort(Comparator.comparing(Pair::getFirst));
    }

    public void draw(Graphics2D g2){};

}