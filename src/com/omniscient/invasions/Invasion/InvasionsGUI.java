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
import org.bukkit.inventory.ItemStack;
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
        ItemStack placeholder = ItemFactory.makeItem(" ", null, Material.STAINED_GLASS_PANE, 9);
        for (int i = 0; i < 27; i += 9) {
            inventory.setItem(i, placeholder);
            inventory.setItem(i+8, placeholder);
        }
        if(Invasions.invasionInstance == null) inventory.setItem(INVASION, ItemFactory.makeItem("&cInvasion Closed", "&7Currently there is no \n&7invasion active.\n\n  &7Cooldown: &f"+Methods.formatTimerString(Invasions.cooldown), Material.BARRIER));
        else inventory.setItem(INVASION, ItemFactory.makeSkull(Invasions.invasionInstance.getInvasion().getName(), "&7Click to join the invasion.\n\n  &7Time Left: &f"+Methods.formatTimerString(Invasions.invasionInstance.getOpenedFor()), Invasions.invasionInstance.getInvasion().getSkin().getTexture()));
        inventory.setItem(INFO, ItemFactory.makeItem("&3Invasions", "&7Invasions are a fast-paced PvE and PvP event\n&7containing high valued rewards while confronting\n&7the player in a very high risk situation.\n\n&7A random invasion occurs every &f&n"+Methods.getDuration(Invasions.config.getCooldown())+"&7.\n&7This timed event lasts for &f&n"+Methods.getDuration(Invasions.config.getDuration())+"&7.\n\n&7Rare mobs and a single &f&nBoss&7 are placed across\n&7each invasion. They contain high loot, but be careful...\n&7PvP is enabled and this place is &nrisk-inventory&7!\n\n&7Click to view all invasions and their loot.", Material.REDSTONE_TORCH_ON));
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getRawSlot() == INVASION && Invasions.invasionInstance != null) new InvasionsJoinGUI(player).open();
        else if(e.getRawSlot() == INFO) new InvasionsInfoGUI(player).open();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        runnable.cancel();
    }
}
