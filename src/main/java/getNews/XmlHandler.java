package getNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class XmlHandler {
    private final String url;

    public XmlHandler (String url) {
        this.url = url;
    }

    Document xml = null;

    public String handleXml () {
        String xmlToBB = "";
        try {
            xml = Jsoup.connect(url).get();
            Elements items = xml.select("item");

                for (Element item : items) {
                    xmlToBB = xmlToBB + "[b][size=" + 14 + "][url=" + item.select("link").first().ownText() + "]" + item.select("title").first().ownText() + "[/url][/size][/b]" + '\n' +
                            "[size=" + 12 + "]" + item.select("description").first().ownText() + "[/size]" + '\n' +
                            "[size=" + 8 + "]" + item.select("pubDate").first().ownText() + "[/size]" + '\n' + '\n';

                    if (xmlToBB.length() > 7500) {
                        System.out.println(url + " Feed Size: " + xmlToBB.length());
                        return xmlToBB;
                    }
                }

        } catch (IOException e) {
            System.err.println("Error fetching website: " + url + " in Class XmlHandler " + e.getMessage());
        }
        System.out.println(url + " Feed Size: " + xmlToBB.length());
        return xmlToBB;
    }
}
