package com.bangkit.tagme;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;


public class QRGeneratorActivity extends AppCompatActivity {

    EditText qrvalue, qrvalue2, qrvalue3, qrvalue4, qrvalue5;
    Button generateBtn;
    ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);

        qrvalue = findViewById(R.id.qrInput1);
        qrvalue2 = findViewById(R.id.qrInput2);
        qrvalue3 = findViewById(R.id.qrInput3);
        qrvalue4 = findViewById(R.id.qrInput4);
        qrvalue5 = findViewById(R.id.qrInput5);

        generateBtn = findViewById(R.id.btn_generate);
        qrImage = findViewById(R.id.qrcode);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = qrvalue5.getText().toString();
                String data5 = qrvalue.getText().toString();
                if(data.isEmpty()){
                    qrvalue.setError("Value Required.");
                }else {
                    QRGEncoder qrgEncoder5 = new QRGEncoder(data, null, com.bangkit.tagme.QRGContents.Type.TEXT, 500);
                    QRGEncoder qrgEncoder = new QRGEncoder(data5, null, com.bangkit.tagme.QRGContents.Type.TEXT, 500);
                    try {
                        Bitmap bitmap5 = qrgEncoder5.encodeAsBitmap();
                        Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap5);
                        qrImage.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();

                    }
                }
            }
        });

    }
}
