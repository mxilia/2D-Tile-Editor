package ui;

import java.awt.*;
import main.Panel;

public class ResourcePanel extends UserInterface {
    public SearchBar searchBar;
    public ResourceSelector resSelect;
    public boolean isObject = false;

    public int tileUserY = 0;
    public int objUserY = 0;
    public int currentTile = 0;
    public int currentObj = -1;

    public Rectangle objSign, tileSign;

    Color panelColor = new Color(104, 104, 104);

    public ResourcePanel(Panel p) {
        super(p);
        setDefaultValue();
        searchBar = new SearchBar(p, x+13, y+10, 124, 25);
        resSelect = new ResourceSelector(p, x+10, y+45, width-20, height-56);
        tileSign = new Rectangle(x+10, y+45, (width-20)/2, 20);
        objSign = new Rectangle(tileSign.x+tileSign.width, y+45, tileSign.width, 20);
    }

    void setDefaultValue() {
        width = 150;
        x = p.width-width;
        y = 0;
        height = p.height-y;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(panelColor);
        g2.fillRect(x, tileSign.y, width, height-tileSign.y);
        resSelect.draw(g2);
        g2.setColor(panelColor);
        g2.fillRect(x, y, width, tileSign.y-y);
        g2.fillRect(x, resSelect.y+resSelect.height, width, height-resSelect.y+resSelect.height);
        g2.setColor(Color.darkGray);
        g2.fillRect(x-1,y, 1, height);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        if(isObject){
            g2.setColor(Color.GRAY);
            g2.fillRect(tileSign.x, tileSign.y, tileSign.width, tileSign.height);
            g2.setColor(Color.darkGray);
            g2.drawString("Tiles", tileSign.x+20, tileSign.y+14);
            g2.fillRect(objSign.x, objSign.y, objSign.width, objSign.height);
            g2.setColor(Color.GRAY);
            g2.drawString("Objects", objSign.x+13, objSign.y+14);
        }
        else {
            g2.setColor(Color.darkGray);
            g2.fillRect(tileSign.x, tileSign.y, tileSign.width, tileSign.height);
            g2.setColor(Color.GRAY);
            g2.drawString("Tiles", tileSign.x+20, tileSign.y+14);
            g2.fillRect(objSign.x, objSign.y, objSign.width, objSign.height);
            g2.setColor(Color.darkGray);
            g2.drawString("Objects", objSign.x+13, objSign.y+14);
        }
    }

}
