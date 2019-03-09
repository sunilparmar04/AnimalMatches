package phonepe.memory.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import phonepe.memory.R;
import phonepe.memory.adapters.SelectOptionRecycleViewAdapter;
import phonepe.memory.models.UserOptionModel;
import phonepe.memory.utils.Constants;
import phonepe.memory.utils.PrefHelper;

public class HomeLauncherActivity extends AppCompatActivity {

    private RecyclerView mSelectOptionRecyclerView;
    private SelectOptionRecycleViewAdapter mSelectOptionRecycleViewAdapter;
    private TextView titleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        initAdapter();
        initListener();
    }

    private void initViews() {
        findViewById(R.id.backImageView).setVisibility(View.INVISIBLE);
        mSelectOptionRecyclerView = findViewById(R.id.selectOptionRecyclerView);
        findViewById(R.id.timeTextView).setVisibility(View.GONE);
        findViewById(R.id.startStopTextview).setVisibility(View.GONE);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(getString(R.string.title_home));
    }

    private void initAdapter() {
        mSelectOptionRecycleViewAdapter = new SelectOptionRecycleViewAdapter(getList(), this);
        mSelectOptionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSelectOptionRecyclerView.setAdapter(mSelectOptionRecycleViewAdapter);
    }

    private List getList() {
        List list = new ArrayList();
        for (int i = 1; i < 3; i++) {
            UserOptionModel model = new UserOptionModel();
            model.setId(i);
            model.setTitle("Level " + i);
            list.add(model);
        }

        return list;
    }

    private void initListener() {


        mSelectOptionRecycleViewAdapter.setOnItemClickListener(new SelectOptionRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openPlayActivity(position);
            }
        });
    }

    private void openPlayActivity(int position) {

        if (position == 0) {
            startActivity(new Intent(this, PlayAcivity.class).putExtra(Constants.SELECTED_LEVEL, position + 1));
        } else if (position == 1 && PrefHelper.getInstance(this).getScore() >= 50) {
            startActivity(new Intent(this, PlayAcivity.class).putExtra(Constants.SELECTED_LEVEL, position + 1));
        } else {
            showError();
        }

    }

    private void showError() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.error_now_allow_level))
                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
