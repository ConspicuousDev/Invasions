package com.omniscient.invasions.Commands;

import com.omniscient.invasions.Config.InvasionListGUI;
import com.omniscient.invasions.Invasion.InvasionsGUI;
import com.omniscient.omnicore.Commands.OmniCommand;
import com.omniscient.omnicore.Commands.OmniParameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class InvasionsCommand extends OmniCommand {
    public InvasionsCommand() {
        super("invasions", true);
    }

    @Override
    public void execute(CommandSender sender, Map<String, Object> parameters) {
        new InvasionsGUI((Player) sender).open();
    }
}
