package org.pushkraj;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReferInfoCommand implements CommandExecutor {
    private final ReferMe plugin;
    private final ReferralGUI gui;

    public ReferInfoCommand(ReferMe plugin) {
        this.plugin = plugin;
        this.gui = new ReferralGUI(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        gui.openGUI(player);
        return true;
    }
}