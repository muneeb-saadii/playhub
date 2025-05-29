package com.project.playhub.adapters;



import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.playhub.databinding.ItemOnboardingBinding;
import com.project.playhub.models.OnBoardingModel;

import java.util.ArrayList;



public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.VHolder> {

    private final ArrayList<OnBoardingModel> data;

    public OnBoardingAdapter(ArrayList<OnBoardingModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOnboardingBinding binding = ItemOnboardingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        OnBoardingModel iData = data.get(position);
        if (iData == null) return;

        ItemOnboardingBinding binding = holder.binding;

        // Load image using Glide
        Glide.with(binding.getRoot().getContext())
                .load(iData.getImage())
                .into(binding.imageView);

        // Set title and description
        binding.itemHTxt.setText(iData.getTitle());
        binding.itemVTxt.setText(iData.getDesc());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class VHolder extends RecyclerView.ViewHolder {
        final ItemOnboardingBinding binding;

        public VHolder(@NonNull ItemOnboardingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
