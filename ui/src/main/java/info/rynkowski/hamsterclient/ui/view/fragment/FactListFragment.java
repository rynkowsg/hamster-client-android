/*
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.ui.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.model.PresentationFact;
import info.rynkowski.hamsterclient.presentation.presenter.FactListPresenter;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import info.rynkowski.hamsterclient.ui.R;
import info.rynkowski.hamsterclient.ui.model.UiFact;
import info.rynkowski.hamsterclient.ui.model.mapper.UiFactMapper;
import info.rynkowski.hamsterclient.ui.view.activity.FactFormActivity;
import info.rynkowski.hamsterclient.ui.view.adapter.FactsAdapter;
import info.rynkowski.hamsterclient.ui.view.adapter.FactsLayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * Fragment that shows a list of Facts.
 */
@Slf4j
public class FactListFragment extends BaseFragment implements FactListView {

  //TODO: move those consts to FactFormActivity according to article:
  //      http://bottega.com.pl/pdf/materialy/android/ThereIsNoApp.pdf, paragraph: Porządek w kodzie
  private final static int REQUEST_CODE_ADD_FACT = 0;
  private final static int REQUEST_CODE_EDIT_FACT = 1;

  @Inject UiFactMapper mapper;

  @Inject FactListPresenter factListPresenter;

  @Bind(R.id.rv_facts) RecyclerView rv_facts;
  @Bind(R.id.progress_circular) ProgressBar progress_circular;

  private FactsAdapter factsAdapter;

  private AlertDialog retryDialog;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    log.debug("onCreateView()");
    View view = inflater.inflate(R.layout.fragment_fact_list, container, false);
    ButterKnife.bind(this, view);

    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    log.debug("onActivityCreated()");
    injectDependencies();

    // TODO: Inject FactsLayoutManager object (ActivityScope)
    FactsLayoutManager factsLayoutManager = new FactsLayoutManager(getActivity());
    rv_facts.setLayoutManager(factsLayoutManager);

    // TODO: Inject FactsAdapter object (ActivityScope)
    factsAdapter = new FactsAdapter(getActivity(), new ArrayList<>());
    factsAdapter.setOnFactOperationsListener(factListPresenter);
    rv_facts.setAdapter(factsAdapter);
  }

  @Override public void onStart() {
    super.onStart();
    log.debug("onStart()");
    factListPresenter.setView(this);
    factListPresenter.start();
  }

  @Override public void onResume() {
    super.onResume();
    log.debug("onResume()");
    this.setupRetryDialog();
    factListPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    log.debug("onPause()");
    factListPresenter.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    log.debug("onDestroyView()");
    ButterKnife.unbind(this);

    factListPresenter.setView(null);
    factListPresenter.destroy();
  }

  private void injectDependencies() {
    this.getApplicationComponent().inject(this);
  }

  @OnClick(R.id.fab_add_fact) public void onAddFactClicked(View view) {
    factListPresenter.onAddFact();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    log.debug("Called onActivityResult(requestCode={}, resultCode={}) : {}", requestCode,
        resultCode, resultCode == Activity.RESULT_OK ? "ok"
            : (resultCode == Activity.RESULT_CANCELED ? "cancelled" : "unknown result code"));

    switch (requestCode) {
      case REQUEST_CODE_ADD_FACT:
        if (resultCode == Activity.RESULT_OK) {
          UiFact fact = data.getParcelableExtra(FactFormActivity.OUTPUT_EXTRAS_KEY_FACT);
          factListPresenter.onNewFactPrepared(mapper.transform(fact));
          showToastMessage("New fact:" + fact.getActivity());
        }
        break;
      case REQUEST_CODE_EDIT_FACT:
        if (resultCode == Activity.RESULT_OK) {
          UiFact fact = data.getParcelableExtra(FactFormActivity.OUTPUT_EXTRAS_KEY_FACT);
          factListPresenter.onEditedFactPrepared(mapper.transform(fact));
        }
        break;
      default:
        throw new AssertionError("Unknown request code");
    }
  }

  @Override public void navigateToAddFact() {
    navigator.navigateToFactFormForResult(FactListFragment.this, REQUEST_CODE_ADD_FACT);
  }

  @Override public void navigateToEditFact(@NonNull PresentationFact presentationFact) {
    UiFact fact = mapper.transform(presentationFact);

    Intent intentToLaunch = FactFormActivity.getCallingIntent(FactListFragment.this.getActivity());
    intentToLaunch.putExtra(FactFormActivity.INPUT_EXTRAS_KEY_FACT, fact);
    startActivityForResult(intentToLaunch, REQUEST_CODE_EDIT_FACT);
    //navigator.navigateToFactFormForResult(FactListFragment.this, REQUEST_CODE_EDIT_FACT);
  }

  @Override public void showFactList(@NonNull List<PresentationFact> facts) {
    log.debug("showFactList()");
    this.factsAdapter.setFactsList(facts);
  }

  @Override public void showLoading() {
    log.debug("showLoading()");
    progress_circular.setVisibility(View.VISIBLE);
  }

  @Override public void hideLoading() {
    log.debug("hideLoading()");
    progress_circular.setVisibility(View.INVISIBLE);
  }

  private void setupRetryDialog() {
    log.debug("setupRetryDialog()");
    retryDialog = new AlertDialog.Builder(getActivity())
        .setTitle("No Connection")
        .setMessage("Cannot connect to the internet!")
        .setPositiveButton("Retry", new AlertDialog.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            log.trace("Retry...");
            factListPresenter.onRetry();
          }
        })
        .setNegativeButton("Close", new AlertDialog.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            log.trace("Closing...");
            getActivity().finish();
          }
        })
        .setNeutralButton("Settings", new AlertDialog.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            navigator.navigateToSettings(getActivity());
          }
        })
        .create();
  }

  @Override public void showRetry() {
    log.debug("showRetry()");
    retryDialog.show();
  }

  @Override public void hideRetry() {
    log.debug("hideRetry()");
    retryDialog.hide();
  }

  @Override public void showError(String message) {
    this.showToastMessage(message);
  }

}
