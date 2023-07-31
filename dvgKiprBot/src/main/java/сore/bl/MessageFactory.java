package сore.bl;

import org.jvnet.hk2.annotations.Service;
import сore.models.*;

@Service
public class MessageFactory implements FactoryBase {

    @Override
    public Message createCustomTour(CustomTour model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Message createActivity(Activity model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Message createUser(User model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Message createHotel(Hotel model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

    @Override
    public Message createResort(Resort model) {
        Message message = new Message();
        message.setText(model.toString());
        return message;
    }

}
