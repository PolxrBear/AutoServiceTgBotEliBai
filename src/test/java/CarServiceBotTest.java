import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceBotTest {

    @Test
    @DisplayName("Проверка асинхронной обработки апдейта в боте")
    void testAsyncUpdateProcessing() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Создаем бота и переопределяем execute, чтобы поймать вызов от фонового потока
        CarServiceBot bot = new CarServiceBot("test_bot", "12345:fake_token") {
            @Override
            public java.io.Serializable execute(org.telegram.telegrambots.meta.api.methods.BotApiMethod method) {
                latch.countDown();
                return null;
            }
        };

        Chat chat = new Chat();
        chat.setId(12345L);

        Message message = new Message();
        message.setChat(chat);
        message.setText("/start");

        Update update = new Update();
        update.setMessage(message);

        // Запускаем асинхронную обработку
        bot.onUpdateReceived(update);

        // Ждем завершения в фоновом потоке
        boolean completedInTime = latch.await(2, TimeUnit.SECONDS);

        assertTrue(completedInTime, "Асинхронная задача не успела выполниться за отведенное время");
    }

    @Test
    @DisplayName("Проверка геттеров токена и юзернейма")
    void testBotGetters() {
        CarServiceBot bot = new CarServiceBot("my_test_bot", "secret_token_123");

        assertEquals("my_test_bot", bot.getBotUsername());
        assertEquals("secret_token_123", bot.getBotToken());
    }
}
