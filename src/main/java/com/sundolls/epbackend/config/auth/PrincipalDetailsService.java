package com.sundolls.epbackend.config.auth;

import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public PrincipalDetails loadUserByUsername(Jws<Claims> payload) throws UsernameNotFoundException {
        String username = (String) payload.getBody().get("username");
        String tag = (String) payload.getBody().get("tag");

        Optional<User> optionalUser =  userRepository.findByUsernameAndTag(username, tag);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            return null;
        }

        return new PrincipalDetails(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
