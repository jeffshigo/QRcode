package com.example.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneratorActivity extends AppCompatActivity {
    EditText editText;
    Button generate;
    ImageView image;
    Button scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        final Activity activity = this;
        editText = findViewById(R.id.editText);
        generate = findViewById(R.id.generate);
        scan = findViewById(R.id.scan);
        image = findViewById(R.id.imageView);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(editText.getText().toString().trim(), BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                IntentIntegrator integrator = new IntentIntegrator(activity);

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setPrompt("scan");
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.setBeepEnabled(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result.getContents() != null) {
            Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "you cancelled scanning request", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
