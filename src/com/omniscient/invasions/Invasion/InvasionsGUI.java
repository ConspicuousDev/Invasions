package com.omniscient.invasions.Invasion;

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
import org.bukkit.scheduler.BukkitRunnable;

public class InvasionsGUI extends OmniGUI {
    private final int INVASION = 11;
    private final int INFO = 15;

    private final BukkitRunnable runnable;
    public InvasionsGUI(Player player) {
        super(player);
        InvasionsGUI gui = this;
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                gui.update(null);
            }
        };
        runnable.runTaskTimer(Invasions.plugin, 0, 20);
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, "Invasions");
        if(Invasions.invasionInstance == null) inventory.setItem(INVASION, ItemFactory.makeItem("&cInvasion Closed", "&7Currently there is no \n&7invasion active.\n\n  &7Cooldown: &f"+Methods.formatTimerString(Invasions.cooldown), Material.BARRIER));
        else inventory.setItem(INVASION, ItemFactory.makeSkull(Invasions.invasionInstance.getInvasion().getName(), "&7Click to join the invasion.\n\n  &7Time Left: &f"+Methods.formatTimerString(Invasions.invasionInstance.getOpenedFor()), Invasions.invasionInstance.getInvasion().getSkin().getTexture()));
        inventory.setItem(INFO, ItemFactory.makeItem("&eInformation", "&7Click to see all the available\n&7invasions, mobs, and loot tables.", Material.REDSTONE_TORCH_ON));
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getSlot() == INVASION && Invasions.invasionInstance != null) new InvasionsJoinGUI(player).open();
        else if(e.getSlot() == INFO) new InvasionsInfoGUI(player).open();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        runnable.cancel();
    }
}
