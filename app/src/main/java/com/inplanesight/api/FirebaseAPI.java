package com.inplanesight.api;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.ui.find.HuntFragment;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FirebaseAPI extends Service {

    private final FirebaseAuth mAuth;

    private static FirebaseFirestore db;

    private static FirebaseStorage storage;

    private static StorageReference storageRef;

    public FirebaseAPI() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static void writeToFirebase(Object object, String collection) {
        // Add a new document with a generated ID
        db.collection(collection)
                .add(object)
                .addOnSuccessListener(documentReference -> Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Failure", "Error adding document", e));
    }

    public static void readFromFirebase(String airportCode, String collection, ArrayList<JSONObject> list, GameViewModel.ProcessHunts f) {
        //Query firebase for airport, return empty if nothing found
        db.collection(collection)
                .whereEqualTo("airportCode", airportCode)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Success", document.getId() + " => " + document.getData());
                            list.add(new JSONObject(document.getData()));
                        }
                    } else {
                        Log.w("Failure", "Error getting documents.", task.getException());
                    }
                    try {
                        f.f();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void uploadPhotoBitmapFromPlace(Bitmap bitmap, String imageRef) {
        // Create a reference
        StorageReference placeRef = storageRef.child(imageRef);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = placeRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            exception.printStackTrace();
        }).addOnSuccessListener(taskSnapshot -> {
            Log.d("upload", taskSnapshot.getMetadata().toString());
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
        });
    }

    public static void downloadPhotoFromStorage(String imageRef, OnSuccessListener<byte[]> f) {
        StorageReference image = storage.getReferenceFromUrl("gs://in-plane-sight-adbd4.appspot.com/" + imageRef);
        Log.d("ImageRef", imageRef);

        final long ONE_MEGABYTE = 1024 * 1024;
        image.getBytes(ONE_MEGABYTE).addOnSuccessListener(f).addOnFailureListener(exception ->
                Log.d("Failure", "downloadPhotoFromStorage(" + imageRef + "): " + exception.getMessage()));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}