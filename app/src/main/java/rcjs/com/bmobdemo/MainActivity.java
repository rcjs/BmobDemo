package rcjs.com.bmobdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.add)
    Button add;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.update_name)
    EditText updateName;
    @BindView(R.id.update_password)
    EditText updatePassword;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.login_name)
    EditText loginName;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.search_tv)
    TextView searchTv;
    @BindView(R.id.search_sp)
    Button searchSp;
    @BindView(R.id.search_lv)
    ListView searchLv;
    @BindView(R.id.search_ml)
    Button searchMl;
    @BindView(R.id.add_ml)
    Button addMl;
    @BindView(R.id.update_ml)
    Button updateMl;
    @BindView(R.id.delete_ml)
    Button deleteMl;
    @BindView(R.id.third_party_login)
    Button thirdPartyLogin;
    private Person p2;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, "67b1f7b6d74a9f08e4d7a9906628603d");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

    }


    @OnClick({R.id.add, R.id.delete, R.id.update, R.id.login, R.id.search_sp, R.id.search_ml, R.id.add_ml, R.id.update_ml, R.id.delete_ml,R.id.third_party_login})
    public void onClick(View view) {
        switch (view.getId()) {
            //单条添加
            case R.id.add:
                Person p1 = new Person();
                p1.setName("rcjs");
                p1.setPassword("123");
                p1.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "添加数据成功，返回objectId为：" + objectId, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            //多条添加
            case R.id.add_ml:
                List<BmobObject> persons = new ArrayList<BmobObject>();
                for (int i = 0; i < 10; i++) {
                    Person person = new Person();
                    person.setName("rcjs" + i);
                    person.setPassword("12345" + i);
                    persons.add(person);
                }
                new BmobBatch().insertBatch(persons).doBatch(new QueryListListener<BatchResult>() {

                    @Override
                    public void done(List<BatchResult> o, BmobException e) {
                        if (e == null) {
                            for (int i = 0; i < o.size(); i++) {
                                BatchResult result = o.get(i);
                                BmobException ex = result.getError();
                                if (ex == null) {
                                    Toast.makeText(MainActivity.this, "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
                break;
            //单条查询
            case R.id.search_sp:
                //查找Person表里面id为6b6c11c537的数据
                BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
                bmobQuery.addWhereEqualTo("name", "rcjs");
                bmobQuery.findObjects(new FindListener<Person>() {

                    @Override
                    public void done(List<Person> list, BmobException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                            searchTv.setText(list.size() + "条");
                        } else {
                            Toast.makeText(MainActivity.this, "查询失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            //多条查询
            case R.id.search_ml:

                BmobQuery<Person> query = new BmobQuery<Person>();
                //查询name叫“rcjs”的数据
                query.addWhereEqualTo("name", "rcjs");
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(50);
                //执行查询方法
                query.findObjects(new FindListener<Person>() {
                    @Override
                    public void done(List<Person> object, BmobException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "查询成功：共" + object.size() + "条数据。", Toast.LENGTH_SHORT).show();
                            adapter = new ListAdapter(MainActivity.this, object);
                            searchLv.setAdapter(adapter);
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
                break;
            //单条修改
            case R.id.update:
                //更新Person表里面id为6b6c11c537的数据，address内容更新为“北京朝阳”
                final Person p2 = new Person();
                p2.setName(updateName.getText().toString());
                p2.setPassword(updatePassword.getText().toString());
                p2.update("b106d0b70c", new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "更新成功:" + p2.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            //批量修改
            case R.id.update_ml:
                List<BmobObject> persons2 = new ArrayList<BmobObject>();
                Person p3 = new Person();
                p3.setObjectId("44c2c17446");
                p3.setName("rcjs1");
                p3.setPassword("rcjs1");
                Person p4 = new Person();
                p4.setObjectId("c3b842f530");
                p4.setName("rcjs2");
                p4.setPassword("rcjs2");
                Person p5 = new Person();
                p5.setObjectId("44337da683");
                p5.setName("rcjs3");
                p5.setPassword("rcjs3");
                persons2.add(p3);
                persons2.add(p4);
                persons2.add(p5);

                //第二种方式：v3.5.0开始提供
                new BmobBatch().updateBatch(persons2).doBatch(new QueryListListener<BatchResult>() {

                    @Override
                    public void done(List<BatchResult> o, BmobException e) {
                        if (e == null) {
                            for (int i = 0; i < o.size(); i++) {
                                BatchResult result = o.get(i);
                                BmobException ex = result.getError();
                                if (ex == null) {
                                    Toast.makeText(MainActivity.this, "第" + i + "个数据批量更新成功：" + result.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "第" + i + "个数据批量更新失败：" + ex.getMessage() + "," + ex.getErrorCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
                break;
            case R.id.delete:
                final Person p6 = new Person();
                p6.setObjectId("44337da683");
                p6.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "删除成功:" + p6.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "删除失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                break;
            case R.id.delete_ml:


                BmobQuery<Person> query2 = new BmobQuery<Person>();
                //查询name叫“rcjs”的数据
                query2.addWhereEqualTo("name", "rcjs");
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query2.setLimit(50);
                //执行查询方法
                query2.findObjects(new FindListener<Person>() {
                    @Override
                    public void done(List<Person> object, BmobException e) {
                        if (e == null) {

                            //第二种方式：v3.5.0开始提供
                            new BmobBatch().deleteBatch(convertUserToObject(object)).doBatch(new QueryListListener<BatchResult>() {

                                @Override
                                public void done(List<BatchResult> o, BmobException e) {
                                    if (e == null) {
                                        for (int i = 0; i < o.size(); i++) {
                                            BatchResult result = o.get(i);
                                            BmobException ex = result.getError();
                                            if (ex == null) {
                                                Toast.makeText(MainActivity.this, "第" + i + "个数据批量删除成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this, "第" + i + "个数据批量删除失败：" + ex.getMessage() + "," + ex.getErrorCode(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                    }
                                }
                            });
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
                break;
            case R.id.third_party_login:

                Intent authintent = new Intent(MainActivity.this,AuthActivity.class);
                startActivity(authintent);

               /* UMShareAPI mShareAPI = UMShareAPI.get( MainActivity.this );
                mShareAPI.doOauthVerify(MainActivity.this, SHARE_MEDIA.QQ, umAuthListener);*/
                break;
            case R.id.login:
            default:
                break;
        }
    }


    /*private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };*/





    private List<BmobObject> queryBeans = new ArrayList<BmobObject>();

    /**
     * 将子类集合转换为基类BmobObject集合
     *
     * @param userBeanList
     * @return
     */
    private List<BmobObject> convertUserToObject(List<Person> userBeanList) {
        queryBeans.clear();
        for (Person userBean : userBeanList) {
            queryBeans.add(userBean);
        }

        return queryBeans;
    }
}
