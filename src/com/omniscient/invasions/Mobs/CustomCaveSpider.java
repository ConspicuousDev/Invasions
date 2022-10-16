package com.omniscient.invasions.Mobs;

import com.omniscient.omnicore.Utils.Methods;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

import java.util.List;

public class CustomCaveSpider extends EntityCaveSpider {
    public CustomCaveSpider(World world) {
        super(world);
        this.setCustomName(getName());
        this.setInvisible(false);
    }
}
