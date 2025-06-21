package main;

import ui.Map;
import ui.ResourcePanel;
import resource.ObjectManager;
import resource.TileManager;
import utility.ActivityRecorder;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Panel extends JPanel {
    public JFrame window;
    public String mainDirectory;
    public String resDirectory;
    public final String dataDirectory = "\\data";
    public final String settingsName = "\\settings.txt";
    public final String mapsName = "\\maps.txt";

    public final int baseTileLength = 16;
    final int scale = 3;

    public final int tileLength = baseTileLength * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int width = maxScreenCol * tileLength;
    public final int height = maxScreenRow * tileLength;

    MouseClickRegister clickRegister = new MouseClickRegister(this);
    MouseWheelRegister wheelRegister = new MouseWheelRegister(this);
    MouseMotionRegister motionRegister = new MouseMotionRegister(this);

    public ActivityRecorder actRecord;
    public TileManager tm;
    public ObjectManager om;
    public final String tileDirectory = "\\tiles";
    public final String objectDirectory = "\\objects";

    public ResourcePanel resp;
    public Map map;

    public boolean eraserOn = false;

    public Panel(JFrame window) {
        this.window = window;
        setupFolder();
        resp = new ResourcePanel(this);
        actRecord = new ActivityRecorder(this);
        tm = new TileManager(this, "tiles");
        om = new ObjectManager(this, "objects");
        map = new Map(this);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        this.setBackground(Color.darkGray);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addMouseListener(clickRegister);
        this.addMouseWheelListener(wheelRegister);
        this.addMouseMotionListener(motionRegister);
    }

    public void createFolder(String dir, String errorMsg) {
        File folder = new File(dir);
        if(!folder.exists()){
            if(!folder.mkdirs()) {
                JOptionPane.showMessageDialog(window, errorMsg);
                System.exit(0);
            }
        }
    }

    public void createFile(String dir, String fileName, String defaultVal) {
        File file = new File(dir);
        boolean notExisted = false;
        try {
            notExisted = file.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(window, fileName + " Error.\n(Please Restart)");
            System.exit(0);
        }
        if(notExisted){
            try {
                FileWriter fileWriter = new FileWriter(dir);
                fileWriter.write(defaultVal);
                fileWriter.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(window, fileName + " Not Found.\n(Please Restart)");
                System.exit(0);
            }
        }
    }

    private void setupFolder() {
        mainDirectory = System.getProperty("user.dir");
        resDirectory = mainDirectory+"\\res";
        createFolder(resDirectory, "Resource Not Found\n(Please Restart)");
        createFolder(resDirectory+tileDirectory, "Tile Folder Not Found\n(Please Restart)");
        createFolder(resDirectory+objectDirectory, "Object Not Found\n(Please Restart)");
        createFolder(resDirectory+dataDirectory, "Data Not Found\n(Please Restart)");
        createFile(resDirectory+dataDirectory+settingsName, "Settings", "1");
        createFile(resDirectory+dataDirectory+mapsName, "Maps", "");
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        map.draw(g2);
        resp.draw(g2);
    }
}
