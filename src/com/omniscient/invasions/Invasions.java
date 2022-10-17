package com.omniscient.invasions;

import com.google.gson.reflect.TypeToken;
import com.omniscient.invasions.Commands.ConfigCommand;
import com.omniscient.invasions.Commands.InvasionsCommand;
import com.omniscient.invasions.Invasion.Invasion;
import com.omniscient.invasions.Invasion.InvasionConfig;
import com.omniscient.invasions.Invasion.InvasionInstance;
import com.omniscient.invasions.Invasion.MobListener;
import com.omniscient.invasions.Mobs.*;
import com.omniscient.invasions.Serialization.ItemStackArraySerializer;
import com.omniscient.invasions.Serialization.ItemStackListSerializer;
import com.omniscient.invasions.Serialization.LocationSerializer;
import com.omniscient.omnicore.OmniCore;
import com.omniscient.omnicore.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class Invasions extends JavaPlugin {
    public static Invasions plugin;
    public static InvasionConfig config;
    public static int cooldown = 0;
    public static InvasionInstance invasionInstance = null;

    @Override
    public void onEnable() {
        plugin = this;
        OmniCore.PREFIX = "&c[&eInvasions&c]";
        OmniCore.enable(this);

        OmniCore.registerListener(new MobListener());
        OmniCore.registerCommand(new ConfigCommand());
        OmniCore.registerCommand(new InvasionsCommand());
        OmniCore.registerSerializer(Location.class, new LocationSerializer());
        OmniCore.registerSerializer(ItemStack[].class, new ItemStackArraySerializer());
        OmniCore.registerSerializer(new TypeToken<List<ItemStack>>(){}.getType(), new ItemStackListSerializer());

        OmniCore.registerEntity("CustomZombie", 54, CustomZombie.class);
        OmniCore.registerEntity("CustomCaveSpider", 59, CustomCaveSpider.class);
        OmniCore.registerEntity("CustomCreeper", 50, CustomCreeper.class);
        OmniCore.registerEntity("CustomEnderman", 58, CustomEnderman.class);
        OmniCore.registerEntity("CustomBlaze", 61, CustomBlaze.class);
        OmniCore.registerEntity("CustomIronGolem", 99, CustomIronGolem.class);
        OmniCore.registerEntity("CustomWolf", 95, CustomWolf.class);
        OmniCore.registerEntity("CustomGhast", 56, CustomGhast.class);

        InvasionConfig newConfig = OmniCore.GSON.fromJson(OmniCore.readFile("config.json"), new TypeToken<InvasionConfig>(){}.getType());
        config = newConfig == null ? new InvasionConfig() : newConfig;
        Invasion.invasions.addAll(OmniCore.GSON.fromJson(OmniCore.readFile("invasions.json"), new TypeToken<List<Invasion>>(){}.getType()));

        new BukkitRunnable() {
            @Override
            public void run() {
                cooldown--;
                if(cooldown >= 0) return;
                cooldown = 0;
                if(Invasion.invasions.size() == 0) return;
                new InvasionInstance(Invasion.invasions.get(new Random().nextInt(Invasion.invasions.size())));
                cooldown = config.getCooldown();
            }
        }.runTaskTimer(plugin, 0, 20);

        Methods.consoleLog("&aPlugin enabled.");
    }

    @Override
    public void onDisable() {
        OmniCore.writeFile("config.json", OmniCore.GSON.toJson(config));
        OmniCore.writeFile("invasions.json", OmniCore.GSON.toJson(Invasion.invasions));

        if(invasionInstance != null) invasionInstance.close(0);

        Methods.consoleLog("&cPlugin disabled.");
    }
}
