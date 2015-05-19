package info.rynkowski.hamsterclient.presentation.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.R;

public class MainActivity extends BaseActivity {

  @InjectView(R.id.btn_AddFact) Button btnAddFact;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.btn_AddFact)
  void navigateToAddFact() {
    this.navigator.navigateToAddFact(this);
  }
}
