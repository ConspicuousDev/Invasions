package com.omniscient.invasions.Mobs;

import com.omniscient.omnicore.Utils.Methods;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

import java.util.List;

public class CustomGhast extends EntityGhast {
    public CustomGhast(World world) {
        super(world);
        this.setCustomName(getName());
    }
}
