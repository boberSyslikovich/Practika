package com.pupukaka.practika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private ListView tasksListView;
    private TaskMessageAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference taskDatabaseReference;
    private ChildEventListener taskChildEventListener;

    private DatabaseReference userDatabaseReference;
    private ChildEventListener userChildEventListener;

    private int number;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        setTitle("Архів");

        database = FirebaseDatabase.getInstance();
        taskDatabaseReference = database.getReference().child("tasks");
        userDatabaseReference = database.getReference().child("users");

        tasksListView = findViewById(R.id.taskListView);
        List<TaskMessage> taskMessages = new ArrayList<>();
        adapter = new TaskMessageAdapter(this, R.layout.task_layout, taskMessages);
        tasksListView.setAdapter(adapter);

        userChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

                if (user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    number = user.getWorkplaceNumber();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        userDatabaseReference.addChildEventListener(userChildEventListener);

        taskChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaskMessage task = snapshot.getValue(TaskMessage.class);
                if (Integer.parseInt(task.getWorkplaceNumber()) == number  || mode==1){
                    adapter.add(task);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        taskDatabaseReference.addChildEventListener(taskChildEventListener);
    }
}