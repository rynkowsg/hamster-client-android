package info.rynkowski.hamsterclient.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import info.rynkowski.hamsterclient.presentation.R;

public class FactsListActivity extends Activity {

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactsListActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_facts_list);
    ButterKnife.inject(this);
  }
}
