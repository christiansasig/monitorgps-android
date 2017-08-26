package com.roussun.android.apps.monitoreogps;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.roussun.android.apps.monitoreogps.fragment.DeviceFragmentDialog;
import com.roussun.android.apps.monitoreogps.fragment.HistorialPositionFragment;
import com.roussun.android.apps.monitoreogps.fragment.PositionFragment;
import com.roussun.android.apps.monitoreogps.fragment.SearchDeviceFragment;
import com.roussun.android.apps.monitoreogps.model.entity.Device;
import com.roussun.android.apps.monitoreogps.model.entity.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DeviceFragmentDialog.ConfirmDeviceFragmentDialogListener {
    private int positionActivity;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mTitles;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extra = this.getIntent().getExtras();

        //this.user = (User) extra.getSerializable("user");
        this.user = ((MyApplication) this.getApplication()).getUser();
        if(this.user.getDevices().size() == 0)
        {
            getAlert("Aviso", "No tiene dispositivos asignados a su usuario");
        }

        mTitle = mDrawerTitle = getTitle();
        mTitles = getResources().getStringArray(R.array.opcions_array);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            ;
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            setTitle(mTitles[0]);
            navigationView.setCheckedItem(R.id.nav_location);
            Fragment fragment = new PositionFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment, "fragment_position")
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        MenuInflater inflater = getMenuInflater();

        switch (positionActivity) {
            case R.id.nav_location:
                menu.clear();
                inflater.inflate(R.menu.main, menu);
                break;
            case R.id.nav_search_device:
                menu.clear();
                inflater.inflate(R.menu.search_device, menu);
                break;
            case R.id.nav_historial_position:
                menu.clear();
                //inflater.inflate(R.menu.report, menu);
                break;

            case R.id.nav_exit:
                //menu.clear();
                //inflater.inflate(R.menu.report, menu);
                break;

            default:
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh_position) {
            PositionFragment fragment = (PositionFragment)fm.findFragmentByTag("fragment_position");
            fragment.refreshPostionsByDevice();
            return true;
        }

        if (id == R.id.action_refresh_search) {
            SearchDeviceFragment fragment = (SearchDeviceFragment)fm.findFragmentByTag("fragment_search_device");
            fragment.refreshPostionsCurrentDevice();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        positionActivity = id;
        FragmentManager fm = getSupportFragmentManager();

        if (id == R.id.nav_location) {
            setTitle(mTitles[0]);
            /*Fragment fragment = new PositionFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment, "fragment_position")
                    .commit();*/

            Fragment fragmentSearch = fm.findFragmentByTag("fragment_search_device");
            if(fragmentSearch != null)
            {
                if(fragmentSearch.isVisible())
                {
                    fm.beginTransaction().hide(fragmentSearch).commit();
                }
            }

            Fragment fragmentHistorial = fm.findFragmentByTag("fragment_historial_position");
            if(fragmentHistorial != null)
            {
                if(fragmentHistorial.isVisible())
                {
                    fm.beginTransaction().hide(fragmentHistorial).commit();
                }
            }

            Fragment fragment = fm.findFragmentByTag("fragment_position");
            if(fragment != null)
            {
                fm.beginTransaction().show(fragment).commit();
            }

        } else if (id == R.id.nav_search_device) {
            setTitle(mTitles[2]);
            DeviceFragmentDialog dialogFragment = new DeviceFragmentDialog();
            dialogFragment.show(fm, "fragment_devices");

            Fragment fragmentPosition = fm.findFragmentByTag("fragment_position");
            if(fragmentPosition.isVisible())
            {
                fm.beginTransaction().hide(fragmentPosition).commit();
            }

            Fragment fragmentHistorial = fm.findFragmentByTag("fragment_historial_position");
            if(fragmentHistorial != null)
            {
                if(fragmentHistorial.isVisible())
                {
                    fm.beginTransaction().hide(fragmentHistorial).commit();
                }
            }

            Fragment fragment = fm.findFragmentByTag("fragment_search_device");
            if(fragment == null)
            {
                fragment = new SearchDeviceFragment();
                fm.beginTransaction()
                        .add(R.id.content_frame, fragment, "fragment_search_device")
                        .commit();
            }
            else
            {
                fm.beginTransaction().show(fragment).commit();
            }


        } else if (id == R.id.nav_historial_position) {
            setTitle(mTitles[1]);
            Fragment fragmentPosition = fm.findFragmentByTag("fragment_position");
            if(fragmentPosition.isVisible())
            {
                fm.beginTransaction().hide(fragmentPosition).commit();
            }

            Fragment fragmentSearch = fm.findFragmentByTag("fragment_search_device");
            if(fragmentSearch != null)
            {
                if(fragmentSearch.isVisible())
                {
                    fm.beginTransaction().hide(fragmentSearch).commit();
                }
            }

            Fragment fragment = fm.findFragmentByTag("fragment_historial_position");
            if(fragment == null)
            {
                fragment = new HistorialPositionFragment();
                fm.beginTransaction()
                        .add(R.id.content_frame, fragment, "fragment_historial_position")
                        .commit();
            }
            else
            {
                fm.beginTransaction().show(fragment).commit();
            }

        }
        else if (id == R.id.nav_exit) {
            getExitAlert();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onFinishDialogStatus(Device device) {
        FragmentManager fm = getSupportFragmentManager();
        SearchDeviceFragment fragment = (SearchDeviceFragment)fm.findFragmentByTag("fragment_search_device");
        fragment.refreshPostionsByDevice(device);
    }
    /**
     * Metodo para indicar alertas
     *
     * @param title
     * @param message
     */
    private void getAlert(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message).setTitle(title);

        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getExitAlert();

        }
        return false;
    }
    private void getExitAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Esta seguro que desea salir?")
                .setTitle("Confirmación");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
