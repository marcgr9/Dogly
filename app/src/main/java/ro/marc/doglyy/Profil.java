package ro.marc.doglyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profil extends AppCompatActivity {

    private TextView mNume, mNumec, mRasa, mVarsta, mDesc, mTel;
    private Button mButon, mDeconectare;
    private FirebaseAuth mAuth;
    private String user;
    DatabaseReference db;
    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getUid();
        sex = getIntent().getExtras().getString("sex");
        System.out.println(sex);

        if (user == null) {
            Intent i = new Intent(Profil.this, splash.class);
            startActivity(i);
            finish();
            return;
        }

        mNume = findViewById(R.id.nume);
        mNumec = findViewById(R.id.numec);
        mRasa = findViewById(R.id.rasa);
        mVarsta = findViewById(R.id.varsta);
        mDesc = findViewById(R.id.descriere);
        mTel = findViewById(R.id.tel);

        populeazaCampuri(sex);

        mButon = findViewById(R.id.buton);
        mDeconectare = findViewById(R.id.deconectare);

        mDeconectare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(Profil.this, Login.class);
                startActivity(i);
                finish();
                return;
            }
        });

        mButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nume = mNume.getText().toString();
                final String numec = mNumec.getText().toString();
                final String rasa = mRasa.getText().toString();
                final String varsta = mVarsta.getText().toString();
                final String descriere = mDesc.getText().toString();
                final String tel = mTel.getText().toString();

                final String userId = mAuth.getCurrentUser().getUid();

                DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("oameni").child(userId).child("sex_caine");

                post.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sex = dataSnapshot.getValue(String.class);
                        Log.w("marctest", sex);

                        DatabaseReference om = FirebaseDatabase.getInstance().getReference().child("oameni").child(userId).child("nume");
                        om.setValue(nume);

                        DatabaseReference caine = FirebaseDatabase.getInstance().getReference().child(sex).child(userId);
                        Map info = new HashMap<>();
                        info.put("nume", numec);
                        info.put("rasa", rasa);
                        info.put("varsta", varsta);
                        info.put("descriere", descriere);
                        info.put("telefon", tel);
                        caine.updateChildren(info);

                        Intent i = new Intent(Profil.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("err", "oncancel");
                    }
                });
            }
        });

    }


    /// TODO

    private void populeazaCampuri(String sex) {
        db = FirebaseDatabase.getInstance().getReference().child(sex).child(user);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map <String, String> map = (Map)dataSnapshot.getValue();
                    mNumec.setText(map.get("nume"));
                    mDesc.setText(map.get("descriere"));
                    mVarsta.setText(map.get("varsta"));
                    mRasa.setText(map.get("rasa"));
                    mTel.setText(map.get("telefon"));

                    DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("oameni").child(user);
                    db2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                mNume.setText(map.get("nume"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


/*
    private Caine getUser() {

        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("oameni").child(uid).child("sex_caine");

        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sex = dataSnapshot.getValue(String.class);
                //Log.w("marctest", "sex: " + sex);

                if (sex.equals("f")) sex = "m";
                else sex = "f";

                System.out.println("[MARC] sex opus    " + sex);

                DatabaseReference post2 = FirebaseDatabase.getInstance().getReference().child(sex);
                post2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final ArrayList<Caine> caini = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map <String, String> map = (Map)snapshot.getValue();
                            //Log.w("marcrr", map.get("nume") + "gata");
                            Caine c = new Caine(map.get("nume"), map.get("rasa"), map.get("varsta"), map.get("uid"));
                            System.out.println("[MARC] "); c.afis(); System.out.println("/n");
                            //if (!uid.equals(c.iaUid())) caini.add(c);

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    */
}
