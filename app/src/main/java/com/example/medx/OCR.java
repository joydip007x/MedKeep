package com.example.medx;

import static com.example.medx.MainActivity.dtf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class OCR extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://medikeep-abf40-default-rtdb.firebaseio.com/");

    Button button_capture,button_copy;
    TextView textView_data;
    Bitmap bitmap;
    private static final  int REQUEST_CAMERA_CODE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        button_capture=findViewById(R.id.button_capture);
        button_copy=findViewById(R.id.button_copy);
        textView_data=findViewById(R.id.textdata);

        if(ContextCompat.checkSelfPermission(OCR.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(OCR.this,new String[]{
                    Manifest.permission.CAMERA
            },REQUEST_CAMERA_CODE);
        }
        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(OCR.this);
                
            }
        });

        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scanned_text= textView_data.getText().toString();
                copyToClipboard(scanned_text);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result=CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){

                     Uri resultUri = result.getUri();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                        System.out.println("007x"+ bitmap);
                        getTextFromImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void getTextFromImage(Bitmap bitmap){

        TextRecognizer recognizer= new TextRecognizer.Builder(this).build();
        if( !recognizer.isOperational()){
            Toast.makeText(OCR.this,"Error Occured!",Toast.LENGTH_LONG).show();
        }
        else{
            Frame frame= new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray= recognizer.detect(frame);
            StringBuilder stringBuilder= new StringBuilder();

            for (int i=0; i<textBlockSparseArray.size(); i++){

                TextBlock textBlock=textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append('\n');
            }
            System.out.println("007x"+ stringBuilder.toString() );
            textView_data.setText(stringBuilder.toString());
            button_capture.setText("Retake");
            button_copy.setVisibility(View.VISIBLE);

            handleString(textBlockSparseArray);

        }

    }

    private void handleString(SparseArray<TextBlock> textBlockSparseArray) {

        LinkedHashMap<String,String> hm=new LinkedHashMap<>();
        TextBlock t1=null,t2=null;
        for (int i=0; i<textBlockSparseArray.size(); i=i+2){

            if(i+1<textBlockSparseArray.size()) {
                hm.put(textBlockSparseArray.valueAt(i).getValue(), textBlockSparseArray.valueAt(i + 1).getValue());
            }
        }

        hm.put("DateandTime",new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        for (String key : hm.keySet()) {
            System.out.println(key + " -----007x----- "
                    + hm.get(key));
        }
        HashMap hashMap= (HashMap) hm;
//        System.out.println( "007x:  "+hm);
       String x= databaseReference.push().getKey();
        databaseReference.child("OCR")/*.child(MainActivity.phoneNo)*/.child(x).setValue(hashMap);

    }

    private void copyToClipboard(String text){

         Toast.makeText(OCR.this, "Copied to Clipboard!", Toast.LENGTH_LONG).show();
        ClipboardManager clipBoard= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip=ClipData.newPlainText("CopiedData",text);
        clipBoard.setPrimaryClip(clip);
    }
}