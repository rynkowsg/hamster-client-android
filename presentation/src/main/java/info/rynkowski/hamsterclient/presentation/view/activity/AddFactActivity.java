package info.rynkowski.hamsterclient.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import info.rynkowski.hamsterclient.presentation.R;

public class AddFactActivity extends Activity {

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, AddFactActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_fact);
  }
}
