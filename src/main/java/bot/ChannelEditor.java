package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelEditor {
    private final TS3Api api;

    String username = "serveradmin";
    String password = "2jtwh++y";
    String nickname = "TheBot";
    String arrivalMessage = nickname + " is online!";

    int chosenChannelID = 1;

    public ChannelEditor(TS3Api api) {
        this.api = api;
    }

    public void loginBot () {
        api.login(username, password);
        api.selectVirtualServerById(1);
        api.setNickname(nickname);
        api.sendChannelMessage(arrivalMessage);
    }
//looking for: channel_name=Welcome cid=1, ...
    public void tellChannelID () {

        List<Channel> allChannels = api.getChannels();
        List<String> filteredAllChannels = new ArrayList<>();

        for (Channel channelEigenschaft : allChannels) {
            filteredAllChannels.add(channelEigenschaft.toString());
        }

        for (String _ : filteredAllChannels) {
            String name = String.valueOf(filteredAllChannels.stream().filter(nameFilter -> nameFilter.contains("channel_name")));
            String id = String.valueOf(filteredAllChannels.stream().filter(idFilter -> idFilter.contains("cid")));
            api.sendChannelMessage(name + "\n" + id);
        }

    }

    public void editChannel (String newsMessage) {
        api.editChannel(chosenChannelID, ChannelProperty.CHANNEL_DESCRIPTION, newsMessage);
    }
}