<!DOCTYPE html>
<html>
<head>
<title>Create User</title>
</head>
<body>
	<form class="form-horizontal" role="form" method="POST"
		action="create.do"
		style="margin-top: 40px; margin-left: 30%; width: 40em">
		<div class="form-group">
			<label for="username" class="col-sm-2 control-label">Username</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="username"
					name="username" placeholder="登录名">
			</div>
		</div>
		<div class="form-group">
			<label for="alias" class="col-sm-2 control-label">Alias</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="alias" name="alias"
					placeholder="请输入昵称">
			</div>
		</div>
		<div class="form-group">
			<label for="password" class="col-sm-2 control-label">Password</label>
			<div class="col-sm-10">
				<input type="password" class="form-control" id="password"
					name="password" placeholder="请输入密码">
			</div>
		</div>
		<#list roles as role>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="checkbox">
					<label>${role}</label> <input class="role" type="checkbox"
						name="${role}" />
				</div>
			</div>
		</div>
		</#list>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button type="submit" class="btn btn-default"
					onclick="createUser();return false;">提交</button>
			</div>
		</div>
	</form>
	<script>
    function createUser(){
        var roles=new Array();
        var index=0;
        $("input.role").each(
            function(index){
                if(this.checked==true){
                    roles[index]=this.name;
                    index++;
                }
         });
         params={
                username:$("#username")[0].value,
                password:$("#password")[0].value,
                alias:$("#alias")[0].value,
                roles:roles
            };
        jcall(
        		"create.do",
        		params,function(data){
        			alert(data);
        		},
        		function(code,msg){
        			alert("Error["+code+"]:"+msg);
        		});
    }
</script>
</body>
</html>
