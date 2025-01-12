package lv.cliquant.votinglevels.listeners;

import lv.cliquant.votinglevels.VotingLevels;
import lv.cliquant.votinglevels.objects.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class VotingLevelGUIListeners implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (VotingLevels.levelsGUI.openInventories.contains(event.getWhoClicked().getUniqueId())) {
            String inventoryTitle = event.getView().getOriginalTitle();
            if (inventoryTitle.startsWith(VotingLevels.levelsGUI.BASE_INVENTORY_TITLE)) {
                Player player = (Player) event.getWhoClicked();
                Level currentLevel = VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId());

                if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.TRIDENT) {
                    if (VotingLevels.getLevelsManager().canRedeemLevel(currentLevel, VotingLevels.getLevelsManager().getVoter(player).getPoints())) {
                        VotingLevels.getLevelsManager().redeemLevel(player, currentLevel);
                        Level newLevel = VotingLevels.getLevelsManager().getActiveLevel(player.getUniqueId());
                        player.closeInventory();
                        if (newLevel != null) {
                            VotingLevels.levelsGUI.openInventory(player);
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        VotingLevels.levelsGUI.openInventories.remove(event.getPlayer().getUniqueId());
    }
}
