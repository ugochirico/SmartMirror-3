package info.cantu.smartmirror.view.animator.interpolators;

/**
 * Created by Viviano on 5/9/2016.
 */
public interface Interpolator {

  /**
   * Maps the value given a fraction as input
   * @param input current input
   * @return interpolation at the current fraction
   */
  float getInterpolation(float input);
}
