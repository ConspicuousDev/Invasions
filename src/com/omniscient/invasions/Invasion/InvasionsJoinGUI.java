package com.omniscient.invasions.Invasion;

import com.omniscient.invasions.Invasions;
import com.omniscient.omnicore.GUI.OmniGUI;
import com.omniscient.omnicore.Items.ItemFactory;
import com.omniscient.omnicore.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class InvasionsJoinGUI extends OmniGUI {
    private final int NORTH = 10;
    private final int EAST = 12;
    private final int SOUTH = 14;
    private final int WEST = 16;

    public InvasionsJoinGUI(Player player) {
        super(player);
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, Methods.stripColor(Invasions.invasionInstance.getInvasion().getName()));
        inventory.setItem(NORTH, ItemFactory.makeItem(Invasions.invasionInstance.getInvasion().getName()+" &8North Entry", "&7Click to join the invasion.", Material.COMPASS));
        inventory.setItem(EAST, ItemFactory.makeItem(Invasions.invasionInstance.getInvasion().getName()+" &8East Entry", "&7Click to join the invasion.", Material.COMPASS));
        inventory.setItem(SOUTH, ItemFactory.makeItem(Invasions.invasionInstance.getInvasion().getName()+" &8South Entry", "&7Click to join the invasion.", Material.COMPASS));
        inventory.setItem(WEST, ItemFactory.makeItem(Invasions.invasionInstance.getInvasion().getName()+" &8West Entry", "&7Click to join the invasion.", Material.COMPASS));
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getRawSlot() == NORTH) Invasions.invasionInstance.spawnPlayer(player, BlockFace.NORTH);
        else if(e.getRawSlot() == EAST) Invasions.invasionInstance.spawnPlayer(player, BlockFace.EAST);
        else if(e.getRawSlot() == SOUTH) Invasions.invasionInstance.spawnPlayer(player, BlockFace.SOUTH);
        else if(e.getRawSlot() == WEST) Invasions.invasionInstance.spawnPlayer(player, BlockFace.WEST);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
