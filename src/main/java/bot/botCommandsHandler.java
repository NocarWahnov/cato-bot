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
        //api.registerEvent(TS3EventType.TEXT_CHANNEL);
        api.registerAllEvents();

        //Listen to Channel Messages
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String inputMessage = e.getMessage().toLowerCase();

                    switch (inputMessage) {
                        case "!ping":
                            api.sendPrivateMessage(e.getInvokerId(), "Hi, how can I !help? :)");
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
                            api.sendPrivateMessage(e.getInvokerId(), "Available commands: !id, !shutdown, !wherebot, !aboutme, !edit ID SOURCE");
                            break;

                        case "!id":
                            Id id = new Id(api);
                            api.sendPrivateMessage(e.getInvokerId(), id.tellChannelID());
                            break;

                        case "!shutdown":
                            api.sendPrivateMessage(e.getInvokerId(),"Goodbye!");
                            query.exit();
                            break;

                        case "!wherebot":
                            int channelOfPoster = api.whoAmI().getChannelId();
                            api.sendPrivateMessage(e.getInvokerId(),"Result: " + channelOfPoster);
                            break;

                        case "!aboutme":
                            int posterID = e.getInvokerId();
                            String whereIsClient = String.valueOf(api.getClientInfo(posterID));
                            //client_channel_group_inherited_channel_id=2
                            api.sendPrivateMessage(e.getInvokerId(),whereIsClient);
                            break;
                    }

                    if (inputMessage.startsWith("!edithtml")) {
                        String[] filterInt = inputMessage.split("\\s+");
                        int channelNumber = Integer.parseInt(filterInt[1]);

                        HtmlHandler htmlHandler = new HtmlHandler();

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