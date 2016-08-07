package com.subha.security.controller

import com.subha.security.model.AuthenticationRequest
import com.subha.security.model.AuthenticationResponse
import com.subha.security.model.User
import com.subha.security.utils.ConfigConstant
import com.subha.security.utils.TokenUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * Created by user on 8/7/2016.
 */
@RestController
@RequestMapping("/auth")
class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    TokenUtils tokenUtils

    @Autowired
    UserDetailsService userDetailsService

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> authenticateRequest(@RequestBody AuthenticationRequest authenticationRequest) {

        println "************  In AuthenticateRequest  $authenticationRequest"
        println "************  The Authentication Manager ${authenticationManager.getClass()}"
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        println "************  The Authentication class is: ${authentication.getClass()}"
        SecurityContextHolder.getContext().setAuthentication(authentication);


        println "************  The User Details Service is:${userDetailsService.getClass()}"
        // Reload password post-authentication so we can generate token
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token = this.tokenUtils.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new AuthenticationResponse(token));

    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

        println "************  In RefreshToken  ************"

        String token = request.getHeader(ConfigConstant.tokenHeader);
        String username = this.tokenUtils.getUsernameFromToken(token);
        User user = (User)this.userDetailsService.loadUserByUsername(username);

        if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset())) {
            String refreshedToken = this.tokenUtils.refreshToken(token);
            return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }

    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public ResponseEntity<?> testMeFree()
    {
        return ResponseEntity.ok().body("Ok I am Fine")
    }


}
