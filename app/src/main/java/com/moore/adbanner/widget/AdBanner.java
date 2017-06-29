package com.moore.adbanner.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MooreLi on 2017/6/28.
 */

public class AdBanner extends RelativeLayout {
    private ViewPager mViewPager;
    private LinearLayout mLlIndicator;
    private Context mContext;
    private List<? extends AdInfo> mAdInfoList;
    private List<RelativeLayout> mPagerLayout;
    private ViewPagerAdapter mPagerAdapter;
    private List<ImageView> mIndicatorViews;
    private int currentPosition;
    private int selectPosition;
    private boolean isMoving = false;

    /**
     * 是否显示两边item
     */
    private boolean isShowSideItem = false;
    /**
     * 5s切换
     */
    private int changeTime = 5000;
    /**
     * 点击监听
     */
    private OnAdItemClickListener itemClickListener;

    public AdBanner(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public AdBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public AdBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initView();
        pagerSetting();
    }

    /**
     * 初始化View
     */
    private void initView() {
        //广告位
        mViewPager = new ViewPager(mContext);
        mPagerLayout = new ArrayList<>();
        mPagerAdapter = new ViewPagerAdapter(mPagerLayout);
        mViewPager.setAdapter(mPagerAdapter);
        //设置显示两边的item部分
        if (isShowSideItem) {
            mViewPager.setPadding(dip2px(20), 0, dip2px(20), 0);
            mViewPager.setClipToPadding(false);
        }
        this.addView(mViewPager);
        //指示器
        mLlIndicator = new LinearLayout(mContext);
        mLlIndicator.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams llParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        llParams.addRule(CENTER_HORIZONTAL, TRUE);
        llParams.setMargins(0, 0, 0, dip2px(8));
        this.addView(mLlIndicator, llParams);
    }

    /**
     * 自动切换
     * 设置指示器变换
     */
    private void pagerSetting() {
        final Bitmap whiteCircle = getCircleIndicator(Color.WHITE);
        final Bitmap grayCircle = getCircleIndicator(Color.GRAY);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == mAdInfoList.size() + 1)
                    return;
                currentPosition = position - 1;
                selectPosition = position + 1;
                for (int i = 0; i < mIndicatorViews.size(); i++) {
                    mIndicatorViews.get(i).setImageBitmap(i == currentPosition ? whiteCircle : grayCircle);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    handleSetCurrentItem(mViewPager.getCurrentItem());
                }
            }
        });
        /**
         * 设置触摸监听，当滑动viewPager时，不自动切换
         */
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isMoving = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isMoving = false;
                }
                return false;
            }
        });
    }

    /**
     * 在切换到第一个和最后一个（即为我们手动添加的两个）的时候，默默切换到最后一个和第一个。。
     */
    private void handleSetCurrentItem(int position) {
        int lastPosition = mViewPager.getAdapter().getCount() - 1;
        if (position == 0) {
            mViewPager.setCurrentItem(lastPosition == 0 ? 0 : lastPosition - 1, false);
        } else if (position == lastPosition) {
            mViewPager.setCurrentItem(1, false);
        }
    }

    /**
     * 生成广告页面
     */
    private void generatePager() {
        if (mAdInfoList != null && mAdInfoList.size() > 0) {
            for (int i = 0; i < mAdInfoList.size(); i++) {
                AdInfo info = mAdInfoList.get(i);
                RelativeLayout pager = new RelativeLayout(mContext);
                LayoutParams pagerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                pager.setLayoutParams(pagerParams);
                //image
                ImageView ivAd = new ImageView(mContext);
                ivAd.setScaleType(ImageView.ScaleType.CENTER_CROP);
                /* ******图片变暗处理开始****** */
                int brightness = -80;
                ColorMatrix matrix = new ColorMatrix();
                matrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
                ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(matrix);
                ivAd.setColorFilter(cmcf);
                /* ******图片变暗处理结束****** */
                RelativeLayout.LayoutParams ivParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ivAd.setLayoutParams(ivParams);
                Picasso.with(mContext).load(info.getImgUrl()).into(ivAd);
                pager.addView(ivAd);
                //title
                TextView tvTitle = new TextView(mContext);
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                tvTitle.setTextColor(Color.WHITE);
                RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                tvParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                tvParams.setMargins(dip2px(10), 0, dip2px(10), dip2px(30));
                tvTitle.setText(info.getTitle());
                pager.addView(tvTitle, tvParams);

                mPagerLayout.add(pager);
            }
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 生成圆点指示器
     */
    private void generateIndicator() {
        if (mAdInfoList != null && mAdInfoList.size() > 0) {
            Bitmap whiteCircle = getCircleIndicator(Color.WHITE);
            Bitmap grayCircle = getCircleIndicator(Color.GRAY);
            mIndicatorViews = new ArrayList<>();
            for (int i = 0; i < mAdInfoList.size(); i++) {
                ImageView circle = new ImageView(mContext);
                circle.setImageBitmap(i == 0 ? whiteCircle : grayCircle);
                circle.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ivParams.setMargins(dip2px(5), 0, 0, 0);
                circle.setLayoutParams(ivParams);
                mLlIndicator.addView(circle);
                mIndicatorViews.add(circle);
            }
        }
        if (mAdInfoList != null && mAdInfoList.size() > 1) {
            mViewPager.setCurrentItem(1);
        }
        postInvalidate();
    }

    /**
     * 设置数据
     *
     * @param mAdInfoList
     */
    public void setAdInfoList(List<? extends AdInfo> mAdInfoList) {
        this.mAdInfoList = mAdInfoList;
        generatePager();
        generateIndicator();
        postInvalidate();
        mHandler.sendEmptyMessageDelayed(1, changeTime);
    }

    /**
     * 自动切换时间间隔--单位：秒
     *
     * @param seconds
     */
    public void setAutoChangeTime(int seconds) {
        this.changeTime = seconds * 1000;
    }

    /**
     * 设置是否显示两边item
     *
     * @param showSideItem
     */
    public void IsShowSideItem(boolean showSideItem) {
        isShowSideItem = showSideItem;
        if (isShowSideItem) {
            mViewPager.setPadding(dip2px(20), 0, dip2px(20), 0);
            mViewPager.setClipToPadding(false);
        }else{
            mViewPager.setPadding(0, 0, 0, 0);
            mViewPager.setClipToPadding(true);
        }
    }

    /**
     * 设置监听
     *
     * @param itemClickListener
     */
    public void setOnAdItemClickListener(OnAdItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 控制自动切换
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (!isMoving) {
                    mViewPager.setCurrentItem(selectPosition, true);
                }
                mHandler.sendEmptyMessageDelayed(1, changeTime);
            }
        }
    };

    /**
     * 适配器
     * 无缝切换，所以在前后各添加一页
     */
    private class ViewPagerAdapter extends PagerAdapter {
        private List<RelativeLayout> layoutList;

        public ViewPagerAdapter(List<RelativeLayout> layoutList) {
            this.layoutList = layoutList;
        }

        @Override
        public int getCount() {
            return layoutList.size() > 1 ? layoutList.size() + 2 : layoutList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int size = layoutList.size();
            RelativeLayout view;
            if (position == 0) {
                view = layoutList.get(size - 1);
            } else if (position == size + 1) {
                view = layoutList.get(0);
            } else {
                view = layoutList.get(position - 1);
            }
            /**
             * 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
             */
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }

            if(position == selectPosition){
                view.setScaleX(1);
                view.setScaleY(1);
            }

            container.addView(view);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onAdItemClickListener(position - 1);
                    }
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }

    /**
     * 广告信息类，包含title和图片url
     */
    public static class AdInfo {
        private String title;
        private String imgUrl;
        private String id;

        public AdInfo() {
        }

        public AdInfo(String title, String imgUrl) {
            this.title = title;
            this.imgUrl = imgUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    /**
     * 点击监听接口
     */
    public interface OnAdItemClickListener {
        void onAdItemClickListener(int position);
    }

    /**
     * 工具类 dp转px
     *
     * @param dip
     * @return
     */
    private int dip2px(int dip) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (scale * dip + 0.5f);
    }

    /**
     * 获取小圆点图片
     *
     * @param color
     * @return
     */
    private Bitmap getCircleIndicator(int color) {
        int width = dip2px(7);
        int height = dip2px(7);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //截成圆形
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawOval(rectF, paint);
        return output;
    }
}
