package com.subha.security.utils

import org.apache.commons.logging.LogFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by user on 8/7/2016.
 */
@Component(value = "authenticationEntryPoint")
class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint{

    def logger = LogFactory.getLog(EntryPointUnauthorizedHandler)

    @Override
    void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.info(" *** In Auth EntryPoint with Exception $authException")
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied")
    }
}
