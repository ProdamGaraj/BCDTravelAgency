package сore.services;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
public class KeyboardService {
    public InlineKeyboardMarkup getTourChoosingKeyboard() {
        String str = EmojiParser.parseToUnicode(" :gay_pride_flag:");
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Активности" + str)
                                .callbackData("activities")
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Курорты" + str)
                                .callbackData("resorts")
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Авторские туры" + str)
                                .callbackData("personal_tours")
                        .build()))
                .build();
    }
}
