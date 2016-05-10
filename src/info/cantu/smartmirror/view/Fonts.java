package info.cantu.smartmirror.view;

import java.awt.*;
import java.io.*;

/**
 * Created by Viviano on 5/5/2016.
 */
public class Fonts {

  public static Font bold, light, regular, thin;

  public static void load() throws IOException, FontFormatException {
    bold = Font.createFont(Font.TRUETYPE_FONT,
            Fonts.class.getResourceAsStream("/Roboto-Bold.ttf"));
    light = Font.createFont(Font.TRUETYPE_FONT,
            Fonts.class.getResourceAsStream("/Roboto-Light.ttf"));
    regular = Font.createFont(Font.TRUETYPE_FONT,
            Fonts.class.getResourceAsStream("/Roboto-Regular.ttf"));
    thin = Font.createFont(Font.TRUETYPE_FONT,
            Fonts.class.getResourceAsStream("/Roboto-Thin.ttf"));

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(bold);
    ge.registerFont(light);
    ge.registerFont(regular);
    ge.registerFont(thin);
  }
}