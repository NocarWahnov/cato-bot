package config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BackupHandler {

    Logger logger = Logger.getLogger(BackupHandler.class.getName());

    File backupFolder = new File("news/backup/");
    File[] getBackupFiles = backupFolder.listFiles();

    public String makeBackup (String time) {
        try {
            Path copyFrom = Paths.get("news/news.yaml");
            Path copyTo = Paths.get("news/backup/news-" + time + ".yaml");

            Files.copy(copyFrom, copyTo);
            new FileWriter("news/news.yaml", false).close();

        } catch (IOException e) {
            return "Error backing up news.yaml in Class BotCommandsHandler!";
        }

        return "Successfully backed up news.yaml as news-" + time + ".yaml";
    }

    public String listFiles() {
        String message = "\n";

        for (File file : getBackupFiles) {
            message = message + file.getName() + "\n";
        }

        //System.out.println(message);
        return message;
    }

    public String loadBackup (String filename) {
        try {
            Path copyFrom = Paths.get("news/backup/" + filename);
            Path copyTo = Paths.get("news/news.yaml");

            Files.copy(copyFrom, copyTo, StandardCopyOption.REPLACE_EXISTING);
            //new FileWriter("news/backup/" + filename, false).close();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading Backup", e);
            return "Error overwriting " + filename + " into news.yaml";
        }

        return "Successfully loaded " + filename;
    }

}
