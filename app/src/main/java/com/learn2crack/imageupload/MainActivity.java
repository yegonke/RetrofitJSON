package com.learn2crack.imageupload;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {
    private Button submit;
    private ProgressDialog progressDialog;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submit = (Button) findViewById(R.id.btn_select_image);
        baseUrl = "https://yegon.pythonanywhere.com";

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    private void submitData(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Sending");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //Defining retrofit api service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("category", "Testing");
        jsonObject.addProperty("title", "123 Testing");
        jsonObject.addProperty("description", "Hakuna matata hapa kenya");
        jsonObject.addProperty("location", "0,0");

        ApiService service = retrofit.create(ApiService.class);
        Call<PostResponse> call = service.postData(jsonObject);
        //calling the api
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Post submitted Title: "+response.body().getTitle()+" Body: "+response.body().getBody()+" PostId: "+response.body().getId(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private interface ApiService {
        @POST("/api/upload/")
        Call<PostResponse> postData(
                @Body JsonObject body);
    }

    private class PostResponse{
        @SerializedName("category")
        private String title;
        @SerializedName("title")
        private String body;
        @SerializedName("userId")
        private String userId;
        @SerializedName("id")
        private Integer id;

        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return title;
        }
        public void setBody(String body){
            this.body = body;
        }
        public String getBody(){
            return body;
        }
        public void setUserId(String userId){
            this.userId = userId;
        }
        public String getUserId(){
            return userId;
        }
        public void setId(Integer id){
            this.id = id;
        }
        public Integer getId(){
            return id;
        }
    }
}