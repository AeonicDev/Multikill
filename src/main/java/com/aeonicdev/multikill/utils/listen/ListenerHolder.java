package com.aeonicdev.multikill.utils.listen;

import org.apache.commons.lang.Validate;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds all of the listeners for a plugin, for easy registration/unregistration/access.
 * @author sc4re
 */
public class ListenerHolder<T extends JavaPlugin> {

    /** The dictionary that holds the listeners **/
    protected final Map<Class<? extends PluginListener>, PluginListener<T>> listeners = new HashMap<>();
    /** Determines if this ListenerHolder has already registered it's listeners **/
    protected boolean registered = false;

    /**
     * A method to add a plugin listener. Returns this instance of the listener holder so a chain can
     * easily be formed. If register() has already been called, this method calls the register() method
     * of the listener to make sure everything we have is set up correctly with no duplicates / unregistered
     * listeners.
     * @param listener The listener to add.
     * @return this instance.
     */
    public void addListener(PluginListener<T> listener) {
        Validate.notNull(listener, "Listener cannot be null.");
        Validate.isTrue(!listeners.containsKey(listener.getClass()), "Listener cannot already exist!");
        listeners.put(listener.getClass(), listener);
        if (registered)
            listener.register();
    }

    public void removeListener(Class<? extends PluginListener> key) {
        Validate.notNull(key, "Key to remove cannot be null!");
        if (!listeners.containsKey(key))
            return;
        if (registered)
            HandlerList.unregisterAll(listeners.get(key));
        listeners.remove(key);
    }

    public boolean isRegistered() {
        return registered;
    }

    public final void register() {
        for (PluginListener<T> listener : this.listeners.values()) {
            listener.register();
        }
        registered = true;
    }

    public final void unregister() {
        for (PluginListener<T> listener : this.listeners.values()) {
            listener.unregister();
        }
        registered = false;
    }
}
