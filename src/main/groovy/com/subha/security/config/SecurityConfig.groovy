package com.subha.security.config

import com.subha.security.filter.AuthenticationTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * Created by user on 8/7/2016.
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter  {

    /*@Autowired
    private UserDetailsService userDetailsService;
*/
    /*@Autowired
    @Qualifier("userDetailsService2")
    private UserDetailsService userDetailsService2;*/

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint

    @Autowired
    AuthenticationProvider authenticationProvider

   /* @Autowired
    private AuthenticationManagerBuilder $authManagerBuilder*/

    /**
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     *
     * This is another way autowiring an instance and configuring it at the same time
     *
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        println "***********  The Authentication Manager Builder is: ${authenticationManagerBuilder.getClass()}"
        //println "*********** AuthManagerBuilder: $authManagerBuilder"

        authenticationManagerBuilder.authenticationProvider(authenticationProvider)

       /* authenticationManagerBuilder
                .userDetailsService(userDetailsService2)*/
                /*.passwordEncoder(new BCryptPasswordEncoder());*/


    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        def authenticationManager = super.authenticationManagerBean()
        authenticationManager
    }


    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(super.authenticationManagerBean());
        return authenticationTokenFilter;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated();

        // Custom JWT based authentication
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter);
    }
}
