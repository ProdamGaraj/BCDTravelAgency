package bcd.solution.dvgKiprBot.core.repository;;

import bcd.solution.dvgKiprBot.core.models.Resort;

import java.util.ArrayList;
import java.util.List;

public class ResortRepo {

    private final ActivityRepo activityRepo = new ActivityRepo();


    public List<Resort> resortList() {

        List<Resort> resorts = new ArrayList<>();
        resorts.add(new Resort(1L, "PROTARAS", "desc", "geo", "image_src", activityRepo.activityList()));
        resorts.add(new Resort(1L, "AYIA NAPA", "desc", "geo", "image_src", activityRepo.activityList()));
        resorts.add(new Resort(1L, "LARNACA", "desc", "geo", "image_src", activityRepo.activityList()));
        resorts.add(new Resort(1L, "LIMASSOL", "desc", "geo", "image_src", activityRepo.activityList()));

        return resorts;
    }
}
