package com.example.yuze.navigationdrawerdemo.layout;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.yuze.navigationdrawerdemo.Constants;
import com.example.yuze.navigationdrawerdemo.ExportActivity;
import com.example.yuze.navigationdrawerdemo.GetFPActivity;
import com.example.yuze.navigationdrawerdemo.R;
import com.example.yuze.navigationdrawerdemo.State;
import com.example.yuze.navigationdrawerdemo.dto.FPResponse;
import com.example.yuze.navigationdrawerdemo.task.GetFootPrintImagesTask;
import com.example.yuze.navigationdrawerdemo.task.GetTraceTask;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;
import com.example.yuze.navigationdrawerdemo.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectFootPrintFragment extends Fragment {

    private ListView fpList;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //阻塞获取足迹列表
            new GetFootPrintListTask().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.select_foot_print, container, false);
        fpList = layout.findViewById(R.id.foot_print_list);
        adapter = new SimpleAdapter(
                getActivity(),
                list,
                R.layout.simpleitem,
                new String[]{"title", "time", "description"},
                new int[]{R.id.title, R.id.time, R.id.description});
        fpList.setAdapter(adapter);
        fpList.setClickable(true);

        //添加长按弹出菜单
        fpList.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
//            menu.setHeaderTitle("操作足迹");
            menu.add(0, 0, 0, "查看");
            menu.add(0, 1, 0, "删除");
            menu.add(0, 2, 0, "分享");
        });
        return layout;
    }

    //给菜单项添加点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //获取索引，覆盖
        State.INSTANCE.fpResponse = State.INSTANCE.fpResponses[Long.valueOf(info.id).intValue()];
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case 0:
                //拉取图片
                try {
                    new GetFootPrintImagesTask().execute().get();
                    new GetTraceTask().execute(State.INSTANCE.fpResponse.getTraceId()).get();
                } catch (Exception e) {
                    Log.e("SelectFootPrintFragment", "获取足迹图片", e);
                }
                //跳转
                intent.setClass(getActivity(), GetFPActivity.class);
                startActivity(intent);
                return true;
            case 1:
                try {
                    new DeleteFootPrintTask().execute(State.INSTANCE.fpResponse.getId()).get();
                    new GetFootPrintListTask().execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                return true;
            case 2:
                State.INSTANCE.footPrintId = State.INSTANCE.fpResponse.getId();
                intent.setClass(getActivity(), ExportActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public class GetFootPrintListTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... strings) {
            String responseJson = HttpUtils.get_with_session(
                    Constants.HOST + Constants.FootPrintsList +
                            "?user_id=" + State.INSTANCE.userId, State.INSTANCE.sessionId);
            list.clear();
            final FPResponse[] fpResponses = JsonUtils.read(responseJson, FPResponse[].class);
            if (fpResponses != null) {
                //覆盖
                State.INSTANCE.fpResponses = fpResponses;
                //遍历
                for (FPResponse r : fpResponses) {
                    if (r.getId() == null) {
                        Log.e("footprints", "get footprints err");
                    } else {
                        Map<String, Object> item = new HashMap<>();
                        item.put("title", r.getTitle());
                        item.put("time", TimeUtils.format(r.getTime()));
                        item.put("description", r.getDescription());
                        item.put("images", r.getImages());
                        list.add(item);
                    }
                }
            } else {
                Toast.makeText(getActivity(), "获取足迹列表异常", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    private class DeleteFootPrintTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Toast.makeText(getActivity(), aBoolean ? "删除成功" : "删除失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return HttpUtils.delete_with_session(Constants.HOST + Constants.FootPrints + "/" + strings[0], State.INSTANCE.sessionId);
        }
    }
}
