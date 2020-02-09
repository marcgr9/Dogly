package ro.marc.doglyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Button mButon;
    private EditText mEmail, mPass;
    private TextView mReg;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStatusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStatusListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(Login.this, Alegere.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mButon = findViewById(R.id.button);
        mEmail = findViewById(R.id.mail);
        mPass = findViewById(R.id.pass);

        mReg = findViewById(R.id.reg);

        mButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mail = mEmail.getText().toString();
                final String pass = mPass.getText().toString();
                mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("log", "eroare la login ");
                        }
                    }
                });
            }
        });

        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                //finish();
                return;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStatusListener);
    }

    protected void onStop() {

        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStatusListener);
    }
}
