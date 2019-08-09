<div class="layui-row">
    <div class="layui-col-xs7 layui-col-md-offset3" align="center">
        <div style="margin: 0,auto; width: 800px;height: 80px;background-color: #383939">
            <div style="margin: 0,auto;height: 80px;text-align:center;line-height:80px;font-size: 40px;color: #E51C23">
                人脸识别系统
            </div>
        </div>
        <div style="color:#FFFFFF;height: 50px;margin-top:20px;margin-left:20px;margin-right:20px;background-color: #5A5B5B;border-radius:5px">
            <label style="margin-left: 180px;height: 50px;line-height:50px;font-size: 21px;text-align: right;float: left">姓名：</label>

            <input style="width:300px;margin-left: 0px;height: 50px;font-size: 21px;background-color: #5a5b5b;color:#FFFFFF;border: 0px;"
                  placeholder="在此输入姓名" type="text" name="userName" id="userName">
        </div>

        <div id="mainDiv">

        </div>
        <div>
            <table frame="void" >
                <tr>
                    <td><button title="摄像头注册" value="摄像头注册" onclick="getMedia()" style="color:#FFFFFF;height: 30px;display:block;margin:0 auto;margin-top:10px;width:120px;background-color: #3F51B5;border-radius:5px;text-align: center;line-height: 30px;font-size: 20px" >摄像头注册</button></td>
                    <td><button style="color:#FFFFFF;height: 30px;display:block;margin:0 auto;margin-top:10px;width:120px;background-color: #3F51B5;border-radius:5px;text-align: center;line-height: 30px;font-size: 20px" onclick="imageRecog()">照片注册</button></td>
                </tr>
                <#--<td><button id="snap" onclick="commitPhoto()" style="color:#FFFFFF;height: 30px;display:block;margin:0 auto;margin-top:10px;width:100px;background-color: #3F51B5;border-radius:5px;text-align: center;line-height: 30px;font-size: 20px">照片提交</button></td>-->
                <tr><td colspan="2"><button id="snap" onclick="takePhoto()"  style="color:#FFFFFF;height: 30px;display:block;margin:0 auto;margin-top:10px;width:100px;background-color: #3F51B5;border-radius:5px;text-align: center;line-height: 30px;font-size: 20px">提交</button></td></tr>
            </table>
        </div>
        <div style="float: right">

        </div>
    </div>


</div>

<script>


    function getMedia() {
        $("#mainDiv").empty();
        let videoComp = " <video id='video' width='500px' height='500px' autoplay='autoplay' style='margin-top: 20px'></video><canvas id='canvas' width='500px' height='500px' style='display: none'></canvas>";
        $("#mainDiv").append(videoComp);

        let constraints = {
            video: {width: 500, height: 500},
            audio: true
        };
        //获得video摄像头区域
        let video = document.getElementById("video");
        //这里介绍新的方法，返回一个 Promise对象
        // 这个Promise对象返回成功后的回调函数带一个 MediaStream 对象作为其参数
        // then()是Promise对象里的方法
        // then()方法是异步执行，当then()前的方法执行完后再执行then()内部的程序
        // 避免数据没有获取到
        let promise = navigator.mediaDevices.getUserMedia(constraints);
        promise.then(function (MediaStream) {
            video.srcObject = MediaStream;
            video.play();
        });

        // var t1 = window.setTimeout(function() {
        //     takePhoto();
        // },2000)
    }
    //拍照事件
    function takePhoto() {
        let mainComp = $("#mainDiv");
        if(mainComp.has('video').length)
        {
            let userNameInput = $("#userName").val();
            if(userNameInput == "")
            {
                alert("姓名不能为空!");
                return false;
            }
            //获得Canvas对象
            let video = document.getElementById("video");
            let canvas = document.getElementById("canvas");
            let ctx = canvas.getContext('2d');
            ctx.drawImage(video, 0, 0, 500, 500);
            var formData = new FormData();
            var base64File = canvas.toDataURL();
            var userName = $("#userName").val();
            formData.append("file", base64File);
            formData.append("name", userName);
            formData.append("groupId", "101");
            $.ajax({
                type: "post",
                url: "/faceAdd",
                data: formData,
                contentType: false,
                processData: false,
                async: false,
                success: function (text) {
                    var res = JSON.stringify(text)
                    if (text.code == 0) {
                        alert("注册成功")
                    } else {
                        alert(text.message)
                    }
                },
                error: function (error) {
                    alert(JSON.stringify(error))
                }
            });
        }
        else{
            var formData = new FormData();
            let userName = $("#userName").val();
            formData.append("groupId", "101");
            var file = $("#file0")[0].files[0];
            var reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function () {
            var base64 = reader.result;
            formData.append("file", base64);
            formData.append("name",userName);
                $.ajax({
                    type: "post",
                    url: "/faceAdd",
                    data: formData,
                    contentType: false,
                    processData: false,
                    async: false,
                    success: function (text) {
                        var res = JSON.stringify(text)
                        if (text.code == 0) {
                            alert("注册成功")
                        } else {
                            alert(text.message)
                        }
                    },
                    error: function (error) {
                        alert(JSON.stringify(error))
                    }
                });
                location.reload();
            }
        }

    }

    function imageRecog() {
        let imageInput = " <h2>点击图片区域上传文件</h2><input style='display: none' type='file'  name='file0' id='file0' multiple='multiple' /><br><img src='images/shibie.jpg' id='img0' onclick='toChooseFile()' style='width: 30rem;height: 25rem;'>";
        $("#mainDiv").empty();
        $("#mainDiv").append(imageInput);
    }

    function toChooseFile()
    {
        $("#file0").trigger('click');
    }
    $(document).on("change","#file0",function(){
        var objUrl = getObjectURL(this.files[0]) ;//获取文件信息
        console.log("objUrl = "+objUrl);
        if (objUrl) {
            $("#img0").attr("src", objUrl);
        }
    });

    function getObjectURL(file) {
        var url = null;
        if (window.createObjectURL!=undefined) {
            url = window.createObjectURL(file) ;
        } else if (window.URL!=undefined) { // mozilla(firefox)
            url = window.URL.createObjectURL(file) ;
        } else if (window.webkitURL!=undefined) { // webkit or chrome
            url = window.webkitURL.createObjectURL(file) ;
        }
        return url ;
    }


</script>
