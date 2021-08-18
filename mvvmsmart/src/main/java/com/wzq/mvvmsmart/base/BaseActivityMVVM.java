package com.wzq.mvvmsmart.base;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wzq.mvvmsmart.R;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.ToastUtils;
import com.wzq.mvvmsmart.utils.Utils;
import com.wzq.mvvmsmart.widget.EmptyViewHelper;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 * * 未启用DataBinding的xml绑定数据，只使用viewBinding解决不好调试问题
 */
public abstract class BaseActivityMVVM<V extends ViewDataBinding, VM extends BaseViewModelMVVM> extends AppCompatActivity implements IBaseViewMVVM {
    protected V binding;
    protected VM viewModel;
    private EmptyViewHelper emptyViewHelper;
    private int viewModelId;
    protected Toolbar mToolbar;
    protected TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 设置为竖屏SCREEN_ORIENTATION_PORTRAIT   横SCREEN_ORIENTATION_LANDSCAPE
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //页面接受的参数方法
        try {
            initParam();
        } catch (Exception e) {
            KLog.e(e.getMessage());
        }
        //私有的初始化 binding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //页面数据初始化方法
        initData(savedInstanceState);
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        if (hasToolBar()) {
            initTitle();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        int layoutResID = initContentView(savedInstanceState);
        if (hasToolBar()) {
            //如果是引用toolbar布局的话我们根布局重写一下，需要引入base_activity作为根布局文件，然后把各ui页面的getLayoutId()定义的布局资源添加到根布局文件中去
            super.setContentView(R.layout.base_activity);
            FrameLayout container = findViewById(R.id.container);
            binding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResID, container, true);
            //DataBinding 不好调试
//            binding.setVariable(BR.context, this);
            //toolbarConfig是xml定义的name，setToolbarStyle()是我们刚才新建的toolbar配置类
//            binding.setVariable(BR.toolbarConfig, setToolbarStyle());
//            initTitle();
        } else {
            //如果不需要toolbar的话，我们直接就以getLayoutId()的布局资源id作为根布局
            binding = DataBindingUtil.setContentView(this, layoutResID);
        }
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.binding包
        viewModelId = initVariableId();
        viewModel = initViewModel();
        binding.setLifecycleOwner(this);
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModelMVVM.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        initImmersive();
    }

    public void initImmersive() {
        UltimateBarX.with(this)                   // 在当前 Activity/Fragment 生效
//                .transparent()
                .fitWindow(false)                      // 是否侵入状态栏 （true: 不侵入）
//                .color(Color.WHITE)                   // 状态栏颜色（色值）
//                .colorRes(R.color.deepSkyBlue)        // 状态栏颜色（资源 id）
//                .drawableRes(R.drawable.bg_gradient)  // 状态栏背景（drawable 资源）
                .light(true)                         // light 模式（true: 字体变灰）
                .applyStatusBar();                   // 应用到状态栏
//        UltimateBarX.addStatusBarTopPadding(targetView);
//        UltimateBarX.addNavigationBarBottomPadding(targetView);
    }

    protected void initTitle() {
        if (hasToolBar()) {
            mToolbar = findViewById(R.id.main_bar);
            mTvTitle = findViewById(R.id.tv_title);
            mToolbar.setVisibility(View.VISIBLE);
            UltimateBarX.addStatusBarTopPadding(mToolbar);
            if (mToolbar != null) {
                mToolbar.setNavigationOnClickListener(v -> finish());
            }
            if (mTvTitle != null) {
                mTvTitle.setText(setTitleBar());
                mTvTitle.setTextColor(getResources().getColor(R.color.black));
            }
        } else {
            KLog.d(KLog.COMMON, "未启用title");
        }
    }

    //刷新布局数据
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }


    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    @Override
    public void initParam() {

    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initViewObservable() {
        //私有的ViewModel与View的契约事件回调逻辑
    }

    @Override
    public void onContentReload() {
        //  有列表的页面,无数据的时候点击空白页重新加载网络
        ToastUtils.showShort("onContentReload");
    }

    /**
     * @param cls 类
     * @param <T> 泛型参数,必须继承ViewMode
     * @return 生成的viewMode实例
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
//        return ViewModelProviders.of(activity).get(cls);
//        return new ViewModelProvider(activity,
//                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(cls);
//        return new ViewModelProvider(activity).get(cls);
        return new ViewModelProvider(activity, ViewModelFactory.getInstance(Utils.getApplication())).get(cls);
    }

    public void showNormalLayout(View view) {
        if (emptyViewHelper == null) {
            emptyViewHelper = new EmptyViewHelper(this);
            emptyViewHelper.setReloadCallBack(this);
        }
        emptyViewHelper.loadNormallLayout(view);
    }

    /***
     * 加载无数据、无网络、数据异常布局
     * @param target 被替换的view
     * @param text 显示的文字
     * @param imgId 占位图
     * @param reload 是否显示重新加载按钮
     */
    public void showEmptyLayout(View target, String text, int imgId, boolean reload) {
        if (emptyViewHelper == null) {
            emptyViewHelper = new EmptyViewHelper(this);
            emptyViewHelper.setReloadCallBack(this);
        }
        emptyViewHelper.loadPlaceLayout(target, text, imgId, reload, false, null);
    }

    /***
     * 加载无数据、无网络、数据异常布局
     * @param target 被替换的view
     * @param text 显示的文字
     * @param imgId 占位图
     * @param reload 是否显示重新加载按钮
     * @param whetherToReturn 是否显示返回按钮
     * @param backClickListener 返回点击事件
     */
    public void showEmptyLayout(View target, String text, int imgId, boolean reload, boolean whetherToReturn, EmptyViewHelper.BackClickListener backClickListener) {
        if (emptyViewHelper == null) {
            emptyViewHelper = new EmptyViewHelper(this);
            emptyViewHelper.setReloadCallBack(this);
        }
        emptyViewHelper.loadPlaceLayout(target, text, imgId, reload, whetherToReturn, backClickListener);
    }

    /**
     * 是否引用toolbar
     *
     * @return 默认显示
     */
    protected boolean hasToolBar() {
        return true;
    }

    /**
     * 设置toolbar的title
     *
     * @return 标题
     */
    public String setTitleBar() {
        return "";
    }

    public Toolbar getTitleBar() {
        return mToolbar;
    }

    /**
     * 获取标题tv
     *
     * @return
     */
    public TextView getTvTitle() {
        return mTvTitle;
    }

    /**
     * 获取标题tv
     *
     * @return
     */
    public void setTvTitle(String title) {
        if (hasToolBar() && mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

}
