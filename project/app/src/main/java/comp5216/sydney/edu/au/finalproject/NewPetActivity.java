package comp5216.sydney.edu.au.finalproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.finalproject.Utils.FileUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewPetActivity extends Activity {
    EditText etNickname;
    EditText etText;
    File petAvatar;
    List<File> petImages;
    ImageView post;
    MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);
    private static final int MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_AVATAR = 101;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_POST = 102;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Populate the screen using the layout
        setContentView(R.layout.new_pet);
        etNickname = (EditText) findViewById(R.id.etNickname);
        etText = (EditText) findViewById(R.id.etAddText);
        petImages = new ArrayList<File>();
    }

    public void onBack(View v) {
        finish(); // Close the activity then return to parent
    }

    public void onSubmit(View v) {

        RadioGroup radioGroup = findViewById(R.id.rgType);
        String tag = "";
        if (radioGroup.getCheckedRadioButtonId() == R.id.rbCat) {
            tag = "cat";
        } else {
            tag = "dog";
        }
        if (petAvatar == null) {
            ToastUtils.showShort("please add pet avatar");
        } else if (TextUtils.isEmpty(etNickname.getText().toString())) {
            ToastUtils.showShort("please fill pet nickname");
        } else if (TextUtils.isEmpty(etText.getText().toString())) {
            ToastUtils.showShort("please fill the description");
        } else if (petImages.size() == 0) {
            ToastUtils.showShort("please add post image");
        } else {
            addPet(petImages, petAvatar, etNickname.getText().toString(), tag, etText.getText().toString());
            // Prepare data intent for sending it back
            Intent data = new Intent();

            // Pass relevant data back as a result
            data.putExtra("pet_Nickname", etNickname.getText().toString());

            // Activity finishes OK, return the data
            setResult(RESULT_OK, data); // Set result code and bundle data for response
            finish(); // Close the activity, pass data to parent
        }
    }

    public void onLoadPhotoClick(View view) {
        if (!marshmallowPermission.checkPermissionForReadfiles()) {
            marshmallowPermission.requestPermissionForReadfiles();
        } else {
            // Create intent for picking a photo from the gallery
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            if (view.getId() == R.id.avatarView) {
                // Bring up gallery to select a photo
                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_AVATAR);
            } else if (i < 3){
                // Bring up gallery to select a photo
                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_POST);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView avatar = (ImageView) findViewById(R.id.avatarView);

//        avatar.setVisibility(View.GONE);

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_AVATAR) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();
                // Do something with the photo based on Uri
                Bitmap selectedImage;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), photoUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    //petAvatar = getFileByUri(photoUri);
                    petAvatar = FileUtils.getFileByUri(photoUri,this);
                    System.out.println(petAvatar.isFile());

                    // Load the selected image into a preview
                    avatar.setImageBitmap(selectedImage);
                    avatar.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHOTO_TO_POST) {
            if (resultCode == RESULT_OK) {
                if (i == 0) {
                    post = (ImageView) findViewById(R.id.postView1);
                } else if (i == 1) {
                    post = (ImageView) findViewById(R.id.postView2);
                } else if (i == 2) {
                    post = (ImageView) findViewById(R.id.postView3);
                }
                i += 1;

                Uri photoUri = data.getData();
                // Do something with the photo based on Uri
                Bitmap selectedImage;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), photoUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    File file = FileUtils.getFileByUri(photoUri,this);
                   // File file = getFileByUri(photoUri);
                    System.out.println(file.isFile());
                    petImages.add(file);

                    // Load the selected image into a preview
                    post.setImageBitmap(selectedImage);
                    post.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void addPet(List<File> files, File avatar, String petName, String category, String petDescription) {
        String url = "http://35.189.24.208:8080/api/pet/android/newPet";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart("petName",petName );
        builder.addFormDataPart("category",category );
        builder.addFormDataPart("petDescription",petDescription );

        for (int i = 0; i <files.size() ; i++) {
            File file = files.get(i);
            builder.addFormDataPart("images", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));//文件名,请求体里的文件
        }
        builder.addFormDataPart("avatar", avatar.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), avatar));//文件名,请求体里的文件

        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", ToolManager.getINSTANCE().getSessionId())
                .post(builder.build())
                .build();
        ToolManager.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("FAILED",e.toString());
                System.out.println("FAILED"+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    Log.i("SUCCESS","add new pet");
                }
            }
        });
    }


}
