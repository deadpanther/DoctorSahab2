package com.example.doctorsahab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.*;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mImageURL = new ArrayList<>();
    private ArrayList<String> mAppointmentName = new ArrayList<>();
    appointmentDetails appDetails = new appointmentDetails();

    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        String userID = sessionManagement.getSession();

        if(userID.equals("NOTLOGGEDIN")){
            moveToLogin();
        }
    }

    /*
    private void initAppointmentValues(appointmentDetails user){
        mAppointmentName.add(user.getAppointmentName());
    }
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSession();

        final FirebaseDatabase database = getInstance();
        final DatabaseReference myRef = database.getReference("appointments");
        final Button newAppointmentButton = findViewById(R.id.bookNewAppointmentButton);
        Toast.makeText(MainActivity.this, "Firebase connected", Toast.LENGTH_LONG).show();
        //final RecyclerView allAppointments = findViewById(R.id.allAppointments);
        //final RecyclerViewAdapter adapter = new RecyclerViewAdapter();

    newAppointmentButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final EditText appointmentInput = new EditText(MainActivity.this);
            appointmentInput.setSingleLine();
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Book a new appointment")
                    .setMessage("What is the new appointment")
                    .setView(appointmentInput)
                    .setPositiveButton("Book an appointment", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appDetails.setAppointmentName(appointmentInput.getText().toString());
                            //initAppointmentValues(appDetails);
                            Toast.makeText(getApplicationContext(),"The appointment is booked",Toast.LENGTH_LONG).show();
                            mImageURL.add("Newval");
                            mAppointmentName.add(appDetails.getAppointmentName());
                            SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                            String userID = sessionManagement.getSession();
                            myRef.child(userID).push().setValue(appDetails);
                            initRecyclerView();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        }
    });

    //allAppointments.setAdapter();

        initRecyclerView();
    }
    /*
    public void saveInfo(){
        try{
            File file = new File(this.getFilesDir(), "saved");

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            for(int i=0;i<list.size();i++)
            {
                bw.write(list.get(i));
                bw.newLine();
            }
            bw.close();
            fOut.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readInfo(){
        File file = new File(this.getFilesDir(),"saved");
        if(!file.exists()){
            return;
        }

        try{
            FileInputStream is =new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            while(line!=null)
            {
                list.add(line);
                line = br.readLine();
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

     */

// Used after logout to go to the signup page
    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void logout(View view) {
        //this method will remove session and open login screen
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.removeSession();
        Log.i("id", sessionManagement.getSession().toString());

        moveToLogin();
    }


    private void initRecyclerView(){
        Log.d("TAGo", "initRecyclerView: inti recyclerview");
        RecyclerView recyclerView = findViewById(R.id.allAppointments);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mImageURL,mAppointmentName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
