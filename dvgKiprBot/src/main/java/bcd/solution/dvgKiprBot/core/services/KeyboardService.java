package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.Activity;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class KeyboardService {

    private final ActivityRepo activityRepo;

    private final CustomTourRepo customTourRepo;

    private final String rightArrowText = "Далее ➡️";
    private final String leftArrowText = "⬅️ Назад";
    private final String restartButtonText = "🔄 В начало 🔄";
    private final String cancelButtonText = "❌ Отмена ❌";
    private final String phoneButtonText = "☎️ Добавить номер телефона ☎️";
    private final String sendPhoneButtonText = "☎️ Отправить номер телефона ☎️";
    private final String confirmButtonText = "✅ Выбрать ✅";
    private final String showPhotoButtonText = "🖼️ Показать все фото 🖼️";
    private final String noMatterButtonText = "🤷‍♂️ Не важно 🤷‍♂️";
    private final String homeButtonText = "🏠 На домашнюю страницу 🏠";
    private final String authButtonText = "🔐 Авторизация 🔐";
    private final String activitiesButtonText = "⚽️ Активности ⚽️";
    private final String resortsButtonText = "🏝️ Курорты 🏝️";
    private final String hotelsButtonText = "🏨 Отели 🏨";
    private final String customToursButtonText = "🗺️ Авторские туры 🗺️";

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
                                .text(noMatterButtonText)
                                .callbackData("hotels_change/" + 0)
                                .build()))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(restartButtonText)
                                .callbackData("restart")
                                .build()));

        return builder.build();
    }

    public InlineKeyboardMarkup getRestartKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(homeButtonText)
                        .callbackData("start")
                        .build()))
                .build();
    }

    public InlineKeyboardMarkup getAuthCancelKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(cancelButtonText)
                        .callbackData("auth_cancel")
                        .build()))
                .build();
    }

    public InlineKeyboardMarkup getPhoneCancelKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(cancelButtonText)
                        .callbackData("auth_phoneCancel")
                        .build()))
                .build();
    }

    public InlineKeyboardMarkup getTourChoosingKeyboard(boolean hasPhone, boolean isAuthorized) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
//        if (true) {
        if (hasPhone) {
            builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                            .text(activitiesButtonText)
                            .callbackData("activities")
                            .build()))
                    .keyboardRow(List.of(InlineKeyboardButton.builder()
                            .text(resortsButtonText)
                            .callbackData("resorts")
                            .build()))
                    .keyboardRow(List.of(InlineKeyboardButton.builder()
                            .text(hotelsButtonText)
                            .callbackData("hotels")
                            .build()))
                    .keyboardRow(List.of(InlineKeyboardButton.builder()
                            .text(customToursButtonText)
                            .callbackData("customTours")
                            .build()
                    ));
        }
        if (!hasPhone) {
            builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(phoneButtonText)
                    .callbackData("auth_getPhone")
                    .build()));
        }
        if (!isAuthorized) {
            builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(authButtonText)
                    .callbackData("auth")
                    .build()));
        }

        return builder.build();
    }

    public InlineKeyboardMarkup getActivitiesKeyboard(List<Activity> selectedActivities) {
        List<Activity> allActivities = activityRepo.findAll();
        Set<Activity> activitySet = new HashSet<>(selectedActivities);

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();

        for (Activity activity : allActivities) {
            boolean isChosen = activitySet.contains(activity);
            builder.keyboardRow(List.of(
                    InlineKeyboardButton.builder()
                            .text(activity.name + (isChosen ? " (убрать)" : ""))
                            .callbackData("activities_" + (isChosen ? "delete" : "add") + "/" + (activity.getId()))
                            .build()
            ));
        }

        return builder
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(confirmButtonText)
                                .callbackData("activities_select")
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(restartButtonText)
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getResortsKeyboard(Integer index, Long resortId, long size) {

        List<InlineKeyboardButton> navigation_row = new ArrayList<>();
        if (index > 0) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text(leftArrowText)
                    .callbackData("resorts_change/" + (index - 1))
                    .build());
        }
        navigation_row.add(InlineKeyboardButton.builder()
                .text((index + 1) + "/" + size)
                .callbackData("null")
                .build());
        if (index < size - 1) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text(rightArrowText)
                    .callbackData("resorts_change/" + (index + 1))
                    .build());
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(navigation_row)
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(confirmButtonText)
                                .callbackData("resorts_select/" + (resortId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(showPhotoButtonText)
                                .callbackData("resorts_media/" + (index) + "/" + (resortId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(restartButtonText)
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
                    .text(leftArrowText)
                    .callbackData("customTours_change/" + (index - 1))
                    .build());
        }
        navigation_row.add(InlineKeyboardButton.builder()
                .text((index + 1) + "/" + size)
                .callbackData("null")
                .build());
        if (index < size - 1) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text(rightArrowText)
                    .callbackData("customTours_change/" + (index + 1))
                    .build());
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(navigation_row)
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(confirmButtonText)
                                .callbackData("customTours_select/" + (customTourId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(showPhotoButtonText)
                                .callbackData("customTours_media/" + (index) + "/" + (customTourId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(restartButtonText)
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getHotelsKeyboard(Integer index, Long hotelId, long size) {

        List<InlineKeyboardButton> navigation_row = new ArrayList<>();
        if (index > 0) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text(leftArrowText)
                    .callbackData("hotels_change/" + (index - 1))
                    .build());
        }
        navigation_row.add(InlineKeyboardButton.builder()
                .text((index + 1) + "/" + size)
                .callbackData("null")
                .build());
        if (index < size - 1) {
            navigation_row.add(InlineKeyboardButton.builder()
                    .text(rightArrowText)
                    .callbackData("hotels_change/" + (index + 1))
                    .build());
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(navigation_row)
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(confirmButtonText)
                                .callbackData("hotels_select/" + (hotelId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(showPhotoButtonText)
                                .callbackData("hotels_media/" + (index) + "/" + (hotelId))
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(restartButtonText)
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getStarterKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(phoneButtonText)
                                .callbackData("auth_getPhone")
                                .build()
//                        InlineKeyboardButton.builder()
//                                .text("Подобрать тур")
//                                .callbackData("restart")
//                                .build()
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
                                                .text(sendPhoneButtonText)
                                                .requestContact(true)
                                                .build())))
                .build();
    }

}
