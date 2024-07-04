package getNews;

import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
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

    int parameterSize = 6000;

    /**
    * - Uses JSoup to load a xml source
    * - Formats the Input to BB Code for Teamspeak
    * - Returns a String with the formatted Text, so it can be used by the Teamspeak API to edit the Channel
    * - Checks the size of the String in the for loop, to avoid an exception to the Teamspeak API if the String is too big for a Channel Description
    * - Checks the size of the xml description tag, to avoid an exception to the Teamspeak API if the String is too big for a Channel Description
    * */

    public String handleXml () {
        String xmlToBB = "";
        try {
            xml = Jsoup.connect(url).get();
            Elements items = xml.select("item");

                for (Element item : items) {
                    String description = item.select("description").first().ownText().replaceAll("\\<.*?\\>", " ");

                    if (description.length() > 500) {
                        description = description.substring(0, 500) + "...";
                    }

                    xmlToBB = xmlToBB + "[b][size=" + 14 + "][url=" + item.select("link").first().ownText() + "]" + item.select("title").first().ownText() + "[/url][/size][/b]" + '\n' +
                            "[size=" + 12 + "]" + description + "[/size]" + '\n' +
                            "[size=" + 8 + "]" + item.select("pubDate").first().ownText() + "[/size]" + '\n' + '\n';

                    if (xmlToBB.length() > parameterSize) {
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