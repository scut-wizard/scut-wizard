package com.scut.scutwizard.ScoreHelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scut.scutwizard.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author MinutesSneezer
 */
public class StatsFragment extends Fragment {
    public static final int CATEGORY_DE = 0, CATEGORY_ZHI = 1, CATEGORY_TI = 2;
    private static final String ARG_CATEGORY = "category";
    private              int    mCategory;

    private OnFragmentInteractionListener mListener;

    private RecyclerView    mRowsView;
    private ScoreRowAdapter mAdapter = new ScoreRowAdapter();
    //    private BottomAppBar mStatsBar;
    private View            view;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StatsFragment.
     */
    @NonNull
    public static StatsFragment newInstance(int category) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    void setData(ArrayList<Score> newScores) {
        mAdapter.setData(newScores);
        if (mRowsView != null)
            mRowsView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mCategory = getArguments().getInt(ARG_CATEGORY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stats, container, false);

        mRowsView = view.findViewById(R.id.score_rows_view);
        // Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRowsView.setLayoutManager(layoutManager);
//        mRowsView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
//        mStatsBar = view.findViewById(R.id.bottomStatsBar);

        refresh();
        return view;
    }

    private void refresh() {
        /* RecyclerView */

        // Test Data
//        List<Score> scoreList = new ArrayList<>();
//        Random random = new Random();
//        final Date now = new Date();
//        for (int i = 0; i < 20; ++i) {
//            Score tmp = new Score();
//            final int category = random.nextInt(3);
//            switch (category) {
//                case 0:
//                    tmp.setCategory(Score.Category.DEYU);
//                    tmp.setDescription("灾后清扫");
//                    break;
//                case 1:
//                    tmp.setCategory(Score.Category.ZHIYU);
//                    tmp.setDescription("阅读核心期刊论文");
//                    break;
//                case 2:
//                    tmp.setCategory(Score.Category.WENTI);
//                    tmp.setDescription("使用共享单车");
//                    break;
//            }
//            tmp.setCreateDate(now);
//            tmp.setEventDate(now);
//            tmp.setLastModifiedDate(now);
//            tmp.setSpecificCategory("国家级");
//            tmp.setValue(random.nextDouble() * 10 - 5);
//            scoreList.add(tmp);
//        }
        mRowsView.setAdapter(mAdapter);

        /* BottomBar */
//        mStatsBar.setSubtitle(getCategoryName(mCategory));
//        double totalValue = 0;
//        for (Score s : scoreList) totalValue += s.getValue();
//        mStatsBar.setTitle(String.format("总计: %+.1f", totalValue));
//        mStatsBar.setTitleTextColor(0xFFFFFF);
    }

    @NonNull
    public String getCategoryName(int category) {
        switch (category) {
            case CATEGORY_DE:
                return "德育";
            case CATEGORY_ZHI:
                return "智育";
            case CATEGORY_TI:
                return "文体";
            default:
                return "";
        }
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
