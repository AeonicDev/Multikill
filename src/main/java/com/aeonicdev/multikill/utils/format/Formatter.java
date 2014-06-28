package com.aeonicdev.multikill.utils.format;

/**
 * Defines the API for the actions that can be taken by the StringStyler.
 * @author sc4re
 */
public interface Formatter {
    /**
     * Formats the message given.
     * @param message The message to format.
     * @return The formatted message.
     */
    public String format(String message);
}
