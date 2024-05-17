import bot.botCommandsHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

public class tsRssBot {

    public static void main(String[] args) {

        String ipAddress = "nocars.tk";
        String username = "serveradmin";
        String password = "2jtwh++y";
        String nickname = "LeBot";
        String arrivalMessage = nickname + " is online!";

        TS3Config config = new TS3Config();
        config.setHost(ipAddress);
        config.setEnableCommunicationsLogging(true);

        //Connect and Login Bot
        TS3Query query = new TS3Query(config);
        query.connect();

        TS3Api api = query.getApi();
        api.login(username, password);
        api.selectVirtualServerById(1);
        api.setNickname(nickname);
        api.sendChannelMessage(arrivalMessage);

        //Handle incoming commands
        botCommandsHandler botCommandsHandler = new botCommandsHandler(api, query);
        botCommandsHandler.commands();
    }
}