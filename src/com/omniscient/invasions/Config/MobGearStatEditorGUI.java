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
import org.bukkit.inventory.ItemStack;

public class MobGearStatEditorGUI extends OmniGUI {
    public final int BACK = 49;
    public final int HELMET = 10;
    public final int CHESTPLATE = 11;
    public final int LEGGINGS = 12;
    public final int BOOTS = 13;
    public final int WEAPON = 16;
    public final int HEALTH = 28;
    public final int SPEED = 31;
    public final int DAMAGE = 34;

    private final InvasionMob mob;
    private final Invasion invasion;
    protected MobGearStatEditorGUI(Player player, Invasion invasion, InvasionMob mob) {
        super(player);
        this.mob = mob;
        this.invasion = invasion;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Mob Editor: "+Methods.stripColor(mob.getName()));
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        inventory.setItem(HELMET, ItemFactory.addToLore(mob.getItems()[0].getType() != Material.AIR ? mob.getItems()[0] : ItemFactory.makeItem("&eHelmet", null, Material.STAINED_GLASS_PANE, 14), "\n&7Place item here to set helmet.\n&7Shift + Click to remove."));
        inventory.setItem(CHESTPLATE, ItemFactory.addToLore(mob.getItems()[1].getType() != Material.AIR ? mob.getItems()[1] :ItemFactory.makeItem("&eChestplate", null, Material.STAINED_GLASS_PANE, 14), "\n&7Place item here to set chestplate.\n&7Shift + Click to remove."));
        inventory.setItem(LEGGINGS, ItemFactory.addToLore(mob.getItems()[2].getType() != Material.AIR ? mob.getItems()[2] :ItemFactory.makeItem("&eLeggings", null, Material.STAINED_GLASS_PANE, 14), "\n&7Place item here to set leggings.\n&7Shift + Click to remove."));
        inventory.setItem(BOOTS, ItemFactory.addToLore(mob.getItems()[3].getType() != Material.AIR ? mob.getItems()[3] :ItemFactory.makeItem("&eBoots", null, Material.STAINED_GLASS_PANE, 14), "\n&7Place item here to set boots.\n&7Shift + Click to remove."));
        inventory.setItem(WEAPON, ItemFactory.addToLore(mob.getItems()[4].getType() != Material.AIR ? mob.getItems()[4] :ItemFactory.makeItem("&eWeapon", null, Material.STAINED_GLASS_PANE, 14), "\n&7Place item here to set weapon.\n&7Shift + Click to remove."));
        inventory.setItem(HEALTH, ItemFactory.makeItem("&cHealth", "&7Click to set health.\n\n  &fCurrent: &c"+mob.getHealth(), Material.APPLE));
        inventory.setItem(SPEED, ItemFactory.makeItem("&fSpeed", "&7Click to set speed.\n\n  &fCurrent: &f"+mob.getSpeed(), Material.SUGAR));
        inventory.setItem(DAMAGE, ItemFactory.makeItem("&cDamage", "&7Click to set damage.\n\n  &fCurrent: &c"+mob.getDamage(), Material.DIAMOND_SWORD));
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        if(e.getClickedInventory() == player.getOpenInventory().getTopInventory()) e.setCancelled(true);
        else if(e.getAction() != InventoryAction.PICKUP_ALL) e.setCancelled(true);
        if(e.getRawSlot() == BACK) new MobEditorGUI(player, invasion, mob).open();
        else if(e.getClickedInventory() == player.getOpenInventory().getTopInventory() && e.getCursor() != null && e.getCursor().getType() != Material.AIR && e.getAction() == InventoryAction.SWAP_WITH_CURSOR){
            if(e.getRawSlot() == HELMET) mob.getItems()[0] = e.getCursor();
            else if(e.getRawSlot() == CHESTPLATE) mob.getItems()[1] = e.getCursor();
            else if(e.getRawSlot() == LEGGINGS) mob.getItems()[2] = e.getCursor();
            else if(e.getRawSlot() == BOOTS) mob.getItems()[3] = e.getCursor();
            else if(e.getRawSlot() == WEAPON) mob.getItems()[4] = e.getCursor();
            else {
                e.setCancelled(true);
                return;
            }
            e.setCursor(new ItemStack(Material.AIR));
        }else if(e.getRawSlot() == HELMET) mob.getItems()[0] = new ItemStack(Material.AIR);
        else if(e.getRawSlot() == CHESTPLATE) mob.getItems()[1] = new ItemStack(Material.AIR);
        else if(e.getRawSlot() == LEGGINGS) mob.getItems()[2] = new ItemStack(Material.AIR);
        else if(e.getRawSlot() == BOOTS) mob.getItems()[3] = new ItemStack(Material.AIR);
        else if(e.getRawSlot() == WEAPON) mob.getItems()[4] = new ItemStack(Material.AIR);
        else if(e.getRawSlot() == HEALTH || e.getRawSlot() == SPEED || e.getRawSlot() == DAMAGE){
            new AnswerRunnable(e.getRawSlot() == HEALTH ? "health" : e.getRawSlot() == SPEED ? "speed" : "damage", this, 15, value -> {
                double v;
                try{
                    v = Double.parseDouble(value);
                }catch (NumberFormatException ex){
                    player.sendMessage(Methods.color("&c"+(e.getRawSlot() == HEALTH ? "Health" : e.getRawSlot() == SPEED ? "Speed" : "Damage")+" must be a number."));
                    return;
                }
                if(v <= 0){
                    player.sendMessage(Methods.color("&c"+(e.getRawSlot() == HEALTH ? "Health" : e.getRawSlot() == SPEED ? "Speed" : "Damage")+" must be positive."));
                    return;
                }
                if(e.getRawSlot() == HEALTH) mob.setHealth(v);
                else if(e.getRawSlot() == SPEED) mob.setSpeed(v);
                else if(e.getRawSlot() == DAMAGE) mob.setDamage(v);
            });
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }
    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
