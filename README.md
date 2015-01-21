android-KRScrollView
=================

No use viewPager present photos and scroll page by page, just directly extends HorizontalScrollView and overrides.

In currently that only implemented HorizontalScrollView and easy swipe page by page, in the future will implement more features when I'm free.

``` java
import pers.kalvar.tools.scrollview.KRScrollView;
```

Then setup xml likes as below :

``` java
<pers.kalvar.tools.scrollview.KRScrollView
        android:layout_width="300dp"
        android:layout_height="400dp"
        android:id="@+id/horizontalScrollView"
        android:layout_centerVertical="true"
        android:layout_centerHorizontal="true" >

        <LinearLayout
            android:orientation="horizontal"
            android:layout_width="fill_parent"
            android:layout_height="fill_parent">

            <ImageView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/imageView"
                android:src="@drawable/p0" />

            <ImageView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/imageView2"
                android:src="@drawable/p1" />

            <ImageView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/imageView3"
                android:src="@drawable/p2" />

        </LinearLayout>
    </pers.kalvar.tools.scrollview.KRScrollView>
```

## Version

V0.8 beta

## License

MIT.
