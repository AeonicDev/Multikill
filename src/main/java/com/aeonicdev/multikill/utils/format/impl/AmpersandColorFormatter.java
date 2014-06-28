package com.aeonicdev.multikill.utils.format.impl;

import com.aeonicdev.multikill.utils.format.Formatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Colors a message for the Minecraft colors, replacing correctly formatted
 * ampersands with section symbols.
 * @author sc4re
 */
public class AmpersandColorFormatter implements Formatter {

    /** The section symbol **/
    public static final String SECTION_SYMBOL = "§";

    /** The pattern to use when matching the ampersands in the message **/
    public static final Pattern SECTION_PATTERN = Pattern.compile("(?i)(&)[0-9A-FK-OR]");

    @Override
    public String format(String message) {
        Matcher m = SECTION_PATTERN.matcher(message);
        return m.replaceAll(SECTION_SYMBOL);
    }
}
