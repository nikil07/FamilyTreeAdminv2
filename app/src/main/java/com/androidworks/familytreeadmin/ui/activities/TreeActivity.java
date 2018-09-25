package com.androidworks.familytreeadmin.ui.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.androidworks.familytreeadmin.R;
import com.androidworks.familytreeadmin.data.DataStore;
import com.androidworks.familytreeadmin.data.model.Member;
import com.androidworks.familytreeadmin.ui.adapters.TreeAdapter;
import com.androidworks.familytreeadmin.utils.Constants;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.blox.treeview.BaseTreeAdapter;
import de.blox.treeview.TreeNode;
import de.blox.treeview.TreeView;

public class TreeActivity extends AppCompatActivity {

    StorageReference storageRef;
    DatabaseReference myRef;
    FirebaseStorage storage;
    FirebaseDatabase database;
    @BindView(R.id.treeView)
    TreeView treeView;
    private Gson gson = new Gson();
    private int nodeCount = 0;
    BaseTreeAdapter adapter;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    final private String TAG = "NIK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        ButterKnife.bind(this);
        init();
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        adapter = new TreeAdapter(this, R.layout.node);
        treeView.setAdapter(adapter);

        treeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(TreeActivity.this, ((Member) parent.getAdapter().getItem(position)).getName(), Toast.LENGTH_SHORT).show();
                Member clickedMember = (Member) parent.getAdapter().getItem(position);
                Log.d("NIK","Position " + position);
                update(clickedMember, position);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private String getNodeText() {
        return "Member " + nodeCount++;
    }

    private void init() {
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://nivezzle.appspot.com/");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("FAMILY");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("nikhil", "Value is: " + value);
                DataStore.getInstance(TreeActivity.this).setMembersJSON(value);
                initViews();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("nikhil", "Failed to read value.", error.toException());
            }
        });

    }

    private void initViews() {
        ArrayList<Member> members = convertToList(DataStore.getInstance(this).getMembersJSON());
        TreeNode rootNode = new TreeNode(getRootMember());
        TreeNode firstGenNode = null, secondGenNode = null, thirdGenNode = null, fourthGenNode = null, fifthGenNode = null, sixthGenNode = null;

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            switch (member.getGeneration()) {
                case 1:
                    firstGenNode = new TreeNode(member);
                    rootNode.addChild(firstGenNode);
                    break;
                case 2:
                    secondGenNode = new TreeNode(member);
                    if (firstGenNode != null) {
                        firstGenNode.addChild(secondGenNode);
                    }
                    break;
                case 3:
                    thirdGenNode = new TreeNode(member);
                    if (secondGenNode != null) {
                        secondGenNode.addChild(thirdGenNode);
                    }
                    break;
                case 4:
                    Log.d(TAG, "Gen 1");
                    break;
                case 5:
                    Log.d(TAG, "Gen 1");
                    break;
                case 6:
                    Log.d(TAG, "Gen 1");
                    break;
                case 7:
                    Log.d(TAG, "Gen 1");
                    break;
            }
        }
        adapter.setRootNode(rootNode);
    }


    private Member getRootMember() {
        Member rootMember = new Member();

        rootMember.setBirthYear(Constants.ROOT_NODE_MEMBER_BIRTH_YEAR);
        rootMember.setDead(Constants.ROOT_NODE_IS_DEAD);
        rootMember.setGeneration(Constants.ROOT_NODE_MEMBER_GENERATION);
        rootMember.setDeathYear(Constants.ROOT_NODE_MEMBER_DEATH_YEAR);
        rootMember.setName(Constants.ROOT_NODE_MEMBER_NAME);
        rootMember.setNickName(Constants.ROOT_NODE_MEMBER_NICKNAME);
        rootMember.setLocation(Constants.ROOT_NODE_MEMBER_LOCATION);

        return rootMember;
    }

    public ArrayList<Member> convertToList(String JSON) {
        return gson.fromJson(JSON, new TypeToken<ArrayList<Member>>() {
        }.getType());
    }

    private void update(final Member clickedMember, final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = View.inflate(this, R.layout.choose_member_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button childBT = (Button) dialogView.findViewById(R.id.bt_child);
        final Button spouseBT = (Button) dialogView.findViewById(R.id.bt_spouse);
        final Button siblingBT = (Button) dialogView.findViewById(R.id.bt_sibling);

        dialogBuilder.setTitle("Add a member");
//        dialogBuilder.setMessage("Choose the type of member you want to add");
//        dialogBuilder.setPositiveButton("Save", null);
//        dialogBuilder.setNegativeButton("Cancel", null);
        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        });

        childBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TreeActivity.this, "Child clicked", Toast.LENGTH_SHORT).show();
                addMemberDetails(clickedMember.getGeneration() + 1, position);
                alertDialog.dismiss();
            }
        });

        spouseBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TreeActivity.this, "Spouse clicked", Toast.LENGTH_SHORT).show();
                clickedMember.setGeneration(clickedMember.getGeneration() + 1);
                alertDialog.dismiss();
            }
        });

        siblingBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TreeActivity.this, "Sibling clicked", Toast.LENGTH_SHORT).show();
                clickedMember.setGeneration(clickedMember.getGeneration());
                addMemberDetails(clickedMember.getGeneration(), position);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void addMemberDetails(final int generation, final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = View.inflate(this, R.layout.member_details_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText nameET = (EditText) dialogView.findViewById(R.id.et_member_name);
        final EditText nickNameET = (EditText) dialogView.findViewById(R.id.et_member_nick_name);
        final EditText birthYearET = (EditText) dialogView.findViewById(R.id.et_member_birth_year);
        final EditText locationET = (EditText) dialogView.findViewById(R.id.et_member_location);
        final EditText deathYearET = (EditText) dialogView.findViewById(R.id.et_member_death_year);
        final CheckBox isDeadCB = (CheckBox) dialogView.findViewById(R.id.cb_is_dead);

        isDeadCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeadCB.isChecked())
                    deathYearET.setVisibility(View.VISIBLE);
                else
                    deathYearET.setVisibility(View.GONE);
            }
        });

        dialogBuilder.setPositiveButton("Done", null);
        dialogBuilder.setTitle("Edit Details");
//        dialogBuilder.setMessage("Choose the type of member you want to add");
        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!nameET.getText().toString().isEmpty() &&
                                !nickNameET.getText().toString().isEmpty() &&
                                !birthYearET.getText().toString().isEmpty() &&
                                !locationET.getText().toString().isEmpty()) {

                            Member member = new Member();
                            member.setName(nameET.getText().toString());
                            member.setNickName(nickNameET.getText().toString());
                            member.setLocation(locationET.getText().toString());
                            member.setGeneration(generation);
                            member.setBirthYear(Integer.parseInt(birthYearET.getText().toString()));
                            member.setDead(isDeadCB.isChecked());
                            if (isDeadCB.isChecked() && !deathYearET.getText().toString().isEmpty())
                                member.setDeathYear(Integer.parseInt(deathYearET.getText().toString()));

                            addMember(member, position);
                            Toast.makeText(TreeActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(TreeActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });

        alertDialog.show();
    }

    private void addMember(Member member, int position) {
//        Member newMember = new Member();
//        member.setBirthYear(Integer.parseInt(birth.getText().toString()));
//        member.setDead(isDead.isChecked());
//        member.setGeneration(Integer.parseInt(generation.getText().toString()));
//        member.setDeathYear(Integer.parseInt(death.getText().toString()));
//        member.setName(name.getText().toString());
//        member.setNickName(nickName.getText().toString());
//        member.setLocation(location.getText().toString());

        DataStore.getInstance(TreeActivity.this).storeMembers(member, position);
        myRef.setValue(DataStore.getInstance(TreeActivity.this).getMembersJSON());
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            treeView.setScaleX(mScaleFactor);
            treeView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
