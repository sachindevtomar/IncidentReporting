package com.fgiet.incidentreporting;

import android.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpFragment extends Fragment {

    View mView;
    ImageView h1,h2,h3,h4;

    public HelpFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mView= inflater.inflate(R.layout.fragment_help, container, false);
        return  mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        h1=(ImageView)mView.findViewById(R.id.help1);
        h2=(ImageView)mView.findViewById(R.id.help2);
        h3=(ImageView)mView.findViewById(R.id.help3);
        h4=(ImageView)mView.findViewById(R.id.help4);
        getActivity().setTitle(getString(R.string.help));

        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
                View mView=getActivity().getLayoutInflater().inflate(R.layout.overlay_help,null);
                final TextView customView_overlay=(TextView)mView.findViewById(R.id.overlay_text);
                final ImageButton close_overlay=(ImageButton)mView.findViewById(R.id.close_overlay1) ;
                customView_overlay.setText("This button denotes the current status of your request is yet to be assign. ");
                mBuilder.setView(mView);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
                View mView=getActivity().getLayoutInflater().inflate(R.layout.overlay_help,null);
                final TextView customView_overlay=(TextView)mView.findViewById(R.id.overlay_text);
                final ImageButton close_overlay=(ImageButton)mView.findViewById(R.id.close_overlay1) ;
                customView_overlay.setText("This button denotes the current status of your request is assigned to Jeev Aashraya's representative. ");
                mBuilder.setView(mView);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
                View mView=getActivity().getLayoutInflater().inflate(R.layout.overlay_help,null);
                final TextView customView_overlay=(TextView)mView.findViewById(R.id.overlay_text);
                final ImageButton close_overlay=(ImageButton)mView.findViewById(R.id.close_overlay1) ;
                customView_overlay.setText("This button denotes the current status of your request which is completed. ");
                mBuilder.setView(mView);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
                View mView=getActivity().getLayoutInflater().inflate(R.layout.overlay_help,null);
                final TextView customView_overlay=(TextView)mView.findViewById(R.id.overlay_text);
                final ImageButton close_overlay=(ImageButton)mView.findViewById(R.id.close_overlay1) ;
                customView_overlay.setText("This button denotes send option in the application. ");
                mBuilder.setView(mView);
                final AlertDialog dialog1=mBuilder.create();
                dialog1.show();

                close_overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
            }
        });
    }
}
