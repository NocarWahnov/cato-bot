package config;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BackupHandler {

    private final YamlHandler yamlHandler;

    public BackupHandler(YamlHandler yaml) {
        this.yamlHandler = yaml;
    }

    Logger logger = Logger.getLogger(BackupHandler.class.getName());

    File backupFolder = new File("news/backup/");
    File[] getBackupFiles = backupFolder.listFiles();

    public String makeBackup (String time) {
        try {
            Path copyFrom = Paths.get("news/news.yaml");
            Path copyTo = Paths.get("news/backup/news-" + time + ".yaml");

            Files.copy(copyFrom, copyTo);
            //new FileWriter("news/news.yaml", false).close();

        } catch (IOException e) {
            return "Error backing up news.yaml in Class BackupHandler!";
        }

        return "Successfully backed up news.yaml as news-" + time + ".yaml";
    }

    public String listFiles() {
        String message = "\n";

        for (File file : getBackupFiles) {
            message = message + file.getName() + "\n";
        }

        return message;
    }

    public Map<Integer, String> getBackupFile (String filename) {
        File file = new File("news/backup/" + filename);
        try {
            Yaml yaml = new Yaml();
            InputStream fis = new FileInputStream(file);
            Map<Integer, String> backupyaml = yaml.load(fis);

            return backupyaml;
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE,"Error loading backup file " + e.getMessage());
        }
        return null;
    }

    public String loadBackup (String filename) {

        for (Map.Entry<Integer, String> entry : getBackupFile(filename).entrySet()) {
            yamlHandler.addNews(entry.getKey(), entry.getValue());
        }
        yamlHandler.writeNews();

        return "Successfully loaded " + filename;
    }

    public String printBackup (String filename) {
        String content = "\n";

        for (Map.Entry<Integer, String> entry : getBackupFile(filename).entrySet()) {
            content = content + entry.getKey() + ": " + entry.getValue() + "\n";
        }

        return content;
    }

}
