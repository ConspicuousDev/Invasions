package com.omniscient.invasions.Config;

import com.omniscient.invasions.Invasion.Invasion;
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

public class InvasionEditorGUI extends OmniGUI {
    public final int BACK = 31;
    private final int VISUAL = 10;
    private final int MOBS = 12;
    private final int LOCATIONS = 13;
    private final int COMMANDS = 14;
    private final int DELETE = 16;

    private final Invasion invasion;
    protected InvasionEditorGUI(Player player, Invasion invasion) {
        super(player);
        this.invasion = invasion;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, "Invasion Editor: "+ Methods.stripColor(invasion.getName()));
        inventory.setItem(BACK, ItemFactory.makeItem("&eBack", "&7Click to go back.", Material.ARROW));
        inventory.setItem(VISUAL, ItemFactory.makeSkull(invasion.getName(), "&7Left Click to edit name.\n&7Right Click to change skin.", invasion.getSkin().getTexture()));
        inventory.setItem(MOBS, ItemFactory.makeItem("&eMobs", "&7Click to edit mobs.", Material.SKULL_ITEM, 2));
        inventory.setItem(LOCATIONS, ItemFactory.makeItem("&eLocations", "&7Click to edit spawn locations.", Material.COMPASS));
        inventory.setItem(COMMANDS, ItemFactory.makeItem("&eCommands", "&7Left Click to set open command.\n&7Right Click to set close command.\n\n  &7- Open: &f"+(invasion.getOpenCommand().length() == 0 ? "&cUnset" : invasion.getOpenCommand())+"\n  &7- Close: &f"+(invasion.getCloseCommand().length() == 0 ? "&cUnset" : invasion.getCloseCommand()), Material.COMMAND));
        inventory.setItem(DELETE, ItemFactory.makeItem("&cDelete", "&7Click to remove this Invasion.\n\n  &cWARNING: this action is\n  &cirreversible and there is\n  &cno confirmation prompt.", Material.BARRIER));
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getRawSlot() == BACK) new InvasionListGUI(player).open();
        else if(e.getRawSlot() == VISUAL && e.getAction() == InventoryAction.PICKUP_ALL) new AnswerRunnable("new name", this, 15, invasion::setName);
        else if(e.getRawSlot() == VISUAL && e.getAction() == InventoryAction.PICKUP_HALF) {
            int index = Arrays.stream(Invasion.Skin.values()).collect(Collectors.toList()).indexOf(invasion.getSkin());
            invasion.setSkin(Invasion.Skin.values()[index+1 >= Invasion.Skin.values().length ? 0 : index+1]);
        }else if(e.getRawSlot() == LOCATIONS) new LocationEditorGUI(player, invasion).open();
        else if(e.getRawSlot() == MOBS) new MobListGUI(player, invasion).open();
        else if(e.getRawSlot() == COMMANDS && e.getAction() == InventoryAction.PICKUP_ALL) new AnswerRunnable("open command", this, 30, invasion::setOpenCommand);
        else if(e.getRawSlot() == COMMANDS && e.getAction() == InventoryAction.PICKUP_HALF) new AnswerRunnable("close command", this, 30, invasion::setCloseCommand);
        else if(e.getRawSlot() == DELETE){
            Invasion.invasions.remove(invasion);
            new InvasionListGUI(player).open();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
