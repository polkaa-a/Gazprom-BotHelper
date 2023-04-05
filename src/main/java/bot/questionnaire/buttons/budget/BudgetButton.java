package bot.questionnaire.buttons.budget;

import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;
import lombok.Getter;

import java.util.Map;

//Кнопка должна наследоваться от абстрактного класса Button
//Данный класс представляет кнопки уровня Layer.BUDGET (см. enum Layer)
@Getter
public class BudgetButton extends Button {
    public BudgetButton(String text, EventType eventType, Map<String, SourceType> source) {
        super(text, Layer.BUDGET, eventType, source, false, null);
    }
}
