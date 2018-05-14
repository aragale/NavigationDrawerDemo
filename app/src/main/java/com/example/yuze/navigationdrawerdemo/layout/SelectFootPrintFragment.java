package com.example.yuze.navigationdrawerdemo.layout;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.yuze.navigationdrawerdemo.Constants;
import com.example.yuze.navigationdrawerdemo.R;
import com.example.yuze.navigationdrawerdemo.State;
import com.example.yuze.navigationdrawerdemo.dto.FPResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

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
        new GetFootPrintListTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.select_foot_print, container, false);
        fpList = layout.findViewById(R.id.foot_print_list);
        adapter = new SimpleAdapter(getContext(), list, R.layout.simpleitem,
                new String[]{"title", "time", "description", "images"},
                new int[]{R.id.title, R.id.time, R.id.description, R.id.image});//dehb hao ni lai ug ?
        fpList.setAdapter(adapter);
        return layout;
    }

    public class GetFootPrintListTask extends AsyncTask<Void, Void, String> {//wo刚才也在想这个问题。。嗯，在哪里用，这个应该是一点击查询就用了。。

        @Override
        protected void onPostExecute(String s) {
            list.clear();
            final FPResponse[] fpResponses = JsonUtils.read(s, FPResponse[].class);
            Log.w("footprints", fpResponses.toString());
            //遍历
            for (FPResponse r : fpResponses) {
                if (r.getId() == null) {
                    Log.e("footprints", "get footprints err");
                } else {
                    Map<String, Object> item = new HashMap<>();
                    item.put("title", r.getTitle());
                    item.put("time", r.getTime());
                    item.put("description", r.getDescription());
                    item.put("images", r.getImages());
                    list.add(item);
                }
            }

        }

        @Override
        protected String doInBackground(Void... strings) {
            return HttpUtils.get_with_session(
                    Constants.HOST + Constants.FootPrintsList +
                            "?user_id=" + State.INSTANCE.userId, State.INSTANCE.sessionId);
        }
    }
}
