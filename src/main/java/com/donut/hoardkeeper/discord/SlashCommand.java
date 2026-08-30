package com.donut.hoardkeeper.discord;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommand {
    CommandData getCommandData();
}
