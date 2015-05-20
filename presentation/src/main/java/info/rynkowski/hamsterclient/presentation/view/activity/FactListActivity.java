package info.rynkowski.hamsterclient.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.view.fragment.FactListFragment;

public class FactListActivity extends BaseActivity
    implements FactListFragment.OnAddFactClickedListener {

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactListActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fact_list);
    ButterKnife.inject(this);
  }

  @Override public void onAddFactClicked() {
    this.navigator.navigateToFactFormForResult(this);
  }
}
