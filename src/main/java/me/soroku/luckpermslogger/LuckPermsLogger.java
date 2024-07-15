package me.soroku.luckpermslogger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import me.soroku.luckpermslogger.commands.MainCommand;
import me.soroku.luckpermslogger.config.FileConfiguration;
import me.soroku.luckpermslogger.listeners.LPEventListeners;
import me.soroku.luckpermslogger.manager.CommandRegistry;
import me.soroku.luckpermslogger.manager.ResourceManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.soroku.dcvp.api.DiscordAPI;
import net.soroku.dcvp.api.interfaces.DiscordManager;
import org.slf4j.Logger;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Optional;

@Plugin(
        id = "luckpermslogger",
        name = "LuckPermsLogger",
        version = "1.0-SNAPSHOT",
        dependencies = {
                @Dependency(id = "dcvelocityplus"),
                @Dependency(id = "luckperms")
        }
)
public class LuckPermsLogger {

    private final ProxyServer server;
    private final Logger logger;
    private final File dataDirectory;
    private final File file;
    private final ResourceManager resourceManager;
    private DiscordManager mng;

    private FileConfiguration config;



    @Inject
    public LuckPermsLogger(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = new File(dataDirectory.toFile().getParentFile(), this.getClass().getAnnotation(Plugin.class).name());
        this.resourceManager = new ResourceManager(this.dataDirectory);


        try {
            this.file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (final URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        reloadConfig();
        logger.info("Successfully enabled.");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        PluginManager pluginManager = server.getPluginManager();
        Optional<PluginContainer> optionalPlugin = pluginManager.getPlugin("dcvelocityplus");

        if (!optionalPlugin.isPresent()) {
            logger.warn("discordvelocityplus plugin is not present.");
            return;
        }

        this.mng = DiscordAPI.getManager();

        LuckPerms luckPerms = LuckPermsProvider.get();
        CommandRegistry.register(server, new MainCommand(this, server));

        server.getEventManager().register(this, new LPEventListeners(this, logger, luckPerms));

    }

    public Logger getLogger() {
        return logger;
    }

    public DiscordManager getManager(){
        return mng;
    }

    public FileConfiguration getConfig(){
        return config;
    }

    public void reloadConfig(){
        this.config = resourceManager.loadConfig("config.yml");
    }
}
