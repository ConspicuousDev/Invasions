package com.omniscient.invasions.Config;

import com.omniscient.invasions.Invasion.Invasion;
import com.omniscient.invasions.Invasion.InvasionMob;
import com.omniscient.omnicore.GUI.OmniGUI;
import com.omniscient.omnicore.Items.ItemFactory;
import com.omniscient.omnicore.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MobLocationsEditorGUI extends OmniGUI {
    public final int BACK = 49;
    public final int NEW = 4;
    public final int PREVIOUS = 18;
    public final int NEXT = 26;
    public final List<Integer> LOCATIONS = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23 ,24, 25,
            28, 29, 30, 31, 32, 33, 34
    );

    private int page = 0;
    private final Invasion invasion;
    private final InvasionMob mob;
    public MobLocationsEditorGUI(Player player, Invasion invasion, InvasionMob mob) {
        super(player);
        this.invasion = invasion;
        this.mob = mob;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Mob Editor: "+ Methods.stripColor(mob.getName()));
        int maxPage = (int) Math.ceil((double) mob.getLocations().size()/ LOCATIONS.size());
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        inventory.setItem(NEW, ItemFactory.makeItem("&aCreate new Spawn Location", "&7Click to create a\n&7new Spawn Location.", Material.INK_SACK, 10));
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to see the previous page.", Material.ARROW));
        if(page < maxPage-1) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to see the next page.", Material.ARROW));
        for (int i = 0; i < LOCATIONS.size(); i++) {
            int slot = LOCATIONS.get(i);
            if(page* LOCATIONS.size()+i >= mob.getLocations().size()) break;
            Location location = mob.getLocations().get(page* LOCATIONS.size()+i);
            inventory.setItem(slot, ItemFactory.makeItem("&eLocation &8#"+(page*LOCATIONS.size()+i+1), "&7Left Click to teleport.\n&7Right Click to remove.\n\n  &7- World: &f"+location.getWorld().getName()+"\n  &7- X: &f"+Methods.formatDecimal(location.getX())+"\n  &7- Y: &f"+Methods.formatDecimal(location.getY())+"\n  &7- Z: &f"+Methods.formatDecimal(location.getZ())+"\n  &7- Yaw: &f"+Methods.formatDecimal(location.getYaw())+"\n  &7- Pitch: &f"+Methods.formatDecimal(location.getPitch()), Material.COMPASS));
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int maxPage = (int) Math.ceil((double) mob.getLocations().size()/ LOCATIONS.size());
        if(e.getSlot() == BACK) new MobEditorGUI(player, invasion, mob).open();
        else if(page > 0 && e.getSlot() == PREVIOUS) page--;
        else if(page < maxPage-1 && e.getSlot() == NEXT) page++;
        else if(e.getSlot() == NEW) mob.getLocations().add(player.getLocation());
        else if(LOCATIONS.contains(e.getSlot()) && page* LOCATIONS.size()+ LOCATIONS.indexOf(e.getSlot()) < mob.getLocations().size()){
            Location location = mob.getLocations().get(LOCATIONS.indexOf(e.getSlot())+page* LOCATIONS.size());
            if(e.getAction() == InventoryAction.PICKUP_ALL) player.teleport(location);
            else if(e.getAction() == InventoryAction.PICKUP_HALF) mob.getLocations().remove(location);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
    }
    @Override
    public void onClose(InventoryCloseEvent e) {
    }
}
