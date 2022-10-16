package com.omniscient.invasions.Config;

import com.omniscient.invasions.Invasion.Invasion;
import com.omniscient.invasions.Invasion.InvasionMob;
import com.omniscient.omnicore.GUI.OmniGUI;
import com.omniscient.omnicore.Items.ItemFactory;
import com.omniscient.omnicore.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class MobDropsEditorGUI extends OmniGUI {
    public final int BACK = 49;
    public final int PREVIOUS = 18;
    public final int NEXT = 26;
    public final List<Integer> DROPS = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23 ,24, 25,
            28, 29, 30, 31, 32, 33, 34
    );

    private int page = 0;
    private final Invasion invasion;
    private final InvasionMob mob;
    public MobDropsEditorGUI(Player player, Invasion invasion, InvasionMob mob) {
        super(player);
        this.invasion = invasion;
        this.mob = mob;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Mobs: "+ Methods.stripColor(invasion.getName()));
        int maxPage = (int) Math.ceil((double) mob.getDrops().size()/ DROPS.size());
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to see the previous page.", Material.ARROW));
        if(page < maxPage-1) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to see the next page.", Material.ARROW));
        for (int i = 0; i < DROPS.size(); i++) {
            int slot = DROPS.get(i);
            if(page* DROPS.size()+i >= mob.getDrops().size()) break;
            ItemStack drop = mob.getDrops().get(page* DROPS.size()+i);
            inventory.setItem(slot, ItemFactory.addToLore(drop, "\n&7Shift + Click to remove."));
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int maxPage = (int) Math.ceil((double) mob.getDrops().size()/ DROPS.size());
        if(e.getClickedInventory() == player.getOpenInventory().getBottomInventory()) {
            ItemStack drop = e.getCurrentItem();
            if(drop == null || drop.getType() == Material.AIR) return;
            mob.getDrops().add(drop);
        }else if(e.getSlot() == BACK) new MobEditorGUI(player, invasion, mob).open();
        else if(page > 0 && e.getSlot() == PREVIOUS) page--;
        else if(page < maxPage-1 && e.getSlot() == NEXT) page++;
        else if(DROPS.contains(e.getSlot()) && page* DROPS.size()+ DROPS.indexOf(e.getSlot()) < mob.getDrops().size()){
            ItemStack drop = mob.getDrops().get(page* DROPS.size()+ DROPS.indexOf(e.getSlot()));
            if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) mob.getDrops().remove(drop);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }
    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
