package me.bungfro.quickmaintenance.events;

import jdk.jpackage.main.Main;
import me.bungfro.quickmaintenance.QuickMaintenance;
import me.bungfro.quickmaintenance.config.ConfigConfig;
import me.bungfro.quickmaintenance.config.MessagesConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class EventsMainListener implements Listener {
    private QuickMaintenance main;
    public EventsMainListener(QuickMaintenance quickMaintenance) {
        this.main = quickMaintenance;
    }

    @EventHandler

    public void PlayerJoinedEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (ConfigConfig.get().getBoolean("maintenance.on") == true) {
            player.kickPlayer("Maintenance Mode has been turned on for this server.");
        }
    }

    @EventHandler

    public void maintenanceMotd(final ServerListPingEvent event){
        if (ConfigConfig.get().getBoolean("maintenance.on") == true) {
            event.setMotd(ChatColor.YELLOW + "Currently in Maintenance Mode.");
        } else {
            String motd = ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("Motd")));

            event.setMotd(motd);
        }
    }
    @EventHandler

    public void guiClicked(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Edit Menu")) {
            e.setCancelled(true);

            switch (e.getCurrentItem().getType()) {
                case PLAYER_HEAD:
                    e.getWhoClicked().closeInventory();
                    main.openGui((Player) e.getWhoClicked(), "Players", "Nothing");
                    break;
                case GREEN_WOOL:
                    e.getWhoClicked().closeInventory();
                    main.openGui((Player) e.getWhoClicked(), "Settings", "Nothing");
                    break;
            }
        }

        if (e.getView().getTitle().equals("Players")) {
            e.setCancelled(true);


            switch (e.getCurrentItem().getType()) {
                case PLAYER_HEAD:

                    e.getWhoClicked().closeInventory();
                    main.openGui((Player) e.getWhoClicked(), "PlayerSettings", e.getCurrentItem().getItemMeta().getDisplayName());
                    break;
            }
        }




        if (e.getView().getTitle().equals("Settings")) {
            e.setCancelled(true);

            switch (e.getCurrentItem().getType()) {
                case GREEN_WOOL:

                    Bukkit.getServer().dispatchCommand(e.getWhoClicked(), "maintenance on");
                    break;
                case RED_WOOL:

                    Bukkit.getServer().dispatchCommand(e.getWhoClicked(), "maintenance off");
                    break;
            }
        }

            if (e.getView().getTitle().equals("Player Settings")) {
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()) {
                    case GREEN_WOOL:

                        Bukkit.getServer().dispatchCommand(e.getWhoClicked(), "maintenance add " + e.getClickedInventory().getItem(2).getItemMeta().getDisplayName());
                        break;
                    case RED_WOOL:

                         Bukkit.getServer().dispatchCommand(e.getWhoClicked(), "maintenance remove " + e.getClickedInventory().getItem(2).getItemMeta().getDisplayName());
                         break;
        }
    }



}

}

