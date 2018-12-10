<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>人脸识别系统</title>
    <#--<link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.min.css">-->

    <link rel="stylesheet" href="layui/css/layui.css">

    <script src="jquery/jquery-3.3.1.min.js"></script>
    <#--<script src="bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>-->
    <script src="/layui/layui.js"></script>

</head>
<body>
<div class="layui-tab">
    <ul class="layui-tab-title">
        <li class="layui-this">人脸注册</li>
        <li>人脸搜索</li>

    </ul>
    <div class="layui-tab-content">
        <div class="layui-tab-item layui-show"><#include "face_registration.ftl"></div>
        <div class="layui-tab-item"><#include "face_search.ftl"></div>

    </div>

</div>

<script>
    //注意：选项卡 依赖 element 模块，否则无法进行功能性操作
    layui.use('element', function(){
        var element = layui.element;

    });



</script>
</html>