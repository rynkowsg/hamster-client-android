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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.ui.R;
import info.rynkowski.hamsterclient.ui.utils.TimeConverter;
import java.util.Collection;
import java.util.List;

/**
 * Adapter that manages a collection of {@link FactModel}.
 */
public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.FactViewHolder> {

  private final @NonNull LayoutInflater layoutInflater;
  private @NonNull List<FactModel> factsCollection;
  private @NonNull Optional<OnItemClickListener> onItemClickListener;

  public FactsAdapter(@NonNull Context context, @NonNull Collection<FactModel> factsCollection) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.factsCollection = (List<FactModel>) factsCollection;
    this.onItemClickListener = Optional.absent();
  }

  @Override public int getItemCount() {
    return this.factsCollection.size();
  }

  @Override public FactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = this.layoutInflater.inflate(R.layout.row_fact, parent, false);
    return new FactViewHolder(view);
  }

  @Override public void onBindViewHolder(FactViewHolder holder, final int position) {
    final FactModel factModel = this.factsCollection.get(position);
    holder.activity_name.setText(factModel.getActivity());
    holder.start_time.setText(TimeConverter.toString(factModel.getStartTime()));
    holder.end_time.setText(TimeConverter.toString(factModel.getEndTime()));
    holder.itemView.setOnClickListener((View v) -> {
      if (FactsAdapter.this.onItemClickListener.isPresent()) {
        FactsAdapter.this.onItemClickListener.get().onFactItemClicked(factModel);
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void setOnItemClickListener(@NonNull OnItemClickListener onItemClickListener) {
    this.onItemClickListener = Optional.of(onItemClickListener);
  }

  public void setFactsCollection(@NonNull Collection<FactModel> factsCollection) {
    this.factsCollection = (List<FactModel>) factsCollection;
    this.notifyDataSetChanged();
  }

  public interface OnItemClickListener {
    void onFactItemClicked(@NonNull FactModel factModel);
  }

  static class FactViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.activity_name) TextView activity_name;
    @Bind(R.id.start_time) TextView start_time;
    @Bind(R.id.end_time) TextView end_time;

    public FactViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
