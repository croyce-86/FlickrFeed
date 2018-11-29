package com.example.croyce.flickrfeed;

/*
 *  The class to handle the display of the fragment containing the image title and source
 */

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FlickrImageFragment extends Fragment
{

    private String _imgFilename;
    private String _imgSource;

    private ImageView _image;

    /**
     * Create the view
     *
     * @param inflater - The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container - The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState -  If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return View - the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState )
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.view_flikr_img, container, false );

        TextView imgTitle = rootView.findViewById( R.id.txv_imgTitle );
        imgTitle.setText( _imgFilename );

        _image = rootView.findViewById( R.id.imgv_flickrImg );

        ImageDownloadHandler handler = new ImageDownloadHandler();
        Thread dlThread = new Thread( handler );
        dlThread.start();

        return rootView;
    }

    /**
     * Accessor method for fragment data comparison
     *
     * @return String - the image source currently displayed in this fragment
     */
    final String getImgSrc()
    {
        return _imgSource;
    }

    /**
     * Update the data in the fragment
     *
     * @param item - the item to be displayed in this fragment
     */
    void updateData( FlickrItem item )
    {
        _imgFilename = item.getTitle();
        _imgSource = item.getSource();
    }

    /**
     * Runnable for downloading images in the background when a fragment is created
     */
    private class ImageDownloadHandler implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                Bitmap img = Picasso.get().load( _imgSource ).get();
                Objects.requireNonNull( getActivity() ).runOnUiThread( () -> _image.setImageBitmap( img ) );
            }
            catch ( Exception e )
            {
                Log.e( "Error", e.toString() );
                e.printStackTrace();
            }
        }
    }
}
