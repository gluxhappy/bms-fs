<!DOCTYPE html>
<html>
<head>
<title>Users</title>
${viewres}
</head>
<body>
       <#if users?? && users?size gt 0>
        <table class="table table-hover">
           <caption>共有用户${users?size}个</caption>
           <thead>
              <tr>
                 <th style="width:5em">Id</th>
                 <th>昵称</th>
                 <th>用户名</th>
                 <th style="width:6em">操作</th>
              </tr>
           </thead>
           <tbody>
                <#escape x as x?html>
                <#list users as user> 
                        <tr id="user_${user.id}">
                            <td>${user.id}</td>
                            <td >${user.alias}</td>
                            <td>${user.username}</td>
                            <td><a href="#" onclick="deleteUser('${user.id}');return false;">删除</a></td>
                        </tr>
                </#list> 
                </#escape>
           </tbody>
        </table>
        <div style="width:100%;height:40px;" />
    <script>
        function deleteUser(userId){
	        var params={
	                userId:userId
	            };
	        jcall(
	        		"delete.do",
	        		params,function(data){
	        			if(data == success){
	        				$("#"+trid).remove();
	        			}else{
	        				alert("删除失败");
	        			}
	        		},
	        		function(code,msg){
	        			alert("Error["+code+"]:"+msg);
	        		});
        };
    </script>
    <#else>
        <p class="text-align text-danger">没有用户</p>
    </#if>
</body>
</html>
