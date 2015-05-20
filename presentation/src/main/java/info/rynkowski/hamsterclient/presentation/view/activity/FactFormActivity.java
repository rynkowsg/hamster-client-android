package info.rynkowski.hamsterclient.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import info.rynkowski.hamsterclient.presentation.R;

public class FactFormActivity extends BaseActivity {

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactFormActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fact_form);
  }
}
