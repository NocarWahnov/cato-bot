package getNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlHandler {
    private String url = "https://www.guildwars2.com/de/news/";
    private String parentSelector = "li.blog-post";
    private String titleSelector = "h3.blog-title";
    private String linkSelector = "h3.blog-title > a";
    private String paragraphSelector = "div.text > p:first-child";
    private String dateSelector = "p.blog-attribution";

    Document feed = null;

    public String handleHtml() {
        String htmlToBB = "";

        try {
            feed = Jsoup.connect(url).get();
            Elements oneNews = feed.select(parentSelector);

            for (Element tlcd : oneNews) {
                htmlToBB = htmlToBB +
                        "[b][size=" + 14 + "][url=" + tlcd.select(linkSelector).attr("href") + "]" + tlcd.select(titleSelector).text() + "[/url][/size][/b]" + '\n' +
                        "[size=" + 12 + "]" + tlcd.select(paragraphSelector).text() + "[/size]" + '\n' +
                        "[size=" + 8 + "]" + tlcd.select(dateSelector).text() + "[/size]" + '\n' + '\n';
            }

        } catch (IOException error) {
            System.err.println("Error fetching website: " + url);
        }
        System.out.println(htmlToBB.length());
        return htmlToBB;
    }
}