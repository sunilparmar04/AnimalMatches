package phonepe.memory.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import phonepe.memory.R;
import phonepe.memory.models.UserOptionModel;

public class SelectOptionRecycleViewAdapter extends RecyclerView.Adapter<SelectOptionRecycleViewAdapter.MyViewHolder> {

    private List<UserOptionModel> listModels;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView selectLevelTextView;

        public MyViewHolder(View view) {
            super(view);
            selectLevelTextView = view.findViewById(R.id.selectLevelTextView);

        }
    }

    public SelectOptionRecycleViewAdapter(List<UserOptionModel> userOptionModelList, Context context) {
        this.listModels = userOptionModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_play_option, parent, false);

        return new SelectOptionRecycleViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectOptionRecycleViewAdapter.MyViewHolder holder, final int position) {
        final UserOptionModel model = listModels.get(position);

        holder.selectLevelTextView.setText("" + model.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}