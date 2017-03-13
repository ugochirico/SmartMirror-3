package info.cantu.smartmirror.model.clock;


import info.cantu.smartmirror.model.BaseModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Viviano on 5/4/2016.
 */
public class ClockModel extends BaseModel {

  private Date date;

  /**
   * Time in milliseconds it will take for this model to onUpdate it's data
   *
   * @return a time in milliseconds
   */
  @Override
  public int getInterval() {
    return 1000;
  }

  /**
   * Updates the data of this model
   */
  @Override
  public void onUpdate() {
    this.date = new Date();
  }

  /**
   * @return a string of the current time at
   * the given format
   */
  public String format(DateFormat format) {
    return format.format(this.date);
  }

  /**
   * convenience method that takes in a string
   *
   * @return a string of the current time at
   * the given format
   */
  public String format(String format) {
    return new SimpleDateFormat(format).format(this.date);
  }
}
