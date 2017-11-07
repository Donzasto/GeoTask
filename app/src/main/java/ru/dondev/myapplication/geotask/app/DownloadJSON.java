package ru.dondev.myapplication.geotask.app;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class DownloadJSON {

    public DownloadJSON(String s){
        new PlacesTask().execute(s);
    }



    private class PlacesTask extends AsyncTask<String, Void, String> {

        private String downloadUrl(String strUrl) throws IOException {
            InputStream inputStream = null;
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(strUrl);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuffer = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line);
                }

                strUrl = stringBuffer.toString();

                bufferedReader.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
                httpURLConnection.disconnect();
            }
            return strUrl;
        }

        @Override
        protected String doInBackground(String... place) {
            String data = "";
            String input = "";
            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String sensor = "sensor=false";
            String key = "key=AIzaSyD99IxEcSW7Ku925H3vtyDIKQ2ZHFnoIkY";
            String parameters = input+"&"+sensor+"&"+key;

            String strUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" + parameters;
            try {
                data = downloadUrl(strUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("myLogs", data);
            ParserTask pt = new ParserTask();
            pt.execute(data);
        }
    }

    private class ParserTask extends AsyncTask<String, Void, List<HashMap<String, String>>>{

        JSONObject jsonObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... data) {
            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJSONParser = new PlaceJSONParser();

            try {
                jsonObject = new JSONObject(data[0]);

                places = placeJSONParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> place) {
            super.onPostExecute(place);
//              String[] s = new String[]{"description"};
//              int[] i = new int[]{android.R.id.text1};
//              SimpleAdapter sa = new SimpleAdapter(getActivity(), place, android.R.layout.simple_dropdown_item_1line,s ,i);
//              autoCompleteTextView.setAdapter(sa);
//              autoCompleteTextView.showDropDown();

            Log.d("myLogs", "place " + place);
        }
    }
}
