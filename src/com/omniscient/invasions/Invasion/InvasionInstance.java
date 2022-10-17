package com.omniscient.invasions.Invasion;

import com.omniscient.invasions.Invasions;
import com.omniscient.omnicore.Utils.Methods;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class InvasionInstance {
    private final Invasion invasion;
    private final List<Entity> entities = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private int openedFor = Invasions.config.getDuration();
    public InvasionInstance(Invasion invasion){
        this.invasion = invasion;
        Invasions.invasionInstance = this;
        invasion.getMobs().forEach(mob -> mob.getLocations().forEach(location -> entities.add(mob.summon(location).getBukkitEntity())));
        if(invasion.getOpenCommand().length() > 0) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), invasion.getOpenCommand());
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Methods.color(" ")));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Methods.color("  &eThe invasion "+invasion.getName()+" &ehas started!.")));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Methods.color("  &eThe entry is available at &7/invasions&e for &7"+Methods.getDuration(openedFor)+"&e.")));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Methods.color(" ")));
        new BukkitRunnable() {
            @Override
            public void run() {
                players.forEach(p -> {
                    PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(Methods.color("&eTime Left: "+Methods.formatTimerString(openedFor))), (byte)2);
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                });
                openedFor--;
                if(Invasions.invasionInstance != null && openedFor < 0){
                    cancel();
                    close(60);
                }
            }
        }.runTaskTimer(Invasions.plugin, 0, 20);
    }

    public Invasion getInvasion() {
        return invasion;
    }
    public List<Entity> getEntities() {
        return entities;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public int getOpenedFor() {
        return openedFor;
    }

    public void spawnPlayer(Player player, BlockFace blockFace){
        player.teleport(invasion.getLocations().get(blockFace));
        players.add(player);
    }

    public void close(int time){
        Invasions.invasionInstance = null;
        if(invasion.getCloseCommand().length() > 0) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), invasion.getCloseCommand());
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Methods.color(" ")));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Methods.color("  &eThe invasion "+invasion.getName()+" &eis ending in &7"+time+" &eseconds.")));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Methods.color("  &eThe entry of new players has been closed.")));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Methods.color(" ")));
        openedFor = time;
        if(time == 0) reset();
        else new BukkitRunnable() {
            @Override
            public void run() {
                reset();
            }
        }.runTaskLater(Invasions.plugin, time* 20L);
    }

    public void reset(){
        entities.forEach(Entity::remove);
        MobListener.decoys.forEach(Entity::remove);
        players.forEach(player -> {
            player.teleport(player.getBedSpawnLocation() != null ? player.getBedSpawnLocation() : player.getWorld().getSpawnLocation());
        });
        entities.clear();
        MobListener.decoys.clear();
        players.clear();
    }
}
