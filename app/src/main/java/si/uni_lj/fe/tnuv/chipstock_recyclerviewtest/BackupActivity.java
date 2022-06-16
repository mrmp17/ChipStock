package si.uni_lj.fe.tnuv.chipstock_recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class BackupActivity extends AppCompatActivity {

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        //write out json to text field
        TextView jsonField = findViewById(R.id.jsonField_id);
        SharedPreferences sp = ctx.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        if(sp.contains("cList") && sp.contains("cListDeleted")){
            String cListJson = sp.getString("cList", "");
            jsonField.setText(cListJson);
        }


        //import json from text field (used as restore)
        Button importBtn = findViewById(R.id.btnImport_id);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = ctx.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("cList", jsonField.getText().toString());
                editor.apply();
                Intent intent = new Intent(ctx, MainActivity.class);
                ctx.startActivity(intent);
            }
        });
    }


}