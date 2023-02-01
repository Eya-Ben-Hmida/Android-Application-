package com.example.isi.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.isi.Models.ListProcess;
import com.example.isi.R;

import java.util.List;

public class ListProcessAdapter extends ArrayAdapter<ListProcess> {
    private Context context;
    private List<ListProcess> process;
    public ListProcessAdapter(Context context,List<ListProcess> process){
        super(context, R.layout.process_row,process);
        this.context=context;
        this.process=process;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        convertView=layoutInflater.inflate(R.layout.process_row,parent,false);
        ListProcess listProcess= process.get(position);
        TextView text=(TextView) convertView.findViewById(R.id.text);
        text.setText(listProcess.getName());
        Log.e("tag",listProcess.getName());
        return convertView;
    }
}
