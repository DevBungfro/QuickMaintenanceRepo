package me.bungfro.quickmaintenance.events;

import me.bungfro.quickmaintenance.config.ConfigConfig;
import me.bungfro.quickmaintenance.config.MessagesConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerJoined implements Listener {
    @EventHandler

    public void PlayerJoinedEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (ConfigConfig.get().getBoolean("maintenance.on") == true) {
            player.kickPlayer("Maintenance Mode has been turned on for this server.");
        }
    }

    @EventHandler

    public void countDown(final ServerListPingEvent event){
        if (ConfigConfig.get().getBoolean("maintenance.on") == true) {
            event.setMotd(ChatColor.YELLOW + "Currently in Maintenance Mode.");
        } else {
            String motd = ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("Motd")));

            event.setMotd(motd);
        }
    }

    }

