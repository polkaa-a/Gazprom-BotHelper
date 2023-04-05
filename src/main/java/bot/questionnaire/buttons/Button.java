package bot.questionnaire.buttons;

import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.Getter;

import java.util.Map;

//Абстрактный класс кнопки
@Getter
public abstract class Button {
    private final String text;
    private final Layer layer;

    //true, если для доступа нужен пароль
    private final boolean isSecret;
    //Тип действия кнопки (см. enum EventType)
    private final EventType eventType;

    /*
     * Ключ = путь к ресурсу (String)
     * Значение = тип ресурса (SourceType)
     *
     * Каждая кнопка может иметь 0 и более ресурсов
     */
    private final Map<String, SourceType> source;
    private String password;

    public Button(String text, Layer layer, EventType eventType, Map<String, SourceType> source, boolean isSecret, String password) {
        this.text = text;
        this.layer = layer;
        this.source = source;
        this.eventType = eventType;
        this.isSecret = isSecret;
        if (isSecret) this.password = password;
    }

    public String getPassword() {
        if (isSecret) return password;
        else return null;
    }
}
