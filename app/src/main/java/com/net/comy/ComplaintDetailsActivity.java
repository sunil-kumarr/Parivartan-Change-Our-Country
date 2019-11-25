package com.net.comy;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ComplaintDetailsActivity extends AppCompatActivity {
    TextView category, title, desc, requestID, dataTime, remarksTime, mStatus,mHappenedAt,mRegisteredAt;
    EditText mRemarks;
    Button mChangeComplaintStatus;
    ImageView mImageView;
    ProgressBar mProgressBar;
    CardView mComplainTab;
    View mLineRemarks;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    String complainId;
    static boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("admin");
        mChangeComplaintStatus = findViewById(R.id.complain_status_button);
        remarksTime = findViewById(R.id.complain_remarks_time);
        mRemarks = findViewById(R.id.complain_remarks_by_admin);
        category = findViewById(R.id.complain_category);
        title = findViewById(R.id.complain_title);
        desc = findViewById(R.id.complain_Desc);
        requestID = findViewById(R.id.complain_ref_id);
        dataTime = findViewById(R.id.complain_time);
        mImageView = findViewById(R.id.complain_image_preview);
        mProgressBar = findViewById(R.id.complain_image_preview_progressbar);
        mComplainTab = findViewById(R.id.Main_complainTab);
        mLineRemarks = findViewById(R.id.line2);
        mStatus = findViewById(R.id.complain_status);
        mHappenedAt = findViewById(R.id.complain_addrees);
        mRegisteredAt = findViewById(R.id.complain_filling_location);

        Intent intent = getIntent();
        if (intent != null) {
            complainId = intent.getStringExtra("complainId");
            if (complainId != null) {
                getComplaintDetails();
                mChangeComplaintStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        updateComplaintRemarksAndStatus();
                    }
                });
            }
        }
    }

    private void updateComplaintRemarksAndStatus() {
        String remarks = mRemarks.getText().toString();
        long timestamp = System.currentTimeMillis();
        Map<String, Object> update = new HashMap<>();
        update.put("remarksByAdmin", remarks);
        update.put("remarksTimeStamp", timestamp);
        update.put("status", "closed");
        DatabaseReference reference = mFirebaseDatabase.getReference("complaints").child(complainId);
        reference.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> pTask) {
                if (pTask.isSuccessful()) {
                    mChangeComplaintStatus.setVisibility(View.GONE);
                    Toast.makeText(ComplaintDetailsActivity.this, "Closed Complaint.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ComplaintDetailsActivity.this, "Sorry,Can't close complaint.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAdminControls(final String complaintStatus) {
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                UserAdminView userAdminView = pDataSnapshot.getValue(UserAdminView.class);
                if (userAdminView != null && complaintStatus.equals("open")) {
                    if(userAdminView.getFirebaseID().equals(mFirebaseAuth.getCurrentUser().getUid())) {
                        Toast.makeText(ComplaintDetailsActivity.this, "Admin Account", Toast.LENGTH_SHORT).show();
                        mChangeComplaintStatus.setVisibility(View.VISIBLE);
                        mRemarks.setVisibility(View.VISIBLE);
                        mLineRemarks.setVisibility(View.VISIBLE);
                        mChangeComplaintStatus.setBackgroundColor(getResources().getColor(R.color.red));
                        mChangeComplaintStatus.setText("Close Complaint");
                    }
                } else {
                   // Toast.makeText(ComplaintDetailsActivity.this, "Not Admin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot pDataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError pDatabaseError) {

            }
        });
    }

    private void getComplaintDetails() {
        mFirebaseDatabase
                .getReference("complaints")
                .child(complainId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot pDataSnapshot) {
                        ComplaintModel complaintModel = pDataSnapshot.getValue(ComplaintModel.class);
                        if (complaintModel == null) {
                            return;
                        }
                        String status = complaintModel.getStatus();
                        showAdminControls(status);
                        if (complaintModel.getStatus().equals("open")) {
                            mStatus.setText("OPEN");
                        } else {
                            mStatus.setText("CLOSED");
                            mStatus.setTextColor(getResources().getColor(R.color.white));
                            mStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_fill));
                        }
                        if (complaintModel.getComplaintImage() == null) {
                            mImageView.setVisibility(View.GONE);
                            mProgressBar.setVisibility(View.GONE);
                        } else {
                            Picasso.get()
                                    .load(complaintModel.getComplaintImage())
                                    .fit()
                                    .into(mImageView, new Callback() {

                                        @Override
                                        public void onSuccess() {
                                            mProgressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                        }
                        if(complaintModel.getRemarksByAdmin()!=null){
                            mRemarks.setVisibility(View.VISIBLE);
                            mRemarks.setEnabled(false);
                            mRemarks.setText(String.format("Remarks : %s", complaintModel.getRemarksByAdmin()));
                            remarksTime.setVisibility(View.VISIBLE);
                            mLineRemarks.setVisibility(View.VISIBLE);
                            remarksTime.setText(String.format("Resolved At: %s", Utils.getDateTime(complaintModel.getRemarksTimeStamp())));
                        }
                        title.setText(complaintModel.getComplaintTitle());
                        desc.setText(complaintModel.getComplaintDetails());
                        mHappenedAt.setText(String.format("Happened At: %s", complaintModel.getHappenedAt()));
                        mRegisteredAt.setText(String.format("Registered At : %s", complaintModel.getSubmitFromAddress()));
                        category.setText(complaintModel.getComplaintCategory());
                        requestID.setText(String.format("Request ID: %s", complaintModel.getRequestId()));
                        dataTime.setText(String.format("Date: %s", Utils.getDateTime(complaintModel.getTimestamp())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError pDatabaseError) {

                    }
                });
    }
}
