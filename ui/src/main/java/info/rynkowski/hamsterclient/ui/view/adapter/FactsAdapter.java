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

package info.rynkowski.hamsterclient.ui.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.view.OnFactActionListener;
import info.rynkowski.hamsterclient.ui.R;
import info.rynkowski.hamsterclient.ui.utils.TimeConverter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Adapter that manages a collection of {@link FactModel}.
 */
@Slf4j
public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.FactViewHolder> {

  private final @NonNull Context context;
  private final @NonNull LayoutInflater layoutInflater;
  private @NonNull List<FactModel> factsList;
  private @Nullable OnFactActionListener onFactActionListener;

  public FactsAdapter(@NonNull Context context, @NonNull List<FactModel> factsList) {
    this.context = context;
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.factsList = factsList;
    this.onFactActionListener = null;
  }

  @Override public int getItemCount() {
    return factsList.size();
  }

  @Override public FactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = layoutInflater.inflate(R.layout.row_fact, parent, false);
    return new FactViewHolder(view);
  }

  @Override public void onBindViewHolder(FactViewHolder holder, final int position) {
    assert onFactActionListener != null : "onFactActionListener must not be null";

    final FactModel factModel = factsList.get(position);
    holder.activity_name.setText(factModel.getActivity());
    holder.category_name.setText(factModel.getCategory());
    holder.start_time.setText(TimeConverter.toString(factModel.getStartTime(), "HH:mm"));
    holder.end_time.setText(TimeConverter.toString(factModel.getEndTime(), "HH:mm"));

    holder.itemView.setOnClickListener(view -> onFactActionListener.onFactItemClicked(factModel));

    createPopupMenu(holder, factModel);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void setOnFactActionListener(@NonNull OnFactActionListener onFactActionListener) {
    log.debug("setOnFactActionListener(onFactActionListener = {})", onFactActionListener);
    this.onFactActionListener = onFactActionListener;
  }

  public void setFactsList(@NonNull List<FactModel> factsList) {
    this.factsList = factsList;
    notifyDataSetChanged();
  }

  private void createPopupMenu(@NonNull FactViewHolder holder, @NonNull FactModel fact) {
    assert onFactActionListener != null : "onFactActionListener must not be null";

    // creating the instance of PopupMenu
    PopupMenu popupMenu = new PopupMenu(context, holder.overflow);
    // inflating the Popup using xml file
    popupMenu.getMenuInflater().inflate(R.menu.menu_row_fact, popupMenu.getMenu());
    // registering popup with OnMenuItemClickListener
    popupMenu.setOnMenuItemClickListener(item -> {
      Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

      switch (item.getItemId()) {
        case R.id.start:
          onFactActionListener.onStartFactClicked(fact);
          break;
        case R.id.stop:
          onFactActionListener.onStopFactClicked(fact);
          break;
        case R.id.edit:
          onFactActionListener.onEditFactClicked(fact);
          break;
        case R.id.remove:
          onFactActionListener.onRemoveFactClicked(fact);
          break;
        default:
          assert false : "Unknown popup menu position, item id: " + item.getItemId() + ", title: "
              + item.getTitle();
      }
      return true;
    });

    // if the fact is ongoing then disable 'start' menu position, otherwise disable 'stop'
    if (!fact.getEndTime().isPresent()) {
      popupMenu.getMenu().getItem(0).setEnabled(false);
    } else {
      popupMenu.getMenu().getItem(1).setEnabled(false);
    }

    // showing popup menu on ImageView's onClick event
    holder.overflow.setOnClickListener((view) -> popupMenu.show());
  }

  @Slf4j
  static class FactViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.activity_name) TextView activity_name;
    @Bind(R.id.category_name) TextView category_name;
    @Bind(R.id.start_time) TextView start_time;
    @Bind(R.id.end_time) TextView end_time;
    @Bind(R.id.more_button) ImageView overflow;

    public FactViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
