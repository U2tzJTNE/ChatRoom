package com.u2tzjtne.chatroom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.u2tzjtne.chatroom.R;
import com.u2tzjtne.chatroom.entity.MessageEntity;
import com.u2tzjtne.chatroom.entity.MyUser;
import com.u2tzjtne.chatroom.entity.UserModel;
import com.u2tzjtne.chatroom.http.ClientServer;
import com.u2tzjtne.chatroom.ui.adapter.ChatAdapter;
import com.u2tzjtne.chatroom.utils.LogUtil;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_emoji)
    ImageButton emojiButton;
    @BindView(R.id.emoji_edit)
    EmojiEditText emojiEdit;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    EmojiPopup emojiPopup;
    @BindView(R.id.root_view)
    LinearLayout rootView;
    @BindView(R.id.chat_list)
    RecyclerView chatList;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;

    private CircleImageView nv_header_avatar;
    private TextView nv_header_nickname;
    private TextView nv_header_email;
    private Handler handler;
    // 定义与服务器通信的子线程
    private ClientServer clientThread;
    //发送时间
    private String date;
    private long exitTime = 0;

    private ChatAdapter adapter;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
        setUpEmojiPopup();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 如果消息来自于子线程
                if (msg.what == 0x123) {
                    String json = msg.obj.toString();
                    MessageEntity entity = new Gson().fromJson(json, MessageEntity.class);
                    if (entity != null) {
                        //插入数据
                        LogUtil.d(json);
                        adapter.addItem(entity);
                        chatList.scrollToPosition(adapter.getItemCount()-1);
                    } else {
                        Toast.makeText(ChatActivity.this, "接收的数据异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        clientThread = new ClientServer(handler);
        // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        new Thread(clientThread).start();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        nv_header_avatar = headerLayout.findViewById(R.id.nav_header_avatar);
        nv_header_nickname = headerLayout.findViewById(R.id.nav_header_nickname);
        nv_header_email = headerLayout.findViewById(R.id.nav_header_email);
        nv_header_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭drawer
                if (drawer.isDrawerOpen(Gravity.START)) {
                    drawer.closeDrawers();
                }
                if (UserModel.getInstance().isLogin()) {
                    startActivity(new Intent(ChatActivity.this, AccountActivity.class));
                }
            }
        });
        adapter = new ChatAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatList.setLayoutManager(linearLayoutManager);
        chatList.setAdapter(adapter);
    }

    //发送消息
    public void send(String msg) {
        try {
            // 当用户按下发送按钮后，将用户输入的数据封装成Message
            // 然后发送给子线程的Handler
            Message message = new Message();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = dateFormat.format(new Date());

            MyUser user = UserModel.getInstance().getCurrentUser();
            MessageEntity entity = new MessageEntity();
            if (user.getAvatarURL() == null) {
                entity.setAvatarURL("http://img.blog.csdn.net/20160121144006405");
            } else {
                entity.setAvatarURL(user.getAvatarURL());
            }
            entity.setDate(date);
            entity.setNickname(user.getNickname());
            entity.setUserId(user.getObjectId());
            entity.setMessage(msg);

            Gson gson = new Gson();
            String json = gson.toJson(entity);
            message.what = 0x345;
            if (json != null) {
                message.obj = json;
                clientThread.revHandler.sendMessage(message);
            } else {
                Toast.makeText(this, "json转换失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyUser user = UserModel.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getAvatarURL() != null) {
                Glide.with(this)
                        .load(user.getAvatarURL())
                        .into(nv_header_avatar);
            }
            nv_header_nickname.setText(user.getNickname());
            nv_header_email.setText(user.getEmail());
        }
    }

    /**
     * 返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override
                    public void onEmojiPopupShown() {
                        emojiButton.setImageResource(R.drawable.icon_keyboard_normal);
                    }
                })
                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        emojiButton.setImageResource(R.drawable.icon_face_normal);
                    }
                })
                .build(emojiEdit);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        if (emojiPopup != null) {
            emojiPopup.dismiss();
        }

        super.onStop();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sound:
                break;
            case R.id.nav_vibration:
                break;
            case R.id.nav_theme:
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_exit:
                finish();
                System.exit(0);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick({R.id.btn_emoji, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_emoji:
                emojiPopup.toggle();
                break;
            case R.id.btn_send:
                String msg = emojiEdit.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(ChatActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    send(msg);
                }
                // 清空input文本框
                emojiEdit.setText("");
                break;
        }
    }
}
