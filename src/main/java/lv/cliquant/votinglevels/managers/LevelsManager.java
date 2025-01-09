package lv.cliquant.votinglevels.managers;

import com.bencodez.votingplugin.VotingPluginMain;
import com.bencodez.votingplugin.user.VotingPluginUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lv.cliquant.votinglevels.VotingLevels;
import lv.cliquant.votinglevels.objects.Level;
import lv.cliquant.votinglevels.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class LevelsManager {
    private static final Map<Integer, Level> levels = new HashMap<>();
    private final Map<UUID, List<Integer>> playerRedeemedLevels = new HashMap<>();
    private final Gson gson = new Gson();
    private final File databaseFolder;

    public LevelsManager() {
        databaseFolder = new File(VotingLevels.get().getDataFolder(), "database");
        if (!databaseFolder.exists()) {
            databaseFolder.mkdirs();
        }
    }

    public void loadLevels() {
        ConfigurationSection levelsPath = VotingLevels.get().getConfig().getConfigurationSection("levels");

        if (levelsPath == null) {
            VotingLevels.get().getLogger().info("No levels defined in the configuration.");
            return;
        }

        for (String levelKey : levelsPath.getKeys(false)) {
            try {
                int levelNumber = Integer.parseInt(levelKey);
                ConfigurationSection levelSection = levelsPath.getConfigurationSection(levelKey);

                if (levelSection != null) {
                    int requiredVotes = levelSection.getInt("req");
                    List<String> rewardText = levelSection.getStringList("reward-text");
                    List<String> commands = levelSection.getStringList("commands");

                    Level level = new Level(levelNumber, requiredVotes, rewardText, commands);
                    VotingLevels.getLevelsManager().addLevel(levelNumber, level);
                }
            } catch (NumberFormatException e) {
                VotingLevels.get().getLogger().info("Invalid level key: " + levelKey);
            } catch (Exception e) {
                VotingLevels.get().getLogger().info("Error loading level " + levelKey + ": " + e.getMessage());
            }
        }

        VotingLevels.get().getLogger().info("Loaded " + levels.size() + " levels.");
    }

    public void addLevel(int levelNumber, Level level) {
        levels.put(levelNumber, level);
    }

    public Level getLevel(int levelNumber) {
        return levels.get(levelNumber);
    }

    public Map<Integer, Level> getLevels() {
        return levels;
    }

    public boolean canRedeemLevel(Level level, int voteCount) {
        return voteCount >= level.getRequiredVotes();
    }

    public int calculateLeftVotes(Level level, int voteCount) {
        if ((level.getRequiredVotes() - voteCount) < 0) {
            return 0;
        } else {
            return level.getRequiredVotes() - voteCount;
        }
    }

    public VotingPluginUser getVoter(OfflinePlayer player) {
        VotingPluginUser voter = VotingPluginMain.getPlugin().getVotingPluginUserManager().getVotingPluginUser(player);
        return voter;
    }

    public Level getActiveLevel(UUID playerUUID) {
        List<Integer> redeemedLevels = playerRedeemedLevels.getOrDefault(playerUUID, new ArrayList<>());
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
        VotingPluginUser voter = getVoter(offlinePlayer);

        Level nextLevel = null;

        for (Map.Entry<Integer, Level> entry : new TreeMap<>(levels).entrySet()) {
            Level level = entry.getValue();

            if (!redeemedLevels.contains(level.getLevel())) {
                nextLevel = level;
                break;
            }
        }

        return nextLevel;
    }
    public void updateUser(OfflinePlayer player) {
        List<Integer> redeemedLevels = playerRedeemedLevels.getOrDefault(player.getUniqueId(), new ArrayList<>());
        Level currentLevel = getActiveLevel(player.getUniqueId());

        playerRedeemedLevels.put(player.getUniqueId(), redeemedLevels);
    }

    public void redeemLevel(OfflinePlayer player, Level level) {
        List<Integer> redeemedLevels = playerRedeemedLevels.getOrDefault(player.getUniqueId(), new ArrayList<>());
        redeemedLevels.add(level.getLevel());
        playerRedeemedLevels.put(player.getUniqueId(), redeemedLevels);
        grantRewards(player, level);
    }

    private void grantRewards(OfflinePlayer offlinePlayer, Level level) {
        for (String command : level.getCommands()) {
            String formattedCommand = command.replace("{player_name}", offlinePlayer.getName()).replace("{player_uuid}", offlinePlayer.getUniqueId() + "").replace("{level}", level.getLevel() + "").replace("{needvotes}", level.getRequiredVotes() +"");
            VotingLevels.get().getServer().dispatchCommand(VotingLevels.get().getServer().getConsoleSender(), Text.placeholdersAPI(offlinePlayer, formattedCommand));
        }
    }

    public void loadDatabaseData() {
        for (File file : Objects.requireNonNull(databaseFolder.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                String uuidString = file.getName().replace(".json", "");
                try {
                    UUID playerUUID = UUID.fromString(uuidString);
                    try (FileReader reader = new FileReader(file)) {
                        Type listType = new TypeToken<List<Integer>>() {}.getType();
                        List<Integer> redeemedLevels = gson.fromJson(reader, listType);
                        playerRedeemedLevels.put(playerUUID, redeemedLevels);
                    }
                } catch (Exception e) {
                    VotingLevels.get().getLogger().severe("Failed to load data for player " + uuidString + ": " + e.getMessage());
                }
            }
        }
    }

    public void saveDatabaseData() {
        for (Map.Entry<UUID, List<Integer>> entry : playerRedeemedLevels.entrySet()) {
            UUID playerUUID = entry.getKey();
            List<Integer> redeemedLevels = entry.getValue();

            File playerFile = new File(databaseFolder, playerUUID.toString() + ".json");
            try (FileWriter writer = new FileWriter(playerFile)) {
                gson.toJson(redeemedLevels, writer);
            } catch (IOException e) {
                VotingLevels.get().getLogger().severe("Failed to save data for player " + playerUUID + ": " + e.getMessage());
            }
        }
    }
}
