//В данном пакете создаются классы кнопок уровня Layer.INSTRUCTIONS (см. enum Layer)
package bot.questionnaire.buttons.instructions;

import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.Getter;

import java.util.Map;

//Кнопка должна наследоваться от абстрактного класса Button
@Getter
public class InstructionButton extends Button {
    public InstructionButton(String text, EventType eventType, Map<String, SourceType> sources) {
        super(text, Layer.INSTRUCTIONS, eventType, sources, false, null);
    }
}
