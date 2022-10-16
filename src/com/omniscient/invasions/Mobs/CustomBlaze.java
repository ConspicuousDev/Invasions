package com.omniscient.invasions.Mobs;

import com.omniscient.omnicore.Utils.Methods;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

import java.util.List;

public class CustomBlaze extends EntityBlaze {
    public CustomBlaze(World world) {
        super(world);
        this.setCustomName(getName());
    }
}
