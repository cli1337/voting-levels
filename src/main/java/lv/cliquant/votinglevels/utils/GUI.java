package lv.cliquant.votinglevels.utils;

import com.bencodez.votingplugin.user.VotingPluginUser;
import lv.cliquant.votinglevels.VotingLevels;
import lv.cliquant.votinglevels.objects.Level;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class GUI {

    public static String getUserLevelTitleString(OfflinePlayer offlinePlayer) {
        Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(offlinePlayer.getUniqueId());
        VotingPluginUser voter = VotingLevels.getLevelsManager().getVoter(offlinePlayer);

        if (VotingLevels.getLevelsManager().canRedeemLevel(currentLevel, voter.getPoints())) {
            return Text.placeholdersAPI(offlinePlayer, getLevelTitleString(offlinePlayer, currentLevel, "can").replace("{player_name}", offlinePlayer.getName()));
        } else {
            return Text.placeholdersAPI(offlinePlayer, getLevelTitleString(offlinePlayer, currentLevel, "cant").replace("{player_name}", offlinePlayer.getName()));
        }
    }

    public static String getUserLevelLoreString(OfflinePlayer offlinePlayer) {
        Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(offlinePlayer.getUniqueId());
        VotingPluginUser voter = VotingLevels.getLevelsManager().getVoter(offlinePlayer);

        if (VotingLevels.getLevelsManager().canRedeemLevel(currentLevel, voter.getPoints())) {
            return Text.placeholdersAPI(offlinePlayer, getLevelLoreString(offlinePlayer, currentLevel, "can"));
        } else {
            return Text.placeholdersAPI(offlinePlayer, getLevelLoreString(offlinePlayer, currentLevel, "cant"));
        }
    }

    public static List<String> getUserLevelLoreStringList(OfflinePlayer offlinePlayer) {
        Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(offlinePlayer.getUniqueId());
        VotingPluginUser voter = VotingLevels.getLevelsManager().getVoter(offlinePlayer);

        List<String> loreStrings;
        if (VotingLevels.getLevelsManager().canRedeemLevel(currentLevel, voter.getPoints())) {
            loreStrings = getLevelLoreList(offlinePlayer, currentLevel, "can");
        } else {
            loreStrings = getLevelLoreList(offlinePlayer, currentLevel, "cant");
        }

        List<String> finalLoreStrings = new ArrayList<>();

        for (String lore : loreStrings) {
            String replacedString = lore.replace("{current_level}", VotingLevels.getLevelsManager().getActiveLevel(offlinePlayer.getUniqueId()).getLevel() + "").replace("{player_name}", offlinePlayer.getName());
            if (lore.equalsIgnoreCase("{rewards_text}")) {
                for (String rewardText : getRewardsTextList(offlinePlayer, VotingLevels.getLevelsManager().getActiveLevel(offlinePlayer.getUniqueId()))) {
                    finalLoreStrings.add(Text.placeholdersAPI(offlinePlayer, rewardText));
                }
            } else {
                finalLoreStrings.add(Text.placeholdersAPI(offlinePlayer, replacedString));
            }
        }

        return finalLoreStrings;
    }

    public static String getRewardsTextString(OfflinePlayer offlinePlayer, Level level) {
        return Text.convertListToString(level.getRewardText(), "\n");
    }

    public static List<String> getRewardsTextList(OfflinePlayer offlinePlayer, Level level) {
        return level.getRewardText();
    }

    public static String getLevelTitleString(OfflinePlayer offlinePlayer, Level level, String mode) {
        if (!mode.equalsIgnoreCase("can") && !mode.equalsIgnoreCase("cant")) return null;
        return VotingLevels.get().getConfig().getString("gui.cant.title");
    }


    public static String getLevelLoreString(OfflinePlayer offlinePlayer, Level level, String mode) {
        if (!mode.equalsIgnoreCase("can") && !mode.equalsIgnoreCase("cant")) return null;
        return Text.convertListToString(VotingLevels.get().getConfig().getStringList("gui." + mode + ".lore"), "\n");
    }


    public static List<String> getLevelLoreList(OfflinePlayer offlinePlayer, Level level, String mode) {
        if (!mode.equalsIgnoreCase("can") && !mode.equalsIgnoreCase("cant")) return null;
        return VotingLevels.get().getConfig().getStringList("gui." + mode + ".lore");
    }
}
