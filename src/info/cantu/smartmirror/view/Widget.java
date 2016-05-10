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
   * @param str
   * @param x
   * @param y
   * @param g2d
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
}
