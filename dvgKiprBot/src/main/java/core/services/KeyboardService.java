package core.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import core.models.Activity;
import core.models.CustomTour;
import core.models.Hotel;
import core.models.Resort;
import core.repository.TemporaryRepos.ActivityRepo;
import core.repository.TemporaryRepos.CustomTourRepo;
import core.repository.TemporaryRepos.HotelRepo;
import core.repository.TemporaryRepos.ResortRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyboardService {

    private final ActivityRepo activityRepo = new ActivityRepo();

    private final ResortRepo resortRepo = new ResortRepo();

    private final CustomTourRepo customTourRepo = new CustomTourRepo();

    private final HotelRepo hotelRepo = new HotelRepo();

    public KeyboardService() {
    }

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
                        .callbackData("personal_tours")
                        .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getActivitiesKeyboard(Integer index) {

        List<Activity> activities = activityRepo.activityList();

        if (index < 0 || index >= activities.size()) {
            index = 0;
        }

        Activity acurrentActivity = activities.get(index);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("activity_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(acurrentActivity.name)
                                .callbackData("activity_select")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("activity_right")
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Home")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getResortsKeyboard(Integer index) {

        List<Resort> resorts = resortRepo.resortList();

        if (index < 0 || index >= resorts.size()) {
            index = 0;
        }

        Resort currentResort =  resorts.get(index);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("resort_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentResort.name)
                                .callbackData("resort_select")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("resort_right")
                                .build()
                ))
                .keyboardRow( List.of(
                        InlineKeyboardButton.builder()
                                .text("Home")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getPersonalToursKeyboard(Integer index) {

        List<CustomTour> customTours = customTourRepo.customTourList();

        if (index < 0 || index >= customTours.size()) {
            index = 0;
        }

        CustomTour currentCustomTour = customTours.get(index);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("personalTour_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentCustomTour.name)
                                .callbackData("personalTour_select")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("personalTour_right")
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("Home")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getHotelChoosingKeyboard() {

        List<Hotel> hotels = hotelRepo.hotelList();

        Hotel currentHotel = hotels.get(0);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("hotel_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentHotel.name)
                                .callbackData("hotel_select")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("hotel_right")
                                .build()
                ))
                .keyboardRow( List.of(
                        InlineKeyboardButton.builder()
                                .text("Home")
                                .callbackData("restart")
                                .build()
                ))
                .build();
    }
}
