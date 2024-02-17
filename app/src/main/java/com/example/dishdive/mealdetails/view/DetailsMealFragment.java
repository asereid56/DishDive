package com.example.dishdive.mealdetails.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.dishdive.R;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.mealdetails.presenter.MealDetailsPresenter;
import com.example.dishdive.model.Meal;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DetailsMealFragment extends Fragment implements MealDetailsView {
    MealDetailsPresenter presenter;
    Meal meal;
    String mealName , mealID , mealThumb;
    ImageView imgMeal;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView category;
    TextView area;
    FloatingActionButton btnFav;
    TextView instructions;
    YouTubePlayerView youTubePlayerView;
    ImageView youtubeImg;
    boolean isVideoAppear = false;
    String videoLink = null;
    private boolean isVideoReady = false;
    Button btnCalender;
    FirebaseAuth auth;
    FirebaseUser user;
    FloatingActionButton btnPlan;
    public static boolean isDayAdded = false;
    public static boolean addedToSaturday = false;
    public static boolean addedToSunday = false;
    public static boolean addedToMonday = false;
    public static boolean addedToTuesday = false;
    public static boolean addedToWedensday = false;
    public static boolean addedToThursday = false;
    public static boolean addedToFriday = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_details_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        getInformationOfMealFromTheArgs();
        setDetailstoUI();

        Context applicationContext = requireContext().getApplicationContext();
        presenter = new MealDetailsPresenter(this , MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()) , MealRemoteDataSource.getInstance(applicationContext)));

        presenter.getMealDetails(mealID);

        youtubeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVideoPlayback(meal.getStrYoutube());
            }
        });
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = auth.getCurrentUser();
                if(user != null){
                    presenter.addToFav(meal);
                    Toast.makeText(applicationContext, "Added to Favourite", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Log in First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventToCalendar();
            }
        });
        btnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = auth.getCurrentUser();
                if(user != null){
                    showDialogToAddToPlan();
                }else{
                    Toast.makeText(getContext(), "Log in First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getInformationOfMealFromTheArgs() {
        DetailsMealFragmentArgs args = DetailsMealFragmentArgs.fromBundle(getArguments());
        mealName = args.getMealName();
        mealID = args.getMealID();
        mealThumb = args.getMealThumb();
    }

    private void initUI(View view) {
        imgMeal = view.findViewById(R.id.img_meal_detail);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        category = view.findViewById(R.id.category);
        area = view.findViewById(R.id.area);
        instructions = view.findViewById(R.id.instructionDetails);
        youtubeImg = view.findViewById(R.id.youtube);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.setVisibility(View.INVISIBLE);
        btnFav = view.findViewById(R.id.add_to_favourite);
        btnCalender = view.findViewById(R.id.calenderBtn);
        btnPlan = view.findViewById(R.id.add_to_plan);
    }

    private void setDetailstoUI() {
        Glide.with(this).load(mealThumb).into(imgMeal);
        collapsingToolbarLayout.setTitle(mealName);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.secondary));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.secondary));
    }

    public void playVideo(String url) {
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = getId(url);
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }
    private String getId(String url) {
        String result = "";
        if (url != null && url.split("\\?v=").length > 1)
            result = url.split("\\?v=")[1];
        return result;
    }

    @Override
    public void setMealDetails(Meal meal) {
        this.meal = meal;
        instructions.setText(meal.getStrInstructions());
        category.setText("Category: " + meal.getStrCategory());
        area.setText("Area: " + meal.getStrArea());
        videoLink = meal.getStrYoutube().toString();
    }

    private void toggleVideoPlayback(String url) {
        if (!isVideoAppear) {
            youTubePlayerView.setVisibility(View.VISIBLE);
            playVideo(url);
            isVideoAppear = true;
        } else {
            youTubePlayerView.setVisibility(View.GONE);
            youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
                @Override
                public void onYouTubePlayer(YouTubePlayer youTubePlayer) {
                        youTubePlayer.pause();
                }
            });
            isVideoAppear = false;
        }
    }


    private void addEventToCalendar() {

        String mealTitle = mealName;
        String mealDescription = instructions.getText().toString();

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, mealTitle)
                .putExtra(CalendarContract.Events.DESCRIPTION, mealDescription);

        startActivity(intent);
    }
    private void showDialogToAddToPlan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View dialogView = getLayoutInflater().inflate(R.layout.choose_day_plan,null);
        dialogView.setBackgroundResource(R.drawable.dialog_background);
        builder.setView(dialogView);

        Button btnSat = dialogView.findViewById(R.id.btnSat);
        Button btnSun = dialogView.findViewById(R.id.btnSun);
        Button btnMon = dialogView.findViewById(R.id.btnMon);
        Button btnTues = dialogView.findViewById(R.id.btnTues);
        Button btnWed = dialogView.findViewById(R.id.btnWed);
        Button btnThurs = dialogView.findViewById(R.id.btnThursday);
        Button btnFri = dialogView.findViewById(R.id.btnFri);


        btnSat.setOnClickListener(v -> addToPlan(btnSat.getText().toString(), addedToSaturday));
        btnSun.setOnClickListener(v -> addToPlan(btnSun.getText().toString(), addedToSunday));
        btnMon.setOnClickListener(v -> addToPlan(btnMon.getText().toString(), addedToMonday));
        btnTues.setOnClickListener(v -> addToPlan(btnTues.getText().toString(), addedToTuesday));
        btnWed.setOnClickListener(v -> addToPlan(btnWed.getText().toString(), addedToWedensday));
        btnThurs.setOnClickListener(v -> addToPlan(btnThurs.getText().toString(), addedToThursday));
        btnFri.setOnClickListener(v -> addToPlan(btnFri.getText().toString(), addedToFriday));


        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void addToPlan(String day, boolean addedToDay) {
        if(!addedToDay){
            Toast.makeText(getContext(), "Added to "+ day, Toast.LENGTH_SHORT).show();
            meal.setDay(day);
            presenter.addToPlan(meal);
            switch (day) {
                case "Saturday":
                    addedToSaturday = true;
                    break;
                case "Sunday":
                    addedToSunday = true;
                    break;
                case "Monday":
                    addedToMonday = true;
                    break;
                case "Tuesday":
                    addedToTuesday = true;
                    break;
                case "Wednesday":
                    addedToWedensday = true;
                    break;
                case "Thursday":
                    addedToThursday = true;
                    break;
                case "Friday":
                    addedToFriday = true;
                    break;
            }
        } else {
            Toast.makeText(getContext(), "You have already added a meal", Toast.LENGTH_SHORT).show();
        }
    }

}