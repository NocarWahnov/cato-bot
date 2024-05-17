package bot.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

import java.util.List;

public class Id {

    private final TS3Api api;

    public Id(TS3Api api) {
        this.api = api;
    }

    //looking for: channel_name=Welcome cid=1, ...
    public String tellChannelID () {
        //Save all channels in a List
        List<Channel> allChannels = api.getChannels();
        String message = "";

        //For each Channel api.getChannels
        for (Channel channelEigenschaft : allChannels) {
            String completeInfo = channelEigenschaft.toString();

            //Split data of every Channel and filter relevant Information
            for (String filteredInfo : completeInfo.split(",")) {
                if (filteredInfo.contains("channel_name")) {
                    message = message + filteredInfo + ", ";
                }
                if (filteredInfo.contains("cid")) {
                    message = message + filteredInfo + '\n';
                }
            }
        }
        return message;
    }
}
