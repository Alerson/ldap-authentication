package br.com.authentication.utils;

import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.authentication.model.CustomLdapUserDetails;

//@Slf4j
@Component
public class LdapHelper {

	@Value("${spring.ldap.urls}")
	private String LDAP_URL;

	@Value("${spring.ldap.base-dn}")
	private String LDAP_BASE_DN;

	@Value("${spring.ldap.search}")
	private String LDAP_BASE_SEARCH;

	@Value("${spring.ldap.username}")
	private String LDAP_USER;

	@Value("${spring.ldap.password}")
	private String LDAP_PASSWORD;

	public NamingEnumeration<SearchResult> getLdapContext(String login) {
		NamingEnumeration<SearchResult> results = null;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, LDAP_URL);

		// Authenticate as S. User and password "mysecret"
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, LDAP_USER + "," + LDAP_BASE_SEARCH + "," + LDAP_BASE_DN);
		env.put(Context.SECURITY_CREDENTIALS, LDAP_PASSWORD);

		// Create the initial context
		InitialDirContext ctx;
		try {
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			ctx = new InitialDirContext(env);

			results = ctx.search(LDAP_BASE_SEARCH + "," + LDAP_BASE_DN, "(&(sAMAccountName=" + login + ")(objectclass=user))", searchControls);

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return results;
	}

	public HashMap<String, String> getUserNameFromLDAP() {
		HashMap<String, String> ldapMap = new HashMap<>();
		CustomLdapUserDetails user = (CustomLdapUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		NamingEnumeration<SearchResult> result = getLdapContext(user.getUsername());
		if(result != null) {
			SearchResult searchResult = result.nextElement();
			try {
				ldapMap.put("name", (String) searchResult.getAttributes().get("name").get());
				ldapMap.put("givenName", (String) searchResult.getAttributes().get("givenName").get());
				ldapMap.put("userPrincipalName", (String) searchResult.getAttributes().get("userPrincipalName").get());
				ldapMap.put("amdocs_employee_id", (String) searchResult.getAttributes().get("amdocs-employee-id").get());
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return ldapMap;
	}
}
