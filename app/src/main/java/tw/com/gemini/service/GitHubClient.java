package tw.com.gemini.service;


import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import tw.com.gemini.model.GithubRepoList;

public interface GitHubClient {
    String SERVICE_ENDPOINT = "http://api.acme.international";

    @GET("/fortune")
    Observable<List<GithubRepoList>> reposForUser(

    );

}
