package com.esteka.arrosapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.esteka.arrosapp.jsonparser.HttpHandler;
import com.esteka.arrosapp.radio.PlaybackStatus;
import com.esteka.arrosapp.radio.Station;
import com.esteka.arrosapp.radio.StationAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = ItemListActivity.class.getSimpleName();
    // URL to get contacts JSON
    private static Boolean isGettingStations = false;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        swipeRefreshLayout = findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.item_list);

        ImageButton btnAppInfo = findViewById(R.id.btn_appinfo);
        btnAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(ItemListActivity.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_appinfo);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        new GetStations(this).execute();
    }

    @Override
    public void onRefresh() {
        if (!isGettingStations) {
            new GetStations(this).execute();
        }
    }

    @Override
    public void onDestroy() {
        RadioApp app = (RadioApp) getApplication();
        if (app.getRadioManager().isPlaying())
        {
            app.getRadioManager().stop();
            app.getRadioManager().unbind();
        }
        super.onDestroy();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStations extends AsyncTask<Void, Void, Void> {

        private Context context;
        //private ArrayList<Station> mStationList = new ArrayList<Station>();

        public GetStations(Context context)
        {
            isGettingStations = true;
            this.context = context;
        }
        private String loadJSONFromResources() {
            InputStream is = getResources().openRawResource(R.raw.stations);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                is.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

            return writer.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RadioApp.stationList = new ArrayList<Station>();
            swipeRefreshLayout.setRefreshing(true);
            // Showing progress dialog
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = null;

            if (RadioApp.useLocalStations)
            {
                jsonStr = loadJSONFromResources();
            }
            else
            {
                // Making a request to url and getting response
                jsonStr = sh.makeServiceCall(RadioApp.stationDataURL);
                Log.e(TAG, "Response from url: " + jsonStr);
            }

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray JSONStations = jsonObj.getJSONArray("station");

                    // looping through All Contacts
                    for (int i = 0; i < JSONStations.length(); i++) {
                        JSONObject c = JSONStations.getJSONObject(i);

                        // If there were any nodes we'd use:
                        //JSONObject node = c.getJSONObject("node");
                        //String node_item = node.getString("node_item");

                        // tmp hash map for single contact
                        Station station = new Station();
                        station.setId(i);
                        station.setName(c.getString("name"));
                        station.setStreamURL(c.getString("streamURL"));
                        station.setPodcastURL(c.getString("podcastURL"));
                        station.setImageURL(c.getString("imageURL"));
                        station.setFreq(c.getString("freq"));
                        station.setArea(c.getString("area"));
                        station.setDesc(c.getString("desc"));
                        station.setAddress(c.getString("address"));
                        station.setWeb(c.getString("web"));
                        station.setEmail(c.getString("email"));
                        station.setPhone(c.getString("phone"));
                        station.setLongDesc(c.getString("longDesc"));

                        int resourceId = context.getResources().getIdentifier("sl_"+station.getImageURL().replaceAll("\\.[^.]*$", ""), "drawable",  context.getPackageName());
                        if (resourceId!=0)
                        {
                            station.setImageResId(resourceId);
                        }
                        else
                        {
                            station.setImageResId(R.drawable.stationimage);
                        }

                        // adding station to station list
                        RadioApp.stationList.add(station);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String error_msg = null;
                        if (RadioApp.useLocalStations) {
                            error_msg = "Couldn't get json from assets.";
                        }
                        else
                        {
                            error_msg = "Couldn't get json from server.";
                        }
                        Log.e(TAG, error_msg);
                        Toast.makeText(getApplicationContext(),
                                error_msg + " Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            isGettingStations = false;
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            recyclerView.setAdapter(new StationAdapter((ItemListActivity)context, RadioApp.stationList, mTwoPane));

            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        RadioApp.getInstance().getRadioManager().bind();
    }

    @Override
    public void onBackPressed() {
        RadioApp.getInstance().getRadioManager().unbind();
        finish();
    }

    @Subscribe
    public void onEvent(String status){

        switch (status){

            case PlaybackStatus.LOADING:

                // loading

                break;

            case PlaybackStatus.ERROR:

                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();

                break;

        }

        /*trigger.setImageResource(status.equals(PlaybackStatus.PLAYING)
                ? R.drawable.ic_pause_black
                : R.drawable.ic_play_arrow_black);

         */

    }

}
