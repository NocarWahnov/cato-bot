import bot.ChannelEditor;
import bot.LoginBot;
import bot.botCommandsHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import getNews.Converter;
import getNews.Extractor;

public class tsRssBot {

    public static void main(String[] args) {

        Extractor extractor = new Extractor();
        extractor.fetchWebsite();
        extractor.extractWebsite();

        String ipAddress = "nocars.tk";
        TS3Config config = new TS3Config();
        config.setHost(ipAddress);
        config.setEnableCommunicationsLogging(true);

        TS3Query query = new TS3Query(config);
        query.connect();

        TS3Api api = query.getApi();
        LoginBot bot = new LoginBot(api);
        bot.connect();

        //Handle incoming commands
        botCommandsHandler botCommandsHandler = new botCommandsHandler(api, query);
        botCommandsHandler.commands();

        //Edit the Channel to RSS Feed
        ChannelEditor channelEditor = new ChannelEditor(api);
        Converter converter = new Converter();
        channelEditor.editChannel(
                (converter.headlineWithLink(extractor.title, extractor.link) +
                        converter.paragraph(extractor.paragraph) +
                        converter.date(extractor.date))
        );
    }
}