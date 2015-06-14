package info.rynkowski.hamsterclient.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import butterknife.ButterKnife;
import info.rynkowski.hamsterclient.presentation.R;

public class FactListActivity extends BaseActivity {

  private static final String TAG = "FactListActvity";

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, FactListActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fact_list);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d(TAG, "onActivityResult(requestCode=" + requestCode + ", resultCode=" + resultCode + ")");
    switch (requestCode) {
      default:
        Log.w(TAG, "onActivityResult have got unknown response, requestCode = " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
  }
}
