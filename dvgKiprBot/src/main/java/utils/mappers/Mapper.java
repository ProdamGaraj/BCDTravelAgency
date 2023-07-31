package utils.mappers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public interface Mapper {

    public User getUserFromMessage(Message message);

}
