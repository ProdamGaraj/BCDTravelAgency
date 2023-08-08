package core.services;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import core.models.User;
import core.models.UserRole;
import core.repository.UserRepository;

@Service
public class AutorizationService {
    @Autowired
    private UserRepository repository;


    public void login(User user){
        if (user.getPassword()=="admin"){//TODO: refactor
            user.setRole(UserRole.admin);
        }else if (user.getPassword()!=null){
            if (user.getPassword().hashCode()==repository.findById(user.getId()).hashCode()){
                user.setRole(UserRole.partner);
            }
        } else {
            user.setRole(UserRole.client);
        }
    }

    public void register(){

    }
}