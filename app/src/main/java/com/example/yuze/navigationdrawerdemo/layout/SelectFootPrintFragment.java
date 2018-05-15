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
        try {
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
                new String[]{"title", "time", "description", "images"},
                new int[]{R.id.title, R.id.time, R.id.description, R.id.image});
        fpList.setAdapter(adapter);
        return layout;
    }

    public class GetFootPrintListTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... strings) {
            String responseJson = HttpUtils.get_with_session(
                    Constants.HOST + Constants.FootPrintsList +
                            "?user_id=" + State.INSTANCE.userId, State.INSTANCE.sessionId);
            list.clear();
            final FPResponse[] fpResponses = JsonUtils.read(responseJson, FPResponse[].class);
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
            return null;
        }
    }
}
