package com.ssuog.service;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LDAPService {

	/**
	 * LDAP login
	 * @param userName
	 * @param password
	 * @return
	 */
	public LdapContext adLogin(String username, String password) {
		String server = "ldap://dv-srv1.gla.ac.uk:389/" + "cn="+username+",ou=student,o=gla";
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			// LDAP factory
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			// Type of validation: "none", "simple", "strong"
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put("com.sun.jndi.ldap.connect.pool", "true");
		    env.put("java.naming.referral", "follow");
			// url fomat：protocal://ip:port/start,scope ,Connect directly to a domain or group
			env.put(Context.PROVIDER_URL, server);
			// username, cn,ou,dc : user, domain, group
			env.put(Context.SECURITY_PRINCIPAL, username);
/*			env.put(Context.SECURITY_PRINCIPAL, "cn="+username+",ou=student,o=gla");
*/			// user cn's password
			env.put(Context.SECURITY_CREDENTIALS, password);
			
			LdapContext ldapContext = new InitialLdapContext(env, null);
			System.out.println("ldapContext:" + ldapContext);
			System.out.println("user" + username + "Login validation successful");
			return ldapContext;
		} catch (NamingException e) {
			e.printStackTrace();
			System.out.println("user" + username + "Login validation failed");
			return null;
		}
	}

	/**
	 * query student by username
	 * @param username
	 * @param password
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getUserKey(String username, String password) {
		System.out.println("ad information to query: " + username);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		LdapContext ldapContext = adLogin(username, password); // Connect to domain
		if (ldapContext != null) {
			String company = "";
			try {
				// domain node
				String searchBase = "o=gla";
				// LDAP Search filter class
				// cn=*name* fuzzy query
				// cn=name precise query
				// String searchFilter = "(objectClass="+type+")";
				String searchFilter = "(cn=" + username + ")"; // Query domain account

				// Create a search controller
				SearchControls searchCtls = new SearchControls();
				String returnedAtts[] = { "uid", "fullName", "dn" };
				searchCtls.setReturningAttributes(returnedAtts); // Sets the fields specified for return, or returns all if not set
				// Set the search depth
				searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				// The depth is obtained by searching LDAP based on the set domain node, filter class, and search controller
				NamingEnumeration answer = ldapContext.search(searchBase,
						searchFilter, searchCtls);
				// Initialize the number of search results to 0
				int totalResults = 0;
				int rows = 0;
				// Traverses the result set
				while (answer.hasMoreElements()) {
					SearchResult sr = (SearchResult) answer.next();// Get DN that meets the search criteria
					++rows;
					String dn = sr.getName();
					System.out.println(dn);
					Attributes Attrs = sr.getAttributes();// Get the set of attributes that meet the conditions
					if (Attrs != null) {
						try {
							for (NamingEnumeration ne = Attrs.getAll(); ne
									.hasMore();) {
								Attribute Attr = (Attribute) ne.next();// get the next property
								// Read property values
								for (NamingEnumeration e = Attr.getAll(); e
										.hasMore(); totalResults++) {
									company = e.next().toString();
									resultMap.put(Attr.getID(),
											company.toString());
								}
							}
						} catch (NamingException e) {
							e.printStackTrace();
							System.out.println("Throw Exception : "
									+ e.getMessage());
						}
					}
				}
				System.out.println("total number of users: " + rows);
			} catch (NamingException e) {
				e.printStackTrace();
				System.out.println("Throw Exception : " + e.getMessage());
			} finally {
				try {
					ldapContext.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return resultMap;
	}

	public static void main(String[] args) {
		LDAPService service = new LDAPService();
		Map<String, Object> userKey = service.getUserKey("2382495x", "tony960902");
		System.out.println("==================================");
		for (Entry<String, Object> entry : userKey.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
}
