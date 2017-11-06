package com.example.foodlife;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private EditText mEtTitle;
    private EditText mEtContent;

    private String mNoteFileName;
    private Note mLoadedNote = null;
    private long mNoteCreationTime;
    private boolean mViewOrUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mEtTitle = (EditText) findViewById(R.id.note_edit_et_title);
        mEtContent = (EditText) findViewById(R.id.note_edit_et_content);

        // Check if loading note or creating new note
        mNoteFileName = getIntent().getStringExtra(NoteUtility.EXTRAS_NOTE_FILENAME);
        if(mNoteFileName != null && !mNoteFileName.isEmpty() && mNoteFileName.endsWith(NoteUtility.FILE_EXTENSION)){
            mLoadedNote = NoteUtility.getNoteByName(this, mNoteFileName);

            if(mLoadedNote != null){
                mEtTitle.setText(mLoadedNote.getTitle());
                mEtContent.setText(mLoadedNote.getContent());
                mNoteCreationTime = mLoadedNote.getDateTime();
                mViewOrUpdate = true;
            }
        } else{     // Creating new note
            mNoteCreationTime = System.currentTimeMillis();
            mViewOrUpdate = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load menu based on state of activity
        if(mViewOrUpdate){
            getMenuInflater().inflate(R.menu.menu_note_view, menu);
        } else{
            getMenuInflater().inflate(R.menu.menu_note_new, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_note_save:
            case R.id.action_note_update:
                validateAndSaveNote();
                break;
            case R.id.action_note_delete:
                deleteNote();
                break;
            case R.id.action_note_cancel:
                cancelNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        cancelNote();
    }

    private void validateAndSaveNote(){

        String title = mEtTitle.getText().toString().trim();
        String content = mEtContent.getText().toString().trim();

        // See if user has entered anything
        if(title.isEmpty()){
            Toast.makeText(this, "Please enter a title for your note!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(content.isEmpty()){
            Toast.makeText(this, "Please enter content for your note!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Keep original creation time if old note, otherwise load new creation time
        if(mLoadedNote != null){
            mNoteCreationTime = mLoadedNote.getDateTime();
        } else{
            mNoteCreationTime = System.currentTimeMillis();
        }

        // Save the note
        if(NoteUtility.saveNote(this, new Note(mNoteCreationTime, title, content))){
            Toast.makeText(this, "Note has been saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else{
            Toast.makeText(this, "Cannot save the note, please make sure you have enough space on your device",
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void deleteNote(){

        // Make sure user wants to delete note
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Delete note")
                .setMessage("You are about to delete " + mEtTitle.getText().toString() + ", are you sure?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mLoadedNote != null && NoteUtility.deleteNote(getApplicationContext(), mNoteFileName)){
                            Toast.makeText(NoteActivity.this, mLoadedNote.getTitle() + " is deleted",
                                    Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(NoteActivity.this, "Cannot delete the note '" + mLoadedNote.getTitle() +
                                    "'", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                })
                .setNegativeButton("NO", null);

        dialog.show();
    }

    private void cancelNote(){
        if(!checkNoteAltered()){  // If note is not altered by user
            finish();
        } else{ // Remind user to save changes or not
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("Discard changes..")
                    .setMessage("Are you sure you do not want to save changes to this note?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("NO", null);

            dialogCancel.show();
        }
    }

    /**
     * Check if the loaded note/new note was altered by the user
     * @return true if note is change, false otherwise
     */
    private boolean checkNoteAltered(){
        if(mViewOrUpdate){  // If load note for view/update
            return mLoadedNote != null && (!mEtTitle.getText().toString().equalsIgnoreCase(mLoadedNote.getTitle())) ||
                    !mEtContent.getText().toString().equalsIgnoreCase(mLoadedNote.getContent());
        } else{ // If load new note
            return !mEtTitle.getText().toString().isEmpty() || !mEtContent.getText().toString().isEmpty();
        }
    }
}
