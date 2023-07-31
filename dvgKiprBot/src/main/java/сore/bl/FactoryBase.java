package сore.bl;

import сore.models.*;

public interface FactoryBase {
    Message customTourToMessage(CustomTour model);
    Message activityToMessage(Activity model);
    Message userToMessage(User model);
    Message hotelToMessage(Hotel model);
    Message resortToMessage(Resort model);
    Iterable<Message> convertToursMessages(Iterable<CustomTour> model);
}
