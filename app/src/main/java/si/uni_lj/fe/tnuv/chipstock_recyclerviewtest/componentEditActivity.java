package si.uni_lj.fe.tnuv.chipstock_recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

public class componentEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_edit);

        Toolbar myToolbar = findViewById(R.id.editToolbar_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        Context ctx = this;

        TextView nmtxt;
        TextView desctxt;
        TextView partnumtxt;
        TextView ordernumtxt;
        TextView stockloctxt;
        TextView notestxt;
        TextView stocknumtxt;

        nmtxt = findViewById(R.id.nameEdit_id);
        desctxt = findViewById(R.id.descriptionEdit_id);
        partnumtxt = findViewById(R.id.partNumEdit_id);
        ordernumtxt = findViewById(R.id.orderNumEdit_id);
        stockloctxt = findViewById(R.id.stockLocEdit_id);
        notestxt = findViewById(R.id.notesEdit_id);
        stocknumtxt = findViewById(R.id.stockEdit_id);

        Button incbtn;
        Button decbtn;

        incbtn = findViewById(R.id.btnInc_id);
        decbtn = findViewById(R.id.btnDec_id);

        Button savebutton;
        Button deletebutton;

        savebutton = findViewById(R.id.btnSave_id);
        deletebutton = findViewById(R.id.btnDelete_id);


        //nmtxt.setText("helo");
        //get index of component that we should be editing (saved in intent extra at activity swap)
        int index;
        index = getIntent().getIntExtra("idx", 0);
        int newCompFlag;
        newCompFlag = getIntent().getIntExtra("new", 0);

        //int deletedFlag = getIntent().getIntExtra("deletedList", 0);

//        if(deletedFlag == 1){
//            deletebutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_restore_24, 0, 0 ,0);
//        }
//        else{
//            deletebutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_delete_24, 0, 0 ,0);
//        }

        //new component, add blank element to array
        if (newCompFlag == 1){
            ComponentClass newComp = new ComponentClass();
            MainActivity.cList.add(newComp);

        }
        else{
            //get component at selected index (load textviews)
            ComponentClass selComp = MainActivity.cList.get(index);

            nmtxt.setText(selComp.name);
            desctxt.setText(selComp.description);
            partnumtxt.setText(selComp.partNumber);
            ordernumtxt.setText(selComp.orderNumber);
            stockloctxt.setText(selComp.stockLocation);
            notestxt.setText(selComp.notes);
            stocknumtxt.setText(String.valueOf(selComp.inStock));
        }


        // ### onClick listeners for buttons

        decbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stock = Integer.parseInt(stocknumtxt.getText().toString());
                if(stock>0){
                    stock -= 1;
                }
                stocknumtxt.setText(String.valueOf(stock));
            }
        });

        incbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stock = Integer.parseInt(stocknumtxt.getText().toString());
                stock += 1;
                stocknumtxt.setText(String.valueOf(stock));
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read component we need to edit
                ComponentClass cEdit = MainActivity.cList.get(index);
                //now rewrite all values
                cEdit.name = nmtxt.getText().toString();
                cEdit.description = desctxt.getText().toString();
                cEdit.partNumber = partnumtxt.getText().toString();
                cEdit.orderNumber = ordernumtxt.getText().toString();
                cEdit.stockLocation = stockloctxt.getText().toString();
                cEdit.notes = notestxt.getText().toString();
                cEdit.inStock = Integer.parseInt(stocknumtxt.getText().toString());
                //write component back to list
                MainActivity.cList.set(index, cEdit);
                //save to json
                MainActivity.saveToJson(ctx);
                //go back to main activity
                Intent intent = new Intent(ctx, MainActivity.class);
                ctx.startActivity(intent);

            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get component to be deleted
                ComponentClass delComp = MainActivity.cList.get(index);
                //write it to deleted list
                MainActivity.cListDeleted.add(delComp);
                //remove it from main list
                MainActivity.cList.remove(index);
                //save to json
                MainActivity.saveToJson(ctx);
                //switch to main activity
                Intent intent = new Intent(ctx, MainActivity.class);
                ctx.startActivity(intent);

            }
        });





    }
}