package com.donut.hoardkeeper.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Component
public class PingCommandListener extends ListenerAdapter implements SlashCommand {

    @Override
    public CommandData getCommandData() {
        return Commands.slash("ping", "Sprawdź opóźnienie");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        long gatewayPing;
        if (!event.getName().equals("ping")) {return;}

        gatewayPing = event.getJDA().getGatewayPing();
        event.reply("Pong! (%dms)".formatted(gatewayPing)).queue();

    }
}
