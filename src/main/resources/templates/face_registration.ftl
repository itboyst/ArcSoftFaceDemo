<div class="layui-row">
    <div class="layui-col-xs7 layui-col-md-offset3">
        <div style="width: 1024px;height: 80px;background-color: #383939">
            <div style="margin-left: 100px;height: 80px;text-align:left;line-height:80px;font-size: 40px;color: #E51C23">
                人脸识别系统
            </div>
        </div>
        <div style="width: 1024px;height: 80px;background-color: #101010">
            <div style="display:inline;margin-left: 20px;height: 80px;text-align:left;line-height:80px;font-size: 36px;color: #E51C23">
                基础资料
            </div>
            <#--<div style="display:inline;margin-left: 116px;height: 80px;text-align:left;line-height:80px;font-size: 36px;color: #E51C23">-->
                <#--注册-->
            <#--</div>-->
        </div>

        <div style="width: 1024px;height:600px;background-color: #383939">

            <a href="#">
                <div id="imageDiv" class="layui-col-xs8"
                     style="width: 600px;height: 550px;background-image :url(/images/v2_pgf70f.png);  background-repeat: no-repeat;background-position: center center;background-size: cover;      /* 覆盖:图片成比例填满盒子。可用于适配 */
			background-size: contain;">
                </div>
            </a>

            <div class="layui-col-xs4" style="height: 824px;margin-left: 0px" id="registerDiv">
                <div style="color: #FFFFFF;font-size: 30px;margin-left: 50px;margin-top: 29px;text-align: left">
                    身份信息
                </div>

                <form class="form-horizontal form" role="from">

                    <div style="color:#FFFFFF;height: 50px;margin-top:20px;margin-left:20px;margin-right:20px;background-color: #5A5B5B;border-radius:5px">
                        <label style="margin-left: 20px;height: 50px;line-height:50px;font-size: 21px;text-align: right;float: left">姓名：</label>

                        <input style="width:180px;margin-left: auto;height: 50px;font-size: 21px;background-color: #5A5B5B;color:#FFFFFF;border: 0px;"
                               type="text" name="name">
                    </div>


                    <input type="file" name="file" id="chooseFile" onchange="chooseFileChange()" style="display:none;"/>

                    <button class="submitInp" type="submit"
                            style="color:#FFFFFF;height: 30px;display:block;margin:0 auto;margin-top:100px;width:100px;background-color: #3F51B5;border-radius:5px;text-align: center;line-height: 30px;font-size: 20px">
                        注册
                    </button>
                </form>

            </div>


        </div>


    </div>

</div>

<script>

    $("#imageDiv").click(function () {
        $("#chooseFile").click();


    });

    function chooseFileChange() {
        $("#imageDiv").css("background-image", 'url(' + URL.createObjectURL($("#chooseFile")[0].files[0]) + ')');
    }


    $('.submitInp').click(function () {

        var formData = new FormData();
        var a = $('#registerDiv .form').serializeArray();

        for (var item in a) {
            formData.append(a[item].name, a[item].value)
        }

        formData.append("groupId", "101")

        if (formData.get("name") == "") {
            alert("请输入名字")
            return;
        }

        if (formData.get("groupId") == "") {
            alert("请输入库Id")
            return;
        }


        var file = $("#chooseFile")[0].files[0];
        if (file == undefined) {
            alert("请先选择有人脸的照片")
            return;
        }
        formData.append("file", file);


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


    });


</script>