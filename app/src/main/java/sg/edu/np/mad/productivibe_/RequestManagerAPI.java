package sg.edu.np.mad.productivibe_;

import android.content.Context;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

// This class manages API requests to retrieve random quotes from the "https://api.quotable.io/" API endpoint
public class RequestManagerAPI {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()  // Create a Retrofit instance for making API calls
            .baseUrl("https://api.quotable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManagerAPI(Context context){
        this.context = context;
    }

    private interface CallQuotes{ // Interface to define the API endpoint and request method
        @GET("random")
        Call<QuoteResponse> callQuotes();
    }

    public void getRandomQuote(Callback<QuoteResponse> callback) { // Method to fetch a random quote from the API and handle the response using a callback
        Call<QuoteResponse> call = retrofit.create(CallQuotes.class).callQuotes(); // Create call object for the API request
        call.enqueue(callback); // Enqueue the API call and attach a callback to handle the response asynchronously
    }

}
