package com.omniscient.invasions.Invasion;

import com.omniscient.invasions.Invasions;
import com.omniscient.omnicore.Utils.Methods;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class MobListener implements Listener {
    public static List<Entity> decoys = new ArrayList<>();
    public static List<TNTPrimed> tnts = new ArrayList<>();
    public static List<Entity> invulnerables = new ArrayList<>();

    @EventHandler
    public void onMobDeath(EntityDeathEvent e){
        Entity entity = e.getEntity();
        AtomicReference<InvasionMob> mobType = new AtomicReference<>(null);
        Arrays.asList(InvasionMob.Type.values()).forEach(type -> {
            if(((CraftEntity) entity).getHandle().getClass() != type.getClazz()) return;
            Invasion.invasions.forEach(invasion -> invasion.getMobs().forEach(mob -> {
                if(!entity.getCustomName().equals(Methods.color(mob.getName()))) return;
                mobType.set(mob);
            }));
        });
        if(mobType.get() == null) return;
        e.setDroppedExp(0);
        e.getDrops().forEach(item -> item.setType(Material.AIR));
        if(mobType.get().getDrops().size() > 0) {
            Random r = new Random();
            for (int i = 0; i < mobType.get().getDropAmount(); i++)
                entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), mobType.get().getDrops().get(r.nextInt(mobType.get().getDrops().size())));
        }
        if (Invasions.invasionInstance == null) return;
        if(!Invasions.invasionInstance.getEntities().contains(e.getEntity())) return;
        Invasions.invasionInstance.getEntities().remove(e.getEntity());
        if(Invasions.invasionInstance.getEntities().size() > 0) return;
        Invasions.invasionInstance.close(60);
    }

    @EventHandler
    public void onDecoyInteraction(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player)) return;
        if(!decoys.contains(e.getEntity())) return;
        e.setCancelled(true);
        e.getEntity().remove();
        ((Player) e.getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 0));
        decoys.remove(e.getEntity());
    }

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent e){
        if(!(e.getEntity() instanceof TNTPrimed)) return;
        TNTPrimed tnt = (TNTPrimed) e.getEntity();
        if(!tnts.contains(tnt)) return;
        e.setCancelled(true);
        e.blockList().clear();
        tnts.remove(tnt);
    }

    @EventHandler
    public void onInvulnerableInteraction(EntityDamageEvent e){
        if(invulnerables.contains(e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void onInvasionCommand(PlayerCommandPreprocessEvent e){
        if(e.getMessage().trim().toLowerCase().startsWith("/spawn")){
            if(Invasions.invasionInstance == null) return;
            if(!Invasions.invasionInstance.getPlayers().contains(e.getPlayer())) return;
            Invasions.invasionInstance.getPlayers().remove(e.getPlayer());
            return;
        }
        if(Invasions.invasionInstance == null) return;
        if(!Invasions.invasionInstance.getPlayers().contains(e.getPlayer())) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(Methods.color("&cYou cannot send commands during invasions!"));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(Invasions.invasionInstance == null) return;
        if(!Invasions.invasionInstance.getPlayers().contains(e.getEntity())) return;
        Invasions.invasionInstance.getPlayers().remove(e.getEntity());
    }
}
