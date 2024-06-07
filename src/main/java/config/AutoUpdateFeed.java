package config;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import getNews.HtmlHandler;
import getNews.XmlHandler;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class AutoUpdateFeed {

    private final TS3Api api;

    public AutoUpdateFeed(TS3Api api) {
        this.api = api;
    }

    public void readNews() {
        File file = new File("news/news.yaml");
        try {
            Yaml yaml = new Yaml();
            InputStream fis = new FileInputStream(file);
            Map<Integer, Object> newsYaml = yaml.load(fis);

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
            System.err.println("Error loading news.yaml to auto refresh feeds in Class AutoUpdateFeed.java " + e.getMessage());
        }
    }
}
