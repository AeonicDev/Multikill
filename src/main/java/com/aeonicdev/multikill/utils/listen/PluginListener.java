package com.aeonicdev.multikill.utils.listen;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Defines the API for a Bukkit plugin listener.
 * @author sc4re
 * @param <T> The type of the JavaPlugin used.
 */
public class PluginListener<T extends JavaPlugin> implements Listener {

    /** The plugin used by this listener **/
    protected final T plugin;

    public PluginListener(T plugin) {
        Validate.notNull(plugin, "Plugin cannot be null!");
        Validate.isTrue(plugin.isEnabled(), "Plugin must be enabled before a listener is registered.");
        this.plugin = plugin;
    }

    /**
     * Easy meethod to register this listener.
     */
    public final void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Easy method to unregister this listener.
     */
    public final void unregister() {
        HandlerList.unregisterAll(this);
    }

    /**
     * The plugin used by this listener
     * @return plugin
     */
    public T getPlugin() {
        return plugin;
    }
}
