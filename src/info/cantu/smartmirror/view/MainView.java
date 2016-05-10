package info.cantu.smartmirror.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Viviano on 5/4/2016.
 */
public class MainView extends JFrame {


  public void initialize(List<Widget> widgets) {

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);//fullscreen
    this.setUndecorated(true);//remove titlebar

    JPanel background = backgroundPane();

    for (Widget w : widgets) {
      JPanel holder = widgetHolder();
      holder.add(w);
      background.add(holder);
      w.onInitialize();
    }

    add(background);
    pack();
    this.setVisible(true);//shows
  }


  private JPanel backgroundPane() {
    JPanel background = new JPanel();
    background.setBackground(Color.BLACK);
    background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
    background.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    return background;
  }

  private JPanel widgetHolder() {
    JPanel panel = new JPanel();
    panel.setBackground(Color.BLACK);
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.add(Box.createHorizontalGlue());
    return panel;
  }

}
