package сore.bl;

import org.jvnet.hk2.annotations.Service;
import сore.models.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageFactory implements FactoryBase {

    @Override
    public Message customTourToMessage(CustomTour model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Message activityToMessage(Activity model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Message userToMessage(User model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Message hotelToMessage(Hotel model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Message resortToMessage(Resort model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Iterable<Message> convertToursMessages(Iterable<CustomTour> model){
        List<CustomTour> list = (List<CustomTour>) model;
        return  list.stream().map(this::customTourToMessage).collect(Collectors.toList()); //returning messages
    }
}