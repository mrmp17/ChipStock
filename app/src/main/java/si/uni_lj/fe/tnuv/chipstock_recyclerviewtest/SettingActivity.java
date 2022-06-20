package si.uni_lj.fe.tnuv.chipstock_recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Context ctx = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar myToolbar = findViewById(R.id.settingsToolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button cpyToClpbrd = findViewById(R.id.btn_cpyClpbrd_id);
        Button restoreBtn = findViewById(R.id.btn_restoreFromJson_id);
        TextView jsonField = findViewById(R.id.jsonPasteField_id);

        //cpy to clipboard button clicklistener
        cpyToClpbrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = ctx.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                if(sp.contains("cList") && sp.contains("cListDeleted")){
                    String cListJson = sp.getString("cList", "");
                    //jsonField.setText(cListJson);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(ctx.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("JSON", cListJson);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(ctx, "Coppied to clipboard", Toast.LENGTH_SHORT).show();


                }
            }
        });

        //restore button click listener
        restoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = ctx.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("cList", jsonField.getText().toString());
                editor.apply();
                Toast.makeText(ctx, "Restored from JSON", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, MainActivity.class);
                ctx.startActivity(intent);


            }
        });


    }


}



