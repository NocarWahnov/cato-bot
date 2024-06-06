package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import config.YamlHandler;
import getNews.HtmlHandler;
import getNews.XmlHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;

public class BotCommandsHandler {
    private final TS3Api api;
    private final TS3Query query;
    private final ScheduledExecutorService executor;

    YamlHandler yaml = new YamlHandler();

    public BotCommandsHandler(TS3Api api, TS3Query query, ScheduledExecutorService executor) {
        this.api = api;
        this.query = query;
        this.executor = executor;
    }

    public void commands () {
        //Own Client ID (Bot ID), to prevent listening to own messages
        final int clientId = api.whoAmI().getId();

        //Type of event to listen to
        api.registerAllEvents();

        //Listen to Channel Messages
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String inputMessage = e.getMessage().toLowerCase();

                    switch (inputMessage) {
                        case "!ping":
                            api.sendPrivateMessage(e.getInvokerId(), "Hello there! Type !h for an overview of available commands.");
                            break;
                    }
                }
            }
        });

        //Listen to Private Messages
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {

                if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {

                    String inputMessage = e.getMessage().toLowerCase();

                    switch (inputMessage) {
                        case "!help", "!h":
                            api.sendPrivateMessage(e.getInvokerId(), "Available commands:" + '\n' +
                                    "!cid - Shows all Channels and their IDs." + '\n' +
                                    "!shutdown - Disconnects the Bot from the server and disables any channel updates." + '\n' +
                                    "!edit CID RSS-URL - Defines a channel and a source the Bot is supposed to edit. Only Accepts xml. For other sources check !editHTML." + '\n' +
                                    "!editHTML CID URL CSS-PARENT CSS-TITLE CSS-LINK CSS-DESCRIPTION CSS-DATE - Defines a channel and a source the Bot is supposed to edit. Fetches Websites HTML with defined CSS-Selectors. For examples, please visit the [url=]GitHub Repository[/url]" + '\n' +
                                    "!rm CID - Removes the feed from the given channel-id");
                            break;

                        case "!cid":
                            Id id = new Id(api);
                            api.sendPrivateMessage(e.getInvokerId(), id.tellChannelID());
                            break;

                        case "!shutdown":
                            LocalDateTime time = LocalDateTime.now();

                            try {
                                Path copied = Paths.get("src/main/resources/backups/news-" + time + ".yaml");
                                Path originalPath = Paths.get("src/main/resources/news.yaml");
                                Files.copy(originalPath, copied);

                            } catch (IOException ex) {
                                api.sendPrivateMessage(e.getInvokerId(),"Error backing up news.yaml!");
                            }

                            api.sendPrivateMessage(e.getInvokerId(), "Successfully backed up news.yaml as news-" + time + ".yaml");
                            api.sendPrivateMessage(e.getInvokerId(),"Shutting down - Goodbye!");
                            query.exit();
                            executor.shutdown();

                            break;
                    }

                    if (inputMessage.startsWith("!edithtml")) {
                        String[] splitCommand = inputMessage.split("\\s+");
                        int channelNumber = Integer.parseInt(splitCommand[1]);
                        String url = splitCommand[2].replaceAll("\\[.*?\\] ?", "");
                        String parent = splitCommand[3];
                        String title = splitCommand[4];
                        String link = splitCommand[5];
                        String description = splitCommand[6];
                        String date = splitCommand[7];

                        HtmlHandler htmlHandler = new HtmlHandler(url, parent, title, link, description, date);

                        api.editChannel(channelNumber, ChannelProperty.CHANNEL_DESCRIPTION, htmlHandler.handleHtml());

                        String store = url + " " + parent + " " + title + " " + link + " " + description + " " + date;
                        yaml.addNews(channelNumber, store);
                        yaml.writeNews();

                        api.sendPrivateMessage(e.getInvokerId(),"Added custom html feed from " + url + " to channel: " + channelNumber + " :)");
                    }
                    else if (inputMessage.startsWith("!edit")) {
                        String[] splitCommand = inputMessage.split("\\s+");
                        int channelNumber = Integer.parseInt(splitCommand[1]);
                        String url = splitCommand[2].replaceAll("\\[.*?\\] ?", "");

                        XmlHandler xmlHandler = new XmlHandler(url);

                        api.editChannel(channelNumber, ChannelProperty.CHANNEL_DESCRIPTION, xmlHandler.handleXml());

                        yaml.addNews(channelNumber, url);
                        yaml.writeNews();

                        api.sendPrivateMessage(e.getInvokerId(),"Added rss-feed from" + url + " to channel: " + channelNumber + " :)");
                    }
                    else if (inputMessage.startsWith("!rm")) {
                        String[] splitCommand = inputMessage.split("\\s+");
                        int channelNumber = Integer.parseInt(splitCommand[1]);

                        api.editChannel(channelNumber, ChannelProperty.CHANNEL_DESCRIPTION, "");
                        yaml.removeNews(channelNumber);
                        yaml.writeNews();

                        api.sendPrivateMessage(e.getInvokerId(),"Removed feed from channel: " + channelNumber + " :)");
                    }
                }
            }
        });
    }

}