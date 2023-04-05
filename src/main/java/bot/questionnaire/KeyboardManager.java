package bot.questionnaire;

import bot.questionnaire.buttons.Button;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

//Класс, отвечающий за привязку кнопок определенного уровня к сообщению
@Component
@RequiredArgsConstructor
public class KeyboardManager {

    //Создание ReplyKeyboardMarkup на основе экземпляров класса Button
    private ReplyKeyboardMarkup createReplyKeyboardMarkup(List<? extends Button> buttons) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (Button button : buttons) {
            var keyBoardRow = new KeyboardRow();
            keyBoardRow.add(button.getText());
            keyboardRows.add(keyBoardRow);
        }

        return new ReplyKeyboardMarkup(keyboardRows);
    }

    //Привязка списка кнопок (экземпляры класса Button) к сообщению message
    private SendMessage bindInlineKeyboardFromList(SendMessage message, List<? extends Button> buttons) {
        message.setReplyMarkup(createReplyKeyboardMarkup(buttons));
        return message;
    }


    public SendMessage bindMainButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getMainButtons());
    }

    public SendMessage bindWorkInstructionButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getWorkInstructionButtons());
    }

    public SendMessage bindBudgetButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getBudgetButtons());
    }

    public SendMessage bindConnectionBudgetButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getConnectionBudgetButtons());
    }

    public SendMessage bindServersButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getServersButtons());
    }

    public SendMessage bindNetButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getNetButtons());
    }

    public SendMessage bindKCButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getKCButtons());
    }

    public SendMessage bindInstructionButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getInstructionButtons());
    }

    public SendMessage bindAPMButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getAPMButtons());
    }

    public SendMessage bindContactsButtons(SendMessage message) {
        return bindInlineKeyboardFromList(message,
                ButtonsManager.getContactsButtons());
    }
}
