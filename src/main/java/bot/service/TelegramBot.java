package bot.service;

import bot.config.BotConfig;
import bot.questionnaire.ButtonsManager;
import bot.questionnaire.KeyboardManager;
import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private static final String DEF_MESSAGE = "Выберите пункт из меню";
    private static final String HELP_MESSAGE = "Для просмотра справочной информации нажмите /help";
    private static final String HELP_TEXT =
            "/start - команда для начала работы с ботом из основного меню\n\n" +
                    "Иерархия расположения информации в боте:\n\n" +
                    "<b>- Инструкции\n</b>" +
                    "\t\t\t\t- Рабочие инструкции\n" +
                    "\t\t\t\t\t\t\t\t- П-7.1-048-УИСО\n" +
                    "\t\t\t\t\t\t\t\t- П-7.1-132-УИСО\n" +
                    "\t\t\t\t\t\t\t\t- П-7.1-255-УИСО\n" +
                    "\t\t\t\t\t\t\t\t- П-7.1-263-УИСО\n" +
                    "\t\t\t\t\t\t\t\t- РИ-7.1-054-УИСО\n" +
                    "\t\t\t\t\t\t\t\t- РИ-312\n" +
                    "\t\t\t\t- Сеть\n" +
                    "\t\t\t\t\t\t\t\t- Port Security\n" +
                    "\t\t\t\t\t\t\t\t- Настройка ELTEX\n" +
                    "\t\t\t\t- АРМ\n" +
                    "\t\t\t\t\t\t\t\t- Гиперссылки в excel\n" +
                    "\t\t\t\t\t\t\t\t- Просмотрoщик фото\n" +
                    "\t\t\t\t- Серверы\n" +
                    "\t\t\t\t\t\t\t\t- ANSIBLE PLAYBOOK EXEC DEFAULT TIMEOUT\n" +
                    "\t\t\t\t\t\t\t\t- Вывод ip адресов на Nginx\n" +
                    "\t\t\t\t\t\t\t\t- Закрытие файла в samba\n" +
                    "\t\t\t\t\t\t\t\t- Изменение прав на Disk N\n" +
                    "\t\t\t\t\t\t\t\t- Использовать хранилище Alfresco\n" +
                    "\t\t\t\t\t\t\t\t- Восстановление базы данных SQL Server\n" +
                    "\t\t\t\t\t\t\t\t- Настройка прокси-сервера\n" +
                    "\t\t\t\t- ВКС/АКС\n" +
                    "\t\t\t\t\t\t\t\t- Данные по ВКС\n" +
                    "\t\t\t\t\t\t\t\t- Контакты\n" +
                    "\t\t\t\t\t\t\t\t- АКС\n" +
                    "\t\t\t\t\t\t\t\t- Инструкция Eyealink\n" +
                    "<b>- Контакты\n</b>" +
                    "\t\t\t\t- Провайдеры\n" +
                    "\t\t\t\t- Список сотрудников в ТУ, ответственных за IT\n" +
                    "<b>- Отпуска\n</b>" +
                    "<b>- Шаблоны заявлений\n</b>" +
                    "<b>- Командировки\n</b>" +
                    "<b>- Бюджет\n</b>" +
                    "\t\t\t\t- Бюджет IT\n" +
                    "\t\t\t\t- Бюджет связь\n" +
                    "\t\t\t\t\t\t\t\t- Оборудование\n" +
                    "\t\t\t\t\t\t\t\t- Услуги\n" +
                    "\t\t\t\t- Бюджет ИПСС\n";
    private final BotConfig config;
    private final KeyboardManager keyboardManager;

    /*
     * Ключ = chatID
     * Значение = уровень, на котором находится пользователь (см. enum Layer)
     */
    Map<Long, Layer> usersLayers = new HashMap<>();

    /*
     * Ключ = chatID
     *
     * Значение = null или boolean (null/true/false)
     * ---- null = пользователь еще не общался с ботом
     * ---- false = пользователю было предложено авторизоваться, но он еще не авторизован
     * ---- true = пользователю было предложено авторизоваться, авторизация прошла успешно
     */
    Map<Long, Boolean> usersBotAuth = new HashMap<>();

    /*
     * Ключ = chatID
     *
     * Значение = мапа по кнопкам со значением true/false,
     * зависящим от того, ввел пользователь нужный пароль для конкретной кнопки или нет
     */
    Map<Long, Map<Button, Boolean>> usersSecretButtonsAuth = new HashMap<>();

    //Создание меню бота
    @PostConstruct
    private void createMenu() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "старт из основного меню бота"));
        commands.add(new BotCommand("/help", "справочная информация"));

        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error occurred while set commands list (menu): " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    //Метод срабатывает каждый раз, когда пользователь отправляет что-либо боту
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            var messageText = message.getText();
            var chatId = message.getChatId();
            var firstName = message.getChat().getFirstName();

            if (usersBotAuth.get(chatId) == null) {
                sendAuthMessage(chatId);
                return;
            } else if (!usersBotAuth.get(chatId)) {
                if (messageText.equals(config.getPassword()) && usersLayers.get(chatId) == null) {
                    usersBotAuth.put(chatId, true);
                    usersSecretButtonsAuth.put(chatId, new HashMap<>());
                    sendMessage(getMessage(chatId, "Авторизация прошла успешно"));
                    messageText = "/start";
                } else {
                    sendMessage(getMessage(chatId, "Неверный пароль"));
                    return;
                }
            }

            switch (messageText) {
                case "/start" -> {
                    startCommandReceived(chatId, firstName);
                    usersLayers.put(chatId, Layer.MAIN);
                }
                case "/help" -> {
                    SendMessage mess = getMessage(chatId, HELP_TEXT);
                    mess.enableHtml(true);
                    sendMessage(mess);
                }
                default -> {
                    if (usersLayers.get(chatId) == null) {
                        //Если пользователь еще не писал боту ранее -- устанавливаем его на начальный уровень (MAIN)
                        //Это нужно также для сброса уровня при перезапуске бота
                        usersLayers.put(chatId, Layer.MAIN);
                        startCommandReceived(chatId, firstName);
                    } else if (usersLayers.get(chatId).equals(Layer.MAIN)) {
                        //Если пользователь находится на уровне MAIN -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getMainButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.INSTRUCTIONS)) {
                        //Если пользователь находится на уровне INSTRUCTIONS -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getInstructionButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.NET)) {
                        //Если пользователь находится на уровне NET -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getNetButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.WORK_INSTRUCTIONS)) {
                        ///Если пользователь находится на уровне WORK_INSTRUCTIONS -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getWorkInstructionButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.BUDGET)) {
                        ///Если пользователь находится на уровне BUDGET -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getBudgetButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.CONNECTION_BUDGET)) {
                        ///Если пользователь находится на уровне CONNECTION_BUDGET -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getConnectionBudgetButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.SERVERS)) {
                        //Если пользователь находится на уровне SERVERS -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getServersButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.APM)) {
                        //Если пользователь находится на уровне APM -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getAPMButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.KC)) {
                        //Если пользователь находится на уровне KC -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getKCButtons(), messageText);
                    } else if (usersLayers.get(chatId).equals(Layer.CONTACTS)) {
                        //Если пользователь находится на уровне CONTACTS -- проверяем кнопки данного уровня
                        identifyButton(chatId, ButtonsManager.getContactsButtons(), messageText);
                    }
                }
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        var answer = "Здравствуйте, " + name + "! Выберите необходимую информацию из основного меню.\n\n" +
                "Чтобы просмотреть справочную информацию или иерархию расположения информации в боте нажмите /help";
        SendMessage message = getMessage(chatId, answer);
        keyboardManager.bindMainButtons(message);
        sendMessage(message);

        log.info("Replied to user: " + name);
    }

    private void sendAuthMessage(long chatId) {
        sendMessage(getMessage(chatId, "Чтобы получить доступ к боту, необходимо авторизоваться"));
        sendMessage(getMessage(chatId, "Введите пароль"));
        usersBotAuth.put(chatId, false);
    }

    private void startMainMenu(long chatId) {
        SendMessage message = getMessage(chatId, DEF_MESSAGE + "\n\n" + HELP_MESSAGE);
        keyboardManager.bindMainButtons(message);
        sendMessage(message);
    }

    private SendMessage getMessage(long chatId, String textToSend) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("Warning occurred: " + e.getMessage());
        }
    }

    //Идетификация нажатой пользователем кнопки и ее исполнение
    private void identifyButton(Long chatId, List<? extends Button> buttons, String messageText) {
        //выполнение кнопки, если был запрос на кнопку
        if (!tryToExecuteButtonIfMessageTextEqualsButtonName(chatId, buttons, messageText)) {
            checkPassword(chatId, buttons, messageText);
        }
    }

    public boolean tryToExecuteButtonIfMessageTextEqualsButtonName(Long chatId, List<? extends Button> buttons, String messageText) {
        for (Button button : buttons) {
            if (messageText.equals(button.getText())) {
                if (button.isSecret()) {
                    //если кнопка isSecret, то проверка на правильный ввод пароля для кнопки
                    if (usersSecretButtonsAuth.get(chatId).get(button) != null && usersSecretButtonsAuth.get(chatId).get(button)) {
                        //если был введен верный пароль, то исполняем кнопку
                        executeButton(chatId, button);
                    } else {
                        //если пароль для кнопки был введен неверно или кнопка запрашивается в первый раз
                        //отправляем сообщение с запросом пароля
                        askButtonPassword(chatId);
                        usersSecretButtonsAuth.get(chatId).put(button, false);
                    }
                } else {
                    //если кнопка не isSecret, просто исполняем ее
                    executeButton(chatId, button);
                }
                return true;
            }
        }
        return false;
    }

    public void checkPassword(Long chatId, List<? extends Button> buttons, String messageText) {
        //проверка пароля по кнопкам
        for (Button button : buttons) {
            if (button.isSecret() && messageText.equals(button.getPassword()) && !usersSecretButtonsAuth.get(chatId).get(button)) {
                usersSecretButtonsAuth.get(chatId).put(button, true);
                executeButton(chatId, button);
            } else if (button.isSecret() && !messageText.equals(button.getPassword()) && !usersSecretButtonsAuth.get(chatId).get(button)) {
                sendMessage(getMessage(chatId, "Неверный пароль"));
            }
        }
    }

    //Исполнение кнопки в зависимости от ее типа действия (см. enum EventType)
    private void executeButton(Long chatId, Button button) {
        if (button.getEventType().equals(EventType.MESSAGE)) { //Действие = отправка сообщения
            for (Map.Entry<String, SourceType> entry : button.getSource().entrySet()) {
                if (entry.getValue().equals(SourceType.TEXT)) {
                    //Если ресурс текст -- вызываем метод sendText
                    sendText(chatId, entry.getKey());
                } else if (entry.getValue().equals(SourceType.FILE)) {
                    //Если ресурс файл -- вызываем метод sendDocument
                    sendDocument(chatId, entry.getKey());
                } else if (entry.getValue().equals(SourceType.PICTURE)) {
                    //Если ресурс картинка -- вызываем метод sendPhoto
                    sendPhoto(chatId, entry.getKey());
                }
            }
        } else if (button.getEventType().equals(EventType.NEXT)) { //Действие = переход на следующий уровень
            //Отображение пользователю кнопок следующего уровня
            sendButtons(chatId, button);
        } else if (button.getEventType().equals(EventType.BACK)) { //Действие = переход на предыдущий уровень
            if (button.getLayer().equals(Layer.INSTRUCTIONS) || button.getLayer().equals(Layer.CONTACTS)
                    || button.getLayer().equals(Layer.BUDGET)) {
                startMainMenu(chatId);
                usersLayers.put(chatId, Layer.MAIN);
            } else if (button.getLayer().equals(Layer.SERVERS) || button.getLayer().equals(Layer.NET) ||
                    button.getLayer().equals(Layer.APM) || button.getLayer().equals(Layer.KC)
                    || button.getLayer().equals(Layer.WORK_INSTRUCTIONS)) {
                giveInstructionButtons(chatId);
                usersLayers.put(chatId, Layer.INSTRUCTIONS);
            } else if (button.getLayer().equals(Layer.CONNECTION_BUDGET)) {
                giveBudgetButtons(chatId);
                usersLayers.put(chatId, Layer.BUDGET);
            }
        }
    }

    private void askButtonPassword(long chatId) {
        sendMessage(getMessage(chatId, "Чтобы получить доступ к данной информации, введите пароль"));
    }

    private void sendDocument(SendDocument document) throws TelegramApiException {
        try {
            execute(document);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage() + e.getClass());
            throw new TelegramApiException("Файл не найден");
        }
    }

    private void sendPhoto(SendPhoto photo) {
        try {
            execute(photo);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendPhoto(Long chatId, String path) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(new File(path)));
        sendPhoto.setChatId(chatId);
        sendPhoto(sendPhoto);
    }

    private void sendDocument(Long chatId, String path) {
        var document = new SendDocument(String.valueOf(chatId), new InputFile(new File(path)));
        try {
            sendDocument(document);
        } catch (TelegramApiException e) {
            sendMessage(getMessage(chatId, e.getMessage()));
        }
    }

    private void sendText(Long chatId, String path) {
        File file = new File(path);
        String text;
        try {
            Scanner scanner = new Scanner(file);
            text = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (FileNotFoundException e) {
            text = "Файл не найден";
        }
        SendMessage message = getMessage(chatId, text);
        message.enableHtml(true);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня INSTRUCTIONS
    private void giveInstructionButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindInstructionButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня CONTACTS
    private void giveContactsButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindContactsButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня NET
    private void giveNetButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindNetButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня APM
    private void giveAPMButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindAPMButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня SERVERS
    private void giveServersButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindServersButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня KC
    private void giveKCButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindKCButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня WORK_INSTRUCTIONS
    private void giveWorkInstructionButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindWorkInstructionButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня BUDGET
    private void giveBudgetButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindBudgetButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам уровня CONNECTION_BUDGET
    private void giveConnectionBudgetButtons(Long chatId) {
        var message = getMessage(chatId, DEF_MESSAGE);
        keyboardManager.bindConnectionBudgetButtons(message);
        sendMessage(message);
    }

    //Отправка сообщения с привязкой к кнопкам следующего уровня (текущий уровень = уровень кнопки button)
    //Также осуществляется смена уровня пользователя через мапу usersLayers
    private void sendButtons(Long chatId, Button button) {
        if (button.getLayer().equals(Layer.MAIN)) {
            switch (button.getText()) {
                case "Инструкции" -> {
                    giveInstructionButtons(chatId);
                    usersLayers.put(chatId, Layer.INSTRUCTIONS);
                }
                case "Контакты" -> {
                    giveContactsButtons(chatId);
                    usersLayers.put(chatId, Layer.CONTACTS);
                }
                case "Бюджет" -> {
                    giveBudgetButtons(chatId);
                    usersLayers.put(chatId, Layer.BUDGET);
                }
            }
        } else if (button.getLayer().equals(Layer.INSTRUCTIONS)) {
            switch (button.getText()) {
                case "Сеть" -> {
                    giveNetButtons(chatId);
                    usersLayers.put(chatId, Layer.NET);
                }
                case "Рабочие инструкции" -> {
                    giveWorkInstructionButtons(chatId);
                    usersLayers.put(chatId, Layer.WORK_INSTRUCTIONS);
                }
                case "АРМ" -> {
                    giveAPMButtons(chatId);
                    usersLayers.put(chatId, Layer.APM);
                }
                case "Серверы" -> {
                    giveServersButtons(chatId);
                    usersLayers.put(chatId, Layer.SERVERS);
                }
                case "ВКС/АКС" -> {
                    giveKCButtons(chatId);
                    usersLayers.put(chatId, Layer.KC);
                }
            }
        } else if (button.getLayer().equals(Layer.BUDGET)) {
            switch (button.getText()) {
                case "Бюджет связь" -> {
                    giveConnectionBudgetButtons(chatId);
                    usersLayers.put(chatId, Layer.CONNECTION_BUDGET);
                }
            }
        }
    }
}
