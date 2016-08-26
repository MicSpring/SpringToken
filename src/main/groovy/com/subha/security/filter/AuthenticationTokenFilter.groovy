package com.subha.security.filter

import com.subha.security.utils.ConfigConstant
import com.subha.security.utils.TokenUtils
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.context.support.WebApplicationContextUtils

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by user on 8/7/2016.
 */
class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter{

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    def logger = LogFactory.getLog(AuthenticationTokenFilter);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        logger.info "AuthenticationTokenFiler Called......"

        tokenUtils = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext())
                .getBean(TokenUtils.class);

        userDetailsService = WebApplicationContextUtils
                .getRequiredWebApplicationContext(this.getServletContext())
                .getBean(UserDetailsService.class)//("userDetailsService");

        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " + ConfigConstant.tokenHeader);


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(ConfigConstant.tokenHeader);
        String username = this.tokenUtils.getUsernameFromToken(authToken);

        logger.info "****** Authentication:  ${SecurityContextHolder.getContext().getAuthentication()} \n ****** UserName: $username"

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            logger.info "  ****** In Custom Filter Hurrah!!! Token Present"

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.tokenUtils.validateToken(authToken, userDetails)) {

                logger.info "  ****** In Custom Filter Hurrah!!! Valid Token Present"


                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }



}
