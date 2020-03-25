package br.com.authentication.model;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

public class CustomLdapUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LdapUserDetails details;

	@Value("${spring.ldap.username}")
	private String LDAP_USER;

	public CustomLdapUserDetails(LdapUserDetails details) {
		this.details = details;
	}

	public boolean isEnabled() {
		return details.isEnabled() && getUsername().equals(LDAP_USER);
	}

	public String getDn() {
		return details.getDn();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return details.getAuthorities();
	}

	public String getPassword() {
		return details.getPassword();
	}

	public String getUsername() {
		return details.getUsername();
	}

	public boolean isAccountNonExpired() {
		return details.isAccountNonExpired();
	}

	public boolean isAccountNonLocked() {
		return details.isAccountNonLocked();
	}

	public boolean isCredentialsNonExpired() {
		return details.isCredentialsNonExpired();
	}

}
