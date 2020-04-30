package com.allright.android_filterrecyclerviewusingsearchviewintoolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar, searchtollbar;
    Menu search_menu;
    private RecyclerView recyclerView;
    MenuItem item_search;
    ArrayList<Model> os_version = new ArrayList<>();
    ModelAdapter mAdapter;

// My Test Code Implements
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initControls();

    }

    private void initControls() {

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        setSearchToolbar();

        recyclerView = (RecyclerView) findViewById( R.id.recycler_view );
        os_version.add( new Model( "Alpha", "version 1" ) );
        os_version.add( new Model( "Beta", "version 1" ) );
        os_version.add( new Model( "Cup Cake", "version 1" ) );
        os_version.add( new Model( "Donut", "version 1.6" ) );
        os_version.add( new Model( "Eclair", "version 2.1" ) );
        os_version.add( new Model( "Froyo", "version 2.2" ) );
        os_version.add( new Model( "Ginger Bread", "version 2.3" ) );
        os_version.add( new Model( "Honycomb", "version 3.0" ) );
        os_version.add( new Model( "Icecream Sandwhich", "version 4.0" ) );
        os_version.add( new Model( "Jelly Bean", "version 4.1" ) );
        os_version.add( new Model( "Kitkat", "version 4.4" ) );
        os_version.add( new Model( "Lolly Pop", "version 5.0" ) );
        os_version.add( new Model( "Marsh Mallow", "version 6.0" ) );
        os_version.add( new Model( "Nougat", "version 7.0" ) );

        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        mAdapter = new ModelAdapter( os_version );
        recyclerView.setAdapter( mAdapter );

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate( R.menu.menu_home, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_status:
                Toast.makeText( this, "Home Status Click", Toast.LENGTH_SHORT ).show();
                return true;
            case R.id.action_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal( R.id.searchtoolbar, 1, true, true );
                else
                    searchtollbar.setVisibility( View.VISIBLE );

                item_search.expandActionView();
                return true;
            case R.id.action_settings:
                Toast.makeText( this, "Home Settings Click", Toast.LENGTH_SHORT ).show();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    public void setSearchToolbar() {

        searchtollbar = (Toolbar) findViewById( R.id.searchtoolbar );
        if (searchtollbar != null) {
            searchtollbar.inflateMenu( R.menu.menu_search );
            search_menu = searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal( R.id.searchtoolbar, 1, true, false );
                    else
                        searchtollbar.setVisibility( View.GONE );
                }
            } );

            item_search = search_menu.findItem( R.id.action_filter_search );

            MenuItemCompat.setOnActionExpandListener( item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal( R.id.searchtoolbar, 1, true, false );
                    } else
                        searchtollbar.setVisibility( View.GONE );
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            } );

            initSearchView();


        } else
            Log.d( "toolbar", "setSearchtollbar: NULL" );
    }

    public void initSearchView() {
        final SearchView searchView =
                (SearchView) search_menu.findItem( R.id.action_filter_search ).getActionView();

        // Enable/Disable Submit button in the keyboard

        searchView.setSubmitButtonEnabled( false );

        // Change search close button image

        ImageView closeButton = (ImageView) searchView.findViewById( R.id.search_close_btn );
        closeButton.setImageResource( R.drawable.ic_close );


        // set hint and the text colors

        EditText txtSearch = ((EditText) searchView.findViewById( androidx.appcompat.R.id.search_src_text ));
        txtSearch.setHint( "Search.." );
        txtSearch.setHintTextColor( Color.DKGRAY );
        txtSearch.setTextColor( getResources().getColor( R.color.colorPrimary ) );


        // set the cursor

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById( androidx.appcompat.R.id.search_src_text );
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField( "mCursorDrawableRes" );
            mCursorDrawableRes.setAccessible( true );
            mCursorDrawableRes.set( searchTextView, R.drawable.ic_search_cursor ); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final ArrayList<Model> filteredModelList = filter( os_version, query );
                mAdapter.setFilter( filteredModelList );
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ArrayList<Model> filteredModelList = filter( os_version, newText );
                mAdapter.setFilter( filteredModelList );
                return true;
            }

        } );

    }

    private ArrayList<Model> filter(ArrayList<Model> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<Model> filteredModelList = new ArrayList<>();

        for (Model model : models) {

            final String text = model.getName().toLowerCase();
            if (text.contains( search_txt )) {
                filteredModelList.add( model );
            }
        }
        return filteredModelList;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById( viewID );

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize( R.dimen.abc_action_button_min_width_material )) - (getResources().getDimensionPixelSize( R.dimen.abc_action_button_min_width_material ) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize( R.dimen.abc_action_button_min_width_overflow_material );

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal( myView, cx, cy, 0, (float) width );
        else
            anim = ViewAnimationUtils.createCircularReveal( myView, cx, cy, (float) width, 0 );

        anim.setDuration( (long) 220 );

        // make the view invisible when the animation is done
        anim.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd( animation );
                    myView.setVisibility( View.INVISIBLE );
                }
            }
        } );

        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility( View.VISIBLE );

        // start the animation
        anim.start();


    }
}
