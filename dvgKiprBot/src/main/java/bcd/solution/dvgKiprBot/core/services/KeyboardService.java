package bcd.solution.dvgKiprBot.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.repository.ActivityRepo;
import bcd.solution.dvgKiprBot.core.repository.CustomTourRepo;
import bcd.solution.dvgKiprBot.core.repository.HotelRepo;
import bcd.solution.dvgKiprBot.core.repository.ResortRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    private final ActivityRepo activityRepo;

    private final ResortRepo resortRepo;

    private final CustomTourRepo customTourRepo;

    private final HotelRepo hotelRepo;

    @Autowired
    public KeyboardService(ActivityRepo activityRepo,
                           ResortRepo resortRepo,
                           CustomTourRepo customTourRepo,
                           HotelRepo hotelRepo) {
        this.activityRepo = activityRepo;
        this.resortRepo = resortRepo;
        this.customTourRepo = customTourRepo;
        this.hotelRepo = hotelRepo;
    }

    public InlineKeyboardMarkup getAuthCancelKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text("Отмена")
                        .callbackData("auth_cancel")
                        .build()))
                .build();
    }

    @Async
    public InlineKeyboardMarkup getTourChoosingKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text("Активности")
                        .callbackData("activities")
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text("Курорты")
                        .callbackData("resorts")
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text("Авторские туры")
                        .callbackData("customTours")
                        .build()
                ))
                .build();
    }

    @Async
    public InlineKeyboardMarkup getActivitiesKeyboard(Integer index, Long activityId) {
        long size = activityRepo.count();

        List<InlineKeyboardButton> navigation_row = new ArrayList<>();
        if (index > 0) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("<-")
                    .callbackData("activities_change/" + (index - 1))
                    .build());
        }
        navigation_row.add(InlineKeyboardButton.builder()
                .text("Добавить")
                .callbackData("activities_add/"+(index) + "/" + (activityId))
                .build());
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

    @Async
    public InlineKeyboardMarkup getResortsKeyboard(Integer index, Long resortId, long size) {

        List<InlineKeyboardButton> navigation_row = new ArrayList<>();
        if (index > 0) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text("<-")
                    .callbackData("resorts_change/" + (index - 1))
                    .build());
        }
        navigation_row.add(InlineKeyboardButton.builder()
                .text("Выбрать")
                .callbackData("resorts_select/"+(resortId))
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
                                .text("В начало")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    @Async
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
                .text("Выбрать")
                .callbackData("customTours_select/"+(customTourId))
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
                .text("Выбрать")
                .callbackData("hotels_select/"+(hotelId))
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
                                .text("В начало")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }
}
