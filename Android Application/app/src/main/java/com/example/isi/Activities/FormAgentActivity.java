package com.example.isi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.isi.BasicURLConnection;
import com.example.isi.R;
import com.example.isi.Service.UserService;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FormAgentActivity extends AppCompatActivity {

    private EditText et;

    private List<String> fields = new ArrayList<String>();
    private List<String> values = new ArrayList<String>();
    private List<String> types = new ArrayList<String>();

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
                    String key;
                    for (Iterator i = keys.iterator(); i.hasNext();) {
                        key = (String) i.next();
                        fields.add(key);
                        noms=noms+key+"%2C";
                        Log.e("TAG", key );
                        et = new EditText(FormAgentActivity.this);
                        RelativeLayout.LayoutParams edit =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                        j=j+1;
                        et.setId(j);
                        et.setHint(key);
                        et.setRawInputType(View.AUTOFILL_TYPE_TEXT);
                        et.setCompoundDrawablePadding(20);
                        et.setMaxLines(1);
                        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        et.setTextColor(R.color.white);
                        et.setPadding(40,40,40,40);
                        et.setBackgroundColor(Color.parseColor("#30ffffff"));
                        et.setHintTextColor(Color.WHITE);
                        edit.addRule(RelativeLayout.BELOW,j-1);
                        edit.setMargins(20,20,20,20);
                        linearLayout.addView(et,edit);
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
                            String json_string = "{\n\"variables\":";
                            String variables = "{\n\t";
                            String suffix;
                            for (int i = 0; i < fields.size(); i++) {
                                values.add(et.getText().toString());
                                suffix = ",\n\t";
                                if (i == fields.size() - 1)
                                    suffix = "\n}";
                                variables += "\"" + fields.get(i) + "\":{\"type\":\"String\",\"value\":\"" + et.getText().toString() + "\",\"valueInfo\":{}}" + suffix;
                            }
                            json_string += variables;
                            json_string += ",\n\"businessKey\":\"" + auth + "\"\n}";
                            Log.v("JSON Request Body", json_string);
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, json_string);
                            Call call = service.submitForm(auth, id, body);
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