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

package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.internal.di.components.FactListComponent;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;
import info.rynkowski.hamsterclient.presentation.presenter.FactListPresenter;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import info.rynkowski.hamsterclient.presentation.view.activity.FactFormActivity;
import info.rynkowski.hamsterclient.presentation.view.adapter.FactsAdapter;
import info.rynkowski.hamsterclient.presentation.view.adapter.FactsLayoutManager;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * Fragment that shows a list of Facts.
 */
@Slf4j
public class FactListFragment extends BaseFragment
    implements FactListView, FactsAdapter.OnItemClickListener {

  @Inject FactListPresenter factListPresenter;

  @InjectView(R.id.rv_facts) RecyclerView rv_facts;
  @InjectView(R.id.progress_circular) ProgressBar progress_circular;

  private FactsAdapter factsAdapter;

  private AlertDialog retryDialog;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    log.debug("onCreateView()");
    View view = inflater.inflate(R.layout.fragment_fact_list, container, false);
    ButterKnife.inject(this, view);
    this.setupRecyclerView();

    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    log.debug("onActivityCreated()");
    this.injectDependencies();
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
    ButterKnife.reset(this);
    factListPresenter.destroy();
  }

  private void injectDependencies() {
    this.getComponent(FactListComponent.class).inject(this);
  }

  private void setupRecyclerView() {
    FactsLayoutManager factsLayoutManager = new FactsLayoutManager(getActivity());
    this.rv_facts.setLayoutManager(factsLayoutManager);

    this.factsAdapter = new FactsAdapter(getActivity(), new ArrayList<>());
    this.factsAdapter.setOnItemClickListener(FactListFragment.this);
    this.rv_facts.setAdapter(factsAdapter);
  }

  @OnClick(R.id.fab_add_fact) public void onAddFactClicked(View view) {
    navigator.navigateToFactFormForResult(FactListFragment.this, Navigator.REQUEST_CODE_PICK_FACT);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case (Navigator.REQUEST_CODE_PICK_FACT):
        if (resultCode == Activity.RESULT_OK) {
          log.debug("Called onActivityResult(requestCode={}, resultCode={}) : ok", requestCode,
              resultCode);
          FactModel fact = (FactModel) data.getSerializableExtra(FactFormActivity.EXTRAS_KEY_FACT);
          factListPresenter.addFact(fact);
          showToastMessage("New fact:" + fact.getActivity());
        } else {
          log.warn("Called onActivityResult(requestCode={}, resultCode={}) : failed", requestCode,
              resultCode);
        }
        break;
      default:
        log.debug("Called onActivityResult(requestCode={}, resultCode={}) : unknown request code",
            requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override public void onFactItemClicked(FactModel factModel) {
    if (FactListFragment.this.factListPresenter != null && factModel != null) {
      FactListFragment.this.factListPresenter.onFactClicked(factModel);
    }
  }

  @Override public void renderFactList(Collection<FactModel> factModelCollection) {
    log.debug("renderFactList()");
    this.hideLoading();
    if (factModelCollection != null) {
      this.factsAdapter.setFactsCollection(factModelCollection);
    }
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

  @Override public Context getContext() {
    return this.getActivity().getApplicationContext();
  }
}
