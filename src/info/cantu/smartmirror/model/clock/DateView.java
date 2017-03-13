package info.cantu.smartmirror.model.clock;

import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.Widget;
import info.cantu.smartmirror.model.BaseModel;

import java.awt.*;

/**
 * Created by Viviano on 5/6/2016.
 */
public class DateView extends Widget {

  private float DIMEN = 35f;

  private String date = "";

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

    g2d.setFont(Fonts.light.deriveFont(DIMEN));

    FontMetrics fm = getFontMetrics(g2d.getFont());
    int width = fm.stringWidth(this.date);

    g2d.drawString(this.date, this.getWidth() - width, DIMEN);
  }

  /**
   * Used by Box Layout to Force Preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(400, (int)DIMEN + 30);
  }

  /**
   * Will be called by the model every time it updates
   *
   * @param model model that calls this method
   */
  @Override
  public void onUpdate(BaseModel model) {
    if (!(model instanceof ClockModel))
      throw new IllegalStateException("DateView not linked to ClockModel");
    this.date = ((ClockModel)model).format("EEE, MMMM d");
    this.repaint();
  }
}
