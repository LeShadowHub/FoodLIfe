package com.example.foodlife;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by OEM on 11/3/2017.
 */

public class NoteUtility {

    public static final String FILE_EXTENSION = ".bin";
    public static final String EXTRAS_NOTE_FILENAME = "EXTRAS_NOTE_FILENAME";

    /**
     * Save a note in private storage of app
     * @param context Application's contex
     * @param note Note to be saved
     */
    public static boolean  saveNote(Context context, Note note){
        String fileName = String.valueOf(note.getDateTime()) + FILE_EXTENSION;

        FileOutputStream fos;
        ObjectOutputStream oos;

        try{
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(note);
            oos.close();
            fos.close();

        } catch(IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Read all saved notes
     * @param context Application's context
     * @return ArrayList of Note
     */
    public static ArrayList<Note> getAllSavedNotes(Context context){
        ArrayList<Note> notes = new ArrayList<>();

        File filesDir = context.getFilesDir();
        ArrayList<String> noteFiles = new ArrayList<>();

        for(String file : filesDir.list()){
            if(file.endsWith(FILE_EXTENSION)){
                noteFiles.add(file);
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        // Add Note objects to list of notes
        for(int i = 0; i < noteFiles.size(); i++){
            try{
                fis = context.openFileInput(noteFiles.get(i));
                ois = new ObjectInputStream(fis);

                notes.add((Note)ois.readObject());

                fis.close();
                ois.close();

            } catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }
        return notes;
    }

    /**
     * Loads note by file name
     * @param context Application's context
     * @param fileName Name of note file
     * @return Note object
     */
    public static Note getNoteByName(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        Note note;

        if(file.exists() && !file.isDirectory()){
            FileInputStream fis;
            ObjectInputStream ois;

            try{
                fis = context.openFileInput(fileName);
                ois = new ObjectInputStream(fis);

                note = (Note) ois.readObject();

                fis.close();
                ois.close();

            } catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
            return note;
        }
        return null;
    }

    public static boolean deleteNote(Context context, String fileName) {
        File dir = context.getFilesDir();
        File file = new File(dir, fileName);

        if(file.exists() && !file.isDirectory()){
            return file.delete();
        }
        return false;
    }
}
