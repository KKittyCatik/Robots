package gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


public class LocaleManager
{
    public interface LocaleChangeListener
    {
        void onLocaleChanged(ResourceBundle bundle);
    }

    private static final LocaleManager INSTANCE = new LocaleManager();

    private ResourceBundle bundle;
    private final List<LocaleChangeListener> listeners = new ArrayList<>();

    private LocaleManager()
    {
        bundle = loadBundle(Locale.of("ru"));
    }

    public static LocaleManager getInstance()
    {
        return INSTANCE;
    }

    public ResourceBundle getBundle()
    {
        return bundle;
    }

    public String getString(String key)
    {
        return bundle.getString(key);
    }

    public void setLocale(Locale locale)
    {
        bundle = loadBundle(locale);
        for (LocaleChangeListener listener : new ArrayList<>(listeners))
            listener.onLocaleChanged(bundle);
    }

    public void addListener(LocaleChangeListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(LocaleChangeListener listener)
    {
        listeners.remove(listener);
    }

    private static ResourceBundle loadBundle(Locale locale)
    {
        String fileName = "/resources/messages_" + locale.getLanguage() + ".properties";
        try (InputStream is = LocaleManager.class.getResourceAsStream(fileName))
        {
            if (is == null)
                throw new RuntimeException("Resource not found: " + fileName);
            return new PropertyResourceBundle(
                new InputStreamReader(is, StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load locale: " + fileName, e);
        }
    }
}