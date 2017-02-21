package rcjs.com.bmobdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<Person> list = new ArrayList<>();

    public ListAdapter(Context context, List<Person> list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_simpleadapter, null);
            holder.name = (TextView)view.findViewById(R.id.name);
            holder.password = (TextView)view.findViewById(R.id.password);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(list.get(i).getName());
        holder.password.setText(list.get(i).getPassword());
        return view;
    }

    private  class ViewHolder {
        private  TextView name;
        private  TextView password;

    }
}
