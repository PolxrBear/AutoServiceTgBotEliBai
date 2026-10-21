import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Messages {

    private static class Utf8Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IOException, IllegalAccessException, InstantiationException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                if (stream != null) {
                    try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                        return new PropertyResourceBundle(reader);
                    }
                }
            }
            return null;
        }
    }

    private static final ResourceBundle MAIN    = ResourceBundle.getBundle("messages.messages_main", Locale.ROOT, new Utf8Control());
    private static final ResourceBundle BALANCE = ResourceBundle.getBundle("messages.messages_balance", Locale.ROOT, new Utf8Control());
    private static final ResourceBundle CATALOG = ResourceBundle.getBundle("messages.messages_catalog", Locale.ROOT, new Utf8Control());

    private Messages() {}

    public static String get(String key) {
        if (MAIN.containsKey(key)) return MAIN.getString(key);
        if (BALANCE.containsKey(key)) return BALANCE.getString(key);
        return key;
    }

    public static String getCatalogItem(String key) {
        return CATALOG.containsKey(key) ? CATALOG.getString(key) : "";
    }
}
