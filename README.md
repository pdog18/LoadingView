# 半行代码实现LoadingView

## 使用

实现`LoadingView` 接口，

```kotlin
    class MainActivity : Activity(), LoadingView {}
```

然后就可以调用`loadingFinish()`等方法

```kotlin

    fun startLoading() 
    fun loadingFinish() 
    fun loadingEmpty() 
    fun loadingError(exception: RuntimeException) 

```


## 指定contentView
如果没有在xml 中指定某个View为 content 那么会采用默认的布局。
如果指定了，那么指定的优先。

指定方法, 其中 `@string/content`值为 `loading_content`
```xml


    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:contentDescription="@string/content">
        
    </FrameLayout>
```