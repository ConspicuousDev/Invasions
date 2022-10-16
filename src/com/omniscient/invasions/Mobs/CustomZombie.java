package com.omniscient.invasions.Mobs;

import com.omniscient.omnicore.Utils.Methods;
import net.minecraft.server.v1_8_R3.*;

import java.util.List;

public class CustomZombie extends EntityZombie {
    public CustomZombie(World world) {
        super(world);
        this.setBaby(false);
        this.setVillager(false);
    }
}
