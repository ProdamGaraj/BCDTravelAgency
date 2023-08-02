package сore.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import сore.models.Activity;
import сore.models.CustomTour;
import сore.models.Hotel;
import сore.models.Resort;
import сore.repository.TemporaryRepos.ActivityRepo;
import сore.repository.TemporaryRepos.CustomTourRepo;
import сore.repository.TemporaryRepos.HotelRepo;
import сore.repository.TemporaryRepos.ResortRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyboardService {

    private final ActivityRepo activityRepo = new ActivityRepo();

    private final ResortRepo resortRepo = new ResortRepo();

    private final CustomTourRepo customTourRepo = new CustomTourRepo();

    private final HotelRepo hotelRepo = new HotelRepo();

    static final Map<Long, Integer> selectedActivity = new HashMap<>();

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
                        .build()))
                .build();
    }

    public InlineKeyboardMarkup getActivitiesKeyboard() {

        List<Activity> activities = activityRepo.activityList();

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();


        return builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text("В начало").callbackData("restart")
                .build())).build();
    }

    public InlineKeyboardMarkup getResortsKeyboard() {

        List<Resort> resorts = resortRepo.resortList();

        Resort currentResort =  resorts.get(0);

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
                .build();
    }

    public InlineKeyboardMarkup getPersonalToursKeyboard() {

        List<CustomTour> customTours = customTourRepo.customTourList();

        CustomTour currentCustomTour = customTours.get(0);

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
                .build();
    }
}
