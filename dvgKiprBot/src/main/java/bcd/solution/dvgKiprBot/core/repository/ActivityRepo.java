package bcd.solution.dvgKiprBot.core.repository;;

import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.models.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ActivityRepo extends JpaRepository<Activity, Long>{

}
