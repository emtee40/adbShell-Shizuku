package in.sunilpaulmathew.ashell.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.sunilpaulmathew.ashell.R;
import in.sunilpaulmathew.ashell.adapters.ExamplesAdapter;
import in.sunilpaulmathew.ashell.utils.Commands;

/*
 * Created by sunilpaulmathew <sunil.kde@gmail.com> on November 05, 2022
 */
public class ExamplesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examples);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        ExamplesAdapter mRecycleViewAdapter = new ExamplesAdapter(Commands.commandList());
        mRecyclerView.setAdapter(mRecycleViewAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}