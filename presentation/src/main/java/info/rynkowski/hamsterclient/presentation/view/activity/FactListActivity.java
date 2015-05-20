package info.rynkowski.hamsterclient.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import butterknife.ButterKnife;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.view.fragment.FactListFragment;

public class FactListActivity extends BaseActivity
    implements FactListFragment.OnAddFactClickedListener {

  public static final int REQUEST_CODE_PICK_FACT = 1;

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactListActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fact_list);
    ButterKnife.inject(this);
  }

  @Override public void onAddFactClicked() {
    this.navigator.navigateToFactFormForResult(this, REQUEST_CODE_PICK_FACT);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case (REQUEST_CODE_PICK_FACT):
        if (resultCode == RESULT_OK) {
          FactModel fact = (FactModel) data.getSerializableExtra("fact");
          Toast.makeText(this, "New fact data received:" + fact.getActivity(), Toast.LENGTH_LONG).show();
        }
        break;
      default:
        super.onActivityResult(requestCode, resultCode, data);
    }
  }
}
