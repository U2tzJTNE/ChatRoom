package com.u2tzjtne.chatroom.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.u2tzjtne.chatroom.R;
import com.u2tzjtne.chatroom.entity.MessageEntity;
import com.u2tzjtne.chatroom.entity.UserModel;
import com.u2tzjtne.chatroom.utils.LogUtil;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by JK on 2018/1/20.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MessageEntity> mItems;

    private int TYPE_LEFT = 1;
    private int TYPE_RIGHT = 2;

    public ChatAdapter(Context context) {
        this.context = context;
        mItems = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<MessageEntity> getList() {
        return mItems;
    }

    public void setItems(ArrayList<MessageEntity> data) {
        this.mItems.clear();
        this.mItems.addAll(data);
    }

    public void addItems(ArrayList<MessageEntity> data) {
        mItems.addAll(data);
    }

    public void addItem(MessageEntity data) {
        mItems.add(data);
        this.notifyItemInserted(mItems.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_item_layout, parent, false);
            return new LeftHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_item_layout, parent, false);
            return new RightHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageEntity entity = mItems.get(position);
        if (entity != null) {
            if (holder instanceof LeftHolder) {
                LeftHolder leftHolder = (LeftHolder) holder;
                Glide.with(context).load(entity.getAvatarURL()).into(leftHolder.ic_avatar);
                leftHolder.nickname.setText(entity.getNickname());
                leftHolder.message.setText(entity.getMessage());
            } else {
                RightHolder rightHolder = (RightHolder) holder;
                Glide.with(context).load(entity.getAvatarURL()).into(rightHolder.ic_avatar);
                rightHolder.message.setText(entity.getMessage());
            }
        } else {
            LogUtil.d("entity为空");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        String id = mItems.get(position).getUserId();
        if (id.equals(UserModel.getInstance().getCurrentUser().getObjectId())) {
            return TYPE_RIGHT;
        } else {
            return TYPE_LEFT;
        }
    }

    //左
    private class LeftHolder extends RecyclerView.ViewHolder {
        private CircleImageView ic_avatar;
        private TextView nickname;
        private EmojiTextView message;

        private LeftHolder(View itemView) {
            super(itemView);
            ic_avatar = itemView.findViewById(R.id.chat_icon_left);
            nickname = itemView.findViewById(R.id.chat_name_left);
            message = itemView.findViewById(R.id.chat_text_left);
        }
    }

    //右
    private class RightHolder extends RecyclerView.ViewHolder {
        private CircleImageView ic_avatar;
        private EmojiTextView message;

        private RightHolder(View itemView) {
            super(itemView);
            ic_avatar = itemView.findViewById(R.id.chat_icon_right);
            message = itemView.findViewById(R.id.chat_text_right);
        }
    }

}
