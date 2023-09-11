package com.example.courseregapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class view extends AppCompatActivity {
    ListView lst1;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    Button b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        b3 = findViewById(R.id.bt3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


        SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE, null);
        lst1 = findViewById(R.id.lst1);
        final Cursor c = db.rawQuery("SELECT records.id, records.name, records.course, records.fee, carrera.nombre " +
                "FROM records INNER JOIN carrera ON records.carrera_id = carrera.id", null);

        int idColumnIndex = c.getColumnIndex("id");
        int nameColumnIndex = c.getColumnIndex("name");
        int courseColumnIndex = c.getColumnIndex("course");
        int feeColumnIndex = c.getColumnIndex("fee");
        int carreraNameColumnIndex = c.getColumnIndex("nombre");

        titles.clear();
        arrayAdapter = new ArrayAdapter(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, titles);
        lst1.setAdapter(arrayAdapter);
        final ArrayList<student> stud = new ArrayList<student>();

        if (c.moveToFirst()) {
            do {
                student stu = new student();
                stu.id = c.getString(idColumnIndex);
                stu.name = c.getString(nameColumnIndex);
                stu.course = c.getString(courseColumnIndex);
                stu.fee = c.getString(feeColumnIndex);
                stud.add(stu);
                String recordInfo = c.getString(idColumnIndex) + "\t" +
                        c.getString(nameColumnIndex) + "\t" + "-"+  "\t" +
                        c.getString(carreraNameColumnIndex);
                titles.add(recordInfo);
            } while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
            lst1.invalidateViews();
        }

        lst1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                student stu = stud.get(position);
                Intent i = new Intent(getApplicationContext(), edit.class);
                i.putExtra("id", stu.id);
                i.putExtra("name", stu.name);
                i.putExtra("course", stu.course);
                i.putExtra("fee", stu.fee);
                startActivity(i);
            }
        });
    }
}