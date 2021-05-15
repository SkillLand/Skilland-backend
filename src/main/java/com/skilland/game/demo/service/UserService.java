package com.skilland.game.demo.service;


import com.skilland.game.demo.exception.GameDataException;
import com.skilland.game.demo.exception.UserExceptions;
import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.model.TopicEntity;
import com.skilland.game.demo.model.gameroom.CourseEntity;
import com.skilland.game.demo.model.gameroom.GameEntity;
import com.skilland.game.demo.model.gameroom.GameJsonEntity;
import com.skilland.game.demo.model.gameroom.req.CreateGameRequest;
import com.skilland.game.demo.model.gameroom.resp.CourseResponse;
import com.skilland.game.demo.model.gameroom.resp.GameResponse;
import com.skilland.game.demo.model.gameroom.resp.NewCourseResponse;
import com.skilland.game.demo.model.user.*;
import com.skilland.game.demo.model.user.request.SaveUserRequest;
import com.skilland.game.demo.model.user.resp.UserResponse;
import com.skilland.game.demo.repository.*;
//import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import com.skilland.game.demo.util.GrigorianCalendarDateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserAuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap;


    @Autowired
    public UserService(UserRepository userRepository, UserAuthorityRepository authorityRepository,
                       PasswordEncoder passwordEncoder, Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.dataReceiverByUserAuthorityMap = dataReceiverByUserAuthorityMap;
    }

    @Transactional
    public UserResponse create(SaveUserRequest request) {
        validateUniqueFields(request);
        Map<KnownAuthority, UserAuthority> authorities = null;
        String authority = request.getAuthority();
        if(authority.equals("teacher")){
            authorities = this.getTeacherAuthorities();
        }else if (authority.equals("student")){
            authorities = this.getStudentAuthorities();
        }else {
            throw UserExceptions.authorityNotFound(request.getAuthority());
        }

        GameUserEntity gameUserEntity = save(request, authorities);
        System.out.println("USER ENTITY " + gameUserEntity.getFirstName() + gameUserEntity.getEmail() + gameUserEntity.getId());
        return UserResponse.fromUser(gameUserEntity);
    }


    @Transactional(propagation = Propagation.NEVER)
    public GameUserEntity save(SaveUserRequest request, Map<KnownAuthority, UserAuthority> authorities) {
        DataReceiverByUserAuthority dataReceiver = this.getDataReceiver(authorities.keySet().stream().findFirst().get().getAuthority());
        GameUserEntity user = dataReceiver.createEmptyUser();
        user.getAuthorities().putAll(authorities);
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
        return user;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        GameUserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User with mail " + email + " not found"));
        Set<KnownAuthority> grantedAuthorities = EnumSet.copyOf(user.getAuthorities().keySet());
        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserResponse::fromUser);
    }

    @Transactional(readOnly = true)
    public GameUserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow( () -> UserExceptions.userNotFound(id.toString()));
    }

    private void validateUniqueFields(SaveUserRequest request) {
        String email = request.getEmail();
        System.out.println(email);
        if (userRepository.existsByEmail(email)) {
            throw UserExceptions.duplicateEmail(email);
        }
    }

    private DataReceiverByUserAuthority getDataReceiver(String authority){
        DataReceiverByUserAuthority dataReceiver = this.dataReceiverByUserAuthorityMap.get(authority);
        if(dataReceiver == null){
            throw UserExceptions.authorityNotFound(authority);
        }
        return dataReceiver;
    }

    public void save(GameUserEntity gameUserEntity){
        userRepository.save(gameUserEntity);
    }


    private Map<KnownAuthority, UserAuthority> getStudentAuthorities() {
        UserAuthority authority = authorityRepository
                .findByValue(KnownAuthority.ROLE_STUDENT)
                .orElseThrow(() -> UserExceptions.authorityNotFound(KnownAuthority.ROLE_STUDENT.name()));

        Map<KnownAuthority, UserAuthority> authorities = new EnumMap<>(KnownAuthority.class);
        authorities.put(KnownAuthority.ROLE_STUDENT, authority);
        return authorities;
    }

    private Map<KnownAuthority, UserAuthority> getTeacherAuthorities() {
        UserAuthority authority = authorityRepository
                .findByValue(KnownAuthority.ROLE_TEACHER)
                .orElseThrow(() -> UserExceptions.authorityNotFound(KnownAuthority.ROLE_TEACHER.name()));

        Map<KnownAuthority, UserAuthority> authorities = new EnumMap<>(KnownAuthority.class);
        authorities.put(KnownAuthority.ROLE_TEACHER, authority);
        return authorities;
    }




}
