package info.rynkowski.hamsterclient.presentation.view.activity;

import android.app.Activity;
import android.os.Bundle;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;

public abstract class BaseActivity extends Activity {
  protected Navigator navigator = new Navigator();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
}
