//В данном пакете создаются классы кнопок уровня Layer.INSTRUCTIONS (см. enum Layer)
package bot.questionnaire.buttons.instructions;

import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.Getter;

import java.util.Map;

//Кнопка должна наследоваться от абстрактного класса Button
//Данный класс представляет кнопки уровня Layer.NET (см. enum Layer)
@Getter
public class NetButton extends Button {
    public NetButton(String text, EventType eventType, Map<String, SourceType> source) {
        super(text, Layer.NET, eventType, source, false, null);
    }
}
