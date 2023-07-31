package сore.bl;

import сore.models.*;

public interface FactoryBase {
    Message createCustomTour(CustomTour model);
    Message createActivity(Activity model);
    Message createUser(User model);
    Message createHotel(Hotel model);
    Message createResort(Resort model);
}
