//В данном пакете создаются классы кнопок уровня Layer.CONTACTS (см. enum Layer)
package bot.questionnaire.buttons.contacts;

import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.Getter;

import java.util.Map;

//Кнопка должна наследоваться от абстрактного класса Button
@Getter
public class ContactsButton extends Button {
    public ContactsButton(String text, EventType eventType, Map<String, SourceType> source) {
        super(text, Layer.CONTACTS, eventType, source, false, null);
    }
}

