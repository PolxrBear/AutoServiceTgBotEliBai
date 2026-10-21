import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class UpdateHandler {

    // Метод, который принимает апдейт и решает, что с ним делать
    public SendMessage handleMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (text.equals("/start")) {
                return createMainMenuMessage(chatId);
            }
        }
        return null;
    }

    // Обработка нажатий на инлайн-кнопки (возвращает EditMessageText)
    public EditMessageText handleCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();

        switch (callbackData) {
            case "catalog":
                return createCatalogEdit(chatId, messageId);
            case "balance":
                return createBalanceEdit(chatId, messageId);
            case "back":
                return createMainMenuEdit(chatId, messageId);
            default:
                return null;
        }
    }

    private SendMessage createMainMenuMessage(long chatId) {
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(Messages.get("main.title"))
                .parseMode("Markdown")
                .replyMarkup(createMainMenuKeyboard())
                .build();
    }

    private EditMessageText createMainMenuEdit(long chatId, int messageId) {
        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(Messages.get("main.title"))
                .parseMode("Markdown")
                .replyMarkup(createMainMenuKeyboard())
                .build();
    }

    private EditMessageText createCatalogEdit(long chatId, int messageId) {
        StringBuilder sb = new StringBuilder(Messages.getCatalogItem("catalog.title")).append("\n");

        for (int i = 1; i <= 13; i++) {
            String item = Messages.getCatalogItem("item." + i);
            if (!item.isEmpty()) {
                sb.append(item).append("\n\n");
            }
        }

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(sb.toString().trim())
                .parseMode("Markdown")
                .replyMarkup(createBackKeyboard())
                .build();
    }

    private EditMessageText createBalanceEdit(long chatId, int messageId) {
        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(Messages.get("balance.text"))
                .parseMode("Markdown")
                .replyMarkup(createBackKeyboard())
                .build();
    }

    private InlineKeyboardMarkup createMainMenuKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(InlineKeyboardButton.builder().text(Messages.get("main.btn.catalog")).callbackData("catalog").build()));
        rows.add(List.of(InlineKeyboardButton.builder().text(Messages.get("main.btn.balance")).callbackData("balance").build()));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    private InlineKeyboardMarkup createBackKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(InlineKeyboardButton.builder().text(Messages.get("main.btn.back")).callbackData("back").build()));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }
}
