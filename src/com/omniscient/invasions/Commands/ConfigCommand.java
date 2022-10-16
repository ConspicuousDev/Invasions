package com.omniscient.invasions.Commands;

import com.omniscient.invasions.Config.InvasionListGUI;
import com.omniscient.omnicore.Commands.OmniCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConfigCommand extends OmniCommand {
    public ConfigCommand() {
        super("invasionsconfig", true);
    }

    @Override
    public void execute(CommandSender sender, Map<String, Object> parameters) {
        new InvasionListGUI((Player) sender).open();
    }
}
