import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YamlHandler {

    Yaml yaml = new Yaml();
    File file = new File("src/main/resources/config.yaml");

    private String ipAddress;
    private String username;
    private String password;
    private String nickname;

    public void getYaml () {
        //Yaml yaml = new Yaml();
        //File file = new File("src/main/resources/config.yaml");
        try {
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

    public void addNews (String id, String parameters) {
        Map<String, Object> news = new HashMap<>();
        news.put(id, parameters);
        yaml.dump(news);
    }

    public void testDump() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "Silenthand Olleander");
        data.put("race", "Human");
        data.put("traits", new String[] { "ONE_HAND", "ONE_EYE" });
        Yaml yaml = new Yaml();
        String output = yaml.dump(data);
        System.out.println(output);
    }
}
