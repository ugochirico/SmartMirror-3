package info.cantu.smartmirror.model.weather;

import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.Widget;
import info.cantu.smartmirror.model.BaseModel;

import java.awt.*;

/**
 * Created by Viviano on 5/7/2016.
 */
public class WeatherView extends Widget {

  private float DIMEN = 70f;

  private String code = "";
  private int temp;

  /**
   * Paints each of the components in this container.
   *
   * @param g the graphics context.
   * @see Component#paint
   * @see Component#paintAll
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = getG2D(g);

    g2d.setFont(WeatherIcons.font.deriveFont(DIMEN * .6f));
    drawIconAt(this.code, getWidth() - (int)(DIMEN * .5f), (int) (DIMEN * .6f), g2d);

    g2d.setFont(Fonts.thin.deriveFont(DIMEN));
    FontMetrics fm = getFontMetrics(g2d.getFont());
    int width = fm.stringWidth(this.temp + "°");

    g2d.drawString(temp + "°", getWidth() - DIMEN - width, DIMEN);
  }

  private void drawIconAt(String icon, int x, int y, Graphics2D g2d) {
    FontMetrics fm = getFontMetrics(g2d.getFont());
    int width = fm.stringWidth(icon);
    int height = fm.getAscent();

    g2d.drawString(icon, x - width / 2, y + height / 2);
  }

  /**
   * The three methods below are used by the box layout
   * to force the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension((int)(DIMEN * 4f), (int)(DIMEN * 1.1f));
  }

  /**
   * Will be called by the model every time it updates
   *
   * @param model model that calls this method
   */
  @Override
  public void onUpdate(BaseModel model) {
    if (!(model instanceof WeatherModel))
      throw new IllegalStateException("WeatherView not linked to WeatherModel");
    WeatherModel wm = (WeatherModel)model;
    this.code = wm.getCurrentCode();
    this.temp = Math.round(wm.getCurrentTemp());
    this.repaint();
  }
}
