package org.pushkraj;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class ReferCommand implements CommandExecutor {
    private final ReferMe plugin;

    public ReferCommand(ReferMe plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("§cUsage: /refer <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cPlayer not found!");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§cYou cannot refer yourself!");
            return true;
        }

        String playerIP = player.getAddress().getAddress().getHostAddress();
        String targetIP = target.getAddress().getAddress().getHostAddress();

        if (playerIP.equals(targetIP)) {
            player.sendMessage("§cYou cannot refer players from the same IP address!");
            return true;
        }

        ReferralData.PlayerReferralData referrerData = plugin.getReferralData().getPlayerData(player.getName());
        
        // Check cooldown
        long cooldownTime = plugin.getConfig().getLong("cooldown") * 1000; // Convert to milliseconds
        long timeLeft = (referrerData.getLastReferralTime() + cooldownTime) - System.currentTimeMillis();
        
        if (timeLeft > 0) {
            String cooldownMsg = plugin.getConfig().getString("messages.cooldown")
                    .replace("{time}", String.format("%d seconds", timeLeft / 1000));
            player.sendMessage(cooldownMsg);
            return true;
        }

        // Check if player was already referred
        if (plugin.getReferralData().hasReferral(player.getName(), target.getName())) {
            player.sendMessage(plugin.getConfig().getString("messages.already-referred"));
            return true;
        }

        // Add referral and save data
        plugin.getReferralData().addReferral(player.getName(), target.getName());
        plugin.executeRewards(player.getName(), target.getName());

        // Send success messages
        player.sendMessage(plugin.getConfig().getString("messages.success")
                .replace("{player}", target.getName()));
        target.sendMessage(plugin.getConfig().getString("messages.received")
                .replace("{referrer}", player.getName()));

        return true;
    }
}