package ru.dondev.myapplication.geotask.app;

/**
 * Created by artem on 24.06.14.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends FragmentActivity {

    private static final String TAG = "myLogs";
    private GoogleMap map;
    private DirectionsJSONParser directionsJSONParser = new DirectionsJSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        Intent intent = getIntent();
        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        if (map == null) {
            finish();
            return;
        }

//        map.setOnMapClickListener(new OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                //Creating a marker
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                //Setting the position for the marker
//                markerOptions.position(latLng);
//
//                // Setting the title for the marker.
//                // This will be displayed on taping the marker
//                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//
//                // Clears the previously touched position
//                map.clear();
//
//                // Animating to the touched position
//                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//
//                map.addMarker(markerOptions);
//            }
//        });
        // rt.calculateRoute(55.772935, 37.594272, 55.88459, 37.4263165);
        //Получение значений полей из MainActivity
        String url = getDirectionUrl(intent.getStringExtra("from"),intent.getStringExtra("to"));

        //Вызов AsyncTask
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String getDirectionUrl(String from, String to) {
        //Формирование url запроса
        StringBuilder origin = new StringBuilder();
        origin.append(from);

        StringBuilder destination = new StringBuilder();
        destination.append(to);

        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("origin", origin.toString()));
        nameValuePairs.add(new BasicNameValuePair("destination", destination.toString()));
        nameValuePairs.add(new BasicNameValuePair("sensor", "false"));

        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        String url = "http://maps.googleapis.com/maps/api/directions/json?" + paramString;
        Log.d(TAG, url);
        return url;
    }

    private class DownloadTask extends AsyncTask<String, String, List<LatLng>> {
        private String content;

        @Override
        protected List<LatLng> doInBackground(String... urls) {
            List<LatLng> polyline = new ArrayList<LatLng>();
            //Создание соединения
            try {
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpPost httpPost = new HttpPost(urls[0]);

                content = defaultHttpClient.execute(httpPost, responseHandler);
                polyline = directionsJSONParser.parse(content);
                Log.d(TAG, content);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return polyline;
        }

        @Override
        protected void onPostExecute(List<LatLng> polyline) {
            super.onPostExecute(polyline);
            try {
                //Получение прямоугольника отрисовки карты
                JSONObject bounds = directionsJSONParser.getBounds();
                double latSouthwest = bounds.getJSONObject("southwest").getDouble("lat");
                double lngSouthwest = bounds.getJSONObject("southwest").getDouble("lng");
                double latNortheast = bounds.getJSONObject("northeast").getDouble("lat");
                double lngNortheast = bounds.getJSONObject("northeast").getDouble("lng");
                cameraUpdate(latSouthwest, lngSouthwest, latNortheast, lngNortheast);
                Log.d(TAG, bounds.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setPolyline(polyline);
        }
    }

    //	public void getPlace(double lat, double lng) {
//		map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
//	}
    //Отрисовка полилинии
    private void setPolyline(List<LatLng> polyline) {
        map.addPolyline((new PolylineOptions().color(Color.BLACK).width(5)).addAll(polyline));
    }
    //Отображение прямоугольника карты
    private void cameraUpdate(double latSouthwest, double lngSouthwest,double latNortheast, double lngNortheast) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(latSouthwest, lngSouthwest),new LatLng(latNortheast, lngNortheast)), 100);
        map.animateCamera(cameraUpdate);
    }
}
