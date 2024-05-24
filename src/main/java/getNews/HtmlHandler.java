package getNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlHandler {
    private final String url; // = "https://www.guildwars2.com/de/news/", "https://9to5mac.com/"
    private final String parentSelector; // = "li.blog-post", "article.article.standard"
    private final String titleSelector; // = "h3.blog-title", "h2.h1"
    private final String linkSelector; // = "h3.blog-title > a"; "h2.h1>a"
    private final String paragraphSelector; // = "div.text > p:first-child", "div.article__content>div.article__excerpt>p"
    private final String dateSelector; // = "p.blog-attribution", "div.post-meta>span.meta__post-date"

    Document feed = null;

    public HtmlHandler(String url, String parentSelector, String titleSelector, String linkSelector, String paragraphSelector, String dateSelector) {
        this.url = url;
        this.parentSelector = parentSelector;
        this.titleSelector = titleSelector;
        this.linkSelector = linkSelector;
        this.paragraphSelector = paragraphSelector;
        this.dateSelector = dateSelector;
    }

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

                if (htmlToBB.length() > 7500) {
                    return htmlToBB;
                }
            }

        } catch (IOException error) {
            System.err.println("Error fetching website: " + url + error.getMessage());
        }
        //System.out.println(htmlToBB.length());
        return htmlToBB;
    }
}