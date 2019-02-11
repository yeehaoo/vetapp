package mdad.vetapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    ImageButton btnTrackPet, btnAppointments;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences myPrefs = getSharedPreferences("SPREF_NAME", 0);
        final SharedPreferences.Editor myEditor = myPrefs.edit();
        String username = myPrefs.getString("username", "");
        String id = myPrefs.getString("id","");
        Toast.makeText(getApplicationContext(), "Welcome, " + username + " . Your userid is " + id, Toast.LENGTH_SHORT).show();

        btnTrackPet = (ImageButton) findViewById(R.id.btnTrackPet);
        btnAppointments = (ImageButton) findViewById(R.id.btnAppointments);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SelectPet2.class);
                startActivity(i);
            }
        });
        btnTrackPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TrackPetActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditor.putString("username","");
                myEditor.commit();

                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
