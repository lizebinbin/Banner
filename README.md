# AdBanner
广告轮播控件

**一个封装的广告轮播控件，简单好用啦，效果如图：（PS：录制效果不太好）**
![](http://i.imgur.com/8t9LZgY.gif)

## 用法：
### 导入
* 将项目中的AdBanner拷到自己项目中就可以用？很简单伐？
* 图片加载用了Picasso，导入Picasso也很简单？

### xml
        <com.moore.adbanner.widget.AdBanner
        android:id="@+id/Main_adBanner"
        android:layout_width="match_parent"
        android:layout_height="200dp" />

### 设置
    /**
	 * 设置数据集
	 */
	mAdBanner.setAdInfoList(infos);
	/**
	 * 设置自动切换时间 单位 秒
	 */
	mAdBanner.setAutoChangeTime(3);
	/**
	 * 是否显示两边item
	 */
	mAdBanner.IsShowSideItem(false);
	/**
	 * 设置点击事件
	 */
	mAdBanner.setOnAdItemClickListener(new AdBanner.OnAdItemClickListener() {
		 @ Override
		public void onAdItemClickListener(int position) {
			Toast.makeText(MainActivity.this, "click position:" + position, Toast.LENGTH_SHORT).show();
		}
	});

### 数据集？
例如这样的：

        //填充数据 来源于知乎日报api
        List<AdBanner.AdInfo> infos = new ArrayList<>();
        AdBanner.AdInfo info1 = new AdBanner.AdInfo("高学历家长和低学历家长，在教育孩子方面的差别在哪儿？",
                "https://pic4.zhimg.com/v2-ebb209405ac01909c588dca7b8ef0bcf.jpg");
        AdBanner.AdInfo info2 = new AdBanner.AdInfo("四大银行和互联网四巨头「相爱」了，这次会有好结果吗？",
                "https://pic2.zhimg.com/v2-d08943a4b1dc610169d11da3a6dc9f25.jpg");
        AdBanner.AdInfo info3 = new AdBanner.AdInfo("为什么喊麦是老土，Rap 就是潮流了？他们区别在哪？",
                "https://pic4.zhimg.com/v2-424e40eb20637e707ef6b2baaf596d17.jpg");
        AdBanner.AdInfo info4 = new AdBanner.AdInfo("杭州纵火保姆为何是「放火罪」而不是「故意杀人罪」？",
                "https://pic2.zhimg.com/v2-3b72f3e22a29ff5ed38019efb56ee77d.jpg");
        AdBanner.AdInfo info5 = new AdBanner.AdInfo("喊了这么多年的互联网 BAT，接下来大概是 ATM² 了",
                "https://pic1.zhimg.com/v2-9034dd4d806d4a8e75b15b29f0e31570.jpg");

### 如果觉得好用？点个star？
