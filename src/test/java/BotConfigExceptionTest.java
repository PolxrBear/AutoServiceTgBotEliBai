import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotConfigExceptionTest {

    @Test
    @DisplayName("Проверка создания и сообщения кастомного исключения конфигурации")
    void testBotConfigExceptionMessage() {
        String errorMessage = "Тестовая ошибка конфигурации";
        BotConfigException exception = new BotConfigException(errorMessage);

        assertEquals(errorMessage, exception.getMessage(), "Сообщение исключения должно совпадать с переданным");
    }

    @Test
    @DisplayName("Проверка исключения с причиной (cause)")
    void testBotConfigExceptionWithCause() {
        Throwable cause = new RuntimeException("Корневая проблема");
        BotConfigException exception = new BotConfigException("Ошибка с причииной", cause);

        assertEquals(cause, exception.getCause(), "Причина исключения должна сохраняться");
    }
}
