package config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YamlHandler {

    Logger logger = Logger.getLogger(YamlHandler.class.getName());

    private String ipAddress;
    private int virtualServer;
    private String username;
    private String password;
    private int autoUpdate;

    public void readConfig() {
        File file = new File("config.yaml");
        try {
            Yaml yaml = new Yaml();
            InputStream fis = new FileInputStream(file);
            Map<String, Object> configyaml = yaml.load(fis);

            ipAddress = (String) configyaml.get("ipAddress");
            virtualServer = (Integer) configyaml.get("virtualServerId");
            username = (String) configyaml.get("username");
            password = (String) configyaml.get("password");
            autoUpdate = (Integer) configyaml.get("autoUpdate");

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,"Error loading config.yaml in Class YamlHandler " + e.getMessage());
        }

    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getVirtualServer() {
        return virtualServer;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAutoUpdate() {
        return autoUpdate;
    }


    Map<Integer, Object> news = new HashMap<>();

    public Map<Integer, Object> getNews() {
        return news;
    }

    public void addNews (int id, String parameters) {
        news.put(id, parameters);
    }

    public void removeNews (int id) {
        news.remove(id);
    }

    public void writeNews() {
        try {
            FileWriter writer = new FileWriter("news/news.yaml");
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml writeyaml = new Yaml(options);
            writeyaml.dump(news, writer);

        } catch (IOException e) {
            logger.log(Level.SEVERE,"Error writing news.yaml in Class YamlHandler " + e.getMessage());
        }
    }
}
