package org.pushkraj;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class ReferralGUI {
    private final ReferMe plugin;
    private static final int INVENTORY_SIZE = 27;

    public ReferralGUI(ReferMe plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, INVENTORY_SIZE, "§6Your Referral Status");

        ReferralData.PlayerReferralData playerData = plugin.getReferralData().getPlayerData(player.getName());
        int referralCount = playerData.getReferralCount();

        // Info item
        ItemStack infoItem = createItem(Material.BOOK,
                "§e§lReferral Information",
                "§7Total Referrals: §6" + referralCount,
                "§7Next Milestone: §6" + getNextMilestone(referralCount));
        gui.setItem(4, infoItem);

        // Recent referrals
        int slot = 10;
        for (String referred : playerData.getReferredPlayers().keySet()) {
            if (slot > 16) break;
            ItemStack playerHead = createItem(Material.PLAYER_HEAD,
                    "§a" + referred,
                    "§7Referred on: §f" + new java.util.Date(playerData.getReferredPlayers().get(referred)));
            gui.setItem(slot++, playerHead);
        }

        // Milestone progress
        ItemStack milestone = createItem(Material.EXPERIENCE_BOTTLE,
                "§6§lMilestone Progress",
                "§7Current: §6" + referralCount + " referrals",
                "§7Next reward at: §6" + getNextMilestone(referralCount) + " referrals",
                "§7Progress: §6" + getProgressBar(referralCount));
        gui.setItem(22, milestone);

        player.openInventory(gui);
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        
        List<String> loreList = new ArrayList<>();
        for (String line : lore) {
            loreList.add(line);
        }
        meta.setLore(loreList);
        
        item.setItemMeta(meta);
        return item;
    }

    private int getNextMilestone(int currentReferrals) {
        int[] milestones = {5, 10, 15, 20, 25, 30, 40, 50};
        for (int milestone : milestones) {
            if (milestone > currentReferrals) {
                return milestone;
            }
        }
        return milestones[milestones.length - 1];
    }

    private String getProgressBar(int referrals) {
        int nextMilestone = getNextMilestone(referrals);
        int prevMilestone = getPreviousMilestone(referrals);
        int total = nextMilestone - prevMilestone;
        int current = referrals - prevMilestone;
        int progressBarLength = 20;
        
        int filledBars = (int) ((double) current / total * progressBarLength);
        StringBuilder bar = new StringBuilder();
        
        bar.append("§a");
        for (int i = 0; i < filledBars; i++) {
            bar.append("|");
        }
        bar.append("§7");
        for (int i = filledBars; i < progressBarLength; i++) {
            bar.append("|");
        }
        
        return bar.toString();
    }

    private int getPreviousMilestone(int currentReferrals) {
        int[] milestones = {0, 5, 10, 15, 20, 25, 30, 40, 50};
        for (int i = milestones.length - 1; i >= 0; i--) {
            if (milestones[i] <= currentReferrals) {
                return milestones[i];
            }
        }
        return 0;
    }
}