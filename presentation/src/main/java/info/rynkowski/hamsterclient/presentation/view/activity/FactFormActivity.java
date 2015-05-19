package info.rynkowski.hamsterclient.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.view.fragment.FactFormFragment;

public class FactFormActivity extends Activity implements FactFormFragment.FactFormListener {

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactFormActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fact_form);
    ButterKnife.inject(this);
  }

  @Override public void onFactPrepared() {
  }
}
