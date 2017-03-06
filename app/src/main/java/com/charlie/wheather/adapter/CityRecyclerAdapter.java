package com.charlie.wheather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.charlie.wheather.R;
import com.charlie.wheather.pojo.Cities;

import java.util.List;

/**
 * 搜索城市显示列表的Adapter
 * Created by Charlie on 2017/3/6.
 */
public class CityRecyclerAdapter extends RecyclerView.Adapter<CityRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Cities> mCities;

    public CityRecyclerAdapter(Context ctx, List<Cities> cities) {
        mContext = ctx;
        mCities = cities;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyViewHolder(View.inflate(mContext, R.layout.layout_list_item, null));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Cities cities = mCities.get(position);
        String city_zh = cities.getCity_zh();
        String leader_zh = cities.getLeader_zh();
        String province_zh = cities.getProvince_zh();
        if(city_zh.equals(leader_zh)){
            if(leader_zh.equals(province_zh)){
                holder.tv.setText(city_zh);
            } else {
                holder.tv.setText(city_zh+"-"+province_zh);
            }
        } else {
            holder.tv.setText(city_zh+"-"+leader_zh+"-"+province_zh);
        }

        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCities == null ? 0 : mCities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setBackgroundResource(R.drawable.selec_list_item);
            tv = (TextView)itemView.findViewById(R.id.tv_item);
        }
    }
}
