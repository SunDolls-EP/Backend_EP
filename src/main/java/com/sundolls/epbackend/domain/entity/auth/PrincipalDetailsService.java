package com.sundolls.epbackend.domain.entity.auth;

import com.sundolls.epbackend.domain.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser =  userRepository.findById(username);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            throw new UsernameNotFoundException("USER NOT FOUND");
        }

        return new PrincipalDetails(user);
    }
}
