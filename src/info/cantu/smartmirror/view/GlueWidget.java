package info.cantu.smartmirror.view;

import info.cantu.smartmirror.model.BaseModel;
import info.cantu.smartmirror.view.Widget;

import java.awt.*;

/**
 * Created by Viviano on 5/10/2016.
 *
 * Holds no purpose other than to let the main view add a
 * Vertical Glue in its place
 */
public class GlueWidget extends Widget {
  /**
   * The three methods below are used by the box layout
   * to force the preferred size
   */
  @Override
  public Dimension getPreferredSize() {
    return null;
  }

  /**
   * Will be called by the model every time it updates
   *
   * @param model model that calls this method
   */
  @Override
  public void onUpdate(BaseModel model) {

  }
}
