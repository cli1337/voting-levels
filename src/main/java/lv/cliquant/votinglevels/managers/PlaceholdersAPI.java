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
        if (parts.length == 1) {
            try {
                if (player == null) {
                    return null;
                }

                Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId());
                if (currentLevel == null) {
                    return null;
                }

                String placeholderType = parts[0];

                switch (placeholderType) {
                    case "votes":
                        return VotingLevels.getLevelsManager().getVoter(player).getPoints() + "";
                    case "level":
                        return VotingLevels.getLevelsManager().getCurrentLevelNumber(player.getUniqueId()) + "";
                    case "activelevel":
                        return VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId()).getLevel() + "";
                    case "itemtitle":
                        return GUI.getUserLevelTitleString(player);
                    case "itemlore":
                        return GUI.getUserLevelLoreString(player);
                    case "rewardstext":
                        return GUI.getRewardsTextString(player, currentLevel);
                    case "needvotes":
                        return VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId()).getRequiredVotes() + "";
                    default:
                        return null;
                }
            } catch (IllegalArgumentException e) {
                return null;
            }
        } else if (parts.length >= 2) {
            try {
                OfflinePlayer player1;
                Player player_ = Bukkit.getPlayerExact(parts[0]);
                if (player_ != null) {
                    player1 = player_;
                } else {
                    player1 = Bukkit.getOfflinePlayer(parts[0]);
                }

                Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(player1.getUniqueId());
                if (currentLevel == null) {
                    return null;
                }
                String placeholderType = parts[1];

                switch (placeholderType) {
                    case "votes":
                        return VotingLevels.getLevelsManager().getVoter(player1).getPoints() + "";
                    case "level":
                        return VotingLevels.getLevelsManager().getCurrentLevelNumber(player1.getUniqueId()) + "";
                    case "activelevel":
                        return VotingLevels.getLevelsManager().getActiveLevel(player1.getUniqueId()).getLevel() + "";
                    case "itemtitle":
                        return GUI.getUserLevelTitleString(player1);
                    case "itemlore":
                        return GUI.getUserLevelLoreString(player1);
                    case "rewardstext":
                        return GUI.getRewardsTextString(player1, currentLevel);
                    case "needvotes":
                        return VotingLevels.getLevelsManager().getActiveLevel(player1.getUniqueId()).getRequiredVotes() + "";
                    default:
                        return null;
                }
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }
}
