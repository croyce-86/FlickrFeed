package com.example.croyce.flickrfeed;

/*
 * Container class for all of the data that was read from the FlickrJson stream
 */
import java.util.ArrayList;

class FlickrData
{

    private final String _feedTitle;
    private final String _feedTimestamp;

    private final ArrayList<FlickrItem> _imgData;

    /**
     * Constructor
     *
     * @param title - the title of the flickr data stream
     * @param timestamp - the last updated timestamp from the stream
     */
    FlickrData( final String title, final String timestamp )
    {
        _feedTimestamp = timestamp;
        _feedTitle = title;

        _imgData = new ArrayList<>( 0 );
    }

    /**
     * Find the index of the item with the given source
     * This could prove to be problematic if the feed gives multiple items with the same source
     *
     * @param imgSrc - the image source to search for in the data set
     * @return int - the index of the image if found, otherwise -1
     */
    final int findIndex( final String imgSrc )
    {
        int itemIndex = -1;
        for ( int x = 0; x < _imgData.size(); x++ )
        {
            if ( _imgData.get( x ).getSource().equals( imgSrc ) )
            {
                itemIndex = x;
            }
        }
        return itemIndex;
    }

    /**
     * Get the timestamp of the data set
     *
     * @return String - the timestamp of the data set
     */
    final String getTimestamp()
    {
        return _feedTimestamp;
    }

    /**
     * Get the title of the data set
     *
     * @return String - the title of the data set
     */
    final String getTitle()
    {
        return _feedTitle;
    }

    /**
     * Get the size of the data set
     *
     * @return int - the size of the data set
     */
    int getDataSize()
    {
        return _imgData.size();
    }

    /**
     * Get the item at the given index from the data set
     * For now, we will assume it's used properly and will be between 0 & data size
     *
     * @param index - the index to be retrieved
     * @return FlickrItem - the item at the given index
     */
    FlickrItem getItem( final int index )
    {
        return _imgData.get( index );
    }

    /**
     * Add an item to the data set
     *
     * @param newImgData - the item to be added
     */
    boolean addItem( final FlickrItem newImgData )
    {
        return _imgData.add( newImgData );
    }
}
