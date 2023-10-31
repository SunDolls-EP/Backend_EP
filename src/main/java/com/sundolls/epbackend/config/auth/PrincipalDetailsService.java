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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] str = username.split(",");
        String name = str[0];
        String tag = str[1];
        User user =  userRepository.findByUsernameAndTag(name, tag)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return new PrincipalDetails(user);
    }

}
