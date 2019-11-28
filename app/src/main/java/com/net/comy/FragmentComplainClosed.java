package com.net.comy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentComplainClosed  extends Fragment {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference,mComplaints;
    private LinearLayout mNoComplaint;
    private ShimmerFrameLayout mShimmerFrameLayout;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.veiwpager_main_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerFrameLayout.startShimmer();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.complain_rec_view);
        mShimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        final HomeAdapter homeAdapter = new HomeAdapter( mContext, 1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(homeAdapter);
        mNoComplaint = view.findViewById(R.id.no_complaint_here);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null) {
            final String userID = mFirebaseAuth.getCurrentUser().getUid();
            mFirebaseDatabase.getReference("admin")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                            UserAdminView userAdminView = pDataSnapshot.getValue(UserAdminView.class);
                            mDatabaseReference = mFirebaseDatabase.getReference("users").child(userID);
                            mComplaints = mFirebaseDatabase.getReference("complaints");
                            if(userAdminView!=null && mFirebaseAuth.getCurrentUser()!=null){
                                if(userAdminView.getFirebaseID().equals(mFirebaseAuth.getCurrentUser().getUid())){
                                    mComplaints.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                                            ComplaintModel complaintModel =pDataSnapshot.getValue(ComplaintModel.class);
                                            // Toast.makeText(mContext, ""+complaintModel.getComplaintTitle(), Toast.LENGTH_SHORT).show();
                                            if(complaintModel!=null) {
                                                if (("closed").equals(complaintModel.getStatus())) {
                                                    mNoComplaint.setVisibility(View.GONE);
                                                    homeAdapter.addComplainFromFirebase(complaintModel);
                                                }
                                            }
                                            mShimmerFrameLayout.stopShimmer();
                                            mShimmerFrameLayout.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                                            homeAdapter.notifyDataSetChanged();
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
                                }else{
                                    mDatabaseReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                                            ComplaintInfo userView = pDataSnapshot.getValue(ComplaintInfo.class);
                                            mComplaints.child(userView.getComplainID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot pDataSnapshot) {
                                                    ComplaintModel complaintModel =pDataSnapshot.getValue(ComplaintModel.class);
                                                    // Toast.makeText(mContext, ""+complaintModel.getComplaintTitle(), Toast.LENGTH_SHORT).show();
                                                    if(complaintModel!=null) {
                                                        if (("closed").equals(complaintModel.getStatus())) {
                                                            mNoComplaint.setVisibility(View.GONE);
                                                            homeAdapter.addComplainFromFirebase(complaintModel);
                                                        }
                                                    }
                                                    mShimmerFrameLayout.stopShimmer();
                                                    mShimmerFrameLayout.setVisibility(View.GONE);
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
    }
}
