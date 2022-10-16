package com.omniscient.invasions.Invasion;

import com.omniscient.omnicore.Utils.Methods;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

import java.util.*;

public class Invasion {
    public enum Skin{
        BURNED("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UxZTFlODJiZjQzNzhhN2IxMzkyMjliNTYxYzhmMDExOWJmNTY1NTEyODAxNGQzYzU0MzlkODk4MzAzZjFiMCJ9fX0="),
        EMERALD("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE1ZDMzYWMzZDQwNjJlOWM3YjRhNDMzN2NiZWEzZmE1MGQ1NDNhZTJlMWNkODFiNmQwNzcxOGFhZDcwOTU3MSJ9fX0="),
        DIAMOND("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMxMWU0OWE2NDk3ZTc3YjViNDFkN2JiMzRlNTY3MzU5YWEzMjIyNTY5Y2ViOGM1MGU2M2YxYTVhZjdiZjUyOCJ9fX0="),
        IRON("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ5ZmNlOTA2ZTNjOWViNWY2NTlmZWIxNmUwNzI2M2VjZjA4MTFiM2RhNjk2NTk4NGRhNzEwZGQ5NGQxNGJjZSJ9fX0="),
        GOLD("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I4NDAwN2IyMDQ5Y2Q5Y2VkNDExN2QxMDc5M2IwNzZiYTUwZWJmNDQwNWU2Zjk2ZjU5ZjY3NTliYWJkMzE4NiJ9fX0="),
        ;
        private final String texture;
        Skin(String texture){
            this.texture = texture;
        }

        public String getTexture() {
            return texture;
        }
    }

    public static final List<Invasion> invasions = new ArrayList<>();

    private final UUID uuid;
    private String name = "&7New Invasion";
    private Skin skin = Skin.BURNED;
    private final List<InvasionMob> mobs = new ArrayList<>();
    private final Map<BlockFace, Location> locations = new HashMap<BlockFace, Location>(){{
        put(BlockFace.NORTH, null);
        put(BlockFace.EAST, null);
        put(BlockFace.SOUTH, null);
        put(BlockFace.WEST, null);
    }};
    private String openCommand = "";
    private String closeCommand = "";
    public Invasion(UUID uuid){
        this.uuid = uuid;
        invasions.add(this);
    }

    public UUID getUUID() {
        return uuid;
    }
    public String getName() {
        return name;
    }
    public Skin getSkin() {
        return skin;
    }
    public List<InvasionMob> getMobs() {
        return mobs;
    }
    public Map<BlockFace, Location> getLocations() {
        return locations;
    }
    public String getOpenCommand() {
        return openCommand;
    }
    public String getCloseCommand() {
        return closeCommand;
    }

    public void setName(String name){
        this.name = (name.startsWith("&7") ? "" : "&7")+name;
    }
    public void setSkin(Skin skin) {
        this.skin = skin;
    }
    public void setOpenCommand(String openCommand) {
        this.openCommand = openCommand;
    }
    public void setCloseCommand(String closeCommand) {
        this.closeCommand = closeCommand;
    }

    public static Invasion get(UUID uuid){
        return invasions.stream()
                .filter(invasion -> invasion.getUUID() == uuid)
                .findFirst()
                .orElse(null);
    }
    public static Invasion get(String name){
        return invasions.stream()
                .filter(invasion -> Methods.stripColor(name).equalsIgnoreCase(Methods.stripColor(invasion.getName())))
                .findFirst()
                .orElse(null);
    }
}
