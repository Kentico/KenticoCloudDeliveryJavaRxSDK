package kenticocloud.kenticoclouddancinggoat.app.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;

import kenticocloud.kenticoclouddancinggoat.R;
import kenticocloud.kenticoclouddancinggoat.app.articles.ArticlesActivity;
import kenticocloud.kenticoclouddancinggoat.app.cafes.CafesActivity;
import kenticocloud.kenticoclouddancinggoat.app.coffees.CoffeesActivity;
import kenticocloud.kenticoclouddancinggoat.util.NetworkHelper;
import kenticocloud.kenticoclouddancinggoat.util.SyncHelper;

/**
 * Created by RichardS on 16. 8. 2017.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Drawer layout
     */
    protected DrawerLayout _drawerLayout;

    /**
     * Network helper
     */
    protected NetworkHelper _networkHelper;

     /**
     * Implement to get activity specific layout
     * @return id of the layout
     */
    protected int getLayoutResourceId(){
        return -1;
    }

    /**
     * Gets resource to title
     * @return id of the title string
     */
    protected int getTitleResourceId(){
        return -1;
    }

    /**
     * Gets menu item id
     * @return if of the menu item
     */
    protected int getMenuItemId(){
        return -1;
    }

    /**
     * Indicates if back button is shown in navigation drawer instead of menu items
     */
    protected boolean useBackButton(){
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init android networking
        AndroidNetworking.initialize(getApplicationContext());

        // Init network helper
        this._networkHelper = NetworkHelper.getInstance();

        // Render proper layout for child activity
        setContentView(getLayoutResourceId());

        // Set title
        int titleResourceId = getTitleResourceId();
        if (titleResourceId >= 0){
            setTitle(this.getTitleResourceId());
        }
        else{
            // set empty title
            setTitle("");
        }

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            if (useBackButton()) {
                ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            } else {
                ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            }
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the navigation drawer.
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // Refresh connection button
        AppCompatButton refreshConnectionACB = (AppCompatButton)findViewById(R.id.refreshConnectionACB);
        refreshConnectionACB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check connection again
                if (_networkHelper.isNetworkAvailable(getApplicationContext())){
                    // reload activity
                    finish();
                    startActivity(getIntent());
                }
                else{
                    // network still not available
                    Snackbar.make(findViewById(android.R.id.content), R.string.error_network_not_available, Snackbar.LENGTH_LONG)
                        .show();
                }
            }
        });
    }

    protected void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.getItemId() == getMenuItemId()){
                            // do nothing, we are on this screen already
                        }
                        else{
                            switch (menuItem.getItemId()) {
                                case R.id.cafes:
                                    Intent cafesIntent = new Intent(navigationView.getContext(), CafesActivity.class);
                                    startActivity(cafesIntent);
                                    break;
                                case R.id.articles:
                                    Intent articlesIntent = new Intent(navigationView.getContext(), ArticlesActivity.class);
                                    startActivity(articlesIntent);
                                    break;
                                case R.id.coffees:
                                    Intent coffeesIntent = new Intent(navigationView.getContext(), CoffeesActivity.class);
                                    startActivity(coffeesIntent);
                                    break;
                            }
                        }

                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        _drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (useBackButton()){
            NavUtils.navigateUpFromSameTask(this);
            return false;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                _drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showConnectionNotAvailable(){
        LinearLayout noConnectionLL = (LinearLayout) findViewById(R.id.noConnectionLL);
        noConnectionLL.setVisibility(View.VISIBLE);
    }
}