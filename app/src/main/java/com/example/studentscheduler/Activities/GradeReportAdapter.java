package com.example.studentscheduler.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.studentscheduler.Entities.Assessment;
import com.example.studentscheduler.R;

import java.util.List;

public class GradeReportAdapter extends ArrayAdapter<Assessment> {
    private Context mContext;
    int mResource;

    public GradeReportAdapter(Context context, int resource, List<Assessment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getAssessment_name();
        int score = getItem(position).getAssessment_score();
        String scorePer = String.valueOf(getItem(position).getAssessment_score());
        String scoreLet;

        if(score >= 90){
            scoreLet = "A";
        }
        else if(score < 90 && score >= 80){
            scoreLet = "B";
        }
        else if(score < 80 && score >= 70){
            scoreLet = "C";
        }
        else if(score < 70 && score >= 60){
            scoreLet = "D";
        }
        else{
            scoreLet = "F";
        }

        Assessment assessment = new Assessment();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.gradeReportListAssessmentName);
        TextView tvPer = (TextView) convertView.findViewById(R.id.gradeReportGradePer);
        TextView tvLet = (TextView) convertView.findViewById(R.id.gradeReportGradeLetter);

        tvName.setText(name);
        tvPer.setText(scorePer + "%");
        tvLet.setText(scoreLet);

        return convertView;

    }


}
