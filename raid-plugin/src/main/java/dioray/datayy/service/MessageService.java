package dioray.datayy.service;

import dioray.datayy.RaidPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;

public class MessageService extends Service {

    private final Map<String, String> messageMap;

    public MessageService(RaidPlugin main) {
        super(main);

        this.messageMap = new HashMap<>();
    }

    @Override
    public void enable() {
        File messagesFile = new File(this.main.getDataFolder(), "messages.properties");
        if (!messagesFile.exists()) {
            this.main.saveResource("messages.properties", false);
        }

        try (FileReader fileReader = new FileReader(messagesFile)) {
            Properties messageProperties = new Properties();
            messageProperties.load(fileReader);

            messageProperties.forEach((key, value) -> {
                String translated = ChatColor.translateAlternateColorCodes('&', (String) value);

                this.messageMap.put((String) key, translated);
            });
        } catch (Exception ex) {
            this.main.getLogger().log(Level.SEVERE, "Something got wrong when loading messages.properties", ex);
        }
    }

    public String get(String key, Object... args) {
        String message = this.messageMap.get(key);
        Objects.requireNonNull(message, key + " message was not found");

        if (args.length % 2 > 0) {
            throw new IllegalArgumentException("args must be even");
        }

        for (int i = 0; i < args.length; i += 2) {
            String argKey = (String) args[i];
            String argValue = args[i + 1].toString();

            message = message.replace("{" + argKey + "}", argValue);
        }

        return message;
    }

    public void sendMessage(CommandSender player, String key, Object... args) {
        String message = this.get(key, args);

        player.sendMessage(message);
    }

    public void reload() {
        this.messageMap.clear();
        this.enable();
    }
}
