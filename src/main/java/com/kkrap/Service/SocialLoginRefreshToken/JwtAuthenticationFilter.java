package com.kkrap.Service.SocialLoginRefreshToken;

import com.kkrap.Entity.Users;
import com.kkrap.Service.Users.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsersService usersService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   UsersService usersService) {
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 제외
            try {
                jwtUtil.validateToken(token);
                Long userId = jwtUtil.getUserIdFromToken(token);
                Users user = usersService.findById(userId); // 사용자 조회

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user.getUserId(), null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception ex){
                SecurityContextHolder.clearContext();
            }

        }

        filterChain.doFilter(request, response);
    }
}