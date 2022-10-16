package com.omniscient.invasions.Config;

import com.omniscient.invasions.Invasion.Invasion;
import com.omniscient.invasions.Invasion.InvasionMob;
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
import java.util.stream.Collectors;

public class MobEditorGUI extends OmniGUI {
    public final int BACK = 31;
    private final int VISUAL = 10;
    private final int DROPS = 11;
    private final int LOCATIONS = 12;
    private final int GEAR = 13;
    private final int ABILITIES = 14;
    private final int SUMMON = 15;
    private final int DELETE = 16;

    private final Invasion invasion;
    private final InvasionMob mob;
    public MobEditorGUI(Player player, Invasion invasion, InvasionMob mob) {
        super(player);
        this.invasion = invasion;
        this.mob = mob;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, "Mob Editor: "+ Methods.stripColor(mob.getName()));
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        inventory.setItem(VISUAL, ItemFactory.makeSkull(mob.getName(), "&7Left Click to edit name.\n&7Right Click to change entity type.", mob.getType().getTexture()));
        inventory.setItem(DROPS, ItemFactory.makeItem("&eDrops", "&7Click to edit drops.", Material.DIAMOND));
        inventory.setItem(LOCATIONS, ItemFactory.makeItem("&eLocations", "&7Click to edit mob spawn locations.", Material.COMPASS));
        inventory.setItem(GEAR, ItemFactory.makeItem("&eGear & Stats", "&7Click to edit mob gear and stats.", Material.DIAMOND_CHESTPLATE));
        inventory.setItem(ABILITIES, ItemFactory.makeItem("&eAbilities", "&7Click to edit mob abilities.", Material.BLAZE_POWDER));
        inventory.setItem(SUMMON, ItemFactory.makeItem("&eSummon", "&7Click to summon mob for testing.", Material.NETHER_STAR));
        inventory.setItem(DELETE, ItemFactory.makeItem("&cDelete", "&7Click to remove this Mob.\n\n  &cWARNING: this action is\n  &cirreversible and there is\n  &cno confirmation prompt.", Material.BARRIER));
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getSlot() == BACK) new MobListGUI(player, invasion).open();
        else if(e.getSlot() == VISUAL && e.getAction() == InventoryAction.PICKUP_ALL) new AnswerRunnable("new name", this, 15, mob::setName);
        else if(e.getSlot() == VISUAL && e.getAction() == InventoryAction.PICKUP_HALF){
            int index = Arrays.stream(InvasionMob.Type.values()).collect(Collectors.toList()).indexOf(mob.getType());
            mob.setType(InvasionMob.Type.values()[index+1 >= InvasionMob.Type.values().length ? 0 : index+1]);
        }else if(e.getSlot() == DROPS) new MobDropsEditorGUI(player, invasion, mob).open();
        else if(e.getSlot() == LOCATIONS) new MobLocationsEditorGUI(player, invasion, mob).open();
        else if(e.getSlot() == GEAR) new MobGearStatEditorGUI(player, invasion, mob).open();
        else if(e.getSlot() == ABILITIES) new MobAbilitySelectorGUI(player, invasion, mob).open();
        else if(e.getSlot() == SUMMON) mob.summon(player.getLocation());
        else if(e.getSlot() == DELETE){
            invasion.getMobs().remove(mob);
            new MobListGUI(player, invasion).open();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
