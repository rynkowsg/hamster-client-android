package info.rynkowski.hamsterclient.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.internal.di.HasComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.components.DaggerFactListComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.components.FactListComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactListActivity extends BaseActivity implements HasComponent<FactListComponent> {

  private static final Logger log = LoggerFactory.getLogger(FactListActivity.class);

  private FactListComponent factListComponent;

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactListActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fact_list);
    this.initializeDependencyInjector();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      default:
        log.debug("Called onActivityResult(requestCode={}, resultCode={}) : unknown request code",
            requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
  }

  private void initializeDependencyInjector() {
    this.factListComponent = DaggerFactListComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .build();
  }

  @Override public FactListComponent getComponent() {
    return factListComponent;
  }
}
