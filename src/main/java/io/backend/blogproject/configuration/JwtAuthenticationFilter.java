package io.backend.blogproject.configuration;


import io.backend.blogproject.common.CustomException;
import io.backend.blogproject.common.JwtCookie;
import io.backend.blogproject.domain.entity.DefaultCurrentUser;
import io.backend.blogproject.service.MemberService;
import io.backend.blogproject.service.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String extractedToken = extractToken(request);

        try {
            if(extractedToken != null && tokenProvider.validate(extractedToken)){
                Long foundedUserId = tokenProvider.parseJwtToUserId(extractedToken);
                DefaultCurrentUser defaultCurrentUser = memberService.loadUserDetailsFromMember(foundedUserId);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        defaultCurrentUser,
                        null,
                        defaultCurrentUser.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        } catch (CustomException exception) {
            SecurityContextHolder.clearContext();
            response.addHeader(
                    HttpHeaders.SET_COOKIE,
                    JwtCookie.expired(request.isSecure()).toString()
            );
        }
        filterChain.doFilter(request,response);

    }
    public String extractToken(HttpServletRequest httpServletRequest){
        String bearerToken = httpServletRequest.getHeader(
                HttpHeaders.AUTHORIZATION
        );
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        if (httpServletRequest.getCookies() == null) {
            return null;
        }

        return Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie -> JwtCookie.ACCESS_TOKEN.equals(cookie.getName()))
                .map(cookie -> cookie.getValue())
                .findFirst()
                .orElse(null);
    }
}
