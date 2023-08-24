package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.Stars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import bcd.solution.dvgKiprBot.core.repository.ActivityRepo;
import bcd.solution.dvgKiprBot.core.repository.CustomTourRepo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    private final ActivityRepo activityRepo;

    private final CustomTourRepo customTourRepo;

    @Autowired
    public KeyboardService(ActivityRepo activityRepo,
                           CustomTourRepo customTourRepo) {
        this.activityRepo = activityRepo;
        this.customTourRepo = customTourRepo;
    }

    public InlineKeyboardMarkup getHotelsStarsKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();

        builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(Stars.threestar.toString())
                        .callbackData("hotels_stars/" + Stars.threestar.name())
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(Stars.fourstar.toString())
                        .callbackData("hotels_stars/" + Stars.fourstar.name())
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(Stars.fivestar.toString())
                        .callbackData("hotels_stars/" + Stars.fivestar.name())
                        .build()))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Не важно")
                                .callbackData("hotels_change/" + 0)
                                .build()))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("В начало")
                                .callbackData("restart")
                                .build()));

        return builder.build();
    }

    public InlineKeyboardMarkup getRestartKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text("На домашнюю страницу")
                        .callbackData("start")
                        .build()))
                .build();
    }

    public InlineKeyboardMarkup getAuthCancelKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text("Отмена")
                        .callbackData("auth_cancel")
                        .build()))
                .build();
    }

    public InlineKeyboardMarkup getPhoneCancelKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text("Отмена")
                        .callbackData("auth_phoneCancel")
                        .build()))
                .build();
    }

    public InlineKeyboardMarkup getTourChoosingKeyboard(boolean hasPhone, boolean isAuthorized) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
//        if (true) {
        if (hasPhone) {
            builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                            .text("Активности")
                            .callbackData("activities")
                            .build()))
                    .keyboardRow(List.of(InlineKeyboardButton.builder()
                            .text("Курорты")
                            .callbackData("resorts")
                            .build()))
                    .keyboardRow(List.of(InlineKeyboardButton.builder()
                            .text("Отели")
                            .callbackData("hotels")
                            .build()))
                    .keyboardRow(List.of(InlineKeyboardButton.builder()
                            .text("Авторские туры")
                            .callbackData("customTours")
                            .build()
                    ));
        }
        if (!hasPhone) {
            builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text("Добавить номер телефона")
                    .callbackData("auth_getPhone")
                    .build()));
        }
        if (!isAuthorized) {
            builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text("Авторизоваться")
                    .callbackData("auth")
                    .build()));
        }

        return builder.build();
    }

    public InlineKeyboardMarkup getActivitiesKeyboard(Integer index, Long activityId, boolean isDeleting) {
        long size = activityRepo.count();

        List<InlineKeyboardButton> navigation_row = new ArrayList<>();
        if (index > 0) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("<-")
                    .callbackData("activities_change/" + (index - 1))
                    .build());
        }
        if (isDeleting) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("Убрать")
                    .callbackData("activities_delete/" + (index) + "/" + (activityId))
                    .build());
        } else {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("Добавить")
                    .callbackData("activities_add/" + (index) + "/" + (activityId))
                    .build());
        }

        if (index < size - 1) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("->")
                    .callbackData("activities_change/" + (index + 1))
                    .build());
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(navigation_row)
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Выбрать")
                                .callbackData("activities_select")
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("В начало")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getResortsKeyboard(Integer index, Long resortId, long size) {

        List<InlineKeyboardButton> navigation_row = new ArrayList<>();
        if (index > 0) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("<-")
                    .callbackData("resorts_change/" + (index - 1))
                    .build());
        }
        navigation_row.add(InlineKeyboardButton.builder()
                .text((index + 1) + "/" + size)
                .callbackData("null")
                .build());
        if (index < size - 1) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("->")
                    .callbackData("resorts_change/" + (index + 1))
                    .build());
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(navigation_row)
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Выбрать")
                                .callbackData("resorts_select/" + (resortId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Показать все фото")
                                .callbackData("resorts_media/" + (index) + "/" + (resortId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("В начало")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getCustomToursKeyboard(Integer index, Long customTourId) {

        long size = customTourRepo.count();

        List<InlineKeyboardButton> navigation_row = new ArrayList<>();
        if (index > 0) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("<-")
                    .callbackData("customTours_change/" + (index - 1))
                    .build());
        }
        navigation_row.add(InlineKeyboardButton.builder()
                .text((index + 1) + "/" + size)
                .callbackData("null")
                .build());
        if (index < size - 1) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("->")
                    .callbackData("customTours_change/" + (index + 1))
                    .build());
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(navigation_row)
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Выбрать")
                                .callbackData("customTours_select/" + (customTourId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Показать все фото")
                                .callbackData("customTours_media/" + (index) + "/" + (customTourId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("В начало")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getHotelsKeyboard(Integer index, Long hotelId, long size) {

        List<InlineKeyboardButton> navigation_row = new ArrayList<>();
        if (index > 0) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("<-")
                    .callbackData("hotels_change/" + (index - 1))
                    .build());
        }
        navigation_row.add(InlineKeyboardButton.builder()
                .text((index + 1) + "/" + size)
                .callbackData("null")
                .build());
        if (index < size - 1) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("->")
                    .callbackData("hotels_change/" + (index + 1))
                    .build());
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(navigation_row)
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Выбрать")
                                .callbackData("hotels_select/" + (hotelId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Показать все фото")
                                .callbackData("hotels_media/" + (index) + "/" + (hotelId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("В начало")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getStarterKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Ввести номер телефона")
                                .callbackData("auth_getPhone")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Подобрать тур")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public ReplyKeyboardMarkup getPhoneKeyboard() {
        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(
                        new KeyboardRow(
                                List.of(
                                        KeyboardButton.builder()
                                                .text("Отправить номер телефона")
                                                .requestContact(true)

                                                .build())))
                .build();
    }

}
