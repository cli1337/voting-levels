package lv.cliquant.votinglevels.gui;

import lv.cliquant.votinglevels.VotingLevels;
import lv.cliquant.votinglevels.objects.Level;
import lv.cliquant.votinglevels.utils.GUI;
import lv.cliquant.votinglevels.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LevelsGUI {
    public static List<UUID> openInventories = new ArrayList<>();
    public static String BASE_INVENTORY_TITLE = "Voting Levels";

    public LevelsGUI() {}

    public void openInventory(Player player) {
        UUID inventoryId = UUID.randomUUID();
        String inventoryTitle = BASE_INVENTORY_TITLE;

        org.bukkit.inventory.Inventory inventory = Bukkit.createInventory(null, 27, inventoryTitle);
        openInventories.add(player.getUniqueId());

        fillGui(inventory);
        setupLevelItem(inventory, player);

        player.openInventory(inventory);
    }

    private void setupLevelItem(Inventory inventory, Player player) {
        Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId());
        ItemStack itemStack = new ItemStack(Material.TRIAL_KEY, 1);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.displayName(Text.colorize(GUI.getUserLevelTitleString(player)));
            List<String> LORE = GUI.getUserLevelLoreStringList(player);

            meta.lore(LORE.stream()
                    .map(Text::colorize)
                    .collect(Collectors.toList()));

            if (VotingLevels.getLevelsManager().canRedeemLevel(currentLevel, VotingLevels.getLevelsManager().getVoter(player).getPoints())) {
                meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, false);
            }

            for(ItemFlag flag : ItemFlag.values()) {
                meta.addItemFlags(flag);
            }

            itemStack.setItemMeta(meta);
        }

        inventory.setItem(13, itemStack);
    }

    private void fillGui(org.bukkit.inventory.Inventory inventory) {
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, filler);
        }
    }

    public static boolean isInventoryOpen(Player player) {
        return openInventories.contains(player.getUniqueId());
    }
}
