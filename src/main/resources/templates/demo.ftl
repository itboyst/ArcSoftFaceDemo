<!DOCTYPE html >
<html>
<head>
    <meta charset="utf-8">
    <title>人脸识别系统</title>

    <link rel="stylesheet" href="layui/css/layui.css">

    <script src="jquery/jquery-3.3.1.min.js"></script>
    <script src="/layui/layui.js"></script>

</head>
<body>
<div class="layui-tab">
    <ul class="layui-tab-title">
        <li class="layui-this">人脸注册</li>
        <li>人脸搜索</li>
    </ul>
  <div style="position: absolute;margin-left: 40px" >
      <style type="text/css">
          table.hovertable {
              font-family: verdana,arial,sans-serif;
              font-size:11px;
              color:#333333;
              border-width: 1px;
              border-color: #999999;
              border-collapse: collapse;
          }
          table.hovertable th {
              background-color:#c3dde0;
              border-width: 1px;
              padding: 8px;
              border-style: solid;
              border-color: #a9c6c9;
          }
          table.hovertable tr {
              background-color:#d4e3e5;
          }
          table.hovertable td {
              border-width: 1px;
              padding: 8px;
              border-style: solid;
              border-color: #a9c6c9;
          }
      </style>

      <table class="hovertable" id="userTable">
          <tr id="firstTr">
              <th>用户姓名</th><th>注册时间</th>
          </tr>

      </table>
  </div>
    <div class="layui-tab-content" style="">
        <div class="layui-tab-item layui-show"><#include "face_registration.ftl"></div>
        <div class="layui-tab-item"><#include "face_search.ftl"></div>

    </div>
</div>

<script>
    //注意：选项卡 依赖 element 模块，否则无法进行功能性操作
    layui.use('element', function(){
        var element = layui.element;

    });
    $(function () {
       f();
    })
    function f() {
        $.ajax({
            type:"get",
            url:"/userInfo",
            success:function (userList) {
                if(userList.length == 0)
                {
                    let addElement = "<h2>暂无用户</h2>";
                    $("#userTable").append(addElement);
                }
                for (let i = 0; i < userList.length; i++) {
                    let userName = userList[i].name;
                    let createTime = userList[i].createTime.toString().slice(0,10);
                    let addElement = "<tr onmouseover='this.style.backgroundColor='#ffff66';' onmouseout='this.style.backgroundColor='#d4e3e5';'><td>"+userName+"</td><td>"+createTime+"</td></tr>";
                    $("#userTable").append(addElement);
                }
            },
            dataType:"json",
            error: function (error) {
                alert(JSON.stringify(error))
            }
        });
    }

</script>
</html>