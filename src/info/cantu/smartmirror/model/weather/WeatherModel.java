package info.cantu.smartmirror.model.weather;

import info.cantu.smartmirror.model.BaseModel;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.HourlyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;

import java.util.Date;


/**
 * Created by Viviano on 5/6/2016.
 */
public class WeatherModel extends BaseModel {

  private static final String API_KEY = "607ce4032bdb7c7d41ceb85dc820725f";

  private CurrentWeather current;
  private DailyForecast daily;
  private HourlyForecast hourly;

  /**
   * Time in milliseconds it will take for this model to onUpdate it's data
   *
   * @return a time in milliseconds
   */
  @Override
  protected int getInterval() {
    return 1000 * 1800;
  }

  /**
   * Updates the data of this model
   */
  @Override
  protected void onUpdate() {
//    new Thread(() -> {//new Thread to avoid UI lag
      OpenWeatherMap map = new OpenWeatherMap(OpenWeatherMap.Units.IMPERIAL, API_KEY);

      //TODO: get own gps location
      float lat = 40.991865f, lon = -73.99127099999998f;

      this.current = map.currentWeatherByCoordinates(lat, lon);
      this.daily = map.dailyForecastByCoordinates(lat, lon, (byte) 5);
      this.hourly = map.hourlyForecastByCoordinates(lat, lon);
      invalidate();
//    }).start();
  }

  /**
   * @return the current weather code
   */
  public String getCurrentCode() {
    return iconFromCode(current.getWeatherInstance(0).getWeatherCode());
  }

  /**
   * @return the current temperature
   */
  public float getCurrentTemp() {
    return current.getMainInstance().getTemperature();
  }

  /**
   * @return the weather codes for the daily forecast
   */
  public String[] getDailyCodes() {
    String[] res = new String[daily.getForecastCount()];
    for (int i = 0; i < res.length; i++) {
      res[i] = "" + iconFromCode(
              daily.getForecastInstance(i).getWeatherInstance(0).getWeatherCode(),
              true);
    }
    return res;
  }

  /**
   * @return the temperature for the daily forecast
   */
  public float[] getDailyTemps() {
    float[] res = new float[daily.getForecastCount()];
    for (int i = 0; i < res.length; i++) {
      res[i] = daily.getForecastInstance(i).getTemperatureInstance().getDayTemperature();
    }
    return res;
  }

  /**
   * @return the weather codes for the hourly forecast
   */
  public String[] getHourlyCodes() {
    Date[] times = getHourlyTimes();
    Date rise = getSunRise();
    Date set = getSunSet();

    String[] res = new String[hourly.getForecastCount()];
    for (int i = 0; i < res.length; i++) {
      res[i] = "" + iconFromCode(
              hourly.getForecastInstance(i).getWeatherInstance(0).getWeatherCode(),
              times[i].after(rise) && times[i].before(set));
    }
    return res;
  }

  /**
   * @return the temperature for the hourly forecast
   */
  public float[] getHourlyTemps() {
    float[] res = new float[hourly.getForecastCount()];
    for (int i = 0; i < res.length; i++) {
      res[i] = hourly.getForecastInstance(i).getMainInstance().getTemperature();
    }
    return res;
  }

  /**
   * @return the times for each weather instance for the daily forecast
   */
  public Date[] getDailyTimes() {
    Date[] res = new Date[daily.getForecastCount()];
    for (int i = 0; i < res.length; i++) {
      res[i] = daily.getForecastInstance(i).getDateTime();
    }
    return res;
  }

  /**
   * @return the times for each weather instance for the hourly forecast
   */
  public Date[] getHourlyTimes() {
    Date[] res = new Date[hourly.getForecastCount()];
    for (int i = 0; i < res.length; i++) {
      res[i] = hourly.getForecastInstance(i).getDateTime();
    }
    return res;
  }

  /**
   * @return the time of the sun rise
   */
  public Date getSunRise() {
    return current.getSysInstance().getSunriseTime();
  }

  /**
   * @return the time of the sun set
   */
  public Date getSunSet() {
    return current.getSysInstance().getSunsetTime();
  }

  private String iconFromCode(int code, boolean day) {
    String pre = day ? "day_" : "night_";
    String str = pre + code;
    return WeatherIcons.getIcon(str);
  }

  private String iconFromCode(int code) {
    String pre = isDay() ? "day_" : "night_";
    String str = pre + code;
    return WeatherIcons.getIcon(str);
  }

  private boolean isDay() {
    try {
      Date rise = getSunRise();
      Date set = getSunSet();
      Date curr = new Date();

      return curr.before(set) && curr.after(rise);
    } catch (Exception e) {
      //if OWM does not return any sunrise/set data just use the default day icon
      return true;
    }
  }
}
