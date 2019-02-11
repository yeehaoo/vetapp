package mdad.vetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectPet2 extends AppCompatActivity {

    ListView listView;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_getallpets = LoginActivity.ipAddress + "/getAllPetsJson.php";
    private static final String TAG_SUCCESS = "success";
    ArrayList<HashMap<String, String>> petList;
    JSONArray pets = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pet2);

        petList = new ArrayList<HashMap<String, String>>();
        new GetPet().execute();
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pet_id = ((TextView) view.findViewById(R.id.id)).getText().toString();
                String pet_name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), AppointmentActivity.class);
                intent.putExtra("pet_id", pet_id);
                intent.putExtra("pet_name", pet_name);
                startActivity(intent);
            }
        });
    }

    class GetPet extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectPet2.this);
            pDialog.setMessage("Loading Pets..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            //get user id and request list of pets from server
            SharedPreferences myPrefs = getSharedPreferences("SPREF_NAME", 0);
            String id = myPrefs.getString("id", "");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id));
            JSONObject json = jsonParser.makeHttpRequest(url_getallpets, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    //pet list obtained
                    pets = json.getJSONArray("pets");
                    for (int i = 0; i < pets.length(); i++) {
                        JSONObject c = pets.getJSONObject(i);
                        String pet_id = c.getString("pet_id");
                        String pet_name = c.getString("pet_name");
//                        String pet_userid = c.getString("pet_userid");
//                        String pet_type = c.getString("pet_type");
//                        String pet_sex = c.getString("pet_sex");
//                        String pet_dob = c.getString("pet_dob");

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("pet_id", pet_id);
                        map.put("pet_name", pet_name);
//                        map.put("pet_userid", pet_userid);
//                        map.put("pet_type", pet_type);
//                        map.put("pet_sex", pet_sex);
//                        map.put("pet_dob", pet_dob);

                        petList.add(map);
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
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            SelectPet2.this, petList,
                            R.layout.list_item, new String[]{"pet_id", "pet_name"}, new int[] {R.id.id, R.id.name}
                    );
                    listView.setAdapter(adapter);
                }
            });
        }
    }
}