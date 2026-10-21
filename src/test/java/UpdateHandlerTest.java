import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

class UpdateHandlerTest {

    private UpdateHandler updateHandler;

    @BeforeEach
    void setUp() {
        updateHandler = new UpdateHandler();
    }

    @Test
    @DisplayName("start command test")
    void testHandleStartCommand() {
        Chat chat = new Chat();
        chat.setId(1L); // Указываем 1 с буквой L

        Message message = new Message();
        message.setChat(chat);
        message.setText("/start");

        Update update = new Update();
        update.setMessage(message);

        SendMessage resp = updateHandler.handleMessage(update);

        assertNotNull(resp, "ответ пустой");
        assertEquals("1", resp.getChatId(), "айди чата не сошлся"); // Ожидаем "1" в строке
        assertTrue(resp.getText().contains("Добро пожаловать в автосервис"), "нет текста приветствия");
        assertNotNull(resp.getReplyMarkup(), "клавы нет");
    }

    @Test
    @DisplayName("catalog callback test")
    void testHandleCatalogCallback() {
        Chat chat = new Chat();
        chat.setId(2L);

        Message message = new Message();
        message.setChat(chat);
        message.setMessageId(1);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setData("catalog");
        callbackQuery.setMessage(message);

        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        EditMessageText resp = updateHandler.handleCallback(update);

        assertNotNull(resp, "ответ null");
        assertEquals("2", resp.getChatId());
        assertEquals(1, resp.getMessageId());
        assertTrue(resp.getText().contains("Каталог услуг"), "не тот текст каталога");
    }

    @Test
    @DisplayName("balance callback test")
    void testHandleBalanceCallback() {
        Chat chat = new Chat();
        chat.setId(3L);

        Message message = new Message();
        message.setChat(chat);
        message.setMessageId(2);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setData("balance");
        callbackQuery.setMessage(message);

        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        EditMessageText resp = updateHandler.handleCallback(update);

        assertNotNull(resp, "ответ пустой");
        assertEquals("3", resp.getChatId());
        assertEquals(2, resp.getMessageId());
        assertTrue(resp.getText().contains("Ваш профиль и баланс"), "текст баланса не найден");
    }
}
