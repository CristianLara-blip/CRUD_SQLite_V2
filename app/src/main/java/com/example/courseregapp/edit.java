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

public class edit extends AppCompatActivity {

    EditText ed1, ed2, ed3, ed4;
    Spinner spinner;
    Button b1, b2, b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ed1 = findViewById(R.id.name);
        ed2 = findViewById(R.id.course);
        ed3 = findViewById(R.id.fee);
        ed4 = findViewById(R.id.id);
        b1 = findViewById(R.id.bt1);
        b2 = findViewById(R.id.bt2);
        b3 = findViewById(R.id.bt3);
        spinner = findViewById(R.id.carrera_spinner);

        Intent i = getIntent();
        String t1 = i.getStringExtra("id").toString();
        String t2 = i.getStringExtra("name").toString();
        String t3 = i.getStringExtra("course").toString();
        String t4 = i.getStringExtra("fee").toString();
        ed4.setText(t1);
        ed1.setText(t2);
        ed3.setText(t4);
        ed2.setText(t3);

        List<String> carreraNames = getCarreraNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carreraNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int selectedCarreraIndex = findCarreraIndexIgnoreCase(carreraNames, t3);
        if (selectedCarreraIndex != -1) {
            spinner.setSelection(selectedCarreraIndex);
        }

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), view.class);
                startActivity(i);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit();
            }
        });
    }

    public void Delete()
    {
        try
        {
            String id = ed4.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE,null);
            String sql = "delete from records where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,id);
            statement.execute();
            Toast.makeText(this,"Alumno eliminado",Toast.LENGTH_LONG).show();
            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed1.requestFocus();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Error al eliminar",Toast.LENGTH_LONG).show();
        }
    }
    public void Edit()
    {
        try
        {
            String name = ed1.getText().toString();
            String course = ed2.getText().toString();
            String fee = ed3.getText().toString();
            String id = ed4.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("SliteDb",Context.MODE_PRIVATE,null);
            String sql = "update records set name = ?,course=?,fee=? where id= ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,name);
            statement.bindString(2,course);
            statement.bindString(3,fee);
            statement.bindString(4,id);
            statement.execute();
            Toast.makeText(this,"Alumno editado",Toast.LENGTH_LONG).show();
            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed1.requestFocus();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Error al editar",Toast.LENGTH_LONG).show();
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

    private int findCarreraIndexIgnoreCase(List<String> carreraNames, String targetCarrera) {
        for (int i = 0; i < carreraNames.size(); i++) {
            if (carreraNames.get(i).equalsIgnoreCase(targetCarrera)) {
                return i;
            }
        }
        return -1;
    }
}
