package utility;

import main.Panel;

import java.awt.*;

public class UtilFunc {
    main.Panel p;

    public UtilFunc(Panel p) {
        this.p = p;
    }

    public static void drawHighlight(Graphics2D g2, int x, int y, int width, int height, Color recC, Color borC, float opacity) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setColor(recC);
        g2.fillRect(x, y, width, height);
        drawBorder(g2, x, y, width, height, 1, 15, borC);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public static void drawBorder(Graphics2D g2, int x, int y, int width, int height, int thickness, int sideBit, Color color) {
        g2.setColor(color);
        if((sideBit&1) == 1) g2.fillRect(x, y, width, thickness); sideBit>>=1;
        if((sideBit&1) == 1) g2.fillRect(x, y, thickness, height); sideBit>>=1;
        if((sideBit&1) == 1) g2.fillRect(x+width-(thickness-1), y, thickness, height); sideBit>>=1;
        if((sideBit&1) == 1) g2.fillRect(x, y+height-(thickness-1), width, thickness);
    }

    public static String filterStr(String str, boolean full) {
        if(str.length()-4>10 && !full) return (str.substring(0, 7) + "...");
        else return str.substring(0, str.length()-4);
    }

}
