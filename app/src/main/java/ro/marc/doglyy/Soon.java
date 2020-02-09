package ro.marc.doglyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Soon extends AppCompatActivity {

    private Button mBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soon);

        mBut = findViewById(R.id.deconectare);
        mBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Soon.this, Alegere.class);
                startActivity(i);
                finish();
                return;
            }
        });
    }
}
