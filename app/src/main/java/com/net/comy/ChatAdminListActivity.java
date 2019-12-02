package com.net.comy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class ChatAdminListActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser mFirebaseUser;

    private RecyclerView mChatListView;
    private Toolbar mToolbar;
    private ChatAdminAdapter mChatAdminAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_admin_list);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mToolbar = findViewById(R.id.ping_toolbar);
        mChatListView = findViewById(R.id.ping_recycler_View);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
        mChatAdminAdapter = new ChatAdminAdapter(this);
        mChatListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mChatListView.setAdapter(mChatAdminAdapter);
        getPings();
    }

    private void getPings() {
        mFirebaseDatabase.getReference("chats")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                        if (pDataSnapshot.exists()) {
                            ChatNodeModel nodeModel = pDataSnapshot.getValue(ChatNodeModel.class);
                            if (nodeModel != null) {
                                mChatAdminAdapter.addChatMessage(nodeModel);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                        mChatAdminAdapter.notifyDataSetChanged();
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
