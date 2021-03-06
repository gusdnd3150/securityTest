package com.example.demo.test.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.test.user.service.UserService;
import com.example.demo.test.user.vo.UserDetailsVO;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;
	
	@NonNull
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		 UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
	        // AuthenticaionFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
	        String userId = token.getName();
	        String userPw = (String) token.getCredentials();
	        // UserDetailsService를 통해 DB에서 아이디로 사용자 조회
	        UserDetailsVO userDetailsVO = (UserDetailsVO) userService.loadUserByUsername(userId);
	        
	        System.out.println("유저:"+userDetailsVO.getUsername());
	        System.out.println("유저:"+userDetailsVO.getPassword());
	        /*if (!passwordEncoder.matches(userPw, userDetailsVO.getPassword())) {
	            throw new BadCredentialsException(userDetailsVO.getUsername() + "Invalid password");
	        }*/

	        return new UsernamePasswordAuthenticationToken(userDetailsVO, userPw, userDetailsVO.getAuthorities());
	}

	 @Override
	    public boolean supports(Class<?> authentication) {
	        return authentication.equals(UsernamePasswordAuthenticationToken.class);
	    }
}
