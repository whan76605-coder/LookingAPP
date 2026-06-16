package com.example.xhsapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xhsapp.R;
import com.example.xhsapp.adapter.ChatAdapter;
import com.example.xhsapp.model.ChatMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ChatActivity extends AppCompatActivity {

    private static final String API_KEY = "sk-4e31ce983e224efd848b71299603be46";
    private static final String API_URL = "https://api.deepseek.com/chat/completions";

    private RecyclerView rvChat;
    private EditText etInput;
    private TextView tvSend, tvClear;
    private ImageView ivBack;

    private ChatAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    private OkHttpClient client = new OkHttpClient();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ivBack = findViewById(R.id.iv_back);
        rvChat = findViewById(R.id.rv_chat);
        etInput = findViewById(R.id.et_input);
        tvSend = findViewById(R.id.tv_send);
        tvClear = findViewById(R.id.tv_clear);

        ivBack.setOnClickListener(v -> finish());
        tvClear.setOnClickListener(v -> {
            messages.clear();
            adapter.clear();
            addWelcome();
        });

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(this, messages);
        rvChat.setAdapter(adapter);

        tvSend.setOnClickListener(v -> sendMessage());
        etInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                sendMessage();
                return true;
            }
            return false;
        });

        addWelcome();
    }

    private void addWelcome() {
        ChatMessage welcome = new ChatMessage(
                "你好！我是 AI 助手，基于 DeepSeek 大模型。可以帮你写文案、找灵感、解答问题，随时问我~",
                false,
                "刚刚");
        messages.add(welcome);
        adapter.notifyDataSetChanged();
    }

    private void sendMessage() {
        String text = etInput.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        String now = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        ChatMessage userMsg = new ChatMessage(text, true, now);
        adapter.addMessage(userMsg);
        etInput.setText("");

        rvChat.postDelayed(() -> rvChat.scrollToPosition(messages.size() - 1), 100);

        // Show typing indicator
        ChatMessage typing = new ChatMessage("思考中...", false, now);
        adapter.addMessage(typing);
        int typingPos = messages.size() - 1;

        executor.execute(() -> {
            try {
                String reply = callDeepSeek(text);
                handler.post(() -> {
                    messages.remove(typingPos);
                    adapter.notifyItemRemoved(typingPos);
                    ChatMessage aiMsg = new ChatMessage(reply, false, now);
                    adapter.addMessage(aiMsg);
                    rvChat.postDelayed(() -> rvChat.scrollToPosition(messages.size() - 1), 100);
                });
            } catch (Exception e) {
                handler.post(() -> {
                    messages.remove(typingPos);
                    adapter.notifyItemRemoved(typingPos);
                    ChatMessage errMsg = new ChatMessage("抱歉，请求失败了：" + e.getMessage(), false, now);
                    adapter.addMessage(errMsg);
                });
            }
        });
    }

    private String callDeepSeek(String userInput) throws Exception {
        JSONObject json = new JSONObject();
        json.put("model", "deepseek-chat");
        json.put("max_tokens", 2048);
        json.put("temperature", 0.7);

        JSONArray msgs = new JSONArray();
        JSONObject sys = new JSONObject();
        sys.put("role", "system");
        sys.put("content", "你是一个友好的AI助手，回答简洁有用。");
        msgs.put(sys);

        JSONObject user = new JSONObject();
        user.put("role", "user");
        user.put("content", userInput);
        msgs.put(user);

        json.put("messages", msgs);

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        okhttp3.Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("HTTP " + response.code());
        }

        String respBody = response.body().string();
        JSONObject respJson = new JSONObject(respBody);
        JSONArray choices = respJson.getJSONArray("choices");
        JSONObject choice = choices.getJSONObject(0);
        JSONObject message = choice.getJSONObject("message");
        return message.getString("content");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
