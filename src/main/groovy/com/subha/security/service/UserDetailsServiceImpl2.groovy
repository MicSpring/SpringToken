package com.subha.security.service

import com.subha.security.model.User
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Created by user on 8/10/2016.
 */
//@Service("userDetailsService2")
class UserDetailsServiceImpl2 implements UserDetailsService{
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        println "************ userDetailsService 2 called............"

        return new User(
                999,
                "MIC999",
                "MIC123456",
                null,
                null,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_A")
        );
    }
}
