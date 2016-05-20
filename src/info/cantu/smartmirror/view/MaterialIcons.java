package info.cantu.smartmirror.view;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Viviano on 5/9/2016.
 */
public class MaterialIcons {

  public static Font font;

  private static Map<String, String> codes;

  public static void load() throws IOException, FontFormatException {
    codes = new HashMap<>();

    Scanner scan = new Scanner(
            MaterialIcons.class.getResourceAsStream("/icons.csv"));
    String[] code = scan.nextLine().split(",");
    String[] name = scan.nextLine().split(",");
    scan.close();

    for (int i = 0; i < code.length; i++) {
      codes.put(name[i], "" + (char) Integer.parseInt(code[i], 16));
    }

    font = Font.createFont(Font.TRUETYPE_FONT,
            MaterialIcons.class.getResourceAsStream("/materialdesignicons-webfont.ttf"));
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(font);
  }

  public static String getIcon(String name) {
    if (name == null)
      return null;
    return codes.get(name);
  }

  /**
   * Draws the given icon
   *
   * @param name name of the icon
   * @param x center x position
   * @param y y position of base of text
   * @param size size of the icon
   * @param g2d icon graphics
   */
  public static void draw(String name, int x, int y, float size, Graphics2D g2d) {
    String ic = getIcon(name);
    if (ic == null)
      return;
    g2d.setFont(font.deriveFont(size));
    FontMetrics fm = g2d.getFontMetrics();
    int iconW = fm.stringWidth(ic);
    g2d.drawString(ic, x - iconW, y);
  }

  /**
   * Returns the width of the icon if it where to be drawn
   *
   * @param name name of the icon
   * @param size size of the icon
   * @param g2d graphics
   * @return width in pixels or 0 if name is invalid
   */
  public static int iconWidth(String name, float size, Graphics2D g2d) {
    String ic = getIcon(name);
    if (ic == null)
      return 0;
    FontMetrics fm = g2d.getFontMetrics(font.deriveFont(size));
    return fm.stringWidth(ic);
  }
}
