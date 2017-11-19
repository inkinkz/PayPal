package com.example.paypal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    Button payButt;
    TextView payText;
    EditText thb;
    PayPalConfiguration payConfig;
    Intent service;
    String clientID = "AdpOU4oQtBnhzwOKXxUXLNRdREk_AnWACHU0fJbiQjgsp26JDYAE-3F27w1Cre8JSzLAFjEQNjNNBDP0";
    int paypalRequestCode = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        payButt = (Button) findViewById(R.id.button);
        payText = (TextView) findViewById(R.id.textView);
        thb = (EditText) findViewById(R.id.editText);

        payConfig = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(clientID); // envi.production for real money
        service = new Intent(this, PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payConfig); // config above

        startService(service); // paypal service, listen to calls to paypal app


    }



     public void pay(View view) {

         String amount = thb.getText()+"";

        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "THB", "Pay for me m8 ty", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity .class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, paypalRequestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == paypalRequestCode) {

            if (resultCode == Activity.RESULT_OK){

                // confirm that the payment works
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if(confirm != null){

                    String state = confirm.getProofOfPayment().getState();

                    if(state.equals("approved")) // payment works
                        payText.setText("Payment Success!!");
                    else
                        payText.setText("Patment Failed!! ");

                } else {

                    payText.setText("Confirmation Failed!! (null) ");



                }
            }
        }
    }
}
