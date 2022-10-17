package com.omniscient.invasions.Invasion;

import com.omniscient.invasions.Config.InvasionEditorGUI;
import com.omniscient.invasions.Invasions;
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
import java.util.UUID;

public class InvasionsInfoGUI extends OmniGUI {
    private final int BACK = 49;
    private final int INFO = 4;
    public final int PREVIOUS = 18;
    public final int NEXT = 26;
    public final List<Integer> INVASIONS = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23 ,24, 25,
            28, 29, 30, 31, 32, 33, 34
    );

    int page = 0;
    public InvasionsInfoGUI(Player player) {
        super(player);
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Invasions");
        int maxPage = (int) Math.ceil((double) Invasion.invasions.size()/INVASIONS.size());
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        inventory.setItem(INFO, ItemFactory.makeItem("&3Invasions", "&7Invasions are a fas-paced PvE and PvP event\n&7containing high valued rewards while confronting\n&7the player in a very high risk situation.\n\n&7A random invasion occurs every &f&n"+Methods.getDuration(Invasions.config.getCooldown())+"&7.\n&7This timed event lasts for &f&n"+Methods.getDuration(Invasions.config.getDuration())+"&7.\n\n&7Rare mobs and a single &f&nBoss&7 are placed across\n&7each invasion. They contain high loot, but be careful...\n&7PvP is enabled and this place is &nrisk-inventory&7!", Material.REDSTONE_TORCH_ON));
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to see the previous page.", Material.ARROW));
        if(page < maxPage-1) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to see the next page.", Material.ARROW));
        for (int i = 0; i < INVASIONS.size(); i++) {
            int slot = INVASIONS.get(i);
            if(page*INVASIONS.size()+i >= Invasion.invasions.size()) break;
            Invasion invasion = Invasion.invasions.get(page*INVASIONS.size()+i);
            inventory.setItem(slot, ItemFactory.makeSkull(invasion.getName(), "&7Click to view mobs and loot tables.", invasion.getSkin().getTexture()));
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int maxPage = (int) Math.ceil((double) Invasion.invasions.size()/INVASIONS.size());
        if(e.getSlot() == BACK) new InvasionsGUI(player).open();
        else if(page > 0 && e.getSlot() == PREVIOUS) page--;
        else if(page < maxPage-1 && e.getSlot() == NEXT) page++;
        else if(INVASIONS.contains(e.getSlot()) && page*INVASIONS.size()+INVASIONS.indexOf(e.getSlot()) < Invasion.invasions.size()) new InvasionInfoViewerGUI(player, Invasion.invasions.get(INVASIONS.indexOf(e.getSlot())+page*INVASIONS.size())).open();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
