package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.*;
import bcd.solution.dvgKiprBot.core.repository.ActivityRepo;
import bcd.solution.dvgKiprBot.core.repository.StateMachineRepo;
import bcd.solution.dvgKiprBot.core.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StateMachineService {
    private final StateMachineRepo stateMachineRepo;
    private final UserRepository userRepository;
    private final ActivityRepo activityRepo;
    private final UserService userService;

    public StateMachineService(StateMachineRepo stateMachineRepo,
                               UserRepository userRepository,
                               ActivityRepo activityRepo,
                               UserService userService) {
        this.stateMachineRepo = stateMachineRepo;
        this.userRepository = userRepository;
        this.activityRepo = activityRepo;
        this.userService = userService;
    }

    @Async
    protected StateMachine getOrAddIfNotExists(Long id) {
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
    public StateMachine setWaitPhoneByUserId(Long id, boolean waitPhone, Integer messageId) {
        StateMachine stateMachine = getOrAddIfNotExists(id);
        stateMachine.setWaitPhone(waitPhone);
        stateMachine.setPhoneMessageId(messageId);
        stateMachineRepo.save(stateMachine);
        return stateMachine;
    }

    @Async
    public StateMachine getByUserId(Long id) {
        return getOrAddIfNotExists(id);
    }

    @Async
    public StateMachine getOrAddIfNodeExist(Long id, String username) {
        userService.addUserIfNotExists(id, username);
        return getByUserId(id);
    }

    @Async
    public void addActivityByIdByUserId(Long userId, Long activityId) {
        Activity activity = activityRepo.getReferenceById(activityId);
        StateMachine stateMachine = getOrAddIfNotExists(userId);

        stateMachine.activities.add(activity);
        stateMachineRepo.save(stateMachine);

    }

    @Async
    public void clearStateByUserId(Long userId) {
        StateMachine stateMachine = getOrAddIfNotExists(userId);
        stateMachineRepo.delete(stateMachine);
    }

    @Async
    public void setResortByUserId(Resort resort, Long userId) {
        StateMachine userState = getByUserId(userId);
        userState.resort = resort;
        stateMachineRepo.save(userState);
    }

    @Async
    public void setHotelByUserId(Hotel hotel, Long userId) {
        StateMachine userState = getByUserId(userId);
        userState.hotel = hotel;
        stateMachineRepo.save(userState);
    }

    @Async
    public void setCustomTourByUserId(CustomTour customTour, Long userId) {
        StateMachine userState = getByUserId(userId);
        userState.customTour = customTour;
        stateMachineRepo.save(userState);
    }

    @Async
    public void removeActivityFromStateByUserId(Activity deletingActivity, Long userId) {
        StateMachine stateMachine = getOrAddIfNotExists(userId);
        stateMachine.activities.removeIf(activity -> Objects.equals(deletingActivity.getId(), activity.getId()));
        stateMachineRepo.save(stateMachine);
    }

    @Async
    public String getManagerCardByUserId(Long userId) {
        StateMachine stateMachine = getOrAddIfNotExists(userId);
        StringBuilder card = new StringBuilder("Пользователь @" + stateMachine.user.getLogin() + " подобрал тур:\n\n");

        if (stateMachine.customTour != null) {
            card.append("Авторский тур: ").append(stateMachine.customTour.name).append("\n\n");
        } else {
            card
                    .append("Курорт: ").append(stateMachine.resort.name).append("\n")
                    .append("Отель: ").append(stateMachine.hotel.name).append("\n\n");
        }

        card.append("Вскоре он с Вами свяжется для завершения оформления тура.");
        return card.toString();
    }

    @Async
    public String getUserCardByUserId(Long userId, String managerUsername) {
        StateMachine stateMachine = getOrAddIfNotExists(userId);
        StringBuilder card = new StringBuilder("Спасибо, что выбрали нас!\n\nВаш выбор:\n");

        if (stateMachine.customTour != null) {
            card.append("Авторский тур: ").append(stateMachine.customTour.name).append("\n\n");
        } else {
            card
                    .append("Курорт: ").append(stateMachine.resort.name).append("\n")
                    .append("Отель: ").append(stateMachine.hotel.name).append("\n\n");
        }

        card.append("Обратитесь к менеджеру (@")
                .append(managerUsername)
                .append(") для оформления выбранного тура");
        return card.toString();
    }
}
