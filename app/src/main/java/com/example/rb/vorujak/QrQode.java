package com.example.rb.vorujak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.logging.Logger;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.rb.vorujak.R.id.state;

public class QrQode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qr_qode);
            // Programmatically initialize the scanner view
            mScannerView = new ZXingScannerView(this);
            // Set the scanner view as the content view
            setContentView(mScannerView);
        }

        @Override
        public void onResume() {
            super.onResume();
            // Register ourselves as a handler for scan results.
            mScannerView.setResultHandler(this);
            // Start camera on resume
            mScannerView.startCamera();
    }
    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Prints scan results
        //Logger.verbose("result", rawResult.getText());
        // Prints the scan format (qrcode, pdf417 etc.)
      //  Logger.verbose("result", rawResult.getBarcodeFormat().toString());
        //If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
        Intent intent = new Intent(QrQode.this,QrResult.class);
        intent.putExtra("QR_CODE", rawResult.getText());
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(QrQode.this,MapsActivity.class);
         finish();
    }
}
