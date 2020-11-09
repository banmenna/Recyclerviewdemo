package com.example.music;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends ListActivity {

    private static final String TAG="yang";
    private static final int SEARCH_MUSIC_SUCCESS=0;
    private ProgressDialog progressDialog=null;
    private ListView musicListView;
    private SimpleAdapter listAdapter;
    private List<HashMap<String,String>> list=new ArrayList<>();

    private MediaPlayer mediaPlayer;
    private TextView currtimeView;
    private TextView totaltimeView;
    private SeekBar seekBar;
    private AlwaysMarqueeTextView nameView;
    private ImageButton playBtn;

    private String nameChecked;
    private Uri uriChecked;

    private int currPosition;//当前选中的list

    //    定义当前播放器的状态
    private static final int IDLE=0;   //空闲：没有播放音乐
    private static final int PAUSE=1;  //暂停：播放音乐时暂停
    private static final int START=2;  //正在播放音乐

    private static final int CURR_TIME_VALUE=1;

    private int currState=IDLE;//当前播放器的状态
    private boolean flag=false;//控制进度条的索引
    private boolean tag=true;

    ExecutorService executorService= Executors.newSingleThreadExecutor();
//    private void getOverflowMenu() {
//        try {
//            ViewConfiguration config = ViewConfiguration.get(this);
//            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//            if(menuKeyField != null) {
//                menuKeyField.setAccessible(true);
//                menuKeyField.setBoolean(config, false);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Button btn1=(Button)findViewById(R.id.btn1);
btn1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mediaPlayer.setLooping(true);
    }
});
        Button btn2=(Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.setLooping(false);
             tag=true;
        }
        });
        Button btn3=(Button)findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.setLooping(false);
                tag=false;
            }
        });
        musicListView=(ListView)findViewById(android.R.id.list);
        currtimeView=(TextView)findViewById(R.id.currTime);
        totaltimeView=(TextView)findViewById(R.id.totalTime);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        //ListView list = (ListView) findViewById(R.id.list);
        registerForContextMenu(musicListView);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(currState==START){
                    if(fromUser){
                        currtimeView.setText(toTime(progress));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(currState==START){
                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();

                }
            }
        });

        nameView=(AlwaysMarqueeTextView)findViewById(R.id.nameDisplay);
        playBtn=(ImageButton)findViewById(R.id.play);

        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(tag)
                next();
                else{
                    currPosition=(int) (Math.random() * list.size());
                    next();
                }
//                if (musicListView.getCount() > 0) {
//                    next();
//                } else {
//                    Toast.makeText(MainActivity.this, "播放列表为空", Toast.LENGTH_LONG).show();
//                }
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mediaPlayer.reset();
                return false;
            }
        });
//         搜索MediaStore中的音频文件，填充文件列表
        progressDialog=ProgressDialog.show(this,"","正在搜索音乐",true);
        searchMusicFile();
//        getOverflowMenu();
    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId=null;
        AdapterView.AdapterContextMenuInfo info = null;
        View itemView = null;

        switch (item.getItemId()) {
            case R.id.delete:
                //删除单词
                //info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                itemView = info.targetView;
//                textId = (TextView) itemView.findViewById(R.id.songName);
//                if (textId != null) {
//                   String strId = textId.getText().toString();
                 DeleteDialog();
                }




        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            progressDialog= ProgressDialog.show(this,"","正在搜索音乐",true);
            searchMusicFile();

            return true;
        }else if(id==R.id.action_clear){
            list.clear();
            listAdapter.notifyDataSetChanged();
            return true;
        }else if(id==R.id.action_exit){
            flag=false;
            mediaPlayer.stop();
            mediaPlayer.release();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchMusicFile(){
//        如果list不是空的，就先清空
        if(!list.isEmpty()){
            list.clear();

        }
        ContentResolver contentResolver=getContentResolver();
        //搜索SD卡里的music文件
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={
                MediaStore.Audio.Media._ID,      //根据_ID可以定位歌曲
                MediaStore.Audio.Media.TITLE,   //这个是歌曲名
                MediaStore.Audio.Media.DISPLAY_NAME, //这个是文件名
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.DATA
        };
        String where=MediaStore.Audio.Media.IS_MUSIC+">0";
        Cursor cursor=contentResolver.query(uri,projection,where,null, MediaStore.Audio.Media.DATA);
        while (cursor.moveToNext()){
            //将歌曲的信息保存到list中
            //其中，TITLE和ARTIST是用来显示到ListView中的
            // _ID和DATA都可以用来播放音乐，其实保存任一个就可以
            String songName=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artistName=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String id=Integer.toString(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            String data=Integer.toString(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            HashMap<String,String> map=new HashMap<>();
            map.put("name",songName);
            map.put("artist",artistName);
            map.put("id",id);
            map.put("data",data);
            list.add(map);

        }
        cursor.close();
        //搜索完毕之后，发一个message给Handler，对ListView的显示内容进行更新
        handler.sendEmptyMessage(SEARCH_MUSIC_SUCCESS);
    }
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                //更新播放列表
                case SEARCH_MUSIC_SUCCESS:
                    listAdapter=new SimpleAdapter(MainActivity.this,list,R.layout.musiclist,
                            new String[]{"name","artist"}, new int[]{R.id.songName,R.id.artistName});
                    MainActivity.this.setListAdapter(listAdapter);
                    Toast.makeText(MainActivity.this,"找到"+list.size()+"份音频文件",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    break;
                //更新当前歌曲的播放时间
                case CURR_TIME_VALUE:
                    currtimeView.setText(message.obj.toString());
//                    if(mediaPlayer.getCurrentPosition()==seekBar.getMax()){
//                        next();}
                    break;
                default:
                    break;
            }
        }
    };




    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(currState!= START) {
//            若在IDLE状态下,选中list中的item,则改变相应项目
            HashMap<String, String> map = list.get(position);
            nameChecked = map.get("name");
            Long idChecked = Long.parseLong(map.get("id"));
            //uriChecked:选中的歌曲相对应的Uri
            uriChecked = Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + idChecked);
            nameView.setText(nameChecked);
            currPosition = position; //这个是歌曲在列表中的位置，“上一曲”“下一曲”功能将会用到
        }else{//正在播放时点击列表的其他歌曲是，切歌
            HashMap<String, String> map = list.get(position);
            nameChecked = map.get("name");
            Long idChecked = Long.parseLong(map.get("id"));
            //uriChecked:选中的歌曲相对应的Uri
            uriChecked = Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + idChecked);
            nameView.setText(nameChecked);
            currPosition = position; //这个是歌曲在列表中的位置，“上一曲”“下一曲”功能将会用到
            start();
        }
    }
    public void onPlayClick(View v){
        switch (currState){
            case IDLE:
                start();
                currState=START;
                break;
            case PAUSE:
                //mediaPlayer.start();
                start();
                playBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_stop));
                currState=START;

                break;
            case START:
                mediaPlayer.pause();
                playBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_play));
                currState=PAUSE;
                break;
        }
    }

    private void start(){
        if(uriChecked!=null){
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(MainActivity.this,uriChecked);
                mediaPlayer.prepare();
                mediaPlayer.start();

                initSeekBar();
                nameView.setText(nameChecked);
                playBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_stop));
                currState=START;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        flag=true;
                        while(flag){
                            if(mediaPlayer.getCurrentPosition()<seekBar.getMax()){
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                Message msg=handler.obtainMessage(CURR_TIME_VALUE,
                                        toTime(mediaPlayer.getCurrentPosition()));
                                handler.sendMessage(msg);

                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                flag=false;
                            }
//                            if(mediaPlayer.getCurrentPosition()==seekBar.getMax()){
//                                next();
//                            }
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "播放列表为空或尚未选中曲目", Toast.LENGTH_LONG).show();
        }
    }
    private void initSeekBar(){
        int duration=mediaPlayer.getDuration();
        seekBar.setMax(duration);
        seekBar.setProgress(0);
        if(duration>0){
            totaltimeView.setText(toTime(duration));
        }
    }
    private String toTime(int duration){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        date.setTime(duration);
        return sdf.format(date);
    }

    private void stop() {
                 initState();
                 mediaPlayer.stop();
                 currState = IDLE;
            }
    private void initState(){
        nameView.setText("");
        currtimeView.setText("00:00");
        totaltimeView.setText("00:00");
        flag = false;
        seekBar.setProgress(0);
        playBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_play));
    }
    private void next() {
        if(musicListView.getCount()>0) {
            if (currPosition == musicListView.getCount()-1) {

                        musicListView.smoothScrollToPosition(0);
                        musicListView.performItemClick(
                                musicListView.getAdapter().getView(0, null, null),
                                0,
                                musicListView.getItemIdAtPosition(0));
                        start();


            } else {

                        musicListView.smoothScrollToPosition(currPosition + 1);
                        musicListView.performItemClick(
                                musicListView.getAdapter().getView(currPosition + 1, null, null),
                                currPosition + 1,
                                musicListView.getItemIdAtPosition(currPosition + 1));
start();
            }
        }
    }
    public void onNextClick(View v){
        next();
    }
    public void onPreviousClick(View v){
        previous();
    }
    private void previous(){
        if(musicListView.getCount()>0){
            if(currPosition>0){

                        musicListView.smoothScrollToPosition(currPosition - 1);
                        musicListView.performItemClick(
                                musicListView.getAdapter().getView(currPosition - 1, null, null),
                                currPosition - 1,
                                musicListView.getItemIdAtPosition(currPosition-1));

            }else{

                        musicListView.smoothScrollToPosition(musicListView.getCount() - 1);
                        musicListView.performItemClick(
                                musicListView.getAdapter().getView(musicListView.getCount()-1, null, null),
                                musicListView.getCount()-1,
                                musicListView.getItemIdAtPosition(musicListView.getCount()-1));


            }
            start();
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }


    private void DeleteDialog() {
       // list.remove(map.get(currPosition));
        list.remove(currPosition);
        listAdapter.notifyDataSetChanged();
        if(musicListView.getCount()>0) {
            if (currPosition == musicListView.getCount()) {

                musicListView.smoothScrollToPosition(0);
                musicListView.performItemClick(
                        musicListView.getAdapter().getView(0, null, null),
                        0,
                        musicListView.getItemIdAtPosition(0));


            } else {

                musicListView.smoothScrollToPosition(currPosition);
                musicListView.performItemClick(
                        musicListView.getAdapter().getView(currPosition , null, null),
                        currPosition,
                        musicListView.getItemIdAtPosition(currPosition ));
            }
            start();
        }

//        mediaPlayer.reset();
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        final View viewDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.deletedialog, null, false);
//        builder.setTitle("删除").setView(viewDialog)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        list.remove(currPosition);
//                    }
//
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//        builder.create().show();
    }


}