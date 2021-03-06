package com.example.cctv_tmap.CCTV;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cctv_tmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Database(entities = {CCTV_info.class}, version = 2)
public abstract class CCTVDatabase extends RoomDatabase {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `CCTVDatabase` (`id` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };
    private static CCTVDatabase INSTANCE;
    private static Context activity;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    public static synchronized CCTVDatabase getINSTANCE(Context context) {

        activity = context.getApplicationContext();

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CCTVDatabase.class,
                    "CCTVDatabase").fallbackToDestructiveMigration().addCallback(roomCallback).build();
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CCTVDatabase.class, "CCTVDatabase")
                    .addMigrations(MIGRATION_1_2).build();
        }
        return INSTANCE;
    }

    private static void fillWithStartingData(Context context) {
        CCTVDao dao = getINSTANCE(context).cctvDao();

        JSONArray cctvs = loadJSONArray(context);

        int length = cctvs.length();

        //Log.d("TAG","?????? : " + length);
        try {
            for (int i = 0; i < length; i++) {
                JSONObject cctv = cctvs.getJSONObject(i);

                String Control_name = cctv.getString("Control_name");
                String address_road = cctv.getString("address_road");
                String address_bungi = cctv.getString("address_bungi");
                String angle = cctv.getString("angle");
                String save = cctv.getString("save");
                String Control_number = cctv.getString("Control_number");
                double latitude = Double.parseDouble(cctv.getString("latitude"));
                double longitude = Double.parseDouble(cctv.getString("longitude"));

                dao.insert(new CCTV_info(Control_name, Control_number, save, address_road, address_bungi, angle, latitude, longitude));
                Log.d("TAG", "ID : " + (i + 1));
            }
        } catch (JSONException e) {
        }
    }

    private static JSONArray loadJSONArray(Context context) {
        StringBuilder builder = new StringBuilder();
        InputStream in = context.getResources().openRawResource(R.raw.cctv);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject json = new JSONObject(builder.toString());

            return json.getJSONArray("CCTV");
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public abstract CCTVDao cctvDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CCTVDao cctv_dao;

        private PopulateDbAsyncTask(CCTVDatabase db) {
            cctv_dao = db.cctvDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            fillWithStartingData(activity);

            return null;
        }
    }
}
