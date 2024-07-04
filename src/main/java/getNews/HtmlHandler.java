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
     * When the selectors contain 0, nothing regarding the selector will be added to the feed.
    * Formats the extracted data to BBCode for Teamspeak
    * Returns a String with the formatted Text, so it can be used by the Teamspeak API to edit the Channel
    */

    public String handleHtml() {
        String htmlToBB = "";

        try {
            feed = Jsoup.connect(url).get();
            Elements newsBlock = feed.select(parentSelector);

            for (Element element : newsBlock) {
                String href = element.select(linkSelector).attr("href");
                String title = "";
                String description = "";
                String date = "";

                if (href.startsWith("/")) {
                    href = url + href;
                }

                if (!titleSelector.contains("0")) {
                    title = element.select(titleSelector).text();
                }

                if (!parentSelector.contains("0")) {
                    description = element.select(paragraphSelector).text();
                }

                if (!dateSelector.contains("0")) {
                    date = element.select(dateSelector).text();
                }

                htmlToBB = htmlToBB +
                        "[b][size=" + 14 + "][url=" + href + "]" + title + "[/url][/size][/b]" + '\n' +
                        "[size=" + 12 + "]" + description + "[/size]" + '\n' +
                        "[size=" + 8 + "]" + date + "[/size]" + '\n' + '\n';


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