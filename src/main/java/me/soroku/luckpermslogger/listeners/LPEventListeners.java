package me.soroku.luckpermslogger.listeners;


import me.soroku.luckpermslogger.LuckPermsLogger;
import me.soroku.luckpermslogger.config.FileConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.log.LogBroadcastEvent;
import net.soroku.dcvp.api.discord.BotInstance;
import net.soroku.dcvp.api.interfaces.DiscordManager;
import org.slf4j.Logger;

import java.awt.*;


public class LPEventListeners {
    private final LuckPermsLogger plugin;
    private final Logger logger;
    private final DiscordManager mng;


    public LPEventListeners(LuckPermsLogger plugin, Logger logger, LuckPerms luckPerms){
        this.plugin = plugin;
        this.logger = logger;
        this.mng = plugin.getManager();
        EventBus eventBus = luckPerms.getEventBus();

        eventBus.subscribe(this.plugin, LogBroadcastEvent.class, this::onLogReceive);
    }


    private void onLogReceive(LogBroadcastEvent event) {
        FileConfiguration config = plugin.getConfig();

        if (!config.getBoolean("enabled"))
            return;

        BotInstance bot = mng.getBotByName(config.getString("discord.bot_name"));
        if (bot != null) {
            long channelId = config.getLong("discord.channel_id");
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("LuckPerms Logger");
            eb.setColor(Color.GREEN);
            eb.setDescription(event.getEntry().getDescription());
            eb.addField("Timestamp", "<t:" + event.getEntry().getTimestamp().getEpochSecond() + ">", true);
            eb.addField("Source", event.getEntry().getSource().getName(), true);
            eb.addField("Target", event.getEntry().getTarget().getName(), true);
            bot.sendMessageToChannel(channelId, eb);
        } else {
            logger.info("Bot " + config.getString("discord.bot_name") + " not found.");
        }
    }
}
