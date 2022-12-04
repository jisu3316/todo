//package com.example.barckend.security;
//
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class ServletFilter extends HttpFilter {
//
//    private TokenProvider tokenProvider;
//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilter(request, response, chain);
//        try {
//            final String token = parseBearerToken(request);
//
//            if (token != null && !token.equalsIgnoreCase("null")) {
//                //userID가져오기 위조된 경우 예외 처리된다.
//                String userId = tokenProvider.validateAndGetUserId(token);
//
//                //다음 ServletFilter 실행
//                chain.doFilter(request, response);
//            }
//        } catch (Exception e) {
//            //예외시 403
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        }
//    }
//
//    private String parseBearerToken(HttpServletRequest request) {
//        //Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
//        String bearerToken = request.getHeader("Authorization");
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}
