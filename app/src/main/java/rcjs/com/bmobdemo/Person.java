package rcjs.com.bmobdemo;

import cn.bmob.v3.BmobObject;

/**
 * Created by 仁昌居士 on 2017/2/20.
 */

public class Person extends BmobObject {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {

        return password;
    }
}
