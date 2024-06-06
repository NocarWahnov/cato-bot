package getNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlHandler {
    private final String url; // !edithtml 1 https://www.guildwars2.com/de/news/ li.blog-post h3.blog-title h3.blog-title>a div.text>p:first-child p.blog-attribution
    private final String parentSelector; // !edithtml 3 https://9to5mac.com/ article.article.standard h2.h1 h2.h1>a div.article__content>div.article__excerpt>p div.post-meta>span.meta__post-date
    private final String titleSelector;
    private final String linkSelector;
    private final String paragraphSelector;
    private final String dateSelector;

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
                String href = tlcd.select(linkSelector).attr("href");
                if (href.startsWith("/")) {
                    href = url + href;
                }

                htmlToBB = htmlToBB +
                        "[b][size=" + 14 + "][url=" + href + "]" + tlcd.select(titleSelector).text() + "[/url][/size][/b]" + '\n' +
                        "[size=" + 12 + "]" + tlcd.select(paragraphSelector).text() + "[/size]" + '\n' +
                        "[size=" + 8 + "]" + tlcd.select(dateSelector).text() + "[/size]" + '\n' + '\n';

                if (htmlToBB.length() > 7500) {
                    System.out.println(url + " Feed Size: " + htmlToBB.length());
                    return htmlToBB;
                }
            }

        } catch (IOException error) {
            System.err.println("Error fetching website: " + url + error.getMessage());
        }
        System.out.println(url + " Feed Size: " + htmlToBB.length());
        return htmlToBB;
    }
}