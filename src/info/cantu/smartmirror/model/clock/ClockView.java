package info.cantu.smartmirror.model.clock;

import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.Widget;
import info.cantu.smartmirror.model.BaseModel;

import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Created by Viviano on 5/4/2016.
 */
public class ClockView extends Widget {

  private float DIMEN = 100f;

  private String hh = "", mm = "", aa = "";

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

    Font hhf = Fonts.regular.deriveFont(DIMEN);
    Font mmf = Fonts.thin.deriveFont(DIMEN);
    Font aaf = Fonts.thin.deriveFont(DIMEN / 2f);

    FontMetrics fm = getFontMetrics(hhf);
    int hhw = fm.stringWidth(hh);
    fm = getFontMetrics(mmf);
    int mmw = fm.stringWidth(mm);
    fm = getFontMetrics(aaf);
    int aaw = fm.stringWidth(aa);

    g2d.setFont(hhf);
    g2d.drawString(hh, this.getWidth() - hhw - mmw - aaw, DIMEN);
    g2d.setFont(mmf);
    g2d.drawString(mm, this.getWidth() - mmw - aaw, DIMEN);
    g2d.setFont(aaf);
    g2d.drawString(aa, this.getWidth() - aaw, DIMEN);
  }

  /**
   * If the <code>preferredSize</code> has been set to a
   * non-<code>null</code> value just returns it.
   * If the UI delegate's <code>getPreferredSize</code>
   * method returns a non <code>null</code> value then return that;
   * otherwise defer to the component's layout manager.
   *
   * @return the value of the <code>preferredSize</code> property
   * @see #setPreferredSize
   * @see ComponentUI
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(400, (int)DIMEN + 5);
  }

  /**
   * Will be called by the model every time it updates
   */
  @Override
  public void onUpdate(BaseModel model) {
    if (!(model instanceof ClockModel))
      throw new IllegalStateException("ClockView not linked to ClockModel");

    ClockModel cm = (ClockModel)model;
    hh = cm.format("h");
    mm = cm.format("mm");
    aa = cm.format("aa");
    this.repaint();
  }
}
