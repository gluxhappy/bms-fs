<!DOCTYPE html>
<html>
<head>
<title>redis/patterns</title>
${viewres}
</head>
<body>
	<#if patterns?? && patterns?size gt 0>
		<table class="table table-hover">
		   <caption><a href="detail" class="pull-right btn btn-primary" style="margin-right:5em;">新建Pattern</a></caption>
		   <thead>
		      <tr>
		         <th style="width:5em;">Id</th>
		         <th>Pattern</th>
		         <th>简称</th>
		         <th style="width:5em;">数据库</th>
		         <th style="width:20em;">操作</th>
		      </tr>
		   </thead>
		   <tbody>
    		    <#assign idx = 0>
				<#list patterns as pattern> 
				    <#assign idx=idx+1>
                        <tr id="pattern_${idx}">
                        <td>${idx}</td>
                        <#escape x as x?html>
                        <td>${pattern.pattern}</td>
                        <td>${pattern.name}</td>
                        </#escape>
                        <td>${pattern.db}</td>
                        <td>
                        	<div class="btn-group" style="margin-left:3em">
                        		  <a href="/redis/key/search?pattern=${pattern.pattern?url}&db=${pattern.db}" class="btn btn-default">Search</a>
								  <a href="detail?id=${pattern.id}" class="btn btn-default">Detail</a>
                        		  <a href="#" class="btn btn-default" onclick="deletePattern('pattern_${idx}','${pattern.id}');return false;">删除</a>
							</div>
						</td>
                         </tr>
				</#list> 
		   </tbody>
		</table>
		<div style="width:100%;height:40px;" />
	<script>
		function deletePattern(trid,id){
	        var params={
	                id:id
	            };
	        jcall(
	        		"delete.do",
	        		params,
	        		function(data){
	        			if(data == 'SUCCESS'){
	        				$("#"+trid).remove();
	        			}
	        		},
	        		function(code,msg){
	        			alert("Error["+code+"]:"+msg);
	        		});
		};
	</script>
	<#else>
		<p class="text-center">没有任何Pattern</p>
	</#if>
</body>
</html>
