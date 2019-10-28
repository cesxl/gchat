<#include "./include/global.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>Login-登录</title>
    <meta name="description" content="User login page" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${ctxStatic}/ace/assets/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${ctxStatic}/ace/assets/font-awesome/4.5.0/css/font-awesome.min.css" />
    <!-- text fonts -->
    <link rel="stylesheet" href="${ctxStatic}/ace/assets/css/fonts.googleapis.com.css" />
    <!-- ace styles -->
    <link rel="stylesheet" href="${ctxStatic}/ace/assets/css/ace.min.css" />
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${ctxStatic}/ace/assets/css/ace-part2.min.css" />
    <![endif]-->
    <link rel="stylesheet" href="${ctxStatic}/ace/assets/css/ace-rtl.min.css" />
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${ctxStatic}/ace/assets/css/ace-ie.min.css" />
    <![endif]-->
    <link rel="stylesheet" href="${ctxStatic}/bootstrap/bootstrap-validator/css/bootstrap-validator.css" />
    <style>
        .login-container {
            width: 375px;
            margin: 0 auto;
            margin-top: 80px;
        }
        .alert {
            color: red;
            padding: 0px !important;
            border: 1px solid transparent;
            border-radius: 4px;
            margin-bottom: 0px !important;
        }
    </style>
</head>
<body class="login-layout">
<div class="main-container">
    <div class="main-content">
        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div class="login-container">
                    <div class="center">
                        <h1> <span class="white" id="id-text2">${Global.getConfig('productName')}</span> </h1>
                    </div>
                    <div class="space-6"></div>
                    <div class="position-relative">
                        <div id="login-box" class="login-box visible widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header blue lighter bigger"> <i class="ace-icon green glyphicon glyphicon-user"></i> 请输入账户名/密码 </h4>
                                    <div class="space-6"></div>
                                    <form id="loginForm" action="${ctx}/login" method="post">
                                        <div id="messageBox" class="alert alert-error ${message!'hide'}"><button data-dismiss="alert" class="close">×</button>
                                            <label id="loginError" class="error">${message!}</label>
                                        </div>
                                        <fieldset>
                                            <label class="block clearfix"> <span class="block input-icon input-icon-right">
                                                    <input type="text" name="username" class="form-control" placeholder="Username" />
                                                    <i class="ace-icon fa fa-user"></i> </span>
                                            </label>
                                            <label class="block clearfix"> <span class="block input-icon input-icon-right">
                                                    <input type="password" name="password" class="form-control" placeholder="Password" />
                                                    <i class="ace-icon fa fa-lock"></i> </span>
                                            </label>
                                            <div class="space"></div>
                                            <div class="clearfix">
                                                <button type="submit" class="width-35 pull-right btn btn-sm btn-primary"> <i class="ace-icon fa fa-key"></i> <span class="bigger-110">登陆</span> </button>
                                            </div>
                                            <div class="space-4"></div>
                                        </fieldset>
                                    </form>
                                    <div class="social-or-login center">
                                        <span class="bigger-110">第三方登录</span>
                                    </div>
                                    <div class="space-6"></div>
                                    <div class="social-login center">
                                        <a class="btn btn-primary"> <i class="ace-icon fa fa-qq"></i> </a>
                                        <a class="btn btn-info"> <i class="ace-icon fa fa-wechat"></i> </a>
                                        <a class="btn btn-danger"> <i class="ace-icon fa fa-weibo"></i> </a>
                                    </div>
                                </div>
                                <!-- /.widget-main -->
                                <div class="toolbar clearfix">
                                    <div>
                                        <a href="#" data-target="#forgot-box" class="forgot-password-link"> <i class="ace-icon fa fa-arrow-left"></i> 忘记密码 </a>
                                    </div>
                                    <div>
                                        <a href="#" data-target="#signup-box" class="user-signup-link"> 注册 <i class="ace-icon fa fa-arrow-right"></i> </a>
                                    </div>
                                </div>
                            </div>
                            <!-- /.widget-body -->
                        </div>
                        <!-- /.login-box -->
                        <div id="forgot-box" class="forgot-box widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header red lighter bigger"> <i class="ace-icon fa fa-key"></i> 重置密码 </h4>
                                    <div class="space-6"></div>
                                    <p> 输入您的电子邮件 </p>
                                    <form>
                                        <fieldset>
                                            <label class="block clearfix"> <span class="block input-icon input-icon-right"> <input type="email" class="form-control" placeholder="Email" /> <i class="ace-icon fa fa-envelope"></i> </span> </label>
                                            <div class="clearfix">
                                                <button type="button" class="width-35 pull-right btn btn-sm btn-danger"> <i class="ace-icon fa fa-lightbulb-o"></i> <span class="bigger-110">发送!</span> </button>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>
                                <!-- /.widget-main -->
                                <div class="toolbar center">
                                    <a href="#" data-target="#login-box" class="back-to-login-link"> 返回登陆 <i class="ace-icon fa fa-arrow-right"></i> </a>
                                </div>
                            </div>
                            <!-- /.widget-body -->
                        </div>
                        <!-- /.forgot-box -->
                        <div id="signup-box" class="signup-box widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header green lighter bigger"> <i class="ace-icon fa fa-users blue"></i> 新用户注册 </h4>
                                    <div class="space-6"></div>
                                    <p> 输入详细信息: </p>
                                    <form>
                                        <fieldset>
                                            <label class="block clearfix"> <span class="block input-icon input-icon-right"> <input type="email" class="form-control" placeholder="邮箱" /> <i class="ace-icon fa fa-envelope"></i> </span> </label>
                                            <label class="block clearfix"> <span class="block input-icon input-icon-right"> <input type="text" class="form-control" placeholder="用户名" /> <i class="ace-icon fa fa-user"></i> </span> </label>
                                            <label class="block clearfix"> <span class="block input-icon input-icon-right"> <input type="password" class="form-control" placeholder="密码" /> <i class="ace-icon fa fa-lock"></i> </span> </label>
                                            <label class="block clearfix"> <span class="block input-icon input-icon-right"> <input type="password" class="form-control" placeholder="确认密码" /> <i class="ace-icon fa fa-retweet"></i> </span> </label>
                                            <label class="block"> <input type="checkbox" class="ace" /> <span class="lbl"> 我同意 <a href="#">用户协议</a> </span> </label>
                                            <div class="space-24"></div>
                                            <div class="clearfix">
                                                <button type="reset" class="width-30 pull-left btn btn-sm"> <i class="ace-icon fa fa-refresh"></i> <span class="bigger-110">重置</span> </button>
                                                <button type="button" class="width-65 pull-right btn btn-sm btn-success"> <span class="bigger-110">注册</span> <i class="ace-icon fa fa-arrow-right icon-on-right"></i> </button>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>
                                <div class="toolbar center">
                                    <a href="#" data-target="#login-box" class="back-to-login-link"> <i class="ace-icon fa fa-arrow-left"></i> 返回登陆 </a>
                                </div>
                            </div>
                            <!-- /.widget-body -->
                        </div>
                        <!-- /.signup-box -->
                    </div>
                </div>
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </div>
    <!-- /.main-content -->
</div>
<!-- /.main-container -->
<!-- basic scripts -->
<!--[if !IE]> -->
<script src="${ctxStatic}/ace/assets/js/jquery-2.1.4.min.js"></script>
<!-- <![endif]-->
<!--[if IE]>
<script src="${ctxStatic}/ace/assets/js/jquery-1.11.3.min.js"></script>
<![endif]-->
<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
<!--[if lte IE 8]>
<script src="${ctxStatic}/ace/assets/js/html5shiv.min.js"></script>
<script src="${ctxStatic}/ace/assets/js/respond.min.js"></script>
<![endif]-->
<script src="${ctxStatic}/ace/assets/js/bootstrap.min.js"></script>
<script src="${ctxStatic}/bootstrap/bootstrap-validator/js/bootstrap-validator.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
    jQuery(function($) {
        //session失效  iframe嵌套问题
        if (window.top != null && window.top.document.URL != document.URL) {
            window.top.location = document.URL;
        }

        $(document).on('click', '.toolbar a[data-target]', function(e) {
            e.preventDefault();
            var target = $(this).data('target');
            $('.widget-box.visible').removeClass('visible');//hide others
            $(target).addClass('visible');//show target
        });
    });

    //you don't need this, just used for changing background
    jQuery(function($) {
        $('body').attr('class', 'login-layout light-login');
        $('#id-text2').attr('class', 'grey');
        $('#id-company-text').attr('class', 'blue');
    });

    $(function () {
        $("#loginForm").bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            submitHandler: function (validator, form, submitButton) {
                validator.defaultSubmit();
            },
            fields: {
                username: {
                    validators: {
                        notEmpty : {message : '请输入用户名'}
                    }
                },
                password: {
                    validators: {
                        notEmpty : {message : '请输入密码'}
                    }
                }
            }
        });
    })
</script>
</body>
</html>