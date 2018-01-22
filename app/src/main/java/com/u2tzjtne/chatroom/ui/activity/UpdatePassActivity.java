package com.u2tzjtne.chatroom.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.u2tzjtne.chatroom.R;

public class UpdatePassActivity extends AppCompatActivity {

    private EditText oldPass, newPass, newPass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        oldPass = findViewById(R.id.old_password);
        newPass = findViewById(R.id.new_password);
        newPass2 = findViewById(R.id.new_password2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
