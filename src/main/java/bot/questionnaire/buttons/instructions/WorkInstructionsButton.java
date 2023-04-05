package bot.questionnaire.buttons.instructions;

import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.Getter;

import java.util.Map;

//Кнопка должна наследоваться от абстрактного класса Button
//Данный класс представляет кнопки уровня Layer.WORK_INSTRUCTIONS (см. enum Layer)
@Getter
public class WorkInstructionsButton extends Button {
    public WorkInstructionsButton(String text, EventType eventType, Map<String, SourceType> source) {
        super(text, Layer.WORK_INSTRUCTIONS, eventType, source, false, null);
    }
}
