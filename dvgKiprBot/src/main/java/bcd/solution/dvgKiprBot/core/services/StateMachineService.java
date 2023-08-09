package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.models.User;
import bcd.solution.dvgKiprBot.core.repository.StateMachineRepo;
import bcd.solution.dvgKiprBot.core.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StateMachineService {
    private final StateMachineRepo stateMachineRepo;
    private final UserRepository userRepository;

    public StateMachineService(StateMachineRepo stateMachineRepo,
                               UserRepository userRepository) {
        this.stateMachineRepo = stateMachineRepo;
        this.userRepository = userRepository;
    }

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

    public StateMachine setWaitPasswordByUserId(Long id, boolean wait_password, Integer message_id) {
        StateMachine stateMachine = getOrAddIfNotExists(id);
        stateMachine.setWait_password(wait_password);
        stateMachine.setAuth_message_id(message_id);
        stateMachineRepo.save(stateMachine);
        return stateMachine;
    }

    public StateMachine getByUserId(Long id) {
        User user = userRepository.getReferenceById(id);
        return getOrAddIfNotExists(user.getId());
    }
}
