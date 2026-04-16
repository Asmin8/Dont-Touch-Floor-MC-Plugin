package com.asmin.myPlugin1;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class MyPlugin1 extends JavaPlugin implements Listener {

    // Stores locations of blocks placed by players (safe blocks)
    private final Set<String> placedBlocks = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("MyPlugin1 enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MyPlugin1 disabled!");
    }

    /**
     * Track blocks placed by players so they are considered safe.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        placedBlocks.add(serializeLocation(block));
    }

    /**
     * Kill player if they touch a naturally generated block.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Avoid unnecessary checks if the player hasn't changed block position
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Block blockBelow = player.getLocation().clone().subtract(0, 1, 0).getBlock();
        Block blockAt = player.getLocation().getBlock();
        Block blockHead = player.getLocation().clone().add(0, 1, 0).getBlock();

        if (isNatural(blockBelow) || isNatural(blockAt) || isNatural(blockHead)) {
            player.setHealth(0.0); // Instantly kills the player
            player.sendMessage("§cYou touched a naturally generated block!");
        }
    }

    /**
     * Determines if a block is naturally generated.
     */
    private boolean isNatural(Block block) {
        if (block.getType() == Material.AIR) {
            return false;
        }
        return !placedBlocks.contains(serializeLocation(block));
    }

    /**
     * Converts a block's location into a unique string.
     */
    private String serializeLocation(Block block) {
        return block.getWorld().getName() + ":" +
                block.getX() + ":" +
                block.getY() + ":" +
                block.getZ();
    }
}