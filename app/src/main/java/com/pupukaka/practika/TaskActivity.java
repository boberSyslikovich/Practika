package com.pupukaka.practika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.ChildEventRegistration;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private ListView tasksListView;
    private TaskMessageAdapter adapter;
    private FloatingActionButton addNewTaskButton;
    private ProgressBar progressBar;

    private FirebaseDatabase database;
    private DatabaseReference taskDatabaseReference;
    private ChildEventListener taskChildEventListener;

    private DatabaseReference userDatabaseReference;
    private ChildEventListener userChildEventListener;

    private int number;
    private int mode;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        database = FirebaseDatabase.getInstance();
        taskDatabaseReference = database.getReference().child("tasks");
        userDatabaseReference = database.getReference().child("users");

        progressBar = findViewById(R.id.progressBar);
        tasksListView = findViewById(R.id.taskListView);
        List<TaskMessage> taskMessages = new ArrayList<>();
        adapter = new TaskMessageAdapter(this, R.layout.task_layout, taskMessages);
        tasksListView.setAdapter(adapter);
        addNewTaskButton = findViewById(R.id.floatingActionButton);
        addNewTaskButton.setVisibility(View.GONE);

        progressBar.setVisibility(View.INVISIBLE);

        addNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, AddNewTaskActivity.class);
                startActivity(intent);
            }
        });

        userChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

                if (user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    setTitle(user.getName());
                    number = user.getWorkplaceNumber();
                    if (user.getMode() == 1) {
                        mode = 1;
                        addNewTaskButton.setVisibility(View.VISIBLE);
                    } else {
                        mode = 0;
                        addNewTaskButton.setVisibility(View.GONE);
                    }
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
                if ((Integer.parseInt(task.getWorkplaceNumber()) == number && task.getExecutionStatus() == 0) || mode==1){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.taskArchive:
                Intent intent = new Intent(TaskActivity.this, ArchiveActivity.class);
                startActivity(intent);
                return true;
            case R.id.calculated:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}