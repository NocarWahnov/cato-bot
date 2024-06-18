package getNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlHandler {
    private final String url;
    private final String parentSelector;
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

    /**
    * Uses the selectors (Strings that are collected by the constructor) to extract text out of a html document
    * Formats the extracted data to BBCode for Teamspeak
    * Returns a String with the formatted Text, so it can be used by the Teamspeak API to edit the Channel
    */

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

                if (htmlToBB.length() > 7000) {
                    System.out.println(url + " Feed Size: " + htmlToBB.length());
                    return htmlToBB;
                }
            }

        } catch (IOException e) {
            System.err.println("Error fetching website: " + url + " in Class HtmlHandler " + e.getMessage());
        }
        System.out.println(url + " Feed Size: " + htmlToBB.length());
        return htmlToBB;
    }
}