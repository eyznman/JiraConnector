package atlassian.jira.client;

import java.io.File;
import java.net.URI;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.fasterxml.jackson.databind.ObjectMapper;

import atlassian.jira.configuration.JiraProperies;
import utils.json.JsonUtils;

public class CustomJiraRestClient {
	
	private static JiraProperies jiraProperties = setJiraProperties();
	private static JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
	private static JiraRestClient client = setClient();
	

	private static JiraRestClient setClient() {
		try {
			return client != null ? client
					: factory.createWithBasicHttpAuthentication(new URI(jiraProperties.getJiraUrl()),
							jiraProperties.getUsername(), jiraProperties.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static JiraProperies setJiraProperties() {
		ObjectMapper jsonUtils = JsonUtils.getIgnoreUnknownPropertiesJsonMapper();
		JiraProperies jiraProperties = null;
		try {
			jiraProperties = jsonUtils.readValue(new File("configuration/jira_settings.json"), JiraProperies.class);
			System.out.println(jiraProperties.getJiraUrl());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jiraProperties;
	}
		
	

	public static JiraRestClient getJiraRestClient() {
		return client;
	}
	
	

	public static User getUser(String byUserName) {
		Promise<User> promise = client.getUserClient().getUser(byUserName);
		return promise.claim();
	}
	
	

	public static User getUser() {
		Promise<User> promise = client.getUserClient().getUser(jiraProperties.getUsername());
		System.out.println(jiraProperties.getUsername());
		return promise.claim();
	}
	
	
	
	
	
	
	
}