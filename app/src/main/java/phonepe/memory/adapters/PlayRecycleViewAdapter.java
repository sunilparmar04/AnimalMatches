package phonepe.memory.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

import phonepe.memory.R;
import phonepe.memory.models.UserChoiceModel;
import phonepe.memory.utils.PrefHelper;
import phonepe.memory.utils.UtilityMethods;


public class PlayRecycleViewAdapter extends RecyclerView.Adapter<PlayRecycleViewAdapter.MyViewHolder> {

    private List<UserChoiceModel> listModels;
    private List animalImageList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int maxNumber = 1, minNumber;
    private int selectedAnimal = -1;
    int count = 0;
    boolean isReset = false;
    boolean isPlay=true;
    boolean isStarted=false;

    public void resetCount() {
        count = 0;
        isReset = true;
        selectedAnimal = -1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_image;

        public MyViewHolder(View view) {
            super(view);
            item_image = view.findViewById(R.id.item_image);

            generateRandonNumber();
        }
    }

    public PlayRecycleViewAdapter(List<UserChoiceModel> userChoiceModelList, Context context) {
        this.listModels = userChoiceModelList;
        this.context = context;
        animalImageList = UtilityMethods.getInstance(context).getImageList();
        minNumber = 0;
        maxNumber = animalImageList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_play, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserChoiceModel model = listModels.get(position);


        if (isReset) {
            holder.item_image.setImageResource(R.drawable.ic_place_holder);
        }
        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStarted){
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemGameNotStarted();
                    }
                    return;
                }
                if (count > 0) {
                    int currentImage = (int) animalImageList.get(getRandomImageNumber());
                    holder.item_image.setImageResource(currentImage);
                    if (currentImage == selectedAnimal) {
                        PrefHelper.getInstance(context).setScore(randomNumber);
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position, randomNumber);
                        }
                    } else {
                        selectedAnimal = currentImage;
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClickNotWon(position);
                        }
                    }
                } else {
                    ++count;
                    int currentImage = (int) animalImageList.get(getRandomImageNumber());
                    holder.item_image.setImageResource(currentImage);
                    selectedAnimal = currentImage;
                }

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int winPoints);

        void onItemClickNotWon(int position);
        void onItemGameNotStarted();
    }

    private void generateRandonNumber() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                startGenerateNumber();
            }
        }).start();
    }

    private int randomNumber;

    private void startGenerateNumber() {
        while (isPlay) {
            randomNumber = new Random().nextInt(maxNumber) + minNumber;
        }
    }

    private int getRandomImageNumber() {
        return randomNumber;
    }

    public void pause(){
        isPlay=false;
    }
    public  void play(){
        isPlay=true;
    }


    public void gameStarted(){
        isStarted=true;
    }
}