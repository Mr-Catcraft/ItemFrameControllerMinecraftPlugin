package org.mr_catcraft.itemframecontrollerplugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ItemFrameControllerPlugin extends JavaPlugin implements Listener {

    private List<String> restrictedWorlds;
    private boolean preventRotation;
    private boolean preventPlacingItems;
    private boolean preventKnockingByPlayer;
    private boolean preventKnockingByEntity;
    private boolean preventPlacingFrames;
    private String rotationMessage;
    private String placingItemsMessage;
    private String knockingByPlayerMessage;
    private String knockingByEntityMessage;
    private String placingFramesMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfiguration();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void loadConfiguration() {
        FileConfiguration config = getConfig();
        restrictedWorlds = config.getStringList("restricted-worlds");
        preventRotation = config.getBoolean("prevent-rotation");
        preventPlacingItems = config.getBoolean("prevent-placing-items");
        preventKnockingByPlayer = config.getBoolean("prevent-knocking-by-player");
        preventKnockingByEntity = config.getBoolean("prevent-knocking-by-entity");
        preventPlacingFrames = config.getBoolean("prevent-placing-frames");

        rotationMessage = config.getString("messages.rotation");
        placingItemsMessage = config.getString("messages.placing-items");
        knockingByPlayerMessage = config.getString("messages.knocking-by-player");
        knockingByEntityMessage = config.getString("messages.knocking-by-entity");
        placingFramesMessage = config.getString("messages.placing-frames");
    }

    private boolean isRestrictedWorld(World world) {
        return restrictedWorlds.contains(world.getName());
    }

    private boolean isCreative(Entity entity) {
        return entity instanceof Player && ((Player) entity).getGameMode() == GameMode.CREATIVE;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof ItemFrame && !isCreative(event.getPlayer())) {
            World world = entity.getWorld();
            if (isRestrictedWorld(world)) {
                if (preventRotation && event.getPlayer().isSneaking()) {
                    event.setCancelled(true);
                    if (rotationMessage != null) {
                        event.getPlayer().sendMessage(rotationMessage);
                    }
                }
                if (preventPlacingItems && !event.getPlayer().isSneaking()) {
                    event.setCancelled(true);
                    if (placingItemsMessage != null) {
                        event.getPlayer().sendMessage(placingItemsMessage);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Entity entity = event.getRemover();
        if (event.getEntity() instanceof ItemFrame && (entity instanceof Player) && !isCreative(entity)) {
            World world = event.getEntity().getWorld();
            if (isRestrictedWorld(world)) {
                if (entity.getType() == EntityType.PLAYER && preventKnockingByPlayer) {
                    event.setCancelled(true);
                    if (knockingByPlayerMessage != null) {
                        entity.sendMessage(knockingByPlayerMessage);
                    }
                }
                if (entity.getType() != EntityType.PLAYER && preventKnockingByEntity) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame) {
            Entity damager = event.getDamager();
            if (!isCreative(damager)) {
                World world = event.getEntity().getWorld();
                if (isRestrictedWorld(world) && preventKnockingByEntity) {
                    event.setCancelled(true);
                    if (knockingByEntityMessage != null && damager instanceof Player) {
                        damager.sendMessage(knockingByEntityMessage);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (event.getEntity() instanceof ItemFrame) {
            Player player = event.getPlayer();
            if (!isCreative(player)) {
                World world = event.getEntity().getWorld();
                if (isRestrictedWorld(world) && preventPlacingFrames) {
                    event.setCancelled(true);
                    if (placingFramesMessage != null) {
                        assert player != null;
                        player.sendMessage(placingFramesMessage);
                    }
                }
            }
        }
    }
}
