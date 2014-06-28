package com.aeonicdev.multikill;

import com.aeonicdev.multikill.event.MultikillConfigurationEvent;
import com.aeonicdev.multikill.listeners.KillListener;
import com.aeonicdev.multikill.utils.listen.ListenerHolder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * The main JavaPlugin instance used by Bukkit.
 * Contains logic for initialization of plugin listeners and configuration loading.
 * @author sc4re
 */
public class Multikill extends JavaPlugin {

    /** The static instance of this plugin **/
    private static Multikill instance;
    /** The ListenerHolder that manages all of our listeners for us. How nice :^) **/
    protected final ListenerHolder<Multikill> listeners;
    /** The location of the config file on disc. **/
    protected final File configFile;

    public Multikill() {
        instance = this;
        listeners = new ListenerHolder<>();
        listeners.addListener(new KillListener(this));
        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdir();
        configFile = new File(this.getDataFolder(), "config.yml");
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        listeners.register();

        try {
            this.getConfig().load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().callEvent(new MultikillConfigurationEvent((YamlConfiguration)this.getConfig()));
    }

    @Override
    public void onDisable() {
        listeners.unregister();
    }
}
