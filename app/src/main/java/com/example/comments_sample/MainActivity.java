package com.example.comments_sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    Button b;
    ImageButton b2;
    SampleAdapter adapter;
    ArrayList<DummyData> d ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SampleAdapter();
        //d = getDummyData("");
        d = new ArrayList<>();
        adapter.setData(d);
        recycler_view.setAdapter(adapter);


        b = (Button)findViewById(R.id.add_comm);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_alert_comm(view);

            }
        });

    }


    void call_alert_comm(View view2){
        LayoutInflater layoutInflater = getLayoutInflater();;
        final View view = layoutInflater.inflate(R.layout.alert_view, null);
        TextView tv = view.findViewById(R.id.tv3);
        tv.setVisibility(View.INVISIBLE);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Please add your comment");
        alertDialog.setCancelable(false);

        final EditText etComments = (EditText) view.findViewById(R.id.ed2);

        alertDialog.setPositiveButton( "ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reply = etComments.getText().toString();
                SharedPreferences pref = getSharedPreferences("my_pref",MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("new_comm",reply);
                edit.apply();
                dialog.dismiss();
                if(!reply.equals("")){
                    Toast.makeText(view.getContext(), "Comment added", Toast.LENGTH_SHORT).show();
                    addComment();
                }
            }
        });

        alertDialog.setNegativeButton( "DISCARD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reply = "cancelled ";
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.setView(view);
        dialog.show();
    }

    private void addComment() {
        SharedPreferences pref = getSharedPreferences("my_pref",MODE_PRIVATE);
        String new_comm = pref.getString("new_comm","");
        adapter.addComment(new_comm);
    }

    /*ArrayList<DummyData> getDummyData(String prefix) {
        if (prefix.split("Sub").length > 3) return new ArrayList<>();
        int max = 5;
        ArrayList<DummyData> list = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            DummyData data = new DummyData(prefix + "Dummy Data " + i, getDummyData("Sub " + prefix));
            if(("Dummy Data "+i).equals(data.data)){
                data.is_main = true;
            }
            list.add(data);
        }
        return list;
    }*/
}
