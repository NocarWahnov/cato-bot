import bot.BotCommandsHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import config.AutoUpdateFeed;
import config.YamlHandler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RssBot {

    public static void main(String[] args) {

        //Read Login Data from config.yaml
        YamlHandler yaml = new YamlHandler();
        yaml.readConfig();

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

        //Handle incoming commands, initialize autoupdate for Feeds
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        BotCommandsHandler botCommandsHandler = new BotCommandsHandler(api, query, executor);
        botCommandsHandler.commands();

        //Auto update
        AutoUpdateFeed autoUpdateFeed = new AutoUpdateFeed(api);
        Runnable task = autoUpdateFeed::readNews;
        int period = 10;
        executor.scheduleAtFixedRate(task, 0, period, TimeUnit.MINUTES);
    }
}