package info.cantu.smartmirror.model.news;

import info.cantu.smartmirror.model.BaseModel;
import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.MaterialIcons;
import info.cantu.smartmirror.view.Widget;
import info.cantu.smartmirror.view.animator.Animator;
import info.cantu.smartmirror.view.animator.interpolators.AccelerateInterpolator;
import info.cantu.smartmirror.view.animator.interpolators.DecelerateInterpolator;
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
  private int LINE_COUNT = 4;//max line count

  private String[] headlines;
  private int[] positions;
  private boolean showing = false;

  private Timer scrollTimer;
  private float[] scrollPer; // to animate scrolling
  private boolean goingLeft = true; // for scrolling direction
  private int currIndex = 0; // current news shown

  /**
   * Called when the view is initialized
   */
  @Override
  protected void onInitialize() {
    new Timer(3000 * LINE_COUNT, this).start();
    positions = new int[LINE_COUNT];
    scrollPer = new float[LINE_COUNT];
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
    int iconW = MaterialIcons.iconWidth("newspaper", DIMEN * .6f, g2d);
    int hgap = iconW / 4;

    MaterialIcons.draw("newspaper",
            getWidth() - hgap + x, (int)(y + DIMEN * .08f),
            DIMEN * .6f, g2d);

    g2d.setFont(Fonts.light.deriveFont(DIMEN * .4f));

    FontMetrics fm = getFontMetrics(g2d.getFont());
    int lineW = fm.stringWidth(line);
    int textWidth = getWidth() - hgap * 2 - iconW;

    int diff = lineW - textWidth;
    if (diff < 0)
      diff = 0;

    g2d.setClip(x, (int)(y - DIMEN + fm.getDescent()), textWidth, (int)DIMEN);
    g2d.drawString(line, textWidth - lineW + x
            + (diff * scrollPer[level]), y);
    g2d.setClip(null);
  }

  /**
   * Used by Box Layout to Force Preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(600, (int) (LINE_COUNT * DIMEN * 1.1f));
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
    //set scroll before entering
    for (int i=0; i<scrollPer.length; i++) {
      scrollPer[i] = 1f;
    }
    goingLeft = false;

    showing = true;
    for (int i = 0; i < LINE_COUNT; i++) {
      positions[i] = getWidth();
      final int finalI = i;
      Animator anim = new Animator(getWidth(), 0)
              .setDuration(500)
              .setStartDelay(i * 120)
              .setInterpolator(new OvershootInterpolator(.6f))
              .addUpdateListener(value -> {
                positions[finalI] = (int)value;
                repaint();
              });
      if (i == LINE_COUNT - 1)//last one
        anim.addEndListener(() -> {
          startScroller();
        });
      anim.start();
    }
  }

  private void animateExit() {
    showing = false;
    for (int i = 0; i < LINE_COUNT; i++) {
      final int finalI = i;
      Animator anim = new Animator(0, getWidth())
              .setDuration(500)
              .setStartDelay(i * 120)
              .setInterpolator(new AccelerateInterpolator())
              .addUpdateListener(value -> {
                positions[finalI] = (int)value;
                repaint();
              });
      if (i == LINE_COUNT - 1)//last one
        anim.addEndListener(() -> {
          if (scrollTimer != null)
            scrollTimer.stop();//stop scroller
          //cycle through news
          currIndex += LINE_COUNT;
          if (currIndex >= headlines.length)
            currIndex = 0;
          animateEntry();
        });
      anim.start();
    }
  }

  private void startScroller() {
    //set scroll before entering
    for (int i=0; i<scrollPer.length; i++) {
      scrollPer[i] = 1f;
    }

    this.scrollTimer = new Timer(3000, event -> {
      new Animator(goingLeft ? 1f : 0f, goingLeft ? 0f : 1f)
              .setDuration(2000)
              .addUpdateListener(value -> {
                for (int i=0; i<scrollPer.length; i++) {
                  scrollPer[i] = value;
                }
                repaint();
              })
              .addEndListener(() -> {
                goingLeft = !goingLeft;
              })
              .start();
    });
    this.scrollTimer.start();
  }
}
