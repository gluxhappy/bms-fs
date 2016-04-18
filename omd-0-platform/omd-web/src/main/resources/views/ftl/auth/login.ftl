<!DOCTYPE html>
<html>
<head>
<title>登陆</title>
<link href="/res/bootstrap/bootstrap.min.css" rel="stylesheet" />
<script src="/res/js/jquery/jquery.min.js"></script>
<script src="/res/js/jcall.js"></script>
<script src="/res/bootstrap/bootstrap.min.js"></script>
</head>
<body>


<form class="form-horizontal" role="form" method="POST" action="/auth/login.do" style="margin-top:20%;margin-left:30%;width:40em">
   <div class="form-group">
      <label for="username" class="col-sm-2 control-label">Username</label>
      <div class="col-sm-10">
         <input type="text" class="form-control" id="username" name="username"
            placeholder="请输入用户名">
      </div>
   </div>
   <div class="form-group">
      <label for="password" class="col-sm-2 control-label">Password</label>
      <div class="col-sm-10">
         <input type="password" class="form-control" id="password" name="password" 
            placeholder="请输入密码">
      </div>
   </div>
   <!-- 
   <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
         <div class="checkbox">
            <label>
               <input type="checkbox" name="_spring_security_remember_me" checked="true"> 请记住我
            </label>
         </div>
      </div>
   </div>
    -->
   <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
         <button type="submit" class="btn btn-default">登录</button>
      </div>
      
    <#if error> 
      <h4 class="text-danger text-center" style="width:100%">密码错误</h4>
    </#if> 
   </div>
</form>
</body>
</html>
