package me.soroku.luckpermslogger.commands;


import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import me.soroku.luckpermslogger.LuckPermsLogger;
import me.soroku.luckpermslogger.commands.interfaces.ICommand;
import me.soroku.luckpermslogger.config.FileConfiguration;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.soroku.dcvp.api.DiscordAPI;
import net.soroku.dcvp.api.discord.BotInstance;
import net.soroku.dcvp.api.interfaces.DiscordManager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class MainCommand implements ICommand {
    private final String command = "lplogger";
    private final String[] aliases = {"lpl"};
    private final LuckPermsLogger plugin;
    private final ProxyServer server;

    private final String prefix = "<light_purple><b>LuckPerms Logger</light_purple> <#26ffc9><b>| </#26ffc9>";

    public MainCommand(LuckPermsLogger plugin, ProxyServer server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        FileConfiguration config = plugin.getConfig();

        if(args.length >= 1){
            if("reload".equals(args[0])){
                plugin.reloadConfig();
                source.sendMessage(color("<yellow>Plugin reloaded successfully.</yellow>"));
                return;
            }else if("toggle".equals(args[0])){
                config.setBoolean("enabled", !config.getBoolean("enabled"));
                source.sendMessage(color("<yellow>Plugin " + (config.getBoolean("enabled") ? "<green>enabled</green>" : "<red>disabled</red>") + "</yellow>"));
                return;
            }else if("set".equals(args[0])){
                if(args.length >= 2){
                    if("channel".equals(args[1])){
                        if(args.length == 3){
                            DiscordManager mng = plugin.getManager();
                            BotInstance bot = mng.getBotByName(config.getString("discord.bot_name"));
                            if(bot != null){
                                TextChannel channel = bot.getJda().getTextChannelById(args[2]);
                                if(channel != null){
                                    config.setLong("discord.channel_id", Long.parseLong(args[2]));
                                    source.sendMessage(color("<green>Channel set successfully.</green>"));
                                    return;
                                }else{
                                    source.sendMessage(color("<red>Bot cannot find this channel.</red>"));
                                    return;
                                }
                            }
                            source.sendMessage(color("<red>Invalid bot, please set/configure a new one.</red>"));
                            return;
                        }else{
                            source.sendMessage(color("<red>/lpl set channel <channelId></red>"));
                            return;
                        }
                    }else if("bot".equals(args[1])){
                        if(args.length == 3){
                            DiscordManager mng = plugin.getManager();
                            Map<String, BotInstance> bot = mng.getBots();
                            if(bot.containsKey(args[2])){
                                config.setString("discord.bot_name", args[2]);
                                source.sendMessage(color("<green>Bot set successfully.</green>"));
                                return;
                            }else{
                                source.sendMessage(color("<red>Bot '"+args[2]+" doesn't exist.'.</red>"));
                                return;
                            }
                        }
                    }else{
                        source.sendMessage(color("<red>/lpl set <channel|bot></red>"));
                        return;
                    }
                }
            }

        }
        source.sendMessage(color("<red>Invalid command. Type /lpl help for help.</red>"));
    }


    @Override
    public boolean hasPermission(final Invocation invocation) {


        return invocation.source().hasPermission("luckpermslogger.all");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        DiscordManager mng = plugin.getManager();
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if(args.length >= 1){
            if("set".equals(args[0]) && args.length >= 2){
                if("bot".equals(args[1]) && args.length == 3) {
                    List<String> botNames = new ArrayList<>(mng.getBots().keySet());
                    return CompletableFuture.completedFuture(botNames);
                }
            }

            if(args.length == 2 && "set".equals(args[0]))
                return CompletableFuture.completedFuture(List.of("channel", "bot"));
            return CompletableFuture.completedFuture(List.of());
        }

        return CompletableFuture.completedFuture(List.of("reload", "toggle", "set"));
    }


    @Override
    public String getCommand() {return this.command;}

    @Override
    public String[] getAliases() {
        return this.aliases;
    }

    @Override
    public LuckPermsLogger getPlugin() {
        return plugin;
    }

    public Component color(String msg){
        return MiniMessage.miniMessage().deserialize(prefix + msg);
    }
}