package ro.marc.doglyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Alegere extends AppCompatActivity {

    private Button mTinder, mHotel, mPlimbare, mDeconect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alegere);

        mTinder = findViewById(R.id.tinder);
        mHotel = findViewById(R.id.hotel);
        mPlimbare = findViewById(R.id.plimbare);
        mDeconect = findViewById(R.id.deconectare);

        mTinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Alegere.this, MainActivity.class);
                startActivity(i);
                //finish();
                return;
            }
        });

        mPlimbare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Alegere.this, Soon.class);
                startActivity(i);
                //finish();
                return;
            }
        });

        mHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Alegere.this, Soon.class);
                startActivity(i);
                //finish();
                return;
            }
        });

        mDeconect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(Alegere.this, Login.class);
                startActivity(i);
                finish();
                return;
            }
        });
    }
}
