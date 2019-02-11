package mdad.vetapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterPetActivity extends AppCompatActivity {

    Button btnDate;
    Button btnRegister;
    EditText etName;
    Spinner spinnerType;
    RadioButton radioMale;
    RadioButton radioFemale;
    DatePickerDialog datePickerDialog;
    private ProgressDialog pDialog;
    private static String url_register_pet = LoginActivity.ipAddress + "/register_pet.php";
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet);

        btnDate = findViewById(R.id.btnDate);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //datePickerDialog.show();
                datePickerDialog = new DatePickerDialog(RegisterPetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        btnDate.setText(i + "-" + i1 + "-" + i2);
                    }
                }, 2010, 01, 01);
                datePickerDialog.show();
            }
        });
        etName = findViewById(R.id.etName);
        spinnerType = findViewById(R.id.spinnerType);
        btnRegister = findViewById(R.id.btnRegister);
        radioFemale = findViewById(R.id.radioFemale);
        radioMale = findViewById(R.id.radioMale);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.pettypes_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerAdapter);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RegisterPet().execute();
            }
        });
    }
    class RegisterPet extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterPetActivity.this);
            pDialog.setMessage("Registering New Pet..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String pet_name = etName.getText().toString();
            String pet_sex = "";
            if(radioFemale.isChecked()){
                pet_sex = "M";
            }
            else if (radioMale.isChecked()){
                pet_sex = "F";
            }
            String pet_dob  = btnDate.getText().toString();
            String pet_type = spinnerType.getSelectedItem().toString();
            SharedPreferences myPrefs = getSharedPreferences("SPREF_NAME", 0);
            String pet_userid = myPrefs.getString("id","");

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pet_name", pet_name));
            params.add(new BasicNameValuePair("pet_sex", pet_sex));
            params.add(new BasicNameValuePair("pet_dob", pet_dob));
            params.add(new BasicNameValuePair("pet_type", pet_type));
            params.add(new BasicNameValuePair("pet_userid", pet_userid));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_register_pet, "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully registered pet
                    Intent i = new Intent(getApplicationContext(), TrackPetActivity.class);
                    startActivity(i);
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Pet Registered Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
