package ro.marc.doglyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private Button mButon;
    private EditText mNumec, mRasa, mVarsta, mDesc;
    private EditText mNume, mEmail, mPass, mTel;
    private ChipGroup mRadio;
    private TextView mLog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStatusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStatusListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(Register.this, Alegere.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mButon = findViewById(R.id.button);
        mLog = findViewById(R.id.log);

        mEmail = findViewById(R.id.mail);
        mPass = findViewById(R.id.pass);
        mNume = findViewById(R.id.nume);
        mTel = findViewById(R.id.tel);

        mNumec = findViewById(R.id.nume_caine);
        mRasa = findViewById(R.id.rasa);
        mVarsta = findViewById(R.id.varsta);
        mDesc = findViewById(R.id.descriere);

        mRadio = findViewById(R.id.radiogr);
        mRadio.setSingleSelection(true);

        //Log.w("marcr", "asd");

        mLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
                return;
            }
        });

        mButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("regmarc", "Asd");

                int selectId = mRadio.getCheckedChipId();

                final Chip mRButon = findViewById(selectId);

                if (mRButon.getText() == null) {
                    return;
                }
                //Log.w("asd", mRButon.getText().toString());


                final String mail = mEmail.getText().toString();
                final String pass = mPass.getText().toString();
                final String nume = mNume.getText().toString();
                final String numec = mNumec.getText().toString();
                final String rasa = mRasa.getText().toString();
                final String varsta = mVarsta.getText().toString();
                final String descriere = mDesc.getText().toString();
                final String tel = mTel.getText().toString();
                String sexInput = mRButon.getText().toString();


                final String sex = (sexInput.equals("Baietel")?"m":"f");


                Log.w("pula", mail + " " + pass + " " + nume + " " + numec + " " + rasa + " " + varsta + " " + sex);

                mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("reg", "eroare la register ");
                        } else {
                            Log.w("reg", "reg reusit");
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference om = FirebaseDatabase.getInstance().getReference().child("oameni").child(userId);
                            Map info = new HashMap<>();
                            info.put("nume", nume);
                            info.put("sex_caine", sex);

                            om.updateChildren(info);

                            DatabaseReference caine = FirebaseDatabase.getInstance().getReference().child(sex).child(userId);
                            info = new HashMap<>();
                            info.put("nume", numec);
                            info.put("rasa", rasa);
                            info.put("varsta", varsta);
                            info.put("descriere", descriere);
                            info.put("telefon", tel);
                            info.put("uid", userId);
                            caine.updateChildren(info);
                        }
                    }
                });
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
