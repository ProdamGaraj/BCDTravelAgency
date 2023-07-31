package сore.services;

import org.springframework.beans.factory.annotation.Autowired;
import сore.models.CustomTour;
import сore.repository.CustomToursRepository;

public class CustomToursService {
    @Autowired
    private static CustomToursRepository repository;
    public static Iterable<CustomTour> list = repository.findAll();
}
