package com.donut.hoardkeeper.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandRegistrar extends ListenerAdapter {

    private final List<SlashCommand> slashCommands;

    public CommandRegistrar(List<SlashCommand> slashCommands) {
        this.slashCommands = slashCommands;
    }

    @Override
    public void onReady(@NonNull ReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();

        for (SlashCommand command : slashCommands) {
            commandDataList.add(command.getCommandData());
        }

        event.getJDA().updateCommands().addCommands(commandDataList).queue();
    }
}
