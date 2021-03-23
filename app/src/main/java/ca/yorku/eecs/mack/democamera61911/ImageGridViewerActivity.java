package ca.yorku.eecs.mack.democamera61911;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


/* This activity receives a bundle containing the name of a directory.  All the images
 * in the directory are retrieved and displayed in a grid view.
 *
 */


public class ImageGridViewerActivity extends Activity implements AdapterView.OnItemClickListener
{
    final static String MYDEBUG = "MYDEBUG"; // for Log.i messages

    final static String IMAGE_INDEX_KEY = DemoCamera61911Activity.IMAGE_INDEX_KEY;
    final static String DIRECTORY_KEY = DemoCamera61911Activity.DIRECTORY_KEY;
    final static String IMAGE_FILENAMES_KEY = DemoCamera61911Activity.IMAGE_FILENAMES_KEY;

    String[] imageFilenames;
    String directory;

//    DirectoryContentsActivity
    GridView gridView;
    ImageAdapter imageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagegrid);
        init();

    }

    private void init()
    {
        if (getActionBar() != null)
            getActionBar().hide();

        // data passed from the calling activity in startActivityForResult
        Bundle b = getIntent().getExtras();
        imageFilenames = b.getStringArray(IMAGE_FILENAMES_KEY);
        directory = b.getString(DIRECTORY_KEY);

        // get references to the GridView and TextView
        gridView = (GridView)findViewById(R.id.gridview);


        /*
         * Determine the display width and height. The column width is calculated so we have three
         * columns when the screen is in portrait mode. We'll keep the same column width in
         * landscape mode, but use as many columns as will fit. Including "-12" in the calculation
         * accommodates 3 pixels of space on the left and right and between each column.
         */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int columnWidth = dm.widthPixels < dm.heightPixels ? dm.widthPixels / 3 - 12
                : dm.heightPixels / 3 - 12;

        gridView.setColumnWidth(columnWidth);

        imageAdapter = new ImageAdapter(imageFilenames,directory,columnWidth);

        // give the ImageAdapter to the GridView (and load the images)
        gridView.setAdapter(imageAdapter);

        // attach a click listener to the GridView (to respond to finger taps)
        gridView.setOnItemClickListener(this);
    }

	/*
     * If the user taps on an image in the GridView, create an Intent to launch a new activity to
	 * view the image in an ImageView. The image will respond to touch events (e.g., flings), so
	 * we'll bundle up the filenames array and the directory and pass the bundle to the activity.
	 */

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        final Bundle b = new Bundle();
        b.putStringArray("imageFilenames", imageFilenames);
        b.putString("directory", directory);
        b.putInt("position", position);

        // start image viewer activity
        Intent i = new Intent(getApplicationContext(), ImageViewerActivity.class);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void onBackPressed()
    {
        this.setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

}
