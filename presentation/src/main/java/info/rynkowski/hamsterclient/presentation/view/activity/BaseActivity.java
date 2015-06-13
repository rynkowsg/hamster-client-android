package info.rynkowski.hamsterclient.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import info.rynkowski.hamsterclient.presentation.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.components.DaggerApplicationComponent;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;
import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

  @Inject Navigator navigator;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ApplicationComponent applicationComponent = DaggerApplicationComponent.builder().build();
    applicationComponent.inject(this);
  }
}
