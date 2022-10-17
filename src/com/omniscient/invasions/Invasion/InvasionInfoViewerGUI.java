package com.omniscient.invasions.Invasion;

import com.omniscient.invasions.Config.InvasionEditorGUI;
import com.omniscient.invasions.Config.MobDropsEditorGUI;
import com.omniscient.invasions.Config.MobEditorGUI;
import com.omniscient.omnicore.GUI.OmniGUI;
import com.omniscient.omnicore.Items.ItemFactory;
import com.omniscient.omnicore.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class InvasionInfoViewerGUI extends OmniGUI {
    public final int BACK = 49;
    public final int PREVIOUS = 18;
    public final int NEXT = 26;
    public final List<Integer> MOBS = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23 ,24, 25,
            28, 29, 30, 31, 32, 33, 34
    );

    private int page = 0;
    private final Invasion invasion;
    public InvasionInfoViewerGUI(Player player, Invasion invasion) {
        super(player);
        this.invasion = invasion;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Mobs: "+Methods.stripColor(invasion.getName()));
        int maxPage = (int) Math.ceil((double) invasion.getMobs().size()/ MOBS.size());
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to see the previous page.", Material.ARROW));
        if(page < maxPage-1) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to see the next page.", Material.ARROW));
        for (int i = 0; i < MOBS.size(); i++) {
            int slot = MOBS.get(i);
            if(page*MOBS.size()+i >= invasion.getMobs().size()) break;
            InvasionMob mob = invasion.getMobs().get(page* MOBS.size()+i);
            inventory.setItem(slot, ItemFactory.makeSkull(mob.getName(), "&7Click to view loot table.", mob.getType().getTexture()));
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int maxPage = (int) Math.ceil((double) invasion.getMobs().size()/ MOBS.size());
        if(e.getSlot() == BACK) new InvasionsInfoGUI(player).open();
        else if(page > 0 && e.getSlot() == PREVIOUS) page--;
        else if(page < maxPage-1 && e.getSlot() == NEXT) page++;
        else if(MOBS.contains(e.getSlot()) && page* MOBS.size()+ MOBS.indexOf(e.getSlot()) < invasion.getMobs().size()) new MobDropsEditorGUI(player, invasion, invasion.getMobs().get(MOBS.indexOf(e.getSlot())+page*MOBS.size()), false).open();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
