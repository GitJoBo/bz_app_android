package com.bizeng.lh.http;

import android.text.TextUtils;

import com.bizeng.lh.bean.PageList;
import com.bizeng.lh.bean.Response;
import com.bizeng.lh.bean.event.LoginEvent;
import com.bizeng.lh.utils.MMKVUtils;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import rxhttp.wrapper.annotation.Parser;
import rxhttp.wrapper.entity.ParameterizedTypeImpl;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.parse.AbstractParser;
import rxhttp.wrapper.utils.Converter;
import rxhttp.wrapper.utils.GsonUtil;

/**
 * 输入T,输出T,并对code统一判断
 * 200	请求成功
 * 9000	请求失败，并且带有提示msg
 * 403	需要登陆才能请求（未登陆）
 * User: ljx
 * Date: 2018/10/23
 * Time: 13:49
 */
@Parser(name = "Response", wrappers = {PageList.class})
public class ResponseParser<T> extends AbstractParser<T> {


    /**
     * 此构造方法适用于任意Class对象，但更多用于带泛型的Class对象，如：List<Student>
     * <p>
     * 用法:
     * Java: .asParser(new ResponseParser<List<Student>>(){})
     * Kotlin: .asParser(object : ResponseParser<List<Student>>() {})
     * <p>
     * 注：此构造方法一定要用protected关键字修饰，否则调用此构造方法将拿不到泛型类型
     */
    protected ResponseParser() {
        super();
    }

    /**
     * 此构造方法仅适用于不带泛型的Class对象，如: Student.class
     * <p>
     * 用法
     * Java: .asParser(new ResponseParser<>(Student.class))   或者  .asResponse(Student.class)
     * Kotlin: .asParser(ResponseParser(Student::class.java)) 或者  .asResponse(Student::class.java)
     */
    public ResponseParser(Type type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T onParse(@NotNull okhttp3.Response response) throws IOException {
        final Type type = ParameterizedTypeImpl.get(Response.class, mType); //获取泛型类型
        Response<T> data = Converter.convert(response, type);
        T t = data.getData(); //获取data字段
        if (t == null) {
            if (mType == String.class) {
                /*
                 * 考虑到有些时候服务端会返回：{"errorCode":0,"errorMsg":"关注成功"}  类似没有data的数据
                 * 此时code正确，但是data字段为空，直接返回data的话，会报空指针错误，
                 * 所以，判断泛型为String类型时，重新赋值，并确保赋值不为null
                 */
                t = (T) data.getMsg();
            }
        }
        if (data.getCode() == 200 && t == null) {
            //解决后台数据不规范 同一个接口 data一会null，一会{},不为null时，app会用到data数据
            Response<String> dataStr = Converter.convert(response, type);
            //第二步，取出data字段，转换成我们想要的对象
            t = GsonUtil.getObject(dataStr.getData(), mType);
        }
        if (data.getCode() == 403) {
//            || data.getCode() == 9000
            LoginEvent loginEvent = new LoginEvent();
            if (!TextUtils.isEmpty(MMKVUtils.getInstance().getToken())) {
                loginEvent.state = 3;
                data.setMsg("您的登录信息已过期，请重新登录");
            }else {
                loginEvent.state = 0;
            }
            //token 过期或未登录
            EventBus.getDefault().post(loginEvent);
            MMKVUtils.getInstance().clearUserAndToken();
        }
        if (t == null || data.getCode() != 200) {
            throw new ParseException(String.valueOf(data.getCode()), data.getMsg(), response);
        }

        return t;
    }
}
