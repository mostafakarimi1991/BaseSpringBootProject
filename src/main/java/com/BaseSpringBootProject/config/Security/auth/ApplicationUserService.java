package com.BaseSpringBootProject.config.Security.auth;

import com.BaseSpringBootProject.config.Security.ApplicationUserRole;
import com.BaseSpringBootProject.entities.ServiceAccess;
import com.BaseSpringBootProject.repositories.UserRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserDao applicationUserDao;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserService(@Qualifier("fake") ApplicationUserDao applicationUserDao, UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        this.applicationUserDao = applicationUserDao;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var dbUser = userRepository.findAllByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found",username)));
        var fetchedDbUser =
                new ApplicationUser(
                        getGrantedAuthorities(dbUser.getRole().getServiceAccessList(), dbUser.getRole().getName()),
                        passwordEncoder.encode(dbUser.getPassword()),
                        dbUser.getUsername(),
                        true,
                        true,
                        true,
                        true
                );
        return fetchedDbUser;
        /*
        //HardCode Username Password
        return applicationUserDao.selectApplicationUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found",username)));
        */
    }

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(List<ServiceAccess> serviceAccessList, String roleName){
        Set<SimpleGrantedAuthority> permissions = serviceAccessList.stream()
                .map(serviceAccess -> new SimpleGrantedAuthority(serviceAccess.getName()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        return permissions;
    }
}
