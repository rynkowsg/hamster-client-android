package info.rynkowski.hamsterclient.presentation.view.activity;

import android.os.Bundle;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.R;

public class MainActivity extends BaseActivity {

  @InjectView(R.id.btn_open_fact_form) Button btnOpenFactForm;
  @InjectView(R.id.btn_open_facts_list) Button btnOpenFactsList;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.btn_open_fact_form)
  void navigateToFactForm() {
    this.navigator.navigateToFactForm(this);
  }

  @OnClick(R.id.btn_open_facts_list)
  void navigateToFactsList() {
    this.navigator.navigateToFactsList(this);
  }
}
