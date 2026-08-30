package com.donut.hoardkeeper.discord;

import com.donut.hoardkeeper.domain.dice.DiceRoll;
import com.donut.hoardkeeper.domain.dice.DiceService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Component
public class RollCommand extends ListenerAdapter implements SlashCommand {

    private final DiceService diceService;

    public RollCommand(DiceService diceService) {
        this.diceService = diceService;
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("roll", "Rzut kośćmi")
                .addOption(OptionType.STRING, "expression", "Wyrażenie rzutu, np. 2d6+3", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping option = event.getOption("expression");
        if (option == null) {
            event.reply("Musisz wpisać rzut w postaci np. '2d6+3'!").setEphemeral(true).queue();
            return;
        }

        String input = option.getAsString();

        try {
            DiceRoll rollResult = diceService.roll(input);

            String response = String.format(
                    "**Rzut:** `%s`\n**Wynik:** %s **Razem:** **`%d`**",
                    rollResult.rawInput(),
                    rollResult.individualRolls(),
                    rollResult.total()
            );

            event.reply(response).queue();
        }
        catch (IllegalArgumentException e) {
            event.reply("Błąd: " + e.getMessage()).setEphemeral(true).queue();
        }
    }


}
