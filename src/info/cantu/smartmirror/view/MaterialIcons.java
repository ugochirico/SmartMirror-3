package info.cantu.smartmirror.view;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Viviano on 5/9/2016.
 */
public class MaterialIcons {

  public static Font font;

  private static Map<String, String> codes;

  public static void load() throws IOException, FontFormatException {
    codes = new HashMap<>();

    Scanner scan = new Scanner(
            MaterialIcons.class.getResourceAsStream("/icons.csv"));
    String[] code = scan.nextLine().split(",");
    String[] name = scan.nextLine().split(",");
    scan.close();

    for (int i = 0; i < code.length; i++) {
      codes.put(name[i], "" + (char) Integer.parseInt(code[i], 16));
    }

    font = Font.createFont(Font.TRUETYPE_FONT,
            MaterialIcons.class.getResourceAsStream("/materialdesignicons-webfont.ttf"));
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(font);
  }

  public static String getIcon(String name) {
    return codes.get(name);
  }
}
