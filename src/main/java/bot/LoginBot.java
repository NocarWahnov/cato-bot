package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;

public class LoginBot {
    private final TS3Api api;

    public LoginBot(TS3Api api) {
        this.api = api;
    }

    String username = "serveradmin";
    String password = "2jtwh++y";
    String nickname = "LeBot";
    String arrivalMessage = nickname + " is online!";

    public void connect () {
        api.login(username, password);
        api.selectVirtualServerById(1);
        api.setNickname(nickname);
        api.sendChannelMessage(arrivalMessage);
    }
}
