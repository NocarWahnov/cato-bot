package config;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import getNews.HtmlHandler;
import getNews.XmlHandler;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoUpdateFeed {

    Logger logger = Logger.getLogger(AutoUpdateFeed.class.getName());

    private final TS3Api api;

    public AutoUpdateFeed(TS3Api api) {
        this.api = api;
    }

    /**
     * readNews reads the news.yaml file
     * If news.yaml is empty, abort the method (return), so the ScheduledExecutorService in RssBot won't get stuck
     * Else split each line of news.yaml into an array
     * If the array is bigger than one it is an addhtml command, so it needs the HtmlHandler. It then directly edits the channel descriptions
     * else it is a xml command, and it needs the XmlHandler. It then directly edits the channel descriptions
     * */

    public void readNews() {
        File file = new File("news/news.yaml");
        try {
            Yaml yaml = new Yaml();
            InputStream fis = new FileInputStream(file);
            Map<Integer, Object> newsYaml = yaml.load(fis);

            if (newsYaml == null || newsYaml.isEmpty()) {
                logger.info("news.yaml is currently empty.");
                return;
            }

            for (Map.Entry<Integer, Object> entry : newsYaml.entrySet()) {
                String[] size = entry.getValue().toString().split(" ");
                String url = size[0];

                if (size.length > 1) {
                    String parent = size[1];
                    String title = size[2];
                    String link = size[3];
                    String description = size[4];
                    String date = size[5];
                    HtmlHandler html = new HtmlHandler(url, parent, title, link, description, date);
                    api.editChannel(entry.getKey(), ChannelProperty.CHANNEL_DESCRIPTION, html.handleHtml());
                } else {
                    XmlHandler xml = new XmlHandler(url);
                    api.editChannel(entry.getKey(), ChannelProperty.CHANNEL_DESCRIPTION, xml.handleXml());
                }
            }

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,"Error loading news.yaml to auto refresh feeds in Class AutoUpdateFeed.java ", e.getMessage());
        } catch (TS3CommandFailedException ts) {
            System.err.println("Error while auto updating one feed." + ts.getMessage());
        }
    }
}
