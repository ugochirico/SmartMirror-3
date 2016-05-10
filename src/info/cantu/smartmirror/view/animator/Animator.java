package info.cantu.smartmirror.view.animator;

import info.cantu.smartmirror.view.animator.interpolators.Interpolator;
import info.cantu.smartmirror.view.animator.interpolators.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viviano on 5/9/2016.
 */
public class Animator {

  private List<UpdateListener> listeners;
  private List<EndListener> endListeners;
  private Interpolator interpolator;

  private final float startVal, endVal;

  private long duration = 1000;//default value
  private long delay = 0;

  /**
   * Creates an animator given the start and end value
   *
   * @param startValue starting value
   * @param endValue ending value to interpolate
   */
  public Animator(float startValue, float endValue) {
    this.startVal = startValue;
    this.endVal = endValue;
    this.listeners = new ArrayList<>();
    this.endListeners = new ArrayList<>();
    this.interpolator = new LinearInterpolator();
  }

  public Animator setDuration(long duration) {
    this.duration = duration;
    return this;
  }

  /**
   * adds a start delay to this animator
   *
   * @param delay in milliseconds
   * @return the animator being built
   */
  public Animator setStartDelay(long delay) {
    this.delay = delay;
    return this;
  }

  /**
   * set this animator's interpolator
   *
   * @param interpolator time interpolator
   * @return the animator being built
   */
  public Animator setInterpolator(Interpolator interpolator) {
    this.interpolator = interpolator;
    return this;
  }


  public void start() {
    new Thread(() -> {
      try {
        Thread.sleep(this.delay);
        long startTime = System.currentTimeMillis();

        long timeleft = duration - (System.currentTimeMillis() - startTime);
        while (timeleft >= 0) {
          timeleft = duration - (System.currentTimeMillis() - startTime);

          float frac = 1f - ((float)timeleft / (float)duration);
          float iFrac = this.interpolator.getInterpolation(frac);


          float val = startVal + iFrac * (endVal - startVal);
          for (UpdateListener l : listeners) {
            l.onUpdate(val);
          }
          Thread.sleep(Math.min(20, Math.max(timeleft, 0)));
        }

        for (EndListener el : endListeners) {
          el.onEnd();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }


  public Animator addUpdateListener(UpdateListener listener) {
    this.listeners.add(listener);
    return this;
  }
  public interface UpdateListener {
    /**
     * Called every frame of the animation
     *
     * @param value current value determined by
     *              the start and end values
     */
    void onUpdate(float value);
  }


  public Animator addEndListener(EndListener listener) {
    this.endListeners.add(listener);
    return this;
  }

  public interface EndListener {
    /**
     * Called at the end of the animation
     */
    void onEnd();
  }

}
