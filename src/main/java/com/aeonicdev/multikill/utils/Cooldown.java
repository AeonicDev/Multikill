package com.aeonicdev.multikill.utils;

import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * A UUID-based cooldown class.
 * @author sc4re
 */
public class Cooldown {

    /** The map of UUID's to cooldown times **/
    protected final Map<UUID, Long> cooldowns = new HashMap<>();

    /**
     * Removes a cooldown from the UUID given.
     * @param id The ID to remove from the cooldown dict.
     */
    public void removeCooldown(UUID id) {
        Validate.notNull(id, "UUID must exist!");
        cooldowns.remove(id);
    }

    /**
     * Sets a cooldown directly by UUID using the Milliseconds time unit
     * @param id The UUID to set the oooldown for.
     * @param milliseconds The amount of milliseconds
     */
    public void setCooldown(UUID id, long milliseconds) {
        setCooldown(id, TimeUnit.MILLISECONDS, milliseconds);
    }

    /**
     * Sets a cooldown internally using the TimeUnit and time provided.
     * @param id The UUID to set the oooldown for.
     * @param unit The unit of time to use for the cooldown.
     * @param time The time to set the cooldown for.
     */
    public void setCooldown(UUID id, TimeUnit unit, long time) {
        Validate.notNull(id, "UUID must exist!");
        Validate.notNull(unit, "Time unit must exist!");
        Validate.isTrue(time > 0, "Cooldown must be above 0!");
    }

    /**
     * Returns whether the UUID provided has cooled down, using Milliseconds as the time unit
     * @param id The ID to check the cooldown of.
     * @param milliseconds The time in milliseconds of the cooldown to check
     * @return cooldown
     */
    public boolean hasCooled(UUID id, long milliseconds) {
        return hasCooled(id, TimeUnit.MILLISECONDS, milliseconds);
    }

    /**
     * Returns whether the UUID provided has cooled down.
     * @param id The ID to check the cooldown of.
     * @param unit The time unit to use in checking the cooldown
     * @param coolTime The cooldown time to check for.
     * @return cooldown
     */
    public boolean hasCooled(UUID id, TimeUnit unit, long coolTime) {
        Validate.notNull(id, "UUID must exist!");
        if (coolTime < 0)
            return true;
        if (!cooldowns.containsKey(id))
            return true;
        return currentMilliseconds() - cooldowns.get(id) >= unit.toMillis(coolTime);
    }

    /**
     * Checks whether the UUID provided has cooled down, and if so, removes it from the cooldowns, using
     * Milliseconds as the time unit.
     * @param id The ID to check the cooldown of.
     * @param ms The milliseconds of the cooldown.
     * @return whether the UUID has cooled down
     */
    public boolean checkCoolAndRemove(UUID id, long ms) {
        return checkCoolAndRemove(id, TimeUnit.MILLISECONDS, ms);
    }

    /**
     * Checks whether the UUID provided has cooled down, and if so, removes it from the cooldowns/
     * @param id The ID to check the cooldown of.
     * @param unit The time unit to use when checking the cooldown.
     * @param cooldown The cooldown to check.
     * @return whether the UUID has cooled down
     */
    public boolean checkCoolAndRemove(UUID id, TimeUnit unit, long cooldown) {
        boolean check = hasCooled(id, unit, cooldown);
        if (check) {
            removeCooldown(id);
        }
        return check;
    }

    /**
     * Returns the current milliseconds.
     * @return current milliseconds
     */
    private long currentMilliseconds() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }


}
