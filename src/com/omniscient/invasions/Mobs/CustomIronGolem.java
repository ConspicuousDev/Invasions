package com.omniscient.invasions.Mobs;

import com.omniscient.omnicore.Utils.Methods;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

import java.util.List;

public class CustomIronGolem extends EntityIronGolem {
    public CustomIronGolem(World world) {
        super(world);
        this.setCustomName(getName());
        this.setPlayerCreated(false);
    }
}
