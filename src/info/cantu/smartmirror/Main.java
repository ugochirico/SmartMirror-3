package info.cantu.smartmirror;

import info.cantu.smartmirror.model.BaseModel;
import info.cantu.smartmirror.model.clock.ClockModel;
import info.cantu.smartmirror.model.clock.ClockView;
import info.cantu.smartmirror.model.clock.DateView;
import info.cantu.smartmirror.model.news.NewsModel;
import info.cantu.smartmirror.model.news.NewsView;
import info.cantu.smartmirror.model.weather.WeatherForecastView;
import info.cantu.smartmirror.model.weather.WeatherIcons;
import info.cantu.smartmirror.model.weather.WeatherModel;
import info.cantu.smartmirror.model.weather.WeatherView;
import info.cantu.smartmirror.view.Fonts;
import info.cantu.smartmirror.view.MainView;
import info.cantu.smartmirror.view.MaterialIcons;
import info.cantu.smartmirror.view.Widget;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
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

    //add date & time
    addWidget(new ClockView(), clock);
    addWidget(new DateView(), clock);

    //add weather
    addWidget(new WeatherView(), weather);
    addWidget(new WeatherForecastView(), weather);

    addWidget(new GlueWidget(), null);
    //add news
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
