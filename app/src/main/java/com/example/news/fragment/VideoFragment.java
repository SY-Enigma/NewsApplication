package com.example.news.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;


import com.example.news.R;
import com.example.news.entity.Video;
import com.example.news.util.HttpUtils;
import com.example.news.util.MyDatabaseHelper;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;


import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoFragment extends BaseFragment {

    final String url = "http://api.tianapi.com/txapi/dyvideohot/index?key=5247ca753ac53750629e9a01664d99c2&num=30";
    private PullLoadMoreRecyclerView recyclerView;
    private MyDatabaseHelper helper;

    List<Video> videoList = new ArrayList<>();
    VideoAdapter adapter;
    int page = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video,container,false);
        helper = new MyDatabaseHelper(getContext(),"UserDB.db",null,6);
//        dao = new OperationDao(helper);
        recyclerView = (PullLoadMoreRecyclerView) v.findViewById(R.id.recycler_view_video_fragment);
        recyclerView.setLinearLayout();
        page = 1;
        getVideoListfromAPI(page);
        adapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setFooterViewText("加载更多···");
        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = new Random().nextInt(10)+1;
                        videoList.clear();
                        getVideoListfromAPI(page);
                        recyclerView.setPullLoadMoreCompleted();
                    }
                },1500);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = new Random().nextInt(20)+9;
                        getMoreVideo(page);
                        recyclerView.setPullLoadMoreCompleted();
                    }
                },1500);
            }
        });


        return v;
    }

    private class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView avatar;
        private TextView title;
        private TextView author;
        private Video mVideo;
        private JCVideoPlayerStandard player;
        public VideoHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.videoTitle);
            author = (TextView) view.findViewById(R.id.video_item_author);
            avatar = (ImageView) view.findViewById(R.id.video_item_avatar);
            player = (JCVideoPlayerStandard) view.findViewById(R.id.player_list_video);
        }

        public void bind(Video video){
            mVideo = video;
            title.setText(mVideo.getTitle());
            author.setText(mVideo.getAuthor());
            if (player != null){
                player.release();
            }
            boolean setUp = player.setUp(mVideo.getPlayaddr(),JCVideoPlayer.SCREEN_LAYOUT_LIST,"");
            if (setUp){
                Glide.with(VideoFragment.this).load(mVideo.getCoverurl()).into(player.thumbImageView);
            }
            String imgurl = mVideo.getAvatar();
            Glide.with(getContext())
                    .load(imgurl)
                    .centerCrop()
                    .into(avatar);
        }

        @Override
        public void onClick(View view) {

            player.startWindowFullscreen();
            player.startButton.performClick();
        }
    }

    private class VideoAdapter extends RecyclerView.Adapter<VideoHolder>{
        private List<Video> videoList;
        public VideoAdapter(List<Video> videoList) {
            this.videoList = videoList;
        }

        @NonNull
        @Override
        public VideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.video_item,viewGroup,false);
            return new VideoHolder(v);

        }

        public void onBindViewHolder(@NonNull VideoHolder videoHolder, int i) {
            Video mVideo = videoList.get(i);
            videoHolder.bind(mVideo);
        }

        @Override
        public int getItemCount() {
            return videoList.size();
        }
    }

    private void getMoreVideo(int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
//                    Dao mdao = new Dao();
                    String jsonData = HttpUtils.requestHttp(url);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("newslist");


                    JSONObject json_video = jsonArray.getJSONObject(i);
                    String imgUrl = json_video.getString("avatar");
                    String cover = json_video.getString("coverurl");
                    String title = json_video.getString("title");
                    String address = json_video.getString("playaddr");
                    address = convertUrlHttp(address);
                    String author = json_video.getString("author");
                    Video video = new Video(address,cover,title,author,imgUrl);

                    SQLiteDatabase db = helper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    //组装数据
                    values.put("coverurl",cover);
                    values.put("title",title);
                    values.put("url",address);
                    values.put("author",author);
                    values.put("avatar",imgUrl);
                    db.insert("Video",null,values);

                    db.close();
                    videoList.add(video);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getVideoListfromAPI(int ran) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String jsonData = HttpUtils.requestHttp(url);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("newslist");

                    for (int i = ran;i<ran+10;i++){
                        JSONObject json_video = jsonArray.getJSONObject(i);
                        String imgUrl = json_video.getString("avatar");
                        String cover = json_video.getString("coverurl");
                        String title = json_video.getString("title");
                        String address = json_video.getString("playaddr");
                        address = convertUrlHttp(address);
                        String author = json_video.getString("author");
                        Video video = new Video(address,cover,title,author,imgUrl);

                        SQLiteDatabase db = helper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        //组装数据
                        values.put("coverurl",cover);
                        values.put("title",title);
                        values.put("url",address);
                        values.put("author",author);
                        values.put("avatar",imgUrl);
                        db.insert("Video",null,values);

                        db.close();
                        videoList.add(video);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public String convertUrlHttp(String url){
        if(url == null || url.length() == 0){
            return "";
        }

        return url.replaceFirst("^(?:https:)?//", "http://");
    }

}

