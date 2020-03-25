package br.com.authentication.configuration;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import br.com.authentication.model.CustomLdapUserDetails;
import br.com.authentication.utils.LdapHelper;

@Configuration
@EnableWebSecurity
@ConfigurationProperties(prefix = "spring.ldap")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	LdapHelper ldapHelper;

	@Value("${spring.ldap.urls}")
	private String LDAP_URL;

	@Value("${spring.ldap.base-dn}")
	private String LDAP_BASE_DN;

	@Value("${spring.ldap.search}")
	private String LDAP_BASE_SEARCH;

	@Value("${spring.ldap.search-filter}")
	private String LDAP_SEARCH_FILTER;

	@Value("${spring.ldap.username}")
	private String LDAP_USER;

	@Value("${spring.ldap.password}")
	private String LDAP_PASSWORD;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers("/logged").fullyAuthenticated()
		.and()
			.formLogin().loginPage("/login").defaultSuccessUrl("/logged", true)
		.and()
			.logout()
		.and()
			.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.ldapAuthentication()
			.userDetailsContextMapper(userDetailsContextMapper())
			.userSearchFilter(LDAP_SEARCH_FILTER).contextSource(getContext());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/resources/**").anyRequest();
		web.ignoring().antMatchers("/resources/**");
	}

	private DefaultSpringSecurityContextSource getContext() {
		DefaultSpringSecurityContextSource defaultSpringSecurityContextSource = new DefaultSpringSecurityContextSource(Arrays.asList(LDAP_URL), LDAP_BASE_DN);
		defaultSpringSecurityContextSource.setBase(LDAP_BASE_SEARCH + "," + LDAP_BASE_DN);
		defaultSpringSecurityContextSource.setUserDn(LDAP_USER + "," + LDAP_BASE_SEARCH + "," + LDAP_BASE_DN);
		defaultSpringSecurityContextSource.setPassword(LDAP_PASSWORD);
		defaultSpringSecurityContextSource.setAnonymousReadOnly(false);
		defaultSpringSecurityContextSource.setPooled(true);
		defaultSpringSecurityContextSource.afterPropertiesSet();
		return defaultSpringSecurityContextSource;
	}

	@Bean
	public UserDetailsContextMapper userDetailsContextMapper() {
		return new LdapUserDetailsMapper() {

			@Override
			public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
				UserDetails details = super.mapUserFromContext(ctx, username, authorities);
				return new CustomLdapUserDetails((LdapUserDetails) details); 
				} 
			};

	}
}
