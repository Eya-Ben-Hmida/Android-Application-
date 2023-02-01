package com.example.isi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.isi.Adapters.ListProcessAdapter;
import com.example.isi.Adapters.ListProcessAdapterClaim;
import com.example.isi.BasicURLConnection;
import com.example.isi.Models.ListProcess;
import com.example.isi.Models.LoginResponse;
import com.example.isi.R;
import com.example.isi.Service.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListActivity extends AppCompatActivity {
    LoginResponse loginResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ListActivity.this );
        String user = sharedPref.getString("nom", "je m'authetfiee");
        String auth = sharedPref.getString("auth", "je m'authetfiee");
        BasicURLConnection basic=new BasicURLConnection();
        Retrofit retrofit=basic.getConnection();
        ListView listView =(ListView) findViewById(R.id.list) ;
        Intent intent= getIntent();
        if(intent.getExtras()!=null){
        String btn=(String) intent.getSerializableExtra("btn");

                if (btn.equals("My Tasks")){
                    UserService service = retrofit.create(UserService.class);
                    Call<List<ListProcess>> call = service.AssignedTasks(auth, user);
                    call.enqueue(new Callback<List<ListProcess>>() {
                        @Override
                        public void onResponse(Call<List<ListProcess>> call, Response<List<ListProcess>> response) {
                            List<ListProcess> process = response.body();
                            Log.e("tag",process.toString());
                            listView.setAdapter(new ListProcessAdapter(ListActivity.this, process));
                            listView.setClickable(true);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent i = new Intent(ListActivity.this, FormAgentActivity.class);
                                    i.putExtra("id", process.get(position).getId());
                                    i.putExtra("data", auth);
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<ListProcess>> call, Throwable t) {
                            Log.e("tag","eror.");
                            Toast.makeText(ListActivity.this, "Something goes wrong ..", Toast.LENGTH_LONG).show();
                        }
                    });
                }else if (btn.equals("Group Tasks")){
                    UserService service = retrofit.create(UserService.class);
                    Call<List<ListProcess>> call = service.GroupAssignedTasks(auth);
                    call.enqueue(new Callback<List<ListProcess>>() {
                        @Override
                        public void onResponse(Call<List<ListProcess>> call, Response<List<ListProcess>> response) {
                            List<ListProcess> process = response.body();
                            Log.v("tag",process.toString());
                            listView.setAdapter(new ListProcessAdapterClaim(ListActivity.this, process,auth));
                            listView.setClickable(true);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent i = new Intent(ListActivity.this, FormAgentActivity.class);
                                    i.putExtra("id", process.get(position).getId());
                                    i.putExtra("data", auth);
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<ListProcess>> call, Throwable t) {
                            Log.e("tag","eror.");
                            Toast.makeText(ListActivity.this, "Something goes wrong ..", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {


                    UserService service = retrofit.create(UserService.class);
                    Call<List<ListProcess>> call = service.processUser(auth);
                    call.enqueue(new Callback<List<ListProcess>>() {
                        @Override
                        public void onResponse(Call<List<ListProcess>> call, Response<List<ListProcess>> response) {
                            List<ListProcess> process = response.body();

                            listView.setAdapter(new ListProcessAdapter(ListActivity.this, process));
                            listView.setClickable(true);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent i = new Intent(ListActivity.this, FormActivity.class);
                                    i.putExtra("id", process.get(position).getId());
                                    i.putExtra("data", auth);
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<ListProcess>> call, Throwable t) {
                            Toast.makeText(ListActivity.this, "Something goes wrong ..", Toast.LENGTH_LONG).show();
                        }
                    });
                }
        }
    }


}