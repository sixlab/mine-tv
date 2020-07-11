package tech.minesoft.minetv.page.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import tech.minesoft.minetv.R;

public class MineSearchActivity extends Activity {

    private static final String TAG = MineSearchActivity.class.getSimpleName();
    // private MineSearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //
        // mSearchFragment = (MineSearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
    }

    @Override
    public boolean onSearchRequested() {
        //if (mSearchFragment.hasResults()) {
        startActivity(new Intent(this, MineSearchActivity.class));
        //} else {
        //mSearchFragment.startRecognition();
        //}
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}