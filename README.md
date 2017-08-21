# 是时候来一波Android插件化了

---

[TOC]

<!-- TOC -->

- [是时候来一波Android插件化了](#是时候来一波android插件化了)
    - [前言](#前言)
    - [Android开发演进](#android开发演进)
    - [模块化介绍](#模块化介绍)
    - [插件化介绍](#插件化介绍)
    - [前提技术介绍](#前提技术介绍)
        - [APK构成](#apk构成)
            - [Manifest](#manifest)
            - [Application](#application)
            - [四大组件](#四大组件)
            - [so](#so)
            - [resource](#resource)
            - [安装路径](#安装路径)
        - [App启动流程介绍](#app启动流程介绍)
            - [AIDL](#aidl)
            - [AMS](#ams)
            - [ActivityThread](#activitythread)
        - [插件化技术问题与解决方案](#插件化技术问题与解决方案)
            - [代码加载](#代码加载)
            - [资源获取](#资源获取)
            - [Hook](#hook)
    - [主流框架方案](#主流框架方案)
        - [Fragment加载](#fragment加载)
        - [Activity代理](#activity代理)
        - [Activity占坑](#activity占坑)
    - [360RePlugin介绍](#360replugin介绍)
        - [继承与Demo演示](#继承与demo演示)
        - [原理介绍](#原理介绍)
            - [host lib](#host-lib)
            - [host gradle](#host-gradle)
            - [plugin lib](#plugin-lib)
            - [plugin gradle](#plugin-gradle)
    - [其他插件化方案](#其他插件化方案)
        - [Install App](#install-app)
        - [淘宝atlas](#淘宝atlas)
        - [滴滴VirtualAPK](#滴滴virtualapk)
        - [Small](#small)
    - [总结](#总结)
    - [相关资料](#相关资料)

<!-- /TOC -->

## 前言

今年（2017年）6月时候，有幸参加了在北京举行的GMTC大会，恰巧360的张炅轩大神分享了360的插件化方案—— [RePlugin](https://github.com/Qihoo360/RePlugin) ，当时听了以后，立马有了很大的兴趣。

因为是公司报销费用参加的，回来后是要有技术分享，所以选择了介绍RePlugin以及Android插件化相关内容，本文主要介绍RePlugin以及自己对插件化的理解。

几年前，世面上就已经出现了几款插件化的方案，同时更新技术也是遍地开花。当时是比较抵触这类技术的，个人觉的这样会破坏Android的生态圈，但是毕竟出现了这么多的插件化方案，出现总是有道理的。本着学习的态度，在这边简单介绍下插件化相关的技术点。

## Android开发演进

Android开发初期，基本上是没有框架的，什么东西都往Activity里面塞，最后Activity就变得很大。然后借鉴了Java后端的思想，使用MVC模式，一定程度上解决了代码乱堆的问题，使用了一段时间MVC后，Activity依旧变的很大，因为Activity里面不光有UI的逻辑，还有数据的逻辑。

![MVC](doc/1_mvc.png)

再后来有了MVP，MVP解决了UI逻辑和数据逻辑在一起的问题，同时也解决了Android单元测试的问题。

![MVP](doc/1_mvp.png)

随着业务的增多，我们添加了Domain的概念，Domain从Data中获取数据，Data可能会是Net，File，Cache各种IO等，然后项目架构变成了这样。

![MVP2](doc/2.png)

## 模块化介绍

上面的MVP升级版用了一段时间以后，新问题又出现了。

随着业务的增多，代码变的越来越复杂，每个模块之间的代码耦合变得越来越严重，解耦问题急需解决，同时编译时间也会越来越长。

人员增多，每个业务的组件各自实现一套，导致同一个App的UI风格不一样，技术实现也不一样，团队技术无法得到沉淀。

![Modular](doc/3.png)

然后模块化（组件化）解决方案就出现了。

![Modular2](doc/4.png)

## 插件化介绍

讲道理，模块化已经是最终的解决方案了，为啥还要有插件化呢？

还是得从业务说起，如果一个公司有很多业务，并且每个业务可以汇总成一个大的App，又或者某一个小业务又可以单独做成一个小的App。

按照上面的说的模块化解决方案，需要把这个业务设计成一个模块，代码最终打包成一个aar，主App和业务App设计成一个运行壳子，打包的时候使用Gradle做maven依赖即可。

举例说明美团和猫眼电影。

![美团和猫眼](doc/6.png)

实际上这样做比较麻烦，主App和业务模块会或多或少依赖一点公共代码，如果公共代码出现变动，则需要对应做出修改。同时业务代码会设计成Android Lib project，开发、编译、调试也有点麻烦，那么能不能这样设计，某个业务模块单独做出一个Apk，主App直接使用插件的方式，如果需要某种功能，那么直接加载某一个apk，而不是直接代码的形式。

## 前提技术介绍

通过上面的业务演进，最终我们需要做的就是一个Apk调用另外一个Apk文件，这个就是我们今天需要讲的主题插件化。

一个常识，大家都知道，Apk只有在安装的情况下，才可以被运行，如果一个Apk只是一个文件，放置在存储卡上，我们如何才能调用起来呢？

上面的问题，接下来会做讲解，为了简单快速的了解插件化，需要先回顾一下基础知识。

### APK构成

Apk是App代码最终编译打包生成的文件，主要包含代码（dex、so）、配置文件、资源问题、签名校验等。

![](doc/7.png)

#### Manifest

App中系统组件配置文件，包括Application、Activity、Service、Receiver、Provider等。

App中所有的可运行的Activity必须要在这里定义，否则就不能运行，包括其他组件，Receiver也可以动态注册。（敲黑板，这里很重要，记住这句话。）

#### Application

App启动，代码中可以获取到被运行调用的第一个类，常用来做一些初始化操作。

#### 四大组件

四大系统组件Activity、Service、Receiver、Provider，代码中继承系统中的父类。如上面所说，必须要在manifest中配置定义，否则不可以被调用。

#### so

App中C、C++代码编译生成的二进制文件，与手机的CPU架构相关，不同CPU架构生成的文件有些不同。开发中常常会生成多份文件，然后打包到Apk中，不同CPU类型，会调用不同的文件。

#### resource

Android中资源文件比较多，通常放在res和assets文件夹下面。常见的有布局、图片、字符、样式、主题等。

#### 安装路径

上面的介绍的Apk结构，那么Apk安装以后，它的安装位置在哪，资源和数据又放在哪里呢？

![安装路径](doc/8.png)

`/data/app/{package}/`主要放置apk文件，同时Cpu对于so文件也会被解压到对应的文件夹中，Android高级版本中还会对dex做优化，生产odex文件，也在这个文件夹中。

`data/data/{pcakge}/`主要存放App生产的数据，比如SharedPreferences、cache等其他文件。

那么问题来了，如果调用为安装的Apk，假设能够运行，那么他们的运行文件放在哪里？代码中生成的数据文件又要放在哪里？

### App启动流程介绍

App的二进制文件Apk安装以后，就可以直接启动了，直接点击Launcher上面的图片即可，但是我们需要的是一个App启动另外一个apk文件，所以有必要了解下App的启动流程。

#### IPC & Binder

在Android系统中，每一个应用程序都是由一些Activity和Service组成的，这些Activity和Service有可能运行在同一个进程中，也有可能运行在不同的进程中。那么，不在同一个进程的Activity或者Service是如何通信的呢？

Android系统提供一种Binder机制，能够使进程之间相互通信。

[Android进程间通信资料](http://blog.csdn.net/luoshengyang/article/details/6618363)

![](doc/ipc.gif)

#### AMS

启动流程

http://blog.csdn.net/AllenWells/article/details/68926952

#### ActivityThread

### 插件化技术问题与解决方案

#### 代码加载

按照正常思路，如果一个主Apk需要运行一个插件Apk，那么怎么样才能把里面的代码加载过来呢？

##### Java ClassLoader

Java中提供了ClassLoader方式来加载代码，然后就可以运行其中的代码了。这里有一份资料([深入分析Java ClassLoader原理](http://blog.csdn.net/xyang81/article/details/7292380)) ，可以简单了解下。

- 原理介绍

ClassLoader使用的是双亲委托模型来搜索类的，每个ClassLoader实例都有一个父类加载器的引用（不是继承的关系，是一个包含的关系），虚拟机内置的类加载器（Bootstrap ClassLoader）本身没有父类加载器，但可以用作其它ClassLoader实例的的父类加载器。
当一个ClassLoader实例需要加载某个类时，它会试图亲自搜索某个类之前，先把这个任务委托给它的父类加载器，这个过程是由上至下依次检查的，首先由最顶层的类加载器Bootstrap ClassLoader试图加载，
如果没加载到，则把任务转交给Extension ClassLoader试图加载，如果也没加载到，则转交给App ClassLoader 进行加载，如果它也没有加载得到的话，则返回给委托的发起者，由它到指定的文件系统或网络等URL中加载该类。
如果它们都没有加载到这个类时，则抛出ClassNotFoundException异常。否则将这个找到的类生成一个类的定义，并将它加载到内存当中，最后返回这个类在内存中的Class实例对象。

- 为什么要使用双亲委托这种模型呢？

因为这样可以避免重复加载，当父亲已经加载了该类的时候，就没有必要子ClassLoader再加载一次。
考虑到安全因素，我们试想一下，如果不使用这种委托模式，那我们就可以随时使用自定义的String来动态替代java核心api中定义的类型，这样会存在非常大的安全隐患，而双亲委托的方式，就可以避免这种情况，
因为String已经在启动时就被引导类加载器（Bootstrcp ClassLoader）加载，所以用户自定义的ClassLoader永远也无法加载一个自己写的String，除非你改变JDK中ClassLoader搜索类的默认算法。

- 但是JVM在搜索类的时候，又是如何判定两个class是相同的呢？

JVM在判定两个class是否相同时，不仅要判断两个类名是否相同，而且要判断是否由同一个类加载器实例加载的。
只有两者同时满足的情况下，JVM才认为这两个class是相同的。就算两个class是同一份class字节码，如果被两个不同的ClassLoader实例所加载，JVM也会认为它们是两个不同class。
比如网络上的一个Java类org.classloader.simple.NetClassLoaderSimple，javac编译之后生成字节码文件NetClassLoaderSimple.class，ClassLoaderA和ClassLoaderB这两个类加载器并读取了NetClassLoaderSimple.class文件，
并分别定义出了java.lang.Class实例来表示这个类，对于JVM来说，它们是两个不同的实例对象，但它们确实是同一份字节码文件，如果试图将这个Class实例生成具体的对象进行转换时，
就会抛运行时异常java.lang.ClassCaseException，提示这是两个不同的类型。

##### Android ClassLoader

Android 的 Dalvik/ART 虚拟机如同标准 Java 的 JVM 虚拟机一样，也是同样需要加载 class 文件到内存中来使用，但是在 ClassLoader 的加载细节上会有略微的差别。

[热修复入门：Android 中的 ClassLoader](http://jaeger.itscoder.com/android/2016/08/27/android-classloader.html)比较详细介绍了Android中ClassLoader。

在Android开发者官网上的[ClassLoader](https://developer.android.com/reference/java/lang/ClassLoader.html)的文档说明中我们可以看到，
ClassLoader是个抽象类，其具体实现的子类有 BaseDexClassLoader和SecureClassLoader。

SecureClassLoader的子类是URLClassLoader，其只能用来加载jar文件，这在Android的 Dalvik/ART 上没法使用的。

BaseDexClassLoader的子类是PathClassLoader和DexClassLoader 。

**PathClassLoader**

PathClassLoader 在应用启动时创建，从 data/app/… 安装目录下加载 apk 文件。

其有 2 个构造函数，如下所示，这里遵从之前提到的双亲委托模型：

```java
public PathClassLoader(String dexPath, ClassLoader parent) {
    super(dexPath, null, null, parent);
}

public PathClassLoader(String dexPath, String libraryPath, ClassLoader parent) {
    super(dexPath, null, libraryPath, parent);
}
```

- dexPath : 包含dex的jar文件或apk文件的路径集，多个以文件分隔符分隔，默认是“：”

- libraryPath : 包含 C/C++ 库的路径集，多个同样以文件分隔符分隔，可以为空

PathClassLoader 里面除了这2个构造方法以外就没有其他的代码了，具体的实现都是在 BaseDexClassLoader 里面，其dexPath比较受限制，一般是已经安装应用的 apk 文件路径。

在Android中，App安装到手机后，apk里面的class.dex中的class均是通过PathClassLoader来加载的。

**DexClassLoader**

介绍 DexClassLoader 之前，先来看看其官方描述：

> A class loader that loads classes from .jar and .apk filescontaining a classes.dex entry. This can be used to execute code notinstalled as part of an application.

很明显，对比 PathClassLoader 只能加载已经安装应用的dex或apk文件，DexClassLoader则没有此限制，可以从SD卡上加载包含class.dex的.jar和.apk 文件，这也是插件化和热修复的基础，在不需要安装应用的情况下，完成需要使用的dex的加载。

DexClassLoader 的源码里面只有一个构造方法，这里也是遵从双亲委托模型：

```java
public DexClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
    super(dexPath, new File(optimizedDirectory), libraryPath, parent);
}
```

参数说明：

- String dexPath : 包含 class.dex 的 apk、jar 文件路径 ，多个用文件分隔符(默认是 ：)分隔

- String optimizedDirectory : 用来缓存优化的 dex 文件的路径，即从 apk 或 jar 文件中提取出来的 dex 文件。该路径不可以为空，且应该是应用私有的，有读写权限的路径（实际上也可以使用外部存储空间

- String libraryPath : 存储 C/C++ 库文件的路径集

- ClassLoader parent : 父类加载器，遵从双亲委托模型

#### 资源获取

AssetsManager

Resource

Theme

#### Hook

Hook可以修改函数的调用以及增强系统Api，是常见的一种手段，通常使用代理模式就可以达到Hook目的。

比如有个Java示例代码

```java
public interface IService {

    void fun();
}
public class ServiceImpl implements IService {

    private static final String TAG = "ServiceImpl";

    @Override
    public void fun() {
        Log.i(TAG, "fun: ");
    }
}
```

正常调用直接这样就可以了。

```java
public class MainActivity extends AppCompatActivity {

    private IService iService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iService = new ServiceImpl();
        callService();
    }

    void callService() {
        iService.fun();
    }
}
```

上面代码中MainActivity中含有iService字段，可以利用反射机制来替换它，然后当有其他地方调用iService的时候，就可以对调用方法进拦截和处理。

可以先实现自己的代理类，对需要Hook的地方添加下代码。

```java
public class ServiceProxy implements IService {

    private static final String TAG = "ServiceProxy";

    @NonNull
    private IService base;

    public ServiceProxy(@NonNull IService base) {
        this.base = base;
    }

    @Override
    public void fun() {
        Log.i(TAG, "fun: before");
        base.fun();
        Log.i(TAG, "fun: after");
    }
}
```

然后再修改MainActivity中的iService的值，首先获取iService字段的值，传给自己定义的Proxy对象，然后把Proxy对象再赋值给原先的iService字段，这样调用iService中方法的时候，就会执行Proxy的方法，然后由Proxy再进行处理。

```java
void reflectHock() {
    try {
        Class<? extends MainActivity> aClass = MainActivity.class;
        Field field = aClass.getDeclaredField("iService");
        field.setAccessible(true);
        IService service = (IService) field.get(this);
        IService proxy = new ServiceProxy(service);
        field.set(this, proxy);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

当然有时候，实现自己的Proxy类是很麻烦的，可以利用Java的动态代理技术来搞定。

```java
public class MyInvocationHandler implements InvocationHandler {

    private static final String TAG = "MyInvocationHandler";

    @NonNull
    private IService service;

    public MyInvocationHandler(@NonNull IService service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Log.i(TAG, "invoke: before");
        Object result = method.invoke(service, objects);
        Log.i(TAG, "invoke: after");
        return result;
    }
}

void proxyHook() {
    try {
        Class<? extends MainActivity> aClass = MainActivity.class;
        Field field = aClass.getDeclaredField("iService");
        field.setAccessible(true);
        IService value = (IService) field.get(this);

        InvocationHandler handler = new MyInvocationHandler(value);
        ClassLoader classLoader = value.getClass().getClassLoader();
        Object instance = Proxy.newProxyInstance(classLoader, value.getClass().getInterfaces(), handler);

        field.set(this, instance);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

## 主流框架方案

### Fragment加载

AndroidDynamicLoader

### Activity代理

dynamic-load-apk

### Activity占坑

## 360RePlugin介绍

### 继承与Demo演示

### 原理介绍

#### host lib

#### host gradle

config.json生产

Activity坑位插入

#### plugin lib

#### plugin gradle

修改Activity的父类

## 其他插件化方案

### Install App

### 淘宝atlas

### 滴滴VirtualAPK

### Small

## 总结

## 相关资料

[关于Android模块化我有一些话不知当讲不当讲](https://github.com/LiushuiXiaoxia/AndroidModular)

[Android插件化原理解析——Hook机制之动态代理](http://weishu.me/2016/01/28/understand-plugin-framework-proxy-hook/)

[APK文件结构和安装过程](http://blog.csdn.net/bupt073114/article/details/42298337)

[Android进程间通信资料](http://blog.csdn.net/luoshengyang/article/details/6618363)

AMS

[深入分析Java ClassLoader原理](http://blog.csdn.net/xyang81/article/details/7292380)

[热修复入门：Android中的ClassLoader](http://jaeger.itscoder.com/android/2016/08/27/android-classloader.html)

资源获取

编译过程

[DroidPlugin](https://github.com/Qihoo360/DroidPlugin)

[DynamicAPK](https://github.com/CtripMobile/DynamicAPK)

[AndroidDynamicLoader，利用动态加载Fragment来解决](https://github.com/mmin18/AndroidDynamicLoader)

[dynamic-load-apk，百度任玉刚解决方案，利用Activity做代理](https://github.com/singwhatiwanna/dynamic-load-apk)

[android-pluginmgr](https://github.com/houkx/android-pluginmgr)

[Small](https://github.com/wequick/Small)

[DynamicAPK](https://github.com/CtripMobile/DynamicAPK)

[atlas](https://github.com/alibaba/atlas)

[VirtualAPK](https://github.com/didi/VirtualAPK)

[VirtualAPK介绍](http://geek.csdn.net/news/detail/130917)