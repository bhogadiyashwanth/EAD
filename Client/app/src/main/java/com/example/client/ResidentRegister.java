package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.client.Retrofit.IMyService;
import com.example.client.Retrofit.RetrofitClient;
import com.example.client.dto.User;
import com.example.client.dto.UserResidence;
import com.example.client.dto.BaseResponse;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class ResidentRegister extends AppCompatActivity {
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    IMyService iMyService;
    MaterialEditText edt_block_name;
    MaterialEditText edt_flat_num;
    Button btn_continue;
    Gson gson = new Gson();
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_register);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        edt_block_name=(MaterialEditText) findViewById(R.id.edt_block_name);
        edt_flat_num = (MaterialEditText) findViewById(R.id.edt_flat_num);
        btn_continue=(Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blockname = edt_block_name.getText().toString();
                String flatnum = edt_flat_num.getText().toString();
                if(TextUtils.isEmpty(blockname)) {  Toast.makeText(ResidentRegister.this, "Please enter block name to proceed", Toast.LENGTH_SHORT).show(); return;}
                if(TextUtils.isEmpty(flatnum)){  Toast.makeText(ResidentRegister.this, "Please enter flat number to proceed", Toast.LENGTH_SHORT).show(); return;}
                Bundle extras = getIntent().getExtras();
                String jsonString = extras.getString("user");
                String city = extras.getString("city");
                String name = extras.getString("society_name");
                User user = gson.fromJson(jsonString, User.class);
                registerResident( user, name, city,blockname,flatnum);
            }
        });
    }

    public void registerResident(User user, String name, String city , String blockname,String flatnum){

        UserResidence userResidence = new UserResidence();
        userResidence.setName(name);
        userResidence.setBlockname(blockname);
        userResidence.setCity(city);
        userResidence.setFlatnum(flatnum);
        userResidence.setUser(user);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(userResidence));
        compositeDisposable.add(iMyService.registerResident(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>(){
                    @Override
                    public void accept(String response) throws Exception {
                        BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                        Toast.makeText(ResidentRegister.this,""+ baseResponse.getMsg(),Toast.LENGTH_LONG).show();
                        if(baseResponse.getMsg().equals("successful"))
                        {
                            Intent i =new Intent(ResidentRegister.this,Leadpage.class);
                            Bundle extras = new Bundle();
                            extras.putString("user", gson.toJson(baseResponse.getUser()));
                            i.putExtras(extras);
                            startActivity(i);
                        }
                    }
                })
        );
    }
}
