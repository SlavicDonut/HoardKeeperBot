package com.donut.hoardkeeper.config;

import com.donut.hoardkeeper.discord.PingCommandListener;
import com.donut.hoardkeeper.discord.RollCommandListener;
import jakarta.annotation.PreDestroy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JDAConfig {

    @Value("${discord.bot.token}")
    private String botToken;
    private JDA jda;

    @Bean
    public JDA jda(List<ListenerAdapter> listeners) throws InterruptedException {
        System.out.println("=== [DEBUG] Wstrzyknięte listenery do JDA: " + listeners.size());

        JDABuilder builder = JDABuilder.createDefault(botToken);

        for (ListenerAdapter listener : listeners) {
            System.out.println("=== [DEBUG] Rejestruję listener: " + listener.getClass().getSimpleName());
            builder.addEventListeners(listener);
        }

        this.jda = builder.build();
        return this.jda;
    }

    @PreDestroy
    public void shutdownJDA() {
        if (this.jda != null) {
            this.jda.shutdown();
        }
    }
}
