package com.bizeng.lh.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizeng.lh.R;
import com.bizeng.lh.base.Content;
import com.bizeng.lh.bean.LeaderBoardBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class LeaderBoardAdapter extends BaseQuickAdapter<LeaderBoardBean, BaseViewHolder> {
    private int mType = 0;//0日，1月，2年

    public LeaderBoardAdapter(@Nullable List<LeaderBoardBean> data, int type) {
        super(R.layout.item_leader_board, data);
        mType = type;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LeaderBoardBean item) {
        int itemPosition = getItemPosition(item);
        ImageView imageView = holder.getView(R.id.iv_leader_board_head);
        TextView tvLeaderBoardNum = holder.getView(R.id.tv_leader_board_num);
        TextView tv_leader_board_money = holder.getView(R.id.tv_leader_board_money);
        View view_decoration = holder.getView(R.id.view_decoration);
        switch (itemPosition) {
            case 0:
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.drawable.shape_top_one);
                tvLeaderBoardNum.setText(String.valueOf(itemPosition + 1));
                break;
            case 1:
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.drawable.shape_top_two);
                tvLeaderBoardNum.setText(String.valueOf(itemPosition + 1));
                break;
            case 2:
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.drawable.shape_top_three);
                tvLeaderBoardNum.setText(String.valueOf(itemPosition + 1));
                break;
            default:
                imageView.setVisibility(View.INVISIBLE);
        }
        holder.setText(R.id.tv_leader_board_name, "*" + item.getNickName());
        BigDecimal bigDecimal1 = new BigDecimal(item.money).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bigDecimal = bigDecimal1.multiply(new BigDecimal(Content.U2CNY)).setScale(2, RoundingMode.HALF_UP);
        switch (mType) {
            default:
//                holder.setText(R.id.tv_leader_board_money, String.format("日币增数量%s u ≈ %s CNY", bigDecimal1.toString(), bigDecimal.toString()));
                holder.setText(R.id.tv_leader_board_money, String.format("日币增数量%s U", bigDecimal1.toString()));
//                TextParserUtils textParser = new TextParserUtils();
//                textParser.append(bigDecimal1.toString(), ConvertUtils.dp2px(21), 0, Typeface.BOLD);
//                textParser.append(" U", ConvertUtils.dp2px(14), 0, Typeface.BOLD);
//                textParser.parse(tv_leader_board_money);
                break;
            case 1:
//                holder.setText(R.id.tv_leader_board_money, String.format("月币增数量%s u ≈ %s CNY", bigDecimal1.toString(), bigDecimal.toString()));
                holder.setText(R.id.tv_leader_board_money, String.format("月币增数量%s U", bigDecimal1.toString()));
                break;
            case 2:
//                holder.setText(R.id.tv_leader_board_money, String.format("年币增数量%s u ≈ %s CNY", bigDecimal1.toString(), bigDecimal.toString()));
                holder.setText(R.id.tv_leader_board_money, String.format("年币增数量%s U", bigDecimal1.toString()));
                break;
        }
//        if (itemPosition == getData().size() - 1) {
//            view_decoration.setVisibility(View.GONE);
//        } else {
//            view_decoration.setVisibility(View.VISIBLE);
//        }
    }
}
