package bcd.solution.dvgKiprBot.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.models.Resort;
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

        List<Activity> activities = new ArrayList<>();
//        List<Activity> activities = activityRepo.activityList();

        Activity currentActivity = activities.get(index);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("activity_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentActivity.name)
                                .callbackData("activity_select"+currentActivity.getId())
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

        List<Resort> resorts = new ArrayList<>();
//        List<Resort> resorts = resortRepo.resortList();

        Resort currentResort =  resorts.get(index);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("resort_left/")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentResort.name)
                                .callbackData("resort_select/"+currentResort.getId())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("resort_right/")
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

        List<CustomTour> customTours = new ArrayList<>();
//        List<CustomTour> customTours = customTourRepo.customTourList();

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

        List<Hotel> hotels = new ArrayList<>();
//        List<Hotel> hotels = hotelRepo.hotelList();

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
