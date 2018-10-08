package com.example.prakharagarwal.binge.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.prakharagarwal.binge.BaseApplication;
import com.example.prakharagarwal.binge.MainScreen.MainActivity;
import com.example.prakharagarwal.binge.MainScreen.MySharedPreference;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReviewOrderActivity extends AppCompatActivity {

    TextView pay_amount, error_text;
    TextView restaurant_name;
    Toolbar toolbar;
    EditText email, name, phone;
    Button payButton;
    float paymentAmount;
    Intent intent;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private boolean preOrderFlag;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);
        pay_amount = findViewById(R.id.text_pay_amount);
        restaurant_name = findViewById(R.id.text_restaurant_name);
        email = findViewById(R.id.user_email_review_order);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        email.setClickable(false);
        email.setCursorVisible(false);
        name = findViewById(R.id.user_name_review_order);
        phone = findViewById(R.id.user_mobile_review_order);
        payButton = findViewById(R.id.pay_now_btn_review_order);
        error_text = findViewById(R.id.error_text_review_order);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                onBackPressed();
            }
        });

        intent = getIntent();
        if (intent.getIntExtra("preorderFlag", 0) != 0) {
            preOrderFlag = true;
        }
        pay_amount.setText("Payment Amount  =" + intent.getFloatExtra("payamount", 0) + "");
        restaurant_name.setText("Restaurant Name - " + intent.getStringExtra("restaurant"));
        paymentAmount = intent.getFloatExtra("payamount", 0);


        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        final String uID = prefs.getString("username", null);
        final String UName = prefs.getString("display_name", null);
        final String uPhone = prefs.getString("userphone", null);

        if (uID != null) {
            email.setText(uID);
        }
        if (UName != null) {
            name.setText(UName);
        }
        if (uPhone != null) {
            phone.setText(uPhone);
        }


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(name.getText()) || phone.getText().length() < 10) {
                    error_text.setVisibility(View.VISIBLE);
                    error_text.setText("Name cannot be empty or mobile no must be 10 digit");
                    Toast.makeText(ReviewOrderActivity.this, "Please check the username or mobile no ", Toast.LENGTH_SHORT).show();
                } else {
                    if (paymentAmount == 0) {
                        Toast.makeText(ReviewOrderActivity.this, "Please add items to cart", Toast.LENGTH_SHORT).show();

                    } else {
                        if (UName == null) {
                            editor.putString("display_name", name.getText().toString().trim()).commit();
                        }
                        if (uPhone == null) {
                            editor.putString("userphone", phone.getText().toString()).commit();
                        }
                        payButton.setEnabled(false);
                        launchPayUMoneyFlow(phone.getText().toString(), uID, name.getText().toString());
                        Toast.makeText(ReviewOrderActivity.this, "Opening the Payment GateWay...", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void launchPayUMoneyFlow(String phone1, String email1, String firstname1) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Pay Binge");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Binge");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0d;
        try {
            DecimalFormat df2 = new DecimalFormat(".##");

            amount = Double.parseDouble(df2.format((double) intent.getFloatExtra("payamount", 0)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        String phone = phone1.trim();
        String productName = intent.getStringExtra("restaurant");
        String firstName = firstname1.trim();
        String email = email1;

        JSONObject data = new JSONObject();
        try {
            data.put("timestamp", Calendar.getInstance().getTimeInMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Please wait ..");
            dialog.setCancelable(false);
            dialog.show();
            mPaymentParams = builder.build();

            /*
            * Hash should always be generated from your server side.
            * */
            generateHashFromServer(mPaymentParams);
//           calculateServerSideHashAndInitiatePayment1(mPaymentParams);

/*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
           /* mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

        } catch (Exception e) {
            dialog.dismiss();
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            payButton.setEnabled(true);
        }
    }

    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                URL url = new URL("http://13.126.117.141/test.php");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "result":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                dialog.dismiss();
                Toast.makeText(ReviewOrderActivity.this, "Some error occured. Try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (ProtocolException e) {
                dialog.dismiss();
                Toast.makeText(ReviewOrderActivity.this, "Some error occured. Try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                dialog.dismiss();
                Toast.makeText(ReviewOrderActivity.this, "Some error occured. Try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (JSONException e) {
                dialog.dismiss();
                Toast.makeText(ReviewOrderActivity.this, "Some error occured. Try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);
            payButton.setEnabled(true);

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                dialog.dismiss();
                Toast.makeText(ReviewOrderActivity.this, "Some error occured. Try again.", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);

                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, ReviewOrderActivity.this, R.style.PayuTheme, true);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Map<String, Object> paymentData = new HashMap<>();
                paymentData.put("payuResponse", payuResponse);
                paymentData.put("merchantResponse", merchantResponse);
                paymentData.put("timestamp", Calendar.getInstance().getTimeInMillis());
                paymentData.put("orderId", intent.getStringExtra("orderID"));

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction


                    if (preOrderFlag) {
                        paymentData.put("ordertype", "preOrder");
                        firebaseFirestore.collection("orders/" + intent.getStringExtra("restaurantID") + "/payments").add(paymentData);

                         HashMap<String, Object> placedOrderHashmap = new HashMap<>();
                        placedOrderHashmap= (HashMap<String, Object>) intent.getSerializableExtra("preoderHashMap");
                        placedOrderHashmap.put("customer_contact",phone.getText().toString());
                        firebaseFirestore.collection("orders/" + intent.getStringExtra("restaurantID") + "/PreOrder").document(intent.getStringExtra("orderID")).set(placedOrderHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Intent intent1 = new Intent(ReviewOrderActivity.this, CartSuccess.class);
                                intent1.putExtra("orderId",intent.getStringExtra("orderID"));
                                intent1.putExtra("latitude", PassingData.getLatitude());
                                intent1.putExtra("longitude", PassingData.getLongitude());
                                intent1.putExtra("restaurant_id", intent.getStringExtra("restaurantID"));
                                startActivity(intent1);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                    } else {
                        paymentData.put("ordertype", "orderInside");
                        firebaseFirestore.collection("orders/" + intent.getStringExtra("restaurantID") + "/InsideOrder").document(intent.getStringExtra("orderID")).update("payment", "done");
                        firebaseFirestore.collection("orders/" + intent.getStringExtra("restaurantID") + "/payments").add(paymentData);
                        dialog.dismiss();
                        new MySharedPreference(ReviewOrderActivity.this).set_insideorderpayment(false);
                        final Dialog dialog = new Dialog(ReviewOrderActivity.this);
                        dialog.setContentView(R.layout.thankyou_layout);
                        dialog.setCancelable(false);
                        dialog.show();
                        Button button = dialog.findViewById(R.id.thankyou_btn);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.cancel();
                                Intent intent = new Intent(ReviewOrderActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        });
                    }


                } else {
                    dialog.dismiss();
                    if (preOrderFlag) {
                    paymentData.put("orderType", "PreOrder");}
                    else{
                        paymentData.put("orderType", "orderInside");
                    }
                    firebaseFirestore.collection("orders/" + intent.getStringExtra("restaurantID") + "/payments").add(paymentData);
                    error_text.setVisibility(View.VISIBLE);
                    error_text.setText("Transaction Failed. Please try again.");
                    //Failure Transaction
                }


            } else if (resultModel != null && resultModel.getError() != null) {
                dialog.dismiss();
                error_text.setVisibility(View.VISIBLE);
                error_text.setText("Transaction Failed. Please try again.");
                Log.d("Review Order Activity", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                dialog.dismiss();
                error_text.setVisibility(View.VISIBLE);
                error_text.setText("Transaction Failed. Please try again.");
                Log.d("Review Order Activity", "Both objects are null!");
            }
        } else {
            dialog.dismiss();
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("Transaction Failed. Please try again.");
        }
    }


}
