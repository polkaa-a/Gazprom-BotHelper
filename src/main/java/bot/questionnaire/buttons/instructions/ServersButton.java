//В данном пакете создаются классы кнопок уровня Layer.INSTRUCTIONS (см. enum Layer)
package bot.questionnaire.buttons.instructions;

import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.Getter;

import java.util.Map;

//Кнопка должна наследоваться от абстрактного класса Button
//Данный класс представляет кнопки уровня Layer.SERVERS (см. enum Layer)
@Getter
public class ServersButton extends Button {
    public ServersButton(String text, EventType eventType, Map<String, SourceType> source) {
        super(text, Layer.SERVERS, eventType, source, false, null);
    }
}
