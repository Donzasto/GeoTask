package ru.dondev.myapplication.geotask.app;

/**
 * Created by artem on 24.06.14.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceJSONParser {

    private List<HashMap<String, String>> place;

    public List<HashMap<String, String>> parse(JSONObject jsonObject) {

        JSONArray jsonArray = null;

        try {
            jsonArray = jsonObject.getJSONArray("predictions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonPlaces) {

        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> place = null;
        for (int i = 0; i < jsonPlaces.length(); i++) {
            try {
                place = getPlace((JSONObject) jsonPlaces.get(i));
                placesList.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public HashMap<String, String> getPlace(JSONObject jsonPlace) {

        HashMap<String, String> place = new HashMap<String, String>();

        String description = "";
        String id = "";
        String reference = "";

        try {
            description = jsonPlace.getString("description");
            id = jsonPlace.getString("id");
            reference = jsonPlace.getString("reference");

            place.put("description", description);
            place.put("id", id);
            place.put("reference" ,reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
