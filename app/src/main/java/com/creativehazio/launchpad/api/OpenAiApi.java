package com.creativehazio.launchpad.api;

import com.creativehazio.launchpad.model.Text;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenAiApi {

    @POST("completions")
    Call<Text> createTextCompletionPost(@Body Text text);

    @POST
    Call<Text> createAudioTranscriptPost();

    @POST("edits")
    Call<Text> createTextEditPost(@Body Text text);
}
