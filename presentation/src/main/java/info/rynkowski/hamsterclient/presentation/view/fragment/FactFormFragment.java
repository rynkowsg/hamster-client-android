package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Fragment that allows to provide new fact attributes.
 */
public class FactFormFragment extends BaseFragment {

  @InjectView(R.id.et_activity) EditText activity;
  @InjectView(R.id.et_category) EditText category;
  @InjectView(R.id.et_tags) EditText tags;
  @InjectView(R.id.et_start_time) EditText startTime;
  @InjectView(R.id.et_end_time) EditText endTime;
  @InjectView(R.id.cb_ongoing) CheckBox isOngoingCheckbox;
  @InjectView(R.id.et_description) EditText description;
  @InjectView(R.id.btn_ok) Button okButton;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Log.i("FactForm", "onCreateView()");
    View view = inflater.inflate(R.layout.fragment_fact_form, container, false);
    ButterKnife.inject(this, view);
    // TODO: Rewrite below lines.
    startTime.setEnabled(true);
    startTime.setClickable(true);
    startTime.setFocusable(false);
    startTime.setText(04 + ":" + 02);
    endTime.setEnabled(true);
    endTime.setClickable(true);
    endTime.setFocusable(false);
    endTime.setText(04 + ":" + 02);
    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
  }

  @OnClick(R.id.cb_ongoing) public void onCheckBoxClicked(View view) {
    boolean isChecked = ((CheckBox) view).isChecked();
    endTime.setEnabled(!isChecked);
  }

  @OnClick({ R.id.et_start_time, R.id.et_end_time })
  public void onTimeClicked(final View view) {
    Calendar currentTime = Calendar.getInstance();
    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
    int minute = currentTime.get(Calendar.MINUTE);

    TimePickerDialog timePicker = new TimePickerDialog(getActivity(),
        (timePicker1, selectedHour, selectedMinutes) -> {
          switch (view.getId()) {
            case R.id.et_start_time:
              startTime.setText(selectedHour + ":" + selectedMinutes);
              break;
            case R.id.et_end_time:
              endTime.setText(selectedHour + ":" + selectedMinutes);
              break;
          }
        }, hour, minute, true);

    timePicker.setTitle("Select Time");
    timePicker.show();
  }

  @OnClick(R.id.btn_ok)
  public void onSubmitButtonClicked(View view) {
    // TODO: Rewrite below lines.
    Intent intent = getActivity().getIntent();
    intent.putExtra("fact", readFact());
    getActivity().setResult(Activity.RESULT_OK, intent);
    getActivity().finish();
  }

  private FactModel readFact() {
    FactModel factModel = new FactModel.Builder().activity(activity.getText().toString())
        .category(category.getText().toString())
        .tags(getTags(tags.getText().toString()))
        .description(description.getText().toString())
        .build();
    return factModel;
  }

  private List<String> getTags(String tags) {
    List<String> result = new ArrayList<>();
    for (String i : StringUtils.split(tags, ","))
      result.add(StringUtils.trim(i));
    return result;
  }
}
