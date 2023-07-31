package utils.mappers.impl;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import utils.mappers.Mapper;

public class MapperImpl implements Mapper {

    @Override
    public User getUserFromMessage(Message message) {
        return message.getFrom();
    }
}
