package com.omniscient.invasions.Invasion;

import com.omniscient.omnicore.Utils.Methods;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MobUtils {
    public static class TargetRunnable extends BukkitRunnable {
        private final EntityInsentient entity;
        private final Consumer<Player> then;
        public TargetRunnable(EntityInsentient entity, Consumer<Player> then){
            this.entity = entity;
            this.then = then;
        }

        @Override
        public void run() {
            if(!entity.isAlive()) {
                cancel();
                return;
            }
            AtomicReference<Player> target = new AtomicReference<>(null);
            AtomicReference<Double> minDistance = new AtomicReference<>(-1.0);
            entity.getBukkitEntity().getNearbyEntities(16, 16, 16).forEach(t -> {
                if(!(t instanceof Player)) return;
                if(((Player) t).getGameMode() == GameMode.CREATIVE || ((Player) t).getGameMode() == GameMode.SPECTATOR) return;
                double distance = entity.getBukkitEntity().getLocation().distance(t.getLocation());
                if(minDistance.get() < 0 || distance < minDistance.get()){
                    target.set((Player) t);
                    minDistance.set(distance);
                }
            });
            if(target.get() == null) return;
            then.accept(target.get());
        }
    }
    public static class MeleeRunnable extends BukkitRunnable {
        private final EntityInsentient entity;
        private final InvasionMob mob;
        public MeleeRunnable(EntityInsentient entity, InvasionMob mob){
            this.entity = entity;
            this.mob = mob;
        }

        @Override
        public void run() {
            if(!entity.isAlive()) {
                cancel();
                return;
            }
            if(entity.getGoalTarget() == null) return;
            if(entity.getBukkitEntity().getLocation().distance(entity.getGoalTarget().getBukkitEntity().getLocation()) >= 1) return;
            entity.getGoalTarget().damageEntity(DamageSource.mobAttack(entity), (float) mob.getDamage());
        }
    }
    public static class AbilityRunnable extends BukkitRunnable{
        private final EntityInsentient entity;
        private final InvasionMob mob;
        public AbilityRunnable(EntityInsentient entity, InvasionMob mob){
            this.entity = entity;
            this.mob = mob;
        }

        @Override
        public void run() {
            if(!entity.isAlive() || mob.getAbilities().size() == 0) {
                cancel();
                return;
            }
            if(entity.getGoalTarget() == null) return;
            if(!(entity.getGoalTarget().getBukkitEntity() instanceof Player)) return;
            int r = new Random().nextInt(100);
            if(r < 50) return;
            InvasionMob.Ability ability = mob.getAbilities().get(new Random().nextInt(mob.getAbilities().size()));
            ability.getExecute().accept(entity, (Player) entity.getGoalTarget().getBukkitEntity());
        }
    }
}
