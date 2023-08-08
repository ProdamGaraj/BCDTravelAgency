package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public class HotelService {

    @Autowired
    public HotelService() {
    }

    public void hotel_select(CallbackQuery callbackQuery, DvgKiprBot bot) {

    }

    public void hotel_rightHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

    }

    public void hotels_leftHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

    }

    public void hotelsChooseHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

    }
}
