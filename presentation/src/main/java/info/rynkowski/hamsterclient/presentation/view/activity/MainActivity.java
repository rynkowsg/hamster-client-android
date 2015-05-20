package info.rynkowski.hamsterclient.presentation.view.activity;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.R;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.btn_open_facts_list) void navigateToFactsList() {
    this.navigator.navigateToFactsList(this);
  }
}
