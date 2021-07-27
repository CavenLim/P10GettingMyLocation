package com.example.p10_gettingmylocationsenhanced;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ListTracked extends AppCompatActivity {
    Button btnRefresh,btnFav;
    TextView tvCount;
    ListView lvLoc;
    String[] arrayLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tracked);

        tvCount = (TextView)  findViewById(R.id.tvRecords);
        lvLoc = (ListView)  findViewById(R.id.lvLocations);
        btnRefresh = (Button)  findViewById(R.id.btnRefresh);
        btnFav = findViewById(R.id.btnFavourites);


        String folderLocation2 = getFilesDir().getAbsolutePath() + "/Folder";
        File folder_I = new File(folderLocation2); if (folder_I.exists() == false){
            boolean result = folder_I.mkdir(); if (result == true){
                Log.d("File Read/Write", "Folder created"); }
        }

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
                arrayLocations = array;
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

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderLocation_I = getFilesDir().getAbsolutePath() + "/Folder";
                File targetFile = new File(folderLocation_I, "favorites.txt");
                if (targetFile.exists() == true){
                    String data ="";
                    try {
                        FileReader reader = new FileReader(targetFile); BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine(); while (line != null){
                            data += line + "\n";
                            line = br.readLine(); }
                        Log.i("data",data);

                        String [] array = data.split("\n");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListTracked.this,
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
            }
        });

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

        lvLoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ListTracked.this);
                builder1.setMessage("Add this to your favourite list");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    String folderLocation_I = getFilesDir().getAbsolutePath() + "/Folder";
                                    File targetFile_I = new File(folderLocation_I, "favorites.txt");
                                    Log.i("arrayLocations: ",arrayLocations[position]);
                                    FileWriter writer_I = new FileWriter(targetFile_I, true);
                                    writer_I.write(arrayLocations[position]+"\n");
                                    writer_I.flush();
                                    writer_I.close();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to write!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace(); }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }
}