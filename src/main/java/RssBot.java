import bot.BotCommandsHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import config.YamlHandler;

public class RssBot {

    public static void main(String[] args) {

        //Read Login Data from config.yaml
        YamlHandler yaml = new YamlHandler();
        yaml.getYaml();

        //Connect and Login Bot
        TS3Config config = new TS3Config();
        config.setHost(yaml.getIpAddress());
        config.setEnableCommunicationsLogging(true);

        TS3Query query = new TS3Query(config);
        query.connect();

        TS3Api api = query.getApi();
        api.login(yaml.getUsername(), yaml.getPassword());
        api.selectVirtualServerById(1);
        api.setNickname(yaml.getNickname());
        api.sendChannelMessage(yaml.getNickname() + " is online!");

        //Handle incoming commands
        BotCommandsHandler botCommandsHandler = new BotCommandsHandler(api, query);
        botCommandsHandler.commands();
    }
}