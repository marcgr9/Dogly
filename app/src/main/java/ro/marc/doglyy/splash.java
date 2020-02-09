package ro.marc.doglyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {

    private Handler handler;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(splash.this, Login.class);
                if (user != null) intent = new Intent(splash.this, Alegere.class);
                startActivity(intent);
                finish();
                return;
            }
        },3000);

    }
}
