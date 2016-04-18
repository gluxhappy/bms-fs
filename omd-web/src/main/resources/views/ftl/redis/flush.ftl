<!DOCTYPE html>
<html>
<head>
	<title>Profile/Info</title>
</head>
<body>
	<div style="padding: 100px 100px 10px;">
	   <form role="form" action="flush"  method="POST" style="margin-left:30%;width:400px">
	      <div class="input-group">
	         <input type="text" class="form-control" placeholder="请输入flush" name="flush"/>
	         <span class="input-group-addon">@</span>
	         <select class="form-control" name="db">
	         	<option value="-1">ALL</option>
	         	<option value="3">3</option>
	         	<option value="4">4</option>
	         </select>
	      </div>
	      <input  style="margin-top:10px;margin-left:40%;width:100px" type="submit" value="Flush" class="btn btn-primary"/>
	   </form>
	</div>
	<#if db??>
		<p class="text-center text-danger"><#if db==-1>所有数据库<#else>数据库${db}</#if>已经被清空。</p>
	</#if>
</body>
</html>
