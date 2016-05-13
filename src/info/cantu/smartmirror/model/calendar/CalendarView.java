package info.cantu.smartmirror.model.calendar;

import com.google.api.client.util.DateTime;
import info.cantu.smartmirror.model.BaseModel;
import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.MaterialIcons;
import info.cantu.smartmirror.view.Widget;

import com.google.api.services.calendar.model.Event;
import info.cantu.smartmirror.view.animator.Animator;
import info.cantu.smartmirror.view.animator.interpolators.OvershootInterpolator;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Viviano on 5/10/2016.
 */
public class CalendarView extends Widget {

  private float DIMEN = 40f;

  private String[] summaries;
  private Date[] starts;
  private int [] positions;

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
    if (summaries == null)//if one is null they are all null
      return;
    Graphics2D g2d = getG2D(g);

    for (int i = 0; i < summaries.length; i++) {
      drawLine(String.format("%s at %s", summaries[i],
              new SimpleDateFormat("h:mmaa").format(starts[i]).toLowerCase()),
              i, positions[i], g2d);
    }
  }

  /**
   * Draws the headline attached to the left
   *
   * @param line headline
   * @param level int with 0 being the top line
   * @param x position of line where 0 shows all and getWidth() hides all
   */
  private void drawLine(String line, int level, int x, Graphics2D g2d) {
    int y = (int) (DIMEN * (level + 1));

    g2d.setFont(MaterialIcons.font.deriveFont(DIMEN * .6f));
    String icon = MaterialIcons.getIcon("calendar");

    FontMetrics fm = getFontMetrics(g2d.getFont());
    int iconW = fm.stringWidth(icon);

    g2d.drawString(icon, getWidth() - iconW +  x, y + (DIMEN * .08f));

    g2d.setFont(Fonts.light.deriveFont(DIMEN * .4f));
    fm = getFontMetrics(g2d.getFont());
    int lineW = fm.stringWidth(line);

    g2d.drawString(line, getWidth() - iconW - lineW - 10 + x, y);
  }

  /**
   * The three methods below are used by the box layout
   * to force the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(600, 200);
  }

  /**
   * Will be called by the model every time it updates
   *
   * @param model model that calls this method
   */
  @Override
  public void onUpdate(BaseModel model) {
    if (!(model instanceof CalendarModel))
      throw new IllegalStateException("CalendarView not linked to CalendarModel");
    CalendarModel cm = (CalendarModel)model;
    this.summaries = cm.getSummaries();
    this.starts = cm.getStartTimes();
    this.positions = new int[summaries.length];
    repaint();
    animateEntry();
  }

  private void animateEntry() {
    for (int i = 0; i < positions.length; i++) {
      positions[i] = getWidth();
      final int finalI = i;
      new Animator(getWidth(), 0)
              .setDuration(500)
              .setStartDelay(i * 250)
              .setInterpolator(new OvershootInterpolator(.6f))
              .addUpdateListener(new Animator.UpdateListener() {
                @Override
                public void onUpdate(float value) {
                  positions[finalI] = (int)value;
                  repaint();
                }
              }).start();
    }
  }
}
