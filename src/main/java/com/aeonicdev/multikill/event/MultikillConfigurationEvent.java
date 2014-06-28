package com.aeonicdev.multikill.event;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event fired when Multikill loads it's configuration.
 * @author sc4re
 */
public final class MultikillConfigurationEvent extends Event {

    /** The Bukkit-required handlerlist **/
    private static final HandlerList handlerList = new HandlerList();

    /** The configuration to send with this event **/
    protected final YamlConfiguration config;

    public MultikillConfigurationEvent(YamlConfiguration config) {
        Validate.notNull(config, "YamlConfiguration cannot be null!");
        this.config = config;
    }

    /**
     * Accessor for the config.
     * @return the config.
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static final HandlerList getHandlerList() {
        return handlerList;
    }
}
