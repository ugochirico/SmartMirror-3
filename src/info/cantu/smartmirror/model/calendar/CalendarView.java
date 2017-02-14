package info.cantu.smartmirror.model.calendar;

import info.cantu.smartmirror.model.BaseModel;
import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.Widget;

import info.cantu.smartmirror.view.animator.Animator;
import info.cantu.smartmirror.view.animator.interpolators.AccelerateInterpolator;
import info.cantu.smartmirror.view.animator.interpolators.OvershootInterpolator;

import javax.swing.Timer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Viviano on 5/10/2016.
 */
public class CalendarView extends Widget {

  private static String[] DAYS_OF_WEEK = new String[] {
          "Sunday", "Monday", "Tuesday", "Wednesday",
          "Thursday", "Friday", "Friday"
  };

  private float DIMEN = 40f;
  private final int MAX_COUNT = 4;

  private Day[][] days;//month array
  private Event[] events, showing;
  private int current = 0;//represents the page number

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
    if (events == null)//if one is null they are all null
      return;
    Graphics2D g2d = getG2D(g);

    drawCalendar(0, g2d);

    Calendar cal = Calendar.getInstance();
    for (int i = 0; i < showing.length; i++) {
        String addon = getAddon(showing[i], cal);
        drawLine(showing[i].summary + addon, "calendar", (int) (getWidth() * showing[i].w),
                200 + 40 * i, 40f, g2d);
    }
  }

  /**
   * Draw monthly calendar,
   * with the dots representing the amount of events
   * and with the current date made bigger
   *
   * @param x left most coordinate
   * @param g2d graphics
   */
  private void drawCalendar(int x, Graphics2D g2d) {
    int currDay = Calendar.getInstance().get(Calendar.DATE);
    g2d.setFont(Fonts.light.deriveFont(DIMEN * .4f));

    int grid = (int) (DIMEN * .7f);
    int viewW = getWidth();
    int w = 7 * grid;

    int startX = viewW - w + grid / 2 + x;
    int startY = grid / 2 * 3;
    for (int c = 0; c < days.length; c++) {
      for (int r = 0; r < days[c].length; r++) {
        Day d = days[c][r];
        if (d != null) {
          if (d.date == currDay) {
            g2d.setFont(Fonts.light.deriveFont(DIMEN * .6f));
          }
          int dx = startX + grid * c;
          int dy = startY + grid * r;
          centerString("" + d.date, dx, dy, g2d);
          if (d.events.size() > 0)
            drawDots(dx, dy + 4, d.events.size(), g2d);

          if (d.date == currDay) {
            g2d.setFont(Fonts.light.deriveFont(DIMEN * .4f));
          }
        }

      }
    }

  }

  /**
   * Draws a given amount of 2x2 dots centered horizontally at x, y
   * @param x x position of the center of all dots
   * @param y y position of the center of all dots
   * @param count amount of dots(if count > 6 it will draw 6 dots)
   * @param g2d graphics
   */
  private void drawDots(int x, int y, int count, Graphics2D g2d) {
    if (count > 6)
      count = 6;
    boolean even = count % 2 == 0;

    int startX = x - ((count - 1) / 2 * 4) - (even ? 2 : 0);
    for (int i=0; i<count; i++) {
      g2d.fillRect(startX + (i * 4), y - 1, 2, 2);
    }
  }

  /**
   * The three methods below are used by the box layout
   * to force the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(600, 400);
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
    initializeCalendar();


    List<Event> lst = new ArrayList<>();
    this.events = new Event[cm.getEventCount()];

    for (int i=0; i<events.length; i++) {
      lst.add(createEvent(
              cm.getSummary(i),
              cm.getDescription(i),
              cm.getStartTime(i)));
    }
    lst.sort((e1, e2) -> e1.date.compareTo(e2.date));


    //Adds the current
    this.events = lst.toArray(this.events);

    if (showing == null || true) {
      updateShowing();
      startSlideshow();
    } else {
      animateExit(() -> {
        updateShowing();
        startSlideshow();
      });
    }

    repaint();
  }

  private void updateShowing() {
    this.showing = new Event[MAX_COUNT];
    Calendar cal = Calendar.getInstance();
    int idx = 0;
    for (int i = 0, curr = 0; i < events.length
            && curr < MAX_COUNT * (current+1); i++) {
      if (cal.compareTo(events[i].getCal()) <= 0) {
        if (curr >= current * MAX_COUNT) {
          this.showing[idx] = events[i];
          idx++;
        }
        curr++;
      }
    }
    if (showing[0] == null) {
      //reset page
      current = 0;
      updateShowing();
    }
  }

  /**
   * Initializes the days array to a 7x6 array representing the current month
   */
  private void initializeCalendar() {
    Calendar curr = Calendar.getInstance();
    Calendar cal = new GregorianCalendar(//first day of the month
            curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), 1);
    int moSize = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    int firstDay = cal.get(Calendar.DAY_OF_WEEK);

    days = new Day[7][6];

    int year = Calendar.getInstance().get(Calendar.YEAR);
    for (int i=0; i<moSize; i++) {
      int onGrid = (firstDay + i - 1);
      int c = onGrid % 7;
      int r = onGrid / 7;
      days[c][r] = new Day(i + 1, cal.get(Calendar.MONTH), year);
    }
  }

  /**
   * Creates an event and links it to a day if it exists
   *
   * @param summary summary of the event
   * @param description description of the event
   * @param date date of the event
   * @return the created event
   */
  private Event createEvent(String summary, String description, Date date) {
    int mo = date.getMonth();
    int day = date.getDate();

    Event e = new Event(summary, description, date);
    for (Day[] arr : days) {
      for (Day d : arr) {
        if (d != null && d.month == mo && d.date == day) {
          d.add(e);
        }
      }
    }
    return e;
  }

  /**
   * Gets the days between the two dates
   */
  private int daysBetween(Calendar startDate, Calendar endDate) {
    startDate.set(Calendar.HOUR_OF_DAY, 0);
    startDate.set(Calendar.MINUTE, 0);
    startDate.set(Calendar.SECOND, 1);
    long end = endDate.getTimeInMillis();
    long start = startDate.getTimeInMillis();
    return (int)TimeUnit.MILLISECONDS.toDays(end - start);
  }

  /**
   * Gets the weeks between the two dates
   */
  private int weeksBetween(Calendar startDate, Calendar endDate) {
    startDate.set(Calendar.HOUR_OF_DAY, 0);
    startDate.set(Calendar.MINUTE, 0);
    startDate.set(Calendar.SECOND, 0);
    int start = (int)TimeUnit.MILLISECONDS.toDays(
            startDate.getTimeInMillis())
            - startDate.get(Calendar.DAY_OF_WEEK);
    int end = (int)TimeUnit.MILLISECONDS.toDays(
            endDate.getTimeInMillis());
    return (end - start) / 7;
  }

  /**
   * Get's the addon to the event summary depending on the time
   * of the event and the current time
   *
   * @param e event
   * @param cal current time
   * @return a string addon
   */
  private String getAddon(Event e, Calendar cal) {
    Calendar ec = e.getCal();
    int diffD = daysBetween(cal, ec);
    int diffW = weeksBetween(cal, ec);
    String dow = DAYS_OF_WEEK[ec.get(Calendar.DAY_OF_WEEK) - 1];

    if (diffD == 0) {//same day
      return " today at " + e.time;
    }
    if (diffD == 1) {//tomorrow
      return " tomorrow at " + e.time;
    }
    if (diffW == 0) {//same week
      return " " + dow + " at " + e.time;
    }
    else if (diffW == 1) {//next week
      return " next " + dow;// + " at " + e.time;
    }
    else if (diffD >= 1) {//event is later
      if (e.day.month
              == cal.get(Calendar.MONTH)) {
        return " the " + e.day.date
                + getDayOfMonthSuffix(e.day.date);
      }
      else {
        return " next month";
      }
    }
    return "";
  }

  private String getDayOfMonthSuffix(int n) {
    if (n >= 11 && n <= 13) {
      return "th";
    }
    switch (n % 10) {
      case 1:  return "st";
      case 2:  return "nd";
      case 3:  return "rd";
      default: return "th";
    }
  }

  /**
   * Day class
   */
  private class Day {
    List<Event> events;
    int date, month, year;

    Day(int date, int month, int year) {
      this.date = date;
      this.month = month;
      this.year = year;
      this.events = new ArrayList<>();
    }

    void add(Event e) {
      this.events.add(e);
      e.day = this;
    }
  }

  /**
   * Event class
   */
  private class Event {
    String summary, description, time;
    Date date;
    Day day;
    float w = 1f;

    Event(String summary, String description, Date date) {
      this.summary = summary;
      this.description = description;
      this.time = new SimpleDateFormat("h:mmaa")
              .format(date).toLowerCase();
      this.date = date;
    }

    Calendar getCal() {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      return cal;
    }

    void animateEntry(int level) {
      new Animator(1f, 0f)
              .setDuration(500)
              .setStartDelay(level * 80)
              .setInterpolator(new OvershootInterpolator())
              .addUpdateListener(value -> {
                w = value;
                repaint();
              }).start();
    }

    void animateExit(int level) {
      new Animator(0f, 1f)
              .setDuration(500)
              .setStartDelay(level * 80)
              .setInterpolator(new AccelerateInterpolator())
              .addUpdateListener(value -> {
                w = value;
                repaint();
              }).start();
    }

    void animateExit(int level, Animator.EndListener end) {
      new Animator(0f, 1f)
              .setDuration(500)
              .setStartDelay(level * 80)
              .setInterpolator(new AccelerateInterpolator())
              .addUpdateListener(value -> {
                w = value;
                repaint();
              })
              .addEndListener(end)
              .start();
    }
  }

  private void startSlideshow() {
    animateEntry();
    javax.swing.Timer t = new Timer(4000 * MAX_COUNT,
            event -> {
              animateExit();
            });
    //t.start();
  }

  /**
   * Animates the entry of all the events which are showing
   */
  private void animateEntry() {
    for (int i=0; i<showing.length; i++) {
      showing[i].animateEntry(i);
    }
  }

  /**
   * Animates the exit of all the events which are showing
   */
  private void animateExit() {
    animateExit(() -> {
      current++;
      updateShowing();
      animateEntry();
    });
  }

  /**
   * Animates the exit of all the events which are showing
   */
  private void animateExit(Animator.EndListener end) {
    for (int i=0; i<showing.length; i++) {
      if (i < showing.length - 1)
        showing[i].animateExit(i);
      else
        showing[i].animateExit(i, end);
    }
  }

}
