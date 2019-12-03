package com.example.gost.ticketmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class GenerateTicketActivity extends AppCompatActivity {

    private DBAdapter db;

    Context context;

    EditText timeStamp;
    EditText dateStamp;
    EditText licenseET;
    EditText provET;
    EditText carManET;
    EditText tickID;
    ImageView imageView;
    Spinner spinner;
    Bitmap licBitmap;
    DialogInterface dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ticket);
        //makes app portrait only
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //adds back bar to main
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //db
       // db = new DBAdapter(this);

        //set variables
        context = getApplicationContext();
        timeStamp = findViewById(R.id.timeET);
        dateStamp = findViewById(R.id.dateET);
        licenseET = findViewById(R.id.plateNum);
        provET = findViewById(R.id.provET);
        carManET = findViewById(R.id.carManET);
        imageView = findViewById(R.id.imageView);
        spinner = findViewById(R.id.infrac_spinner);
        tickID = findViewById(R.id.ticIDET);

        tickID.setTextColor(Color.rgb(255,255,255));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.infrac_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //provide unique ID for ticket
        //String uniqueID = UUID.randomUUID().toString();
        //tickID.setText(uniqueID);
        //Random rand = new Random();
        int randomNum = ThreadLocalRandom.current().nextInt(1, 1000+1);
        //double x = (int)Math.random();
        tickID.setText(Integer.toString(randomNum));

        //get date and time for ticket
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String formatDate = date.format(new Date());
        SimpleDateFormat time = new SimpleDateFormat("kk:mm:ss");
        String formatTime = time.format(new Date());
        timeStamp.setText(formatTime);
        dateStamp.setText(formatDate);
        //disable text fields
        timeStamp.setEnabled(false);
        dateStamp.setEnabled(false);
        licenseET.setEnabled(false);
        provET.setEnabled(false);
        tickID.setEnabled(false);
    }

    public void onBackToMainClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Allow non-editable fields to be editable if incorrect data
    public void onEditTicClick(View view){
        carManET.setEnabled(true);
        carManET.setTextColor(Color.rgb(200,0,0));
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    public void dispatchTakePictureIntent(View view) {
        if(askForFilePermissions()) {
            takePicture();
        }
    }

    public boolean askForFilePermissions(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
            return askForFilePermissions();
        }else{
            return true;
        }
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            try {
                //Thumbnail
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                Matrix matrix = new Matrix();
//                matrix.postRotate(90);
//                Bitmap rotatedImage = Bitmap.createBitmap(imageBitmap, 0,0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
                imageView.setImageBitmap(imageBitmap);

                //Actual file
                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                File finalFile = new File(getRealPathFromURI(tempUri));
                System.out.println(tempUri);

                /*TESTING*/
                //using initial bitmap as dataset
                FirebaseVisionImage fvImage = FirebaseVisionImage.fromBitmap(imageBitmap);
                //the on-device model for text-recognition
                FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                //pass the image to the processImage method
                Task<FirebaseVisionText> result = detector.processImage(fvImage)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                // Display the text found in the textView
                                String text = firebaseVisionText.getText();
                                String[] lines = text.split("\n");
                                licenseET.setText(text);

                                boolean nunOrNewf = false;

                                //newfoundland has an odd font that doesn't like to be properly picked up
                                String[] provinces = {"Newfoundland", "Newfoundland and Labrador", "Newfoundland & Labrador", "land", "Prince Edward Island",
                                        "Nova Scotia","New Brunswick", "Brunswick","Québec", "Quebec", "Ontario","Manitoba","Saskatchewan","Alberta","British Columbia",
                                        "Yukon","Northwest Territories","Nunavut"};

                                for (String p : provinces) {
                                    if (text.contains(p) || text.contains(p.toUpperCase())) {
                                        //new brunswick's tag has "new" in very small font and may not be picked up
                                        //nunOrNewf if nunavut or newfoundland
                                        if(p.toUpperCase().equals("BRUNSWICK"))
                                        {
                                            provET.setText("New " + p);
                                            text.replace(p, "");
                                            break;
                                        }
                                        else if(p.toUpperCase().equals("NEWFOUNDLAND") || p.toUpperCase().equals("NEWFOUNDLAND AND LABRADOR") || p.toUpperCase().equals("NEWFOUNDLAND & LABRADOR")|| p.toUpperCase().equals("LAND"))
                                        {
                                            nunOrNewf = true;
                                            provET.setText("Newfoundland");
                                            text.replace(p, "");
                                            break;
                                        }
                                        else if(p.toUpperCase().equals("NUNAVUT"))
                                        {
                                            nunOrNewf = true;
                                        }

                                        provET.setText(p);
                                        text.replace(p, "");
                                        break;
                                    }
                                }

                                //for typical plates, the license number is on the second line
                                //nunavut and newfoundland the license is the first line
                                if(nunOrNewf)
                                    licenseET.setText(lines[0]);
                                else
                                    licenseET.setText(lines[1]);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        licenseET.setText("task failed");
                                    }
                                });
            }catch(Exception e){
                String what = e.getMessage();
            }
        }
    }



    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void OnPublishTicClick(View view){
        Cursor c;
        final Intent intent = new Intent(this, MainActivity.class);

        if(licenseET.getText().equals("") || provET.getText().equals(""))
        {
            //todo make a popup saying no license plate or province
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(GenerateTicketActivity.this);
            builder.setTitle("Confirm Ticket");
            builder.setMessage("Are you sure you want to publish this ticket?");
            builder.setIcon(R.drawable.tickettron);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                    String previousData = "";


                    //save old data
                    try
                    {
                        FileInputStream fin = openFileInput("tickets.csv");
                        InputStreamReader isr = new InputStreamReader(fin);
                        String prev = "";

                        int data = isr.read();

                        while (data != -1) {
                            prev+= (char) data;
                            data = isr.read();
                        }

                        previousData = prev;
                    }
                    catch(IOException ioe)
                    {
                        ioe.printStackTrace();
                    }

                    try ( FileOutputStream ofile = openFileOutput("tickets.csv",MODE_PRIVATE)) {
                        StringBuilder sb = new StringBuilder();

                        OutputStreamWriter osw = new OutputStreamWriter(ofile);

                        sb.append(previousData);
                        sb.append(tickID.getText().toString());
                        sb.append(',');
                        sb.append(dateStamp.getText().toString());
                        sb.append(',');
                        sb.append(timeStamp.getText().toString());
                        sb.append(',');
                        sb.append(licenseET.getText().toString());
                        sb.append(',');
                        sb.append(provET.getText().toString());
                        sb.append(',');
                        sb.append(carManET.getText().toString());
                        sb.append(',');
                        sb.append(spinner.getSelectedItem().toString());
                        sb.append('|');

                        osw.write(sb.toString());
                        osw.flush();
                        osw.close();

                        //todo say that the ticket was saved
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

//                db.open();
//                long tid = db.insertTicket(tickID.toString(), dateStamp.toString(), timeStamp.toString(),
//                        licenseET.toString(), provET.toString(), carManET.toString(),
//                        spinner.toString());
//                db.close();
//                finish();
//                startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}
