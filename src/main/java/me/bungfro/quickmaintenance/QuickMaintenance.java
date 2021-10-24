package me.bungfro.quickmaintenance;

import me.bungfro.quickmaintenance.commands.MaintenanceCommand;
import me.bungfro.quickmaintenance.config.ConfigConfig;
import me.bungfro.quickmaintenance.config.MessagesConfig;
import me.bungfro.quickmaintenance.config.PlayersConfig;
import me.bungfro.quickmaintenance.events.PlayerJoined;
import me.bungfro.quickmaintenance.events.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Callable;

public final class QuickMaintenance extends JavaPlugin {
    String prefix = "[Maintenance]";

    @Override
    public void onEnable() {

        bStats();
        setupConfigs();
        prefix = ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("Prefix")));
        setupUpdateCheck();


        getCommand("maintenance").setExecutor(new MaintenanceCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoined(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void bStats() {
        int pluginId = 13123; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        // Optional: Add custom charts
        metrics.addCustomChart(new SingleLineChart("players", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                // (This is useless as there is already a player chart by default.)
                return Bukkit.getOnlinePlayers().size();
            }
        }));

        metrics.addCustomChart(new SingleLineChart("servers", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                // (This is useless as there is already a player chart by default.)
                return 1;
            }
        }));
    }

    public void setupConfigs() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        MessagesConfig.setup();
        MessagesConfig.get().addDefault("Prefix", "&7[&bMaintenance&7]");
        MessagesConfig.get().addDefault("Motd", "This is a server!");
        MessagesConfig.get().addDefault("commands.maintenance.reload", "&aConfigs Reloaded!");
        MessagesConfig.get().addDefault("commands.maintenance.on", "&aMaintenance mode has turned on!");
        MessagesConfig.get().addDefault("commands.maintenance.off", "&aMaintenance mode has turned off!");
        MessagesConfig.get().addDefault("commands.maintenance.add", "&aPlayer has been added to the whitelist.");
        MessagesConfig.get().addDefault("commands.maintenance.remove", "&aPlayer has been removed from the whitelist.");
        MessagesConfig.get().addDefault("commands.maintenance.kick", "&aMaintenance mode has been turned on for this server.");

        MessagesConfig.get().options().copyDefaults(true);
        MessagesConfig.save();

        PlayersConfig.setup();
        PlayersConfig.save();

        ConfigConfig.setup();
        ConfigConfig.save();

    }

    public void setupUpdateCheck() {
        UpdateChecker.init(this, 97056).requestUpdateCheck().whenComplete((result, exception) -> {
            if (result.requiresUpdate()) {
                this.getLogger().info(String.format("An update is available! QuickMaintenance %s may be downloaded on SpigotMC", result.getNewestVersion()));
                return;
            }

            UpdateChecker.UpdateReason reason = result.getReason();
            if (reason == UpdateChecker.UpdateReason.UP_TO_DATE) {
                this.getLogger().info(String.format("Your version of QuickMaintenance (%s) is up to date!", result.getNewestVersion()));
            } else if (reason == UpdateChecker.UpdateReason.UNRELEASED_VERSION) {
                this.getLogger().info(String.format("Your version of QuickMaintenance (%s) is more recent than the one publicly available. Are you on a development build?", result.getNewestVersion()));
            } else {
                this.getLogger().warning("Could not check for a new version of QuickMaintenance. Reason: " + reason);
            }
        });
    }

}




