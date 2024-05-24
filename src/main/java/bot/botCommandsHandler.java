package bot;

import bot.commands.Id;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import getNews.HtmlHandler;
import getNews.XmlHandler;

public class botCommandsHandler {
    private final TS3Api api;
    private final TS3Query query;

    public botCommandsHandler(TS3Api api, TS3Query query) {
        this.api = api;
        this.query = query;
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
                                    "!edit CID RSS-SOURCE - Defines a channel and a source the Bot is supposed to edit. Only Accepts xml. For other sources check !editHTML." + '\n' +
                                    "!editHTML CID SOURCE CSS-TITLE CSS-LINK CSS-DESCRIPTION CSS-DATE - Defines a channel and a source the Bot is supposed to edit. Fetches Websites HTML with defined CSS-Selectors. For examples, please visit the [url=]GitHub Repository[/url]");
                            break;

                        case "!cid":
                            Id id = new Id(api);
                            api.sendPrivateMessage(e.getInvokerId(), id.tellChannelID());
                            break;

                        case "!shutdown":
                            api.sendPrivateMessage(e.getInvokerId(),"Shutting down - Goodbye!");
                            query.exit();
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
                        api.sendPrivateMessage(e.getInvokerId(),"Edited Channel: " + channelNumber + " :)");
                    }
                    else if (inputMessage.startsWith("!edit")) {
                        String[] splitCommand = inputMessage.split("\\s+");
                        int channelNumber = Integer.parseInt(splitCommand[1]);
                        String url = splitCommand[2].replaceAll("\\[.*?\\] ?", "");

                        XmlHandler xmlHandler = new XmlHandler(url);

                        api.editChannel(channelNumber, ChannelProperty.CHANNEL_DESCRIPTION, xmlHandler.handleXml());
                        api.sendPrivateMessage(e.getInvokerId(),"Edited Channel: " + channelNumber + " :)");
                    }
                }
            }
        });
    }

}