package lv.cliquant.votinglevels.utils;

import lv.cliquant.votinglevels.managers.PlaceholdersAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Text {

    public static String legacy(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String placeholdersAPI(OfflinePlayer player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static String convertListToString(List<String> stringList, String delimiter) {
        if (stringList == null || stringList.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(delimiter);
        for (String s : stringList) {
            joiner.add(s);
        }

        return joiner.toString();
    }


    public static Component colorize(String text) {
        return MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }
}
