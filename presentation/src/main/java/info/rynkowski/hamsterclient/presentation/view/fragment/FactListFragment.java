package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;
import info.rynkowski.hamsterclient.presentation.view.activity.FactFormActivity;
import info.rynkowski.hamsterclient.presentation.view.adapter.FactsAdapter;
import info.rynkowski.hamsterclient.presentation.view.adapter.FactsLayoutManager;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.presenter.FactListPresenter;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import java.util.Collection;

/**
 * Fragment that shows a list of Facts.
 */
public class FactListFragment extends BaseFragment
    implements FactListView, FactsAdapter.OnItemClickListener {

  private String TAG = "FactListFragment";

  // TODO: Use DI!
  FactListPresenter factListPresenter = new FactListPresenter();

  @InjectView(R.id.rv_facts) RecyclerView rv_facts;

  private FactsLayoutManager factsLayoutManager;
  private FactsAdapter factsAdapter;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_fact_list, container, false);
    ButterKnife.inject(this, view);
    setupUI();
    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    factListPresenter.setView(this);
    factListPresenter.initialize();
  }

  @Override public void onResume() {
    super.onResume();
    factListPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    factListPresenter.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  private void setupUI() {
    this.factsLayoutManager = new FactsLayoutManager(getActivity());
    this.rv_facts.setLayoutManager(factsLayoutManager);
  }

  @OnClick(R.id.btn_add_fact) public void onAddFactClicked(View view) {
    navigator.navigateToFactFormForResult(FactListFragment.this, Navigator.REQUEST_CODE_PICK_FACT);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d(TAG, "onActivityResult(requestCode=" + requestCode + ", resultCode=" + resultCode + ")");
    switch (requestCode) {
      case (Navigator.REQUEST_CODE_PICK_FACT):
        if (resultCode == Activity.RESULT_OK) {
          FactModel fact = (FactModel) data.getSerializableExtra(FactFormActivity.EXTRAS_KEY_FACT);
          factListPresenter.addFact(fact);
          showToastMessage("New fact:" + fact.getActivity());
        } else {
          Log.e(TAG, "onActivityResult failed, requestCode = " + requestCode + ", resultCode = "
              + resultCode);
        }
        break;
      default:
        Log.w(TAG, "onActivityResult have got unknown response, requestCode = " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override public void onFactItemClicked(FactModel factModel) {
    if (FactListFragment.this.factListPresenter != null && factModel != null) {
      FactListFragment.this.factListPresenter.onFactClicked(factModel);
    }
  }

  @Override public void renderFactList(Collection<FactModel> factModelCollection) {
    if (factModelCollection != null) {
      if (this.factsAdapter == null) {
        this.factsAdapter = new FactsAdapter(getActivity(), factModelCollection);
      } else {
        this.factsAdapter.setFactsCollection(factModelCollection);
      }
      this.factsAdapter.setOnItemClickListener(FactListFragment.this);
      this.rv_facts.setAdapter(factsAdapter);
    }
  }

  @Override public void showLoading() {
  }

  @Override public void hideLoading() {
  }

  @Override public void showRetry() {
  }

  @Override public void hideRetry() {
  }

  @Override public void showError(String message) {
    this.showToastMessage(message);
  }

  @Override public Context getContext() {
    return this.getActivity().getApplicationContext();
  }
}
