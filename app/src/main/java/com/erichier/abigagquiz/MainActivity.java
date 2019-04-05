package com.erichier.abigagquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor = preferences.edit();

        final EditText editText = findViewById(R.id.ip);
        final Spinner spieler = findViewById(R.id.spinner);

        editText.setText(preferences.getString("IP", ""));

        Button btn = findViewById(R.id.btnsend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("IP", editText.getText().toString());
                editor.putLong("Spieler", spieler.getSelectedItemId());
                editor.apply();

                Intent i = new Intent(MainActivity.this, QuestionActivity.class);
                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}
