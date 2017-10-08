package tw.com.gemini.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tw.com.gemini.model.GithubRepoList;
import tw.com.gemini.service.GitHubClient;
import tw.com.gemini.service.ServiceFactory;

/**
 * Created by geminihsu on 05/09/2017.
 */

public class FetchDataUtil {
    private final String TAG = this.getClass().getSimpleName();
    private int count;

    public void fetchDataList()
    {

        final List<GithubRepoList> result = new ArrayList<>();
        GitHubClient service = ServiceFactory.createRetrofitService(GitHubClient.class, GitHubClient.SERVICE_ENDPOINT);
        service.reposForUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GithubRepoList>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("GithubDemo", e.toString());
                        if(mServerQueryRepoListManagerCallBackFunction != null)
                            mServerQueryRepoListManagerCallBackFunction.error(true);

                        }

                    @Override
                    public void onNext(List<GithubRepoList> githubRepoLists) {



                        if(mServerQueryRepoListManagerCallBackFunction != null) {
                            mServerQueryRepoListManagerCallBackFunction.setRepoList();
                        }
                    }
                });

    }

    private ServerQueryRepoListManagerCallBackFunction mServerQueryRepoListManagerCallBackFunction;

    public void setServerQueryRepoListManagerCallBackFunction(ServerQueryRepoListManagerCallBackFunction serverQueryNewsListManagerCallBackFunction) {
        mServerQueryRepoListManagerCallBackFunction = serverQueryNewsListManagerCallBackFunction;

    }

    public interface ServerQueryRepoListManagerCallBackFunction {
        public void setRepoList();
        public void error(boolean isError);

    }

}
