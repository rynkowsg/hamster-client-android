package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import info.rynkowski.hamsterclient.presentation.R;

/**
 * Fragment that allows to provide new fact attributes.
 */
public class FactFormFragment extends Fragment {

  @InjectView(R.id.et_activity) EditText etActivity;
  @InjectView(R.id.et_category) EditText etCategory;
  @InjectView(R.id.et_tags) EditText etTags;
  @InjectView(R.id.et_start_time) EditText etStartTime;
  @InjectView(R.id.et_end_time) EditText etEndTime;
  @InjectView(R.id.cb_ongoing) CheckBox cbOngoing;
  @InjectView(R.id.et_description) EditText etDescription;
  @InjectView(R.id.btn_ok) Button btnOk;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_fact_form, container, false);
    ButterKnife.inject(this, view);
    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
  }
}
