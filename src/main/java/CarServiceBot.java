import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarServiceBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final UpdateHandler updateHandler;

    // Пул потоков для асинхронной обработки запросов пользователей
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public CarServiceBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.updateHandler = new UpdateHandler();
    }

    @Override
    public String getBotUsername() { return botUsername; }

    @Override
    public String getBotToken() { return botToken; }

    @Override
    public void onUpdateReceived(Update update) {
        // обернул логику в executorService.submit, чтобы каждый запрос обрабатывался в отдельном потоке
        executorService.submit(() -> {
            try {
                if (update.hasMessage() && update.getMessage().hasText()) {
                    SendMessage message = updateHandler.handleMessage(update);
                    if (message != null) {
                        execute(message);
                    }
                } else if (update.hasCallbackQuery()) {
                    EditMessageText editMessage = updateHandler.handleCallback(update);
                    if (editMessage != null) {
                        execute(editMessage);
                    }
                }
            } catch (TelegramApiException e) {
                System.err.println("Ошибка при отправке сообщения: " + e.getMessage());
            }
        });
    }
}
