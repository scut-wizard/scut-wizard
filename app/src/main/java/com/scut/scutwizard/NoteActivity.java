package com.scut.scutwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NoteActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initEvent();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        EventAdapter adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);
    }

    public void initEvent() {
        Event event = new Event("Initial Event");
        eventList.add(event);
    }

    /**
     * private List<Event> createEventList(){
     * List<Event>  eventList = new ArrayList<>();
     * Event event0=new Event("事件1");
     * eventList.add(event0);
     * Event event1=new Event("事件2");
     * eventList.add(event1);
     * return eventList;
     * }
     **/

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

        private List<Event> mEventList;

        public EventAdapter(List<Event> eventList) {
            mEventList = eventList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            Event event = mEventList.get(position);
            holder.eventName_tv.setText(event.getName());
        }

        public int getItemCount() {
            return mEventList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView eventName_tv;

            public ViewHolder(View view) {
                super(view);
                eventName_tv = (TextView) view.findViewById(R.id.event_name);
            }
        }

    }


}
