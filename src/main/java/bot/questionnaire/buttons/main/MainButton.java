//В данном пакете создаются классы кнопок уровня Layer.MAIN (см. enum Layer)
package bot.questionnaire.buttons.main;

import bot.questionnaire.buttons.Button;
import bot.service.enums.EventType;
import bot.service.enums.Layer;
import bot.service.enums.SourceType;

import java.util.Map;

//Кнопка должна наследоваться от абстрактного класса Button
public class MainButton extends Button {
    public MainButton(String text, EventType eventType, Map<String, SourceType> source, boolean isSecret, String password) {
        super(text, Layer.MAIN, eventType, source, isSecret, password);
    }
}
