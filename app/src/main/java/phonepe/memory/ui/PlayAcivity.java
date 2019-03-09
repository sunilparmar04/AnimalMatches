package phonepe.memory.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import phonepe.memory.R;
import phonepe.memory.adapters.PlayRecycleViewAdapter;
import phonepe.memory.models.UserChoiceModel;
import phonepe.memory.utils.Constants;
import phonepe.memory.utils.PrefHelper;

public class PlayAcivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mChoiceRecyclerView;
    private PlayRecycleViewAdapter mPlayRecycleViewAdapter;
    private List<UserChoiceModel> userChoiceModelList = new ArrayList<>();
    TextView mTimeTextView, mStartStopTextview, mTitleTextView, mTextScoreTextView;
    private boolean mIsStarted = false;
    private GameTimer gameTimer;
    private long timeRemaining = 60 * 1000L;
    private int mSelectedLevel;
    private int mChoiceTotalSize, mChoiceRow;
    private SoundPool soundPool;
    private int soundid;
    private boolean loaded;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_play);
        getData();
        initSound();
        initViews();
        initValues();
        initAdapter();
        initListener();


    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.SELECTED_LEVEL)) {
            mSelectedLevel = intent.getIntExtra(Constants.SELECTED_LEVEL, 1);
        } else {
            mSelectedLevel = 1;
        }

        if (mSelectedLevel == 1) {
            mChoiceTotalSize = 9;
            mChoiceRow = 3;
        } else if (mSelectedLevel == 2) {
            mChoiceTotalSize = 16;
            mChoiceRow = 4;

        }
    }

    private void initViews() {

        mTitleTextView = findViewById(R.id.titleTextView);
        mChoiceRecyclerView = findViewById(R.id.choiceRecyclerView);
        mTimeTextView = findViewById(R.id.timeTextView);
        mStartStopTextview = findViewById(R.id.startStopTextview);
        mTextScoreTextView = findViewById(R.id.scoreTextView);

    }

    private void initValues() {
        setTitle();
        setScore();
    }


    private void initAdapter() {
        userChoiceModelList.clear();
        mPlayRecycleViewAdapter = new PlayRecycleViewAdapter(getList(), this);

        GridLayoutManager manager = new GridLayoutManager(this, mChoiceRow, GridLayoutManager.VERTICAL, false);
        mChoiceRecyclerView.setLayoutManager(manager);

        mChoiceRecyclerView.setAdapter(mPlayRecycleViewAdapter);
    }

    private List getList() {
        for (int i = 0; i < mChoiceTotalSize; i++) {
            UserChoiceModel model = new UserChoiceModel();
            model.setId(i);
            userChoiceModelList.add(model);
        }
        return userChoiceModelList;
    }

    private void initListener() {
        findViewById(R.id.backImageView).setOnClickListener(this);
        mStartStopTextview.setOnClickListener(this);

        mPlayRecycleViewAdapter.setOnItemClickListener(new PlayRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int winPoints) {
                Log.v("selected_item", "position:" + position);
                setScore();
                showStatus("You won " + winPoints);
            }

            @Override
            public void onItemClickNotWon(int position) {
                showStatus("Ohh you miss try again ");
            }

            @Override
            public void onItemGameNotStarted() {
                showError();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startStopTextview:
                if (mIsStarted) {
                    mStartStopTextview.setText(getString(R.string.start_game));
                    stopTimer();
                    mIsStarted = false;
                } else {
                    mIsStarted = true;

                    mStartStopTextview.setText(getString(R.string.pause_game));
                    startTimer();
                }
                break;

            case R.id.backImageView:
                finish();
                break;

        }
    }

    private class GameTimer extends CountDownTimer {

        public GameTimer(long startTime, long interval) {
            super(startTime, interval);

        }

        @Override
        public void onFinish() {
            showStatus("Time out, you socred: " + PrefHelper.getInstance(PlayAcivity.this).getScore());

        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeRemaining = millisUntilFinished;
            int time = (int) (millisUntilFinished / 1000);
            mTimeTextView.setText("" + time + " " + Constants.LEFT);
            Log.v("selected_item", "time left:" + time);

        }
    }

    private void startTimer() {

        if (gameTimer == null) {
            gameTimer = new GameTimer(timeRemaining,
                    1000);
            gameTimer.start();
        } else {
            gameTimer.cancel();
        }
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
    }

    private void setTitle() {
        mTitleTextView.setText(Constants.LEVEL + mSelectedLevel);
    }

    private void setScore() {
        mTextScoreTextView.setText(Constants.CURRENT_SCORE + PrefHelper.getInstance(this).getScore());
    }

    private void playSound() {
        if (loaded) {
            soundPool.play(soundid, 1, 1, 0, 0, 1);
        }
    }

    private void showStatus(String message) {
        playSound();
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mPlayRecycleViewAdapter.resetCount();
                        mPlayRecycleViewAdapter.notifyDataSetChanged();

                    }
                })
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mIsStarted) {
            stopTimer();
            if (mPlayRecycleViewAdapter != null) {
                mPlayRecycleViewAdapter.pause();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mIsStarted) {
            if (mPlayRecycleViewAdapter != null) {
                mPlayRecycleViewAdapter.play();
            }
            startTimer();

        }
    }

    private void initSound() {

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });

        soundid = soundPool.load(this, R.raw.elephant, 1);

    }

    private void showError() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.error_start_game))
                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


}
