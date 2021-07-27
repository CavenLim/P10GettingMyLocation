package com.example.p10_gettingmylocationsenhanced;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ListTracked extends AppCompatActivity {
    Button btnRefresh;
    TextView tvCount;
    ListView lvLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tracked);

        tvCount = (TextView)  findViewById(R.id.tvRecords);
        lvLoc = (ListView)  findViewById(R.id.lvLocations);
        btnRefresh = (Button)  findViewById(R.id.btnRefresh);


        String folderLocation_I = getFilesDir().getAbsolutePath() + "/Folder";
        File targetFile = new File(folderLocation_I, "location.txt");
        if (targetFile.exists() == true){
            String data ="";
            try {
                FileReader reader = new FileReader(targetFile); BufferedReader br = new BufferedReader(reader);
                String line = br.readLine(); while (line != null){
                    data += line + "\n";
                    line = br.readLine(); }
                    Log.i("data",data);

                String [] array = data.split("\n");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, array);
                lvLoc.setAdapter(adapter);

                tvCount.setText("Num of Records: "+array.length);
                br.close();
                reader.close();
            } catch (Exception e) {

                Toast.makeText(ListTracked.this, "Failed to read!", Toast.LENGTH_LONG).show();
                e.printStackTrace(); }
            Log.d("Content", data); }

        else{
            Toast.makeText(ListTracked.this, "Folder doesnt exist!", Toast.LENGTH_LONG).show();
        }

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderLocation_I = getFilesDir().getAbsolutePath() + "/Folder";
                File targetFile = new File(folderLocation_I, "location.txt");
                if (targetFile.exists() == true){
                    String data ="";
                    try {
                        FileReader reader = new FileReader(targetFile); BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine(); while (line != null){
                            data += line + "\n";
                            line = br.readLine(); }

                        String [] array = data.split("\n");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, array);
                        lvLoc.setAdapter(adapter);

                        tvCount.setText("Num of Records"+array.length);
                        br.close();
                        reader.close();
                    } catch (Exception e) {

                        Toast.makeText(ListTracked.this, "Failed to read!", Toast.LENGTH_LONG).show();
                        e.printStackTrace(); }
                    Log.d("Content", data); }
            }
        });
    }
}