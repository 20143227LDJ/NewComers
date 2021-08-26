package com.example.capstone.pushmessage;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

//이거 왜 만들었는지 기억 안남
public class MyJobService extends JobService {
    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG, "Performing log running task in scheduled job");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
