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

public class MobAbilitySelectorGUI extends OmniGUI {
    public final int BACK = 49;
    public final int PREVIOUS = 18;
    public final int NEXT = 26;
    public final List<Integer> ABILITIES = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23 ,24, 25,
            28, 29, 30, 31, 32, 33, 34
    );

    private int page = 0;
    private final Invasion invasion;
    private final InvasionMob mob;
    public MobAbilitySelectorGUI(Player player, Invasion invasion, InvasionMob mob) {
        super(player);
        this.invasion = invasion;
        this.mob = mob;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Mob Editor: "+ Methods.stripColor(mob.getName()));
        int maxPage = (int) Math.ceil((double) mob.getAbilities().size()/ ABILITIES.size());
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to see the previous page.", Material.ARROW));
        if(page < maxPage-1) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to see the next page.", Material.ARROW));
        for (int i = 0; i < ABILITIES.size(); i++) {
            int slot = ABILITIES.get(i);
            if(page* ABILITIES.size()+i >= InvasionMob.Ability.values().length) break;
            InvasionMob.Ability ability = InvasionMob.Ability.values()[page* ABILITIES.size()+i];
            inventory.setItem(slot, ItemFactory.makeItem((mob.getAbilities().contains(ability) ? "&a" : "&c")+ability.getName(), "&7Click to "+(mob.getAbilities().contains(ability) ? "deactivate" : "activate")+".\n\n"+ability.getLore(), ability.getIcon()));
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int maxPage = (int) Math.ceil((double) mob.getAbilities().size()/ ABILITIES.size());
        if(e.getRawSlot() == BACK) new MobEditorGUI(player, invasion, mob).open();
        else if(page > 0 && e.getRawSlot() == PREVIOUS) page--;
        else if(page < maxPage-1 && e.getRawSlot() == NEXT) page++;
        else if(ABILITIES.contains(e.getRawSlot()) && page* ABILITIES.size()+ ABILITIES.indexOf(e.getRawSlot()) < InvasionMob.Ability.values().length){
            InvasionMob.Ability ability = InvasionMob.Ability.values()[ABILITIES.indexOf(e.getRawSlot())+page* ABILITIES.size()];
            if(mob.getAbilities().contains(ability)) mob.getAbilities().remove(ability);
            else mob.getAbilities().add(ability);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
    }
    @Override
    public void onClose(InventoryCloseEvent e) {
    }
}
