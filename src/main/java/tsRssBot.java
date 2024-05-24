import bot.botCommandsHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

public class tsRssBot {

    public static void main(String[] args) {

        YamlHandler yaml = new YamlHandler();
        yaml.getYaml();

        TS3Config config = new TS3Config();
        config.setHost(yaml.getIpAddress());
        config.setEnableCommunicationsLogging(true);

        //Connect and Login Bot
        TS3Query query = new TS3Query(config);
        query.connect();

        TS3Api api = query.getApi();
        api.login(yaml.getUsername(), yaml.getPassword());
        api.selectVirtualServerById(1);
        api.setNickname(yaml.getNickname());
        api.sendChannelMessage(yaml.getNickname() + " is online!");

        //Handle incoming commands
        botCommandsHandler botCommandsHandler = new botCommandsHandler(api, query);
        botCommandsHandler.commands();
    }
}