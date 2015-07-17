package pl.marek1and.myworktime;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.marek1and.myworktime.create.AbstractCreateEventFragment;
import pl.marek1and.myworktime.create.AddDayOffFragment;
import pl.marek1and.myworktime.create.AddVacationFragment;
import pl.marek1and.myworktime.create.AddWorkTimeFragment;
import pl.marek1and.myworktime.create.CreateEvent;
import pl.marek1and.myworktime.create.StartWorkFragment;
import pl.marek1and.myworktime.db.beans.Note;
import pl.marek1and.myworktime.db.beans.Transport;
import pl.marek1and.myworktime.db.beans.WorkTime;
import pl.marek1and.myworktime.transport.TransportChooserDialog;
import pl.marek1and.myworktime.transport.TransportListChangeListener;

public class CreatePanelFragment extends Fragment implements AdapterView.OnItemSelectedListener, TransportListChangeListener {

    public static final String FGMT_TAG = "fgmt_create_panel";

    private WorkTimeActivity wtActivity;
    private Spinner createEventSpinner;
    private EditText etNote;

    private Set<Transport> selectedTransports;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof WorkTimeActivity) {
            wtActivity = (WorkTimeActivity)activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fgmt_create, container, false);
        createEventSpinner = (Spinner)v.findViewById(R.id.spinner_create_event);
        etNote = (EditText)v.findViewById(R.id.et_create_panel_note);

        ImageButton saveBtn = (ImageButton)v.findViewById(R.id.fgmt_create_save_btn);
        ImageButton cancelBtn = (ImageButton)v.findViewById(R.id.fgmt_create_cancel_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wtActivity != null) {
                    wtActivity.addWorkTimeData(getWorkTimeData());
                    close();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               close();
            }
        });

        initializeSpinner();

        FlowLayout transportLayout = (FlowLayout) v.findViewById(R.id.create_panel_transport_container);

        final TransportListChangeListener listener = this;
        transportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransportChooserDialog tcd = new TransportChooserDialog(getActivity());
                tcd.setChangeListener(listener);
                tcd.setSelectedTransports(selectedTransports);
                tcd.show();
            }
        });

        drawTransports(transportLayout, null);

        return v;
    }

    private void initializeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.create_event_options,
                R.layout.spinner_item);
        createEventSpinner.setAdapter(adapter);
        createEventSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float displayHeight = size.y;

        Animator animator = null;
        if(enter) {
            animator = ObjectAnimator.ofFloat(this, "translationY", displayHeight, 0);
        }
        else {
            animator = ObjectAnimator.ofFloat(this, "translationY", 0, displayHeight);
        }

        animator.setDuration(300);
        return animator;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner_create_event) {
            CreateEvent event = CreateEvent.values()[position];
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            switch(event) {
                case START_WORK:
                    ft.replace(R.id.create_panel_fragments, new StartWorkFragment(), AbstractCreateEventFragment.TAG)
                      .commit();

                    break;
                case ADD_WORK_HOURS:
                    ft.replace(R.id.create_panel_fragments, new AddWorkTimeFragment(), AbstractCreateEventFragment.TAG)
                      .commit();

                    break;
                case ADD_DAY_OFF:
                    ft.replace(R.id.create_panel_fragments, new AddDayOffFragment(), AbstractCreateEventFragment.TAG)
                      .commit();

                    break;
                case ADD_VACATION:
                    ft.replace(R.id.create_panel_fragments, new AddVacationFragment(), AbstractCreateEventFragment.TAG)
                      .commit();

                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void transportListChanged(Set<Transport> transports) {
        FlowLayout transportLayout = (FlowLayout) getView().findViewById(R.id.create_panel_transport_container);
        drawTransports(transportLayout, transports);
        this.selectedTransports = transports;
    }

    private void drawTransports(ViewGroup container, Set<Transport> transports) {
        container.removeAllViews();

        if(transports == null || transports.isEmpty()) {
            TextView emptyTv = new TextView(getActivity());
            emptyTv.setText(getString(R.string.choose_transport));
            emptyTv.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            container.addView(emptyTv);
            return;
        }

        for(Iterator<Transport> iter = transports.iterator(); iter.hasNext();) {
            Transport transport = iter.next();
            ImageView imageView = new ImageView(getActivity());
            int id = transport.getIconIdentifier(getActivity());
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
            bmp = Bitmap.createScaledBitmap(bmp, 96, 96, true);
            imageView.setImageBitmap(bmp);

            container.addView(imageView);

            if(iter.hasNext()) {
                ImageView plusView = new ImageView(getActivity());

                Bitmap pBmp = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_input_add);
                pBmp = Bitmap.createScaledBitmap(pBmp, 64, 64, true);
                plusView.setImageBitmap(pBmp);

                container.addView(plusView);
            }
        }
    }

    public String getNote() {
        return etNote.getText().toString();
    }

    public Set<Transport> getTransports() {
        if(selectedTransports == null) {
            selectedTransports = Collections.emptySet();
        }
        return selectedTransports;
    }

    public WorkTime getWorkTimeData() {

        FragmentManager fm = getFragmentManager();
        Fragment af = fm.findFragmentByTag(AbstractCreateEventFragment.TAG);

        if(af != null && af instanceof AbstractCreateEventFragment) {
            AbstractCreateEventFragment acef = (AbstractCreateEventFragment) af;
            WorkTime wt = new WorkTime();
            wt.setType(acef.getType());
            wt.setStartTime(acef.getStartDateTime());
            wt.setEndTime(acef.getEndDateTime());

            String note = getNote();
            if(!note.isEmpty()) {
                wt.addNote(new Note(getNote()));
            }
            for(Transport t: getTransports()) {
                wt.addTransport(t);
            }
            return wt;
        }

        return null;
    }

    private void close() {
        FragmentManager fm = getFragmentManager();
        Fragment af = fm.findFragmentByTag(FGMT_TAG);
        if (af != null) {
            fm.popBackStack();
        }
    }

}
