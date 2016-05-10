package info.cantu.smartmirror.model.weather;

import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.Widget;
import info.cantu.smartmirror.model.BaseModel;
import info.cantu.smartmirror.view.animator.Animator;
import info.cantu.smartmirror.view.animator.interpolators.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Viviano on 5/8/2016.
 */
public class WeatherForecastView extends Widget implements ActionListener {

  private float DIMEN = 70f;

  private String[] dCodes, hCodes;
  private float[] dTemps, hTemps;
  private Date[] dTimes, hTimes;

  private Timer timer;

  private int currX = 0;
  private int gap = (int)(DIMEN / 5);

  private boolean onDaily = true;

  /**
   * Called when the view is initialized
   */
  @Override
  protected void onInitialize() {
    this.timer = new Timer(8000, this);
    this.timer.start();
  }

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
    if (dCodes == null)//if one is null they're all null(i'm lazy :])
      return;
    Graphics2D g2d = getG2D(g);

    drawDaily(currX, g2d);
    drawHourly(currX + getWidth() + gap, g2d);
  }

  private void drawIconAt(String icon, int x, int y, Graphics2D g2d) {
    FontMetrics fm = getFontMetrics(g2d.getFont());
    int width = fm.stringWidth(icon);
    int height = fm.getAscent();

    g2d.drawString(icon, x - width / 2, y + height / 2);
  }

  /**
   * draws the daily forecast
   * @param x left most position
   * @param g2d
   */
  private void drawDaily(int x, Graphics2D g2d) {
    int div = getWidth() / dCodes.length;
    for (int i = 0; i < dCodes.length; i++) {
      int ix = x + (div / 2 + div * i);

      //draw icon
      g2d.setFont(WeatherIcons.font.deriveFont(DIMEN * .2f));
      drawIconAt(dCodes[i], ix, (int)(DIMEN * .2f), g2d);

      //draw temp
      g2d.setFont(Fonts.light.deriveFont(DIMEN * .15f));
      String temp = Math.round(dTemps[i]) + "°";
      centerString(temp, ix, (int)(DIMEN * .55f), g2d);

      //draw day
      g2d.setFont(Fonts.light.deriveFont(DIMEN * .15f));
      String day = new SimpleDateFormat("EEE")
              .format(dTimes[i]);
      if (i == 0)
        day = "Today";
      centerString(day, ix, (int)(DIMEN * .7f), g2d);
    }
  }

  /**
   * draws the daily forecast
   * @param x left most position
   * @param g2d
   */
  private void drawHourly(int x, Graphics2D g2d) {
    int div = getWidth() / 5;
    for (int i = 0; i < 5; i++) {
      int ix = x + (div / 2 + div * i);

      //draw icon
      g2d.setFont(WeatherIcons.font.deriveFont(DIMEN * .2f));
      drawIconAt(hCodes[i], ix, (int)(DIMEN * .2f), g2d);

      //draw temp
      g2d.setFont(Fonts.light.deriveFont(DIMEN * .15f));
      String temp = Math.round(hTemps[i]) + "°";
      centerString(temp, ix, (int)(DIMEN * .55f), g2d);

      //draw day
      g2d.setFont(Fonts.light.deriveFont(DIMEN * .15f));
      String time = new SimpleDateFormat("h aa")
              .format(hTimes[i]).toLowerCase();
      centerString(time, ix, (int)(DIMEN * .7f), g2d);
    }
  }

  /**
   * The three methods below are used by the box layout
   * to force the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension((int)(DIMEN * 2.5f), (int)(DIMEN));
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
    dCodes = wm.getDailyCodes();
    dTemps = wm.getDailyTemps();
    dTimes = wm.getDailyTimes();

    hCodes = wm.getHourlyCodes();
    hTemps = wm.getHourlyTemps();
    hTimes = wm.getHourlyTimes();

    this.repaint();
  }

  /**
   * Invoked when an action occurs.
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (onDaily)
      slideLeft();
    else
      slideRight();
  }

  private void slideLeft() {
    new Animator(0, -getWidth() - gap)
            .setDuration(1000)
            .setInterpolator(new DecelerateInterpolator())
            .addUpdateListener(new Animator.UpdateListener() {
              @Override
              public void onUpdate(float value) {
                currX = (int)value;
                repaint();
              }
            })
            .addEndListener(new Animator.EndListener() {
              @Override
              public void onEnd() {
                onDaily = false;
              }
            }).start();
  }

  private void slideRight() {
    new Animator(-getWidth() - gap, 0)
            .setDuration(1000)
            .setInterpolator(new DecelerateInterpolator())
            .addUpdateListener(new Animator.UpdateListener() {
              @Override
              public void onUpdate(float value) {
                currX = (int)value;
                repaint();
              }
            })
            .addEndListener(new Animator.EndListener() {
              @Override
              public void onEnd() {
                onDaily = true;
              }
            }).start();
  }

}
