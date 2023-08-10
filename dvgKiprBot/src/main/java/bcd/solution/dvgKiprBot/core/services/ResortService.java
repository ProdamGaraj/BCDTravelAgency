package bcd.solution.dvgKiprBot.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.repository.ResortRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ResortService {
    private final ResortRepo resortRepo;

    @Autowired
    public ResortService(ResortRepo resortRepo) {
        this.resortRepo = resortRepo;
    }

    @Async
    public Resort getByIndex(Integer index) {
        return resortRepo.findAll().get(index);
    }
}
