package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import config.BackupHandler;
import config.YamlHandler;
import getNews.HtmlHandler;
import getNews.XmlHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledExecutorService;

public class BotCommandsHandler {
    private final TS3Api api;
    private final TS3Query query;
    private final ScheduledExecutorService executor;

    YamlHandler yaml = new YamlHandler();
    BackupHandler backup = new BackupHandler();

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
                        case "!cato":
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
                            api.sendPrivateMessage(e.getInvokerId(), '\n' + "[b]Available commands:[/b]" + '\n' +
                                    "[u]General[/u]" + '\n' +
                                    "!shutdown - Disconnects the Bot from the server and disables any channel updates." + '\n' +
                                    '\n' + "[u]Feed Configuration[/u]" + '\n' +
                                    "!cid - Shows all Channels and their IDs." + '\n' +
                                    "!add CID RSS-URL - Only Accepts xml. For other sources check !editHTML." + '\n' +
                                    "!addHTML CID URL CSS-PARENT CSS-TITLE CSS-LINK CSS-DESCRIPTION CSS-DATE - Fetches Websites HTML with the defined CSS-Selectors. For examples, please visit the [url=]GitHub Repository[/url]" + '\n' +
                                    "!rm CID - Removes the feed from the given channel-id" + '\n' +
                                    '\n' + "[u]Backups[/u]" + '\n' +
                                    "!listbackup - Lists all available backup files." + '\n' +
                                    "!loadbackup FILENAME - Loads the backup file, if available."
                            );
                            break;

                        case "!cid":
                            Id id = new Id(api);
                            api.sendPrivateMessage(e.getInvokerId(), id.tellChannelID());
                            break;

                        case "!shutdown":
                            LocalDateTime time = LocalDateTime.now();
                            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
                            String formattedTime = time.format(format);

                            api.sendPrivateMessage(e.getInvokerId(), backup.makeBackup(formattedTime));

                            api.sendPrivateMessage(e.getInvokerId(),"Shutting down - Goodbye!");
                            query.exit();
                            executor.shutdown();
                            break;

                        case "!listbackup":
                            api.sendPrivateMessage(e.getInvokerId(), backup.listFiles());
                            break;
                    }

                    if (inputMessage.startsWith("!addhtml")) {
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

                        api.sendPrivateMessage(e.getInvokerId(),"Added custom html feed from  " + url + " to channel: " + channelNumber + " :)");
                    }
                    else if (inputMessage.startsWith("!add")) {
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
                    else if (inputMessage.startsWith("!loadbackup")) {
                        String[] splitCommand = inputMessage.split("\\s+");
                        String filename = splitCommand[1];

                        api.sendPrivateMessage(e.getInvokerId(), backup.loadBackup(filename));
                    }
                }
            }
        });
    }

}