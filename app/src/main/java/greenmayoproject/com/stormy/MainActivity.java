package greenmayoproject.com.stormy;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather mCurrentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // getActionBar().hide();
        String apiKey = "4ccf8b574f0c5f3f2fb8d0c05bfbc014";
        double latitude = 37.8267;
        double longitude = -122.423;
        //String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +"/" + latitude +","+ longitude;
        String forecastUrl = "https://api.forecast.io/forecast/4ccf8b574f0c5f3f2fb8d0c05bfbc014/37.8267,-122.423";
        OkHttpClient client = new OkHttpClient();

        if(isNetworkAvailble()) {

            Request request = new Request.Builder().url(forecastUrl).build();


            Log.d(TAG, "Main UI Code is running12!");
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, response.body().string());

                        if (response.isSuccessful()) {
                            Log.v(TAG, response.body().string());
                            mCurrentWeather = getCurrentDetails(jsonData);
                        } else {
                            alertUSerAboutError();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Exception caught:", e);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Exception caught:", e);
                    }
                }
            });

        } else {
            Toast.makeText(this,getString(R.string.network_unavailble_message),Toast.LENGTH_LONG).show();
        }
            Log.d(TAG, "Main UI Code is running23!");



    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);

        String timezone = forecast.getString("timezone");
        Log.i(TAG,"From JSON:"+timezone);
        JSONObject currently = forecast.getJSONObject("currently");
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);


        Log.i(TAG,"From H JSON:"+currentWeather.getFormattedTime());




        return currentWeather;

    }

    private boolean isNetworkAvailble() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;

    }

    private void alertUSerAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");

    }
}
