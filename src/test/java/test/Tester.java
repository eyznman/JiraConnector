package test;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.util.concurrent.Promise;

import atlassian.jira.client.CustomJiraRestClient;

public class Tester {
	public static void main(String[] args) throws Exception {

		User user = CustomJiraRestClient.getUser();
		JiraRestClient client = CustomJiraRestClient.getJiraRestClient();

		for (BasicProject project : client.getProjectClient().getAllProjects().claim()) {
			System.out.println(project.getKey() + ": " + project.getName());
		}

		Promise<SearchResult> searchJqlPromise = client.getSearchClient().searchJql(
				"project = MYPURRJECT AND status in (Closed, Completed, Resolved) ORDER BY assignee, resolutiondate");

		for (Issue issue : searchJqlPromise.claim().getIssues()) {
			System.out.println(issue.getSummary());
		}

		// Print the result
		System.out.println(String.format("Your admin user's email address is: %s\r\n", user.getEmailAddress()));

		// Done
		System.out.println("Example complete. Now exiting.");
		System.exit(0);
	}
}
