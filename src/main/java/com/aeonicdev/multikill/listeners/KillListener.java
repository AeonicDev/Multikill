package com.aeonicdev.multikill.listeners;

import com.aeonicdev.multikill.Multikill;
import com.aeonicdev.multikill.event.MultikillConfigurationEvent;
import com.aeonicdev.multikill.utils.Cooldown;
import com.aeonicdev.multikill.utils.format.StringStyler;
import com.aeonicdev.multikill.utils.format.impl.AmpersandColorFormatter;
import com.aeonicdev.multikill.utils.listen.PluginListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * The actual listener for kills.
 * @author sc4re
 */
public class KillListener extends PluginListener<Multikill> {
    /** The plugin logger to use when logging. **/
    protected final Logger logger;
    /** The cooldown between user kills to check for **/
    protected final Cooldown killCooldown = new Cooldown();
    /** The map of how many kills a player has **/
    protected final Map<UUID, Integer> kills = new HashMap<>();

    /** The prefix of messages broadcasted from Multikill **/
    protected String prefix = "§8[§6Multikill§8]§r§f: ";
    /** Whether we are logging or not **/
    protected boolean logging = true;
    /** The message format of announcing kills **/
    protected String messageFormat = "§9%player§r§f has scored a §4%kill§r§f!";
    /** The maximum time between kills to ascend to higher kill counts **/
    protected long cooldownTime = 1200;
    protected final Map<Integer, String> killMessages = new HashMap<>();


    public KillListener(Multikill plugin) {
        super(plugin);
        logger = plugin.getLogger();
        killMessages.put(2, "Double Kill");
        killMessages.put(3, "Multi Kill");
        killMessages.put(4, "Mega Kill");
        killMessages.put(5, "Ultra Kill");
        killMessages.put(6, "M-M-M-M-MONSTER KILL");
        killMessages.put(7, "Ludicrous Kill");
        killMessages.put(8, "HOLY SHIT");
    }

    @EventHandler(priority = EventPriority.MONITOR) // use MONITOR because we're not changing anything.
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity().getKiller();
        if (p == null)
            return;
        if (killCooldown.checkCoolAndRemove(p.getUniqueId(), cooldownTime)) {
            this.kills.remove(p.getUniqueId());
            return;
        }
        int counter = 1;
        if (!kills.containsKey(p.getUniqueId()))
            kills.put(p.getUniqueId(), counter);
        counter = kills.get(p.getUniqueId());
        if (killMessages.containsKey(counter)) {
            String message = this.messageFormat.replaceAll("%player", p.getDisplayName());
            message = message.replaceAll("%kill", this.killMessages.get(counter));
            Bukkit.broadcastMessage(this.prefix + message);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cleanQuit(PlayerQuitEvent event) {
        killCooldown.removeCooldown(event.getPlayer().getUniqueId());
        kills.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cleanDeath(PlayerDeathEvent event) {
        killCooldown.removeCooldown(event.getEntity().getUniqueId());
        kills.remove(event.getEntity().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cleanKick(PlayerKickEvent event) {
        killCooldown.removeCooldown(event.getPlayer().getUniqueId());
        kills.remove(event.getPlayer().getUniqueId());
    }


    @EventHandler
    public void bigGayConfigurationHandler(MultikillConfigurationEvent event) {
        YamlConfiguration yc = event.getConfig();
        if (!yc.isConfigurationSection("multikill"))
            return;
        ConfigurationSection sc = yc.getConfigurationSection("multikill");
        if (sc.isBoolean("logging"))
            this.logging = sc.getBoolean("logging");
        if (sc.isString("prefix"))
            this.prefix = StringStyler.get().format(AmpersandColorFormatter.class, sc.getString("prefix"));
        if (!sc.isConfigurationSection("behavior")) {
            this.logger.warning("'behavior' section not found in multikill config.yml! Stopping configuration load.");
            return;
        }
        ConfigurationSection bhv = sc.getConfigurationSection("behavior");
        if (bhv.isString("message_format"))
            this.messageFormat = StringStyler.get().format(AmpersandColorFormatter.class, bhv.getString("message_format"));
        if (bhv.isLong("cooldown_time")) {
            long cd = bhv.getLong("cooldown_time");
            if (cd > 0) {
                this.cooldownTime = cd;
            } else {
                if (this.logging)
                    this.logger.warning("Not accepting cooldown_time argument in config.yml, it must be greater than 0!");
            }
        }
        if (!bhv.isConfigurationSection("killMessages")) {
            logger.warning("'killMessages' section not found under multikill.behavior. Ending configuration load.");
            return;
        }
        ConfigurationSection kills = bhv.getConfigurationSection("killMessages");
        for (String s : kills.getKeys(false)) {
            int killNumber = -1;
            try {
                killNumber = Integer.parseInt(s);
                if (killNumber < 1) {
                    logger.warning("Key " + s + " found in 'killMessages' section must be > 0! ");
                    continue;
                }
            } catch (NumberFormatException e) {
                logger.warning("Key " + s + " found in 'killMessages' section is not a valid number!");
                continue;
            }
            if (!kills.isString(s))
                return;
            String kill = kills.getString(s);
            this.killMessages.put(killNumber, kill);
        }
    }
}
