package com.skilland.game.demo;

import com.skilland.game.demo.repository.UserAuthorityRepository;
import com.skilland.game.demo.repository.UserRepository;
import com.skilland.game.demo.service.UserService;
import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

//@Configuration
//@ComponentScan("com.skilland.game.demo")
public class UserServiceSetUpConfig {

    //@Bean
    public UserService userService(PasswordEncoder passwordEncoder, UserAuthorityRepository authorityRepository, UserRepository userRepository, Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap){
        return new UserService(userRepository, authorityRepository, passwordEncoder, dataReceiverByUserAuthorityMap);
    }

}
