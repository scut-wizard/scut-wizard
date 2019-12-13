package com.scut.scutwizard.ScoreHelper.DetailView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scut.scutwizard.Helpers.DateHelper;
import com.scut.scutwizard.R;
import com.scut.scutwizard.ScoreHelper.HelperActivity;
import com.scut.scutwizard.ScoreHelper.Score;
import com.scut.scutwizard.ScoreHelper.ScoreImage.PhotoShowFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author MinutesSneezer
 */

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEWTYPE_PLAIN = 0, VIEWTYPE_PHOTO = 1;
    private static final int TOTAL_FIELDS = 10;

    private FragmentManager mFragManager;
    private Context         mContext;

    private Score mScore;

    public DetailAdapter(@NonNull Context context, @NonNull Score score) {
        this.mContext = context;
        this.mScore = score;
    }

    public void setFragManager(@NonNull FragmentManager fragManager) {
        this.mFragManager = fragManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEWTYPE_PHOTO:
                return new PhotoViewHolder(LayoutInflater.from(parent.getContext())
                                                         .inflate(R.layout.score_detail_row_photo,
                                                                  parent,
                                                                  false));
            case VIEWTYPE_PLAIN:
            default:
                return new PlainViewHolder(LayoutInflater.from(parent.getContext())
                                                         .inflate(R.layout.score_detail_row_plain,
                                                                  parent,
                                                                  false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEWTYPE_PHOTO: {
                PhotoViewHolder photoHolder = (PhotoViewHolder) holder;
                photoHolder.titleTv.setText(R.string.img_certificate);
//                photoHolder.photoFrag.
            }
            break;
            case VIEWTYPE_PLAIN: {
                PlainViewHolder plainHolder = (PlainViewHolder) holder;
                int titleId = 0;
                String content = "";
                switch (position) {
                    // description,value,eventDate,
                    // category,specificCategory,comment,
                    // image,subtable,createDate,modifyDate
                    case 0:
                        titleId = R.string.title;
                        content = mScore.getDescription();
                        break;
                    case 1:
                        titleId = R.string.value;
                        content = mScore.getValueStr();
                        break;
                    case 2:
                        titleId = R.string.date;
                        content = DateHelper.dateToStr(mScore.getEventDate());
                        break;
                    case 3:
                        titleId = R.string.belong_to;
                        content = mContext.getString(mScore.getCategoryRStrId());
                        break;
                    case 4:
                        titleId = R.string.specific_category;
                        content = mScore.getSpecificCategory();
                        break;
                    case 5:
                        titleId = R.string.comment;
                        content = mScore.getComment();
                        break;
                    case 7:
                        titleId = R.string.subtable;
                        content = HelperActivity.getSubtableNameById(mScore.getSubtable());
                        break;
                    case 8:
                        titleId = R.string.create_date;
                        content = DateHelper.dateToStr(mScore.getCreateDate());
                        break;
                    case 9:
                        titleId = R.string.last_modified_date;
                        content = DateHelper.dateToStr(mScore.getLastModifiedDate());
                        break;
                }
                plainHolder.titleTv.setText(titleId);
                plainHolder.contentTv.setText(content);
            }
            break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == TOTAL_FIELDS - 4 ? VIEWTYPE_PHOTO : VIEWTYPE_PLAIN;
    }

    @Override
    public int getItemCount() {
        return TOTAL_FIELDS;
    }

    class PlainViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv, contentTv;

        public PlainViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.score_dtr_plain_title);
            contentTv = itemView.findViewById(R.id.score_dtr_plain_content);
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        TextView          titleTv;
        PhotoShowFragment photoFrag;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.score_dtr_photo_title);
            photoFrag = (PhotoShowFragment) mFragManager.findFragmentById(R.id.detail_img_frag);
        }
    }
}
