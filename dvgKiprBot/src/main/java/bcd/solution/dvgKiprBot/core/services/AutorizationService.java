package bcd.solution.dvgKiprBot.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import bcd.solution.dvgKiprBot.core.models.User;
import bcd.solution.dvgKiprBot.core.models.UserRole;
import bcd.solution.dvgKiprBot.core.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AutorizationService {
    @Autowired
    private UserRepository repository;


   // public void login(User user){
   //     if (user.getPassword()=="admin"){//TODO: refactor
   //         user.setRole(UserRole.admin);
   //     }else if (user.getPassword()!=null){
   //         if (user.getPassword().hashCode()==repository.findById(user.getId()).hashCode()){
   //             user.setRole(UserRole.partner);
   //         }
   //     } else {
   //         user.setRole(UserRole.client);
   //     }
   // }

   // public void register(){

   // }
}
