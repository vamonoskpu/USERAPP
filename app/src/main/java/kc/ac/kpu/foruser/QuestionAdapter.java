package kc.ac.kpu.foruser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    ArrayList<QuestionData> mData = null;


    //생성자에서 list객체를 전달받음
    public QuestionAdapter(ArrayList<QuestionData> list){
        mData = list;
    }



    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.question_item,parent,false);


        QuestionAdapter.ViewHolder vh = new QuestionAdapter.ViewHolder(view);
        //뷰홀더로 감쌈


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {

        QuestionData questionData = mData.get(position);



       holder.title.setText(questionData.getTitle());
       holder.writer.setText(questionData.getWriter());
       holder.answer.setText(questionData.getAnswer());



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;    // 제목
        TextView writer;   //작성자
        TextView answer;   //답변여부

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            writer = itemView.findViewById(R.id.writer);
            answer= itemView.findViewById(R.id.answer);




            //뷰 객체에 대한 참조



        }
    }
}
