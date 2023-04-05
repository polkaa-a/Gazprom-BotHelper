package bot.service.enums;

//Список действий на нажатие кнопки
public enum EventType {
    MESSAGE, //Отправка сообщения
    NEXT, //Переход на следующий уровень (см. enum Layer)
    BACK //Переход на предыдущий уровень (см. enum Layer)
}