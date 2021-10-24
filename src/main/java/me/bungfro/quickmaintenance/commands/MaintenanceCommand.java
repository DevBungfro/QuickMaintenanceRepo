package me.bungfro.quickmaintenance.commands;

import jdk.jpackage.main.Main;
import me.bungfro.quickmaintenance.QuickMaintenance;
import me.bungfro.quickmaintenance.config.ConfigConfig;
import me.bungfro.quickmaintenance.config.MessagesConfig;
import me.bungfro.quickmaintenance.config.PlayersConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceCommand implements CommandExecutor {
    private QuickMaintenance main;

    public MaintenanceCommand(QuickMaintenance quickMaintenance) {
        this.main = quickMaintenance;
    }


    String prefix = ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("Prefix")));
    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {

        if (args.length == 0) {
            sender.sendMessage("Invalid Arguments");
            return true;

        }

        if (!sender.hasPermission("maintenance.admin")) {
            return true;
        }

        switch (args[0].toLowerCase()) {
            default:
                sender.sendMessage(ChatColor.RED + "Unknown Arg(s)");
                break;
            case "reload":

                MessagesConfig.reload();
                String reloadmessage = ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("commands.maintenance.reload")));

                sender.sendMessage(prefix + reloadmessage);

                break;
            case "add":
                if (args.length > 1) {
                    Player player = Bukkit.getPlayer(args[1]);

                    if (!(player == null)) {
                        if (!PlayersConfig.get().contains("player." + player.getName())) {
                            PlayersConfig.get().set("player." + player.getName(), true);
                            PlayersConfig.save();
                            sender.sendMessage(prefix + translateCodes(MessagesConfig.get().getString("commands.maintenance.add")));
                        } else {
                            sender.sendMessage(prefix + "Player is already whitelisted.");
                        }
                    }
                } else {
                    sender.sendMessage(prefix + "Please enter a player to add to the maintenance whitelist.");
                }
                break;
            case "remove":
                if (args.length > 1) {
                    Player player = Bukkit.getPlayer(args[1]);

                    if (!(player == null)) {
                        if (PlayersConfig.get().contains("player." + player.getName())) {
                            PlayersConfig.get().set("player." + player.getName(), null);
                            PlayersConfig.save();
                            sender.sendMessage(prefix + translateCodes(MessagesConfig.get().getString("commands.maintenance.remove")));
                        } else {
                            sender.sendMessage(prefix + "Player is not whitelisted.");
                        }
                    }
                } else {
                    sender.sendMessage(prefix + "Please enter a player to add to the maintenance whitelist.");
                }
                break;

            case "on":
                ConfigConfig.get().set("maintenance.on", true);
                kickPlayers();
                sender.sendMessage(prefix + translateCodes(MessagesConfig.get().getString("commands.maintenance.on")));
                ConfigConfig.save();
                break;
            case "off":
                ConfigConfig.get().set("maintenance.on", false);
                sender.sendMessage(prefix + translateCodes(MessagesConfig.get().getString("commands.maintenance.off")));
                ConfigConfig.save();
                break;
            case "list":

                sender.sendMessage(prefix + "------------------------------");


                for(String string : PlayersConfig.get().getConfigurationSection("player").getKeys(false)){
                    sender.sendMessage(prefix + string);

                }

                sender.sendMessage(prefix + "------------------------------");

                break;
            case "gui":


                main.openGui((Player) sender, "Main", "Nothing");
                break;
            case "help" :

                sender.sendMessage(prefix + "------------------------------");

                sender.sendMessage(prefix + "/maintenance help - Bring up this help menu");
                sender.sendMessage(prefix + "/maintenance on - Turn on Maintenance Mode");
                sender.sendMessage(prefix + "/maintenance off - Turn off Maintenance Mode");
                sender.sendMessage(prefix + "/maintenance add - Add a player to Whitelist");
                sender.sendMessage(prefix + "/maintenance remove - Remove a player from Whitelist ");
                sender.sendMessage(prefix + "/maintenance list - List all Whitelisted players");
                sender.sendMessage(prefix + "/maintenance gui - Open gui version");
                sender.sendMessage(prefix + "/maintenance reload - Reload All Configs");



                sender.sendMessage(prefix + "------------------------------");
        }

        return true;
    }

    public void kickPlayers() {
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!PlayersConfig.get().contains("player." + player.getName())) {
                player.kickPlayer(translateCodes(MessagesConfig.get().getString("commands.maintenance.kick")));
            }
        }
    }

    public String translateCodes(String code) {
        return " " + ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', code));
    }

}
