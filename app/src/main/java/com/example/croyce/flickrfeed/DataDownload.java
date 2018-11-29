package com.example.croyce.flickrfeed;

/*
 * This class defines the process of downloading and extracting the
 * data from Flickr's JSON feed: https://api.flickr.com/services/feeds/photos_public.gne?format=json
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

class DataDownload extends AsyncTask<DataDownload.DataHandler, Void, FlickrData>
{
    private static final String TAG = "DataDL";
    private static final String FlickrJsonFeed = "https://api.flickr.com/services/feeds/photos_public.gne?format=json";

    private DataHandler _handler;

    /**
     * Background task for loading in the data from the Flickr Json feed
     *
     * @param handler - the callback method for handling the data that has been downloaded
     * @return FlickrData - the data that was downloaded
     */
    @Override
    protected FlickrData doInBackground( DataHandler... handler )
    {
        _handler = handler[0];
        final String dataRead = readDataFromUrl();
        if ( dataRead.equals( "" ) )
        {
            return null;
        }
        final String processedData = processData( dataRead );
        return parseData( processedData );
    }

    /**
     * Once the downloading of data has been completed, use the provided callback method
     * to update the data in the main activity
     *
     * @param result FlickrData - the downloaded data
     */
    @Override
    protected void onPostExecute( final FlickrData result )
    {
        _handler.handleReadData( result );
    }

    /**
     * Read the data from the Flickr JSON feed
     *
     * @return String - the data read from the Flickr stream
     */
    private String readDataFromUrl()
    {
        StringBuilder sb = new StringBuilder();
        try (
                InputStream in = new URL( FlickrJsonFeed ).openStream();
                InputStreamReader isr = new InputStreamReader( in );
                BufferedReader br = new BufferedReader( isr )
        )
        {
            String buffer;
            while ( ( buffer = br.readLine() ) != null )
                sb.append( buffer );
        }
        catch ( MalformedURLException e )
        {
            Log.e( TAG, "Bad URL Provided { " + FlickrJsonFeed + '}', e );
        }
        catch ( IOException e )
        {
            Log.e( TAG, "Error reading from stream" );
        }

        return sb.toString();
    }

    /**
     * The data read from the page has extraneous data we don't care about, so we need to strip it
     *
     * @param data - the data read from the stream
     * @return String - processed data with the extra parts removed
     */
    private String processData( String data )
    {
        data = data.replace( "jsonFlickrFeed(", "" );
        data = data.substring( 0, data.length() - 1 );
        return data;
    }

    /**
     * Parse out all of the JSON data to fill in the
     *
     * @param JsonData - the JSON data as a String
     * @return FlickrData - the JSON data parsed and put into a container
     */
    private FlickrData parseData( final String JsonData )
    {
        FlickrData data = null;
        try
        {
            final JSONObject jsonOb = new JSONObject( JsonData );

            final String timestamp = jsonOb.getString( "modified" );
            final String title = jsonOb.getString( "title" );
            data = new FlickrData( title, timestamp );

            JSONArray imgData = jsonOb.getJSONArray( "items" );
            final int ArrSize = imgData.length();
            for ( int i = 0; i < ArrSize; i++ )
            {
                JSONObject img = imgData.getJSONObject( i );

                final FlickrItem ItemToAdd = parseItem( img );
                final boolean ItemAdded = data.addItem( ItemToAdd );
                if ( ItemAdded == false )
                {
                    Log.e( TAG, String.format( "Failed to add item %s to data set", ItemToAdd.getSource() ) );
                }
            }
        }
        catch ( JSONException e )
        {
            Log.e( "FlickrErr", "Failed to parse JSON data.", e );
        }

        return data;
    }

    /**
     * Parse the individual image data from each JSON object
     *
     * @param imgData the JSONObject containing
     * @return FlickItem - the data parsed from
     * @throws JSONException when the given JSONObject does nor contain the expected data
     */
    private FlickrItem parseItem( final JSONObject imgData ) throws JSONException
    {
        final String imgTitle = imgData.getString( "title" );

        JSONObject imgSrcJson = imgData.getJSONObject( "media" );
        final String imgSrc = imgSrcJson.getString( "m" );

        // The image source with the _m is a preview, strip it out to get the full image
        final String modImgSrc = imgSrc.replace( "_m.jpg", ".jpg" );

        return new FlickrItem( imgTitle, modImgSrc );
    }

    /**
     * Define the callback interface for handling the data once it has all been loaded
     */
    interface DataHandler
    {
        void handleReadData( FlickrData data );
    }
}
