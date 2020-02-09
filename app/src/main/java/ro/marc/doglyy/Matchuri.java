package ro.marc.doglyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class Matchuri extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String uid;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter adapp;
    private ListView listview;

    private ImageButton mBut, mProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchuri);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        listview = (ListView) findViewById(R.id.list);
        adapp = new ArrayAdapter(this, R.layout.textmatch, list);
        listview.setAdapter(adapp);

        mProfil = findViewById(R.id.profil);
        mBut = findViewById(R.id.matchuri);

        mBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Matchuri.this, MainActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });


        iaSex(uid);

    }

    private void iaDate(ArrayList<String> uids, String sex) {
        final String sexx = (sex.equals("f") ? "m" : "f");
        for (int i = 0; i < uids.size(); i++) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(sex).child(uids.get(i));
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        mProfil.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Matchuri.this, Profil.class);
                                i.putExtra("sex", sexx);
                                startActivity(i);
                                //finish();
                                return;
                            }
                        });


                        final Map<String, String> map = (Map) dataSnapshot.getValue();
//                    System.out.println("[LOG MARC] sex = " + map.get("nume"));
                        ObiectMatch obj = new ObiectMatch(map.get("nume"), map.get("varsta"), map.get("rasa"));
                        System.out.println("[LOG MARC] " + obj.iaDate());

                        String text = obj.iaDate() + "\n";
                        int n = text.length() / 2;
                        for (int i = 0; i < 10; i++) text += " ";
                        text += "tel: " + map.get("telefon");

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:" + map.get("telefon")));
                                    startActivity(callIntent);
                                    return;

                            }
                        });

                        //txt.append(text);
                        list.add(text);
                        adapp.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void iaUids(String sex) {
        final String sexx = sex;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("oameni").child(uid).child("matchuri");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> uids = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //System.out.println("MATCHH " + snapshot.getKey());
                        System.out.println("[LOG MARC] uid = " + snapshot.getKey());
                        //iaDate(snapshot.getKey(), sexx);
                        uids.add(snapshot.getKey());
                    }
                    iaDate(uids, sexx);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String sex;
    public void iaSex(String uid) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("oameni").child(uid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sex = dataSnapshot.child("sex_caine").getValue(String.class);
                    if (sex.equals("f")) sex = "m";
                    else sex = "f";
                    System.out.println("[LOG MARC] sex = " + sex);
                    iaUids(sex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
