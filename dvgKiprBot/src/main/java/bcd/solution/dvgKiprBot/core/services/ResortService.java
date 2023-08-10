package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.repository.ResortRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Async
    public List<Resort> getByIndexAndActivities(Integer index, List<Activity> activities) {
        return getByActivities(activities);
    }

    @Async
    public List<Resort> getByActivities(List<Activity> activities) {
        if (activities.isEmpty()) {
            return resortRepo.findAll();
        }
        List<Resort> allResorts = resortRepo.findAll();
        return allResorts.stream()
                .filter((resort) -> new HashSet<>(resort.activities).containsAll(activities)).toList();
    }
}
