package com.example.cctv_tmap;

import android.os.AsyncTask;

import com.example.cctv_tmap.Search_map.SearchEntity;
import com.example.cctv_tmap.autosearch.Poi;
import com.example.cctv_tmap.autosearch.TMapSearchInfo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AutoCompleteParse extends AsyncTask<String, Void, ArrayList<SearchEntity>> {
    private final String TMAP_API_KEY = "TMAP APP KEY";
    private final int SEARCH_COUNT = 20;  // minimum is 20
    private final String SearchType = "all";
    private ArrayList<SearchEntity> mListData;
    private RecyclerViewAdapter mAdapter;
    private double lat, longi;

    public AutoCompleteParse(RecyclerViewAdapter adapter, double lat, double longi) {
        this.mAdapter = adapter;
        this.lat = lat;
        this.longi = longi;
        mListData = new ArrayList<SearchEntity>();
    }

    @Override
    protected ArrayList<SearchEntity> doInBackground(String... word) {
        return getAutoComplete(word[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<SearchEntity> autoCompleteItems) {
        mAdapter.setData(autoCompleteItems);
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<SearchEntity> getAutoComplete(String word) {
        try {
            String encodeWord = URLEncoder.encode(word, "UTF-8");
            URL acUrl = new URL(
                    "https://apis.openapi.sk.com/tmap/pois?version=1&page=1&count=" + SEARCH_COUNT +
                            "&searchKeyword=" + encodeWord + "&areaLLCode=&areaLMCode=&resCoordType=WGS84GEO" +
                            "&searchType=" + SearchType + "&searchtypCd=&radius=&reqCoordType=WGS84GEO&centerLon=" + longi + "&centerLat=" + lat + "&multiPoint=" +
                            "&callback=&appKey=" + TMAP_API_KEY);

            HttpURLConnection acConn = (HttpURLConnection) acUrl.openConnection();
            acConn.setRequestProperty("Accept", "application/json");
            acConn.setRequestProperty("appKey", TMAP_API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    acConn.getInputStream()));

            String line = reader.readLine();
            if (line == null) {
                mListData.clear();
                return mListData;
            }

            reader.close();

            mListData.clear();

            TMapSearchInfo searchPoiInfo = new Gson().fromJson(line, TMapSearchInfo.class);

            ArrayList<Poi> poi = searchPoiInfo.getSearchPoiInfo().getPois().getPoi();

            for (int i = 0; i < poi.size(); i++) {
                String fullAddr = poi.get(i).getUpperAddrName() + " " + poi.get(i).getMiddleAddrName() +
                        " " + poi.get(i).getLowerAddrName() + " " + poi.get(i).getDetailAddrName();

                mListData.add(new SearchEntity(poi.get(i).getName(), fullAddr));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mListData;
    }
}