package info.cantu.smartmirror.model.notifications;

import com.github.sheigutn.pushbullet.Pushbullet;
import com.github.sheigutn.pushbullet.ephemeral.DismissalEphemeral;
import com.github.sheigutn.pushbullet.ephemeral.NotificationEphemeral;
import com.github.sheigutn.pushbullet.stream.PushbulletWebsocketClient;
import com.github.sheigutn.pushbullet.stream.PushbulletWebsocketListener;
import com.github.sheigutn.pushbullet.stream.message.PushStreamMessage;
import com.github.sheigutn.pushbullet.stream.message.StreamMessage;
import com.github.sheigutn.pushbullet.stream.message.StreamMessageType;
import info.cantu.smartmirror.model.BaseModel;

/**
 * Created by Viviano on 5/12/2016.
 */
public class NotificationModel extends BaseModel {

  private final String key = "o.DKCu6X4dZeXnozdHtk0JQo0rUpBwLmwD";

  /**
   * Time in milliseconds it will take for this model to onUpdate it's data
   *
   * @return a time in milliseconds
   */
  @Override
  protected int getInterval() {
    // the program will be notified of a change so no refresh is needed
    return -1;
  }

  /**
   * Updates the data of this model
   * <p>
   * do not call this method directly instead call the update() method
   */
  @Override
  protected void onUpdate() {
    Pushbullet pb = new Pushbullet(key);

    PushbulletWebsocketClient sock = pb.createWebsocketClient();
    sock.registerListener(new PushbulletWebsocketListener() {
      @Override
      public void handle(Pushbullet pushbullet, StreamMessage message) {

        if (message.getType() == StreamMessageType.PUSH) {
          PushStreamMessage psm = (PushStreamMessage)message;
          if (psm.getPush() instanceof NotificationEphemeral) {
            //handle notification
            NotificationEphemeral e = (NotificationEphemeral)psm.getPush();
            System.out.println("pack: " + e.getPackageName());
            System.out.println("name: " + e.getApplicationName());
            System.out.println("title: " + e.getTitle());
            System.out.println("body: " + e.getBody());
            System.out.println("noteid: " + e.getNotificationId());
            System.out.println("notetag: " + e.getNotificationTag());
            System.out.println();
          }
          else if (psm.getPush() instanceof DismissalEphemeral) {
            //handle dismissal
          }
        }
      }
    });
    sock.connect();
    System.out.println("pushbullet socket connected");
  }
}
