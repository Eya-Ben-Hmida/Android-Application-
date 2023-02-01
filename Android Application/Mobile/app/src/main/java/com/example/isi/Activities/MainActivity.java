package com.example.isi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.isi.BasicURLConnection;
import com.example.isi.Models.LoginResponse;
import com.example.isi.R;
import com.example.isi.Service.UserService;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BasicURLConnection basic=new BasicURLConnection();
        Retrofit retrofit=basic.getConnection();
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        MaterialButton loginbutton = (MaterialButton) findViewById(R.id.loginbutton);
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(MainActivity.this);//n9ydu fih login w mdp
                SharedPreferences.Editor editor = sharedPref.edit();
                if(TextUtils.isEmpty(username.getText().toString())||TextUtils.isEmpty(password.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "All inputs are required ..", Toast.LENGTH_LONG).show();

                }else {

                            try {
                                String user = username.getText().toString();
                                String pass = password.getText().toString();
                                String base = user + ":" + pass;
                                String auth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
                                UserService userService = retrofit.create(UserService.class);
                                Call<List<LoginResponse>> call = userService.loginUser(auth);
                                Response<List<LoginResponse>> response = call.execute();
                                if (response.isSuccessful()) {
                                    List<LoginResponse> loginResponse = response.body();
                                    editor.putString("nom",loginResponse.get(0).getFirstName());
                                    editor.putString("auth",auth);
                                    editor.commit() ;

                                   // SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
                                   // String login = sp.getString("login", "je m'authetfiee");
                                   // Toast.makeText(MainActivity.this, login, Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(MainActivity.this, AgentTasksActivity.class));


                                } else {
                                    Log.e("tag", auth);
                                    Toast.makeText(MainActivity.this, "Error user not found ..", Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


            }
        });

    }

    /*
    public void loginUser(String auth)  {
        Call<LoginResponse> loginResponseCall = ApiClient.getSerice().loginUser(auth);
        Response<LoginResponse> response= null;
        try {
            response = loginResponseCall.execute();
            if(response.isSuccessful()){
                LoginResponse loginResponse=response.body();
                startActivity(new Intent(MainActivity.this,ListActivity.class).putExtra("data",loginResponse));
            }
            else{
                Toast.makeText(MainActivity.this,"Error user not found ..",Toast.LENGTH_LONG).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loginResponseCall.enqueue(new Callback<LoginResponse>(){
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response){
                Log.e("tag","succes");
                LoginResponse loginResponse=response.body();
                startActivity(new Intent(MainActivity.this,ListActivity.class).putExtra("data",loginResponse));
                finish();
            }
            @Override 
            public void onFailure(Call<LoginResponse> call,Throwable t){
                Log.e("tag",auth);
                Toast.makeText(MainActivity.this,"Error user not found ..",Toast.LENGTH_LONG).show();
            }
        });*/


}