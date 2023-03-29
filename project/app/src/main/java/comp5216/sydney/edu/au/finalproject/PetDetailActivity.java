package comp5216.sydney.edu.au.finalproject;


import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.pojo.Pet;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PetDetailActivity extends AppCompatActivity {

    private Pet pet;
    private TextView pet_name;
    private TextView pet_type;
    private TextView pet_desc;
    private ImageView pet_avatar;
    private ImageSlider pet_images;
    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);
        pet = (Pet) getIntent().getSerializableExtra("pet");
        pet_name = findViewById(R.id.pet_detail_name);
        pet_avatar = findViewById(R.id.pet_detail_photo);
        pet_type = findViewById(R.id.pet_detail_type);
        pet_desc = findViewById(R.id.pet_detail_desc);
        pet_images = findViewById(R.id.pet_detail_slider);
        back = findViewById(R.id.pet_detail_back);
        getPet(pet.getPetId());
    }

    private void getPet(int petId){
        String url = "http://35.189.24.208:8080/api/pet/";
        url = url+petId;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = findViewById(R.id.tv);
                        tv.setText("No such pet found");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject body = null;
                try {
                    String result = response.body().string();
                    body = new JSONObject(result);
                    JSONObject data = (JSONObject) body.get("data");
                    Gson gson = new Gson();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Pet pet = gson.fromJson(data.toString(), Pet.class);
                            if(pet.getPetImageAddress() != null){
                                Uri avatar = Uri.parse(pet.getPetImageAddress());
                                pet_avatar.setImageURI(avatar);
                            }
                            List<SlideModel> slideModels = new ArrayList<>();
                            for(int i = 0; i < pet.getPetImageList().size(); i++){
                                slideModels.add(new SlideModel(pet.getPetImageList().get(i), ScaleTypes.FIT));
                            }
                            pet_images.setImageList(slideModels);
                            pet_name.setText(pet.getPetName());
                            pet_desc.setText(pet.getPetDescription());
                            pet_type.setText(pet.getCategory());
                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}