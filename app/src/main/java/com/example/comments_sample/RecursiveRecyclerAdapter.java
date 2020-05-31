package com.example.comments_sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public abstract class RecursiveRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    List<RecursiveItemSet> mItems = new ArrayList<>();

    public void setItems(List items) { // ITEM = DummyData class
        for (Object item : items) {
            mItems.add(new RecursiveItemSet(
                    item, false, 0
            ));
        }
    }

    public Object getItem(int position) {
        Log.d("getItem","pos is "+position + " mItems size as of now is "+mItems.size());
        return mItems.get(position).getItem();
    }

    public boolean isParent(int position) {
        return mItems.get(position).depth==0;
    }


    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        //notifyDataSetChanged();
        final DummyData item = (DummyData)getItem(position);
        Log.d("recycl0","pos got is "+position);
        final Object item2 = mItems.get(position);
        ImageButton b1 = (ImageButton) holder.itemView.findViewById(R.id.b1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(expanded(item2)){
                        hideItems(item2,item.getChildren());
                    }
                    else{
                        showItems(item2,item.getChildren());
                    }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos2 = holder.getAdapterPosition();
                call_alert_reply(view,pos2);
            }
        });
        ImageButton b2 = (ImageButton) holder.itemView.findViewById(R.id.delete_btn);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos2 = holder.getAdapterPosition();
                delete_comm(pos2);
            }
        });

    }

    private void delete_comm(int position) {
        DummyData d = (DummyData) getItem(position);
        DummyData parent = d.parent;
        ArrayList<DummyData> arr = (ArrayList<DummyData>)d.getChildren();
        int len = arr.size();
        Log.d("del_debug_before","compare b4 clear child "+((DummyData) getItem(position)).getChildren().size() + "len as of now "+len);
        RecursiveItemSet r = mItems.get(position);
        if(r.isExpanded() && len>0 ){   // hide as of now but recursively delete when data is taken from server
            hideItems(r,arr);
        }
        d.clearChildren();
        if(parent!=null){
            parent.getChildren().remove(d);
        }
        Log.d("del_debug","compare after clear child "+((DummyData) getItem(position)).getChildren().size());
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,mItems.size());
    }

    void call_alert_reply(View view2, final int pos) {
        final InputMethodManager imm = (InputMethodManager)view2.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater)view2.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.alert_view, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Please add your reply");
        alertDialog.setCancelable(false);
        TextView tv = (TextView) view.findViewById(R.id.tv3);
        tv.setText(getItem(pos).toString());
        Log.d("recycl1","pos got is "+pos);

        final EditText etComments = (EditText) view.findViewById(R.id.ed2);
        etComments.requestFocus();
        alertDialog.setPositiveButton( "ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reply = etComments.getText().toString();
                SharedPreferences pref = view.getContext().getSharedPreferences("my_pref",MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("reply",reply);
                edit.apply();
                dialog.dismiss();
                if(!reply.equals("")){
                    Toast.makeText(view.getContext(), "Reply added", Toast.LENGTH_SHORT).show();
                    addReply(reply,pos);
                }
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

            }
        });

        alertDialog.setNegativeButton( "DISCARD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.setView(view);
        dialog.show();
        etComments.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public void addComment(String new_comm) {
        DummyData d = new DummyData(new_comm,new ArrayList<DummyData>(),null);
        RecursiveItemSet itemset = new RecursiveItemSet(d,false,0);
        mItems.add(itemset);
        notifyItemRangeInserted(mItems.size(), 1);
        notifyItemChanged(mItems.size()+1);
    }

    public void addReply(String reply,int position){

        DummyData d = (DummyData) getItem(position);
        Log.d("recyc2","pos got is "+position);
        List<DummyData> children = d.getChildren();
        int child_list_len = children.size();
        Log.d("Recursiverecycler","exp id "+reply+" pos "+position);
        children.add(new DummyData(reply,new ArrayList<DummyData>(),d));
        if(mItems.get(position).isExpanded()) {
            mItems.add(position + child_list_len + 1, new RecursiveItemSet(
                    new DummyData(reply, new ArrayList<DummyData>(),d), false, mItems.get(position).getDepth() + 1

            ));
            notifyItemRangeInserted(position + child_list_len + 1, 1);
            notifyItemChanged(position + child_list_len);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getDepth();
    }

    public boolean expanded(Object item) {
        int position =  mItems.indexOf(item);
        return mItems.get(position).isExpanded();
    }

    public void showItems(Object parent, List items) {
        int position = mItems.indexOf(parent);
        for (int i = 0; i < items.size(); i ++) {
            Object item = items.get(i);
            mItems.add(position + 1 + i, new RecursiveItemSet(
                    item, false, mItems.get(position).getDepth() + 1
            ));
        }
        if(items.size()!=0) {
            mItems.get(position).expanded = true;
            notifyItemRangeInserted(position + 1, items.size());
            notifyItemChanged(position);
        }
        //notifyDataSetChanged();
    }

    public void hideItems(Object parent, List items) {
        int position = mItems.indexOf(parent);
        for (int i = 0; i < items.size(); i ++) {
            if (expanded(mItems.get(position + 1))) {
                DummyData item = (DummyData) getItem(position + 1);
                hideItems(mItems.get(position + 1), item.getChildren());
            }
            mItems.remove(position + 1);
        }
        mItems.get(position).expanded = false;
        notifyItemRangeRemoved(position+1, items.size());
        notifyItemChanged(position);
    }

    public void clearItems() {
        if (mItems != null) {
            mItems.clear();
        }
    }

}