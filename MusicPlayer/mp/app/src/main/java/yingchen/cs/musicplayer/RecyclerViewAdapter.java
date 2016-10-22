package yingchen.cs.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import yingchen.cs.musicplayer.visualizer.Concepts.NamesInNavDrawer;

/**
 * Created by yingchen on 4/18/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<NamesInNavDrawer> namesInNavDrawers = Collections.emptyList();
    private Context context;
    private ClickListener clickListener;

    public RecyclerViewAdapter(Context context, List<NamesInNavDrawer> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        namesInNavDrawers = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.sort_by_for_navi, viewGroup, false);
        MyViewHolder h = new MyViewHolder(v);
        return h;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        NamesInNavDrawer s = namesInNavDrawers.get(i);
        viewHolder.t.setText(s.title);
        viewHolder.i.setImageResource(s.itemId);

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return namesInNavDrawers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t;
        ImageView i;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            t = (TextView) itemView.findViewById(R.id.songsName);
            i = (ImageView) itemView.findViewById(R.id.songIcon);
            i.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(context, MainActivity.class));
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }
}
