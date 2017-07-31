# 是时候来一波Android插件化了

---

[TOC]

## 前言

今年（2017年）6月时候，有幸参加了北京的GMTC大会，刚刚好360的张炅轩大神分享了360的插件化方案—— [RePlugin](https://github.com/Qihoo360/RePlugin) ，当时在台下听了以后，立马对插件表示了很大的兴趣。

因为是公司报销费用参加的，回来以后是要有技术分享，所以我选择了介绍RePlugin以及Android插件化相关内容，本文主要介绍RePlugin以及自己对插件化的理解。

几年前，世面上已经出现了几款插件化的方案，同时更新技术也是遍地开花，当时是比较抵触这类技术的，个人觉的这样会破坏Android的生态圈，但是毕竟出现了这么多的插件化方案，本着学习的态度，在这边简单介绍下插件化相关的技术点。

## Android开发演进

## 组建化介绍

## 插件化介绍和使用场景

## 前提技术介绍

### APK构成

#### Manifest

#### Application

#### 四大组件

#### so

#### resource

### App启动流程介绍

#### ActivityManagerService

#### ActivityThread

### 插件化技术问题

#### 代码加载

ClassLoader

#### 资源获取

AssetsManager

Resource

Theme

### 技术问题解决方案

#### ClassLoader

双亲委托

基础知识

BootClassLoader

PathClassLoader

DexClassLoader

#### Hook

反射

代理

实现Hook


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

Hook

IPC

AMS

ClassLoader

资源获取

编译过程

APK结构

[DroidPlugin](https://github.com/Qihoo360/DroidPlugin)

[DynamicAPK](https://github.com/CtripMobile/DynamicAPK)

[AndroidDynamicLoader](https://github.com/mmin18/AndroidDynamicLoader) 利用动态加载Fragment来解决

[dynamic-load-apk](https://github.com/singwhatiwanna/dynamic-load-apk) 百度任玉刚解决方案，利用Activity做代理

[android-pluginmgr](https://github.com/houkx/android-pluginmgr)

[Small](https://github.com/wequick/Small)

[DynamicAPK](https://github.com/CtripMobile/DynamicAPK)

[atlas](https://github.com/alibaba/atlas)

[VirtualAPK](https://github.com/didi/VirtualAPK)

[VirtualAPK介绍](http://geek.csdn.net/news/detail/130917)