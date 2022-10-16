package com.omniscient.invasions.Config;

import com.omniscient.invasions.Invasion.Invasion;
import com.omniscient.omnicore.GUI.OmniGUI;
import com.omniscient.omnicore.Items.ItemFactory;
import com.omniscient.omnicore.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LocationEditorGUI extends OmniGUI {
    public final int BACK = 31;
    public final List<Integer> LOCATIONS = Arrays.asList(10, 12, 14, 16);
    public final List<BlockFace> BLOCK_FACES = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

    private final Invasion invasion;
    protected LocationEditorGUI(Player player, Invasion invasion) {
        super(player);
        this.invasion = invasion;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, "Location Editor: "+ Methods.stripColor(invasion.getName()));
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        AtomicInteger i = new AtomicInteger();
        BLOCK_FACES.forEach(blockFace -> {
            Location location = invasion.getLocations().get(blockFace);
            inventory.setItem(LOCATIONS.get(i.getAndIncrement()), ItemFactory.makeItem("&e"+Methods.capitalize(blockFace.name()), "&7Left Click to "+(location == null ? "set" : "teleport to")+" location.\n&7Right Click to remove location.\n\n  "+(location != null ? "&7" : "&c")+"Current location"+(location != null ? ":\n" : " is not set.")+(location != null ? "    &7- World: &f"+location.getWorld().getName()+"\n    &7- X: &f"+Methods.formatDecimal(location.getX())+"\n    &7- Y: &f"+Methods.formatDecimal(location.getY())+"\n    &7- Z: &f"+Methods.formatDecimal(location.getZ())+"\n    &7- Yaw: &f"+Methods.formatDecimal(location.getYaw())+"\n    &7- Pitch: &f"+Methods.formatDecimal(location.getPitch()) : ""), Material.COMPASS));
        });
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getSlot() == BACK) new InvasionEditorGUI(player, invasion).open();
        else if(LOCATIONS.contains(e.getSlot())){
            BlockFace blockFace = BLOCK_FACES.get(LOCATIONS.indexOf(e.getSlot()));
            if(e.getAction() == InventoryAction.PICKUP_ALL){
                if(invasion.getLocations().get(blockFace) == null) invasion.getLocations().put(blockFace, player.getLocation());
                else player.teleport(invasion.getLocations().get(blockFace));
            }else if(e.getAction() == InventoryAction.PICKUP_HALF) invasion.getLocations().put(blockFace, null);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
