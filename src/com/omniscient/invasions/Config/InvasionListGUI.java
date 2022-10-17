package com.omniscient.invasions.Config;

import com.omniscient.invasions.Invasion.Invasion;
import com.omniscient.invasions.Invasions;
import com.omniscient.omnicore.GUI.AnswerRunnable;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InvasionListGUI extends OmniGUI {
    public final int CONFIG = 4;
    public final int NEW = 49;
    public final int PREVIOUS = 18;
    public final int NEXT = 26;
    public final List<Integer> INVASIONS = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23 ,24, 25,
            28, 29, 30, 31, 32, 33, 34
    );

    private int page = 0;
    public InvasionListGUI(Player player) {
        super(player);
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Invasions");
        int maxPage = (int) Math.ceil((double) Invasion.invasions.size()/INVASIONS.size());
        inventory.setItem(CONFIG, ItemFactory.makeItem("&eGeneral Configuration", "&7Left Click to set invasion cooldown.\n&7Right Click to set invasion duration.\n\n  &7- Cooldown: &f"+ Methods.getDuration(Invasions.config.getCooldown())+"\n  &7- Duration: &f"+Methods.getDuration(Invasions.config.getDuration()), Material.WATCH));
        inventory.setItem(NEW, ItemFactory.makeItem("&aCreate new Invasion", "&7Click to create a\n&7new Invasion.", Material.INK_SACK, 10));
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to see the previous page.", Material.ARROW));
        if(page < maxPage-1) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to see the next page.", Material.ARROW));
        for (int i = 0; i < INVASIONS.size(); i++) {
            int slot = INVASIONS.get(i);
            if(page*INVASIONS.size()+i >= Invasion.invasions.size()) break;
            Invasion invasion = Invasion.invasions.get(page*INVASIONS.size()+i);
            inventory.setItem(slot, ItemFactory.makeSkull(invasion.getName(), "&eClick to edit.", invasion.getSkin().getTexture()));
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int maxPage = (int) Math.ceil((double) Invasion.invasions.size()/INVASIONS.size());
        if(e.getSlot() == CONFIG){
            if(e.getAction() == InventoryAction.PICKUP_ALL) new AnswerRunnable("invasion cooldown", this, 15, value -> {
                try{
                    Invasions.config.setCooldown(Integer.parseInt(value));
                }catch (NumberFormatException ignored){
                    player.sendMessage(Methods.color("&cThe value must be the number of seconds."));
                }
            });
            else if(e.getAction() == InventoryAction.PICKUP_HALF) new AnswerRunnable("invasion duration", this, 15, value -> {
                try{
                    Invasions.config.setDuration(Integer.parseInt(value));
                }catch (NumberFormatException ignored){
                    player.sendMessage(Methods.color("&cThe value must be the number of seconds."));
                }
            });
        }else if(page > 0 && e.getSlot() == PREVIOUS) page--;
        else if(page < maxPage-1 && e.getSlot() == NEXT) page++;
        else if(e.getSlot() == NEW) new InvasionEditorGUI(player, new Invasion(UUID.randomUUID())).open();
        else if(INVASIONS.contains(e.getSlot()) && page*INVASIONS.size()+INVASIONS.indexOf(e.getSlot()) < Invasion.invasions.size()) new InvasionEditorGUI(player, Invasion.invasions.get(INVASIONS.indexOf(e.getSlot())+page*INVASIONS.size())).open();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
    }
    @Override
    public void onClose(InventoryCloseEvent e) {
    }
}
