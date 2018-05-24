package com.example.b17annni.fksake;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {



    private List<Qte_class> mybug = new ArrayList<Qte_class>();
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FetchData().execute();




        String[] one = {"one","two","three"};
        List<String> listData = new ArrayList<String>(Arrays.asList(one));
        adapter = new ArrayAdapter(getApplicationContext(),R.layout.list_item_textview,
                R.id.my_item_textview,mybug);
        ListView myListView = (ListView)findViewById(R.id.myListView);
        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Qte_class m = mybug.get(position);
                Toast.makeText(getApplicationContext(), m.info(), Toast.LENGTH_LONG).show();
            }
        });

  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "This is dedicated for those few who needs a quickfix quote on the go.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.meny_refresh:
                new FetchData().execute();
                return true;

            case R.id.meny_clear:
                adapter.clear();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private class FetchData extends AsyncTask<Void,Void,String>{



        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=b17annni");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            Log.d("mylog","DataFetched"+o);
            try {
                JSONArray allaberg = new JSONArray(o);

                for (int start=0;start<allaberg.length();start++){
                    JSONObject woow = allaberg.getJSONObject(start);
                    int qid = woow.getInt("ID");
                    String qname = woow.getString("name");
                    String qtype = woow.getString("type");
                    String qcompany = woow.getString("company");
                    String qlocation = woow.getString("location");
                    String qcategory = woow.getString("category");
                    int qheight = woow.getInt("size");
                    int qncost = woow.getInt("cost");
                    String qauxdata = woow.getString("auxdata");

                    Qte_class m = new Qte_class(qname,qlocation,qheight);
                    adapter.add(m);

                    Log.d("mylog","forloopvarv "+start+qname);
                }
            } catch (JSONException e) {
                Log.e("mylog","E:"+e.getMessage());
            }
        }
    }


}