package info.rynkowski.hamsterclient.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import info.rynkowski.hamsterclient.presentation.R;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import java.util.Collection;
import java.util.List;

/**
 * Adaptar that manages a collection of {@link FactModel}.
 */
public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.FactViewHolder> {

  public interface OnItemClickListener {
    void onFactItemClicked(FactModel factModel);
  }

  private List<FactModel> factsCollection;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;

  public FactsAdapter(Context context, Collection<FactModel> factsCollection) {
    this.validateFactsCollection(factsCollection);
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.factsCollection = (List<FactModel>) factsCollection;
  }

  @Override public int getItemCount() {
    return (this.factsCollection != null) ? this.factsCollection.size() : 0;
  }

  @Override public FactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = this.layoutInflater.inflate(R.layout.row_fact, parent, false);
    return new FactViewHolder(view);
  }

  @Override public void onBindViewHolder(FactViewHolder holder, final int position) {
    final FactModel factModel = this.factsCollection.get(position);
    holder.textViewActivity.setText(factModel.getActivity());
    holder.itemView.setOnClickListener((View v) -> {
      if (FactsAdapter.this.onItemClickListener != null) {
        FactsAdapter.this.onItemClickListener.onFactItemClicked(factModel);
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateFactsCollection(Collection<FactModel> factsCollection) {
    if (factsCollection == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }

  public void setFactsCollection(Collection<FactModel> factsCollection) {
    this.validateFactsCollection(factsCollection);
    this.factsCollection = (List<FactModel>) factsCollection;
    this.notifyDataSetChanged();
  }

  static class FactViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.activity_name) TextView textViewActivity;

    public FactViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }
}
