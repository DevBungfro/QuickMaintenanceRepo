package me.bungfro.quickmaintenance;

import me.bungfro.quickmaintenance.commands.MaintenanceCommand;
import me.bungfro.quickmaintenance.config.ConfigConfig;
import me.bungfro.quickmaintenance.config.MessagesConfig;
import me.bungfro.quickmaintenance.config.PlayersConfig;
import me.bungfro.quickmaintenance.events.PlayerJoined;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Callable;

public final class QuickMaintenance extends JavaPlugin {

    @Override
    public void onEnable() {

        bStats();
        setupConfigs();


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
        MessagesConfig.get().addDefault("commands.reload", "&aConfigs Reloaded!");
        MessagesConfig.get().options().copyDefaults(true);
        MessagesConfig.save();

        PlayersConfig.setup();
        PlayersConfig.save();

        ConfigConfig.setup();
        ConfigConfig.save();

    }

}




