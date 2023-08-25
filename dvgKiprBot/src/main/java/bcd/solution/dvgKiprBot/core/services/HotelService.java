package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.models.Stars;
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
        return hotelRepo.findAllByIsDeleted(false).get(index);
    }

    public List<Hotel> findByResort(Resort resort) {
        if (resort == null) {
            return hotelRepo.findAllByIsDeleted(false);
        }
        return hotelRepo.findAllByResortAndIsDeleted(resort, false);
    }
    public Optional<Hotel> getById(Long hotelId) {
        return hotelRepo.findById(hotelId);
    }

    public List<Hotel> findByResortAndStars(Resort resort, Stars stars) {
        if (resort == null) {
            if (stars == null) {
                return hotelRepo.findAllByIsDeleted(false);
            }
            return hotelRepo.findAllByStarsAndIsDeleted(stars, false);
        }
        if (stars == null) {
            return hotelRepo.findAllByResortAndIsDeleted(resort, false);
        }
        return hotelRepo.findAllByResortAndStarsAndIsDeleted(resort, stars, false);
    }

}
