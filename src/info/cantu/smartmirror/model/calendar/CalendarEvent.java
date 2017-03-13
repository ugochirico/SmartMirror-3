package info.cantu.smartmirror.model.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vcantu on 2/13/17.
 *
 * Calendar event
 */
public class CalendarEvent {

  private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm:aa");

  public final String summary;
  public final Date date;
  public final boolean isAllDay;
  public final CalendarDay day;

  public CalendarEvent(String summary, Date date, boolean isAllDay, CalendarDay day) {
    this.summary = summary;
    this.date = date;
    this.isAllDay = isAllDay;
    this.day = day;
    this.day.add(this);
  }

  /**
   * @return calendar with the event's date as today
   */
  public Calendar getCal() {
    Calendar c = Calendar.getInstance();
    c.setTime(this.date);
    return c;
  }

  /**
   * Returns this event's start time as a string
   *
   * @return time as "h:mm:aa"
   */
  public String getStringTime() {
    return TIME_FORMAT.format(this.date).toLowerCase();
  }
}
