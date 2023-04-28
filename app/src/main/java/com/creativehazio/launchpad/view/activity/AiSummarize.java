package com.creativehazio.launchpad.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.model.Text;
import com.creativehazio.launchpad.mutual_functions.CustomToast;
import com.creativehazio.launchpad.mutual_functions.NetworkStatus;
import com.creativehazio.launchpad.viewmodel.AiSummarizerViewModel;

public class AiSummarize extends AppCompatActivity {

    private View articleView;
    private View websiteView;
    private View youtubeView;

    private EditText articleEdt;
    private EditText summarizedArticleEdt;
    private EditText websiteEdt;
    private EditText summarizedWebsiteEdt;
    private EditText youtubeLinkEdt;
    private EditText youtubeNameEdt;
    private EditText summarizedYoutubeEdt;

    private Button articleSummarizerBtn;
    private Button websiteLinkSummarizerBtn;
    private Button youtubeVideoLinkSummarizerBtn;
    private Button summarizeArticleBtn;
    private Button summarizeWebsiteBtn;
    private Button summarizeYoutubeBtn;
    private Button clearArticleBtn;
    private Button clearWebsiteBtn;
    private Button clearYoutubeBtn;
    private ImageButton copySummarizedArticleBtn;
    private ImageButton copySummarizedWebsiteBtn;
    private ImageButton copySummarizedYoutubeBtn;

    private AiSummarizerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_summarize);
        initViewsAndConfigs();

        //TODO Add loading screen
        articleSummarizerBtn.setTextColor(getColor(R.color.blue_stone));
        articleSummarizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setDataToNull();
                articleSummarizerBtn.setTextColor(getColor(R.color.blue_stone));
                websiteLinkSummarizerBtn.setTextColor(getColor(R.color.white));
                youtubeVideoLinkSummarizerBtn.setTextColor(getColor(R.color.white));
                articleView.setVisibility(View.VISIBLE);
                websiteView.setVisibility(View.GONE);
                youtubeView.setVisibility(View.GONE);
            }
        });

        websiteLinkSummarizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setDataToNull();
                websiteLinkSummarizerBtn.setTextColor(getColor(R.color.blue_stone));
                articleSummarizerBtn.setTextColor(getColor(R.color.white));
                youtubeVideoLinkSummarizerBtn.setTextColor(getColor(R.color.white));
                websiteView.setVisibility(View.VISIBLE);
                articleView.setVisibility(View.GONE);
                youtubeView.setVisibility(View.GONE);
            }
        });

        youtubeVideoLinkSummarizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setDataToNull();
                youtubeVideoLinkSummarizerBtn.setTextColor(getColor(R.color.blue_stone));
                websiteLinkSummarizerBtn.setTextColor(getColor(R.color.white));
                articleSummarizerBtn.setTextColor(getColor(R.color.white));
                youtubeView.setVisibility(View.VISIBLE);
                articleView.setVisibility(View.GONE);
                websiteView.setVisibility(View.GONE);
            }
        });

        summarizeArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String articlePrompt = articleEdt.getText().toString();
                NetworkStatus internetStatus = new NetworkStatus(AiSummarize.this);

                if (TextUtils.isEmpty(articlePrompt.trim())) {
                    CustomToast.showLongToast(AiSummarize.this, "Text is empty");
                } else if (!internetStatus.isOnline()){
                    CustomToast.showLongToast(getApplication(),
                            getApplication().getString(R.string.internet_conn_poor));
                } else {
                    Text text = new Text(getString(R.string.open_ai_model),
                            "Summarize the following text with the most unique and helpful " +
                                    "points, into a numbered list of key points and takeaways: " +articlePrompt,
                            2048);
                    viewModel.summarizeText(text, getString(R.string.OPENAI_KEY));
                    viewModel.getSummarizedText().observe(AiSummarize.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                copySummarizedArticleBtn.setVisibility(View.VISIBLE);
                                summarizedArticleEdt.setVisibility(View.VISIBLE);
                                summarizedArticleEdt.setText(s);
                                summarizedArticleEdt.requestFocus();
                            }
                        }
                    });
                }
            }
        });

        summarizeWebsiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String websitePrompt = websiteEdt.getText().toString();
                NetworkStatus internetStatus = new NetworkStatus(AiSummarize.this);

                if (TextUtils.isEmpty(websitePrompt.trim())) {
                    CustomToast.showLongToast(AiSummarize.this, "Text is empty");
                } else if (!internetStatus.isOnline()){
                    CustomToast.showLongToast(getApplication(),
                            getApplication().getString(R.string.internet_conn_poor));
                } else {
                    Text text = new Text(getString(R.string.open_ai_model),
                            "Summarize with the most unique and helpful " +
                                    "points, into a numbered list of key points and takeaways: " +websitePrompt,
                            2048);
                    viewModel.summarizeText(text, getString(R.string.OPENAI_KEY));
                    viewModel.getSummarizedText().observe(AiSummarize.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                copySummarizedWebsiteBtn.setVisibility(View.VISIBLE);
                                summarizedWebsiteEdt.setVisibility(View.VISIBLE);
                                summarizedWebsiteEdt.setText(s);
                                summarizedWebsiteEdt.requestFocus();
                            }
                        }
                    });
                }
            }
        });

        summarizeYoutubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String youtubePrompt = youtubeLinkEdt.getText().toString() + "\n"
                                            + youtubeNameEdt.getText().toString();
                NetworkStatus internetStatus = new NetworkStatus(AiSummarize.this);

                if (TextUtils.isEmpty(youtubePrompt.trim())) {
                    CustomToast.showLongToast(AiSummarize.this, "Text is empty");
                } else if (!internetStatus.isOnline()){
                    CustomToast.showLongToast(getApplication(),
                            getApplication().getString(R.string.internet_conn_poor));
                } else {
                    Text text = new Text(getString(R.string.open_ai_model),
                            "Summarize with the most unique and helpful " +
                                    "points, into a numbered list of key points and takeaways: " +youtubePrompt,
                            2048);
                    viewModel.summarizeText(text, getString(R.string.OPENAI_KEY));
                    viewModel.getSummarizedText().observe(AiSummarize.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                copySummarizedYoutubeBtn.setVisibility(View.VISIBLE);
                                summarizedYoutubeEdt.setVisibility(View.VISIBLE);
                                summarizedYoutubeEdt.setText(s);
                                summarizedYoutubeEdt.requestFocus();
                            }
                        }
                    });
                }
            }
        });

        clearArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                articleEdt.setText("");
                summarizedArticleEdt.setText("");
                summarizedArticleEdt.setVisibility(View.GONE);
                copySummarizedArticleBtn.setVisibility(View.GONE);
            }
        });

        clearWebsiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                websiteEdt.setText("");
                summarizedWebsiteEdt.setText("");
                summarizedWebsiteEdt.setVisibility(View.GONE);
                copySummarizedWebsiteBtn.setVisibility(View.GONE);
            }
        });

        clearYoutubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtubeLinkEdt.setText("");
                youtubeNameEdt.setText("");
                summarizedYoutubeEdt.setText("");
                summarizedYoutubeEdt.setVisibility(View.GONE);
                copySummarizedYoutubeBtn.setVisibility(View.GONE);
            }
        });

        copySummarizedArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(summarizedArticleEdt.getText().toString().trim())){
                    CustomToast.showLongToast(AiSummarize.this, "No text to copy");
                }
                String textResultString = summarizedArticleEdt.getText().toString();
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Summarized text",textResultString);
                clipboard.setPrimaryClip(clip);
                CustomToast.showLongToast(AiSummarize.this, "Text has been copied" +
                        " to clipboard");
            }
        });

        copySummarizedWebsiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(summarizedWebsiteEdt.getText().toString().trim())){
                    CustomToast.showLongToast(AiSummarize.this, "No text to copy");
                }
                String textResultString = summarizedWebsiteEdt.getText().toString();
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Summarized text",textResultString);
                clipboard.setPrimaryClip(clip);
                CustomToast.showLongToast(AiSummarize.this, "Text has been copied" +
                        " to clipboard");
            }
        });

        copySummarizedYoutubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(summarizedYoutubeEdt.getText().toString().trim())){
                    CustomToast.showLongToast(AiSummarize.this, "No text to copy");
                }
                String textResultString = summarizedYoutubeEdt.getText().toString();
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Summarized text",textResultString);
                clipboard.setPrimaryClip(clip);
                CustomToast.showLongToast(AiSummarize.this, "Text has been copied" +
                        " to clipboard");
            }
        });
    }

    private void initViewsAndConfigs() {
        viewModel = new ViewModelProvider(this).get(AiSummarizerViewModel.class);
        articleView = findViewById(R.id.article_view);
        websiteView = findViewById(R.id.website_view);
        youtubeView = findViewById(R.id.youtube_view);
        articleEdt = findViewById(R.id.article_edt);
        summarizedArticleEdt = findViewById(R.id.summarized_txt_edt);
        websiteEdt = findViewById(R.id.website_link_edt);
        summarizedWebsiteEdt = findViewById(R.id.summarized_website_edt);
        youtubeLinkEdt = findViewById(R.id.youtube_link_edt);
        youtubeNameEdt = findViewById(R.id.youtube_video_name_edt);
        summarizedYoutubeEdt = findViewById(R.id.summarized_youtube_edt);
        articleSummarizerBtn = findViewById(R.id.long_article_summarizer_btn);
        copySummarizedArticleBtn = findViewById(R.id.copy_summarized_txt_btn);
        websiteLinkSummarizerBtn = findViewById(R.id.website_summarizer_btn);
        copySummarizedWebsiteBtn = findViewById(R.id.copy_summarized_website_btn);
        youtubeVideoLinkSummarizerBtn = findViewById(R.id.youtube_video_link_summarizer_btn);
        copySummarizedYoutubeBtn = findViewById(R.id.copy_summarized_youtube_btn);
        summarizeArticleBtn = findViewById(R.id.summarize_article_btn);
        summarizeWebsiteBtn = findViewById(R.id.summarize_weblink_btn);
        summarizeYoutubeBtn = findViewById(R.id.summarize_youtube_link_btn);
        clearArticleBtn = findViewById(R.id.clear_article_btn);
        clearWebsiteBtn = findViewById(R.id.clear_weblink_btn);
        clearYoutubeBtn = findViewById(R.id.clear_youtube_link_btn);
    }
}