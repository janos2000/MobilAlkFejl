package com.example.mvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SingleBillActivity extends AppCompatActivity {
    private static final String LOG_TAG = ListActivity.class.getName();
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private ArrayList<Bills> mItemsData;
    private BillAdapter mAdapter;
    EditText nameT;
    EditText priceT;
    EditText dateT;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bill);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mItemsData = new ArrayList<>();
        mAdapter = new BillAdapter(this, mItemsData);
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Bills");
        queryData();

        nameT = findViewById(R.id.name);
        priceT = findViewById(R.id.price);
        dateT = findViewById(R.id.date);
        imageView = findViewById(R.id.imageView);

    }

    public void queryData() {
        mItemsData.clear();
        mItems.orderBy("name").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Bills item = document.toObject(Bills.class);
                mItemsData.add(item);
            }

            if (mItemsData.size() == 0) {
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });
    }

    public void addNewBill(View view) {
        String name = nameT.getText().toString();
        String price = priceT.getText().toString();
        String date = dateT.getText().toString();

        mItems.add(new Bills(name, date, price, 0));

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);

    }


    public void cancel(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }


    public void openCamera(View view){
        checkUserPermission();

    }

    void checkUserPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                return;
            }
        }
        takePic();
    }

    private void takePic() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePic();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            Bitmap img = (Bitmap) b.get("data");
            imageView.setImageBitmap(img);
        }
    }


}