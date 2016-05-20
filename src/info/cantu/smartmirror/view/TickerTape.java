package info.cantu.smartmirror.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Viviano on 5/14/2016.
 */
public class TickerTape extends JPanel {

  public enum Alignment {
    LEFT, CENTER, RIGHT
  }

  public enum Animation {
    SCROLL_LEFT, BACK_FORTH, SCROLL_RIGHT;
  }

  private String str;
  private Alignment alignment;
  private Animation animation;
  private int width;
  private float size;


  public TickerTape(String str, int width, float size) {
    this.str = str;
    this.alignment = Alignment.CENTER;
    this.animation = Animation.BACK_FORTH;
    this.width = width;
    this.size = size;
  }

  public TickerTape setAlignment(Alignment alignment) {
    this.alignment = alignment;
    return this;
  }

  public TickerTape setAnimation(Animation animation) {
    this.animation = animation;
    return this;
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

  }

  /**
   * The three methods below are used by the box layout
   * to force the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(width, 40);
  }

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
}
