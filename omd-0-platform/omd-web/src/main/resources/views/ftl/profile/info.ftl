<!DOCTYPE html>
<html>
<head>
<title>Profile/Info</title>
</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
	   <div class="navbar-header">
	      <button type="button" class="navbar-toggle" data-toggle="collapse" 
	         data-target="#example-navbar-collapse">
	         <span class="sr-only">切换导航</span>
	         <span class="icon-bar"></span>
	         <span class="icon-bar"></span>
	         <span class="icon-bar"></span>
	      </button>
	      <a class="navbar-brand" href="#">Welcome ${name}!</a>
	   </div>
	   <div class="collapse navbar-collapse" id="example-navbar-collapse">
	      <ul class="nav navbar-nav">
	         <li class="dropdown">
	            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Redis <b class="caret"></b>
	            </a>
	            <ul class="dropdown-menu">
	               <li><a href="/redis/info">Server Info</a></li>
	               <li><a href="/redis/key/search">Search Keys</a></li>
	               <li><a href="/redis/pattern/list">Patters</a></li>
	               <li class="divider"></li>
	               <li><a href="/redis/flush">Flush</a></li>
	            </ul>
	         </li>
	      </ul>
	      <ul class="nav navbar-nav">
	         <li class="dropdown">
	            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Admin<b class="caret"></b>
	            </a>
	            <ul class="dropdown-menu">
	               <li><a href="/admin/user/users">Users</a></li>
	               <li><a href="/admin/user/create">Create User</a></li>
	            </ul>
	         </li>
		      <li>
	              <a href="/auth/logout" target="_top">退出登录</a>
	          </li>
	      </ul>
	   </div>
	</nav>
	<div style="height:20px" />
	<sitemesh:write property='body'/>
</body>
</html>
