package com.erichier.abigagquiz;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(QuestionActivity.this);

        setContentView(R.layout.activity_question);
        final Button btn[] = {findViewById(R.id.answ1), findViewById(R.id.answ2), findViewById(R.id.answ3), findViewById(R.id.answ4)};
        final Switch s = findViewById(R.id.buzzer);
        TextView notes = findViewById(R.id.notes);
        notes.setText("Spieler " + (pm.getLong("Spieler", -1) + 1) + " | IP: " + pm.getString("IP", "-"));

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn[1].setVisibility(View.GONE);
                    btn[2].setVisibility(View.GONE);
                    btn[3].setVisibility(View.GONE);
                    LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) btn[0].getLayoutParams();
                    parms.weight = 900f;
                    btn[0].setLayoutParams(parms);
                    btn[0].setText("Buzzer");
                } else {
                    btn[1].setVisibility(View.VISIBLE);
                    btn[2].setVisibility(View.VISIBLE);
                    btn[3].setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) btn[0].getLayoutParams();
                    parms.weight = 225f;
                    btn[0].setLayoutParams(parms);
                    btn[0].setText("Antwort 1");
                }
            }
        });

        for (final Button b : btn) {
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!s.isChecked()) {
                        UDP_Client client = new UDP_Client(getApplicationContext());
                        client.Message = "P" + pm.getLong("Spieler", -1) + "|" + "A" + (Integer.parseInt(b.getText().toString().replace("Antwort ", "")) - 1);
                        client.NachrichtSenden();

                        b.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        b.animate().setDuration(2000).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                b.getBackground().clearColorFilter();
                            }
                        }).start();
                    }
                    return false;
                }
            });
        }

        btn[0].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (s.isChecked() && event.getAction() == MotionEvent.ACTION_DOWN) {
                    UDP_Client client = new UDP_Client(getApplicationContext());
                    client.Message = "P" + pm.getLong("Spieler", -1) + "|" + "A0";
                    client.NachrichtSenden();
                }
                return false;
            }
        });

        hideSystemUI();

    }

    @Override
    public void onBackPressed() {
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
