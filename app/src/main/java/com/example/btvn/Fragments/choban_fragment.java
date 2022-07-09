package com.example.btvn.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btvn.ApiService.NewFeedApi;
import com.example.btvn.R;
import com.example.btvn.models.Item;
import com.example.btvn.models.Root;
import com.example.btvn.utils.Credentials;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class choban_fragment extends Fragment {
    private TextView res;
    private RecyclerView itemRec;
    private NewFeedApi newFeedApi;

    public choban_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newFeedApi = retrofit.create(NewFeedApi.class);

        Observable observable = getObservableUsers();
        Observer observer = getObserverUsers();

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        //getNewFeed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choban, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // hien thi retrofit danh sach cac item
        res = view.findViewById(R.id.result);
    }

    public void getNewFeed() {
        Call<Root> call = newFeedApi.getListNewFeed();
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (!response.isSuccessful()) {
                    res.setText(response.code() + "");
                    return;
                }
                Root root = response.body();
                res.setText(root.getItems().size());
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.d("tag", t.getMessage());
            }
        });
    }

    private Observable<Root> getObservableUsers() {
        return Observable.create(new ObservableOnSubscribe<Root>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Root> emitter) throws Throwable {
                Call<Root> call = newFeedApi.getListNewFeed();
                call.enqueue(new Callback<Root>() {
                    @Override
                    public void onResponse(Call<Root> call, Response<Root> response) {
                        if (!response.isSuccessful()) {
                            res.setText(response.code() + "");
                            return;
                        }
                        Root root = response.body();
                        if (root == null || root.getItems().isEmpty()) {
                            emitter.onError(new Exception());
                        }
                        if (!root.getItems().isEmpty()) {
                            emitter.onNext(root);
                        }
                    }

                    @Override
                    public void onFailure(Call<Root> call, Throwable t) {
                        Log.d("tag", t.getMessage());
                    }
                });
            }
        });
    }

    private Observer<Root> getObserverUsers() {
        return new Observer<Root>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                Log.e("err", "onSubscribe");
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Root root) {
                Log.d("tag", root.getItems().size() + "");
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.e("err", "onError");
            }

            @Override
            public void onComplete() {
                Log.e("err", "onComplete");
            }
        };
    }
}