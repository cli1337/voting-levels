package lv.cliquant.votinglevels.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lv.cliquant.votinglevels.VotingLevels;
import lv.cliquant.votinglevels.objects.Level;
import lv.cliquant.votinglevels.utils.GUI;
import lv.cliquant.votinglevels.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.nio.Buffer;
import java.util.List;
import java.util.stream.Collectors;

public class LevelsGUI implements InventoryProvider {
    public LevelsGUI() {}

    public static SmartInventory getInventory(Player player) {
        return SmartInventory.builder()
                .provider(new LevelsGUI())
                .size(3, 9)
                .id("votingLevelsGUI")
                .manager(VotingLevels.getInventoryManager())
                .title("Voting Levels")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        fillGui(contents);
        setupLevelItem(contents, player);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {}

    private void setupLevelItem(InventoryContents contents, Player player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
        Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId());
        ItemStack itemStack = new ItemStack(Material.TRIDENT, 1);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.displayName(Text.colorize(GUI.getUserLevelTitleString(offlinePlayer)));
            List<String> LORE = GUI.getUserLevelLoreStringList(offlinePlayer);

            meta.lore(LORE.stream()
                    .map(Text::colorize)
                    .collect(Collectors.toList()));
            if (VotingLevels.getLevelsManager().canRedeemLevel(currentLevel, VotingLevels.getLevelsManager().getVoter(offlinePlayer).getPoints())) {
                meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            }
            itemStack.setItemMeta(meta);
        }

        if (VotingLevels.getLevelsManager().canRedeemLevel(currentLevel, VotingLevels.getLevelsManager().getVoter(offlinePlayer).getPoints())) {
            contents.set(1, 4, ClickableItem.of(itemStack, e -> {
                VotingLevels.getLevelsManager().redeemLevel(offlinePlayer, currentLevel);
                Level level = VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId());
                if (level == null) {
                    player.closeInventory();
                }
            }));
        } else {
            contents.set(1, 4, ClickableItem.empty(itemStack));
        }
    }

    private void fillGui(InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1)));
    }
}
