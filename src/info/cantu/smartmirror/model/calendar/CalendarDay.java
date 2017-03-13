package info.cantu.smartmirror.model.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by vcantu on 2/14/17.
 */
public class CalendarDay {
  List<CalendarEvent> events;
  int date, month, year;

  public CalendarDay(int date, int month, int year) {
    this.date = date;
    this.month = month;
    this.year = year;
    this.events = new ArrayList<>();
  }

  public void add(CalendarEvent e) {
    this.events.add(e);
  }

  /**
   * Generates the current month as a double calendarDay array
   *
   * @return 7x6 calendarDay double array
   */
  public static CalendarDay[][] generateMonth() {
    Calendar curr = Calendar.getInstance();
    Calendar cal = new GregorianCalendar(//first day of the month
        curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), 1);
    int moSize = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    int firstDay = cal.get(Calendar.DAY_OF_WEEK);

    CalendarDay[][] month = new CalendarDay[7][6];

    int year = Calendar.getInstance().get(Calendar.YEAR);
    for (int i = 0; i < moSize; i++) {
      int onGrid = (firstDay + i - 1);
      int c = onGrid % 7;
      int r = onGrid / 7;
      month[c][r] = new CalendarDay(i + 1, cal.get(Calendar.MONTH), year);
    }
    return month;
  }

}
