package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.repository.ResortRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ResortService {
    private final ResortRepo resortRepo;

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

    @Async
    public Optional<Resort> getById(Long resortId) {
//        Resort resort = resortRepo.getReferenceById(resortId);
//        return resort;
        Optional<Resort> resort = resortRepo.findById(resortId);
        return resort;

    }

    @Async
    public String toString(Resort resort) {
        StringBuilder activity_list = new StringBuilder();
        for (Activity activity : resort.activities) {
            activity_list.append("- ").append(activity.name).append("\n");
        }

        return resort.name + "\n\n"
                + resort.description + "\n\n"
                + resort.geo + "\n\n"
                + "Доступные развлечения:\n"
                + activity_list;
    }
}
