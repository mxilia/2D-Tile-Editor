package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Tile Editor");

        Panel panel = new Panel(window);
        MenuBar menu = new MenuBar(panel, window);
        window.add(panel);
        window.setJMenuBar(menu.menuBar);

        ImageIcon logoIcon = new ImageIcon(panel.mainDirectory+"\\grass.png");
        window.setIconImage(logoIcon.getImage());

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}