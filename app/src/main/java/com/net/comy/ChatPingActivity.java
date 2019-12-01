package com.net.comy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatPingActivity extends AppCompatActivity {

    private RecyclerView mChatRecyclerView;
    private TextView mNoMessageTxt;
    private EditText mChatMessageBox;
    private Toolbar mChatToolbar;
    private FloatingActionButton mSendMessageBtn;
    private ChatAdapter mChatAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ping);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mChatRecyclerView = findViewById(R.id.chat_recycler_view);
        mSendMessageBtn = findViewById(R.id.send_chat_message_btn);
        mChatMessageBox = findViewById(R.id.edt_chat_message);
        mNoMessageTxt = findViewById(R.id.chat_no_messages);
        mChatToolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }
        if (mFirebaseAuth.getCurrentUser() != null) {
            mCurrentUser = mFirebaseAuth.getCurrentUser();
            mChatAdapter = new ChatAdapter(this, mFirebaseAuth.getCurrentUser().getUid());
            mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            mChatRecyclerView.setAdapter(mChatAdapter);
            getMessageFromServer();
        }
        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                sendMessageToServer();
            }
        });

    }

    private void getMessageFromServer() {
        String userId = mCurrentUser.getUid();
        mFirebaseDatabase.getReference("chats")
                .child(userId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot pDataSnapshot, @Nullable String pS) {
                        if (pDataSnapshot.exists()) {
//                            Toast.makeText(ChatPingActivity.this, "here", Toast.LENGTH_SHORT).show();
                            mNoMessageTxt.setVisibility(View.GONE);
                            mChatRecyclerView.setVisibility(View.VISIBLE);
                            ChatMessageModel messageModel = pDataSnapshot.getValue(ChatMessageModel.class);
                            mChatAdapter.addChatMessage(messageModel);
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

    private void sendMessageToServer() {
        String message = mChatMessageBox.getText().toString();
        if (TextUtils.isEmpty(message)) return;
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        if (mCurrentUser != null) {
            String userId = mCurrentUser.getUid();
            long timestamp = System.currentTimeMillis();
            String chatid = String.valueOf(timestamp);
            ChatMessageModel messageModel = new ChatMessageModel(message, userId, timestamp);
            mDatabaseReference = mFirebaseDatabase.getReference("chats").child(userId).child(chatid);
            mDatabaseReference.setValue(messageModel)
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void pVoid) {
                            mChatMessageBox.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {
                            Toast.makeText(ChatPingActivity.this, "Message Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        if (mCurrentUser == null) {
            finish();
        }
    }
}
