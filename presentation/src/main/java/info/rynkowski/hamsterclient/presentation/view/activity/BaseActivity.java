package info.rynkowski.hamsterclient.presentation.view.activity;

import android.support.v7.app.AppCompatActivity;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;

public abstract class BaseActivity extends AppCompatActivity {
  protected Navigator navigator = new Navigator();
}
