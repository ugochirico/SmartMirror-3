package info.cantu.smartmirror.model.news;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import info.cantu.smartmirror.model.BaseModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Viviano on 5/8/2016.
 */
public class NewsModel extends BaseModel {

  private final List<SyndEntry> entries = new ArrayList<>();

  /**
   * Time in milliseconds it will take for this model to onUpdate it's data
   *
   * @return a time in milliseconds
   */
  @Override
  protected int getInterval() {
    return 1000 * 3600 * 24;
  }

  /**
   * Updates the data of this model
   * <p>
   * do not call this method directly instead call the update() method
   */
  @Override
  protected void onUpdate() {
    entries.clear();
    addFromRss("http://feeds.reuters.com/reuters/topNews");
    addFromRss("http://feeds.reuters.com/reuters/technologyNews");
    addFromRss("http://feeds.reuters.com/reuters/businessNews");
  }

  private void addFromRss(String address) {
    try {
      URL url = new URL(address);
      HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
      // Reading the feed
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feed = input.build(new XmlReader(httpcon));
      entries.addAll(feed.getEntries());

//      Iterator<SyndEntry> itEntries = entries.iterator();
//      while (itEntries.hasNext()) {
//        SyndEntry entry = itEntries.next();
//        System.out.println("Title: " + entry.getTitle());
//        System.out.println("Link: " + entry.getLink());
//        System.out.println("Author: " + entry.getAuthor());
//        System.out.println("Publish Date: " + entry.getPublishedDate());
//        System.out.println("Description: " + entry.getDescription().getValue());
//        System.out.println();
//      }

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (FeedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public String[] getHeadlines() {
    //set removes duplicates
    Set<String> set = new TreeSet<>();
    for (SyndEntry e : entries) {
      set.add(e.getTitle());
    }
    String[] arr = new String[set.size()];
    return set.toArray(arr);
  }
}
