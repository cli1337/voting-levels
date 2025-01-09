package lv.cliquant.votinglevels.commands;

import com.bencodez.votingplugin.VotingPluginMain;
import com.bencodez.votingplugin.user.VotingPluginUser;
import lv.cliquant.votinglevels.VotingLevels;
import lv.cliquant.votinglevels.gui.LevelsGUI;
import lv.cliquant.votinglevels.objects.Level;
import lv.cliquant.votinglevels.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("votinglevels")
public class MainCommand {

    @CommandPermission("votinglevels.use")
    public void use(Player player) {
        if (VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId()) == null) {
            player.sendMessage(Text.colorize(VotingLevels.get().getConfig().getString("messages.finished-levels")));
            return;
        }
        VotingLevels.levelsGUI.openInventory(player);
    }

    @Subcommand("update")
    @CommandPermission("votinglevels.update")
    public void update(CommandSender sender, String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

        if (!offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline()) {
            sender.sendMessage(Text.colorize(VotingLevels.get().getConfig().getString("messages.player-notfound")));
            return;
        }

        VotingPluginUser voter = VotingPluginMain.getPlugin().getVotingPluginUserManager().getVotingPluginUser(offlinePlayer);
        VotingLevels.getLevelsManager().updateUser(offlinePlayer);
    }

    @Subcommand("reload")
    @CommandPermission("votinglevels.reload")
    public void reload(CommandSender sender) {
        VotingLevels.levelsGUI.openInventories.forEach((uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.closeInventory();
            }
        }));

        VotingLevels.levelsGUI = new LevelsGUI();

        VotingLevels.getLevelsManager().saveDatabaseData();
        VotingLevels.get().reloadConfig();
        sender.sendMessage("Reloaded VotingLevels");
    }
}
