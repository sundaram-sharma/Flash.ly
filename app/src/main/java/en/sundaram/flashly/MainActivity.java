package en.sundaram.flashly;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {


    ImageButton flashLightMainButton, flashLightState, flashLightSosButton;
    boolean state;

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
            public void onClick(View view)
            {

                String myString = "0101010101";
                long blinkDelay = 500; //Delay the blink in ms
                for (int i = 0; i < myString.length(); i++)
                {
                    if (myString.charAt(i) == '0') {
                        flashLightState.setBackgroundResource(R.drawable.torch_on);
                        try {
                            String CameraId = cameraManager.getCameraIdList()[0]; //0 back 1 front
                            cameraManager.setTorchMode(CameraId, true);
                            state = true;

                        }
                        catch (CameraAccessException e) {
                            e.printStackTrace();
                        }


                    } else {
                        flashLightState.setBackgroundResource(R.drawable.torch_off);
                        try {
                            String CameraId = cameraManager.getCameraIdList()[0]; //0 back 1 front
                            cameraManager.setTorchMode(CameraId, false);
                            state = false;


                        }
                        catch (CameraAccessException e) {
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
        });


    }
}