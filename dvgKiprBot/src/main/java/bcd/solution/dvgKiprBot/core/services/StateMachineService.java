package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.models.User;
import bcd.solution.dvgKiprBot.core.repository.ActivityRepo;
import bcd.solution.dvgKiprBot.core.repository.StateMachineRepo;
import bcd.solution.dvgKiprBot.core.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        StateMachine stateMachine = StateMachine.builder().user(user).activities(new ArrayList<>()).build();
        stateMachineRepo.saveAndFlush(stateMachine);
        return stateMachine;
    }

    @Async
    public StateMachine setWaitPasswordByUserId(Long id, boolean waitPassword, Integer messageId) {
        StateMachine stateMachine = getOrAddIfNotExists(id);
        stateMachine.setWait_password(waitPassword);
        stateMachine.setAuth_message_id(messageId);
        stateMachineRepo.save(stateMachine);
        return stateMachine;
    }

    @Async
    public StateMachine getByUserId(Long id) {
        return getOrAddIfNotExists(id);
    }

    @Async
    public void addActivityByIdByUserId(Long userId, Long activityId) {
        Activity activity = activityRepo.getReferenceById(activityId);
        StateMachine stateMachine = getOrAddIfNotExists(userId);

        stateMachine.activities.add(activity);
        stateMachineRepo.save(stateMachine);

    }

    @Async
    public void clearActivitiesByUserId(Long userId) {
        StateMachine stateMachine = getOrAddIfNotExists(userId);
        stateMachine.activities.clear();
        stateMachineRepo.save(stateMachine);
    }

    @Async
    public void clearStateByUserId(Long userId) {
        StateMachine stateMachine = getOrAddIfNotExists(userId);
        stateMachineRepo.delete(stateMachine);
    }
}
