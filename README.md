# LoadingView

## 使用

实现`LoadingView` 接口，然后就可以调用`loadingFinish()`等方法


```kotlin

    /**
     * 开始加载状态
     */
    fun startLoading() {
       
    }

    /**
     * 关闭加载状态
     */
    fun loadingFinish() {
       
    }

    /**
     * 请求成功，响应数据为空
     */
    fun loadingEmpty() {
        
    }

    /**
     * 加载失败，显示错误界面
     */
    fun loadingError(exception: RuntimeException) {
       
    }

```