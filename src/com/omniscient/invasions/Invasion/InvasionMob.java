package com.omniscient.invasions.Invasion;

import com.omniscient.invasions.Invasions;
import com.omniscient.invasions.Mobs.*;
import com.omniscient.omnicore.Entities.OmniEntityUtil;
import com.omniscient.omnicore.Utils.Methods;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class InvasionMob {
    public enum Type{
        ZOMBIE(EntityType.ZOMBIE, CustomZombie.class, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ=="),
        CAVE_SPIDER(EntityType.CAVE_SPIDER, CustomCaveSpider.class, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE2NDVkZmQ3N2QwOTkyMzEwN2IzNDk2ZTk0ZWViNWMzMDMyOWY5N2VmYzk2ZWQ3NmUyMjZlOTgyMjQifX19"),
        CREEPER(EntityType.CREEPER, CustomCreeper.class, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQyNTQ4MzhjMzNlYTIyN2ZmY2EyMjNkZGRhYWJmZTBiMDIxNWY3MGRhNjQ5ZTk0NDQ3N2Y0NDM3MGNhNjk1MiJ9fX0="),
        ENDERMAN(EntityType.ENDERMAN, CustomEnderman.class, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0="),
        BLAZE(EntityType.BLAZE, CustomBlaze.class, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIwNjU3ZTI0YjU2ZTFiMmY4ZmMyMTlkYTFkZTc4OGMwYzI0ZjM2Mzg4YjFhNDA5ZDBjZDJkOGRiYTQ0YWEzYiJ9fX0="),
        IRON_GOLEM(EntityType.IRON_GOLEM, CustomIronGolem.class, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTEzZjM0MjI3MjgzNzk2YmMwMTcyNDRjYjQ2NTU3ZDY0YmQ1NjJmYTlkYWIwZTEyYWY1ZDIzYWQ2OTljZjY5NyJ9fX0="),
        WOLF(EntityType.WOLF, CustomWolf.class, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjlkMWQzMTEzZWM0M2FjMjk2MWRkNTlmMjgxNzVmYjQ3MTg4NzNjNmM0NDhkZmNhODcyMjMxN2Q2NyJ9fX0="),
        GHAST(EntityType.GHAST, CustomGhast.class, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E4YjcxNGQzMmQ3ZjZjZjhiMzdlMjIxYjc1OGI5YzU5OWZmNzY2NjdjN2NkNDViYmM0OWM1ZWYxOTg1ODY0NiJ9fX0="),
        ;
        private final EntityType type;
        private final Class<? extends EntityInsentient> clazz;
        private final String texture;
        Type(EntityType type, Class<? extends EntityInsentient> clazz, String texture){
            this.type = type;
            this.texture = texture;
            this.clazz = clazz;
        }

        public EntityType getType() {
            return type;
        }
        public Class<? extends EntityInsentient> getClazz() {
            return clazz;
        }
        public String getTexture() {
            return texture;
        }
    }
    public enum Ability{
        EXPLOSION("Explosion", "&7Sends a wave of explosives\n&7in a circular pattern.", Material.TNT, (entity, player) -> {
            entity.getBukkitEntity().getNearbyEntities(32, 32, 32).forEach(t -> {
                if(!(t instanceof Player)) return;
                if(((Player) t).getGameMode() == GameMode.CREATIVE || ((Player) t).getGameMode() == GameMode.SPECTATOR) return;
                TNTPrimed tnt = (TNTPrimed) t.getLocation().getWorld().spawnEntity(t.getLocation(), EntityType.PRIMED_TNT);
                MobListener.tnts.add(tnt);
                tnt.setFuseTicks(20);
            });
        }),
        LIGHTNING("Lightning", "&7Sends a wave of lightning\n&7to all nearby players.", Material.BLAZE_ROD, (entity, player) -> {
            entity.getBukkitEntity().getNearbyEntities(32, 32, 32).forEach(t -> {
                if(!(t instanceof Player)) return;
                if(((Player) t).getGameMode() == GameMode.CREATIVE || ((Player) t).getGameMode() == GameMode.SPECTATOR) return;
                for (int i = 0; i < 10; i++) t.getWorld().strikeLightning(t.getLocation());
            });
        }),
        BLINDNESS("Blindness", "&7Sends a wave of blindness\n&7to all nearby players for\n&710 seconds.", Material.EYE_OF_ENDER, (entity, player) -> {
            entity.getBukkitEntity().getNearbyEntities(32, 32, 32).forEach(t -> {
                if(!(t instanceof Player)) return;
                if(((Player) t).getGameMode() == GameMode.CREATIVE || ((Player) t).getGameMode() == GameMode.SPECTATOR) return;
                ((Player) t).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
            });
        }),
        TELEPORT("Teleport", "&7Teleports the entity to\n&7the current target.", Material.ENDER_PEARL, (entity, player) -> {
            entity.getBukkitEntity().teleport(player);
        }),
        PULL("Pull", "&7Pulls nearby entities towards\n&7the current entity.", Material.LEASH, (entity, player) -> {
            entity.getBukkitEntity().getNearbyEntities(32, 32, 32).forEach(t -> {
                if(!(t instanceof Player)) return;
                if(((Player) t).getGameMode() == GameMode.CREATIVE || ((Player) t).getGameMode() == GameMode.SPECTATOR) return;
                Vector vector = t.getLocation().getDirection().subtract(entity.getBukkitEntity().getLocation().getDirection());
                t.setVelocity(vector.add(new Vector(.0, .1, .0)));
            });
        }),
        INVINCIBLE("Invincible", "&7Makes the entity invincible\n&7for 5 seconds.", Material.GOLDEN_APPLE, (entity, player) -> {
            org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
            MobListener.invulnerables.add(bukkitEntity);
            new BukkitRunnable() {
                @Override
                public void run() {
                    MobListener.invulnerables.remove(bukkitEntity);
                }
            }.runTaskLater(Invasions.plugin, 100);
        }),
        DECOY("Decoy", "&7Spawns a few decoys that\n&7don't take or deal damage.\n&7Hitting a decoy makes you\n&7invisible for a short time.", Material.MONSTER_EGG, (entity, player) -> {
            entity.getBukkitEntity().getNearbyEntities(32, 32, 32).forEach(t -> {
                if(!(t instanceof Player)) return;
                if(((Player) t).getGameMode() == GameMode.CREATIVE || ((Player) t).getGameMode() == GameMode.SPECTATOR) return;
                AtomicReference<InvasionMob> mobType = new AtomicReference<>(null);
                Arrays.asList(InvasionMob.Type.values()).forEach(type -> {
                    if(entity.getClass() != type.getClazz()) return;
                    Invasion.invasions.forEach(invasion -> invasion.getMobs().forEach(mob -> {
                        if(!entity.getCustomName().equals(Methods.color(mob.getName()))) return;
                        mobType.set(mob);
                    }));
                });
                if(mobType.get() == null) return;
                MobListener.decoys.add(mobType.get().summon(entity.getBukkitEntity().getLocation()).getBukkitEntity());
            });
        }),
        WITHER("Wither", "&7Sends a wave of wither\n&7to all nearby players for\n&710 seconds.", Material.NETHER_STAR, (entity, player) -> {
            entity.getBukkitEntity().getNearbyEntities(32, 32, 32).forEach(t -> {
                if(!(t instanceof Player)) return;
                if(((Player) t).getGameMode() == GameMode.CREATIVE || ((Player) t).getGameMode() == GameMode.SPECTATOR) return;
                ((Player) t).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0));
            });
        }),
        ;
        private final String name;
        private final String lore;
        private final Material icon;
        private final BiConsumer<EntityInsentient, Player> execute;
        Ability(String name, String lore, Material icon, BiConsumer<EntityInsentient, Player> execute){
            this.name = name;
            this.lore = lore;
            this.icon = icon;
            this.execute = execute;
        }

        public String getName() {
            return name;
        }
        public String getLore() {
            return lore;
        }
        public Material getIcon() {
            return icon;
        }
        public BiConsumer<EntityInsentient, Player> getExecute() {
            return execute;
        }
    }

    private final UUID uuid;
    private String name = "&7New Mob";
    private Type type = Type.ZOMBIE;
    private double health = 20.0;
    private double speed = 0.2;
    private double damage = 1.0;
    private final ItemStack[] items = new ItemStack[]{
            new ItemStack(Material.AIR),
            new ItemStack(Material.AIR),
            new ItemStack(Material.AIR),
            new ItemStack(Material.AIR),
            new ItemStack(Material.AIR)
    };
    private final List<ItemStack> drops = new ArrayList<>();
    private final List<Location> locations = new ArrayList<>();
    private final List<Ability> abilities = new ArrayList<>();
    public InvasionMob(UUID uuid){
        this.uuid = uuid;
    }

    public UUID getUUID(){
        return uuid;
    }
    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }
    public double getHealth() {
        return health;
    }
    public double getSpeed() {
        return speed;
    }
    public double getDamage() {
        return damage;
    }
    public ItemStack[] getItems() {
        return items;
    }
    public List<ItemStack> getDrops() {
        return drops;
    }
    public List<Location> getLocations() {
        return locations;
    }
    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setName(String name) {
        this.name = (name.startsWith("&7") ? "" : "&7")+name;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public void setHealth(double health) {
        this.health = health;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public void setDamage(double damage) {
        this.damage = damage;
    }

    public EntityInsentient summon(Location location){
        Entity entity = OmniEntityUtil.spawn(type.getClazz(), location);
        EntityInsentient insentient = (EntityInsentient) entity;
        entity.setCustomName(Methods.color(name));
        entity.setCustomNameVisible(true);

        if(entity instanceof EntityCreature) {
            ((List) Methods.getPrivateField("b", PathfinderGoalSelector.class, insentient.goalSelector)).clear();
            ((List) Methods.getPrivateField("c", PathfinderGoalSelector.class, insentient.goalSelector)).clear();
            ((List) Methods.getPrivateField("b", PathfinderGoalSelector.class, insentient.targetSelector)).clear();
            ((List) Methods.getPrivateField("c", PathfinderGoalSelector.class, insentient.targetSelector)).clear();

            insentient.goalSelector.a(1, new PathfinderGoalMeleeAttack((EntityCreature) insentient, EntityHuman.class, 1.0D, true));
            insentient.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget((EntityCreature) insentient, 0.9D, 32.0F));

            insentient.targetSelector.a(1, new PathfinderGoalHurtByTarget((EntityCreature) insentient, true));
            insentient.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>((EntityCreature) insentient, EntityHuman.class, true, true));
        }

        new MobUtils.TargetRunnable(insentient, target -> insentient.setGoalTarget((EntityLiving) ((CraftEntity) target).getHandle(), EntityTargetEvent.TargetReason.FORGOT_TARGET, true)).runTaskTimer(Invasions.plugin, 0, 10);
        new MobUtils.AbilityRunnable(insentient, this).runTaskTimer(Invasions.plugin, 0, 100);
        if(entity instanceof CustomCreeper) new MobUtils.MeleeRunnable(insentient, this).runTaskTimer(Invasions.plugin, 0, 20);

        entity.setEquipment(0, CraftItemStack.asNMSCopy(items[4]));
        entity.setEquipment(4, CraftItemStack.asNMSCopy(items[0]));
        entity.setEquipment(3, CraftItemStack.asNMSCopy(items[1]));
        entity.setEquipment(2, CraftItemStack.asNMSCopy(items[2]));
        entity.setEquipment(1, CraftItemStack.asNMSCopy(items[3]));
        if(insentient.getAttributeInstance(GenericAttributes.maxHealth) != null) insentient.getAttributeInstance(GenericAttributes.maxHealth).setValue(health);
        if(insentient.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED) != null) insentient.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        if(insentient.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE) != null) insentient.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(damage);

        return insentient;
    }
}
