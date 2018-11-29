package com.example.croyce.flickrfeed;

/*
 * Container class for all of the images from the Flickr Json feed
 */
class FlickrItem
{

    private final String _imgTitle;
    private final String _imgSource;

    /**
     * Constructor
     *
     * @param title - the title of the image
     * @param source - the source of the image
     */
    FlickrItem( final String title, final String source )
    {
        _imgTitle = title;
        _imgSource = source;
    }

    /**
     * Get the image title
     *
     * @return String - the image title
     */
    final String getTitle()
    {
        return _imgTitle;
    }

    /**
     * Get the image source
     *
     * @return String - the source of the image
     */
    final String getSource()
    {
        return _imgSource;
    }
}
