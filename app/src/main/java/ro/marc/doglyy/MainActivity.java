package ro.marc.doglyy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button mLike, mDislike;
    private ImageButton mBut, mMatchuri;
    private String UID;
    //private MaterialButton mBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();


//        ImageView img = findViewById(R.id.imageView);
//        int[] imgs = {R.drawable.caine1, R.drawable.caine2, R.drawable.caine3};
//
//        Random rand = new Random();
//        img.setImageResource(imgs[rand.nextInt(imgs.length)]);


        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getUid();

        iaLista2(UID);

        mLike = findViewById(R.id.like);
        mDislike = findViewById(R.id.dislike);

        mBut = findViewById(R.id.profil);

        mMatchuri = findViewById(R.id.matchuri);

        mMatchuri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Matchuri.class);
                startActivity(i);
                //finish();
                return;
            }
        });

    }

    private void iaLista2(String uid) {
        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("oameni").child(uid).child("sex_caine");

        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String sex = dataSnapshot.getValue(String.class);
                    //Log.w("marctest", "sex: " + sex);
                    if (sex.equals("f")) sex = "m";
                    else sex = "f";

                    final String sexx = (sex.equals("f")) ? "m" : "f";

                    System.out.println("[MARC] sex opus    " + sex);

                    DatabaseReference post2 = FirebaseDatabase.getInstance().getReference().child(sex);
                    post2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final ArrayList<Caine> caini = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Map<String, String> map = (Map) snapshot.getValue();
                                //Log.w("marcrr", map.get("nume") + "gata");
                                Caine c = new Caine(map.get("nume"), map.get("rasa"), map.get("varsta"), map.get("descriere"), map.get("uid"));
                                System.out.println("[MARC] ");
                                c.afis();
                                System.out.println("/n");
                                caini.add(c);

                            }

                            for (Caine c : caini) c.afis();

                            Collections.shuffle(caini);

                            if (caini.size() > 0) updateUI(caini.get(0));

                            mLike.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (caini.size() > 0) {
                                        String uid = FirebaseAuth.getInstance().getUid();
                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("oameni").child(uid).child("da").child(caini.get(0).iaUid());
                                        db.setValue(true);
                                        eMatch(caini.get(0).iaUid());

                                        caini.remove(0);
                                        if (caini.size() > 0) updateUI(caini.get(0));
                                        else {
                                            Intent i = new Intent(MainActivity.this, faracaini.class);
                                            startActivity(i);
                                            finish();
                                            return;
                                        }
                                    }
                                }
                            });

                            mDislike.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (caini.size() > 0) {
                                        String uid = FirebaseAuth.getInstance().getUid();
                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("oameni").child(uid).child("nu").child(caini.get(0).iaUid());
                                        db.setValue(true);

                                        caini.remove(0);
                                        if (caini.size() > 0) updateUI(caini.get(0));
                                        else {
                                            Intent i = new Intent(MainActivity.this, faracaini.class);
                                            startActivity(i);
                                            finish();
                                            return;
                                        }
                                    }
                                }
                            });

                            mBut.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(MainActivity.this, Profil.class);
                                    i.putExtra("sex", sexx);
                                    startActivity(i);
                                    //finish();
                                    return;
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void eMatch(String uid) {
        final String uID = uid;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("oameni").child(uid).child("da").child(UID);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println(",matcghhhh");
                    Toast.makeText(MainActivity.this, "match", Toast.LENGTH_LONG).show();
                    DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("oameni").child(UID).child("matchuri").child(uID);
                    db2.setValue(true);
                    db2 = FirebaseDatabase.getInstance().getReference().child("oameni").child(uID).child("matchuri").child(UID);
                    db2.setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(Caine c) {
        TextView mTitlu = findViewById(R.id.user);
        TextView mDesc = findViewById(R.id.descriere);

        mTitlu.setText(c.iaNume() + ", " + c.iaRasa() + ", " + c.iaVarsta() + " ani");
        mDesc.setText(c.iaDesc());

        ImageView img = findViewById(R.id.imageView);
        int[] imgs = {R.drawable.caine1, R.drawable.caine2, R.drawable.caine3, R.drawable.caine4, R.drawable.caine5, R.drawable.caine6, R.drawable.caine7, R.drawable.caine8, R.drawable.caine9, R.drawable.caine10, R.drawable.caine11, R.drawable.caine12, R.drawable.caine13, R.drawable.caine14};

        Random rand = new Random();
        img.setImageResource(imgs[rand.nextInt(imgs.length)]);
    }

    /*
    public String iaSex(FirebaseUser user) {
        String sex = "f";
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("oameni").child(user.getUid()).child("sex_caine");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sex = dataSnapshot.getValue(String.class);
                return sex;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return sex;
    }

     */
}