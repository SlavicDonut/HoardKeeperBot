package com.donut.hoardkeeper.config;

import jakarta.annotation.PreDestroy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
        JDABuilder builder = JDABuilder.createDefault(botToken);

        for (ListenerAdapter listener : listeners) {
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
