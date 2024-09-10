package com.dev02.TimeTrackingApp.security.service;

import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameEquals(username);

        if(user != null){
            return new UserDetailsImpl(
                    user.getUserId(),
                    user.getUsername(),
                    user.getFirstName(),
                    false,
                    user.getPassword(),
                    user.getUserRole().getRoleType().name(),
                    user.getSsn());
        }
        throw new UsernameNotFoundException("User' " + username + " not found");
    }
}
