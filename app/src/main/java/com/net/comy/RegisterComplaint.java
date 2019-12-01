package com.net.comy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.Locale;

public class RegisterComplaint extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 165;
    private Uri mImageUri;
    private ImageView mImageView;
    private String fileLink;
    private FloatingActionButton mNextButton;
    ArrayList<String> names;
    private String currentAddress;
    private EditText mComplaintAddress, mComplaintTitle, mComplaintDesc;
    private ProgressDialog mDialog;
    private FrameLayout mFragmentContainer;
    private RecyclerView mRecyclerView;
    private ConstraintLayout mStepOne,mStepTwo;
    private Button mNextStepBtn,mRegisterBtn,mGetImage;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, mUserComplaint;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Geocoder mGeocoder;
    private CategoryAdapter adapter;

    private FragmentManager mFragmentManager;

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
        mGeocoder = new Geocoder(this);

        mStepOne = findViewById(R.id.container_step_1);
        mStepTwo = findViewById(R.id.container_step_2);
        setUpRegisterStepOne();
        setUpRegisterStepTwo();
    }


    private void setUpRegisterStepTwo() {
        mImageView = findViewById(R.id.complaint_image);
        mGetImage = findViewById(R.id.add_complaint_image);
        mComplaintAddress = findViewById(R.id.edt_complaint_location);
        mComplaintDesc = findViewById(R.id.edt_complaint_description);
        mComplaintTitle = findViewById(R.id.edt_complaint_title);
        mRegisterBtn = findViewById(R.id.register_com_button);
        mGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                openFileChooser();
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                mFusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> pTask) {
                                if (pTask.isSuccessful()) {
                                    Location pTaskResult = pTask.getResult();
                                    try {
                                        List<Address> addressList = mGeocoder.getFromLocation(pTaskResult.getLatitude(),pTaskResult.getLongitude(),1);
                                        Address address = addressList.get(0);
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                            sb.append(address.getAddressLine(i)).append("\n");
                                        }
                                        sb.append(address.getLocality()).append("\n");
                                        sb.append(address.getPostalCode()).append("\n");
                                        sb.append(address.getCountryName());
                                        currentAddress = sb.toString();
                                        registerComplaint(currentAddress);
                                    } catch (IOException pE) {
                                        pE.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(RegisterComplaint.this, "Location Error!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void setUpRegisterStepOne() {
        mNextStepBtn = findViewById(R.id.next_step_btn);
        mRecyclerView = findViewById(R.id.category_select_rec_view);
        ArrayList<String> names = new ArrayList<>();
        names.add("Government & Politics");
        names.add("Restaurant & Cafes");
        names.add("Hotels");
        names.add("Telecom & Internet");
        names.add("E-Commerce");
        names.add("Railways");
        names.add("Flights & Airports");
        names.add("E-Wallet Payments");
        names.add("Banking");
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_politics);
        images.add(R.drawable.ic_restaurtant);
        images.add(R.drawable.ic_hotel);
        images.add(R.drawable.ic_telecom);
        images.add(R.drawable.ic_ecom);
        images.add(R.drawable.ic_railway);
        images.add(R.drawable.ic_flight);
        images.add(R.drawable.ic_wallet);
        images.add(R.drawable.ic_banking);
        adapter = new CategoryAdapter(this,names,images);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                mStepOne.setVisibility(View.GONE);
                mStepTwo.setVisibility(View.VISIBLE);
                TextView view = findViewById(R.id.toolbar_custom);
                view.setText("Enter Complaint Details");
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
            Toast.makeText(this, "Invalid inputs!!", Toast.LENGTH_SHORT).show();
            return;
        }
        checkImageFile(location,title, desc, pCurrentAddress);

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
            Picasso.get().load(mImageUri).fit().into(mImageView);

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

        if (mFirebaseUser != null ) {
            final String userId = mFirebaseUser.getUid();
            final String userEmail = mFirebaseUser.getEmail();
            String phoneNumber = mFirebaseUser.getPhoneNumber();

            String category = "General";
            category = adapter.getSelectedItem();
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
                            productId,
                            System.currentTimeMillis());

            mDatabaseReference.child(productId).setValue(complaintModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            ComplaintInfo userView = new ComplaintInfo();
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

    void checkImageFile(final String location, final String title, final String desc, final String pCurrentAddress){
        if(fileLink == null){
            new AlertDialog.Builder(this)
                    .setTitle("No Image Selected!")
                    .setMessage("Do you want to continue?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface pDialogInterface, int pI) {
                            mDialog = new ProgressDialog(RegisterComplaint.this);
                            mDialog.setMessage("Registering...");
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                            uploadImage(location,title, desc, pCurrentAddress);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface pDialogInterface, int pI) {
                    Toast.makeText(RegisterComplaint.this, "Cancelled !", Toast.LENGTH_SHORT).show();
                }
            }).create().show();
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
            addDataToFirebase(null, location, title, desc, pCurrentAddress);
        }
    }

    public void goBackNow(View view) {
        finish();
    }
}
