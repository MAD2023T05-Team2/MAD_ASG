package sg.edu.np.mad.productivibe_;

import android.content.Context;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

// This class manages API requests to retrieve random quotes from the "https://api.quotable.io/" API endpoin
public class RequestManagerAPI {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.quotable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManagerAPI(Context context){
        this.context = context;
    }

    private interface CallQuotes{
        @GET("random")
        Call<QuoteResponse> callQuotes();
    }

    public void getRandomQuote(Callback<QuoteResponse> callback) {
        Call<QuoteResponse> call = retrofit.create(CallQuotes.class).callQuotes();
        call.enqueue(callback);
    }

}
