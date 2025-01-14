package main;

import ui.Map;
import ui.ResourcePanel;
import resource.ObjectManager;
import resource.TileManager;
import utility.ActivityRecorder;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Panel extends JPanel {
    JFrame window;
    public String mainDirectory;
    public String resDirectory;

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

    public ResourcePanel resp = new ResourcePanel(this);
    public Map map = new Map(this);

    public boolean eraserOn = false;

    public Panel(JFrame window) {
        this.window = window;
        setupFolder();
        actRecord = new ActivityRecorder(this);
        tm = new TileManager(this, "tiles");
        om = new ObjectManager(this, "objects");
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        this.setBackground(Color.darkGray);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addMouseListener(clickRegister);
        this.addMouseWheelListener(wheelRegister);
        this.addMouseMotionListener(motionRegister);
    }

    private void setupFolder(){
        mainDirectory = System.getProperty("user.dir");
        resDirectory = mainDirectory+"\\res";
        File folder = new File(resDirectory);
        if(!folder.exists()){
            if(!folder.mkdirs()) {
                JOptionPane.showMessageDialog(window, "Resource Not Found\n(Please Restart)");
                System.exit(0);
            }
        }
        folder = new File(resDirectory+tileDirectory);
        if(!folder.exists()){
            if(!folder.mkdirs()) {
                JOptionPane.showMessageDialog(window, "Tile Folder Not Found\n(Please Restart)");
                System.exit(0);
            }
        }
        folder = new File(resDirectory+objectDirectory);
        if(!folder.exists()){
            if(!folder.mkdirs()) {
                JOptionPane.showMessageDialog(window, " Object Not Found\n(Please Restart)");
                System.exit(0);
            }
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        map.draw(g2);
        resp.draw(g2);
    }
}
