package com.omniscient.invasions.Config;

import com.omniscient.invasions.Invasion.Invasion;
import com.omniscient.invasions.Invasion.InvasionInfoViewerGUI;
import com.omniscient.invasions.Invasion.InvasionMob;
import com.omniscient.invasions.Invasion.InvasionsInfoGUI;
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

import java.util.Arrays;
import java.util.List;

public class MobDropsEditorGUI extends OmniGUI {
    public final int BACK = 49;
    public final int AMOUNT = 4;
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
    private final boolean admin;
    public MobDropsEditorGUI(Player player, Invasion invasion, InvasionMob mob, boolean admin) {
        super(player);
        this.invasion = invasion;
        this.mob = mob;
        this.admin = admin;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Mobs: "+ Methods.stripColor(invasion.getName()));
        int maxPage = (int) Math.ceil((double) mob.getDrops().size()/ DROPS.size());
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        inventory.setItem(AMOUNT, ItemFactory.makeItem("&eDrop Amount", (admin ? "&7Click to set how many stacks\n&7from the list below the mob\n&7will drop.\n" : "")+"\n  &7- Amount: &f"+mob.getDropAmount(), Material.DIAMOND));
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to see the previous page.", Material.ARROW));
        if(page < maxPage-1) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to see the next page.", Material.ARROW));
        for (int i = 0; i < DROPS.size(); i++) {
            int slot = DROPS.get(i);
            if(page* DROPS.size()+i >= mob.getDrops().size()) break;
            ItemStack drop = mob.getDrops().get(page* DROPS.size()+i);
            inventory.setItem(slot, admin ? ItemFactory.addToLore(drop, "\n&7Shift + Click to remove.") : drop);
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int maxPage = (int) Math.ceil((double) mob.getDrops().size()/ DROPS.size());
        if(admin && e.getClickedInventory() == player.getOpenInventory().getBottomInventory()) {
            ItemStack drop = e.getCurrentItem();
            if(drop == null || drop.getType() == Material.AIR) return;
            mob.getDrops().add(drop);
        }else if(e.getRawSlot() == BACK){
            if(admin) new MobEditorGUI(player, invasion, mob).open();
            else new InvasionInfoViewerGUI(player, invasion).open();
        }else if(admin && e.getRawSlot() == AMOUNT) new AnswerRunnable("drop amount", this, 15, value -> {
            try{
                int i = Integer.parseInt(value);
                if(i < 0) Methods.color("&cThe amount must be greater than zero.");
                mob.setDropAmount(i);
            }catch (NumberFormatException ex){
                player.sendMessage(Methods.color("&cThe amount must be a number."));
            }
        });
        else if(page > 0 && e.getRawSlot() == PREVIOUS) page--;
        else if(page < maxPage-1 && e.getRawSlot() == NEXT) page++;
        else if(admin && DROPS.contains(e.getRawSlot()) && page* DROPS.size()+ DROPS.indexOf(e.getRawSlot()) < mob.getDrops().size() && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) mob.getDrops().remove(mob.getDrops().get(page* DROPS.size()+ DROPS.indexOf(e.getRawSlot())));
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }
    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
