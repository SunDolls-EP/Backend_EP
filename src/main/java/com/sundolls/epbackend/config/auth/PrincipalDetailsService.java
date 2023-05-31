package com.sundolls.epbackend.config.auth;

import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
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

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] tagedUsername = username.split(",");
        Optional<User> optionalUser =  userRepository.findByUsernameAndTag(tagedUsername[0],tagedUsername[1]);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            return null;
        }

        return new PrincipalDetails(user);
    }

}
