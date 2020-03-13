<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>人脸检测</title>
    <#import "common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
</head>

</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
    <!-- Main Sidebar Container -->
    <@netCommon.commonSidebar "人脸相似度" />
    <div class="content-wrapper">
        <h4 align="center" style="margin-top: 30px">
            人脸相似度示例
        </h4>

        <div class="imageSelect">
            <div class="imageDiv">
                <#--            <canvas id="canvas" height="438" width="780"></canvas>-->
                <img id="img1" height="200" width="auto" style="background-color: #7abaff"/>
            </div>

            <div class="dropdown selectDiv">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" data-image="image1">
                    Image1-1.jpg
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <a class="dropdown-item" href="#">Image1-1.jpg</a>
                    <a class="dropdown-item" href="#">Image2-1.jpg</a>
                    <a class="dropdown-item" href="#">Image3-1.jpg</a>
                </div>
            </div>
        </div>

        <div class="imageSelect">
            <div class="imageDiv">
                <#--            <canvas id="canvas" height="438" width="780"></canvas>-->
                <img id="img2" height="200" width="auto" style="background-color: #7abaff"/>
            </div>

            <div class="dropdown selectDiv">
                <button class="btn btn-secondary dropdown-toggle" type="button"
                        data-toggle="dropdown" data-image="image2">
                    Image1-2.jpg
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <a class="dropdown-item" href="#">Image1-2.jpg</a>
                    <a class="dropdown-item" href="#">Image2-2.jpg</a>
                    <a class="dropdown-item" href="#">Image3-2.jpg</a>
                </div>
            </div>
        </div>


        <h4  align="center" id="similarId" style="margin-top: 30px"></h4>

    </div>

</div>

<@netCommon.commonScript />

<script>


    let img1 = document.getElementById('img1');
    let img2 = document.getElementById('img2');

    img1.src = 'images/Image1-1.jpg'
    img2.src = 'images/Image1-2.jpg'
    let changeCount = 0

    img1.onload = function () {
        imageChange();
    }
    img2.onload = function () {
        imageChange();
    }

    $(".dropdown").on('hidden.bs.dropdown', function (e) {
        let content = e.clickEvent.target.textContent;
        if (content.substring(0, 5) == 'Image') {
            e.relatedTarget.textContent = content;
            if (e.relatedTarget.dataset.image == 'image1') {
                img1.src = 'images/' + content
            } else if (e.relatedTarget.dataset.image == 'image2') {
                img2.src = 'images/' + content
            }
        }
    });

    function imageChange() {
        changeCount++;
        if (changeCount >= 2) {
            let canvas1 = document.createElement('canvas');
            canvas1.width = img1.width;
            canvas1.height = img1.height;
            let ctx1 = canvas1.getContext("2d");
            ctx1.drawImage(img1, 0, 0, img1.width, img1.height)

            let canvas2 = document.createElement('canvas');
            canvas2.width = img2.width;
            canvas2.height = img2.height;
            let ctx2 = canvas2.getContext("2d");
            ctx2.drawImage(img2, 0, 0, img2.width, img2.height)

            let data = {
                image1: canvas1.toDataURL("image/png"),
                image2: canvas2.toDataURL("image/png")
            }

            $.post("/compareFaces", data, function (result) {
                $('#similarId').text('人脸相似度为: '+result.data)
            });
        }
    }


</script>

<style>

    .content-wrapper {
        width: 650px;
        background-color: #ffffff;
    }

    .imageSelect {
        display: inline-block;
    }


    .imageDiv {
        width: 300px;
        height: 202px;
        margin-left: 20px;
        margin-top: 20px;
        margin-bottom: 5px;
        /*background-color: #1aa67d;*/
        display: inline-flex;
        align-items: center;
        justify-content: center;
        /*为了效果明显，可以将如下边框打开，看一下效果*/
        border: 1px solid black;
    }

    .selectDiv {
        width: 300px;
        margin-left: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        /*border: 1px solid black;*/
    }

</style>

</body>
</html>
