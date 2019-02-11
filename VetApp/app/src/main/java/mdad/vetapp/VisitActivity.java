package mdad.vetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VisitActivity extends AppCompatActivity {

    String visit_id, visit_date, visit_comments, visit_petname, visit_vetname;
    TextView tvDate, tvComments, tvPetName, tvVetName;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    String url_getvisitdetails = LoginActivity.ipAddress + "/getVisitDetailsJson.php";
    private static final String TAG_SUCCESS = "success";
    JSONArray visits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        Intent i = getIntent();
        visit_id = i.getStringExtra("visit_id");
        Log.d("visit_id", visit_id);
        visit_petname = i.getStringExtra("pet_name");

        tvDate = findViewById(R.id.tvDate);
        tvComments = findViewById(R.id.tvComments);
        tvPetName = findViewById(R.id.tvPetName);
        tvVetName = findViewById(R.id.tvVetName);

        new GetVisitDetails().execute();
    }

    class GetVisitDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VisitActivity.this);
            pDialog.setMessage("Loading Visit Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("visit_id", visit_id));
            JSONObject json = jsonParser.makeHttpRequest(url_getvisitdetails, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    //pet list obtained
                    visits = json.getJSONArray("visits");
                    for (int i = 0; i < visits.length(); i++) {
                        JSONObject c = visits.getJSONObject(i);
                        visit_date = c.getString("visit_date");
                        visit_comments = c.getString("visit_comments");
                        visit_vetname = c.getString("visit_vetname");
                    }
                } else {
                    //error getting pets
                    Toast.makeText(getApplicationContext(), "Failed to obtain pet list", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            tvDate.setText(visit_date);
            tvComments.setText(visit_comments);
            tvPetName.setText(visit_petname);
            tvVetName.setText(visit_vetname);
        }
    }
}
