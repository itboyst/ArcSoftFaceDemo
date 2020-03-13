<#macro commonStyle>
<#--    ${request.contextPath}-->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <link rel="stylesheet" href="/plugins/bootstrap-4.4.1-dist/css/bootstrap-grid.css">
    <link rel="stylesheet" href="/plugins/bootstrap-4.4.1-dist/css/bootstrap-reboot.css">
    <link rel="stylesheet" href="/plugins/fontawesome-free/css/all.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="/plugins/admin-lte-3.0.2/dist/css/adminlte.min.css">

</#macro>

<#macro commonScript>
    <!-- jQuery -->
    <script src="plugins/jquery/jquery.min.js"></script>
    <!-- jQuery UI 1.11.4 -->
    <script src="plugins/jquery-ui/jquery-ui.min.js"></script>


    <!-- Bootstrap 4 -->
<#--    <script src="plugins/bootstrap-4.4.1-dist/js/bootstrap.js"></script>-->
    <script src="plugins/bootstrap-4.4.1-dist/js/bootstrap.bundle.js"></script>
    <script src="plugins/popper/popper.js"></script>
    <!-- AdminLTE App -->
    <script src="plugins/admin-lte-3.0.2/dist/js/adminlte.js"></script>
</#macro>
<#macro commonSidebar pageName >

    <aside class="main-sidebar sidebar-dark-primary elevation-4">
        <!-- Brand Logo -->
        <a href="#" class="brand-link">
            <span class="brand-text font-weight-light">人脸识别Demo</span>
        </a>

        <!-- Sidebar -->
        <div class="sidebar">
            <!-- Sidebar Menu -->
            <nav class="mt-2">
                <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu"
                    data-accordion="false">
                    <li class="nav-item" >
                        <a href="/faceDetect" class="nav-link  <#if pageName == "人脸检测">active</#if>">
                        <i class="nav-icon far fa-circle text-yellow"></i>
                        <p class="text">人脸检测</p>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/faceSimilarity" class="nav-link <#if pageName == "人脸相似度">active</#if>">
                        <i class="nav-icon far fa-circle text-danger"></i>
                        <p class="text">人脸相似度</p>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="/faceRecognition" class="nav-link <#if pageName == "人脸识别">active</#if>">
                            <i class="nav-icon far fa-circle text-aqua"></i>
                            <p class="text">人脸识别</p>
                        </a>
                    </li>
                </ul>
            </nav>
            <!-- /.sidebar-menu -->
        </div>
        <!-- /.sidebar -->
    </aside>

</#macro>


