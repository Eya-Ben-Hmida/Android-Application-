package com.example.isi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isi.Adapters.ListProcessAdapterClaim;
import com.example.isi.BasicURLConnection;
import com.example.isi.Models.ListProcess;
import com.example.isi.R;
import com.example.isi.Service.UserService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AgentTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        BasicURLConnection basic=new BasicURLConnection();
        Retrofit retrofit=basic.getConnection();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AgentTasksActivity.this );
        String nom = sharedPref.getString("nom", "je m'authetfiee");
        String auth = sharedPref.getString("auth", "je m'authetfiee");
        setContentView(R.layout.activity_agent_tasks);
        TextView welcome=(TextView)findViewById(R.id.welcome);
        welcome.setText("Bienvennue "+nom);
        Intent intent= getIntent();
        Button taskBTN= (Button) findViewById(R.id.button_Task);
        Button GroupTasksBTN= (Button) findViewById(R.id.button_GroupTasks);
        Button process_listBTN= (Button) findViewById(R.id.process_list);
        UserService service = retrofit.create(UserService.class);
        Call<Object> call = service.processcountUser(auth);
        try {
            Toast.makeText(AgentTasksActivity.this,"before respone",Toast.LENGTH_LONG);

            Response<Object> response=call.execute();
            Toast.makeText(AgentTasksActivity.this,"after response "+response.code(),Toast.LENGTH_LONG);

            try {
                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                if(jsonObject.getInt("count")==0)
                    process_listBTN.setVisibility(View.GONE);
                Toast.makeText(AgentTasksActivity.this,"hhhh",Toast.LENGTH_LONG);
            } catch (JSONException e) {
                Toast.makeText(AgentTasksActivity.this,e.getMessage(),Toast.LENGTH_LONG);
                e.printStackTrace();
            }

        } catch (IOException e) {
            Toast.makeText(AgentTasksActivity.this,e.getMessage(),Toast.LENGTH_LONG);
            welcome.setText(e.getMessage());
            e.printStackTrace();
        }

        /*call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                JSONObject jsonObject = null;
                Toast.makeText(AgentTasksActivity.this,"ggg",Toast.LENGTH_LONG);
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if(jsonObject.getInt("count")==0)
                        process_listBTN.setVisibility(View.GONE);
                    Toast.makeText(AgentTasksActivity.this,"hhhh",Toast.LENGTH_LONG);
                } catch (JSONException e) {
                    Toast.makeText(AgentTasksActivity.this,e.getMessage(),Toast.LENGTH_LONG);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("tag","eror.");
                Toast.makeText(AgentTasksActivity.this, "Something goes wrong ..", Toast.LENGTH_LONG).show();
            }
        });*/
        taskBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom= taskBTN.getText().toString();
                startActivity(new Intent(AgentTasksActivity.this, ListActivity.class).putExtra("btn", nom));
            }
        });
        GroupTasksBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom= GroupTasksBTN.getText().toString();
                startActivity(new Intent(AgentTasksActivity.this, ListActivity.class).putExtra("btn", nom));
            }
        });

        process_listBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom= process_listBTN.getText().toString();
                startActivity(new Intent(AgentTasksActivity.this, ListActivity.class).putExtra("btn", nom));
            }
        });
    }
}