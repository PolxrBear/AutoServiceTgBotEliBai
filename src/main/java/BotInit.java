import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotInit {

    public static void start() throws BotConfigException, BotRunException {
        Properties config = loadConfig();

        String username = config.getProperty("bot.username");
        String token = config.getProperty("bot.token");

        validateConfig(username, token);

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new CarServiceBot(username, token));
            System.out.println("✅ Бот @" + username + " успешно запущен и работает!");
        } catch (TelegramApiException e) {
            throw new BotRunException("Не удалось зарегистрировать бота в Telegram API", e);
        }
    }

    private static Properties loadConfig() throws BotConfigException {
        Properties config = new Properties();
        try (InputStream input = BotInit.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new BotConfigException("Файл config.properties не найден в classpath");
            }
            config.load(input);
        } catch (IOException e) {
            throw new BotConfigException("Не удалось прочитать config.properties", e);
        }
        return config;
    }

    private static void validateConfig(String username, String token) throws BotConfigException {
        if (username == null || username.isBlank()) {
            throw new BotConfigException("Имя бота не указано в конфиге");
        }
        if (token == null || token.isBlank()) {
            throw new BotConfigException("Токен бота не указан в конфиге");
        }
    }
}