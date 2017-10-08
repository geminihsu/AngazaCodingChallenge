package tw.com.gemini.angaza;

import android.os.CountDownTimer;
import android.provider.FontsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.toString();
    private TextView content;
    private ImageButton imageButton;

    private RequestQueue mQueue;
    private StringRequest getRequest;

    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = (TextView)findViewById(R.id.content);
        imageButton = (ImageButton)findViewById(R.id.fetch);

        //If data already exit, it replace the original text
        if (savedInstanceState != null) {
            if(savedInstanceState.getString("content").equals(getString(R.string.loading)))
                sendRequest();
            else
                content.setTextSize(30);
            content.setText(savedInstanceState.getString("content"));
        }

        if (countDownTimer == null) {
            checkError();
        }
        setLister();
    }

    @Override
    protected void onStart()
    {

        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("content", content.getText().toString());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        content.setText(savedInstanceState.getString("content"));
    }
    @Override
    protected void onStop()
    {
      if(countDownTimer!=null)
      {
          countDownTimer.cancel();
          countDownTimer = null;
      }

      if(mQueue !=null) {
          mQueue.cancelAll(getRequest);
          getRequest = null;
          mQueue = null;
      }
      super.onStop();
    }
    private void setLister()
    {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.setTextSize(50);
                content.setText(getString(R.string.loading));
                sendRequest();
                countDownTimer.start();
            }
        });


    }


    private void checkError()
    {
        countDownTimer = new CountDownTimer(10000,1000){

            @Override
            public void onFinish() {
                if(content.getText().equals(getString(R.string.loading)))
                {
                    content.setTextSize(30);
                    content.setText(getString(R.string.defaultText));
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // mTextView.setText("seconds remaining:"+millisUntilFinished/1000);
            }

        }.start();
    }
    private void sendRequest()
    {
        mQueue = Volley.newRequestQueue(this);
        getRequest = new StringRequest("http://api.acme.international/fortune",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            String message = obj.getString("fortune");
                            content.setTextSize(30);
                            content.setText(message);
                            Log.e("",message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(countDownTimer!=null)
                            countDownTimer.cancel();
                        sendRequest();
                        if(countDownTimer == null)
                            checkError();
                        else
                            countDownTimer.start();
                        Log.e(TAG,volleyError.toString()+",time:"+volleyError.getNetworkTimeMs());
                    }
                });
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(getRequest);
    }
}
