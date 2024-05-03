package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class botCommands {
    private final TS3Api api;

    public botCommands(TS3Api api) {
        this.api = api;
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
                        api.sendChannelMessage(poster + ", " + posterID);
                    }
                    else if (inputMessage.equals("!whoami")) {
                        api.sendChannelMessage("Result: " + channelOfPoster);
                    }
                    else if (inputMessage.equals("!client")) {
                        //client_channel_group_inherited_channel_id=2
                        api.sendChannelMessage(whereIsClient);
                    }
                }
            }
        });
    }

}
