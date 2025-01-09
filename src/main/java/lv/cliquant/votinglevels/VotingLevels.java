package lv.cliquant.votinglevels;

import fr.minuskube.inv.InventoryManager;
import lv.cliquant.votinglevels.commands.MainCommand;
import lv.cliquant.votinglevels.gui.LevelsGUI;
import lv.cliquant.votinglevels.listeners.VotingLevelGUIListeners;
import lv.cliquant.votinglevels.managers.LevelsManager;
import lv.cliquant.votinglevels.managers.PlaceholdersAPI;
import lv.cliquant.votinglevels.objects.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public final class VotingLevels extends JavaPlugin {

    private static VotingLevels plugin;
    private FileConfiguration mainConfig;
    private static LevelsManager levelsManager;

    public static LevelsGUI levelsGUI;

    private static InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        plugin = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        saveDefaultConfig();
        mainConfig = getConfig();

        levelsManager = new LevelsManager();

        levelsManager.loadLevels();
        levelsManager.loadDatabaseData();

        levelsGUI = new LevelsGUI();

        registerPlaceholderAPI();

        getServer().getPluginManager().registerEvents(new VotingLevelGUIListeners(), this);

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this).build();
        lamp.register(new MainCommand());
    }

    @Override
    public void onDisable() {
        getLevelsManager().saveDatabaseData();
    }

    private void registerPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholdersAPI().register();
        } else {
            getLogger().severe("PlaceholderAPI not found! This plugin requires PlaceholderAPI to function.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public static LevelsManager getLevelsManager() {
        return levelsManager;
    }

    public static VotingLevels get() {
        return plugin;
    }
}
