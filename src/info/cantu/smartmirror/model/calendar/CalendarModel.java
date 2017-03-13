package info.cantu.smartmirror.model.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import info.cantu.smartmirror.model.BaseModel;

import java.io.*;
import java.util.*;

/**
 * Created by Viviano on 5/6/2016.
 */
public class CalendarModel extends BaseModel{

  /** Application name. */
  private static final String APPLICATION_NAME = "Smart Mirror";

  /** Directory to store user credentials for this application. */
  private static final java.io.File DATA_STORE_DIR = new java.io.File(
          System.getProperty("user.home"), ".credentials/smart-mirror.json");

  /** Global instance of the {@link FileDataStoreFactory}. */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY =
          JacksonFactory.getDefaultInstance();

  /** Global instance of the HTTP transport. */
  private static HttpTransport HTTP_TRANSPORT;

  /** Global instance of the scopes required by this quickstart.
   *
   * If modifying these scopes, delete your previously saved credentials
   * at ~/.credentials/calendar-java-quickstart.json
   */
  private static final List<String> SCOPES =
          Arrays.asList(CalendarScopes.CALENDAR_READONLY);

  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Creates an authorized Credential object.
   * @return an authorized Credential object.
   * @throws IOException
   */
  public static Credential authorize() throws IOException {
    // Load client secrets.
    InputStream in = CalendarModel.class.getResourceAsStream("/client_secret.json");
    GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
            new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(DATA_STORE_FACTORY)
                    .setAccessType("offline")
                    .build();
    Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
//    System.out.println(
//            "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
    return credential;
  }


  /**
   * Build and return an authorized Calendar client service.
   * @return an authorized Calendar client service
   * @throws IOException
   */
  public static com.google.api.services.calendar.Calendar
  getCalendarService() throws Exception {
    Credential credential = authorize();
    return new com.google.api.services.calendar.Calendar.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
  }


  //model starts here
  private List<Event> items;
  private CalendarEvent[] events;
  private CalendarDay[][] month;

  /**
   * Time in milliseconds it will take for this model to onUpdate it's data
   *
   * @return a time in milliseconds
   */
  @Override
  protected int getInterval() {
    return 1000 * 60 * 1;
  }

  /**
   * Updates the data of this model
   * <p>
   * do not call this method directly instead call the update() method
   */
  @Override
  protected void onUpdate() {
    try {
      // Build a new authorized API client service.
      // Note: Do not confuse this class with the
      //   com.google.api.services.calendar.model.Calendar class.
      com.google.api.services.calendar.Calendar service =
              getCalendarService();

      Calendar curr = Calendar.getInstance();
      Calendar min = new GregorianCalendar(
          curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), 1);
      Calendar max = new GregorianCalendar(
          curr.get(Calendar.YEAR), curr.get(Calendar.MONTH) + 1 % 12, 7);
      //get all events from this month and the first week of next month

      CalendarList calendars = service.calendarList().list().execute();
      List<CalendarListEntry> lst = calendars.getItems();

      //clear items
      items = null;//TODO why is this null?
      for (CalendarListEntry cle : lst) {
        Events events = service.events().list(cle.getId())
            .setTimeMin(new DateTime(min.getTimeInMillis()))
            .setTimeMax(new DateTime(max.getTimeInMillis()))
            .execute();
        if (items == null)
          items = events.getItems();
        else
          items.addAll(events.getItems());
      }

      month = CalendarDay.generateMonth();
      //initializeEvents();

      invalidate();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }



//  private void initializeEvents() {
//    events = new CalendarEvent[items.size()];
//    int i = 0;
//    for (Event event : items) {
//      boolean isAllDay = false;
//      // all-day events have a null datetime but a valid date
//      DateTime start = event.getStart().getDateTime();
//      if (start == null) {
//        start = event.getStart().getDate();
//        isAllDay = true;
//      }
//      Date d = new Date(start.getValue());
//
//
//      int mo = date.getMonth();
//      int day = date.getDate();
//
//      CalendarView.Event e = new CalendarView.Event(summary, description, date);
//      for (Day[] arr : days) {
//        for (Day d : arr) {
//          if (d != null && d.month == mo && d.date == day) {
//            d.add(e);
//          }
//        }
//      }
//
//      // initialize CalendarEvent
//      events[i] = new CalendarEvent(event.getSummary(), d, isAllDay, );
//      i++;
//    }
//  }

  /**
   * @return a month with the current events added
   */
  public CalendarDay[][] getMonth() {
    return month;
  }

  /**
   * @return all calendar events
   */
  public CalendarEvent[] getEvents() {
    return events;
  }

}
