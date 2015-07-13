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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.presenter.FactListPresenter;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import info.rynkowski.hamsterclient.ui.R;
import info.rynkowski.hamsterclient.ui.internal.di.components.FactListComponent;
import info.rynkowski.hamsterclient.ui.navigation.Navigator;
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
public class FactListFragment extends BaseFragment
    implements FactListView, FactsAdapter.OnItemClickListener {

  @Inject FactListPresenter factListPresenter;

  @Bind(R.id.rv_facts) RecyclerView rv_facts;
  @Bind(R.id.fragment_swipe_container) SwipeRefreshLayout swipeRefreshLayout;

  private FactsAdapter factsAdapter;

  private AlertDialog retryDialog;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    log.debug("onCreateView()");
    View view = inflater.inflate(R.layout.fragment_fact_list, container, false);
    ButterKnife.bind(this, view);

    this.setupRecyclerView();

    //noinspection Convert2MethodRef
    this.swipeRefreshLayout.setOnRefreshListener(() -> factListPresenter.onRefresh());

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
    ButterKnife.unbind(this);

    factListPresenter.setView(null);
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
          FactModel fact = data.getParcelableExtra(FactFormActivity.EXTRAS_KEY_FACT);
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

  @Override public void onFactItemClicked(@NonNull FactModel factModel) {
    FactListFragment.this.factListPresenter.onFactClicked(factModel);
  }

  @Override public void renderFactList(@NonNull List<FactModel> factModelList) {
    log.debug("renderFactList()");
    this.factsAdapter.setFactsList(factModelList);
  }

  @Override public void showLoading() {
    log.debug("showLoading()");
    swipeRefreshLayout.setRefreshing(true);
  }

  @Override public void hideLoading() {
    log.debug("hideLoading()");
    swipeRefreshLayout.setRefreshing(false);
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
