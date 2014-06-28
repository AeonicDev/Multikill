package com.aeonicdev.multikill.utils.format;

import com.aeonicdev.multikill.utils.format.impl.AmpersandColorFormatter;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a way to format strings in various ways, contains a String->Formatter
 * dictionary
 * @author sc4re
 */
public final class StringStyler {

    /** The formatter objects used by this styler. **/
    protected final Map<Class<? extends Formatter>, Formatter> formats = new HashMap<>();

    /**
     * Private because this is a static instance.
     */
    private StringStyler() {
        addFormat(new AmpersandColorFormatter());
    }

    /**
     * Formats the message given with the Formatter found in the internal dictionary.
     * @param formatName The name of the formatter to use
     * @param message The message to format
     * @return A formatted message
     */
    public String format(Class<? extends Formatter> key, String message) {
        Validate.notNull(key, "Format class cannot be null!");
        Validate.notNull(message, "Message cannot be null!");
        Validate.notEmpty(message, "Message cannot be empty!");
        if (!formats.containsKey(key))
            return message;
        return formats.get(key).format(message);
    }

    /**
     * Adds a formatter
     * @param format The formatter to add
     */
    public void addFormat(Formatter format) {
        Validate.notNull(format, "Formatter cannot be null!");
        Validate.isTrue(!formats.containsKey(format.getClass()), "Formatter cannot already exist!");
        formats.put(format.getClass(), format);
    }

    public void removeFormat(Class<? extends Formatter> key) {
        Validate.notNull(key, "Key cannot be null!");
        formats.remove(key);
    }

    /** The static instance of this styler **/
    private static final StringStyler instance = new StringStyler();

    /**
     * The static accessor for the styler.
     */
    public static final StringStyler get() { return instance; }
}
