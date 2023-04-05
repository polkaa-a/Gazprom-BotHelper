package bot.questionnaire;

import bot.questionnaire.buttons.budget.BudgetButton;
import bot.questionnaire.buttons.budget.ConnectionBudgetButton;
import bot.questionnaire.buttons.contacts.ContactsButton;
import bot.questionnaire.buttons.instructions.*;
import bot.questionnaire.buttons.main.MainButton;
import bot.service.enums.EventType;
import bot.service.enums.SourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//Класс, отвечающий за предоставление экземпляров кнопок каждого уровня
@Component
@RequiredArgsConstructor
public class ButtonsManager {

    private static final List<MainButton> mainButtons = new ArrayList<>();
    private static final List<InstructionButton> instructionButtons = new ArrayList<>();
    private static final List<WorkInstructionsButton> workInstructionButtons = new ArrayList<>();
    private static final List<BudgetButton> budgetButtons = new ArrayList<>();
    private static final List<ConnectionBudgetButton> connectionBudgetButtons = new ArrayList<>();
    private static final List<NetButton> netButtons = new ArrayList<>();
    private static final List<APMButton> armButtons = new ArrayList<>();
    private static final List<ServersButton> serversButtons = new ArrayList<>();
    private static final List<KCButton> kcButtons = new ArrayList<>();
    private static final List<ContactsButton> contactsButtons = new ArrayList<>();

    //Основной путь к папке, где лежат все файлы для бота
    private static final String mainPath = "C:/Users/user/Desktop/BotFiles/";

    public static List<MainButton> getMainButtons() {
        if (mainButtons.isEmpty()) {
            mainButtons.add(new MainButton("Инструкции", EventType.NEXT, null, false, null));
            mainButtons.add(new MainButton("Контакты", EventType.NEXT, null, false, null));
            mainButtons.add(new MainButton("Отпуска", EventType.MESSAGE,
                    Map.of(mainPath + "vacation/vacations2023.xlsx", SourceType.FILE), false, null));
            mainButtons.add(new MainButton("Шаблоны заявлений", EventType.MESSAGE,
                    Map.of(mainPath + "", SourceType.TEXT), false, null));
            mainButtons.add(new MainButton("Командировки", EventType.MESSAGE,
                    Map.of(mainPath + "", SourceType.FILE), false, null));
            mainButtons.add(new MainButton("Бюджет", EventType.NEXT, null, true, "0000"));
        }
        return mainButtons;
    }

    public static List<BudgetButton> getBudgetButtons() {
        if (budgetButtons.isEmpty()) {
            budgetButtons.add(new BudgetButton("Бюджет IT", EventType.MESSAGE,
                    Map.of(mainPath + "", SourceType.FILE)));
            budgetButtons.add(new BudgetButton("Бюджет связь", EventType.NEXT, null));
            budgetButtons.add(new BudgetButton("Бюджет ИПСС", EventType.MESSAGE,
                    Map.of(mainPath + "", SourceType.FILE)));
            budgetButtons.add(new BudgetButton("Назад", EventType.BACK, null));
            }
        return budgetButtons;
    }

    public static List<ConnectionBudgetButton> getConnectionBudgetButtons() {
        if (connectionBudgetButtons.isEmpty()) {
            connectionBudgetButtons.add(new ConnectionBudgetButton("Оборудование", EventType.MESSAGE,
                    Map.of(mainPath + "", SourceType.FILE)));
            connectionBudgetButtons.add(new ConnectionBudgetButton("Услуги", EventType.MESSAGE,
                    Map.of(mainPath + "", SourceType.FILE)));
            connectionBudgetButtons.add(new ConnectionBudgetButton("Назад", EventType.BACK, null));
        }
        return connectionBudgetButtons;
    }

    public static List<InstructionButton> getInstructionButtons() {
        if (instructionButtons.isEmpty()) {
            instructionButtons.add(new InstructionButton("Рабочие инструкции", EventType.NEXT, null));
            instructionButtons.add(new InstructionButton("Сеть", EventType.NEXT, null));
            instructionButtons.add(new InstructionButton("АРМ", EventType.NEXT, null));
            instructionButtons.add(new InstructionButton("Серверы", EventType.NEXT, null));
            instructionButtons.add(new InstructionButton("ВКС/АКС", EventType.NEXT, null));
            instructionButtons.add(new InstructionButton("Назад", EventType.BACK, null));
        }
        return instructionButtons;
    }

    public static List<WorkInstructionsButton> getWorkInstructionButtons() {
        if (workInstructionButtons.isEmpty()) {
            workInstructionButtons.add(new WorkInstructionsButton("П-7.1-048-УИСО", EventType.MESSAGE,
                    Map.of(mainPath + "instructions/П-7.1-048-УИСО.DOCX", SourceType.FILE)));
            workInstructionButtons.add(new WorkInstructionsButton("П-7.1-132-УИСО", EventType.MESSAGE,
                    Map.of(mainPath + "instructions/П-7.1-132-УИСО.DOCX", SourceType.FILE)));
            workInstructionButtons.add(new WorkInstructionsButton("П-7.1-255-УИСО", EventType.MESSAGE,
                    Map.of(mainPath + "instructions/П-7.1-255-УИСО.DOCX", SourceType.FILE)));
            workInstructionButtons.add(new WorkInstructionsButton("П-7.1-263-УИСО", EventType.MESSAGE,
                    Map.of(mainPath + "instructions/П-7.1-263-УИСО.DOCX", SourceType.FILE)));
            workInstructionButtons.add(new WorkInstructionsButton("РИ-7.1-054-УИСО", EventType.MESSAGE,
                    Map.of(mainPath + "instructions/РИ-7.1-054-УИСО.DOCX", SourceType.FILE)));
            workInstructionButtons.add(new WorkInstructionsButton("РИ-312", EventType.MESSAGE,
                    Map.of(mainPath + "instructions/РИ-312.docx", SourceType.FILE)));
            workInstructionButtons.add(new WorkInstructionsButton("Назад", EventType.BACK, null ));
        }
        return workInstructionButtons;
    }

    public static List<NetButton> getNetButtons() {
        if (netButtons.isEmpty()) {
            netButtons.add(new NetButton("Port Security", EventType.MESSAGE,
                    Map.of(mainPath + "net/portsecurity.txt", SourceType.TEXT)));
            netButtons.add(new NetButton("Настройка ELTEX", EventType.MESSAGE,
                    Map.of(mainPath + "net/...", SourceType.FILE)));
            netButtons.add(new NetButton("Назад", EventType.BACK, null));
        }
        return netButtons;
    }

    public static List<APMButton> getAPMButtons() {
        if (armButtons.isEmpty()) {
            armButtons.add(new APMButton("Гиперссылки в excel", EventType.MESSAGE,
                    Map.of("", SourceType.TEXT)));
            armButtons.add(new APMButton("Просмотрощик фото", EventType.MESSAGE,
                    Map.of(mainPath + "APM/photoviewer.txt", SourceType.TEXT)));
            armButtons.add(new APMButton("Назад", EventType.BACK, null));
        }
        return armButtons;
    }

    public static List<ServersButton> getServersButtons() {
        //if the order of messages is important - put sources into LinkedHashMap
        Map<String, SourceType> proxy = new LinkedHashMap<>();
        proxy.put(mainPath + "servers/proxyBeggining.txt", SourceType.TEXT);
        proxy.put(mainPath + "servers/proxyWindows.txt", SourceType.TEXT);
        proxy.put(mainPath + "servers/proxyLinux.txt", SourceType.TEXT);
        proxy.put(mainPath + "servers/proxyMac.txt", SourceType.TEXT);

        if (serversButtons.isEmpty()) {
            serversButtons.add(new ServersButton("ANSIBLE PLAYBOOK EXEC DEFAULT TIMEOUT", EventType.MESSAGE,
                    Map.of(mainPath + "servers/ansibleplaybook.txt", SourceType.TEXT)));
            serversButtons.add(new ServersButton("Вывод ip адресов на Nginx", EventType.MESSAGE,
                    Map.of(mainPath + "servers/outputIP.txt", SourceType.TEXT)));
            serversButtons.add(new ServersButton("Закрытие файла в samba", EventType.MESSAGE,
                    Map.of(mainPath + "servers/closeFileInSamba.txt", SourceType.TEXT)));
            serversButtons.add(new ServersButton("Изменение прав на Disk N", EventType.MESSAGE,
                    Map.of(mainPath + "servers/diskN.txt", SourceType.TEXT)));
            serversButtons.add(new ServersButton("Восстановление базы данных SQL Server", EventType.MESSAGE,
                    Map.of(mainPath + "servers/databaseSQL.txt", SourceType.TEXT)));
            serversButtons.add(new ServersButton("Настройка прокси-сервера", EventType.MESSAGE, proxy));
            serversButtons.add(new ServersButton("Назад", EventType.BACK, null));
        }
        return serversButtons;
    }

    public static List<KCButton> getKCButtons() {
        if (kcButtons.isEmpty()) {
            kcButtons.add(new KCButton("Данные по ВКС", EventType.MESSAGE,
                    Map.of(mainPath + "KC/BKC.txt", SourceType.TEXT)));
            kcButtons.add(new KCButton("Контакты", EventType.MESSAGE,
                    Map.of(mainPath + "KC/contacts.txt", SourceType.TEXT)));
            kcButtons.add(new KCButton("АКС", EventType.MESSAGE,
                    Map.of(mainPath + "KC/AKC.txt", SourceType.TEXT)));
            kcButtons.add(new KCButton("Инструкция Eyealink", EventType.MESSAGE,
                    Map.of(mainPath + "KC/YealinkVCMobile.pdf", SourceType.FILE)));
            kcButtons.add(new KCButton("Назад", EventType.BACK, null));
        }
        return kcButtons;
    }

    public static List<ContactsButton> getContactsButtons() {
        if (contactsButtons.isEmpty()) {
            contactsButtons.add(new ContactsButton("Провайдеры", EventType.MESSAGE,
                    Map.of(mainPath + "contacts/contactsWork.pdf", SourceType.FILE)));
            contactsButtons.add(new ContactsButton("Список сотрудников в ТУ, ответственных за IT", EventType.MESSAGE,
                    Map.of(mainPath + "contacts/TUrespIT.txt", SourceType.TEXT)));
            contactsButtons.add(new ContactsButton("Назад", EventType.BACK, null));
        }
        return contactsButtons;
    }
}
