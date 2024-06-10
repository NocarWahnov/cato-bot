import bot.BotCommandsHandler;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import config.AutoUpdateFeed;
import config.YamlHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RssBot {

    public static void main(String[] args) {

        Logger logger = Logger.getLogger(RssBot.class.getName());

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
        api.selectVirtualServerById(yaml.getVirtualServer());
        api.setNickname("cato");
        api.sendChannelMessage("!cato is online.");
        logger.info("cato is now online on " + yaml.getIpAddress());

        //Auto update
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            logger.info("Starting scheduled task");
            AutoUpdateFeed autoUpdateFeed = new AutoUpdateFeed(api);
            autoUpdateFeed.readNews();
            logger.info("Finished scheduled task");
        };
        executor.scheduleAtFixedRate(task, 1, yaml.getAutoUpdate(), TimeUnit.MINUTES);

        //Listen to Bot commands
        BotCommandsHandler botCommandsHandler = new BotCommandsHandler(api, query, executor);
        botCommandsHandler.commands();
    }
}