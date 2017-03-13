package info.cantu.smartmirror.model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viviano on 5/4/2016.
 */
public abstract class BaseModel implements ActionListener {

  private final List<UpdateListener> listeners;

  public BaseModel() {
    listeners = new ArrayList<>();
  }

  public void initialize() {
    update();
    long interval = getInterval();
    if (interval > 0) {
      Timer timer = new Timer(getInterval(), this);
      timer.start();
    }
  }

  /**
   * updates this model and it's listeners
   */
  protected void update() {
    onUpdate();
    invalidate();
  }

  /**
   * updates the view
   * use to update view after a network call
   */
  protected void invalidate() {
    for (UpdateListener l : listeners) {
      l.onUpdate(this);
    }
  }

  /**
   * Time in milliseconds it will take for this model to onUpdate it's data
   *
   * @return a time in milliseconds
   */
  protected abstract int getInterval();

  /**
   * Updates the data of this model
   *
   * do not call this method directly instead call the update() method
   */
  protected abstract void onUpdate();

  /**
   * Invoked when an action occurs.
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    update();
  }

  /**
   * Add an onUpdate listener to this model
   *
   * @param listener UpdateListener to add
   */
  public void addListener(UpdateListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Register this listener to this model
   */
  public interface UpdateListener {
    /**
     * Will be called by the model every time it updates
     *
     * @param model model that calls this method
     */
    void onUpdate(BaseModel model);
  }
}
