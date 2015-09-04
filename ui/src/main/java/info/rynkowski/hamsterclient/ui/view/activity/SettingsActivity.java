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

package info.rynkowski.hamsterclient.ui.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import info.rynkowski.hamsterclient.ui.R;
import info.rynkowski.hamsterclient.ui.view.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    // enabling action bar app icon and behaving it as toggle button
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // Display the fragment as the main content.
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction().add(R.id.container, new SettingsFragment()).commit();
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // TODO: Probably it is not good idea. Check this out.
        super.onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
