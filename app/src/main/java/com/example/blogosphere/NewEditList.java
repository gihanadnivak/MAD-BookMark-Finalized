package com.example.blogosphere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.ListModal;
import com.example.blogosphere.database.UserModel;

public class NewEditList extends AppCompatActivity {

    private EditText ET_EnterEditTopic, ET_EnterEditDiscription;
    private Button btn_editList_Update;
    private DBHelper myDB;
    private Context context;
    UserModel user;

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_list);

        context = this ;
        myDB = new DBHelper(context);

        // Get login user object
        userID = getIntent().getIntExtra("UserID",0);
        user = myDB.getUserbyID(userID);

        ET_EnterEditTopic = findViewById(R.id.ET_EnterEditTopic);
        ET_EnterEditDiscription = findViewById(R.id.ET_EnterEditDiscription);
        btn_editList_Update = findViewById(R.id.btn_editList_Update);

        final String list_id = getIntent().getStringExtra("LIST_ID");
        ListModal Newlistmodel = myDB.getSingleList(Integer.parseInt(list_id));

        ET_EnterEditTopic.setText(Newlistmodel.getList_Topic());
        ET_EnterEditDiscription.setText(Newlistmodel.getList_Description());
        Integer listuserID = Newlistmodel.getUser_ID();

        btn_editList_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listTopicedited = ET_EnterEditTopic.getText().toString();
                String listdescriptionedited = ET_EnterEditDiscription.getText().toString();

                if(listTopicedited.isEmpty() && listdescriptionedited.isEmpty() ){
                    ET_EnterEditTopic.setError("Please enter Topic");
                    ET_EnterEditDiscription.setError("Please enter a discription");
                    return;
                }
                else if (listdescriptionedited.isEmpty()){
                    ET_EnterEditDiscription.setError("Please enter a discription");
                    return;
                }
                else if (listTopicedited.isEmpty()){
                    ET_EnterEditTopic.setError("Please enter Topic");
                    return;
                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewEditList.this);
                    builder.setTitle("Alert !");
                    builder.setMessage("Are you sure you want to update list");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ListModal listModel = new ListModal(Integer.parseInt(list_id),listuserID ,listTopicedited ,listdescriptionedited);
                            myDB.updateSingleList(listModel);
                            Intent intentbookmark = new Intent(context,Bookmarks.class);
                            intentbookmark.putExtra("UserID", userID);
                            startActivity(intentbookmark);
                            Toast.makeText(context, "You Edited a List", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }


            }
        });

    }
}