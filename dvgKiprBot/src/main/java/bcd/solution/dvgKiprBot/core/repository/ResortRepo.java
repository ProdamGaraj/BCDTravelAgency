package bcd.solution.dvgKiprBot.core.repository;;

import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.models.Resort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface ResortRepo extends JpaRepository<Resort, Long> {
}
