package com.hxsn.intelliwork.emoji;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hxsn.intelliwork.R;

@SuppressLint("InflateParams")
public class FaceFragment extends Fragment {

    public static FaceFragment Instance() {
        FaceFragment instance = new FaceFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    ViewPager faceViewPager;
    EmojiIndicatorView faceIndicator;

    ArrayList<View> ViewPagerItems = new ArrayList<View>();
    ArrayList<Emoji> emojiList;
    ArrayList<Emoji> recentlyEmojiList;
    private int columns = 7; //ÿһ�еı�������
    private int rows = 3;  //�����ܹ��м���

    private OnEmojiClickListener listener;

    public void setListener(OnEmojiClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof OnEmojiClickListener) {
            this.listener = (OnEmojiClickListener) activity;
        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        emojiList = EmojiUtil.getEmojiList();
        recentlyEmojiList = new ArrayList<Emoji>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_face, container, false);
        faceViewPager = (ViewPager) view.findViewById(R.id.face_viewPager);
        faceIndicator = (EmojiIndicatorView) view.findViewById(R.id.face_indicator);
        initViews();
        return view;
    }

    private void initViews() {
        initViewPager(emojiList);
    }

    @SuppressWarnings("deprecation")
	private void initViewPager(ArrayList<Emoji> list) {
        intiIndicator(list);
        ViewPagerItems.clear();
        for (int i = 0; i < getPagerCount(list); i++) {
            ViewPagerItems.add(getViewPagerItem(i, list));
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(ViewPagerItems);
        faceViewPager.setAdapter(mVpAdapter);
        faceViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                faceIndicator.playBy(oldPosition, position);
                oldPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void intiIndicator(ArrayList<Emoji> list) {
        faceIndicator.init(getPagerCount(list));
    }

    /**
     * ��ݱ��������Լ�GridView���õ�������������Pager����
     *
     * @return
     */
    private int getPagerCount(ArrayList<Emoji> list) {
        int count = list.size();
        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
                : count / (columns * rows - 1) + 1;
    }

    private View getViewPagerItem(int position, ArrayList<Emoji> list) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_face_grid, null);//���鲼��
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * ע����Ϊÿһҳĩβ����һ��ɾ��ͼ�꣬����ÿһҳ��ʵ�ʱ���columns *��rows������1; �ճ����һ��λ�ø�ɾ��ͼ��
         * */
        final List<Emoji> subList = new ArrayList<Emoji>();
        subList.addAll(list.subList(position * (columns * rows - 1),
                (columns * rows - 1) * (position + 1) > list
                        .size() ? list.size() : (columns
                        * rows - 1)
                        * (position + 1)));
        /**
         * ĩβ���ɾ��ͼ��
         * */
        if (subList.size() < (columns * rows - 1)) {
            for (int i = subList.size(); i < (columns * rows - 1); i++) {
                subList.add(null);
            }
        }
        Emoji deleteEmoji = new Emoji();
        deleteEmoji.setImageID(R.drawable.face_delete);
        subList.add(deleteEmoji);
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, getActivity());
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // ��������ִ�еĲ���
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == columns * rows - 1) {
                    if(listener != null){
                        listener.onEmojiDelete();
                    }
                    return;
                }
                if(listener != null){
                    listener.onEmojiClick(subList.get(position));
                }
                insertToRecentList(subList.get(position));
            }
        });

        return gridview;
    }

    private void insertToRecentList(Emoji emoji) {
        if (emoji != null) {
            if (recentlyEmojiList.contains(emoji)) {
                //����Ѿ��иñ��飬�ͰѸñ���ŵ���һ��λ��
                int index = recentlyEmojiList.indexOf(emoji);
                Emoji emoji0 = recentlyEmojiList.get(0);
                recentlyEmojiList.set(index, emoji0);
                recentlyEmojiList.set(0, emoji);
                return;
            }
            if (recentlyEmojiList.size() == (rows * columns - 1)) {
                //ȥ�����һ��
                recentlyEmojiList.remove(rows * columns - 2);
            }
            recentlyEmojiList.add(0, emoji);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    class FaceGVAdapter extends BaseAdapter {
        private List<Emoji> list;
        private Context mContext;

        public FaceGVAdapter(List<Emoji> list, Context mContext) {
            super();
            this.list = list;
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_face, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.face_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list.get(position) != null) {
                    holder.iv.setImageBitmap(EmojiUtil.decodeSampledBitmapFromResource(getActivity().getResources(), list.get(position).getImageID(),
                            EmojiUtil.dip2px(getActivity(), 32), EmojiUtil.dip2px(getActivity(), 32)));
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
        }
    }

    class FaceVPAdapter extends PagerAdapter {
        // �����б�
        private List<View> views;

        public FaceVPAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) (arg2));
        }

        @Override
        public int getCount() {
            return views.size();
        }

        // ��ʼ��arg1λ�õĽ���
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1));
            return views.get(arg1);
        }

        // �ж��Ƿ��ɶ�����ɽ�
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }
    }
    
    //�����Activityʵ��(Fragment��set�ӿ�)����ӿ�������emoji�ĵ���¼�
    public interface OnEmojiClickListener {
        void onEmojiDelete();

        void onEmojiClick(Emoji emoji);
    }
}
