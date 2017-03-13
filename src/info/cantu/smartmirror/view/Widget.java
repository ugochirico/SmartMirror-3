package info.cantu.smartmirror.view;

import info.cantu.smartmirror.model.BaseModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Viviano on 5/5/2016.
 */
public abstract class Widget extends JPanel implements BaseModel.UpdateListener {

  public Widget() {
    this.setBackground(Color.BLACK);
  }

  /**
   * Called when the view is initialized
   */
  protected void onInitialize() {
    //meant to be overriden
  }

  /**
   * Returns a Graphics2D with anti alias enabled
   * and the color set to white for convenience
   */
  protected Graphics2D getG2D(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d.setRenderingHints(rh);

    g2d.setColor(Color.WHITE);
    return g2d;
  }

  /**
   * Draws string centered on the x axis
   *
   * @param str String to draw
   * @param x center x position
   * @param y y position of base of text
   * @param g2d graphics(will use it's font size)
   */
  protected void centerString(String str, int x, int y, Graphics2D g2d) {
    FontMetrics fm = getFontMetrics(g2d.getFont());
    int width = fm.stringWidth(str);

    g2d.drawString(str, x - width / 2, y);
  }

  /**
   * The three methods below are used by the box layout
   * to force the preferred size
   */
  @Override
  public abstract Dimension getPreferredSize();

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  /**
   * Draws the headline attached to the left
   *
   * @param line headline
   * @param x position of line where 0 shows all and getWidth() hides all
   * @param y position of the base of the text
   * @param g2d graphics (will use it's font size
   */
  protected void drawLine(String line, String icon, int x, int y, float size, Graphics2D g2d) {
    int iconW = MaterialIcons.iconWidth(icon, size * .6f, g2d);
    int hgap = iconW / 4;

    MaterialIcons.draw(icon,
            getWidth() - hgap + x,
            (int)(y + size * .08f),
            size * .6f, g2d);

    g2d.setFont(Fonts.light.deriveFont(size * .4f));

    FontMetrics fm = g2d.getFontMetrics();
    int lineW = fm.stringWidth(line);

    g2d.drawString(line, getWidth() - iconW - lineW - hgap * 2 + x, y);
  }

}
