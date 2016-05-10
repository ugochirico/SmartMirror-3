package info.cantu.smartmirror.model.weather;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Viviano on 5/7/2016.
 */
public class WeatherIcons {

  public static Font font;

  private static Map<String, String> codes, names;

  public static void load() throws IOException, FontFormatException {
    codes = new HashMap<>();
    names = new HashMap<>();

    Scanner scan = new Scanner(
            WeatherIcons.class.getResourceAsStream("/wi_name_code.csv"));
    while (scan.hasNext()) {
      String line = scan.nextLine();
      String[] arr = line.split(",");
      if (arr[0].contains("wi_owm")) {
        codes.put(arr[0], "" + (char) Integer.parseInt(arr[1], 16));
      }
    }

    //names arent really used but are there for convenience
    scan = new Scanner(
            WeatherIcons.class.getResourceAsStream("/omw_wi_name.csv"));
    while (scan.hasNext()) {
      String line = scan.nextLine();
      String[] arr = line.split(",");
      names.put(arr[0], arr[1]);
    }
    scan.close();

    font = Font.createFont(Font.TRUETYPE_FONT,
            WeatherIcons.class.getResourceAsStream("/weathericons-regular-webfont.ttf"));
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(font);

  }

  public static String getIcon(String code) {
    return codes.get("wi_owm_" + code);
  }

  public static String getName(String code) {
    return names.get(code.substring(7));
  }
}
