package bot;

import bot.commands.Id;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

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
        api.registerEvent(TS3EventType.TEXT_CHANNEL);

        //Registers all Inputs and reacts to them
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String inputMessage = e.getMessage().toLowerCase();
                    String poster = e.getInvokerName();
                    int posterID = e.getInvokerId();
                    int channelOfPoster = api.whoAmI().getChannelId();

                    String whereIsClient = String.valueOf(api.getClientInfo(posterID));

                    if (inputMessage.equals("!help") || inputMessage.equals("!h")) {
                        api.sendChannelMessage("Available commands: !id, !whoami, !client");
                    }
                    else if (inputMessage.equals("!id")) {
                        Id id = new Id(api);
                        id.tellChannelID();
                    }
                    else if (inputMessage.equals("!quit")) {
                        api.sendChannelMessage("Goodbye!");
                        query.exit();
                    }
                    else if (inputMessage.equals("!whoami")) {
                        api.sendChannelMessage("Result: " + channelOfPoster);
                    }
                    else if (inputMessage.equals("!me")) {
                        //client_channel_group_inherited_channel_id=2
                        api.sendChannelMessage(whereIsClient);
                    }
                }
            }
        });
    }

}
