package tw.com.gemini.angaza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tw.com.gemini.model.GithubRepoList;
import tw.com.gemini.util.FetchDataUtil;

public class MainActivity extends AppCompatActivity {

    FetchDataUtil fetchDataUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchDataUtil = new FetchDataUtil();
        fetchDataUtil.setServerQueryRepoListManagerCallBackFunction(new FetchDataUtil.ServerQueryRepoListManagerCallBackFunction() {


            @Override
            public void setRepoList() {

            }

            @Override
            public void error(boolean isError) {

            }
        });
        fetchDataUtil.fetchDataList();
    }
}
