package com.skilland.game.demo.repository;

import com.skilland.game.demo.model.user.KnownAuthority;
import com.skilland.game.demo.model.user.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Integer> {



    Set<KnownAuthority> ADMIN_AUTHORITIES = EnumSet.of(KnownAuthority.ROLE_ADMIN);

    Optional<UserAuthority> findByValue(KnownAuthority value);

    Stream<UserAuthority> findAllByValueIn(Set<KnownAuthority> values);

}
