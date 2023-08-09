package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.User;
import bcd.solution.dvgKiprBot.core.models.UserRole;
import bcd.solution.dvgKiprBot.core.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUserIfNotExists(Long telegram_id, String username) {
        if (userRepository.existsById(telegram_id)) {
            return userRepository.getReferenceById(telegram_id);
        }
        User user = new User(telegram_id, username, null, UserRole.client);
        userRepository.save(user);
        return user;
    }
}
