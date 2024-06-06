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

    public void getYaml () {
        File file = new File("src/main/resources/config.yaml");
        try {
            Yaml yaml = new Yaml();
            InputStream fis = new FileInputStream(file);
            Map<String, Object> configyaml = yaml.load(fis);

            ipAddress = (String) configyaml.get("ipAddress");
            username = (String) configyaml.get("username");
            password = (String) configyaml.get("password");
            nickname = (String) configyaml.get("nickname");

        } catch (FileNotFoundException e) {
            System.err.println("Error loading config.yaml " + e.getMessage());
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

        try {
            FileWriter writer = new FileWriter("src/main/resources/news.yaml");
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(news, writer);

        } catch (IOException e) {
            System.err.println("Error writing news.yaml " + e.getMessage());
        }
    }
}
