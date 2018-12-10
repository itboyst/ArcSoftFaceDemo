<div class="layui-row">
    <div class="layui-col-xs7 layui-col-md-offset3">
        <div style="width: 1000px;height: 800px;border: 1px;background-color: #383939">

            <div style="float:left;height: 80px;width: 100%;line-height: 80px;text-align: center;color: #ffffff;font-size: 40px">
                请选择识别的人脸的照片
            </div>
        <#--<div style="float:right;height: 80px;width: 49%;line-height: 80px;text-align: center;color: #E51C23;font-size: 40px">-->
        <#--相似度：60%-->
        <#--</div>-->
            <a href="#">
                <div id="imageDivComp" style="background-image :url(images/v2_pgfcmk.png) ;  background-repeat: no-repeat;background-position: center center;background-size: cover;      /* 覆盖:图片成比例填满盒子。可用于适配 */
			background-size: contain; float:left;height: 500px;width: 100%;border: 1px solid #877dd6">
                </div>
            <#--</a>-->
        <#--<div style="background-image :url(images/v2_pgfcnq.jpg);  background-repeat: no-repeat;background-position: center center;float:right;height: 500px;width: 49%;border: 2px solid #877dd6">-->
        <#--</div>-->
            <input type="file" name="file" id="chooseFileComp" onchange="chooseFileChangeComp()" style="display:none;"/>
            <div style="float:left;height: 40px;width: 100%;line-height: 40px;text-align: center;color: #ffffff;font-size: 30px">
                <div  id="nameDiv"></div>
                <div id="similarDiv"></div>
                <div id="ageDiv"></div>
                <div id="genderDiv"></div>
            </div>


        </div>


    </div>


</div>

</div>
<script>


    $("#imageDivComp").click(function () {
        $("#chooseFileComp").click();


    });

    function chooseFileChangeComp() {
        var file = $("#chooseFileComp")[0].files[0];
        if (file == undefined) {
            return
        }
        var img=$("#imageDivComp");
        img.css("background-image", 'url(' + URL.createObjectURL(file) + ')');

        var formData = new FormData();

        formData.append("groupId", "101")

        if (file == undefined) {
            alert("请先选择有人脸的照片")
            return;
        }
        formData.append("file", file);

        $.ajax({
            type: "post",
            url: "/faceSearch",
            data: formData,
            contentType: false,
            processData: false,
            async: false,
            success: function (text) {
                var res = JSON.stringify(text)
                if (text.code == 0) {
                    var name = text.data.name;
                    $("#nameDiv").html("姓名：" + name);
                    var similar = text.data.similarValue;
                    $("#similarDiv").html("相似度：" + similar + "%");
                    var age = text.data.age;
                    $("#ageDiv").html("年龄：" + age);
                    var gender = text.data.gender;
                    $("#genderDiv").html("性别：" + gender);
                    img.css("background-image", 'url(' + text.data.image + ')');
                } else {
                    $("#nameDiv").html("");
                    $("#similarDiv").html("");
                    $("#ageDiv").html("");
                    $("#genderDiv").html("");
                    alert("人脸不匹配")
                }

            },
            error: function (error) {
                $("#nameDiv").html("");
                $("#similarDiv").html("");
                $("#ageDiv").html("");
                $("#genderDiv").html("");
                alert(JSON.stringify(error))
            }


        });


    }


</script>
