package en.sundaram.flashly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {


    ImageButton flashLightMainButton, flashLightState, flashLightSosButton;
    boolean state, state2;
    long blinkDelay = 400;
    String myString = "0101010101";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        flashLightState =findViewById(R.id.lightState);
        flashLightSosButton = findViewById(R.id.btnSos); //SOS button

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            
                runFlashLight();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "Camera permission is required", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();


        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void runFlashLight()
    {
        flashLightState.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {

                if(!state)
                {
                    CameraManager cameraManager =(CameraManager) getSystemService(Context.CAMERA_SERVICE);

                    try
                    {
                        String CameraId=cameraManager.getCameraIdList()[0]; //0 back 1 front
                        cameraManager.setTorchMode(CameraId, true);
                        state=true;

                        flashLightState.setBackgroundResource(R.drawable.torch_on);
                    }
                    catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    CameraManager cameraManager =(CameraManager) getSystemService(Context.CAMERA_SERVICE);

                    try
                    {
                        String CameraId=cameraManager.getCameraIdList()[0]; //0 back 1 front
                        cameraManager.setTorchMode(CameraId, false);
                        state=false;

                        flashLightState.setBackgroundResource(R.drawable.torch_off);
                    }
                    catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        //Blinking

        flashLightSosButton.setOnClickListener(new View.OnClickListener() {

            CameraManager cameraManager =(CameraManager) getSystemService(Context.CAMERA_SERVICE);


            @Override
            public void onClick(View view) {
                if (!state)
                {
                    flashLightState.setBackgroundResource(R.drawable.ic_sos2);

                 //Delay the blink in ms
                for (int i = 0; i < myString.length(); i++) {
                    if (myString.charAt(i) == '0') {
                        flashLightState.setBackgroundResource(R.drawable.torch_on);
                        try {
                            String CameraId = cameraManager.getCameraIdList()[0]; //0 back 1 front
                            cameraManager.setTorchMode(CameraId, true);
                            state = true;


                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }


                    }
                    else
                    {
                        state2 = false;

                        try {
                            String CameraId = cameraManager.getCameraIdList()[0]; //0 back 1 front
                            cameraManager.setTorchMode(CameraId, false);
                            state = false;
                            flashLightState.setBackgroundResource(R.drawable.ic_sos);
                            flashLightState.setBackgroundResource(R.drawable.torch_off);


                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        Thread.sleep(blinkDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                }
                else
                {
                    CameraManager cameraManager =(CameraManager) getSystemService(Context.CAMERA_SERVICE);

                    try
                    {
                        String CameraId=cameraManager.getCameraIdList()[0]; //0 back 1 front
                        cameraManager.setTorchMode(CameraId, false);
                        state=false;

                        flashLightState.setBackgroundResource(R.drawable.torch_off);
                    }
                    catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // long press
        flashLightSosButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                // TODO Auto-generated method stub
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, flashLightSosButton);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        // Toast message on menu item clicked

                        Toast.makeText(MainActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                        String temp1 = menuItem.toString();

                        if(temp1.equals("Slow"))
                        {
                            myString = "01010101";
                            blinkDelay = 800;
                            Log.d("myTag", "Delay by 800");
                        }
                        else if(temp1.equals("Medium"))
                        {
                            myString = "01010101010101";
                            blinkDelay = 400;
                            Log.d("myTag", "Delay by 400");
                        }
                        else if(temp1.equals("Fast"))
                        {
                            myString = "010101010101010101010101010101010101010101";
                            blinkDelay = 100;
                            Log.d("myTag", "Delay by 150");
                        }
                        else if(temp1.equals("Random"))
                        {
                            myString = "01100001111000101000011100100111000101100000111100101011011100101011110000011101010000101110011000";
                            blinkDelay = 120;
                            Log.d("myTag", "Delay by 50");
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;


            }
        });

    }
}