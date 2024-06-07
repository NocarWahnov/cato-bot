package config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class YamlHandler {

    private String ipAddress;
    private String username;
    private String password;
    private String nickname;

    public void readConfig() {
        File file = new File("config.yaml");
        try {
            Yaml yaml = new Yaml();
            InputStream fis = new FileInputStream(file);
            Map<String, Object> configyaml = yaml.load(fis);

            ipAddress = (String) configyaml.get("ipAddress");
            username = (String) configyaml.get("username");
            password = (String) configyaml.get("password");
            nickname = (String) configyaml.get("nickname");

        } catch (FileNotFoundException e) {
            System.err.println("Error loading config.yaml in Class YamlHandler " + e.getMessage());
        }

    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    Map<Integer, Object> news = new HashMap<>();

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
            System.err.println("Error writing news.yaml in Class YamlHandler " + e.getMessage());
        }
    }
}
