package com.net.comy;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegisterComplaint extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 165;
    private AppCompatSpinner mCategorySpinner;
    private Uri mImageUri;
    private ImageView mImageView;
    private String fileLink;
    private FloatingActionButton mGetImage;
    ArrayList<String> names;
    private String currentAddress;
    private EditText mComplaintAddress, mComplaintTitle, mComplaintDesc;
    private ProgressDialog mDialog;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, mUserComplaint;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mStorageReference = mFirebaseStorage.getReference("complaint_images");
        mUserComplaint = mFirebaseDatabase.getReference("users");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mCategorySpinner = findViewById(R.id.complaint_main_category);
        names = new ArrayList<>();
        names.add("Select Complaint Categoty");
        names.add("Restaurant");
        names.add("Hotels");
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_keyboard_arrow_down_black_24dp);
        images.add(R.drawable.ic_restaurant);
        images.add(R.drawable.ic_hotel);
        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_itme, names, images);
        mCategorySpinner.setAdapter(adapter);
        mImageView = findViewById(R.id.complaint_image);
        mGetImage = findViewById(R.id.add_image_button);
        mComplaintAddress = findViewById(R.id.edt_complaint_location);
        mComplaintDesc = findViewById(R.id.edt_complaint_description);
        mComplaintTitle = findViewById(R.id.edt_complaint_title);
        mGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                openFileChooser();
            }
        });


    }

    public void registerComplaint(String pCurrentAddress) {
        String title = mComplaintTitle.getText().toString();
        String desc = mComplaintDesc.getText().toString();
        String location = mComplaintAddress.getText().toString();
        if (pCurrentAddress == null) {
            Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(location)) {
            return;
        }
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Registering...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        uploadImage(title, desc, location, pCurrentAddress);

    }

    public void getCurrentLocationAndRegister(View pView) {
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> pTask) {
                if (pTask.isSuccessful()) {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    Location location = pTask.getResult();
                    List<Address> address = null;
                    try {
                        address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (address.size() > 0) {
                            currentAddress = address.get(0).getAddressLine(0);
                            Toast.makeText(RegisterComplaint.this, "" + currentAddress, Toast.LENGTH_SHORT).show();
                            registerComplaint(currentAddress);
                        } else {
                            Toast.makeText(RegisterComplaint.this, "Come back later.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException pE) {
                        pE.printStackTrace();
                    }
                } else {
                    Toast.makeText(RegisterComplaint.this, "Error while fetching location!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void addDataToFirebase(String fileLink, String location, String title, String desc, String pCurrentAddress) {
        mDatabaseReference = mFirebaseDatabase.getReference("complaints");
        final String productId = mDatabaseReference.push().getKey();
        if (mFirebaseUser != null && fileLink != null) {
            final String userId = mFirebaseUser.getUid();
            final String userEmail = mFirebaseUser.getEmail();
            String phoneNumber = mFirebaseUser.getPhoneNumber();

            int position = mCategorySpinner.getSelectedItemPosition();
            String category = "General";
            if (position != 0) {
                category = names.get(position);
            }
            final String shortId = Utils.getAlphaNumericString(10);
            ComplaintModel complaintModel =
                    new ComplaintModel(
                            category,
                            location,
                            title,
                            desc,
                            fileLink,
                            userId,
                            userEmail,
                            phoneNumber,
                            pCurrentAddress,
                            shortId,
                            System.currentTimeMillis());

            mDatabaseReference.child(productId).setValue(complaintModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            UserView userView = new UserView();
                            userView.setComplainID(productId);
                            mUserComplaint.child(userId).child(shortId).setValue(userView);
                            Toast.makeText(RegisterComplaint.this, "Complaint Registered.", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterComplaint.this, "Failed to Register!!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Error occured!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(final String location, final String title, final String desc, final String pCurrentAddress) {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(RegisterComplaint.this, "Posting...", Toast.LENGTH_SHORT).show();
                                    fileLink = uri.toString();

                                    addDataToFirebase(fileLink, location, title, desc, pCurrentAddress);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterComplaint.this, "Failed to post!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterComplaint.this, e.getMessage() + "firebase", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    });
        } else {
            Toast.makeText(RegisterComplaint.this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}
