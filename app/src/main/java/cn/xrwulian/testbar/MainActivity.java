package cn.xrwulian.testbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private TextView tv;
    // private View view;
    private List<String> data;
    private int height = 640;  // 滑动到什么地方完全变色
    private int ScrollUnm = 0;  //滑动的距离总和

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        tv = findViewById(R.id.tv);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        StatusBarUtil.setImmersiveStatusBar(this, false);
        StatusBarUtil.setStatusBarFontIconDark(this, StatusBarUtil.TYPE_M);

        int statusBarHeight = BarUtils.getStatusBarHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.height = layoutParams.height + statusBarHeight;
        //layoutParams.topMargin = statusBarHeight;
        tv.setLayoutParams(layoutParams);
        tv.setPadding(0, statusBarHeight, 0, 0);
        tv.setTextColor(Color.WHITE);

        initData();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initData() {
        data = Arrays.asList("嗯嗯", "啊啊", "哦哦", "呵呵", "嘻嘻", "哈哈", "嘎嘎", "嘿嘿", "哼哼", "哇哇", "啪啪", "嗝");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new MyAdapter(this, data));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ScrollUnm = ScrollUnm + dy; //滑动距离总合
                Log.i("dy", dy + "");
                Log.i("overallXScroll", ScrollUnm + "");
                if (ScrollUnm <= 0) {  //在顶部时完全透明
                    tv.setBackgroundColor(Color.argb((int) 0, 255, 41, 76));
                    tv.setTextColor(Color.WHITE);
                } else if (ScrollUnm > 0 && ScrollUnm <= height) {  //在滑动高度中时，设置透明度百分比（当前高度/总高度）
                    double d = (double) ScrollUnm / height;
                    double alpha = (d * 255);
                    tv.setTextColor(Color.BLACK);
                    tv.setBackgroundColor(Color.argb((int) alpha, 255, 41, 76));
                } else { //滑出总高度 完全不透明
                    tv.setTextColor(Color.BLACK);
                    tv.setBackgroundColor(Color.argb((int) 255, 255, 41, 76));
                    StatusBarUtil.setStatusBarFontIconDark(MainActivity.this, StatusBarUtil.TYPE_FLYME);
                }
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.Vh> {
        private List<String> datas;
        private Context context;

        public MyAdapter(Context context, List datas) {
            this.datas = datas;
            this.context = context;
        }

        @Override
        public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.layout_item, null);
            return new Vh(view);
        }

        @Override
        public void onBindViewHolder(Vh holder, int position) {
            holder.tv.setText(datas.get(position).toString());
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class Vh extends RecyclerView.ViewHolder {
            TextView tv;

            public Vh(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
            }
        }
    }
}
