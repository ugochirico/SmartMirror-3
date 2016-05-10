package info.cantu.smartmirror.model.news;

import info.cantu.smartmirror.model.BaseModel;
import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.MaterialIcons;
import info.cantu.smartmirror.view.Widget;
import info.cantu.smartmirror.view.animator.Animator;
import info.cantu.smartmirror.view.animator.interpolators.AccelerateInterpolator;
import info.cantu.smartmirror.view.animator.interpolators.OvershootInterpolator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Viviano on 5/9/2016.
 */
public class NewsView extends Widget implements ActionListener {

  private float DIMEN = 40f;//line size
  private int LINE_COUNT = 4;

  private String[] headlines;
  private int[] positions;
  private boolean showing = false;

  private Timer timer;
  private int currIndex = 0;

  /**
   * Called when the view is initialized
   */
  @Override
  protected void onInitialize() {
    this.timer = new Timer(12000, this);
    this.timer.start();
    positions = new int[LINE_COUNT];
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
    if (headlines == null)
      return;
    Graphics2D g2d = getG2D(g);

    for (int i = currIndex; i < headlines.length &&
            i < currIndex + LINE_COUNT; i++) {
      int pos = i - currIndex;
      if (pos >= 0)
        drawLine(headlines[i], i - currIndex, positions[pos], g2d);
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
    String icon = MaterialIcons.getIcon("newspaper");

    FontMetrics fm = getFontMetrics(g2d.getFont());
    int iconW = fm.stringWidth(icon);

    g2d.drawString(icon, getWidth() - iconW +  x, y);

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
    return new Dimension(800, (int) (LINE_COUNT * DIMEN * 1.5f));
  }

  /**
   * Will be called by the model every time it updates
   *
   * @param model model that calls this method
   */
  @Override
  public void onUpdate(BaseModel model) {
    if (!(model instanceof NewsModel))
      throw new IllegalStateException("NewsView not linked to NewsModel");
    NewsModel nm = (NewsModel)model;
    headlines = nm.getHeadlines();
    animateEntry();
  }

  /**
   * Invoked when an action occurs.
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (!showing)
      animateEntry();
    else {
      animateExit();
    }
  }

  private void animateEntry() {
    showing = true;
    for (int i = 0; i < LINE_COUNT; i++) {
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

  private void animateExit() {
    showing = false;
    for (int i = 0; i < LINE_COUNT; i++) {
      final int finalI = i;
      Animator anim = new Animator(0, getWidth())
              .setDuration(500)
              .setStartDelay(i * 250)
              .setInterpolator(new AccelerateInterpolator())
              .addUpdateListener(new Animator.UpdateListener() {
                @Override
                public void onUpdate(float value) {
                  positions[finalI] = (int)value;
                  repaint();
                }
              });
      if (i == LINE_COUNT - 1)//last one
        anim.addEndListener(new Animator.EndListener() {
          @Override
          public void onEnd() {
            //cycle through news
            currIndex += LINE_COUNT;
            if (currIndex >= headlines.length)
              currIndex = 0;
            animateEntry();
          }
        });
      anim.start();
    }
  }
}
