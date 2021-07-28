package com.empik.github

import com.empik.github.adapters.db.RequestStatisticsRepository
import com.empik.github.adapters.rest.api.UserDetailsController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GithubApplicationTest extends Specification {

    @Autowired
    private RestTemplate restTemplate
    @Autowired
    private TestRestTemplate testRestTemplate
    @Autowired
    private RequestStatisticsRepository requestStatisticsRepository

    private MockRestServiceServer mockServer

    def setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
        requestStatisticsRepository.deleteAll()
    }

    def "Should return correct response for given Github user and increase statistics in the DB"() {
        given: "User's login"
        def login = "someUser"

        and: "Github API responds with user details and 200 status"
        mockServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8300/users/${login}")))
                  .andRespond(withSuccess(correctUsersResponse(), MediaType.APPLICATION_JSON))

        when: "GET request to /users/{login} endpoint is sent"
        def response = testRestTemplate.getForEntity("/users/{login}", UserDetailsController.UserDetailsResponse, login)
                                       .getBody()

        then: "The correct response is returned"
        response.id == 17217123
        response.login == "KrystianDziedziola"
        response.name == "Krystian Dziędzioła"
        response.type == "User"
        response.avatarUrl.toString() == "https://avatars.githubusercontent.com/u/17217123?v=4"
        response.createdAt.toString() == "2016-02-13T12:27:15Z"
        response.calculations == 84.0
    }

    def "Should create and increase request statistics after request is made"() {
        given: "User's login with no statistics"
        def login = "someUser"
        def statistics = requestStatisticsRepository.findByLogin(login)
        assert statistics.isEmpty()

        and: "Github API responds with user details and 200 status"
        mockServer.expect(ExpectedCount.twice(), requestTo(new URI("http://localhost:8300/users/${login}")))
                  .andRespond(withSuccess(correctUsersResponse(), MediaType.APPLICATION_JSON))

        when: "GET request to /users/{login} endpoint is sent"
        testRestTemplate.getForEntity("/users/{login}", UserDetailsController.UserDetailsResponse, login).getBody()

        then: "The statistics are created with initial value"
        def createdStatistics = requestStatisticsRepository.findByLogin(login)
                                                           .get()
        createdStatistics.login == login
        createdStatistics.requestCount == 1

        when: "Another request is made"
        testRestTemplate.getForEntity("/users/{login}", UserDetailsController.UserDetailsResponse, login).getBody()

        then: "The statistics are increased by 1"
        def increasedStatistics = requestStatisticsRepository.findByLogin(login)
                                                             .get()
        increasedStatistics.login == login
        increasedStatistics.requestCount == 2
    }

    private static String correctUsersResponse() {
        return """
        {
          "login": "KrystianDziedziola",
          "id": 17217123,
          "node_id": "MDQ6VXNlcjE3MjE3MTIz",
          "avatar_url": "https://avatars.githubusercontent.com/u/17217123?v=4",
          "gravatar_id": "",
          "url": "https://api.github.com/users/KrystianDziedziola",
          "html_url": "https://github.com/KrystianDziedziola",
          "followers_url": "https://api.github.com/users/KrystianDziedziola/followers",
          "following_url": "https://api.github.com/users/KrystianDziedziola/following{/other_user}",
          "gists_url": "https://api.github.com/users/KrystianDziedziola/gists{/gist_id}",
          "starred_url": "https://api.github.com/users/KrystianDziedziola/starred{/owner}{/repo}",
          "subscriptions_url": "https://api.github.com/users/KrystianDziedziola/subscriptions",
          "organizations_url": "https://api.github.com/users/KrystianDziedziola/orgs",
          "repos_url": "https://api.github.com/users/KrystianDziedziola/repos",
          "events_url": "https://api.github.com/users/KrystianDziedziola/events{/privacy}",
          "received_events_url": "https://api.github.com/users/KrystianDziedziola/received_events",
          "type": "User",
          "site_admin": false,
          "name": "Krystian Dziędzioła",
          "company": null,
          "blog": "",
          "location": "Zielona Góra",
          "email": null,
          "hireable": null,
          "bio": null,
          "twitter_username": null,
          "public_repos": 12,
          "public_gists": 0,
          "followers": 1,
          "following": 0,
          "created_at": "2016-02-13T12:27:15Z",
          "updated_at": "2020-12-04T20:56:47Z"
        }
        """
    }
}
