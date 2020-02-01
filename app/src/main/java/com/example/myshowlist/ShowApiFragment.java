package com.example.myshowlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//import com.example.myshowlist.littlehelp.ApiInterface;
//import com.example.myshowlist.littlehelp.MovieResults;

public class ShowApiFragment extends Fragment {
    private static final String TAG = "ShowApiFragment";
    public static String BASE_URL = "https://api.themoviedb.org";
    public static String CATEGORY = "popular";
    public static int PAGE = 1;
    public static String API_KEY = "YOUR_API_KEY";
    public static String LANGUAGE = "pl-PL";
    public static boolean INCLUDE_ADULT = false;

    //vars for adapter
    private ArrayList<ShowAPI> shows = new ArrayList<>();

    List<MultiSearchResults.ResultsBean> listOfResults;
    SearchView searchView;
    RecyclerView recyclerView;
    ApiInterfaceMultiSearch myMultiSearchInterface;
    Retrofit retrofit;
    ShowAPI show;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_api_fragment,container,false);

        searchView = view.findViewById(R.id.search_from_database);
        recyclerView = view.findViewById(R.id.add_view_from_database);

        listOfResults = new ArrayList<>();

        show = new ShowAPI();

        //Retrofit object turns http api into interface
        retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create()) //convert json to gson
                            .build();


        myMultiSearchInterface = retrofit.create(ApiInterfaceMultiSearch.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                Call<MultiSearchResults> call = myMultiSearchInterface.getMultiSearchResult(API_KEY,LANGUAGE,query,PAGE,INCLUDE_ADULT);


                call.enqueue(new Callback<MultiSearchResults>() {
                    @Override
                    public void onResponse(Call<MultiSearchResults> call, Response<MultiSearchResults> response) {
                        //root
                        MultiSearchResults results = response.body();

                        //listOfResults.clear();
                        //list of details
                        listOfResults = results.getResults();
                        //getResults(listOfResults);
                        movieOrTVShow(listOfResults);

                        //Log.i(TAG,"Co jest kurwa: " + shows);
                        for(int i=1 ; i<shows.size(); i++){

                            Log.i(TAG,"Co jest kurwa: " + shows.get(i).getTitle());
                        }

                        initRecycleView();

                    }


                    @Override
                    public void onFailure(Call<MultiSearchResults> call, Throwable t) {

                    }
                });


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //making request call
        //Call<MovieResults> call = myInterface.getMovies(CATEGORY,API_KEY,LANGUAGE,PAGE);


        //Call<MultiSearchResults> call = myMultiSearchInterface.getMultiSearchResult(API_KEY,LANGUAGE,QUERY,PAGE,INCLUDE_ADULT);
        //Log.i(TAG,"call: " + myMultiSearchInterface.getMultiSearchResult(API_KEY,LANGUAGE,QUERY,PAGE,INCLUDE_ADULT).request());
        /*
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                //root element
                MovieResults results = response.body();
                //list of details
                List<MovieResults.ResultsBean> listOfMovies = results.getResults();
                //get details of movies
                setValues(listOfMovies);
                //make view for each show
                initRecycleView();
                //Log.i("Size: ",String.valueOf(listOfMovies.size()));
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                t.printStackTrace();
            }
        });
        */
        return view;

    }

    private void getResults(List<MultiSearchResults.ResultsBean> listofresults){

            listOfResults = listofresults;
    }

    private void initRecycleView(){
        //Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getContext(), shows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    private void movieOrTVShow(List<MultiSearchResults.ResultsBean> listofresults){
        //clearing data list for new data
        clearData();
       for(int i=0;i<listofresults.size();i++){
           //Log.i(TAG,"overview: " + listofresults.get(i).getOverview());
           //Log.i(TAG,"overview: " + listofresults.get(i).getMedia_type());

            if(listofresults.get(i).getMedia_type().contains("movie"))
                setMovie(listofresults.get(i));
            else if (listofresults.get(i).getMedia_type().contains("person"))
            {
                //do nothing
            }
            else
                setTvShow(listofresults.get(i));
            //Log.i(TAG, "setValue: " + mTitles.get(i));
        }

    }

    private void setMovie(MultiSearchResults.ResultsBean result){
            //Log.i(TAG,"image" + result.getPoster_path());
            show = new ShowAPI();
            String URL = "https://image.tmdb.org/t/p/w600_and_h900_bestv2";

            show.setImage(URL + result.getPoster_path());
            show.setTitle(result.getOriginal_title());
            show.setType(result.getMedia_type());
            show.setRating(String.valueOf(result.getVote_average()));

            if(result.getOverview() == null){
                //Log.i(TAG,"setMovie overviecheck: " + "Nie ma opisu");
                show.setDescription("Brak informacji");
            }
            else if(result.getOverview().isEmpty()){

                show.setDescription("Brak informacji");
            }
            else
                show.setDescription(result.getOverview());
                //Log.i(TAG, "setValue: " + mTitles.get(i));

            shows.add(show);
    }

    private void setTvShow(MultiSearchResults.ResultsBean result){
        /*
        for(int i=0;i<listofresults.size();i++){
            setImage(listofresults.get(i).getPoster_path());
            mTitles.add(listofresults.get(i).getTitle());
            mTypes.add();
            mRatings.add(String.valueOf(listofresults.get(i).getVote_average()));
            mDescripton.add(listofresults.get(i).getOverview());

            //Log.i(TAG, "setValue: " + mTitles.get(i));
        }
        */
        //Log.i(TAG,"Ustawiam serial");
        show = new ShowAPI();
        String URL = "https://image.tmdb.org/t/p/w600_and_h900_bestv2";

        show.setImage(URL + result.getPoster_path());
        show.setTitle(result.getOriginal_name());
        show.setType(result.getMedia_type());
        show.setRating(String.valueOf(result.getVote_average()));
        if(result.getOverview() == null){
            //Log.i(TAG,"setMovie overviecheck: " + "Nie ma opisu");
            show.setDescription("Brak informacji");
        }
        else if(result.getOverview().isEmpty()){

            show.setDescription("Brak informacji");
        }
        else
            show.setDescription(result.getOverview());

        shows.add(show);
    }


    private void clearData(){

        shows.clear();
    }
}
