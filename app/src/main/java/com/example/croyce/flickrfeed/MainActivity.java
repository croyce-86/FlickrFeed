package com.example.croyce.flickrfeed;

/*
 * Main Activity class holding the view data
 */

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends AppCompatActivity
{
    private FlickrData _processedData;

    private TextView _txvMainTitle;
    private TextView _txvSubTitle;
    private SwipeRefreshLayout _mainLayout;

    private FlickrPagerAdapter _pgrAdapter;
    private ViewPager _vPager;
    private LinePageIndicator _indicator;

    /**
     * Create the view and get handles to the view components
     *
     * @param savedInstanceState - previous saved app state
     */
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar tlbrMain = findViewById( R.id.toolbar );
        setSupportActionBar( tlbrMain );

        _txvMainTitle = findViewById( R.id.txv_title );
        _txvSubTitle = findViewById( R.id.txv_lastMod );
        _vPager = findViewById( R.id.vpgr_imagePager );
        _indicator = findViewById( R.id.indicator );
        _mainLayout = findViewById( R.id.main );

        _pgrAdapter = new FlickrPagerAdapter( getSupportFragmentManager() );
        _vPager.setAdapter( _pgrAdapter );

        final float density = getResources().getDisplayMetrics().density;
        _indicator.setStrokeWidth( 3 * density );
        _indicator.setViewPager( _vPager );

        _mainLayout.setOnRefreshListener(
                this::refreshData
        );

        refreshData();
    }

    /**
     * Create the options menu to allow for refreshing with a button press instead of swipe
     *
     * @param menu - the Menu item to use as the displayed menu
     * @return boolean - true to display the menu
     */
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    /**
     * Handle the selection of an item in the menu
     *
     * @param item - The menu item that was selected.
     * @return boolean - True if the menu item was handled, otherwise pass to super
     */
    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        int id = item.getItemId();

        if ( id == R.id.menu_refresh )
        {
            refreshData();
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    /**
     * Start the background image download task to get the latest data from the flickr feed.
     */
    private void refreshData()
    {
        _mainLayout.setRefreshing( true );
        _vPager.setCurrentItem( 0 );
        final DataDownload dl = new DataDownload();
        dl.execute( new JsonHandler() );
    }

    /**
     * Update the data in the view to reflect the latest data received from the feed.
     */
    private void updateViewData()
    {
        _mainLayout.setRefreshing( false );
        _txvMainTitle.setText( _processedData.getTitle() );
        _txvSubTitle.setText( _processedData.getTimestamp() );

        // Calculate the width of each line
        // the set line width does not include spacing between lines
        // increase the size the 25% to account for spacing between lines
        final float widthPixels = getResources().getDisplayMetrics().widthPixels;
        final float lineWidth = (float) ( widthPixels / ( _processedData.getDataSize() + ( _processedData.getDataSize() * .25 ) ) );
        _indicator.setLineWidth( lineWidth );
        _indicator.postInvalidate();

    }

    /**
     * Class for interfacing with the download task
     */
    private class JsonHandler implements DataDownload.DataHandler
    {
        /**
         * Update the displayed data, notify the user if the data was unable to be read
         *
         * @param data - the latest data for the data set
         */
        @Override
        public void handleReadData( FlickrData data )
        {
            _processedData = data;
            _pgrAdapter.updateData( data );

            _vPager.invalidate();
            _pgrAdapter.notifyDataSetChanged();
            _indicator.notifyDataSetChanged();

            if ( data != null )
            {
                runOnUiThread( MainActivity.this::updateViewData );
            }
            else
            {
                _txvMainTitle.setText( getString(R.string.noData) );
                _txvSubTitle.setText( "" );
                Toast.makeText( getApplicationContext(), "Failed to read data", Toast.LENGTH_LONG ).show();
                _mainLayout.setRefreshing( false );

            }
        }
    }
}
