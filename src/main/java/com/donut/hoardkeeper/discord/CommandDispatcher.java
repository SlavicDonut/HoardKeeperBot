package com.donut.hoardkeeper.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommandDispatcher extends ListenerAdapter {

    private final Map<String, SlashCommand> commands;

    public CommandDispatcher(List<SlashCommand> slashCommands) {
        this.commands = slashCommands.stream()
                .collect(Collectors.toMap(
                        cmd -> cmd.getCommandData().getName(),
                        Function.identity()
                ));
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        SlashCommand slashCommand = commands.get(event.getName());
        if (slashCommand != null) {
            slashCommand.execute(event);
        }
        else {
            event.reply("Nie rozpoznano komendy").setEphemeral(true).queue();
        }
    }
}
