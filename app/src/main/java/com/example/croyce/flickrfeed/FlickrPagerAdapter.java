package com.example.croyce.flickrfeed;

/*
 * This class defines the adapter to be used in the ViewPager
 */

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class FlickrPagerAdapter extends FragmentStatePagerAdapter implements com.viewpagerindicator.IconPagerAdapter
{
    private FlickrData _adapterData;

    /**
     * Constructor
     *
     * @param fm - the FragmentManager associated with the fragment
     */
    FlickrPagerAdapter( FragmentManager fm )
    {
        super( fm );
    }

    /**
     * Update the data set to be used in the adaptor
     *
     * @param data - the new data set to use
     */
    void updateData( FlickrData data )
    {
        _adapterData = data;

        notifyDataSetChanged();
    }

    /**
     * Find the position of the given fragment in the data set
     *
     * @param FragmentToFind - the fragment to find from the data set
     * @return int - the position of the fragment if found, otherwise POSITION_NONE
     */
    @Override
    public int getItemPosition( @NonNull Object FragmentToFind )
    {
        int retIndex = POSITION_NONE;
        if ( _adapterData != null )
        {
            FlickrImageFragment selectedFragment = (FlickrImageFragment) FragmentToFind;
            String imgSrc = selectedFragment.getImgSrc();
            final int itemPos = _adapterData.findIndex( imgSrc );
            if ( itemPos >= 0 )
            {
                retIndex = itemPos;
            }
        }
        return retIndex;
    }

    /**
     * Get the fragment at the given index
     *
     * @param index - the index to get the fragment of
     * @return Fragment -
     */
    @Override
    public Fragment getItem( int index )
    {
        FlickrImageFragment newFragment = new FlickrImageFragment();
        if ( _adapterData != null )
        {
            newFragment.updateData( _adapterData.getItem( index ) );
        }
        return newFragment;
    }

    /**
     * This function is used for other Indicator types, since we are only using lines
     * returning 0 is fine.
     *
     * @param index N/A
     * @return int - zero
     */
    @Override
    public int getIconResId( int index )
    {
        return 0;
    }

    /**
     * Get the current count of items from the data set
     *
     * @return int - the number of items from the data set
     */
    @Override
    public int getCount()
    {
        return ( _adapterData != null ) ? _adapterData.getDataSize() : 0;
    }

}
