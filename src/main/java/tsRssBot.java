import bot.LoginBot;
import bot.botCommandsHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

public class tsRssBot {

    public static void main(String[] args) {

        String ipAddress = "nocars.tk";
        TS3Config config = new TS3Config();
        config.setHost(ipAddress);
        config.setEnableCommunicationsLogging(true);

        TS3Query query = new TS3Query(config);
        query.connect();

        TS3Api api = query.getApi();
        LoginBot bot = new LoginBot(api);
        bot.connect();

        //Handle incoming commands
        botCommandsHandler botCommandsHandler = new botCommandsHandler(api, query);
        botCommandsHandler.commands();
    }
}