//В данном пакете создаются классы кнопок уровня Layer.INSTRUCTIONS (см. enum Layer)
package bot.questionnaire.buttons.instructions;

import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.Getter;

import java.util.Map;

//Кнопка должна наследоваться от абстрактного класса Button
//Данный класс представляет кнопки уровня Layer.KC (см. enum Layer)
@Getter
public class KCButton extends Button {
    public KCButton(String text, EventType eventType, Map<String, SourceType> source) {
        super(text, Layer.KC, eventType, source, false, null);
    }
}
