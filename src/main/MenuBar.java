package main;

import ui.UserInterface;
import utility.ObjScale;
import utility.UtilFunc;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;

public class MenuBar extends UserInterface {
    JFrame window;

    JMenuBar menuBar = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu grid = new JMenu("Grid");
    JMenu eraser = new JMenu("Eraser");
    JMenu undo = new JMenu("Undo");
    JMenu redo = new JMenu("Redo");

    Color menuItemColor = new Color(104, 104, 104);

    int lastTile = 0;
    int lastObj = -1;

    public MenuBar(Panel p, JFrame window) {
        super(p);
        this.window = window;
        menuBar.setBackground(Color.DARK_GRAY);
        menuBar.setBorderPainted(false);
        setupFile();
        setupGrid();
        setupEraser();
        setupUndoRedo();
        addToPanel();
    }

    private void setupUndoRedo() {
        undo.setFont(new Font("Arial", Font.BOLD, 11));
        undo.setForeground(Color.white);
        undo.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                p.actRecord.undo();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        redo.setFont(new Font("Arial", Font.BOLD, 11));
        redo.setForeground(Color.white);
        redo.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                p.actRecord.redo();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void setupEraser() {
        eraser.setFont(new Font("Arial", Font.BOLD, 11));
        eraser.setForeground(Color.white);
        eraser.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                p.eraserOn = !p.eraserOn;
                if(p.eraserOn){
                    lastTile = p.resp.currentTile;
                    lastObj = p.resp.currentObj;
                    p.resp.currentTile = 0;
                    p.resp.currentObj = -1;
                }
                else {
                    p.resp.currentTile = lastTile;
                    p.resp.currentObj = lastObj;
                }
                p.repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void setupGrid() {
        grid.setFont(new Font("Arial", Font.BOLD, 11));
        grid.setForeground(Color.white);
        grid.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                p.map.gridOn = !p.map.gridOn;
                p.repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void setupFile() {
        file.setFont(new Font("Arial", Font.BOLD, 11));
        file.setForeground(Color.white);
        /* New */
        JMenuItem create = new JMenuItem("New");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(window, "Create new", true);
                dialog.setLayout(new FlowLayout());
                dialog.setSize(250, 175);
                dialog.setLocationRelativeTo(null);

                JTextField mapName = new JTextField(12);
                JTextField mapRow = new JTextField(3);
                JTextField mapCol = new JTextField(3);

                dialog.add(new JLabel("map name: "));
                dialog.add(mapName);
                dialog.add(new JLabel("map row: "));
                dialog.add(mapRow);
                dialog.add(new JLabel("map column: "));
                dialog.add(mapCol);

                final JLabel[] directoryLabel = new JLabel[1];
                directoryLabel[0] = new JLabel("Pick your directory");
                directoryLabel[0].setHorizontalAlignment(JLabel.LEFT);
                final File[] selectedDirectory = new File[1];

                JButton fileChooseButton = new JButton("Directory :");
                fileChooseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        int result = fileChooser.showOpenDialog(null);
                        if(result == JFileChooser.APPROVE_OPTION) {
                            selectedDirectory[0] = fileChooser.getSelectedFile();
                            directoryLabel[0].setText(selectedDirectory[0].getAbsolutePath());
                        }
                    }
                });

                JButton submitButton = new JButton("submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = mapName.getText();
                        String rowString = mapRow.getText();
                        String colString = mapCol.getText();
                        int row, col;
                        try {
                            row = Integer.parseInt(rowString);
                            col = Integer.parseInt(colString);
                            if(row>p.map.maxRow || col>p.map.maxRow){
                                JOptionPane.showMessageDialog(window, "Maximum row/col exceeded (Only up to 500 row/col)");
                                return;
                            }
                            if(name.isEmpty()){
                                JOptionPane.showMessageDialog(window, "No File Name");
                                return;
                            }
                            if(selectedDirectory[0] == null) return;

                            File mapFile = new File(directoryLabel[0].getText()+"\\"+name+".txt");
                            try {
                                if(!mapFile.createNewFile()){
                                    JOptionPane.showMessageDialog(window, "File Already Existed");
                                    return;
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(window, "An Error Occurred");
                                return;
                            }

                            try {
                                FileWriter fileWriter = new FileWriter(mapFile, true);
                                fileWriter.write(row + " " + col + "\n");
                                for(int i=0;i<row;i++){
                                    for(int j=0;j<col;j++){
                                        fileWriter.write("0");
                                        if(j!=col-1) fileWriter.write(" ");
                                    }
                                    fileWriter.write("\n");
                                }
                                fileWriter.write("Obj:");
                                fileWriter.close();
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(window, "An Error Occurred");
                                return;
                            }
                            dialog.dispose();
                        } catch(NumberFormatException ex) {
                            JOptionPane.showMessageDialog(window, "Not Integer.");
                        }
                    }
                });

                dialog.add(fileChooseButton);
                dialog.add(directoryLabel[0]);
                dialog.add(submitButton);
                dialog.setVisible(true);
            }
        });
        /* Open */
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                fileChooser.setFileFilter(new FileNameExtensionFilter(".txt Files", "txt"));

                int result = fileChooser.showOpenDialog(null);
                if(result != JFileChooser.APPROVE_OPTION) return;

                boolean okFile = true;
                int row = 0, col = 0;
                p.map.currentFileDirectory = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(p.map.currentFileDirectory));
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
                            p.map.mapNum[row][col] = Integer.parseInt(number);
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
                                                      p.om.img.get(Integer.parseInt(objInfo[0])).getHeight(),
                                                      Integer.parseInt(objInfo[3])
                                                     )
                                        );
                    }
                    p.om.objList.sort(Comparator.comparing(ObjScale::compare));
                    reader.close();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(window, "File Not Found");
                    okFile = false;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(window, "An Error Occurred");
                    okFile = false;
                }
                if(okFile){
                    p.map.changeMap(row, col);
                    p.actRecord.clear();
                    p.repaint();
                }
                else {
                    JOptionPane.showMessageDialog(window, "Invalid Map");
                    p.map.mapNum = new int[p.map.maxRow][p.map.maxCol];
                    p.map.currentFileDirectory = "";
                    p.om.objList.clear();
                }
            }
        });
        /* Save */
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(p.map.currentFileDirectory.isEmpty()) return;
                try {
                    FileWriter fileWriter = new FileWriter(p.map.currentFileDirectory);
                    fileWriter.write(p.map.row + " " + p.map.col + "\n");
                    for(int i=0;i<p.map.row;i++){
                        for(int j=0;j<p.map.col;j++){
                            fileWriter.write(String.valueOf(p.map.mapNum[i][j]));
                            if(j!=p.map.col-1) fileWriter.write(" ");
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
                    JOptionPane.showMessageDialog(window, "Saved");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(window, "An Error Occurred");
                }
            }
        });
        /* Upload */
        JMenuItem upload = new JMenuItem("Upload");
        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(window, "Upload resources");
                dialog.setLayout(new FlowLayout());
                dialog.setSize(200, 155);
                dialog.setLocationRelativeTo(null);

                final JLabel[] fileLabel = new JLabel[1];
                fileLabel[0] = new JLabel("Pick file");
                fileLabel[0].setHorizontalAlignment(JLabel.LEFT);
                final File[] selectedFile = new File[1];

                JButton fileChooseButton = new JButton("File (PNG):");
                fileChooseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                        fileChooser.setFileFilter(new FileNameExtensionFilter(".png Files", "png"));

                        int result = fileChooser.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            selectedFile[0] = fileChooser.getSelectedFile();
                            fileLabel[0].setText(selectedFile[0].getAbsolutePath());
                        }
                    }
                });

                JRadioButton isTile = new JRadioButton("Tile");
                JRadioButton isObject = new JRadioButton("Object");

                ButtonGroup group = new ButtonGroup();
                group.add(isTile);
                group.add(isObject);

                dialog.add(isTile);
                dialog.add(isObject);

                JButton submitButton = new JButton("submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String directory;
                        if(selectedFile[0] == null) return;
                        if(isTile.isSelected()) directory = p.tm.imgDirectory+"\\";
                        else if(isObject.isSelected()) directory = p.om.imgDirectory+"\\";
                        else {
                            JOptionPane.showMessageDialog(window, "Please Select Resource Type");
                            return;
                        }
                        try {
                            if(Files.exists(Paths.get(directory+selectedFile[0].getName()))){
                                JOptionPane.showMessageDialog(window, "File Name Already Existed");
                                return;
                            }
                            FileTime newCreationTime = FileTime.fromMillis(System.currentTimeMillis());
                            Files.copy(Paths.get(fileLabel[0].getText()), Paths.get(directory+selectedFile[0].getName()), StandardCopyOption.REPLACE_EXISTING);
                            Files.setAttribute(Paths.get(directory+selectedFile[0].getName()), "basic:creationTime", newCreationTime);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(window, "File Creation Error");
                            return;
                        }
                        try {
                            if(isObject.isSelected()) p.om.addImgRT(ImageIO.read(selectedFile[0]), selectedFile[0].getName());
                            else p.tm.addImgRT(ImageIO.read(selectedFile[0]), selectedFile[0].getName());
                        } catch(IOException ex) {
                            JOptionPane.showMessageDialog(window, "Adding To List Failed");
                            return;
                        }
                        JOptionPane.showMessageDialog(window, "Uploaded Successfully");
                        p.repaint();
                        dialog.dispose();
                    }
                });

                dialog.add(fileChooseButton);
                dialog.add(fileLabel[0]);
                dialog.add(submitButton);
                dialog.setVisible(true);
            }
        });
        /* Import Tiles */
        JMenuItem importTiles = new JMenuItem("Import Tiles");
        importTiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(window, "Import Tiles Folder");
                dialog.setLayout(new FlowLayout());
                dialog.setSize(200, 125);
                dialog.setLocationRelativeTo(null);

                final JLabel[] folderLabel = new JLabel[1];
                folderLabel[0] = new JLabel("Pick folder");
                folderLabel[0].setHorizontalAlignment(JLabel.LEFT);
                final File[] selectedFolder = new File[1];

                JButton folderChooseButton = new JButton("Pick folder :");
                folderChooseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        int result = fileChooser.showOpenDialog(null);
                        if(result == JFileChooser.APPROVE_OPTION) {
                            selectedFolder[0] = fileChooser.getSelectedFile();
                            folderLabel[0].setText(selectedFolder[0].getAbsolutePath());
                        }
                    }
                });

                JButton submit = new JButton("submit");
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(selectedFolder[0] == null) return;
                        File[] files = selectedFolder[0].listFiles();
                        if(files != null){
                            for(int i=0;i<files.length;i++){
                                if(!files[i].getName().endsWith(".png")) continue;
                                try {
                                    Path source = Paths.get(files[i].getAbsolutePath());
                                    Path destination = Paths.get(p.tm.imgDirectory+"\\"+files[i].getName());
                                    BasicFileAttributes sourceAttributes = Files.readAttributes(Paths.get(files[i].getAbsolutePath()), BasicFileAttributes.class);
                                    FileTime creationTime = sourceAttributes.creationTime();

                                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                                    Files.setAttribute(destination, "basic:creationTime", creationTime);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(window, "File Creation Error");
                                    return;
                                }
                            }
                            JOptionPane.showMessageDialog(window, "Imported Successfully");
                            dialog.dispose();
                            SwingUtilities.invokeLater(() -> {
                                p.tm.loadImg();
                                p.repaint();
                            });
                        }
                        else {
                            JOptionPane.showMessageDialog(window, "No Files Imported");
                        }
                    }
                });

                dialog.add(folderChooseButton);
                dialog.add(folderLabel[0]);
                dialog.add(submit);
                dialog.setVisible(true);
            }
        });
        /* Export Tiles */
        JMenuItem exportTiles = new JMenuItem("Export Tiles");
        exportTiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(window, "Export Tiles Folder");
                dialog.setLayout(new FlowLayout());
                dialog.setSize(200, 125);
                dialog.setLocationRelativeTo(null);

                final JLabel[] folderLabel = new JLabel[1];
                folderLabel[0] = new JLabel("Pick folder");
                folderLabel[0].setHorizontalAlignment(JLabel.LEFT);
                final File[] selectedFolder = new File[1];

                JButton folderChooseButton = new JButton("Pick folder :");
                folderChooseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        int result = fileChooser.showOpenDialog(null);
                        if(result == JFileChooser.APPROVE_OPTION) {
                            selectedFolder[0] = fileChooser.getSelectedFile();
                            folderLabel[0].setText(selectedFolder[0].getAbsolutePath());
                        }
                    }
                });

                JButton submit = new JButton("submit");
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File folder = new File(p.tm.imgDirectory);
                        File[] files = folder.listFiles();
                        if(files != null){
                            String newFolder = selectedFolder[0].getAbsolutePath()+"\\"+"exported_tiles";
                            try {
                                Files.createDirectory(Paths.get(newFolder));
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(window, "Error Creating Folder");
                                return;
                            }
                            for(int i=0;i<files.length;i++){
                                if(files[i].getName().substring(files[i].getName().length()-4).compareTo(".png")!=0) continue;
                                try {
                                    Path source = Paths.get(files[i].getAbsolutePath());
                                    Path destination = Paths.get(newFolder+"\\"+files[i].getName());
                                    BasicFileAttributes sourceAttributes = Files.readAttributes(source, BasicFileAttributes.class);
                                    FileTime creationTime = sourceAttributes.creationTime();

                                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                                    Files.setAttribute(destination, "basic:creationTime", creationTime);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(window, "File Creation Error");
                                    return;
                                }
                            }
                            JOptionPane.showMessageDialog(window, "Exported Successfully");
                            dialog.dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(window, "No Files Exported");
                        }
                    }
                });

                dialog.add(folderChooseButton);
                dialog.add(folderLabel[0]);
                dialog.add(submit);
                dialog.setVisible(true);
            }
        });
        /* Clear Tiles */
        JMenuItem clearTiles = new JMenuItem("Clear Tiles");
        clearTiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File folder = new File(p.tm.imgDirectory);
                File[] files = folder.listFiles();
                if(files != null){
                    for(int i=0;i<files.length;i++){
                        if(files[i].getName().substring(files[i].getName().length()-4).compareTo(".png")!=0) continue;
                        try {
                            Files.delete(Paths.get(p.tm.imgDirectory+"\\"+files[i].getName()));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(window, "An Error Occurred");
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(window, "Cleared Successfully");
                    SwingUtilities.invokeLater(() -> {
                        p.tm.loadImg();
                        p.repaint();
                    });
                }
                else JOptionPane.showMessageDialog(window, "An Error Occurred");
            }
        });
        /* Customization */
        create.setForeground(Color.white);
        create.setBackground(menuItemColor);
        create.setBorder(new EmptyBorder(0, 0, 0, 0));
        open.setForeground(Color.white);
        open.setBackground(menuItemColor);
        open.setBorder(new EmptyBorder(0, 0, 0, 0));
        save.setForeground(Color.white);
        save.setBackground(menuItemColor);
        save.setBorder(new EmptyBorder(0, 0, 0, 0));
        upload.setForeground(Color.white);
        upload.setBackground(menuItemColor);
        upload.setBorder(new EmptyBorder(0, 0, 0, 0));
        importTiles.setForeground(Color.white);
        importTiles.setBackground(menuItemColor);
        importTiles.setBorder(new EmptyBorder(0, 0, 0, 0));
        exportTiles.setForeground(Color.white);
        exportTiles.setBackground(menuItemColor);
        exportTiles.setBorder(new EmptyBorder(0, 0, 0, 0));
        clearTiles.setForeground(Color.white);
        clearTiles.setBackground(menuItemColor);
        clearTiles.setBorder(new EmptyBorder(0, 0, 0, 0));
        file.add(create); file.add(open); file.add(save); file.add(upload); file.add(importTiles); file.add(exportTiles); file.add(clearTiles);
    }

    private void addToPanel() {
        menuBar.add(file);
        menuBar.add(grid);
        menuBar.add(eraser);
        menuBar.add(undo);
        menuBar.add(redo);
    }
}
