package info.cantu.smartmirror;

import info.cantu.smartmirror.model.BaseModel;
import info.cantu.smartmirror.model.calendar.CalendarModel;
import info.cantu.smartmirror.model.calendar.CalendarView;
import info.cantu.smartmirror.model.clock.ClockModel;
import info.cantu.smartmirror.model.clock.ClockView;
import info.cantu.smartmirror.model.clock.DateView;
import info.cantu.smartmirror.model.news.NewsModel;
import info.cantu.smartmirror.model.news.NewsView;
import info.cantu.smartmirror.model.notifications.NotificationModel;
import info.cantu.smartmirror.model.weather.WeatherForecastView;
import info.cantu.smartmirror.model.weather.WeatherIcons;
import info.cantu.smartmirror.model.weather.WeatherModel;
import info.cantu.smartmirror.model.weather.WeatherView;
import info.cantu.smartmirror.view.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Viviano on 5/4/2016.
 */
public class Main {

  private static List<Widget> widgets;
  private static List<BaseModel> models;

  public static void main(String[] args) throws IOException, FontFormatException {
    Fonts.load();
    WeatherIcons.load();
    MaterialIcons.load();

    widgets = new ArrayList<>();
    models = new ArrayList<>();

    ClockModel clock = new ClockModel();
    models.add(clock);
    WeatherModel weather = new WeatherModel();
    models.add(weather);
    NewsModel news = new NewsModel();
    models.add(news);
    CalendarModel calendar = new CalendarModel();
    models.add(calendar);
    NotificationModel notifications = new NotificationModel();
    models.add(notifications);

    //add date & time
    addWidget(new ClockView(), clock);
    addWidget(new DateView(), clock);

    //add weather
    addWidget(new WeatherView(), weather);
    addWidget(new WeatherForecastView(), weather);

    //add calendar
    addWidget(new CalendarView(), calendar);

    //add news
    addWidget(new GlueWidget(), null);
    addWidget(new NewsView(), news);

    new MainView().initialize(widgets);
    for (BaseModel m : models)
      m.initialize();
  }

  private static void addWidget(Widget widget, BaseModel linkTo) {
    widgets.add(widget);
    if (linkTo != null)
      linkTo.addListener(widget);
  }

}
