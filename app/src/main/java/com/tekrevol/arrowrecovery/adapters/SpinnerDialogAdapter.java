package com.tekrevol.arrowrecovery.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.tekrevol.arrowrecovery.R;
import com.tekrevol.arrowrecovery.callbacks.OnSpinnerItemClickListener;
import com.tekrevol.arrowrecovery.models.SpinnerModel;
import com.tekrevol.arrowrecovery.widget.AnyTextView;

/**
 */
public class SpinnerDialogAdapter extends RecyclerView.Adapter<SpinnerDialogAdapter.ViewHolder> {


    private final OnSpinnerItemClickListener onItemClick;


    private Activity activity;
    private ArrayList<SpinnerModel> arrData;

    private ArrayList<SpinnerModel> filteredData;
    private Filter mFilter = new ItemFilter();

    public SpinnerDialogAdapter(Activity activity, ArrayList<SpinnerModel> arrayList, OnSpinnerItemClickListener onItemClickListener) {
        this.arrData = arrayList;
        this.activity = activity;
        this.filteredData = arrayList;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_spinner_dialog, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final SpinnerModel model = filteredData.get(holder.getAdapterPosition());

        holder.txtChoice.setText(model.getText());
        if (model.isSelected()) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }
        holder.contParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(holder.getAdapterPosition(), model, SpinnerDialogAdapter.this);
            }
        });


    }

    public void addItem(ArrayList<SpinnerModel> homeCategories) {
        this.arrData = homeCategories;
        notifyDataSetChanged();
    }

    public ArrayList<SpinnerModel> getArrData() {
        return arrData;
    }
    public ArrayList<SpinnerModel> getFilteredData() {
        return filteredData;
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        AnyTextView txtChoice;
        LinearLayout contParentLayout;

        ViewHolder(View view) {
            super(view);
            radioButton = view.findViewById(R.id.radioButton);
            txtChoice = view.findViewById(R.id.txtChoice);
            contParentLayout = view.findViewById(R.id.contParentLayout);

            ButterKnife.bind(this, view);
        }
    }

    public Filter getFilter() {

        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<SpinnerModel> list = arrData;

            int count = list.size();

//            final ArrayList<String> nlist = new ArrayList<String>(count);
            final ArrayList<SpinnerModel> filterData = new ArrayList<SpinnerModel>();

            String filterableString1;
//            String filterableString2;
//            String filterableString3;
//            String filterableString4;
//            String filterableString5;

            for (int i = 0; i < count; i++) {
                filterableString1 = list.get(i).getText();

                if (filterableString1.toLowerCase().contains(filterString)
//                        || filterableString2.toLowerCase().contains(filterString)
//                        || filterableString3.toLowerCase().contains(filterString)
//                        || filterableString4.toLowerCase().contains(filterString)
//                        || filterableString5.toLowerCase().contains(filterString)
                        ) {
                    filterData.add(list.get(i));
                }
            }

            results.values = filterData;
            results.count = filterData.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<SpinnerModel>) results.values;
            notifyDataSetChanged();
        }

    }
}
