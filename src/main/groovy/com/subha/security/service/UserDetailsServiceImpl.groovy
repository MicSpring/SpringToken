package com.subha.security.service

import com.subha.security.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Created by user on 8/7/2016.
 */
@Service("userDetailsService")
class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    User user

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        println "************ userDetailsService called............"

          return new User(
                    999,
                    "MIC",
                    "MIC123",
                    null,
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_A")
            );
        }
    }