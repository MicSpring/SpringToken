package com.subha.security.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

/**
 * Created by user on 8/9/2016.
 */

@Component("authenticationProvider")
class CustomAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    @Qualifier("userDetailsService")
    UserDetailsService userDetailsService

    Authentication authenticate(Authentication authentication)
            throws AuthenticationException{

        println "@@@@@@ The Authentication n authenticate is: ${authentication.getClass()}"
        userDetailsService.loadUserByUsername(authentication.getPrincipal())
        new UsernamePasswordAuthenticationToken("Mic","Mic123")
    }

    boolean supports(Class<?> authentication)
    {
        println "@@@@@@ The authentication in Supports is ${authentication.getClass()}"
        true
    }


}
