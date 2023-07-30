package sg.edu.np.mad.productivibe_;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
This activity displays the splash screen that is shown whenever the user opens the application
 */
public class SplashScreen extends AppCompatActivity {
    ProgressBar progBar;
    int counter = 0;
    private static final int SPLASH_DURATION = 3700;
    String TITLE = "SplashScreen";
    private TextView destressQuote;
    RequestManagerAPI requestManagerAPI;

    public SplashScreen() {
    }
    public SplashScreen(RequestManagerAPI requestManagerAPI) {
        this.requestManagerAPI = requestManagerAPI;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        destressQuote = findViewById(R.id.destressQuote);
        fetchQuoteFromAPI();

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBarAnimate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, LoginPage.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DURATION);
    }

    // This animates the progress bar, showing users the loading progress, enhancing user experience
    public void progressBarAnimate() {
        progBar = findViewById(R.id.progressBar);
        final Timer progTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                progBar.setVisibility(View.VISIBLE);
                progBar.setProgress(counter);
                if (counter == 100) {
                    progTimer.cancel();
                }
            }
        };
        progTimer.schedule(timerTask, 0, 30);
        Log.v(TITLE, "Splash done");
    }

    // Calling of API to fetch the quote using Retrofit
    private void fetchQuoteFromAPI() {
        // Create an instance of RequestManagerAPI
        RequestManagerAPI requestManagerAPI = new RequestManagerAPI(this);
        // Make the API request and handle the response using a Callback object
        requestManagerAPI.getRandomQuote(new Callback<QuoteResponse>() {
            @Override
            public void onResponse(Call<QuoteResponse> call, Response<QuoteResponse> response) {
                if (response.isSuccessful()) {
                    QuoteResponse randomQuote = response.body();
                    if (randomQuote != null) {
                        // Get the quote content and author from the response
                        String content = randomQuote.getContent();
                        String author = randomQuote.getAuthor();

                        // Update the TextViews with the quote content and author
                        destressQuote.setText(content + " ~"+author);
                    }
                } else {
                    // Handle error if the request was not successful
                    destressQuote.setText("");
                    Log.v(TITLE, "api request not successful");
                }
            }

            @Override
            public void onFailure(Call<QuoteResponse> call, Throwable t) {
                // Handle failure (e.g., network error)
                destressQuote.setText("");
                Log.v(TITLE, "api network error");
            }
        });
    }
}
