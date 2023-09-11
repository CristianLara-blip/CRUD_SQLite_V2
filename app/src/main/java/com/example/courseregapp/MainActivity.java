package com.example.courseregapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText ed1,ed2,ed3;
    Button b1,b2;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1 = findViewById(R.id.name);
        ed2 = findViewById(R.id.course);
        ed3 = findViewById(R.id.fee);
        b1 = findViewById(R.id.bt1);
        b2 = findViewById(R.id.bt2);
        spinner = findViewById(R.id.carrera_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCarreraNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), view.class);
                startActivity(i);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
    }

    public void insert() {
        try {
            String name = ed1.getText().toString();
            String course = ed2.getText().toString();
            String fee = ed3.getText().toString();
            String selectedCarrera = spinner.getSelectedItem().toString();

            SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS carrera(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR)");
            db.execSQL("CREATE TABLE IF NOT EXISTS records(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, course VARCHAR, fee VARCHAR, carrera_id INTEGER)");

            // Insert the selected carrera into the carrera table
            String carreraSql = "INSERT INTO carrera(nombre) VALUES (?)";
            SQLiteStatement carreraStatement = db.compileStatement(carreraSql);
            carreraStatement.bindString(1, selectedCarrera);
            long carreraId = carreraStatement.executeInsert();

            String recordSql = "INSERT INTO records(name, course, fee, carrera_id) VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(recordSql);
            statement.bindString(1, name);
            statement.bindString(2, course);
            statement.bindString(3, fee);
            statement.bindLong(4, carreraId);
            statement.execute();

            Toast.makeText(this,"Alumno registrado", Toast.LENGTH_LONG).show();
            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed1.requestFocus();
        } catch (Exception ex) {
            Toast.makeText(this,"Error al registrar", Toast.LENGTH_LONG).show();
        }
    }

    private List<String> getCarreraNames() {
        List<String> carreraNames = new ArrayList<>();
        carreraNames.add("Ingeniería de Sistemas");
        carreraNames.add("Ingeniería Civil");
        carreraNames.add("Arquitectura");
        carreraNames.add("Medicina");
        return carreraNames;
    }
}
