package br.com.webbudget.infrastructure.i18n;

import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.*;

/**
 * Implementation of a {@link ResourceBundle} holder supporting multiples bundles
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 01/09/2019
 */
public final class MultiResourceBundle {

    private final Map<String, String> combined;

    /**
     * Constructor...
     *
     * @param bundles the bundles to be loaded
     */
    private MultiResourceBundle(String... bundles) {
        this.combined = new HashMap<>(64);
        this.load(bundles);
    }

    /**
     * Static factory method to get instances of this class
     *
     * @param bundles the bundles to be loaded
     */
    public static MultiResourceBundle combine(String... bundles) {
        return new MultiResourceBundle(bundles);
    }

    /**
     * Load the bundles by the name
     *
     * @param bundles the bundles to load
     */
    private void load(String[] bundles) {

        final FacesContext facesContext = FacesContext.getCurrentInstance();

        for (String bundle : bundles) {

            final Locale locale;

            if (facesContext != null) {
                locale = facesContext.getApplication().getDefaultLocale();
            } else {
                locale = Locale.getDefault();
            }

            final ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle, locale);

            resourceBundle.getKeys()
                    .asIterator()
                    .forEachRemaining(key -> this.combined.put(key, resourceBundle.getString(key)));
        }
    }

    /**
     * Use this method to get a single message by his key
     *
     * @param key the key
     * @return the message
     *
     * @throws MissingResourceException if the key lead to no value
     */
    public String get(String key) {
        if (this.combined.containsKey(key)) {
            return this.combined.get(key);
        }
        throw new MissingResourceException("Key not found", MultiResourceBundle.class.getName(), key);
    }

    /**
     * Same as {@link #get(String)} but this one permits you to pass parameters and format the resulting message
     *
     * @param key the key
     * @param parameters the parameters to format the message
     * @return the message
     *
     * @throws MissingResourceException if the key lead to no value
     */
    public String get(String key, Object... parameters) {
        return MessageFormat.format(this.get(key), parameters);
    }
}
