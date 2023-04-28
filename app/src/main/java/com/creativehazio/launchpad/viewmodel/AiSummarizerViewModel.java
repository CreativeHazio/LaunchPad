package com.creativehazio.launchpad.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.creativehazio.launchpad.R;
import com.creativehazio.launchpad.api.OpenAiApi;
import com.creativehazio.launchpad.api.OpenAiRetrofitClient;
import com.creativehazio.launchpad.model.Text;
import com.creativehazio.launchpad.mutual_functions.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AiSummarizerViewModel extends AndroidViewModel {

    private Retrofit retrofitClient;
    private OpenAiApi openAiApi;

    private MutableLiveData<String> data;

    public AiSummarizerViewModel(@NonNull Application application) {
        super(application);
        data = new MutableLiveData<>();
    }

    public void setDataToNull() {
        data.setValue(null);
    }

    public LiveData<String> getSummarizedText() {
        return data;
    }

    public void summarizeText(Text text, String open_ai_key) {
        new SummarizeTextAsyncTask(open_ai_key).execute(text);
    }

    private class SummarizeTextAsyncTask extends AsyncTask<Text,Void,Void> {

        private String open_ai_key;

        public SummarizeTextAsyncTask(String open_ai_key) {
            this.open_ai_key = open_ai_key;
        }

        @Override
        protected Void doInBackground(Text... text) {
            retrofitClient = OpenAiRetrofitClient.getInstance(open_ai_key);
            openAiApi = retrofitClient.create(OpenAiApi.class);

            Call<Text> textCall = openAiApi.createTextCompletionPost(text[0]);
            textCall.enqueue(new Callback<Text>() {
                @Override
                public void onResponse(Call<Text> call, Response<Text> response) {
                    if (!response.isSuccessful()){
                        CustomToast.showShortToast(getApplication(), ""+response.code());
                        return;
                    } else {
                        Text textResponse = response.body();
                        Text.Choices[] choices = textResponse.getChoices();
                        for (Text.Choices choice : choices){
                            data.setValue(choice.getText());
                        }

                    }
                }

                @Override
                public void onFailure(Call<Text> call, Throwable t) {
                    CustomToast.showLongToast(getApplication(),
                            getApplication().getString(R.string.internet_conn_poor));
                }
            });
            return null;
        }
    }
}
