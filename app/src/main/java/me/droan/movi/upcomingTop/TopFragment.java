package me.droan.movi.upcomingTop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;
import me.droan.movi.MoviFragment;
import me.droan.movi.MoviServices;
import me.droan.movi.MovieListModel.MovieList;
import me.droan.movi.MovieListModel.Result;
import me.droan.movi.popular.PoUpToAdapter;
import me.droan.movi.utils.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopFragment extends MoviFragment {
  private static final String TAG = "TopFragment";
  MoviServices services;
  ArrayList<Result> list = new ArrayList<>();

  public static TopFragment newInstance() {
    Bundle args = new Bundle();
    TopFragment fragment = new TopFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void initViews() {

  }

  @Override public RecyclerView.Adapter getAdapter() {
    return new PoUpToAdapter(getActivity(), PoUpToAdapter.FROM_TOP, list, null);
  }

  @Override public int getFancyGridType() {
    return SIMPLE_FANCY_TYPE;
  }

  private void initRetrofit() {

    services = RetrofitHelper.getRetrofitObj().create((MoviServices.class));
    serviceHandler();
  }

  private void serviceHandler() {
    Call<MovieList> call;
    call = services.getTopRatedMovies();

    call.enqueue(new Callback<MovieList>() {
      @Override public void onResponse(Call<MovieList> call, Response<MovieList> response) {
        Log.d(TAG,
            "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
        MovieList movieList = response.body();
        list = (ArrayList<Result>) movieList.results;
        recyclerView.setAdapter(new PoUpToAdapter(getActivity(), PoUpToAdapter.FROM_TOP, list,
            new PoUpToAdapter.OnItemClickListener() {
              @Override public void onItemClick(Result model) {
                ((OpenDetailListener) getActivity()).openDetail(model);
              }
            }));
      }

      @Override public void onFailure(Call<MovieList> call, Throwable t) {
        Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
      }
    });
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initRetrofit();
  }
}