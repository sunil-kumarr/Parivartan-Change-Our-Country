package com.net.comy;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int MY_LOCATION = 143;
    private static final int REQUEST_CHECK_SETTINGS = 123;
    private ComBookAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth mFirebaseAuth;
    private FloatingActionButton mRegisterComplaint,mAdminPing;
    private FirebaseUser mCurrentUser;
    private Toolbar mToolbar;
    private LocationCallback locationCallback;
    private Geocoder mGeocoder;
    private EditText mSearchView;
    private boolean isAdmin;
    private Location mCurrentLocation;
    private LocationRequest locationRequest;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseDatabase mFirebaseDatabase;
    private int TabIcons[] = {R.drawable.ic_assignment, R.drawable.ic_pending, R.drawable.ic_completed};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mGeocoder = new Geocoder(this, Locale.getDefault());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mToolbar = findViewById(R.id.toolbar_location);
        setSupportActionBar(mToolbar);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));

            }
        });
        mAdminPing = findViewById(R.id.chat_ping_messages_btn);
        mRegisterComplaint = findViewById(R.id.register_com_button);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ComBookAdapter(getSupportFragmentManager(), MainActivity.this);
        adapter.addFragment(new FragmentComplainOpen(), "Open", TabIcons[0]);
//        adapter.addFragment(new FragmentComplainInProgress(), "Pending",TabIcons[1]);
        adapter.addFragment(new FragmentComplainClosed(), "Closed", TabIcons[2]);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        findViewById(R.id.chat_Support_ping_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                startActivity(new Intent(MainActivity.this,ChatPingActivity.class));
            }
        });
        mAdminPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                startActivity(new Intent(MainActivity.this,ChatAdminListActivity.class));
            }
        });
        mRegisterComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                Intent intent = new Intent(MainActivity.this, RegisterComplaint.class);
                startActivity(intent);
            }
        });
        if (checkLocationPermission()) {
            createLocationRequest();
        }
        setAdminControls();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                //Toast.makeText(MainActivity.this, "hellllo", Toast.LENGTH_SHORT).show();
                for (Location location : locationResult.getLocations()) {
                    try {
                        showLocation(location);
                    } catch (IOException pE) {
                        pE.printStackTrace();
                    }
                }
            }
        };
    }

    private void setAdminControls() {

        SharedPreferences preferences = getSharedPreferences("adminCredentials",MODE_PRIVATE);
        isAdmin = preferences.getBoolean("isAdmin",false);
        if(isAdmin){
            mRegisterComplaint.setVisibility(View.GONE);
            mAdminPing.setVisibility(View.VISIBLE);
            mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_person_24px));
        }
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Grant Location Permission")
                        .setMessage("loaction permission is required to get the complaint location.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(100000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    findCurrentLocation();
                }
            } else {
                mToolbar.setTitle("No Location Permission");
            }
        }
    }

    private void findCurrentLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> pTask) {
                        if (pTask.isSuccessful()) {
                            mCurrentLocation = pTask.getResult();
                        } else {
                            mToolbar.setTitle("No Location Found!");
                        }
                    }
                });
    }

    public void signOut(View pView) {
        mFirebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCurrentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        ComplaintInfo complaintInfo = new ComplaintInfo();
        complaintInfo.setComplainID("sadaskd");
        mFirebaseDatabase.getReference("users").child(mCurrentUser.getUid())
                .child("asdfghjkl").setValue(complaintInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_logout == item.getItemId()) {
            mFirebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLocation(Location pLocation) throws IOException {
//        Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
        List<Address> addressList = mGeocoder.getFromLocation(pLocation.getLatitude(),
                pLocation.getLongitude(), 1);
        if (addressList.size() > 0) {
            Address address = addressList.get(0);
            mToolbar.setTitle(address.getAddressLine(0));
        }
    }

    @Override
    public void onLocationChanged(Location pLocation) {
//        Toast.makeText(this, "called...", Toast.LENGTH_SHORT).show();
        try {
            mCurrentLocation = pLocation;
            showLocation(pLocation);
        } catch (IOException pE) {
            pE.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String pS, int pI, Bundle pBundle) {

    }

    @Override
    public void onProviderEnabled(String pS) {

    }

    @Override
    public void onProviderDisabled(String pS) {

    }
}
