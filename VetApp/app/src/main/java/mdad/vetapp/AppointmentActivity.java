package mdad.vetapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    DatePicker datePicker;
    Button btnBook;
    Calendar calendar = Calendar.getInstance();
    RadioGroup radioGroup;
    RadioButton radioButton, rbtnSlot1, rbtnSlot2, rbtnSlot3, rbtnSlot4;
    public String dateSelected;
    public String Timeslot;
    private ProgressDialog pDialog;
    private static String url_Book_Appointment = "http://mdadvetapp.atspace.cc/book_appt.php";
    private static String url_check_slot = "http://mdadvetapp.atspace.cc/check_bookingslot.php";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    JSONArray pets = null;
    TextView tvPetID, tvPetName;
    String petID, petName;
    RadioGroup timeslotGroup;
    int day, month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        rbtnSlot1 = findViewById(R.id.rbtnSlot1);
        rbtnSlot2 = findViewById(R.id.rbtnSlot2);
        rbtnSlot3 = findViewById(R.id.rbtnSlot3);
        rbtnSlot4 = findViewById(R.id.rbtnSlot4);
        tvPetID = findViewById(R.id.tvPetID);
        tvPetName = findViewById(R.id.tvPetName);
        Intent intent = getIntent();
        petID = intent.getStringExtra("pet_id");
        petName = intent.getStringExtra("pet_name");
        tvPetID.setText("Pet ID:" + petID);
        tvPetName.setText("Pet Name:" + petName);
        timeslotGroup = findViewById(R.id.radioGrpTimeSlot);
        datePicker = findViewById(R.id.datePicker);
        btnBook = findViewById(R.id.btnBook);
        radioGroup = findViewById(R.id.radioGrpTimeSlot);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Toast.makeText(AppointmentActivity.this, "" + i + " " + (i1 + 1) + " " + i2 + "", Toast.LENGTH_SHORT).show();
                day = i;
                month = i1+1;
                year = i2;
                dateSelected = "" + i + "/" + (i1 + 1) + "/" + i2 + "";
                rbtnSlot1.setEnabled(true);
                rbtnSlot2.setEnabled(true);
                rbtnSlot3.setEnabled(true);
                rbtnSlot4.setEnabled(true);
                new checkSlot().execute();
        }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedRadiobtn = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedRadiobtn);
                Timeslot = radioButton.getText().toString();
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BookAppointment().execute();
            }
        });
    }

    class BookAppointment extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AppointmentActivity.this);
            pDialog.setMessage("Booking Appointment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String apptdate = dateSelected;
            String timeslot = Timeslot;
            SharedPreferences myPrefs = getSharedPreferences("SPREF_NAME", 0);
            String id = myPrefs.getString("id","");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("visit_petid", petID));
            params.add(new BasicNameValuePair("visit_date", apptdate));
            params.add(new BasicNameValuePair("visit_slot", timeslot));
            params.add(new BasicNameValuePair("id", id));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_Book_Appointment,"POST",params);
            Log.d("Create Response", jsonParser.toString());
            try {
                int success = jsonObject.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Calendar calendarTemp = Calendar.getInstance();
                    //calendarTemp.add(Calendar.SECOND, 10);

                    int hour;
                    hour = Integer.parseInt(""+timeslot.charAt(1));
                    hour += 10;
                    calendarTemp.set(year, month, day - 3, hour, 0, 0);


                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarTemp.getTimeInMillis(), pendingIntent);

                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Booking unsuccessful, please refresh and try again.", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Booking Successful", Toast.LENGTH_SHORT).show();
        }
    }
    class checkSlot extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AppointmentActivity.this);
            pDialog.setMessage("Check Availability...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            ArrayList<String> timeslots = new ArrayList<>();
            String apptdate = dateSelected;
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("visit_date", apptdate));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_check_slot,"POST",params);
            Log.d("Create Response", jsonParser.toString());
            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    pets = jsonObject.getJSONArray("pets");
                    for (int i = 0; i < pets.length(); i++) {
                        JSONObject c = pets.getJSONObject(i);
                        String time_slot = c.getString("visit_slot");
                        timeslots.add(time_slot);
                    }
                    for(int i = 0; i < timeslots.size(); i++)
                    {
                        String a = timeslots.get(i);
                        for(int x = 0; x < timeslotGroup.getChildCount(); x++)
                        {
                            String b = ((RadioButton)timeslotGroup.getChildAt(x)).getText().toString();
                            if(a.equals(b))
                            {
                                (timeslotGroup.getChildAt(x)).setEnabled(false);
                            }
                        }
                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }
}
