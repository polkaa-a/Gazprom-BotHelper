package bot.service.enums;

//Список уровней (согласно иерархии расположения информации в боте)
public enum Layer {
    MAIN, //Основной уровень (содержит в себе INSTRUCTIONS, CONTACTS)
    INSTRUCTIONS, //Уровень инструкций (содержит в себе NET, APM, SERVERS, KC, WORK_INSTRUCTIONS)
    BUDGET, //Бюджет
    CONNECTION_BUDGET, //Бюджет связь
    WORK_INSTRUCTIONS, //Рабочие инструкции
    CONTACTS, //Контакты
    NET, //Сеть
    APM, //АРМ
    SERVERS, //Серверы
    KC //АКС/ВКС
}
