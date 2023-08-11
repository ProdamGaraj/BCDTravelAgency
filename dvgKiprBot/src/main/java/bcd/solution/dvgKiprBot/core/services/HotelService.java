package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.repository.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepo hotelRepo;

    @Autowired
    public HotelService(HotelRepo hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    public Hotel getByIndex(Integer index) {
        return hotelRepo.findAll().get(index);
    }

    public List<Hotel> findByResort(Resort resort) {
        return hotelRepo.findAllByResort(resort);
    }

    public Optional<Hotel> getById(Long hotelId) {
        return hotelRepo.findById(hotelId);
    }
}
