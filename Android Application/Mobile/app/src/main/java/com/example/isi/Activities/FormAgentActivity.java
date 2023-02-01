package com.example.isi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.isi.BasicURLConnection;
import com.example.isi.R;
import com.example.isi.Service.UserService;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Iterator;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FormAgentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Intent intent= getIntent();

        RelativeLayout linearLayout =  (RelativeLayout) findViewById(R.id.linearlayout);
        BasicURLConnection basic=new BasicURLConnection();
        Retrofit retrofit=basic.getConnection();

        if(intent.getExtras()!=null) {
            String id = (String) intent.getSerializableExtra("id");
            String auth=(String) intent.getSerializableExtra("data");
            UserService service=retrofit.create(UserService.class);
            Call call=service.formTask(auth,id,id);
            call.enqueue(new Callback() {
                @SuppressLint("ResourceType")
                @Override
                public void onResponse(Call call, Response response) {
                    LinkedTreeMap input= (LinkedTreeMap) response.body();
                    Set keys = input.keySet();
                    Log.e("a",response.toString());
                    TextView editText = (TextView) findViewById(R.id.textField);
                    int j=1;

                    editText.setId(j);
                    String noms="";
                    for (Iterator i = keys.iterator(); i.hasNext();) {
                        String key = (String) i.next();
                        noms=noms+key+"%2C";
                        Log.e("TAG", key );
                        EditText editText1 = new EditText(getApplicationContext());
                        RelativeLayout.LayoutParams edit =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                        editText1.setHint(key);
                        j=j+1;
                        editText1.setId(j);
                        editText1.setCompoundDrawablePadding(20);
                        editText1.setTextColor(R.color.white);
                        editText1.setPadding(40,40,40,40);
                        editText1.setBackgroundColor(Color.parseColor("#30ffffff"));
                        editText1.setHintTextColor(Color.WHITE);
                        edit.addRule(RelativeLayout.BELOW,j-1);
                        edit.setMargins(20,20,20,20);
                        linearLayout.addView(editText1,edit);

                    }

                    Button btnShow = new Button(getApplicationContext());
                    btnShow.setText("Valider");
                    btnShow.setId(8);

                    RelativeLayout.LayoutParams edit1 =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    edit1.addRule(RelativeLayout.BELOW,6);
                    edit1.setMargins(20,20,20,20);

                    String finalNoms = noms;
                    btnShow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserService service = retrofit.create(UserService.class);
                            Call call = service.submitForm(auth, id, finalNoms.substring(0, finalNoms.length()-2));
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    startActivity(new Intent(FormAgentActivity.this, SubmitActivity.class));
                                }
                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Log.e("TAG", "erreur API 2" );

                                }
                            });
                        }
                    });
                    linearLayout.addView(btnShow,edit1);

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("TAG", "erreur API 1" );

                }
            });
        }

    }
}