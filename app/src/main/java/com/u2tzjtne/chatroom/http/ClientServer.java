package com.u2tzjtne.chatroom.http;

/**
 * Created by 77009 on 2017-01-25.
 */

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.u2tzjtne.chatroom.utils.Const;
import com.u2tzjtne.chatroom.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class ClientServer implements Runnable {

    private Socket s;
    // 定义向UI线程发送消息的Handler对象
    private Handler handler;
    // 定义接收UI线程的消息的Handler对象
    public Handler revHandler;

    // 该线程所处理的Socket所对应的输入流
    BufferedReader br = null;
    OutputStream os = null;

    public ClientServer(Handler handler) {
        this.handler = handler;
    }

    @SuppressLint("HandlerLeak")
    public void run() {
        try {
            LogUtil.d("开始连接服务器");
            s = new Socket(Const.SERVER_IP, 30000);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = s.getOutputStream();
            LogUtil.d("连接成功");
            // 启动一条子线程来读取服务器响应的数据
            new Thread() {
                @Override
                public void run() {

                    String content;
                    // 不断读取Socket输入流中的内容
                    try {
                        while ((content = br.readLine()) != null) {
                            // 每当读到来自服务器的数据之后，发送消息通知程序
                            // 界面显示该数据
                            LogUtil.d("content:" + content);
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }.start();
            // 为当前线程初始化Looper
            Looper.prepare();
            // 创建revHandler对象
            revHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 接收到UI线程中用户输入的数据
                    if (msg.what == 0x345) {
                        // 将用户在文本框内输入的内容写入网络
                        try {
                            LogUtil.d("开始发送数据");
                            os.write((msg.obj.toString() + "\r\n")
                                    .getBytes("utf-8"));
                            LogUtil.d("数据发送成功");
                        } catch (Exception e) {
                            LogUtil.d("数据发送失败");
                            e.printStackTrace();
                        }
                    }
                }
            };
            // 启动Looper
            Looper.loop();
        } catch (SocketTimeoutException e1) {
            System.out.println("网络连接超时！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}