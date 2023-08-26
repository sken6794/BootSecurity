package com.coding404.demo.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.coding404.demo.command.UserVO;

//이 객체는 화면에 전달이 되는데, 화면에서 여러분이 사용할 값들은 getter로 생성
public class MyUserDetails implements UserDetails{
	
	//멤버변수로 UserVO객체를 받는다.
	private UserVO userVO;
	
	public MyUserDetails(UserVO vo) { //생성자로 유저객체를 받아서 MyUserDetails 가 갖고있는 멤버변수를 전달받은 객체로 대체
		this.userVO = vo;
		
	}
	
	//화면에서 권한도 사용할 수 있게 해주고 싶으면 getter를 만들면 된다
	public String getRole() {
		return userVO.getRole();
	}
	
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		//UserVO가 가지고 있는 권한을 리스트에 담아서 반환시키면, 스프링 시큐리티가 참조해서 사용한다.
		List<GrantedAuthority> list = new ArrayList<>();
		list.add(new GrantedAuthority() {
			//유저의 권한이 GrantedAuthority 여기로 들어가고 그게 리스트에 담김
			@Override
			public String getAuthority() {
				return userVO.getRole();
			}
		});
		
		
	
		return list;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return userVO.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userVO.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// 계정이 만료됐는지 안됐는지?
		return true; //true 면 만료되지않음 false면 만료됨
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true; //계정이 락이 걸리지 않았는지?
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true; // 비밀번호가 만료되지 않았는지?
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true; //사용할 수 있는 계정인지?
	}

}
