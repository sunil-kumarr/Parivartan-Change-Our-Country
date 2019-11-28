package com.net.comy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HomeAdapter mSearchAdapter;
    private List<ComplaintModel> mComplaintModelList;
    private EditText mSearchView;
    private TextView mSeachText;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference, mComplaints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toast.makeText(this, "onCreate called", Toast.LENGTH_SHORT).show();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSearchView = findViewById(R.id.search_complaints_edt);
        mRecyclerView = findViewById(R.id.search_rec_view);
        mSeachText = findViewById(R.id.search_Results_text);
        mSearchAdapter = new HomeAdapter(this, 1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mSearchAdapter);
        if (mFirebaseAuth.getCurrentUser() != null) {
            getComplaintsFromFirebase();
        }
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence pCharSequence, int pI, int pI1, int pI2) {

            }

            @Override
            public void onTextChanged(CharSequence pCharSequence, int pI, int pI1, int pI2) {

            }

            @Override
            public void afterTextChanged(Editable pEditable) {
                if(pEditable.toString().length()>0)
                    filter(pEditable.toString());
               else
                    mSearchAdapter.updateList(new ArrayList<ComplaintModel>());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void getComplaintsFromFirebase() {
        mComplaintModelList = new ArrayList<>();
        final String userID = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
        mFirebaseDatabase.getReference("admin")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                        UserAdminView userAdminView = pDataSnapshot.getValue(UserAdminView.class);
                        mDatabaseReference = mFirebaseDatabase.getReference("users").child(userID);
                        mComplaints = mFirebaseDatabase.getReference("complaints");
                        if (userAdminView != null && mFirebaseAuth.getCurrentUser() != null) {
                            if (userAdminView.getFirebaseID().equals(mFirebaseAuth.getCurrentUser().getUid())) {
                                mComplaints.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                                        ComplaintModel complaintModel = pDataSnapshot.getValue(ComplaintModel.class);
                                        // Toast.makeText(mContext, ""+complaintModel.getComplaintTitle(), Toast.LENGTH_SHORT).show();
                                        if (complaintModel != null) {
                                            mComplaintModelList.add(complaintModel);
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
                            } else {
                                mDatabaseReference.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                                        ComplaintInfo userView = pDataSnapshot.getValue(ComplaintInfo.class);
                                        assert userView != null;
                                        mComplaints.child(userView.getComplainID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot pDataSnapshot) {
                                                ComplaintModel complaintModel = pDataSnapshot.getValue(ComplaintModel.class);
                                                // Toast.makeText(mContext, ""+complaintModel.getComplaintTitle(), Toast.LENGTH_SHORT).show();
                                                if (complaintModel != null) {
                                                    mComplaintModelList.add(complaintModel);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError pDatabaseError) {

                                            }
                                        });

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

    void filter(String searchText) {
        ArrayList<ComplaintModel> temp = new ArrayList<>();
        if (mComplaintModelList.size() > 0) {
            mSeachText.setVisibility(View.GONE);
            for (ComplaintModel d : mComplaintModelList) {
                String title = d.getComplaintTitle().toLowerCase();
                if (title.contains(searchText.toLowerCase())) {
                    temp.add(d);
                }
            }
            mSearchAdapter.updateList(temp);
        } else {
            mSeachText.setVisibility(View.VISIBLE);
            mSeachText.setText("No Results Found!");
            Toast.makeText(this, "No Complaints Found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view) {
        finish();
    }
}
