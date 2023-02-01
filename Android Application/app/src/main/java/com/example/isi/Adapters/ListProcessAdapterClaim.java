package com.example.isi.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.isi.BasicURLConnection;
import com.example.isi.Models.ListProcess;
import com.example.isi.R;
import com.example.isi.Service.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListProcessAdapterClaim extends ArrayAdapter<ListProcess> {
    private Context context;
    private List<ListProcess> process;
    private String auth;

    public ListProcessAdapterClaim(Context context, List<ListProcess> process, String auth){
        super(context, R.layout.task_row,process);
        this.context=context;
        this.process=process;
        this.auth=auth;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        convertView=layoutInflater.inflate(R.layout.task_row,parent,false);
        BasicURLConnection basic=new BasicURLConnection();
        Retrofit retrofit=basic.getConnection();
        ListProcess listProcess= process.get(position);
        TextView text=(TextView) convertView.findViewById(R.id.text);
        text.setText(listProcess.getName());
        Log.e("tag",listProcess.getName());
        Button btn=(Button) convertView.findViewById(R.id.claim);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService service=retrofit.create(UserService.class);
                Call call=service.TaskClaim(auth,listProcess.getId());
                call.enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.e("tag",response.toString());
                        Toast.makeText(context.getApplicationContext(), "Task claimed succesfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
        return convertView;
    }
}
