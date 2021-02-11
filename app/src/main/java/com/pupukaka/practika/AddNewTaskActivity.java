package com.pupukaka.practika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewTaskActivity extends AppCompatActivity {
    private static final int RC_IMAGE_PICKER = 123;
    private EditText workplaceTaskEditText;
    private EditText descriptionTaskEditText;
    private EditText timeTaskEditText;
    private Button downloadBlueprintButton;
    private ImageView downloadBlueprintImageView;
    private Calendar currentTime = Calendar.getInstance();


    private FirebaseDatabase database;
    private DatabaseReference taskDatabaseReference;
    private FirebaseStorage storage;
    private StorageReference blueprintStorageReference;

    private String blueprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        setTitle("Додати нове завдання");

        database = FirebaseDatabase.getInstance();
        taskDatabaseReference = database.getReference().child("tasks");
        storage = FirebaseStorage.getInstance();
        blueprintStorageReference = storage.getReference().child("blueprints");


        workplaceTaskEditText = findViewById(R.id.workplaceTaskEditText);
        descriptionTaskEditText = findViewById(R.id.descriptionEditText);
        timeTaskEditText = findViewById(R.id.timeEditText);
        downloadBlueprintButton = findViewById(R.id.downloadBlueprintButton);
        downloadBlueprintImageView = findViewById(R.id.downloadBlueprintImageView);

        timeTaskEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddNewTaskActivity.this, time ,
                        currentTime.get(Calendar.HOUR_OF_DAY),
                        currentTime.get(Calendar.MINUTE), true)
                        .show();
                Log.i("Test", "Слушатель работает нормально");
            }
        });

        downloadBlueprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Оберіть зображення"), RC_IMAGE_PICKER);

            }
        });

    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        timeTaskEditText.setText(sdf.format(currentTime.getTime()));
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener time =new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            currentTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.saveTask:
                if (workplaceTaskEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AddNewTaskActivity.this, "Будь ласка, вкажіть номер робочого місця!",
                            Toast.LENGTH_LONG).show();
                } else if (descriptionTaskEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AddNewTaskActivity.this, "Будь ласка, дайте замітки, необхідні для завдання!",
                            Toast.LENGTH_LONG).show();
                } else if (timeTaskEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AddNewTaskActivity.this, "Будь ласка, вкажіть термін виконання завдання!",
                            Toast.LENGTH_LONG).show();
                } else if (blueprint == null) {
                    Toast.makeText(AddNewTaskActivity.this, "Будь ласка, завантажте креслення!",
                            Toast.LENGTH_LONG).show();
                } else {
                    TaskMessage taskMessage = new TaskMessage();

                    taskMessage.setImageUrl(blueprint);
                    taskMessage.setTime(timeTaskEditText.getText().toString());
                    taskMessage.setExecutionStatus(0);
                    taskMessage.setNotes(descriptionTaskEditText.getText().toString());
                    taskMessage.setWorkplaceNumber(workplaceTaskEditText.getText().toString());

                    taskDatabaseReference.push().setValue(taskMessage);
                    finish();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK){

            if (data.getData() == null){
                Log.d("Test", "данные пустиые");
            }
            Uri selectedImageUrl = data.getData();
            downloadBlueprintImageView.setImageURI(selectedImageUrl);
            StorageReference imageReference = blueprintStorageReference.child(selectedImageUrl.getLastPathSegment());

            UploadTask uploadTask = imageReference.putFile(selectedImageUrl);


            uploadTask = imageReference.putFile(selectedImageUrl);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        blueprint = downloadUri.toString();
                        // taskDatabaseReference.push().setValue(taskMessage);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
}