package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.models.User;
import bcd.solution.dvgKiprBot.core.repository.ActivityRepo;
import bcd.solution.dvgKiprBot.core.repository.StateMachineRepo;
import bcd.solution.dvgKiprBot.core.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StateMachineService {
    private final StateMachineRepo stateMachineRepo;
    private final UserRepository userRepository;
    private final ActivityRepo activityRepo;

    public StateMachineService(StateMachineRepo stateMachineRepo,
                               UserRepository userRepository,
                               ActivityRepo activityRepo) {
        this.stateMachineRepo = stateMachineRepo;
        this.userRepository = userRepository;
        this.activityRepo = activityRepo;
    }

    @Async
    private StateMachine getOrAddIfNotExists(Long id) {
        User user = userRepository.getReferenceById(id);
        Optional<StateMachine> stateMachineOptional = stateMachineRepo.findByUser(user);
        if (stateMachineOptional.isPresent()) {
            return stateMachineOptional.get();
        }
        StateMachine stateMachine = StateMachine.builder().user(user).build();
        stateMachineRepo.saveAndFlush(stateMachine);
        return stateMachine;
    }

    @Async
    public StateMachine setWaitPasswordByUserId(Long id, boolean wait_password, Integer message_id) {
        StateMachine stateMachine = getOrAddIfNotExists(id);
        stateMachine.setWait_password(wait_password);
        stateMachine.setAuth_message_id(message_id);
        stateMachineRepo.save(stateMachine);
        return stateMachine;
    }

    @Async
    public StateMachine getByUserId(Long id) {
        return getOrAddIfNotExists(id);
    }

    @Async
    public StateMachine addActivityByIdByUserId(Long user_id, Long activity_id) {
        Activity activity = activityRepo.getReferenceById(activity_id);
        StateMachine stateMachine = getOrAddIfNotExists(user_id);

        stateMachine.activities.add(activity);
        stateMachineRepo.save(stateMachine);

        return stateMachine;
    }

    @Async
    public StateMachine clearActivitiesByUserId(Long user_id) {
        StateMachine stateMachine = getOrAddIfNotExists(user_id);
        stateMachine.activities.clear();
        stateMachineRepo.save(stateMachine);
        return stateMachine;
    }
}
