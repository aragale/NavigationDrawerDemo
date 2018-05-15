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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.select_foot_print, container, false);
        fpList = layout.findViewById(R.id.foot_print_list);
        new GetFootPrintListTask().execute(State.INSTANCE.sessionId);
        adapter = new SimpleAdapter(getContext(), list, R.layout.simpleitem,
                new String[]{"title", "time", "description", "images"},
                new int[]{R.id.title, R.id.time, R.id.description, R.id.image});//dehb hao ni lai ug ?
        fpList.setAdapter(adapter);
        return layout;
    }

    public class GetFootPrintListTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            list.clear();
            final FPResponse[] fpResponses = JsonUtils.read(s, FPResponse[].class);
            Log.w("footprints", fpResponses.toString());
            for (int i = 0; i < fpResponses.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("title", fpResponses[i].getTitle());
                item.put("time", fpResponses[i].getTime());
                item.put("description", fpResponses[i].getDescription());
                item.put("images", fpResponses[i].getImages());
                list.add(item);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.get_with_session(
                    Constants.HOST + Constants.FootPrintsList +
                            "?user_id=" + State.INSTANCE.userId, strings[0]);
        }
    }
}
