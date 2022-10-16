package com.omniscient.invasions.Mobs;

import com.omniscient.invasions.Invasions;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicReference;

public class CustomEnderman extends EntityEnderman {
    public CustomEnderman(World world) {
        super(world);
        this.setCustomName(getName());
    }
}
