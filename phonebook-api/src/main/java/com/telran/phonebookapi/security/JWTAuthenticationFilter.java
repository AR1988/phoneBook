package com.telran.phonebookapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.phonebookapi.dto.ErrorDto;
import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.service.JWTUtil;
import io.swagger.models.HttpMethod;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager,
            ObjectMapper objectMapper, JWTUtil jwtUtil) {
        super(new AntPathRequestMatcher("/api/user/login", HttpMethod.POST.name()));
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res)
            throws AuthenticationException, IOException {
//        try {
            UserDto userDto = objectMapper.readValue(req.getInputStream(), UserDto.class);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDto.email,
                    userDto.password
            );
            return authenticationManager.authenticate(auth);
//        } catch (BadCredentialsException authEx) {
//            logger.warn("Error Authenticating User: " + authEx.getLocalizedMessage());
//            res.setStatus(401);
//            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            res.getWriter().println(objectMapper.writeValueAsString(new ErrorDto("Username or password is incorrect!")));
//            return null;
//        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {

        String accessToken = jwtUtil.generateAccessToken(((User) auth.getPrincipal()).getUsername());
        HttpCookie cookie = jwtUtil.createAccessTokenCookie(accessToken);
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
