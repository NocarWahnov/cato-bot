package getNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Extractor {
    private String url = "https://www.guildwars2.com/de/news/";
    private String parentSelector = "li.blog-post";
    private String titleSelector = "h3.blog-title";
    private String linkSelector = "h3.blog-title > a";
    private String paragraphSelector = "div.text > p:first-child";
    private String dateSelector = "p.blog-attribution";

    Document feed = null;

    public void fetchWebsite() {
        try {
            feed = Jsoup.connect(url).get();

        } catch (IOException error) {
            System.out.println("ERROR!!!!");
        }
    }

    public String title;
    public String link;
    public String paragraph;
    public String date;

    public void extractWebsite() {
        Elements oneBlock = feed.select(parentSelector);

        for (Element tlcd : oneBlock) {
            title = tlcd.select(titleSelector).text();
            link = tlcd.select(linkSelector).attr("href");
            paragraph = tlcd.select(paragraphSelector).text();
            date = tlcd.select(dateSelector).text();

            //System.out.println(title);
            //System.out.println(link);
            //System.out.println(content);
            //System.out.println(date);
        }
    }
}