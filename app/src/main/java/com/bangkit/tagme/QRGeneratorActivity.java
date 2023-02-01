package com.bangkit.tagme;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.journeyapps.barcodescanner.BarcodeEncoder;


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
                                            String data =   qrvalue.getText().toString();
                                            String data2   = qrvalue2.getText().toString().trim();
                                            String data3 =  qrvalue3.getText().toString().trim();

//                if(data.isEmpty()){
//                    qrvalue.setError("Value Required.");
//                }else {
//                    QRGEncoder qrgEncoder = new QRGEncoder(data, null, com.bangkit.tagme.QRGContents.Type.TEXT, 500);
//                    QRGEncoder qrgEncoder2 = new QRGEncoder(data2, null, com.bangkit.tagme.QRGContents.Type.TEXT, 500);

                                               MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                               try {
                                                   BitMatrix bitMatrix = multiFormatWriter.encode("Text1 :" + data + "\n Text2 :" + data2 + "\n Text3 :" + data3,
                                                           BarcodeFormat.QR_CODE, 200, 200);
                                                   BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                                   Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//                                                   Bitmap bitmap1 = qrgEncoder.encodeAsBitmap(bitMatrix);
//                                                   Bitmap bitmap2 = qrgEncoder2.encodeAsBitmap();
                                                   qrImage.setImageBitmap(bitmap);
//                                                   qrImage.setImageBitmap(bitmap1);
//                                                   qrImage.setImageBitmap(bitmap2);
                                               } catch (WriterException e) {
                                                   e.printStackTrace();

                                               }
                                           }


                                       }
        );
    }
}
