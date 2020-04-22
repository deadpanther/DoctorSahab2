package com.example.doctorsahab;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    final List<String> list = new ArrayList<>();
    appointmentDetails user = new appointmentDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseDatabase database = getInstance();
        final DatabaseReference myRef = database.getReference("user");

        Toast.makeText(MainActivity.this, "Firebase connected", Toast.LENGTH_LONG).show();

        final ListView allAppointments = findViewById(R.id.allAppointments);
        final TextAdapter adapter = new TextAdapter();

        readInfo();

        adapter.setData(list);
        allAppointments.setAdapter(adapter);

        allAppointments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete this appointment")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setData(list);
                                Toast.makeText(getApplicationContext(),"The appointment is cancelled",Toast.LENGTH_LONG).show();
                                saveInfo();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                        dialog.show();
                return true;
            }
        });


        final Button newAppointmentButton = findViewById(R.id.bookNewAppointmentButton);

        newAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText appointmentInput = new EditText(MainActivity.this);
                appointmentInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Book a new appointment")
                        .setMessage("What is the new appointment")
                        .setView(appointmentInput)
                        .setPositiveButton("Book an appointment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(appointmentInput.getText().toString());
                                user.setDoctorName(appointmentInput.getText().toString());
                                adapter.setData(list);
                                Toast.makeText(getApplicationContext(),"The appointment is booked",Toast.LENGTH_LONG).show();
                                myRef.push().setValue(user);
                                saveInfo();
                                Toast.makeText(getApplicationContext(),"Value inserted",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        final Button clearAllAppointments = findViewById(R.id.clearAllAppointments);

        clearAllAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Clear all the appointments?")
                        .setMessage("Are you sure you want to clear all the appointments?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                list.clear();
                                adapter.setData(list);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                        dialog.show();
            }
        });

    }


    private void saveInfo(){
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

    private void  readInfo(){
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

    class TextAdapter extends BaseAdapter
    {

        List<String> list = new ArrayList<>();

        void setData(List<String> mList)
        {
            list.clear();
            list.addAll(mList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)
                        MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item, parent, false);
            }

            final TextView textView = convertView.findViewById(R.id.appointment);
            textView.setBackgroundColor(Color.GRAY);
            textView.setTextColor(Color.WHITE);
            textView.setText(list.get(position));

            return convertView;
        }
    }

}
