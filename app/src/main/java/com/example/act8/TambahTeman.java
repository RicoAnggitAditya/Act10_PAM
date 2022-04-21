package com.example.act8;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.act8.app.AppController;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class TambahTeman extends AppCompatActivity {

    private TextInputEditText tNama, tTelpon;
    private Button simpanBtn;
    String nm, tlp;
    AppController controller = new AppController(this);

    private EditText editNama, editTelpon;
    int success;

    private static String url_insert = "http://10.0.2.2:80/PAM/insert.php";
    private static final String TAG = TambahTeman.class.getSimpleName();
    private static final String TAG_SUCCES = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_teman);

        tNama = (TextInputEditText) findViewById(R.id.tietNama);
        tTelpon = (TextInputEditText) findViewById(R.id.tietTelpon);
        simpanBtn = (Button) findViewById(R.id.buttonSave);

        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpanData();

                if (tNama.getText().toString().isEmpty() || tTelpon.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Data Belum komplit !", Toast.LENGTH_SHORT).show();

                } else {
                    nm = tNama.getText().toString();
                    tlp = tTelpon.getText().toString();

                    HashMap<String, String> qvalues = new HashMap<>();
                    qvalues.put("nama", nm);
                    qvalues.put("telpon", tlp);

                    controller.insertData(qvalues);
                    callHome();
                }
            }
        });
    }

    public void callHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void SimpanData() {
        if (tNama.getText().toString().equals("") || tTelpon.getText().toString().equals("")) {
            Toast.makeText(TambahTeman.this, "Semua harus diisi data", Toast.LENGTH_SHORT).show();
        } else {
            nm = tNama.getText().toString();
            tlp = tTelpon.getText().toString();

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            StringRequest strReq = new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response : " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCES);
                        if (success == 1) {
                            Toast.makeText(TambahTeman.this, "Sukses simpan data", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TambahTeman.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.getMessage());
                    Toast.makeText(TambahTeman.this, "Gagal simpan data", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Nama", nm);
                    params.put("Telpon", tlp);

                    return params;
                }
            };
            requestQueue.add(strReq);

        }
    }
}
