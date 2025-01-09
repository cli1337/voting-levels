package lv.cliquant.votinglevels.managers;

import lv.cliquant.votinglevels.VotingLevels;
import lv.cliquant.votinglevels.objects.Level;
import lv.cliquant.votinglevels.utils.GUI;
import lv.cliquant.votinglevels.utils.Text;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaceholdersAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "votinglevels";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", VotingLevels.get().getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return VotingLevels.get().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        String[] parts = params.split("_");
        if (parts.length < 2) {
            return null;
        }

        try {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(parts[0]);

            if (!offlinePlayer.hasPlayedBefore()) {
                return null;
            }

            Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(offlinePlayer.getUniqueId());

            String placeholderType = parts[1];

            switch (placeholderType) {
                case "votes":
                    return VotingLevels.getLevelsManager().getVoter(offlinePlayer).getPoints() + "";
                case "level":
                    return VotingLevels.getLevelsManager().getActiveLevel(offlinePlayer.getUniqueId()).getLevel() + "";
                case "itemtitle":
                    return GUI.getUserLevelTitleString(offlinePlayer);
                case "itemlore":
                    return GUI.getUserLevelLoreString(offlinePlayer);
                case "rewardstext":
                    return GUI.getRewardsTextString(offlinePlayer, currentLevel);
                case "needvotes":
                    return VotingLevels.getLevelsManager().calculateLeftVotes(VotingLevels.getLevelsManager().getActiveLevel(offlinePlayer.getUniqueId()), VotingLevels.getLevelsManager().getVoter(offlinePlayer).getPoints()) + "";
                default:
                    return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
