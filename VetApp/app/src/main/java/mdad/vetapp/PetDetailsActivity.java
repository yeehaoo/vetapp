package mdad.vetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PetDetailsActivity extends AppCompatActivity {

    TextView tvName, tvType, tvAge, tvSex;
    ListView listView;
    private static String url_getpetdetails = LoginActivity.ipAddress + "/getPetDetailsJson.php";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    JSONArray visits = null;
    ArrayList<HashMap<String, String>> visitList;
    String pet_id, pet_name, pet_sex, pet_type, pet_dob;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        tvName = (TextView) findViewById(R.id.tvName);
        tvType = (TextView) findViewById(R.id.tvType);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvSex = (TextView) findViewById(R.id.tvSex);
        listView = (ListView) findViewById(R.id.listView);

        Intent i = getIntent();
        pet_id = i.getStringExtra("pet_id");
        visitList = new ArrayList<HashMap<String, String>>();
        new GetPetDetails().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String visit_id = ((TextView) view.findViewById(R.id.id)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), VisitActivity.class);
                intent.putExtra("visit_id", visit_id);
                intent.putExtra("pet_name", pet_name);
                startActivity(intent);
            }
        });
    }

    class GetPetDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PetDetailsActivity.this);
            pDialog.setMessage("Loading pet details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            int success;
            try {
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("pet_id", pet_id));
                Log.i("url..........", url_getpetdetails);
                JSONObject json = jsonParser.makeHttpRequest(url_getpetdetails, "POST", nameValuePair);
                Log.d("Single Product Details", json.toString());

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray petObj = json.getJSONArray("pets"); // JSON Array
                    JSONObject pet = petObj.getJSONObject(0);
                    pet_name = pet.getString("pet_name");
                    pet_type = pet.getString("pet_type");
                    pet_sex = pet.getString("pet_sex");
                    pet_dob = pet.getString("pet_dob");

                    JSONArray visits = pet.getJSONArray("visits");
                    Log.d("visits array", visits.toString());
                    for (int i = 0; i < visits.length(); i++) {
                        JSONObject visit = visits.getJSONObject(i);
                        Log.d("visit object", visit.toString());
                        String visit_id = visit.getString("visit_id");
                        Log.d("visit_id", visit_id);
                        String visit_date = visit.getString("visit_date");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("visit_id", visit_id);
                        map.put("visit_date", visit_date);
                        Log.d("map", map.toString());
                        visitList.add(map);
                    }
                }else{
                    // pet with pet_id not found
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();

            // display product data in EditText
            tvName.setText(pet_name);
            tvType.setText(pet_type);
            tvSex.setText(pet_sex);
            tvAge.setText(pet_dob);
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            PetDetailsActivity.this, visitList,
                            R.layout.list_visits, new String[]{"visit_id", "visit_date"}, new int[] {R.id.id, R.id.date}
                    );
                    listView.setAdapter(adapter);
                }
            });
        }
    }
}