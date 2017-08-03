package com.example.a15017274.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> al;
    ArrayAdapter<String> adapter;
    boolean saveDetail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lvDetails);
        al = new ArrayList<>();
        al.add("Singapore National Day is on 9 Aug");
        al.add("Singapore is 52 years old");
        al.add("Theme is '#OneNationTogether'");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send) {
            String[] list = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    .setItems(list, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String msg = "";
                            for (int i = 0; i < al.size(); i++) {
                                msg += (i + 1) + ". " + al.get(i) + "\n";
                            }

                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{
                                        "jiaajiee@gmail.com"
                                });
                                email.putExtra(Intent.EXTRA_SUBJECT,
                                        "P11 - Know your national day");
                                email.putExtra(Intent.EXTRA_TEXT,
                                        msg);
                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email, "Choose an Email client: "));
                            } else {
                                try {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage("+6597289596", null, msg, null, null);

                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("sms:+6597289596"));
                                    i.putExtra("sms_body", "");
                                    startActivity(i);
                                    Toast.makeText(MainActivity.this, "You have sent a message",
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, "You have not sent a message",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.action_quiz) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);
            final RadioGroup rg1 = (RadioGroup) quiz.findViewById(R.id.rg1);
            final RadioGroup rg2 = (RadioGroup) quiz.findViewById(R.id.rg2);
            final RadioGroup rg3 = (RadioGroup) quiz.findViewById(R.id.rg3);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test yourself!")
                    .setView(quiz)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String answer = "";
                            int selected1 = rg1.getCheckedRadioButtonId();
                            int selected2 = rg2.getCheckedRadioButtonId();
                            int selected3 = rg3.getCheckedRadioButtonId();

                            if (selected1 == R.id.rb1) {
                                answer += "Wrong\n";
                            } else {
                                answer += "Correct\n";
                            }

                            if (selected2 == R.id.rb3) {
                                answer += "Correct\n";
                            } else {
                                answer += "Wrong\n";
                            }

                            if (selected3 == R.id.rb5) {
                                answer += "Correct\n";
                            } else {
                                answer += "Wrong\n";
                            }
                            Toast.makeText(MainActivity.this, "Answer:\n" + answer, Toast.LENGTH_LONG).show();
                        }
                    })

                    .setNegativeButton("Don't know lah", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "You don't know how to do",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.action_quit){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "You exited the app",
                                    Toast.LENGTH_LONG).show();
                            SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("details", false);
                            editor.apply();
                            finish();
                        }
                    })
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "You did not quit",
                                    Toast.LENGTH_LONG).show();

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
        saveDetail = preferences.getBoolean("details", false);
        if (!saveDetail) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.passphrase, null);
            final EditText etPassphrase = (EditText) passPhrase
                    .findViewById(R.id.editTextPassPhrase);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Login")
                    .setView(passPhrase)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (!etPassphrase.getText().toString().equals("738964")) {
                                Toast.makeText(MainActivity.this, "You had entered the wrong access code " +
                                        etPassphrase.getText().toString(), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("details", true);
                                editor.apply();
                            }
                        }
                    })

                    .setNegativeButton("No Access Code", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "You have no access code",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
