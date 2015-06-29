/*
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.internal.di.HasComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.components.DaggerFactListComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.components.FactListComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactListActivity extends BaseActivity implements HasComponent<FactListComponent> {

  private static final Logger log = LoggerFactory.getLogger(FactListActivity.class);

  @InjectView(R.id.toolbar) Toolbar toolbar;

  private FactListComponent factListComponent;

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactListActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    log.debug("onCreate()");
    setContentView(R.layout.activity_fact_list);
    ButterKnife.inject(this);
    setSupportActionBar(toolbar);

    this.initializeDependencyInjector();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_fact_list, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        navigator.navigateToSettings(this);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
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

  @Override protected void onDestroy() {
    super.onDestroy();
    log.debug("onDestroy()");
  }

  @Override protected void onPause() {
    super.onPause();
    log.debug("onPause()");
  }

  @Override protected void onResume() {
    super.onResume();
    log.debug("onResume()");
  }
}
