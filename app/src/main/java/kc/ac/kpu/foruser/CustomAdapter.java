package kc.ac.kpu.foruser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<MenuItem> arrayList;


    public CustomAdapter(ArrayList<MenuItem> arrayList, Context context) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item,parent,false);


        CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(view);
        //뷰홀더로 감쌈

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        MenuItem user = arrayList.get(position);
        holder.iv_profile.setImageResource(user.getProfile());
        holder.tv_menu.setText(user.getId());
        holder.tv_price.setText(user.getPrice());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_menu;
        TextView tv_price;
        ImageView iv_profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_menu = itemView.findViewById(R.id.tv_menuname);
            tv_price =itemView.findViewById(R.id.tv_price);
            iv_profile=itemView.findViewById(R.id.iv_profile);
        }
    }
}
