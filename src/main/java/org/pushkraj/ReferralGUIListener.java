package org.pushkraj;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ReferralGUIListener implements Listener {
    private final ReferMe plugin;

    public ReferralGUIListener(ReferMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§6Your Referral Status")) {
            event.setCancelled(true);
        }
    }
}